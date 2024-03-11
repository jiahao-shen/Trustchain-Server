package com.trustchain.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.convert.OrganizationConvert;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.OrganizationInformation;
import com.trustchain.model.vo.OrganizationRegisterInformation;
import com.trustchain.model.vo.OrganizationSelectItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class OrganizationService {
    @Autowired
    private OrganizationMapper orgMapper;
    @Autowired
    private OrganizationRegisterMapper orgRegMapper;
    @Autowired
    private FabricService fabricService;
    @Autowired
    private MinioService minioService;
    @Autowired
    private EmailSerivce emailSerivce;

    private static final Logger logger = LogManager.getLogger(OrganizationService.class);

    /**
     * 获取机构选择列表
     *
     * @return
     */
    public List<Organization> selectList() {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .select(Organization::getId, Organization::getName);

        return orgMapper.selectListByQuery(query);
    }

    /**
     * 判断机构是否存在
     *
     * @param orgName
     * @param orgID
     * @return
     */
    public Boolean exist(String orgName, String orgID) {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .where(Organization::getName).eq(orgName)
                .and(Organization::getId).ne(orgID);

        Organization org = orgMapper.selectOneByQuery(query);

        return org != null;
    }

    /**
     * 机构注册申请
     *
     * @param orgReg
     * @return
     */
    public String registerApply(OrganizationRegister orgReg) {
        int count = orgRegMapper.insert(orgReg);

        if (count != 0) {
            emailSerivce.send(orgReg.getEmail(), "注册申请已提交",
                    "欢迎您注册数据资源可信共享平台, 您的注册申请号如下。<br>" +
                            "<h3>" + orgReg.getRegID() + "</h3>");
            return orgReg.getRegID();
        } else {
            return null;
        }
    }

    /**
     * 机构注册查询
     *
     * @param regIDs: 注册申请号
     * @return
     */
    public List<OrganizationRegister> registerApplySearch(List<String> regIDs) {
        return orgRegMapper.selectListByIds(regIDs);
    }

    /**
     * 获取注册申请列表
     *
     * @param id: 上级机构ID
     * @return
     */
    public List<OrganizationRegister> registerList(String id) {
        QueryWrapper query = QueryWrapper.create()
                .from(OrganizationRegister.class)
                .where(OrganizationRegister::getSuperiorID).eq(id);

        return orgRegMapper.selectListByQuery(query);
    }

    /**
     * @param regID
     * @return
     */
    public OrganizationRegister registerDetail(String regID) {
        return orgRegMapper.selectOneById(regID);
    }

    /**
     * 机构注册回复
     *
     * @return
     */
    public Boolean registerReply(String regID, RegisterStatus reply, String reason) {
        OrganizationRegister orgReg = orgRegMapper.selectOneById(regID);
        if (orgReg == null) {
            return false;
        }
        if (reply == RegisterStatus.ALLOW) {
            // 复制Logo
            String oldLogoPath = orgReg.getLogo();
            String newLogoPath = "organization/" + oldLogoPath.substring(oldLogoPath.lastIndexOf("/") + 1);
            minioService.copy(oldLogoPath, newLogoPath);

            // 复制文件
            String oldFilePath = orgReg.getFile();
            String newFilePath = "organization/" + oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
            minioService.copy(oldFilePath, newFilePath);

            // 插入新机构
            Organization org = OrganizationConvert.INSTANCE.toOrganization(orgReg);
            org.setLogo(newLogoPath);
            org.setFile(newFilePath);
            int count = orgMapper.insert(org);
            if (count != 0) {
                // 更新注册表状态
                orgReg.setId(org.getId());
                orgReg.setRegStatus(RegisterStatus.ALLOW);
                orgReg.setReplyTime(new Date());

                orgRegMapper.update(orgReg);
                return true;
            }
            return false;
        } else if (reply == RegisterStatus.REJECT) {
            // 更新注册表状态
            orgReg.setRegStatus(RegisterStatus.REJECT);
            orgReg.setReplyTime(new Date());
            orgReg.setReplyMessage(reason);

            orgRegMapper.update(orgReg);
            return true;
        }
        return false;
    }

    /**
     * 机构注册
     *
     * @param org
     * @return
     */
    public Boolean register(Organization org) {
        int count = orgMapper.insert(org);

        return count != 0;
    }


    public Organization informationDetail(String id, String version) {
        RelationManager.setMaxDepth(1);
        return orgMapper.selectOneWithRelationsById(id);
    }
}

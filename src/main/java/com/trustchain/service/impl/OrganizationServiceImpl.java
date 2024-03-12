package com.trustchain.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.OrganizationInformation;
import com.trustchain.service.EmailSerivce;
import com.trustchain.service.MinioService;
import com.trustchain.service.OrganizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationMapper orgMapper;
    @Autowired
    private OrganizationRegisterMapper orgRegMapper;
    //    @Autowired
//    private FabricService fabricService;
    @Autowired
    private MinioService minioService;
    @Autowired
    private EmailSerivce emailSerivce;

    private static final Logger logger = LogManager.getLogger(OrganizationServiceImpl.class);

    /**
     * 获取机构选择列表
     *
     * @return: 机构选择列表
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
     * @param orgName: 机构名称
     * @param orgId:   机构ID
     * @return: 是否存在
     */
    public boolean exist(String orgName, String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .where(Organization::getName).eq(orgName)
                .and(Organization::getId).ne(orgId);

        Organization org = orgMapper.selectOneByQuery(query);

        return org != null;
    }

    /**
     * 机构注册申请
     *
     * @param orgReg: 机构注册对象
     * @return: 注册申请号
     */
    public String registerApply(OrganizationRegister orgReg) {
        int count = orgRegMapper.insert(orgReg);

        if (count != 0) {
            emailSerivce.send(orgReg.getEmail(), "注册申请已提交",
                    "欢迎您注册数据资源可信共享平台, 您的注册申请号如下。<br>" +
                            "<h3>" + orgReg.getRegId() + "</h3>");
            return orgReg.getRegId();
        } else {
            return null;
        }
    }

    /**
     * 机构注册查询
     *
     * @param regIds: 注册申请号
     * @return: 注册申请列表
     */
    public List<OrganizationRegister> registerApplySearch(List<String> regIds) {
        return orgRegMapper.selectListByIds(regIds);
    }

    /**
     * 获取注册申请列表
     *
     * @param id: 上级机构ID
     * @return: 注册申请列表
     */
    public List<OrganizationRegister> registerList(String id) {
        QueryWrapper query = QueryWrapper.create()
                .from(OrganizationRegister.class)
                .where(OrganizationRegister::getSuperiorId).eq(id);

        return orgRegMapper.selectListByQuery(query);
    }

    /**
     * @param regId: 注册申请号
     * @return: 注册申请信息
     */
    public OrganizationRegister registerDetail(String regId) {
        return orgRegMapper.selectOneById(regId);
    }

    /**
     * 注册申请回复
     *
     * @param regId:  注册申请号
     * @param reply:  回复类型
     * @param reason: 回复理由
     * @return: 成功与否
     */
    public boolean registerReply(String regId, RegisterStatus reply, String reason) {
        OrganizationRegister orgReg = orgRegMapper.selectOneById(regId);
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
     * @param org: 机构
     * @return: 是否插入成功
     */
    public boolean register(Organization org) {
        int count = orgMapper.insert(org);

        return count != 0;
    }


    /**
     * 获取机构详情
     *
     * @param id:      机构ID
     * @param version: 版本号
     * @return: 机构信息
     */
    public Organization informationDetail(String orgId, String version) {
        // TODO: 使用版本信息
        RelationManager.setMaxDepth(1);
        return orgMapper.selectOneWithRelationsById(orgId);
    }

    /**
     * 获取机构下级机构列表
     *
     * @param orgId: 机构ID
     * @return
     */
    public List<Organization> subordinateList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .where(Organization::getSuperiorId).eq(orgId);

        return orgMapper.selectListByQuery(query);
    }

    /**
     * @param orgId: 机构ID
     * @return
     */
    public Organization subordinateDetail(String orgId) {
        return orgMapper.selectOneById(orgId);
    }
}

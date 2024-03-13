package com.trustchain.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.enums.OrganizationType;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
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
            emailSerivce.send(orgReg.getEmail(), "数据资源可信共享平台 注册申请",
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
                // TODO: 写入长安链

                // 更新注册表状态
                orgReg.setId(org.getId());
                orgReg.setRegStatus(RegisterStatus.ALLOW);
                orgReg.setReplyTime(new Date());

                orgRegMapper.update(orgReg);

                emailSerivce.send(org.getEmail(), "数据资源可信共享平台 注册成功",
                        "您的机构注册申请已通过, 请点击以下链接注册管理员账号。<br>" +
                                "<a>http://localhost:5173/registerApplySearch</a>");
                return true;
            }
            return false;
        } else if (reply == RegisterStatus.REJECT) {
            // 更新注册表状态
            orgReg.setRegStatus(RegisterStatus.REJECT);
            orgReg.setReplyTime(new Date());
            orgReg.setReplyReason(reason);

            orgRegMapper.update(orgReg);

            emailSerivce.send(orgReg.getEmail(), "数据资源可信共享平台 注册失败",
                    "您的机构注册申请未通过, 请点击以下链接查看详情。<br>" +
                            "<a>http://localhost:5173/registerApplySearch</a>");
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
     * @param orgId:   机构ID
     * @param version: 版本号
     * @return: 机构信息
     */
    public Organization informationDetail(String orgId, String version) {
        // TODO: 使用版本信息
        RelationManager.setMaxDepth(1);
        return orgMapper.selectOneWithRelationsById(orgId);
    }

    /**
     * @param orgId:        机构ID
     * @param logo:         机构Logo
     * @param name:         机构名称
     * @param type:         机构类型
     * @param creationTime: 创建时间
     * @param telephone:    机构电话
     * @param email:        机构邮箱
     * @param city:         机构城市
     * @param address:      机构地址
     * @param introduction: 机构介绍
     * @param file:         机构文件
     * @return
     */
    @Override
    public boolean informationUpdate(String orgId,
                                     String logo,
                                     String name,
                                     OrganizationType type,
                                     Date creationTime,
                                     String telephone,
                                     String email,
                                     String city,
                                     String address,
                                     String introduction,
                                     String file) {
        Organization org = orgMapper.selectOneById(orgId);
        if (org == null) {
            return false;
        } else {
            if (!minioService.isUrl(logo)) {
                String newLogoPath = "organization/" + logo.substring(logo.lastIndexOf("/") + 1);
                minioService.copy(logo, newLogoPath);
                org.setLogo(newLogoPath);
            }
            org.setName(name);
            org.setType(type);
            org.setCreationTime(creationTime);
            org.setTelephone(telephone);
            org.setEmail(email);
            org.setCity(city);
            org.setAddress(address);
            org.setIntroduction(introduction);
            if (!minioService.isUrl(file)) {
                String newFilePath = "organization/" + file.substring(file.lastIndexOf("/") + 1);
                minioService.copy(file, newFilePath);
                org.setFile(newFilePath);
            }
            orgMapper.update(org);
            // TODO: 写入长安链
            return true;
        }
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

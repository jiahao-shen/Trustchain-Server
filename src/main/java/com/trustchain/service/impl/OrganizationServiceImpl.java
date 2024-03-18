package com.trustchain.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.model.enums.ApplyStatus;
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
    @Autowired
    private MinioService minioService;
    @Autowired
    private EmailSerivce emailSerivce;

    private static final Logger logger = LogManager.getLogger(OrganizationServiceImpl.class);

    @Override
    public List<Organization> selectList() {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .select(Organization::getId, Organization::getName);

        return orgMapper.selectListByQuery(query);
    }

    @Override
    public boolean exist(String orgName, String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .where(Organization::getName).eq(orgName)
                .and(Organization::getId).ne(orgId);

        Organization org = orgMapper.selectOneByQuery(query);

        return org != null;
    }

    @Override
    public String registerApply(OrganizationRegister orgReg) {
        int count = orgRegMapper.insert(orgReg);

        if (count != 0) {
            emailSerivce.send(orgReg.getEmail(), "数据资源可信共享平台 注册申请",
                    "欢迎您注册数据资源可信共享平台, 您的注册申请号如下。<br>" +
                            "<h3>" + orgReg.getApplyId() + "</h3>");
            return orgReg.getApplyId();
        } else {
            return null;
        }
    }

    @Override
    public List<OrganizationRegister> registerApplySearch(List<String> applyIds) {
        return orgRegMapper.selectListByIds(applyIds);
    }

    @Override
    public List<OrganizationRegister> registerList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(OrganizationRegister.class)
                .where(OrganizationRegister::getSuperiorId).eq(orgId);

        return orgRegMapper.selectListByQuery(query);
    }

    @Override
    public OrganizationRegister registerDetail(String applyId) {
        return orgRegMapper.selectOneById(applyId);
    }

    @Override
    public boolean registerReply(String applyId, ApplyStatus reply, String reason) {
        OrganizationRegister orgReg = orgRegMapper.selectOneById(applyId);
        if (orgReg == null) {
            return false;
        }
        if (reply == ApplyStatus.ALLOW) {
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
                orgReg.setApplyStatus(ApplyStatus.ALLOW);
                orgReg.setReplyTime(new Date());

                orgRegMapper.update(orgReg);

                emailSerivce.send(org.getEmail(), "数据资源可信共享平台 注册成功",
                        "您的机构注册申请已通过, 请点击以下链接注册管理员账号。<br>" +
                                "<a>http://localhost:5173/registerApplySearch</a>");
                return true;
            }
            return false;
        } else if (reply == ApplyStatus.REJECT) {
            // 更新注册表状态
            orgReg.setApplyStatus(ApplyStatus.REJECT);
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

    @Override
    public boolean register(Organization org) {
        int count = orgMapper.insert(org);

        return count != 0;
    }

    @Override
    public Organization informationDetail(String orgId, String version) {
        // TODO: 对接长安链
        RelationManager.setMaxDepth(1);
        return orgMapper.selectOneWithRelationsById(orgId);
    }

    @Override
    public Organization informationUpdate(Organization org) {
        // TODO: 对接长安链
        String logo = org.getLogo();
        if (!minioService.isUrl(logo)) {
            String newLogoPath = "user/" + logo.substring(logo.lastIndexOf("/") + 1);
            minioService.copy(logo, newLogoPath);
            org.setLogo(newLogoPath);
        } else {
            org.setLogo(null);
        }
        String file = org.getFile();
        if (!minioService.isUrl(file)) {
            String newFilePath = "organization/" + file.substring(file.lastIndexOf("/") + 1);
            minioService.copy(file, newFilePath);
            org.setFile(newFilePath);
        } else {
            org.setFile(null);
        }
        orgMapper.update(org, true);

        RelationManager.setMaxDepth(1);
        return orgMapper.selectOneWithRelationsById(org.getId());
    }

    @Override
    public List<Organization> informationHistory(String orgId) {
        // TODO: 对接长安链
        return null;
    }

    @Override
    public boolean informationRollback(String orgId, String version) {
        // TODO: 对接长安链
        return false;
    }

    @Override
    public List<Organization> subordinateList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class)
                .where(Organization::getSuperiorId).eq(orgId);

        return orgMapper.selectListByQuery(query);
    }

    @Override
    public Organization subordinateDetail(String orgId) {
        return orgMapper.selectOneById(orgId);
    }
}

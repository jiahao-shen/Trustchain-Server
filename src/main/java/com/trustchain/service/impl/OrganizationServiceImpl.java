package com.trustchain.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.service.EmailSerivce;
import com.trustchain.service.MinioService;
import com.trustchain.service.OrganizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.trustchain.model.entity.table.OrganizationRegisterTableDef.ORGANIZATION_REGISTER;
import static com.trustchain.model.entity.table.OrganizationTableDef.ORGANIZATION;


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
                .select(ORGANIZATION.ID, ORGANIZATION.NAME)
                .from(ORGANIZATION)
                .orderBy(ORGANIZATION.NAME, true);

        return orgMapper.selectListByQuery(query);
    }

    @Override
    public boolean exist(String orgName, String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(ORGANIZATION)
                .where(ORGANIZATION.NAME.eq(orgName))
                .and(ORGANIZATION.ID.ne(orgId));

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
            // 将Logo移动至新目录
            String oldLogoPath = orgReg.getLogo();
            String newLogoPath = "organization_register/" + orgReg.getId() + "/" + oldLogoPath.substring(oldLogoPath.lastIndexOf("/") + 1);
            minioService.copy(oldLogoPath, newLogoPath);

            // 将File移动至新目录
            String oldFilePath = orgReg.getFile();
            String newFilePath = "organization_register/" + orgReg.getId() + "/" + oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
            minioService.copy(oldFilePath, newFilePath);

            orgReg.setLogo(newLogoPath);
            orgReg.setFile(newFilePath);
            orgRegMapper.update(orgReg, true);

            return orgReg.getApplyId();
        } else {
            return null;
        }
    }

    @Override
    public List<OrganizationRegister> registerApplyList(List<String> applyIds) {
        QueryWrapper query = QueryWrapper.create()
                .select(ORGANIZATION_REGISTER.APPLY_ID,
                        ORGANIZATION_REGISTER.NAME,
                        ORGANIZATION_REGISTER.TYPE,
                        ORGANIZATION_REGISTER.APPLY_STATUS,
                        ORGANIZATION_REGISTER.APPLY_TIME,
                        ORGANIZATION_REGISTER.REPLY_TIME)
                .from(ORGANIZATION_REGISTER)
                .where(ORGANIZATION_REGISTER.APPLY_ID.in(applyIds))
                .orderBy(ORGANIZATION_REGISTER.APPLY_TIME, false);

        return orgRegMapper.selectListByQuery(query);
    }

    @Override
    public Page<OrganizationRegister> registerApplyList(List<String> applyIds,
                                                        Integer pageNumber,
                                                        Integer pageSize,
                                                        Map<String, List<String>> filter,
                                                        Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .select(ORGANIZATION_REGISTER.APPLY_ID,
                        ORGANIZATION_REGISTER.NAME,
                        ORGANIZATION_REGISTER.TYPE,
                        ORGANIZATION_REGISTER.APPLY_STATUS,
                        ORGANIZATION_REGISTER.APPLY_TIME,
                        ORGANIZATION_REGISTER.REPLY_TIME)
                .from(ORGANIZATION_REGISTER)
                .where(ORGANIZATION_REGISTER.APPLY_ID.in(applyIds));

        filter.forEach((key, value) -> {
        });

        if (sort.isEmpty()) {
            query.orderBy(ORGANIZATION_REGISTER.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
            });
        }

        return orgRegMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public OrganizationRegister registerApplyDetail(String applyId) {
        RelationManager.setMaxDepth(1);
        return orgRegMapper.selectOneById(applyId);
    }

    @Override
    public List<OrganizationRegister> registerApprovalList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .select(ORGANIZATION_REGISTER.APPLY_ID,
                        ORGANIZATION_REGISTER.NAME,
                        ORGANIZATION_REGISTER.TYPE,
                        ORGANIZATION_REGISTER.APPLY_STATUS,
                        ORGANIZATION_REGISTER.APPLY_TIME)
                .from(ORGANIZATION_REGISTER)
                .where(ORGANIZATION_REGISTER.SUPERIOR_ID.eq(orgId))
                .orderBy(ORGANIZATION_REGISTER.APPLY_TIME, false);

        return orgRegMapper.selectListByQuery(query);
    }

    @Override
    public Page<OrganizationRegister> registerApprovalList(String orgId,
                                                           Integer pageNumber,
                                                           Integer pageSize,
                                                           Map<String, List<String>> filter,
                                                           Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(ORGANIZATION_REGISTER.APPLY_ID,
                        ORGANIZATION_REGISTER.NAME,
                        ORGANIZATION_REGISTER.TYPE,
                        ORGANIZATION_REGISTER.APPLY_STATUS,
                        ORGANIZATION_REGISTER.APPLY_TIME)
                .from(ORGANIZATION_REGISTER)
                .where(ORGANIZATION_REGISTER.SUPERIOR_ID.eq(orgId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "type": {
                    query.where(ORGANIZATION_REGISTER.TYPE.in(value.stream().map(OrganizationType::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "applyStatus": {
                    query.where(ORGANIZATION_REGISTER.APPLY_STATUS.in(value.stream().map(ApplyStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(ORGANIZATION_REGISTER.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "applyTime": {
                        query.orderBy(ORGANIZATION_REGISTER.APPLY_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return orgRegMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public OrganizationRegister registerApprovalDetail(String applyId) {
        return orgRegMapper.selectOneById(applyId);
    }

    @Override
    @Transactional
    public void registerReply(String applyId, ApplyStatus reply, String reason) {
        OrganizationRegister orgReg = orgRegMapper.selectOneById(applyId);
        if (orgReg == null) {
            throw new RuntimeException("机构注册申请不存在");
        }
        if (reply == ApplyStatus.ALLOW) {
            // 创建新机构
            Organization org = OrganizationConvert.INSTANCE.toOrganization(orgReg);
            org.setId(UUID.randomUUID().toString().replaceAll("-", ""));

            // 复制Logo
            String oldLogoPath = orgReg.getLogo();
            String newLogoPath = "organization/" + org.getId() + "/" + oldLogoPath.substring(oldLogoPath.lastIndexOf("/") + 1);
            minioService.copy(oldLogoPath, newLogoPath);

            // 复制文件
            String oldFilePath = orgReg.getFile();
            String newFilePath = "organization/" + org.getId() + "/" + oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
            minioService.copy(oldFilePath, newFilePath);

            org.setLogo(newLogoPath);
            org.setFile(newFilePath);

            orgMapper.insert(org);
            // TODO: 写入长安链

            // 更新注册表状态
            orgReg.setId(org.getId());
            orgReg.setApplyStatus(ApplyStatus.ALLOW);
            orgReg.setReplyTime(new Date());
            orgRegMapper.update(orgReg, true);

            emailSerivce.send(org.getEmail(), "数据资源可信共享平台 注册成功",
                    "您的机构注册申请已通过, 请点击以下链接注册管理员账号。<br>" +
                            "<a>http://localhost:5173/registerApplySearch</a>");
        } else if (reply == ApplyStatus.REJECT) {
            // 更新注册表状态
            orgReg.setApplyStatus(ApplyStatus.REJECT);
            orgReg.setReplyTime(new Date());
            orgReg.setReplyReason(reason);

            orgRegMapper.update(orgReg);

            emailSerivce.send(orgReg.getEmail(), "数据资源可信共享平台 注册失败",
                    "您的机构注册申请未通过, 请点击以下链接查看详情。<br>" +
                            "<a>http://localhost:5173/registerApplySearch</a>");
        }
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
            String newLogoPath = "organization/" + org.getId() + "/" + logo.substring(logo.lastIndexOf("/") + 1);
            minioService.copy(logo, newLogoPath);
            org.setLogo(newLogoPath);
        } else {
            org.setLogo(null);
        }
        String file = org.getFile();
        if (!minioService.isUrl(file)) {
            String newFilePath = "organization/" + org.getId() + "/" + file.substring(file.lastIndexOf("/") + 1);
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
    public Page<Organization> informationHistory(String orgId, Integer pageNumber, Integer pageSize, Map<String, List<String>> filter, Map<String, String> sort) {
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
                .select(ORGANIZATION.ID,
                        ORGANIZATION.NAME,
                        ORGANIZATION.TYPE,
                        ORGANIZATION.CREATION_TIME,
                        ORGANIZATION.REGISTRATION_TIME)
                .from(ORGANIZATION)
                .where(ORGANIZATION.SUPERIOR_ID.eq(orgId))
                .orderBy(ORGANIZATION.REGISTRATION_TIME, false);

        return orgMapper.selectListByQuery(query);
    }

    @Override
    public Page<Organization> subordinateList(String orgId,
                                              Integer pageNumber,
                                              Integer pageSize,
                                              Map<String, List<String>> filter,
                                              Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(ORGANIZATION.ID,
                        ORGANIZATION.NAME,
                        ORGANIZATION.TYPE,
                        ORGANIZATION.CREATION_TIME,
                        ORGANIZATION.REGISTRATION_TIME)
                .from(ORGANIZATION)
                .where(ORGANIZATION.SUPERIOR_ID.eq(orgId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "type": {
                    query.where(ORGANIZATION.TYPE.in(value.stream().map(OrganizationType::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(ORGANIZATION.REGISTRATION_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "creationTime": {
                        query.orderBy(ORGANIZATION.CREATION_TIME, "ascending".equals(value));
                        break;
                    }
                    case "registrationTime": {
                        query.orderBy(ORGANIZATION.REGISTRATION_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return orgMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public Organization subordinateDetail(String orgId) {
        return orgMapper.selectOneById(orgId);
    }
}

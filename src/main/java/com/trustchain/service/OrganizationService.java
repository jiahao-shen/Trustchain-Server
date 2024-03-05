package com.trustchain.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.OrganizationSelectItemVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationMapper orgMapper;
    @Autowired
    private OrganizationRegisterMapper orgRegMapper;
    @Autowired
    private MinioService minioService;
    @Autowired
    private FabricService fabricService;

    private static final Logger logger = LogManager.getLogger(OrganizationService.class);

    public List<OrganizationSelectItemVO> selectList() {
        QueryWrapper query = QueryWrapper.create()
                .select(Organization::getId, Organization::getName)
                .from(Organization.class);

        return orgMapper.selectListByQueryAs(query, OrganizationSelectItemVO.class);
    }

    public String registerApply(OrganizationRegister orgReg) {
        int count = orgRegMapper.insert(orgReg);

        if (count != 0) {
            return orgReg.getRegID();
        } else {
            return null;
        }
    }

    public String registerReply() {
        return null;
    }

    public Boolean register(Organization org) {
        int count = orgMapper.insert(org);

        return count != 0;
    }

    public Boolean exist(String prgName) {
        return true;
    }
}
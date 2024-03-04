package com.trustchain.service;

import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.OrganizationRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String organizationRegisterApply(OrganizationRegister orgReg) {
        int count = orgRegMapper.insert(orgReg);

        if (count != 0) {
            return orgReg.getSerialNumber();
        } else {
            return null;
        }
    }
}

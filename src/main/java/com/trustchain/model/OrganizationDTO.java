package com.trustchain.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trustchain.mapper.OrganizationMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class OrganizationDTO extends Organization{

    List<Organization> childOrganizations = new ArrayList<>();
    Stack<Organization> st = new Stack<>();
    public List<Organization> getChildOrganizations(OrganizationMapper organizationMapper, Long id){
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getId, id);
        Organization organization = organizationMapper.selectOne(queryWrapper);
        childOrganizations.add(organization);
        findChildOrganizations(organizationMapper, id);
        return childOrganizations;
    }
    private void findChildOrganizations(OrganizationMapper organizationMapper, Long id) {
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getSuperior, id);
        List<Organization> child = organizationMapper.selectList(queryWrapper);
        if(child != null) {
            Iterator<Organization> iterator = child.iterator();
            while(iterator.hasNext()){
                st.push(iterator.next());
            }
            while(!st.empty()){
                Organization childOrg = st.pop();
                childOrganizations.add(childOrg);
                findChildOrganizations(organizationMapper, childOrg.getId());
            }
        }
    }

}

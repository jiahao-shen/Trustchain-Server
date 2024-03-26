package com.trustchain;

import com.mybatisflex.core.mask.MaskManager;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.mapper.ApiMapper;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.UserMapper;
import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiRegister;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.User;
import com.trustchain.model.enums.ApiVisible;
import com.trustchain.model.enums.OrganizationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.trustchain.model.entity.table.ApiTableDef.API;
import static com.trustchain.model.entity.table.OrganizationTableDef.ORGANIZATION;
import static com.trustchain.model.entity.table.UserTableDef.USER;

@SpringBootTest
public class MyBatisFlexTest {
    @Autowired
    private OrganizationMapper orgMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ApiMapper apiMapper;
    private static final Logger logger = LogManager.getLogger(MyBatisFlexTest.class);

    @Test
    void testPagination() {
        List<String> filter = Arrays.asList("PUBLIC", "EDUCATION");
        QueryWrapper query = QueryWrapper.create()
                .from(Organization.class).where(Organization::getType).in(filter.stream().map(OrganizationType::valueOf).collect(Collectors.toList()));

//        logger.info(orgMapper.selectOneByQuery(query));

        Page<Organization> result = orgMapper.paginate(5, 10, query);
        logger.info(result.getRecords());
        logger.info(result.getPageNumber());
        logger.info(result.getPageSize());
        logger.info(result.getTotalPage());
        logger.info(result.getTotalRow());
    }


    @Test
    void testLogicalDelete() {
//        userMapper.deleteById("3759462c2a7449068052e77c3c014f6a");

        List<User> users = userMapper.selectAll();

        users.forEach(logger::info);
    }

    @Test
    void testIgnoreNulls() {
        User user = new User();
        user.setId("73b3f489688c417291213691f96bf149");
        user.setPassword("1234567");
        user.setEmail("7654321");
        logger.info(user);
        userMapper.update(user, true);
    }

    @Test
    void testMask() {
        ApiRegister apiReg = new ApiRegister();
        apiReg.setUrl("127.0.0.1:8080");
        logger.info(apiReg);

        MaskManager.registerMaskProcessor("url", data -> {
            return "************************";
        });

        logger.info(MaskManager.mask("url", apiReg));
    }

    @Test
    void testRelation() {
        QueryWrapper query = QueryWrapper.create()
                .select(USER.ALL_COLUMNS)
                .select(ORGANIZATION.NAME)
                .from(USER);
        RelationManager.setMaxDepth(1);
        RelationManager.addExtraConditionParam("organization.id", "a0c7b2b7e3564e70b6f4f80ef42b9b24");
        logger.info(userMapper.selectListWithRelationsByQuery(query));
    }

    @Test
    void testColumnAlias() {
        QueryWrapper query = QueryWrapper.create()
                .select(API.ID,
                        API.NAME,
                        API.METHOD,
                        API.REGISTRATION_TIME,
                        USER.USERNAME,
                        ORGANIZATION.NAME,
                        ORGANIZATION.TYPE
                )
                .from(API)
                .leftJoin(USER)
                .on(USER.ID.eq(API.USER_ID))
                .leftJoin(ORGANIZATION)
                .on(ORGANIZATION.ID.eq(USER.ORGANIZATION_ID)).limit(10);

        List<Api> apis = apiMapper.selectListByQuery(query);
        apis.forEach(logger::info);
    }

}

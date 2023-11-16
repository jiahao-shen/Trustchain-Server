package com.trustchain.controller.statics;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.protobuf.Api;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.trustchain.configuration.RedisConfig;
import com.trustchain.controller.organization.OrganizationController;
import com.trustchain.mapper.*;
import com.trustchain.model.*;
import com.trustchain.service.EmailService;
import com.trustchain.service.FabricService;
import com.trustchain.service.MinioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@CrossOrigin
@RestController
public class StaticsController {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private APIRegisterMapper apiRegisterMapper;

    @Autowired
    private APIMapper apiMapper;

    @Autowired
    private APIInvokeMapper apiInvokeMapper;

    @Autowired
    private APIInvokeLogMapper apiInvokeLogMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrganizationRegisterMapper organizationRegisterMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private FabricService fabricService;

    @Autowired
    private RedisConfig redisConfig = new RedisConfig();

    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    RedisConnectionFactory redisConnectionFactory = redisConfig.initConnectionFactory();

    /**
     * the sum number of the users
     */
    @GetMapping("/statics/usersum")
    public ResponseEntity<Object> getUserSum(HttpSession session){
        User login = (User)session.getAttribute("login");
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
        Long orgId = login.getOrganization();
        OrganizationDTO organizationDTO = new OrganizationDTO();
        List<Organization> childOrg = organizationDTO.getChildOrganizations(organizationMapper, orgId);
        Iterator<Organization> iterator = childOrg.iterator();
        int childSum = 0;
        while(iterator.hasNext()){
            Organization org = iterator.next();
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            int num = userMapper.userSum(org.getId(),queryWrapper);
            childSum += num;
        }
        return ResponseEntity.status(HttpStatus.OK).body(childSum);
    }

    /**
     * number of new users in one day
     */
    @PostMapping("/statics/usersum/oneday")
    public ResponseEntity<Object> getUserSumOneDay(@RequestBody JSONObject request, HttpSession session){
        User login = (User)session.getAttribute("login");
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
        OrganizationDTO organizationDTO = new OrganizationDTO();
        List<Organization> childOrg = organizationDTO.getChildOrganizations(organizationMapper, login.getOrganization());
        Iterator<Organization> iterator = childOrg.iterator();
        String dateStr = request.getString("date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int sum = 0;
        try{
            Date startDate = format.parse(dateStr.toString());
            Instant instant = startDate.toInstant();
            Instant nextDay = instant.plus(1, ChronoUnit.DAYS);
            Date endDate = Date.from(nextDay);
            while(iterator.hasNext()){
                Organization org = iterator.next();
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                int nums = userMapper.userSumOneDay(startDate, endDate, org.getId(), queryWrapper);
                sum += nums;
            }
            return ResponseEntity.status(HttpStatus.OK).body(sum);
        }catch (ParseException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("parse false");
        }
    }

    /**
     * the sum number of the apis
     */
    @GetMapping("/statics/apisum")
    public ResponseEntity<Object> getApiSum(){
        LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
        int nums = apiMapper.getApiSum(queryWrapper);
        return ResponseEntity.status(HttpStatus.OK).body(nums);
   }

    /**
     * number of new api in one day
     */
   @PostMapping("statics/apisum/oneday")
   public ResponseEntity<Object> getApiSumOneDay(@RequestBody JSONObject request){
       String dateStr = request.getString("date");
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       try{
           Date startDate = format.parse(dateStr);
           Instant instant = startDate.toInstant();
           Instant nextDay = instant.plus(1, ChronoUnit.DAYS);
           Date endDate = Date.from(nextDay);
           LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
           int nums = apiMapper.getApiSumOneDay(startDate, endDate, queryWrapper);
           return ResponseEntity.status(HttpStatus.OK).body(nums);
       }catch (ParseException e){
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("parse false");
       }
   }

    /**
     * all api invoke times
     */
    @GetMapping("/statics/apiinvokesum")
    public ResponseEntity<Object> getAllApiInvokeTimes(HttpSession session){
        User login = (User)session.getAttribute("login");
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
        OrganizationDTO organizationDTO = new OrganizationDTO();
        List<Organization> childOrg = organizationDTO.getChildOrganizations(organizationMapper, login.getOrganization());
        Iterator<Organization> iterator = childOrg.iterator();
        int sum = 0;
        while(iterator.hasNext()){
            Organization org = iterator.next();
            LambdaQueryWrapper<APIInvokeLog> queryWrapper = new LambdaQueryWrapper<>();
            int nums = apiInvokeLogMapper.apiInvokeSum(org.getId(),queryWrapper);
            sum += nums;
        }
        return ResponseEntity.status(HttpStatus.OK).body(sum);
    }

    /**
     * all api invoke times in one day
     */
    @PostMapping("/statics/apiinvokesum/oneday")
    public ResponseEntity<Object> getApiInvokeTimesOneDay(@RequestBody JSONObject request, HttpSession session){
        User login = (User)session.getAttribute("login");
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
        String dateStr = request.getString("date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        OrganizationDTO organizationDTO = new OrganizationDTO();
        List<Organization> childOrg = organizationDTO.getChildOrganizations(organizationMapper, login.getOrganization());
        Iterator<Organization> iterator = childOrg.iterator();
        int sum = 0;
        try{
            Date startDate = format.parse(dateStr);
            Instant instant = startDate.toInstant();
            Instant nextDay = instant.plus(1, ChronoUnit.DAYS);
            Date endDate = Date.from(nextDay);
            while(iterator.hasNext()){
                Organization org = iterator.next();
                LambdaQueryWrapper<APIInvokeLog> queryWrapper = new LambdaQueryWrapper<>();
                int nums = apiInvokeLogMapper.apiInvokeSumOneDay(startDate, endDate, org.getId(), queryWrapper);
                sum += nums;
            }
            return ResponseEntity.status(HttpStatus.OK).body(sum);
        }catch (ParseException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("parse false");
        }
    }

    /**
     * percent of each api type
     */
    @GetMapping("/statics/apipercent")
    public ResponseEntity<Object> getApiPercent(){
        LambdaQueryWrapper<API> queryWrapperApiSum = new LambdaQueryWrapper<>();
        int sumcount = apiMapper.getApiSum(queryWrapperApiSum);
        LambdaQueryWrapper<API> queryWrapperApi = new LambdaQueryWrapper<>();
        HashMap<String, Integer> apiPercent = new HashMap<String, Integer>();
        for(int i=1; i<=8; i++){
            int num = apiMapper.getApiOneTypeNum(i, queryWrapperApi);
            //float percent = (float)num / (float)sumcount;
            switch (i){
                case 1:
                    apiPercent.put("GET", num);
                    break;
                case 2:
                    apiPercent.put("POST", num);
                    break;
                case 3:
                    apiPercent.put("PUT", num);
                    break;
                case 4:
                    apiPercent.put("DELETE", num);
                    break;
                case 5:
                    apiPercent.put("TRACE", num);
                    break;
                case 6:
                    apiPercent.put("CONNECT", num);
                    break;
                case 7:
                    apiPercent.put("HEAD", num);
                    break;
                case 8:
                    apiPercent.put("OPTIONS", num);
                    break;
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiPercent);
    }

    /**
     * api num of one organization
     */
    @PostMapping("/statics/apisum/oneorganization")
    public ResponseEntity<Object> getApiSumOneOrganization(@RequestBody JSONObject request){
        Long organization = request.getLong("organization");
        LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
        int num = apiMapper.getApiSumOneOrganizaiton(organization, queryWrapper);
        return ResponseEntity.status(HttpStatus.OK).body(num);
    }

}

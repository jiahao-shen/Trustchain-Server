package com.trustchain.service;

import com.trustchain.util.Notice;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpService {

    //get request without param
//    public Notice restTemplateGet(String url){
//        RestTemplate restTemplate = new RestTemplate();
//        //Notice notice = restTemplate.getForObject(url, Notice.class);
//        Notice notice = restTemplate.getForObject(url, Notice.class);
//        return notice;
//    }
    public String restTemplateGet(String url){
        RestTemplate restTemplate = new RestTemplate();
        //Notice notice = restTemplate.getForObject(url, Notice.class);
        String res = restTemplate.getForObject(url, String.class);
        return res;
    }

    //get request with param
    public Notice restTemplateGetwithparam(String url, Map<String, String> map){
        RestTemplate restTemplate = new RestTemplate();
        Notice notice = restTemplate.getForObject(url, Notice.class);
        return notice;
    }

    public String rtPostObject(String url, MultiValueMap<String, String> map){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }


}

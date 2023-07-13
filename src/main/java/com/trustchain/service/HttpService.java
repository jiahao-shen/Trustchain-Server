package com.trustchain.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.trustchain.util.Notice;
import net.sf.json.JSONArray;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.springframework.http.HttpEntity;
import org.apache.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.awt.X11.XSystemTrayPeer;

import java.io.IOException;
import java.util.*;

@Service
public class HttpService {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    //get without params
    public String sendGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    //get with params
    public String sendGetParams(String url, Map<String, String> header) {
        HttpGet httpGet = new HttpGet(url);

        for(Map.Entry entry:header.entrySet()){
            httpGet.setHeader(entry.getKey().toString(), entry.getValue().toString());
        }

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e){
            e.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity!= null) {
                result = EntityUtils.toString(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                response.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    //post without params
    public String sendPost(String url){
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpPost);
        }catch (IOException e){
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        try{
            result = EntityUtils.toString(entity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //post with params
    public String sendPostParams(String url, Map<String, String> map) {
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry entry: map.entrySet()) {
            jsonObject.addProperty(entry.getKey().toString(),entry.getValue().toString());
        }

        StringEntity entity = new StringEntity(jsonObject.toString(), Consts.UTF_8);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



}

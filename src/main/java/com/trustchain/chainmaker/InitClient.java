package com.trustchain.chainmaker;

import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainManager;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitClient {
    static String SDK_CONFIG = "sdk_config.yml";
    static String SDK_CONFIG_TEST = "sdk_config_test.yml";
    static String ENV = "TEST";
    static ChainManager chainManager;
    static User user;

    @Bean
    public ChainClient inItChainClient() throws Exception {
        ChainClient chainClient;
        Yaml yaml = new Yaml();
        InputStream in;
        if (GlobalVariable.ENV.equals("TEST") && ENV.equals("TEST")){
            in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG_TEST);
        } else {
            in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);
        }
        SdkConfig sdkConfig;
        sdkConfig = yaml.loadAs(in, SdkConfig.class);
        assert in != null;
        in.close();

        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
            List<byte[]> tlsCaCertList = new ArrayList<>();
            if (nodeConfig.getTrustRootPaths() != null) {
                for (String rootPath : nodeConfig.getTrustRootPaths()) {
                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                    for (String filePath : filePathList) {
                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                    }
                }
            }
            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);
            nodeConfig.setTrustRootBytes(tlsCaCerts);
        }

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());

        if (chainClient == null) {
            System.out.println("%%%%%%%%%%%%%%%%%%try to create client%%%%%%%%%%%%%%%%%%");
            chainClient = chainManager.createChainClient(sdkConfig);
            if (chainClient==null) {
                System.out.println("%%%%%%%%%%%%%%%%%%create client failed%%%%%%%%%%%%%%%%%%");
            }
        }
        return chainClient;
//        user = new User(ORG_ID1, FileUtils.getResourceFileBytes(CLIENT1_KEY_PATH),
//                FileUtils.getResourceFileBytes(CLIENT1_CERT_PATH),
//                FileUtils.getResourceFileBytes(CLIENT1_TLS_KEY_PATH),
//                FileUtils.getResourceFileBytes(CLIENT1_TLS_CERT_PATH), false);
    }
}

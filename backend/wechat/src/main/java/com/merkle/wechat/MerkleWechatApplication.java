package com.merkle.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring4all.swagger.EnableSwagger2Doc;

/**
 *
 * @author Terry Yao
 * @author Danny Wang
 *
 */
@EnableSwagger2Doc
@SpringBootApplication
public class MerkleWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerkleWechatApplication.class, args);
    }
}

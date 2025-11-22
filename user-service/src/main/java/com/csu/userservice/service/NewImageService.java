package com.csu.userservice.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class NewImageService {

    // ========= 从 application.yml 读取 OSS 配置 =========
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;

    @Value("${oss.endpoint}")
    public void setEndpoint(String e) {
        NewImageService.endpoint = e;
    }

    @Value("${oss.accessKeyId}")
    public void setAccessKeyId(String id) {
        NewImageService.accessKeyId = id;
    }

    @Value("${oss.accessKeySecret}")
    public void setAccessKeySecret(String secret) {
        NewImageService.accessKeySecret = secret;
    }

    @Value("${oss.bucketName}")
    public void setBucketName(String bucket) {
        NewImageService.bucketName = bucket;
    }

    // ========= 上传头像并返回图片 URL =========
    public static String uploadImage(byte[] fileBytes) {
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 生成唯一文件名
            String fileName = "avatar/" + UUID.randomUUID().toString().replace("-", "") + ".jpg";

            // 上传
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(fileBytes));

            // 设置 10 年过期时间
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000L * 24L * 365L * 10);

            // 获取签名 URL
            URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);

            ossClient.shutdown();

            return url != null ? url.toString() : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

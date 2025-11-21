package com.csu.productservice.service;

import com.aliyun.oss.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
public class NewImageService {
    private static final Dotenv dotenv = Dotenv.configure().load();

    // 从 .env 文件读取配置
    private static final String OSS_AK = dotenv.get("ALIBABA_CLOUD_ACCESS_KEY_ID");
    private static final String OSS_SK = dotenv.get("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
    private static final String OSS_BUCKET_NAME = dotenv.get("ALIBABA_CLOUD_BUCKET_NAME", "csu-ssm-dessert");
    private static final String OSS_ENDPOINT = dotenv.get("ALIBABA_CLOUD_ENDPOINT", "https://oss-cn-wuhan-lr.aliyuncs.com");

    // 添加配置验证
    static {
        if (OSS_AK == null || OSS_SK == null) {
            throw new RuntimeException("阿里云凭据未配置，请创建 .env 文件并设置 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET");
        }
    }

    // 上传图片并返回图片URL
    public static String uploadImage(byte[] fileBytes) {
        String result = null;
        OSS ossClient = new OSSClientBuilder().build(OSS_ENDPOINT, OSS_AK, OSS_SK);

        try {
            // 生成唯一的文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            // 上传字节数组到 OSS
            ossClient.putObject(OSS_BUCKET_NAME, uuid, new ByteArrayInputStream(fileBytes));

            // 设置URL过期时间为10年
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000L * 24L * 365L * 10L);
            URL url = ossClient.generatePresignedUrl(OSS_BUCKET_NAME, uuid, expiration);
            if (url != null) {
                result = url.toString();
            }
        } catch (OSSException oe) {
            StringBuilder sb = new StringBuilder();
            sb.append("Caught an OSSException, which means your request made it to OSS, ")
                    .append("but was rejected with an error response for some reason.");
            sb.append("Error Message:").append(oe.getErrorMessage());
            sb.append("Error Code:").append(oe.getErrorCode());
            sb.append("Request ID:").append(oe.getRequestId());
            sb.append("Host ID:").append(oe.getHostId());
            System.err.println(sb.toString());
        } catch (ClientException ce) {
            StringBuilder sb = new StringBuilder();
            sb.append("Caught an ClientException, which means the client encountered ")
                    .append("a serious internal problem while trying to communicate with OSS, ")
                    .append("such as not being able to access the network.");
            sb.append("Error Message:" + ce.getMessage());
            System.err.println(sb.toString());
            ce.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }
}
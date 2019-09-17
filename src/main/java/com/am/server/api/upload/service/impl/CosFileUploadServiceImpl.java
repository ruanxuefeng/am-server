package com.am.server.api.upload.service.impl;

import com.am.server.api.upload.config.TencentConfig;
import com.am.server.api.upload.exception.UploadFileException;
import com.am.server.api.upload.service.FileUploadService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * 腾讯云文件存储
 * @author 阮雪峰
 * @date 2018/8/24 15:50
 */
@Slf4j
public class CosFileUploadServiceImpl implements FileUploadService {

    @Resource
    private TencentConfig tencentConfig;

    private COSClient init() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(tencentConfig.getSecretId(), tencentConfig.getSecretKey());
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(tencentConfig.getRegionName()));

        return new COSClient(cred, clientConfig);
    }


    @Override
    public String upload(MultipartFile file, String key) {
        COSClient cosclient = null;
        try {
            cosclient = init();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType("image/jpeg");
            PutObjectRequest putObjectRequest = new PutObjectRequest(tencentConfig.getBucketName(), key, file.getInputStream(), objectMetadata);
            cosclient.putObject(putObjectRequest);
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(tencentConfig.getBucketName(), key, HttpMethodName.GET);

            URL url = cosclient.generatePresignedUrl(generatePresignedUrlRequest);

            return url.getProtocol() + "://" + url.getHost() + url.getPath();
        } catch (IOException e) {
            throw new UploadFileException(e);
        }finally {
            Optional.ofNullable(cosclient)
                    .ifPresent(COSClient::shutdown);
        }
    }

    @Override
    public void remove(String key) {
        COSClient cosclient = null;
        try {
            cosclient = init();

            cosclient.deleteObject(tencentConfig.getBucketName(), key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }finally {
            Optional.ofNullable(cosclient)
                    .ifPresent(COSClient::shutdown);
        }
    }

}

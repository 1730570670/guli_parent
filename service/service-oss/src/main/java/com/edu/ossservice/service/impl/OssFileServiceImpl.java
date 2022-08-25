package com.edu.ossservice.service.impl;

import com.edu.ossservice.service.OssFileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;


/**
 * @CreateTime: 2022-08-22
 */
@Service
public class OssFileServiceImpl implements OssFileService {


    @Value("${tencentOss.secretId}")
    private String secretId;
    @Value("${tencentOss.secretKey}")
    private String secretKey;
    @Value("${tencentOss.bucketName}")
    private String bucketName;
    @Value("${tencentOss.regionArea}")
    private String regionArea;//地域


    @Override
    public String fileUpload(MultipartFile file) throws IOException {
        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID和SECRETKEY请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionArea);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        try{
            file.getResource().getInputStream();
            InputStream inputStream = file.getInputStream();//创建输入流
            //获取后缀
            String sufix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            // 指定要上传的文件
            ObjectMetadata meta=new ObjectMetadata();
            //必须设置该属性
            meta.setContentLength(file.getSize());
            //设置字符编码格式
            meta.setContentEncoding("UTF-8");
            Calendar calendar=Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);//获取年
            int month = calendar.get(Calendar.MONTH)+1;//获取月份
            String key = year+"/"+month+"/"+UUID.randomUUID()+sufix;//创建文件夹,按照年月进行创建,文件名(UUID)+文件后缀
            // bucketName:存储桶名   key:文件路径+名  file.getInputStream():流的输入   meta:文件格式
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key,  file.getInputStream(),meta);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);//将对象放置存储桶
            cosClient.shutdown();//关闭客户端

            return "https://"+bucketName+".cos."+regionArea+".myqcloud.com/"+key;
        }catch (Exception e){
            return "20001";
        }
    }

}

package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private OssProperties ossProperties;
    @Override
    public String upload(InputStream inputStream, String module, String originalFileName) {
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        if (!ossClient.doesBucketExist(bucketname)){
            //创建bucket
            ossClient.createBucket(bucketname);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        //构建日期路径：avatar/2019/02/26/文件名
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileName= UUID.randomUUID().toString();
        String fileExtension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String key=module+"/"+folder+"/"+fileName+fileExtension;
        // 上传文件流。
        //InputStream inputStream = new FileInputStream("<yourlocalFile>");
        ossClient.putObject(bucketname, key, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return "https://"+bucketname+"."+endpoint+"/"+key;
    }

    @Override
    public void removeFile(String url) {
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        // 判断文件是否存在。doesObjectExist还有一个参数isOnlyInOSS，
        // 如果为true则忽略302重定向或镜像；如为false，则考虑302重定向或镜像。
        boolean found = ossClient.doesObjectExist(bucketname, objectName);
        if (found){
            // 删除文件。
            ossClient.deleteObject(bucketname, objectName);
        }
        ossClient.shutdown();
    }

}

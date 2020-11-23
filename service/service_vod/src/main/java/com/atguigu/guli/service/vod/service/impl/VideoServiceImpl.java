package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @author wutao
 * @date 2020-11-16 21:39
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VodProperties vodProperties;
    /**
     *  @author: 吴涛
     *  @Date: 2020/11/16 21:42
     *  @Description: 视频上传
     * @param file
     * @param originalFilename
     * @return
    */
    @Override
    public String uploadVideo(InputStream file, String originalFilename) {
        String keysecret = vodProperties.getKeysecret();
        String keyid = vodProperties.getKeyid();
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest
                (keyid, keysecret, title, originalFilename, file);
        //request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        String videoId = response.getVideoId();
        if (StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败：" + response.getCode() + " - " + response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }
    /**
     *  @author: 吴涛
     *  @Date: 2020/11/19 20:21
     *  @Description:删除视频
    */
    @Override
    public void removeVideo(String videoId) throws ClientException {
        String keysecret = vodProperties.getKeysecret();
        String keyid = vodProperties.getKeyid();
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(keyid,keysecret);
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws ClientException {
        String keysecret = vodProperties.getKeysecret();
        String keyid = vodProperties.getKeyid();
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(keyid,keysecret);
        DeleteVideoRequest request = new DeleteVideoRequest();
        int size = videoIdList.size();
        StringBuffer idListStr=new StringBuffer();
        for (int i=0;i<size;i++){
            idListStr.append(videoIdList.get(i));
            if (i==size-1||i%20==19){
                System.out.println("idListStr = " + idListStr.toString());
                //支持传入多个视频ID，多个用逗号分隔，最多20个
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                idListStr=new StringBuffer();
            }else if(i%20<19){
                idListStr.append(",");
            }
        }
    }


}

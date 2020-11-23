package com.atguigu.guli.service.vod.service;
import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

/**
 * @author wutao
 * @date 2020-11-16 21:32
 */
public interface VideoService {
    String uploadVideo(InputStream file, String originalFilename);
    void removeVideo(String videoId) throws ClientException;

    void removeVideoByIdList(List<String> videoIdList) throws ClientException;
}

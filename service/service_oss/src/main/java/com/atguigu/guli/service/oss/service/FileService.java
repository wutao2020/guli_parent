package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

public interface FileService {
    /**
     *  文件上传至阿里云
     * @param inputStream
     * @param module
     * @param originalFileName
     * @return
     */
    String upload(InputStream inputStream,String module,String originalFileName);
    void removeFile(String url);
}

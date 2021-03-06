package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
public interface VideoService extends IService<Video> {

    boolean removeVideo(String id);

    boolean isAliyunVideo(String id);

    boolean removeAndUpdateVideo(String id,String videoSourceId);
    void  removeMediaVideoByChapterId(String chapterId);
    void  removeMediaVideoByCourseraId(String courseraIdId);
}

package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.MediaService;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Autowired
    private MediaService mediaService;
    /**
     *  @author: 吴涛
     *  @Date: 2020/11/20 21:18
     *  @Description:删除远程视频
    */
    @Override
    public boolean removeVideo(String id) {
        Video video = baseMapper.selectById(id);
        if (video==null||StringUtils.isEmpty(video.getVideoSourceId())){
            return true;
        }
        R r = mediaService.removeVideo(video.getVideoSourceId());
        return r.getSuccess();
    }
    /**
     *  @author: 吴涛
     *  @Date: 2020/11/20 21:31
     *  @Description:判断是否上传视频
    */
    @Override
    public boolean isAliyunVideo(String id) {
        Video video=baseMapper.selectById(id);
        if (video==null){
            return false;
        }
        return StringUtils.isEmpty(video.getVideoSourceId());
    }
    /**
    * @Description
    * @Author  吴涛
    * @Date   2020/11/20 21:18
    * @Param  id
    * @Return
    * @Exception   删除远程视频和更新数据
    *
    */
    @Override
    public boolean removeAndUpdateVideo(String id,String videoSourceId) {
        R r = mediaService.removeVideo(videoSourceId);
        if (!r.getSuccess()){
            return false;
        }
        if (!StringUtils.isEmpty(id)){
            Video newVideo=new Video();
            newVideo.setId(id);
            newVideo.setVideoSourceId("");
            newVideo.setVideoOriginalName("");
            return baseMapper.updateById(newVideo)>0;
        }
        return true;
    }
    /**
     *  @author: 吴涛
     *  @Date: 2020/11/22 16:40
     *  @Description: 根据章节id删去视频
    */
    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",chapterId);
        List<Video> videoList = baseMapper.selectList(videoQueryWrapper);
        List<String> videoSourceIdList=new ArrayList<>();
        if (videoList!=null&&videoList.size()>0) {
            for (Video video : videoList) {
                if (!StringUtils.isEmpty(video.getVideoSourceId())) {
                    videoSourceIdList.add(video.getVideoSourceId());
                }
            }
        }
        if (videoSourceIdList.size()>0){
            mediaService.removeVideoByIdList(videoSourceIdList);
        }
    }
    /**
     *  @author: 吴涛
     *  @Date: 2020/11/22 19:44
     *  @Description:根据课程id删去视频
    */
    @Override
    public void removeMediaVideoByCourseraId(String courseraIdId) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseraIdId);
        List<Video> videoList = baseMapper.selectList(videoQueryWrapper);
        List<String> videoSourceIdList=new ArrayList<>();
        if (videoList!=null&&videoList.size()>0) {
            for (Video video : videoList) {
                if (!StringUtils.isEmpty(video.getVideoSourceId())) {
                    videoSourceIdList.add(video.getVideoSourceId());
                }
            }
        }
        if (videoSourceIdList.size()>0){
            mediaService.removeVideoByIdList(videoSourceIdList);
        }
    }
}

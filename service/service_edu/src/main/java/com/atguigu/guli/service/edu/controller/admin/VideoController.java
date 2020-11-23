package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.MediaService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.awt.image.VolatileImage;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
@CrossOrigin
@Api(description = "课时管理")
@RestController
@RequestMapping("/admin/edu/video")
@Slf4j
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private MediaService mediaService;
    @ApiOperation("新增课时")
    @PostMapping("save")
    public R save(
            @ApiParam(value="课时对象", required = true)
            @RequestBody Video video){
        boolean result = videoService.save(video);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }
    @ApiOperation("新增课时")
    @PostMapping("update")
    public R update(@ApiParam(value ="课时对象",required = true)@RequestBody Video video){
        boolean result=videoService.updateById(video);
        if (result){
            return  R.ok().message("修改成功");
        }else {
            return R.error().message("修改失败");
        }
    }
    @ApiOperation("根据id查询课时")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value="课时id", required = true)
            @PathVariable String id){
        Video video=videoService.getById(id);
        if (video!=null){
            return R.ok().data("item",video);
        }else {
            return R.error().message("保存失败");
        }
    }
    @ApiOperation("根据ID删除课时")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "课时ID", required = true) @PathVariable String id){
        videoService.removeVideo(id);

        boolean result = videoService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }
    @ApiOperation("根据id删除阿里云视频")
    @DeleteMapping("removeVideo")
    public R removeVideo(
            @ApiParam(value="课时id", required = false)
            @RequestBody Video videoSource){
        String id = videoSource.getId();
        String videoSourceId = videoSource.getVideoSourceId();
        boolean isRemove=videoService.removeAndUpdateVideo(id,videoSourceId);
        if (isRemove){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");
        }
    }
}


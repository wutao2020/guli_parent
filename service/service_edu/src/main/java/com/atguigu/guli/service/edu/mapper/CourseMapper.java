package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseVo> selectPageByCourseQueryVo(Page<CourseVo> courseVoPage, @Param(Constants.WRAPPER)QueryWrapper<CourseVo> queryWrapper);

    CoursePublishVo getCoursePublshVoById(@Param("id") String id);

    WebCourseVo selectWebCourseVoById(@Param("id") String courseId);
    CourseDto selectCourseDtoById(@Param("id") String courseId);
}

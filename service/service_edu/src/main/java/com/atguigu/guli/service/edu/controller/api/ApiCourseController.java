package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wutao
 * @date 2020-11-26 21:11
 */
@CrossOrigin
@Api(description = "课程")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private ChapterService chapterService;
    @ApiOperation("课程列表")
    @GetMapping("list")
    public R list(
            @ApiParam(value = "查询对象", required = false)
                    WebCourseQueryVo webCourseQueryVo){
        List<Course> courseList = courseService.webSelectList(webCourseQueryVo);
        return  R.ok().data("courseList", courseList);
    }
    @ApiOperation("根据id查询课程")
    @GetMapping("get/{courseId}")
    public R getById(@ApiParam(value = "课程id",required = true) @PathVariable String courseId){
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);
        List<ChapterVo> chapterVos = chapterService.nestedList(courseId);
        return R.ok().data("course",webCourseVo).data("chapterVoList",chapterVos);

    }
    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String courseId){
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;
    }
}

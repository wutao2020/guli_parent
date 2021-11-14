package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private OssFileService ossFileService;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;

    /**
     * 新增课程信息
     * @param courseInfoForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        course.setStatus(Course.COURSE_DRAFT);
         baseMapper.insert(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);
        return course.getId();
    }

    /**
     * 查询课程信息
     * @param id
     * @return
     */
    @Override
    public CourseInfoForm getCouroseInfoById(String id) {
        //从course表中取数据
        Course course = baseMapper.selectById(id);
        if(course == null){
            return null;
        }

        //从course_description表中取数据
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        //创建courseInfoForm对象
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    /**
     * 修改课程
     * @param courseInfoForm
     */
    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);
    }

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param courseQueryVo
     * @return
     */
    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("c.gmt_create");
        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("c.title",title);
        }
        if (!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("c.teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("c.subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("c.subject_parent_id", subjectParentId);
        }
        Page<CourseVo> courseVoPage=new Page<>(page,limit);
        List<CourseVo> list=baseMapper.selectPageByCourseQueryVo(courseVoPage,queryWrapper);
        courseVoPage.setRecords(list);
        return courseVoPage;
    }

    /**
     *
     * 删除课程图片
     * @param id
     * @return
     */
    @Override
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);
        if (course!=null){
            String cover = course.getCover();
            if (!StringUtils.isEmpty(cover)){
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    /**
     * 删除课程
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {

        //收藏信息：course_collect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //评论信息：comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);

        //课程信息：course
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        CoursePublishVo coursePublishVo= baseMapper.getCoursePublshVoById(id);
        return coursePublishVo;
    }

    @Override
    public boolean publishCourseById(String id) {
        Course oldCourse = baseMapper.selectById(id);
        if(oldCourse==null){
            return false;
        }
        if (oldCourse.getStatus().equals(Course.COURSE_NORMAL)){
            return true;
        }
        Course course=new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",Course.COURSE_NORMAL);
        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", webCourseQueryVo.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())) {
            queryWrapper.eq("subject_id", webCourseQueryVo.getSubjectId());
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())) {
            if (webCourseQueryVo.getType()==null||webCourseQueryVo.getType()==1){
                queryWrapper.orderByAsc("price");
            }else {
                queryWrapper.orderByDesc("price");
            }
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebCourseVo selectWebCourseVoById(String id) {
        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
        WebCourseVo webCourseVo = baseMapper.selectWebCourseVoById(id);
        return webCourseVo;
    }

    @Override
    @Cacheable(value = "index", key = "'selectHotCourse'")
    public List<Course> selectHotCourse() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        return baseMapper.selectCourseDtoById(courseId);
    }
}

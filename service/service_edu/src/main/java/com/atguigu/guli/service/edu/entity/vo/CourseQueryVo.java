package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CourseQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 课程标题
     */
    private String title;
    /**
     * 教师id
     */
    private String teacherId;
    /**
     * 一级课程id
     */
    private String subjectParentId;
    /**
     * 二级课程id
     */
    private String subjectId;
}

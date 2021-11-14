package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wutao
 * @date 2020-11-26 21:09
 */
@Data
public class WebCourseQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;
    private Integer type;
}

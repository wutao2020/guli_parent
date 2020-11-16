package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
public interface SubjectService extends IService<Subject> {
    /**
     * 批量导入
     * @param inputStream
     */
    void batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();
}

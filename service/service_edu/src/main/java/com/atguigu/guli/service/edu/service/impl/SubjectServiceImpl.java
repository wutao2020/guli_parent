package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.listener.ExcelSubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    /**
     * 批量导入
     * @param inputStream
     */
    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class,
                new ExcelSubjectDataListener(baseMapper)).
                excelType(ExcelTypeEnum.XLS).sheet().doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.selectNestedListByParentId("0");
    }
}

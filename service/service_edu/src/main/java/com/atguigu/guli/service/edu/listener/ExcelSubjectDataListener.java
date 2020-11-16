package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {
    private SubjectMapper subjectMapper;
    public ExcelSubjectDataListener(SubjectMapper subjectMapper){
        this.subjectMapper=subjectMapper;
    }
    public ExcelSubjectDataListener(){

    }
    /**
     * 遍历每一行记录
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context) {
        log.info("解析到一条记录: {}", data);
        //处理读取出来的数据
        String levelOneTitle = data.getLevelOneTitle();//一级标题
        String levelTwoTitle = data.getLevelTwoTitle();//二级标题
        Subject subjectOne = this.getByTitle(levelOneTitle);
        String parentId=null;
        if (subjectOne==null){
            Subject subject=new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);
            subjectMapper.insert(subject);
            parentId=subject.getId();
        }else {
            parentId=subjectOne.getId();
        }
        Subject subjectTwo = this.getSubByTitle(levelTwoTitle, parentId);
        if (subjectTwo==null){
            Subject subject=new Subject();
            subject.setParentId(parentId);
            subject.setTitle(levelTwoTitle);
            subjectMapper.insert(subject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context ) {
        log.info("所有数据解析完成！");
    }

    /**
     * 根据一级分类的名称查询数据是否存在
     * @param title
     * @return
     */
    public Subject getByTitle(String title){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",title);
        wrapper.eq("parent_id",0);
        Subject subject = subjectMapper.selectOne(wrapper);
        return subject;
    }

    /**
     * 根据二级分类和父亲ID查询数据是否存在
     * @param title
     * @param parentId
     * @return
     */
    public Subject getSubByTitle(String title,String parentId ){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",parentId);
        wrapper.eq("title",title);
        return subjectMapper.selectOne(wrapper);
    }
}

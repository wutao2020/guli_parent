package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-08-23
 */
@RestController
@RequestMapping("/admin/edu/subject")
@CrossOrigin //允许跨域
@Api(description = "课程分类管理")
@Slf4j
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    @PostMapping("/import")
    @ApiOperation("Excel批量导入")
    public R batchImport(@ApiParam(value = "Excel文件",required = true) @RequestParam("file") MultipartFile file)   {
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }
    @GetMapping("nested_list")
    @ApiOperation("嵌套数据列表")
    public R nestedList(){
        List<SubjectVo> subjectVoList= subjectService.nestedList();
        return R.ok().data("items",subjectVoList);
    }
}


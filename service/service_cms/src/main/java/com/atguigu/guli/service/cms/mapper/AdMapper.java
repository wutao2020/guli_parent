package com.atguigu.guli.service.cms.mapper;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 广告推荐 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2020-04-26
 */
public interface AdMapper extends BaseMapper<Ad> {

    List<AdVo> selectPageByQuerywt(
            Page<AdVo> pageParam,
            @Param(Constants.WRAPPER) QueryWrapper<AdVo> queryWrapper);
}

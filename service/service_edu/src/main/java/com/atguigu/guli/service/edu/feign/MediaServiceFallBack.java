package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.common.base.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wutao
 * @date 2020-11-19 20:49
 */
@Slf4j
@Service
public class MediaServiceFallBack implements MediaService {
    @Override
    public R removeVideo(String id) {
        return R.error().message("删除阿里云视频失败");
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        return R.error().message("批量删除阿里云视频失败");
    }
}

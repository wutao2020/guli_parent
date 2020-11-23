package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.common.base.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wutao
 * @date 2020-11-19 20:47
 */
@Service
@FeignClient(value = "service-vod",fallback = MediaServiceFallBack.class)
public interface MediaService {
    @DeleteMapping("/admin/vod/media/remove")
    R removeVideo(@RequestBody String id);
    @DeleteMapping("/admin/vod/media/removelist")
     R removeVideoByIdList(@RequestBody List<String> videoIdList);
}

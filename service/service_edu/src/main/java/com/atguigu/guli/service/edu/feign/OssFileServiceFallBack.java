package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.common.base.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OssFileServiceFallBack implements OssFileService{
    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}

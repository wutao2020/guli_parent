package com.atguigu.guli.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
/**
 *  @author: 吴涛
 *  @Date: 2020/11/16 20:47
 *  @Description:
*/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
@ComponentScan({"com.atguigu.guli"})
@EnableDiscoveryClient 
public class ServiceVodApplication {
    /**
    * @Description
    * @Author  吴涛
    * @Date   2020/11/16 20:46
    * @Param  
    * @Return      
    * @Exception   
    * 
    */
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class,args);
    }
}

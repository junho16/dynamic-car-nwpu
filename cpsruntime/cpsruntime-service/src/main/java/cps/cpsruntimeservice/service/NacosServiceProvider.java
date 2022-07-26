package cps.cpsruntimeservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cps.runtime.api.service.NacosService;

/**
 * Nacos服务注册发现
 */
@RefreshScope
@RestController
public class NacosServiceProvider implements NacosService {

    /**
     * 获取nacos配置信息
     */
    @Value("${test.key}")
    private String config;

    /**
     * nacos接口注册，返回nacos配置的信息
     * @return
     */
    @Override
    @RequestMapping("/test")
    public String test() {
        return config;
    }

    /**
     * nacos接口注册
     * @param param
     * @return
     */
    @Override
    @RequestMapping("/testParam/{param}")
    public String testParam(@PathVariable String param) {
        return "hello,"+param;
    }

}

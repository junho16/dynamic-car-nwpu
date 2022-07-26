package cps.runtime.api.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Nacos接口
 */
public interface NacosService {

    @RequestMapping("/test")
    public String test();

    /**
     * vvalue:
     * @param param
     * @return
     */
    @RequestMapping("/testParam/{param}")
    public String testParam(@PathVariable(value = "param") String param);

}

package cps.dyncar.controller;

import cps.api.entity.CPSInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 功能描述
 *
 * @author: mdd
 * @date: 2022年05月09日 14:13
 */
@RestController
@RequestMapping("/cps")
public class carController {
    private static final Logger logger = LoggerFactory.getLogger(carController.class);

    @Resource
    private CPSInstance cpsInstance;

    @GetMapping("/getCPSVisualData")
    public String getCPSVisualData() {
        return "";
    }
}

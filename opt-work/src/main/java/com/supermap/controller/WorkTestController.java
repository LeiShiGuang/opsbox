package com.supermap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class WorkTestController {

    /**
     * 这是获取日志的方式一（不推荐使用，而是使用 lombok 组件的自动注解，详见：
     */
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/workhome")
    @ResponseBody
    public String workHome(){
        logger.debug("这是 Debug 日志");
        return "Hello Work";
    }


}
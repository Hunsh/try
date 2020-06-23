package com.thxpace.portal.controller;

import com.thxpace.portal.service.NavigationPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 首页内容管理Controller
 * Created by macro on 2019/1/28.
 */
@Controller
@Api(tags = "NavigatonPageController", description = "首页内容管理")
@RequestMapping("/navigationPage")
public class NavigatonPageController {
    @Autowired
    private NavigationPageService navigationPageService;

    @ApiOperation("获取所有导航页")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public String content() {
        return "";
    }



}

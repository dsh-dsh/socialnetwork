package com.skillbox.socialnet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {


    @RequestMapping
    public String index() {
        return "index";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "index";
    }

//    @GetMapping(value = "/**/{path:[^\\\\.]*}")
//    public String redirectToIndex() {
//        return "forward:/";
//    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
    return "forward:/";
}

}

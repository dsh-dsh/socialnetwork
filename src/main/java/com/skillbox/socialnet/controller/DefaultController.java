package com.skillbox.socialnet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@Controller
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = {"/shift-email", "/**/{path:^(?!/ws)[^\\\\.]*}"})
    public String redirectToIndex() {
        return "forward:/";
    }

}

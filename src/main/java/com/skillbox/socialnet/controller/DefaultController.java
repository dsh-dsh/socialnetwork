package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.util.anotation.MethodLog;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @MethodLog
    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:^(?!/ws)[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }

}

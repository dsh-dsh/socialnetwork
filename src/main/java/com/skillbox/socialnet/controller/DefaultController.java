//package com.skillbox.socialnet.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//@CrossOrigin
//@Controller
//public class DefaultController {
//
//
//    @RequestMapping
//    public String index() {
//        return "index";
//    }
//
//    @GetMapping("/change-password")
//    public String changePassword() {
//        return "index";
//    }
//
//    @GetMapping("/shift-email")
//    public String shiftEmail() {
//        return "index";
//    }
//
//    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
//    public String redirectToIndex() {
//    return "forward:/";
//}
//
//}

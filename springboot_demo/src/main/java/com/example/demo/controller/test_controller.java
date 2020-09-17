package com.example.demo.controller;

import com.example.demo.bean.UserBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.Map;

@RestController
public class test_controller {

    @RequestMapping("/hello")
    public String sayHello(){
        return "hello";
    }

    /**
     * 测试springboot接收post
     * @param userBean
     * @return
     */
    @PostMapping(path = "/p")
    public String testPost(@RequestBody UserBean userBean){
        System.out.println(userBean.getStudentId());
        return "ok";
    }

    @PostMapping(path = "/p2")
    public String testPost(@RequestBody Map<String, String> person){
        System.out.println(person.get("studentId"));
        return "ok";
    }


}


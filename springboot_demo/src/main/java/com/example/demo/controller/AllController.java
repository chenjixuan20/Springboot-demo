package com.example.demo.controller;

import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AllController {

    @Autowired
    UserService userService;

    /**
     * 展示所有的学生信息
     * @return
     */
    @RequestMapping(value = "/api/v1/student",method = RequestMethod.GET)
//    下面写法也正确
//    @GetMapping(path = "/api/v1/student")
    public List<UserBean> showstudent(){
        List<UserBean> userBeanList = userService.queryAllStudent();
        for(int i = 0; i < userBeanList.size(); i++){
            System.out.println(userBeanList.get(i).getName());
        }
        return userBeanList;
    }

    /**
     * 添加学生信息
     * @return
     */
    @RequestMapping(value = "/api/v1/student",method = RequestMethod.POST)
//    下面写法也正确
//    @PostMapping(path = "/api/v1/student" )
    public int addStudent(@RequestBody UserBean userBean){
        System.out.println(userBean.getStudentId());
        int aflag = userService.addStudent(userBean);
        return aflag; //1表示成功
    }


    /**
     * 根据studentId修改学生信息
     * @return
     */
    @RequestMapping(value = "/api/v1/student",method = RequestMethod.PUT)
//    下面写法也正确
//    @PutMapping(path = "/api/v1/student")
    public int modiftStudent(@RequestBody UserBean userBean){
        System.out.println(userBean.getStudentId() + ' ' + userBean.getName() + ' ' + userBean.getDepartment());
        int flag = userService.modifyStudent(userBean);
        return flag;
    }

    /**
     * 根据studentId删除学生
     * @return
     */
    @RequestMapping(value = "/api/v1/student",method = RequestMethod.DELETE)
    public int deleteStudent(@RequestParam String studentId){
        System.out.println(studentId);
        int flag = userService.dropStudent(studentId);
        return flag; //1表示成功
    }

    /**
     * 根据studentId查找学生由于作业只要求写一个GET，所以没写
     */

}

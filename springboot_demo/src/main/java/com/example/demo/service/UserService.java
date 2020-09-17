package com.example.demo.service;

import com.example.demo.bean.UserBean;

import java.util.List;

public interface UserService {
    //根据ID查询用户信息
    UserBean queryStudentById(String id);

    //插入新的用户
    int addStudent(UserBean userBean);

    //删除用户
    int dropStudent(String id);

    //修改用户
    int modifyStudent(UserBean userBean);

    //查询所有用户
    List<UserBean> queryAllStudent();
}

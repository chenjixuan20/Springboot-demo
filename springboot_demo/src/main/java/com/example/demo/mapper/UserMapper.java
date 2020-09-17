package com.example.demo.mapper;

import com.example.demo.bean.UserBean;

import java.util.List;

public interface UserMapper {

    //根据ID查询用户信息
    UserBean selectStudentById(String id);

    //插入新的用户
    int insertStudent(UserBean userBean);

    //删除用户
    int deleteStudent(String id);

    //修改用户
    int updateStudent(UserBean userBean);

    //查询所有用户
    List<UserBean> getAllStudent();

}



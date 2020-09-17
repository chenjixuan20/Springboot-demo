package com.example.demo.servicelmpl;

import com.example.demo.bean.UserBean;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    //将DAO注入Service层
    @Autowired
    private UserMapper userMapper;


    /**
     * 根据ID来查询用户
     * @param id
     * @return
     */
    @Override
    public UserBean queryStudentById(String id) {
        return userMapper.selectStudentById(id);
    }

    /**
     * 添加学生
     * @param userBean
     * @return
     */
    @Override
    public int addStudent(UserBean userBean) {
        int aFlag = userMapper.insertStudent(userBean);
        return aFlag;
    }

    /**
     * 根据id删除学生
     * @param id
     * @return
     */
    @Override
    public int dropStudent(String id) {
        int dFlag = userMapper.deleteStudent(id);
        return dFlag;
    }

    /**
     * 修改学生信息
     * @param userBean
     * @return
     */
    @Override
    public int modifyStudent(UserBean userBean) {
        int mFlag = userMapper.updateStudent(userBean);
        return mFlag;
    }

    /**
     * 查询所有学生
     * @return
     */
    @Override
    public List<UserBean> queryAllStudent() {
        return userMapper.getAllStudent();
    }
}

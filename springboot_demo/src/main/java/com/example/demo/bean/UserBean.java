package com.example.demo.bean;

public class UserBean {
    private String studentId;
    private String name;
    private String department;
    private String major;

    //get方法
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }


    public String getMajor() {
        return major;
    }

    //set方法
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setMajor(String major) {
        this.major = major;
    }


}

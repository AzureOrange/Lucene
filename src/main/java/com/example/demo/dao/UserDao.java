package com.example.demo.dao;

import com.example.demo.domain.User;

import java.util.List;

/**
 * 用户相关接口
 *
 * @author orange
 * @Time 2018/9/11 0011
 */
public interface UserDao {

    void save(User user);

    User findOne(String id);

    List<User> findAll();
}

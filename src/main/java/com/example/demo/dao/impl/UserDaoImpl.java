package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户相关接口实现
 *
 * @author orange
 * @Time 2018/9/11 0011
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(User user) {
        mongoTemplate.insert(user);
    }

    @Override
    public User findOne(String id) {
        Criteria criteria = Criteria.where("_id").is(id);

        return mongoTemplate.findOne(Query.query(criteria), User.class);
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }


}

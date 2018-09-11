package com.example.demo;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;
import com.example.demo.lucene.CreateAndSearch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * descript
 *
 * @author orange
 * @Time 2018/9/11 0011
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuceneTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CreateAndSearch createAndSearch;

    @Test
    public void createIndex() throws Exception {
        List<User> users = userDao.findAll();

        createAndSearch.createIndex(users);
    }

    @Test
    public void search(){
        String keyWork = "傻子";

        List<User> users = createAndSearch.search(keyWork);
        if (CollectionUtils.isEmpty(users)){
            log.warn("查询结果为空");
            return;
        }

        for (User user : users){
            System.out.println(user);
        }
    }
}

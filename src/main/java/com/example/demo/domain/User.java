package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户信息
 *
 * @author orange
 * @Time 2018/9/11 0011
 */
@Data
@Builder
@Document(collection = "user")
public class User {
    @Id
    private String id;

    private int age;

    private String content;

    public User() {
    }

    public User(String id, int age, String content) {
        this.id = id;
        this.age = age;
        this.content = content;
    }
}

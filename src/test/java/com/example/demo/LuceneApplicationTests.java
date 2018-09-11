package com.example.demo;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuceneApplicationTests {

	@Autowired
	private UserDao userDao;

	@Test
	public void insert() {
		User user = User.builder().age(14).content("穿越\n" +
				"中华民族是一个具有灿烂文化和悠久历史的民族，在灿烂的文化瑰宝中数学在世界数学发展史中也同样具有许多耀眼的光环。中国古代算数的许多研究成果里面就早已孕育了后来西方数学才设计的先进思想方法，近代也有不少世界领先的数学研究成果就是以华人数学家命名的。</p><p></p><p>数学家李善兰在级数求和方面的研究成果，在国际上被命名为【李氏恒定式】</p><p></p><p>“华氏定理”是我国著名数学家华罗庚的研究成果。华氏定理为：体的半自同构必是自同构自同体或反同体。　数学家华罗庚关于完整三角和的研究成果被国际数学界称为“华氏定理”；另外他与数学家王元提出多重积分近似计算的方法被国际上誉为“华—王方法”。</p><p></p><p>数学家苏步青在仿射微分几何学方面的研究成果在国际上被命名为“苏氏锥面”。苏步青院士对仿射微分几何的一个极其美妙的发现是：他对一般的曲面，构做出一个访射不变的4次（3阶）代数锥面。在访射的曲面理论中为人们许多协变几何对象，包括2条主切曲线，3条达布切线，3条塞格雷切线和仿射法线等等，都可以由这个锥面和它的3根尖点直线以美妙的方式体现出来，形成一个十分引人入胜的构图，这个锥面被命名为苏氏锥面。").build();

		userDao.save(user);
	}

	@Test
	public void findAll(){
		List<User> userList = userDao.findAll();
		userList.forEach(System.out::println);
 	}

}

package dao;

import com.igeek.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Gyges on 2017/5/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class UserLogin {

    @Resource
    private UserMapper userMapper;

    @Test
    public void getResult(){
        String username = "system";
        int count  = userMapper.selectByUsername(username);
        System.out.println(count);
    }
}

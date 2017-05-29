package com.douzuiwa.dao;

import com.douzuiwa.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int selectByEmail(String email);

    int selectByUsername(String username);

    int selectByPhone(String phone);

    User selectLogin(@Param("username")String username,@Param("password") String password);
}
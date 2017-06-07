package com.igeek.dao;

import com.igeek.pojo.User;
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

    String selectQuestionByUsername(String username);

    User selectLogin(@Param("username")String username,@Param("password") String password);

    User selectAdminLogin(String phone);

    User selectAnswer(@Param("username") String username,@Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    int selectPasswordById(@Param("password") String password,@Param("userId") Integer userId);

    int selectEmailById(@Param("email") String email,@Param("userId") Integer userId);
}
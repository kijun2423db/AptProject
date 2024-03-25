package com.example.aptProject.dao;

import com.example.aptProject.entity.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UsersDao {

    @Select("select * from users where uid=#{uid}")
    Users getUser(String uid);

    @Select("select * from users where isDeleted=0 order by regDate desc"
            + " limit #{count} offset #{offset}")
    List<Users> getUserList(int count, int offset);

    @Insert("insert into users values (#{uid}, #{pwd}, #{uname}, #{email}, default, default)")
    void insertUser(Users user);

    @Update("update users set pwd=#{pwd}, uname=#{uname}, email=#{email} where uid=#{uid}")
    void updateUser(Users user);

    @Update("update users set isDeleted=1 where uid=#{uid}")
    void deleteUser(String uid);

    @Select("select count(uid) from users where isDeleted=0")
    int getUserCount();
}

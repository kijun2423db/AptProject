package com.example.aptProject.dao;

import com.example.aptProject.entity.MyRegion;
import com.example.aptProject.service.LocationService;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Mapper
public interface MyRegionDao {

    @Select("SELECT * FROM myRegion WHERE uid = #{uid}")
    MyRegion findByUserId(@Param("uid") String uid);

    @Insert("INSERT INTO myRegion (uid, lCode) VALUES (#{uid}, #{lCode})")
    void insert(@Param("uid") String uid, @Param("lCode") int lCode);

    @Update("UPDATE myRegion SET lCode = #{lCode} WHERE mid = #{mid}")
    void update(@Param("lCode") int lCode, @Param("mid") int mid);

    @Delete("DELETE FROM myRegion WHERE mid = #{mid}")
    void delete(@Param("mid") int mid);

 }

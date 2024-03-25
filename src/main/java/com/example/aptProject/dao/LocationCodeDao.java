package com.example.aptProject.dao;

import com.example.aptProject.entity.LocationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface LocationCodeDao {
    @Select("SELECT * FROM locationCode WHERE lCode=#{lCode}")
    LocationCode getLocationCode(int lCode);

    // 모든 firstName 목록을 불러옵니다.
    @Select("SELECT DISTINCT firstName FROM locationCode")
    List<String> findAllFirstNames();

    // 선택된 firstName에 따른 secondName 목록을 불러옵니다.
    @Select("SELECT distinct secondName FROM locationCode WHERE firstName = #{firstName}")
    List<String> findSecondNamesByFirstName(@Param("firstName") String firstName);

    @Select("SELECT distinct lastName FROM locationCode WHERE firstName = #{firstName} AND secondName = #{secondName}")
    List<String> findLastNamesByFirstNameAndSecondName(@Param("firstName") String firstName, @Param("secondName") String secondName);

    @Select("SELECT lCode FROM LocationCode WHERE firstName = #{firstName} AND secondName = #{secondName}")
    LocationCode getLocationCodeByFirstNameAndSecondName(@Param("firstName") String firstName, @Param("secondName") String secondName);

    @Select("SELECT lCode FROM LocationCode WHERE firstName = #{firstName} AND secondName = #{secondName} AND lastName = #{lastName}")
    LocationCode getLocationCodeByFirstNameAndSecondNameAndLastName(@Param("firstName") String firstName, @Param("secondName") String secondName, @Param("lastName") String lastName);


}
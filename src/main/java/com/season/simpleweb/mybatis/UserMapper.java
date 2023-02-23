package com.season.simpleweb.mybatis;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
	public Integer getName(@Param("_name") String name);
	public Map<String, Object> getUser(@Param("_id") String id);
}

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<mapper namespace="${daoPackage}.${className_d}Mapper">
	
	<sql id="condition">
		<#list tableCarrays as carray>
			<if test="${carray.carrayName_x} != null and ${carray.carrayName_x} != ''">
			  <#if carray_index != 0>and</#if> t.${carray.carrayName} = ${carray.carrayName_m}
			</if>
		</#list>
	</sql>
	
    <sql id="columns">
    	${stringCarrayNames6}
    </sql>
	
	<!-- 新增 -->
	<insert id="insert" useGeneratedKeys="true" keyProperty="id" parameterType="${pojoPackage}.${className_d}">
		INSERT INTO ${className} (
			${stringCarrayNames3}
		) VALUES (
			${stringCarrayNames4}
		)
	</insert>
	
	<!-- 查询所有-->
	<select id="query" resultType="${pojoPackage}.${className_d}">
		SELECT
		<include refid="columns"/>
		FROM ${className} t
	</select>
	
	<!-- 条件查询-->
	<select id="queryByMap" parameterType="hashmap" resultType="${pojoPackage}.${className_d}">
		SELECT
		<include refid="columns"/>
		FROM ${className} t
		<where>
		<include refid="condition" />
		</where>
	</select>
	
	<!-- 单对象查询-->
	<select id="get" parameterType="hashmap" resultType="${pojoPackage}.${className_d}">
		SELECT
		<include refid="columns"/>
		FROM ${className} t
		<where>
		<include refid="condition" />
		</where>
		limit 0,1
	</select>
	
	<#list tableIndexs as tableIndex>
	<#if tableIndex.unique>
	<#if tableIndex.indexName=="PRIMARY">
	<!-- 更新-->
	<update id="update" parameterType="${pojoPackage}.${className_d}">
		UPDATE ${className} 
		SET
		${stringCarrayNames5}
		WHERE
		${tableIndex.stringCarrayNames5}
	</update>
	
	<!-- 删除-->
	<delete id="delete" parameterType="${pojoPackage}.${className_d}">
		DELETE FROM ${className} 
		WHERE
		${tableIndex.stringCarrayNames5}
	</delete>
	</#if>
	</#if>
	</#list>
	
</mapper>

package com.school.project.framework.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJsonUtil {

	private static ObjectMapper mapper;
	
	@BeforeClass
	public static void setUp() {
		mapper = new ObjectMapper();
	}
	@Test
	public void testJsonStrToMap() throws IOException {
		Cat cat = new Cat("kitty", 22);
		String jsonStr = mapper.writeValueAsString(cat);
		Map<String, String> map = JsonUtil.jsonStrToMap(jsonStr, String.class, String.class);
		System.out.println(map);
	}
	
	@Test
	public void testJsonArrToList() throws JsonProcessingException {
		Cat[] cats = {new Cat("mi", 11), new Cat("mi222", 121), new Cat("mi33", 11)};
		String jsonStr = mapper.writeValueAsString(cats);
		System.out.println(new JsonUtil());
		List<Cat> list = JsonUtil.jsonArrayToList(jsonStr, Cat.class);
		System.out.println(list);
	}
	
	@Test
	public void TestJsonToObject() {
		String jsonStr = "{\"access_token\":\"GGKffUyI2hKSd5HAL_Wh1TcDT9Ag69nxEsUka26FKhDGGUwpQzRCFNcdJAe00bpc7I0_P_vxp2ffWA2JY683qcf5WoEWX8Skr5wtwe-FXPMNCWjAIAGVP\",\"expires_in\":7200}";
		Token acc = JsonUtil.jsonStrToObjet(jsonStr, Token.class);
		System.out.println(acc);
	}
	
}
class Cat {
	@JsonView()
	private String name;
	
	private int age;

	public Cat() {
		super();
	}
	public Cat(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Cat [name=" + name + ", age=" + age + "]";
	}
}

class Token {
	@JsonProperty("access_token")
	String accessToken;
	@JsonProperty("expires_in")
	String expireIn;
	String name;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(String expireIn) {
		this.expireIn = expireIn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Token [accessToken=" + accessToken + ", expireIn=" + expireIn + ", name=" + name + "]";
	}
	
	
}
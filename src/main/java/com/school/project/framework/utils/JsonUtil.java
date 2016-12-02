package com.school.project.framework.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.project.framework.exceptions.SwpException;

public class JsonUtil {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Convert a json string to a java object.
	 * @author Danny.Wang
	 * @param jsonStr
	 * @param obj
	 * @return
	 * @throws SwpException
	 */
	public static <T> T jsonStrToObjet(String jsonStr, Class<T> clazz) {
		T pojo = null;
		try {
			pojo = mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			return pojo;
		}
		return pojo;
	}
	
	/**
	 * Convert a java object to a json string.
	 * @author Danny.Wang
	 * @param obj
	 * @return json string
	 */
	public static String objectToJsonStr(Object obj) {
		String jsonStr = null;
		try {
			jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return jsonStr;
		}
		return jsonStr;
	}
	
	/**
	 * Convert a json format file to a java object.
	 * @author Danny.Wang
	 * @param pathname
	 * @param obj
	 * @return
	 */
	public static <T> T josnFileToObject(String pathname, Class<T> clazz) {
		T pojo = null;
		try {
			pojo = mapper.readValue(new File(pathname), clazz);
		} catch (IOException e) {
			return pojo;
		}
		return pojo;
	}
	
	/**
	 *Method that can be used to serialize any Java value as JSON output, written to File provided 
	 * @author Danny.Wang
	 * @param pathname
	 * @param obj
	 */
	public static void objectToJsonFile(String pathname,Object obj) {
		try {
			mapper.writeValue(new File(pathname), obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that can be used to serialize JSON array as list. 
	 * @author Danny.Wang
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonArrayToList(String jsonStr, Class<T> clazz) {
		List<T> list = null;
		try {
			JavaType type = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, clazz);
			list = mapper.readValue(jsonStr, type); 
		} catch (IOException e) {
			return list;
		}
		return list;
	}
	
	public static <K, V> Map<K, V> jsonStrToMap(String jsonStr, Class<K> K, Class<V> V) {
		Map<K, V> map = null;
		try {
			JavaType type = mapper.getTypeFactory().constructParametrizedType(HashMap.class, Map.class, K, V);
			map = mapper.readValue(jsonStr, type);
		} catch (IOException e) {
			return map;
		}
		return map;
	}
}

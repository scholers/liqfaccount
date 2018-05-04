package com.scholers.account.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * Title: Ext JS 辅助类
 * Description: 该类用于转换java对象为XML文件格式或JSON文件格式
 * @author weijun
 * @time: 2008.07.09
 */
public class ExtHelper {
	/**
	 * 通过List生成XML数据
	 * @param recordTotal 记录总数，不一定与beanList中的记录数相等
	 * @param beanList 包含bean对象的集合
	 * @return 生成的XML数据
	 */
	public static String getXmlFromList(int recordTotal , List beanList) {
		Total total = new Total();
		total.setResults(recordTotal);
		List results = new ArrayList();
		results.add(total);
		results.addAll(beanList);
		XStream sm = new XStream(new DomDriver());
		for (int i = 0; i < results.size(); i++) {
			Class c = results.get(i).getClass();
			String b = c.getName();
			String[] temp = b.split("\\.");
			sm.alias(temp[temp.length - 1], c);
		}
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"	+ sm.toXML(results);
		return xml;
	}
	/**
	 * 通过List生成XML数据  
	 * @param beanList 包含bean对象的集合    
	 * @return 生成的XML数据  
	 */
	public static String getXmlFromList(List beanList,int Bsize){
		return getXmlFromList(Bsize,beanList);  
	}        
	/**
	 * 通过List生成JSON数据
	 * @param recordTotal 记录总数，不一定与beanList中的记录数相等
	 * @param beanList 包含bean对象的集合
	 * @return 生成的JSON数据
	 */
	public static String getJsonFromList(long recordTotal , List beanList, String strTotalMoney){
		TotalJson total = new TotalJson();
		total.setResults(recordTotal);
		total.setItems(beanList);  
		total.setStrTotalMoney(strTotalMoney);
		
		JsonConfig jsonConfig = new JsonConfig(); 
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		jsonConfig.registerJsonValueProcessor(Float.class, new JsonFloatValueProcessor()); 
		
		JSONObject JsonObject = JSONObject.fromObject(total, jsonConfig);
		String strRen = "";
		try {
			strRen = URLDecoder.decode(JsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		System.out.println(strRen);
		return strRen;
	}
	
	
	/**
	 * 通过List生成JSON数据
	 * @param recordTotal 记录总数，不一定与beanList中的记录数相等
	 * @param beanList 包含bean对象的集合
	 * @return 生成的JSON数据
	 */
	public static String getJsonFromListTime(long recordTotal , List beanList, String strTotalMoney){
		TotalJson total = new TotalJson();
		total.setResults(recordTotal);
		total.setItems(beanList);  
		total.setStrTotalMoney(strTotalMoney);
		
		JsonConfig jsonConfig = new JsonConfig(); 
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jsonConfig.registerJsonValueProcessor(Float.class, new JsonFloatValueProcessor()); 
		
		JSONObject JsonObject = JSONObject.fromObject(total, jsonConfig);
		String strRen = "";
		try {
			strRen = URLDecoder.decode(JsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return strRen;
	}
	/**  
	 * 通过List生成JSON数据
	 * @param beanList 包含bean对象的集合
	 * @return 生成的JSON数据
	 */
	public static String getJsonFromList(List beanList,String strTotalMoney){
		return getJsonFromList(beanList.size(),beanList, strTotalMoney);
	}
	/**
	 * 通过bean生成JSON数据
	 * @param bean bean对象
	 * @return 生成的JSON数据
	 */
	public static String getJsonFromBean(Object bean){
		JsonConfig jsonConfig = new JsonConfig(); 
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONObject JsonObject = JSONObject.fromObject(bean, jsonConfig);
		return JsonObject.toString();
	}
}
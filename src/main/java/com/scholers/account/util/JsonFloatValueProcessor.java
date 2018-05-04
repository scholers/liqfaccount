package com.scholers.account.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonFloatValueProcessor implements JsonValueProcessor { 

    /** 
     * datePattern 
     */ 
    private String datePattern = "yyyy-MM-dd"; 

    /** 
     * JsonDateValueProcessor 
     */ 
    public JsonFloatValueProcessor() { 
        super(); 
    } 

    /** 
     * @param format 
     */ 
    public JsonFloatValueProcessor(String format) { 
        super(); 
        this.datePattern = format; 
    } 

    /** 
     * @param value 
     * @param jsonConfig 
     * @return Object 
     */ 
    public Object processArrayValue(Object value, JsonConfig jsonConfig) { 
        return process(value); 
    } 

    /** 
     * @param key 
     * @param value 
     * @param jsonConfig 
     * @return Object 
     */ 
    public Object processObjectValue(String key, Object value, 
            JsonConfig jsonConfig) { 
        return process(value); 
    } 

    /** 
     * process 
     * @param value 
     * @return 
     */ 
    private Object process(Object value) { 
        try { 
            if (value instanceof Float) { 
               return ComUtil.getBigDecimal(Float.parseFloat(String.valueOf(value)));
            } 
            return value == null ? "0.00" : value.toString(); 
        } catch (Exception e) { 
            return ""; 
        } 

    } 

    /** 
     * @return the datePattern 
     */ 
    public String getDatePattern() { 
        return datePattern; 
    } 

    /** 
     * @param pDatePattern the datePattern to set 
     */ 
    public void setDatePattern(String pDatePattern) { 
        datePattern = pDatePattern; 
    } 

} 


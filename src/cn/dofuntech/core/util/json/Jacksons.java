package cn.dofuntech.core.util.json;

import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * <p>
 * json转换通用工具类
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author lxu(Create on 2015年1月29日)
 * @version 1.0
 * @FileName : Jacksons.java
 */
public class Jacksons {

    private CustomObjectMapper objectMapper;

    public static Jacksons me() {
        return new Jacksons();
    }

    private Jacksons() {
        objectMapper = new CustomObjectMapper();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 属性过滤
     * @param filterName 注解名称
     * @param properties 过滤属性
     * @return
     */
    public Jacksons filter(String filterName, String... properties) {
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName, SimpleBeanPropertyFilter.serializeAllExcept(properties));
        objectMapper.setFilters(filterProvider);
        return this;
    }

    /**
     * 属性动态过滤
     * @param target
     * @param mixinSource
     * @return
     */
    public Jacksons addMixInAnnotations(Class<?> target, Class<?> mixinSource) {
        objectMapper.getSerializationConfig().addMixInAnnotations(target, mixinSource);
        objectMapper.getDeserializationConfig().addMixInAnnotations(target, mixinSource);
        return this;
    }

    /**
     * 动态设置日期转换格式
     * @param dateFormat
     * @return
     */
    public Jacksons setDateFormate(DateFormat dateFormat) {
        objectMapper.setDateFormat(dateFormat);
        return this;
    }

    /**
     * json字符串转换为对象
     * @param json
     * @param clazz 对象类型
     * @return
     */
    public <T> T json2Obj(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析json错误");
        }
    }

    /**
     * json字符串转换为对象
     * @param json
     * @param type 对象泛型
     * @return
     */
    public <T> T json2Obj(String json, TypeReference<T> type) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        }
        catch (Exception e) {
            throw new RuntimeException("解析json错误", e);
        }
    }

    /**
     * json字符串转换为对象
     * @param json
     * @param type 动态泛型
     * @return
     */
    public Object json2Obj(String json, ParameterizedType type) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        }
        catch (Exception e) {
            throw new RuntimeException("解析json错误", e);
        }
    }

    /**
     * json 字符串转换 List<Map> 数据结构
     * @param json
     * @return
     */
    public List<Map<String, Object>> json2List(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });
        }
        catch (Exception e) {
            throw new RuntimeException("解析json错误", e);
        }
    }

    /**
     * json 字符串转换 List<Map> 数据结构
     * @param json
     * @return
     */
    public Map<String, Object> json2Map(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        }
        catch (Exception e) {
            throw new RuntimeException("解析json错误", e);
        }
    }

    /**
     * 对象转换json字符串
     * @param obj
     * @return
     */
    public String readAsString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception e) {
            throw new RuntimeException("解析对象错误", e);
        }
    }
}

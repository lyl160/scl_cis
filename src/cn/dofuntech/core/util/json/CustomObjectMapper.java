package cn.dofuntech.core.util.json;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

/**
 * <p>
 * jackson增强型objectMapper 包含日期格式转换 以及动态泛型转换
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author lxu (Create on 2015年1月29日)
 * @version 1.0
 * @FileName : CustomObjectMapper.java
 */

public class CustomObjectMapper extends ObjectMapper {

    private static final String DATE_PATTERN        = "yyyy-MM-dd HH:mm";
    private static final String DATE_PATTERN_SIMPLE = "yyyy-MM-dd";

    public CustomObjectMapper() {

        CustomSerializerFactory factory = new CustomSerializerFactory();

        factory.addGenericMapping(Date.class, new JsonSerializer<Date>() {

            @Override
            public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
                SimpleDateFormat sdfSimple = new SimpleDateFormat(DATE_PATTERN_SIMPLE);
                //
                //                Date date = DateUtils.truncate(value, Calendar.DAY_OF_MONTH);
                //                if (date.getTime() == value.getTime() && !(value instanceof Timestamp)) {
                //                    jsonGenerator.writeString(StringUtils.defaultString(sdfSimple.format(value)));
                //                }
                //                else {
                //                    jsonGenerator.writeNumber(value.getTime());
                //                }
                String pattern = DATE_PATTERN_SIMPLE;
                if (value instanceof Timestamp) {
                    pattern = DATE_PATTERN;
                }

                jsonGenerator.writeString(StringUtils.defaultString(DateFormatUtils.format(value, pattern)));
            }

        });

        this.setSerializerFactory(factory);
    }

    /**
     * 根据泛型 type 进行转换  （适用于根据反射获取方法返回类型场景）
     * @param content json字符串
     * @param type    泛型类型
     * @return
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     */
    public Object readValue(String content, ParameterizedType type) throws IOException, JsonParseException, JsonMappingException {
        return _readMapAndClose(_jsonFactory.createJsonParser(content), _typeFactory._constructType(type, null));
    }
}

package com.zclcs.common.config.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.vertx.codegen.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author zhouc
 */
public class JsonUtil {
    public static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String NORM_DATE_PATTERN = "yyyy-MM-dd";
    public static String NORM_TIME_PATTERN = "HH:mm:ss";
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN, Locale.getDefault())
            .withZone(ZoneId.systemDefault());
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(NORM_DATE_PATTERN, Locale.getDefault())
            .withZone(ZoneId.systemDefault());
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(NORM_TIME_PATTERN, Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    /**
     * 将对象序列化成json字符串
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    @Nullable
    public static String toJson(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        try {
            return getInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw runtime(e);
        }
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param object            javaBean
     * @param serializationView serializationView
     * @return jsonString json字符串
     */
    @Nullable
    public static String toJsonWithView(@Nullable Object object, Class<?> serializationView) {
        if (object == null) {
            return null;
        }
        try {
            return getInstance()
                    .writerWithView(serializationView)
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw runtime(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytes(@Nullable Object object) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw runtime(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytesWithView(@Nullable Object object, Class<?> serializationView) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return getInstance()
                    .writerWithView(serializationView)
                    .writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonString jsonString
     * @return jsonString json字符串
     */
    public static JsonNode readTree(String jsonString) {
        try {
            return getInstance().readTree(Objects.requireNonNull(jsonString, "jsonString is null"));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将InputStream转成 JsonNode
     *
     * @param in InputStream
     * @return jsonString json字符串
     */
    public static JsonNode readTree(InputStream in) {
        try {
            return getInstance().readTree(Objects.requireNonNull(in, "InputStream in is null"));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将java.io.Reader转成 JsonNode
     *
     * @param reader java.io.Reader
     * @return jsonString json字符串
     */
    public static JsonNode readTree(Reader reader) {
        try {
            return getInstance().readTree(Objects.requireNonNull(reader, "Reader in is null"));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param content content
     * @return jsonString json字符串
     */
    public static JsonNode readTree(byte[] content) {
        try {
            return getInstance().readTree(Objects.requireNonNull(content, "byte[] content is null"));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return jsonString json字符串
     */
    public static JsonNode readTree(JsonParser jsonParser) {
        try {
            return getInstance().readTree(Objects.requireNonNull(jsonParser, "jsonParser is null"));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param content   json bytes
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable byte[] content, Class<T> valueType) {
        if (Objects.isNull(content)) {
            return null;
        }
        try {
            return getInstance().readValue(content, valueType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param jsonString jsonString
     * @param valueType  class
     * @param <T>        T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable String jsonString, Class<T> valueType) {
        if (jsonString == null || jsonString.isBlank()) {
            return null;
        }
        try {
            return getInstance().readValue(jsonString, valueType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in        InputStream
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable InputStream in, Class<T> valueType) {
        if (in == null) {
            return null;
        }
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将java.io.Reader反序列化成对象
     *
     * @param reader    java.io.Reader
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable Reader reader, Class<T> valueType) {
        if (reader == null) {
            return null;
        }
        try {
            return getInstance().readValue(reader, valueType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }


    /**
     * 将json反序列化成对象
     *
     * @param content       bytes
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable byte[] content, TypeReference<T> typeReference) {
        if (Objects.isNull(content)) {
            return null;
        }
        try {
            return getInstance().readValue(content, typeReference);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param jsonString    jsonString
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || jsonString.isBlank()) {
            return null;
        }
        try {
            return getInstance().readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in            InputStream
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable InputStream in, TypeReference<T> typeReference) {
        if (in == null) {
            return null;
        }
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将java.io.Reader反序列化成对象
     *
     * @param reader        java.io.Reader
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable Reader reader, TypeReference<T> typeReference) {
        if (reader == null) {
            return null;
        }
        try {
            return getInstance().readValue(reader, typeReference);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param content  bytes
     * @param javaType JavaType
     * @param <T>      T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable byte[] content, JavaType javaType) {
        if (Objects.isNull(content)) {
            return null;
        }
        try {
            return getInstance().readValue(content, javaType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param jsonString jsonString
     * @param javaType   JavaType
     * @param <T>        T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable String jsonString, JavaType javaType) {
        if (jsonString == null || jsonString.isBlank()) {
            return null;
        }
        try {
            return getInstance().readValue(jsonString, javaType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in       InputStream
     * @param javaType JavaType
     * @param <T>      T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable InputStream in, JavaType javaType) {
        if (in == null) {
            return null;
        }
        try {
            return getInstance().readValue(in, javaType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 将java.io.Reader反序列化成对象
     *
     * @param reader   java.io.Reader
     * @param javaType JavaType
     * @param <T>      T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable Reader reader, JavaType javaType) {
        if (reader == null) {
            return null;
        }
        try {
            return getInstance().readValue(reader, javaType);
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * clazz 获取 JavaType
     *
     * @param clazz Class
     * @return MapType
     */
    public static JavaType getType(Class<?> clazz) {
        return getInstance().getTypeFactory().constructType(clazz);
    }

    /**
     * 封装 map type，keyClass String
     *
     * @param valueClass value 类型
     * @return MapType
     */
    public static MapType getMapType(Class<?> valueClass) {
        return getMapType(String.class, valueClass);
    }

    /**
     * 封装 map type
     *
     * @param keyClass   key 类型
     * @param valueClass value 类型
     * @return MapType
     */
    public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return getInstance().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * 封装 map type
     *
     * @param elementClass 集合值类型
     * @return CollectionLikeType
     */
    public static CollectionLikeType getListType(Class<?> elementClass) {
        return getInstance().getTypeFactory().constructCollectionLikeType(List.class, elementClass);
    }

    /**
     * 封装参数化类型
     *
     * <p>
     * 例如： Map.class, String.class, String.class 对应 Map[String, String]
     * </p>
     *
     * @param parametrized     泛型参数化
     * @param parameterClasses 泛型参数类型
     * @return JavaType
     */
    public static JavaType getParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return getInstance().getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 封装参数化类型，用来构造复杂的泛型
     *
     * <p>
     * 例如： Map.class, String.class, String.class 对应 Map[String, String]
     * </p>
     *
     * @param parametrized   泛型参数化
     * @param parameterTypes 泛型参数类型
     * @return JavaType
     */
    public static JavaType getParametricType(Class<?> parametrized, JavaType... parameterTypes) {
        return getInstance().getTypeFactory().constructParametricType(parametrized, parameterTypes);
    }

    /**
     * 读取集合
     *
     * @param content      bytes
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable byte[] content, Class<T> elementClass) {
        if (Objects.isNull(content)) {
            return Collections.emptyList();
        }
        try {
            return getInstance().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content      InputStream
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable InputStream content, Class<T> elementClass) {
        if (content == null) {
            return Collections.emptyList();
        }
        try {
            return getInstance().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param reader       java.io.Reader
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable Reader reader, Class<T> elementClass) {
        if (reader == null) {
            return Collections.emptyList();
        }
        try {
            return getInstance().readValue(reader, getListType(elementClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content      bytes
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable String content, Class<T> elementClass) {
        if (content == null || content.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return getInstance().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content bytes
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable byte[] content) {
        return readMap(content, Object.class);
    }

    /**
     * 读取集合
     *
     * @param content InputStream
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable InputStream content) {
        return readMap(content, Object.class);
    }

    /**
     * 读取集合
     *
     * @param reader java.io.Reader
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable Reader reader) {
        return readMap(reader, Object.class);
    }

    /**
     * 读取集合
     *
     * @param content bytes
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable String content) {
        return readMap(content, Object.class);
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable byte[] content, Class<?> valueClass) {
        return readMap(content, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param content    InputStream
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable InputStream content, Class<?> valueClass) {
        return readMap(content, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param reader     java.io.Reader
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable Reader reader, Class<?> valueClass) {
        return readMap(reader, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable String content, Class<?> valueClass) {
        return readMap(content, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
        if (Objects.isNull(content)) {
            return Collections.emptyMap();
        }
        try {
            return getInstance().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content    InputStream
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
        if (content == null) {
            return Collections.emptyMap();
        }
        try {
            return getInstance().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param reader     java.io.Reader
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable Reader reader, Class<?> keyClass, Class<?> valueClass) {
        if (reader == null) {
            return Collections.emptyMap();
        }
        try {
            return getInstance().readValue(reader, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
        if (content == null || content.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return getInstance().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw runtime(e);
        }
    }

    /**
     * jackson 的类型转换
     *
     * @param fromValue   来源对象
     * @param toValueType 转换的类型
     * @param <T>         泛型标记
     * @return 转换结果
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    /**
     * jackson 的类型转换
     *
     * @param fromValue   来源对象
     * @param toValueType 转换的类型
     * @param <T>         泛型标记
     * @return 转换结果
     */
    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    /**
     * jackson 的类型转换
     *
     * @param fromValue      来源对象
     * @param toValueTypeRef 泛型类型
     * @param <T>            泛型标记
     * @return 转换结果
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return getInstance().convertValue(fromValue, toValueTypeRef);
    }

    /**
     * 判断是否可以序列化
     *
     * @param value 对象
     * @return 是否可以序列化
     */
    public static boolean canSerialize(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        return getInstance().canSerialize(value.getClass());
    }

    /**
     * 判断是否可以反序列化
     *
     * @param type JavaType
     * @return 是否可以反序列化
     */
    public static boolean canDeserialize(JavaType type) {
        return getInstance().canDeserialize(type);
    }

    /**
     * 创建 ObjectNode
     *
     * @return ObjectNode
     */
    public static ObjectNode createObjectNode() {
        return getInstance().createObjectNode();
    }

    /**
     * 创建 ArrayNode
     *
     * @return ArrayNode
     */
    public static ArrayNode createArrayNode() {
        return getInstance().createArrayNode();
    }

    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    /**
     * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
     *
     * @param throwable Throwable
     * @param <T>       泛型标记
     * @return Throwable
     * @throws T 泛型
     */
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T runtime(Throwable throwable) throws T {
        throw (T) throwable;
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper(JsonFactory.builder()
                // 可解析反斜杠引用的所有字符
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                .build());

        static {
            INSTANCE.setLocale(Locale.CHINA);
            INSTANCE.setDateFormat(new SimpleDateFormat(NORM_DATETIME_PATTERN, Locale.CHINA));
            INSTANCE.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            // 单引号
            INSTANCE.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            // 忽略json字符串中不识别的属性
            INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 忽略无法转换的对象
            INSTANCE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            INSTANCE.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER));
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
            INSTANCE.registerModule(javaTimeModule);
            INSTANCE.findAndRegisterModules();
        }
    }

}

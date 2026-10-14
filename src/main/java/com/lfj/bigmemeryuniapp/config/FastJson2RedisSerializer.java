package com.lfj.bigmemeryuniapp.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;

    public FastJson2RedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(
                t,
                JSONWriter.Feature.WriteClassName
        );
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return JSON.parseObject(
                bytes,
                clazz,
                JSONReader.Feature.SupportAutoType
        );
    }
}
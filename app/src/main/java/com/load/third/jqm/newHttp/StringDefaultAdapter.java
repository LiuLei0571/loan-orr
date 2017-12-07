package com.load.third.jqm.newHttp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 用途：为某特定对象设置固定的序列和反序列化
 * 作者：Created by liulei on 2017/12/6.
 * 邮箱：liulei2@aixuedai.com
 */


public class StringDefaultAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.getAsString().equals("")) {

            }
        } catch (Exception e) {
            return null;
        }
        return json.getAsString();
    }

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive((String) src);
    }
}

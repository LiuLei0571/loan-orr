package com.load.third.jqm.newHttp;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 用途：列表解析类，反正服务端返回为一个空的列表
 * 通过注解方式，可以使优先级高于CollectionTypeAdapterFactory，而CollectionTypeAdapterFactory是gson用于解析的list的解析器
 * 作者：Created by liulei on 2017/12/7.
 * 邮箱：liulei2@aixuedai.com
 */


public class ListTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        Class<? super T> rawType = typeToken.getRawType();
        if (!List.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));

        @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
                TypeAdapter<T> result = new ListAdapter(gson, elementType, elementTypeAdapter);
        return result;
    }


    public class ListAdapter<E> extends TypeAdapter<List<E>> {
        private final TypeAdapter<E> elementTypeAdapter;

        public ListAdapter(Gson context, Type elementType, TypeAdapter<?> elementTypeAdapter) {
//            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(
//                    context, elementTypeAdapter, elementType);
            this.elementTypeAdapter = null;
        }


        @Override
        public void write(JsonWriter out, List<E> list) throws IOException {
            if (list == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            for (E element : list) {
                elementTypeAdapter.write(out, element);
            }
            out.endArray();
        }

        @Override
        public List<E> read(JsonReader in) throws IOException {
            //null值返回null
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            //新建一个空的列表
            List<E> list = new ArrayList<>();
            try {
                in.beginArray();
                while (in.hasNext()) {
                    E instance = elementTypeAdapter.read(in);
                    list.add(instance);
                }
                in.endArray();
                //正常解析成为列表
            } catch (IllegalStateException e) { //如果是空字符串，会有BEGIN_ARRAY报错
                //此时尝试解析成字符串，如果不是空字符串，则依旧抛出异常
                //如果是空字符串，则不抛出异常，使最终返回一个空的列表
                if (!"".equals(in.nextString())) {
                    throw e;
                }
            }

            return list;
        }
    }


}

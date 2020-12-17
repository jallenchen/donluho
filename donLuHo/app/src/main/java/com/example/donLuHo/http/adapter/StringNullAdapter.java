package com.example.donLuHo.http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * desc   :Jason数据为null的时候返回空字符串
 */

public class StringNullAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";
        }
        if (reader.peek() == JsonToken.BEGIN_OBJECT) {
//            in.beginObject();
//            in.nextName();
//            in.beginObject();
//            in.nextName();
//            result.warningConditionList.warningCondition = in.nextString();
//            in.endObject();
//            in.endObject();
            return "";
        }

        return reader.nextString();
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        // TODO Auto-generated method stub
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}

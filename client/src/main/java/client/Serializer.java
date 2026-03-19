package client;

import com.google.gson.Gson;

public class Serializer {
    Gson mapper = new Gson();

    public String toJsonSerializer(String value){
        return mapper.toJson(value);
    }
}

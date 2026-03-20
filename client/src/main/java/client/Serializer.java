package client;

import com.google.gson.Gson;

public class Serializer {
    Gson mapper = new Gson();

    public String toJsonSerializer(String value){
        return mapper.toJson(value);
    }

//    public String fromJsonSerializer(java.lang.String value, java.lang.reflect.Type classUsing){
//        return mapper.fromJson(value, classUsing);
//    }
}

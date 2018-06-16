package com.zemberek.model;

import org.elasticsearch.action.index.IndexResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.zemberek.util.clientProvider;


public class dbModel {

    public static final String indexName = "tweet";
    public static final String typeName = "tweets";

    private static Map<String, Object> dataModel(Date createDate, String user, String city, String country, String countryCode, String text){
        Map<String, Object> map = new HashMap<>();

        map.put("date",createDate);
        map.put("user",user);
        map.put("city",city);
        map.put("country",country);
        map.put("countryCode",countryCode);
        map.put("text",text);

        return map;
    }

    public static void insertData(Date createDate, String user, String city, String country, String countryCode, String text){
        IndexResponse response = clientProvider.instance().getClient().prepareIndex(indexName, typeName).
                setSource(dataModel(createDate,user,city,country,countryCode,text)).execute().actionGet();
    }
}

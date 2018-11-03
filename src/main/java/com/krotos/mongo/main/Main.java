package com.krotos.mongo.main;

import com.krotos.mongo.utils.MongoUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoIterable;

public class Main {

    public static void main(String[] args) {
        MongoClient client=MongoUtils.getInstance().getClient();
        MongoIterable<String> databases = client.listDatabaseNames();

        for(String database:databases){
            System.out.println(database);
        }
    }
}

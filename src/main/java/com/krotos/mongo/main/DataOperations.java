package com.krotos.mongo.main;

import com.krotos.mongo.utils.MongoUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.UUID;

public class DataOperations {

    private static MongoClient client=MongoUtils.getInstance().getClient();
    private static MongoDatabase database=client.getDatabase("testy_nasze");
    private static MongoCollection<Document> collection=database.getCollection("wartosci");

    public static void main(String[] args) {
//        insertData();
        printData(20);
        System.out.println("Found:");
        findData("1650d10f-5d24-44f7-9f0a-ec37b4edb5a1");
    }

    private static void insertData(){
        for (int i=0;i<100;i++){
            Document document=new Document("text",UUID.randomUUID().toString())
                    .append("value",Math.random()*1000);
            collection.insertOne(document);
        }
        System.out.println("Added");
    }

    private static void printData(int limit){
        MongoIterable<Document> iterable=collection.find().limit(limit);
        for(Document doc:iterable){
            System.out.println(doc.toJson());
        }
    }

    private static void findData(String text){
        MongoIterable<Document> result=collection.find(Filters.eq("text",text));
        for (Document doc:result){
            System.out.println(doc);
        }
    }
}

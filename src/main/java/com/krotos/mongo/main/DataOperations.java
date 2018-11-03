package com.krotos.mongo.main;

import com.krotos.mongo.utils.MongoUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Optional;
import java.util.UUID;

public class DataOperations {

    private static MongoClient client = MongoUtils.getInstance().getClient();
    private static MongoDatabase database = client.getDatabase("testy_nasze");
    private static MongoCollection<Document> collection = database.getCollection("wartosci");

    public static void main(String[] args) {
//        insertData();
        printData(20);

        System.out.println("Found:");
        findData("1650d10f-5d24-44f7-9f0a-ec37b4edb5a1");

        System.out.println("Found by value:");
        findData(200, 300);

        System.out.println("Updated:");
        update("5bdd65ec09c3424eb8e74116", "updated");
        findData("updated");

        System.out.println("Updated by values");
        update(100, 150, "values between 100 and 150");
        findData(80, 170);

        System.out.println("Find by id");
        findDataById("5bad65ec09c3424eb8e74102");

        System.out.println("Delete");
        delete("5bdd65ec09c3424eb8e74107");
        delete(280,295);
    }

    private static void insertData() {
        for (int i = 0; i < 100; i++) {
            Document document = new Document("text", UUID.randomUUID().toString())
                    .append("value", Math.random() * 1000);
            collection.insertOne(document);
        }
        System.out.println("Added");
    }

    private static void printData(int limit) {
        MongoIterable<Document> iterable = collection.find().limit(limit);
        for (Document doc : iterable) {
            System.out.println(doc.toJson());
        }
    }

    private static void findData(String text) {
        MongoIterable<Document> results = collection.find(Filters.eq("text", text));
        for (Document doc : results) {
            System.out.println(doc);
        }
    }

    private static void findData(double rangeMin, double rangeMax) {
        MongoIterable<Document> results = collection
                .find(Filters.and(
                        Filters.gte("value", rangeMin),
                        Filters.lte("value", rangeMax)));

        for (Document doc : results) {
            System.out.println(doc);
        }
    }

    private static void update(String id, String text) {
        collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.set("text", text));
    }

    private static void update(double rangeMin, double rangeMax, String text) {
        collection.updateMany(
                Filters.and(
                        Filters.gte("value", rangeMin),
                        Filters.lte("value", rangeMax)),
                Updates.set("text", text));
    }

    private static void findDataById(String id) {
        Optional<Document> result = Optional.ofNullable(
                collection.find(
                        Filters.eq("_id", new ObjectId(id)))
                        .first());
        result.ifPresent(System.out::println);
    }

    private static void delete(String id) {
        DeleteResult deleteResult = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        if(deleteResult.getDeletedCount()>0){
            System.out.println("Deleted by id: "+id);
        }
    }

    private static void delete(double rangeMin, double rangeMax) {
        DeleteResult deleteResult = collection.deleteMany(Filters.and(
                Filters.gte("value", rangeMin),
                Filters.lte("value", rangeMax)));
        if(deleteResult.getDeletedCount()>0){
            System.out.println("Range deleted");
        }
    }
}

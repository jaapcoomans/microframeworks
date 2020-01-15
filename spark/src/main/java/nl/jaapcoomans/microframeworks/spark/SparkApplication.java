package nl.jaapcoomans.microframeworks.spark;

import spark.Request;
import spark.Response;

import static spark.Spark.get;

public class SparkApplication {
    public static void main(String[] args) {
        get("/hello", SparkApplication::helloWorld);
        get("/hello/:name", SparkApplication::hello);
    }

    private static String helloWorld(Request request, Response response) {
        return "Hello World!";
    }

    private static String hello(Request request, Response response) {
        return String.format("Hello, %s!", capitalize(request.params("name")));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}

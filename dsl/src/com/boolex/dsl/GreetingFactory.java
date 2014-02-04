/**
 * Created by Alex on 2/4/14.
 * Utterly unnecessary factory to check that the classpath is correct.
 */
package com.boolex.dsl;

public class GreetingFactory {
    public static String getGreeting(String language) throws Exception {
        if(language.equals("en_US")) return "Hello, world!";
        else throw new Exception("No such language: " + language);
    }
}

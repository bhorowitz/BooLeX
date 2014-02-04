/**
 * Created by Alex on 2/4/14.
 * "Hello, world!" to check my build environment's sanity.
 */
package com.boolex.dsl;

public class Hello {
    public static void main(String[] args) {
        try {
            System.out.println(GreetingFactory.getGreeting("en_US"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

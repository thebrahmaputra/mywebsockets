package com.application.utils;

import com.application.domain.Message;

import java.time.LocalTime;

import static java.lang.String.format;

public class Messages {

    public static final String WELCOME_MESSAGE = "Welcome to Java chat";
    public static final String ANNOUNCE_NEW_USER = "%s just entered the room";
    public static final String ANNOUNCE_LEAVER = "%s just left the room. We'll miss you";

    public static String personalize(String message, String... args){
        return format(message, args);
    }

    public static Message objectify(String content/*, String... args*/){
        return objectify(content, "Duke Bot"/*, LocalTime.now().toString(), args*/);
    }

    public static Message objectify(String content, String sender/*, String tm*/){
        return objectify(content, sender, LocalTime.now().toString());
    }

    public static Message objectify(String content, String sender, String received, String... args){
        return new Message(personalize(content, args), sender, received);
    }
}

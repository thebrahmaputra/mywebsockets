package com.application.infra;

import javax.json.Json;

public class JsonUtils {
    public static String formatMessage(String message, String user){
        return Json.createObjectBuilder()
                .add("message", message)
                .add("sender", user)
                .add("received", "")
                .build().toString();
    }
}

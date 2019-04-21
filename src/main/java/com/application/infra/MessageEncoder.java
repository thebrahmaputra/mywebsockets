package com.application.infra;

import com.application.domain.Message;

import javax.json.Json;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * author: mepratikbidwai
 */
public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(final Message message) {
        return Json.createObjectBuilder()
                .add("content", message.getContent())
                .add("sender", message.getSender())
                .add("received", message.getReceived())
                .build().toString();
    }

    @Override
    public void init(EndpointConfig config){

    }

    @Override
    public void destroy(){

    }
}

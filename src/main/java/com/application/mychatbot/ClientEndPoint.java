package com.application.mychatbot;

import com.application.domain.Message;
import com.application.infra.MessageDecoder;
import com.application.infra.MessageEncoder;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import java.text.SimpleDateFormat;

@ClientEndpoint (encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndPoint {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnMessage
    public void onMessage(Message message){
        System.out.println(String.format("[%s:%s] %s", simpleDateFormat
        .format(message.getReceived()), message.getSender(), message.getContent()));
    }
}

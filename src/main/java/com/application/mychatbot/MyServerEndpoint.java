package com.application.mychatbot;

import com.application.domain.Message;
import com.application.infra.MessageDecoder;
import com.application.infra.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@ServerEndpoint(value = "/chat",  encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class MyServerEndpoint {
    private static Set<Session>  peers = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session){

        System.out.println(format("%s joined the chat room", session.getId()));
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        System.out.print(session);
        System.out.println("User : " + message.getSender() +"[" + message.getReceived() +
                " ]" + "Server onMessage : " +message.getContent());
        for(Session peer: peers){
            if(!session.getId().equals(peer.getId())){
                peer.getBasicRemote().sendObject(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException,  EncodeException{
        System.out.println(format("Mr./Ms. %s left the chat room", session.getId()));
        peers.remove(session);
        //broadcast the user left message
        for(Session peer : peers){
            Message message = new Message();
            message.setSender("Chat_Server");
            message.setContent(format("Mr./Ms. %s left the chat room", (String)session.getUserProperties().get("user")));
            message.setReceived((new Date()).toString());
            peer.getBasicRemote().sendObject(message);
        }
    }
}

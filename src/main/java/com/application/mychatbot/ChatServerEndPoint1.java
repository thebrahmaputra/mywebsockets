package com.application.mychatbot;

import com.application.domain.Message;
import com.application.domain.Room;
import com.application.infra.MessageDecoder;
import com.application.infra.MessageEncoder;
import com.application.utils.Messages;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.String.format;

@ServerEndpoint(value = "/chat/{roomName}/{userName}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatServerEndPoint1 {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    public static final Logger log = Logger.getLogger(ChatServerEndPoint1.class.getSimpleName());

    private static final Map<String, Room> rooms = Collections.synchronizedMap(new HashMap<String, Room>());

    private static final String[] roomNames = {"JavaSE", "JavaEE", "WebSocket", "JSON"};

    @PostConstruct
    public void intialise(){
        //Arrays.stream(roomNames).forEach(roomName -> rooms.computeIfAbsent(roomName, key -> new Room(roomName)));
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomName") final String roomName, @PathParam("userName") final String userName) throws IOException, EncodeException {
        Arrays.stream(roomNames).forEach(roomNam -> rooms.computeIfAbsent(roomNam, key -> new Room(roomNam)));
        session.setMaxIdleTimeout(5*60*1000);
        session.getUserProperties().putIfAbsent("roomName", roomName);
        session.getUserProperties().putIfAbsent("userName", userName);
        Room room = rooms.get(roomName);
        room.join(session);
        System.out.println(rooms.get(roomName));
        System.out.println(format("%s joined the chat room ", session.getId()));
        session.getBasicRemote().sendObject(Messages.objectify(Messages.WELCOME_MESSAGE));
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        for(Session peer : peers){
            if(!session.getId().equals(peer.getId())){
                peer.getBasicRemote().sendObject(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println(format("%s left the chat room due to some reasons", session.getId()));
        for (Session peer : peers){
            Message message = new Message();
            message.setSender("ChatServer");
            message.setContent(format("%s have left the chat room", (String) session.getUserProperties().get("User")));
            message.setReceived((new Date()).toString());
            peer.getBasicRemote().sendObject(message);
        }
        peers.remove(session);
    }
}

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
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{roomName}/{userName}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatServerEndpoint {

    public static final Logger log = Logger.getLogger(ChatServerEndpoint.class.getSimpleName());

    private static final Map<String, Room> rooms = Collections.synchronizedMap(new HashMap<String, Room>());

    private static final String[] roomNames = {"JavaSE", "JavaEE", "WebSocket", "JSON"};

    //private Session session;

    @PostConstruct
    public void intialise(){
        Arrays.stream(roomNames).forEach(roomName -> rooms.computeIfAbsent(roomName, new Room(roomName)));
    }

    @OnOpen
    public void onOpen(final Session session,
                       @PathParam("roomName") final String roomName,
                       @PathParam("userName") final String userName) throws IOException, EncodeException {
        session.setMaxIdleTimeout(5 * 60 * 1000);
        System.out.println(roomName);
        System.out.println(userName);
        session.getUserProperties().putIfAbsent("roomName", roomName);
        session.getUserProperties().putIfAbsent("userName", userName);
        Room room = rooms.get(roomName);
        room.join(session);
        System.out.println(rooms.get(roomName));
        session.getBasicRemote().sendObject(Messages.objectify(Messages.WELCOME_MESSAGE));
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
        //rooms.get(extractRoomFrom(session)).sendMessage(message);
        session.getBasicRemote().sendText("From Server : "+message.getContent());
        //System.out.println(message.getContent());
    }

    private String extractRoomFrom(Session session) {
        return ((String) session.getUserProperties().get("roomName"));
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message, Session session) {
        // Not implemented
    }

    @OnMessage
    public void onPongMessage(PongMessage message, Session session) {
        // Not implemented
    }

    @OnClose
    public void onClose(Session session, CloseReason reason){
        rooms.get(extractRoomFrom(session)).leave(session);
        log.info(reason::getReasonPhrase);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        //
    }



    /**
     * Returns the list of rooms in chat application
     * @return Map of roomnames to the room instance
     */
    static Map<String, Room> getRooms(){
        return rooms;
    }
}

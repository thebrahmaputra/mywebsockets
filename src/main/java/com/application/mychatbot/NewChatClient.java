package com.application.mychatbot;

import com.application.domain.Message;
import com.application.utils.Messages;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.Session;
import java.net.URI;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

public class NewChatClient {
    public static void main (String[] args) throws Exception{
        ClientManager clientManager = ClientManager.createClient();

        String message;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to tiny chat");
        System.out.println("What's your name?");
        String user = scanner.nextLine();
        System.out.println("Enter room name");
        String myRoom = scanner.nextLine();
        Session session = clientManager.connectToServer(MyClientEndPoint.class, new URI("ws://localhost:8025/ws/chat/"+ myRoom + "/" + user));

        System.out.println("Your are logged in as user : " +user);
        do {
            message = scanner.nextLine();
            session.getBasicRemote().sendObject(Messages.objectify(message, user, (LocalTime.now().toString())));
        }while (!message.equalsIgnoreCase("quit"));

        session.close();
    }
}

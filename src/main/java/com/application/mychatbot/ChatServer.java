package com.application.mychatbot;

import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.util.Scanner;

public class ChatServer {
    public static void main(String [] args){
        Server server = new Server("localhost", 8025, "/ws", ChatServerEndPoint1.class);
        try {
            server.start();
            System.out.println("Press a button to exit server...");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}

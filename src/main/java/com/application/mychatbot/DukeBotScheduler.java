package com.application.mychatbot;

import com.application.utils.Messages;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
public class DukeBotScheduler {

    @Schedule(minute = "*/20", hour = "*")
    private void interrupt() {
        //ChatServerEndpoint.getRooms().forEach((s, room) -> room.sendMessage(Messages.objectify("Hello from duke bot")));
    }
}

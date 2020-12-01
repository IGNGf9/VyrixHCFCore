/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.hcriots.hcfactions.chat;

import lombok.Getter;
import net.hcriots.hcfactions.Hulu;
import net.hcriots.hcfactions.chat.listeners.ChatListener;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatHandler {

    @Getter
    private static final AtomicInteger publicMessagesSent = new AtomicInteger();

    public ChatHandler() {
        Hulu.getInstance().getServer().getPluginManager().registerEvents(new ChatListener(), Hulu.getInstance());
    }

}
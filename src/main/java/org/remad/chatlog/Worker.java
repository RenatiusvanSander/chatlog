package org.remad.chatlog;

import org.remad.chatlog.log.ChatLogger;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Works so that a message is written to chat-log.
 * @author Remy Meier
 */
public class Worker extends Thread implements Runnable {

    /**
     * Creates a new instance of Worker.
     * @param name The name of the worker (Thread).
     * @param chatLogger The shared ChatLogger instance between worker instances.
     */
    public Worker(String name, ChatLogger chatLogger) {
        setName(name);
        this.chatLogger = chatLogger;
    }

    /**
     * Runs the worker instance.
     */
    @Override
    public void run() {
        while(true) {
            try {
                chatLogger.writeSynchronizedChatLogMessage(
                        LocalDateTime.now().toInstant(ZoneOffset.UTC).toString() + " " + getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private final ChatLogger chatLogger;
}

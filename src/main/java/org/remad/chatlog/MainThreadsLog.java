package org.remad.chatlog;

import org.remad.chatlog.log.ChatLogger;

/**
 * Starts several threads and logs the chat without dead-lock.
 *
 * @author Remy Meier
 */
public class MainThreadsLog {

    public static int MAX = 1000000;

    public static void main(String[] args) throws InterruptedException {
        ChatLogger chatLogger = ChatLogger.createChatLogger("logs", "chats.log");

        Worker worker1 = new Worker("worker1", chatLogger);
        Worker worker2 = new Worker("worker2", chatLogger);
        Worker worker3 = new Worker("worker3", chatLogger);
        Worker worker4 = new Worker("worker4", chatLogger);

        worker1.start();
        worker2.start();
        worker3.start();
        worker4.start();

        for(int i = 0; i < MAX; i++) {
            System.out.println("Threads slept " + i + " times.");
            worker1.sleep(0, 2);
            worker2.sleep(0,2);
            worker3.sleep(0,2);
            worker4.sleep(0,2);
        }
    }
}

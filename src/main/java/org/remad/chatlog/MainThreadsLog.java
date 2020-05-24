package org.remad.chatlog;

import org.remad.chatlog.log.ChatLogger;

/**
 * Starts several threads and logs the chat without dead-lock.
 * @author Remy Meier
 */
public class MainThreadsLog {

    public static int MAX = 10000;

    public static void main(String[] args) throws InterruptedException {
        ChatLogger chatLogger = ChatLogger.createChatLogger("logs", "chats.log");
        Worker[] workers = new Worker[150];

        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker" + (i + 1), chatLogger);
            workers[i].start();
        }

        for (int i = 1; i < MAX; i++) {
            for(Worker worker : workers) {
                worker.sleep(0,1);
            }
            System.out.println("150 workers slept " + i + " times.");
        }

        for(Worker worker : workers) {
            worker.terminate();
        }
    }
}

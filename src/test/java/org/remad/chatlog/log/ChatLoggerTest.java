package org.remad.chatlog.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.remad.chatlog.Worker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ChatLogger}.
 */
public class ChatLoggerTest {

    private static final String PATH = "logs";
    private static final String FULL_FILENAME = "logs" + File.separator + "chats.log";
    private static final String WORKER_NAME = "worker";

    private final CountDownLatch lock = new CountDownLatch(1);

    @BeforeEach
    public void cleanChatLogDirectory() throws IOException {
        Files.deleteIfExists(Paths.get(FULL_FILENAME));
        Files.deleteIfExists(Paths.get(PATH));
    }

    @Test
    public void createChatLoggerTest() {
        String path = "logs", filename = "chats.log";

        ChatLogger logger = ChatLogger.createChatLogger(path, filename);

        assertEquals("logs", logger.getChatLogPath());
        assertEquals("chats.log", logger.getChatLogFilename());
        assertTrue(Files.exists(Paths.get(path)));
        assertTrue(Files.exists(Paths.get(path + File.separator + filename)));
    }

    @Test
    public void writeChatLogMessageIsWrittenTest() throws IllegalAccessException {
        String path = "logs", filename = "chats.log", chatLogMessage = "Test message!";
        ChatLogger logger = ChatLogger.createChatLogger(path, filename);
        boolean errorFalse = logger.writeChatLogMessage(chatLogMessage);
        assertFalse(errorFalse);
    }

    @Test
    public void setChatLogPathCorrectSetTest() {
        ChatLogger logger = new ChatLogger();
        logger.setChatLogPath(PATH);
        assertEquals(PATH, logger.getChatLogPath());
    }

    @Test
    public void setChatLogFilenameCorrectSetTest() {
        ChatLogger logger = new ChatLogger();
        logger.setChatLogFilename("chats.log");
        assertEquals("chats.log", logger.getChatLogFilename());
    }

    @Test
    public void writeSynchronizedChatLogMessageCorrectTest() throws InterruptedException, IOException {
        ChatLogger logger = ChatLogger.createChatLogger("logs", "chats.log");
        Worker[] workers = createAndStart150Workers(logger);
        Timer scheduledWorkersStop = createScheduledTimerWithTask(workers);

        lock.await(6, TimeUnit.MINUTES);

        FileInputStream fileInputStream = new FileInputStream(new File("logs/chats.log"));
        assertTrue(fileInputStream.readAllBytes().length > 0);
    }

    private Timer createScheduledTimerWithTask(Worker[] workers) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private final Worker[] workerThreads = workers;

            @Override
            public void run() {
                for (Worker worker : workerThreads) {
                    worker.terminate();
                }
            }
        };
        timer.schedule(task, 300000L);
        return timer;
    }

    private Worker[] createAndStart150Workers(ChatLogger logger) {
        Worker[] workers = new Worker[150];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker(WORKER_NAME + (i + 1), logger);
            workers[i].start();
        }
        return workers;
    }
}
package org.remad.chatlog;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.remad.chatlog.log.ChatLogger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Thread.State.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Worker}
 */
public class WorkerTest {

    private static final String PATH = "logs";
    private static final String FILENAME = "chats.log";
    private static final String FULL_FILENAME = PATH + File.separator + FILENAME;
    private static final String NAME = "worker1";
    private static final ChatLogger CHAT_LOGGER = ChatLogger.createChatLogger(PATH, FILENAME);

    @AfterAll
    private static void tearDown() throws Exception {
        CHAT_LOGGER.close();
        Files.deleteIfExists(Paths.get(FULL_FILENAME));
        Files.deleteIfExists(Paths.get(PATH));
    }

    @Test
    public void runWorkerTest() throws InterruptedException {
        Worker worker = new Worker(NAME, CHAT_LOGGER);
        worker.start();

        assertEquals(NAME, worker.getName());
        assertTrue(worker.isAlive());
        assertFalse(worker.isDaemon());
        assertFalse(worker.isInterrupted());
        assertEquals(RUNNABLE, worker.getState());
    }

    @Test
    public void terminateWorkerTest() {
        Worker worker = new Worker(NAME, CHAT_LOGGER);

        worker.start();
        worker.terminate();

        assertEquals(NAME, worker.getName());
        assertFalse(worker.isAlive());
        assertFalse(worker.isDaemon());
        assertFalse(worker.isInterrupted());
        assertEquals(TERMINATED, worker.getState());
    }
}
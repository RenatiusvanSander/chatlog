package org.remad.chatlog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Unit tests for {@link MainThreadsLog}.
 */
class MainThreadsLogTest {

    private static final String PATH = "logs";
    private static final String FILENAME = "chats.log";
    private static final String FULL_FILENAME = PATH + File.separator + FILENAME;

    @AfterEach
    private void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(FULL_FILENAME));
        Files.deleteIfExists(Paths.get(PATH));
    }

    @Test
    public void runMainTest() throws InterruptedException {
        String[] args = new String[]{ "", "" };
        MainThreadsLog.main(args);
    }
}
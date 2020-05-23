package org.remad.chatlog.log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logs the chat messages into a define chatlog file.
 * ToDo refactor with builder pattern for chaining.
 * @author Remy Meier
 */
public class ChatLogger implements AutoCloseable {

    /**
     * Standard ctor.
     */
    public ChatLogger() {
    }

    /**
     * Factory method so that a new ChatLogger instance is created.
     * @param path     The full qualified path name without file.
     * @param filename The full qualified filename, means with extension of file e.g txt.
     * @return The created ChatLogger
     */
    public static ChatLogger createChatLogger(String path, String filename) {
        try {
            ChatLogger logger = new ChatLogger();
            logger.setChatLogPath(path);
            logger.setChatLogFilename(filename);
            logger.initLogger();
            return logger;
        } catch (Exception e) {
            throw new IllegalStateException("The logger is not created, because of an illegal state.", e);
        }
    }

    /**
     * Closes the file stream.
     * @throws Exception In case the stream to file is not correct closed.
     */
    @Override
    public void close() throws Exception {
        chatLogPath = null;
        chatLogFilename = null;
        if (logWriter != null) {
            logWriter.close();
        }
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
    }

    /**
     * @return The path of log directory.
     */
    public String getChatLogPath() {
        return chatLogPath;
    }

    /**
     * Sets the full qualified log path.
     * @param chatLogPath The full qualified log path.
     */
    public void setChatLogPath(String chatLogPath) {
        this.chatLogPath = chatLogPath;
    }

    /**
     * @return Gets full log filename
     */
    public String getChatLogFilename() {
        return chatLogFilename;
    }

    /**
     * Sets log filename
     * @param chatLogFilename The full qualified filename, contains name and extension. For example filename.txt.
     */
    public void setChatLogFilename(String chatLogFilename) {
        this.chatLogFilename = chatLogFilename;
    }

    /**
     * Writes message into chat log file.
     * @param message The message to write.
     * @return In case there is no write error {@code true}. In case there exist a write error {@code false}.
     * @throws IllegalAccessException {@link IllegalAccessException} - In case an error happens on writing to chat log.
     */
    public boolean writeChatLogMessage(String message) throws IllegalAccessException {
        if (fileOutputStream == null || logWriter == null) {
            // Illegal accessed when file strem is null or logWriter is null.
            throw new IllegalAccessException("Init the chatlogger instance first.");
        }
        logWriter.write(message);
        return logWriter.checkError();
    }

    /**
     * Writes message thread safe into chat log file.
     * @param message The message to write.
     * @throws IllegalAccessException {@link IllegalAccessException} - In case an error happens on writing to chat log.
     */
    public synchronized void writeSynchronizedChatLogMessage(String message) throws IllegalAccessException {
        if (fileOutputStream == null || logWriter == null) {
            // Illegal accessed when file stream is null or logWriter is null.
            throw new IllegalAccessException("Init the chatlogger instance first.");
        }
        logWriter.println(message);
    }

    /**
     * @return In case there is no write error {@code false}. In case there exist a write error {@code true}.
     * @throws IllegalAccessException IllegalAccessException {@link IllegalAccessException} - In case an error happens
     * on writing to chat log.
     */
    public boolean checkError() throws IllegalAccessException {
        if (fileOutputStream == null || logWriter == null) {
            // Illegal accessed when file strem is null or logWriter is null.
            throw new IllegalAccessException("Init the chatlogger instance first.");
        }
        return logWriter.checkError();
    }

    /**
     * @throws Exception {@link FileNotFoundException} - If the file is not found or created.
     * In case when logWriter creation ends in IOException.
     */
    private void initLogger() throws Exception {
        createDirectory();
        if (fileOutputStream == null && logWriter == null && !getChatLogFilename().isEmpty() && !getChatLogPath().isEmpty()) {
            // Create file and logWriter when fileOutputStream is null, chatLogFilename and chatLogPath are not null.
            createFileStream();
            logWriter = new PrintWriter(fileOutputStream, true);
        } else if (fileOutputStream != null && logWriter != null && (getChatLogFilename().isEmpty() || getChatLogPath().isEmpty())) {
            // Flushes and closes file stream and set to null to restart creation
            close();
            createFileStream();
            logWriter = new PrintWriter(fileOutputStream, true);
        }
    }

    private void createDirectory() {
        if(Files.notExists(Paths.get(getChatLogPath())) ) {
            File path = new File(getChatLogPath());
            path.mkdirs();
        }
    }

    private void createFileStream() throws FileNotFoundException {
        String fullFilename = getChatLogPath() + File.separator + getChatLogFilename();
        fileOutputStream = new FileOutputStream(fullFilename);
    }

    private FileOutputStream fileOutputStream;
    private PrintWriter logWriter;
    private String chatLogPath;
    private String chatLogFilename;
}

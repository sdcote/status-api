package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppState {
    public static final String UNKNOWN = "unknown";
    private static final String TEMP_FILE = "app.state";
    private static final String STANDBY = "stand-by";
    private static final File STATE_FILE = new File(System.getProperty("java.io.tmpdir"), TEMP_FILE);
    public static Logger LOGGER = LoggerFactory.getLogger(AppState.class);
    private static String currentState;

    public static String getState() {
        if (StringUtils.isEmpty(currentState))
            return UNKNOWN;
        else
            return currentState;
    }

    public static void setState(String state) throws IllegalArgumentException {
        // Validate and set
        writeState(state);
    }

    public static void init() {
        currentState = readState();
    }

    private static String readState() {
        String retval = STANDBY;
        try {
            String data = new String(Files.readAllBytes(Paths.get(STATE_FILE.getAbsolutePath())), Charset.defaultCharset());
            AppState.LOGGER.debug("READING: " + STATE_FILE.getAbsolutePath());
            retval = data.trim();
        } catch (Throwable t) {
            AppState.LOGGER.error("Could not read state from " + STATE_FILE.getAbsolutePath() + " - " + t.getMessage() + " - setting state to '" + retval + "'");
            writeState(retval); // write the state correctly so we start up without error
        }
        return retval;
    }

    private static void writeState(String state) {
        try (PrintWriter out = new PrintWriter(new FileWriter(STATE_FILE))) {
            AppState.LOGGER.debug("Updating status to " + state + " in " + STATE_FILE.getAbsolutePath());
            out.print(state);
        } catch (Throwable t) {
            AppState.LOGGER.error("Could not write state to " + STATE_FILE.getAbsolutePath() + " - " + t.getMessage());
        }

    }

    public static String getCurrentUsername() {
        String retval;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            retval = ((UserDetails)principal).getUsername();
        } else {
            retval = principal.toString();
        }
        return retval;
    }
}

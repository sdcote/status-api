package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppStatus {
    private static final String TEMP_FILE = "app.state";
    private static final File STATUS_FILE = new File(System.getProperty("java.io.tmpdir"), TEMP_FILE);
    public static Logger LOGGER = LoggerFactory.getLogger(AppStatus.class);
    private static Status currentStatus;

    public static String getStatus() {
        if (StringUtils.isEmpty(currentStatus))
            return Status.STANDBY.toString();
        else
            return currentStatus.toString();
    }

    public static void setStatus(String status) throws IllegalArgumentException {
       ;currentStatus =  Status.getStatus(status);
        writeStatus(currentStatus);
    }

    public static void init() {
        currentStatus = readStatus();
    }

    private static Status readStatus() {
        Status retval = Status.STANDBY;
        try {
            String data = new String(Files.readAllBytes(Paths.get(STATUS_FILE.getAbsolutePath())), Charset.defaultCharset());
            AppStatus.LOGGER.debug("READING: " + STATUS_FILE.getAbsolutePath());
            retval = Status.getStatus(data.trim());
        } catch (Throwable t) {
            AppStatus.LOGGER.error("Could not read status from " + STATUS_FILE.getAbsolutePath() + " - " + t.getMessage() + " - setting status to '" + retval + "'");
            writeStatus(retval); // write the status correctly so we start up without error
        }
        return retval;
    }

    private static void writeStatus(Status status) {
        try (PrintWriter out = new PrintWriter(new FileWriter(STATUS_FILE))) {
            AppStatus.LOGGER.debug("Updating status to " + status + " in " + STATUS_FILE.getAbsolutePath());
            out.print(status.toString());
        } catch (Throwable t) {
            AppStatus.LOGGER.error("Could not write status to " + STATUS_FILE.getAbsolutePath() + " - " + t.getMessage());
        }

    }


    /**
     * There are only 3 status values, all others are set to "stand-by"
     */
    public enum Status {
        READY("ready"),
        STAGE("stage"),
        STANDBY("stand-by");
        private String value;

        Status(String status) {
            this.value = status;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Status getStatus(String status){
            Status retval = STANDBY;
            if( READY.toString().equalsIgnoreCase(status)){
                retval = READY;
            } else if( STAGE.toString().equalsIgnoreCase(status)) {
                retval = STAGE;
            }
            return retval;
        }
    }
}

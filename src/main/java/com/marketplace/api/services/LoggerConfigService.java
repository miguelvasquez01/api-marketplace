package com.marketplace.api.services;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfigService {

    private static LoggerConfigService instance;
    private FileHandler fileHandler;

    private LoggerConfigService() {
        try {
            fileHandler = new FileHandler("app_logs.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            Logger.getLogger(LoggerConfigService.class.getName()).log(Level.SEVERE, "Error al configurar el FileHandler", e);
        }
    }

    public static LoggerConfigService getInstance() {
        if (instance == null) {
            instance = new LoggerConfigService();
        }
        return instance;
    }

    public void configureLogger(Logger logger) {
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
    }
}

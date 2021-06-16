package com.example.zemogatest.common.settings;

import javax.inject.Inject;

/**
 * Typical settings for app.
 *
 * @author n.diazgranados
 */
public class Settings {
    /**
     * Current environment name.
     */
    private String baseUrl;
    private Log log;
    private int serviceTimeout;
    private int connectionTimeout;

    @Inject
    public Settings() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Gets current log level. Will return default value if none defined?
     */
    public int getLoggingLevel() {
        if (log != null) {
            return log.level;
        }
        return 0; //Default level???
    }

    /**
     * Sets current Logging Level.
     */
    public void setLoggingLevel(int newLevel) {
        if (log == null) {
            log = new Log();
        }
        log.level = newLevel;
    }


    /**
     * Get default service timeout.
     */
    public int getServiceTimeOut() {
        return serviceTimeout;
    }

    /**
     * Get default connection timeout.
     */
    public int getConnectionTimeOut() {
        return connectionTimeout;
    }

    private class Log {
        int level;
    }
}

package ru.lagoshny.task.manager.web.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

/**
 * Defining a single format to represent server errors to clients.
 */
public class ServerError {

    /**
     * HTTP error status.
     */
    private HttpStatus status;

    /**
     * Time when an error occurred.
     */
    private Date timestamp;

    /**
     * Url path where an error occurred.
     */
    private String path;

    /**
     * List of error messages.
     */
    private List<String> messages;

    private ServerError(HttpStatus status, String path, Date timestamp, List<String> messages) {
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
        this.messages = messages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpStatus status;
        private Date timestamp;
        private String path;
        private List<String> messages;

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder messages(List<String> messages) {
            this.messages = messages;
            return this;
        }

        public ServerError build() {
            return new ServerError(status, path, timestamp, messages);
        }

    }

    public HttpStatus getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("status", status)
                .append("timestamp", timestamp)
                .append("path", path)
                .append("messages", messages)
                .toString();
    }
}

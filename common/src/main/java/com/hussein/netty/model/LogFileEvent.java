package com.hussein.netty.model;

import java.net.InetSocketAddress;

/**
 * <p>Title: LogFileEvent</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/9 6:09 PM
 */
public class LogFileEvent {

    public static final String SEPARATOR = ":";

    private InetSocketAddress source;

    private Long receivedTimestamp;

    private String fileName;

    private String msg;

    public LogFileEvent(String fileName, String msg) {
        this(null, -1L, fileName, msg);
    }

    public LogFileEvent(InetSocketAddress source, Long receivedTimestamp, String fileName, String msg) {
        this.source = source;
        this.receivedTimestamp = receivedTimestamp;
        this.fileName = fileName;
        this.msg = msg;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public Long getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(receivedTimestamp);
        sb.append("[").append(source).append("]");
        sb.append("[").append(fileName).append("]");
        sb.append(":").append(msg);
        return sb.toString();
    }
}

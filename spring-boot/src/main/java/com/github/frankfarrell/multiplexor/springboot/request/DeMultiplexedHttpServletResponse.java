package com.github.frankfarrell.multiplexor.springboot.request;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * Created by frankfarrell on 23/02/2018.
 */
public class DeMultiplexedHttpServletResponse implements HttpServletResponse {

    private int statusCode;
    private String statusMessage;
    private final CountDownLatch writersCountDownLatch;
    private boolean isCommitted = false;
    private String responseBody;
    private PrintWriter writer;
    private ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();

    public DeMultiplexedHttpServletResponse(CountDownLatch latch) {
        this.writersCountDownLatch = latch;
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    @Override
    public void sendError(int sc) throws IOException {

    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    @Override
    public void addHeader(String name, String value) {

    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    @Override
    public void setStatus(final int status) {
        statusCode = status;
    }

    @Override
    @Deprecated
    public void setStatus(final int status, final String message) {
        statusCode = status;
        statusMessage = message;
    }

    @Override
    public int getStatus() {
        return statusCode;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return Collections.emptySet();
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            private WriteListener listener;

            @Override
            public boolean isReady() {
                return true;
            }


            @Override
            public void setWriteListener(WriteListener writeListener) {
                if (writeListener != null) {
                    try {
                        writeListener.onWritePossible();
                    } catch (IOException e) {
                    }

                    listener = writeListener;
                }
            }


            @Override
            public void write(int b) throws IOException {
                try {
                    bodyOutputStream.write(b);
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }


            @Override
            public void close()
                    throws IOException {
                super.close();
                flushBuffer();
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (null == writer) {
            writer = new PrintWriter(new OutputStreamWriter(bodyOutputStream, StandardCharsets.UTF_8));
        }
        return writer;
    }


    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long length) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int i) {
        bodyOutputStream = new ByteArrayOutputStream(i);
    }


    @Override
    public int getBufferSize() {
        return bodyOutputStream.size();
    }


    @Override
    public void flushBuffer() throws IOException {
        if (null != writer) {
            writer.flush();
        }
        responseBody = new String(bodyOutputStream.toByteArray(), StandardCharsets.UTF_8);
        isCommitted = true;
        writersCountDownLatch.countDown();
    }


    @Override
    public void resetBuffer() {
        bodyOutputStream = new ByteArrayOutputStream();
    }


    @Override
    public boolean isCommitted() {
        return isCommitted;
    }


    @Override
    public void reset() {
        responseBody = null;
        bodyOutputStream = new ByteArrayOutputStream();
    }


    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    public String getAwsResponseBodyString() {
        return responseBody;
    }
}

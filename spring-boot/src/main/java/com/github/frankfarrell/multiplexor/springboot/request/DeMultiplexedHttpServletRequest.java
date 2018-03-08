package com.github.frankfarrell.multiplexor.springboot.request;

import com.github.frankfarrell.multiplexor.springboot.model.MultiplexorRequest;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

/**
 * Created by ffarrell on 23/02/2018.
 */
public class DeMultiplexedHttpServletRequest implements HttpServletRequest {

    private final MultiplexorRequest multiplexorRequest;

    public DeMultiplexedHttpServletRequest(final MultiplexorRequest multiplexorRequest) {
        this.multiplexorRequest = multiplexorRequest;
    }


    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return multiplexorRequest.headers.map(stringStringMap -> stringStringMap.get(name)).orElse(null);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return multiplexorRequest.headers.map(value -> Collections.enumeration(value.keySet())).orElse(Collections.emptyEnumeration());
    }

    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    @Override
    public String getMethod() {
        return multiplexorRequest.method.name();
    }

    @Override
    public String getPathInfo() {
        return multiplexorRequest.path;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getQueryString() {
        return ""; //TODO
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return cleanUri(multiplexorRequest.path);
    }


    @Override
    public StringBuffer getRequestURL() {
        String url = "";
        url += getServerName();
        url += cleanUri(multiplexorRequest.path);

        return new StringBuffer(getScheme() + "://" + url);
    }

    private String cleanUri(String uri) {
        String finalUri = uri;

        if (!finalUri.startsWith("/")) {
            finalUri = "/" + finalUri;
        }

        if (finalUri.endsWith(("/"))) {
            finalUri = finalUri.substring(0, finalUri.length() - 1);
        }

        finalUri = finalUri.replaceAll("/+", "/");

        return finalUri;
    }

    @Override
    public String getServletPath() {
        return "";
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return getHeader("content-type") != null? getHeader("content-type") : "application/json";
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!multiplexorRequest.body.isPresent()) {
            return null;
        }
        else{
            final byte[] bodyBytes = multiplexorRequest.body.get().getBytes(StandardCharsets.UTF_8);
            return new MultiplexorInputStream(new ByteArrayInputStream(bodyBytes));
        }
    }

    public static final class MultiplexorInputStream extends ServletInputStream {

        private InputStream bodyStream;
        private ReadListener listener;

        public MultiplexorInputStream(InputStream body) {
            bodyStream = body;
        }


        @Override
        public boolean isFinished() {
            return true;
        }


        @Override
        public boolean isReady() {
            return true;
        }


        @Override
        public void setReadListener(ReadListener readListener) {
            listener = readListener;
            try {
                listener.onDataAvailable();
            } catch (IOException e) {
                //log.error("Data not available on input stream", e);
            }
        }


        @Override
        public int read()
                throws IOException {
            int readByte = bodyStream.read();
            if (bodyStream.available() == 0 && listener != null) {
                listener.onAllDataRead();
            }
            return readByte;
        }

    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}

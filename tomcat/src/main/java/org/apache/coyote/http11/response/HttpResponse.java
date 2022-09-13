package org.apache.coyote.http11.response;

import java.util.Optional;
import org.apache.coyote.http11.request.HttpHeaders;
import org.apache.coyote.http11.web.Cookie;

public class HttpResponse {

    private final HttpHeaders httpHeaders;
    private StatusLine statusLine;
    private String body;
    private String viewName;

    public HttpResponse() {
        statusLine = new StatusLine(HttpStatus.OK);
        httpHeaders = HttpHeaders.makeEmptyHeader();
        body = "";
        viewName = null;
    }

    public String makeResponse() {
        final StringBuilder sb = new StringBuilder();

        sb.append(statusLine.getStatusLine()).append(" \r\n")
                .append(httpHeaders.toTextHeader()).append("\r\n")
                .append(body);

        return sb.toString();
    }

    public void sendRedirect(final String location) {
        setStatus(HttpStatus.FOUND);
        httpHeaders.setLocation(location);
    }

    public void addCookie(final Cookie cookie) {
        httpHeaders.setCookie(cookie);
    }

    public Optional<String> getViewName() {
        return Optional.ofNullable(viewName);
    }

    public HttpStatus getHttpStatus() {
        return statusLine.getHttpStatus();
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setStatus(final HttpStatus httpStatus) {
        this.statusLine = new StatusLine(httpStatus);
    }

    public void addBody(final String value) {
        body += value;
        httpHeaders.setContentType("text/html");
        httpHeaders.setContentLength(body.length());
    }

    public void addBody(final String value, final String contentType, final int contentLength) {
        body += value;
        httpHeaders.setContentType(contentType);
        httpHeaders.setContentLength(contentLength);
    }

    public void setView(final String viewName) {
        this.viewName = viewName;
    }
}

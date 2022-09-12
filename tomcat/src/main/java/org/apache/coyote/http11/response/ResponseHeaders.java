package org.apache.coyote.http11.response;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.coyote.http11.request.HttpRequest;

public class ResponseHeaders {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String SET_COOKIE = "Set-Cookie";

    private final Map<String, String> header;

    private ResponseHeaders(Map<String, String> header) {
        this.header = header;
    }

    public static ResponseHeaders create(HttpRequest request, String resource) {
        Map<String, String> mp = new LinkedHashMap<>();
        mp.put(CONTENT_TYPE, ContentType.from(request.getPath()));
        mp.put(CONTENT_LENGTH, String.valueOf(resource.getBytes().length));
        return new ResponseHeaders(mp);
    }

    public String getCookieValue() {
        return header.get(SET_COOKIE);
    }

    public void put(String key, String value) {
        header.put(key, value);
    }

    public String get(String key) {
        return header.get(key);
    }

    public Map<String, String> getHeader() {
        return header;
    }
}

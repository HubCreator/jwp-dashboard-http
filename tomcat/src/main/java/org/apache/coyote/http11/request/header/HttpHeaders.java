package org.apache.coyote.http11.request.header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.coyote.http11.response.HttpCookie;

public class HttpHeaders {

    private static final String COOKIE = "Cookie";
    private static final String HEADER_DELIMITER = ": ";

    private final Map<String, String> values;
    private final HttpCookie cookie;

    private HttpHeaders(Map<String, String> values, HttpCookie cookie) {
        this.values = values;
        this.cookie = cookie;
    }

    public static HttpHeaders from(List<String> headers){
        final Map<String, String> values = new HashMap<>();
        for (final String header : headers) {
            final String[] split = header.split(HEADER_DELIMITER);
            values.put(split[0], split[1]);
        }
        final HttpCookie httpCookie = extractCookie(values);
        return new HttpHeaders(values, httpCookie);
    }

    private static HttpCookie extractCookie(Map<String, String> values) {
        if (values.containsKey(COOKIE)) {
            return HttpCookie.from(values.get(COOKIE));
        }
        return HttpCookie.empty();
    }

    public Map<String, String> getValues() {
        return values;
    }

    public boolean containsSession() {
        return cookie.containsSession();
    }

    public String getSessionId() {
        return cookie.getSessionId();
    }

    public int getContentLength() {
        if (!values.containsKey("Content-Length")) {
            return 0;
        }
        return Integer.parseInt(values.get("Content-Length").trim());
    }
}

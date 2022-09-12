package org.apache.catalina;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {
    private final String id;
    private final Map<String, Object> values = new HashMap<>();

    public HttpSession() {
        id = UUID.randomUUID().toString();
    }

    public HttpSession(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Object getAttribute(final String name) {
        return values.get(name);
    }

    public void setAttribute(final String name, final Object value) {
        values.put(name, value);
    }

    @Override
    public String toString() {
        return "HttpSession{" +
                "id='" + id + '\'' +
                ", values=" + values +
                '}';
    }
}

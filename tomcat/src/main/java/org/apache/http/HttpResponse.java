package org.apache.http;

import nextstep.jwp.controller.RequestMapping;
import org.apache.controller.Controller;

public class HttpResponse {

    private static final String SET_COOKIE_HEADER_WITH_JSESSIONID = "Set-Cookies: JSESSIONID=";

    private final StatusCode statusCode;
    private final ContentType contentType;
    private final String responseBody;
    private final Cookies cookies;

    private HttpResponse(StatusCode statusCode, ContentType contentType, String responseBody,
                         Cookies cookies) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.responseBody = responseBody;
        this.cookies = cookies;
    }

    public static HttpResponse handle(HttpRequest httpRequest) {
        Controller controller = RequestMapping.get(httpRequest);
        return controller.service(httpRequest);
    }

    public static HttpResponse of(StatusCode statusCode, ContentType contentType,
                                  String responseBody) {
        return new HttpResponse(statusCode, contentType, responseBody, null);
    }

    public static HttpResponse of(StatusCode statusCode, ContentType contentType,
                                  String responseBody, Cookies cookies) {
        return new HttpResponse(statusCode, contentType, responseBody, cookies);
    }

    public byte[] writeResponse() {
        final String responseLine = String.format("HTTP/1.1 %s ", statusCode.writeStatus());
        if (cookies != null) {
            final String header = String.join("\r\n",
                writeSetCookieOfHeader() +
                "Content-Type: " + contentType.writeMediaType() + ";charset=utf-8 ",
                "Content-Length: " + responseBody.getBytes().length + " ");
            return write(responseLine, header);
        }
        final String header = String.join("\r\n",
            "Content-Type: " + contentType.writeMediaType() + ";charset=utf-8 ",
            "Content-Length: " + responseBody.getBytes().length + " ");
        return write(responseLine, header);
    }

    private String writeSetCookieOfHeader() {
        return cookies.findJSessionId()
            .map(it -> SET_COOKIE_HEADER_WITH_JSESSIONID + it + " \r\n")
            .orElse("");
    }

    private byte[] write(String responseLine, String header) {
        return String.join("\r\n", responseLine, header, "", responseBody).getBytes();
    }
}

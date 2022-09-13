package org.apache.coyote.http11;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import nextstep.jwp.exception.UncheckedServletException;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.servlet.FrontServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream();
             final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {

            final HttpRequest httpRequest = readHttpRequest(bufferedReader);
            final HttpResponse httpResponse = new HttpResponse();

            final FrontServlet servlet = FrontServlet.getInstance();
            servlet.service(httpRequest, httpResponse);
            writeHttpResponse(httpResponse, outputStream);
        } catch (IOException | UncheckedServletException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
    }

    private HttpRequest readHttpRequest(final BufferedReader bufferedReader) throws IOException {
        final String startLine = bufferedReader.readLine();
        final List<String> headerLines = new ArrayList<>();

        String value = bufferedReader.readLine();
        while (value != null && !"".equals(value)) {
            headerLines.add(value);
            value = bufferedReader.readLine();
        }

        return HttpRequest.from(startLine, headerLines, bufferedReader);
    }

    private void writeHttpResponse(final HttpResponse httpResponse, final OutputStream outputStream) {
        final String response = httpResponse.makeResponse();
        try {
            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

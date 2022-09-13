package org.apache.coyote.http11.response.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.response.ResponseEntity;
import org.apache.coyote.http11.response.file.FileHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileHandlerTest {

    @Test
    @DisplayName("isStaticFileResource 메소드는 입력 받은 리소스가 정적 파일인지 판별한다.")
    void isStaticFileResource() {
        // when
        final boolean isStaticFileResource = FileHandler.isStaticFilePath("/wooteco.txt");

        // then
        assertThat(isStaticFileResource).isTrue();
    }

    @Test
    @DisplayName("createFileResponse 메소드는 입력 받은 리소스에 해당하는 파일을 읽어와서 ResponseEntity로 반환한다.")
    void createFileResponse() throws IOException {
        // when
        final ResponseEntity response = FileHandler.createFileResponse("/wooteco.txt");

        // then
        final URL url = getClass().getClassLoader().getResource("static/wooteco.txt");
        final Path path = Path.of(url.getPath());
        final List<String> file = Files.readAllLines(path);

        assertAll(() -> {
            assertThat(response).extracting("httpStatus", "body")
                    .containsExactly(HttpStatus.OK, file.get(0));
            assertThat(response.getHttpHeader().getHeader("Content-Type")).isEqualTo(Files.probeContentType(path));
        });
    }
}
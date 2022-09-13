package nextstep.jwp.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentTypeTest {

    @Test
    @DisplayName("파일 확장자에 맞는 미디어 타입을 찾을 수 있다.")
    void of_success() {
        String extension = "html";

        ContentType actual = ContentType.from(extension);

        assertThat(actual).isEqualTo(ContentType.TEXT_HTML);
    }

    @Test
    @DisplayName("Http Response에 들어갈 미디어 타입 정보를 반환한다.")
    void getMediaType_success() {
        ContentType contentType = ContentType.APPLICATION_JAVASCRIPT;

        String actual = contentType.writeMediaType();

        String expected = "application/javascript";
        assertThat(actual).isEqualTo(expected);
    }
}

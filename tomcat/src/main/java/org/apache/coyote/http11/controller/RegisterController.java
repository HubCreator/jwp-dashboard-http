package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.exception.MemberAlreadyExistException;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.RequestBody;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatus;
import org.apache.coyote.http11.response.ResponseBody;
import org.apache.coyote.http11.response.StaticResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RegisterController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    private static final Uri INDEX_URI = Uri.INDEX;
    private static final Uri REGISTER_URI = Uri.REGISTER;

    @Override
    public boolean canHandle(final HttpRequest request) {
        final String path = request.getRequestLine().getPath();
        return path.startsWith(REGISTER_URI.getSimplePath());
    }

    @Override
    protected HttpResponse doGet(final HttpRequest request) throws Exception {
        final StaticResource staticResource = StaticResource.from(REGISTER_URI.getFullPath());
        final ResponseBody responseBody = ResponseBody.from(staticResource);
        return HttpResponse.of(HttpStatus.OK, responseBody);
    }

    @Override
    protected HttpResponse doPost(final HttpRequest request) {
        final RequestBody requestBody = request.getRequestBody();
        final String accountValue = requestBody.getParamValue("account");
        final Optional<User> userOptional = InMemoryUserRepository.findByAccount(accountValue);
        if (userOptional.isPresent()) {
            log.error("중복 사용자 등록 : ", new MemberAlreadyExistException(accountValue));
            return redirect(INDEX_URI.getFullPath());
        }
        InMemoryUserRepository.save(
                new User(
                        requestBody.getParamValue("account"),
                        requestBody.getParamValue("password"),
                        requestBody.getParamValue("email")
                )
        );

        return redirect(INDEX_URI.getFullPath());
    }

    private HttpResponse redirect(final String path) {
        return HttpResponse.redirect(HttpStatus.FOUND, path);
    }
}

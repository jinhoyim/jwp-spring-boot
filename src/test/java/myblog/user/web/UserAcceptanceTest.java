package myblog.user.web;

import myblog.user.domain.User;
import myblog.user.dto.UserCreatedDto;
import myblog.user.dto.UserUpdatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import support.test.AcceptanceTest;
import support.test.NsWebTestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);

    @DisplayName("사용자 회원가입/조회/수정/삭제")
    @Test
    void crud() {
        // 회원가입
        final UserCreatedDto expected = new UserCreatedDto("javajigi", "javajigi@nextstep.camp", "password");
        final URI location = this.createUser(expected);

        // 조회
        User actual = this.webClient.getResource(location, User.class);
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());

        // 수정
        final UserUpdatedDto updateUser = new UserUpdatedDto("sanjigi", "sanjigi@nextstep.camp");
        this.webClient.basicAuth(expected.getUserId(), expected.getPassword())
                .updateResource(location, updateUser, UserUpdatedDto.class);

        actual = this.webClient.getResource(location, User.class);
        assertThat(actual.getUserId()).isEqualTo(updateUser.getUserId());
        assertThat(actual.getEmail()).isEqualTo(updateUser.getEmail());
    }

    private URI createUser(final UserCreatedDto createdDto) {
        final URI location = this.webClient.createResource("/users", createdDto, UserCreatedDto.class);
        logger.debug("location : {}", location);
        return location;
    }

    @DisplayName("다른 사용자가 내 정보를 수정")
    @Test
    void update_when_other_user() {
        final UserCreatedDto original = new UserCreatedDto("javajigi", "javajigi@nextstep.camp", "password");
        final URI location = this.createUser(original);

        final UserCreatedDto otherUser = new UserCreatedDto("sanjigi", "sanjigi@nextstep.camp", "password");
        this.createUser(otherUser);

        final UserUpdatedDto updateUser = new UserUpdatedDto("sanjigi", "sanjigi@nextstep.camp");
        this.webClient.basicAuth(otherUser.getUserId(), otherUser.getPassword())
                .updateResource(location.toString(), updateUser, UserUpdatedDto.class, HttpStatus.FORBIDDEN);
    }

    @DisplayName("로그인하지 않은 사용자가 수정")
    @Test
    void update_when_not_login() {
        final UserUpdatedDto updateUser = new UserUpdatedDto("sanjigi", "sanjigi@nextstep.camp");
        NsWebTestClient.client(this.port)
                .put()
                .uri("/users/1")
                .body(Mono.just(updateUser), UserUpdatedDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }
}

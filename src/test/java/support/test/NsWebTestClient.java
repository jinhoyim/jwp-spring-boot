package support.test;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

public class NsWebTestClient {
    private static final String BASE_URL = "http://localhost";

    private String baseUrl = BASE_URL;
    private final int port;
    private WebTestClient.Builder testClientBuilder;

    private NsWebTestClient(final String baseUrl, final int port) {
        this.baseUrl = baseUrl;
        this.port = port;
        this.testClientBuilder = WebTestClient
                .bindToServer()
                .baseUrl(baseUrl + ":" + port);
    }

    public static NsWebTestClient of(final int port) {
        return of(BASE_URL, port);
    }

    public static NsWebTestClient of(final String baseUrl, final int port) {
        return new NsWebTestClient(baseUrl, port);
    }

    public static WebTestClient client(final int port) {
        return WebTestClient
                .bindToServer()
                .baseUrl(BASE_URL + ":" + port)
                .build();
    }

    public NsWebTestClient basicAuth(final String username, final String password) {
        this.testClientBuilder = this.testClientBuilder.filter(basicAuthentication(username, password));
        return this;
    }

    public <T> URI createResource(final String url, final T body, final Class<T> clazz) {
        final EntityExchangeResult<byte[]> response = this.testClientBuilder.build()
                .post()
                .uri(url)
                .body(Mono.just(body), clazz)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();
        return response.getResponseHeaders().getLocation();
    }

    public <T> void updateResource(final URI location, final T body, final Class<T> clazz) {
        this.updateResource(location.toString(), body, clazz);
    }

    public <T> void updateResource(final String location, final T body, final Class<T> clazz) {
        this.updateResource(location, body, clazz, HttpStatus.OK);
    }

    public <T> void updateResource(final String location, final T body, final Class<T> clazz, final HttpStatus httpStatus) {
        this.testClientBuilder.build()
                .put()
                .uri(location.toString())
                .body(Mono.just(body), clazz)
                .exchange()
                .expectStatus().isEqualTo(httpStatus.value());
    }

    public <T> T getResource(final URI location, final Class<T> clazz) {
        return this.testClientBuilder.build()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(clazz)
                .returnResult().getResponseBody();
    }

    public WebTestClient.ResponseSpec requestResource(final String url, final HttpMethod httpMethod) {
        return this.testClientBuilder.build()
                .method(httpMethod)
                .uri(url)
                .exchange();
    }
}

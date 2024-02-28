package io.github.vitorclmarques.quarkussocial.rest;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import io.github.vitorclmarques.quarkussocial.domain.model.Follower;
import io.github.vitorclmarques.quarkussocial.domain.model.Post;
import io.github.vitorclmarques.quarkussocial.domain.model.User;
import io.github.vitorclmarques.quarkussocial.domain.repository.FollowerRepository;
import io.github.vitorclmarques.quarkussocial.domain.repository.PostRepository;
import io.github.vitorclmarques.quarkussocial.domain.repository.UserRepository;
import io.github.vitorclmarques.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
public class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp() {

        // usuario padrao dos testes
        var user = new User();
        user.setAge(30);
        user.setName("Vitor");
        userRepository.persist(user);
        userId = user.getId();

        //Criado a postagem para o usuario.
        Post post = new Post();
        post.setText("Post TEste, HELLOW");
        post.setUser(user);
        postRepository.persist(post);

        // ususario que nao segue ninguem
        var userNotFollower = new User();
        userNotFollower.setAge(35);
        userNotFollower.setName("Fulano");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        // Usuario seguidor
        var userFollower = new User();
        userFollower.setAge(23);
        userFollower.setName("Ciclano");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);

    }

    // Teste para a criação de posts
    @Test
    @DisplayName("Should create a post for usern ")
    public void createPostTest() {
        var post = new CreatePostRequest();
        post.setText("Criado post teste");

        var userId = 1;
        given().contentType(ContentType.JSON)
                .body(post)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    // Teste para post de usurio inexistente
    @Test
    @DisplayName("Should return 404 when trying to make a post  for an inexistent usern ")
    public void postForAnInexistentUserTest() {
        var post = new CreatePostRequest();
        post.setText("Criado post teste");

        var inexistentUserId = 999;
        given().contentType(ContentType.JSON)
                .body(post)
                .pathParam("userId", inexistentUserId)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    // Teste de listagem de posts

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    public void listPostNotFoundTest() {

        var inexistentUserId = 999;
        given()
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSentTest() {

        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));

    }

    @Test
    @DisplayName("Should return 400 when follower doesn't exist")
    public void listPostFollowerNotFoundTest() {

        var inexistentFollowerId = 999;

        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Inexistent followerId"));

    }

    @Test
    @DisplayName("Should return 403 when follower isn't a follower")
    public void listPostNotAFollower() {

        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("Should list post")
    public void listPostsTest() {
        
        given()
                .pathParam("userId", userId)
                .header("followerId", userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }

}

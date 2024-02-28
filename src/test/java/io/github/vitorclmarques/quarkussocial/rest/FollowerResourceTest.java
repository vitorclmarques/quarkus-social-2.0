package io.github.vitorclmarques.quarkussocial.rest;

import static io.restassured.RestAssured.given;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.Matchers;

import io.github.vitorclmarques.quarkussocial.domain.model.Follower;
import io.github.vitorclmarques.quarkussocial.domain.model.User;
import io.github.vitorclmarques.quarkussocial.domain.repository.FollowerRepository;
import io.github.vitorclmarques.quarkussocial.domain.repository.UserRepository;
import io.github.vitorclmarques.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;


@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setUp() {
        //usuario padrão dos testes
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        userRepository.persist(user);
        userId = user.getId();

        // o seguidor
        var follower = new User();
        follower.setAge(31);
        follower.setName("Cicrano");
        userRepository.persist(follower);
        followerId = follower.getId();

        //cria um follower
        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("should return 409 when Follower Id is equal to User id")
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(Response.Status.CONFLICT.getStatusCode())
            .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("should return 404 on follow a user when User id doen't exist")
    public void userNotFoundWhenTryingToFollowTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var inexistentUserId = 999;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .pathParam("userId", inexistentUserId)
        .when()
            .put()
        .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should follow a user")
    public void followUserTest(){

        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 on list user followers and User id doen't exist")
    public void userNotFoundWhenListingFollowersTest(){
        var inexistentUserId = 999;

        given()
            .contentType(ContentType.JSON)
            .pathParam("userId", inexistentUserId)
        .when()
            .get()
        .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should list a user's followers")
    public void listFollowersTest(){
        var response =
                given()
                    .contentType(ContentType.JSON)
                    .pathParam("userId", userId)
                .when()
                    .get()
                .then()
                    .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());

    }

    @Test
    @DisplayName("should return 404 on unfollow user and User id doen't exist")
    public void userNotFoundWhenUnfollowingAUserTest(){
        var inexistentUserId = 999;

        given()
            .pathParam("userId", inexistentUserId)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should Unfollow an user")
    public void unfollowUserTest(){
        given()
            .pathParam("userId", userId)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }


}

















































// @QuarkusTest
// @TestHTTPEndpoint(FollowerResource.class)
// public class FollowerResourceTest {

//     @Inject
//     UserRepository userRepository;

//     @Inject
//     FollowerRepository followerRepository;

//     Long userId;
//     Long followerId;

//     @BeforeEach
//     @Transactional
//     void setUp() {

//         // usuarios padrao
//         var user = new User();
//         user.setAge(30);
//         user.setName("Vitor");
//         userRepository.persist(user);
//         userId = user.getId();

//         // Seguidor
//         var follower = new User();
//         follower.setAge(39);
//         follower.setName("Leleo");
//         userRepository.persist(user);
//         followerId = user.getId();

//         // cria um follower
//         var followerEntity = new Follower();
//         followerEntity.setFollower(follower);
//         followerEntity.setUser(user);
//         followerRepository.persist(followerEntity);

//     }

//     @Test
//     @DisplayName("Should return 409 when followerID is equal user id")
//     public void sameUserAsFollowerTest() {

//         var body = new FollowerRequest();
//         body.setFollowerId(userId);

//         given()
//                 .contentType(ContentType.JSON)
//                 .body(body)
//                 .pathParam("userId", userId)
//                 .when()
//                 .put()
//                 .then()
//                 .statusCode(Response.Status.CONFLICT.getStatusCode())
//                 .body(Matchers.is("You can't follow yourself"));
//     }

//     @Test
//     @DisplayName("Should return 404 when user doesn't exist")
//     public void userNotFoundWhenTryingToFollowTest() {

//         var body = new FollowerRequest();
//         body.setFollowerId(userId);

//         var inexistentUser = 999;

//         given()
//                 .contentType(ContentType.JSON)
//                 .body(body)
//                 .pathParam("userId", inexistentUser)
//                 .when()
//                 .put()
//                 .then()
//                 .statusCode(Response.Status.NOT_FOUND.getStatusCode());

//     }

//     public void followerUserTest() {
//         var body = new FollowerRequest();
//         body.setFollowerId(followerId);

//         given()
//                 .contentType(ContentType.JSON)
//                 .body(body)
//                 .pathParam("userId", userId)
//                 .when()
//                 .put()
//                 .then()
//                 .statusCode(Response.Status.NO_CONTENT.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should return 404 on list users followers  a user when user doesn't exist")
//     public void userNotFoundWhenListingFollowersTest() {

//         var inexistentUser = 999;

//         given()
//                 .contentType(ContentType.JSON)
//                 .pathParam("userId", inexistentUser)
//                 .when()
//                 .get()
//                 .then()
//                 .statusCode(Response.Status.NOT_FOUND.getStatusCode());

//     }

//     @Test
//     @DisplayName("should list a user's followers")
//     public void listFollowersTest() {
//         var response = given()
//                 .contentType(ContentType.JSON)
//                 .pathParam("userId", userId)
//                 .when()
//                 .get()
//                 .then()
//                 .extract().response();

//         var followersCount = response.jsonPath().get("followersCount");
//         var followersContent = response.jsonPath().getList("content");

//         assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
//         assertEquals(1, followersCount);
//         assertEquals(1, followersContent.size());

//     }
// }

package io.github.vitorclmarques.quarkussocial.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.vitorclmarques.quarkussocial.rest.dto.CreateUserRequest;
import io.github.vitorclmarques.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiUrl;

    @Test
    @DisplayName("Should create an user successfully")
    @Order(1)
    public void createUserTest() {

        var user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(20);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiUrl)
                .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    public void createUserValidationErrorTest() {
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiUrl)
                .then()
                .extract().response();
        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));

    }

    @Test
    @DisplayName("Should return list")
    @Order(3)
    public void listAllUserTest(){
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(apiUrl)
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(0) );
    }

}

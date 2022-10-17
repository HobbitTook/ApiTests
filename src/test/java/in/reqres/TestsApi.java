package in.reqres;


import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;


public class TestsApi {

    File user = new File("src/test/resources/user.json");

    @Test
    @DisplayName("Регистрация")
    void userRegistrationTest() {

        File registrationData = new File("src/test/resources/registration.json");

        given()
                .contentType(JSON)
                .body(registrationData)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    @Test
    @DisplayName("Неуспешная регистрация")
    public void failedRegistration() {
        JSONObject failedRegistration = new JSONObject()
                .put("email", "sydney@fife");
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(failedRegistration.toString())
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Успешная авторизация")
    void successAuthorization() {
        File authorizationData = new File("src/test/resources/authorization.json");

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(authorizationData)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", not(empty()));
    }

    @Test
    @DisplayName("Неуспешная авторизация")
    public void failedAuthorization() {
        JSONObject failedLoginRequest = new JSONObject()
                .put("email", "eve.holt@reqres.in");
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(failedLoginRequest.toString())
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Получение списка пользователей")
    public void getUsersList() {
        String url = "https://reqres.in/api/users?page=2";
        given()
                .log().all()
                .when()
                .get(url)
                .then()
                .log().all()
                .statusCode(200)
                .body("data[0]", hasKey("id"));

    }
}
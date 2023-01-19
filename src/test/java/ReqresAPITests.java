import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresAPITests {
    String baseUrl = "https://reqres.in/";

    @Test
    void singleUserTest(){
        given()
                .log().uri()
                .baseUri(baseUrl)
                .contentType(JSON)
                .when()
                .get("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void createTest(){
        String data = "{ \"name\": \"Alex\", \"job\": \"QA\" }";
        given()
                .log().uri()
                .log().body()
                .baseUri(baseUrl)
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Alex"))
                .body("job", is("QA"));
    }
    @Test
    void updateTest(){
        String data = "{ \"name\": \"Alex\", \"job\": \"QAA\" }";
        given()
                .log().uri()
                .log().body()
                .baseUri(baseUrl)
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users/943")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Alex"))
                .body("job", is("QAA"));
    }
    @Test
    void unsuccessfulRegistrationMissingPasswordTest(){
        String data = "{ \"email\": \"testemail@test.com\"}";
        given()
                .log().uri()
                .log().body()
                .baseUri(baseUrl)
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
    @Test
    void unsuccessfulRegistrationMissingEmailTest(){
        String data = "{ \"password\": \"pistol\"}";
        given()
                .log().uri()
                .log().body()
                .baseUri(baseUrl)
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }
    @Test
    void unsuccessfulRegistrationUnvalidEmailTest(){
        String data = "{ \"email\": \"eve.holt\", \"password\": \"cityslicka\" }";
        given()
                .log().uri()
                .log().body()
                .baseUri(baseUrl)
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }
}

import models.Create;
import models.EmailAndPassword;
import models.Error;
import models.NameAndJob;
import models.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresAPITests {

    @Test
    @DisplayName("Get single user info")
    void singleUserTest() {
        String expectedEmail = "janet.weaver@reqres.in";
        UserData data = given()
                .spec(Spec.request)
                .filter(withCustomTemplates())
                .when()
                .get("/users/2")
                .then()
                .spec(Spec.response)
                .statusCode(200)
                .extract().as(UserData.class);
        assertEquals(expectedEmail, data.getUser().getEmail());
    }

    @Test
    @DisplayName("Create user")
    void createTest() {
        NameAndJob nameAndJob = new NameAndJob();
        nameAndJob.setName("Alex");
        nameAndJob.setJob("QA");
        Create data = given()
                .spec(Spec.request)
                .filter(withCustomTemplates())
                .body(nameAndJob)
                .when()
                .post("/users")
                .then()
                .spec(Spec.response)
                .statusCode(201)
                .extract().as(Create.class);
        assertEquals(nameAndJob.getName(), data.getName());
        assertEquals(nameAndJob.getJob(), data.getJob());
    }

    @Test
    @DisplayName("Update User")
    void updateTest() {
        NameAndJob nameAndJob = new NameAndJob();
        nameAndJob.setName("AlexAlex");
        nameAndJob.setJob("QAA");
        Create data = given()
                .spec(Spec.request)
                .filter(withCustomTemplates())
                .body(nameAndJob)
                .when()
                .post("/users")
                .then()
                .spec(Spec.response)
                .statusCode(201)
                .extract().as(Create.class);
        assertEquals(nameAndJob.getName(), data.getName());
        assertEquals(nameAndJob.getJob(), data.getJob());
    }

    @Test
    @DisplayName("Unsuccessful registration due to missing password")
    void unsuccessfulRegistrationMissingPasswordTest() {
        String expectedError = "Missing email or username";
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.setEmail("test@test.com");
        Error data = given()
                .spec(Spec.request)
                .contentType("text/html; charset=utf-8")
                .filter(withCustomTemplates())
                .body(emailAndPassword.getEmail())
                .when()
                .post("/register")
                .then()
                .spec(Spec.response)
                .statusCode(400)
                .extract().as(Error.class);
        assertEquals(expectedError, data.getError());
    }

    @Test
    @DisplayName("Unsuccessful registration due to missing email")
    void unsuccessfulRegistrationMissingEmailTest() {
        String expectedError = "Missing email or username";
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.setPassword("2Password");
        Error data = given()
                .spec(Spec.request)
                .contentType("text/html; charset=utf-8")
                .filter(withCustomTemplates())
                .body(emailAndPassword.getPassword())
                .when()
                .post("/register")
                .then()
                .spec(Spec.response)
                .statusCode(400)
                .extract().as(Error.class);
        assertEquals(expectedError, data.getError());
    }

    @Test
    @DisplayName("Unsuccessful registration due to invalid email")
    void unsuccessfulRegistrationInvalidEmailTest() {
        String expectedError = "Note: Only defined users succeed registration";
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.setEmail("test");
        emailAndPassword.setPassword("2Password");
        Error data = given()
                .spec(Spec.request)
                .filter(withCustomTemplates())
                .body(emailAndPassword)
                .when()
                .post("/register")
                .then()
                .spec(Spec.response)
                .statusCode(400)
                .extract().as(Error.class);
        assertEquals(expectedError, data.getError());
    }
}

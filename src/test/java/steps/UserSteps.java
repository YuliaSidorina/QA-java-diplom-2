package steps;

import com.github.javafaker.Faker;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserSteps {

    private static final Faker faker = new Faker();

    public static String createUniqueUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/register");

        response.then().statusCode(200).body("success", equalTo(true));

        return response.jsonPath().getString("accessToken");
    }

    public static void createExistingUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/register");

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(403)
                .body("success", equalTo(false));
    }

    public static void createUserWithMissingField() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(403)
                .body("success", equalTo(false));
    }

    public static String loginWithNewUser() {
        String accessToken = createUniqueUser();

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        return accessToken;
    }

    public static void updateUserWithAuthorization() {
        String accessToken = loginWithNewUser();

        String newPassword = faker.internet().password();
        String newName = faker.name().fullName();
        String newEmail = faker.internet().emailAddress();

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body("{\"email\": \"" + newEmail + "\", \"password\": \"" + newPassword + "\", \"name\": \"" + newName + "\"}")
                .when()
                .patch("/user")
                .then()
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(newName));
    }

    public static void updateUserWithoutAuthorization() {
        String newPassword = faker.internet().password();
        String newName = faker.name().fullName();

        given()
                .contentType("application/json")
                .body("{\"password\": \"" + newPassword + "\", \"name\": \"" + newName + "\"}")
                .when()
                .patch("/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false));
    }

    public static void loginWithEmptyEmail() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        given()
                .contentType("application/json")
                .body("{\"email\": \"\", \"password\": \"password123\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("success", equalTo(false));
    }

    public static void loginWithEmptyPassword() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"password\": \"\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("success", equalTo(false));
    }

}

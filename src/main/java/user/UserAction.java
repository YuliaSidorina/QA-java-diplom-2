package user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import data.AuthToken;
import data.User;
import data.UserCredentials;

import static io.restassured.RestAssured.given;
import static data.Constants.*;

public class UserAction {
    private String accessToken;
    private UserCredentials userCredentials;
    private AuthToken authToken;

    public UserAction() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса на удаление пользователя")
    public Response deleteUser(Response response) {
        userCredentials = response.as(UserCredentials.class);
        accessToken = userCredentials.getAccessToken().replaceFirst("Bearer ", "");
        return given()
                .auth().oauth2(accessToken)
                .when()
                .delete(UPDATE_USER_PATH);
    }

    @Step("Отправка запроса на выход из системы")
    public Response logout(Response response) {
        // Получаю токен используя десериализацию
        UserCredentials userCredentials = response.as(UserCredentials.class);
        authToken = new AuthToken(userCredentials.getRefreshToken());
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(authToken)
                .when()
                .post(LOGOUT_USER_PATH);
    }

    @Step("Отправка запроса на вход пользователя")
    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH);
    }

    @Step("Отправка запроса на вход пользователя с неверным паролем")
    public Response loginUserWithIncorrectPassword(User user) {
        user.setPassword(user.getPassword() + "123");
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH);
    }

    @Step("Отправка запроса на вход пользователя с неверным email")
    public Response loginUserWithIncorrectEmail(User user) {
        user.setEmail("123" + user.getEmail());
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH);
    }

}

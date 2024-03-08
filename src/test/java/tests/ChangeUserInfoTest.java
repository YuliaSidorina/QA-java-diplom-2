package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.User;
import data.UserCredentials;
import data.UserGenerator;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static data.Constants.USER_NOT_AUTH_ERROR;
import static data.Constants.USER_EMAIL_EXISTS_ERROR;

public class ChangeUserInfoTest {
    private User user;
    private User userSecond;
    private UserChange userChange;
    private String accessToken;
    private UserCredentials userCredentials;
    private UserAction userAction;
    private Response response;
    private String userEmail;
    private String userPassword;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userChange = new UserChange();
        userAction = new UserAction();
    }

    @Test
    @DisplayName("Проверка изменения электронной почты пользователя с авторизацией")
    public void changeUserEmailWithAuthTest(){
        response = userChange.changeEmailUserWithAuth(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo("test"+user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()))
                .and().statusCode(SC_OK);
        user.setEmail("test"+user.getEmail());
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя с авторизацией")
    public void changeUserNameWithAuthTest(){
        response = userChange.changeNameUserWithAuth(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo("test"+user.getName()))
                .and().statusCode(SC_OK);
        user.setName("test"+user.getEmail());
    }

    @Test
    @DisplayName("Проверка изменения электронной почты пользователя без авторизации")
    public void changeUserEmailWithoutAuthTest(){
        response = userChange.changeEmailUserWithoutAuth(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_NOT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя без авторизации")
    public void changeUserNameWithoutAuthTest(){
        response = userChange.changeNameUserWithoutAuth(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_NOT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Проверка изменения электронной почты пользователя на существующую с авторизацией")
    public void changeExistEmailWithAuthTest(){
        String emailFirstUser = user.getEmail();
        userSecond = UserGenerator.getUser();

        response = userChange.changeExistEmailWithAuth(user, userSecond, userSecond.getEmail());
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_EMAIL_EXISTS_ERROR))
                .and().statusCode(SC_FORBIDDEN);

        user.setEmail(emailFirstUser);
        userAction.deleteUser(userAction.login(userSecond));

    }

    @After
    public void tearDown(){
        userAction.deleteUser(userAction.login(user));
    }
}

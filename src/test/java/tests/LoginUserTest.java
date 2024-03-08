package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.User;
import data.UserGenerator;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static data.Constants.LOGIN_USER_ERROR;

public class LoginUserTest {
    private User user;
    private UserAction userAction;
    private Response response;
    private String userEmail;
    private String userPassword;
    private UserCreate userCreate = new UserCreate();


    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        userEmail = user.getEmail();
        userPassword = user.getPassword();
        userAction.logout(userCreate.createUser(user));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUserTest() {
        response = userAction.login(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK)
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с неправильным паролем")
    public void loginUserWithIncorrectPasswordTest() {
        response = userAction.loginUserWithIncorrectPassword(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo(LOGIN_USER_ERROR));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с неправильным email")
    public void loginUserWithIncorrectEmailTest() {
        response = userAction.loginUserWithIncorrectEmail(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo(LOGIN_USER_ERROR));
    }

    @After
    public void tearDown() {
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        userAction.deleteUser(userAction.login(user));
    }
}

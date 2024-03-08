package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.User;
import user.UserAction;
import data.UserGenerator;
import user.UserCreate;

import static org.hamcrest.CoreMatchers.equalTo;
import static data.Constants.CREATE_EXIST_USER_ERROR;
import static data.Constants.MISSING_FIELD_ERROR;
import static org.apache.http.HttpStatus.*;

public class CreateUserTest {
    private User user;
    private UserAction userAction;
    private UserCreate createUser;
    private Response response;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        createUser = new UserCreate();
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void createNewUserTest() {
        response = createUser.createUser(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK)
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Проверка создания одинакового пользователя")
    public void createTheSameUserTest() {
        response = createUser.createUser(user);
        Response response2 = createUser.createUser(user);
        response2.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(CREATE_EXIST_USER_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        response = createUser.createUserWithoutPassword(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(MISSING_FIELD_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка создания пользователя без электронной почты")
    public void createUserWithoutEmailTest() {
        response = createUser.createUserWithoutEmail(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(MISSING_FIELD_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    public void createUserWithoutNameTest() {
        response = createUser.createUserWithoutName(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(MISSING_FIELD_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @After
    public void tearDown() {
        if (response.then().extract().path("success").equals(true)) {
            userAction.deleteUser(response);
        }
    }
}

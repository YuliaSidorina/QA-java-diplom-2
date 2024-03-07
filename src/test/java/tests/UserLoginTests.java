package tests;

import org.junit.Test;
import steps.UserSteps;

public class UserLoginTests extends BaseAPITest {

    @Test
    public void testLoginWithExistingUser() {
        UserSteps.loginWithNewUser();
    }

    @Test
    public void testLoginWithEmptyEmail() {
        UserSteps.loginWithEmptyEmail();
    }

    @Test
    public void testLoginWithEmptyPassword() {
        UserSteps.loginWithEmptyPassword();
    }
}

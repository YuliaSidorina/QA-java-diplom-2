package tests;

import org.junit.Test;
import steps.UserSteps;

public class UserUpdateTests extends BaseAPITest {


    @Test
    public void testUpdateUserWithAuthorization() {
        UserSteps.updateUserWithAuthorization();
    }

    @Test
    public void testUpdateUserWithoutAuthorization() {
        UserSteps.updateUserWithoutAuthorization();
    }
}

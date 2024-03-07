package tests;

import org.junit.BeforeClass;
import org.junit.Test;
import steps.UserSteps;

public class UserCreationTests extends BaseAPITest {

    @Test
    public void testCreateUniqueUser() {
        UserSteps.createUniqueUser();
    }

    @Test
    public void testCreateExistingUser() {
        UserSteps.createExistingUser();
    }

    @Test
    public void testCreateUserWithMissingField() {
        UserSteps.createUserWithMissingField();
    }
}

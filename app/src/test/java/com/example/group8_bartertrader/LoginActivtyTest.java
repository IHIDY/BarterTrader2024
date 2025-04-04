package com.example.group8_bartertrader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.group8_bartertrader.utils.CredentialsValidator;

import org.junit.Before;
import org.junit.Test;

public class LoginActivtyTest {

    CredentialsValidator validator;
    LoginActivity loginActivity;

    @Before
    public void setup(){
        System.setProperty("isUnitTest", "true");
        validator = new CredentialsValidator();
    }


    @Test
    public void checkValidEmails() {

        assertTrue(validator.isValidEmail("email@gmail.com"));
        assertTrue(validator.isValidEmail("user123@gmail.com"));
        assertTrue(validator.isValidEmail("hello.world@domain.org"));
    }

    @Test
    public void checkInvalidEmails() {
        assertFalse(validator.isValidEmail("invalidemail"));
        assertFalse(validator.isValidEmail("user@.com"));
        assertFalse(validator.isValidEmail("user@domain"));
        assertFalse(validator.isValidEmail("user@domain,com"));
        assertFalse(validator.isValidEmail("@nouser.com"));
    }

}

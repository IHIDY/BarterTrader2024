package com.example.group8_bartertrader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LoginActivtyTest {

    @Test
    public void checkValidEmails() {

        assertTrue(LoginActivity.isValidEmail("email@gmail.com"));
        assertTrue(LoginActivity.isValidEmail("user123@gmail.com"));
        assertTrue(LoginActivity.isValidEmail("hello.world@domain.org"));
    }

    @Test
    public void checkInvalidEmails() {
        assertFalse(LoginActivity.isValidEmail("invalidemail"));
        assertFalse(LoginActivity.isValidEmail("user@.com"));
        assertFalse(LoginActivity.isValidEmail("user@domain"));
        assertFalse(LoginActivity.isValidEmail("user@domain,com"));
        assertFalse(LoginActivity.isValidEmail("@nouser.com"));
    }

}

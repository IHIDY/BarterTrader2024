package com.example.group8_bartertrader;

import org.junit.Test;
import static org.junit.Assert.*;

public class RegistrationActivityTest {

    @Test
    public void testEmail(){
        String noEmail = "";
        String invalidEmail = "user@";
        String missingEndEmail = "user@email";
        String validEmail = "user@email.com";

        assertFalse(RegistrationActivity.isValidEmail(noEmail));
        assertFalse(RegistrationActivity.isValidEmail(invalidEmail));
        assertFalse(RegistrationActivity.isValidEmail(missingEndEmail));
        assertTrue(RegistrationActivity.isValidEmail(validEmail));
    }

    @Test
    public void testPassword(){
        String noPass = "";
        String tooShortPass = "123";
        String validPass = "password123";

        assertFalse(RegistrationActivity.isValidPass(noPass));
        assertFalse(RegistrationActivity.isValidPass(tooShortPass));
        assertTrue(RegistrationActivity.isValidPass(validPass));
    }

    @Test
    public void testRole(){
        String role = "Select your role";
        String roleProvider = "Provider";
        String roleReceiver = "Receiver";

        assertFalse(RegistrationActivity.isValidRole(role));
        assertTrue(RegistrationActivity.isValidRole(roleProvider));
        assertTrue(RegistrationActivity.isValidRole(roleReceiver));
    }
}

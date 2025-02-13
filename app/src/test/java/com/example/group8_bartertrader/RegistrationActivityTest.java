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

        assertEquals(1, RegistrationActivity.isValidEmail(noEmail));
        assertEquals(2, RegistrationActivity.isValidEmail(invalidEmail));
        assertEquals(2, RegistrationActivity.isValidEmail(missingEndEmail));
        assertEquals(0, RegistrationActivity.isValidEmail(validEmail));
    }

    @Test
    public void testPassword(){
        String noPass = "";
        String tooShortPass = "123";
        String invalidPass = "12345678";
        String validPass = "Password123!";

        assertEquals(1, RegistrationActivity.isValidPass(noPass));
        assertEquals(2, RegistrationActivity.isValidPass(tooShortPass));
        assertEquals(4, RegistrationActivity.isValidPass(invalidPass));
        assertEquals(0, RegistrationActivity.isValidPass(validPass));
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

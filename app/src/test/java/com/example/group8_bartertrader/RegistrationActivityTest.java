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

        assertFalse(CredentialsValidator.isValidEmail(invalidEmail));
        assertFalse(CredentialsValidator.isValidEmail(missingEndEmail));
        assertFalse(CredentialsValidator.isValidEmail(noEmail));
        assertTrue(CredentialsValidator.isValidEmail(validEmail));

    }

    @Test
    public void testPassword(){
        String noPass = "";
        String tooShortPass = "123";
        String invalidPass = "12345678";
        String validPass = "Password123!";

        assertFalse(CredentialsValidator.isValidPass(noPass));
        assertFalse(CredentialsValidator.isValidPass(tooShortPass));
        assertFalse(CredentialsValidator.isValidPass(invalidPass));
        assertTrue(CredentialsValidator.isValidPass(validPass));
    }

    @Test
    public void testRole(){
        String role = "Select your role";
        String roleProvider = "Provider";
        String roleReceiver = "Receiver";

        assertFalse(CredentialsValidator.isValidRole(role));
        assertTrue(CredentialsValidator.isValidRole(roleProvider));
        assertTrue(CredentialsValidator.isValidRole(roleReceiver));
    }
}

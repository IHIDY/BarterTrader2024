package com.example.group8_bartertrader;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.example.group8_bartertrader.utils.CredentialsValidator;

public class ResetPasswordActivityTest {
    CredentialsValidator validator;

    /**
     * before test
     */
    @Before
    public void setup(){
        validator = new CredentialsValidator();
    }

    /**
     * test email
     */
    @Test
    public void testEmail(){
        assertFalse(validator.isValidEmail("")); //empty
        assertFalse(validator.isValidEmail("User@")); //missing the domain
        assertFalse(validator.isValidEmail("User@email")); //missing TLD (.com, .ca, etc)
        assertFalse(validator.isValidEmail("user.email@com")); //wrong format
        assertTrue(validator.isValidEmail("User@email.com")); //Valid
    }

    /**
     * test password
     */
    @Test
    public void testPassword(){
        assertFalse(validator.isValidPass(""));//empty
        assertFalse(validator.isValidPass("pass"));//too short
        assertFalse(validator.isValidPass("password"));//missing numbers, etc...
        assertFalse(validator.isValidPass("password123"));//missing uppercase letter + special character
        assertFalse(validator.isValidPass("Password123"));//missing special character
        assertTrue(validator.isValidPass("Password123!")); //Valid
    }
}

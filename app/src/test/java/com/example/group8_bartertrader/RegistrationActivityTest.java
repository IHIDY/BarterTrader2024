package com.example.group8_bartertrader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class RegistrationActivityTest {

    CredentialsValidator validator;

    @Before
    public void setup(){
        validator = new CredentialsValidator();
    }

    @Test
    public void testFname(){
        assertTrue(validator.isFnameEmpty(""));
        assertFalse(validator.isFnameEmpty("fName"));
    }

    @Test
    public void testLname(){
        assertTrue(validator.isLnameEmpty(""));
        assertFalse(validator.isLnameEmpty("lName"));
    }

    @Test
    public void testEmail(){
        assertFalse(validator.isValidEmail("")); //empty
        assertFalse(validator.isValidEmail("User@")); //missing the domain
        assertFalse(validator.isValidEmail("User@email")); //missing TLD (.com, .ca, etc)
        assertFalse(validator.isValidEmail("user.email@com")); //wrong format
        assertTrue(validator.isValidEmail("User@email.com")); //Valid
    }

    @Test
    public void testPassword(){
        assertFalse(validator.isValidPass(""));//empty
        assertFalse(validator.isValidPass("pass"));//too short
        assertFalse(validator.isValidPass("password"));//missing numbers, etc...
        assertFalse(validator.isValidPass("password123"));//missing uppercase letter + special character
        assertFalse(validator.isValidPass("Password123"));//missing special character
        assertTrue(validator.isValidPass("Password123!")); //Valid
    }

    @Test
    public void testRole(){
        assertFalse(validator.isValidRole("Select your role"));//invalid
        assertTrue(validator.isValidRole("Receiver"));//valid
        assertTrue(validator.isValidRole("Provider"));//valid
    }
}

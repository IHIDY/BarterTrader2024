package com.example.group8_bartertrader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.group8_bartertrader.utils.CredentialsValidator;

public class RegistrationActivityTest {

    CredentialsValidator validator;

    /**
     * before test
     */
    @Before
    public void setup(){
        System.setProperty("isUnitTest", "true");
        validator = new CredentialsValidator();
    }

    /**
     * test empty first name
     */
    @Test
    public void isFnameEmptyTest(){
        assertTrue(validator.isFnameEmpty(""));
        assertFalse(validator.isFnameEmpty("fName"));
    }

    /**
     * test empty last name
     */
    @Test
    public void isLnameEmptyTest(){
        assertTrue(validator.isLnameEmpty(""));
        assertFalse(validator.isLnameEmpty("lName"));
    }

    /**
     * test empty email
     */
    @Test
    public void isEmptyEmailTest(){
        assertTrue(validator.isEmptyEmail("")); //empty
        assertFalse(validator.isEmptyEmail("User@email.com")); //Valid email
    }

    /**
     * test valid email
     */
    @Test
    public void isValidEmailTest(){
        assertFalse(validator.isValidEmail("")); //empty
        assertFalse(validator.isValidEmail("User@")); //missing the domain
        assertFalse(validator.isValidEmail("User@email")); //missing TLD (.com, .ca, etc)
        assertFalse(validator.isValidEmail("user.email@com")); //wrong format
        assertTrue(validator.isValidEmail("User@email.com")); //Valid
    }

    /**
     * test empty password
     */
    @Test
    public void isEmptyPasswordTest(){
        assertTrue(validator.isEmptyPass(""));//empty
        assertFalse(validator.isEmptyPass("Password123!")); //Valid
    }

    /**
     * test valid password
     */
    @Test
    public void isValidPasswordTest(){
        assertFalse(validator.isValidPass(""));//empty
        assertFalse(validator.isValidPass("pass"));//too short
        assertFalse(validator.isValidPass("password"));//missing numbers, etc...
        assertFalse(validator.isValidPass("password123"));//missing uppercase letter + special character
        assertFalse(validator.isValidPass("Password123"));//missing special character
        assertTrue(validator.isValidPass("Password123!")); //Valid
    }

    /**
     * test valid role
     */
    @Test
    public void isValidRoleTest(){
        assertFalse(validator.isValidRole("Select your role"));//invalid
        assertTrue(validator.isValidRole("Receiver"));//valid
        assertTrue(validator.isValidRole("Provider"));//valid
    }
}

package com.example.group8_bartertrader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResetPasswordActivityTest {

    @Test
    public void testPassword(){
        String noPass = "";
        String tooShortPass = "123";
        String validPass = "Password123!";

        assertFalse(CredentialsValidator.isValidPass(noPass));
        assertFalse(CredentialsValidator.isValidPass(tooShortPass));
        assertTrue(CredentialsValidator.isValidPass(validPass));
    }

}

package com.example.group8_bartertrader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResetPasswordActivityTest {

    private CredentialsValidator cred;

    @Before
    public void setup(){
        cred = new CredentialsValidator();
    }

    @Test
    public void testPassword(){
        String noPass = "";
        String tooShortPass = "123";
        String validPass = "password123";

        assertFalse(cred.isValidPass(noPass));
        assertFalse(cred.isValidPass(tooShortPass));
        assertTrue(cred.isValidPass(validPass));
    }

}

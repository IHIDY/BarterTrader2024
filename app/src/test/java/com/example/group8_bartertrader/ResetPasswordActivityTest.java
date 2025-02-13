package com.example.group8_bartertrader;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResetPasswordActivityTest {
    @Test
    public void testPassword(){
        String noPass = "";
        String tooShortPass = "123";
        String validPass = "password123";

        assertFalse(RegistrationActivity.isValidPass(noPass));
        assertFalse(RegistrationActivity.isValidPass(tooShortPass));
        assertTrue(RegistrationActivity.isValidPass(validPass));
    }

}

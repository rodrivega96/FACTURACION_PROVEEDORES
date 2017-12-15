package com.vates.facpro.persistence.service.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PassworGenerator {
    
    public static String generatePassword(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(30, random).toString(32);
    }

}

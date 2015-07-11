package com.snepos.pitchit;

import java.math.BigInteger;

/**
 * Created by Omer on 10/07/2015.
 */
public class KeyGenerator
{
    public static String GenerateKey(String s)
    {
        final BigInteger mod = BigInteger.valueOf(32416189061L);
        final BigInteger p = BigInteger.valueOf(179424907);
        final int iterations = 107;
        BigInteger res = BigInteger.ONE;
        for(int i = 0; i < s.length(); i++)
        {
            res = res.multiply(BigInteger.valueOf((int)s.charAt(i)));
            res = res.mod(mod);
        }

        for (int i = 0; i < iterations; i++)
        {
            res = res.multiply(res).mod(mod);
            res = res.multiply(p).mod(mod);
        }

        return res.toString();
    }

    public static boolean CheckKey(String s, String key)
    {
        return key.equals(GenerateKey(s));
    }
}

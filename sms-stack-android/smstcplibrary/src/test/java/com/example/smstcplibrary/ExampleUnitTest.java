package com.example.smstcplibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void smstcpLayer_form() {
        SMSTCPLayer smsTest = new SMSTCPLayer(0, 17, 1, 1, 1, 1, 0, 0, "hola que tal");

        assertEquals(smsTest.encondeSMS(), "08f80000000000hola que tal");
    }

/*
    @Test
    public void encodeBase64() {
        assertEquals(SMSTCP.encodeBase64("Hello World"), "SGVsbG8gd29ybGQ=");
    }
*/


}
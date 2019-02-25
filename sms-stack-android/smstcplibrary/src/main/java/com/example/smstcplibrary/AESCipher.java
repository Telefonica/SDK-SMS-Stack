package com.example.smstcplibrary;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {

    /**
     * Decrypt an AES/CTR wit No Padding message
     * @param secret AES Secret
     * @param buffer Message in bytes
     * @return Message decrypted
     * @throws GeneralSecurityException
     */
    public static byte[] decryptAES(SecretKey secret, byte[] buffer) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        int n = cipher.getBlockSize();
        byte[] ivData = Arrays.copyOf(buffer, n);
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivData));
        byte[] clear = cipher.doFinal(buffer, n, buffer.length - n);

        return clear;
    }

    /**
     * Decrypt an AES/CTR wit No Padding message
     * @param secret AES Secret
     * @param message Message String
     * @return Message decrypted
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String decryptAES(String secret, String message) throws  GeneralSecurityException, UnsupportedEncodingException {
        byte[] encodedString = Base64.decode(message, Base64.DEFAULT);
        SecretKey secretKey = generateSecretKey(secret);
        byte[] decodedMessage = decryptAES(secretKey, encodedString);
        return new String(decodedMessage, "UTF-8");
    }


    /**
     * Encrypt an AES/CTR wit No Padding message
     * @param secret AES Secret
     * @param buffer Message in bytes
     * @return Message encrypted
     * @throws GeneralSecurityException
     */
    public static byte[] encryptAES(SecretKey secret, byte[] buffer) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        SecureRandom rng = new SecureRandom();
        byte[] ivData = new byte[cipher.getBlockSize()];
        rng.nextBytes(ivData);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivData));
        byte[] ciphertext = cipher.doFinal(buffer);
        return concatenate(ivData, ciphertext);
    }


    /**
     * Encrypt an AES/CTR wit No Padding message
     * @param secret AES Secret
     * @param message Message String
     * @return Message encrypted
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String encryptAES(String secret, String message) throws  GeneralSecurityException, UnsupportedEncodingException {
        SecretKey secretKey = generateSecretKey(secret);
        byte[] finalMessage = message.getBytes();
        byte[] encodedMessage = encryptAES(secretKey, finalMessage);
        return Base64.encodeToString(encodedMessage, Base64.DEFAULT);
    }


    /**
     * Generates a new Secret Key of 16 bytes
     * @param secretKey
     * @return
     * @throws UnsupportedEncodingException
     */
    public static SecretKey generateSecretKey(String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = secretKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        //key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");

    }


    /**
     * Concatenate two arrays of T type
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> T concatenate(T a, T b) {
        if (!a.getClass().isArray() || !b.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        Class<?> resCompType;
        Class<?> aCompType = a.getClass().getComponentType();
        Class<?> bCompType = b.getClass().getComponentType();

        if (aCompType.isAssignableFrom(bCompType)) {
            resCompType = aCompType;
        } else if (bCompType.isAssignableFrom(aCompType)) {
            resCompType = bCompType;
        } else {
            throw new IllegalArgumentException();
        }

        int aLen = Array.getLength(a);
        int bLen = Array.getLength(b);

        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(resCompType, aLen + bLen);
        System.arraycopy(a, 0, result, 0, aLen);
        System.arraycopy(b, 0, result, aLen, bLen);

        return result;
    }

}

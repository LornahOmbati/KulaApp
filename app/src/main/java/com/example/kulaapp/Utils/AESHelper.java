package com.example.kulaapp.Utils;

import static android.util.Base64.decode;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {

    private static final int pswdIterations = 10;
    private static final int keySize = 128;
    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private static final String plainText = "";
    private static final String AESSalt = "exampleSalt";
    private static final String initializationVector = "8119745113154120";
    Context ct;

    public AESHelper(Context ct) {
        this.ct = ct;
    }

    public static String encrypt(String textToEncrypt) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(String textToDecrypt) throws Exception {

        byte[] encryted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] decrypted = cipher.doFinal(encryted_bytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static byte[] getRaw(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), pswdIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    private static final byte[] salt = { (byte) 0xA4, (byte) 0x0B, (byte) 0xC8,
            (byte) 0x34, (byte) 0xD6, (byte) 0x95, (byte) 0xF3, (byte) 0x13 };

    private static final int BLOCKS = 128;

    public static byte[] encryptAES(String seed, String cleartext)
            throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(cleartext.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decryptAES(String seed, byte[] data) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(data);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(BLOCKS, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] pad(byte[] seed) {
        byte[] nseed = new byte[BLOCKS / 8];
        for (int i = 0; i < BLOCKS / 8; i++)
            nseed[i] = 0;
        for (int i = 0; i < seed.length; i++)
            nseed[i] = seed[i];

        return nseed;
    }



    private static byte[] getKey(String password) throws UnsupportedEncodingException {
        String key = "";
        while (key.length() < 16)
            key += password;
        return key.substring(0, 16).getBytes(StandardCharsets.UTF_8);
    }

    public String DecryptText(String text) {

        if(GlobalVariables.is_encrypted)
        {
            String item_item = "";
            byte[] results = null;
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                byte[] keyBytes = new byte[16];
                byte[] b = getkey().getBytes(StandardCharsets.UTF_8);
                int len = b.length;
                if (len > keyBytes.length)
                    len = keyBytes.length;
                System.arraycopy(b, 0, keyBytes, 0, len);
                SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                results = new byte[text.length()];
                try {
                    results = cipher.doFinal(decode(text, Base64.DEFAULT));
                } catch (Exception e) {

                }
                item_item = new String(results, StandardCharsets.UTF_8);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            return item_item;
        }
        else{
            return text;
        }
    }

    public String EncryptText(String text) {
        byte[] results = null;
        String s = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes = new byte[16];
            byte[] b = getkey().getBytes(StandardCharsets.UTF_8);
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            results = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            //BASE64Encoder encoder = new BASE64Encoder();
            s = Base64.encodeToString(results, Base64.DEFAULT);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
        }
        catch (javax.crypto.IllegalBlockSizeException e){
            e.printStackTrace();
        }
        catch (javax.crypto.BadPaddingException e){
            e.printStackTrace();
        }

        return  s;//new String(results, "UTF-8"); //it returns the result as a String
    }

    private String getkey(){
        return ct.getSharedPreferences("com.example.registrationapp", Context.MODE_PRIVATE).getString("move","");
    }

    public String getkey2(){

        return "cap";
    }

    public String getAppVersion(Context cont){
        PackageInfo pinfo = null;
        try {
            pinfo = cont.getPackageManager().getPackageInfo(cont.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pinfo.versionCode+"";
    }

}


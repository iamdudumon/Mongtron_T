//package com.example.mongtron_t.secure;
//
//import android.util.Base64;
//
//import androidx.core.util.Pair;
//
//import com.example.mongtron_t.http.RetrofitClient;
//import com.example.mongtron_t.user.UserInfoService;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//
//
//public class AES {
//    public static SecretKey aesKey;
//    public static String encryptedAESKey;
//
//    public void generateAESKey() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(128); // You can use 128, 192, or 256 bit keys
//        aesKey = keyGenerator.generateKey();
//    }
//
//    public String keyToBase64String(byte[] secretKey) {
//        return Base64.encodeToString(secretKey, Base64.DEFAULT);
//    }
//
//
//    public static Pair<String, String> getEncryptPair(String data, Key key) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//
//        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        byte[] iv = cipher.getIV();
//
//        return new Pair<>(Base64.encodeToString(encryptedData, Base64.DEFAULT), Base64.encodeToString(iv, Base64.DEFAULT));
//    }
//
//    public String aesDecrypt(String aesKeyBase64, String encryptedData, String ivBase64) throws Exception {
//        byte[] aesKeyBytes = Base64.decode(aesKeyBase64, Base64.DEFAULT);
//        Key key = new SecretKeySpec(aesKeyBytes, "AES");
//
//        byte[] ivBytes = Base64.decode(ivBase64, Base64.DEFAULT);
//        IvParameterSpec iv = new IvParameterSpec(ivBytes);
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, key, iv);
//
//        byte[] encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT);
//        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//
//        return new String(decryptedBytes, StandardCharsets.UTF_8);
//    }
//    public Pair<String, String> aesKeyCheck(String email) throws Exception {
//        Pair<String, String> encryptedEmailPair = AES.getEncryptPair(email, AES.aesKey);
//        RetrofitClient retrofitClient = new RetrofitClient();
//        retrofitClient.aesKeyCheckPost(AES.encryptedAESKey, encryptedEmailPair);
//
//        return encryptedEmailPair;
//    }
//}

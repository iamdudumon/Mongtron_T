//package com.example.mongtron_t.secure;
//
//import android.util.Base64;
//import android.util.Log;
//
//import java.security.InvalidKeyException;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.PublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.X509EncodedKeySpec;
//
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//
//public class RSA {
//    private PublicKey rsaPublicKey;
//
//    public PublicKey getRSAPublicKey(){
//        return this.rsaPublicKey;
//    }
//
//
//
//    public void generateRSAPublicKey(String rsaPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        byte[] publicKeyBytes;
//       publicKeyBytes= Base64.decode(rsaPublicKey, Base64.DEFAULT);
//
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//        this.rsaPublicKey = keyFactory.generatePublic(keySpec);
//    }
//
//    public void encryptedAESKey(SecretKey aesKey, PublicKey rsaPublicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
//        byte[] encryptedAESKey = cipher.doFinal(aesKey.getEncoded());
//        AES.encryptedAESKey = Base64.encodeToString(encryptedAESKey, Base64.DEFAULT);
//    }
//}

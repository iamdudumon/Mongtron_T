//package com.example.mongtron_t.secure;
//
//import android.util.Log;
//
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Arrays;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyAgreement;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
//public class EllipticalCurve {
//    public byte[] hexStringToByteArray(String hexString) {
//        int len = hexString.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
//                    + Character.digit(hexString.charAt(i+1), 16));
//        }
//        return data;
//    }
//
//    public byte[] encryptAESKeyWithECCPublicKey(byte[] aesKey, byte[] serverPublicKeyBytes) throws Exception {
////        Log.e("TAG", Arrays.toString(aesKey));
////        Log.e("TAG", Arrays.toString(serverPublicKeyBytes));
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(serverPublicKeyBytes);
//        KeyFactory kf = KeyFactory.getInstance("EC");
//        PublicKey serverPublicKey = kf.generatePublic(spec);
//        Log.e("TAG", "1");
//
//
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
//        kpg.initialize(256);
//        KeyPair kp = kpg.generateKeyPair();
//        PrivateKey clientPrivateKey = kp.getPrivate();
//        Log.e("TAG", "2");
//
//        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
//        ka.init(clientPrivateKey);
//        ka.doPhase(serverPublicKey, true);
//        byte[]sharedSecret = ka.generateSecret();
//        Log.e("TAG", "3");
//
//        SecretKey encryptionKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
//        return cipher.doFinal(aesKey);
//    }
//
//}

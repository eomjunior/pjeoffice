/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.security.Key;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Ciphers
/*    */ {
/*    */   public static final String ALGORITHM_RSA = "RSA/ECB/PKCS1Padding";
/*    */   
/*    */   public static byte[] encryptWithRsa(byte[] message, Key key) throws Exception {
/* 41 */     return encrypt(message, key, "RSA/ECB/PKCS1Padding");
/*    */   }
/*    */   
/*    */   public static byte[] decryptWithRsa(byte[] message, Key key) throws Exception {
/* 45 */     return decrypt(message, key, "RSA/ECB/PKCS1Padding");
/*    */   }
/*    */   
/*    */   public static byte[] encrypt(byte[] message, Key key, String algorithm) throws Exception {
/* 49 */     Cipher cipher = Cipher.getInstance(algorithm);
/* 50 */     cipher.init(1, key);
/* 51 */     return cipher.doFinal(message);
/*    */   }
/*    */   
/*    */   public static byte[] decrypt(byte[] message, Key key, String algorithm) throws Exception {
/* 55 */     Cipher cipher = Cipher.getInstance(algorithm);
/* 56 */     cipher.init(2, key);
/* 57 */     return cipher.doFinal(message);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Ciphers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Base64;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Randomizer
/*    */ {
/* 12 */   private static final SecureRandom RANDOM = new SecureRandom();
/*    */ 
/*    */ 
/*    */   
/*    */   public static String random() {
/* 17 */     return random(Strings.empty());
/*    */   }
/*    */   
/*    */   public static String nocache() {
/* 21 */     return random(Long.toString(System.currentTimeMillis()));
/*    */   }
/*    */   
/*    */   public static String random(String suffix) {
/* 25 */     byte[] randomBytes = new byte[48];
/* 26 */     RANDOM.nextBytes(randomBytes);
/* 27 */     return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes) + Strings.text(suffix);
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 31 */     System.out.println(nocache());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Randomizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.github.utils4j.imp;
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
/*    */ public abstract class LdapTools
/*    */ {
/*    */   public static String shortAccount(String account) {
/* 34 */     String p = account;
/* 35 */     int idx = p.indexOf('@');
/* 36 */     if (idx > 0)
/* 37 */       p = p.substring(0, idx); 
/* 38 */     return p;
/*    */   }
/*    */   
/*    */   public static String fullAccount(String account, String domain) {
/* 42 */     int idx = account.indexOf('@');
/* 43 */     if (idx >= 0)
/* 44 */       return account; 
/* 45 */     return account + domain;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/LdapTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
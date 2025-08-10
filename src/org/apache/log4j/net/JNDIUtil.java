/*    */ package org.apache.log4j.net;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
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
/*    */ public class JNDIUtil
/*    */ {
/*    */   public static final String JNDI_JAVA_NAMESPACE = "java:";
/*    */   static final String RESTRICTION_MSG = "JNDI name must start with java: but was ";
/*    */   
/*    */   public static Object lookupObject(Context ctx, String name) throws NamingException {
/* 31 */     if (ctx == null) {
/* 32 */       return null;
/*    */     }
/*    */     
/* 35 */     if (isNullOrEmpty(name)) {
/* 36 */       return null;
/*    */     }
/*    */     
/* 39 */     jndiNameSecurityCheck(name);
/*    */     
/* 41 */     Object lookup = ctx.lookup(name);
/* 42 */     return lookup;
/*    */   }
/*    */   
/*    */   private static boolean isNullOrEmpty(String str) {
/* 46 */     return (str == null || str.trim().length() == 0);
/*    */   }
/*    */   
/*    */   public static void jndiNameSecurityCheck(String name) throws NamingException {
/* 50 */     if (!name.startsWith("java:"))
/* 51 */       throw new NamingException("JNDI name must start with java: but was " + name); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/JNDIUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class NullEnumeration
/*    */   implements Enumeration
/*    */ {
/* 31 */   private static final NullEnumeration instance = new NullEnumeration();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NullEnumeration getInstance() {
/* 37 */     return instance;
/*    */   }
/*    */   
/*    */   public boolean hasMoreElements() {
/* 41 */     return false;
/*    */   }
/*    */   
/*    */   public Object nextElement() {
/* 45 */     throw new NoSuchElementException();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/NullEnumeration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.util.Hashtable;
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
/*    */ public final class ThreadLocalMap
/*    */   extends InheritableThreadLocal
/*    */ {
/*    */   public final Object childValue(Object parentValue) {
/* 32 */     Hashtable ht = (Hashtable)parentValue;
/* 33 */     if (ht != null) {
/* 34 */       return ht.clone();
/*    */     }
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/ThreadLocalMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
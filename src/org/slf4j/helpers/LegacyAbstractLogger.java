/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import org.slf4j.Marker;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LegacyAbstractLogger
/*    */   extends AbstractLogger
/*    */ {
/*    */   private static final long serialVersionUID = -7041884104854048950L;
/*    */   
/*    */   public boolean isTraceEnabled(Marker marker) {
/* 16 */     return isTraceEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDebugEnabled(Marker marker) {
/* 21 */     return isDebugEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInfoEnabled(Marker marker) {
/* 26 */     return isInfoEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWarnEnabled(Marker marker) {
/* 31 */     return isWarnEnabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isErrorEnabled(Marker marker) {
/* 36 */     return isErrorEnabled();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/LegacyAbstractLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
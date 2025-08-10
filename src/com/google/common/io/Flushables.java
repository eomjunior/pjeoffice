/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.io.Flushable;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public final class Flushables
/*    */ {
/* 35 */   private static final Logger logger = Logger.getLogger(Flushables.class.getName());
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
/*    */   public static void flush(Flushable flushable, boolean swallowIOException) throws IOException {
/*    */     try {
/* 54 */       flushable.flush();
/* 55 */     } catch (IOException e) {
/* 56 */       if (swallowIOException) {
/* 57 */         logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", e);
/*    */       } else {
/* 59 */         throw e;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Beta
/*    */   public static void flushQuietly(Flushable flushable) {
/*    */     try {
/* 73 */       flush(flushable, true);
/* 74 */     } catch (IOException e) {
/* 75 */       logger.log(Level.SEVERE, "IOException should not have been thrown.", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/Flushables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
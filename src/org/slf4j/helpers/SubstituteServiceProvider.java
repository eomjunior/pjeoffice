/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.spi.MDCAdapter;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
/*    */ 
/*    */ public class SubstituteServiceProvider implements SLF4JServiceProvider {
/*  9 */   private final SubstituteLoggerFactory loggerFactory = new SubstituteLoggerFactory();
/* 10 */   private final IMarkerFactory markerFactory = new BasicMarkerFactory();
/* 11 */   private final MDCAdapter mdcAdapter = new BasicMDCAdapter();
/*    */ 
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 15 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public SubstituteLoggerFactory getSubstituteLoggerFactory() {
/* 19 */     return this.loggerFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 24 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public MDCAdapter getMDCAdapter() {
/* 29 */     return this.mdcAdapter;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRequestedApiVersion() {
/* 34 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void initialize() {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/SubstituteServiceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
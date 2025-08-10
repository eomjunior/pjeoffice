/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.spi.MDCAdapter;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NOP_FallbackServiceProvider
/*    */   implements SLF4JServiceProvider
/*    */ {
/* 15 */   public static String REQUESTED_API_VERSION = "2.0.99";
/*    */   
/* 17 */   private final ILoggerFactory loggerFactory = new NOPLoggerFactory();
/* 18 */   private final IMarkerFactory markerFactory = new BasicMarkerFactory();
/* 19 */   private final MDCAdapter mdcAdapter = new NOPMDCAdapter();
/*    */ 
/*    */ 
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 24 */     return this.loggerFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 29 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MDCAdapter getMDCAdapter() {
/* 35 */     return this.mdcAdapter;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRequestedApiVersion() {
/* 40 */     return REQUESTED_API_VERSION;
/*    */   }
/*    */   
/*    */   public void initialize() {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/NOP_FallbackServiceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
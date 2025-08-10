/*    */ package org.slf4j.reload4j;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.helpers.Reporter;
/*    */ import org.slf4j.spi.MDCAdapter;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Reload4jServiceProvider
/*    */   implements SLF4JServiceProvider
/*    */ {
/* 19 */   public static String REQUESTED_API_VERSION = "2.0.99";
/*    */   
/*    */   private ILoggerFactory loggerFactory;
/*    */   
/*    */   private IMarkerFactory markerFactory;
/*    */   private MDCAdapter mdcAdapter;
/*    */   
/*    */   public Reload4jServiceProvider() {
/*    */     try {
/* 28 */       Level level = Level.TRACE;
/* 29 */     } catch (NoSuchFieldError nsfe) {
/* 30 */       Reporter.error("This version of SLF4J requires log4j version 1.2.12 or later. See also http://www.slf4j.org/codes.html#log4j_version");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize() {
/* 36 */     this.loggerFactory = new Reload4jLoggerFactory();
/* 37 */     this.markerFactory = (IMarkerFactory)new BasicMarkerFactory();
/* 38 */     this.mdcAdapter = new Reload4jMDCAdapter();
/*    */   }
/*    */ 
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 43 */     return this.loggerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 49 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MDCAdapter getMDCAdapter() {
/* 55 */     return this.mdcAdapter;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRequestedApiVersion() {
/* 60 */     return REQUESTED_API_VERSION;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/reload4j/Reload4jServiceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
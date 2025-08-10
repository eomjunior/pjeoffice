/*    */ package org.slf4j;
/*    */ 
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.helpers.Reporter;
/*    */ import org.slf4j.spi.SLF4JServiceProvider;
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
/*    */ public class MarkerFactory
/*    */ {
/*    */   static IMarkerFactory MARKER_FACTORY;
/*    */   
/*    */   static {
/* 53 */     SLF4JServiceProvider provider = LoggerFactory.getProvider();
/* 54 */     if (provider != null) {
/* 55 */       MARKER_FACTORY = provider.getMarkerFactory();
/*    */     } else {
/* 57 */       Reporter.error("Failed to find provider");
/* 58 */       Reporter.error("Defaulting to BasicMarkerFactory.");
/* 59 */       MARKER_FACTORY = (IMarkerFactory)new BasicMarkerFactory();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Marker getMarker(String name) {
/* 72 */     return MARKER_FACTORY.getMarker(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Marker getDetachedMarker(String name) {
/* 83 */     return MARKER_FACTORY.getDetachedMarker(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IMarkerFactory getIMarkerFactory() {
/* 95 */     return MARKER_FACTORY;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/MarkerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.log4j.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.log4j.Level;
/*    */ import org.apache.log4j.Priority;
/*    */ import org.apache.log4j.spi.LocationInfo;
/*    */ import org.apache.log4j.spi.LoggingEvent;
/*    */ import org.apache.log4j.spi.ThrowableInformation;
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
/*    */ public class HardenedLoggingEventInputStream
/*    */   extends HardenedObjectInputStream
/*    */ {
/*    */   static final String ARRAY_PREFIX = "[L";
/*    */   
/*    */   public static List<String> getWhilelist() {
/* 37 */     List<String> whitelist = new ArrayList<String>();
/* 38 */     whitelist.add(LoggingEvent.class.getName());
/* 39 */     whitelist.add(Level.class.getName());
/* 40 */     whitelist.add(Priority.class.getName());
/* 41 */     whitelist.add(ThrowableInformation.class.getName());
/* 42 */     whitelist.add(LocationInfo.class.getName());
/*    */     
/* 44 */     return whitelist;
/*    */   }
/*    */   
/*    */   public HardenedLoggingEventInputStream(InputStream is) throws IOException {
/* 48 */     super(is, getWhilelist());
/*    */   }
/*    */ 
/*    */   
/*    */   public HardenedLoggingEventInputStream(InputStream is, List<String> additionalAuthorizedClasses) throws IOException {
/* 53 */     this(is);
/* 54 */     addToWhitelist(additionalAuthorizedClasses);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/HardenedLoggingEventInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
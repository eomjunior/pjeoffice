/*    */ package org.apache.log4j.xml;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import org.apache.log4j.helpers.LogLog;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
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
/*    */ public class Log4jEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private static final String PUBLIC_ID = "-//APACHE//DTD LOG4J 1.2//EN";
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) {
/* 37 */     if (systemId.endsWith("log4j.dtd") || "-//APACHE//DTD LOG4J 1.2//EN".equals(publicId)) {
/* 38 */       Class<?> clazz = getClass();
/* 39 */       InputStream in = clazz.getResourceAsStream("/org/apache/log4j/xml/log4j.dtd");
/* 40 */       if (in == null) {
/* 41 */         LogLog.warn("Could not find [log4j.dtd] using [" + clazz.getClassLoader() + "] class loader, parsed without DTD.");
/*    */         
/* 43 */         in = new ByteArrayInputStream(new byte[0]);
/*    */       } 
/* 45 */       return new InputSource(in);
/*    */     } 
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/xml/Log4jEntityResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
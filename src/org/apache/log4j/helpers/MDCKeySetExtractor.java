/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Set;
/*    */ import org.apache.log4j.pattern.LogEvent;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ public final class MDCKeySetExtractor
/*    */ {
/*    */   private final Method getKeySetMethod;
/* 31 */   public static final MDCKeySetExtractor INSTANCE = new MDCKeySetExtractor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MDCKeySetExtractor() {
/* 38 */     Method getMethod = null;
/*    */     
/*    */     try {
/* 41 */       getMethod = LoggingEvent.class.getMethod("getPropertyKeySet", null);
/* 42 */     } catch (Exception ex) {
/* 43 */       getMethod = null;
/*    */     } 
/* 45 */     this.getKeySetMethod = getMethod;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set getPropertyKeySet(LoggingEvent event) throws Exception {
/* 53 */     Set keySet = null;
/* 54 */     if (this.getKeySetMethod != null) {
/* 55 */       keySet = (Set)this.getKeySetMethod.invoke(event, null);
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 60 */       ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
/* 61 */       ObjectOutputStream os = new ObjectOutputStream(outBytes);
/* 62 */       os.writeObject(event);
/* 63 */       os.close();
/*    */       
/* 65 */       byte[] raw = outBytes.toByteArray();
/*    */ 
/*    */ 
/*    */       
/* 69 */       String subClassName = LogEvent.class.getName();
/* 70 */       if (raw[6] == 0 || raw[7] == subClassName.length()) {
/*    */ 
/*    */ 
/*    */         
/* 74 */         for (int i = 0; i < subClassName.length(); i++) {
/* 75 */           raw[8 + i] = (byte)subClassName.charAt(i);
/*    */         }
/* 77 */         ByteArrayInputStream inBytes = new ByteArrayInputStream(raw);
/* 78 */         ObjectInputStream is = new ObjectInputStream(inBytes);
/* 79 */         Object cracked = is.readObject();
/* 80 */         if (cracked instanceof LogEvent) {
/* 81 */           keySet = ((LogEvent)cracked).getPropertyKeySet();
/*    */         }
/* 83 */         is.close();
/*    */       } 
/*    */     } 
/* 86 */     return keySet;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/MDCKeySetExtractor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
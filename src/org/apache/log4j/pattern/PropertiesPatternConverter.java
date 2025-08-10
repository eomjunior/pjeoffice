/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import org.apache.log4j.helpers.LogLog;
/*    */ import org.apache.log4j.helpers.MDCKeySetExtractor;
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
/*    */ public final class PropertiesPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/*    */   private final String option;
/*    */   
/*    */   private PropertiesPatternConverter(String[] options) {
/* 47 */     super((options != null && options.length > 0) ? ("Property{" + options[0] + "}") : "Properties", "property");
/*    */ 
/*    */     
/* 50 */     if (options != null && options.length > 0) {
/* 51 */       this.option = options[0];
/*    */     } else {
/* 53 */       this.option = null;
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
/*    */   public static PropertiesPatternConverter newInstance(String[] options) {
/* 65 */     return new PropertiesPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 74 */     if (this.option == null) {
/* 75 */       toAppendTo.append("{");
/*    */       
/*    */       try {
/* 78 */         Set keySet = MDCKeySetExtractor.INSTANCE.getPropertyKeySet(event);
/* 79 */         if (keySet != null) {
/* 80 */           for (Iterator i = keySet.iterator(); i.hasNext(); ) {
/* 81 */             Object item = i.next();
/* 82 */             Object val = event.getMDC(item.toString());
/* 83 */             toAppendTo.append("{").append(item).append(",").append(val).append("}");
/*    */           } 
/*    */         }
/* 86 */       } catch (Exception ex) {
/* 87 */         LogLog.error("Unexpected exception while extracting MDC keys", ex);
/*    */       } 
/*    */       
/* 90 */       toAppendTo.append("}");
/*    */     } else {
/*    */       
/* 93 */       Object val = event.getMDC(this.option);
/*    */       
/* 95 */       if (val != null)
/* 96 */         toAppendTo.append(val); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/PropertiesPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
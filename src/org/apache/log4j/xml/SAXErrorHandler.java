/*    */ package org.apache.log4j.xml;
/*    */ 
/*    */ import org.apache.log4j.helpers.LogLog;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class SAXErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   public void error(SAXParseException ex) {
/* 27 */     emitMessage("Continuable parsing error ", ex);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException ex) {
/* 31 */     emitMessage("Fatal parsing error ", ex);
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException ex) {
/* 35 */     emitMessage("Parsing warning ", ex);
/*    */   }
/*    */   
/*    */   private static void emitMessage(String msg, SAXParseException ex) {
/* 39 */     LogLog.warn(msg + ex.getLineNumber() + " and column " + ex.getColumnNumber());
/* 40 */     LogLog.warn(ex.getMessage(), ex.getException());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/xml/SAXErrorHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
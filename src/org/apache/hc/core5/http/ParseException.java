/*    */ package org.apache.hc.core5.http;
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
/*    */ public class ParseException
/*    */   extends ProtocolException
/*    */ {
/*    */   private static final long serialVersionUID = -7288819855864183578L;
/*    */   private final int errorOffset;
/*    */   
/*    */   public ParseException() {
/* 46 */     this.errorOffset = -1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParseException(String message) {
/* 55 */     super(message);
/* 56 */     this.errorOffset = -1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParseException(String description, CharSequence text, int off, int len, int errorOffset) {
/* 65 */     super(description + ((errorOffset >= 0) ? ("; error at offset " + errorOffset) : "") + ((text != null && len < 1024) ? (": <" + text
/*    */         
/* 67 */         .subSequence(off, off + len) + ">") : ""));
/* 68 */     this.errorOffset = errorOffset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParseException(String description, CharSequence text, int off, int len) {
/* 77 */     this(description, text, off, len, -1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getErrorOffset() {
/* 84 */     return this.errorOffset;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
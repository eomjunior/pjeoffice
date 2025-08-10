/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.ParseException;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class LazyLineParser
/*    */   extends BasicLineParser
/*    */ {
/* 49 */   public static final LazyLineParser INSTANCE = new LazyLineParser();
/*    */ 
/*    */   
/*    */   public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
/* 53 */     Args.notNull(buffer, "Char array buffer");
/*    */     
/* 55 */     return (Header)new BufferedHeader(buffer, true);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/LazyLineParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
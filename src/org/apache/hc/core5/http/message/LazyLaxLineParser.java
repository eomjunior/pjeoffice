/*    */ package org.apache.hc.core5.http.message;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LazyLaxLineParser
/*    */   extends BasicLineParser
/*    */ {
/* 51 */   public static final LazyLaxLineParser INSTANCE = new LazyLaxLineParser();
/*    */ 
/*    */   
/*    */   public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
/* 55 */     Args.notNull(buffer, "Char array buffer");
/*    */     
/* 57 */     return (Header)new BufferedHeader(buffer, false);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/LazyLaxLineParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
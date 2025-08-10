/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import java.util.Iterator;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.util.TextUtils;
/*    */ import org.apache.hc.core5.util.Tokenizer;
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
/*    */ public class BasicTokenIterator
/*    */   extends AbstractHeaderElementIterator<String>
/*    */ {
/* 44 */   private static final BitSet COMMA = Tokenizer.INIT_BITSET(new int[] { 44 });
/*    */ 
/*    */ 
/*    */   
/*    */   private final Tokenizer tokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicTokenIterator(Iterator<Header> headerIterator) {
/* 54 */     super(headerIterator);
/* 55 */     this.tokenizer = Tokenizer.INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   String parseHeaderElement(CharSequence buf, ParserCursor cursor) {
/* 60 */     String token = this.tokenizer.parseToken(buf, cursor, COMMA);
/* 61 */     if (!cursor.atEnd()) {
/* 62 */       int pos = cursor.getPos();
/* 63 */       if (buf.charAt(pos) == ',') {
/* 64 */         cursor.updatePos(pos + 1);
/*    */       }
/*    */     } 
/* 67 */     return !TextUtils.isBlank(token) ? token : null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicTokenIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
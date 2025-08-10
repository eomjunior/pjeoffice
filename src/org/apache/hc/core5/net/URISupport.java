/*    */ package org.apache.hc.core5.net;
/*    */ 
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.BitSet;
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
/*    */ final class URISupport
/*    */ {
/* 36 */   static final BitSet HOST_SEPARATORS = new BitSet(256);
/* 37 */   static final BitSet IPV6_HOST_TERMINATORS = new BitSet(256);
/* 38 */   static final BitSet PORT_SEPARATORS = new BitSet(256);
/* 39 */   static final BitSet TERMINATORS = new BitSet(256);
/*    */   
/*    */   static {
/* 42 */     TERMINATORS.set(47);
/* 43 */     TERMINATORS.set(35);
/* 44 */     TERMINATORS.set(63);
/* 45 */     HOST_SEPARATORS.or(TERMINATORS);
/* 46 */     HOST_SEPARATORS.set(64);
/* 47 */     IPV6_HOST_TERMINATORS.set(93);
/* 48 */     PORT_SEPARATORS.or(TERMINATORS);
/* 49 */     PORT_SEPARATORS.set(58);
/*    */   }
/*    */ 
/*    */   
/*    */   static URISyntaxException createException(CharSequence input, Tokenizer.Cursor cursor, String reason) {
/* 54 */     return new URISyntaxException(input
/* 55 */         .subSequence(cursor.getLowerBound(), cursor.getUpperBound()).toString(), reason, cursor
/*    */         
/* 57 */         .getPos());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/URISupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
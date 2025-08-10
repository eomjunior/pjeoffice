/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import java.nio.charset.CodingErrorAction;
/*    */ import org.apache.hc.core5.http.config.CharCodingConfig;
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
/*    */ public final class CharCodingSupport
/*    */ {
/*    */   public static CharsetDecoder createDecoder(CharCodingConfig cconfig) {
/* 43 */     if (cconfig == null) {
/* 44 */       return null;
/*    */     }
/* 46 */     Charset charset = cconfig.getCharset();
/* 47 */     CodingErrorAction malformed = cconfig.getMalformedInputAction();
/* 48 */     CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
/* 49 */     if (charset != null) {
/* 50 */       return charset.newDecoder()
/* 51 */         .onMalformedInput((malformed != null) ? malformed : CodingErrorAction.REPORT)
/* 52 */         .onUnmappableCharacter((unmappable != null) ? unmappable : CodingErrorAction.REPORT);
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   public static CharsetEncoder createEncoder(CharCodingConfig cconfig) {
/* 58 */     if (cconfig == null) {
/* 59 */       return null;
/*    */     }
/* 61 */     Charset charset = cconfig.getCharset();
/* 62 */     if (charset != null) {
/* 63 */       CodingErrorAction malformed = cconfig.getMalformedInputAction();
/* 64 */       CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
/* 65 */       return charset.newEncoder()
/* 66 */         .onMalformedInput((malformed != null) ? malformed : CodingErrorAction.REPORT)
/* 67 */         .onUnmappableCharacter((unmappable != null) ? unmappable : CodingErrorAction.REPORT);
/*    */     } 
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/CharCodingSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
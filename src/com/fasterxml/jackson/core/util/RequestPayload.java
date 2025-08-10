/*    */ package com.fasterxml.jackson.core.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
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
/*    */ public class RequestPayload
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected byte[] _payloadAsBytes;
/*    */   protected CharSequence _payloadAsText;
/*    */   protected String _charset;
/*    */   
/*    */   public RequestPayload(byte[] bytes, String charset) {
/* 28 */     if (bytes == null) {
/* 29 */       throw new IllegalArgumentException();
/*    */     }
/* 31 */     this._payloadAsBytes = bytes;
/* 32 */     this._charset = (charset == null || charset.isEmpty()) ? "UTF-8" : charset;
/*    */   }
/*    */   
/*    */   public RequestPayload(CharSequence str) {
/* 36 */     if (str == null) {
/* 37 */       throw new IllegalArgumentException();
/*    */     }
/* 39 */     this._payloadAsText = str;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getRawPayload() {
/* 49 */     if (this._payloadAsBytes != null) {
/* 50 */       return this._payloadAsBytes;
/*    */     }
/*    */     
/* 53 */     return this._payloadAsText;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     if (this._payloadAsBytes != null) {
/*    */       try {
/* 60 */         return new String(this._payloadAsBytes, this._charset);
/* 61 */       } catch (IOException e) {
/* 62 */         throw new RuntimeException(e);
/*    */       } 
/*    */     }
/* 65 */     return this._payloadAsText.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/core/util/RequestPayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
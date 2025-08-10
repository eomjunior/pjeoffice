/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.security.MessageDigest;
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
/*    */ public abstract class EncoderMessageDigest
/*    */   extends MessageDigest
/*    */ {
/* 35 */   private final ByteArrayOutputStream output = new ByteArrayOutputStream();
/*    */   
/*    */   protected EncoderMessageDigest(String name) {
/* 38 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void engineUpdate(byte input) {
/* 43 */     this.output.write(input);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void engineUpdate(byte[] input, int offset, int len) {
/* 48 */     this.output.write(input, offset, len);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final byte[] engineDigest() {
/*    */     try {
/* 54 */       return encode(doDigest(this.output.toByteArray()));
/*    */     } finally {
/* 56 */       reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void engineReset() {
/* 62 */     this.output.reset();
/*    */   }
/*    */   
/*    */   protected abstract byte[] doDigest(byte[] paramArrayOfbyte);
/*    */   
/*    */   protected abstract byte[] encode(byte[] paramArrayOfbyte);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/EncoderMessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.itextpdf.text.pdf.crypto;
/*    */ 
/*    */ import org.bouncycastle.crypto.BlockCipher;
/*    */ import org.bouncycastle.crypto.CipherParameters;
/*    */ import org.bouncycastle.crypto.engines.AESFastEngine;
/*    */ import org.bouncycastle.crypto.modes.CBCBlockCipher;
/*    */ import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
/*    */ import org.bouncycastle.crypto.params.KeyParameter;
/*    */ import org.bouncycastle.crypto.params.ParametersWithIV;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AESCipher
/*    */ {
/*    */   private PaddedBufferedBlockCipher bp;
/*    */   
/*    */   public AESCipher(boolean forEncryption, byte[] key, byte[] iv) {
/* 62 */     AESFastEngine aESFastEngine = new AESFastEngine();
/* 63 */     CBCBlockCipher cBCBlockCipher = new CBCBlockCipher((BlockCipher)aESFastEngine);
/* 64 */     this.bp = new PaddedBufferedBlockCipher((BlockCipher)cBCBlockCipher);
/* 65 */     KeyParameter kp = new KeyParameter(key);
/* 66 */     ParametersWithIV piv = new ParametersWithIV((CipherParameters)kp, iv);
/* 67 */     this.bp.init(forEncryption, (CipherParameters)piv);
/*    */   }
/*    */   
/*    */   public byte[] update(byte[] inp, int inpOff, int inpLen) {
/* 71 */     int neededLen = this.bp.getUpdateOutputSize(inpLen);
/* 72 */     byte[] outp = null;
/* 73 */     if (neededLen > 0) {
/* 74 */       outp = new byte[neededLen];
/*    */     } else {
/* 76 */       neededLen = 0;
/* 77 */     }  this.bp.processBytes(inp, inpOff, inpLen, outp, 0);
/* 78 */     return outp;
/*    */   }
/*    */   
/*    */   public byte[] doFinal() {
/* 82 */     int neededLen = this.bp.getOutputSize(0);
/* 83 */     byte[] outp = new byte[neededLen];
/* 84 */     int n = 0;
/*    */     try {
/* 86 */       n = this.bp.doFinal(outp, 0);
/* 87 */     } catch (Exception ex) {
/* 88 */       return outp;
/*    */     } 
/* 90 */     if (n != outp.length) {
/* 91 */       byte[] outp2 = new byte[n];
/* 92 */       System.arraycopy(outp, 0, outp2, 0, n);
/* 93 */       return outp2;
/*    */     } 
/*    */     
/* 96 */     return outp;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/crypto/AESCipher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.itextpdf.text.pdf.crypto;
/*    */ 
/*    */ import org.bouncycastle.crypto.BlockCipher;
/*    */ import org.bouncycastle.crypto.CipherParameters;
/*    */ import org.bouncycastle.crypto.engines.AESFastEngine;
/*    */ import org.bouncycastle.crypto.modes.CBCBlockCipher;
/*    */ import org.bouncycastle.crypto.params.KeyParameter;
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
/*    */ public class AESCipherCBCnoPad
/*    */ {
/*    */   private BlockCipher cbc;
/*    */   
/*    */   public AESCipherCBCnoPad(boolean forEncryption, byte[] key) {
/* 60 */     AESFastEngine aESFastEngine = new AESFastEngine();
/* 61 */     this.cbc = (BlockCipher)new CBCBlockCipher((BlockCipher)aESFastEngine);
/* 62 */     KeyParameter kp = new KeyParameter(key);
/* 63 */     this.cbc.init(forEncryption, (CipherParameters)kp);
/*    */   }
/*    */   
/*    */   public byte[] processBlock(byte[] inp, int inpOff, int inpLen) {
/* 67 */     if (inpLen % this.cbc.getBlockSize() != 0)
/* 68 */       throw new IllegalArgumentException("Not multiple of block: " + inpLen); 
/* 69 */     byte[] outp = new byte[inpLen];
/* 70 */     int baseOffset = 0;
/* 71 */     while (inpLen > 0) {
/* 72 */       this.cbc.processBlock(inp, inpOff, outp, baseOffset);
/* 73 */       inpLen -= this.cbc.getBlockSize();
/* 74 */       baseOffset += this.cbc.getBlockSize();
/* 75 */       inpOff += this.cbc.getBlockSize();
/*    */     } 
/* 77 */     return outp;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/crypto/AESCipherCBCnoPad.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
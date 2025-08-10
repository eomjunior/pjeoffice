/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ import org.bouncycastle.crypto.Xof;
/*    */ import org.bouncycastle.pqc.crypto.xmss.DigestUtil;
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class KeyedHashFunctions
/*    */ {
/*    */   private final Digest digest;
/*    */   private final int digestSize;
/*    */   
/*    */   protected KeyedHashFunctions(ASN1ObjectIdentifier paramASN1ObjectIdentifier, int paramInt) {
/* 18 */     if (paramASN1ObjectIdentifier == null)
/*    */     {
/* 20 */       throw new NullPointerException("digest == null");
/*    */     }
/* 22 */     this.digest = DigestUtil.getDigest(paramASN1ObjectIdentifier);
/* 23 */     this.digestSize = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   private byte[] coreDigest(int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 28 */     byte[] arrayOfByte1 = XMSSUtil.toBytesBigEndian(paramInt, this.digestSize);
/*    */     
/* 30 */     this.digest.update(arrayOfByte1, 0, arrayOfByte1.length);
/*    */     
/* 32 */     this.digest.update(paramArrayOfbyte1, 0, paramArrayOfbyte1.length);
/*    */     
/* 34 */     this.digest.update(paramArrayOfbyte2, 0, paramArrayOfbyte2.length);
/*    */     
/* 36 */     byte[] arrayOfByte2 = new byte[this.digestSize];
/* 37 */     if (this.digest instanceof Xof) {
/*    */       
/* 39 */       ((Xof)this.digest).doFinal(arrayOfByte2, 0, this.digestSize);
/*    */     }
/*    */     else {
/*    */       
/* 43 */       this.digest.doFinal(arrayOfByte2, 0);
/*    */     } 
/* 45 */     return arrayOfByte2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] F(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 50 */     if (paramArrayOfbyte1.length != this.digestSize)
/*    */     {
/* 52 */       throw new IllegalArgumentException("wrong key length");
/*    */     }
/* 54 */     if (paramArrayOfbyte2.length != this.digestSize)
/*    */     {
/* 56 */       throw new IllegalArgumentException("wrong in length");
/*    */     }
/* 58 */     return coreDigest(0, paramArrayOfbyte1, paramArrayOfbyte2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] H(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 63 */     if (paramArrayOfbyte1.length != this.digestSize)
/*    */     {
/* 65 */       throw new IllegalArgumentException("wrong key length");
/*    */     }
/* 67 */     if (paramArrayOfbyte2.length != 2 * this.digestSize)
/*    */     {
/* 69 */       throw new IllegalArgumentException("wrong in length");
/*    */     }
/* 71 */     return coreDigest(1, paramArrayOfbyte1, paramArrayOfbyte2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] HMsg(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 76 */     if (paramArrayOfbyte1.length != 3 * this.digestSize)
/*    */     {
/* 78 */       throw new IllegalArgumentException("wrong key length");
/*    */     }
/* 80 */     return coreDigest(2, paramArrayOfbyte1, paramArrayOfbyte2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] PRF(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 85 */     if (paramArrayOfbyte1.length != this.digestSize)
/*    */     {
/* 87 */       throw new IllegalArgumentException("wrong key length");
/*    */     }
/* 89 */     if (paramArrayOfbyte2.length != 32)
/*    */     {
/* 91 */       throw new IllegalArgumentException("wrong address length");
/*    */     }
/* 93 */     return coreDigest(3, paramArrayOfbyte1, paramArrayOfbyte2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/KeyedHashFunctions.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
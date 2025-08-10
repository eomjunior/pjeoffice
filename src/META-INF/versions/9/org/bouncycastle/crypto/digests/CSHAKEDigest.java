/*    */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*    */ 
/*    */ import org.bouncycastle.crypto.digests.SHAKEDigest;
/*    */ import org.bouncycastle.crypto.digests.XofUtils;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ public class CSHAKEDigest
/*    */   extends SHAKEDigest
/*    */ {
/* 11 */   private static final byte[] padding = new byte[100];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final byte[] diff;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CSHAKEDigest(int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 23 */     super(paramInt);
/*    */     
/* 25 */     if ((paramArrayOfbyte1 == null || paramArrayOfbyte1.length == 0) && (paramArrayOfbyte2 == null || paramArrayOfbyte2.length == 0)) {
/*    */       
/* 27 */       this.diff = null;
/*    */     }
/*    */     else {
/*    */       
/* 31 */       this.diff = Arrays.concatenate(XofUtils.leftEncode((this.rate / 8)), encodeString(paramArrayOfbyte1), encodeString(paramArrayOfbyte2));
/* 32 */       diffPadAndAbsorb();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void diffPadAndAbsorb() {
/* 39 */     int i = this.rate / 8;
/* 40 */     absorb(this.diff, 0, this.diff.length);
/*    */     
/* 42 */     int j = this.diff.length % i;
/*    */ 
/*    */     
/* 45 */     if (j != 0) {
/*    */       
/* 47 */       int k = i - j;
/*    */       
/* 49 */       while (k > padding.length) {
/*    */         
/* 51 */         absorb(padding, 0, padding.length);
/* 52 */         k -= padding.length;
/*    */       } 
/*    */       
/* 55 */       absorb(padding, 0, k);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private byte[] encodeString(byte[] paramArrayOfbyte) {
/* 61 */     if (paramArrayOfbyte == null || paramArrayOfbyte.length == 0)
/*    */     {
/* 63 */       return XofUtils.leftEncode(0L);
/*    */     }
/*    */     
/* 66 */     return Arrays.concatenate(XofUtils.leftEncode(paramArrayOfbyte.length * 8L), paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAlgorithmName() {
/* 71 */     return "CSHAKE" + this.fixedOutputLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public int doOutput(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 76 */     if (this.diff != null) {
/*    */       
/* 78 */       if (!this.squeezing)
/*    */       {
/* 80 */         absorbBits(0, 2);
/*    */       }
/*    */       
/* 83 */       squeeze(paramArrayOfbyte, paramInt1, paramInt2 * 8L);
/*    */       
/* 85 */       return paramInt2;
/*    */     } 
/*    */ 
/*    */     
/* 89 */     return super.doOutput(paramArrayOfbyte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 95 */     super.reset();
/*    */     
/* 97 */     if (this.diff != null)
/*    */     {
/* 99 */       diffPadAndAbsorb();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/CSHAKEDigest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
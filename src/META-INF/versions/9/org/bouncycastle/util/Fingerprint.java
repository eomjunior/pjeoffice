/*     */ package META-INF.versions.9.org.bouncycastle.util;
/*     */ 
/*     */ import org.bouncycastle.crypto.digests.SHA512tDigest;
/*     */ import org.bouncycastle.crypto.digests.SHAKEDigest;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Fingerprint
/*     */ {
/*  11 */   private static char[] encodingTable = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] fingerprint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fingerprint(byte[] paramArrayOfbyte) {
/*  27 */     this(paramArrayOfbyte, 160);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fingerprint(byte[] paramArrayOfbyte, int paramInt) {
/*  38 */     this.fingerprint = calculateFingerprint(paramArrayOfbyte, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fingerprint(byte[] paramArrayOfbyte, boolean paramBoolean) {
/*  50 */     if (paramBoolean) {
/*     */       
/*  52 */       this.fingerprint = calculateFingerprintSHA512_160(paramArrayOfbyte);
/*     */     }
/*     */     else {
/*     */       
/*  56 */       this.fingerprint = calculateFingerprint(paramArrayOfbyte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getFingerprint() {
/*  62 */     return Arrays.clone(this.fingerprint);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     StringBuffer stringBuffer = new StringBuffer();
/*  68 */     for (byte b = 0; b != this.fingerprint.length; b++) {
/*     */       
/*  70 */       if (b)
/*     */       {
/*  72 */         stringBuffer.append(":");
/*     */       }
/*  74 */       stringBuffer.append(encodingTable[this.fingerprint[b] >>> 4 & 0xF]);
/*  75 */       stringBuffer.append(encodingTable[this.fingerprint[b] & 0xF]);
/*     */     } 
/*     */     
/*  78 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  83 */     if (paramObject == this)
/*     */     {
/*  85 */       return true;
/*     */     }
/*  87 */     if (paramObject instanceof org.bouncycastle.util.Fingerprint)
/*     */     {
/*  89 */       return Arrays.areEqual(((org.bouncycastle.util.Fingerprint)paramObject).fingerprint, this.fingerprint);
/*     */     }
/*     */     
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     return Arrays.hashCode(this.fingerprint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] calculateFingerprint(byte[] paramArrayOfbyte) {
/* 109 */     return calculateFingerprint(paramArrayOfbyte, 160);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] calculateFingerprint(byte[] paramArrayOfbyte, int paramInt) {
/* 122 */     if (paramInt % 8 != 0)
/*     */     {
/* 124 */       throw new IllegalArgumentException("bitLength must be a multiple of 8");
/*     */     }
/*     */     
/* 127 */     SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
/*     */     
/* 129 */     sHAKEDigest.update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */     
/* 131 */     byte[] arrayOfByte = new byte[paramInt / 8];
/*     */     
/* 133 */     sHAKEDigest.doFinal(arrayOfByte, 0, paramInt / 8);
/*     */     
/* 135 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] calculateFingerprintSHA512_160(byte[] paramArrayOfbyte) {
/* 148 */     SHA512tDigest sHA512tDigest = new SHA512tDigest(160);
/*     */     
/* 150 */     sHA512tDigest.update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */     
/* 152 */     byte[] arrayOfByte = new byte[sHA512tDigest.getDigestSize()];
/*     */     
/* 154 */     sHA512tDigest.doFinal(arrayOfByte, 0);
/*     */     
/* 156 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/Fingerprint.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
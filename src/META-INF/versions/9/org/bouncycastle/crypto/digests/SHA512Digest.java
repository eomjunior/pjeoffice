/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.digests.LongDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.Pack;
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
/*     */ 
/*     */ 
/*     */ public class SHA512Digest
/*     */   extends LongDigest
/*     */ {
/*     */   private static final int DIGEST_LENGTH = 64;
/*     */   
/*     */   public SHA512Digest() {}
/*     */   
/*     */   public SHA512Digest(org.bouncycastle.crypto.digests.SHA512Digest paramSHA512Digest) {
/*  36 */     super(paramSHA512Digest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA512Digest(byte[] paramArrayOfbyte) {
/*  46 */     restoreState(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  51 */     return "SHA-512";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  56 */     return 64;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/*  63 */     finish();
/*     */     
/*  65 */     Pack.longToBigEndian(this.H1, paramArrayOfbyte, paramInt);
/*  66 */     Pack.longToBigEndian(this.H2, paramArrayOfbyte, paramInt + 8);
/*  67 */     Pack.longToBigEndian(this.H3, paramArrayOfbyte, paramInt + 16);
/*  68 */     Pack.longToBigEndian(this.H4, paramArrayOfbyte, paramInt + 24);
/*  69 */     Pack.longToBigEndian(this.H5, paramArrayOfbyte, paramInt + 32);
/*  70 */     Pack.longToBigEndian(this.H6, paramArrayOfbyte, paramInt + 40);
/*  71 */     Pack.longToBigEndian(this.H7, paramArrayOfbyte, paramInt + 48);
/*  72 */     Pack.longToBigEndian(this.H8, paramArrayOfbyte, paramInt + 56);
/*     */     
/*  74 */     reset();
/*     */     
/*  76 */     return 64;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  84 */     super.reset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     this.H1 = 7640891576956012808L;
/*  91 */     this.H2 = -4942790177534073029L;
/*  92 */     this.H3 = 4354685564936845355L;
/*  93 */     this.H4 = -6534734903238641935L;
/*  94 */     this.H5 = 5840696475078001361L;
/*  95 */     this.H6 = -7276294671716946913L;
/*  96 */     this.H7 = 2270897969802886507L;
/*  97 */     this.H8 = 6620516959819538809L;
/*     */   }
/*     */ 
/*     */   
/*     */   public Memoable copy() {
/* 102 */     return (Memoable)new org.bouncycastle.crypto.digests.SHA512Digest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 107 */     org.bouncycastle.crypto.digests.SHA512Digest sHA512Digest = (org.bouncycastle.crypto.digests.SHA512Digest)paramMemoable;
/*     */     
/* 109 */     copyIn(sHA512Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 114 */     byte[] arrayOfByte = new byte[getEncodedStateSize()];
/* 115 */     populateState(arrayOfByte);
/* 116 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA512Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
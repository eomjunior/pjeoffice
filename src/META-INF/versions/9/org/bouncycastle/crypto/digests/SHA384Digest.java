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
/*     */ public class SHA384Digest
/*     */   extends LongDigest
/*     */ {
/*     */   private static final int DIGEST_LENGTH = 48;
/*     */   
/*     */   public SHA384Digest() {}
/*     */   
/*     */   public SHA384Digest(org.bouncycastle.crypto.digests.SHA384Digest paramSHA384Digest) {
/*  36 */     super(paramSHA384Digest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA384Digest(byte[] paramArrayOfbyte) {
/*  46 */     restoreState(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  51 */     return "SHA-384";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  56 */     return 48;
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
/*     */     
/*  72 */     reset();
/*     */     
/*  74 */     return 48;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  82 */     super.reset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     this.H1 = -3766243637369397544L;
/*  89 */     this.H2 = 7105036623409894663L;
/*  90 */     this.H3 = -7973340178411365097L;
/*  91 */     this.H4 = 1526699215303891257L;
/*  92 */     this.H5 = 7436329637833083697L;
/*  93 */     this.H6 = -8163818279084223215L;
/*  94 */     this.H7 = -2662702644619276377L;
/*  95 */     this.H8 = 5167115440072839076L;
/*     */   }
/*     */ 
/*     */   
/*     */   public Memoable copy() {
/* 100 */     return (Memoable)new org.bouncycastle.crypto.digests.SHA384Digest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 105 */     org.bouncycastle.crypto.digests.SHA384Digest sHA384Digest = (org.bouncycastle.crypto.digests.SHA384Digest)paramMemoable;
/*     */     
/* 107 */     copyIn(sHA384Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 112 */     byte[] arrayOfByte = new byte[getEncodedStateSize()];
/* 113 */     populateState(arrayOfByte);
/* 114 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA384Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
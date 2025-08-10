/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsPrivateKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ 
/*     */ public class LMSContext
/*     */   implements Digest
/*     */ {
/*     */   private final byte[] C;
/*     */   private final LMOtsPrivateKey key;
/*     */   private final LMSigParameters sigParams;
/*     */   private final byte[][] path;
/*     */   private final LMOtsPublicKey publicKey;
/*     */   private final Object signature;
/*     */   private LMSSignedPubKey[] signedPubKeys;
/*     */   private volatile Digest digest;
/*     */   
/*     */   public LMSContext(LMOtsPrivateKey paramLMOtsPrivateKey, LMSigParameters paramLMSigParameters, Digest paramDigest, byte[] paramArrayOfbyte, byte[][] paramArrayOfbyte1) {
/*  22 */     this.key = paramLMOtsPrivateKey;
/*  23 */     this.sigParams = paramLMSigParameters;
/*  24 */     this.digest = paramDigest;
/*  25 */     this.C = paramArrayOfbyte;
/*  26 */     this.path = paramArrayOfbyte1;
/*  27 */     this.publicKey = null;
/*  28 */     this.signature = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSContext(LMOtsPublicKey paramLMOtsPublicKey, Object paramObject, Digest paramDigest) {
/*  33 */     this.publicKey = paramLMOtsPublicKey;
/*  34 */     this.signature = paramObject;
/*  35 */     this.digest = paramDigest;
/*  36 */     this.C = null;
/*  37 */     this.key = null;
/*  38 */     this.sigParams = null;
/*  39 */     this.path = null;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] getC() {
/*  44 */     return this.C;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] getQ() {
/*  49 */     byte[] arrayOfByte = new byte[34];
/*     */     
/*  51 */     this.digest.doFinal(arrayOfByte, 0);
/*     */     
/*  53 */     this.digest = null;
/*     */     
/*  55 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[][] getPath() {
/*  60 */     return this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   LMOtsPrivateKey getPrivateKey() {
/*  65 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMOtsPublicKey getPublicKey() {
/*  70 */     return this.publicKey;
/*     */   }
/*     */ 
/*     */   
/*     */   LMSigParameters getSigParams() {
/*  75 */     return this.sigParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSignature() {
/*  80 */     return this.signature;
/*     */   }
/*     */ 
/*     */   
/*     */   LMSSignedPubKey[] getSignedPubKeys() {
/*  85 */     return this.signedPubKeys;
/*     */   }
/*     */ 
/*     */   
/*     */   org.bouncycastle.pqc.crypto.lms.LMSContext withSignedPublicKeys(LMSSignedPubKey[] paramArrayOfLMSSignedPubKey) {
/*  90 */     this.signedPubKeys = paramArrayOfLMSSignedPubKey;
/*     */     
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  97 */     return this.digest.getAlgorithmName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/* 102 */     return this.digest.getDigestSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(byte paramByte) {
/* 107 */     this.digest.update(paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 112 */     this.digest.update(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/* 117 */     return this.digest.doFinal(paramArrayOfbyte, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 122 */     this.digest.reset();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSContext.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
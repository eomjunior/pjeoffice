/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.xmss.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSOid;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WOTSPlusParameters
/*     */ {
/*     */   private final XMSSOid oid;
/*     */   private final int digestSize;
/*     */   private final int winternitzParameter;
/*     */   private final int len;
/*     */   private final int len1;
/*     */   private final int len2;
/*     */   private final ASN1ObjectIdentifier treeDigest;
/*     */   
/*     */   protected WOTSPlusParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  48 */     if (paramASN1ObjectIdentifier == null)
/*     */     {
/*  50 */       throw new NullPointerException("treeDigest == null");
/*     */     }
/*  52 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  53 */     Digest digest = DigestUtil.getDigest(paramASN1ObjectIdentifier);
/*  54 */     this.digestSize = XMSSUtil.getDigestSize(digest);
/*  55 */     this.winternitzParameter = 16;
/*  56 */     this.len1 = (int)Math.ceil((8 * this.digestSize) / XMSSUtil.log2(this.winternitzParameter));
/*  57 */     this.len2 = (int)Math.floor((XMSSUtil.log2(this.len1 * (this.winternitzParameter - 1)) / XMSSUtil.log2(this.winternitzParameter))) + 1;
/*  58 */     this.len = this.len1 + this.len2;
/*  59 */     this.oid = (XMSSOid)WOTSPlusOid.lookup(digest.getAlgorithmName(), this.digestSize, this.winternitzParameter, this.len);
/*  60 */     if (this.oid == null)
/*     */     {
/*  62 */       throw new IllegalArgumentException("cannot find OID for digest algorithm: " + digest.getAlgorithmName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMSSOid getOid() {
/*  73 */     return this.oid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getTreeDigestSize() {
/*  83 */     return this.digestSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getWinternitzParameter() {
/*  93 */     return this.winternitzParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLen() {
/* 103 */     return this.len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLen1() {
/* 113 */     return this.len1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLen2() {
/* 123 */     return this.len2;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier getTreeDigest() {
/* 128 */     return this.treeDigest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/WOTSPlusParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
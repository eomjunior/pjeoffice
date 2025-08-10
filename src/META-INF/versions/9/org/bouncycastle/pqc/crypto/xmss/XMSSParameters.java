/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid;
/*     */ import org.bouncycastle.pqc.crypto.xmss.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSOid;
/*     */ import org.bouncycastle.util.Integers;
/*     */ 
/*     */ public final class XMSSParameters {
/*     */   private static final Map<Integer, org.bouncycastle.pqc.crypto.xmss.XMSSParameters> paramsLookupTable;
/*     */   private final XMSSOid oid;
/*     */   
/*     */   static {
/*  21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  22 */     hashMap.put(Integers.valueOf(1), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(10, NISTObjectIdentifiers.id_sha256));
/*  23 */     hashMap.put(Integers.valueOf(2), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(16, NISTObjectIdentifiers.id_sha256));
/*  24 */     hashMap.put(Integers.valueOf(3), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(20, NISTObjectIdentifiers.id_sha256));
/*  25 */     hashMap.put(Integers.valueOf(4), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(10, NISTObjectIdentifiers.id_sha512));
/*  26 */     hashMap.put(Integers.valueOf(5), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(16, NISTObjectIdentifiers.id_sha512));
/*  27 */     hashMap.put(Integers.valueOf(6), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(20, NISTObjectIdentifiers.id_sha512));
/*  28 */     hashMap.put(Integers.valueOf(7), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(10, NISTObjectIdentifiers.id_shake128));
/*  29 */     hashMap.put(Integers.valueOf(8), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(16, NISTObjectIdentifiers.id_shake128));
/*  30 */     hashMap.put(Integers.valueOf(9), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(20, NISTObjectIdentifiers.id_shake128));
/*  31 */     hashMap.put(Integers.valueOf(10), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(10, NISTObjectIdentifiers.id_shake256));
/*  32 */     hashMap.put(Integers.valueOf(11), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(16, NISTObjectIdentifiers.id_shake256));
/*  33 */     hashMap.put(Integers.valueOf(12), new org.bouncycastle.pqc.crypto.xmss.XMSSParameters(20, NISTObjectIdentifiers.id_shake256));
/*  34 */     paramsLookupTable = Collections.unmodifiableMap(hashMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int k;
/*     */   
/*     */   private final ASN1ObjectIdentifier treeDigestOID;
/*     */   
/*     */   private final int winternitzParameter;
/*     */   
/*     */   private final String treeDigest;
/*     */   
/*     */   private final int treeDigestSize;
/*     */   
/*     */   private final WOTSPlusParameters wotsPlusParams;
/*     */ 
/*     */   
/*     */   public XMSSParameters(int paramInt, Digest paramDigest) {
/*  54 */     this(paramInt, DigestUtil.getDigestOID(paramDigest.getAlgorithmName()));
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
/*     */   public XMSSParameters(int paramInt, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  66 */     if (paramInt < 2)
/*     */     {
/*  68 */       throw new IllegalArgumentException("height must be >= 2");
/*     */     }
/*  70 */     if (paramASN1ObjectIdentifier == null)
/*     */     {
/*  72 */       throw new NullPointerException("digest == null");
/*     */     }
/*     */     
/*  75 */     this.height = paramInt;
/*  76 */     this.k = determineMinK();
/*  77 */     this.treeDigest = DigestUtil.getDigestName(paramASN1ObjectIdentifier);
/*  78 */     this.treeDigestOID = paramASN1ObjectIdentifier;
/*     */     
/*  80 */     this.wotsPlusParams = new WOTSPlusParameters(paramASN1ObjectIdentifier);
/*  81 */     this.treeDigestSize = this.wotsPlusParams.getTreeDigestSize();
/*  82 */     this.winternitzParameter = this.wotsPlusParams.getWinternitzParameter();
/*  83 */     this.oid = (XMSSOid)DefaultXMSSOid.lookup(this.treeDigest, this.treeDigestSize, this.winternitzParameter, this.wotsPlusParams.getLen(), paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int determineMinK() {
/*  91 */     for (byte b = 2; b <= this.height; b++) {
/*     */       
/*  93 */       if ((this.height - b) % 2 == 0)
/*     */       {
/*  95 */         return b;
/*     */       }
/*     */     } 
/*  98 */     throw new IllegalStateException("should never happen...");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTreeDigestSize() {
/* 108 */     return this.treeDigestSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier getTreeDigestOID() {
/* 118 */     return this.treeDigestOID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 128 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   String getTreeDigest() {
/* 133 */     return this.treeDigest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getLen() {
/* 140 */     return this.wotsPlusParams.getLen();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getWinternitzParameter() {
/* 150 */     return this.winternitzParameter;
/*     */   }
/*     */ 
/*     */   
/*     */   WOTSPlus getWOTSPlus() {
/* 155 */     return new WOTSPlus(this.wotsPlusParams);
/*     */   }
/*     */ 
/*     */   
/*     */   XMSSOid getOid() {
/* 160 */     return this.oid;
/*     */   }
/*     */ 
/*     */   
/*     */   int getK() {
/* 165 */     return this.k;
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.xmss.XMSSParameters lookupByOID(int paramInt) {
/* 170 */     return paramsLookupTable.get(Integers.valueOf(paramInt));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
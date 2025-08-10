/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid;
/*     */ import org.bouncycastle.pqc.crypto.xmss.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSOid;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
/*     */ import org.bouncycastle.util.Integers;
/*     */ 
/*     */ public final class XMSSMTParameters {
/*     */   private static final Map<Integer, org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters> paramsLookupTable;
/*     */   private final XMSSOid oid;
/*     */   
/*     */   static {
/*  21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  23 */     hashMap.put(Integers.valueOf(1), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 2, NISTObjectIdentifiers.id_sha256));
/*  24 */     hashMap.put(Integers.valueOf(2), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 4, NISTObjectIdentifiers.id_sha256));
/*  25 */     hashMap.put(Integers.valueOf(3), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 2, NISTObjectIdentifiers.id_sha256));
/*  26 */     hashMap.put(Integers.valueOf(4), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 4, NISTObjectIdentifiers.id_sha256));
/*  27 */     hashMap.put(Integers.valueOf(5), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 8, NISTObjectIdentifiers.id_sha256));
/*  28 */     hashMap.put(Integers.valueOf(6), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 3, NISTObjectIdentifiers.id_sha256));
/*  29 */     hashMap.put(Integers.valueOf(7), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 6, NISTObjectIdentifiers.id_sha256));
/*  30 */     hashMap.put(Integers.valueOf(8), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 12, NISTObjectIdentifiers.id_sha256));
/*  31 */     hashMap.put(Integers.valueOf(9), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 2, NISTObjectIdentifiers.id_sha512));
/*  32 */     hashMap.put(Integers.valueOf(10), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 4, NISTObjectIdentifiers.id_sha512));
/*  33 */     hashMap.put(Integers.valueOf(11), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 2, NISTObjectIdentifiers.id_sha512));
/*  34 */     hashMap.put(Integers.valueOf(12), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 4, NISTObjectIdentifiers.id_sha512));
/*  35 */     hashMap.put(Integers.valueOf(13), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 8, NISTObjectIdentifiers.id_sha512));
/*  36 */     hashMap.put(Integers.valueOf(14), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 3, NISTObjectIdentifiers.id_sha512));
/*  37 */     hashMap.put(Integers.valueOf(15), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 6, NISTObjectIdentifiers.id_sha512));
/*  38 */     hashMap.put(Integers.valueOf(16), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 12, NISTObjectIdentifiers.id_sha512));
/*  39 */     hashMap.put(Integers.valueOf(17), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 2, NISTObjectIdentifiers.id_shake128));
/*  40 */     hashMap.put(Integers.valueOf(18), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 4, NISTObjectIdentifiers.id_shake128));
/*  41 */     hashMap.put(Integers.valueOf(19), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 2, NISTObjectIdentifiers.id_shake128));
/*  42 */     hashMap.put(Integers.valueOf(20), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 4, NISTObjectIdentifiers.id_shake128));
/*  43 */     hashMap.put(Integers.valueOf(21), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 8, NISTObjectIdentifiers.id_shake128));
/*  44 */     hashMap.put(Integers.valueOf(22), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 3, NISTObjectIdentifiers.id_shake128));
/*  45 */     hashMap.put(Integers.valueOf(23), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 6, NISTObjectIdentifiers.id_shake128));
/*  46 */     hashMap.put(Integers.valueOf(24), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 12, NISTObjectIdentifiers.id_shake128));
/*  47 */     hashMap.put(Integers.valueOf(25), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 2, NISTObjectIdentifiers.id_shake256));
/*  48 */     hashMap.put(Integers.valueOf(26), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(20, 4, NISTObjectIdentifiers.id_shake256));
/*  49 */     hashMap.put(Integers.valueOf(27), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 2, NISTObjectIdentifiers.id_shake256));
/*  50 */     hashMap.put(Integers.valueOf(28), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 4, NISTObjectIdentifiers.id_shake256));
/*  51 */     hashMap.put(Integers.valueOf(29), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(40, 8, NISTObjectIdentifiers.id_shake256));
/*  52 */     hashMap.put(Integers.valueOf(30), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 3, NISTObjectIdentifiers.id_shake256));
/*  53 */     hashMap.put(Integers.valueOf(31), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 6, NISTObjectIdentifiers.id_shake256));
/*  54 */     hashMap.put(Integers.valueOf(32), new org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters(60, 12, NISTObjectIdentifiers.id_shake256));
/*  55 */     paramsLookupTable = Collections.unmodifiableMap(hashMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final XMSSParameters xmssParams;
/*     */ 
/*     */   
/*     */   private final int height;
/*     */ 
/*     */   
/*     */   private final int layers;
/*     */ 
/*     */ 
/*     */   
/*     */   public XMSSMTParameters(int paramInt1, int paramInt2, Digest paramDigest) {
/*  72 */     this(paramInt1, paramInt2, DigestUtil.getDigestOID(paramDigest.getAlgorithmName()));
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
/*     */   public XMSSMTParameters(int paramInt1, int paramInt2, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  85 */     this.height = paramInt1;
/*  86 */     this.layers = paramInt2;
/*  87 */     this.xmssParams = new XMSSParameters(xmssTreeHeight(paramInt1, paramInt2), paramASN1ObjectIdentifier);
/*  88 */     this.oid = (XMSSOid)DefaultXMSSMTOid.lookup(getTreeDigest(), getTreeDigestSize(), getWinternitzParameter(), 
/*  89 */         getLen(), getHeight(), paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int xmssTreeHeight(int paramInt1, int paramInt2) throws IllegalArgumentException {
/*  98 */     if (paramInt1 < 2)
/*     */     {
/* 100 */       throw new IllegalArgumentException("totalHeight must be > 1");
/*     */     }
/* 102 */     if (paramInt1 % paramInt2 != 0)
/*     */     {
/* 104 */       throw new IllegalArgumentException("layers must divide totalHeight without remainder");
/*     */     }
/* 106 */     if (paramInt1 / paramInt2 == 1)
/*     */     {
/* 108 */       throw new IllegalArgumentException("height / layers must be greater than 1");
/*     */     }
/* 110 */     return paramInt1 / paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 120 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLayers() {
/* 130 */     return this.layers;
/*     */   }
/*     */ 
/*     */   
/*     */   protected XMSSParameters getXMSSParameters() {
/* 135 */     return this.xmssParams;
/*     */   }
/*     */ 
/*     */   
/*     */   protected WOTSPlus getWOTSPlus() {
/* 140 */     return this.xmssParams.getWOTSPlus();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getTreeDigest() {
/* 145 */     return this.xmssParams.getTreeDigest();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTreeDigestSize() {
/* 155 */     return this.xmssParams.getTreeDigestSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier getTreeDigestOID() {
/* 165 */     return this.xmssParams.getTreeDigestOID();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getWinternitzParameter() {
/* 175 */     return this.xmssParams.getWinternitzParameter();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLen() {
/* 180 */     return this.xmssParams.getLen();
/*     */   }
/*     */ 
/*     */   
/*     */   protected XMSSOid getOid() {
/* 185 */     return this.oid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters lookupByOID(int paramInt) {
/* 190 */     return paramsLookupTable.get(Integers.valueOf(paramInt));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSMTParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSOid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DefaultXMSSMTOid
/*     */   implements XMSSOid
/*     */ {
/*     */   private static final Map<String, org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid> oidLookupTable;
/*     */   private final int oid;
/*     */   private final String stringRepresentation;
/*     */   
/*     */   static {
/*  21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  22 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 20, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(1, "XMSSMT_SHA2_20/2_256"));
/*     */     
/*  24 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 20, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(2, "XMSSMT_SHA2_20/4_256"));
/*     */     
/*  26 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 40, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(3, "XMSSMT_SHA2_40/2_256"));
/*     */     
/*  28 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 40, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(4, "XMSSMT_SHA2_40/4_256"));
/*     */     
/*  30 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 40, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(5, "XMSSMT_SHA2_40/8_256"));
/*     */     
/*  32 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 60, 8), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(6, "XMSSMT_SHA2_60/3_256"));
/*     */     
/*  34 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 60, 6), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(7, "XMSSMT_SHA2_60/6_256"));
/*     */     
/*  36 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 60, 12), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(8, "XMSSMT_SHA2_60/12_256"));
/*     */     
/*  38 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 20, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(9, "XMSSMT_SHA2_20/2_512"));
/*     */     
/*  40 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 20, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(10, "XMSSMT_SHA2_20/4_512"));
/*     */     
/*  42 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 40, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(11, "XMSSMT_SHA2_40/2_512"));
/*     */     
/*  44 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 40, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(12, "XMSSMT_SHA2_40/4_512"));
/*     */     
/*  46 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 40, 8), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(13, "XMSSMT_SHA2_40/8_512"));
/*     */     
/*  48 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 60, 3), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(14, "XMSSMT_SHA2_60/3_512"));
/*     */     
/*  50 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 60, 6), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(15, "XMSSMT_SHA2_60/6_512"));
/*     */     
/*  52 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 60, 12), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(16, "XMSSMT_SHA2_60/12_512"));
/*     */     
/*  54 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 20, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(17, "XMSSMT_SHAKE_20/2_256"));
/*     */     
/*  56 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 20, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(18, "XMSSMT_SHAKE_20/4_256"));
/*     */     
/*  58 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 40, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(19, "XMSSMT_SHAKE_40/2_256"));
/*     */     
/*  60 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 40, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(20, "XMSSMT_SHAKE_40/4_256"));
/*     */     
/*  62 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 40, 8), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(21, "XMSSMT_SHAKE_40/8_256"));
/*     */     
/*  64 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 60, 3), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(22, "XMSSMT_SHAKE_60/3_256"));
/*     */     
/*  66 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 60, 6), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(23, "XMSSMT_SHAKE_60/6_256"));
/*     */     
/*  68 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 60, 12), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(24, "XMSSMT_SHAKE_60/12_256"));
/*     */     
/*  70 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 20, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(25, "XMSSMT_SHAKE_20/2_512"));
/*     */     
/*  72 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 20, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(26, "XMSSMT_SHAKE_20/4_512"));
/*     */     
/*  74 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 40, 2), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(27, "XMSSMT_SHAKE_40/2_512"));
/*     */     
/*  76 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 40, 4), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(28, "XMSSMT_SHAKE_40/4_512"));
/*     */     
/*  78 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 40, 8), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(29, "XMSSMT_SHAKE_40/8_512"));
/*     */     
/*  80 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 60, 3), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(30, "XMSSMT_SHAKE_60/3_512"));
/*     */     
/*  82 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 60, 6), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(31, "XMSSMT_SHAKE_60/6_512"));
/*     */     
/*  84 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 60, 12), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid(32, "XMSSMT_SHAKE_60/12_512"));
/*     */     
/*  86 */     oidLookupTable = Collections.unmodifiableMap(hashMap);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultXMSSMTOid(int paramInt, String paramString) {
/* 107 */     this.oid = paramInt;
/* 108 */     this.stringRepresentation = paramString;
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
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.xmss.DefaultXMSSMTOid lookup(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 122 */     if (paramString == null)
/*     */     {
/* 124 */       throw new NullPointerException("algorithmName == null");
/*     */     }
/* 126 */     return oidLookupTable.get(createKey(paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5));
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
/*     */   
/*     */   private static String createKey(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 140 */     if (paramString == null)
/*     */     {
/* 142 */       throw new NullPointerException("algorithmName == null");
/*     */     }
/*     */     
/* 145 */     return paramString + "-" + paramString + "-" + paramInt1 + "-" + paramInt2 + "-" + paramInt3 + "-" + paramInt4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOid() {
/* 155 */     return this.oid;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return this.stringRepresentation;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/DefaultXMSSMTOid.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ public final class DefaultXMSSOid
/*     */   implements XMSSOid
/*     */ {
/*     */   private static final Map<String, org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid> oidLookupTable;
/*     */   private final int oid;
/*     */   private final String stringRepresentation;
/*     */   
/*     */   static {
/*  21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  22 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 10), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(1, "XMSS_SHA2_10_256"));
/*  23 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 16), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(2, "XMSS_SHA2_16_256"));
/*  24 */     hashMap.put(createKey("SHA-256", 32, 16, 67, 20), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(3, "XMSS_SHA2_20_256"));
/*  25 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 10), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(4, "XMSS_SHA2_10_512"));
/*  26 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 16), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(5, "XMSS_SHA2_16_512"));
/*  27 */     hashMap.put(createKey("SHA-512", 64, 16, 131, 20), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(6, "XMSS_SHA2_20_512"));
/*  28 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 10), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(7, "XMSS_SHAKE_10_256"));
/*  29 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 16), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(8, "XMSS_SHAKE_16_256"));
/*  30 */     hashMap.put(createKey("SHAKE128", 32, 16, 67, 20), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(9, "XMSS_SHAKE_20_256"));
/*  31 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 10), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(10, "XMSS_SHAKE_10_512"));
/*  32 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 16), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(11, "XMSS_SHAKE_16_512"));
/*  33 */     hashMap.put(createKey("SHAKE256", 64, 16, 131, 20), new org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid(12, "XMSS_SHAKE_20_512"));
/*  34 */     oidLookupTable = Collections.unmodifiableMap(hashMap);
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
/*     */   private DefaultXMSSOid(int paramInt, String paramString) {
/*  55 */     this.oid = paramInt;
/*  56 */     this.stringRepresentation = paramString;
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
/*     */   public static org.bouncycastle.pqc.crypto.xmss.DefaultXMSSOid lookup(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  70 */     if (paramString == null)
/*     */     {
/*  72 */       throw new NullPointerException("algorithmName == null");
/*     */     }
/*  74 */     return oidLookupTable.get(createKey(paramString, paramInt1, paramInt2, paramInt3, paramInt4));
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
/*     */   private static String createKey(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  88 */     if (paramString == null)
/*     */     {
/*  90 */       throw new NullPointerException("algorithmName == null");
/*     */     }
/*  92 */     return paramString + "-" + paramString + "-" + paramInt1 + "-" + paramInt2 + "-" + paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOid() {
/* 102 */     return this.oid;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return this.stringRepresentation;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/DefaultXMSSOid.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
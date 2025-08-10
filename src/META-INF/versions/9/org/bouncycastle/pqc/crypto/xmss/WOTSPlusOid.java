/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSOid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class WOTSPlusOid
/*    */   implements XMSSOid
/*    */ {
/*    */   private static final Map<String, org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid> oidLookupTable;
/*    */   private final int oid;
/*    */   private final String stringRepresentation;
/*    */   
/*    */   static {
/* 21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 22 */     hashMap.put(createKey("SHA-256", 32, 16, 67), new org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid(16777217, "WOTSP_SHA2-256_W16"));
/* 23 */     hashMap.put(createKey("SHA-512", 64, 16, 131), new org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid(33554434, "WOTSP_SHA2-512_W16"));
/* 24 */     hashMap.put(createKey("SHAKE128", 32, 16, 67), new org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid(50331651, "WOTSP_SHAKE128_W16"));
/* 25 */     hashMap.put(createKey("SHAKE256", 64, 16, 131), new org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid(67108868, "WOTSP_SHAKE256_W16"));
/* 26 */     oidLookupTable = Collections.unmodifiableMap(hashMap);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private WOTSPlusOid(int paramInt, String paramString) {
/* 47 */     this.oid = paramInt;
/* 48 */     this.stringRepresentation = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static org.bouncycastle.pqc.crypto.xmss.WOTSPlusOid lookup(String paramString, int paramInt1, int paramInt2, int paramInt3) {
/* 60 */     if (paramString == null)
/*    */     {
/* 62 */       throw new NullPointerException("algorithmName == null");
/*    */     }
/* 64 */     return oidLookupTable.get(createKey(paramString, paramInt1, paramInt2, paramInt3));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String createKey(String paramString, int paramInt1, int paramInt2, int paramInt3) {
/* 76 */     if (paramString == null)
/*    */     {
/* 78 */       throw new NullPointerException("algorithmName == null");
/*    */     }
/* 80 */     return paramString + "-" + paramString + "-" + paramInt1 + "-" + paramInt2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOid() {
/* 90 */     return this.oid;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return this.stringRepresentation;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/WOTSPlusOid.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
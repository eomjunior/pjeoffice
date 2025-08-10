/*    */ package META-INF.versions.9.org.bouncycastle.crypto.util;
/*    */ 
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ import org.bouncycastle.crypto.digests.MD5Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA1Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA224Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA256Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA384Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA3Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA512Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA512tDigest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DigestFactory
/*    */ {
/*    */   public static Digest createMD5() {
/* 20 */     return (Digest)new MD5Digest();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA1() {
/* 25 */     return (Digest)new SHA1Digest();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA224() {
/* 30 */     return (Digest)new SHA224Digest();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA256() {
/* 35 */     return (Digest)new SHA256Digest();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA384() {
/* 40 */     return (Digest)new SHA384Digest();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA512() {
/* 45 */     return (Digest)new SHA512Digest();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA512_224() {
/* 50 */     return (Digest)new SHA512tDigest(224);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA512_256() {
/* 55 */     return (Digest)new SHA512tDigest(256);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA3_224() {
/* 60 */     return (Digest)new SHA3Digest(224);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA3_256() {
/* 65 */     return (Digest)new SHA3Digest(256);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA3_384() {
/* 70 */     return (Digest)new SHA3Digest(384);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Digest createSHA3_512() {
/* 75 */     return (Digest)new SHA3Digest(512);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/util/DigestFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*    */ 
/*    */ 
/*    */ public abstract class Bits
/*    */ {
/*    */   public static int bitPermuteStep(int paramInt1, int paramInt2, int paramInt3) {
/*  7 */     int i = (paramInt1 ^ paramInt1 >>> paramInt3) & paramInt2;
/*  8 */     return i ^ i << paramInt3 ^ paramInt1;
/*    */   }
/*    */ 
/*    */   
/*    */   public static long bitPermuteStep(long paramLong1, long paramLong2, int paramInt) {
/* 13 */     long l = (paramLong1 ^ paramLong1 >>> paramInt) & paramLong2;
/* 14 */     return l ^ l << paramInt ^ paramLong1;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int bitPermuteStepSimple(int paramInt1, int paramInt2, int paramInt3) {
/* 19 */     return (paramInt1 & paramInt2) << paramInt3 | paramInt1 >>> paramInt3 & paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public static long bitPermuteStepSimple(long paramLong1, long paramLong2, int paramInt) {
/* 24 */     return (paramLong1 & paramLong2) << paramInt | paramLong1 >>> paramInt & paramLong2;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Bits.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
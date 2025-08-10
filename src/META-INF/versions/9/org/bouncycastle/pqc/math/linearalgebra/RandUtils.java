/*    */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ 
/*    */ public class RandUtils
/*    */ {
/*    */   static int nextInt(SecureRandom paramSecureRandom, int paramInt) {
/*    */     int i;
/*    */     int j;
/* 10 */     if ((paramInt & -paramInt) == paramInt)
/*    */     {
/* 12 */       return (int)(paramInt * (paramSecureRandom.nextInt() >>> 1) >> 31L);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     do {
/* 18 */       i = paramSecureRandom.nextInt() >>> 1;
/* 19 */       j = i % paramInt;
/*    */     }
/* 21 */     while (i - j + paramInt - 1 < 0);
/*    */     
/* 23 */     return j;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/RandUtils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
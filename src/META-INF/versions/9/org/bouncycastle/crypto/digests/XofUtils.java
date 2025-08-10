/*    */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*    */ 
/*    */ 
/*    */ public class XofUtils
/*    */ {
/*    */   public static byte[] leftEncode(long paramLong) {
/*  7 */     byte b = 1;
/*    */     
/*  9 */     long l = paramLong;
/* 10 */     while ((l >>= 8L) != 0L)
/*    */     {
/* 12 */       b = (byte)(b + 1);
/*    */     }
/*    */     
/* 15 */     byte[] arrayOfByte = new byte[b + 1];
/*    */     
/* 17 */     arrayOfByte[0] = b;
/*    */     
/* 19 */     for (byte b1 = 1; b1 <= b; b1++)
/*    */     {
/* 21 */       arrayOfByte[b1] = (byte)(int)(paramLong >> 8 * (b - b1));
/*    */     }
/*    */     
/* 24 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] rightEncode(long paramLong) {
/* 29 */     byte b = 1;
/*    */     
/* 31 */     long l = paramLong;
/* 32 */     while ((l >>= 8L) != 0L)
/*    */     {
/* 34 */       b = (byte)(b + 1);
/*    */     }
/*    */     
/* 37 */     byte[] arrayOfByte = new byte[b + 1];
/*    */     
/* 39 */     arrayOfByte[b] = b;
/*    */     
/* 41 */     for (byte b1 = 0; b1 < b; b1++)
/*    */     {
/* 43 */       arrayOfByte[b1] = (byte)(int)(paramLong >> 8 * (b - b1 - 1));
/*    */     }
/*    */     
/* 46 */     return arrayOfByte;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/XofUtils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
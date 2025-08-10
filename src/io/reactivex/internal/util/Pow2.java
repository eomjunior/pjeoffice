/*    */ package io.reactivex.internal.util;
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
/*    */ public final class Pow2
/*    */ {
/*    */   private Pow2() {
/* 22 */     throw new IllegalStateException("No instances!");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int roundToPowerOfTwo(int value) {
/* 33 */     return 1 << 32 - Integer.numberOfLeadingZeros(value - 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isPowerOfTwo(int value) {
/* 43 */     return ((value & value - 1) == 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/Pow2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
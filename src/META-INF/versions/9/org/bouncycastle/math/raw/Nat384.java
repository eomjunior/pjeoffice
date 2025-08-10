/*    */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*    */ 
/*    */ import org.bouncycastle.math.raw.Nat;
/*    */ import org.bouncycastle.math.raw.Nat192;
/*    */ 
/*    */ public abstract class Nat384 {
/*    */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  8 */     Nat192.mul(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  9 */     Nat192.mul(paramArrayOfint1, 6, paramArrayOfint2, 6, paramArrayOfint3, 12);
/*    */     
/* 11 */     int i = Nat192.addToEachOther(paramArrayOfint3, 6, paramArrayOfint3, 12);
/* 12 */     int j = i + Nat192.addTo(paramArrayOfint3, 0, paramArrayOfint3, 6, 0);
/* 13 */     i += Nat192.addTo(paramArrayOfint3, 18, paramArrayOfint3, 12, j);
/*    */     
/* 15 */     int[] arrayOfInt1 = Nat192.create(), arrayOfInt2 = Nat192.create();
/* 16 */     boolean bool = (Nat192.diff(paramArrayOfint1, 6, paramArrayOfint1, 0, arrayOfInt1, 0) != Nat192.diff(paramArrayOfint2, 6, paramArrayOfint2, 0, arrayOfInt2, 0)) ? true : false;
/*    */     
/* 18 */     int[] arrayOfInt3 = Nat192.createExt();
/* 19 */     Nat192.mul(arrayOfInt1, arrayOfInt2, arrayOfInt3);
/*    */     
/* 21 */     i += bool ? Nat.addTo(12, arrayOfInt3, 0, paramArrayOfint3, 6) : Nat.subFrom(12, arrayOfInt3, 0, paramArrayOfint3, 6);
/* 22 */     Nat.addWordAt(24, i, paramArrayOfint3, 18);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 27 */     Nat192.square(paramArrayOfint1, paramArrayOfint2);
/* 28 */     Nat192.square(paramArrayOfint1, 6, paramArrayOfint2, 12);
/*    */     
/* 30 */     int i = Nat192.addToEachOther(paramArrayOfint2, 6, paramArrayOfint2, 12);
/* 31 */     int j = i + Nat192.addTo(paramArrayOfint2, 0, paramArrayOfint2, 6, 0);
/* 32 */     i += Nat192.addTo(paramArrayOfint2, 18, paramArrayOfint2, 12, j);
/*    */     
/* 34 */     int[] arrayOfInt1 = Nat192.create();
/* 35 */     Nat192.diff(paramArrayOfint1, 6, paramArrayOfint1, 0, arrayOfInt1, 0);
/*    */     
/* 37 */     int[] arrayOfInt2 = Nat192.createExt();
/* 38 */     Nat192.square(arrayOfInt1, arrayOfInt2);
/*    */     
/* 40 */     i += Nat.subFrom(12, arrayOfInt2, 0, paramArrayOfint2, 6);
/* 41 */     Nat.addWordAt(24, i, paramArrayOfint2, 18);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat384.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
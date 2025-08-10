/*    */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*    */ 
/*    */ import org.bouncycastle.math.raw.Nat;
/*    */ import org.bouncycastle.math.raw.Nat256;
/*    */ 
/*    */ public abstract class Nat512 {
/*    */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  8 */     Nat256.mul(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  9 */     Nat256.mul(paramArrayOfint1, 8, paramArrayOfint2, 8, paramArrayOfint3, 16);
/*    */     
/* 11 */     int i = Nat256.addToEachOther(paramArrayOfint3, 8, paramArrayOfint3, 16);
/* 12 */     int j = i + Nat256.addTo(paramArrayOfint3, 0, paramArrayOfint3, 8, 0);
/* 13 */     i += Nat256.addTo(paramArrayOfint3, 24, paramArrayOfint3, 16, j);
/*    */     
/* 15 */     int[] arrayOfInt1 = Nat256.create(), arrayOfInt2 = Nat256.create();
/* 16 */     boolean bool = (Nat256.diff(paramArrayOfint1, 8, paramArrayOfint1, 0, arrayOfInt1, 0) != Nat256.diff(paramArrayOfint2, 8, paramArrayOfint2, 0, arrayOfInt2, 0)) ? true : false;
/*    */     
/* 18 */     int[] arrayOfInt3 = Nat256.createExt();
/* 19 */     Nat256.mul(arrayOfInt1, arrayOfInt2, arrayOfInt3);
/*    */     
/* 21 */     i += bool ? Nat.addTo(16, arrayOfInt3, 0, paramArrayOfint3, 8) : Nat.subFrom(16, arrayOfInt3, 0, paramArrayOfint3, 8);
/* 22 */     Nat.addWordAt(32, i, paramArrayOfint3, 24);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 27 */     Nat256.square(paramArrayOfint1, paramArrayOfint2);
/* 28 */     Nat256.square(paramArrayOfint1, 8, paramArrayOfint2, 16);
/*    */     
/* 30 */     int i = Nat256.addToEachOther(paramArrayOfint2, 8, paramArrayOfint2, 16);
/* 31 */     int j = i + Nat256.addTo(paramArrayOfint2, 0, paramArrayOfint2, 8, 0);
/* 32 */     i += Nat256.addTo(paramArrayOfint2, 24, paramArrayOfint2, 16, j);
/*    */     
/* 34 */     int[] arrayOfInt1 = Nat256.create();
/* 35 */     Nat256.diff(paramArrayOfint1, 8, paramArrayOfint1, 0, arrayOfInt1, 0);
/*    */     
/* 37 */     int[] arrayOfInt2 = Nat256.createExt();
/* 38 */     Nat256.square(arrayOfInt1, arrayOfInt2);
/*    */     
/* 40 */     i += Nat.subFrom(16, arrayOfInt2, 0, paramArrayOfint2, 8);
/* 41 */     Nat.addWordAt(32, i, paramArrayOfint2, 24);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat512.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
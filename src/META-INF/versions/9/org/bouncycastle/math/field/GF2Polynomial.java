/*    */ package META-INF.versions.9.org.bouncycastle.math.field;
/*    */ 
/*    */ import org.bouncycastle.math.field.Polynomial;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ class GF2Polynomial
/*    */   implements Polynomial {
/*    */   protected final int[] exponents;
/*    */   
/*    */   GF2Polynomial(int[] paramArrayOfint) {
/* 11 */     this.exponents = Arrays.clone(paramArrayOfint);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDegree() {
/* 16 */     return this.exponents[this.exponents.length - 1];
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getExponentsPresent() {
/* 21 */     return Arrays.clone(this.exponents);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 26 */     if (this == paramObject)
/*    */     {
/* 28 */       return true;
/*    */     }
/* 30 */     if (!(paramObject instanceof org.bouncycastle.math.field.GF2Polynomial))
/*    */     {
/* 32 */       return false;
/*    */     }
/* 34 */     org.bouncycastle.math.field.GF2Polynomial gF2Polynomial = (org.bouncycastle.math.field.GF2Polynomial)paramObject;
/* 35 */     return Arrays.areEqual(this.exponents, gF2Polynomial.exponents);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 40 */     return Arrays.hashCode(this.exponents);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/field/GF2Polynomial.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
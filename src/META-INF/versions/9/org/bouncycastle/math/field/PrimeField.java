/*    */ package META-INF.versions.9.org.bouncycastle.math.field;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.field.FiniteField;
/*    */ 
/*    */ class PrimeField
/*    */   implements FiniteField {
/*    */   protected final BigInteger characteristic;
/*    */   
/*    */   PrimeField(BigInteger paramBigInteger) {
/* 11 */     this.characteristic = paramBigInteger;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getCharacteristic() {
/* 16 */     return this.characteristic;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDimension() {
/* 21 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 26 */     if (this == paramObject)
/*    */     {
/* 28 */       return true;
/*    */     }
/* 30 */     if (!(paramObject instanceof org.bouncycastle.math.field.PrimeField))
/*    */     {
/* 32 */       return false;
/*    */     }
/* 34 */     org.bouncycastle.math.field.PrimeField primeField = (org.bouncycastle.math.field.PrimeField)paramObject;
/* 35 */     return this.characteristic.equals(primeField.characteristic);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 40 */     return this.characteristic.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/field/PrimeField.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
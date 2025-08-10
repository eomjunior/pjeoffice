/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECConstants;
/*    */ import org.bouncycastle.util.BigIntegers;
/*    */ 
/*    */ public abstract class ECFieldElement implements ECConstants {
/*    */   public abstract BigInteger toBigInteger();
/*    */   
/*    */   public abstract String getFieldName();
/*    */   
/*    */   public abstract int getFieldSize();
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement add(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement);
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement addOne();
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement subtract(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement);
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement multiply(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement);
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement divide(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement);
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement negate();
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement square();
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement invert();
/*    */   
/*    */   public abstract org.bouncycastle.math.ec.ECFieldElement sqrt();
/*    */   
/*    */   public int bitLength() {
/* 33 */     return toBigInteger().bitLength();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOne() {
/* 38 */     return (bitLength() == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isZero() {
/* 43 */     return (0 == toBigInteger().signum());
/*    */   }
/*    */ 
/*    */   
/*    */   public org.bouncycastle.math.ec.ECFieldElement multiplyMinusProduct(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement1, org.bouncycastle.math.ec.ECFieldElement paramECFieldElement2, org.bouncycastle.math.ec.ECFieldElement paramECFieldElement3) {
/* 48 */     return multiply(paramECFieldElement1).subtract(paramECFieldElement2.multiply(paramECFieldElement3));
/*    */   }
/*    */ 
/*    */   
/*    */   public org.bouncycastle.math.ec.ECFieldElement multiplyPlusProduct(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement1, org.bouncycastle.math.ec.ECFieldElement paramECFieldElement2, org.bouncycastle.math.ec.ECFieldElement paramECFieldElement3) {
/* 53 */     return multiply(paramECFieldElement1).add(paramECFieldElement2.multiply(paramECFieldElement3));
/*    */   }
/*    */ 
/*    */   
/*    */   public org.bouncycastle.math.ec.ECFieldElement squareMinusProduct(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement1, org.bouncycastle.math.ec.ECFieldElement paramECFieldElement2) {
/* 58 */     return square().subtract(paramECFieldElement1.multiply(paramECFieldElement2));
/*    */   }
/*    */ 
/*    */   
/*    */   public org.bouncycastle.math.ec.ECFieldElement squarePlusProduct(org.bouncycastle.math.ec.ECFieldElement paramECFieldElement1, org.bouncycastle.math.ec.ECFieldElement paramECFieldElement2) {
/* 63 */     return square().add(paramECFieldElement1.multiply(paramECFieldElement2));
/*    */   }
/*    */ 
/*    */   
/*    */   public org.bouncycastle.math.ec.ECFieldElement squarePow(int paramInt) {
/* 68 */     org.bouncycastle.math.ec.ECFieldElement eCFieldElement = this;
/* 69 */     for (byte b = 0; b < paramInt; b++)
/*    */     {
/* 71 */       eCFieldElement = eCFieldElement.square();
/*    */     }
/* 73 */     return eCFieldElement;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean testBitZero() {
/* 78 */     return toBigInteger().testBit(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return toBigInteger().toString(16);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getEncoded() {
/* 88 */     return BigIntegers.asUnsignedByteArray((getFieldSize() + 7) / 8, toBigInteger());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ECFieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
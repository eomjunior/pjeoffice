/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT113Field;
/*     */ import org.bouncycastle.math.raw.Nat128;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class SecT113FieldElement
/*     */   extends ECFieldElement.AbstractF2m
/*     */ {
/*     */   protected long[] x;
/*     */   
/*     */   public SecT113FieldElement(BigInteger paramBigInteger) {
/*  15 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 113)
/*     */     {
/*  17 */       throw new IllegalArgumentException("x value invalid for SecT113FieldElement");
/*     */     }
/*     */     
/*  20 */     this.x = SecT113Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecT113FieldElement() {
/*  25 */     this.x = Nat128.create64();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecT113FieldElement(long[] paramArrayOflong) {
/*  30 */     this.x = paramArrayOflong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  40 */     return Nat128.isOne64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  45 */     return Nat128.isZero64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  50 */     return ((this.x[0] & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  55 */     return Nat128.toBigInteger64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  60 */     return "SecT113Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  65 */     return 113;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  70 */     long[] arrayOfLong = Nat128.create64();
/*  71 */     SecT113Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement).x, arrayOfLong);
/*  72 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  77 */     long[] arrayOfLong = Nat128.create64();
/*  78 */     SecT113Field.addOne(this.x, arrayOfLong);
/*  79 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  85 */     return add(paramECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     long[] arrayOfLong = Nat128.create64();
/*  91 */     SecT113Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement).x, arrayOfLong);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyMinusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/*  97 */     return multiplyPlusProduct(paramECFieldElement1, paramECFieldElement2, paramECFieldElement3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyPlusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/* 102 */     long[] arrayOfLong1 = this.x, arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement1).x;
/* 103 */     long[] arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement2).x, arrayOfLong4 = ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement3).x;
/*     */     
/* 105 */     long[] arrayOfLong5 = Nat128.createExt64();
/* 106 */     SecT113Field.multiplyAddToExt(arrayOfLong1, arrayOfLong2, arrayOfLong5);
/* 107 */     SecT113Field.multiplyAddToExt(arrayOfLong3, arrayOfLong4, arrayOfLong5);
/*     */     
/* 109 */     long[] arrayOfLong6 = Nat128.create64();
/* 110 */     SecT113Field.reduce(arrayOfLong5, arrayOfLong6);
/* 111 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong6);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/* 116 */     return multiply(paramECFieldElement.invert());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 121 */     return (ECFieldElement)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 126 */     long[] arrayOfLong = Nat128.create64();
/* 127 */     SecT113Field.square(this.x, arrayOfLong);
/* 128 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squareMinusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/* 133 */     return squarePlusProduct(paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squarePlusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/* 138 */     long[] arrayOfLong1 = this.x;
/* 139 */     long[] arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement1).x, arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramECFieldElement2).x;
/*     */     
/* 141 */     long[] arrayOfLong4 = Nat128.createExt64();
/* 142 */     SecT113Field.squareAddToExt(arrayOfLong1, arrayOfLong4);
/* 143 */     SecT113Field.multiplyAddToExt(arrayOfLong2, arrayOfLong3, arrayOfLong4);
/*     */     
/* 145 */     long[] arrayOfLong5 = Nat128.create64();
/* 146 */     SecT113Field.reduce(arrayOfLong4, arrayOfLong5);
/* 147 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong5);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squarePow(int paramInt) {
/* 152 */     if (paramInt < 1)
/*     */     {
/* 154 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 157 */     long[] arrayOfLong = Nat128.create64();
/* 158 */     SecT113Field.squareN(this.x, paramInt, arrayOfLong);
/* 159 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement halfTrace() {
/* 164 */     long[] arrayOfLong = Nat128.create64();
/* 165 */     SecT113Field.halfTrace(this.x, arrayOfLong);
/* 166 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFastTrace() {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int trace() {
/* 176 */     return SecT113Field.trace(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 181 */     long[] arrayOfLong = Nat128.create64();
/* 182 */     SecT113Field.invert(this.x, arrayOfLong);
/* 183 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 188 */     long[] arrayOfLong = Nat128.create64();
/* 189 */     SecT113Field.sqrt(this.x, arrayOfLong);
/* 190 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT113FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRepresentation() {
/* 195 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/* 200 */     return 113;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/* 205 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/* 210 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 215 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 220 */     if (paramObject == this)
/*     */     {
/* 222 */       return true;
/*     */     }
/*     */     
/* 225 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecT113FieldElement))
/*     */     {
/* 227 */       return false;
/*     */     }
/*     */     
/* 230 */     org.bouncycastle.math.ec.custom.sec.SecT113FieldElement secT113FieldElement = (org.bouncycastle.math.ec.custom.sec.SecT113FieldElement)paramObject;
/* 231 */     return Nat128.eq64(this.x, secT113FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 236 */     return 0x1B971 ^ Arrays.hashCode(this.x, 0, 2);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT113FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
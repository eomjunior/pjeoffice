/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT163Field;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class SecT163FieldElement
/*     */   extends ECFieldElement.AbstractF2m
/*     */ {
/*     */   protected long[] x;
/*     */   
/*     */   public SecT163FieldElement(BigInteger paramBigInteger) {
/*  15 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 163)
/*     */     {
/*  17 */       throw new IllegalArgumentException("x value invalid for SecT163FieldElement");
/*     */     }
/*     */     
/*  20 */     this.x = SecT163Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecT163FieldElement() {
/*  25 */     this.x = Nat192.create64();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecT163FieldElement(long[] paramArrayOflong) {
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
/*  40 */     return Nat192.isOne64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  45 */     return Nat192.isZero64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  50 */     return ((this.x[0] & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  55 */     return Nat192.toBigInteger64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  60 */     return "SecT163Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  65 */     return 163;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  70 */     long[] arrayOfLong = Nat192.create64();
/*  71 */     SecT163Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement).x, arrayOfLong);
/*  72 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  77 */     long[] arrayOfLong = Nat192.create64();
/*  78 */     SecT163Field.addOne(this.x, arrayOfLong);
/*  79 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
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
/*  90 */     long[] arrayOfLong = Nat192.create64();
/*  91 */     SecT163Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement).x, arrayOfLong);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyMinusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/*  97 */     return multiplyPlusProduct(paramECFieldElement1, paramECFieldElement2, paramECFieldElement3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyPlusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/* 102 */     long[] arrayOfLong1 = this.x, arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement1).x;
/* 103 */     long[] arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement2).x, arrayOfLong4 = ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement3).x;
/*     */     
/* 105 */     long[] arrayOfLong5 = Nat192.createExt64();
/* 106 */     SecT163Field.multiplyAddToExt(arrayOfLong1, arrayOfLong2, arrayOfLong5);
/* 107 */     SecT163Field.multiplyAddToExt(arrayOfLong3, arrayOfLong4, arrayOfLong5);
/*     */     
/* 109 */     long[] arrayOfLong6 = Nat192.create64();
/* 110 */     SecT163Field.reduce(arrayOfLong5, arrayOfLong6);
/* 111 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong6);
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
/* 126 */     long[] arrayOfLong = Nat192.create64();
/* 127 */     SecT163Field.square(this.x, arrayOfLong);
/* 128 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
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
/* 139 */     long[] arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement1).x, arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramECFieldElement2).x;
/*     */     
/* 141 */     long[] arrayOfLong4 = Nat192.createExt64();
/* 142 */     SecT163Field.squareAddToExt(arrayOfLong1, arrayOfLong4);
/* 143 */     SecT163Field.multiplyAddToExt(arrayOfLong2, arrayOfLong3, arrayOfLong4);
/*     */     
/* 145 */     long[] arrayOfLong5 = Nat192.create64();
/* 146 */     SecT163Field.reduce(arrayOfLong4, arrayOfLong5);
/* 147 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong5);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squarePow(int paramInt) {
/* 152 */     if (paramInt < 1)
/*     */     {
/* 154 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 157 */     long[] arrayOfLong = Nat192.create64();
/* 158 */     SecT163Field.squareN(this.x, paramInt, arrayOfLong);
/* 159 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement halfTrace() {
/* 164 */     long[] arrayOfLong = Nat192.create64();
/* 165 */     SecT163Field.halfTrace(this.x, arrayOfLong);
/* 166 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFastTrace() {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int trace() {
/* 176 */     return SecT163Field.trace(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 181 */     long[] arrayOfLong = Nat192.create64();
/* 182 */     SecT163Field.invert(this.x, arrayOfLong);
/* 183 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 188 */     long[] arrayOfLong = Nat192.create64();
/* 189 */     SecT163Field.sqrt(this.x, arrayOfLong);
/* 190 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT163FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRepresentation() {
/* 195 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/* 200 */     return 163;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/* 205 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/* 210 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 215 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 220 */     if (paramObject == this)
/*     */     {
/* 222 */       return true;
/*     */     }
/*     */     
/* 225 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecT163FieldElement))
/*     */     {
/* 227 */       return false;
/*     */     }
/*     */     
/* 230 */     org.bouncycastle.math.ec.custom.sec.SecT163FieldElement secT163FieldElement = (org.bouncycastle.math.ec.custom.sec.SecT163FieldElement)paramObject;
/* 231 */     return Nat192.eq64(this.x, secT163FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 236 */     return 0x27FB3 ^ Arrays.hashCode(this.x, 0, 3);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT163FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
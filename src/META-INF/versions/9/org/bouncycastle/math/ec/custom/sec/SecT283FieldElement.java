/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT283Field;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat320;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class SecT283FieldElement
/*     */   extends ECFieldElement.AbstractF2m
/*     */ {
/*     */   protected long[] x;
/*     */   
/*     */   public SecT283FieldElement(BigInteger paramBigInteger) {
/*  16 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 283)
/*     */     {
/*  18 */       throw new IllegalArgumentException("x value invalid for SecT283FieldElement");
/*     */     }
/*     */     
/*  21 */     this.x = SecT283Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecT283FieldElement() {
/*  26 */     this.x = Nat320.create64();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecT283FieldElement(long[] paramArrayOflong) {
/*  31 */     this.x = paramArrayOflong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  41 */     return Nat320.isOne64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  46 */     return Nat320.isZero64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  51 */     return ((this.x[0] & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  56 */     return Nat320.toBigInteger64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  61 */     return "SecT283Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  66 */     return 283;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  71 */     long[] arrayOfLong = Nat320.create64();
/*  72 */     SecT283Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement).x, arrayOfLong);
/*  73 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  78 */     long[] arrayOfLong = Nat320.create64();
/*  79 */     SecT283Field.addOne(this.x, arrayOfLong);
/*  80 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  86 */     return add(paramECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  91 */     long[] arrayOfLong = Nat320.create64();
/*  92 */     SecT283Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement).x, arrayOfLong);
/*  93 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyMinusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/*  98 */     return multiplyPlusProduct(paramECFieldElement1, paramECFieldElement2, paramECFieldElement3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyPlusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/* 103 */     long[] arrayOfLong1 = this.x, arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement1).x;
/* 104 */     long[] arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement2).x, arrayOfLong4 = ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement3).x;
/*     */     
/* 106 */     long[] arrayOfLong5 = Nat.create64(9);
/* 107 */     SecT283Field.multiplyAddToExt(arrayOfLong1, arrayOfLong2, arrayOfLong5);
/* 108 */     SecT283Field.multiplyAddToExt(arrayOfLong3, arrayOfLong4, arrayOfLong5);
/*     */     
/* 110 */     long[] arrayOfLong6 = Nat320.create64();
/* 111 */     SecT283Field.reduce(arrayOfLong5, arrayOfLong6);
/* 112 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong6);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/* 117 */     return multiply(paramECFieldElement.invert());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 122 */     return (ECFieldElement)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 127 */     long[] arrayOfLong = Nat320.create64();
/* 128 */     SecT283Field.square(this.x, arrayOfLong);
/* 129 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squareMinusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/* 134 */     return squarePlusProduct(paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squarePlusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/* 139 */     long[] arrayOfLong1 = this.x;
/* 140 */     long[] arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement1).x, arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramECFieldElement2).x;
/*     */     
/* 142 */     long[] arrayOfLong4 = Nat.create64(9);
/* 143 */     SecT283Field.squareAddToExt(arrayOfLong1, arrayOfLong4);
/* 144 */     SecT283Field.multiplyAddToExt(arrayOfLong2, arrayOfLong3, arrayOfLong4);
/*     */     
/* 146 */     long[] arrayOfLong5 = Nat320.create64();
/* 147 */     SecT283Field.reduce(arrayOfLong4, arrayOfLong5);
/* 148 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong5);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squarePow(int paramInt) {
/* 153 */     if (paramInt < 1)
/*     */     {
/* 155 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 158 */     long[] arrayOfLong = Nat320.create64();
/* 159 */     SecT283Field.squareN(this.x, paramInt, arrayOfLong);
/* 160 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement halfTrace() {
/* 165 */     long[] arrayOfLong = Nat320.create64();
/* 166 */     SecT283Field.halfTrace(this.x, arrayOfLong);
/* 167 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFastTrace() {
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int trace() {
/* 177 */     return SecT283Field.trace(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 182 */     long[] arrayOfLong = Nat320.create64();
/* 183 */     SecT283Field.invert(this.x, arrayOfLong);
/* 184 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 189 */     long[] arrayOfLong = Nat320.create64();
/* 190 */     SecT283Field.sqrt(this.x, arrayOfLong);
/* 191 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT283FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRepresentation() {
/* 196 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/* 201 */     return 283;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/* 206 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/* 211 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 216 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 221 */     if (paramObject == this)
/*     */     {
/* 223 */       return true;
/*     */     }
/*     */     
/* 226 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecT283FieldElement))
/*     */     {
/* 228 */       return false;
/*     */     }
/*     */     
/* 231 */     org.bouncycastle.math.ec.custom.sec.SecT283FieldElement secT283FieldElement = (org.bouncycastle.math.ec.custom.sec.SecT283FieldElement)paramObject;
/* 232 */     return Nat320.eq64(this.x, secT283FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 237 */     return 0x2B33AB ^ Arrays.hashCode(this.x, 0, 5);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT283FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
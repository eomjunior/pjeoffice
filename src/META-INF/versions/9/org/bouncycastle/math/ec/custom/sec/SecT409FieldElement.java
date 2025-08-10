/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT409Field;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat448;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class SecT409FieldElement
/*     */   extends ECFieldElement.AbstractF2m
/*     */ {
/*     */   protected long[] x;
/*     */   
/*     */   public SecT409FieldElement(BigInteger paramBigInteger) {
/*  16 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 409)
/*     */     {
/*  18 */       throw new IllegalArgumentException("x value invalid for SecT409FieldElement");
/*     */     }
/*     */     
/*  21 */     this.x = SecT409Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecT409FieldElement() {
/*  26 */     this.x = Nat448.create64();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecT409FieldElement(long[] paramArrayOflong) {
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
/*  41 */     return Nat448.isOne64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  46 */     return Nat448.isZero64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  51 */     return ((this.x[0] & 0x1L) != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  56 */     return Nat448.toBigInteger64(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  61 */     return "SecT409Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  66 */     return 409;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  71 */     long[] arrayOfLong = Nat448.create64();
/*  72 */     SecT409Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement).x, arrayOfLong);
/*  73 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  78 */     long[] arrayOfLong = Nat448.create64();
/*  79 */     SecT409Field.addOne(this.x, arrayOfLong);
/*  80 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
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
/*  91 */     long[] arrayOfLong = Nat448.create64();
/*  92 */     SecT409Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement).x, arrayOfLong);
/*  93 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyMinusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/*  98 */     return multiplyPlusProduct(paramECFieldElement1, paramECFieldElement2, paramECFieldElement3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiplyPlusProduct(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3) {
/* 103 */     long[] arrayOfLong1 = this.x, arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement1).x;
/* 104 */     long[] arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement2).x, arrayOfLong4 = ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement3).x;
/*     */     
/* 106 */     long[] arrayOfLong5 = Nat.create64(13);
/* 107 */     SecT409Field.multiplyAddToExt(arrayOfLong1, arrayOfLong2, arrayOfLong5);
/* 108 */     SecT409Field.multiplyAddToExt(arrayOfLong3, arrayOfLong4, arrayOfLong5);
/*     */     
/* 110 */     long[] arrayOfLong6 = Nat448.create64();
/* 111 */     SecT409Field.reduce(arrayOfLong5, arrayOfLong6);
/* 112 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong6);
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
/* 127 */     long[] arrayOfLong = Nat448.create64();
/* 128 */     SecT409Field.square(this.x, arrayOfLong);
/* 129 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
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
/* 140 */     long[] arrayOfLong2 = ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement1).x, arrayOfLong3 = ((org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramECFieldElement2).x;
/*     */     
/* 142 */     long[] arrayOfLong4 = Nat.create64(13);
/* 143 */     SecT409Field.squareAddToExt(arrayOfLong1, arrayOfLong4);
/* 144 */     SecT409Field.multiplyAddToExt(arrayOfLong2, arrayOfLong3, arrayOfLong4);
/*     */     
/* 146 */     long[] arrayOfLong5 = Nat448.create64();
/* 147 */     SecT409Field.reduce(arrayOfLong4, arrayOfLong5);
/* 148 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong5);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement squarePow(int paramInt) {
/* 153 */     if (paramInt < 1)
/*     */     {
/* 155 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 158 */     long[] arrayOfLong = Nat448.create64();
/* 159 */     SecT409Field.squareN(this.x, paramInt, arrayOfLong);
/* 160 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement halfTrace() {
/* 165 */     long[] arrayOfLong = Nat448.create64();
/* 166 */     SecT409Field.halfTrace(this.x, arrayOfLong);
/* 167 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasFastTrace() {
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int trace() {
/* 177 */     return SecT409Field.trace(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 182 */     long[] arrayOfLong = Nat448.create64();
/* 183 */     SecT409Field.invert(this.x, arrayOfLong);
/* 184 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 189 */     long[] arrayOfLong = Nat448.create64();
/* 190 */     SecT409Field.sqrt(this.x, arrayOfLong);
/* 191 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecT409FieldElement(arrayOfLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRepresentation() {
/* 196 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/* 201 */     return 409;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/* 206 */     return 87;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/* 211 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 221 */     if (paramObject == this)
/*     */     {
/* 223 */       return true;
/*     */     }
/*     */     
/* 226 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecT409FieldElement))
/*     */     {
/* 228 */       return false;
/*     */     }
/*     */     
/* 231 */     org.bouncycastle.math.ec.custom.sec.SecT409FieldElement secT409FieldElement = (org.bouncycastle.math.ec.custom.sec.SecT409FieldElement)paramObject;
/* 232 */     return Nat448.eq64(this.x, secT409FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 237 */     return 0x3E68E7 ^ Arrays.hashCode(this.x, 0, 7);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT409FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
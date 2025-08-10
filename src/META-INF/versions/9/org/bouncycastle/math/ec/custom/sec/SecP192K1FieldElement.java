/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192K1Field;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP192K1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFEE37"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP192K1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP192K1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP192K1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP192K1FieldElement() {
/*  29 */     this.x = Nat192.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP192K1FieldElement(int[] paramArrayOfint) {
/*  34 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  39 */     return Nat192.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  44 */     return Nat192.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  49 */     return (Nat192.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  54 */     return Nat192.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  59 */     return "SecP192K1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  64 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  69 */     int[] arrayOfInt = Nat192.create();
/*  70 */     SecP192K1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat192.create();
/*  77 */     SecP192K1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat192.create();
/*  84 */     SecP192K1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat192.create();
/*  91 */     SecP192K1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat192.create();
/*  99 */     SecP192K1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP192K1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat192.create();
/* 107 */     SecP192K1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat192.create();
/* 114 */     SecP192K1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat192.create();
/* 122 */     SecP192K1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 142 */     int[] arrayOfInt1 = this.x;
/* 143 */     if (Nat192.isZero(arrayOfInt1) || Nat192.isOne(arrayOfInt1))
/*     */     {
/* 145 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 148 */     int[] arrayOfInt2 = Nat192.create();
/* 149 */     SecP192K1Field.square(arrayOfInt1, arrayOfInt2);
/* 150 */     SecP192K1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 151 */     int[] arrayOfInt3 = Nat192.create();
/* 152 */     SecP192K1Field.square(arrayOfInt2, arrayOfInt3);
/* 153 */     SecP192K1Field.multiply(arrayOfInt3, arrayOfInt1, arrayOfInt3);
/* 154 */     int[] arrayOfInt4 = Nat192.create();
/* 155 */     SecP192K1Field.squareN(arrayOfInt3, 3, arrayOfInt4);
/* 156 */     SecP192K1Field.multiply(arrayOfInt4, arrayOfInt3, arrayOfInt4);
/* 157 */     int[] arrayOfInt5 = arrayOfInt4;
/* 158 */     SecP192K1Field.squareN(arrayOfInt4, 2, arrayOfInt5);
/* 159 */     SecP192K1Field.multiply(arrayOfInt5, arrayOfInt2, arrayOfInt5);
/* 160 */     int[] arrayOfInt6 = arrayOfInt2;
/* 161 */     SecP192K1Field.squareN(arrayOfInt5, 8, arrayOfInt6);
/* 162 */     SecP192K1Field.multiply(arrayOfInt6, arrayOfInt5, arrayOfInt6);
/* 163 */     int[] arrayOfInt7 = arrayOfInt5;
/* 164 */     SecP192K1Field.squareN(arrayOfInt6, 3, arrayOfInt7);
/* 165 */     SecP192K1Field.multiply(arrayOfInt7, arrayOfInt3, arrayOfInt7);
/* 166 */     int[] arrayOfInt8 = Nat192.create();
/* 167 */     SecP192K1Field.squareN(arrayOfInt7, 16, arrayOfInt8);
/* 168 */     SecP192K1Field.multiply(arrayOfInt8, arrayOfInt6, arrayOfInt8);
/* 169 */     int[] arrayOfInt9 = arrayOfInt6;
/* 170 */     SecP192K1Field.squareN(arrayOfInt8, 35, arrayOfInt9);
/* 171 */     SecP192K1Field.multiply(arrayOfInt9, arrayOfInt8, arrayOfInt9);
/* 172 */     int[] arrayOfInt10 = arrayOfInt8;
/* 173 */     SecP192K1Field.squareN(arrayOfInt9, 70, arrayOfInt10);
/* 174 */     SecP192K1Field.multiply(arrayOfInt10, arrayOfInt9, arrayOfInt10);
/* 175 */     int[] arrayOfInt11 = arrayOfInt9;
/* 176 */     SecP192K1Field.squareN(arrayOfInt10, 19, arrayOfInt11);
/* 177 */     SecP192K1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt11);
/*     */     
/* 179 */     int[] arrayOfInt12 = arrayOfInt11;
/* 180 */     SecP192K1Field.squareN(arrayOfInt12, 20, arrayOfInt12);
/* 181 */     SecP192K1Field.multiply(arrayOfInt12, arrayOfInt7, arrayOfInt12);
/* 182 */     SecP192K1Field.squareN(arrayOfInt12, 4, arrayOfInt12);
/* 183 */     SecP192K1Field.multiply(arrayOfInt12, arrayOfInt3, arrayOfInt12);
/* 184 */     SecP192K1Field.squareN(arrayOfInt12, 6, arrayOfInt12);
/* 185 */     SecP192K1Field.multiply(arrayOfInt12, arrayOfInt3, arrayOfInt12);
/* 186 */     SecP192K1Field.square(arrayOfInt12, arrayOfInt12);
/*     */     
/* 188 */     int[] arrayOfInt13 = arrayOfInt3;
/* 189 */     SecP192K1Field.square(arrayOfInt12, arrayOfInt13);
/*     */     
/* 191 */     return Nat192.eq(arrayOfInt1, arrayOfInt13) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement(arrayOfInt12) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 196 */     if (paramObject == this)
/*     */     {
/* 198 */       return true;
/*     */     }
/*     */     
/* 201 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement))
/*     */     {
/* 203 */       return false;
/*     */     }
/*     */     
/* 206 */     org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement secP192K1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement)paramObject;
/* 207 */     return Nat192.eq(this.x, secP192K1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 212 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 6);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP192K1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
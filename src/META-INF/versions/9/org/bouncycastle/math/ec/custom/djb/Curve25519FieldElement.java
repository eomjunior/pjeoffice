/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.djb;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.djb.Curve25519Field;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class Curve25519FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  11 */   public static final BigInteger Q = Nat256.toBigInteger(Curve25519Field.P);
/*     */ 
/*     */   
/*  14 */   private static final int[] PRECOMP_POW2 = new int[] { 1242472624, -991028441, -1389370248, 792926214, 1039914919, 726466713, 1338105611, 730014848 };
/*     */ 
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public Curve25519FieldElement(BigInteger paramBigInteger) {
/*  21 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  23 */       throw new IllegalArgumentException("x value invalid for Curve25519FieldElement");
/*     */     }
/*     */     
/*  26 */     this.x = Curve25519Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public Curve25519FieldElement() {
/*  31 */     this.x = Nat256.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Curve25519FieldElement(int[] paramArrayOfint) {
/*  36 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  41 */     return Nat256.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  46 */     return Nat256.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  51 */     return (Nat256.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  56 */     return Nat256.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  61 */     return "Curve25519Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  66 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  71 */     int[] arrayOfInt = Nat256.create();
/*  72 */     Curve25519Field.add(this.x, ((org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement)paramECFieldElement).x, arrayOfInt);
/*  73 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  78 */     int[] arrayOfInt = Nat256.create();
/*  79 */     Curve25519Field.addOne(this.x, arrayOfInt);
/*  80 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  85 */     int[] arrayOfInt = Nat256.create();
/*  86 */     Curve25519Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement)paramECFieldElement).x, arrayOfInt);
/*  87 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  92 */     int[] arrayOfInt = Nat256.create();
/*  93 */     Curve25519Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement)paramECFieldElement).x, arrayOfInt);
/*  94 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/* 100 */     int[] arrayOfInt = Nat256.create();
/* 101 */     Curve25519Field.inv(((org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement)paramECFieldElement).x, arrayOfInt);
/* 102 */     Curve25519Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 103 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 108 */     int[] arrayOfInt = Nat256.create();
/* 109 */     Curve25519Field.negate(this.x, arrayOfInt);
/* 110 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 115 */     int[] arrayOfInt = Nat256.create();
/* 116 */     Curve25519Field.square(this.x, arrayOfInt);
/* 117 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 123 */     int[] arrayOfInt = Nat256.create();
/* 124 */     Curve25519Field.inv(this.x, arrayOfInt);
/* 125 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt);
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
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 146 */     int[] arrayOfInt1 = this.x;
/* 147 */     if (Nat256.isZero(arrayOfInt1) || Nat256.isOne(arrayOfInt1))
/*     */     {
/* 149 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 152 */     int[] arrayOfInt2 = Nat256.create();
/* 153 */     Curve25519Field.square(arrayOfInt1, arrayOfInt2);
/* 154 */     Curve25519Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 155 */     int[] arrayOfInt3 = arrayOfInt2;
/* 156 */     Curve25519Field.square(arrayOfInt2, arrayOfInt3);
/* 157 */     Curve25519Field.multiply(arrayOfInt3, arrayOfInt1, arrayOfInt3);
/* 158 */     int[] arrayOfInt4 = Nat256.create();
/* 159 */     Curve25519Field.square(arrayOfInt3, arrayOfInt4);
/* 160 */     Curve25519Field.multiply(arrayOfInt4, arrayOfInt1, arrayOfInt4);
/* 161 */     int[] arrayOfInt5 = Nat256.create();
/* 162 */     Curve25519Field.squareN(arrayOfInt4, 3, arrayOfInt5);
/* 163 */     Curve25519Field.multiply(arrayOfInt5, arrayOfInt3, arrayOfInt5);
/* 164 */     int[] arrayOfInt6 = arrayOfInt3;
/* 165 */     Curve25519Field.squareN(arrayOfInt5, 4, arrayOfInt6);
/* 166 */     Curve25519Field.multiply(arrayOfInt6, arrayOfInt4, arrayOfInt6);
/* 167 */     int[] arrayOfInt7 = arrayOfInt5;
/* 168 */     Curve25519Field.squareN(arrayOfInt6, 4, arrayOfInt7);
/* 169 */     Curve25519Field.multiply(arrayOfInt7, arrayOfInt4, arrayOfInt7);
/* 170 */     int[] arrayOfInt8 = arrayOfInt4;
/* 171 */     Curve25519Field.squareN(arrayOfInt7, 15, arrayOfInt8);
/* 172 */     Curve25519Field.multiply(arrayOfInt8, arrayOfInt7, arrayOfInt8);
/* 173 */     int[] arrayOfInt9 = arrayOfInt7;
/* 174 */     Curve25519Field.squareN(arrayOfInt8, 30, arrayOfInt9);
/* 175 */     Curve25519Field.multiply(arrayOfInt9, arrayOfInt8, arrayOfInt9);
/* 176 */     int[] arrayOfInt10 = arrayOfInt8;
/* 177 */     Curve25519Field.squareN(arrayOfInt9, 60, arrayOfInt10);
/* 178 */     Curve25519Field.multiply(arrayOfInt10, arrayOfInt9, arrayOfInt10);
/* 179 */     int[] arrayOfInt11 = arrayOfInt9;
/* 180 */     Curve25519Field.squareN(arrayOfInt10, 11, arrayOfInt11);
/* 181 */     Curve25519Field.multiply(arrayOfInt11, arrayOfInt6, arrayOfInt11);
/* 182 */     int[] arrayOfInt12 = arrayOfInt6;
/* 183 */     Curve25519Field.squareN(arrayOfInt11, 120, arrayOfInt12);
/* 184 */     Curve25519Field.multiply(arrayOfInt12, arrayOfInt10, arrayOfInt12);
/*     */     
/* 186 */     int[] arrayOfInt13 = arrayOfInt12;
/* 187 */     Curve25519Field.square(arrayOfInt13, arrayOfInt13);
/*     */     
/* 189 */     int[] arrayOfInt14 = arrayOfInt10;
/* 190 */     Curve25519Field.square(arrayOfInt13, arrayOfInt14);
/*     */     
/* 192 */     if (Nat256.eq(arrayOfInt1, arrayOfInt14))
/*     */     {
/* 194 */       return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt13);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     Curve25519Field.multiply(arrayOfInt13, PRECOMP_POW2, arrayOfInt13);
/*     */     
/* 203 */     Curve25519Field.square(arrayOfInt13, arrayOfInt14);
/*     */     
/* 205 */     if (Nat256.eq(arrayOfInt1, arrayOfInt14))
/*     */     {
/* 207 */       return (ECFieldElement)new org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement(arrayOfInt13);
/*     */     }
/*     */     
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 215 */     if (paramObject == this)
/*     */     {
/* 217 */       return true;
/*     */     }
/*     */     
/* 220 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement))
/*     */     {
/* 222 */       return false;
/*     */     }
/*     */     
/* 225 */     org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement curve25519FieldElement = (org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement)paramObject;
/* 226 */     return Nat256.eq(this.x, curve25519FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 231 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/djb/Curve25519FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
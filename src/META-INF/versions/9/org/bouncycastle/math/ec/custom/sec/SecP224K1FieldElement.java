/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP224K1Field;
/*     */ import org.bouncycastle.math.raw.Nat224;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP224K1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFE56D"));
/*     */ 
/*     */   
/*  16 */   private static final int[] PRECOMP_POW2 = new int[] { 868209154, -587542221, 579297866, -1014948952, -1470801668, 514782679, -1897982644 };
/*     */ 
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP224K1FieldElement(BigInteger paramBigInteger) {
/*  23 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  25 */       throw new IllegalArgumentException("x value invalid for SecP224K1FieldElement");
/*     */     }
/*     */     
/*  28 */     this.x = SecP224K1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP224K1FieldElement() {
/*  33 */     this.x = Nat224.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP224K1FieldElement(int[] paramArrayOfint) {
/*  38 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  43 */     return Nat224.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  48 */     return Nat224.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  53 */     return (Nat224.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  58 */     return Nat224.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  63 */     return "SecP224K1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  68 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  73 */     int[] arrayOfInt = Nat224.create();
/*  74 */     SecP224K1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  75 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  80 */     int[] arrayOfInt = Nat224.create();
/*  81 */     SecP224K1Field.addOne(this.x, arrayOfInt);
/*  82 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  87 */     int[] arrayOfInt = Nat224.create();
/*  88 */     SecP224K1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  89 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  94 */     int[] arrayOfInt = Nat224.create();
/*  95 */     SecP224K1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  96 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/* 102 */     int[] arrayOfInt = Nat224.create();
/* 103 */     SecP224K1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 104 */     SecP224K1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 105 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 110 */     int[] arrayOfInt = Nat224.create();
/* 111 */     SecP224K1Field.negate(this.x, arrayOfInt);
/* 112 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 117 */     int[] arrayOfInt = Nat224.create();
/* 118 */     SecP224K1Field.square(this.x, arrayOfInt);
/* 119 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 125 */     int[] arrayOfInt = Nat224.create();
/* 126 */     SecP224K1Field.inv(this.x, arrayOfInt);
/* 127 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt);
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
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 149 */     int[] arrayOfInt1 = this.x;
/* 150 */     if (Nat224.isZero(arrayOfInt1) || Nat224.isOne(arrayOfInt1))
/*     */     {
/* 152 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 155 */     int[] arrayOfInt2 = Nat224.create();
/* 156 */     SecP224K1Field.square(arrayOfInt1, arrayOfInt2);
/* 157 */     SecP224K1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 158 */     int[] arrayOfInt3 = arrayOfInt2;
/* 159 */     SecP224K1Field.square(arrayOfInt2, arrayOfInt3);
/* 160 */     SecP224K1Field.multiply(arrayOfInt3, arrayOfInt1, arrayOfInt3);
/* 161 */     int[] arrayOfInt4 = Nat224.create();
/* 162 */     SecP224K1Field.square(arrayOfInt3, arrayOfInt4);
/* 163 */     SecP224K1Field.multiply(arrayOfInt4, arrayOfInt1, arrayOfInt4);
/* 164 */     int[] arrayOfInt5 = Nat224.create();
/* 165 */     SecP224K1Field.squareN(arrayOfInt4, 4, arrayOfInt5);
/* 166 */     SecP224K1Field.multiply(arrayOfInt5, arrayOfInt4, arrayOfInt5);
/* 167 */     int[] arrayOfInt6 = Nat224.create();
/* 168 */     SecP224K1Field.squareN(arrayOfInt5, 3, arrayOfInt6);
/* 169 */     SecP224K1Field.multiply(arrayOfInt6, arrayOfInt3, arrayOfInt6);
/* 170 */     int[] arrayOfInt7 = arrayOfInt6;
/* 171 */     SecP224K1Field.squareN(arrayOfInt6, 8, arrayOfInt7);
/* 172 */     SecP224K1Field.multiply(arrayOfInt7, arrayOfInt5, arrayOfInt7);
/* 173 */     int[] arrayOfInt8 = arrayOfInt5;
/* 174 */     SecP224K1Field.squareN(arrayOfInt7, 4, arrayOfInt8);
/* 175 */     SecP224K1Field.multiply(arrayOfInt8, arrayOfInt4, arrayOfInt8);
/* 176 */     int[] arrayOfInt9 = arrayOfInt4;
/* 177 */     SecP224K1Field.squareN(arrayOfInt8, 19, arrayOfInt9);
/* 178 */     SecP224K1Field.multiply(arrayOfInt9, arrayOfInt7, arrayOfInt9);
/* 179 */     int[] arrayOfInt10 = Nat224.create();
/* 180 */     SecP224K1Field.squareN(arrayOfInt9, 42, arrayOfInt10);
/* 181 */     SecP224K1Field.multiply(arrayOfInt10, arrayOfInt9, arrayOfInt10);
/* 182 */     int[] arrayOfInt11 = arrayOfInt9;
/* 183 */     SecP224K1Field.squareN(arrayOfInt10, 23, arrayOfInt11);
/* 184 */     SecP224K1Field.multiply(arrayOfInt11, arrayOfInt8, arrayOfInt11);
/* 185 */     int[] arrayOfInt12 = arrayOfInt8;
/* 186 */     SecP224K1Field.squareN(arrayOfInt11, 84, arrayOfInt12);
/* 187 */     SecP224K1Field.multiply(arrayOfInt12, arrayOfInt10, arrayOfInt12);
/*     */     
/* 189 */     int[] arrayOfInt13 = arrayOfInt12;
/* 190 */     SecP224K1Field.squareN(arrayOfInt13, 20, arrayOfInt13);
/* 191 */     SecP224K1Field.multiply(arrayOfInt13, arrayOfInt7, arrayOfInt13);
/* 192 */     SecP224K1Field.squareN(arrayOfInt13, 3, arrayOfInt13);
/* 193 */     SecP224K1Field.multiply(arrayOfInt13, arrayOfInt1, arrayOfInt13);
/* 194 */     SecP224K1Field.squareN(arrayOfInt13, 2, arrayOfInt13);
/* 195 */     SecP224K1Field.multiply(arrayOfInt13, arrayOfInt1, arrayOfInt13);
/* 196 */     SecP224K1Field.squareN(arrayOfInt13, 4, arrayOfInt13);
/* 197 */     SecP224K1Field.multiply(arrayOfInt13, arrayOfInt3, arrayOfInt13);
/* 198 */     SecP224K1Field.square(arrayOfInt13, arrayOfInt13);
/*     */     
/* 200 */     int[] arrayOfInt14 = arrayOfInt10;
/* 201 */     SecP224K1Field.square(arrayOfInt13, arrayOfInt14);
/*     */     
/* 203 */     if (Nat224.eq(arrayOfInt1, arrayOfInt14))
/*     */     {
/* 205 */       return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt13);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     SecP224K1Field.multiply(arrayOfInt13, PRECOMP_POW2, arrayOfInt13);
/*     */     
/* 214 */     SecP224K1Field.square(arrayOfInt13, arrayOfInt14);
/*     */     
/* 216 */     if (Nat224.eq(arrayOfInt1, arrayOfInt14))
/*     */     {
/* 218 */       return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement(arrayOfInt13);
/*     */     }
/*     */     
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 226 */     if (paramObject == this)
/*     */     {
/* 228 */       return true;
/*     */     }
/*     */     
/* 231 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement))
/*     */     {
/* 233 */       return false;
/*     */     }
/*     */     
/* 236 */     org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement secP224K1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement)paramObject;
/* 237 */     return Nat224.eq(this.x, secP224K1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 242 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 7);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP224K1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
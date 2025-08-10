/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256K1Field;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP256K1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP256K1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP256K1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP256K1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP256K1FieldElement() {
/*  29 */     this.x = Nat256.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP256K1FieldElement(int[] paramArrayOfint) {
/*  34 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  39 */     return Nat256.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  44 */     return Nat256.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  49 */     return (Nat256.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  54 */     return Nat256.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  59 */     return "SecP256K1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  64 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  69 */     int[] arrayOfInt = Nat256.create();
/*  70 */     SecP256K1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat256.create();
/*  77 */     SecP256K1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat256.create();
/*  84 */     SecP256K1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat256.create();
/*  91 */     SecP256K1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat256.create();
/*  99 */     SecP256K1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP256K1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat256.create();
/* 107 */     SecP256K1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat256.create();
/* 114 */     SecP256K1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat256.create();
/* 122 */     SecP256K1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt);
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
/*     */   public ECFieldElement sqrt() {
/* 143 */     int[] arrayOfInt1 = this.x;
/* 144 */     if (Nat256.isZero(arrayOfInt1) || Nat256.isOne(arrayOfInt1))
/*     */     {
/* 146 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 149 */     int[] arrayOfInt2 = Nat256.create();
/* 150 */     SecP256K1Field.square(arrayOfInt1, arrayOfInt2);
/* 151 */     SecP256K1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 152 */     int[] arrayOfInt3 = Nat256.create();
/* 153 */     SecP256K1Field.square(arrayOfInt2, arrayOfInt3);
/* 154 */     SecP256K1Field.multiply(arrayOfInt3, arrayOfInt1, arrayOfInt3);
/* 155 */     int[] arrayOfInt4 = Nat256.create();
/* 156 */     SecP256K1Field.squareN(arrayOfInt3, 3, arrayOfInt4);
/* 157 */     SecP256K1Field.multiply(arrayOfInt4, arrayOfInt3, arrayOfInt4);
/* 158 */     int[] arrayOfInt5 = arrayOfInt4;
/* 159 */     SecP256K1Field.squareN(arrayOfInt4, 3, arrayOfInt5);
/* 160 */     SecP256K1Field.multiply(arrayOfInt5, arrayOfInt3, arrayOfInt5);
/* 161 */     int[] arrayOfInt6 = arrayOfInt5;
/* 162 */     SecP256K1Field.squareN(arrayOfInt5, 2, arrayOfInt6);
/* 163 */     SecP256K1Field.multiply(arrayOfInt6, arrayOfInt2, arrayOfInt6);
/* 164 */     int[] arrayOfInt7 = Nat256.create();
/* 165 */     SecP256K1Field.squareN(arrayOfInt6, 11, arrayOfInt7);
/* 166 */     SecP256K1Field.multiply(arrayOfInt7, arrayOfInt6, arrayOfInt7);
/* 167 */     int[] arrayOfInt8 = arrayOfInt6;
/* 168 */     SecP256K1Field.squareN(arrayOfInt7, 22, arrayOfInt8);
/* 169 */     SecP256K1Field.multiply(arrayOfInt8, arrayOfInt7, arrayOfInt8);
/* 170 */     int[] arrayOfInt9 = Nat256.create();
/* 171 */     SecP256K1Field.squareN(arrayOfInt8, 44, arrayOfInt9);
/* 172 */     SecP256K1Field.multiply(arrayOfInt9, arrayOfInt8, arrayOfInt9);
/* 173 */     int[] arrayOfInt10 = Nat256.create();
/* 174 */     SecP256K1Field.squareN(arrayOfInt9, 88, arrayOfInt10);
/* 175 */     SecP256K1Field.multiply(arrayOfInt10, arrayOfInt9, arrayOfInt10);
/* 176 */     int[] arrayOfInt11 = arrayOfInt9;
/* 177 */     SecP256K1Field.squareN(arrayOfInt10, 44, arrayOfInt11);
/* 178 */     SecP256K1Field.multiply(arrayOfInt11, arrayOfInt8, arrayOfInt11);
/* 179 */     int[] arrayOfInt12 = arrayOfInt8;
/* 180 */     SecP256K1Field.squareN(arrayOfInt11, 3, arrayOfInt12);
/* 181 */     SecP256K1Field.multiply(arrayOfInt12, arrayOfInt3, arrayOfInt12);
/*     */     
/* 183 */     int[] arrayOfInt13 = arrayOfInt12;
/* 184 */     SecP256K1Field.squareN(arrayOfInt13, 23, arrayOfInt13);
/* 185 */     SecP256K1Field.multiply(arrayOfInt13, arrayOfInt7, arrayOfInt13);
/* 186 */     SecP256K1Field.squareN(arrayOfInt13, 6, arrayOfInt13);
/* 187 */     SecP256K1Field.multiply(arrayOfInt13, arrayOfInt2, arrayOfInt13);
/* 188 */     SecP256K1Field.squareN(arrayOfInt13, 2, arrayOfInt13);
/*     */     
/* 190 */     int[] arrayOfInt14 = arrayOfInt2;
/* 191 */     SecP256K1Field.square(arrayOfInt13, arrayOfInt14);
/*     */     
/* 193 */     return Nat256.eq(arrayOfInt1, arrayOfInt14) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement(arrayOfInt13) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 198 */     if (paramObject == this)
/*     */     {
/* 200 */       return true;
/*     */     }
/*     */     
/* 203 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement))
/*     */     {
/* 205 */       return false;
/*     */     }
/*     */     
/* 208 */     org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement secP256K1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement)paramObject;
/* 209 */     return Nat256.eq(this.x, secP256K1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 214 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP256K1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
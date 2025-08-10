/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.gm;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.gm.SM2P256V1Field;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SM2P256V1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SM2P256V1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SM2P256V1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SM2P256V1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SM2P256V1FieldElement() {
/*  29 */     this.x = Nat256.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SM2P256V1FieldElement(int[] paramArrayOfint) {
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
/*  59 */     return "SM2P256V1Field";
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
/*  70 */     SM2P256V1Field.add(this.x, ((org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat256.create();
/*  77 */     SM2P256V1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat256.create();
/*  84 */     SM2P256V1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat256.create();
/*  91 */     SM2P256V1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat256.create();
/*  99 */     SM2P256V1Field.inv(((org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SM2P256V1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat256.create();
/* 107 */     SM2P256V1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat256.create();
/* 114 */     SM2P256V1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat256.create();
/* 122 */     SM2P256V1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt);
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
/*     */   public ECFieldElement sqrt() {
/* 141 */     int[] arrayOfInt1 = this.x;
/* 142 */     if (Nat256.isZero(arrayOfInt1) || Nat256.isOne(arrayOfInt1))
/*     */     {
/* 144 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 147 */     int[] arrayOfInt2 = Nat256.create();
/* 148 */     SM2P256V1Field.square(arrayOfInt1, arrayOfInt2);
/* 149 */     SM2P256V1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 150 */     int[] arrayOfInt3 = Nat256.create();
/* 151 */     SM2P256V1Field.squareN(arrayOfInt2, 2, arrayOfInt3);
/* 152 */     SM2P256V1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/* 153 */     int[] arrayOfInt4 = Nat256.create();
/* 154 */     SM2P256V1Field.squareN(arrayOfInt3, 2, arrayOfInt4);
/* 155 */     SM2P256V1Field.multiply(arrayOfInt4, arrayOfInt2, arrayOfInt4);
/* 156 */     int[] arrayOfInt5 = arrayOfInt2;
/* 157 */     SM2P256V1Field.squareN(arrayOfInt4, 6, arrayOfInt5);
/* 158 */     SM2P256V1Field.multiply(arrayOfInt5, arrayOfInt4, arrayOfInt5);
/* 159 */     int[] arrayOfInt6 = Nat256.create();
/* 160 */     SM2P256V1Field.squareN(arrayOfInt5, 12, arrayOfInt6);
/* 161 */     SM2P256V1Field.multiply(arrayOfInt6, arrayOfInt5, arrayOfInt6);
/* 162 */     int[] arrayOfInt7 = arrayOfInt5;
/* 163 */     SM2P256V1Field.squareN(arrayOfInt6, 6, arrayOfInt7);
/* 164 */     SM2P256V1Field.multiply(arrayOfInt7, arrayOfInt4, arrayOfInt7);
/* 165 */     int[] arrayOfInt8 = arrayOfInt4;
/* 166 */     SM2P256V1Field.square(arrayOfInt7, arrayOfInt8);
/* 167 */     SM2P256V1Field.multiply(arrayOfInt8, arrayOfInt1, arrayOfInt8);
/*     */     
/* 169 */     int[] arrayOfInt9 = arrayOfInt6;
/* 170 */     SM2P256V1Field.squareN(arrayOfInt8, 31, arrayOfInt9);
/*     */     
/* 172 */     int[] arrayOfInt10 = arrayOfInt7;
/* 173 */     SM2P256V1Field.multiply(arrayOfInt9, arrayOfInt8, arrayOfInt10);
/*     */     
/* 175 */     SM2P256V1Field.squareN(arrayOfInt9, 32, arrayOfInt9);
/* 176 */     SM2P256V1Field.multiply(arrayOfInt9, arrayOfInt10, arrayOfInt9);
/* 177 */     SM2P256V1Field.squareN(arrayOfInt9, 62, arrayOfInt9);
/* 178 */     SM2P256V1Field.multiply(arrayOfInt9, arrayOfInt10, arrayOfInt9);
/* 179 */     SM2P256V1Field.squareN(arrayOfInt9, 4, arrayOfInt9);
/* 180 */     SM2P256V1Field.multiply(arrayOfInt9, arrayOfInt3, arrayOfInt9);
/* 181 */     SM2P256V1Field.squareN(arrayOfInt9, 32, arrayOfInt9);
/* 182 */     SM2P256V1Field.multiply(arrayOfInt9, arrayOfInt1, arrayOfInt9);
/* 183 */     SM2P256V1Field.squareN(arrayOfInt9, 62, arrayOfInt9);
/*     */     
/* 185 */     int[] arrayOfInt11 = arrayOfInt3;
/* 186 */     SM2P256V1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 188 */     return Nat256.eq(arrayOfInt1, arrayOfInt11) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement(arrayOfInt9) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 193 */     if (paramObject == this)
/*     */     {
/* 195 */       return true;
/*     */     }
/*     */     
/* 198 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement))
/*     */     {
/* 200 */       return false;
/*     */     }
/*     */     
/* 203 */     org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement sM2P256V1FieldElement = (org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement)paramObject;
/* 204 */     return Nat256.eq(this.x, sM2P256V1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 209 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/gm/SM2P256V1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
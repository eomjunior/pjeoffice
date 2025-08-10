/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R1Field;
/*     */ import org.bouncycastle.math.raw.Nat160;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP160R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF7FFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP160R1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP160R1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP160R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP160R1FieldElement() {
/*  29 */     this.x = Nat160.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP160R1FieldElement(int[] paramArrayOfint) {
/*  34 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  39 */     return Nat160.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  44 */     return Nat160.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  49 */     return (Nat160.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  54 */     return Nat160.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  59 */     return "SecP160R1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  64 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  69 */     int[] arrayOfInt = Nat160.create();
/*  70 */     SecP160R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat160.create();
/*  77 */     SecP160R1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat160.create();
/*  84 */     SecP160R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat160.create();
/*  91 */     SecP160R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat160.create();
/*  99 */     SecP160R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP160R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat160.create();
/* 107 */     SecP160R1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat160.create();
/* 114 */     SecP160R1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat160.create();
/* 122 */     SecP160R1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt);
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
/* 144 */     if (Nat160.isZero(arrayOfInt1) || Nat160.isOne(arrayOfInt1))
/*     */     {
/* 146 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 149 */     int[] arrayOfInt2 = Nat160.create();
/* 150 */     SecP160R1Field.square(arrayOfInt1, arrayOfInt2);
/* 151 */     SecP160R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 152 */     int[] arrayOfInt3 = Nat160.create();
/* 153 */     SecP160R1Field.squareN(arrayOfInt2, 2, arrayOfInt3);
/* 154 */     SecP160R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/* 155 */     int[] arrayOfInt4 = arrayOfInt2;
/* 156 */     SecP160R1Field.squareN(arrayOfInt3, 4, arrayOfInt4);
/* 157 */     SecP160R1Field.multiply(arrayOfInt4, arrayOfInt3, arrayOfInt4);
/* 158 */     int[] arrayOfInt5 = arrayOfInt3;
/* 159 */     SecP160R1Field.squareN(arrayOfInt4, 8, arrayOfInt5);
/* 160 */     SecP160R1Field.multiply(arrayOfInt5, arrayOfInt4, arrayOfInt5);
/* 161 */     int[] arrayOfInt6 = arrayOfInt4;
/* 162 */     SecP160R1Field.squareN(arrayOfInt5, 16, arrayOfInt6);
/* 163 */     SecP160R1Field.multiply(arrayOfInt6, arrayOfInt5, arrayOfInt6);
/* 164 */     int[] arrayOfInt7 = arrayOfInt5;
/* 165 */     SecP160R1Field.squareN(arrayOfInt6, 32, arrayOfInt7);
/* 166 */     SecP160R1Field.multiply(arrayOfInt7, arrayOfInt6, arrayOfInt7);
/* 167 */     int[] arrayOfInt8 = arrayOfInt6;
/* 168 */     SecP160R1Field.squareN(arrayOfInt7, 64, arrayOfInt8);
/* 169 */     SecP160R1Field.multiply(arrayOfInt8, arrayOfInt7, arrayOfInt8);
/* 170 */     int[] arrayOfInt9 = arrayOfInt7;
/* 171 */     SecP160R1Field.square(arrayOfInt8, arrayOfInt9);
/* 172 */     SecP160R1Field.multiply(arrayOfInt9, arrayOfInt1, arrayOfInt9);
/*     */     
/* 174 */     int[] arrayOfInt10 = arrayOfInt9;
/* 175 */     SecP160R1Field.squareN(arrayOfInt10, 29, arrayOfInt10);
/*     */     
/* 177 */     int[] arrayOfInt11 = arrayOfInt8;
/* 178 */     SecP160R1Field.square(arrayOfInt10, arrayOfInt11);
/*     */     
/* 180 */     return Nat160.eq(arrayOfInt1, arrayOfInt11) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement(arrayOfInt10) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 185 */     if (paramObject == this)
/*     */     {
/* 187 */       return true;
/*     */     }
/*     */     
/* 190 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement))
/*     */     {
/* 192 */       return false;
/*     */     }
/*     */     
/* 195 */     org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement secP160R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement)paramObject;
/* 196 */     return Nat160.eq(this.x, secP160R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 201 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 5);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP160R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
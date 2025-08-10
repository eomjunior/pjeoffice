/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R2Field;
/*     */ import org.bouncycastle.math.raw.Nat160;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP160R2FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC73"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP160R2FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP160R2FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP160R2Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP160R2FieldElement() {
/*  29 */     this.x = Nat160.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP160R2FieldElement(int[] paramArrayOfint) {
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
/*  59 */     return "SecP160R2Field";
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
/*  70 */     SecP160R2Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat160.create();
/*  77 */     SecP160R2Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat160.create();
/*  84 */     SecP160R2Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat160.create();
/*  91 */     SecP160R2Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat160.create();
/*  99 */     SecP160R2Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP160R2Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat160.create();
/* 107 */     SecP160R2Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat160.create();
/* 114 */     SecP160R2Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat160.create();
/* 122 */     SecP160R2Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt);
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
/* 150 */     SecP160R2Field.square(arrayOfInt1, arrayOfInt2);
/* 151 */     SecP160R2Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 152 */     int[] arrayOfInt3 = Nat160.create();
/* 153 */     SecP160R2Field.square(arrayOfInt2, arrayOfInt3);
/* 154 */     SecP160R2Field.multiply(arrayOfInt3, arrayOfInt1, arrayOfInt3);
/* 155 */     int[] arrayOfInt4 = Nat160.create();
/* 156 */     SecP160R2Field.square(arrayOfInt3, arrayOfInt4);
/* 157 */     SecP160R2Field.multiply(arrayOfInt4, arrayOfInt1, arrayOfInt4);
/* 158 */     int[] arrayOfInt5 = Nat160.create();
/* 159 */     SecP160R2Field.squareN(arrayOfInt4, 3, arrayOfInt5);
/* 160 */     SecP160R2Field.multiply(arrayOfInt5, arrayOfInt3, arrayOfInt5);
/* 161 */     int[] arrayOfInt6 = arrayOfInt4;
/* 162 */     SecP160R2Field.squareN(arrayOfInt5, 7, arrayOfInt6);
/* 163 */     SecP160R2Field.multiply(arrayOfInt6, arrayOfInt5, arrayOfInt6);
/* 164 */     int[] arrayOfInt7 = arrayOfInt5;
/* 165 */     SecP160R2Field.squareN(arrayOfInt6, 3, arrayOfInt7);
/* 166 */     SecP160R2Field.multiply(arrayOfInt7, arrayOfInt3, arrayOfInt7);
/* 167 */     int[] arrayOfInt8 = Nat160.create();
/* 168 */     SecP160R2Field.squareN(arrayOfInt7, 14, arrayOfInt8);
/* 169 */     SecP160R2Field.multiply(arrayOfInt8, arrayOfInt6, arrayOfInt8);
/* 170 */     int[] arrayOfInt9 = arrayOfInt6;
/* 171 */     SecP160R2Field.squareN(arrayOfInt8, 31, arrayOfInt9);
/* 172 */     SecP160R2Field.multiply(arrayOfInt9, arrayOfInt8, arrayOfInt9);
/* 173 */     int[] arrayOfInt10 = arrayOfInt8;
/* 174 */     SecP160R2Field.squareN(arrayOfInt9, 62, arrayOfInt10);
/* 175 */     SecP160R2Field.multiply(arrayOfInt10, arrayOfInt9, arrayOfInt10);
/* 176 */     int[] arrayOfInt11 = arrayOfInt9;
/* 177 */     SecP160R2Field.squareN(arrayOfInt10, 3, arrayOfInt11);
/* 178 */     SecP160R2Field.multiply(arrayOfInt11, arrayOfInt3, arrayOfInt11);
/*     */     
/* 180 */     int[] arrayOfInt12 = arrayOfInt11;
/* 181 */     SecP160R2Field.squareN(arrayOfInt12, 18, arrayOfInt12);
/* 182 */     SecP160R2Field.multiply(arrayOfInt12, arrayOfInt7, arrayOfInt12);
/* 183 */     SecP160R2Field.squareN(arrayOfInt12, 2, arrayOfInt12);
/* 184 */     SecP160R2Field.multiply(arrayOfInt12, arrayOfInt1, arrayOfInt12);
/* 185 */     SecP160R2Field.squareN(arrayOfInt12, 3, arrayOfInt12);
/* 186 */     SecP160R2Field.multiply(arrayOfInt12, arrayOfInt2, arrayOfInt12);
/* 187 */     SecP160R2Field.squareN(arrayOfInt12, 6, arrayOfInt12);
/* 188 */     SecP160R2Field.multiply(arrayOfInt12, arrayOfInt3, arrayOfInt12);
/* 189 */     SecP160R2Field.squareN(arrayOfInt12, 2, arrayOfInt12);
/* 190 */     SecP160R2Field.multiply(arrayOfInt12, arrayOfInt1, arrayOfInt12);
/*     */     
/* 192 */     int[] arrayOfInt13 = arrayOfInt2;
/* 193 */     SecP160R2Field.square(arrayOfInt12, arrayOfInt13);
/*     */     
/* 195 */     return Nat160.eq(arrayOfInt1, arrayOfInt13) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement(arrayOfInt12) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 200 */     if (paramObject == this)
/*     */     {
/* 202 */       return true;
/*     */     }
/*     */     
/* 205 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement))
/*     */     {
/* 207 */       return false;
/*     */     }
/*     */     
/* 210 */     org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement secP160R2FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement)paramObject;
/* 211 */     return Nat160.eq(this.x, secP160R2FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 216 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 5);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP160R2FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
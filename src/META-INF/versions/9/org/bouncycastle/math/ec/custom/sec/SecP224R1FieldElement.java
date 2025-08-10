/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP224R1Field;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat224;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP224R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  14 */   public static final BigInteger Q = new BigInteger(1, 
/*  15 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000000000000000000000001"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP224R1FieldElement(BigInteger paramBigInteger) {
/*  21 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  23 */       throw new IllegalArgumentException("x value invalid for SecP224R1FieldElement");
/*     */     }
/*     */     
/*  26 */     this.x = SecP224R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP224R1FieldElement() {
/*  31 */     this.x = Nat224.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP224R1FieldElement(int[] paramArrayOfint) {
/*  36 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  41 */     return Nat224.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  46 */     return Nat224.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  51 */     return (Nat224.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  56 */     return Nat224.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  61 */     return "SecP224R1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  66 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  71 */     int[] arrayOfInt = Nat224.create();
/*  72 */     SecP224R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  73 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  78 */     int[] arrayOfInt = Nat224.create();
/*  79 */     SecP224R1Field.addOne(this.x, arrayOfInt);
/*  80 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  85 */     int[] arrayOfInt = Nat224.create();
/*  86 */     SecP224R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  87 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  92 */     int[] arrayOfInt = Nat224.create();
/*  93 */     SecP224R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  94 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/* 100 */     int[] arrayOfInt = Nat224.create();
/* 101 */     SecP224R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 102 */     SecP224R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 103 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 108 */     int[] arrayOfInt = Nat224.create();
/* 109 */     SecP224R1Field.negate(this.x, arrayOfInt);
/* 110 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 115 */     int[] arrayOfInt = Nat224.create();
/* 116 */     SecP224R1Field.square(this.x, arrayOfInt);
/* 117 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 123 */     int[] arrayOfInt = Nat224.create();
/* 124 */     SecP224R1Field.inv(this.x, arrayOfInt);
/* 125 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 134 */     int[] arrayOfInt1 = this.x;
/* 135 */     if (Nat224.isZero(arrayOfInt1) || Nat224.isOne(arrayOfInt1))
/*     */     {
/* 137 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 140 */     int[] arrayOfInt2 = Nat224.create();
/* 141 */     SecP224R1Field.negate(arrayOfInt1, arrayOfInt2);
/*     */     
/* 143 */     int[] arrayOfInt3 = Mod.random(SecP224R1Field.P);
/* 144 */     int[] arrayOfInt4 = Nat224.create();
/*     */     
/* 146 */     if (!isSquare(arrayOfInt1))
/*     */     {
/* 148 */       return null;
/*     */     }
/*     */     
/* 151 */     while (!trySqrt(arrayOfInt2, arrayOfInt3, arrayOfInt4))
/*     */     {
/* 153 */       SecP224R1Field.addOne(arrayOfInt3, arrayOfInt3);
/*     */     }
/*     */     
/* 156 */     SecP224R1Field.square(arrayOfInt4, arrayOfInt3);
/*     */     
/* 158 */     return Nat224.eq(arrayOfInt1, arrayOfInt3) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement(arrayOfInt4) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 163 */     if (paramObject == this)
/*     */     {
/* 165 */       return true;
/*     */     }
/*     */     
/* 168 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement))
/*     */     {
/* 170 */       return false;
/*     */     }
/*     */     
/* 173 */     org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement secP224R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement)paramObject;
/* 174 */     return Nat224.eq(this.x, secP224R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 7);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isSquare(int[] paramArrayOfint) {
/* 184 */     int[] arrayOfInt1 = Nat224.create();
/* 185 */     int[] arrayOfInt2 = Nat224.create();
/* 186 */     Nat224.copy(paramArrayOfint, arrayOfInt1);
/*     */     
/* 188 */     for (byte b = 0; b < 7; b++) {
/*     */       
/* 190 */       Nat224.copy(arrayOfInt1, arrayOfInt2);
/* 191 */       SecP224R1Field.squareN(arrayOfInt1, 1 << b, arrayOfInt1);
/* 192 */       SecP224R1Field.multiply(arrayOfInt1, arrayOfInt2, arrayOfInt1);
/*     */     } 
/*     */     
/* 195 */     SecP224R1Field.squareN(arrayOfInt1, 95, arrayOfInt1);
/* 196 */     return Nat224.isOne(arrayOfInt1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void RM(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int[] paramArrayOfint4, int[] paramArrayOfint5, int[] paramArrayOfint6, int[] paramArrayOfint7) {
/* 201 */     SecP224R1Field.multiply(paramArrayOfint5, paramArrayOfint3, paramArrayOfint7);
/* 202 */     SecP224R1Field.multiply(paramArrayOfint7, paramArrayOfint1, paramArrayOfint7);
/* 203 */     SecP224R1Field.multiply(paramArrayOfint4, paramArrayOfint2, paramArrayOfint6);
/* 204 */     SecP224R1Field.add(paramArrayOfint6, paramArrayOfint7, paramArrayOfint6);
/* 205 */     SecP224R1Field.multiply(paramArrayOfint4, paramArrayOfint3, paramArrayOfint7);
/* 206 */     Nat224.copy(paramArrayOfint6, paramArrayOfint4);
/* 207 */     SecP224R1Field.multiply(paramArrayOfint5, paramArrayOfint2, paramArrayOfint5);
/* 208 */     SecP224R1Field.add(paramArrayOfint5, paramArrayOfint7, paramArrayOfint5);
/* 209 */     SecP224R1Field.square(paramArrayOfint5, paramArrayOfint6);
/* 210 */     SecP224R1Field.multiply(paramArrayOfint6, paramArrayOfint1, paramArrayOfint6);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void RP(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int[] paramArrayOfint4, int[] paramArrayOfint5) {
/* 215 */     Nat224.copy(paramArrayOfint1, paramArrayOfint4);
/*     */     
/* 217 */     int[] arrayOfInt1 = Nat224.create();
/* 218 */     int[] arrayOfInt2 = Nat224.create();
/*     */     
/* 220 */     for (byte b = 0; b < 7; b++) {
/*     */       
/* 222 */       Nat224.copy(paramArrayOfint2, arrayOfInt1);
/* 223 */       Nat224.copy(paramArrayOfint3, arrayOfInt2);
/*     */       
/* 225 */       int i = 1 << b;
/* 226 */       while (--i >= 0)
/*     */       {
/* 228 */         RS(paramArrayOfint2, paramArrayOfint3, paramArrayOfint4, paramArrayOfint5);
/*     */       }
/*     */       
/* 231 */       RM(paramArrayOfint1, arrayOfInt1, arrayOfInt2, paramArrayOfint2, paramArrayOfint3, paramArrayOfint4, paramArrayOfint5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void RS(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int[] paramArrayOfint4) {
/* 237 */     SecP224R1Field.multiply(paramArrayOfint2, paramArrayOfint1, paramArrayOfint2);
/* 238 */     SecP224R1Field.twice(paramArrayOfint2, paramArrayOfint2);
/* 239 */     SecP224R1Field.square(paramArrayOfint1, paramArrayOfint4);
/* 240 */     SecP224R1Field.add(paramArrayOfint3, paramArrayOfint4, paramArrayOfint1);
/* 241 */     SecP224R1Field.multiply(paramArrayOfint3, paramArrayOfint4, paramArrayOfint3);
/* 242 */     int i = Nat.shiftUpBits(7, paramArrayOfint3, 2, 0);
/* 243 */     SecP224R1Field.reduce32(i, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean trySqrt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 248 */     int[] arrayOfInt1 = Nat224.create();
/* 249 */     Nat224.copy(paramArrayOfint2, arrayOfInt1);
/* 250 */     int[] arrayOfInt2 = Nat224.create();
/* 251 */     arrayOfInt2[0] = 1;
/* 252 */     int[] arrayOfInt3 = Nat224.create();
/* 253 */     RP(paramArrayOfint1, arrayOfInt1, arrayOfInt2, arrayOfInt3, paramArrayOfint3);
/*     */     
/* 255 */     int[] arrayOfInt4 = Nat224.create();
/* 256 */     int[] arrayOfInt5 = Nat224.create();
/*     */     
/* 258 */     for (byte b = 1; b < 96; b++) {
/*     */       
/* 260 */       Nat224.copy(arrayOfInt1, arrayOfInt4);
/* 261 */       Nat224.copy(arrayOfInt2, arrayOfInt5);
/*     */       
/* 263 */       RS(arrayOfInt1, arrayOfInt2, arrayOfInt3, paramArrayOfint3);
/*     */       
/* 265 */       if (Nat224.isZero(arrayOfInt1)) {
/*     */         
/* 267 */         SecP224R1Field.inv(arrayOfInt5, paramArrayOfint3);
/* 268 */         SecP224R1Field.multiply(paramArrayOfint3, arrayOfInt4, paramArrayOfint3);
/* 269 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP224R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256R1Field;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP256R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP256R1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP256R1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP256R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP256R1FieldElement() {
/*  29 */     this.x = Nat256.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP256R1FieldElement(int[] paramArrayOfint) {
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
/*  59 */     return "SecP256R1Field";
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
/*  70 */     SecP256R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat256.create();
/*  77 */     SecP256R1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat256.create();
/*  84 */     SecP256R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat256.create();
/*  91 */     SecP256R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat256.create();
/*  99 */     SecP256R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP256R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat256.create();
/* 107 */     SecP256R1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat256.create();
/* 114 */     SecP256R1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat256.create();
/* 122 */     SecP256R1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement sqrt() {
/* 134 */     int[] arrayOfInt1 = this.x;
/* 135 */     if (Nat256.isZero(arrayOfInt1) || Nat256.isOne(arrayOfInt1))
/*     */     {
/* 137 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 140 */     int[] arrayOfInt2 = Nat256.create();
/* 141 */     int[] arrayOfInt3 = Nat256.create();
/*     */     
/* 143 */     SecP256R1Field.square(arrayOfInt1, arrayOfInt2);
/* 144 */     SecP256R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/*     */     
/* 146 */     SecP256R1Field.squareN(arrayOfInt2, 2, arrayOfInt3);
/* 147 */     SecP256R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/*     */     
/* 149 */     SecP256R1Field.squareN(arrayOfInt3, 4, arrayOfInt2);
/* 150 */     SecP256R1Field.multiply(arrayOfInt2, arrayOfInt3, arrayOfInt2);
/*     */     
/* 152 */     SecP256R1Field.squareN(arrayOfInt2, 8, arrayOfInt3);
/* 153 */     SecP256R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/*     */     
/* 155 */     SecP256R1Field.squareN(arrayOfInt3, 16, arrayOfInt2);
/* 156 */     SecP256R1Field.multiply(arrayOfInt2, arrayOfInt3, arrayOfInt2);
/*     */     
/* 158 */     SecP256R1Field.squareN(arrayOfInt2, 32, arrayOfInt2);
/* 159 */     SecP256R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/*     */     
/* 161 */     SecP256R1Field.squareN(arrayOfInt2, 96, arrayOfInt2);
/* 162 */     SecP256R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/*     */     
/* 164 */     SecP256R1Field.squareN(arrayOfInt2, 94, arrayOfInt2);
/* 165 */     SecP256R1Field.square(arrayOfInt2, arrayOfInt3);
/*     */     
/* 167 */     return Nat256.eq(arrayOfInt1, arrayOfInt3) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement(arrayOfInt2) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 172 */     if (paramObject == this)
/*     */     {
/* 174 */       return true;
/*     */     }
/*     */     
/* 177 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement))
/*     */     {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement secP256R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement)paramObject;
/* 183 */     return Nat256.eq(this.x, secP256R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP256R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
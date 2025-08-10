/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP384R1Field;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP384R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP384R1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP384R1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP384R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP384R1FieldElement() {
/*  29 */     this.x = Nat.create(12);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP384R1FieldElement(int[] paramArrayOfint) {
/*  34 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  39 */     return Nat.isZero(12, this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  44 */     return Nat.isOne(12, this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  49 */     return (Nat.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  54 */     return Nat.toBigInteger(12, this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  59 */     return "SecP384R1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  64 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  69 */     int[] arrayOfInt = Nat.create(12);
/*  70 */     SecP384R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat.create(12);
/*  77 */     SecP384R1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat.create(12);
/*  84 */     SecP384R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat.create(12);
/*  91 */     SecP384R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat.create(12);
/*  99 */     SecP384R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP384R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat.create(12);
/* 107 */     SecP384R1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat.create(12);
/* 114 */     SecP384R1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat.create(12);
/* 122 */     SecP384R1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt);
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
/* 135 */     if (Nat.isZero(12, arrayOfInt1) || Nat.isOne(12, arrayOfInt1))
/*     */     {
/* 137 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 140 */     int[] arrayOfInt2 = Nat.create(12);
/* 141 */     int[] arrayOfInt3 = Nat.create(12);
/* 142 */     int[] arrayOfInt4 = Nat.create(12);
/* 143 */     int[] arrayOfInt5 = Nat.create(12);
/*     */     
/* 145 */     SecP384R1Field.square(arrayOfInt1, arrayOfInt2);
/* 146 */     SecP384R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/*     */     
/* 148 */     SecP384R1Field.squareN(arrayOfInt2, 2, arrayOfInt3);
/* 149 */     SecP384R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/*     */     
/* 151 */     SecP384R1Field.square(arrayOfInt3, arrayOfInt3);
/* 152 */     SecP384R1Field.multiply(arrayOfInt3, arrayOfInt1, arrayOfInt3);
/*     */     
/* 154 */     SecP384R1Field.squareN(arrayOfInt3, 5, arrayOfInt4);
/* 155 */     SecP384R1Field.multiply(arrayOfInt4, arrayOfInt3, arrayOfInt4);
/*     */     
/* 157 */     SecP384R1Field.squareN(arrayOfInt4, 5, arrayOfInt5);
/* 158 */     SecP384R1Field.multiply(arrayOfInt5, arrayOfInt3, arrayOfInt5);
/*     */     
/* 160 */     SecP384R1Field.squareN(arrayOfInt5, 15, arrayOfInt3);
/* 161 */     SecP384R1Field.multiply(arrayOfInt3, arrayOfInt5, arrayOfInt3);
/*     */     
/* 163 */     SecP384R1Field.squareN(arrayOfInt3, 2, arrayOfInt4);
/* 164 */     SecP384R1Field.multiply(arrayOfInt2, arrayOfInt4, arrayOfInt2);
/*     */     
/* 166 */     SecP384R1Field.squareN(arrayOfInt4, 28, arrayOfInt4);
/* 167 */     SecP384R1Field.multiply(arrayOfInt3, arrayOfInt4, arrayOfInt3);
/*     */     
/* 169 */     SecP384R1Field.squareN(arrayOfInt3, 60, arrayOfInt4);
/* 170 */     SecP384R1Field.multiply(arrayOfInt4, arrayOfInt3, arrayOfInt4);
/*     */     
/* 172 */     int[] arrayOfInt6 = arrayOfInt3;
/*     */     
/* 174 */     SecP384R1Field.squareN(arrayOfInt4, 120, arrayOfInt6);
/* 175 */     SecP384R1Field.multiply(arrayOfInt6, arrayOfInt4, arrayOfInt6);
/*     */     
/* 177 */     SecP384R1Field.squareN(arrayOfInt6, 15, arrayOfInt6);
/* 178 */     SecP384R1Field.multiply(arrayOfInt6, arrayOfInt5, arrayOfInt6);
/*     */     
/* 180 */     SecP384R1Field.squareN(arrayOfInt6, 33, arrayOfInt6);
/* 181 */     SecP384R1Field.multiply(arrayOfInt6, arrayOfInt2, arrayOfInt6);
/*     */     
/* 183 */     SecP384R1Field.squareN(arrayOfInt6, 64, arrayOfInt6);
/* 184 */     SecP384R1Field.multiply(arrayOfInt6, arrayOfInt1, arrayOfInt6);
/*     */     
/* 186 */     SecP384R1Field.squareN(arrayOfInt6, 30, arrayOfInt2);
/* 187 */     SecP384R1Field.square(arrayOfInt2, arrayOfInt3);
/*     */     
/* 189 */     return Nat.eq(12, arrayOfInt1, arrayOfInt3) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement(arrayOfInt2) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 194 */     if (paramObject == this)
/*     */     {
/* 196 */       return true;
/*     */     }
/*     */     
/* 199 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement))
/*     */     {
/* 201 */       return false;
/*     */     }
/*     */     
/* 204 */     org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement secP384R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement)paramObject;
/* 205 */     return Nat.eq(12, this.x, secP384R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 210 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 12);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP384R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
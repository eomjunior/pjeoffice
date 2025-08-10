/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192R1Field;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP192R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP192R1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP192R1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP192R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP192R1FieldElement() {
/*  29 */     this.x = Nat192.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP192R1FieldElement(int[] paramArrayOfint) {
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
/*  59 */     return "SecP192R1Field";
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
/*  70 */     SecP192R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat192.create();
/*  77 */     SecP192R1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat192.create();
/*  84 */     SecP192R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat192.create();
/*  91 */     SecP192R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat192.create();
/*  99 */     SecP192R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP192R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat192.create();
/* 107 */     SecP192R1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat192.create();
/* 114 */     SecP192R1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat192.create();
/* 122 */     SecP192R1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt);
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
/*     */   public ECFieldElement sqrt() {
/* 135 */     int[] arrayOfInt1 = this.x;
/* 136 */     if (Nat192.isZero(arrayOfInt1) || Nat192.isOne(arrayOfInt1))
/*     */     {
/* 138 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 141 */     int[] arrayOfInt2 = Nat192.create();
/* 142 */     int[] arrayOfInt3 = Nat192.create();
/*     */     
/* 144 */     SecP192R1Field.square(arrayOfInt1, arrayOfInt2);
/* 145 */     SecP192R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/*     */     
/* 147 */     SecP192R1Field.squareN(arrayOfInt2, 2, arrayOfInt3);
/* 148 */     SecP192R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/*     */     
/* 150 */     SecP192R1Field.squareN(arrayOfInt3, 4, arrayOfInt2);
/* 151 */     SecP192R1Field.multiply(arrayOfInt2, arrayOfInt3, arrayOfInt2);
/*     */     
/* 153 */     SecP192R1Field.squareN(arrayOfInt2, 8, arrayOfInt3);
/* 154 */     SecP192R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/*     */     
/* 156 */     SecP192R1Field.squareN(arrayOfInt3, 16, arrayOfInt2);
/* 157 */     SecP192R1Field.multiply(arrayOfInt2, arrayOfInt3, arrayOfInt2);
/*     */     
/* 159 */     SecP192R1Field.squareN(arrayOfInt2, 32, arrayOfInt3);
/* 160 */     SecP192R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/*     */     
/* 162 */     SecP192R1Field.squareN(arrayOfInt3, 64, arrayOfInt2);
/* 163 */     SecP192R1Field.multiply(arrayOfInt2, arrayOfInt3, arrayOfInt2);
/*     */     
/* 165 */     SecP192R1Field.squareN(arrayOfInt2, 62, arrayOfInt2);
/* 166 */     SecP192R1Field.square(arrayOfInt2, arrayOfInt3);
/*     */     
/* 168 */     return Nat192.eq(arrayOfInt1, arrayOfInt3) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement(arrayOfInt2) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 173 */     if (paramObject == this)
/*     */     {
/* 175 */       return true;
/*     */     }
/*     */     
/* 178 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement))
/*     */     {
/* 180 */       return false;
/*     */     }
/*     */     
/* 183 */     org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement secP192R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement)paramObject;
/* 184 */     return Nat192.eq(this.x, secP192R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 189 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 6);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP192R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
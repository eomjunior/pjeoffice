/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP128R1Field;
/*     */ import org.bouncycastle.math.raw.Nat128;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP128R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP128R1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP128R1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP128R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP128R1FieldElement() {
/*  29 */     this.x = Nat128.create();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP128R1FieldElement(int[] paramArrayOfint) {
/*  34 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  39 */     return Nat128.isZero(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  44 */     return Nat128.isOne(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  49 */     return (Nat128.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  54 */     return Nat128.toBigInteger(this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  59 */     return "SecP128R1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  64 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  69 */     int[] arrayOfInt = Nat128.create();
/*  70 */     SecP128R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat128.create();
/*  77 */     SecP128R1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat128.create();
/*  84 */     SecP128R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat128.create();
/*  91 */     SecP128R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat128.create();
/*  99 */     SecP128R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP128R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat128.create();
/* 107 */     SecP128R1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat128.create();
/* 114 */     SecP128R1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat128.create();
/* 122 */     SecP128R1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt);
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
/* 144 */     if (Nat128.isZero(arrayOfInt1) || Nat128.isOne(arrayOfInt1))
/*     */     {
/* 146 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 149 */     int[] arrayOfInt2 = Nat128.create();
/* 150 */     SecP128R1Field.square(arrayOfInt1, arrayOfInt2);
/* 151 */     SecP128R1Field.multiply(arrayOfInt2, arrayOfInt1, arrayOfInt2);
/* 152 */     int[] arrayOfInt3 = Nat128.create();
/* 153 */     SecP128R1Field.squareN(arrayOfInt2, 2, arrayOfInt3);
/* 154 */     SecP128R1Field.multiply(arrayOfInt3, arrayOfInt2, arrayOfInt3);
/* 155 */     int[] arrayOfInt4 = Nat128.create();
/* 156 */     SecP128R1Field.squareN(arrayOfInt3, 4, arrayOfInt4);
/* 157 */     SecP128R1Field.multiply(arrayOfInt4, arrayOfInt3, arrayOfInt4);
/* 158 */     int[] arrayOfInt5 = arrayOfInt3;
/* 159 */     SecP128R1Field.squareN(arrayOfInt4, 2, arrayOfInt5);
/* 160 */     SecP128R1Field.multiply(arrayOfInt5, arrayOfInt2, arrayOfInt5);
/* 161 */     int[] arrayOfInt6 = arrayOfInt2;
/* 162 */     SecP128R1Field.squareN(arrayOfInt5, 10, arrayOfInt6);
/* 163 */     SecP128R1Field.multiply(arrayOfInt6, arrayOfInt5, arrayOfInt6);
/* 164 */     int[] arrayOfInt7 = arrayOfInt4;
/* 165 */     SecP128R1Field.squareN(arrayOfInt6, 10, arrayOfInt7);
/* 166 */     SecP128R1Field.multiply(arrayOfInt7, arrayOfInt5, arrayOfInt7);
/* 167 */     int[] arrayOfInt8 = arrayOfInt5;
/* 168 */     SecP128R1Field.square(arrayOfInt7, arrayOfInt8);
/* 169 */     SecP128R1Field.multiply(arrayOfInt8, arrayOfInt1, arrayOfInt8);
/*     */     
/* 171 */     int[] arrayOfInt9 = arrayOfInt8;
/* 172 */     SecP128R1Field.squareN(arrayOfInt9, 95, arrayOfInt9);
/*     */     
/* 174 */     int[] arrayOfInt10 = arrayOfInt7;
/* 175 */     SecP128R1Field.square(arrayOfInt9, arrayOfInt10);
/*     */     
/* 177 */     return Nat128.eq(arrayOfInt1, arrayOfInt10) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement(arrayOfInt9) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 182 */     if (paramObject == this)
/*     */     {
/* 184 */       return true;
/*     */     }
/*     */     
/* 187 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement))
/*     */     {
/* 189 */       return false;
/*     */     }
/*     */     
/* 192 */     org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement secP128R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement)paramObject;
/* 193 */     return Nat128.eq(this.x, secP128R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 198 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 4);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP128R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
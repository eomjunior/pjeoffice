/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP521R1Field;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP521R1FieldElement
/*     */   extends ECFieldElement.AbstractFp {
/*  12 */   public static final BigInteger Q = new BigInteger(1, 
/*  13 */       Hex.decodeStrict("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"));
/*     */   
/*     */   protected int[] x;
/*     */ 
/*     */   
/*     */   public SecP521R1FieldElement(BigInteger paramBigInteger) {
/*  19 */     if (paramBigInteger == null || paramBigInteger.signum() < 0 || paramBigInteger.compareTo(Q) >= 0)
/*     */     {
/*  21 */       throw new IllegalArgumentException("x value invalid for SecP521R1FieldElement");
/*     */     }
/*     */     
/*  24 */     this.x = SecP521R1Field.fromBigInteger(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public SecP521R1FieldElement() {
/*  29 */     this.x = Nat.create(17);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SecP521R1FieldElement(int[] paramArrayOfint) {
/*  34 */     this.x = paramArrayOfint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/*  39 */     return Nat.isZero(17, this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOne() {
/*  44 */     return Nat.isOne(17, this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean testBitZero() {
/*  49 */     return (Nat.getBit(this.x, 0) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger toBigInteger() {
/*  54 */     return Nat.toBigInteger(17, this.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/*  59 */     return "SecP521R1Field";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  64 */     return Q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement add(ECFieldElement paramECFieldElement) {
/*  69 */     int[] arrayOfInt = Nat.create(17);
/*  70 */     SecP521R1Field.add(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  71 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement addOne() {
/*  76 */     int[] arrayOfInt = Nat.create(17);
/*  77 */     SecP521R1Field.addOne(this.x, arrayOfInt);
/*  78 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement subtract(ECFieldElement paramECFieldElement) {
/*  83 */     int[] arrayOfInt = Nat.create(17);
/*  84 */     SecP521R1Field.subtract(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  85 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement multiply(ECFieldElement paramECFieldElement) {
/*  90 */     int[] arrayOfInt = Nat.create(17);
/*  91 */     SecP521R1Field.multiply(this.x, ((org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement)paramECFieldElement).x, arrayOfInt);
/*  92 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement divide(ECFieldElement paramECFieldElement) {
/*  98 */     int[] arrayOfInt = Nat.create(17);
/*  99 */     SecP521R1Field.inv(((org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement)paramECFieldElement).x, arrayOfInt);
/* 100 */     SecP521R1Field.multiply(arrayOfInt, this.x, arrayOfInt);
/* 101 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement negate() {
/* 106 */     int[] arrayOfInt = Nat.create(17);
/* 107 */     SecP521R1Field.negate(this.x, arrayOfInt);
/* 108 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement square() {
/* 113 */     int[] arrayOfInt = Nat.create(17);
/* 114 */     SecP521R1Field.square(this.x, arrayOfInt);
/* 115 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement invert() {
/* 121 */     int[] arrayOfInt = Nat.create(17);
/* 122 */     SecP521R1Field.inv(this.x, arrayOfInt);
/* 123 */     return (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt);
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
/* 136 */     if (Nat.isZero(17, arrayOfInt1) || Nat.isOne(17, arrayOfInt1))
/*     */     {
/* 138 */       return (ECFieldElement)this;
/*     */     }
/*     */     
/* 141 */     int[] arrayOfInt2 = Nat.create(17);
/* 142 */     int[] arrayOfInt3 = Nat.create(17);
/*     */     
/* 144 */     SecP521R1Field.squareN(arrayOfInt1, 519, arrayOfInt2);
/* 145 */     SecP521R1Field.square(arrayOfInt2, arrayOfInt3);
/*     */     
/* 147 */     return Nat.eq(17, arrayOfInt1, arrayOfInt3) ? (ECFieldElement)new org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement(arrayOfInt2) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 152 */     if (paramObject == this)
/*     */     {
/* 154 */       return true;
/*     */     }
/*     */     
/* 157 */     if (!(paramObject instanceof org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement))
/*     */     {
/* 159 */       return false;
/*     */     }
/*     */     
/* 162 */     org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement secP521R1FieldElement = (org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement)paramObject;
/* 163 */     return Nat.eq(17, this.x, secP521R1FieldElement.x);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 168 */     return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 17);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP521R1FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
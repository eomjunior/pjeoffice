/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256K1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ 
/*     */ public class SecP256K1Point extends ECPoint.AbstractFp {
/*     */   SecP256K1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  13 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecP256K1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  18 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  23 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP256K1Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     int[] arrayOfInt5, arrayOfInt6, arrayOfInt7, arrayOfInt8;
/*  29 */     if (isInfinity())
/*     */     {
/*  31 */       return paramECPoint;
/*     */     }
/*  33 */     if (paramECPoint.isInfinity())
/*     */     {
/*  35 */       return (ECPoint)this;
/*     */     }
/*  37 */     if (this == paramECPoint)
/*     */     {
/*  39 */       return twice();
/*     */     }
/*     */     
/*  42 */     ECCurve eCCurve = getCurve();
/*     */     
/*  44 */     SecP256K1FieldElement secP256K1FieldElement1 = (SecP256K1FieldElement)this.x, secP256K1FieldElement2 = (SecP256K1FieldElement)this.y;
/*  45 */     SecP256K1FieldElement secP256K1FieldElement3 = (SecP256K1FieldElement)paramECPoint.getXCoord(), secP256K1FieldElement4 = (SecP256K1FieldElement)paramECPoint.getYCoord();
/*     */     
/*  47 */     SecP256K1FieldElement secP256K1FieldElement5 = (SecP256K1FieldElement)this.zs[0];
/*  48 */     SecP256K1FieldElement secP256K1FieldElement6 = (SecP256K1FieldElement)paramECPoint.getZCoord(0);
/*     */ 
/*     */     
/*  51 */     int[] arrayOfInt1 = Nat256.createExt();
/*  52 */     int[] arrayOfInt2 = Nat256.create();
/*  53 */     int[] arrayOfInt3 = Nat256.create();
/*  54 */     int[] arrayOfInt4 = Nat256.create();
/*     */     
/*  56 */     boolean bool1 = secP256K1FieldElement5.isOne();
/*     */     
/*  58 */     if (bool1) {
/*     */       
/*  60 */       arrayOfInt5 = secP256K1FieldElement3.x;
/*  61 */       arrayOfInt6 = secP256K1FieldElement4.x;
/*     */     }
/*     */     else {
/*     */       
/*  65 */       arrayOfInt6 = arrayOfInt3;
/*  66 */       SecP256K1Field.square(secP256K1FieldElement5.x, arrayOfInt6);
/*     */       
/*  68 */       arrayOfInt5 = arrayOfInt2;
/*  69 */       SecP256K1Field.multiply(arrayOfInt6, secP256K1FieldElement3.x, arrayOfInt5);
/*     */       
/*  71 */       SecP256K1Field.multiply(arrayOfInt6, secP256K1FieldElement5.x, arrayOfInt6);
/*  72 */       SecP256K1Field.multiply(arrayOfInt6, secP256K1FieldElement4.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  75 */     boolean bool2 = secP256K1FieldElement6.isOne();
/*     */     
/*  77 */     if (bool2) {
/*     */       
/*  79 */       arrayOfInt7 = secP256K1FieldElement1.x;
/*  80 */       arrayOfInt8 = secP256K1FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  84 */       arrayOfInt8 = arrayOfInt4;
/*  85 */       SecP256K1Field.square(secP256K1FieldElement6.x, arrayOfInt8);
/*     */       
/*  87 */       arrayOfInt7 = arrayOfInt1;
/*  88 */       SecP256K1Field.multiply(arrayOfInt8, secP256K1FieldElement1.x, arrayOfInt7);
/*     */       
/*  90 */       SecP256K1Field.multiply(arrayOfInt8, secP256K1FieldElement6.x, arrayOfInt8);
/*  91 */       SecP256K1Field.multiply(arrayOfInt8, secP256K1FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/*  94 */     int[] arrayOfInt9 = Nat256.create();
/*  95 */     SecP256K1Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/*  97 */     int[] arrayOfInt10 = arrayOfInt2;
/*  98 */     SecP256K1Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
/*     */ 
/*     */     
/* 101 */     if (Nat256.isZero(arrayOfInt9)) {
/*     */       
/* 103 */       if (Nat256.isZero(arrayOfInt10))
/*     */       {
/*     */         
/* 106 */         return twice();
/*     */       }
/*     */ 
/*     */       
/* 110 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 113 */     int[] arrayOfInt11 = arrayOfInt3;
/* 114 */     SecP256K1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 116 */     int[] arrayOfInt12 = Nat256.create();
/* 117 */     SecP256K1Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 119 */     int[] arrayOfInt13 = arrayOfInt3;
/* 120 */     SecP256K1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 122 */     SecP256K1Field.negate(arrayOfInt12, arrayOfInt12);
/* 123 */     Nat256.mul(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 125 */     int i = Nat256.addBothTo(arrayOfInt13, arrayOfInt13, arrayOfInt12);
/* 126 */     SecP256K1Field.reduce32(i, arrayOfInt12);
/*     */     
/* 128 */     SecP256K1FieldElement secP256K1FieldElement7 = new SecP256K1FieldElement(arrayOfInt4);
/* 129 */     SecP256K1Field.square(arrayOfInt10, secP256K1FieldElement7.x);
/* 130 */     SecP256K1Field.subtract(secP256K1FieldElement7.x, arrayOfInt12, secP256K1FieldElement7.x);
/*     */     
/* 132 */     SecP256K1FieldElement secP256K1FieldElement8 = new SecP256K1FieldElement(arrayOfInt12);
/* 133 */     SecP256K1Field.subtract(arrayOfInt13, secP256K1FieldElement7.x, secP256K1FieldElement8.x);
/* 134 */     SecP256K1Field.multiplyAddToExt(secP256K1FieldElement8.x, arrayOfInt10, arrayOfInt1);
/* 135 */     SecP256K1Field.reduce(arrayOfInt1, secP256K1FieldElement8.x);
/*     */     
/* 137 */     SecP256K1FieldElement secP256K1FieldElement9 = new SecP256K1FieldElement(arrayOfInt9);
/* 138 */     if (!bool1)
/*     */     {
/* 140 */       SecP256K1Field.multiply(secP256K1FieldElement9.x, secP256K1FieldElement5.x, secP256K1FieldElement9.x);
/*     */     }
/* 142 */     if (!bool2)
/*     */     {
/* 144 */       SecP256K1Field.multiply(secP256K1FieldElement9.x, secP256K1FieldElement6.x, secP256K1FieldElement9.x);
/*     */     }
/*     */     
/* 147 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)secP256K1FieldElement9 };
/*     */     
/* 149 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP256K1Point(eCCurve, (ECFieldElement)secP256K1FieldElement7, (ECFieldElement)secP256K1FieldElement8, arrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ECPoint twice() {
/* 155 */     if (isInfinity())
/*     */     {
/* 157 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 160 */     ECCurve eCCurve = getCurve();
/*     */     
/* 162 */     SecP256K1FieldElement secP256K1FieldElement1 = (SecP256K1FieldElement)this.y;
/* 163 */     if (secP256K1FieldElement1.isZero())
/*     */     {
/* 165 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 168 */     SecP256K1FieldElement secP256K1FieldElement2 = (SecP256K1FieldElement)this.x, secP256K1FieldElement3 = (SecP256K1FieldElement)this.zs[0];
/*     */ 
/*     */ 
/*     */     
/* 172 */     int[] arrayOfInt1 = Nat256.create();
/* 173 */     SecP256K1Field.square(secP256K1FieldElement1.x, arrayOfInt1);
/*     */     
/* 175 */     int[] arrayOfInt2 = Nat256.create();
/* 176 */     SecP256K1Field.square(arrayOfInt1, arrayOfInt2);
/*     */     
/* 178 */     int[] arrayOfInt3 = Nat256.create();
/* 179 */     SecP256K1Field.square(secP256K1FieldElement2.x, arrayOfInt3);
/* 180 */     int i = Nat256.addBothTo(arrayOfInt3, arrayOfInt3, arrayOfInt3);
/* 181 */     SecP256K1Field.reduce32(i, arrayOfInt3);
/*     */     
/* 183 */     int[] arrayOfInt4 = arrayOfInt1;
/* 184 */     SecP256K1Field.multiply(arrayOfInt1, secP256K1FieldElement2.x, arrayOfInt4);
/* 185 */     i = Nat.shiftUpBits(8, arrayOfInt4, 2, 0);
/* 186 */     SecP256K1Field.reduce32(i, arrayOfInt4);
/*     */     
/* 188 */     int[] arrayOfInt5 = Nat256.create();
/* 189 */     i = Nat.shiftUpBits(8, arrayOfInt2, 3, 0, arrayOfInt5);
/* 190 */     SecP256K1Field.reduce32(i, arrayOfInt5);
/*     */     
/* 192 */     SecP256K1FieldElement secP256K1FieldElement4 = new SecP256K1FieldElement(arrayOfInt2);
/* 193 */     SecP256K1Field.square(arrayOfInt3, secP256K1FieldElement4.x);
/* 194 */     SecP256K1Field.subtract(secP256K1FieldElement4.x, arrayOfInt4, secP256K1FieldElement4.x);
/* 195 */     SecP256K1Field.subtract(secP256K1FieldElement4.x, arrayOfInt4, secP256K1FieldElement4.x);
/*     */     
/* 197 */     SecP256K1FieldElement secP256K1FieldElement5 = new SecP256K1FieldElement(arrayOfInt4);
/* 198 */     SecP256K1Field.subtract(arrayOfInt4, secP256K1FieldElement4.x, secP256K1FieldElement5.x);
/* 199 */     SecP256K1Field.multiply(secP256K1FieldElement5.x, arrayOfInt3, secP256K1FieldElement5.x);
/* 200 */     SecP256K1Field.subtract(secP256K1FieldElement5.x, arrayOfInt5, secP256K1FieldElement5.x);
/*     */     
/* 202 */     SecP256K1FieldElement secP256K1FieldElement6 = new SecP256K1FieldElement(arrayOfInt3);
/* 203 */     SecP256K1Field.twice(secP256K1FieldElement1.x, secP256K1FieldElement6.x);
/* 204 */     if (!secP256K1FieldElement3.isOne())
/*     */     {
/* 206 */       SecP256K1Field.multiply(secP256K1FieldElement6.x, secP256K1FieldElement3.x, secP256K1FieldElement6.x);
/*     */     }
/*     */     
/* 209 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP256K1Point(eCCurve, (ECFieldElement)secP256K1FieldElement4, (ECFieldElement)secP256K1FieldElement5, new ECFieldElement[] { (ECFieldElement)secP256K1FieldElement6 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 214 */     if (this == paramECPoint)
/*     */     {
/* 216 */       return threeTimes();
/*     */     }
/* 218 */     if (isInfinity())
/*     */     {
/* 220 */       return paramECPoint;
/*     */     }
/* 222 */     if (paramECPoint.isInfinity())
/*     */     {
/* 224 */       return twice();
/*     */     }
/*     */     
/* 227 */     ECFieldElement eCFieldElement = this.y;
/* 228 */     if (eCFieldElement.isZero())
/*     */     {
/* 230 */       return paramECPoint;
/*     */     }
/*     */     
/* 233 */     return twice().add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint threeTimes() {
/* 238 */     if (isInfinity() || this.y.isZero())
/*     */     {
/* 240 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 244 */     return twice().add((ECPoint)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 249 */     if (isInfinity())
/*     */     {
/* 251 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 254 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP256K1Point(this.curve, this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP256K1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
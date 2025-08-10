/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192K1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ 
/*     */ public class SecP192K1Point extends ECPoint.AbstractFp {
/*     */   SecP192K1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  13 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecP192K1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  18 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  23 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192K1Point(null, getAffineXCoord(), getAffineYCoord());
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
/*  44 */     SecP192K1FieldElement secP192K1FieldElement1 = (SecP192K1FieldElement)this.x, secP192K1FieldElement2 = (SecP192K1FieldElement)this.y;
/*  45 */     SecP192K1FieldElement secP192K1FieldElement3 = (SecP192K1FieldElement)paramECPoint.getXCoord(), secP192K1FieldElement4 = (SecP192K1FieldElement)paramECPoint.getYCoord();
/*     */     
/*  47 */     SecP192K1FieldElement secP192K1FieldElement5 = (SecP192K1FieldElement)this.zs[0];
/*  48 */     SecP192K1FieldElement secP192K1FieldElement6 = (SecP192K1FieldElement)paramECPoint.getZCoord(0);
/*     */ 
/*     */     
/*  51 */     int[] arrayOfInt1 = Nat192.createExt();
/*  52 */     int[] arrayOfInt2 = Nat192.create();
/*  53 */     int[] arrayOfInt3 = Nat192.create();
/*  54 */     int[] arrayOfInt4 = Nat192.create();
/*     */     
/*  56 */     boolean bool1 = secP192K1FieldElement5.isOne();
/*     */     
/*  58 */     if (bool1) {
/*     */       
/*  60 */       arrayOfInt5 = secP192K1FieldElement3.x;
/*  61 */       arrayOfInt6 = secP192K1FieldElement4.x;
/*     */     }
/*     */     else {
/*     */       
/*  65 */       arrayOfInt6 = arrayOfInt3;
/*  66 */       SecP192K1Field.square(secP192K1FieldElement5.x, arrayOfInt6);
/*     */       
/*  68 */       arrayOfInt5 = arrayOfInt2;
/*  69 */       SecP192K1Field.multiply(arrayOfInt6, secP192K1FieldElement3.x, arrayOfInt5);
/*     */       
/*  71 */       SecP192K1Field.multiply(arrayOfInt6, secP192K1FieldElement5.x, arrayOfInt6);
/*  72 */       SecP192K1Field.multiply(arrayOfInt6, secP192K1FieldElement4.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  75 */     boolean bool2 = secP192K1FieldElement6.isOne();
/*     */     
/*  77 */     if (bool2) {
/*     */       
/*  79 */       arrayOfInt7 = secP192K1FieldElement1.x;
/*  80 */       arrayOfInt8 = secP192K1FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  84 */       arrayOfInt8 = arrayOfInt4;
/*  85 */       SecP192K1Field.square(secP192K1FieldElement6.x, arrayOfInt8);
/*     */       
/*  87 */       arrayOfInt7 = arrayOfInt1;
/*  88 */       SecP192K1Field.multiply(arrayOfInt8, secP192K1FieldElement1.x, arrayOfInt7);
/*     */       
/*  90 */       SecP192K1Field.multiply(arrayOfInt8, secP192K1FieldElement6.x, arrayOfInt8);
/*  91 */       SecP192K1Field.multiply(arrayOfInt8, secP192K1FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/*  94 */     int[] arrayOfInt9 = Nat192.create();
/*  95 */     SecP192K1Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/*  97 */     int[] arrayOfInt10 = arrayOfInt2;
/*  98 */     SecP192K1Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
/*     */ 
/*     */     
/* 101 */     if (Nat192.isZero(arrayOfInt9)) {
/*     */       
/* 103 */       if (Nat192.isZero(arrayOfInt10))
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
/* 114 */     SecP192K1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 116 */     int[] arrayOfInt12 = Nat192.create();
/* 117 */     SecP192K1Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 119 */     int[] arrayOfInt13 = arrayOfInt3;
/* 120 */     SecP192K1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 122 */     SecP192K1Field.negate(arrayOfInt12, arrayOfInt12);
/* 123 */     Nat192.mul(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 125 */     int i = Nat192.addBothTo(arrayOfInt13, arrayOfInt13, arrayOfInt12);
/* 126 */     SecP192K1Field.reduce32(i, arrayOfInt12);
/*     */     
/* 128 */     SecP192K1FieldElement secP192K1FieldElement7 = new SecP192K1FieldElement(arrayOfInt4);
/* 129 */     SecP192K1Field.square(arrayOfInt10, secP192K1FieldElement7.x);
/* 130 */     SecP192K1Field.subtract(secP192K1FieldElement7.x, arrayOfInt12, secP192K1FieldElement7.x);
/*     */     
/* 132 */     SecP192K1FieldElement secP192K1FieldElement8 = new SecP192K1FieldElement(arrayOfInt12);
/* 133 */     SecP192K1Field.subtract(arrayOfInt13, secP192K1FieldElement7.x, secP192K1FieldElement8.x);
/* 134 */     SecP192K1Field.multiplyAddToExt(secP192K1FieldElement8.x, arrayOfInt10, arrayOfInt1);
/* 135 */     SecP192K1Field.reduce(arrayOfInt1, secP192K1FieldElement8.x);
/*     */     
/* 137 */     SecP192K1FieldElement secP192K1FieldElement9 = new SecP192K1FieldElement(arrayOfInt9);
/* 138 */     if (!bool1)
/*     */     {
/* 140 */       SecP192K1Field.multiply(secP192K1FieldElement9.x, secP192K1FieldElement5.x, secP192K1FieldElement9.x);
/*     */     }
/* 142 */     if (!bool2)
/*     */     {
/* 144 */       SecP192K1Field.multiply(secP192K1FieldElement9.x, secP192K1FieldElement6.x, secP192K1FieldElement9.x);
/*     */     }
/*     */     
/* 147 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)secP192K1FieldElement9 };
/*     */     
/* 149 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192K1Point(eCCurve, (ECFieldElement)secP192K1FieldElement7, (ECFieldElement)secP192K1FieldElement8, arrayOfECFieldElement);
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
/* 162 */     SecP192K1FieldElement secP192K1FieldElement1 = (SecP192K1FieldElement)this.y;
/* 163 */     if (secP192K1FieldElement1.isZero())
/*     */     {
/* 165 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 168 */     SecP192K1FieldElement secP192K1FieldElement2 = (SecP192K1FieldElement)this.x, secP192K1FieldElement3 = (SecP192K1FieldElement)this.zs[0];
/*     */ 
/*     */ 
/*     */     
/* 172 */     int[] arrayOfInt1 = Nat192.create();
/* 173 */     SecP192K1Field.square(secP192K1FieldElement1.x, arrayOfInt1);
/*     */     
/* 175 */     int[] arrayOfInt2 = Nat192.create();
/* 176 */     SecP192K1Field.square(arrayOfInt1, arrayOfInt2);
/*     */     
/* 178 */     int[] arrayOfInt3 = Nat192.create();
/* 179 */     SecP192K1Field.square(secP192K1FieldElement2.x, arrayOfInt3);
/* 180 */     int i = Nat192.addBothTo(arrayOfInt3, arrayOfInt3, arrayOfInt3);
/* 181 */     SecP192K1Field.reduce32(i, arrayOfInt3);
/*     */     
/* 183 */     int[] arrayOfInt4 = arrayOfInt1;
/* 184 */     SecP192K1Field.multiply(arrayOfInt1, secP192K1FieldElement2.x, arrayOfInt4);
/* 185 */     i = Nat.shiftUpBits(6, arrayOfInt4, 2, 0);
/* 186 */     SecP192K1Field.reduce32(i, arrayOfInt4);
/*     */     
/* 188 */     int[] arrayOfInt5 = Nat192.create();
/* 189 */     i = Nat.shiftUpBits(6, arrayOfInt2, 3, 0, arrayOfInt5);
/* 190 */     SecP192K1Field.reduce32(i, arrayOfInt5);
/*     */     
/* 192 */     SecP192K1FieldElement secP192K1FieldElement4 = new SecP192K1FieldElement(arrayOfInt2);
/* 193 */     SecP192K1Field.square(arrayOfInt3, secP192K1FieldElement4.x);
/* 194 */     SecP192K1Field.subtract(secP192K1FieldElement4.x, arrayOfInt4, secP192K1FieldElement4.x);
/* 195 */     SecP192K1Field.subtract(secP192K1FieldElement4.x, arrayOfInt4, secP192K1FieldElement4.x);
/*     */     
/* 197 */     SecP192K1FieldElement secP192K1FieldElement5 = new SecP192K1FieldElement(arrayOfInt4);
/* 198 */     SecP192K1Field.subtract(arrayOfInt4, secP192K1FieldElement4.x, secP192K1FieldElement5.x);
/* 199 */     SecP192K1Field.multiply(secP192K1FieldElement5.x, arrayOfInt3, secP192K1FieldElement5.x);
/* 200 */     SecP192K1Field.subtract(secP192K1FieldElement5.x, arrayOfInt5, secP192K1FieldElement5.x);
/*     */     
/* 202 */     SecP192K1FieldElement secP192K1FieldElement6 = new SecP192K1FieldElement(arrayOfInt3);
/* 203 */     SecP192K1Field.twice(secP192K1FieldElement1.x, secP192K1FieldElement6.x);
/* 204 */     if (!secP192K1FieldElement3.isOne())
/*     */     {
/* 206 */       SecP192K1Field.multiply(secP192K1FieldElement6.x, secP192K1FieldElement3.x, secP192K1FieldElement6.x);
/*     */     }
/*     */     
/* 209 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192K1Point(eCCurve, (ECFieldElement)secP192K1FieldElement4, (ECFieldElement)secP192K1FieldElement5, new ECFieldElement[] { (ECFieldElement)secP192K1FieldElement6 });
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
/* 254 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192K1Point(this.curve, this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP192K1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192R1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ 
/*     */ public class SecP192R1Point extends ECPoint.AbstractFp {
/*     */   SecP192R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  13 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecP192R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  18 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  23 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192R1Point(null, getAffineXCoord(), getAffineYCoord());
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
/*  44 */     SecP192R1FieldElement secP192R1FieldElement1 = (SecP192R1FieldElement)this.x, secP192R1FieldElement2 = (SecP192R1FieldElement)this.y;
/*  45 */     SecP192R1FieldElement secP192R1FieldElement3 = (SecP192R1FieldElement)paramECPoint.getXCoord(), secP192R1FieldElement4 = (SecP192R1FieldElement)paramECPoint.getYCoord();
/*     */     
/*  47 */     SecP192R1FieldElement secP192R1FieldElement5 = (SecP192R1FieldElement)this.zs[0];
/*  48 */     SecP192R1FieldElement secP192R1FieldElement6 = (SecP192R1FieldElement)paramECPoint.getZCoord(0);
/*     */ 
/*     */     
/*  51 */     int[] arrayOfInt1 = Nat192.createExt();
/*  52 */     int[] arrayOfInt2 = Nat192.create();
/*  53 */     int[] arrayOfInt3 = Nat192.create();
/*  54 */     int[] arrayOfInt4 = Nat192.create();
/*     */     
/*  56 */     boolean bool1 = secP192R1FieldElement5.isOne();
/*     */     
/*  58 */     if (bool1) {
/*     */       
/*  60 */       arrayOfInt5 = secP192R1FieldElement3.x;
/*  61 */       arrayOfInt6 = secP192R1FieldElement4.x;
/*     */     }
/*     */     else {
/*     */       
/*  65 */       arrayOfInt6 = arrayOfInt3;
/*  66 */       SecP192R1Field.square(secP192R1FieldElement5.x, arrayOfInt6);
/*     */       
/*  68 */       arrayOfInt5 = arrayOfInt2;
/*  69 */       SecP192R1Field.multiply(arrayOfInt6, secP192R1FieldElement3.x, arrayOfInt5);
/*     */       
/*  71 */       SecP192R1Field.multiply(arrayOfInt6, secP192R1FieldElement5.x, arrayOfInt6);
/*  72 */       SecP192R1Field.multiply(arrayOfInt6, secP192R1FieldElement4.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  75 */     boolean bool2 = secP192R1FieldElement6.isOne();
/*     */     
/*  77 */     if (bool2) {
/*     */       
/*  79 */       arrayOfInt7 = secP192R1FieldElement1.x;
/*  80 */       arrayOfInt8 = secP192R1FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  84 */       arrayOfInt8 = arrayOfInt4;
/*  85 */       SecP192R1Field.square(secP192R1FieldElement6.x, arrayOfInt8);
/*     */       
/*  87 */       arrayOfInt7 = arrayOfInt1;
/*  88 */       SecP192R1Field.multiply(arrayOfInt8, secP192R1FieldElement1.x, arrayOfInt7);
/*     */       
/*  90 */       SecP192R1Field.multiply(arrayOfInt8, secP192R1FieldElement6.x, arrayOfInt8);
/*  91 */       SecP192R1Field.multiply(arrayOfInt8, secP192R1FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/*  94 */     int[] arrayOfInt9 = Nat192.create();
/*  95 */     SecP192R1Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/*  97 */     int[] arrayOfInt10 = arrayOfInt2;
/*  98 */     SecP192R1Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
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
/* 114 */     SecP192R1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 116 */     int[] arrayOfInt12 = Nat192.create();
/* 117 */     SecP192R1Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 119 */     int[] arrayOfInt13 = arrayOfInt3;
/* 120 */     SecP192R1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 122 */     SecP192R1Field.negate(arrayOfInt12, arrayOfInt12);
/* 123 */     Nat192.mul(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 125 */     int i = Nat192.addBothTo(arrayOfInt13, arrayOfInt13, arrayOfInt12);
/* 126 */     SecP192R1Field.reduce32(i, arrayOfInt12);
/*     */     
/* 128 */     SecP192R1FieldElement secP192R1FieldElement7 = new SecP192R1FieldElement(arrayOfInt4);
/* 129 */     SecP192R1Field.square(arrayOfInt10, secP192R1FieldElement7.x);
/* 130 */     SecP192R1Field.subtract(secP192R1FieldElement7.x, arrayOfInt12, secP192R1FieldElement7.x);
/*     */     
/* 132 */     SecP192R1FieldElement secP192R1FieldElement8 = new SecP192R1FieldElement(arrayOfInt12);
/* 133 */     SecP192R1Field.subtract(arrayOfInt13, secP192R1FieldElement7.x, secP192R1FieldElement8.x);
/* 134 */     SecP192R1Field.multiplyAddToExt(secP192R1FieldElement8.x, arrayOfInt10, arrayOfInt1);
/* 135 */     SecP192R1Field.reduce(arrayOfInt1, secP192R1FieldElement8.x);
/*     */     
/* 137 */     SecP192R1FieldElement secP192R1FieldElement9 = new SecP192R1FieldElement(arrayOfInt9);
/* 138 */     if (!bool1)
/*     */     {
/* 140 */       SecP192R1Field.multiply(secP192R1FieldElement9.x, secP192R1FieldElement5.x, secP192R1FieldElement9.x);
/*     */     }
/* 142 */     if (!bool2)
/*     */     {
/* 144 */       SecP192R1Field.multiply(secP192R1FieldElement9.x, secP192R1FieldElement6.x, secP192R1FieldElement9.x);
/*     */     }
/*     */     
/* 147 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)secP192R1FieldElement9 };
/*     */     
/* 149 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192R1Point(eCCurve, (ECFieldElement)secP192R1FieldElement7, (ECFieldElement)secP192R1FieldElement8, arrayOfECFieldElement);
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
/* 162 */     SecP192R1FieldElement secP192R1FieldElement1 = (SecP192R1FieldElement)this.y;
/* 163 */     if (secP192R1FieldElement1.isZero())
/*     */     {
/* 165 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 168 */     SecP192R1FieldElement secP192R1FieldElement2 = (SecP192R1FieldElement)this.x, secP192R1FieldElement3 = (SecP192R1FieldElement)this.zs[0];
/*     */ 
/*     */     
/* 171 */     int[] arrayOfInt1 = Nat192.create();
/* 172 */     int[] arrayOfInt2 = Nat192.create();
/*     */     
/* 174 */     int[] arrayOfInt3 = Nat192.create();
/* 175 */     SecP192R1Field.square(secP192R1FieldElement1.x, arrayOfInt3);
/*     */     
/* 177 */     int[] arrayOfInt4 = Nat192.create();
/* 178 */     SecP192R1Field.square(arrayOfInt3, arrayOfInt4);
/*     */     
/* 180 */     boolean bool = secP192R1FieldElement3.isOne();
/*     */     
/* 182 */     int[] arrayOfInt5 = secP192R1FieldElement3.x;
/* 183 */     if (!bool) {
/*     */       
/* 185 */       arrayOfInt5 = arrayOfInt2;
/* 186 */       SecP192R1Field.square(secP192R1FieldElement3.x, arrayOfInt5);
/*     */     } 
/*     */     
/* 189 */     SecP192R1Field.subtract(secP192R1FieldElement2.x, arrayOfInt5, arrayOfInt1);
/*     */     
/* 191 */     int[] arrayOfInt6 = arrayOfInt2;
/* 192 */     SecP192R1Field.add(secP192R1FieldElement2.x, arrayOfInt5, arrayOfInt6);
/* 193 */     SecP192R1Field.multiply(arrayOfInt6, arrayOfInt1, arrayOfInt6);
/* 194 */     int i = Nat192.addBothTo(arrayOfInt6, arrayOfInt6, arrayOfInt6);
/* 195 */     SecP192R1Field.reduce32(i, arrayOfInt6);
/*     */     
/* 197 */     int[] arrayOfInt7 = arrayOfInt3;
/* 198 */     SecP192R1Field.multiply(arrayOfInt3, secP192R1FieldElement2.x, arrayOfInt7);
/* 199 */     i = Nat.shiftUpBits(6, arrayOfInt7, 2, 0);
/* 200 */     SecP192R1Field.reduce32(i, arrayOfInt7);
/*     */     
/* 202 */     i = Nat.shiftUpBits(6, arrayOfInt4, 3, 0, arrayOfInt1);
/* 203 */     SecP192R1Field.reduce32(i, arrayOfInt1);
/*     */     
/* 205 */     SecP192R1FieldElement secP192R1FieldElement4 = new SecP192R1FieldElement(arrayOfInt4);
/* 206 */     SecP192R1Field.square(arrayOfInt6, secP192R1FieldElement4.x);
/* 207 */     SecP192R1Field.subtract(secP192R1FieldElement4.x, arrayOfInt7, secP192R1FieldElement4.x);
/* 208 */     SecP192R1Field.subtract(secP192R1FieldElement4.x, arrayOfInt7, secP192R1FieldElement4.x);
/*     */     
/* 210 */     SecP192R1FieldElement secP192R1FieldElement5 = new SecP192R1FieldElement(arrayOfInt7);
/* 211 */     SecP192R1Field.subtract(arrayOfInt7, secP192R1FieldElement4.x, secP192R1FieldElement5.x);
/* 212 */     SecP192R1Field.multiply(secP192R1FieldElement5.x, arrayOfInt6, secP192R1FieldElement5.x);
/* 213 */     SecP192R1Field.subtract(secP192R1FieldElement5.x, arrayOfInt1, secP192R1FieldElement5.x);
/*     */     
/* 215 */     SecP192R1FieldElement secP192R1FieldElement6 = new SecP192R1FieldElement(arrayOfInt6);
/* 216 */     SecP192R1Field.twice(secP192R1FieldElement1.x, secP192R1FieldElement6.x);
/* 217 */     if (!bool)
/*     */     {
/* 219 */       SecP192R1Field.multiply(secP192R1FieldElement6.x, secP192R1FieldElement3.x, secP192R1FieldElement6.x);
/*     */     }
/*     */     
/* 222 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192R1Point(eCCurve, (ECFieldElement)secP192R1FieldElement4, (ECFieldElement)secP192R1FieldElement5, new ECFieldElement[] { (ECFieldElement)secP192R1FieldElement6 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 227 */     if (this == paramECPoint)
/*     */     {
/* 229 */       return threeTimes();
/*     */     }
/* 231 */     if (isInfinity())
/*     */     {
/* 233 */       return paramECPoint;
/*     */     }
/* 235 */     if (paramECPoint.isInfinity())
/*     */     {
/* 237 */       return twice();
/*     */     }
/*     */     
/* 240 */     ECFieldElement eCFieldElement = this.y;
/* 241 */     if (eCFieldElement.isZero())
/*     */     {
/* 243 */       return paramECPoint;
/*     */     }
/*     */     
/* 246 */     return twice().add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint threeTimes() {
/* 251 */     if (isInfinity() || this.y.isZero())
/*     */     {
/* 253 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 257 */     return twice().add((ECPoint)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 262 */     if (isInfinity())
/*     */     {
/* 264 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 267 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP192R1Point(this.curve, this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP192R1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
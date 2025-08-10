/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R1FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat160;
/*     */ 
/*     */ public class SecP160R1Point extends ECPoint.AbstractFp {
/*     */   SecP160R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  13 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecP160R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  18 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  23 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP160R1Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */   
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     int[] arrayOfInt5, arrayOfInt6, arrayOfInt7, arrayOfInt8;
/*  28 */     if (isInfinity())
/*     */     {
/*  30 */       return paramECPoint;
/*     */     }
/*  32 */     if (paramECPoint.isInfinity())
/*     */     {
/*  34 */       return (ECPoint)this;
/*     */     }
/*  36 */     if (this == paramECPoint)
/*     */     {
/*  38 */       return twice();
/*     */     }
/*     */     
/*  41 */     ECCurve eCCurve = getCurve();
/*     */     
/*  43 */     SecP160R1FieldElement secP160R1FieldElement1 = (SecP160R1FieldElement)this.x, secP160R1FieldElement2 = (SecP160R1FieldElement)this.y;
/*  44 */     SecP160R1FieldElement secP160R1FieldElement3 = (SecP160R1FieldElement)paramECPoint.getXCoord(), secP160R1FieldElement4 = (SecP160R1FieldElement)paramECPoint.getYCoord();
/*     */     
/*  46 */     SecP160R1FieldElement secP160R1FieldElement5 = (SecP160R1FieldElement)this.zs[0];
/*  47 */     SecP160R1FieldElement secP160R1FieldElement6 = (SecP160R1FieldElement)paramECPoint.getZCoord(0);
/*     */ 
/*     */     
/*  50 */     int[] arrayOfInt1 = Nat160.createExt();
/*  51 */     int[] arrayOfInt2 = Nat160.create();
/*  52 */     int[] arrayOfInt3 = Nat160.create();
/*  53 */     int[] arrayOfInt4 = Nat160.create();
/*     */     
/*  55 */     boolean bool1 = secP160R1FieldElement5.isOne();
/*     */     
/*  57 */     if (bool1) {
/*     */       
/*  59 */       arrayOfInt5 = secP160R1FieldElement3.x;
/*  60 */       arrayOfInt6 = secP160R1FieldElement4.x;
/*     */     }
/*     */     else {
/*     */       
/*  64 */       arrayOfInt6 = arrayOfInt3;
/*  65 */       SecP160R1Field.square(secP160R1FieldElement5.x, arrayOfInt6);
/*     */       
/*  67 */       arrayOfInt5 = arrayOfInt2;
/*  68 */       SecP160R1Field.multiply(arrayOfInt6, secP160R1FieldElement3.x, arrayOfInt5);
/*     */       
/*  70 */       SecP160R1Field.multiply(arrayOfInt6, secP160R1FieldElement5.x, arrayOfInt6);
/*  71 */       SecP160R1Field.multiply(arrayOfInt6, secP160R1FieldElement4.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  74 */     boolean bool2 = secP160R1FieldElement6.isOne();
/*     */     
/*  76 */     if (bool2) {
/*     */       
/*  78 */       arrayOfInt7 = secP160R1FieldElement1.x;
/*  79 */       arrayOfInt8 = secP160R1FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  83 */       arrayOfInt8 = arrayOfInt4;
/*  84 */       SecP160R1Field.square(secP160R1FieldElement6.x, arrayOfInt8);
/*     */       
/*  86 */       arrayOfInt7 = arrayOfInt1;
/*  87 */       SecP160R1Field.multiply(arrayOfInt8, secP160R1FieldElement1.x, arrayOfInt7);
/*     */       
/*  89 */       SecP160R1Field.multiply(arrayOfInt8, secP160R1FieldElement6.x, arrayOfInt8);
/*  90 */       SecP160R1Field.multiply(arrayOfInt8, secP160R1FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/*  93 */     int[] arrayOfInt9 = Nat160.create();
/*  94 */     SecP160R1Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/*  96 */     int[] arrayOfInt10 = arrayOfInt2;
/*  97 */     SecP160R1Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
/*     */ 
/*     */     
/* 100 */     if (Nat160.isZero(arrayOfInt9)) {
/*     */       
/* 102 */       if (Nat160.isZero(arrayOfInt10))
/*     */       {
/*     */         
/* 105 */         return twice();
/*     */       }
/*     */ 
/*     */       
/* 109 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 112 */     int[] arrayOfInt11 = arrayOfInt3;
/* 113 */     SecP160R1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 115 */     int[] arrayOfInt12 = Nat160.create();
/* 116 */     SecP160R1Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 118 */     int[] arrayOfInt13 = arrayOfInt3;
/* 119 */     SecP160R1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 121 */     SecP160R1Field.negate(arrayOfInt12, arrayOfInt12);
/* 122 */     Nat160.mul(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 124 */     int i = Nat160.addBothTo(arrayOfInt13, arrayOfInt13, arrayOfInt12);
/* 125 */     SecP160R1Field.reduce32(i, arrayOfInt12);
/*     */     
/* 127 */     SecP160R1FieldElement secP160R1FieldElement7 = new SecP160R1FieldElement(arrayOfInt4);
/* 128 */     SecP160R1Field.square(arrayOfInt10, secP160R1FieldElement7.x);
/* 129 */     SecP160R1Field.subtract(secP160R1FieldElement7.x, arrayOfInt12, secP160R1FieldElement7.x);
/*     */     
/* 131 */     SecP160R1FieldElement secP160R1FieldElement8 = new SecP160R1FieldElement(arrayOfInt12);
/* 132 */     SecP160R1Field.subtract(arrayOfInt13, secP160R1FieldElement7.x, secP160R1FieldElement8.x);
/* 133 */     SecP160R1Field.multiplyAddToExt(secP160R1FieldElement8.x, arrayOfInt10, arrayOfInt1);
/* 134 */     SecP160R1Field.reduce(arrayOfInt1, secP160R1FieldElement8.x);
/*     */     
/* 136 */     SecP160R1FieldElement secP160R1FieldElement9 = new SecP160R1FieldElement(arrayOfInt9);
/* 137 */     if (!bool1)
/*     */     {
/* 139 */       SecP160R1Field.multiply(secP160R1FieldElement9.x, secP160R1FieldElement5.x, secP160R1FieldElement9.x);
/*     */     }
/* 141 */     if (!bool2)
/*     */     {
/* 143 */       SecP160R1Field.multiply(secP160R1FieldElement9.x, secP160R1FieldElement6.x, secP160R1FieldElement9.x);
/*     */     }
/*     */     
/* 146 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)secP160R1FieldElement9 };
/*     */     
/* 148 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP160R1Point(eCCurve, (ECFieldElement)secP160R1FieldElement7, (ECFieldElement)secP160R1FieldElement8, arrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twice() {
/* 153 */     if (isInfinity())
/*     */     {
/* 155 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 158 */     ECCurve eCCurve = getCurve();
/*     */     
/* 160 */     SecP160R1FieldElement secP160R1FieldElement1 = (SecP160R1FieldElement)this.y;
/* 161 */     if (secP160R1FieldElement1.isZero())
/*     */     {
/* 163 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 166 */     SecP160R1FieldElement secP160R1FieldElement2 = (SecP160R1FieldElement)this.x, secP160R1FieldElement3 = (SecP160R1FieldElement)this.zs[0];
/*     */ 
/*     */     
/* 169 */     int[] arrayOfInt1 = Nat160.create();
/* 170 */     int[] arrayOfInt2 = Nat160.create();
/*     */     
/* 172 */     int[] arrayOfInt3 = Nat160.create();
/* 173 */     SecP160R1Field.square(secP160R1FieldElement1.x, arrayOfInt3);
/*     */     
/* 175 */     int[] arrayOfInt4 = Nat160.create();
/* 176 */     SecP160R1Field.square(arrayOfInt3, arrayOfInt4);
/*     */     
/* 178 */     boolean bool = secP160R1FieldElement3.isOne();
/*     */     
/* 180 */     int[] arrayOfInt5 = secP160R1FieldElement3.x;
/* 181 */     if (!bool) {
/*     */       
/* 183 */       arrayOfInt5 = arrayOfInt2;
/* 184 */       SecP160R1Field.square(secP160R1FieldElement3.x, arrayOfInt5);
/*     */     } 
/*     */     
/* 187 */     SecP160R1Field.subtract(secP160R1FieldElement2.x, arrayOfInt5, arrayOfInt1);
/*     */     
/* 189 */     int[] arrayOfInt6 = arrayOfInt2;
/* 190 */     SecP160R1Field.add(secP160R1FieldElement2.x, arrayOfInt5, arrayOfInt6);
/* 191 */     SecP160R1Field.multiply(arrayOfInt6, arrayOfInt1, arrayOfInt6);
/* 192 */     int i = Nat160.addBothTo(arrayOfInt6, arrayOfInt6, arrayOfInt6);
/* 193 */     SecP160R1Field.reduce32(i, arrayOfInt6);
/*     */     
/* 195 */     int[] arrayOfInt7 = arrayOfInt3;
/* 196 */     SecP160R1Field.multiply(arrayOfInt3, secP160R1FieldElement2.x, arrayOfInt7);
/* 197 */     i = Nat.shiftUpBits(5, arrayOfInt7, 2, 0);
/* 198 */     SecP160R1Field.reduce32(i, arrayOfInt7);
/*     */     
/* 200 */     i = Nat.shiftUpBits(5, arrayOfInt4, 3, 0, arrayOfInt1);
/* 201 */     SecP160R1Field.reduce32(i, arrayOfInt1);
/*     */     
/* 203 */     SecP160R1FieldElement secP160R1FieldElement4 = new SecP160R1FieldElement(arrayOfInt4);
/* 204 */     SecP160R1Field.square(arrayOfInt6, secP160R1FieldElement4.x);
/* 205 */     SecP160R1Field.subtract(secP160R1FieldElement4.x, arrayOfInt7, secP160R1FieldElement4.x);
/* 206 */     SecP160R1Field.subtract(secP160R1FieldElement4.x, arrayOfInt7, secP160R1FieldElement4.x);
/*     */     
/* 208 */     SecP160R1FieldElement secP160R1FieldElement5 = new SecP160R1FieldElement(arrayOfInt7);
/* 209 */     SecP160R1Field.subtract(arrayOfInt7, secP160R1FieldElement4.x, secP160R1FieldElement5.x);
/* 210 */     SecP160R1Field.multiply(secP160R1FieldElement5.x, arrayOfInt6, secP160R1FieldElement5.x);
/* 211 */     SecP160R1Field.subtract(secP160R1FieldElement5.x, arrayOfInt1, secP160R1FieldElement5.x);
/*     */     
/* 213 */     SecP160R1FieldElement secP160R1FieldElement6 = new SecP160R1FieldElement(arrayOfInt6);
/* 214 */     SecP160R1Field.twice(secP160R1FieldElement1.x, secP160R1FieldElement6.x);
/* 215 */     if (!bool)
/*     */     {
/* 217 */       SecP160R1Field.multiply(secP160R1FieldElement6.x, secP160R1FieldElement3.x, secP160R1FieldElement6.x);
/*     */     }
/*     */     
/* 220 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP160R1Point(eCCurve, (ECFieldElement)secP160R1FieldElement4, (ECFieldElement)secP160R1FieldElement5, new ECFieldElement[] { (ECFieldElement)secP160R1FieldElement6 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 225 */     if (this == paramECPoint)
/*     */     {
/* 227 */       return threeTimes();
/*     */     }
/* 229 */     if (isInfinity())
/*     */     {
/* 231 */       return paramECPoint;
/*     */     }
/* 233 */     if (paramECPoint.isInfinity())
/*     */     {
/* 235 */       return twice();
/*     */     }
/*     */     
/* 238 */     ECFieldElement eCFieldElement = this.y;
/* 239 */     if (eCFieldElement.isZero())
/*     */     {
/* 241 */       return paramECPoint;
/*     */     }
/*     */     
/* 244 */     return twice().add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint threeTimes() {
/* 249 */     if (isInfinity() || this.y.isZero())
/*     */     {
/* 251 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 255 */     return twice().add((ECPoint)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 260 */     if (isInfinity())
/*     */     {
/* 262 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 265 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP160R1Point(this.curve, this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP160R1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
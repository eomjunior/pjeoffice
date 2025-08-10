/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP521R1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ 
/*     */ public class SecP521R1Point extends ECPoint.AbstractFp {
/*     */   SecP521R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  12 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecP521R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  17 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  22 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP521R1Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */   
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     int[] arrayOfInt5, arrayOfInt6, arrayOfInt7, arrayOfInt8;
/*  27 */     if (isInfinity())
/*     */     {
/*  29 */       return paramECPoint;
/*     */     }
/*  31 */     if (paramECPoint.isInfinity())
/*     */     {
/*  33 */       return (ECPoint)this;
/*     */     }
/*  35 */     if (this == paramECPoint)
/*     */     {
/*  37 */       return twice();
/*     */     }
/*     */     
/*  40 */     ECCurve eCCurve = getCurve();
/*     */     
/*  42 */     SecP521R1FieldElement secP521R1FieldElement1 = (SecP521R1FieldElement)this.x, secP521R1FieldElement2 = (SecP521R1FieldElement)this.y;
/*  43 */     SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement)paramECPoint.getXCoord(), secP521R1FieldElement4 = (SecP521R1FieldElement)paramECPoint.getYCoord();
/*     */     
/*  45 */     SecP521R1FieldElement secP521R1FieldElement5 = (SecP521R1FieldElement)this.zs[0];
/*  46 */     SecP521R1FieldElement secP521R1FieldElement6 = (SecP521R1FieldElement)paramECPoint.getZCoord(0);
/*     */     
/*  48 */     int[] arrayOfInt1 = Nat.create(17);
/*  49 */     int[] arrayOfInt2 = Nat.create(17);
/*  50 */     int[] arrayOfInt3 = Nat.create(17);
/*  51 */     int[] arrayOfInt4 = Nat.create(17);
/*     */     
/*  53 */     boolean bool1 = secP521R1FieldElement5.isOne();
/*     */     
/*  55 */     if (bool1) {
/*     */       
/*  57 */       arrayOfInt5 = secP521R1FieldElement3.x;
/*  58 */       arrayOfInt6 = secP521R1FieldElement4.x;
/*     */     }
/*     */     else {
/*     */       
/*  62 */       arrayOfInt6 = arrayOfInt3;
/*  63 */       SecP521R1Field.square(secP521R1FieldElement5.x, arrayOfInt6);
/*     */       
/*  65 */       arrayOfInt5 = arrayOfInt2;
/*  66 */       SecP521R1Field.multiply(arrayOfInt6, secP521R1FieldElement3.x, arrayOfInt5);
/*     */       
/*  68 */       SecP521R1Field.multiply(arrayOfInt6, secP521R1FieldElement5.x, arrayOfInt6);
/*  69 */       SecP521R1Field.multiply(arrayOfInt6, secP521R1FieldElement4.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  72 */     boolean bool2 = secP521R1FieldElement6.isOne();
/*     */     
/*  74 */     if (bool2) {
/*     */       
/*  76 */       arrayOfInt7 = secP521R1FieldElement1.x;
/*  77 */       arrayOfInt8 = secP521R1FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  81 */       arrayOfInt8 = arrayOfInt4;
/*  82 */       SecP521R1Field.square(secP521R1FieldElement6.x, arrayOfInt8);
/*     */       
/*  84 */       arrayOfInt7 = arrayOfInt1;
/*  85 */       SecP521R1Field.multiply(arrayOfInt8, secP521R1FieldElement1.x, arrayOfInt7);
/*     */       
/*  87 */       SecP521R1Field.multiply(arrayOfInt8, secP521R1FieldElement6.x, arrayOfInt8);
/*  88 */       SecP521R1Field.multiply(arrayOfInt8, secP521R1FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/*  91 */     int[] arrayOfInt9 = Nat.create(17);
/*  92 */     SecP521R1Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/*  94 */     int[] arrayOfInt10 = arrayOfInt2;
/*  95 */     SecP521R1Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
/*     */ 
/*     */     
/*  98 */     if (Nat.isZero(17, arrayOfInt9)) {
/*     */       
/* 100 */       if (Nat.isZero(17, arrayOfInt10))
/*     */       {
/*     */         
/* 103 */         return twice();
/*     */       }
/*     */ 
/*     */       
/* 107 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 110 */     int[] arrayOfInt11 = arrayOfInt3;
/* 111 */     SecP521R1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 113 */     int[] arrayOfInt12 = Nat.create(17);
/* 114 */     SecP521R1Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 116 */     int[] arrayOfInt13 = arrayOfInt3;
/* 117 */     SecP521R1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 119 */     SecP521R1Field.multiply(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 121 */     SecP521R1FieldElement secP521R1FieldElement7 = new SecP521R1FieldElement(arrayOfInt4);
/* 122 */     SecP521R1Field.square(arrayOfInt10, secP521R1FieldElement7.x);
/* 123 */     SecP521R1Field.add(secP521R1FieldElement7.x, arrayOfInt12, secP521R1FieldElement7.x);
/* 124 */     SecP521R1Field.subtract(secP521R1FieldElement7.x, arrayOfInt13, secP521R1FieldElement7.x);
/* 125 */     SecP521R1Field.subtract(secP521R1FieldElement7.x, arrayOfInt13, secP521R1FieldElement7.x);
/*     */     
/* 127 */     SecP521R1FieldElement secP521R1FieldElement8 = new SecP521R1FieldElement(arrayOfInt12);
/* 128 */     SecP521R1Field.subtract(arrayOfInt13, secP521R1FieldElement7.x, secP521R1FieldElement8.x);
/* 129 */     SecP521R1Field.multiply(secP521R1FieldElement8.x, arrayOfInt10, arrayOfInt2);
/* 130 */     SecP521R1Field.subtract(arrayOfInt2, arrayOfInt1, secP521R1FieldElement8.x);
/*     */     
/* 132 */     SecP521R1FieldElement secP521R1FieldElement9 = new SecP521R1FieldElement(arrayOfInt9);
/* 133 */     if (!bool1)
/*     */     {
/* 135 */       SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement5.x, secP521R1FieldElement9.x);
/*     */     }
/* 137 */     if (!bool2)
/*     */     {
/* 139 */       SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement6.x, secP521R1FieldElement9.x);
/*     */     }
/*     */     
/* 142 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)secP521R1FieldElement9 };
/*     */     
/* 144 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP521R1Point(eCCurve, (ECFieldElement)secP521R1FieldElement7, (ECFieldElement)secP521R1FieldElement8, arrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twice() {
/* 149 */     if (isInfinity())
/*     */     {
/* 151 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 154 */     ECCurve eCCurve = getCurve();
/*     */     
/* 156 */     SecP521R1FieldElement secP521R1FieldElement1 = (SecP521R1FieldElement)this.y;
/* 157 */     if (secP521R1FieldElement1.isZero())
/*     */     {
/* 159 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 162 */     SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement)this.x, secP521R1FieldElement3 = (SecP521R1FieldElement)this.zs[0];
/*     */     
/* 164 */     int[] arrayOfInt1 = Nat.create(17);
/* 165 */     int[] arrayOfInt2 = Nat.create(17);
/*     */     
/* 167 */     int[] arrayOfInt3 = Nat.create(17);
/* 168 */     SecP521R1Field.square(secP521R1FieldElement1.x, arrayOfInt3);
/*     */     
/* 170 */     int[] arrayOfInt4 = Nat.create(17);
/* 171 */     SecP521R1Field.square(arrayOfInt3, arrayOfInt4);
/*     */     
/* 173 */     boolean bool = secP521R1FieldElement3.isOne();
/*     */     
/* 175 */     int[] arrayOfInt5 = secP521R1FieldElement3.x;
/* 176 */     if (!bool) {
/*     */       
/* 178 */       arrayOfInt5 = arrayOfInt2;
/* 179 */       SecP521R1Field.square(secP521R1FieldElement3.x, arrayOfInt5);
/*     */     } 
/*     */     
/* 182 */     SecP521R1Field.subtract(secP521R1FieldElement2.x, arrayOfInt5, arrayOfInt1);
/*     */     
/* 184 */     int[] arrayOfInt6 = arrayOfInt2;
/* 185 */     SecP521R1Field.add(secP521R1FieldElement2.x, arrayOfInt5, arrayOfInt6);
/* 186 */     SecP521R1Field.multiply(arrayOfInt6, arrayOfInt1, arrayOfInt6);
/* 187 */     Nat.addBothTo(17, arrayOfInt6, arrayOfInt6, arrayOfInt6);
/* 188 */     SecP521R1Field.reduce23(arrayOfInt6);
/*     */     
/* 190 */     int[] arrayOfInt7 = arrayOfInt3;
/* 191 */     SecP521R1Field.multiply(arrayOfInt3, secP521R1FieldElement2.x, arrayOfInt7);
/* 192 */     Nat.shiftUpBits(17, arrayOfInt7, 2, 0);
/* 193 */     SecP521R1Field.reduce23(arrayOfInt7);
/*     */     
/* 195 */     Nat.shiftUpBits(17, arrayOfInt4, 3, 0, arrayOfInt1);
/* 196 */     SecP521R1Field.reduce23(arrayOfInt1);
/*     */     
/* 198 */     SecP521R1FieldElement secP521R1FieldElement4 = new SecP521R1FieldElement(arrayOfInt4);
/* 199 */     SecP521R1Field.square(arrayOfInt6, secP521R1FieldElement4.x);
/* 200 */     SecP521R1Field.subtract(secP521R1FieldElement4.x, arrayOfInt7, secP521R1FieldElement4.x);
/* 201 */     SecP521R1Field.subtract(secP521R1FieldElement4.x, arrayOfInt7, secP521R1FieldElement4.x);
/*     */     
/* 203 */     SecP521R1FieldElement secP521R1FieldElement5 = new SecP521R1FieldElement(arrayOfInt7);
/* 204 */     SecP521R1Field.subtract(arrayOfInt7, secP521R1FieldElement4.x, secP521R1FieldElement5.x);
/* 205 */     SecP521R1Field.multiply(secP521R1FieldElement5.x, arrayOfInt6, secP521R1FieldElement5.x);
/* 206 */     SecP521R1Field.subtract(secP521R1FieldElement5.x, arrayOfInt1, secP521R1FieldElement5.x);
/*     */     
/* 208 */     SecP521R1FieldElement secP521R1FieldElement6 = new SecP521R1FieldElement(arrayOfInt6);
/* 209 */     SecP521R1Field.twice(secP521R1FieldElement1.x, secP521R1FieldElement6.x);
/* 210 */     if (!bool)
/*     */     {
/* 212 */       SecP521R1Field.multiply(secP521R1FieldElement6.x, secP521R1FieldElement3.x, secP521R1FieldElement6.x);
/*     */     }
/*     */     
/* 215 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP521R1Point(eCCurve, (ECFieldElement)secP521R1FieldElement4, (ECFieldElement)secP521R1FieldElement5, new ECFieldElement[] { (ECFieldElement)secP521R1FieldElement6 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 220 */     if (this == paramECPoint)
/*     */     {
/* 222 */       return threeTimes();
/*     */     }
/* 224 */     if (isInfinity())
/*     */     {
/* 226 */       return paramECPoint;
/*     */     }
/* 228 */     if (paramECPoint.isInfinity())
/*     */     {
/* 230 */       return twice();
/*     */     }
/*     */     
/* 233 */     ECFieldElement eCFieldElement = this.y;
/* 234 */     if (eCFieldElement.isZero())
/*     */     {
/* 236 */       return paramECPoint;
/*     */     }
/*     */     
/* 239 */     return twice().add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint threeTimes() {
/* 244 */     if (isInfinity() || this.y.isZero())
/*     */     {
/* 246 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 250 */     return twice().add((ECPoint)this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECFieldElement two(ECFieldElement paramECFieldElement) {
/* 255 */     return paramECFieldElement.add(paramECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECFieldElement three(ECFieldElement paramECFieldElement) {
/* 260 */     return two(paramECFieldElement).add(paramECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECFieldElement four(ECFieldElement paramECFieldElement) {
/* 265 */     return two(two(paramECFieldElement));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECFieldElement eight(ECFieldElement paramECFieldElement) {
/* 270 */     return four(two(paramECFieldElement));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ECFieldElement doubleProductFromSquares(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement paramECFieldElement3, ECFieldElement paramECFieldElement4) {
/* 280 */     return paramECFieldElement1.add(paramECFieldElement2).square().subtract(paramECFieldElement3).subtract(paramECFieldElement4);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 285 */     if (isInfinity())
/*     */     {
/* 287 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 290 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP521R1Point(this.curve, this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP521R1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
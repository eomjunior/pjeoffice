/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571R1Curve;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat576;
/*     */ 
/*     */ public class SecT571R1Point extends ECPoint.AbstractF2m {
/*     */   SecT571R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  15 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecT571R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  20 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  25 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getYCoord() {
/*  30 */     ECFieldElement eCFieldElement1 = this.x, eCFieldElement2 = this.y;
/*     */     
/*  32 */     if (isInfinity() || eCFieldElement1.isZero())
/*     */     {
/*  34 */       return eCFieldElement2;
/*     */     }
/*     */ 
/*     */     
/*  38 */     ECFieldElement eCFieldElement3 = eCFieldElement2.add(eCFieldElement1).multiply(eCFieldElement1);
/*     */     
/*  40 */     ECFieldElement eCFieldElement4 = this.zs[0];
/*  41 */     if (!eCFieldElement4.isOne())
/*     */     {
/*  43 */       eCFieldElement3 = eCFieldElement3.divide(eCFieldElement4);
/*     */     }
/*     */     
/*  46 */     return eCFieldElement3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean getCompressionYTilde() {
/*  51 */     ECFieldElement eCFieldElement1 = getRawXCoord();
/*  52 */     if (eCFieldElement1.isZero())
/*     */     {
/*  54 */       return false;
/*     */     }
/*     */     
/*  57 */     ECFieldElement eCFieldElement2 = getRawYCoord();
/*     */ 
/*     */     
/*  60 */     return (eCFieldElement2.testBitZero() != eCFieldElement1.testBitZero());
/*     */   }
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     long[] arrayOfLong6, arrayOfLong7, arrayOfLong9, arrayOfLong10;
/*     */     SecT571FieldElement secT571FieldElement7, secT571FieldElement8, secT571FieldElement9;
/*  65 */     if (isInfinity())
/*     */     {
/*  67 */       return paramECPoint;
/*     */     }
/*  69 */     if (paramECPoint.isInfinity())
/*     */     {
/*  71 */       return (ECPoint)this;
/*     */     }
/*     */     
/*  74 */     ECCurve eCCurve = getCurve();
/*     */     
/*  76 */     SecT571FieldElement secT571FieldElement1 = (SecT571FieldElement)this.x;
/*  77 */     SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)paramECPoint.getRawXCoord();
/*     */     
/*  79 */     if (secT571FieldElement1.isZero()) {
/*     */       
/*  81 */       if (secT571FieldElement2.isZero())
/*     */       {
/*  83 */         return eCCurve.getInfinity();
/*     */       }
/*     */       
/*  86 */       return paramECPoint.add((ECPoint)this);
/*     */     } 
/*     */     
/*  89 */     SecT571FieldElement secT571FieldElement3 = (SecT571FieldElement)this.y, secT571FieldElement4 = (SecT571FieldElement)this.zs[0];
/*  90 */     SecT571FieldElement secT571FieldElement5 = (SecT571FieldElement)paramECPoint.getRawYCoord(), secT571FieldElement6 = (SecT571FieldElement)paramECPoint.getZCoord(0);
/*     */     
/*  92 */     long[] arrayOfLong1 = Nat576.create64();
/*  93 */     long[] arrayOfLong2 = Nat576.create64();
/*  94 */     long[] arrayOfLong3 = Nat576.create64();
/*  95 */     long[] arrayOfLong4 = Nat576.create64();
/*     */     
/*  97 */     long[] arrayOfLong5 = secT571FieldElement4.isOne() ? null : SecT571Field.precompMultiplicand(secT571FieldElement4.x);
/*     */     
/*  99 */     if (arrayOfLong5 == null) {
/*     */       
/* 101 */       arrayOfLong6 = secT571FieldElement2.x;
/* 102 */       arrayOfLong7 = secT571FieldElement5.x;
/*     */     }
/*     */     else {
/*     */       
/* 106 */       SecT571Field.multiplyPrecomp(secT571FieldElement2.x, arrayOfLong5, arrayOfLong6 = arrayOfLong2);
/* 107 */       SecT571Field.multiplyPrecomp(secT571FieldElement5.x, arrayOfLong5, arrayOfLong7 = arrayOfLong4);
/*     */     } 
/*     */     
/* 110 */     long[] arrayOfLong8 = secT571FieldElement6.isOne() ? null : SecT571Field.precompMultiplicand(secT571FieldElement6.x);
/*     */     
/* 112 */     if (arrayOfLong8 == null) {
/*     */       
/* 114 */       arrayOfLong9 = secT571FieldElement1.x;
/* 115 */       arrayOfLong10 = secT571FieldElement3.x;
/*     */     }
/*     */     else {
/*     */       
/* 119 */       SecT571Field.multiplyPrecomp(secT571FieldElement1.x, arrayOfLong8, arrayOfLong9 = arrayOfLong1);
/* 120 */       SecT571Field.multiplyPrecomp(secT571FieldElement3.x, arrayOfLong8, arrayOfLong10 = arrayOfLong3);
/*     */     } 
/*     */     
/* 123 */     long[] arrayOfLong11 = arrayOfLong3;
/* 124 */     SecT571Field.add(arrayOfLong10, arrayOfLong7, arrayOfLong11);
/*     */     
/* 126 */     long[] arrayOfLong12 = arrayOfLong4;
/* 127 */     SecT571Field.add(arrayOfLong9, arrayOfLong6, arrayOfLong12);
/*     */     
/* 129 */     if (Nat576.isZero64(arrayOfLong12)) {
/*     */       
/* 131 */       if (Nat576.isZero64(arrayOfLong11))
/*     */       {
/* 133 */         return twice();
/*     */       }
/*     */       
/* 136 */       return eCCurve.getInfinity();
/*     */     } 
/*     */ 
/*     */     
/* 140 */     if (secT571FieldElement2.isZero()) {
/*     */ 
/*     */       
/* 143 */       ECPoint eCPoint = normalize();
/* 144 */       secT571FieldElement1 = (SecT571FieldElement)eCPoint.getXCoord();
/* 145 */       ECFieldElement eCFieldElement1 = eCPoint.getYCoord();
/*     */       
/* 147 */       SecT571FieldElement secT571FieldElement = secT571FieldElement5;
/* 148 */       ECFieldElement eCFieldElement2 = eCFieldElement1.add((ECFieldElement)secT571FieldElement).divide((ECFieldElement)secT571FieldElement1);
/*     */       
/* 150 */       secT571FieldElement7 = (SecT571FieldElement)eCFieldElement2.square().add(eCFieldElement2).add((ECFieldElement)secT571FieldElement1).addOne();
/* 151 */       if (secT571FieldElement7.isZero())
/*     */       {
/* 153 */         return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)secT571FieldElement7, (ECFieldElement)SecT571R1Curve.SecT571R1_B_SQRT);
/*     */       }
/*     */       
/* 156 */       ECFieldElement eCFieldElement3 = eCFieldElement2.multiply(secT571FieldElement1.add((ECFieldElement)secT571FieldElement7)).add((ECFieldElement)secT571FieldElement7).add(eCFieldElement1);
/* 157 */       secT571FieldElement8 = (SecT571FieldElement)eCFieldElement3.divide((ECFieldElement)secT571FieldElement7).add((ECFieldElement)secT571FieldElement7);
/* 158 */       secT571FieldElement9 = (SecT571FieldElement)eCCurve.fromBigInteger(ECConstants.ONE);
/*     */     }
/*     */     else {
/*     */       
/* 162 */       SecT571Field.square(arrayOfLong12, arrayOfLong12);
/*     */       
/* 164 */       long[] arrayOfLong13 = SecT571Field.precompMultiplicand(arrayOfLong11);
/*     */       
/* 166 */       long[] arrayOfLong14 = arrayOfLong1;
/* 167 */       long[] arrayOfLong15 = arrayOfLong2;
/*     */       
/* 169 */       SecT571Field.multiplyPrecomp(arrayOfLong9, arrayOfLong13, arrayOfLong14);
/* 170 */       SecT571Field.multiplyPrecomp(arrayOfLong6, arrayOfLong13, arrayOfLong15);
/*     */       
/* 172 */       secT571FieldElement7 = new SecT571FieldElement(arrayOfLong1);
/* 173 */       SecT571Field.multiply(arrayOfLong14, arrayOfLong15, secT571FieldElement7.x);
/*     */       
/* 175 */       if (secT571FieldElement7.isZero())
/*     */       {
/* 177 */         return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)secT571FieldElement7, (ECFieldElement)SecT571R1Curve.SecT571R1_B_SQRT);
/*     */       }
/*     */       
/* 180 */       secT571FieldElement9 = new SecT571FieldElement(arrayOfLong3);
/* 181 */       SecT571Field.multiplyPrecomp(arrayOfLong12, arrayOfLong13, secT571FieldElement9.x);
/*     */       
/* 183 */       if (arrayOfLong8 != null)
/*     */       {
/* 185 */         SecT571Field.multiplyPrecomp(secT571FieldElement9.x, arrayOfLong8, secT571FieldElement9.x);
/*     */       }
/*     */       
/* 188 */       long[] arrayOfLong16 = Nat576.createExt64();
/*     */       
/* 190 */       SecT571Field.add(arrayOfLong15, arrayOfLong12, arrayOfLong4);
/* 191 */       SecT571Field.squareAddToExt(arrayOfLong4, arrayOfLong16);
/*     */       
/* 193 */       SecT571Field.add(secT571FieldElement3.x, secT571FieldElement4.x, arrayOfLong4);
/* 194 */       SecT571Field.multiplyAddToExt(arrayOfLong4, secT571FieldElement9.x, arrayOfLong16);
/*     */       
/* 196 */       secT571FieldElement8 = new SecT571FieldElement(arrayOfLong4);
/* 197 */       SecT571Field.reduce(arrayOfLong16, secT571FieldElement8.x);
/*     */       
/* 199 */       if (arrayOfLong5 != null)
/*     */       {
/* 201 */         SecT571Field.multiplyPrecomp(secT571FieldElement9.x, arrayOfLong5, secT571FieldElement9.x);
/*     */       }
/*     */     } 
/*     */     
/* 205 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)secT571FieldElement7, (ECFieldElement)secT571FieldElement8, new ECFieldElement[] { (ECFieldElement)secT571FieldElement9 });
/*     */   }
/*     */   
/*     */   public ECPoint twice() {
/*     */     long[] arrayOfLong4, arrayOfLong5, arrayOfLong8;
/* 210 */     if (isInfinity())
/*     */     {
/* 212 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 215 */     ECCurve eCCurve = getCurve();
/*     */     
/* 217 */     SecT571FieldElement secT571FieldElement1 = (SecT571FieldElement)this.x;
/* 218 */     if (secT571FieldElement1.isZero())
/*     */     {
/*     */       
/* 221 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 224 */     SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)this.y, secT571FieldElement3 = (SecT571FieldElement)this.zs[0];
/*     */     
/* 226 */     long[] arrayOfLong1 = Nat576.create64();
/* 227 */     long[] arrayOfLong2 = Nat576.create64();
/*     */     
/* 229 */     long[] arrayOfLong3 = secT571FieldElement3.isOne() ? null : SecT571Field.precompMultiplicand(secT571FieldElement3.x);
/*     */     
/* 231 */     if (arrayOfLong3 == null) {
/*     */       
/* 233 */       arrayOfLong4 = secT571FieldElement2.x;
/* 234 */       arrayOfLong5 = secT571FieldElement3.x;
/*     */     }
/*     */     else {
/*     */       
/* 238 */       SecT571Field.multiplyPrecomp(secT571FieldElement2.x, arrayOfLong3, arrayOfLong4 = arrayOfLong1);
/* 239 */       SecT571Field.square(secT571FieldElement3.x, arrayOfLong5 = arrayOfLong2);
/*     */     } 
/*     */     
/* 242 */     long[] arrayOfLong6 = Nat576.create64();
/* 243 */     SecT571Field.square(secT571FieldElement2.x, arrayOfLong6);
/* 244 */     SecT571Field.addBothTo(arrayOfLong4, arrayOfLong5, arrayOfLong6);
/*     */     
/* 246 */     if (Nat576.isZero64(arrayOfLong6))
/*     */     {
/* 248 */       return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)new SecT571FieldElement(arrayOfLong6), (ECFieldElement)SecT571R1Curve.SecT571R1_B_SQRT);
/*     */     }
/*     */     
/* 251 */     long[] arrayOfLong7 = Nat576.createExt64();
/* 252 */     SecT571Field.multiplyAddToExt(arrayOfLong6, arrayOfLong4, arrayOfLong7);
/*     */     
/* 254 */     SecT571FieldElement secT571FieldElement4 = new SecT571FieldElement(arrayOfLong1);
/* 255 */     SecT571Field.square(arrayOfLong6, secT571FieldElement4.x);
/*     */     
/* 257 */     SecT571FieldElement secT571FieldElement5 = new SecT571FieldElement(arrayOfLong6);
/* 258 */     if (arrayOfLong3 != null)
/*     */     {
/* 260 */       SecT571Field.multiply(secT571FieldElement5.x, arrayOfLong5, secT571FieldElement5.x);
/*     */     }
/*     */ 
/*     */     
/* 264 */     if (arrayOfLong3 == null) {
/*     */       
/* 266 */       arrayOfLong8 = secT571FieldElement1.x;
/*     */     }
/*     */     else {
/*     */       
/* 270 */       SecT571Field.multiplyPrecomp(secT571FieldElement1.x, arrayOfLong3, arrayOfLong8 = arrayOfLong2);
/*     */     } 
/*     */     
/* 273 */     SecT571Field.squareAddToExt(arrayOfLong8, arrayOfLong7);
/* 274 */     SecT571Field.reduce(arrayOfLong7, arrayOfLong2);
/* 275 */     SecT571Field.addBothTo(secT571FieldElement4.x, secT571FieldElement5.x, arrayOfLong2);
/* 276 */     SecT571FieldElement secT571FieldElement6 = new SecT571FieldElement(arrayOfLong2);
/*     */     
/* 278 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)secT571FieldElement4, (ECFieldElement)secT571FieldElement6, new ECFieldElement[] { (ECFieldElement)secT571FieldElement5 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 283 */     if (isInfinity())
/*     */     {
/* 285 */       return paramECPoint;
/*     */     }
/* 287 */     if (paramECPoint.isInfinity())
/*     */     {
/* 289 */       return twice();
/*     */     }
/*     */     
/* 292 */     ECCurve eCCurve = getCurve();
/*     */     
/* 294 */     SecT571FieldElement secT571FieldElement1 = (SecT571FieldElement)this.x;
/* 295 */     if (secT571FieldElement1.isZero())
/*     */     {
/*     */       
/* 298 */       return paramECPoint;
/*     */     }
/*     */     
/* 301 */     SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)paramECPoint.getRawXCoord(), secT571FieldElement3 = (SecT571FieldElement)paramECPoint.getZCoord(0);
/* 302 */     if (secT571FieldElement2.isZero() || !secT571FieldElement3.isOne())
/*     */     {
/* 304 */       return twice().add(paramECPoint);
/*     */     }
/*     */     
/* 307 */     SecT571FieldElement secT571FieldElement4 = (SecT571FieldElement)this.y, secT571FieldElement5 = (SecT571FieldElement)this.zs[0];
/* 308 */     SecT571FieldElement secT571FieldElement6 = (SecT571FieldElement)paramECPoint.getRawYCoord();
/*     */     
/* 310 */     long[] arrayOfLong1 = Nat576.create64();
/* 311 */     long[] arrayOfLong2 = Nat576.create64();
/* 312 */     long[] arrayOfLong3 = Nat576.create64();
/* 313 */     long[] arrayOfLong4 = Nat576.create64();
/*     */     
/* 315 */     long[] arrayOfLong5 = arrayOfLong1;
/* 316 */     SecT571Field.square(secT571FieldElement1.x, arrayOfLong5);
/*     */     
/* 318 */     long[] arrayOfLong6 = arrayOfLong2;
/* 319 */     SecT571Field.square(secT571FieldElement4.x, arrayOfLong6);
/*     */     
/* 321 */     long[] arrayOfLong7 = arrayOfLong3;
/* 322 */     SecT571Field.square(secT571FieldElement5.x, arrayOfLong7);
/*     */     
/* 324 */     long[] arrayOfLong8 = arrayOfLong4;
/* 325 */     SecT571Field.multiply(secT571FieldElement4.x, secT571FieldElement5.x, arrayOfLong8);
/*     */     
/* 327 */     long[] arrayOfLong9 = arrayOfLong8;
/* 328 */     SecT571Field.addBothTo(arrayOfLong7, arrayOfLong6, arrayOfLong9);
/*     */     
/* 330 */     long[] arrayOfLong10 = SecT571Field.precompMultiplicand(arrayOfLong7);
/*     */     
/* 332 */     long[] arrayOfLong11 = arrayOfLong3;
/* 333 */     SecT571Field.multiplyPrecomp(secT571FieldElement6.x, arrayOfLong10, arrayOfLong11);
/* 334 */     SecT571Field.add(arrayOfLong11, arrayOfLong6, arrayOfLong11);
/*     */     
/* 336 */     long[] arrayOfLong12 = Nat576.createExt64();
/* 337 */     SecT571Field.multiplyAddToExt(arrayOfLong11, arrayOfLong9, arrayOfLong12);
/* 338 */     SecT571Field.multiplyPrecompAddToExt(arrayOfLong5, arrayOfLong10, arrayOfLong12);
/* 339 */     SecT571Field.reduce(arrayOfLong12, arrayOfLong11);
/*     */     
/* 341 */     long[] arrayOfLong13 = arrayOfLong1;
/* 342 */     SecT571Field.multiplyPrecomp(secT571FieldElement2.x, arrayOfLong10, arrayOfLong13);
/*     */     
/* 344 */     long[] arrayOfLong14 = arrayOfLong2;
/* 345 */     SecT571Field.add(arrayOfLong13, arrayOfLong9, arrayOfLong14);
/* 346 */     SecT571Field.square(arrayOfLong14, arrayOfLong14);
/*     */     
/* 348 */     if (Nat576.isZero64(arrayOfLong14)) {
/*     */       
/* 350 */       if (Nat576.isZero64(arrayOfLong11))
/*     */       {
/* 352 */         return paramECPoint.twice();
/*     */       }
/*     */       
/* 355 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 358 */     if (Nat576.isZero64(arrayOfLong11))
/*     */     {
/* 360 */       return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)new SecT571FieldElement(arrayOfLong11), (ECFieldElement)SecT571R1Curve.SecT571R1_B_SQRT);
/*     */     }
/*     */     
/* 363 */     SecT571FieldElement secT571FieldElement7 = new SecT571FieldElement();
/* 364 */     SecT571Field.square(arrayOfLong11, secT571FieldElement7.x);
/* 365 */     SecT571Field.multiply(secT571FieldElement7.x, arrayOfLong13, secT571FieldElement7.x);
/*     */     
/* 367 */     SecT571FieldElement secT571FieldElement8 = new SecT571FieldElement(arrayOfLong1);
/* 368 */     SecT571Field.multiply(arrayOfLong11, arrayOfLong14, secT571FieldElement8.x);
/* 369 */     SecT571Field.multiplyPrecomp(secT571FieldElement8.x, arrayOfLong10, secT571FieldElement8.x);
/*     */     
/* 371 */     SecT571FieldElement secT571FieldElement9 = new SecT571FieldElement(arrayOfLong2);
/* 372 */     SecT571Field.add(arrayOfLong11, arrayOfLong14, secT571FieldElement9.x);
/* 373 */     SecT571Field.square(secT571FieldElement9.x, secT571FieldElement9.x);
/*     */     
/* 375 */     Nat.zero64(18, arrayOfLong12);
/* 376 */     SecT571Field.multiplyAddToExt(secT571FieldElement9.x, arrayOfLong9, arrayOfLong12);
/* 377 */     SecT571Field.addOne(secT571FieldElement6.x, arrayOfLong4);
/* 378 */     SecT571Field.multiplyAddToExt(arrayOfLong4, secT571FieldElement8.x, arrayOfLong12);
/* 379 */     SecT571Field.reduce(arrayOfLong12, secT571FieldElement9.x);
/*     */     
/* 381 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(eCCurve, (ECFieldElement)secT571FieldElement7, (ECFieldElement)secT571FieldElement9, new ECFieldElement[] { (ECFieldElement)secT571FieldElement8 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 386 */     if (isInfinity())
/*     */     {
/* 388 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 391 */     ECFieldElement eCFieldElement1 = this.x;
/* 392 */     if (eCFieldElement1.isZero())
/*     */     {
/* 394 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 398 */     ECFieldElement eCFieldElement2 = this.y, eCFieldElement3 = this.zs[0];
/* 399 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571R1Point(this.curve, eCFieldElement1, eCFieldElement2.add(eCFieldElement3), new ECFieldElement[] { eCFieldElement3 });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT571R1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
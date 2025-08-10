/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat576;
/*     */ 
/*     */ public class SecT571K1Point
/*     */   extends ECPoint.AbstractF2m {
/*     */   SecT571K1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  14 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecT571K1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  19 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  24 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getYCoord() {
/*  29 */     ECFieldElement eCFieldElement1 = this.x, eCFieldElement2 = this.y;
/*     */     
/*  31 */     if (isInfinity() || eCFieldElement1.isZero())
/*     */     {
/*  33 */       return eCFieldElement2;
/*     */     }
/*     */ 
/*     */     
/*  37 */     ECFieldElement eCFieldElement3 = eCFieldElement2.add(eCFieldElement1).multiply(eCFieldElement1);
/*     */     
/*  39 */     ECFieldElement eCFieldElement4 = this.zs[0];
/*  40 */     if (!eCFieldElement4.isOne())
/*     */     {
/*  42 */       eCFieldElement3 = eCFieldElement3.divide(eCFieldElement4);
/*     */     }
/*     */     
/*  45 */     return eCFieldElement3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean getCompressionYTilde() {
/*  50 */     ECFieldElement eCFieldElement1 = getRawXCoord();
/*  51 */     if (eCFieldElement1.isZero())
/*     */     {
/*  53 */       return false;
/*     */     }
/*     */     
/*  56 */     ECFieldElement eCFieldElement2 = getRawYCoord();
/*     */ 
/*     */     
/*  59 */     return (eCFieldElement2.testBitZero() != eCFieldElement1.testBitZero());
/*     */   }
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     long[] arrayOfLong6, arrayOfLong7, arrayOfLong9, arrayOfLong10;
/*     */     SecT571FieldElement secT571FieldElement7, secT571FieldElement8, secT571FieldElement9;
/*  64 */     if (isInfinity())
/*     */     {
/*  66 */       return paramECPoint;
/*     */     }
/*  68 */     if (paramECPoint.isInfinity())
/*     */     {
/*  70 */       return (ECPoint)this;
/*     */     }
/*     */     
/*  73 */     ECCurve eCCurve = getCurve();
/*     */     
/*  75 */     SecT571FieldElement secT571FieldElement1 = (SecT571FieldElement)this.x;
/*  76 */     SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)paramECPoint.getRawXCoord();
/*     */     
/*  78 */     if (secT571FieldElement1.isZero()) {
/*     */       
/*  80 */       if (secT571FieldElement2.isZero())
/*     */       {
/*  82 */         return eCCurve.getInfinity();
/*     */       }
/*     */       
/*  85 */       return paramECPoint.add((ECPoint)this);
/*     */     } 
/*     */     
/*  88 */     SecT571FieldElement secT571FieldElement3 = (SecT571FieldElement)this.y, secT571FieldElement4 = (SecT571FieldElement)this.zs[0];
/*  89 */     SecT571FieldElement secT571FieldElement5 = (SecT571FieldElement)paramECPoint.getRawYCoord(), secT571FieldElement6 = (SecT571FieldElement)paramECPoint.getZCoord(0);
/*     */     
/*  91 */     long[] arrayOfLong1 = Nat576.create64();
/*  92 */     long[] arrayOfLong2 = Nat576.create64();
/*  93 */     long[] arrayOfLong3 = Nat576.create64();
/*  94 */     long[] arrayOfLong4 = Nat576.create64();
/*     */     
/*  96 */     long[] arrayOfLong5 = secT571FieldElement4.isOne() ? null : SecT571Field.precompMultiplicand(secT571FieldElement4.x);
/*     */     
/*  98 */     if (arrayOfLong5 == null) {
/*     */       
/* 100 */       arrayOfLong6 = secT571FieldElement2.x;
/* 101 */       arrayOfLong7 = secT571FieldElement5.x;
/*     */     }
/*     */     else {
/*     */       
/* 105 */       SecT571Field.multiplyPrecomp(secT571FieldElement2.x, arrayOfLong5, arrayOfLong6 = arrayOfLong2);
/* 106 */       SecT571Field.multiplyPrecomp(secT571FieldElement5.x, arrayOfLong5, arrayOfLong7 = arrayOfLong4);
/*     */     } 
/*     */     
/* 109 */     long[] arrayOfLong8 = secT571FieldElement6.isOne() ? null : SecT571Field.precompMultiplicand(secT571FieldElement6.x);
/*     */     
/* 111 */     if (arrayOfLong8 == null) {
/*     */       
/* 113 */       arrayOfLong9 = secT571FieldElement1.x;
/* 114 */       arrayOfLong10 = secT571FieldElement3.x;
/*     */     }
/*     */     else {
/*     */       
/* 118 */       SecT571Field.multiplyPrecomp(secT571FieldElement1.x, arrayOfLong8, arrayOfLong9 = arrayOfLong1);
/* 119 */       SecT571Field.multiplyPrecomp(secT571FieldElement3.x, arrayOfLong8, arrayOfLong10 = arrayOfLong3);
/*     */     } 
/*     */     
/* 122 */     long[] arrayOfLong11 = arrayOfLong3;
/* 123 */     SecT571Field.add(arrayOfLong10, arrayOfLong7, arrayOfLong11);
/*     */     
/* 125 */     long[] arrayOfLong12 = arrayOfLong4;
/* 126 */     SecT571Field.add(arrayOfLong9, arrayOfLong6, arrayOfLong12);
/*     */     
/* 128 */     if (Nat576.isZero64(arrayOfLong12)) {
/*     */       
/* 130 */       if (Nat576.isZero64(arrayOfLong11))
/*     */       {
/* 132 */         return twice();
/*     */       }
/*     */       
/* 135 */       return eCCurve.getInfinity();
/*     */     } 
/*     */ 
/*     */     
/* 139 */     if (secT571FieldElement2.isZero()) {
/*     */ 
/*     */       
/* 142 */       ECPoint eCPoint = normalize();
/* 143 */       secT571FieldElement1 = (SecT571FieldElement)eCPoint.getXCoord();
/* 144 */       ECFieldElement eCFieldElement1 = eCPoint.getYCoord();
/*     */       
/* 146 */       SecT571FieldElement secT571FieldElement = secT571FieldElement5;
/* 147 */       ECFieldElement eCFieldElement2 = eCFieldElement1.add((ECFieldElement)secT571FieldElement).divide((ECFieldElement)secT571FieldElement1);
/*     */       
/* 149 */       secT571FieldElement7 = (SecT571FieldElement)eCFieldElement2.square().add(eCFieldElement2).add((ECFieldElement)secT571FieldElement1);
/* 150 */       if (secT571FieldElement7.isZero())
/*     */       {
/* 152 */         return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, (ECFieldElement)secT571FieldElement7, eCCurve.getB());
/*     */       }
/*     */       
/* 155 */       ECFieldElement eCFieldElement3 = eCFieldElement2.multiply(secT571FieldElement1.add((ECFieldElement)secT571FieldElement7)).add((ECFieldElement)secT571FieldElement7).add(eCFieldElement1);
/* 156 */       secT571FieldElement8 = (SecT571FieldElement)eCFieldElement3.divide((ECFieldElement)secT571FieldElement7).add((ECFieldElement)secT571FieldElement7);
/* 157 */       secT571FieldElement9 = (SecT571FieldElement)eCCurve.fromBigInteger(ECConstants.ONE);
/*     */     }
/*     */     else {
/*     */       
/* 161 */       SecT571Field.square(arrayOfLong12, arrayOfLong12);
/*     */       
/* 163 */       long[] arrayOfLong13 = SecT571Field.precompMultiplicand(arrayOfLong11);
/*     */       
/* 165 */       long[] arrayOfLong14 = arrayOfLong1;
/* 166 */       long[] arrayOfLong15 = arrayOfLong2;
/*     */       
/* 168 */       SecT571Field.multiplyPrecomp(arrayOfLong9, arrayOfLong13, arrayOfLong14);
/* 169 */       SecT571Field.multiplyPrecomp(arrayOfLong6, arrayOfLong13, arrayOfLong15);
/*     */       
/* 171 */       secT571FieldElement7 = new SecT571FieldElement(arrayOfLong1);
/* 172 */       SecT571Field.multiply(arrayOfLong14, arrayOfLong15, secT571FieldElement7.x);
/*     */       
/* 174 */       if (secT571FieldElement7.isZero())
/*     */       {
/* 176 */         return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, (ECFieldElement)secT571FieldElement7, eCCurve.getB());
/*     */       }
/*     */       
/* 179 */       secT571FieldElement9 = new SecT571FieldElement(arrayOfLong3);
/* 180 */       SecT571Field.multiplyPrecomp(arrayOfLong12, arrayOfLong13, secT571FieldElement9.x);
/*     */       
/* 182 */       if (arrayOfLong8 != null)
/*     */       {
/* 184 */         SecT571Field.multiplyPrecomp(secT571FieldElement9.x, arrayOfLong8, secT571FieldElement9.x);
/*     */       }
/*     */       
/* 187 */       long[] arrayOfLong16 = Nat576.createExt64();
/*     */       
/* 189 */       SecT571Field.add(arrayOfLong15, arrayOfLong12, arrayOfLong4);
/* 190 */       SecT571Field.squareAddToExt(arrayOfLong4, arrayOfLong16);
/*     */       
/* 192 */       SecT571Field.add(secT571FieldElement3.x, secT571FieldElement4.x, arrayOfLong4);
/* 193 */       SecT571Field.multiplyAddToExt(arrayOfLong4, secT571FieldElement9.x, arrayOfLong16);
/*     */       
/* 195 */       secT571FieldElement8 = new SecT571FieldElement(arrayOfLong4);
/* 196 */       SecT571Field.reduce(arrayOfLong16, secT571FieldElement8.x);
/*     */       
/* 198 */       if (arrayOfLong5 != null)
/*     */       {
/* 200 */         SecT571Field.multiplyPrecomp(secT571FieldElement9.x, arrayOfLong5, secT571FieldElement9.x);
/*     */       }
/*     */     } 
/*     */     
/* 204 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, (ECFieldElement)secT571FieldElement7, (ECFieldElement)secT571FieldElement8, new ECFieldElement[] { (ECFieldElement)secT571FieldElement9 });
/*     */   }
/*     */   
/*     */   public ECPoint twice() {
/*     */     ECFieldElement eCFieldElement5;
/* 209 */     if (isInfinity())
/*     */     {
/* 211 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 214 */     ECCurve eCCurve = getCurve();
/*     */     
/* 216 */     ECFieldElement eCFieldElement1 = this.x;
/* 217 */     if (eCFieldElement1.isZero())
/*     */     {
/*     */       
/* 220 */       return eCCurve.getInfinity();
/*     */     }
/*     */ 
/*     */     
/* 224 */     ECFieldElement eCFieldElement2 = this.y, eCFieldElement3 = this.zs[0];
/*     */     
/* 226 */     boolean bool = eCFieldElement3.isOne();
/* 227 */     ECFieldElement eCFieldElement4 = bool ? eCFieldElement3 : eCFieldElement3.square();
/*     */     
/* 229 */     if (bool) {
/*     */       
/* 231 */       eCFieldElement5 = eCFieldElement2.square().add(eCFieldElement2);
/*     */     }
/*     */     else {
/*     */       
/* 235 */       eCFieldElement5 = eCFieldElement2.add(eCFieldElement3).multiply(eCFieldElement2);
/*     */     } 
/*     */     
/* 238 */     if (eCFieldElement5.isZero())
/*     */     {
/* 240 */       return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, eCFieldElement5, eCCurve.getB());
/*     */     }
/*     */     
/* 243 */     ECFieldElement eCFieldElement6 = eCFieldElement5.square();
/* 244 */     ECFieldElement eCFieldElement7 = bool ? eCFieldElement5 : eCFieldElement5.multiply(eCFieldElement4);
/*     */     
/* 246 */     ECFieldElement eCFieldElement8 = eCFieldElement2.add(eCFieldElement1).square();
/* 247 */     ECFieldElement eCFieldElement9 = bool ? eCFieldElement3 : eCFieldElement4.square();
/* 248 */     ECFieldElement eCFieldElement10 = eCFieldElement8.add(eCFieldElement5).add(eCFieldElement4).multiply(eCFieldElement8).add(eCFieldElement9).add(eCFieldElement6).add(eCFieldElement7);
/*     */     
/* 250 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, eCFieldElement6, eCFieldElement10, new ECFieldElement[] { eCFieldElement7 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 255 */     if (isInfinity())
/*     */     {
/* 257 */       return paramECPoint;
/*     */     }
/* 259 */     if (paramECPoint.isInfinity())
/*     */     {
/* 261 */       return twice();
/*     */     }
/*     */     
/* 264 */     ECCurve eCCurve = getCurve();
/*     */     
/* 266 */     ECFieldElement eCFieldElement1 = this.x;
/* 267 */     if (eCFieldElement1.isZero())
/*     */     {
/*     */       
/* 270 */       return paramECPoint;
/*     */     }
/*     */ 
/*     */     
/* 274 */     ECFieldElement eCFieldElement2 = paramECPoint.getRawXCoord(), eCFieldElement3 = paramECPoint.getZCoord(0);
/* 275 */     if (eCFieldElement2.isZero() || !eCFieldElement3.isOne())
/*     */     {
/* 277 */       return twice().add(paramECPoint);
/*     */     }
/*     */     
/* 280 */     ECFieldElement eCFieldElement4 = this.y, eCFieldElement5 = this.zs[0];
/* 281 */     ECFieldElement eCFieldElement6 = paramECPoint.getRawYCoord();
/*     */     
/* 283 */     ECFieldElement eCFieldElement7 = eCFieldElement1.square();
/* 284 */     ECFieldElement eCFieldElement8 = eCFieldElement4.square();
/* 285 */     ECFieldElement eCFieldElement9 = eCFieldElement5.square();
/* 286 */     ECFieldElement eCFieldElement10 = eCFieldElement4.multiply(eCFieldElement5);
/*     */     
/* 288 */     ECFieldElement eCFieldElement11 = eCFieldElement8.add(eCFieldElement10);
/* 289 */     ECFieldElement eCFieldElement12 = eCFieldElement6.addOne();
/* 290 */     ECFieldElement eCFieldElement13 = eCFieldElement12.multiply(eCFieldElement9).add(eCFieldElement8).multiplyPlusProduct(eCFieldElement11, eCFieldElement7, eCFieldElement9);
/* 291 */     ECFieldElement eCFieldElement14 = eCFieldElement2.multiply(eCFieldElement9);
/* 292 */     ECFieldElement eCFieldElement15 = eCFieldElement14.add(eCFieldElement11).square();
/*     */     
/* 294 */     if (eCFieldElement15.isZero()) {
/*     */       
/* 296 */       if (eCFieldElement13.isZero())
/*     */       {
/* 298 */         return paramECPoint.twice();
/*     */       }
/*     */       
/* 301 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 304 */     if (eCFieldElement13.isZero())
/*     */     {
/* 306 */       return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, eCFieldElement13, eCCurve.getB());
/*     */     }
/*     */     
/* 309 */     ECFieldElement eCFieldElement16 = eCFieldElement13.square().multiply(eCFieldElement14);
/* 310 */     ECFieldElement eCFieldElement17 = eCFieldElement13.multiply(eCFieldElement15).multiply(eCFieldElement9);
/* 311 */     ECFieldElement eCFieldElement18 = eCFieldElement13.add(eCFieldElement15).square().multiplyPlusProduct(eCFieldElement11, eCFieldElement12, eCFieldElement17);
/*     */     
/* 313 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(eCCurve, eCFieldElement16, eCFieldElement18, new ECFieldElement[] { eCFieldElement17 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 318 */     if (isInfinity())
/*     */     {
/* 320 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 323 */     ECFieldElement eCFieldElement1 = this.x;
/* 324 */     if (eCFieldElement1.isZero())
/*     */     {
/* 326 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 330 */     ECFieldElement eCFieldElement2 = this.y, eCFieldElement3 = this.zs[0];
/* 331 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT571K1Point(this.curve, eCFieldElement1, eCFieldElement2.add(eCFieldElement3), new ECFieldElement[] { eCFieldElement3 });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT571K1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
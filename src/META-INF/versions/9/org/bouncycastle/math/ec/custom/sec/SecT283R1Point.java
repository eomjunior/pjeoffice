/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ 
/*     */ 
/*     */ public class SecT283R1Point
/*     */   extends ECPoint.AbstractF2m
/*     */ {
/*     */   SecT283R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  13 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecT283R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  18 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  23 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getYCoord() {
/*  28 */     ECFieldElement eCFieldElement1 = this.x, eCFieldElement2 = this.y;
/*     */     
/*  30 */     if (isInfinity() || eCFieldElement1.isZero())
/*     */     {
/*  32 */       return eCFieldElement2;
/*     */     }
/*     */ 
/*     */     
/*  36 */     ECFieldElement eCFieldElement3 = eCFieldElement2.add(eCFieldElement1).multiply(eCFieldElement1);
/*     */     
/*  38 */     ECFieldElement eCFieldElement4 = this.zs[0];
/*  39 */     if (!eCFieldElement4.isOne())
/*     */     {
/*  41 */       eCFieldElement3 = eCFieldElement3.divide(eCFieldElement4);
/*     */     }
/*     */     
/*  44 */     return eCFieldElement3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean getCompressionYTilde() {
/*  49 */     ECFieldElement eCFieldElement1 = getRawXCoord();
/*  50 */     if (eCFieldElement1.isZero())
/*     */     {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     ECFieldElement eCFieldElement2 = getRawYCoord();
/*     */ 
/*     */     
/*  58 */     return (eCFieldElement2.testBitZero() != eCFieldElement1.testBitZero());
/*     */   }
/*     */   
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     ECFieldElement eCFieldElement13, eCFieldElement14, eCFieldElement15;
/*  63 */     if (isInfinity())
/*     */     {
/*  65 */       return paramECPoint;
/*     */     }
/*  67 */     if (paramECPoint.isInfinity())
/*     */     {
/*  69 */       return (ECPoint)this;
/*     */     }
/*     */     
/*  72 */     ECCurve eCCurve = getCurve();
/*     */     
/*  74 */     ECFieldElement eCFieldElement1 = this.x;
/*  75 */     ECFieldElement eCFieldElement2 = paramECPoint.getRawXCoord();
/*     */     
/*  77 */     if (eCFieldElement1.isZero()) {
/*     */       
/*  79 */       if (eCFieldElement2.isZero())
/*     */       {
/*  81 */         return eCCurve.getInfinity();
/*     */       }
/*     */       
/*  84 */       return paramECPoint.add((ECPoint)this);
/*     */     } 
/*     */     
/*  87 */     ECFieldElement eCFieldElement3 = this.y, eCFieldElement4 = this.zs[0];
/*  88 */     ECFieldElement eCFieldElement5 = paramECPoint.getRawYCoord(), eCFieldElement6 = paramECPoint.getZCoord(0);
/*     */     
/*  90 */     boolean bool1 = eCFieldElement4.isOne();
/*  91 */     ECFieldElement eCFieldElement7 = eCFieldElement2, eCFieldElement8 = eCFieldElement5;
/*  92 */     if (!bool1) {
/*     */       
/*  94 */       eCFieldElement7 = eCFieldElement7.multiply(eCFieldElement4);
/*  95 */       eCFieldElement8 = eCFieldElement8.multiply(eCFieldElement4);
/*     */     } 
/*     */     
/*  98 */     boolean bool2 = eCFieldElement6.isOne();
/*  99 */     ECFieldElement eCFieldElement9 = eCFieldElement1, eCFieldElement10 = eCFieldElement3;
/* 100 */     if (!bool2) {
/*     */       
/* 102 */       eCFieldElement9 = eCFieldElement9.multiply(eCFieldElement6);
/* 103 */       eCFieldElement10 = eCFieldElement10.multiply(eCFieldElement6);
/*     */     } 
/*     */     
/* 106 */     ECFieldElement eCFieldElement11 = eCFieldElement10.add(eCFieldElement8);
/* 107 */     ECFieldElement eCFieldElement12 = eCFieldElement9.add(eCFieldElement7);
/*     */     
/* 109 */     if (eCFieldElement12.isZero()) {
/*     */       
/* 111 */       if (eCFieldElement11.isZero())
/*     */       {
/* 113 */         return twice();
/*     */       }
/*     */       
/* 116 */       return eCCurve.getInfinity();
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if (eCFieldElement2.isZero()) {
/*     */ 
/*     */       
/* 123 */       ECPoint eCPoint = normalize();
/* 124 */       eCFieldElement1 = eCPoint.getXCoord();
/* 125 */       ECFieldElement eCFieldElement16 = eCPoint.getYCoord();
/*     */       
/* 127 */       ECFieldElement eCFieldElement17 = eCFieldElement5;
/* 128 */       ECFieldElement eCFieldElement18 = eCFieldElement16.add(eCFieldElement17).divide(eCFieldElement1);
/*     */       
/* 130 */       eCFieldElement13 = eCFieldElement18.square().add(eCFieldElement18).add(eCFieldElement1).addOne();
/* 131 */       if (eCFieldElement13.isZero())
/*     */       {
/* 133 */         return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement13, eCCurve.getB().sqrt());
/*     */       }
/*     */       
/* 136 */       ECFieldElement eCFieldElement19 = eCFieldElement18.multiply(eCFieldElement1.add(eCFieldElement13)).add(eCFieldElement13).add(eCFieldElement16);
/* 137 */       eCFieldElement14 = eCFieldElement19.divide(eCFieldElement13).add(eCFieldElement13);
/* 138 */       eCFieldElement15 = eCCurve.fromBigInteger(ECConstants.ONE);
/*     */     }
/*     */     else {
/*     */       
/* 142 */       eCFieldElement12 = eCFieldElement12.square();
/*     */       
/* 144 */       ECFieldElement eCFieldElement16 = eCFieldElement11.multiply(eCFieldElement9);
/* 145 */       ECFieldElement eCFieldElement17 = eCFieldElement11.multiply(eCFieldElement7);
/*     */       
/* 147 */       eCFieldElement13 = eCFieldElement16.multiply(eCFieldElement17);
/* 148 */       if (eCFieldElement13.isZero())
/*     */       {
/* 150 */         return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement13, eCCurve.getB().sqrt());
/*     */       }
/*     */       
/* 153 */       ECFieldElement eCFieldElement18 = eCFieldElement11.multiply(eCFieldElement12);
/* 154 */       if (!bool2)
/*     */       {
/* 156 */         eCFieldElement18 = eCFieldElement18.multiply(eCFieldElement6);
/*     */       }
/*     */       
/* 159 */       eCFieldElement14 = eCFieldElement17.add(eCFieldElement12).squarePlusProduct(eCFieldElement18, eCFieldElement3.add(eCFieldElement4));
/*     */       
/* 161 */       eCFieldElement15 = eCFieldElement18;
/* 162 */       if (!bool1)
/*     */       {
/* 164 */         eCFieldElement15 = eCFieldElement15.multiply(eCFieldElement4);
/*     */       }
/*     */     } 
/*     */     
/* 168 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement13, eCFieldElement14, new ECFieldElement[] { eCFieldElement15 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twice() {
/* 173 */     if (isInfinity())
/*     */     {
/* 175 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 178 */     ECCurve eCCurve = getCurve();
/*     */     
/* 180 */     ECFieldElement eCFieldElement1 = this.x;
/* 181 */     if (eCFieldElement1.isZero())
/*     */     {
/*     */       
/* 184 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 187 */     ECFieldElement eCFieldElement2 = this.y, eCFieldElement3 = this.zs[0];
/*     */     
/* 189 */     boolean bool = eCFieldElement3.isOne();
/* 190 */     ECFieldElement eCFieldElement4 = bool ? eCFieldElement2 : eCFieldElement2.multiply(eCFieldElement3);
/* 191 */     ECFieldElement eCFieldElement5 = bool ? eCFieldElement3 : eCFieldElement3.square();
/* 192 */     ECFieldElement eCFieldElement6 = eCFieldElement2.square().add(eCFieldElement4).add(eCFieldElement5);
/* 193 */     if (eCFieldElement6.isZero())
/*     */     {
/* 195 */       return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement6, eCCurve.getB().sqrt());
/*     */     }
/*     */     
/* 198 */     ECFieldElement eCFieldElement7 = eCFieldElement6.square();
/* 199 */     ECFieldElement eCFieldElement8 = bool ? eCFieldElement6 : eCFieldElement6.multiply(eCFieldElement5);
/*     */     
/* 201 */     ECFieldElement eCFieldElement9 = bool ? eCFieldElement1 : eCFieldElement1.multiply(eCFieldElement3);
/* 202 */     ECFieldElement eCFieldElement10 = eCFieldElement9.squarePlusProduct(eCFieldElement6, eCFieldElement4).add(eCFieldElement7).add(eCFieldElement8);
/*     */     
/* 204 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement7, eCFieldElement10, new ECFieldElement[] { eCFieldElement8 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 209 */     if (isInfinity())
/*     */     {
/* 211 */       return paramECPoint;
/*     */     }
/* 213 */     if (paramECPoint.isInfinity())
/*     */     {
/* 215 */       return twice();
/*     */     }
/*     */     
/* 218 */     ECCurve eCCurve = getCurve();
/*     */     
/* 220 */     ECFieldElement eCFieldElement1 = this.x;
/* 221 */     if (eCFieldElement1.isZero())
/*     */     {
/*     */       
/* 224 */       return paramECPoint;
/*     */     }
/*     */     
/* 227 */     ECFieldElement eCFieldElement2 = paramECPoint.getRawXCoord(), eCFieldElement3 = paramECPoint.getZCoord(0);
/* 228 */     if (eCFieldElement2.isZero() || !eCFieldElement3.isOne())
/*     */     {
/* 230 */       return twice().add(paramECPoint);
/*     */     }
/*     */     
/* 233 */     ECFieldElement eCFieldElement4 = this.y, eCFieldElement5 = this.zs[0];
/* 234 */     ECFieldElement eCFieldElement6 = paramECPoint.getRawYCoord();
/*     */     
/* 236 */     ECFieldElement eCFieldElement7 = eCFieldElement1.square();
/* 237 */     ECFieldElement eCFieldElement8 = eCFieldElement4.square();
/* 238 */     ECFieldElement eCFieldElement9 = eCFieldElement5.square();
/* 239 */     ECFieldElement eCFieldElement10 = eCFieldElement4.multiply(eCFieldElement5);
/*     */     
/* 241 */     ECFieldElement eCFieldElement11 = eCFieldElement9.add(eCFieldElement8).add(eCFieldElement10);
/* 242 */     ECFieldElement eCFieldElement12 = eCFieldElement6.multiply(eCFieldElement9).add(eCFieldElement8).multiplyPlusProduct(eCFieldElement11, eCFieldElement7, eCFieldElement9);
/* 243 */     ECFieldElement eCFieldElement13 = eCFieldElement2.multiply(eCFieldElement9);
/* 244 */     ECFieldElement eCFieldElement14 = eCFieldElement13.add(eCFieldElement11).square();
/*     */     
/* 246 */     if (eCFieldElement14.isZero()) {
/*     */       
/* 248 */       if (eCFieldElement12.isZero())
/*     */       {
/* 250 */         return paramECPoint.twice();
/*     */       }
/*     */       
/* 253 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 256 */     if (eCFieldElement12.isZero())
/*     */     {
/* 258 */       return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement12, eCCurve.getB().sqrt());
/*     */     }
/*     */     
/* 261 */     ECFieldElement eCFieldElement15 = eCFieldElement12.square().multiply(eCFieldElement13);
/* 262 */     ECFieldElement eCFieldElement16 = eCFieldElement12.multiply(eCFieldElement14).multiply(eCFieldElement9);
/* 263 */     ECFieldElement eCFieldElement17 = eCFieldElement12.add(eCFieldElement14).square().multiplyPlusProduct(eCFieldElement11, eCFieldElement6.addOne(), eCFieldElement16);
/*     */     
/* 265 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(eCCurve, eCFieldElement15, eCFieldElement17, new ECFieldElement[] { eCFieldElement16 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 270 */     if (isInfinity())
/*     */     {
/* 272 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 275 */     ECFieldElement eCFieldElement1 = this.x;
/* 276 */     if (eCFieldElement1.isZero())
/*     */     {
/* 278 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 282 */     ECFieldElement eCFieldElement2 = this.y, eCFieldElement3 = this.zs[0];
/* 283 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecT283R1Point(this.curve, eCFieldElement1, eCFieldElement2.add(eCFieldElement3), new ECFieldElement[] { eCFieldElement3 });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT283R1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.djb;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.djb.Curve25519Field;
/*     */ import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ 
/*     */ public class Curve25519Point extends ECPoint.AbstractFp {
/*     */   Curve25519Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  12 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   Curve25519Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  17 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  22 */     return (ECPoint)new org.bouncycastle.math.ec.custom.djb.Curve25519Point(null, getAffineXCoord(), getAffineYCoord());
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getZCoord(int paramInt) {
/*  27 */     if (paramInt == 1)
/*     */     {
/*  29 */       return (ECFieldElement)getJacobianModifiedW();
/*     */     }
/*     */     
/*  32 */     return super.getZCoord(paramInt);
/*     */   }
/*     */   
/*     */   public ECPoint add(ECPoint paramECPoint) {
/*     */     int[] arrayOfInt5, arrayOfInt6, arrayOfInt7, arrayOfInt8;
/*  37 */     if (isInfinity())
/*     */     {
/*  39 */       return paramECPoint;
/*     */     }
/*  41 */     if (paramECPoint.isInfinity())
/*     */     {
/*  43 */       return (ECPoint)this;
/*     */     }
/*  45 */     if (this == paramECPoint)
/*     */     {
/*  47 */       return twice();
/*     */     }
/*     */     
/*  50 */     ECCurve eCCurve = getCurve();
/*     */     
/*  52 */     Curve25519FieldElement curve25519FieldElement1 = (Curve25519FieldElement)this.x, curve25519FieldElement2 = (Curve25519FieldElement)this.y;
/*  53 */     Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement)this.zs[0];
/*  54 */     Curve25519FieldElement curve25519FieldElement4 = (Curve25519FieldElement)paramECPoint.getXCoord(), curve25519FieldElement5 = (Curve25519FieldElement)paramECPoint.getYCoord();
/*  55 */     Curve25519FieldElement curve25519FieldElement6 = (Curve25519FieldElement)paramECPoint.getZCoord(0);
/*     */ 
/*     */     
/*  58 */     int[] arrayOfInt1 = Nat256.createExt();
/*  59 */     int[] arrayOfInt2 = Nat256.create();
/*  60 */     int[] arrayOfInt3 = Nat256.create();
/*  61 */     int[] arrayOfInt4 = Nat256.create();
/*     */     
/*  63 */     boolean bool1 = curve25519FieldElement3.isOne();
/*     */     
/*  65 */     if (bool1) {
/*     */       
/*  67 */       arrayOfInt5 = curve25519FieldElement4.x;
/*  68 */       arrayOfInt6 = curve25519FieldElement5.x;
/*     */     }
/*     */     else {
/*     */       
/*  72 */       arrayOfInt6 = arrayOfInt3;
/*  73 */       Curve25519Field.square(curve25519FieldElement3.x, arrayOfInt6);
/*     */       
/*  75 */       arrayOfInt5 = arrayOfInt2;
/*  76 */       Curve25519Field.multiply(arrayOfInt6, curve25519FieldElement4.x, arrayOfInt5);
/*     */       
/*  78 */       Curve25519Field.multiply(arrayOfInt6, curve25519FieldElement3.x, arrayOfInt6);
/*  79 */       Curve25519Field.multiply(arrayOfInt6, curve25519FieldElement5.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  82 */     boolean bool2 = curve25519FieldElement6.isOne();
/*     */     
/*  84 */     if (bool2) {
/*     */       
/*  86 */       arrayOfInt7 = curve25519FieldElement1.x;
/*  87 */       arrayOfInt8 = curve25519FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  91 */       arrayOfInt8 = arrayOfInt4;
/*  92 */       Curve25519Field.square(curve25519FieldElement6.x, arrayOfInt8);
/*     */       
/*  94 */       arrayOfInt7 = arrayOfInt1;
/*  95 */       Curve25519Field.multiply(arrayOfInt8, curve25519FieldElement1.x, arrayOfInt7);
/*     */       
/*  97 */       Curve25519Field.multiply(arrayOfInt8, curve25519FieldElement6.x, arrayOfInt8);
/*  98 */       Curve25519Field.multiply(arrayOfInt8, curve25519FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/* 101 */     int[] arrayOfInt9 = Nat256.create();
/* 102 */     Curve25519Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/* 104 */     int[] arrayOfInt10 = arrayOfInt2;
/* 105 */     Curve25519Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
/*     */ 
/*     */     
/* 108 */     if (Nat256.isZero(arrayOfInt9)) {
/*     */       
/* 110 */       if (Nat256.isZero(arrayOfInt10))
/*     */       {
/*     */         
/* 113 */         return twice();
/*     */       }
/*     */ 
/*     */       
/* 117 */       return eCCurve.getInfinity();
/*     */     } 
/*     */     
/* 120 */     int[] arrayOfInt11 = Nat256.create();
/* 121 */     Curve25519Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 123 */     int[] arrayOfInt12 = Nat256.create();
/* 124 */     Curve25519Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 126 */     int[] arrayOfInt13 = arrayOfInt3;
/* 127 */     Curve25519Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 129 */     Curve25519Field.negate(arrayOfInt12, arrayOfInt12);
/* 130 */     Nat256.mul(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 132 */     int i = Nat256.addBothTo(arrayOfInt13, arrayOfInt13, arrayOfInt12);
/* 133 */     Curve25519Field.reduce27(i, arrayOfInt12);
/*     */     
/* 135 */     Curve25519FieldElement curve25519FieldElement7 = new Curve25519FieldElement(arrayOfInt4);
/* 136 */     Curve25519Field.square(arrayOfInt10, curve25519FieldElement7.x);
/* 137 */     Curve25519Field.subtract(curve25519FieldElement7.x, arrayOfInt12, curve25519FieldElement7.x);
/*     */     
/* 139 */     Curve25519FieldElement curve25519FieldElement8 = new Curve25519FieldElement(arrayOfInt12);
/* 140 */     Curve25519Field.subtract(arrayOfInt13, curve25519FieldElement7.x, curve25519FieldElement8.x);
/* 141 */     Curve25519Field.multiplyAddToExt(curve25519FieldElement8.x, arrayOfInt10, arrayOfInt1);
/* 142 */     Curve25519Field.reduce(arrayOfInt1, curve25519FieldElement8.x);
/*     */     
/* 144 */     Curve25519FieldElement curve25519FieldElement9 = new Curve25519FieldElement(arrayOfInt9);
/* 145 */     if (!bool1)
/*     */     {
/* 147 */       Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement3.x, curve25519FieldElement9.x);
/*     */     }
/* 149 */     if (!bool2)
/*     */     {
/* 151 */       Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement6.x, curve25519FieldElement9.x);
/*     */     }
/*     */     
/* 154 */     int[] arrayOfInt14 = (bool1 && bool2) ? arrayOfInt11 : null;
/*     */ 
/*     */     
/* 157 */     Curve25519FieldElement curve25519FieldElement10 = calculateJacobianModifiedW(curve25519FieldElement9, arrayOfInt14);
/*     */     
/* 159 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)curve25519FieldElement9, (ECFieldElement)curve25519FieldElement10 };
/*     */     
/* 161 */     return (ECPoint)new org.bouncycastle.math.ec.custom.djb.Curve25519Point(eCCurve, (ECFieldElement)curve25519FieldElement7, (ECFieldElement)curve25519FieldElement8, arrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twice() {
/* 166 */     if (isInfinity())
/*     */     {
/* 168 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 171 */     ECCurve eCCurve = getCurve();
/*     */     
/* 173 */     ECFieldElement eCFieldElement = this.y;
/* 174 */     if (eCFieldElement.isZero())
/*     */     {
/* 176 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 179 */     return (ECPoint)twiceJacobianModified(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 184 */     if (this == paramECPoint)
/*     */     {
/* 186 */       return threeTimes();
/*     */     }
/* 188 */     if (isInfinity())
/*     */     {
/* 190 */       return paramECPoint;
/*     */     }
/* 192 */     if (paramECPoint.isInfinity())
/*     */     {
/* 194 */       return twice();
/*     */     }
/*     */     
/* 197 */     ECFieldElement eCFieldElement = this.y;
/* 198 */     if (eCFieldElement.isZero())
/*     */     {
/* 200 */       return paramECPoint;
/*     */     }
/*     */     
/* 203 */     return twiceJacobianModified(false).add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint threeTimes() {
/* 208 */     if (isInfinity())
/*     */     {
/* 210 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 213 */     ECFieldElement eCFieldElement = this.y;
/* 214 */     if (eCFieldElement.isZero())
/*     */     {
/* 216 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 219 */     return twiceJacobianModified(false).add((ECPoint)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 224 */     if (isInfinity())
/*     */     {
/* 226 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 229 */     return (ECPoint)new org.bouncycastle.math.ec.custom.djb.Curve25519Point(getCurve(), this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Curve25519FieldElement calculateJacobianModifiedW(Curve25519FieldElement paramCurve25519FieldElement, int[] paramArrayOfint) {
/* 234 */     Curve25519FieldElement curve25519FieldElement1 = (Curve25519FieldElement)getCurve().getA();
/* 235 */     if (paramCurve25519FieldElement.isOne())
/*     */     {
/* 237 */       return curve25519FieldElement1;
/*     */     }
/*     */     
/* 240 */     Curve25519FieldElement curve25519FieldElement2 = new Curve25519FieldElement();
/* 241 */     if (paramArrayOfint == null) {
/*     */       
/* 243 */       paramArrayOfint = curve25519FieldElement2.x;
/* 244 */       Curve25519Field.square(paramCurve25519FieldElement.x, paramArrayOfint);
/*     */     } 
/* 246 */     Curve25519Field.square(paramArrayOfint, curve25519FieldElement2.x);
/* 247 */     Curve25519Field.multiply(curve25519FieldElement2.x, curve25519FieldElement1.x, curve25519FieldElement2.x);
/* 248 */     return curve25519FieldElement2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Curve25519FieldElement getJacobianModifiedW() {
/* 253 */     Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement)this.zs[1];
/* 254 */     if (curve25519FieldElement == null)
/*     */     {
/*     */       
/* 257 */       this.zs[1] = (ECFieldElement)(curve25519FieldElement = calculateJacobianModifiedW((Curve25519FieldElement)this.zs[0], null));
/*     */     }
/* 259 */     return curve25519FieldElement;
/*     */   }
/*     */ 
/*     */   
/*     */   protected org.bouncycastle.math.ec.custom.djb.Curve25519Point twiceJacobianModified(boolean paramBoolean) {
/* 264 */     Curve25519FieldElement curve25519FieldElement1 = (Curve25519FieldElement)this.x, curve25519FieldElement2 = (Curve25519FieldElement)this.y;
/* 265 */     Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement)this.zs[0], curve25519FieldElement4 = getJacobianModifiedW();
/*     */ 
/*     */ 
/*     */     
/* 269 */     int[] arrayOfInt1 = Nat256.create();
/* 270 */     Curve25519Field.square(curve25519FieldElement1.x, arrayOfInt1);
/* 271 */     int i = Nat256.addBothTo(arrayOfInt1, arrayOfInt1, arrayOfInt1);
/* 272 */     i += Nat256.addTo(curve25519FieldElement4.x, arrayOfInt1);
/* 273 */     Curve25519Field.reduce27(i, arrayOfInt1);
/*     */     
/* 275 */     int[] arrayOfInt2 = Nat256.create();
/* 276 */     Curve25519Field.twice(curve25519FieldElement2.x, arrayOfInt2);
/*     */     
/* 278 */     int[] arrayOfInt3 = Nat256.create();
/* 279 */     Curve25519Field.multiply(arrayOfInt2, curve25519FieldElement2.x, arrayOfInt3);
/*     */     
/* 281 */     int[] arrayOfInt4 = Nat256.create();
/* 282 */     Curve25519Field.multiply(arrayOfInt3, curve25519FieldElement1.x, arrayOfInt4);
/* 283 */     Curve25519Field.twice(arrayOfInt4, arrayOfInt4);
/*     */     
/* 285 */     int[] arrayOfInt5 = Nat256.create();
/* 286 */     Curve25519Field.square(arrayOfInt3, arrayOfInt5);
/* 287 */     Curve25519Field.twice(arrayOfInt5, arrayOfInt5);
/*     */     
/* 289 */     Curve25519FieldElement curve25519FieldElement5 = new Curve25519FieldElement(arrayOfInt3);
/* 290 */     Curve25519Field.square(arrayOfInt1, curve25519FieldElement5.x);
/* 291 */     Curve25519Field.subtract(curve25519FieldElement5.x, arrayOfInt4, curve25519FieldElement5.x);
/* 292 */     Curve25519Field.subtract(curve25519FieldElement5.x, arrayOfInt4, curve25519FieldElement5.x);
/*     */     
/* 294 */     Curve25519FieldElement curve25519FieldElement6 = new Curve25519FieldElement(arrayOfInt4);
/* 295 */     Curve25519Field.subtract(arrayOfInt4, curve25519FieldElement5.x, curve25519FieldElement6.x);
/* 296 */     Curve25519Field.multiply(curve25519FieldElement6.x, arrayOfInt1, curve25519FieldElement6.x);
/* 297 */     Curve25519Field.subtract(curve25519FieldElement6.x, arrayOfInt5, curve25519FieldElement6.x);
/*     */     
/* 299 */     Curve25519FieldElement curve25519FieldElement7 = new Curve25519FieldElement(arrayOfInt2);
/* 300 */     if (!Nat256.isOne(curve25519FieldElement3.x))
/*     */     {
/* 302 */       Curve25519Field.multiply(curve25519FieldElement7.x, curve25519FieldElement3.x, curve25519FieldElement7.x);
/*     */     }
/*     */     
/* 305 */     Curve25519FieldElement curve25519FieldElement8 = null;
/* 306 */     if (paramBoolean) {
/*     */       
/* 308 */       curve25519FieldElement8 = new Curve25519FieldElement(arrayOfInt5);
/* 309 */       Curve25519Field.multiply(curve25519FieldElement8.x, curve25519FieldElement4.x, curve25519FieldElement8.x);
/* 310 */       Curve25519Field.twice(curve25519FieldElement8.x, curve25519FieldElement8.x);
/*     */     } 
/*     */     
/* 313 */     return new org.bouncycastle.math.ec.custom.djb.Curve25519Point(getCurve(), (ECFieldElement)curve25519FieldElement5, (ECFieldElement)curve25519FieldElement6, new ECFieldElement[] { (ECFieldElement)curve25519FieldElement7, (ECFieldElement)curve25519FieldElement8 });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/djb/Curve25519Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
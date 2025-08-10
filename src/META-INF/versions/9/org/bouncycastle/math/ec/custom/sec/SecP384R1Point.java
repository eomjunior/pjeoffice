/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP384R1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat384;
/*     */ 
/*     */ public class SecP384R1Point extends ECPoint.AbstractFp {
/*     */   SecP384R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  13 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   SecP384R1Point(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  18 */     super(paramECCurve, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint detach() {
/*  23 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP384R1Point(null, getAffineXCoord(), getAffineYCoord());
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
/*  43 */     SecP384R1FieldElement secP384R1FieldElement1 = (SecP384R1FieldElement)this.x, secP384R1FieldElement2 = (SecP384R1FieldElement)this.y;
/*  44 */     SecP384R1FieldElement secP384R1FieldElement3 = (SecP384R1FieldElement)paramECPoint.getXCoord(), secP384R1FieldElement4 = (SecP384R1FieldElement)paramECPoint.getYCoord();
/*     */     
/*  46 */     SecP384R1FieldElement secP384R1FieldElement5 = (SecP384R1FieldElement)this.zs[0];
/*  47 */     SecP384R1FieldElement secP384R1FieldElement6 = (SecP384R1FieldElement)paramECPoint.getZCoord(0);
/*     */ 
/*     */     
/*  50 */     int[] arrayOfInt1 = Nat.create(24);
/*  51 */     int[] arrayOfInt2 = Nat.create(24);
/*  52 */     int[] arrayOfInt3 = Nat.create(12);
/*  53 */     int[] arrayOfInt4 = Nat.create(12);
/*     */     
/*  55 */     boolean bool1 = secP384R1FieldElement5.isOne();
/*     */     
/*  57 */     if (bool1) {
/*     */       
/*  59 */       arrayOfInt5 = secP384R1FieldElement3.x;
/*  60 */       arrayOfInt6 = secP384R1FieldElement4.x;
/*     */     }
/*     */     else {
/*     */       
/*  64 */       arrayOfInt6 = arrayOfInt3;
/*  65 */       SecP384R1Field.square(secP384R1FieldElement5.x, arrayOfInt6);
/*     */       
/*  67 */       arrayOfInt5 = arrayOfInt2;
/*  68 */       SecP384R1Field.multiply(arrayOfInt6, secP384R1FieldElement3.x, arrayOfInt5);
/*     */       
/*  70 */       SecP384R1Field.multiply(arrayOfInt6, secP384R1FieldElement5.x, arrayOfInt6);
/*  71 */       SecP384R1Field.multiply(arrayOfInt6, secP384R1FieldElement4.x, arrayOfInt6);
/*     */     } 
/*     */     
/*  74 */     boolean bool2 = secP384R1FieldElement6.isOne();
/*     */     
/*  76 */     if (bool2) {
/*     */       
/*  78 */       arrayOfInt7 = secP384R1FieldElement1.x;
/*  79 */       arrayOfInt8 = secP384R1FieldElement2.x;
/*     */     }
/*     */     else {
/*     */       
/*  83 */       arrayOfInt8 = arrayOfInt4;
/*  84 */       SecP384R1Field.square(secP384R1FieldElement6.x, arrayOfInt8);
/*     */       
/*  86 */       arrayOfInt7 = arrayOfInt1;
/*  87 */       SecP384R1Field.multiply(arrayOfInt8, secP384R1FieldElement1.x, arrayOfInt7);
/*     */       
/*  89 */       SecP384R1Field.multiply(arrayOfInt8, secP384R1FieldElement6.x, arrayOfInt8);
/*  90 */       SecP384R1Field.multiply(arrayOfInt8, secP384R1FieldElement2.x, arrayOfInt8);
/*     */     } 
/*     */     
/*  93 */     int[] arrayOfInt9 = Nat.create(12);
/*  94 */     SecP384R1Field.subtract(arrayOfInt7, arrayOfInt5, arrayOfInt9);
/*     */     
/*  96 */     int[] arrayOfInt10 = Nat.create(12);
/*  97 */     SecP384R1Field.subtract(arrayOfInt8, arrayOfInt6, arrayOfInt10);
/*     */ 
/*     */     
/* 100 */     if (Nat.isZero(12, arrayOfInt9)) {
/*     */       
/* 102 */       if (Nat.isZero(12, arrayOfInt10))
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
/* 113 */     SecP384R1Field.square(arrayOfInt9, arrayOfInt11);
/*     */     
/* 115 */     int[] arrayOfInt12 = Nat.create(12);
/* 116 */     SecP384R1Field.multiply(arrayOfInt11, arrayOfInt9, arrayOfInt12);
/*     */     
/* 118 */     int[] arrayOfInt13 = arrayOfInt3;
/* 119 */     SecP384R1Field.multiply(arrayOfInt11, arrayOfInt7, arrayOfInt13);
/*     */     
/* 121 */     SecP384R1Field.negate(arrayOfInt12, arrayOfInt12);
/* 122 */     Nat384.mul(arrayOfInt8, arrayOfInt12, arrayOfInt1);
/*     */     
/* 124 */     int i = Nat.addBothTo(12, arrayOfInt13, arrayOfInt13, arrayOfInt12);
/* 125 */     SecP384R1Field.reduce32(i, arrayOfInt12);
/*     */     
/* 127 */     SecP384R1FieldElement secP384R1FieldElement7 = new SecP384R1FieldElement(arrayOfInt4);
/* 128 */     SecP384R1Field.square(arrayOfInt10, secP384R1FieldElement7.x);
/* 129 */     SecP384R1Field.subtract(secP384R1FieldElement7.x, arrayOfInt12, secP384R1FieldElement7.x);
/*     */     
/* 131 */     SecP384R1FieldElement secP384R1FieldElement8 = new SecP384R1FieldElement(arrayOfInt12);
/* 132 */     SecP384R1Field.subtract(arrayOfInt13, secP384R1FieldElement7.x, secP384R1FieldElement8.x);
/* 133 */     Nat384.mul(secP384R1FieldElement8.x, arrayOfInt10, arrayOfInt2);
/* 134 */     SecP384R1Field.addExt(arrayOfInt1, arrayOfInt2, arrayOfInt1);
/* 135 */     SecP384R1Field.reduce(arrayOfInt1, secP384R1FieldElement8.x);
/*     */     
/* 137 */     SecP384R1FieldElement secP384R1FieldElement9 = new SecP384R1FieldElement(arrayOfInt9);
/* 138 */     if (!bool1)
/*     */     {
/* 140 */       SecP384R1Field.multiply(secP384R1FieldElement9.x, secP384R1FieldElement5.x, secP384R1FieldElement9.x);
/*     */     }
/* 142 */     if (!bool2)
/*     */     {
/* 144 */       SecP384R1Field.multiply(secP384R1FieldElement9.x, secP384R1FieldElement6.x, secP384R1FieldElement9.x);
/*     */     }
/*     */     
/* 147 */     ECFieldElement[] arrayOfECFieldElement = { (ECFieldElement)secP384R1FieldElement9 };
/*     */     
/* 149 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP384R1Point(eCCurve, (ECFieldElement)secP384R1FieldElement7, (ECFieldElement)secP384R1FieldElement8, arrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twice() {
/* 154 */     if (isInfinity())
/*     */     {
/* 156 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 159 */     ECCurve eCCurve = getCurve();
/*     */     
/* 161 */     SecP384R1FieldElement secP384R1FieldElement1 = (SecP384R1FieldElement)this.y;
/* 162 */     if (secP384R1FieldElement1.isZero())
/*     */     {
/* 164 */       return eCCurve.getInfinity();
/*     */     }
/*     */     
/* 167 */     SecP384R1FieldElement secP384R1FieldElement2 = (SecP384R1FieldElement)this.x, secP384R1FieldElement3 = (SecP384R1FieldElement)this.zs[0];
/*     */ 
/*     */     
/* 170 */     int[] arrayOfInt1 = Nat.create(12);
/* 171 */     int[] arrayOfInt2 = Nat.create(12);
/*     */     
/* 173 */     int[] arrayOfInt3 = Nat.create(12);
/* 174 */     SecP384R1Field.square(secP384R1FieldElement1.x, arrayOfInt3);
/*     */     
/* 176 */     int[] arrayOfInt4 = Nat.create(12);
/* 177 */     SecP384R1Field.square(arrayOfInt3, arrayOfInt4);
/*     */     
/* 179 */     boolean bool = secP384R1FieldElement3.isOne();
/*     */     
/* 181 */     int[] arrayOfInt5 = secP384R1FieldElement3.x;
/* 182 */     if (!bool) {
/*     */       
/* 184 */       arrayOfInt5 = arrayOfInt2;
/* 185 */       SecP384R1Field.square(secP384R1FieldElement3.x, arrayOfInt5);
/*     */     } 
/*     */     
/* 188 */     SecP384R1Field.subtract(secP384R1FieldElement2.x, arrayOfInt5, arrayOfInt1);
/*     */     
/* 190 */     int[] arrayOfInt6 = arrayOfInt2;
/* 191 */     SecP384R1Field.add(secP384R1FieldElement2.x, arrayOfInt5, arrayOfInt6);
/* 192 */     SecP384R1Field.multiply(arrayOfInt6, arrayOfInt1, arrayOfInt6);
/* 193 */     int i = Nat.addBothTo(12, arrayOfInt6, arrayOfInt6, arrayOfInt6);
/* 194 */     SecP384R1Field.reduce32(i, arrayOfInt6);
/*     */     
/* 196 */     int[] arrayOfInt7 = arrayOfInt3;
/* 197 */     SecP384R1Field.multiply(arrayOfInt3, secP384R1FieldElement2.x, arrayOfInt7);
/* 198 */     i = Nat.shiftUpBits(12, arrayOfInt7, 2, 0);
/* 199 */     SecP384R1Field.reduce32(i, arrayOfInt7);
/*     */     
/* 201 */     i = Nat.shiftUpBits(12, arrayOfInt4, 3, 0, arrayOfInt1);
/* 202 */     SecP384R1Field.reduce32(i, arrayOfInt1);
/*     */     
/* 204 */     SecP384R1FieldElement secP384R1FieldElement4 = new SecP384R1FieldElement(arrayOfInt4);
/* 205 */     SecP384R1Field.square(arrayOfInt6, secP384R1FieldElement4.x);
/* 206 */     SecP384R1Field.subtract(secP384R1FieldElement4.x, arrayOfInt7, secP384R1FieldElement4.x);
/* 207 */     SecP384R1Field.subtract(secP384R1FieldElement4.x, arrayOfInt7, secP384R1FieldElement4.x);
/*     */     
/* 209 */     SecP384R1FieldElement secP384R1FieldElement5 = new SecP384R1FieldElement(arrayOfInt7);
/* 210 */     SecP384R1Field.subtract(arrayOfInt7, secP384R1FieldElement4.x, secP384R1FieldElement5.x);
/* 211 */     SecP384R1Field.multiply(secP384R1FieldElement5.x, arrayOfInt6, secP384R1FieldElement5.x);
/* 212 */     SecP384R1Field.subtract(secP384R1FieldElement5.x, arrayOfInt1, secP384R1FieldElement5.x);
/*     */     
/* 214 */     SecP384R1FieldElement secP384R1FieldElement6 = new SecP384R1FieldElement(arrayOfInt6);
/* 215 */     SecP384R1Field.twice(secP384R1FieldElement1.x, secP384R1FieldElement6.x);
/* 216 */     if (!bool)
/*     */     {
/* 218 */       SecP384R1Field.multiply(secP384R1FieldElement6.x, secP384R1FieldElement3.x, secP384R1FieldElement6.x);
/*     */     }
/*     */     
/* 221 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP384R1Point(eCCurve, (ECFieldElement)secP384R1FieldElement4, (ECFieldElement)secP384R1FieldElement5, new ECFieldElement[] { (ECFieldElement)secP384R1FieldElement6 });
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint twicePlus(ECPoint paramECPoint) {
/* 226 */     if (this == paramECPoint)
/*     */     {
/* 228 */       return threeTimes();
/*     */     }
/* 230 */     if (isInfinity())
/*     */     {
/* 232 */       return paramECPoint;
/*     */     }
/* 234 */     if (paramECPoint.isInfinity())
/*     */     {
/* 236 */       return twice();
/*     */     }
/*     */     
/* 239 */     ECFieldElement eCFieldElement = this.y;
/* 240 */     if (eCFieldElement.isZero())
/*     */     {
/* 242 */       return paramECPoint;
/*     */     }
/*     */     
/* 245 */     return twice().add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint threeTimes() {
/* 250 */     if (isInfinity() || this.y.isZero())
/*     */     {
/* 252 */       return (ECPoint)this;
/*     */     }
/*     */ 
/*     */     
/* 256 */     return twice().add((ECPoint)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint negate() {
/* 261 */     if (isInfinity())
/*     */     {
/* 263 */       return (ECPoint)this;
/*     */     }
/*     */     
/* 266 */     return (ECPoint)new org.bouncycastle.math.ec.custom.sec.SecP384R1Point(this.curve, this.x, this.y.negate(), this.zs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP384R1Point.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
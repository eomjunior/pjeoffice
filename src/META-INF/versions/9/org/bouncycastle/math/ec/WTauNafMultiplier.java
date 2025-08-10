/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.AbstractECMultiplier;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.PreCompCallback;
/*     */ import org.bouncycastle.math.ec.Tnaf;
/*     */ import org.bouncycastle.math.ec.WTauNafPreCompInfo;
/*     */ import org.bouncycastle.math.ec.ZTauElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WTauNafMultiplier
/*     */   extends AbstractECMultiplier
/*     */ {
/*     */   static final String PRECOMP_NAME = "bc_wtnaf";
/*     */   
/*     */   protected ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger) {
/*  24 */     if (!(paramECPoint instanceof ECPoint.AbstractF2m))
/*     */     {
/*  26 */       throw new IllegalArgumentException("Only ECPoint.AbstractF2m can be used in WTauNafMultiplier");
/*     */     }
/*     */ 
/*     */     
/*  30 */     ECPoint.AbstractF2m abstractF2m = (ECPoint.AbstractF2m)paramECPoint;
/*  31 */     ECCurve.AbstractF2m abstractF2m1 = (ECCurve.AbstractF2m)abstractF2m.getCurve();
/*  32 */     int i = abstractF2m1.getFieldSize();
/*  33 */     byte b1 = abstractF2m1.getA().toBigInteger().byteValue();
/*  34 */     byte b2 = Tnaf.getMu(b1);
/*  35 */     BigInteger[] arrayOfBigInteger = abstractF2m1.getSi();
/*     */     
/*  37 */     ZTauElement zTauElement = Tnaf.partModReduction(paramBigInteger, i, b1, arrayOfBigInteger, b2, (byte)10);
/*     */     
/*  39 */     return (ECPoint)multiplyWTnaf(abstractF2m, zTauElement, b1, b2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ECPoint.AbstractF2m multiplyWTnaf(ECPoint.AbstractF2m paramAbstractF2m, ZTauElement paramZTauElement, byte paramByte1, byte paramByte2) {
/*  54 */     ZTauElement[] arrayOfZTauElement = (paramByte1 == 0) ? Tnaf.alpha0 : Tnaf.alpha1;
/*     */     
/*  56 */     BigInteger bigInteger = Tnaf.getTw(paramByte2, 4);
/*     */     
/*  58 */     byte[] arrayOfByte = Tnaf.tauAdicWNaf(paramByte2, paramZTauElement, (byte)4, 
/*  59 */         BigInteger.valueOf(16L), bigInteger, arrayOfZTauElement);
/*     */     
/*  61 */     return multiplyFromWTnaf(paramAbstractF2m, arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ECPoint.AbstractF2m multiplyFromWTnaf(ECPoint.AbstractF2m paramAbstractF2m, byte[] paramArrayOfbyte) {
/*  75 */     ECCurve.AbstractF2m abstractF2m = (ECCurve.AbstractF2m)paramAbstractF2m.getCurve();
/*  76 */     byte b = abstractF2m.getA().toBigInteger().byteValue();
/*     */     
/*  78 */     WTauNafPreCompInfo wTauNafPreCompInfo = (WTauNafPreCompInfo)abstractF2m.precompute((ECPoint)paramAbstractF2m, "bc_wtnaf", (PreCompCallback)new Object(paramAbstractF2m, b));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     ECPoint.AbstractF2m[] arrayOfAbstractF2m1 = wTauNafPreCompInfo.getPreComp();
/*     */ 
/*     */     
/*  96 */     ECPoint.AbstractF2m[] arrayOfAbstractF2m2 = new ECPoint.AbstractF2m[arrayOfAbstractF2m1.length];
/*  97 */     for (byte b1 = 0; b1 < arrayOfAbstractF2m1.length; b1++)
/*     */     {
/*  99 */       arrayOfAbstractF2m2[b1] = (ECPoint.AbstractF2m)arrayOfAbstractF2m1[b1].negate();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 104 */     ECPoint.AbstractF2m abstractF2m1 = (ECPoint.AbstractF2m)paramAbstractF2m.getCurve().getInfinity();
/*     */     
/* 106 */     byte b2 = 0;
/* 107 */     for (int i = paramArrayOfbyte.length - 1; i >= 0; i--) {
/*     */       
/* 109 */       b2++;
/* 110 */       byte b3 = paramArrayOfbyte[i];
/* 111 */       if (b3 != 0) {
/*     */         
/* 113 */         abstractF2m1 = abstractF2m1.tauPow(b2);
/* 114 */         b2 = 0;
/*     */         
/* 116 */         ECPoint.AbstractF2m abstractF2m2 = (b3 > 0) ? arrayOfAbstractF2m1[b3 >>> 1] : arrayOfAbstractF2m2[-b3 >>> 1];
/* 117 */         abstractF2m1 = (ECPoint.AbstractF2m)abstractF2m1.add((ECPoint)abstractF2m2);
/*     */       } 
/*     */     } 
/* 120 */     if (b2 > 0)
/*     */     {
/* 122 */       abstractF2m1 = abstractF2m1.tauPow(b2);
/*     */     }
/* 124 */     return abstractF2m1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/WTauNafMultiplier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
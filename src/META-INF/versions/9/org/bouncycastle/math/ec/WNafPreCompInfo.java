/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ 
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.PreCompInfo;
/*     */ 
/*     */ public class WNafPreCompInfo
/*     */   implements PreCompInfo
/*     */ {
/*   9 */   volatile int promotionCountdown = 4;
/*     */   
/*  11 */   protected int confWidth = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  17 */   protected ECPoint[] preComp = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   protected ECPoint[] preCompNeg = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   protected ECPoint twice = null;
/*     */   
/*  31 */   protected int width = -1;
/*     */ 
/*     */   
/*     */   int decrementPromotionCountdown() {
/*  35 */     int i = this.promotionCountdown;
/*  36 */     if (i > 0)
/*     */     {
/*  38 */       this.promotionCountdown = --i;
/*     */     }
/*  40 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   int getPromotionCountdown() {
/*  45 */     return this.promotionCountdown;
/*     */   }
/*     */ 
/*     */   
/*     */   void setPromotionCountdown(int paramInt) {
/*  50 */     this.promotionCountdown = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPromoted() {
/*  55 */     return (this.promotionCountdown <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConfWidth() {
/*  60 */     return this.confWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConfWidth(int paramInt) {
/*  65 */     this.confWidth = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint[] getPreComp() {
/*  70 */     return this.preComp;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPreComp(ECPoint[] paramArrayOfECPoint) {
/*  75 */     this.preComp = paramArrayOfECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint[] getPreCompNeg() {
/*  80 */     return this.preCompNeg;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPreCompNeg(ECPoint[] paramArrayOfECPoint) {
/*  85 */     this.preCompNeg = paramArrayOfECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getTwice() {
/*  90 */     return this.twice;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTwice(ECPoint paramECPoint) {
/*  95 */     this.twice = paramECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 100 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWidth(int paramInt) {
/* 105 */     this.width = paramInt;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/WNafPreCompInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
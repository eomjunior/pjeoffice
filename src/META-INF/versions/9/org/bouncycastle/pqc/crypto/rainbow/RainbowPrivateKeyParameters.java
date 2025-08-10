/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.rainbow;
/*     */ 
/*     */ import org.bouncycastle.pqc.crypto.rainbow.Layer;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RainbowPrivateKeyParameters
/*     */   extends RainbowKeyParameters
/*     */ {
/*     */   private short[][] A1inv;
/*     */   private short[] b1;
/*     */   private short[][] A2inv;
/*     */   private short[] b2;
/*     */   private int[] vi;
/*     */   private Layer[] layers;
/*     */   
/*     */   public RainbowPrivateKeyParameters(short[][] paramArrayOfshort1, short[] paramArrayOfshort2, short[][] paramArrayOfshort3, short[] paramArrayOfshort4, int[] paramArrayOfint, Layer[] paramArrayOfLayer) {
/*  21 */     super(true, paramArrayOfint[paramArrayOfint.length - 1] - paramArrayOfint[0]);
/*     */     
/*  23 */     this.A1inv = paramArrayOfshort1;
/*  24 */     this.b1 = paramArrayOfshort2;
/*  25 */     this.A2inv = paramArrayOfshort3;
/*  26 */     this.b2 = paramArrayOfshort4;
/*  27 */     this.vi = paramArrayOfint;
/*  28 */     this.layers = paramArrayOfLayer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB1() {
/*  65 */     return this.b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA1() {
/*  75 */     return this.A1inv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB2() {
/*  85 */     return this.b2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA2() {
/*  95 */     return this.A2inv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layer[] getLayers() {
/* 105 */     return this.layers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVi() {
/* 115 */     return this.vi;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/rainbow/RainbowPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
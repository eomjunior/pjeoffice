/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.spec;
/*     */ 
/*     */ import java.security.spec.KeySpec;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.Layer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RainbowPrivateKeySpec
/*     */   implements KeySpec
/*     */ {
/*     */   private short[][] A1inv;
/*     */   private short[] b1;
/*     */   private short[][] A2inv;
/*     */   private short[] b2;
/*     */   private int[] vi;
/*     */   private Layer[] layers;
/*     */   
/*     */   public RainbowPrivateKeySpec(short[][] paramArrayOfshort1, short[] paramArrayOfshort2, short[][] paramArrayOfshort3, short[] paramArrayOfshort4, int[] paramArrayOfint, Layer[] paramArrayOfLayer) {
/*  57 */     this.A1inv = paramArrayOfshort1;
/*  58 */     this.b1 = paramArrayOfshort2;
/*  59 */     this.A2inv = paramArrayOfshort3;
/*  60 */     this.b2 = paramArrayOfshort4;
/*  61 */     this.vi = paramArrayOfint;
/*  62 */     this.layers = paramArrayOfLayer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB1() {
/*  72 */     return this.b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA1() {
/*  82 */     return this.A1inv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB2() {
/*  92 */     return this.b2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA2() {
/* 102 */     return this.A2inv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layer[] getLayers() {
/* 112 */     return this.layers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVi() {
/* 122 */     return this.vi;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/spec/RainbowPrivateKeySpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
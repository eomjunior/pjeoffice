/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.rainbow;
/*     */ 
/*     */ import org.bouncycastle.crypto.CipherParameters;
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
/*     */ public class RainbowParameters
/*     */   implements CipherParameters
/*     */ {
/*  25 */   private final int[] DEFAULT_VI = new int[] { 6, 12, 17, 22, 33 };
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] vi;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RainbowParameters() {
/*  35 */     this.vi = this.DEFAULT_VI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RainbowParameters(int[] paramArrayOfint) {
/*  46 */     this.vi = paramArrayOfint;
/*     */     
/*  48 */     checkParams();
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkParams() {
/*  53 */     if (this.vi == null)
/*     */     {
/*  55 */       throw new IllegalArgumentException("no layers defined.");
/*     */     }
/*  57 */     if (this.vi.length > 1) {
/*     */       
/*  59 */       for (byte b = 0; b < this.vi.length - 1; b++)
/*     */       {
/*  61 */         if (this.vi[b] >= this.vi[b + 1])
/*     */         {
/*  63 */           throw new IllegalArgumentException("v[i] has to be smaller than v[i+1]");
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  70 */       throw new IllegalArgumentException("Rainbow needs at least 1 layer, such that v1 < v2.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumOfLayers() {
/*  82 */     return this.vi.length - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDocLength() {
/*  92 */     return this.vi[this.vi.length - 1] - this.vi[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVi() {
/* 102 */     return this.vi;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/rainbow/RainbowParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
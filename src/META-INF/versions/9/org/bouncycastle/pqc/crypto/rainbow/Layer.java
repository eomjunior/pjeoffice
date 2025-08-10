/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.rainbow;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ public class Layer
/*     */ {
/*     */   private int vi;
/*     */   private int viNext;
/*     */   private int oi;
/*     */   private short[][][] coeff_alpha;
/*     */   private short[][][] coeff_beta;
/*     */   private short[][] coeff_gamma;
/*     */   private short[] coeff_eta;
/*     */   
/*     */   public Layer(byte paramByte1, byte paramByte2, short[][][] paramArrayOfshort1, short[][][] paramArrayOfshort2, short[][] paramArrayOfshort, short[] paramArrayOfshort3) {
/*  55 */     this.vi = paramByte1 & 0xFF;
/*  56 */     this.viNext = paramByte2 & 0xFF;
/*  57 */     this.oi = this.viNext - this.vi;
/*     */ 
/*     */     
/*  60 */     this.coeff_alpha = paramArrayOfshort1;
/*  61 */     this.coeff_beta = paramArrayOfshort2;
/*  62 */     this.coeff_gamma = paramArrayOfshort;
/*  63 */     this.coeff_eta = paramArrayOfshort3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layer(int paramInt1, int paramInt2, SecureRandom paramSecureRandom) {
/*  74 */     this.vi = paramInt1;
/*  75 */     this.viNext = paramInt2;
/*  76 */     this.oi = paramInt2 - paramInt1;
/*     */ 
/*     */     
/*  79 */     this.coeff_alpha = new short[this.oi][this.oi][this.vi];
/*  80 */     this.coeff_beta = new short[this.oi][this.vi][this.vi];
/*  81 */     this.coeff_gamma = new short[this.oi][this.viNext];
/*  82 */     this.coeff_eta = new short[this.oi];
/*     */     
/*  84 */     int i = this.oi;
/*     */     
/*     */     byte b;
/*  87 */     for (b = 0; b < i; b++) {
/*     */       
/*  89 */       for (byte b1 = 0; b1 < this.oi; b1++) {
/*     */         
/*  91 */         for (byte b2 = 0; b2 < this.vi; b2++)
/*     */         {
/*  93 */           this.coeff_alpha[b][b1][b2] = (short)(paramSecureRandom.nextInt() & 0xFF);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     for (b = 0; b < i; b++) {
/*     */       
/* 100 */       for (byte b1 = 0; b1 < this.vi; b1++) {
/*     */         
/* 102 */         for (byte b2 = 0; b2 < this.vi; b2++)
/*     */         {
/* 104 */           this.coeff_beta[b][b1][b2] = (short)(paramSecureRandom.nextInt() & 0xFF);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     for (b = 0; b < i; b++) {
/*     */       
/* 111 */       for (byte b1 = 0; b1 < this.viNext; b1++)
/*     */       {
/* 113 */         this.coeff_gamma[b][b1] = (short)(paramSecureRandom.nextInt() & 0xFF);
/*     */       }
/*     */     } 
/*     */     
/* 117 */     for (b = 0; b < i; b++)
/*     */     {
/* 119 */       this.coeff_eta[b] = (short)(paramSecureRandom.nextInt() & 0xFF);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] plugInVinegars(short[] paramArrayOfshort) {
/* 138 */     short s = 0;
/*     */     
/* 140 */     short[][] arrayOfShort = new short[this.oi][this.oi + 1];
/*     */     
/* 142 */     short[] arrayOfShort1 = new short[this.oi];
/*     */ 
/*     */     
/*     */     byte b;
/*     */ 
/*     */     
/* 148 */     for (b = 0; b < this.oi; b++) {
/*     */       
/* 150 */       for (byte b1 = 0; b1 < this.vi; b1++) {
/*     */         
/* 152 */         for (byte b2 = 0; b2 < this.vi; b2++) {
/*     */ 
/*     */           
/* 155 */           s = GF2Field.multElem(this.coeff_beta[b][b1][b2], paramArrayOfshort[b1]);
/*     */           
/* 157 */           s = GF2Field.multElem(s, paramArrayOfshort[b2]);
/*     */           
/* 159 */           arrayOfShort1[b] = GF2Field.addElem(arrayOfShort1[b], s);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 165 */     for (b = 0; b < this.oi; b++) {
/*     */       
/* 167 */       for (byte b1 = 0; b1 < this.oi; b1++) {
/*     */         
/* 169 */         for (byte b2 = 0; b2 < this.vi; b2++) {
/*     */ 
/*     */           
/* 172 */           s = GF2Field.multElem(this.coeff_alpha[b][b1][b2], paramArrayOfshort[b2]);
/*     */           
/* 174 */           arrayOfShort[b][b1] = GF2Field.addElem(arrayOfShort[b][b1], s);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     for (b = 0; b < this.oi; b++) {
/*     */       
/* 181 */       for (byte b1 = 0; b1 < this.vi; b1++) {
/*     */ 
/*     */         
/* 184 */         s = GF2Field.multElem(this.coeff_gamma[b][b1], paramArrayOfshort[b1]);
/*     */ 
/*     */         
/* 187 */         arrayOfShort1[b] = GF2Field.addElem(arrayOfShort1[b], s);
/*     */       } 
/*     */     } 
/*     */     
/* 191 */     for (b = 0; b < this.oi; b++) {
/*     */       
/* 193 */       for (int i = this.vi; i < this.viNext; i++)
/*     */       {
/*     */ 
/*     */         
/* 197 */         arrayOfShort[b][i - this.vi] = GF2Field.addElem(this.coeff_gamma[b][i], arrayOfShort[b][i - this.vi]);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 202 */     for (b = 0; b < this.oi; b++)
/*     */     {
/*     */       
/* 205 */       arrayOfShort1[b] = GF2Field.addElem(arrayOfShort1[b], this.coeff_eta[b]);
/*     */     }
/*     */ 
/*     */     
/* 209 */     for (b = 0; b < this.oi; b++)
/*     */     {
/* 211 */       arrayOfShort[b][this.oi] = arrayOfShort1[b];
/*     */     }
/* 213 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVi() {
/* 223 */     return this.vi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getViNext() {
/* 233 */     return this.viNext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOi() {
/* 243 */     return this.oi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][][] getCoeffAlpha() {
/* 253 */     return this.coeff_alpha;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][][] getCoeffBeta() {
/* 264 */     return this.coeff_beta;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getCoeffGamma() {
/* 274 */     return this.coeff_gamma;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getCoeffEta() {
/* 284 */     return this.coeff_eta;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 295 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.crypto.rainbow.Layer))
/*     */     {
/* 297 */       return false;
/*     */     }
/* 299 */     org.bouncycastle.pqc.crypto.rainbow.Layer layer = (org.bouncycastle.pqc.crypto.rainbow.Layer)paramObject;
/*     */     
/* 301 */     return (this.vi == layer.getVi() && this.viNext == layer
/* 302 */       .getViNext() && this.oi == layer
/* 303 */       .getOi() && 
/* 304 */       RainbowUtil.equals(this.coeff_alpha, layer.getCoeffAlpha()) && 
/* 305 */       RainbowUtil.equals(this.coeff_beta, layer.getCoeffBeta()) && 
/* 306 */       RainbowUtil.equals(this.coeff_gamma, layer.getCoeffGamma()) && 
/* 307 */       RainbowUtil.equals(this.coeff_eta, layer.getCoeffEta()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 312 */     int i = this.vi;
/* 313 */     i = i * 37 + this.viNext;
/* 314 */     i = i * 37 + this.oi;
/* 315 */     i = i * 37 + Arrays.hashCode(this.coeff_alpha);
/* 316 */     i = i * 37 + Arrays.hashCode(this.coeff_beta);
/* 317 */     i = i * 37 + Arrays.hashCode(this.coeff_gamma);
/* 318 */     i = i * 37 + Arrays.hashCode(this.coeff_eta);
/*     */     
/* 320 */     return i;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/rainbow/Layer.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
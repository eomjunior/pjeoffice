/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
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
/*     */ public class PolynomialRingGF2m
/*     */ {
/*     */   private GF2mField field;
/*     */   private PolynomialGF2mSmallM p;
/*     */   protected PolynomialGF2mSmallM[] sqMatrix;
/*     */   protected PolynomialGF2mSmallM[] sqRootMatrix;
/*     */   
/*     */   public PolynomialRingGF2m(GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  42 */     this.field = paramGF2mField;
/*  43 */     this.p = paramPolynomialGF2mSmallM;
/*  44 */     computeSquaringMatrix();
/*  45 */     computeSquareRootMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM[] getSquaringMatrix() {
/*  53 */     return this.sqMatrix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM[] getSquareRootMatrix() {
/*  61 */     return this.sqRootMatrix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void computeSquaringMatrix() {
/*  70 */     int i = this.p.getDegree();
/*  71 */     this.sqMatrix = new PolynomialGF2mSmallM[i]; int j;
/*  72 */     for (j = 0; j < i >> 1; j++) {
/*     */       
/*  74 */       int[] arrayOfInt = new int[(j << 1) + 1];
/*  75 */       arrayOfInt[j << 1] = 1;
/*  76 */       this.sqMatrix[j] = new PolynomialGF2mSmallM(this.field, arrayOfInt);
/*     */     } 
/*  78 */     for (j = i >> 1; j < i; j++) {
/*     */       
/*  80 */       int[] arrayOfInt = new int[(j << 1) + 1];
/*  81 */       arrayOfInt[j << 1] = 1;
/*  82 */       PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(this.field, arrayOfInt);
/*     */       
/*  84 */       this.sqMatrix[j] = polynomialGF2mSmallM.mod(this.p);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void computeSquareRootMatrix() {
/*  94 */     int i = this.p.getDegree();
/*     */ 
/*     */     
/*  97 */     PolynomialGF2mSmallM[] arrayOfPolynomialGF2mSmallM = new PolynomialGF2mSmallM[i]; int j;
/*  98 */     for (j = i - 1; j >= 0; j--)
/*     */     {
/* 100 */       arrayOfPolynomialGF2mSmallM[j] = new PolynomialGF2mSmallM(this.sqMatrix[j]);
/*     */     }
/*     */ 
/*     */     
/* 104 */     this.sqRootMatrix = new PolynomialGF2mSmallM[i];
/* 105 */     for (j = i - 1; j >= 0; j--)
/*     */     {
/* 107 */       this.sqRootMatrix[j] = new PolynomialGF2mSmallM(this.field, j);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 112 */     for (j = 0; j < i; j++) {
/*     */ 
/*     */       
/* 115 */       if (arrayOfPolynomialGF2mSmallM[j].getCoefficient(j) == 0) {
/*     */         
/* 117 */         boolean bool = false;
/*     */         
/* 119 */         for (int n = j + 1; n < i; n++) {
/*     */           
/* 121 */           if (arrayOfPolynomialGF2mSmallM[n].getCoefficient(j) != 0) {
/*     */ 
/*     */             
/* 124 */             bool = true;
/* 125 */             swapColumns(arrayOfPolynomialGF2mSmallM, j, n);
/* 126 */             swapColumns(this.sqRootMatrix, j, n);
/*     */             
/* 128 */             n = i;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 133 */         if (!bool)
/*     */         {
/*     */           
/* 136 */           throw new ArithmeticException("Squaring matrix is not invertible.");
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 142 */       int k = arrayOfPolynomialGF2mSmallM[j].getCoefficient(j);
/* 143 */       int m = this.field.inverse(k);
/* 144 */       arrayOfPolynomialGF2mSmallM[j].multThisWithElement(m);
/* 145 */       this.sqRootMatrix[j].multThisWithElement(m);
/*     */ 
/*     */       
/* 148 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 150 */         if (b != j) {
/*     */           
/* 152 */           k = arrayOfPolynomialGF2mSmallM[b].getCoefficient(j);
/* 153 */           if (k != 0) {
/*     */ 
/*     */             
/* 156 */             PolynomialGF2mSmallM polynomialGF2mSmallM1 = arrayOfPolynomialGF2mSmallM[j].multWithElement(k);
/*     */             
/* 158 */             PolynomialGF2mSmallM polynomialGF2mSmallM2 = this.sqRootMatrix[j].multWithElement(k);
/* 159 */             arrayOfPolynomialGF2mSmallM[b].addToThis(polynomialGF2mSmallM1);
/* 160 */             this.sqRootMatrix[b].addToThis(polynomialGF2mSmallM2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void swapColumns(PolynomialGF2mSmallM[] paramArrayOfPolynomialGF2mSmallM, int paramInt1, int paramInt2) {
/* 170 */     PolynomialGF2mSmallM polynomialGF2mSmallM = paramArrayOfPolynomialGF2mSmallM[paramInt1];
/* 171 */     paramArrayOfPolynomialGF2mSmallM[paramInt1] = paramArrayOfPolynomialGF2mSmallM[paramInt2];
/* 172 */     paramArrayOfPolynomialGF2mSmallM[paramInt2] = polynomialGF2mSmallM;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/PolynomialRingGF2m.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
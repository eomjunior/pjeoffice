/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
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
/*     */ public final class GoppaCode
/*     */ {
/*     */   public static GF2Matrix createCanonicalCheckMatrix(GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/* 139 */     int i = paramGF2mField.getDegree();
/* 140 */     int j = 1 << i;
/* 141 */     int k = paramPolynomialGF2mSmallM.getDegree();
/*     */ 
/*     */ 
/*     */     
/* 145 */     int[][] arrayOfInt1 = new int[k][j];
/*     */ 
/*     */     
/* 148 */     int[][] arrayOfInt2 = new int[k][j]; byte b1;
/* 149 */     for (b1 = 0; b1 < j; b1++)
/*     */     {
/*     */       
/* 152 */       arrayOfInt2[0][b1] = paramGF2mField.inverse(paramPolynomialGF2mSmallM.evaluateAt(b1));
/*     */     }
/*     */     
/* 155 */     for (b1 = 1; b1 < k; b1++) {
/*     */       
/* 157 */       for (byte b = 0; b < j; b++)
/*     */       {
/*     */         
/* 160 */         arrayOfInt2[b1][b] = paramGF2mField.mult(arrayOfInt2[b1 - 1][b], b);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 165 */     for (b1 = 0; b1 < k; b1++) {
/*     */       
/* 167 */       for (byte b = 0; b < j; b++) {
/*     */         
/* 169 */         for (byte b3 = 0; b3 <= b1; b3++)
/*     */         {
/* 171 */           arrayOfInt1[b1][b] = paramGF2mField.add(arrayOfInt1[b1][b], paramGF2mField.mult(arrayOfInt2[b3][b], paramPolynomialGF2mSmallM
/* 172 */                 .getCoefficient(k + b3 - b1)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 179 */     int[][] arrayOfInt3 = new int[k * i][j + 31 >>> 5];
/*     */     
/* 181 */     for (byte b2 = 0; b2 < j; b2++) {
/*     */       
/* 183 */       int m = b2 >>> 5;
/* 184 */       int n = 1 << (b2 & 0x1F);
/* 185 */       for (byte b = 0; b < k; b++) {
/*     */         
/* 187 */         int i1 = arrayOfInt1[b][b2];
/* 188 */         for (byte b3 = 0; b3 < i; b3++) {
/*     */           
/* 190 */           int i2 = i1 >>> b3 & 0x1;
/* 191 */           if (i2 != 0) {
/*     */             
/* 193 */             int i3 = (b + 1) * i - b3 - 1;
/* 194 */             arrayOfInt3[i3][m] = arrayOfInt3[i3][m] ^ n;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     return new GF2Matrix(j, arrayOfInt3);
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
/*     */   public static MaMaPe computeSystematicForm(GF2Matrix paramGF2Matrix, SecureRandom paramSecureRandom) {
/*     */     GF2Matrix gF2Matrix1, gF2Matrix2;
/*     */     Permutation permutation;
/* 216 */     int i = paramGF2Matrix.getNumColumns();
/*     */     
/* 218 */     GF2Matrix gF2Matrix3 = null;
/*     */     
/* 220 */     boolean bool = false;
/*     */ 
/*     */     
/*     */     do {
/* 224 */       permutation = new Permutation(i, paramSecureRandom);
/* 225 */       gF2Matrix1 = (GF2Matrix)paramGF2Matrix.rightMultiply(permutation);
/* 226 */       gF2Matrix2 = gF2Matrix1.getLeftSubMatrix();
/*     */       
/*     */       try {
/* 229 */         bool = true;
/* 230 */         gF2Matrix3 = (GF2Matrix)gF2Matrix2.computeInverse();
/*     */       }
/* 232 */       catch (ArithmeticException arithmeticException) {
/*     */         
/* 234 */         bool = false;
/*     */       }
/*     */     
/* 237 */     } while (!bool);
/*     */     
/* 239 */     GF2Matrix gF2Matrix4 = (GF2Matrix)gF2Matrix3.rightMultiply((Matrix)gF2Matrix1);
/* 240 */     GF2Matrix gF2Matrix5 = gF2Matrix4.getRightSubMatrix();
/*     */     
/* 242 */     return new MaMaPe(gF2Matrix2, gF2Matrix5, permutation);
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
/*     */   public static GF2Vector syndromeDecode(GF2Vector paramGF2Vector, GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM, PolynomialGF2mSmallM[] paramArrayOfPolynomialGF2mSmallM) {
/* 260 */     int i = 1 << paramGF2mField.getDegree();
/*     */ 
/*     */     
/* 263 */     GF2Vector gF2Vector = new GF2Vector(i);
/*     */ 
/*     */     
/* 266 */     if (!paramGF2Vector.isZero()) {
/*     */ 
/*     */ 
/*     */       
/* 270 */       PolynomialGF2mSmallM polynomialGF2mSmallM1 = new PolynomialGF2mSmallM(paramGF2Vector.toExtensionFieldVector(paramGF2mField));
/*     */ 
/*     */       
/* 273 */       PolynomialGF2mSmallM polynomialGF2mSmallM2 = polynomialGF2mSmallM1.modInverse(paramPolynomialGF2mSmallM);
/*     */ 
/*     */       
/* 276 */       PolynomialGF2mSmallM polynomialGF2mSmallM3 = polynomialGF2mSmallM2.addMonomial(1);
/* 277 */       polynomialGF2mSmallM3 = polynomialGF2mSmallM3.modSquareRootMatrix(paramArrayOfPolynomialGF2mSmallM);
/*     */ 
/*     */       
/* 280 */       PolynomialGF2mSmallM[] arrayOfPolynomialGF2mSmallM = polynomialGF2mSmallM3.modPolynomialToFracton(paramPolynomialGF2mSmallM);
/*     */ 
/*     */       
/* 283 */       PolynomialGF2mSmallM polynomialGF2mSmallM4 = arrayOfPolynomialGF2mSmallM[0].multiply(arrayOfPolynomialGF2mSmallM[0]);
/* 284 */       PolynomialGF2mSmallM polynomialGF2mSmallM5 = arrayOfPolynomialGF2mSmallM[1].multiply(arrayOfPolynomialGF2mSmallM[1]);
/* 285 */       PolynomialGF2mSmallM polynomialGF2mSmallM6 = polynomialGF2mSmallM5.multWithMonomial(1);
/* 286 */       PolynomialGF2mSmallM polynomialGF2mSmallM7 = polynomialGF2mSmallM4.add(polynomialGF2mSmallM6);
/*     */ 
/*     */       
/* 289 */       int j = polynomialGF2mSmallM7.getHeadCoefficient();
/* 290 */       int k = paramGF2mField.inverse(j);
/* 291 */       PolynomialGF2mSmallM polynomialGF2mSmallM8 = polynomialGF2mSmallM7.multWithElement(k);
/*     */ 
/*     */       
/* 294 */       for (byte b = 0; b < i; b++) {
/*     */ 
/*     */         
/* 297 */         int m = polynomialGF2mSmallM8.evaluateAt(b);
/*     */         
/* 299 */         if (m == 0)
/*     */         {
/*     */           
/* 302 */           gF2Vector.setBit(b);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 307 */     return gF2Vector;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/GoppaCode.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*      */ 
/*      */ import java.security.SecureRandom;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.Matrix;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.Vector;
/*      */ import org.bouncycastle.util.Arrays;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GF2Matrix
/*      */   extends Matrix
/*      */ {
/*      */   private int[][] matrix;
/*      */   private int length;
/*      */   
/*      */   public GF2Matrix(byte[] paramArrayOfbyte) {
/*   36 */     if (paramArrayOfbyte.length < 9)
/*      */     {
/*   38 */       throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
/*      */     }
/*      */ 
/*      */     
/*   42 */     this.numRows = LittleEndianConversions.OS2IP(paramArrayOfbyte, 0);
/*   43 */     this.numColumns = LittleEndianConversions.OS2IP(paramArrayOfbyte, 4);
/*      */     
/*   45 */     int i = (this.numColumns + 7 >>> 3) * this.numRows;
/*      */     
/*   47 */     if (this.numRows <= 0 || i != paramArrayOfbyte.length - 8)
/*      */     {
/*   49 */       throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
/*      */     }
/*      */ 
/*      */     
/*   53 */     this.length = this.numColumns + 31 >>> 5;
/*   54 */     this.matrix = new int[this.numRows][this.length];
/*      */ 
/*      */     
/*   57 */     int j = this.numColumns >> 5;
/*      */     
/*   59 */     int k = this.numColumns & 0x1F;
/*      */     
/*   61 */     byte b1 = 8;
/*   62 */     for (byte b2 = 0; b2 < this.numRows; b2++) {
/*      */       byte b;
/*   64 */       for (b = 0; b < j; b++, b1 += 4)
/*      */       {
/*   66 */         this.matrix[b2][b] = LittleEndianConversions.OS2IP(paramArrayOfbyte, b1);
/*      */       }
/*   68 */       for (b = 0; b < k; b += 8)
/*      */       {
/*   70 */         this.matrix[b2][j] = this.matrix[b2][j] ^ (paramArrayOfbyte[b1++] & 0xFF) << b;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GF2Matrix(int paramInt, int[][] paramArrayOfint) {
/*      */     int j;
/*   84 */     if ((paramArrayOfint[0]).length != paramInt + 31 >> 5)
/*      */     {
/*   86 */       throw new ArithmeticException("Int array does not match given number of columns.");
/*      */     }
/*      */     
/*   89 */     this.numColumns = paramInt;
/*   90 */     this.numRows = paramArrayOfint.length;
/*   91 */     this.length = (paramArrayOfint[0]).length;
/*   92 */     int i = paramInt & 0x1F;
/*      */     
/*   94 */     if (i == 0) {
/*      */       
/*   96 */       j = -1;
/*      */     }
/*      */     else {
/*      */       
/*  100 */       j = (1 << i) - 1;
/*      */     } 
/*  102 */     for (byte b = 0; b < this.numRows; b++)
/*      */     {
/*  104 */       paramArrayOfint[b][this.length - 1] = paramArrayOfint[b][this.length - 1] & j;
/*      */     }
/*  106 */     this.matrix = paramArrayOfint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GF2Matrix(int paramInt, char paramChar) {
/*  118 */     this(paramInt, paramChar, new SecureRandom());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GF2Matrix(int paramInt, char paramChar, SecureRandom paramSecureRandom) {
/*  130 */     if (paramInt <= 0)
/*      */     {
/*  132 */       throw new ArithmeticException("Size of matrix is non-positive.");
/*      */     }
/*      */     
/*  135 */     switch (paramChar) {
/*      */ 
/*      */       
/*      */       case 'Z':
/*  139 */         assignZeroMatrix(paramInt, paramInt);
/*      */         return;
/*      */       
/*      */       case 'I':
/*  143 */         assignUnitMatrix(paramInt);
/*      */         return;
/*      */       
/*      */       case 'L':
/*  147 */         assignRandomLowerTriangularMatrix(paramInt, paramSecureRandom);
/*      */         return;
/*      */       
/*      */       case 'U':
/*  151 */         assignRandomUpperTriangularMatrix(paramInt, paramSecureRandom);
/*      */         return;
/*      */       
/*      */       case 'R':
/*  155 */         assignRandomRegularMatrix(paramInt, paramSecureRandom);
/*      */         return;
/*      */     } 
/*      */     
/*  159 */     throw new ArithmeticException("Unknown matrix type.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GF2Matrix(org.bouncycastle.pqc.math.linearalgebra.GF2Matrix paramGF2Matrix) {
/*  170 */     this.numColumns = paramGF2Matrix.getNumColumns();
/*  171 */     this.numRows = paramGF2Matrix.getNumRows();
/*  172 */     this.length = paramGF2Matrix.length;
/*  173 */     this.matrix = new int[paramGF2Matrix.matrix.length][];
/*  174 */     for (byte b = 0; b < this.matrix.length; b++)
/*      */     {
/*  176 */       this.matrix[b] = IntUtils.clone(paramGF2Matrix.matrix[b]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private GF2Matrix(int paramInt1, int paramInt2) {
/*  186 */     if (paramInt2 <= 0 || paramInt1 <= 0)
/*      */     {
/*  188 */       throw new ArithmeticException("size of matrix is non-positive");
/*      */     }
/*      */     
/*  191 */     assignZeroMatrix(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void assignZeroMatrix(int paramInt1, int paramInt2) {
/*  202 */     this.numRows = paramInt1;
/*  203 */     this.numColumns = paramInt2;
/*  204 */     this.length = paramInt2 + 31 >>> 5;
/*  205 */     this.matrix = new int[this.numRows][this.length];
/*  206 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/*  208 */       for (byte b1 = 0; b1 < this.length; b1++)
/*      */       {
/*  210 */         this.matrix[b][b1] = 0;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void assignUnitMatrix(int paramInt) {
/*  222 */     this.numRows = paramInt;
/*  223 */     this.numColumns = paramInt;
/*  224 */     this.length = paramInt + 31 >>> 5;
/*  225 */     this.matrix = new int[this.numRows][this.length]; byte b;
/*  226 */     for (b = 0; b < this.numRows; b++) {
/*      */       
/*  228 */       for (byte b1 = 0; b1 < this.length; b1++)
/*      */       {
/*  230 */         this.matrix[b][b1] = 0;
/*      */       }
/*      */     } 
/*  233 */     for (b = 0; b < this.numRows; b++) {
/*      */       
/*  235 */       int i = b & 0x1F;
/*  236 */       this.matrix[b][b >>> 5] = 1 << i;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void assignRandomLowerTriangularMatrix(int paramInt, SecureRandom paramSecureRandom) {
/*  248 */     this.numRows = paramInt;
/*  249 */     this.numColumns = paramInt;
/*  250 */     this.length = paramInt + 31 >>> 5;
/*  251 */     this.matrix = new int[this.numRows][this.length];
/*  252 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/*  254 */       int i = b >>> 5;
/*  255 */       int j = b & 0x1F;
/*  256 */       int k = 31 - j;
/*  257 */       j = 1 << j; int m;
/*  258 */       for (m = 0; m < i; m++)
/*      */       {
/*  260 */         this.matrix[b][m] = paramSecureRandom.nextInt();
/*      */       }
/*  262 */       this.matrix[b][i] = paramSecureRandom.nextInt() >>> k | j;
/*  263 */       for (m = i + 1; m < this.length; m++)
/*      */       {
/*  265 */         this.matrix[b][m] = 0;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void assignRandomUpperTriangularMatrix(int paramInt, SecureRandom paramSecureRandom) {
/*      */     int j;
/*  280 */     this.numRows = paramInt;
/*  281 */     this.numColumns = paramInt;
/*  282 */     this.length = paramInt + 31 >>> 5;
/*  283 */     this.matrix = new int[this.numRows][this.length];
/*  284 */     int i = paramInt & 0x1F;
/*      */     
/*  286 */     if (i == 0) {
/*      */       
/*  288 */       j = -1;
/*      */     }
/*      */     else {
/*      */       
/*  292 */       j = (1 << i) - 1;
/*      */     } 
/*  294 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/*  296 */       int k = b >>> 5;
/*  297 */       int m = b & 0x1F;
/*  298 */       int n = m;
/*  299 */       m = 1 << m; int i1;
/*  300 */       for (i1 = 0; i1 < k; i1++)
/*      */       {
/*  302 */         this.matrix[b][i1] = 0;
/*      */       }
/*  304 */       this.matrix[b][k] = paramSecureRandom.nextInt() << n | m;
/*  305 */       for (i1 = k + 1; i1 < this.length; i1++)
/*      */       {
/*  307 */         this.matrix[b][i1] = paramSecureRandom.nextInt();
/*      */       }
/*  309 */       this.matrix[b][this.length - 1] = this.matrix[b][this.length - 1] & j;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void assignRandomRegularMatrix(int paramInt, SecureRandom paramSecureRandom) {
/*  322 */     this.numRows = paramInt;
/*  323 */     this.numColumns = paramInt;
/*  324 */     this.length = paramInt + 31 >>> 5;
/*  325 */     this.matrix = new int[this.numRows][this.length];
/*  326 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix1 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, 'L', paramSecureRandom);
/*  327 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix2 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, 'U', paramSecureRandom);
/*  328 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix3 = (org.bouncycastle.pqc.math.linearalgebra.GF2Matrix)gF2Matrix1.rightMultiply(gF2Matrix2);
/*  329 */     Permutation permutation = new Permutation(paramInt, paramSecureRandom);
/*  330 */     int[] arrayOfInt = permutation.getVector();
/*  331 */     for (byte b = 0; b < paramInt; b++)
/*      */     {
/*  333 */       System.arraycopy(gF2Matrix3.matrix[b], 0, this.matrix[arrayOfInt[b]], 0, this.length);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static org.bouncycastle.pqc.math.linearalgebra.GF2Matrix[] createRandomRegularMatrixAndItsInverse(int paramInt, SecureRandom paramSecureRandom) {
/*  348 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix[] arrayOfGF2Matrix = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix[2];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  355 */     int i = paramInt + 31 >> 5;
/*  356 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix1 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, 'L', paramSecureRandom);
/*  357 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix2 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, 'U', paramSecureRandom);
/*  358 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix3 = (org.bouncycastle.pqc.math.linearalgebra.GF2Matrix)gF2Matrix1.rightMultiply(gF2Matrix2);
/*  359 */     Permutation permutation = new Permutation(paramInt, paramSecureRandom);
/*  360 */     int[] arrayOfInt = permutation.getVector();
/*      */     
/*  362 */     int[][] arrayOfInt1 = new int[paramInt][i];
/*  363 */     for (byte b1 = 0; b1 < paramInt; b1++)
/*      */     {
/*  365 */       System.arraycopy(gF2Matrix3.matrix[arrayOfInt[b1]], 0, arrayOfInt1[b1], 0, i);
/*      */     }
/*      */     
/*  368 */     arrayOfGF2Matrix[0] = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, arrayOfInt1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  375 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix4 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, 'I');
/*  376 */     for (byte b2 = 0; b2 < paramInt; b2++) {
/*      */       
/*  378 */       int k = b2 & 0x1F;
/*  379 */       int m = b2 >>> 5;
/*  380 */       int n = 1 << k;
/*  381 */       for (int i1 = b2 + 1; i1 < paramInt; i1++) {
/*      */         
/*  383 */         int i2 = gF2Matrix1.matrix[i1][m] & n;
/*  384 */         if (i2 != 0)
/*      */         {
/*  386 */           for (byte b = 0; b <= m; b++)
/*      */           {
/*  388 */             gF2Matrix4.matrix[i1][b] = gF2Matrix4.matrix[i1][b] ^ gF2Matrix4.matrix[b2][b];
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  394 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix5 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(paramInt, 'I');
/*  395 */     for (int j = paramInt - 1; j >= 0; j--) {
/*      */       
/*  397 */       int k = j & 0x1F;
/*  398 */       int m = j >>> 5;
/*  399 */       int n = 1 << k;
/*  400 */       for (int i1 = j - 1; i1 >= 0; i1--) {
/*      */         
/*  402 */         int i2 = gF2Matrix2.matrix[i1][m] & n;
/*  403 */         if (i2 != 0)
/*      */         {
/*  405 */           for (int i3 = m; i3 < i; i3++)
/*      */           {
/*  407 */             gF2Matrix5.matrix[i1][i3] = gF2Matrix5.matrix[i1][i3] ^ gF2Matrix5.matrix[j][i3];
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  414 */     arrayOfGF2Matrix[1] = (org.bouncycastle.pqc.math.linearalgebra.GF2Matrix)gF2Matrix5.rightMultiply(gF2Matrix4.rightMultiply(permutation));
/*      */     
/*  416 */     return arrayOfGF2Matrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[][] getIntArray() {
/*  424 */     return this.matrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLength() {
/*  432 */     return this.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getRow(int paramInt) {
/*  443 */     return this.matrix[paramInt];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getEncoded() {
/*  453 */     int i = this.numColumns + 7 >>> 3;
/*  454 */     i *= this.numRows;
/*  455 */     i += 8;
/*  456 */     byte[] arrayOfByte = new byte[i];
/*      */     
/*  458 */     LittleEndianConversions.I2OSP(this.numRows, arrayOfByte, 0);
/*  459 */     LittleEndianConversions.I2OSP(this.numColumns, arrayOfByte, 4);
/*      */ 
/*      */     
/*  462 */     int j = this.numColumns >>> 5;
/*      */     
/*  464 */     int k = this.numColumns & 0x1F;
/*      */     
/*  466 */     byte b1 = 8;
/*  467 */     for (byte b2 = 0; b2 < this.numRows; b2++) {
/*      */       byte b;
/*  469 */       for (b = 0; b < j; b++, b1 += 4)
/*      */       {
/*  471 */         LittleEndianConversions.I2OSP(this.matrix[b2][b], arrayOfByte, b1);
/*      */       }
/*  473 */       for (b = 0; b < k; b += 8)
/*      */       {
/*  475 */         arrayOfByte[b1++] = (byte)(this.matrix[b2][j] >>> b & 0xFF);
/*      */       }
/*      */     } 
/*      */     
/*  479 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getHammingWeight() {
/*      */     int j;
/*  490 */     double d1 = 0.0D;
/*  491 */     double d2 = 0.0D;
/*  492 */     int i = this.numColumns & 0x1F;
/*      */     
/*  494 */     if (i == 0) {
/*      */       
/*  496 */       j = this.length;
/*      */     }
/*      */     else {
/*      */       
/*  500 */       j = this.length - 1;
/*      */     } 
/*      */     
/*  503 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       int k;
/*      */       
/*  506 */       for (k = 0; k < j; k++) {
/*      */         
/*  508 */         int m = this.matrix[b][k];
/*  509 */         for (byte b2 = 0; b2 < 32; b2++) {
/*      */           
/*  511 */           int n = m >>> b2 & 0x1;
/*  512 */           d1 += n;
/*  513 */           d2++;
/*      */         } 
/*      */       } 
/*  516 */       k = this.matrix[b][this.length - 1];
/*  517 */       for (byte b1 = 0; b1 < i; b1++) {
/*      */         
/*  519 */         int m = k >>> b1 & 0x1;
/*  520 */         d1 += m;
/*  521 */         d2++;
/*      */       } 
/*      */     } 
/*      */     
/*  525 */     return d1 / d2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isZero() {
/*  535 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/*  537 */       for (byte b1 = 0; b1 < this.length; b1++) {
/*      */         
/*  539 */         if (this.matrix[b][b1] != 0)
/*      */         {
/*  541 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  545 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.GF2Matrix getLeftSubMatrix() {
/*  556 */     if (this.numColumns <= this.numRows)
/*      */     {
/*  558 */       throw new ArithmeticException("empty submatrix");
/*      */     }
/*  560 */     int i = this.numRows + 31 >> 5;
/*  561 */     int[][] arrayOfInt = new int[this.numRows][i];
/*  562 */     int j = (1 << (this.numRows & 0x1F)) - 1;
/*  563 */     if (j == 0)
/*      */     {
/*  565 */       j = -1;
/*      */     }
/*  567 */     for (int k = this.numRows - 1; k >= 0; k--) {
/*      */       
/*  569 */       System.arraycopy(this.matrix[k], 0, arrayOfInt[k], 0, i);
/*  570 */       arrayOfInt[k][i - 1] = arrayOfInt[k][i - 1] & j;
/*      */     } 
/*  572 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, arrayOfInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.GF2Matrix extendLeftCompactForm() {
/*  584 */     int i = this.numColumns + this.numRows;
/*  585 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, i);
/*      */     
/*  587 */     int j = this.numRows - 1 + this.numColumns;
/*  588 */     for (int k = this.numRows - 1; k >= 0; k--, j--) {
/*      */ 
/*      */       
/*  591 */       System.arraycopy(this.matrix[k], 0, gF2Matrix.matrix[k], 0, this.length);
/*      */       
/*  593 */       gF2Matrix.matrix[k][j >> 5] = gF2Matrix.matrix[k][j >> 5] | 1 << (j & 0x1F);
/*      */     } 
/*      */     
/*  596 */     return gF2Matrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.GF2Matrix getRightSubMatrix() {
/*  607 */     if (this.numColumns <= this.numRows)
/*      */     {
/*  609 */       throw new ArithmeticException("empty submatrix");
/*      */     }
/*      */     
/*  612 */     int i = this.numRows >> 5;
/*  613 */     int j = this.numRows & 0x1F;
/*      */     
/*  615 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, this.numColumns - this.numRows);
/*      */     
/*  617 */     for (int k = this.numRows - 1; k >= 0; k--) {
/*      */ 
/*      */       
/*  620 */       if (j != 0) {
/*      */         
/*  622 */         int m = i;
/*      */         
/*  624 */         for (byte b = 0; b < gF2Matrix.length - 1; b++)
/*      */         {
/*      */           
/*  627 */           gF2Matrix.matrix[k][b] = this.matrix[k][m++] >>> j | this.matrix[k][m] << 32 - j;
/*      */         }
/*      */ 
/*      */         
/*  631 */         gF2Matrix.matrix[k][gF2Matrix.length - 1] = this.matrix[k][m++] >>> j;
/*  632 */         if (m < this.length)
/*      */         {
/*  634 */           gF2Matrix.matrix[k][gF2Matrix.length - 1] = gF2Matrix.matrix[k][gF2Matrix.length - 1] | this.matrix[k][m] << 32 - j;
/*      */         
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  640 */         System.arraycopy(this.matrix[k], i, gF2Matrix.matrix[k], 0, gF2Matrix.length);
/*      */       } 
/*      */     } 
/*      */     
/*  644 */     return gF2Matrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.GF2Matrix extendRightCompactForm() {
/*  656 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, this.numRows + this.numColumns);
/*      */     
/*  658 */     int i = this.numRows >> 5;
/*  659 */     int j = this.numRows & 0x1F;
/*      */     
/*  661 */     for (int k = this.numRows - 1; k >= 0; k--) {
/*      */ 
/*      */       
/*  664 */       gF2Matrix.matrix[k][k >> 5] = gF2Matrix.matrix[k][k >> 5] | 1 << (k & 0x1F);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  669 */       if (j != 0) {
/*      */         
/*  671 */         int m = i;
/*      */         int n;
/*  673 */         for (n = 0; n < this.length - 1; n++) {
/*      */ 
/*      */           
/*  676 */           int i1 = this.matrix[k][n];
/*      */           
/*  678 */           gF2Matrix.matrix[k][m++] = gF2Matrix.matrix[k][m++] | i1 << j;
/*  679 */           gF2Matrix.matrix[k][m] = gF2Matrix.matrix[k][m] | i1 >>> 32 - j;
/*      */         } 
/*      */         
/*  682 */         n = this.matrix[k][this.length - 1];
/*  683 */         gF2Matrix.matrix[k][m++] = gF2Matrix.matrix[k][m++] | n << j;
/*  684 */         if (m < gF2Matrix.length)
/*      */         {
/*  686 */           gF2Matrix.matrix[k][m] = gF2Matrix.matrix[k][m] | n >>> 32 - j;
/*      */         
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  692 */         System.arraycopy(this.matrix[k], 0, gF2Matrix.matrix[k], i, this.length);
/*      */       } 
/*      */     } 
/*      */     
/*  696 */     return gF2Matrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix computeTranspose() {
/*  706 */     int[][] arrayOfInt = new int[this.numColumns][this.numRows + 31 >>> 5];
/*  707 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/*  709 */       for (byte b1 = 0; b1 < this.numColumns; b1++) {
/*      */         
/*  711 */         int i = b1 >>> 5;
/*  712 */         int j = b1 & 0x1F;
/*  713 */         int k = this.matrix[b][i] >>> j & 0x1;
/*  714 */         int m = b >>> 5;
/*  715 */         int n = b & 0x1F;
/*  716 */         if (k == 1)
/*      */         {
/*  718 */           arrayOfInt[b1][m] = arrayOfInt[b1][m] | 1 << n;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  723 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, arrayOfInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix computeInverse() {
/*  734 */     if (this.numRows != this.numColumns)
/*      */     {
/*  736 */       throw new ArithmeticException("Matrix is not invertible.");
/*      */     }
/*      */ 
/*      */     
/*  740 */     int[][] arrayOfInt1 = new int[this.numRows][this.length];
/*  741 */     for (int i = this.numRows - 1; i >= 0; i--)
/*      */     {
/*  743 */       arrayOfInt1[i] = IntUtils.clone(this.matrix[i]);
/*      */     }
/*      */ 
/*      */     
/*  747 */     int[][] arrayOfInt2 = new int[this.numRows][this.length]; int j;
/*  748 */     for (j = this.numRows - 1; j >= 0; j--) {
/*      */       
/*  750 */       int k = j >> 5;
/*  751 */       int m = j & 0x1F;
/*  752 */       arrayOfInt2[j][k] = 1 << m;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  757 */     for (j = 0; j < this.numRows; j++) {
/*      */ 
/*      */       
/*  760 */       int k = j >> 5;
/*  761 */       int m = 1 << (j & 0x1F);
/*      */       
/*  763 */       if ((arrayOfInt1[j][k] & m) == 0) {
/*      */         
/*  765 */         boolean bool = false;
/*      */         
/*  767 */         for (int i1 = j + 1; i1 < this.numRows; i1++) {
/*      */           
/*  769 */           if ((arrayOfInt1[i1][k] & m) != 0) {
/*      */ 
/*      */             
/*  772 */             bool = true;
/*  773 */             swapRows(arrayOfInt1, j, i1);
/*  774 */             swapRows(arrayOfInt2, j, i1);
/*      */             
/*  776 */             i1 = this.numRows;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  781 */         if (!bool)
/*      */         {
/*      */           
/*  784 */           throw new ArithmeticException("Matrix is not invertible.");
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  789 */       for (int n = this.numRows - 1; n >= 0; n--) {
/*      */         
/*  791 */         if (n != j && (arrayOfInt1[n][k] & m) != 0) {
/*      */           
/*  793 */           addToRow(arrayOfInt1[j], arrayOfInt1[n], k);
/*  794 */           addToRow(arrayOfInt2[j], arrayOfInt2[n], 0);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  799 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numColumns, arrayOfInt2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix leftMultiply(Permutation paramPermutation) {
/*  811 */     int[] arrayOfInt = paramPermutation.getVector();
/*  812 */     if (arrayOfInt.length != this.numRows)
/*      */     {
/*  814 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/*  817 */     int[][] arrayOfInt1 = new int[this.numRows][];
/*      */     
/*  819 */     for (int i = this.numRows - 1; i >= 0; i--)
/*      */     {
/*  821 */       arrayOfInt1[i] = IntUtils.clone(this.matrix[arrayOfInt[i]]);
/*      */     }
/*      */     
/*  824 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, arrayOfInt1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector leftMultiply(Vector paramVector) {
/*  836 */     if (!(paramVector instanceof GF2Vector))
/*      */     {
/*  838 */       throw new ArithmeticException("vector is not defined over GF(2)");
/*      */     }
/*      */     
/*  841 */     if (paramVector.length != this.numRows)
/*      */     {
/*  843 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/*  846 */     int[] arrayOfInt1 = ((GF2Vector)paramVector).getVecArray();
/*  847 */     int[] arrayOfInt2 = new int[this.length];
/*      */     
/*  849 */     int i = this.numRows >> 5;
/*  850 */     int j = 1 << (this.numRows & 0x1F);
/*      */ 
/*      */     
/*  853 */     byte b = 0; int k;
/*  854 */     for (k = 0; k < i; ) {
/*      */       
/*  856 */       int m = 1;
/*      */       
/*      */       while (true) {
/*  859 */         int n = arrayOfInt1[k] & m;
/*  860 */         if (n != 0)
/*      */         {
/*  862 */           for (byte b1 = 0; b1 < this.length; b1++)
/*      */           {
/*  864 */             arrayOfInt2[b1] = arrayOfInt2[b1] ^ this.matrix[b][b1];
/*      */           }
/*      */         }
/*  867 */         b++;
/*  868 */         m <<= 1;
/*      */         
/*  870 */         if (m == 0)
/*      */           k++; 
/*      */       } 
/*      */     } 
/*  874 */     k = 1;
/*  875 */     while (k != j) {
/*      */       
/*  877 */       int m = arrayOfInt1[i] & k;
/*  878 */       if (m != 0)
/*      */       {
/*  880 */         for (byte b1 = 0; b1 < this.length; b1++)
/*      */         {
/*  882 */           arrayOfInt2[b1] = arrayOfInt2[b1] ^ this.matrix[b][b1];
/*      */         }
/*      */       }
/*  885 */       b++;
/*  886 */       k <<= 1;
/*      */     } 
/*      */     
/*  889 */     return (Vector)new GF2Vector(arrayOfInt2, this.numColumns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector leftMultiplyLeftCompactForm(Vector paramVector) {
/*  902 */     if (!(paramVector instanceof GF2Vector))
/*      */     {
/*  904 */       throw new ArithmeticException("vector is not defined over GF(2)");
/*      */     }
/*      */     
/*  907 */     if (paramVector.length != this.numRows)
/*      */     {
/*  909 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/*  912 */     int[] arrayOfInt1 = ((GF2Vector)paramVector).getVecArray();
/*  913 */     int[] arrayOfInt2 = new int[this.numRows + this.numColumns + 31 >>> 5];
/*      */ 
/*      */     
/*  916 */     int i = this.numRows >>> 5;
/*  917 */     byte b = 0; int j;
/*  918 */     for (j = 0; j < i; ) {
/*      */       
/*  920 */       int m = 1;
/*      */       
/*      */       while (true) {
/*  923 */         int n = arrayOfInt1[j] & m;
/*  924 */         if (n != 0) {
/*      */           int i1;
/*      */           
/*  927 */           for (i1 = 0; i1 < this.length; i1++)
/*      */           {
/*  929 */             arrayOfInt2[i1] = arrayOfInt2[i1] ^ this.matrix[b][i1];
/*      */           }
/*      */           
/*  932 */           i1 = this.numColumns + b >>> 5;
/*  933 */           int i2 = this.numColumns + b & 0x1F;
/*  934 */           arrayOfInt2[i1] = arrayOfInt2[i1] | 1 << i2;
/*      */         } 
/*  936 */         b++;
/*  937 */         m <<= 1;
/*      */         
/*  939 */         if (m == 0)
/*      */           j++; 
/*      */       } 
/*      */     } 
/*  943 */     j = 1 << (this.numRows & 0x1F);
/*  944 */     int k = 1;
/*  945 */     while (k != j) {
/*      */       
/*  947 */       int m = arrayOfInt1[i] & k;
/*  948 */       if (m != 0) {
/*      */         int n;
/*      */         
/*  951 */         for (n = 0; n < this.length; n++)
/*      */         {
/*  953 */           arrayOfInt2[n] = arrayOfInt2[n] ^ this.matrix[b][n];
/*      */         }
/*      */         
/*  956 */         n = this.numColumns + b >>> 5;
/*  957 */         int i1 = this.numColumns + b & 0x1F;
/*  958 */         arrayOfInt2[n] = arrayOfInt2[n] | 1 << i1;
/*      */       } 
/*  960 */       b++;
/*  961 */       k <<= 1;
/*      */     } 
/*      */     
/*  964 */     return (Vector)new GF2Vector(arrayOfInt2, this.numRows + this.numColumns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix rightMultiply(Matrix paramMatrix) {
/*      */     int i;
/*  975 */     if (!(paramMatrix instanceof org.bouncycastle.pqc.math.linearalgebra.GF2Matrix))
/*      */     {
/*  977 */       throw new ArithmeticException("matrix is not defined over GF(2)");
/*      */     }
/*      */     
/*  980 */     if (paramMatrix.numRows != this.numColumns)
/*      */     {
/*  982 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/*  985 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix1 = (org.bouncycastle.pqc.math.linearalgebra.GF2Matrix)paramMatrix;
/*  986 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix2 = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, paramMatrix.numColumns);
/*      */ 
/*      */     
/*  989 */     int j = this.numColumns & 0x1F;
/*  990 */     if (j == 0) {
/*      */       
/*  992 */       i = this.length;
/*      */     }
/*      */     else {
/*      */       
/*  996 */       i = this.length - 1;
/*      */     } 
/*  998 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/* 1000 */       byte b1 = 0; int k;
/* 1001 */       for (k = 0; k < i; k++) {
/*      */         
/* 1003 */         int m = this.matrix[b][k];
/* 1004 */         for (byte b3 = 0; b3 < 32; b3++) {
/*      */           
/* 1006 */           int n = m & 1 << b3;
/* 1007 */           if (n != 0)
/*      */           {
/* 1009 */             for (byte b4 = 0; b4 < gF2Matrix1.length; b4++)
/*      */             {
/* 1011 */               gF2Matrix2.matrix[b][b4] = gF2Matrix2.matrix[b][b4] ^ gF2Matrix1.matrix[b1][b4];
/*      */             }
/*      */           }
/* 1014 */           b1++;
/*      */         } 
/*      */       } 
/* 1017 */       k = this.matrix[b][this.length - 1];
/* 1018 */       for (byte b2 = 0; b2 < j; b2++) {
/*      */         
/* 1020 */         int m = k & 1 << b2;
/* 1021 */         if (m != 0)
/*      */         {
/* 1023 */           for (byte b3 = 0; b3 < gF2Matrix1.length; b3++)
/*      */           {
/* 1025 */             gF2Matrix2.matrix[b][b3] = gF2Matrix2.matrix[b][b3] ^ gF2Matrix1.matrix[b1][b3];
/*      */           }
/*      */         }
/* 1028 */         b1++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1033 */     return gF2Matrix2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Matrix rightMultiply(Permutation paramPermutation) {
/* 1046 */     int[] arrayOfInt = paramPermutation.getVector();
/* 1047 */     if (arrayOfInt.length != this.numColumns)
/*      */     {
/* 1049 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/* 1052 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix = new org.bouncycastle.pqc.math.linearalgebra.GF2Matrix(this.numRows, this.numColumns);
/*      */     
/* 1054 */     for (int i = this.numColumns - 1; i >= 0; i--) {
/*      */       
/* 1056 */       int j = i >>> 5;
/* 1057 */       int k = i & 0x1F;
/* 1058 */       int m = arrayOfInt[i] >>> 5;
/* 1059 */       int n = arrayOfInt[i] & 0x1F;
/* 1060 */       for (int i1 = this.numRows - 1; i1 >= 0; i1--)
/*      */       {
/* 1062 */         gF2Matrix.matrix[i1][j] = gF2Matrix.matrix[i1][j] | (this.matrix[i1][m] >>> n & 0x1) << k;
/*      */       }
/*      */     } 
/*      */     
/* 1066 */     return gF2Matrix;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector rightMultiply(Vector paramVector) {
/* 1077 */     if (!(paramVector instanceof GF2Vector))
/*      */     {
/* 1079 */       throw new ArithmeticException("vector is not defined over GF(2)");
/*      */     }
/*      */     
/* 1082 */     if (paramVector.length != this.numColumns)
/*      */     {
/* 1084 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/* 1087 */     int[] arrayOfInt1 = ((GF2Vector)paramVector).getVecArray();
/* 1088 */     int[] arrayOfInt2 = new int[this.numRows + 31 >>> 5];
/*      */     
/* 1090 */     for (byte b = 0; b < this.numRows; b++) {
/*      */ 
/*      */       
/* 1093 */       int i = 0; int j;
/* 1094 */       for (j = 0; j < this.length; j++)
/*      */       {
/* 1096 */         i ^= this.matrix[b][j] & arrayOfInt1[j];
/*      */       }
/*      */       
/* 1099 */       j = 0;
/* 1100 */       for (byte b1 = 0; b1 < 32; b1++)
/*      */       {
/* 1102 */         j ^= i >>> b1 & 0x1;
/*      */       }
/*      */       
/* 1105 */       if (j == 1)
/*      */       {
/* 1107 */         arrayOfInt2[b >>> 5] = arrayOfInt2[b >>> 5] | 1 << (b & 0x1F);
/*      */       }
/*      */     } 
/*      */     
/* 1111 */     return (Vector)new GF2Vector(arrayOfInt2, this.numRows);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector rightMultiplyRightCompactForm(Vector paramVector) {
/* 1124 */     if (!(paramVector instanceof GF2Vector))
/*      */     {
/* 1126 */       throw new ArithmeticException("vector is not defined over GF(2)");
/*      */     }
/*      */     
/* 1129 */     if (paramVector.length != this.numColumns + this.numRows)
/*      */     {
/* 1131 */       throw new ArithmeticException("length mismatch");
/*      */     }
/*      */     
/* 1134 */     int[] arrayOfInt1 = ((GF2Vector)paramVector).getVecArray();
/* 1135 */     int[] arrayOfInt2 = new int[this.numRows + 31 >>> 5];
/*      */     
/* 1137 */     int i = this.numRows >> 5;
/* 1138 */     int j = this.numRows & 0x1F;
/*      */ 
/*      */     
/* 1141 */     for (byte b = 0; b < this.numRows; b++) {
/*      */ 
/*      */       
/* 1144 */       int k = arrayOfInt1[b >> 5] >>> (b & 0x1F) & 0x1;
/*      */ 
/*      */       
/* 1147 */       int m = i;
/*      */       
/* 1149 */       if (j != 0) {
/*      */         
/* 1151 */         int i1 = 0;
/*      */         
/* 1153 */         for (byte b2 = 0; b2 < this.length - 1; b2++) {
/*      */ 
/*      */           
/* 1156 */           i1 = arrayOfInt1[m++] >>> j | arrayOfInt1[m] << 32 - j;
/* 1157 */           k ^= this.matrix[b][b2] & i1;
/*      */         } 
/*      */         
/* 1160 */         i1 = arrayOfInt1[m++] >>> j;
/* 1161 */         if (m < arrayOfInt1.length)
/*      */         {
/* 1163 */           i1 |= arrayOfInt1[m] << 32 - j;
/*      */         }
/* 1165 */         k ^= this.matrix[b][this.length - 1] & i1;
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1170 */         for (byte b2 = 0; b2 < this.length; b2++)
/*      */         {
/* 1172 */           k ^= this.matrix[b][b2] & arrayOfInt1[m++];
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1177 */       int n = 0;
/* 1178 */       for (byte b1 = 0; b1 < 32; b1++) {
/*      */         
/* 1180 */         n ^= k & 0x1;
/* 1181 */         k >>>= 1;
/*      */       } 
/*      */ 
/*      */       
/* 1185 */       if (n == 1)
/*      */       {
/* 1187 */         arrayOfInt2[b >> 5] = arrayOfInt2[b >> 5] | 1 << (b & 0x1F);
/*      */       }
/*      */     } 
/*      */     
/* 1191 */     return (Vector)new GF2Vector(arrayOfInt2, this.numRows);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object paramObject) {
/* 1203 */     if (!(paramObject instanceof org.bouncycastle.pqc.math.linearalgebra.GF2Matrix))
/*      */     {
/* 1205 */       return false;
/*      */     }
/* 1207 */     org.bouncycastle.pqc.math.linearalgebra.GF2Matrix gF2Matrix = (org.bouncycastle.pqc.math.linearalgebra.GF2Matrix)paramObject;
/*      */     
/* 1209 */     if (this.numRows != gF2Matrix.numRows || this.numColumns != gF2Matrix.numColumns || this.length != gF2Matrix.length)
/*      */     {
/*      */ 
/*      */       
/* 1213 */       return false;
/*      */     }
/*      */     
/* 1216 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/* 1218 */       if (!IntUtils.equals(this.matrix[b], gF2Matrix.matrix[b]))
/*      */       {
/* 1220 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1224 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1232 */     int i = (this.numRows * 31 + this.numColumns) * 31 + this.length;
/* 1233 */     for (byte b = 0; b < this.numRows; b++)
/*      */     {
/* 1235 */       i = i * 31 + Arrays.hashCode(this.matrix[b]);
/*      */     }
/* 1237 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1245 */     int j, i = this.numColumns & 0x1F;
/*      */     
/* 1247 */     if (i == 0) {
/*      */       
/* 1249 */       j = this.length;
/*      */     }
/*      */     else {
/*      */       
/* 1253 */       j = this.length - 1;
/*      */     } 
/*      */     
/* 1256 */     StringBuffer stringBuffer = new StringBuffer();
/* 1257 */     for (byte b = 0; b < this.numRows; b++) {
/*      */       
/* 1259 */       stringBuffer.append("" + b + ": "); int k;
/* 1260 */       for (k = 0; k < j; k++) {
/*      */         
/* 1262 */         int m = this.matrix[b][k];
/* 1263 */         for (byte b2 = 0; b2 < 32; b2++) {
/*      */           
/* 1265 */           int n = m >>> b2 & 0x1;
/* 1266 */           if (n == 0) {
/*      */             
/* 1268 */             stringBuffer.append('0');
/*      */           }
/*      */           else {
/*      */             
/* 1272 */             stringBuffer.append('1');
/*      */           } 
/*      */         } 
/* 1275 */         stringBuffer.append(' ');
/*      */       } 
/* 1277 */       k = this.matrix[b][this.length - 1];
/* 1278 */       for (byte b1 = 0; b1 < i; b1++) {
/*      */         
/* 1280 */         int m = k >>> b1 & 0x1;
/* 1281 */         if (m == 0) {
/*      */           
/* 1283 */           stringBuffer.append('0');
/*      */         }
/*      */         else {
/*      */           
/* 1287 */           stringBuffer.append('1');
/*      */         } 
/*      */       } 
/* 1290 */       stringBuffer.append('\n');
/*      */     } 
/*      */     
/* 1293 */     return stringBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void swapRows(int[][] paramArrayOfint, int paramInt1, int paramInt2) {
/* 1305 */     int[] arrayOfInt = paramArrayOfint[paramInt1];
/* 1306 */     paramArrayOfint[paramInt1] = paramArrayOfint[paramInt2];
/* 1307 */     paramArrayOfint[paramInt2] = arrayOfInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addToRow(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt) {
/* 1319 */     for (int i = paramArrayOfint2.length - 1; i >= paramInt; i--)
/*      */     {
/* 1321 */       paramArrayOfint2[i] = paramArrayOfint1[i] ^ paramArrayOfint2[i];
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/GF2Matrix.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
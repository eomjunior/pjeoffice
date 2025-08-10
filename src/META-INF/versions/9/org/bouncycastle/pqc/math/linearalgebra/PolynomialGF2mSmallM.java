/*      */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*      */ 
/*      */ import java.security.SecureRandom;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.GF2mVector;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
/*      */ import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
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
/*      */ public class PolynomialGF2mSmallM
/*      */ {
/*      */   private GF2mField field;
/*      */   private int degree;
/*      */   private int[] coefficients;
/*      */   public static final char RANDOM_IRREDUCIBLE_POLYNOMIAL = 'I';
/*      */   
/*      */   public PolynomialGF2mSmallM(GF2mField paramGF2mField) {
/*   51 */     this.field = paramGF2mField;
/*   52 */     this.degree = -1;
/*   53 */     this.coefficients = new int[1];
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
/*      */   public PolynomialGF2mSmallM(GF2mField paramGF2mField, int paramInt, char paramChar, SecureRandom paramSecureRandom) {
/*   67 */     this.field = paramGF2mField;
/*      */     
/*   69 */     switch (paramChar) {
/*      */       
/*      */       case 'I':
/*   72 */         this.coefficients = createRandomIrreduciblePolynomial(paramInt, paramSecureRandom);
/*      */         break;
/*      */       default:
/*   75 */         throw new IllegalArgumentException(" Error: type " + paramChar + " is not defined for GF2smallmPolynomial");
/*      */     } 
/*      */ 
/*      */     
/*   79 */     computeDegree();
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
/*      */   private int[] createRandomIrreduciblePolynomial(int paramInt, SecureRandom paramSecureRandom) {
/*   92 */     int[] arrayOfInt = new int[paramInt + 1];
/*   93 */     arrayOfInt[paramInt] = 1;
/*   94 */     arrayOfInt[0] = this.field.getRandomNonZeroElement(paramSecureRandom); int i;
/*   95 */     for (i = 1; i < paramInt; i++)
/*      */     {
/*   97 */       arrayOfInt[i] = this.field.getRandomElement(paramSecureRandom);
/*      */     }
/*   99 */     while (!isIrreducible(arrayOfInt)) {
/*      */       
/*  101 */       i = RandUtils.nextInt(paramSecureRandom, paramInt);
/*  102 */       if (i == 0) {
/*      */         
/*  104 */         arrayOfInt[0] = this.field.getRandomNonZeroElement(paramSecureRandom);
/*      */         
/*      */         continue;
/*      */       } 
/*  108 */       arrayOfInt[i] = this.field.getRandomElement(paramSecureRandom);
/*      */     } 
/*      */     
/*  111 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PolynomialGF2mSmallM(GF2mField paramGF2mField, int paramInt) {
/*  122 */     this.field = paramGF2mField;
/*  123 */     this.degree = paramInt;
/*  124 */     this.coefficients = new int[paramInt + 1];
/*  125 */     this.coefficients[paramInt] = 1;
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
/*      */   public PolynomialGF2mSmallM(GF2mField paramGF2mField, int[] paramArrayOfint) {
/*  137 */     this.field = paramGF2mField;
/*  138 */     this.coefficients = normalForm(paramArrayOfint);
/*  139 */     computeDegree();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PolynomialGF2mSmallM(GF2mField paramGF2mField, byte[] paramArrayOfbyte) {
/*  150 */     this.field = paramGF2mField;
/*      */ 
/*      */     
/*  153 */     byte b1 = 8;
/*  154 */     byte b2 = 1;
/*  155 */     while (paramGF2mField.getDegree() > b1) {
/*      */       
/*  157 */       b2++;
/*  158 */       b1 += 8;
/*      */     } 
/*      */     
/*  161 */     if (paramArrayOfbyte.length % b2 != 0)
/*      */     {
/*  163 */       throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
/*      */     }
/*      */ 
/*      */     
/*  167 */     this.coefficients = new int[paramArrayOfbyte.length / b2];
/*  168 */     b2 = 0;
/*  169 */     for (byte b3 = 0; b3 < this.coefficients.length; b3++) {
/*      */       
/*  171 */       for (byte b = 0; b < b1; b += 8)
/*      */       {
/*  173 */         this.coefficients[b3] = this.coefficients[b3] ^ (paramArrayOfbyte[b2++] & 0xFF) << b;
/*      */       }
/*  175 */       if (!this.field.isElementOfThisField(this.coefficients[b3]))
/*      */       {
/*  177 */         throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  182 */     if (this.coefficients.length != 1 && this.coefficients[this.coefficients.length - 1] == 0)
/*      */     {
/*      */       
/*  185 */       throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
/*      */     }
/*      */     
/*  188 */     computeDegree();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PolynomialGF2mSmallM(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  199 */     this.field = paramPolynomialGF2mSmallM.field;
/*  200 */     this.degree = paramPolynomialGF2mSmallM.degree;
/*  201 */     this.coefficients = IntUtils.clone(paramPolynomialGF2mSmallM.coefficients);
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
/*      */   public PolynomialGF2mSmallM(GF2mVector paramGF2mVector) {
/*  213 */     this(paramGF2mVector.getField(), paramGF2mVector.getIntArrayForm());
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
/*      */   
/*      */   public int getDegree() {
/*  228 */     int i = this.coefficients.length - 1;
/*  229 */     if (this.coefficients[i] == 0)
/*      */     {
/*  231 */       return -1;
/*      */     }
/*  233 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeadCoefficient() {
/*  241 */     if (this.degree == -1)
/*      */     {
/*  243 */       return 0;
/*      */     }
/*  245 */     return this.coefficients[this.degree];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int headCoefficient(int[] paramArrayOfint) {
/*  256 */     int i = computeDegree(paramArrayOfint);
/*  257 */     if (i == -1)
/*      */     {
/*  259 */       return 0;
/*      */     }
/*  261 */     return paramArrayOfint[i];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCoefficient(int paramInt) {
/*  272 */     if (paramInt < 0 || paramInt > this.degree)
/*      */     {
/*  274 */       return 0;
/*      */     }
/*  276 */     return this.coefficients[paramInt];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getEncoded() {
/*  286 */     byte b1 = 8;
/*  287 */     byte b2 = 1;
/*  288 */     while (this.field.getDegree() > b1) {
/*      */       
/*  290 */       b2++;
/*  291 */       b1 += 8;
/*      */     } 
/*      */     
/*  294 */     byte[] arrayOfByte = new byte[this.coefficients.length * b2];
/*  295 */     b2 = 0;
/*  296 */     for (byte b3 = 0; b3 < this.coefficients.length; b3++) {
/*      */       
/*  298 */       for (byte b = 0; b < b1; b += 8)
/*      */       {
/*  300 */         arrayOfByte[b2++] = (byte)(this.coefficients[b3] >>> b);
/*      */       }
/*      */     } 
/*      */     
/*  304 */     return arrayOfByte;
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
/*      */   public int evaluateAt(int paramInt) {
/*  316 */     int i = this.coefficients[this.degree];
/*  317 */     for (int j = this.degree - 1; j >= 0; j--)
/*      */     {
/*  319 */       i = this.field.mult(i, paramInt) ^ this.coefficients[j];
/*      */     }
/*  321 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM add(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  332 */     int[] arrayOfInt = add(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  333 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addToThis(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  343 */     this.coefficients = add(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  344 */     computeDegree();
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
/*      */   private int[] add(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*      */     int[] arrayOfInt1;
/*      */     int[] arrayOfInt2;
/*  358 */     if (paramArrayOfint1.length < paramArrayOfint2.length) {
/*      */       
/*  360 */       arrayOfInt1 = new int[paramArrayOfint2.length];
/*  361 */       System.arraycopy(paramArrayOfint2, 0, arrayOfInt1, 0, paramArrayOfint2.length);
/*  362 */       arrayOfInt2 = paramArrayOfint1;
/*      */     }
/*      */     else {
/*      */       
/*  366 */       arrayOfInt1 = new int[paramArrayOfint1.length];
/*  367 */       System.arraycopy(paramArrayOfint1, 0, arrayOfInt1, 0, paramArrayOfint1.length);
/*  368 */       arrayOfInt2 = paramArrayOfint2;
/*      */     } 
/*      */     
/*  371 */     for (int i = arrayOfInt2.length - 1; i >= 0; i--)
/*      */     {
/*  373 */       arrayOfInt1[i] = this.field.add(arrayOfInt1[i], arrayOfInt2[i]);
/*      */     }
/*      */     
/*  376 */     return arrayOfInt1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM addMonomial(int paramInt) {
/*  387 */     int[] arrayOfInt1 = new int[paramInt + 1];
/*  388 */     arrayOfInt1[paramInt] = 1;
/*  389 */     int[] arrayOfInt2 = add(this.coefficients, arrayOfInt1);
/*  390 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt2);
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM multWithElement(int paramInt) {
/*  403 */     if (!this.field.isElementOfThisField(paramInt))
/*      */     {
/*  405 */       throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
/*      */     }
/*      */     
/*  408 */     int[] arrayOfInt = multWithElement(this.coefficients, paramInt);
/*  409 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   public void multThisWithElement(int paramInt) {
/*  421 */     if (!this.field.isElementOfThisField(paramInt))
/*      */     {
/*  423 */       throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
/*      */     }
/*      */     
/*  426 */     this.coefficients = multWithElement(this.coefficients, paramInt);
/*  427 */     computeDegree();
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
/*      */   private int[] multWithElement(int[] paramArrayOfint, int paramInt) {
/*  440 */     int i = computeDegree(paramArrayOfint);
/*  441 */     if (i == -1 || paramInt == 0)
/*      */     {
/*  443 */       return new int[1];
/*      */     }
/*      */     
/*  446 */     if (paramInt == 1)
/*      */     {
/*  448 */       return IntUtils.clone(paramArrayOfint);
/*      */     }
/*      */     
/*  451 */     int[] arrayOfInt = new int[i + 1];
/*  452 */     for (int j = i; j >= 0; j--)
/*      */     {
/*  454 */       arrayOfInt[j] = this.field.mult(paramArrayOfint[j], paramInt);
/*      */     }
/*      */     
/*  457 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM multWithMonomial(int paramInt) {
/*  468 */     int[] arrayOfInt = multWithMonomial(this.coefficients, paramInt);
/*  469 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   private static int[] multWithMonomial(int[] paramArrayOfint, int paramInt) {
/*  481 */     int i = computeDegree(paramArrayOfint);
/*  482 */     if (i == -1)
/*      */     {
/*  484 */       return new int[1];
/*      */     }
/*  486 */     int[] arrayOfInt = new int[i + paramInt + 1];
/*  487 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, paramInt, i + 1);
/*  488 */     return arrayOfInt;
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM[] div(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  500 */     int[][] arrayOfInt = div(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  501 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM[] { new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt[0]), new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt[1]) };
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
/*      */   
/*      */   private int[][] div(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  516 */     int i = computeDegree(paramArrayOfint2);
/*  517 */     int j = computeDegree(paramArrayOfint1) + 1;
/*  518 */     if (i == -1)
/*      */     {
/*  520 */       throw new ArithmeticException("Division by zero.");
/*      */     }
/*  522 */     int[][] arrayOfInt = new int[2][];
/*  523 */     arrayOfInt[0] = new int[1];
/*  524 */     arrayOfInt[1] = new int[j];
/*  525 */     int k = headCoefficient(paramArrayOfint2);
/*  526 */     k = this.field.inverse(k);
/*  527 */     arrayOfInt[0][0] = 0;
/*  528 */     System.arraycopy(paramArrayOfint1, 0, arrayOfInt[1], 0, (arrayOfInt[1]).length);
/*  529 */     while (i <= computeDegree(arrayOfInt[1])) {
/*      */ 
/*      */       
/*  532 */       int[] arrayOfInt2 = new int[1];
/*  533 */       arrayOfInt2[0] = this.field.mult(headCoefficient(arrayOfInt[1]), k);
/*  534 */       int[] arrayOfInt1 = multWithElement(paramArrayOfint2, arrayOfInt2[0]);
/*  535 */       int m = computeDegree(arrayOfInt[1]) - i;
/*  536 */       arrayOfInt1 = multWithMonomial(arrayOfInt1, m);
/*  537 */       arrayOfInt2 = multWithMonomial(arrayOfInt2, m);
/*  538 */       arrayOfInt[0] = add(arrayOfInt2, arrayOfInt[0]);
/*  539 */       arrayOfInt[1] = add(arrayOfInt1, arrayOfInt[1]);
/*      */     } 
/*  541 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM gcd(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  552 */     int[] arrayOfInt = gcd(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  553 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   private int[] gcd(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  566 */     int[] arrayOfInt1 = paramArrayOfint1;
/*  567 */     int[] arrayOfInt2 = paramArrayOfint2;
/*  568 */     if (computeDegree(arrayOfInt1) == -1)
/*      */     {
/*  570 */       return arrayOfInt2;
/*      */     }
/*  572 */     while (computeDegree(arrayOfInt2) != -1) {
/*      */       
/*  574 */       int[] arrayOfInt = mod(arrayOfInt1, arrayOfInt2);
/*  575 */       arrayOfInt1 = new int[arrayOfInt2.length];
/*  576 */       System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, arrayOfInt1.length);
/*  577 */       arrayOfInt2 = new int[arrayOfInt.length];
/*  578 */       System.arraycopy(arrayOfInt, 0, arrayOfInt2, 0, arrayOfInt2.length);
/*      */     } 
/*  580 */     int i = this.field.inverse(headCoefficient(arrayOfInt1));
/*  581 */     return multWithElement(arrayOfInt1, i);
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM multiply(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  593 */     int[] arrayOfInt = multiply(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  594 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   private int[] multiply(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  608 */     if (computeDegree(paramArrayOfint1) < computeDegree(paramArrayOfint2)) {
/*      */       
/*  610 */       arrayOfInt1 = paramArrayOfint2;
/*  611 */       arrayOfInt2 = paramArrayOfint1;
/*      */     }
/*      */     else {
/*      */       
/*  615 */       arrayOfInt1 = paramArrayOfint1;
/*  616 */       arrayOfInt2 = paramArrayOfint2;
/*      */     } 
/*      */     
/*  619 */     int[] arrayOfInt1 = normalForm(arrayOfInt1);
/*  620 */     int[] arrayOfInt2 = normalForm(arrayOfInt2);
/*      */     
/*  622 */     if (arrayOfInt2.length == 1)
/*      */     {
/*  624 */       return multWithElement(arrayOfInt1, arrayOfInt2[0]);
/*      */     }
/*      */     
/*  627 */     int i = arrayOfInt1.length;
/*  628 */     int j = arrayOfInt2.length;
/*  629 */     int[] arrayOfInt3 = new int[i + j - 1];
/*      */     
/*  631 */     if (j != i) {
/*      */       
/*  633 */       int[] arrayOfInt4 = new int[j];
/*  634 */       int[] arrayOfInt5 = new int[i - j];
/*  635 */       System.arraycopy(arrayOfInt1, 0, arrayOfInt4, 0, arrayOfInt4.length);
/*  636 */       System.arraycopy(arrayOfInt1, j, arrayOfInt5, 0, arrayOfInt5.length);
/*  637 */       arrayOfInt4 = multiply(arrayOfInt4, arrayOfInt2);
/*  638 */       arrayOfInt5 = multiply(arrayOfInt5, arrayOfInt2);
/*  639 */       arrayOfInt5 = multWithMonomial(arrayOfInt5, j);
/*  640 */       arrayOfInt3 = add(arrayOfInt4, arrayOfInt5);
/*      */     }
/*      */     else {
/*      */       
/*  644 */       j = i + 1 >>> 1;
/*  645 */       int k = i - j;
/*  646 */       int[] arrayOfInt4 = new int[j];
/*  647 */       int[] arrayOfInt5 = new int[j];
/*  648 */       int[] arrayOfInt6 = new int[k];
/*  649 */       int[] arrayOfInt7 = new int[k];
/*      */       
/*  651 */       System.arraycopy(arrayOfInt1, 0, arrayOfInt4, 0, arrayOfInt4.length);
/*      */       
/*  653 */       System.arraycopy(arrayOfInt1, j, arrayOfInt6, 0, arrayOfInt6.length);
/*      */ 
/*      */       
/*  656 */       System.arraycopy(arrayOfInt2, 0, arrayOfInt5, 0, arrayOfInt5.length);
/*      */       
/*  658 */       System.arraycopy(arrayOfInt2, j, arrayOfInt7, 0, arrayOfInt7.length);
/*      */       
/*  660 */       int[] arrayOfInt8 = add(arrayOfInt4, arrayOfInt6);
/*  661 */       int[] arrayOfInt9 = add(arrayOfInt5, arrayOfInt7);
/*  662 */       int[] arrayOfInt10 = multiply(arrayOfInt4, arrayOfInt5);
/*  663 */       int[] arrayOfInt11 = multiply(arrayOfInt8, arrayOfInt9);
/*  664 */       int[] arrayOfInt12 = multiply(arrayOfInt6, arrayOfInt7);
/*  665 */       arrayOfInt11 = add(arrayOfInt11, arrayOfInt10);
/*  666 */       arrayOfInt11 = add(arrayOfInt11, arrayOfInt12);
/*  667 */       arrayOfInt12 = multWithMonomial(arrayOfInt12, j);
/*  668 */       arrayOfInt3 = add(arrayOfInt11, arrayOfInt12);
/*  669 */       arrayOfInt3 = multWithMonomial(arrayOfInt3, j);
/*  670 */       arrayOfInt3 = add(arrayOfInt3, arrayOfInt10);
/*      */     } 
/*      */     
/*  673 */     return arrayOfInt3;
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
/*      */ 
/*      */   
/*      */   private boolean isIrreducible(int[] paramArrayOfint) {
/*  689 */     if (paramArrayOfint[0] == 0)
/*      */     {
/*  691 */       return false;
/*      */     }
/*  693 */     int i = computeDegree(paramArrayOfint) >> 1;
/*  694 */     int[] arrayOfInt1 = { 0, 1 };
/*  695 */     int[] arrayOfInt2 = { 0, 1 };
/*  696 */     int j = this.field.getDegree();
/*  697 */     for (byte b = 0; b < i; b++) {
/*      */       
/*  699 */       for (int k = j - 1; k >= 0; k--)
/*      */       {
/*  701 */         arrayOfInt1 = modMultiply(arrayOfInt1, arrayOfInt1, paramArrayOfint);
/*      */       }
/*  703 */       arrayOfInt1 = normalForm(arrayOfInt1);
/*  704 */       int[] arrayOfInt = gcd(add(arrayOfInt1, arrayOfInt2), paramArrayOfint);
/*  705 */       if (computeDegree(arrayOfInt) != 0)
/*      */       {
/*  707 */         return false;
/*      */       }
/*      */     } 
/*  710 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM mod(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  721 */     int[] arrayOfInt = mod(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  722 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   private int[] mod(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  734 */     int i = computeDegree(paramArrayOfint2);
/*  735 */     if (i == -1)
/*      */     {
/*  737 */       throw new ArithmeticException("Division by zero");
/*      */     }
/*  739 */     int[] arrayOfInt = new int[paramArrayOfint1.length];
/*  740 */     int j = headCoefficient(paramArrayOfint2);
/*  741 */     j = this.field.inverse(j);
/*  742 */     System.arraycopy(paramArrayOfint1, 0, arrayOfInt, 0, arrayOfInt.length);
/*  743 */     while (i <= computeDegree(arrayOfInt)) {
/*      */ 
/*      */       
/*  746 */       int k = this.field.mult(headCoefficient(arrayOfInt), j);
/*  747 */       int[] arrayOfInt1 = multWithMonomial(paramArrayOfint2, computeDegree(arrayOfInt) - i);
/*  748 */       arrayOfInt1 = multWithElement(arrayOfInt1, k);
/*  749 */       arrayOfInt = add(arrayOfInt1, arrayOfInt);
/*      */     } 
/*  751 */     return arrayOfInt;
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM modMultiply(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM1, org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM2) {
/*  765 */     int[] arrayOfInt = modMultiply(this.coefficients, paramPolynomialGF2mSmallM1.coefficients, paramPolynomialGF2mSmallM2.coefficients);
/*      */     
/*  767 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM modSquareMatrix(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM[] paramArrayOfPolynomialGF2mSmallM) {
/*  780 */     int i = paramArrayOfPolynomialGF2mSmallM.length;
/*      */     
/*  782 */     int[] arrayOfInt1 = new int[i];
/*  783 */     int[] arrayOfInt2 = new int[i];
/*      */     
/*      */     byte b;
/*  786 */     for (b = 0; b < this.coefficients.length; b++)
/*      */     {
/*  788 */       arrayOfInt2[b] = this.field.mult(this.coefficients[b], this.coefficients[b]);
/*      */     }
/*      */ 
/*      */     
/*  792 */     for (b = 0; b < i; b++) {
/*      */ 
/*      */       
/*  795 */       for (byte b1 = 0; b1 < i; b1++) {
/*      */         
/*  797 */         if (b < (paramArrayOfPolynomialGF2mSmallM[b1]).coefficients.length) {
/*      */ 
/*      */ 
/*      */           
/*  801 */           int j = this.field.mult((paramArrayOfPolynomialGF2mSmallM[b1]).coefficients[b], arrayOfInt2[b1]);
/*      */           
/*  803 */           arrayOfInt1[b] = this.field.add(arrayOfInt1[b], j);
/*      */         } 
/*      */       } 
/*      */     } 
/*  807 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt1);
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
/*      */   private int[] modMultiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  821 */     return mod(multiply(paramArrayOfint1, paramArrayOfint2), paramArrayOfint3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM modSquareRoot(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  832 */     int[] arrayOfInt1 = IntUtils.clone(this.coefficients);
/*  833 */     int[] arrayOfInt2 = modMultiply(arrayOfInt1, arrayOfInt1, paramPolynomialGF2mSmallM.coefficients);
/*  834 */     while (!isEqual(arrayOfInt2, this.coefficients)) {
/*      */       
/*  836 */       arrayOfInt1 = normalForm(arrayOfInt2);
/*  837 */       arrayOfInt2 = modMultiply(arrayOfInt1, arrayOfInt1, paramPolynomialGF2mSmallM.coefficients);
/*      */     } 
/*      */     
/*  840 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt1);
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
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM modSquareRootMatrix(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM[] paramArrayOfPolynomialGF2mSmallM) {
/*  856 */     int i = paramArrayOfPolynomialGF2mSmallM.length;
/*      */     
/*  858 */     int[] arrayOfInt = new int[i];
/*      */     
/*      */     byte b;
/*  861 */     for (b = 0; b < i; b++) {
/*      */ 
/*      */       
/*  864 */       for (byte b1 = 0; b1 < i; b1++) {
/*      */         
/*  866 */         if (b < (paramArrayOfPolynomialGF2mSmallM[b1]).coefficients.length)
/*      */         {
/*      */ 
/*      */           
/*  870 */           if (b1 < this.coefficients.length) {
/*      */             
/*  872 */             int j = this.field.mult((paramArrayOfPolynomialGF2mSmallM[b1]).coefficients[b], this.coefficients[b1]);
/*      */             
/*  874 */             arrayOfInt[b] = this.field.add(arrayOfInt[b], j);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  880 */     for (b = 0; b < i; b++)
/*      */     {
/*  882 */       arrayOfInt[b] = this.field.sqRoot(arrayOfInt[b]);
/*      */     }
/*      */     
/*  885 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM modDiv(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM1, org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM2) {
/*  899 */     int[] arrayOfInt = modDiv(this.coefficients, paramPolynomialGF2mSmallM1.coefficients, paramPolynomialGF2mSmallM2.coefficients);
/*      */     
/*  901 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt);
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
/*      */   private int[] modDiv(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  915 */     int[] arrayOfInt1 = normalForm(paramArrayOfint3);
/*  916 */     int[] arrayOfInt2 = mod(paramArrayOfint2, paramArrayOfint3);
/*  917 */     int[] arrayOfInt3 = { 0 };
/*  918 */     int[] arrayOfInt4 = mod(paramArrayOfint1, paramArrayOfint3);
/*      */ 
/*      */     
/*  921 */     while (computeDegree(arrayOfInt2) != -1) {
/*      */       
/*  923 */       int[][] arrayOfInt5 = div(arrayOfInt1, arrayOfInt2);
/*  924 */       arrayOfInt1 = normalForm(arrayOfInt2);
/*  925 */       arrayOfInt2 = normalForm(arrayOfInt5[1]);
/*  926 */       int[] arrayOfInt = add(arrayOfInt3, modMultiply(arrayOfInt5[0], arrayOfInt4, paramArrayOfint3));
/*  927 */       arrayOfInt3 = normalForm(arrayOfInt4);
/*  928 */       arrayOfInt4 = normalForm(arrayOfInt);
/*      */     } 
/*      */     
/*  931 */     int i = headCoefficient(arrayOfInt1);
/*  932 */     arrayOfInt3 = multWithElement(arrayOfInt3, this.field.inverse(i));
/*  933 */     return arrayOfInt3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM modInverse(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  944 */     int[] arrayOfInt1 = { 1 };
/*  945 */     int[] arrayOfInt2 = modDiv(arrayOfInt1, this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  946 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt2);
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
/*      */   public org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM[] modPolynomialToFracton(org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM paramPolynomialGF2mSmallM) {
/*  959 */     int i = paramPolynomialGF2mSmallM.degree >> 1;
/*  960 */     int[] arrayOfInt1 = normalForm(paramPolynomialGF2mSmallM.coefficients);
/*  961 */     int[] arrayOfInt2 = mod(this.coefficients, paramPolynomialGF2mSmallM.coefficients);
/*  962 */     int[] arrayOfInt3 = { 0 };
/*  963 */     int[] arrayOfInt4 = { 1 };
/*  964 */     while (computeDegree(arrayOfInt2) > i) {
/*      */       
/*  966 */       int[][] arrayOfInt = div(arrayOfInt1, arrayOfInt2);
/*  967 */       arrayOfInt1 = arrayOfInt2;
/*  968 */       arrayOfInt2 = arrayOfInt[1];
/*  969 */       int[] arrayOfInt5 = add(arrayOfInt3, modMultiply(arrayOfInt[0], arrayOfInt4, paramPolynomialGF2mSmallM.coefficients));
/*  970 */       arrayOfInt3 = arrayOfInt4;
/*  971 */       arrayOfInt4 = arrayOfInt5;
/*      */     } 
/*      */     
/*  974 */     return new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM[] { new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt2), new org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM(this.field, arrayOfInt4) };
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
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object paramObject) {
/*  991 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM))
/*      */     {
/*  993 */       return false;
/*      */     }
/*      */     
/*  996 */     org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM polynomialGF2mSmallM = (org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM)paramObject;
/*      */     
/*  998 */     if (this.field.equals(polynomialGF2mSmallM.field) && this.degree == polynomialGF2mSmallM.degree && 
/*  999 */       isEqual(this.coefficients, polynomialGF2mSmallM.coefficients))
/*      */     {
/* 1001 */       return true;
/*      */     }
/*      */     
/* 1004 */     return false;
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
/*      */   private static boolean isEqual(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 1017 */     int i = computeDegree(paramArrayOfint1);
/* 1018 */     int j = computeDegree(paramArrayOfint2);
/* 1019 */     if (i != j)
/*      */     {
/* 1021 */       return false;
/*      */     }
/* 1023 */     for (byte b = 0; b <= i; b++) {
/*      */       
/* 1025 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/*      */       {
/* 1027 */         return false;
/*      */       }
/*      */     } 
/* 1030 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1038 */     int i = this.field.hashCode();
/* 1039 */     for (byte b = 0; b < this.coefficients.length; b++)
/*      */     {
/* 1041 */       i = i * 31 + this.coefficients[b];
/*      */     }
/* 1043 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1053 */     String str = " Polynomial over " + this.field.toString() + ": \n";
/*      */     
/* 1055 */     for (byte b = 0; b < this.coefficients.length; b++)
/*      */     {
/* 1057 */       str = str + str + "Y^" + this.field.elementToStr(this.coefficients[b]) + "+";
/*      */     }
/* 1059 */     str = str + ";";
/*      */     
/* 1061 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void computeDegree() {
/* 1070 */     this.degree = this.coefficients.length - 1;
/* 1071 */     for (; this.degree >= 0 && this.coefficients[this.degree] == 0; this.degree--);
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
/*      */   
/*      */   private static int computeDegree(int[] paramArrayOfint) {
/*      */     int i;
/* 1087 */     for (i = paramArrayOfint.length - 1; i >= 0 && paramArrayOfint[i] == 0; i--);
/*      */ 
/*      */ 
/*      */     
/* 1091 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] normalForm(int[] paramArrayOfint) {
/* 1102 */     int i = computeDegree(paramArrayOfint);
/*      */ 
/*      */     
/* 1105 */     if (i == -1)
/*      */     {
/*      */       
/* 1108 */       return new int[1];
/*      */     }
/*      */ 
/*      */     
/* 1112 */     if (paramArrayOfint.length == i + 1)
/*      */     {
/*      */       
/* 1115 */       return IntUtils.clone(paramArrayOfint);
/*      */     }
/*      */ 
/*      */     
/* 1119 */     int[] arrayOfInt = new int[i + 1];
/* 1120 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, i + 1);
/* 1121 */     return arrayOfInt;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/PolynomialGF2mSmallM.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
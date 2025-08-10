/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.crypto.CryptoServicesRegistrar;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
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
/*     */ public class GF2mField
/*     */ {
/*  27 */   private int degree = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int polynomial;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField(int paramInt) {
/*  38 */     if (paramInt >= 32)
/*     */     {
/*  40 */       throw new IllegalArgumentException(" Error: the degree of field is too large ");
/*     */     }
/*     */     
/*  43 */     if (paramInt < 1)
/*     */     {
/*  45 */       throw new IllegalArgumentException(" Error: the degree of field is non-positive ");
/*     */     }
/*     */     
/*  48 */     this.degree = paramInt;
/*  49 */     this.polynomial = PolynomialRingGF2.getIrreduciblePolynomial(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField(int paramInt1, int paramInt2) {
/*  60 */     if (paramInt1 != PolynomialRingGF2.degree(paramInt2))
/*     */     {
/*  62 */       throw new IllegalArgumentException(" Error: the degree is not correct");
/*     */     }
/*     */     
/*  65 */     if (!PolynomialRingGF2.isIrreducible(paramInt2))
/*     */     {
/*  67 */       throw new IllegalArgumentException(" Error: given polynomial is reducible");
/*     */     }
/*     */     
/*  70 */     this.degree = paramInt1;
/*  71 */     this.polynomial = paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField(byte[] paramArrayOfbyte) {
/*  77 */     if (paramArrayOfbyte.length != 4)
/*     */     {
/*  79 */       throw new IllegalArgumentException("byte array is not an encoded finite field");
/*     */     }
/*     */     
/*  82 */     this.polynomial = LittleEndianConversions.OS2IP(paramArrayOfbyte);
/*  83 */     if (!PolynomialRingGF2.isIrreducible(this.polynomial))
/*     */     {
/*  85 */       throw new IllegalArgumentException("byte array is not an encoded finite field");
/*     */     }
/*     */ 
/*     */     
/*  89 */     this.degree = PolynomialRingGF2.degree(this.polynomial);
/*     */   }
/*     */ 
/*     */   
/*     */   public GF2mField(org.bouncycastle.pqc.math.linearalgebra.GF2mField paramGF2mField) {
/*  94 */     this.degree = paramGF2mField.degree;
/*  95 */     this.polynomial = paramGF2mField.polynomial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDegree() {
/* 105 */     return this.degree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPolynomial() {
/* 115 */     return this.polynomial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/* 125 */     return LittleEndianConversions.I2OSP(this.polynomial);
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
/*     */   public int add(int paramInt1, int paramInt2) {
/* 137 */     return paramInt1 ^ paramInt2;
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
/*     */   public int mult(int paramInt1, int paramInt2) {
/* 149 */     return PolynomialRingGF2.modMultiply(paramInt1, paramInt2, this.polynomial);
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
/*     */   public int exp(int paramInt1, int paramInt2) {
/* 161 */     if (paramInt2 == 0)
/*     */     {
/* 163 */       return 1;
/*     */     }
/* 165 */     if (paramInt1 == 0)
/*     */     {
/* 167 */       return 0;
/*     */     }
/* 169 */     if (paramInt1 == 1)
/*     */     {
/* 171 */       return 1;
/*     */     }
/* 173 */     int i = 1;
/* 174 */     if (paramInt2 < 0) {
/*     */       
/* 176 */       paramInt1 = inverse(paramInt1);
/* 177 */       paramInt2 = -paramInt2;
/*     */     } 
/* 179 */     while (paramInt2 != 0) {
/*     */       
/* 181 */       if ((paramInt2 & 0x1) == 1)
/*     */       {
/* 183 */         i = mult(i, paramInt1);
/*     */       }
/* 185 */       paramInt1 = mult(paramInt1, paramInt1);
/* 186 */       paramInt2 >>>= 1;
/*     */     } 
/* 188 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int inverse(int paramInt) {
/* 199 */     int i = (1 << this.degree) - 2;
/*     */     
/* 201 */     return exp(paramInt, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int sqRoot(int paramInt) {
/* 212 */     for (byte b = 1; b < this.degree; b++)
/*     */     {
/* 214 */       paramInt = mult(paramInt, paramInt);
/*     */     }
/* 216 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRandomElement(SecureRandom paramSecureRandom) {
/* 227 */     return RandUtils.nextInt(paramSecureRandom, 1 << this.degree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRandomNonZeroElement() {
/* 238 */     return getRandomNonZeroElement(CryptoServicesRegistrar.getSecureRandom());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRandomNonZeroElement(SecureRandom paramSecureRandom) {
/* 249 */     int i = 1048576;
/* 250 */     byte b = 0;
/* 251 */     int j = RandUtils.nextInt(paramSecureRandom, 1 << this.degree);
/* 252 */     while (j == 0 && b < i) {
/*     */       
/* 254 */       j = RandUtils.nextInt(paramSecureRandom, 1 << this.degree);
/* 255 */       b++;
/*     */     } 
/* 257 */     if (b == i)
/*     */     {
/* 259 */       j = 1;
/*     */     }
/* 261 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isElementOfThisField(int paramInt) {
/* 270 */     if (this.degree == 31)
/*     */     {
/* 272 */       return (paramInt >= 0);
/*     */     }
/* 274 */     return (paramInt >= 0 && paramInt < 1 << this.degree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String elementToStr(int paramInt) {
/* 282 */     String str = "";
/* 283 */     for (byte b = 0; b < this.degree; b++) {
/*     */       
/* 285 */       if (((byte)paramInt & 0x1) == 0) {
/*     */         
/* 287 */         str = "0" + str;
/*     */       }
/*     */       else {
/*     */         
/* 291 */         str = "1" + str;
/*     */       } 
/* 293 */       paramInt >>>= 1;
/*     */     } 
/* 295 */     return str;
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
/*     */   public boolean equals(Object paramObject) {
/* 308 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.math.linearalgebra.GF2mField))
/*     */     {
/* 310 */       return false;
/*     */     }
/*     */     
/* 313 */     org.bouncycastle.pqc.math.linearalgebra.GF2mField gF2mField = (org.bouncycastle.pqc.math.linearalgebra.GF2mField)paramObject;
/*     */     
/* 315 */     if (this.degree == gF2mField.degree && this.polynomial == gF2mField.polynomial)
/*     */     {
/*     */       
/* 318 */       return true;
/*     */     }
/*     */     
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 326 */     return this.polynomial;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 336 */     return "Finite Field GF(2^" + this.degree + ") = GF(2)[X]/<" + 
/* 337 */       polyToString(this.polynomial) + "> ";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String polyToString(int paramInt) {
/* 343 */     String str = "";
/* 344 */     if (paramInt == 0) {
/*     */       
/* 346 */       str = "0";
/*     */     }
/*     */     else {
/*     */       
/* 350 */       byte b = (byte)(paramInt & 0x1);
/* 351 */       if (b == 1)
/*     */       {
/* 353 */         str = "1";
/*     */       }
/* 355 */       paramInt >>>= 1;
/* 356 */       byte b1 = 1;
/* 357 */       while (paramInt != 0) {
/*     */         
/* 359 */         b = (byte)(paramInt & 0x1);
/* 360 */         if (b == 1)
/*     */         {
/* 362 */           str = str + "+x^" + str;
/*     */         }
/* 364 */         paramInt >>>= 1;
/* 365 */         b1++;
/*     */       } 
/*     */     } 
/* 368 */     return str;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/GF2mField.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
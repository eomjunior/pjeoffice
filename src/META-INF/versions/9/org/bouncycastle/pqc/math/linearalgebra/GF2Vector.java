/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mVector;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Vector;
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
/*     */ public class GF2Vector
/*     */   extends Vector
/*     */ {
/*     */   private int[] v;
/*     */   
/*     */   public GF2Vector(int paramInt) {
/*  31 */     if (paramInt < 0)
/*     */     {
/*  33 */       throw new ArithmeticException("Negative length.");
/*     */     }
/*  35 */     this.length = paramInt;
/*  36 */     this.v = new int[paramInt + 31 >> 5];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Vector(int paramInt, SecureRandom paramSecureRandom) {
/*  47 */     this.length = paramInt;
/*     */     
/*  49 */     int i = paramInt + 31 >> 5;
/*  50 */     this.v = new int[i];
/*     */     
/*     */     int j;
/*  53 */     for (j = i - 1; j >= 0; j--)
/*     */     {
/*  55 */       this.v[j] = paramSecureRandom.nextInt();
/*     */     }
/*     */ 
/*     */     
/*  59 */     j = paramInt & 0x1F;
/*  60 */     if (j != 0)
/*     */     {
/*     */       
/*  63 */       this.v[i - 1] = this.v[i - 1] & (1 << j) - 1;
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
/*     */   public GF2Vector(int paramInt1, int paramInt2, SecureRandom paramSecureRandom) {
/*  77 */     if (paramInt2 > paramInt1)
/*     */     {
/*  79 */       throw new ArithmeticException("The hamming weight is greater than the length of vector.");
/*     */     }
/*     */     
/*  82 */     this.length = paramInt1;
/*     */     
/*  84 */     int i = paramInt1 + 31 >> 5;
/*  85 */     this.v = new int[i];
/*     */     
/*  87 */     int[] arrayOfInt = new int[paramInt1]; int j;
/*  88 */     for (j = 0; j < paramInt1; j++)
/*     */     {
/*  90 */       arrayOfInt[j] = j;
/*     */     }
/*     */     
/*  93 */     j = paramInt1;
/*  94 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  96 */       int k = RandUtils.nextInt(paramSecureRandom, j);
/*  97 */       setBit(arrayOfInt[k]);
/*  98 */       j--;
/*  99 */       arrayOfInt[k] = arrayOfInt[j];
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
/*     */   public GF2Vector(int paramInt, int[] paramArrayOfint) {
/* 112 */     if (paramInt < 0)
/*     */     {
/* 114 */       throw new ArithmeticException("negative length");
/*     */     }
/* 116 */     this.length = paramInt;
/*     */     
/* 118 */     int i = paramInt + 31 >> 5;
/*     */     
/* 120 */     if (paramArrayOfint.length != i)
/*     */     {
/* 122 */       throw new ArithmeticException("length mismatch");
/*     */     }
/*     */     
/* 125 */     this.v = IntUtils.clone(paramArrayOfint);
/*     */     
/* 127 */     int j = paramInt & 0x1F;
/* 128 */     if (j != 0)
/*     */     {
/*     */       
/* 131 */       this.v[i - 1] = this.v[i - 1] & (1 << j) - 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Vector(org.bouncycastle.pqc.math.linearalgebra.GF2Vector paramGF2Vector) {
/* 142 */     this.length = paramGF2Vector.length;
/* 143 */     this.v = IntUtils.clone(paramGF2Vector.v);
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
/*     */   protected GF2Vector(int[] paramArrayOfint, int paramInt) {
/* 156 */     this.v = paramArrayOfint;
/* 157 */     this.length = paramInt;
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
/*     */   public static org.bouncycastle.pqc.math.linearalgebra.GF2Vector OS2VP(int paramInt, byte[] paramArrayOfbyte) {
/* 170 */     if (paramInt < 0)
/*     */     {
/* 172 */       throw new ArithmeticException("negative length");
/*     */     }
/*     */     
/* 175 */     int i = paramInt + 7 >> 3;
/*     */     
/* 177 */     if (paramArrayOfbyte.length > i)
/*     */     {
/* 179 */       throw new ArithmeticException("length mismatch");
/*     */     }
/*     */     
/* 182 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(paramInt, LittleEndianConversions.toIntArray(paramArrayOfbyte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/* 192 */     int i = this.length + 7 >> 3;
/* 193 */     return LittleEndianConversions.toByteArray(this.v, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVecArray() {
/* 201 */     return this.v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHammingWeight() {
/* 212 */     byte b1 = 0;
/* 213 */     for (byte b2 = 0; b2 < this.v.length; b2++) {
/*     */       
/* 215 */       int i = this.v[b2];
/* 216 */       for (byte b = 0; b < 32; b++) {
/*     */         
/* 218 */         int j = i & 0x1;
/* 219 */         if (j != 0)
/*     */         {
/* 221 */           b1++;
/*     */         }
/* 223 */         i >>>= 1;
/*     */       } 
/*     */     } 
/* 226 */     return b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 234 */     for (int i = this.v.length - 1; i >= 0; i--) {
/*     */       
/* 236 */       if (this.v[i] != 0)
/*     */       {
/* 238 */         return false;
/*     */       }
/*     */     } 
/* 241 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBit(int paramInt) {
/* 252 */     if (paramInt >= this.length)
/*     */     {
/* 254 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 256 */     int i = paramInt >> 5;
/* 257 */     int j = paramInt & 0x1F;
/* 258 */     return (this.v[i] & 1 << j) >>> j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBit(int paramInt) {
/* 269 */     if (paramInt >= this.length)
/*     */     {
/* 271 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 273 */     this.v[paramInt >> 5] = this.v[paramInt >> 5] | 1 << (paramInt & 0x1F);
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
/*     */   public Vector add(Vector paramVector) {
/* 286 */     if (!(paramVector instanceof org.bouncycastle.pqc.math.linearalgebra.GF2Vector))
/*     */     {
/* 288 */       throw new ArithmeticException("vector is not defined over GF(2)");
/*     */     }
/*     */     
/* 291 */     org.bouncycastle.pqc.math.linearalgebra.GF2Vector gF2Vector = (org.bouncycastle.pqc.math.linearalgebra.GF2Vector)paramVector;
/* 292 */     if (this.length != gF2Vector.length)
/*     */     {
/* 294 */       throw new ArithmeticException("length mismatch");
/*     */     }
/*     */     
/* 297 */     int[] arrayOfInt = IntUtils.clone(((org.bouncycastle.pqc.math.linearalgebra.GF2Vector)paramVector).v);
/*     */     
/* 299 */     for (int i = arrayOfInt.length - 1; i >= 0; i--)
/*     */     {
/* 301 */       arrayOfInt[i] = arrayOfInt[i] ^ this.v[i];
/*     */     }
/*     */     
/* 304 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(this.length, arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector multiply(Permutation paramPermutation) {
/* 315 */     int[] arrayOfInt = paramPermutation.getVector();
/* 316 */     if (this.length != arrayOfInt.length)
/*     */     {
/* 318 */       throw new ArithmeticException("length mismatch");
/*     */     }
/*     */     
/* 321 */     org.bouncycastle.pqc.math.linearalgebra.GF2Vector gF2Vector = new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(this.length);
/*     */     
/* 323 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*     */       
/* 325 */       int i = this.v[arrayOfInt[b] >> 5] & 1 << (arrayOfInt[b] & 0x1F);
/* 326 */       if (i != 0)
/*     */       {
/* 328 */         gF2Vector.v[b >> 5] = gF2Vector.v[b >> 5] | 1 << (b & 0x1F);
/*     */       }
/*     */     } 
/*     */     
/* 332 */     return gF2Vector;
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
/*     */   public org.bouncycastle.pqc.math.linearalgebra.GF2Vector extractVector(int[] paramArrayOfint) {
/* 345 */     int i = paramArrayOfint.length;
/* 346 */     if (paramArrayOfint[i - 1] > this.length)
/*     */     {
/* 348 */       throw new ArithmeticException("invalid index set");
/*     */     }
/*     */     
/* 351 */     org.bouncycastle.pqc.math.linearalgebra.GF2Vector gF2Vector = new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(i);
/*     */     
/* 353 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 355 */       int j = this.v[paramArrayOfint[b] >> 5] & 1 << (paramArrayOfint[b] & 0x1F);
/* 356 */       if (j != 0)
/*     */       {
/* 358 */         gF2Vector.v[b >> 5] = gF2Vector.v[b >> 5] | 1 << (b & 0x1F);
/*     */       }
/*     */     } 
/*     */     
/* 362 */     return gF2Vector;
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
/*     */   public org.bouncycastle.pqc.math.linearalgebra.GF2Vector extractLeftVector(int paramInt) {
/* 375 */     if (paramInt > this.length)
/*     */     {
/* 377 */       throw new ArithmeticException("invalid length");
/*     */     }
/*     */     
/* 380 */     if (paramInt == this.length)
/*     */     {
/* 382 */       return new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(this);
/*     */     }
/*     */     
/* 385 */     org.bouncycastle.pqc.math.linearalgebra.GF2Vector gF2Vector = new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(paramInt);
/*     */     
/* 387 */     int i = paramInt >> 5;
/* 388 */     int j = paramInt & 0x1F;
/*     */     
/* 390 */     System.arraycopy(this.v, 0, gF2Vector.v, 0, i);
/* 391 */     if (j != 0)
/*     */     {
/* 393 */       gF2Vector.v[i] = this.v[i] & (1 << j) - 1;
/*     */     }
/*     */     
/* 396 */     return gF2Vector;
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
/*     */   public org.bouncycastle.pqc.math.linearalgebra.GF2Vector extractRightVector(int paramInt) {
/* 409 */     if (paramInt > this.length)
/*     */     {
/* 411 */       throw new ArithmeticException("invalid length");
/*     */     }
/*     */     
/* 414 */     if (paramInt == this.length)
/*     */     {
/* 416 */       return new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(this);
/*     */     }
/*     */     
/* 419 */     org.bouncycastle.pqc.math.linearalgebra.GF2Vector gF2Vector = new org.bouncycastle.pqc.math.linearalgebra.GF2Vector(paramInt);
/*     */     
/* 421 */     int i = this.length - paramInt >> 5;
/* 422 */     int j = this.length - paramInt & 0x1F;
/* 423 */     int k = paramInt + 31 >> 5;
/*     */     
/* 425 */     int m = i;
/*     */     
/* 427 */     if (j != 0) {
/*     */ 
/*     */       
/* 430 */       for (byte b = 0; b < k - 1; b++)
/*     */       {
/* 432 */         gF2Vector.v[b] = this.v[m++] >>> j | this.v[m] << 32 - j;
/*     */       }
/*     */       
/* 435 */       gF2Vector.v[k - 1] = this.v[m++] >>> j;
/* 436 */       if (m < this.v.length)
/*     */       {
/* 438 */         gF2Vector.v[k - 1] = gF2Vector.v[k - 1] | this.v[m] << 32 - j;
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 444 */       System.arraycopy(this.v, i, gF2Vector.v, 0, k);
/*     */     } 
/*     */     
/* 447 */     return gF2Vector;
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
/*     */   public GF2mVector toExtensionFieldVector(GF2mField paramGF2mField) {
/* 459 */     int i = paramGF2mField.getDegree();
/* 460 */     if (this.length % i != 0)
/*     */     {
/* 462 */       throw new ArithmeticException("conversion is impossible");
/*     */     }
/*     */     
/* 465 */     int j = this.length / i;
/* 466 */     int[] arrayOfInt = new int[j];
/* 467 */     byte b = 0;
/* 468 */     for (int k = j - 1; k >= 0; k--) {
/*     */       
/* 470 */       for (int m = paramGF2mField.getDegree() - 1; m >= 0; m--) {
/*     */         
/* 472 */         int n = b >>> 5;
/* 473 */         int i1 = b & 0x1F;
/*     */         
/* 475 */         int i2 = this.v[n] >>> i1 & 0x1;
/* 476 */         if (i2 == 1)
/*     */         {
/* 478 */           arrayOfInt[k] = arrayOfInt[k] ^ 1 << m;
/*     */         }
/* 480 */         b++;
/*     */       } 
/*     */     } 
/* 483 */     return new GF2mVector(paramGF2mField, arrayOfInt);
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
/*     */   public boolean equals(Object paramObject) {
/* 495 */     if (!(paramObject instanceof org.bouncycastle.pqc.math.linearalgebra.GF2Vector))
/*     */     {
/* 497 */       return false;
/*     */     }
/* 499 */     org.bouncycastle.pqc.math.linearalgebra.GF2Vector gF2Vector = (org.bouncycastle.pqc.math.linearalgebra.GF2Vector)paramObject;
/*     */     
/* 501 */     return (this.length == gF2Vector.length && IntUtils.equals(this.v, gF2Vector.v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 509 */     int i = this.length;
/* 510 */     i = i * 31 + Arrays.hashCode(this.v);
/* 511 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 519 */     StringBuffer stringBuffer = new StringBuffer();
/* 520 */     for (byte b = 0; b < this.length; b++) {
/*     */       
/* 522 */       if (b != 0 && (b & 0x1F) == 0)
/*     */       {
/* 524 */         stringBuffer.append(' ');
/*     */       }
/* 526 */       int i = b >> 5;
/* 527 */       int j = b & 0x1F;
/* 528 */       int k = this.v[i] & 1 << j;
/* 529 */       if (k == 0) {
/*     */         
/* 531 */         stringBuffer.append('0');
/*     */       }
/*     */       else {
/*     */         
/* 535 */         stringBuffer.append('1');
/*     */       } 
/*     */     } 
/* 538 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/GF2Vector.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
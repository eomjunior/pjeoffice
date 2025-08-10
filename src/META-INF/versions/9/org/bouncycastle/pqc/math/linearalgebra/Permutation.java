/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
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
/*     */ public class Permutation
/*     */ {
/*     */   private int[] perm;
/*     */   
/*     */   public Permutation(int paramInt) {
/*  29 */     if (paramInt <= 0)
/*     */     {
/*  31 */       throw new IllegalArgumentException("invalid length");
/*     */     }
/*     */     
/*  34 */     this.perm = new int[paramInt];
/*  35 */     for (int i = paramInt - 1; i >= 0; i--)
/*     */     {
/*  37 */       this.perm[i] = i;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation(int[] paramArrayOfint) {
/*  48 */     if (!isPermutation(paramArrayOfint))
/*     */     {
/*  50 */       throw new IllegalArgumentException("array is not a permutation vector");
/*     */     }
/*     */ 
/*     */     
/*  54 */     this.perm = IntUtils.clone(paramArrayOfint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation(byte[] paramArrayOfbyte) {
/*  64 */     if (paramArrayOfbyte.length <= 4)
/*     */     {
/*  66 */       throw new IllegalArgumentException("invalid encoding");
/*     */     }
/*     */     
/*  69 */     int i = LittleEndianConversions.OS2IP(paramArrayOfbyte, 0);
/*  70 */     int j = IntegerFunctions.ceilLog256(i - 1);
/*     */     
/*  72 */     if (paramArrayOfbyte.length != 4 + i * j)
/*     */     {
/*  74 */       throw new IllegalArgumentException("invalid encoding");
/*     */     }
/*     */     
/*  77 */     this.perm = new int[i];
/*  78 */     for (byte b = 0; b < i; b++)
/*     */     {
/*  80 */       this.perm[b] = LittleEndianConversions.OS2IP(paramArrayOfbyte, 4 + b * j, j);
/*     */     }
/*     */     
/*  83 */     if (!isPermutation(this.perm))
/*     */     {
/*  85 */       throw new IllegalArgumentException("invalid encoding");
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
/*     */   public Permutation(int paramInt, SecureRandom paramSecureRandom) {
/*  98 */     if (paramInt <= 0)
/*     */     {
/* 100 */       throw new IllegalArgumentException("invalid length");
/*     */     }
/*     */     
/* 103 */     this.perm = new int[paramInt];
/*     */     
/* 105 */     int[] arrayOfInt = new int[paramInt]; int i;
/* 106 */     for (i = 0; i < paramInt; i++)
/*     */     {
/* 108 */       arrayOfInt[i] = i;
/*     */     }
/*     */     
/* 111 */     i = paramInt;
/* 112 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/* 114 */       int j = RandUtils.nextInt(paramSecureRandom, i);
/* 115 */       i--;
/* 116 */       this.perm[b] = arrayOfInt[j];
/* 117 */       arrayOfInt[j] = arrayOfInt[i];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/* 128 */     int i = this.perm.length;
/* 129 */     int j = IntegerFunctions.ceilLog256(i - 1);
/* 130 */     byte[] arrayOfByte = new byte[4 + i * j];
/* 131 */     LittleEndianConversions.I2OSP(i, arrayOfByte, 0);
/* 132 */     for (byte b = 0; b < i; b++)
/*     */     {
/* 134 */       LittleEndianConversions.I2OSP(this.perm[b], arrayOfByte, 4 + b * j, j);
/*     */     }
/* 136 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVector() {
/* 144 */     return IntUtils.clone(this.perm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.math.linearalgebra.Permutation computeInverse() {
/* 154 */     org.bouncycastle.pqc.math.linearalgebra.Permutation permutation = new org.bouncycastle.pqc.math.linearalgebra.Permutation(this.perm.length);
/* 155 */     for (int i = this.perm.length - 1; i >= 0; i--)
/*     */     {
/* 157 */       permutation.perm[this.perm[i]] = i;
/*     */     }
/* 159 */     return permutation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.math.linearalgebra.Permutation rightMultiply(org.bouncycastle.pqc.math.linearalgebra.Permutation paramPermutation) {
/* 170 */     if (paramPermutation.perm.length != this.perm.length)
/*     */     {
/* 172 */       throw new IllegalArgumentException("length mismatch");
/*     */     }
/* 174 */     org.bouncycastle.pqc.math.linearalgebra.Permutation permutation = new org.bouncycastle.pqc.math.linearalgebra.Permutation(this.perm.length);
/* 175 */     for (int i = this.perm.length - 1; i >= 0; i--)
/*     */     {
/* 177 */       permutation.perm[i] = this.perm[paramPermutation.perm[i]];
/*     */     }
/* 179 */     return permutation;
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
/*     */   public boolean equals(Object paramObject) {
/* 194 */     if (!(paramObject instanceof org.bouncycastle.pqc.math.linearalgebra.Permutation))
/*     */     {
/* 196 */       return false;
/*     */     }
/* 198 */     org.bouncycastle.pqc.math.linearalgebra.Permutation permutation = (org.bouncycastle.pqc.math.linearalgebra.Permutation)paramObject;
/*     */     
/* 200 */     return IntUtils.equals(this.perm, permutation.perm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 208 */     String str = "[" + this.perm[0];
/* 209 */     for (byte b = 1; b < this.perm.length; b++)
/*     */     {
/* 211 */       str = str + ", " + str;
/*     */     }
/* 213 */     str = str + "]";
/* 214 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 222 */     return Arrays.hashCode(this.perm);
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
/*     */   private boolean isPermutation(int[] paramArrayOfint) {
/* 234 */     int i = paramArrayOfint.length;
/* 235 */     boolean[] arrayOfBoolean = new boolean[i];
/*     */     
/* 237 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 239 */       if (paramArrayOfint[b] < 0 || paramArrayOfint[b] >= i || arrayOfBoolean[paramArrayOfint[b]])
/*     */       {
/* 241 */         return false;
/*     */       }
/* 243 */       arrayOfBoolean[paramArrayOfint[b]] = true;
/*     */     } 
/*     */     
/* 246 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/Permutation.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GF2mVector
/*     */   extends Vector
/*     */ {
/*     */   private GF2mField field;
/*     */   private int[] vector;
/*     */   
/*     */   public GF2mVector(GF2mField paramGF2mField, byte[] paramArrayOfbyte) {
/*  34 */     this.field = new GF2mField(paramGF2mField);
/*     */ 
/*     */     
/*  37 */     byte b1 = 8;
/*  38 */     byte b2 = 1;
/*  39 */     while (paramGF2mField.getDegree() > b1) {
/*     */       
/*  41 */       b2++;
/*  42 */       b1 += 8;
/*     */     } 
/*     */     
/*  45 */     if (paramArrayOfbyte.length % b2 != 0)
/*     */     {
/*  47 */       throw new IllegalArgumentException("Byte array is not an encoded vector over the given finite field.");
/*     */     }
/*     */ 
/*     */     
/*  51 */     this.length = paramArrayOfbyte.length / b2;
/*  52 */     this.vector = new int[this.length];
/*  53 */     b2 = 0;
/*  54 */     for (byte b3 = 0; b3 < this.vector.length; b3++) {
/*     */       
/*  56 */       for (byte b = 0; b < b1; b += 8)
/*     */       {
/*  58 */         this.vector[b3] = this.vector[b3] | (paramArrayOfbyte[b2++] & 0xFF) << b;
/*     */       }
/*  60 */       if (!paramGF2mField.isElementOfThisField(this.vector[b3]))
/*     */       {
/*  62 */         throw new IllegalArgumentException("Byte array is not an encoded vector over the given finite field.");
/*     */       }
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
/*     */   public GF2mVector(GF2mField paramGF2mField, int[] paramArrayOfint) {
/*  77 */     this.field = paramGF2mField;
/*  78 */     this.length = paramArrayOfint.length;
/*  79 */     for (int i = paramArrayOfint.length - 1; i >= 0; i--) {
/*     */       
/*  81 */       if (!paramGF2mField.isElementOfThisField(paramArrayOfint[i]))
/*     */       {
/*  83 */         throw new ArithmeticException("Element array is not specified over the given finite field.");
/*     */       }
/*     */     } 
/*     */     
/*  87 */     this.vector = IntUtils.clone(paramArrayOfint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mVector(org.bouncycastle.pqc.math.linearalgebra.GF2mVector paramGF2mVector) {
/*  97 */     this.field = new GF2mField(paramGF2mVector.field);
/*  98 */     this.length = paramGF2mVector.length;
/*  99 */     this.vector = IntUtils.clone(paramGF2mVector.vector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/* 107 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIntArrayForm() {
/* 115 */     return IntUtils.clone(this.vector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/* 123 */     byte b1 = 8;
/* 124 */     byte b2 = 1;
/* 125 */     while (this.field.getDegree() > b1) {
/*     */       
/* 127 */       b2++;
/* 128 */       b1 += 8;
/*     */     } 
/*     */     
/* 131 */     byte[] arrayOfByte = new byte[this.vector.length * b2];
/* 132 */     b2 = 0;
/* 133 */     for (byte b3 = 0; b3 < this.vector.length; b3++) {
/*     */       
/* 135 */       for (byte b = 0; b < b1; b += 8)
/*     */       {
/* 137 */         arrayOfByte[b2++] = (byte)(this.vector[b3] >>> b);
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 149 */     for (int i = this.vector.length - 1; i >= 0; i--) {
/*     */       
/* 151 */       if (this.vector[i] != 0)
/*     */       {
/* 153 */         return false;
/*     */       }
/*     */     } 
/* 156 */     return true;
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
/*     */   public Vector add(Vector paramVector) {
/* 171 */     throw new RuntimeException("not implemented");
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
/* 182 */     int[] arrayOfInt1 = paramPermutation.getVector();
/* 183 */     if (this.length != arrayOfInt1.length)
/*     */     {
/* 185 */       throw new ArithmeticException("permutation size and vector size mismatch");
/*     */     }
/*     */ 
/*     */     
/* 189 */     int[] arrayOfInt2 = new int[this.length];
/* 190 */     for (byte b = 0; b < arrayOfInt1.length; b++)
/*     */     {
/* 192 */       arrayOfInt2[b] = this.vector[arrayOfInt1[b]];
/*     */     }
/*     */     
/* 195 */     return new org.bouncycastle.pqc.math.linearalgebra.GF2mVector(this.field, arrayOfInt2);
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
/* 207 */     if (!(paramObject instanceof org.bouncycastle.pqc.math.linearalgebra.GF2mVector))
/*     */     {
/* 209 */       return false;
/*     */     }
/* 211 */     org.bouncycastle.pqc.math.linearalgebra.GF2mVector gF2mVector = (org.bouncycastle.pqc.math.linearalgebra.GF2mVector)paramObject;
/*     */     
/* 213 */     if (!this.field.equals(gF2mVector.field))
/*     */     {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     return IntUtils.equals(this.vector, gF2mVector.vector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 226 */     int i = this.field.hashCode();
/* 227 */     i = i * 31 + Arrays.hashCode(this.vector);
/* 228 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 236 */     StringBuffer stringBuffer = new StringBuffer();
/* 237 */     for (byte b = 0; b < this.vector.length; b++) {
/*     */       
/* 239 */       for (byte b1 = 0; b1 < this.field.getDegree(); b1++) {
/*     */         
/* 241 */         int i = b1 & 0x1F;
/* 242 */         int j = 1 << i;
/* 243 */         int k = this.vector[b] & j;
/* 244 */         if (k != 0) {
/*     */           
/* 246 */           stringBuffer.append('1');
/*     */         }
/*     */         else {
/*     */           
/* 250 */           stringBuffer.append('0');
/*     */         } 
/*     */       } 
/* 253 */       stringBuffer.append(' ');
/*     */     } 
/* 255 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/GF2mVector.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
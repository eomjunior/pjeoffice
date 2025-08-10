/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
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
/*     */ class SimpleBigDecimal
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final BigInteger bigInt;
/*     */   private final int scale;
/*     */   
/*     */   public static org.bouncycastle.math.ec.SimpleBigDecimal getInstance(BigInteger paramBigInteger, int paramInt) {
/*  36 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(paramBigInteger.shiftLeft(paramInt), paramInt);
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
/*     */   public SimpleBigDecimal(BigInteger paramBigInteger, int paramInt) {
/*  48 */     if (paramInt < 0)
/*     */     {
/*  50 */       throw new IllegalArgumentException("scale may not be negative");
/*     */     }
/*     */     
/*  53 */     this.bigInt = paramBigInteger;
/*  54 */     this.scale = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkScale(org.bouncycastle.math.ec.SimpleBigDecimal paramSimpleBigDecimal) {
/*  59 */     if (this.scale != paramSimpleBigDecimal.scale)
/*     */     {
/*  61 */       throw new IllegalArgumentException("Only SimpleBigDecimal of same scale allowed in arithmetic operations");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal adjustScale(int paramInt) {
/*  68 */     if (paramInt < 0)
/*     */     {
/*  70 */       throw new IllegalArgumentException("scale may not be negative");
/*     */     }
/*     */     
/*  73 */     if (paramInt == this.scale)
/*     */     {
/*  75 */       return this;
/*     */     }
/*     */     
/*  78 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.shiftLeft(paramInt - this.scale), paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal add(org.bouncycastle.math.ec.SimpleBigDecimal paramSimpleBigDecimal) {
/*  84 */     checkScale(paramSimpleBigDecimal);
/*  85 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.add(paramSimpleBigDecimal.bigInt), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal add(BigInteger paramBigInteger) {
/*  90 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.add(paramBigInteger.shiftLeft(this.scale)), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal negate() {
/*  95 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.negate(), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal subtract(org.bouncycastle.math.ec.SimpleBigDecimal paramSimpleBigDecimal) {
/* 100 */     return add(paramSimpleBigDecimal.negate());
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal subtract(BigInteger paramBigInteger) {
/* 105 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.subtract(paramBigInteger.shiftLeft(this.scale)), this.scale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal multiply(org.bouncycastle.math.ec.SimpleBigDecimal paramSimpleBigDecimal) {
/* 111 */     checkScale(paramSimpleBigDecimal);
/* 112 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.multiply(paramSimpleBigDecimal.bigInt), this.scale + this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal multiply(BigInteger paramBigInteger) {
/* 117 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.multiply(paramBigInteger), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal divide(org.bouncycastle.math.ec.SimpleBigDecimal paramSimpleBigDecimal) {
/* 122 */     checkScale(paramSimpleBigDecimal);
/* 123 */     BigInteger bigInteger = this.bigInt.shiftLeft(this.scale);
/* 124 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(bigInteger.divide(paramSimpleBigDecimal.bigInt), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal divide(BigInteger paramBigInteger) {
/* 129 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.divide(paramBigInteger), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.SimpleBigDecimal shiftLeft(int paramInt) {
/* 134 */     return new org.bouncycastle.math.ec.SimpleBigDecimal(this.bigInt.shiftLeft(paramInt), this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(org.bouncycastle.math.ec.SimpleBigDecimal paramSimpleBigDecimal) {
/* 139 */     checkScale(paramSimpleBigDecimal);
/* 140 */     return this.bigInt.compareTo(paramSimpleBigDecimal.bigInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(BigInteger paramBigInteger) {
/* 145 */     return this.bigInt.compareTo(paramBigInteger.shiftLeft(this.scale));
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger floor() {
/* 150 */     return this.bigInt.shiftRight(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger round() {
/* 155 */     org.bouncycastle.math.ec.SimpleBigDecimal simpleBigDecimal = new org.bouncycastle.math.ec.SimpleBigDecimal(ECConstants.ONE, 1);
/* 156 */     return add(simpleBigDecimal.adjustScale(this.scale)).floor();
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 161 */     return floor().intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 166 */     return floor().longValue();
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
/*     */   public int getScale() {
/* 181 */     return this.scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 186 */     if (this.scale == 0)
/*     */     {
/* 188 */       return this.bigInt.toString();
/*     */     }
/*     */     
/* 191 */     BigInteger bigInteger1 = floor();
/*     */     
/* 193 */     BigInteger bigInteger2 = this.bigInt.subtract(bigInteger1.shiftLeft(this.scale));
/* 194 */     if (this.bigInt.signum() == -1)
/*     */     {
/* 196 */       bigInteger2 = ECConstants.ONE.shiftLeft(this.scale).subtract(bigInteger2);
/*     */     }
/*     */     
/* 199 */     if (bigInteger1.signum() == -1 && !bigInteger2.equals(ECConstants.ZERO))
/*     */     {
/* 201 */       bigInteger1 = bigInteger1.add(ECConstants.ONE);
/*     */     }
/* 203 */     String str1 = bigInteger1.toString();
/*     */     
/* 205 */     char[] arrayOfChar = new char[this.scale];
/* 206 */     String str2 = bigInteger2.toString(2);
/* 207 */     int i = str2.length();
/* 208 */     int j = this.scale - i; byte b;
/* 209 */     for (b = 0; b < j; b++)
/*     */     {
/* 211 */       arrayOfChar[b] = '0';
/*     */     }
/* 213 */     for (b = 0; b < i; b++)
/*     */     {
/* 215 */       arrayOfChar[j + b] = str2.charAt(b);
/*     */     }
/* 217 */     String str3 = new String(arrayOfChar);
/*     */     
/* 219 */     StringBuffer stringBuffer = new StringBuffer(str1);
/* 220 */     stringBuffer.append(".");
/* 221 */     stringBuffer.append(str3);
/*     */     
/* 223 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 228 */     if (this == paramObject)
/*     */     {
/* 230 */       return true;
/*     */     }
/*     */     
/* 233 */     if (!(paramObject instanceof org.bouncycastle.math.ec.SimpleBigDecimal))
/*     */     {
/* 235 */       return false;
/*     */     }
/*     */     
/* 238 */     org.bouncycastle.math.ec.SimpleBigDecimal simpleBigDecimal = (org.bouncycastle.math.ec.SimpleBigDecimal)paramObject;
/* 239 */     return (this.bigInt.equals(simpleBigDecimal.bigInt) && this.scale == simpleBigDecimal.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 244 */     return this.bigInt.hashCode() ^ this.scale;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/SimpleBigDecimal.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */
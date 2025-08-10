/*     */ package com.itextpdf.text.pdf.qrcode;
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
/*     */ final class GF256Poly
/*     */ {
/*     */   private final GF256 field;
/*     */   private final int[] coefficients;
/*     */   
/*     */   GF256Poly(GF256 field, int[] coefficients) {
/*  44 */     if (coefficients == null || coefficients.length == 0) {
/*  45 */       throw new IllegalArgumentException();
/*     */     }
/*  47 */     this.field = field;
/*  48 */     int coefficientsLength = coefficients.length;
/*  49 */     if (coefficientsLength > 1 && coefficients[0] == 0) {
/*     */       
/*  51 */       int firstNonZero = 1;
/*  52 */       while (firstNonZero < coefficientsLength && coefficients[firstNonZero] == 0) {
/*  53 */         firstNonZero++;
/*     */       }
/*  55 */       if (firstNonZero == coefficientsLength) {
/*  56 */         this.coefficients = (field.getZero()).coefficients;
/*     */       } else {
/*  58 */         this.coefficients = new int[coefficientsLength - firstNonZero];
/*  59 */         System.arraycopy(coefficients, firstNonZero, this.coefficients, 0, this.coefficients.length);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  66 */       this.coefficients = coefficients;
/*     */     } 
/*     */   }
/*     */   
/*     */   int[] getCoefficients() {
/*  71 */     return this.coefficients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getDegree() {
/*  78 */     return this.coefficients.length - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isZero() {
/*  85 */     return (this.coefficients[0] == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getCoefficient(int degree) {
/*  92 */     return this.coefficients[this.coefficients.length - 1 - degree];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int evaluateAt(int a) {
/*  99 */     if (a == 0)
/*     */     {
/* 101 */       return getCoefficient(0);
/*     */     }
/* 103 */     int size = this.coefficients.length;
/* 104 */     if (a == 1) {
/*     */       
/* 106 */       int j = 0;
/* 107 */       for (int k = 0; k < size; k++) {
/* 108 */         j = GF256.addOrSubtract(j, this.coefficients[k]);
/*     */       }
/* 110 */       return j;
/*     */     } 
/* 112 */     int result = this.coefficients[0];
/* 113 */     for (int i = 1; i < size; i++) {
/* 114 */       result = GF256.addOrSubtract(this.field.multiply(a, result), this.coefficients[i]);
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */   
/*     */   GF256Poly addOrSubtract(GF256Poly other) {
/* 120 */     if (!this.field.equals(other.field)) {
/* 121 */       throw new IllegalArgumentException("GF256Polys do not have same GF256 field");
/*     */     }
/* 123 */     if (isZero()) {
/* 124 */       return other;
/*     */     }
/* 126 */     if (other.isZero()) {
/* 127 */       return this;
/*     */     }
/*     */     
/* 130 */     int[] smallerCoefficients = this.coefficients;
/* 131 */     int[] largerCoefficients = other.coefficients;
/* 132 */     if (smallerCoefficients.length > largerCoefficients.length) {
/* 133 */       int[] temp = smallerCoefficients;
/* 134 */       smallerCoefficients = largerCoefficients;
/* 135 */       largerCoefficients = temp;
/*     */     } 
/* 137 */     int[] sumDiff = new int[largerCoefficients.length];
/* 138 */     int lengthDiff = largerCoefficients.length - smallerCoefficients.length;
/*     */     
/* 140 */     System.arraycopy(largerCoefficients, 0, sumDiff, 0, lengthDiff);
/*     */     
/* 142 */     for (int i = lengthDiff; i < largerCoefficients.length; i++) {
/* 143 */       sumDiff[i] = GF256.addOrSubtract(smallerCoefficients[i - lengthDiff], largerCoefficients[i]);
/*     */     }
/*     */     
/* 146 */     return new GF256Poly(this.field, sumDiff);
/*     */   }
/*     */   
/*     */   GF256Poly multiply(GF256Poly other) {
/* 150 */     if (!this.field.equals(other.field)) {
/* 151 */       throw new IllegalArgumentException("GF256Polys do not have same GF256 field");
/*     */     }
/* 153 */     if (isZero() || other.isZero()) {
/* 154 */       return this.field.getZero();
/*     */     }
/* 156 */     int[] aCoefficients = this.coefficients;
/* 157 */     int aLength = aCoefficients.length;
/* 158 */     int[] bCoefficients = other.coefficients;
/* 159 */     int bLength = bCoefficients.length;
/* 160 */     int[] product = new int[aLength + bLength - 1];
/* 161 */     for (int i = 0; i < aLength; i++) {
/* 162 */       int aCoeff = aCoefficients[i];
/* 163 */       for (int j = 0; j < bLength; j++) {
/* 164 */         product[i + j] = GF256.addOrSubtract(product[i + j], this.field
/* 165 */             .multiply(aCoeff, bCoefficients[j]));
/*     */       }
/*     */     } 
/* 168 */     return new GF256Poly(this.field, product);
/*     */   }
/*     */   
/*     */   GF256Poly multiply(int scalar) {
/* 172 */     if (scalar == 0) {
/* 173 */       return this.field.getZero();
/*     */     }
/* 175 */     if (scalar == 1) {
/* 176 */       return this;
/*     */     }
/* 178 */     int size = this.coefficients.length;
/* 179 */     int[] product = new int[size];
/* 180 */     for (int i = 0; i < size; i++) {
/* 181 */       product[i] = this.field.multiply(this.coefficients[i], scalar);
/*     */     }
/* 183 */     return new GF256Poly(this.field, product);
/*     */   }
/*     */   
/*     */   GF256Poly multiplyByMonomial(int degree, int coefficient) {
/* 187 */     if (degree < 0) {
/* 188 */       throw new IllegalArgumentException();
/*     */     }
/* 190 */     if (coefficient == 0) {
/* 191 */       return this.field.getZero();
/*     */     }
/* 193 */     int size = this.coefficients.length;
/* 194 */     int[] product = new int[size + degree];
/* 195 */     for (int i = 0; i < size; i++) {
/* 196 */       product[i] = this.field.multiply(this.coefficients[i], coefficient);
/*     */     }
/* 198 */     return new GF256Poly(this.field, product);
/*     */   }
/*     */   
/*     */   GF256Poly[] divide(GF256Poly other) {
/* 202 */     if (!this.field.equals(other.field)) {
/* 203 */       throw new IllegalArgumentException("GF256Polys do not have same GF256 field");
/*     */     }
/* 205 */     if (other.isZero()) {
/* 206 */       throw new IllegalArgumentException("Divide by 0");
/*     */     }
/*     */     
/* 209 */     GF256Poly quotient = this.field.getZero();
/* 210 */     GF256Poly remainder = this;
/*     */     
/* 212 */     int denominatorLeadingTerm = other.getCoefficient(other.getDegree());
/* 213 */     int inverseDenominatorLeadingTerm = this.field.inverse(denominatorLeadingTerm);
/*     */     
/* 215 */     while (remainder.getDegree() >= other.getDegree() && !remainder.isZero()) {
/* 216 */       int degreeDifference = remainder.getDegree() - other.getDegree();
/* 217 */       int scale = this.field.multiply(remainder.getCoefficient(remainder.getDegree()), inverseDenominatorLeadingTerm);
/* 218 */       GF256Poly term = other.multiplyByMonomial(degreeDifference, scale);
/* 219 */       GF256Poly iterationQuotient = this.field.buildMonomial(degreeDifference, scale);
/* 220 */       quotient = quotient.addOrSubtract(iterationQuotient);
/* 221 */       remainder = remainder.addOrSubtract(term);
/*     */     } 
/*     */     
/* 224 */     return new GF256Poly[] { quotient, remainder };
/*     */   }
/*     */   
/*     */   public String toString() {
/* 228 */     StringBuffer result = new StringBuffer(8 * getDegree());
/* 229 */     for (int degree = getDegree(); degree >= 0; degree--) {
/* 230 */       int coefficient = getCoefficient(degree);
/* 231 */       if (coefficient != 0) {
/* 232 */         if (coefficient < 0) {
/* 233 */           result.append(" - ");
/* 234 */           coefficient = -coefficient;
/*     */         }
/* 236 */         else if (result.length() > 0) {
/* 237 */           result.append(" + ");
/*     */         } 
/*     */         
/* 240 */         if (degree == 0 || coefficient != 1) {
/* 241 */           int alphaPower = this.field.log(coefficient);
/* 242 */           if (alphaPower == 0) {
/* 243 */             result.append('1');
/* 244 */           } else if (alphaPower == 1) {
/* 245 */             result.append('a');
/*     */           } else {
/* 247 */             result.append("a^");
/* 248 */             result.append(alphaPower);
/*     */           } 
/*     */         } 
/* 251 */         if (degree != 0) {
/* 252 */           if (degree == 1) {
/* 253 */             result.append('x');
/*     */           } else {
/* 255 */             result.append("x^");
/* 256 */             result.append(degree);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 261 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/GF256Poly.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
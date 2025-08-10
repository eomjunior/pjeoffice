/*    */ package com.itextpdf.text.pdf.qrcode;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ReedSolomonEncoder
/*    */ {
/*    */   private final GF256 field;
/*    */   private final ArrayList<GF256Poly> cachedGenerators;
/*    */   
/*    */   public ReedSolomonEncoder(GF256 field) {
/* 34 */     if (!GF256.QR_CODE_FIELD.equals(field)) {
/* 35 */       throw new IllegalArgumentException("Only QR Code is supported at this time");
/*    */     }
/* 37 */     this.field = field;
/* 38 */     this.cachedGenerators = new ArrayList<GF256Poly>();
/* 39 */     this.cachedGenerators.add(new GF256Poly(field, new int[] { 1 }));
/*    */   }
/*    */   
/*    */   private GF256Poly buildGenerator(int degree) {
/* 43 */     if (degree >= this.cachedGenerators.size()) {
/* 44 */       GF256Poly lastGenerator = this.cachedGenerators.get(this.cachedGenerators.size() - 1);
/* 45 */       for (int d = this.cachedGenerators.size(); d <= degree; d++) {
/* 46 */         GF256Poly nextGenerator = lastGenerator.multiply(new GF256Poly(this.field, new int[] { 1, this.field.exp(d - 1) }));
/* 47 */         this.cachedGenerators.add(nextGenerator);
/* 48 */         lastGenerator = nextGenerator;
/*    */       } 
/*    */     } 
/* 51 */     return this.cachedGenerators.get(degree);
/*    */   }
/*    */   
/*    */   public void encode(int[] toEncode, int ecBytes) {
/* 55 */     if (ecBytes == 0) {
/* 56 */       throw new IllegalArgumentException("No error correction bytes");
/*    */     }
/* 58 */     int dataBytes = toEncode.length - ecBytes;
/* 59 */     if (dataBytes <= 0) {
/* 60 */       throw new IllegalArgumentException("No data bytes provided");
/*    */     }
/* 62 */     GF256Poly generator = buildGenerator(ecBytes);
/* 63 */     int[] infoCoefficients = new int[dataBytes];
/* 64 */     System.arraycopy(toEncode, 0, infoCoefficients, 0, dataBytes);
/* 65 */     GF256Poly info = new GF256Poly(this.field, infoCoefficients);
/* 66 */     info = info.multiplyByMonomial(ecBytes, 1);
/* 67 */     GF256Poly remainder = info.divide(generator)[1];
/* 68 */     int[] coefficients = remainder.getCoefficients();
/* 69 */     int numZeroCoefficients = ecBytes - coefficients.length;
/* 70 */     for (int i = 0; i < numZeroCoefficients; i++) {
/* 71 */       toEncode[dataBytes + i] = 0;
/*    */     }
/* 73 */     System.arraycopy(coefficients, 0, toEncode, dataBytes + numZeroCoefficients, coefficients.length);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/ReedSolomonEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.itextpdf.text.pdf;
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
/*    */ public class RefKey
/*    */ {
/*    */   int num;
/*    */   int gen;
/*    */   
/*    */   RefKey(int num, int gen) {
/* 54 */     this.num = num;
/* 55 */     this.gen = gen;
/*    */   }
/*    */   public RefKey(PdfIndirectReference ref) {
/* 58 */     this.num = ref.getNumber();
/* 59 */     this.gen = ref.getGeneration();
/*    */   }
/*    */   RefKey(PRIndirectReference ref) {
/* 62 */     this.num = ref.getNumber();
/* 63 */     this.gen = ref.getGeneration();
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 67 */     return (this.gen << 16) + this.num;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 71 */     if (!(o instanceof RefKey)) return false; 
/* 72 */     RefKey other = (RefKey)o;
/* 73 */     return (this.gen == other.gen && this.num == other.num);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 77 */     return Integer.toString(this.num) + ' ' + this.gen;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/RefKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.itextpdf.text.pdf.languages;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ public class IndicCompositeCharacterComparator
/*    */   implements Comparator<String>
/*    */ {
/*    */   public int compare(String o1, String o2) {
/* 64 */     if (o1.length() < o2.length()) {
/* 65 */       return 1;
/*    */     }
/* 67 */     if (o1.length() > o2.length()) {
/* 68 */       return -1;
/*    */     }
/* 70 */     return o1.compareTo(o2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/IndicCompositeCharacterComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
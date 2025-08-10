/*    */ package com.itextpdf.text.pdf.fonts.otf;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public enum Language
/*    */ {
/* 55 */   BENGALI(new String[] { "beng", "bng2" });
/*    */   
/*    */   private final List<String> codes;
/*    */   
/*    */   Language(String... codes) {
/* 60 */     this.codes = Arrays.asList(codes);
/*    */   }
/*    */   
/*    */   public boolean isSupported(String languageCode) {
/* 64 */     return this.codes.contains(languageCode);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/otf/Language.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
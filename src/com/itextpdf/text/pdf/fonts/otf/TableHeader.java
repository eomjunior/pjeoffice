/*    */ package com.itextpdf.text.pdf.fonts.otf;
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
/*    */ public class TableHeader
/*    */ {
/*    */   public int version;
/*    */   public int scriptListOffset;
/*    */   public int featureListOffset;
/*    */   public int lookupListOffset;
/*    */   
/*    */   public TableHeader(int version, int scriptListOffset, int featureListOffset, int lookupListOffset) {
/* 58 */     this.version = version;
/* 59 */     this.scriptListOffset = scriptListOffset;
/* 60 */     this.featureListOffset = featureListOffset;
/* 61 */     this.lookupListOffset = lookupListOffset;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/otf/TableHeader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
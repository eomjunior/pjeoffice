/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListLabel
/*     */   extends ListBody
/*     */ {
/*  50 */   protected PdfName role = PdfName.LBL;
/*  51 */   protected float indentation = 0.0F;
/*     */   
/*     */   protected ListLabel(ListItem parentItem) {
/*  54 */     super(parentItem);
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/*  58 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/*  62 */     this.role = role;
/*     */   }
/*     */   
/*     */   public float getIndentation() {
/*  66 */     return this.indentation;
/*     */   }
/*     */   
/*     */   public void setIndentation(float indentation) {
/*  70 */     this.indentation = indentation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean getTagLabelContent() {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTagLabelContent(boolean tagLabelContent) {}
/*     */   
/*     */   public boolean isInline() {
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ListLabel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
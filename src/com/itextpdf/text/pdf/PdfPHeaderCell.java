/*     */ package com.itextpdf.text.pdf;
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
/*     */ public class PdfPHeaderCell
/*     */   extends PdfPCell
/*     */ {
/*     */   public static final int NONE = 0;
/*     */   public static final int ROW = 1;
/*     */   public static final int COLUMN = 2;
/*     */   public static final int BOTH = 3;
/*  70 */   protected int scope = 0;
/*     */ 
/*     */   
/*     */   protected String name;
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPHeaderCell(PdfPHeaderCell headerCell) {
/*  78 */     super(headerCell);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     this.name = null; this.role = headerCell.role; this.scope = headerCell.scope; this.name = headerCell.getName(); } public PdfPHeaderCell() { this.name = null;
/*     */     this.role = PdfName.TH; }
/*     */    public void setName(String name) {
/*  87 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  91 */     return this.name;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/*  95 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/*  99 */     this.role = role;
/*     */   }
/*     */   
/*     */   public void setScope(int scope) {
/* 103 */     this.scope = scope;
/*     */   }
/*     */   
/*     */   public int getScope() {
/* 107 */     return this.scope;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPHeaderCell.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
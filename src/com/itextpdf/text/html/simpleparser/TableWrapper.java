/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.ElementListener;
/*     */ import com.itextpdf.text.html.HtmlUtilities;
/*     */ import com.itextpdf.text.pdf.PdfPCell;
/*     */ import com.itextpdf.text.pdf.PdfPTable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ @Deprecated
/*     */ public class TableWrapper
/*     */   implements Element
/*     */ {
/*  74 */   private final Map<String, String> styles = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final List<List<PdfPCell>> rows = new ArrayList<List<PdfPCell>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float[] colWidths;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TableWrapper(Map<String, String> attrs) {
/*  91 */     this.styles.putAll(attrs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRow(List<PdfPCell> row) {
/*  99 */     if (row != null) {
/* 100 */       Collections.reverse(row);
/* 101 */       this.rows.add(row);
/* 102 */       row = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColWidths(float[] colWidths) {
/* 111 */     this.colWidths = colWidths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPTable createTable() {
/* 121 */     if (this.rows.isEmpty()) {
/* 122 */       return new PdfPTable(1);
/*     */     }
/* 124 */     int ncol = 0;
/* 125 */     for (PdfPCell pc : this.rows.get(0)) {
/* 126 */       ncol += pc.getColspan();
/*     */     }
/* 128 */     PdfPTable table = new PdfPTable(ncol);
/*     */     
/* 130 */     String width = this.styles.get("width");
/* 131 */     if (width == null) {
/* 132 */       table.setWidthPercentage(100.0F);
/*     */     }
/* 134 */     else if (width.endsWith("%")) {
/* 135 */       table.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
/*     */     } else {
/* 137 */       table.setTotalWidth(Float.parseFloat(width));
/* 138 */       table.setLockedWidth(true);
/*     */     } 
/*     */ 
/*     */     
/* 142 */     String alignment = this.styles.get("align");
/* 143 */     int align = 0;
/* 144 */     if (alignment != null) {
/* 145 */       align = HtmlUtilities.alignmentValue(alignment);
/*     */     }
/* 147 */     table.setHorizontalAlignment(align);
/*     */     
/*     */     try {
/* 150 */       if (this.colWidths != null)
/* 151 */         table.setWidths(this.colWidths); 
/* 152 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 156 */     for (List<PdfPCell> col : this.rows) {
/* 157 */       for (PdfPCell pc : col) {
/* 158 */         table.addCell(pc);
/*     */       }
/*     */     } 
/* 161 */     return table;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(ElementListener listener) {
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 198 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/TableWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
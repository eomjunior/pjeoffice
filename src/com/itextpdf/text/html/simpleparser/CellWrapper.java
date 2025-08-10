/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.ElementListener;
/*     */ import com.itextpdf.text.Phrase;
/*     */ import com.itextpdf.text.TextElementArray;
/*     */ import com.itextpdf.text.html.HtmlUtilities;
/*     */ import com.itextpdf.text.pdf.PdfPCell;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class CellWrapper
/*     */   implements TextElementArray
/*     */ {
/*     */   private final PdfPCell cell;
/*     */   private float width;
/*     */   private boolean percentage;
/*     */   
/*     */   public CellWrapper(String tag, ChainedProperties chain) {
/*  89 */     this.cell = createPdfPCell(tag, chain);
/*  90 */     String value = chain.getProperty("width");
/*  91 */     if (value != null) {
/*  92 */       value = value.trim();
/*  93 */       if (value.endsWith("%")) {
/*  94 */         this.percentage = true;
/*  95 */         value = value.substring(0, value.length() - 1);
/*     */       } 
/*  97 */       this.width = Float.parseFloat(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPCell createPdfPCell(String tag, ChainedProperties chain) {
/* 108 */     PdfPCell cell = new PdfPCell((Phrase)null);
/*     */     
/* 110 */     String value = chain.getProperty("colspan");
/* 111 */     if (value != null) {
/* 112 */       cell.setColspan(Integer.parseInt(value));
/*     */     }
/* 114 */     value = chain.getProperty("rowspan");
/* 115 */     if (value != null) {
/* 116 */       cell.setRowspan(Integer.parseInt(value));
/*     */     }
/* 118 */     if (tag.equals("th"))
/* 119 */       cell.setHorizontalAlignment(1); 
/* 120 */     value = chain.getProperty("align");
/* 121 */     if (value != null) {
/* 122 */       cell.setHorizontalAlignment(HtmlUtilities.alignmentValue(value));
/*     */     }
/*     */     
/* 125 */     value = chain.getProperty("valign");
/* 126 */     cell.setVerticalAlignment(5);
/* 127 */     if (value != null) {
/* 128 */       cell.setVerticalAlignment(HtmlUtilities.alignmentValue(value));
/*     */     }
/*     */     
/* 131 */     value = chain.getProperty("border");
/* 132 */     float border = 0.0F;
/* 133 */     if (value != null)
/* 134 */       border = Float.parseFloat(value); 
/* 135 */     cell.setBorderWidth(border);
/*     */     
/* 137 */     value = chain.getProperty("cellpadding");
/* 138 */     if (value != null)
/* 139 */       cell.setPadding(Float.parseFloat(value)); 
/* 140 */     cell.setUseDescender(true);
/*     */     
/* 142 */     value = chain.getProperty("bgcolor");
/* 143 */     cell.setBackgroundColor(HtmlUtilities.decodeColor(value));
/* 144 */     return cell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPCell getCell() {
/* 152 */     return this.cell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 161 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPercentage() {
/* 170 */     return this.percentage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Element o) {
/* 178 */     this.cell.addElement(o);
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 202 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(ElementListener listener) {
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 216 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/CellWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
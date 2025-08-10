/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfName;
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
/*     */ public class ListItem
/*     */   extends Paragraph
/*     */ {
/*     */   private static final long serialVersionUID = 1970670787169329006L;
/*     */   protected Chunk symbol;
/* 108 */   private ListBody listBody = null;
/* 109 */   private ListLabel listLabel = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem() {
/* 118 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(float leading) {
/* 127 */     super(leading);
/* 128 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(Chunk chunk) {
/* 137 */     super(chunk);
/* 138 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(String string) {
/* 147 */     super(string);
/* 148 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(String string, Font font) {
/* 159 */     super(string, font);
/* 160 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(float leading, Chunk chunk) {
/* 171 */     super(leading, chunk);
/* 172 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(float leading, String string) {
/* 183 */     super(leading, string);
/* 184 */     setRole(PdfName.LI);
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
/*     */   public ListItem(float leading, String string, Font font) {
/* 196 */     super(leading, string, font);
/* 197 */     setRole(PdfName.LI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem(Phrase phrase) {
/* 206 */     super(phrase);
/* 207 */     setRole(PdfName.LI);
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
/*     */   public int type() {
/* 219 */     return 15;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph cloneShallow(boolean spacingBefore) {
/* 226 */     ListItem copy = new ListItem();
/* 227 */     populateProperties(copy, spacingBefore);
/* 228 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListSymbol(Chunk symbol) {
/* 237 */     if (this.symbol == null) {
/* 238 */       this.symbol = symbol;
/* 239 */       if (this.symbol.getFont().isStandardFont()) {
/* 240 */         this.symbol.setFont(this.font);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationLeft(float indentation, boolean autoindent) {
/* 252 */     if (autoindent) {
/* 253 */       setIndentationLeft(getListSymbol().getWidthPoint());
/*     */     } else {
/*     */       
/* 256 */       setIndentationLeft(indentation);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustListSymbolFont() {
/* 266 */     List<Chunk> cks = getChunks();
/* 267 */     if (!cks.isEmpty() && this.symbol != null) {
/* 268 */       this.symbol.setFont(((Chunk)cks.get(0)).getFont());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Chunk getListSymbol() {
/* 279 */     return this.symbol;
/*     */   }
/*     */   
/*     */   public ListBody getListBody() {
/* 283 */     if (this.listBody == null)
/* 284 */       this.listBody = new ListBody(this); 
/* 285 */     return this.listBody;
/*     */   }
/*     */   
/*     */   public ListLabel getListLabel() {
/* 289 */     if (this.listLabel == null)
/* 290 */       this.listLabel = new ListLabel(this); 
/* 291 */     return this.listLabel;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ListItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
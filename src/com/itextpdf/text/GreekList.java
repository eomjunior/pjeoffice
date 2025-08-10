/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.factories.GreekAlphabetFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GreekList
/*     */   extends List
/*     */ {
/*     */   public GreekList() {
/*  63 */     super(true);
/*  64 */     setGreekFont();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GreekList(int symbolIndent) {
/*  72 */     super(true, symbolIndent);
/*  73 */     setGreekFont();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GreekList(boolean greeklower, int symbolIndent) {
/*  82 */     super(true, symbolIndent);
/*  83 */     this.lowercase = greeklower;
/*  84 */     setGreekFont();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setGreekFont() {
/*  93 */     float fontsize = this.symbol.getFont().getSize();
/*  94 */     this.symbol.setFont(FontFactory.getFont("Symbol", fontsize, 0));
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
/*     */   public boolean add(Element o) {
/* 107 */     if (o instanceof ListItem) {
/* 108 */       ListItem item = (ListItem)o;
/* 109 */       Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
/* 110 */       chunk.setAttributes(this.symbol.getAttributes());
/* 111 */       chunk.append(GreekAlphabetFactory.getString(this.first + this.list.size(), this.lowercase));
/* 112 */       chunk.append(this.postSymbol);
/* 113 */       item.setListSymbol(chunk);
/* 114 */       item.setIndentationLeft(this.symbolIndent, this.autoindent);
/* 115 */       item.setIndentationRight(0.0F);
/* 116 */       this.list.add(item);
/* 117 */     } else if (o instanceof List) {
/* 118 */       List nested = (List)o;
/* 119 */       nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
/* 120 */       this.first--;
/* 121 */       return this.list.add(nested);
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List cloneShallow() {
/* 128 */     GreekList clone = new GreekList();
/* 129 */     populateProperties(clone);
/* 130 */     return clone;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/GreekList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
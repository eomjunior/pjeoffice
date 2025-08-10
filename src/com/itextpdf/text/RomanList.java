/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.factories.RomanNumberFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RomanList
/*     */   extends List
/*     */ {
/*     */   public RomanList() {
/*  63 */     super(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RomanList(int symbolIndent) {
/*  72 */     super(true, symbolIndent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RomanList(boolean lowercase, int symbolIndent) {
/*  81 */     super(true, symbolIndent);
/*  82 */     this.lowercase = lowercase;
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
/*  95 */     if (o instanceof ListItem) {
/*  96 */       ListItem item = (ListItem)o;
/*     */       
/*  98 */       Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
/*  99 */       chunk.setAttributes(this.symbol.getAttributes());
/* 100 */       chunk.append(RomanNumberFactory.getString(this.first + this.list.size(), this.lowercase));
/* 101 */       chunk.append(this.postSymbol);
/* 102 */       item.setListSymbol(chunk);
/* 103 */       item.setIndentationLeft(this.symbolIndent, this.autoindent);
/* 104 */       item.setIndentationRight(0.0F);
/* 105 */       this.list.add(item);
/* 106 */     } else if (o instanceof List) {
/* 107 */       List nested = (List)o;
/* 108 */       nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
/* 109 */       this.first--;
/* 110 */       return this.list.add(nested);
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List cloneShallow() {
/* 117 */     RomanList clone = new RomanList();
/* 118 */     populateProperties(clone);
/* 119 */     return clone;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/RomanList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
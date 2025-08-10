/*     */ package com.itextpdf.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZapfDingbatsList
/*     */   extends List
/*     */ {
/*     */   protected int zn;
/*     */   
/*     */   public ZapfDingbatsList(int zn) {
/*  67 */     super(true);
/*  68 */     this.zn = zn;
/*  69 */     float fontsize = this.symbol.getFont().getSize();
/*  70 */     this.symbol.setFont(FontFactory.getFont("ZapfDingbats", fontsize, 0));
/*  71 */     this.postSymbol = " ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZapfDingbatsList(int zn, int symbolIndent) {
/*  81 */     super(true, symbolIndent);
/*  82 */     this.zn = zn;
/*  83 */     float fontsize = this.symbol.getFont().getSize();
/*  84 */     this.symbol.setFont(FontFactory.getFont("ZapfDingbats", fontsize, 0));
/*  85 */     this.postSymbol = " ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZapfDingbatsList(int zn, int symbolIndent, BaseColor zapfDingbatColor) {
/*  96 */     super(true, symbolIndent);
/*  97 */     this.zn = zn;
/*  98 */     float fontsize = this.symbol.getFont().getSize();
/*  99 */     this.symbol.setFont(FontFactory.getFont("ZapfDingbats", fontsize, 0, zapfDingbatColor));
/* 100 */     this.postSymbol = " ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDingbatColor(BaseColor zapfDingbatColor) {
/* 109 */     float fontsize = this.symbol.getFont().getSize();
/* 110 */     this.symbol.setFont(FontFactory.getFont("ZapfDingbats", fontsize, 0, zapfDingbatColor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharNumber(int zn) {
/* 118 */     this.zn = zn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCharNumber() {
/* 127 */     return this.zn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Element o) {
/* 138 */     if (o instanceof ListItem) {
/* 139 */       ListItem item = (ListItem)o;
/* 140 */       Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
/* 141 */       chunk.setAttributes(this.symbol.getAttributes());
/* 142 */       chunk.append(String.valueOf((char)this.zn));
/* 143 */       chunk.append(this.postSymbol);
/* 144 */       item.setListSymbol(chunk);
/* 145 */       item.setIndentationLeft(this.symbolIndent, this.autoindent);
/* 146 */       item.setIndentationRight(0.0F);
/* 147 */       this.list.add(item);
/* 148 */     } else if (o instanceof List) {
/* 149 */       List nested = (List)o;
/* 150 */       nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
/* 151 */       this.first--;
/* 152 */       return this.list.add(nested);
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List cloneShallow() {
/* 159 */     ZapfDingbatsList clone = new ZapfDingbatsList(this.zn);
/* 160 */     populateProperties(clone);
/* 161 */     return clone;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ZapfDingbatsList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
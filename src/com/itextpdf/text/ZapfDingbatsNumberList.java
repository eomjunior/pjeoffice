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
/*     */ public class ZapfDingbatsNumberList
/*     */   extends List
/*     */ {
/*     */   protected int type;
/*     */   
/*     */   public ZapfDingbatsNumberList(int type) {
/*  66 */     super(true);
/*  67 */     this.type = type;
/*  68 */     float fontsize = this.symbol.getFont().getSize();
/*  69 */     this.symbol.setFont(FontFactory.getFont("ZapfDingbats", fontsize, 0));
/*  70 */     this.postSymbol = " ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZapfDingbatsNumberList(int type, int symbolIndent) {
/*  79 */     super(true, symbolIndent);
/*  80 */     this.type = type;
/*  81 */     float fontsize = this.symbol.getFont().getSize();
/*  82 */     this.symbol.setFont(FontFactory.getFont("ZapfDingbats", fontsize, 0));
/*  83 */     this.postSymbol = " ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int type) {
/*  92 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 101 */     return this.type;
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
/* 112 */     if (o instanceof ListItem) {
/* 113 */       ListItem item = (ListItem)o;
/* 114 */       Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
/* 115 */       chunk.setAttributes(this.symbol.getAttributes());
/* 116 */       switch (this.type) {
/*     */         case 0:
/* 118 */           chunk.append(String.valueOf((char)(this.first + this.list.size() + 171)));
/*     */           break;
/*     */         case 1:
/* 121 */           chunk.append(String.valueOf((char)(this.first + this.list.size() + 181)));
/*     */           break;
/*     */         case 2:
/* 124 */           chunk.append(String.valueOf((char)(this.first + this.list.size() + 191)));
/*     */           break;
/*     */         default:
/* 127 */           chunk.append(String.valueOf((char)(this.first + this.list.size() + 201))); break;
/*     */       } 
/* 129 */       chunk.append(this.postSymbol);
/* 130 */       item.setListSymbol(chunk);
/* 131 */       item.setIndentationLeft(this.symbolIndent, this.autoindent);
/* 132 */       item.setIndentationRight(0.0F);
/* 133 */       this.list.add(item);
/* 134 */     } else if (o instanceof List) {
/* 135 */       List nested = (List)o;
/* 136 */       nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
/* 137 */       this.first--;
/* 138 */       return this.list.add(nested);
/*     */     } 
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List cloneShallow() {
/* 145 */     ZapfDingbatsNumberList clone = new ZapfDingbatsNumberList(this.type);
/* 146 */     populateProperties(clone);
/* 147 */     return clone;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ZapfDingbatsNumberList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
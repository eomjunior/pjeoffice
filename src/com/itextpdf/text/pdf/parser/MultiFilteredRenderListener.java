/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ public class MultiFilteredRenderListener
/*     */   implements RenderListener
/*     */ {
/*  55 */   private final List<RenderListener> delegates = new ArrayList<RenderListener>();
/*  56 */   private final List<RenderFilter[]> filters = (List)new ArrayList<RenderFilter>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends RenderListener> E attachRenderListener(E delegate, RenderFilter... filterSet) {
/*  65 */     this.delegates.add((RenderListener)delegate);
/*  66 */     this.filters.add(filterSet);
/*     */     
/*  68 */     return delegate;
/*     */   }
/*     */   
/*     */   public void beginTextBlock() {
/*  72 */     for (RenderListener delegate : this.delegates) {
/*  73 */       delegate.beginTextBlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderText(TextRenderInfo renderInfo) {
/*  78 */     for (int i = 0; i < this.delegates.size(); i++) {
/*  79 */       boolean filtersPassed = true;
/*  80 */       for (RenderFilter filter : (RenderFilter[])this.filters.get(i)) {
/*  81 */         if (!filter.allowText(renderInfo)) {
/*  82 */           filtersPassed = false;
/*     */           break;
/*     */         } 
/*     */       } 
/*  86 */       if (filtersPassed)
/*  87 */         ((RenderListener)this.delegates.get(i)).renderText(renderInfo); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endTextBlock() {
/*  92 */     for (RenderListener delegate : this.delegates) {
/*  93 */       delegate.endTextBlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderImage(ImageRenderInfo renderInfo) {
/*  98 */     for (int i = 0; i < this.delegates.size(); i++) {
/*  99 */       boolean filtersPassed = true;
/* 100 */       for (RenderFilter filter : (RenderFilter[])this.filters.get(i)) {
/* 101 */         if (!filter.allowImage(renderInfo)) {
/* 102 */           filtersPassed = false;
/*     */           break;
/*     */         } 
/*     */       } 
/* 106 */       if (filtersPassed)
/* 107 */         ((RenderListener)this.delegates.get(i)).renderImage(renderInfo); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/MultiFilteredRenderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
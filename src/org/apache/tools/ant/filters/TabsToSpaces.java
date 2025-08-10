/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TabsToSpaces
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final int DEFAULT_TAB_LENGTH = 8;
/*     */   private static final String TAB_LENGTH_KEY = "tablength";
/*  49 */   private int tabLength = 8;
/*     */ 
/*     */   
/*  52 */   private int spacesRemaining = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TabsToSpaces() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TabsToSpaces(Reader in) {
/*  70 */     super(in);
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
/*     */   public int read() throws IOException {
/*  84 */     if (!getInitialized()) {
/*  85 */       initialize();
/*  86 */       setInitialized(true);
/*     */     } 
/*     */     
/*  89 */     int ch = -1;
/*     */     
/*  91 */     if (this.spacesRemaining > 0) {
/*  92 */       this.spacesRemaining--;
/*  93 */       ch = 32;
/*     */     } else {
/*  95 */       ch = this.in.read();
/*  96 */       if (ch == 9) {
/*  97 */         this.spacesRemaining = this.tabLength - 1;
/*  98 */         ch = 32;
/*     */       } 
/*     */     } 
/* 101 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTablength(int tabLength) {
/* 110 */     this.tabLength = tabLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getTablength() {
/* 119 */     return this.tabLength;
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
/*     */   public Reader chain(Reader rdr) {
/* 133 */     TabsToSpaces newFilter = new TabsToSpaces(rdr);
/* 134 */     newFilter.setTablength(getTablength());
/* 135 */     newFilter.setInitialized(true);
/* 136 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 143 */     Parameter[] params = getParameters();
/* 144 */     if (params != null)
/* 145 */       for (Parameter param : params) {
/* 146 */         if (param != null && 
/* 147 */           "tablength".equals(param.getName())) {
/* 148 */           this.tabLength = Integer.parseInt(param.getValue());
/*     */           break;
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/TabsToSpaces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
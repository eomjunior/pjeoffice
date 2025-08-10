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
/*     */ public final class PrefixLines
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String PREFIX_KEY = "prefix";
/*  45 */   private String prefix = null;
/*     */ 
/*     */   
/*  48 */   private String queuedData = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrefixLines() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrefixLines(Reader in) {
/*  66 */     super(in);
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
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  82 */     if (!getInitialized()) {
/*  83 */       initialize();
/*  84 */       setInitialized(true);
/*     */     } 
/*     */     
/*  87 */     int ch = -1;
/*     */     
/*  89 */     if (this.queuedData != null && this.queuedData.isEmpty()) {
/*  90 */       this.queuedData = null;
/*     */     }
/*     */     
/*  93 */     if (this.queuedData != null) {
/*  94 */       ch = this.queuedData.charAt(0);
/*  95 */       this.queuedData = this.queuedData.substring(1);
/*  96 */       if (this.queuedData.isEmpty()) {
/*  97 */         this.queuedData = null;
/*     */       }
/*     */     } else {
/* 100 */       this.queuedData = readLine();
/* 101 */       if (this.queuedData == null) {
/* 102 */         ch = -1;
/*     */       } else {
/* 104 */         if (this.prefix != null) {
/* 105 */           this.queuedData = this.prefix + this.queuedData;
/*     */         }
/* 107 */         return read();
/*     */       } 
/*     */     } 
/* 110 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 121 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPrefix() {
/* 130 */     return this.prefix;
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
/* 144 */     PrefixLines newFilter = new PrefixLines(rdr);
/* 145 */     newFilter.setPrefix(getPrefix());
/* 146 */     newFilter.setInitialized(true);
/* 147 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 154 */     Parameter[] params = getParameters();
/* 155 */     if (params != null)
/* 156 */       for (Parameter param : params) {
/* 157 */         if ("prefix".equals(param.getName())) {
/* 158 */           this.prefix = param.getValue();
/*     */           break;
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/PrefixLines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
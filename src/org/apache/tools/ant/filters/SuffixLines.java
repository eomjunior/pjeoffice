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
/*     */ public final class SuffixLines
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String SUFFIX_KEY = "suffix";
/*  46 */   private String suffix = null;
/*     */ 
/*     */   
/*  49 */   private String queuedData = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SuffixLines() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SuffixLines(Reader in) {
/*  67 */     super(in);
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
/*  83 */     if (!getInitialized()) {
/*  84 */       initialize();
/*  85 */       setInitialized(true);
/*     */     } 
/*     */     
/*  88 */     int ch = -1;
/*     */     
/*  90 */     if (this.queuedData != null && this.queuedData.isEmpty()) {
/*  91 */       this.queuedData = null;
/*     */     }
/*     */     
/*  94 */     if (this.queuedData == null) {
/*  95 */       this.queuedData = readLine();
/*  96 */       if (this.queuedData == null) {
/*  97 */         ch = -1;
/*     */       } else {
/*  99 */         if (this.suffix != null) {
/* 100 */           String lf = "";
/* 101 */           if (this.queuedData.endsWith("\r\n")) {
/* 102 */             lf = "\r\n";
/* 103 */           } else if (this.queuedData.endsWith("\n")) {
/* 104 */             lf = "\n";
/*     */           } 
/* 106 */           this
/* 107 */             .queuedData = this.queuedData.substring(0, this.queuedData
/* 108 */               .length() - lf.length()) + this.suffix + lf;
/*     */         } 
/*     */         
/* 111 */         return read();
/*     */       } 
/*     */     } else {
/* 114 */       ch = this.queuedData.charAt(0);
/* 115 */       this.queuedData = this.queuedData.substring(1);
/* 116 */       if (this.queuedData.isEmpty()) {
/* 117 */         this.queuedData = null;
/*     */       }
/*     */     } 
/* 120 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String suffix) {
/* 131 */     this.suffix = suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSuffix() {
/* 140 */     return this.suffix;
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
/* 154 */     SuffixLines newFilter = new SuffixLines(rdr);
/* 155 */     newFilter.setSuffix(getSuffix());
/* 156 */     newFilter.setInitialized(true);
/* 157 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 164 */     Parameter[] params = getParameters();
/* 165 */     if (params != null)
/* 166 */       for (Parameter param : params) {
/* 167 */         if ("suffix".equals(param.getName())) {
/* 168 */           this.suffix = param.getValue();
/*     */           break;
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/SuffixLines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
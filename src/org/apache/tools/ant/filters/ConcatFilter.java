/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConcatFilter
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private File prepend;
/*     */   private File append;
/*  57 */   private Reader prependReader = null;
/*     */ 
/*     */   
/*  60 */   private Reader appendReader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcatFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcatFilter(Reader in) {
/*  78 */     super(in);
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
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  96 */     if (!getInitialized()) {
/*  97 */       initialize();
/*  98 */       setInitialized(true);
/*     */     } 
/*     */     
/* 101 */     int ch = -1;
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (this.prependReader != null) {
/* 106 */       ch = this.prependReader.read();
/* 107 */       if (ch == -1) {
/*     */         
/* 109 */         this.prependReader.close();
/* 110 */         this.prependReader = null;
/*     */       } 
/*     */     } 
/* 113 */     if (ch == -1) {
/* 114 */       ch = super.read();
/*     */     }
/* 116 */     if (ch == -1)
/*     */     {
/*     */       
/* 119 */       if (this.appendReader != null) {
/* 120 */         ch = this.appendReader.read();
/* 121 */         if (ch == -1) {
/*     */           
/* 123 */           this.appendReader.close();
/* 124 */           this.appendReader = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 129 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrepend(File prepend) {
/* 137 */     this.prepend = prepend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getPrepend() {
/* 145 */     return this.prepend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppend(File append) {
/* 153 */     this.append = append;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getAppend() {
/* 161 */     return this.append;
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
/* 175 */     ConcatFilter newFilter = new ConcatFilter(rdr);
/* 176 */     newFilter.setPrepend(getPrepend());
/* 177 */     newFilter.setAppend(getAppend());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() throws IOException {
/* 192 */     Parameter[] params = getParameters();
/* 193 */     if (params != null) {
/* 194 */       for (Parameter param : params) {
/* 195 */         String paramName = param.getName();
/* 196 */         if ("prepend".equals(paramName)) {
/* 197 */           setPrepend(new File(param.getValue()));
/* 198 */         } else if ("append".equals(paramName)) {
/* 199 */           setAppend(new File(param.getValue()));
/*     */         } 
/*     */       } 
/*     */     }
/* 203 */     if (this.prepend != null) {
/* 204 */       if (!this.prepend.isAbsolute()) {
/* 205 */         this.prepend = new File(getProject().getBaseDir(), this.prepend.getPath());
/*     */       }
/* 207 */       this.prependReader = new BufferedReader(new FileReader(this.prepend));
/*     */     } 
/* 209 */     if (this.append != null) {
/* 210 */       if (!this.append.isAbsolute()) {
/* 211 */         this.append = new File(getProject().getBaseDir(), this.append.getPath());
/*     */       }
/* 213 */       this.appendReader = new BufferedReader(new FileReader(this.append));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/ConcatFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
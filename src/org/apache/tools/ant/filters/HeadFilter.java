/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.util.LineTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HeadFilter
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String LINES_KEY = "lines";
/*     */   private static final String SKIP_KEY = "skip";
/*  47 */   private long linesRead = 0L;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_NUM_LINES = 10;
/*     */ 
/*     */   
/*  53 */   private long lines = 10L;
/*     */ 
/*     */   
/*  56 */   private long skip = 0L;
/*     */ 
/*     */   
/*  59 */   private LineTokenizer lineTokenizer = null;
/*     */ 
/*     */   
/*  62 */   private String line = null;
/*     */   
/*  64 */   private int linePos = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean eof;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeadFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeadFilter(Reader in) {
/*  85 */     super(in);
/*  86 */     this.lineTokenizer = new LineTokenizer();
/*  87 */     this.lineTokenizer.setIncludeDelims(true);
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
/* 103 */     if (!getInitialized()) {
/* 104 */       initialize();
/* 105 */       setInitialized(true);
/*     */     } 
/*     */     
/* 108 */     while (this.line == null || this.line.isEmpty()) {
/* 109 */       this.line = this.lineTokenizer.getToken(this.in);
/* 110 */       if (this.line == null) {
/* 111 */         return -1;
/*     */       }
/* 113 */       this.line = headFilter(this.line);
/* 114 */       if (this.eof) {
/* 115 */         return -1;
/*     */       }
/* 117 */       this.linePos = 0;
/*     */     } 
/*     */     
/* 120 */     int ch = this.line.charAt(this.linePos);
/* 121 */     this.linePos++;
/* 122 */     if (this.linePos == this.line.length()) {
/* 123 */       this.line = null;
/*     */     }
/* 125 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLines(long lines) {
/* 134 */     this.lines = lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getLines() {
/* 143 */     return this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkip(long skip) {
/* 152 */     this.skip = skip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getSkip() {
/* 161 */     return this.skip;
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
/* 175 */     HeadFilter newFilter = new HeadFilter(rdr);
/* 176 */     newFilter.setLines(getLines());
/* 177 */     newFilter.setSkip(getSkip());
/* 178 */     newFilter.setInitialized(true);
/* 179 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 188 */     Parameter[] params = getParameters();
/* 189 */     if (params != null) {
/* 190 */       for (Parameter param : params) {
/* 191 */         String paramName = param.getName();
/* 192 */         if ("lines".equals(paramName)) {
/* 193 */           this.lines = Long.parseLong(param.getValue());
/* 194 */         } else if ("skip".equals(paramName)) {
/* 195 */           this.skip = Long.parseLong(param.getValue());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String headFilter(String line) {
/* 205 */     this.linesRead++;
/* 206 */     if (this.skip > 0L && 
/* 207 */       this.linesRead - 1L < this.skip) {
/* 208 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 212 */     if (this.lines > 0L && 
/* 213 */       this.linesRead > this.lines + this.skip) {
/* 214 */       this.eof = true;
/* 215 */       return null;
/*     */     } 
/*     */     
/* 218 */     return line;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/HeadFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
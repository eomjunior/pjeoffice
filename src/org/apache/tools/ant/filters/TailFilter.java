/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TailFilter
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String LINES_KEY = "lines";
/*     */   private static final String SKIP_KEY = "skip";
/*     */   private static final int DEFAULT_NUM_LINES = 10;
/*  53 */   private long lines = 10L;
/*     */ 
/*     */   
/*  56 */   private long skip = 0L;
/*     */ 
/*     */   
/*     */   private boolean completedReadAhead = false;
/*     */ 
/*     */   
/*  62 */   private LineTokenizer lineTokenizer = null;
/*     */ 
/*     */   
/*  65 */   private String line = null;
/*     */   
/*  67 */   private int linePos = 0;
/*     */   
/*  69 */   private LinkedList<String> lineList = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TailFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TailFilter(Reader in) {
/*  87 */     super(in);
/*  88 */     this.lineTokenizer = new LineTokenizer();
/*  89 */     this.lineTokenizer.setIncludeDelims(true);
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
/*     */   public int read() throws IOException {
/* 106 */     if (!getInitialized()) {
/* 107 */       initialize();
/* 108 */       setInitialized(true);
/*     */     } 
/*     */     
/* 111 */     while (this.line == null || this.line.isEmpty()) {
/* 112 */       this.line = this.lineTokenizer.getToken(this.in);
/* 113 */       this.line = tailFilter(this.line);
/* 114 */       if (this.line == null) {
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
/* 175 */     TailFilter newFilter = new TailFilter(rdr);
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
/* 193 */           setLines(Long.parseLong(param.getValue()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String tailFilter(String line) {
/* 209 */     if (!this.completedReadAhead) {
/* 210 */       if (line != null) {
/* 211 */         this.lineList.add(line);
/* 212 */         if (this.lines == -1L) {
/* 213 */           if (this.lineList.size() > this.skip) {
/* 214 */             return this.lineList.removeFirst();
/*     */           }
/*     */         } else {
/* 217 */           long linesToKeep = this.lines + ((this.skip > 0L) ? this.skip : 0L);
/* 218 */           if (linesToKeep < this.lineList.size()) {
/* 219 */             this.lineList.removeFirst();
/*     */           }
/*     */         } 
/* 222 */         return "";
/*     */       } 
/* 224 */       this.completedReadAhead = true;
/* 225 */       if (this.skip > 0L) {
/* 226 */         for (int i = 0; i < this.skip; i++) {
/* 227 */           this.lineList.removeLast();
/*     */         }
/*     */       }
/* 230 */       if (this.lines > -1L) {
/* 231 */         while (this.lineList.size() > this.lines) {
/* 232 */           this.lineList.removeFirst();
/*     */         }
/*     */       }
/*     */     } 
/* 236 */     if (this.lineList.size() > 0) {
/* 237 */       return this.lineList.removeFirst();
/*     */     }
/* 239 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/TailFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ 
/*     */ 
/*     */ public final class StripLineBreaks
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String DEFAULT_LINE_BREAKS = "\r\n";
/*     */   private static final String LINE_BREAKS_KEY = "linebreaks";
/*  51 */   private String lineBreaks = "\r\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StripLineBreaks() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StripLineBreaks(Reader in) {
/*  69 */     super(in);
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
/*  83 */     if (!getInitialized()) {
/*  84 */       initialize();
/*  85 */       setInitialized(true);
/*     */     } 
/*     */     
/*  88 */     int ch = this.in.read();
/*  89 */     while (ch != -1 && 
/*  90 */       this.lineBreaks.indexOf(ch) != -1)
/*     */     {
/*     */       
/*  93 */       ch = this.in.read();
/*     */     }
/*     */     
/*  96 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineBreaks(String lineBreaks) {
/* 106 */     this.lineBreaks = lineBreaks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getLineBreaks() {
/* 116 */     return this.lineBreaks;
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
/* 130 */     StripLineBreaks newFilter = new StripLineBreaks(rdr);
/* 131 */     newFilter.setLineBreaks(getLineBreaks());
/* 132 */     newFilter.setInitialized(true);
/* 133 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 140 */     String userDefinedLineBreaks = null;
/* 141 */     Parameter[] params = getParameters();
/* 142 */     if (params != null) {
/* 143 */       for (Parameter param : params) {
/* 144 */         if ("linebreaks".equals(param.getName())) {
/* 145 */           userDefinedLineBreaks = param.getValue();
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 150 */     if (userDefinedLineBreaks != null)
/* 151 */       this.lineBreaks = userDefinedLineBreaks; 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/StripLineBreaks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
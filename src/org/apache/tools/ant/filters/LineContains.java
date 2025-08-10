/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LineContains
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String CONTAINS_KEY = "contains";
/*     */   private static final String NEGATE_KEY = "negate";
/*  72 */   private Vector<String> contains = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private String line = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean negate = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchAny = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineContains() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineContains(Reader in) {
/* 101 */     super(in);
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
/* 115 */     if (!getInitialized()) {
/* 116 */       initialize();
/* 117 */       setInitialized(true);
/*     */     } 
/*     */     
/* 120 */     int ch = -1;
/*     */     
/* 122 */     if (this.line != null) {
/* 123 */       ch = this.line.charAt(0);
/* 124 */       if (this.line.length() == 1) {
/* 125 */         this.line = null;
/*     */       } else {
/* 127 */         this.line = this.line.substring(1);
/*     */       } 
/*     */     } else {
/* 130 */       int containsSize = this.contains.size();
/*     */       
/* 132 */       for (this.line = readLine(); this.line != null; this.line = readLine()) {
/* 133 */         boolean matches = true;
/* 134 */         for (int i = 0; i < containsSize; ) {
/* 135 */           String containsStr = this.contains.elementAt(i);
/* 136 */           matches = this.line.contains(containsStr);
/* 137 */           if (!matches ? 
/* 138 */             this.matchAny : 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 147 */             !this.matchAny) {
/*     */             i++;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 153 */         if (matches ^ isNegated()) {
/*     */           break;
/*     */         }
/*     */       } 
/* 157 */       if (this.line != null) {
/* 158 */         return read();
/*     */       }
/*     */     } 
/* 161 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredContains(Contains contains) {
/* 171 */     this.contains.addElement(contains.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNegate(boolean b) {
/* 179 */     this.negate = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNegated() {
/* 187 */     return this.negate;
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
/*     */   public void setMatchAny(boolean matchAny) {
/* 200 */     this.matchAny = matchAny;
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
/*     */   public boolean isMatchAny() {
/* 212 */     return this.matchAny;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setContains(Vector<String> contains) {
/* 223 */     this.contains = contains;
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
/*     */   private Vector<String> getContains() {
/* 236 */     return this.contains;
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
/* 250 */     LineContains newFilter = new LineContains(rdr);
/* 251 */     newFilter.setContains(getContains());
/* 252 */     newFilter.setNegate(isNegated());
/* 253 */     newFilter.setMatchAny(isMatchAny());
/* 254 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 261 */     Parameter[] params = getParameters();
/* 262 */     if (params != null) {
/* 263 */       for (Parameter param : params) {
/* 264 */         if ("contains".equals(param.getType())) {
/* 265 */           this.contains.addElement(param.getValue());
/* 266 */         } else if ("negate".equals(param.getType())) {
/* 267 */           setNegate(Project.toBoolean(param.getValue()));
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
/*     */   public static class Contains
/*     */   {
/*     */     private String value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setValue(String contains) {
/* 288 */       this.value = contains;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getValue() {
/* 297 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/LineContains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
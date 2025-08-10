/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SortFilter
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String REVERSE_KEY = "reverse";
/*     */   private static final String COMPARATOR_KEY = "comparator";
/* 138 */   private Comparator<? super String> comparator = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean reverse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> lines;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   private String line = null;
/*     */   
/* 160 */   private Iterator<String> iterator = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortFilter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortFilter(Reader in) {
/* 179 */     super(in);
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
/* 196 */     if (!getInitialized()) {
/* 197 */       initialize();
/* 198 */       setInitialized(true);
/*     */     } 
/*     */     
/* 201 */     int ch = -1;
/* 202 */     if (this.line != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 207 */       ch = this.line.charAt(0);
/* 208 */       if (this.line.length() == 1) {
/* 209 */         this.line = null;
/*     */       } else {
/* 211 */         this.line = this.line.substring(1);
/*     */       } 
/*     */     } else {
/* 214 */       if (this.lines == null) {
/*     */         
/* 216 */         this.lines = new ArrayList<>();
/* 217 */         for (this.line = readLine(); this.line != null; this.line = readLine()) {
/* 218 */           this.lines.add(this.line);
/*     */         }
/* 220 */         sort();
/* 221 */         this.iterator = this.lines.iterator();
/*     */       } 
/*     */       
/* 224 */       if (this.iterator.hasNext()) {
/* 225 */         this.line = this.iterator.next();
/*     */       } else {
/* 227 */         this.line = null;
/* 228 */         this.lines = null;
/* 229 */         this.iterator = null;
/*     */       } 
/* 231 */       if (this.line != null) {
/* 232 */         return read();
/*     */       }
/*     */     } 
/* 235 */     return ch;
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
/* 249 */     SortFilter newFilter = new SortFilter(rdr);
/* 250 */     newFilter.setReverse(isReverse());
/* 251 */     newFilter.setComparator(getComparator());
/* 252 */     newFilter.setInitialized(true);
/* 253 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReverse() {
/* 264 */     return this.reverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReverse(boolean reverse) {
/* 275 */     this.reverse = reverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super String> getComparator() {
/* 284 */     return this.comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparator(Comparator<? super String> comparator) {
/* 294 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Comparator<? super String> comparator) {
/* 304 */     if (this.comparator != null && comparator != null) {
/* 305 */       throw new BuildException("can't have more than one comparator");
/*     */     }
/* 307 */     setComparator(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 315 */     Parameter[] params = getParameters();
/* 316 */     if (params != null) {
/* 317 */       for (Parameter param : params) {
/* 318 */         String paramName = param.getName();
/* 319 */         if ("reverse".equals(paramName)) {
/* 320 */           setReverse(Boolean.parseBoolean(param.getValue()));
/* 321 */         } else if ("comparator".equals(paramName)) {
/*     */           try {
/* 323 */             String className = param.getValue();
/*     */ 
/*     */             
/* 326 */             Comparator<? super String> comparatorInstance = Class.forName(className).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 327 */             setComparator(comparatorInstance);
/* 328 */           } catch (ClassCastException e) {
/* 329 */             throw new BuildException("Value of comparator attribute should implement java.util.Comparator interface");
/*     */ 
/*     */           
/*     */           }
/* 333 */           catch (Exception e) {
/*     */ 
/*     */ 
/*     */             
/* 337 */             throw new BuildException(e);
/*     */           } 
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
/*     */   private void sort() {
/* 350 */     if (this.comparator == null) {
/* 351 */       if (isReverse()) {
/* 352 */         this.lines.sort(Comparator.reverseOrder());
/*     */       } else {
/* 354 */         Collections.sort(this.lines);
/*     */       } 
/*     */     } else {
/* 357 */       this.lines.sort(this.comparator);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/SortFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
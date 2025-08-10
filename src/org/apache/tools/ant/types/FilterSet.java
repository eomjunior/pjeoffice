/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.VectorSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterSet
/*     */   extends DataType
/*     */   implements Cloneable
/*     */ {
/*     */   public static final String DEFAULT_TOKEN_START = "@";
/*     */   public static final String DEFAULT_TOKEN_END = "@";
/*     */   
/*     */   public static class Filter
/*     */   {
/*     */     String token;
/*     */     String value;
/*     */     
/*     */     public Filter(String token, String value) {
/*  61 */       setToken(token);
/*  62 */       setValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Filter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setToken(String token) {
/*  77 */       this.token = token;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/*  86 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getToken() {
/*  95 */       return this.token;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 104 */       return this.value;
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
/*     */ 
/*     */   
/*     */   public class FiltersFile
/*     */   {
/*     */     public void setFile(File file) {
/* 120 */       FilterSet.this.filtersFiles.add(file);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OnMissing
/*     */     extends EnumeratedAttribute
/*     */   {
/* 130 */     private static final String[] VALUES = new String[] { "fail", "warn", "ignore" };
/*     */ 
/*     */ 
/*     */     
/* 134 */     public static final OnMissing FAIL = new OnMissing("fail");
/*     */     
/* 136 */     public static final OnMissing WARN = new OnMissing("warn");
/*     */     
/* 138 */     public static final OnMissing IGNORE = new OnMissing("ignore");
/*     */ 
/*     */     
/*     */     private static final int FAIL_INDEX = 0;
/*     */ 
/*     */     
/*     */     private static final int WARN_INDEX = 1;
/*     */ 
/*     */     
/*     */     private static final int IGNORE_INDEX = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     public OnMissing() {}
/*     */ 
/*     */     
/*     */     public OnMissing(String value) {
/* 155 */       setValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 162 */       return VALUES;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   private String startOfToken = "@";
/* 173 */   private String endOfToken = "@";
/*     */   
/*     */   private Vector<String> passedTokens;
/*     */   
/*     */   private boolean duplicateToken = false;
/*     */   
/*     */   private boolean recurse = true;
/*     */   
/* 181 */   private Hashtable<String, String> filterHash = null;
/* 182 */   private Vector<File> filtersFiles = new Vector<>();
/* 183 */   private OnMissing onMissingFiltersFile = OnMissing.FAIL;
/*     */   
/*     */   private boolean readingFiles = false;
/* 186 */   private int recurseDepth = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   private Vector<Filter> filters = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilterSet(FilterSet filterset) {
/* 207 */     Vector<Filter> clone = (Vector<Filter>)filterset.getFilters().clone();
/* 208 */     this.filters = clone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized Vector<Filter> getFilters() {
/* 217 */     if (isReference()) {
/* 218 */       return getRef().getFilters();
/*     */     }
/* 220 */     dieOnCircularReference();
/*     */     
/* 222 */     if (!this.readingFiles) {
/* 223 */       this.readingFiles = true;
/* 224 */       for (File filtersFile : this.filtersFiles) {
/* 225 */         readFiltersFromFile(filtersFile);
/*     */       }
/* 227 */       this.filtersFiles.clear();
/* 228 */       this.readingFiles = false;
/*     */     } 
/* 230 */     return this.filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilterSet getRef() {
/* 239 */     return getCheckedRef(FilterSet.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Hashtable<String, String> getFilterHash() {
/* 248 */     if (isReference()) {
/* 249 */       return getRef().getFilterHash();
/*     */     }
/* 251 */     dieOnCircularReference();
/* 252 */     if (this.filterHash == null) {
/* 253 */       this.filterHash = new Hashtable<>(getFilters().size());
/* 254 */       getFilters().forEach(filter -> this.filterHash.put(filter.getToken(), filter.getValue()));
/*     */     } 
/* 256 */     return this.filterHash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiltersfile(File filtersFile) throws BuildException {
/* 267 */     if (isReference()) {
/* 268 */       throw tooManyAttributes();
/*     */     }
/* 270 */     this.filtersFiles.add(filtersFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeginToken(String startOfToken) {
/* 279 */     if (isReference()) {
/* 280 */       throw tooManyAttributes();
/*     */     }
/* 282 */     if (startOfToken == null || startOfToken.isEmpty()) {
/* 283 */       throw new BuildException("beginToken must not be empty");
/*     */     }
/* 285 */     this.startOfToken = startOfToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeginToken() {
/* 294 */     if (isReference()) {
/* 295 */       return getRef().getBeginToken();
/*     */     }
/* 297 */     return this.startOfToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndToken(String endOfToken) {
/* 306 */     if (isReference()) {
/* 307 */       throw tooManyAttributes();
/*     */     }
/* 309 */     if (endOfToken == null || endOfToken.isEmpty()) {
/* 310 */       throw new BuildException("endToken must not be empty");
/*     */     }
/* 312 */     this.endOfToken = endOfToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEndToken() {
/* 321 */     if (isReference()) {
/* 322 */       return getRef().getEndToken();
/*     */     }
/* 324 */     return this.endOfToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecurse(boolean recurse) {
/* 332 */     this.recurse = recurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRecurse() {
/* 340 */     return this.recurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void readFiltersFromFile(File filtersFile) throws BuildException {
/* 351 */     if (isReference()) {
/* 352 */       throw tooManyAttributes();
/*     */     }
/* 354 */     if (!filtersFile.exists()) {
/* 355 */       handleMissingFile("Could not read filters from file " + filtersFile + " as it doesn't exist.");
/*     */     }
/*     */     
/* 358 */     if (filtersFile.isFile()) {
/* 359 */       log("Reading filters from " + filtersFile, 3); 
/* 360 */       try { InputStream in = Files.newInputStream(filtersFile.toPath(), new java.nio.file.OpenOption[0]); 
/* 361 */         try { Properties props = new Properties();
/* 362 */           props.load(in);
/* 363 */           props.forEach((k, v) -> addFilter(new Filter((String)k, (String)v)));
/* 364 */           if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception ex)
/* 365 */       { throw new BuildException("Could not read filters from file: " + filtersFile, ex); }
/*     */     
/*     */     } else {
/*     */       
/* 369 */       handleMissingFile("Must specify a file rather than a directory in the filtersfile attribute:" + filtersFile);
/*     */     } 
/*     */     
/* 372 */     this.filterHash = null;
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
/*     */   public synchronized String replaceTokens(String line) {
/* 386 */     return iReplaceTokens(line);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addFilter(Filter filter) {
/* 395 */     if (isReference()) {
/* 396 */       throw noChildrenAllowed();
/*     */     }
/* 398 */     this.filters.addElement(filter);
/* 399 */     this.filterHash = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FiltersFile createFiltersfile() {
/* 408 */     if (isReference()) {
/* 409 */       throw noChildrenAllowed();
/*     */     }
/* 411 */     return new FiltersFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addFilter(String token, String value) {
/* 421 */     if (isReference()) {
/* 422 */       throw noChildrenAllowed();
/*     */     }
/* 424 */     addFilter(new Filter(token, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addConfiguredFilterSet(FilterSet filterSet) {
/* 433 */     if (isReference()) {
/* 434 */       throw noChildrenAllowed();
/*     */     }
/* 436 */     for (Filter filter : filterSet.getFilters()) {
/* 437 */       addFilter(filter);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addConfiguredPropertySet(PropertySet propertySet) {
/* 447 */     if (isReference()) {
/* 448 */       throw noChildrenAllowed();
/*     */     }
/* 450 */     Properties p = propertySet.getProperties();
/* 451 */     Set<Map.Entry<Object, Object>> entries = p.entrySet();
/* 452 */     for (Map.Entry<Object, Object> entry : entries) {
/* 453 */       addFilter(new Filter(String.valueOf(entry.getKey()), 
/* 454 */             String.valueOf(entry.getValue())));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean hasFilters() {
/* 464 */     return !getFilters().isEmpty();
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
/*     */   public synchronized Object clone() throws BuildException {
/* 476 */     if (isReference()) {
/* 477 */       return getRef().clone();
/*     */     }
/*     */     try {
/* 480 */       FilterSet fs = (FilterSet)super.clone();
/*     */       
/* 482 */       Vector<Filter> clonedFilters = (Vector<Filter>)getFilters().clone();
/* 483 */       fs.filters = clonedFilters;
/* 484 */       fs.setProject(getProject());
/* 485 */       return fs;
/* 486 */     } catch (CloneNotSupportedException e) {
/* 487 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnMissingFiltersFile(OnMissing onMissingFiltersFile) {
/* 496 */     this.onMissingFiltersFile = onMissingFiltersFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OnMissing getOnMissingFiltersFile() {
/* 504 */     return this.onMissingFiltersFile;
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
/*     */   private synchronized String iReplaceTokens(String line) {
/* 516 */     String beginToken = getBeginToken();
/* 517 */     String endToken = getEndToken();
/* 518 */     int index = line.indexOf(beginToken);
/*     */     
/* 520 */     if (index > -1) {
/* 521 */       Hashtable<String, String> tokens = getFilterHash();
/*     */       try {
/* 523 */         StringBuilder b = new StringBuilder();
/* 524 */         int i = 0;
/*     */         
/* 526 */         while (index > -1) {
/*     */           
/* 528 */           int endIndex = line.indexOf(endToken, index + beginToken
/* 529 */               .length() + 1);
/* 530 */           if (endIndex == -1) {
/*     */             break;
/*     */           }
/*     */           
/* 534 */           String token = line.substring(index + beginToken.length(), endIndex);
/* 535 */           b.append(line, i, index);
/* 536 */           if (tokens.containsKey(token)) {
/* 537 */             String value = tokens.get(token);
/* 538 */             if (this.recurse && !value.equals(token))
/*     */             {
/* 540 */               value = replaceTokens(value, token);
/*     */             }
/* 542 */             log("Replacing: " + beginToken + token + endToken + " -> " + value, 3);
/*     */             
/* 544 */             b.append(value);
/*     */             
/* 546 */             i = index + beginToken.length() + token.length() + endToken.length();
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 553 */             b.append(beginToken.charAt(0));
/* 554 */             i = index + 1;
/*     */           } 
/* 556 */           index = line.indexOf(beginToken, i);
/*     */         } 
/*     */         
/* 559 */         b.append(line.substring(i));
/* 560 */         return b.toString();
/* 561 */       } catch (StringIndexOutOfBoundsException e) {
/* 562 */         return line;
/*     */       } 
/*     */     } 
/* 565 */     return line;
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
/*     */   private synchronized String replaceTokens(String line, String parent) throws BuildException {
/* 578 */     String beginToken = getBeginToken();
/* 579 */     String endToken = getEndToken();
/* 580 */     if (this.recurseDepth == 0) {
/* 581 */       this.passedTokens = (Vector<String>)new VectorSet();
/*     */     }
/* 583 */     this.recurseDepth++;
/* 584 */     if (this.passedTokens.contains(parent) && !this.duplicateToken) {
/* 585 */       this.duplicateToken = true;
/* 586 */       System.out.println("Infinite loop in tokens. Currently known tokens : " + this.passedTokens
/*     */           
/* 588 */           .toString() + "\nProblem token : " + beginToken + parent + endToken + " called from " + beginToken + (String)this.passedTokens
/*     */           
/* 590 */           .lastElement() + endToken);
/* 591 */       this.recurseDepth--;
/* 592 */       return parent;
/*     */     } 
/* 594 */     this.passedTokens.addElement(parent);
/* 595 */     String value = iReplaceTokens(line);
/* 596 */     if (!value.contains(beginToken) && !this.duplicateToken && this.recurseDepth == 1) {
/* 597 */       this.passedTokens = null;
/* 598 */     } else if (this.duplicateToken) {
/*     */       
/* 600 */       if (!this.passedTokens.isEmpty()) {
/* 601 */         value = this.passedTokens.remove(this.passedTokens.size() - 1);
/* 602 */         if (this.passedTokens.isEmpty()) {
/* 603 */           value = beginToken + value + endToken;
/* 604 */           this.duplicateToken = false;
/*     */         } 
/*     */       } 
/* 607 */     } else if (!this.passedTokens.isEmpty()) {
/*     */       
/* 609 */       this.passedTokens.remove(this.passedTokens.size() - 1);
/*     */     } 
/* 611 */     this.recurseDepth--;
/* 612 */     return value;
/*     */   }
/*     */   
/*     */   private void handleMissingFile(String message) {
/* 616 */     switch (this.onMissingFiltersFile.getIndex()) {
/*     */       case 2:
/*     */         return;
/*     */       case 0:
/* 620 */         throw new BuildException(message);
/*     */       case 1:
/* 622 */         log(message, 1);
/*     */         return;
/*     */     } 
/* 625 */     throw new BuildException("Invalid value for onMissingFiltersFile");
/*     */   }
/*     */   
/*     */   public FilterSet() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/FilterSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
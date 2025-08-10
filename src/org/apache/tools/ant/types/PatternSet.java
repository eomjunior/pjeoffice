/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternSet
/*     */   extends DataType
/*     */   implements Cloneable
/*     */ {
/*  45 */   private List<NameEntry> includeList = new ArrayList<>();
/*  46 */   private List<NameEntry> excludeList = new ArrayList<>();
/*  47 */   private List<PatternFileNameEntry> includesFileList = new ArrayList<>();
/*  48 */   private List<PatternFileNameEntry> excludesFileList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class NameEntry
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private Object ifCond;
/*     */ 
/*     */     
/*     */     private Object unlessCond;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/*  67 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setIf(Object cond) {
/*  82 */       this.ifCond = cond;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setIf(String cond) {
/*  96 */       setIf(cond);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setUnless(Object cond) {
/* 111 */       this.unlessCond = cond;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setUnless(String cond) {
/* 125 */       setUnless(cond);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 132 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String evalName(Project p) {
/* 145 */       return valid(p) ? this.name : null;
/*     */     }
/*     */     
/*     */     private boolean valid(Project p) {
/* 149 */       PropertyHelper ph = PropertyHelper.getPropertyHelper(p);
/* 150 */       return (ph.testIfCondition(this.ifCond) && ph
/* 151 */         .testUnlessCondition(this.unlessCond));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       StringBuilder buf = new StringBuilder();
/* 160 */       if (this.name == null) {
/* 161 */         buf.append("noname");
/*     */       } else {
/* 163 */         buf.append(this.name);
/*     */       } 
/* 165 */       if (this.ifCond != null || this.unlessCond != null) {
/* 166 */         buf.append(":");
/* 167 */         String connector = "";
/*     */         
/* 169 */         if (this.ifCond != null) {
/* 170 */           buf.append("if->");
/* 171 */           buf.append(this.ifCond);
/* 172 */           connector = ";";
/*     */         } 
/* 174 */         if (this.unlessCond != null) {
/* 175 */           buf.append(connector);
/* 176 */           buf.append("unless->");
/* 177 */           buf.append(this.unlessCond);
/*     */         } 
/*     */       } 
/* 180 */       return buf.toString();
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
/*     */   public class PatternFileNameEntry
/*     */     extends NameEntry
/*     */   {
/*     */     private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setEncoding(String encoding) {
/* 204 */       this.encoding = encoding;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getEncoding() {
/* 214 */       return this.encoding;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 219 */       String baseString = super.toString();
/* 220 */       return (this.encoding == null) ? baseString : (
/* 221 */         baseString + ";encoding->" + this.encoding);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class InvertedPatternSet extends PatternSet {
/*     */     private InvertedPatternSet(PatternSet p) {
/* 227 */       setProject(p.getProject());
/* 228 */       addConfiguredPatternset(p);
/*     */     }
/*     */     
/*     */     public String[] getIncludePatterns(Project p) {
/* 232 */       return super.getExcludePatterns(p);
/*     */     }
/*     */     
/*     */     public String[] getExcludePatterns(Project p) {
/* 236 */       return super.getIncludePatterns(p);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) throws BuildException {
/* 258 */     if (!this.includeList.isEmpty() || !this.excludeList.isEmpty()) {
/* 259 */       throw tooManyAttributes();
/*     */     }
/* 261 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredPatternset(PatternSet p) {
/* 270 */     if (isReference()) {
/* 271 */       throw noChildrenAllowed();
/*     */     }
/* 273 */     String[] nestedIncludes = p.getIncludePatterns(getProject());
/* 274 */     String[] nestedExcludes = p.getExcludePatterns(getProject());
/*     */     
/* 276 */     if (nestedIncludes != null) {
/* 277 */       for (String nestedInclude : nestedIncludes) {
/* 278 */         createInclude().setName(nestedInclude);
/*     */       }
/*     */     }
/* 281 */     if (nestedExcludes != null) {
/* 282 */       for (String nestedExclude : nestedExcludes) {
/* 283 */         createExclude().setName(nestedExclude);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameEntry createInclude() {
/* 293 */     if (isReference()) {
/* 294 */       throw noChildrenAllowed();
/*     */     }
/* 296 */     return addPatternToList(this.includeList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameEntry createIncludesFile() {
/* 304 */     if (isReference()) {
/* 305 */       throw noChildrenAllowed();
/*     */     }
/* 307 */     return addPatternFileToList(this.includesFileList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameEntry createExclude() {
/* 315 */     if (isReference()) {
/* 316 */       throw noChildrenAllowed();
/*     */     }
/* 318 */     return addPatternToList(this.excludeList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameEntry createExcludesFile() {
/* 326 */     if (isReference()) {
/* 327 */       throw noChildrenAllowed();
/*     */     }
/* 329 */     return addPatternFileToList(this.excludesFileList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludes(String includes) {
/* 339 */     if (isReference()) {
/* 340 */       throw tooManyAttributes();
/*     */     }
/* 342 */     if (includes != null && !includes.isEmpty()) {
/* 343 */       StringTokenizer tok = new StringTokenizer(includes, ", ", false);
/* 344 */       while (tok.hasMoreTokens()) {
/* 345 */         createInclude().setName(tok.nextToken());
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
/*     */   public void setExcludes(String excludes) {
/* 357 */     if (isReference()) {
/* 358 */       throw tooManyAttributes();
/*     */     }
/* 360 */     if (excludes != null && !excludes.isEmpty()) {
/* 361 */       StringTokenizer tok = new StringTokenizer(excludes, ", ", false);
/* 362 */       while (tok.hasMoreTokens()) {
/* 363 */         createExclude().setName(tok.nextToken());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameEntry addPatternToList(List<NameEntry> list) {
/* 372 */     NameEntry result = new NameEntry();
/* 373 */     list.add(result);
/* 374 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PatternFileNameEntry addPatternFileToList(List<PatternFileNameEntry> list) {
/* 381 */     PatternFileNameEntry result = new PatternFileNameEntry();
/* 382 */     list.add(result);
/* 383 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludesfile(File includesFile) throws BuildException {
/* 393 */     if (isReference()) {
/* 394 */       throw tooManyAttributes();
/*     */     }
/* 396 */     createIncludesFile().setName(includesFile.getAbsolutePath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludesfile(File excludesFile) throws BuildException {
/* 406 */     if (isReference()) {
/* 407 */       throw tooManyAttributes();
/*     */     }
/* 409 */     createExcludesFile().setName(excludesFile.getAbsolutePath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readPatterns(File patternfile, String encoding, List<NameEntry> patternlist, Project p) throws BuildException {
/*     */     
/* 420 */     try { Reader r = (encoding == null) ? new FileReader(patternfile) : new InputStreamReader(new FileInputStream(patternfile), encoding); 
/* 421 */       try { BufferedReader patternReader = new BufferedReader(r);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 427 */         try { Objects.requireNonNull(p); patternReader.lines().filter(String::isEmpty.negate()).map(p::replaceProperties)
/* 428 */             .forEach(line -> addPatternToList(patternlist).setName(line));
/*     */           
/* 430 */           patternReader.close(); } catch (Throwable throwable) { try { patternReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (r != null) r.close();  } catch (Throwable throwable) { if (r != null) try { r.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 431 */     { throw new BuildException("An error occurred while reading from pattern file: " + patternfile, ioe); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(PatternSet other, Project p) {
/* 442 */     if (isReference()) {
/* 443 */       throw new BuildException("Cannot append to a reference");
/*     */     }
/* 445 */     dieOnCircularReference(p);
/* 446 */     String[] incl = other.getIncludePatterns(p);
/* 447 */     if (incl != null) {
/* 448 */       for (String include : incl) {
/* 449 */         createInclude().setName(include);
/*     */       }
/*     */     }
/* 452 */     String[] excl = other.getExcludePatterns(p);
/* 453 */     if (excl != null) {
/* 454 */       for (String exclude : excl) {
/* 455 */         createExclude().setName(exclude);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getIncludePatterns(Project p) {
/* 466 */     if (isReference()) {
/* 467 */       return getRef(p).getIncludePatterns(p);
/*     */     }
/* 469 */     dieOnCircularReference(p);
/* 470 */     readFiles(p);
/* 471 */     return makeArray(this.includeList, p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludePatterns(Project p) {
/* 480 */     if (isReference()) {
/* 481 */       return getRef(p).getExcludePatterns(p);
/*     */     }
/* 483 */     dieOnCircularReference(p);
/* 484 */     readFiles(p);
/* 485 */     return makeArray(this.excludeList, p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPatterns(Project p) {
/* 495 */     if (isReference()) {
/* 496 */       return getRef(p).hasPatterns(p);
/*     */     }
/* 498 */     dieOnCircularReference(p);
/* 499 */     return (!this.includesFileList.isEmpty() || !this.excludesFileList.isEmpty() || 
/* 500 */       !this.includeList.isEmpty() || !this.excludeList.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PatternSet getRef(Project p) {
/* 508 */     return getCheckedRef(PatternSet.class, getDataTypeName(), p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] makeArray(List<NameEntry> list, Project p) {
/* 515 */     if (list.isEmpty()) {
/* 516 */       return null;
/*     */     }
/* 518 */     return (String[])list.stream().map(ne -> ne.evalName(p)).filter(Objects::nonNull)
/* 519 */       .filter(pattern -> !pattern.isEmpty()).toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFiles(Project p) {
/* 526 */     if (!this.includesFileList.isEmpty()) {
/* 527 */       for (PatternFileNameEntry ne : this.includesFileList) {
/* 528 */         String fileName = ne.evalName(p);
/* 529 */         if (fileName != null) {
/* 530 */           File inclFile = p.resolveFile(fileName);
/* 531 */           if (!inclFile.exists()) {
/* 532 */             throw new BuildException("Includesfile " + inclFile.getAbsolutePath() + " not found.");
/*     */           }
/*     */           
/* 535 */           readPatterns(inclFile, ne.getEncoding(), this.includeList, p);
/*     */         } 
/*     */       } 
/* 538 */       this.includesFileList.clear();
/*     */     } 
/* 540 */     if (!this.excludesFileList.isEmpty()) {
/* 541 */       for (PatternFileNameEntry ne : this.excludesFileList) {
/* 542 */         String fileName = ne.evalName(p);
/* 543 */         if (fileName != null) {
/* 544 */           File exclFile = p.resolveFile(fileName);
/* 545 */           if (!exclFile.exists()) {
/* 546 */             throw new BuildException("Excludesfile " + exclFile.getAbsolutePath() + " not found.");
/*     */           }
/*     */           
/* 549 */           readPatterns(exclFile, ne.getEncoding(), this.excludeList, p);
/*     */         } 
/*     */       } 
/* 552 */       this.excludesFileList.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 561 */     return String.format("patternSet{ includes: %s excludes: %s }", new Object[] { this.includeList, this.excludeList });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 572 */       PatternSet ps = (PatternSet)super.clone();
/* 573 */       ps.includeList = new ArrayList<>(this.includeList);
/* 574 */       ps.excludeList = new ArrayList<>(this.excludeList);
/* 575 */       ps.includesFileList = new ArrayList<>(this.includesFileList);
/* 576 */       ps.excludesFileList = new ArrayList<>(this.excludesFileList);
/* 577 */       return ps;
/* 578 */     } catch (CloneNotSupportedException e) {
/* 579 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredInvert(PatternSet p) {
/* 588 */     addConfiguredPatternset(new InvertedPatternSet(p));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/PatternSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
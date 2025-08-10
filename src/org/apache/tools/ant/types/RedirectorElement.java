/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.Redirector;
/*     */ import org.apache.tools.ant.util.MergingMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedirectorElement
/*     */   extends DataType
/*     */ {
/*     */   private boolean usingInput = false;
/*     */   private boolean usingOutput = false;
/*     */   private boolean usingError = false;
/*     */   private Boolean logError;
/*     */   private String outputProperty;
/*     */   private String errorProperty;
/*     */   private String inputString;
/*     */   private Boolean append;
/*     */   private Boolean alwaysLog;
/*     */   private Boolean createEmptyFiles;
/*     */   private Mapper inputMapper;
/*     */   private Mapper outputMapper;
/*     */   private Mapper errorMapper;
/*  88 */   private Vector<FilterChain> inputFilterChains = new Vector<>();
/*     */ 
/*     */   
/*  91 */   private Vector<FilterChain> outputFilterChains = new Vector<>();
/*     */ 
/*     */   
/*  94 */   private Vector<FilterChain> errorFilterChains = new Vector<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private String outputEncoding;
/*     */ 
/*     */ 
/*     */   
/*     */   private String errorEncoding;
/*     */ 
/*     */   
/*     */   private String inputEncoding;
/*     */ 
/*     */   
/*     */   private Boolean logInputString;
/*     */ 
/*     */   
/*     */   private boolean outputIsBinary = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredInputMapper(Mapper inputMapper) {
/* 116 */     if (isReference()) {
/* 117 */       throw noChildrenAllowed();
/*     */     }
/* 119 */     if (this.inputMapper != null) {
/* 120 */       if (this.usingInput) {
/* 121 */         throw new BuildException("attribute \"input\" cannot coexist with a nested <inputmapper>");
/*     */       }
/*     */       
/* 124 */       throw new BuildException("Cannot have > 1 <inputmapper>");
/*     */     } 
/*     */     
/* 127 */     setChecked(false);
/* 128 */     this.inputMapper = inputMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredOutputMapper(Mapper outputMapper) {
/* 136 */     if (isReference()) {
/* 137 */       throw noChildrenAllowed();
/*     */     }
/* 139 */     if (this.outputMapper != null) {
/* 140 */       if (this.usingOutput) {
/* 141 */         throw new BuildException("attribute \"output\" cannot coexist with a nested <outputmapper>");
/*     */       }
/*     */       
/* 144 */       throw new BuildException("Cannot have > 1 <outputmapper>");
/*     */     } 
/*     */     
/* 147 */     setChecked(false);
/* 148 */     this.outputMapper = outputMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredErrorMapper(Mapper errorMapper) {
/* 156 */     if (isReference()) {
/* 157 */       throw noChildrenAllowed();
/*     */     }
/* 159 */     if (this.errorMapper != null) {
/* 160 */       if (this.usingError) {
/* 161 */         throw new BuildException("attribute \"error\" cannot coexist with a nested <errormapper>");
/*     */       }
/*     */       
/* 164 */       throw new BuildException("Cannot have > 1 <errormapper>");
/*     */     } 
/*     */     
/* 167 */     setChecked(false);
/* 168 */     this.errorMapper = errorMapper;
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
/*     */   public void setRefid(Reference r) throws BuildException {
/* 180 */     if (this.usingInput || this.usingOutput || this.usingError || this.inputString != null || this.logError != null || this.append != null || this.createEmptyFiles != null || this.inputEncoding != null || this.outputEncoding != null || this.errorEncoding != null || this.outputProperty != null || this.errorProperty != null || this.logInputString != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 193 */       throw tooManyAttributes();
/*     */     }
/* 195 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInput(File input) {
/* 203 */     if (isReference()) {
/* 204 */       throw tooManyAttributes();
/*     */     }
/* 206 */     if (this.inputString != null) {
/* 207 */       throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
/*     */     }
/*     */     
/* 210 */     this.usingInput = true;
/* 211 */     this.inputMapper = createMergeMapper(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputString(String inputString) {
/* 219 */     if (isReference()) {
/* 220 */       throw tooManyAttributes();
/*     */     }
/* 222 */     if (this.usingInput) {
/* 223 */       throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
/*     */     }
/*     */     
/* 226 */     this.inputString = inputString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogInputString(boolean logInputString) {
/* 236 */     if (isReference()) {
/* 237 */       throw tooManyAttributes();
/*     */     }
/* 239 */     this.logInputString = logInputString ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(File out) {
/* 249 */     if (isReference()) {
/* 250 */       throw tooManyAttributes();
/*     */     }
/* 252 */     if (out == null) {
/* 253 */       throw new IllegalArgumentException("output file specified as null");
/*     */     }
/* 255 */     this.usingOutput = true;
/* 256 */     this.outputMapper = createMergeMapper(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputEncoding(String outputEncoding) {
/* 264 */     if (isReference()) {
/* 265 */       throw tooManyAttributes();
/*     */     }
/* 267 */     this.outputEncoding = outputEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorEncoding(String errorEncoding) {
/* 276 */     if (isReference()) {
/* 277 */       throw tooManyAttributes();
/*     */     }
/* 279 */     this.errorEncoding = errorEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputEncoding(String inputEncoding) {
/* 287 */     if (isReference()) {
/* 288 */       throw tooManyAttributes();
/*     */     }
/* 290 */     this.inputEncoding = inputEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogError(boolean logError) {
/* 301 */     if (isReference()) {
/* 302 */       throw tooManyAttributes();
/*     */     }
/* 304 */     this.logError = logError ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(File error) {
/* 312 */     if (isReference()) {
/* 313 */       throw tooManyAttributes();
/*     */     }
/* 315 */     if (error == null) {
/* 316 */       throw new IllegalArgumentException("error file specified as null");
/*     */     }
/* 318 */     this.usingError = true;
/* 319 */     this.errorMapper = createMergeMapper(error);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputProperty(String outputProperty) {
/* 329 */     if (isReference()) {
/* 330 */       throw tooManyAttributes();
/*     */     }
/* 332 */     this.outputProperty = outputProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppend(boolean append) {
/* 342 */     if (isReference()) {
/* 343 */       throw tooManyAttributes();
/*     */     }
/* 345 */     this.append = append ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysLog(boolean alwaysLog) {
/* 356 */     if (isReference()) {
/* 357 */       throw tooManyAttributes();
/*     */     }
/* 359 */     this.alwaysLog = alwaysLog ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateEmptyFiles(boolean createEmptyFiles) {
/* 368 */     if (isReference()) {
/* 369 */       throw tooManyAttributes();
/*     */     }
/* 371 */     this
/* 372 */       .createEmptyFiles = createEmptyFiles ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorProperty(String errorProperty) {
/* 382 */     if (isReference()) {
/* 383 */       throw tooManyAttributes();
/*     */     }
/* 385 */     this.errorProperty = errorProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterChain createInputFilterChain() {
/* 393 */     if (isReference()) {
/* 394 */       throw noChildrenAllowed();
/*     */     }
/* 396 */     FilterChain result = new FilterChain();
/* 397 */     result.setProject(getProject());
/* 398 */     this.inputFilterChains.add(result);
/* 399 */     setChecked(false);
/* 400 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterChain createOutputFilterChain() {
/* 408 */     if (isReference()) {
/* 409 */       throw noChildrenAllowed();
/*     */     }
/* 411 */     FilterChain result = new FilterChain();
/* 412 */     result.setProject(getProject());
/* 413 */     this.outputFilterChains.add(result);
/* 414 */     setChecked(false);
/* 415 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterChain createErrorFilterChain() {
/* 423 */     if (isReference()) {
/* 424 */       throw noChildrenAllowed();
/*     */     }
/* 426 */     FilterChain result = new FilterChain();
/* 427 */     result.setProject(getProject());
/* 428 */     this.errorFilterChains.add(result);
/* 429 */     setChecked(false);
/* 430 */     return result;
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
/*     */   public void setBinaryOutput(boolean b) {
/* 443 */     this.outputIsBinary = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(Redirector redirector) {
/* 451 */     configure(redirector, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(Redirector redirector, String sourcefile) {
/* 461 */     if (isReference()) {
/* 462 */       getRef().configure(redirector, sourcefile);
/*     */       return;
/*     */     } 
/* 465 */     dieOnCircularReference();
/* 466 */     if (this.alwaysLog != null) {
/* 467 */       redirector.setAlwaysLog(this.alwaysLog.booleanValue());
/*     */     }
/* 469 */     if (this.logError != null) {
/* 470 */       redirector.setLogError(this.logError.booleanValue());
/*     */     }
/* 472 */     if (this.append != null) {
/* 473 */       redirector.setAppend(this.append.booleanValue());
/*     */     }
/* 475 */     if (this.createEmptyFiles != null) {
/* 476 */       redirector.setCreateEmptyFiles(this.createEmptyFiles.booleanValue());
/*     */     }
/* 478 */     if (this.outputProperty != null) {
/* 479 */       redirector.setOutputProperty(this.outputProperty);
/*     */     }
/* 481 */     if (this.errorProperty != null) {
/* 482 */       redirector.setErrorProperty(this.errorProperty);
/*     */     }
/* 484 */     if (this.inputString != null) {
/* 485 */       redirector.setInputString(this.inputString);
/*     */     }
/* 487 */     if (this.logInputString != null) {
/* 488 */       redirector.setLogInputString(this.logInputString.booleanValue());
/*     */     }
/* 490 */     if (this.inputMapper != null) {
/* 491 */       String[] inputTargets = null;
/*     */       
/*     */       try {
/* 494 */         inputTargets = this.inputMapper.getImplementation().mapFileName(sourcefile);
/* 495 */       } catch (NullPointerException enPeaEx) {
/* 496 */         if (sourcefile != null) {
/* 497 */           throw enPeaEx;
/*     */         }
/*     */       } 
/* 500 */       if (inputTargets != null && inputTargets.length > 0) {
/* 501 */         redirector.setInput(toFileArray(inputTargets));
/*     */       }
/*     */     } 
/* 504 */     if (this.outputMapper != null) {
/* 505 */       String[] outputTargets = null;
/*     */       
/*     */       try {
/* 508 */         outputTargets = this.outputMapper.getImplementation().mapFileName(sourcefile);
/* 509 */       } catch (NullPointerException enPeaEx) {
/* 510 */         if (sourcefile != null) {
/* 511 */           throw enPeaEx;
/*     */         }
/*     */       } 
/* 514 */       if (outputTargets != null && outputTargets.length > 0) {
/* 515 */         redirector.setOutput(toFileArray(outputTargets));
/*     */       }
/*     */     } 
/* 518 */     if (this.errorMapper != null) {
/* 519 */       String[] errorTargets = null;
/*     */       
/*     */       try {
/* 522 */         errorTargets = this.errorMapper.getImplementation().mapFileName(sourcefile);
/* 523 */       } catch (NullPointerException enPeaEx) {
/* 524 */         if (sourcefile != null) {
/* 525 */           throw enPeaEx;
/*     */         }
/*     */       } 
/* 528 */       if (errorTargets != null && errorTargets.length > 0) {
/* 529 */         redirector.setError(toFileArray(errorTargets));
/*     */       }
/*     */     } 
/* 532 */     if (!this.inputFilterChains.isEmpty()) {
/* 533 */       redirector.setInputFilterChains(this.inputFilterChains);
/*     */     }
/* 535 */     if (!this.outputFilterChains.isEmpty()) {
/* 536 */       redirector.setOutputFilterChains(this.outputFilterChains);
/*     */     }
/* 538 */     if (!this.errorFilterChains.isEmpty()) {
/* 539 */       redirector.setErrorFilterChains(this.errorFilterChains);
/*     */     }
/* 541 */     if (this.inputEncoding != null) {
/* 542 */       redirector.setInputEncoding(this.inputEncoding);
/*     */     }
/* 544 */     if (this.outputEncoding != null) {
/* 545 */       redirector.setOutputEncoding(this.outputEncoding);
/*     */     }
/* 547 */     if (this.errorEncoding != null) {
/* 548 */       redirector.setErrorEncoding(this.errorEncoding);
/*     */     }
/* 550 */     redirector.setBinaryOutput(this.outputIsBinary);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Mapper createMergeMapper(File destfile) {
/* 559 */     Mapper result = new Mapper(getProject());
/* 560 */     result.setClassname(MergingMapper.class.getName());
/* 561 */     result.setTo(destfile.getAbsolutePath());
/* 562 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File[] toFileArray(String[] name) {
/* 571 */     if (name == null) {
/* 572 */       return null;
/*     */     }
/*     */     
/* 575 */     ArrayList<File> list = new ArrayList<>(name.length);
/* 576 */     for (String n : name) {
/* 577 */       if (n != null) {
/* 578 */         list.add(getProject().resolveFile(n));
/*     */       }
/*     */     } 
/* 581 */     return list.<File>toArray(new File[0]);
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
/*     */   protected void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 594 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 597 */     if (isReference()) {
/* 598 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 600 */       for (Mapper m : Arrays.<Mapper>asList(new Mapper[] { this.inputMapper, this.outputMapper, this.errorMapper })) {
/* 601 */         if (m != null) {
/* 602 */           stk.push(m);
/* 603 */           m.dieOnCircularReference(stk, p);
/* 604 */           stk.pop();
/*     */         } 
/*     */       } 
/*     */       
/* 608 */       List<? extends List<FilterChain>> filterChainLists = Arrays.asList((List<FilterChain>[])new List[] { this.inputFilterChains, this.outputFilterChains, this.errorFilterChains });
/*     */       
/* 610 */       for (List<FilterChain> filterChains : filterChainLists) {
/* 611 */         if (filterChains != null) {
/* 612 */           for (FilterChain fc : filterChains) {
/* 613 */             pushAndInvokeCircularReferenceCheck(fc, stk, p);
/*     */           }
/*     */         }
/*     */       } 
/* 617 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RedirectorElement getRef() {
/* 627 */     return getCheckedRef(RedirectorElement.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/RedirectorElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
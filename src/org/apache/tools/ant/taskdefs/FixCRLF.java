/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.filters.ChainableReader;
/*     */ import org.apache.tools.ant.filters.FixCrLfFilter;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.FilterChain;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixCRLF
/*     */   extends MatchingTask
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String FIXCRLF_ERROR = "<fixcrlf> error: ";
/*     */   public static final String ERROR_FILE_AND_SRCDIR = "<fixcrlf> error: srcdir and file are mutually exclusive";
/*  92 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private boolean preserveLastModified = false;
/*     */   private File srcDir;
/*  96 */   private File destDir = null;
/*     */   private File file;
/*  98 */   private FixCrLfFilter filter = new FixCrLfFilter();
/*  99 */   private Vector<FilterChain> fcv = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private String outputEncoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Reader chain(Reader rdr) {
/* 120 */     return this.filter.chain(rdr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcdir(File srcDir) {
/* 128 */     this.srcDir = srcDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File destDir) {
/* 137 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavafiles(boolean javafiles) {
/* 145 */     this.filter.setJavafiles(javafiles);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 154 */     this.file = file;
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
/*     */   public void setEol(CrLf attr) {
/* 169 */     this.filter.setEol(FixCrLfFilter.CrLf.newInstance(attr.getValue()));
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
/*     */   @Deprecated
/*     */   public void setCr(AddAsisRemove attr) {
/* 187 */     log("DEPRECATED: The cr attribute has been deprecated,", 1);
/*     */     
/* 189 */     log("Please use the eol attribute instead", 1);
/* 190 */     String option = attr.getValue();
/* 191 */     CrLf c = new CrLf();
/* 192 */     if ("remove".equals(option)) {
/* 193 */       c.setValue("lf");
/* 194 */     } else if ("asis".equals(option)) {
/* 195 */       c.setValue("asis");
/*     */     } else {
/*     */       
/* 198 */       c.setValue("crlf");
/*     */     } 
/* 200 */     setEol(c);
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
/*     */   public void setTab(AddAsisRemove attr) {
/* 214 */     this.filter.setTab(FixCrLfFilter.AddAsisRemove.newInstance(attr.getValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTablength(int tlength) throws BuildException {
/*     */     try {
/* 225 */       this.filter.setTablength(tlength);
/* 226 */     } catch (IOException e) {
/*     */ 
/*     */       
/* 229 */       throw new BuildException(e.getMessage(), e);
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
/*     */   public void setEof(AddAsisRemove attr) {
/* 244 */     this.filter.setEof(FixCrLfFilter.AddAsisRemove.newInstance(attr.getValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 253 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputEncoding(String outputEncoding) {
/* 262 */     this.outputEncoding = outputEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixlast(boolean fixlast) {
/* 271 */     this.filter.setFixlast(fixlast);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveLastModified(boolean preserve) {
/* 280 */     this.preserveLastModified = preserve;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 290 */     validate();
/*     */ 
/*     */     
/* 293 */     String enc = (this.encoding == null) ? "default" : this.encoding;
/* 294 */     log("options: eol=" + this.filter
/* 295 */         .getEol().getValue() + " tab=" + this.filter
/* 296 */         .getTab().getValue() + " eof=" + this.filter
/* 297 */         .getEof().getValue() + " tablength=" + this.filter
/* 298 */         .getTablength() + " encoding=" + enc + " outputencoding=" + (
/*     */ 
/*     */         
/* 301 */         (this.outputEncoding == null) ? enc : this.outputEncoding), 3);
/*     */ 
/*     */     
/* 304 */     DirectoryScanner ds = getDirectoryScanner(this.srcDir);
/*     */     
/* 306 */     for (String filename : ds.getIncludedFiles()) {
/* 307 */       processFile(filename);
/*     */     }
/*     */   }
/*     */   
/*     */   private void validate() throws BuildException {
/* 312 */     if (this.file != null) {
/* 313 */       if (this.srcDir != null) {
/* 314 */         throw new BuildException("<fixcrlf> error: srcdir and file are mutually exclusive");
/*     */       }
/*     */       
/* 317 */       this.fileset.setFile(this.file);
/*     */       
/* 319 */       this.srcDir = this.file.getParentFile();
/*     */     } 
/* 321 */     if (this.srcDir == null) {
/* 322 */       throw new BuildException("<fixcrlf> error: srcdir attribute must be set!");
/*     */     }
/*     */     
/* 325 */     if (!this.srcDir.exists()) {
/* 326 */       throw new BuildException("<fixcrlf> error: srcdir does not exist: '%s'", new Object[] { this.srcDir });
/*     */     }
/*     */     
/* 329 */     if (!this.srcDir.isDirectory()) {
/* 330 */       throw new BuildException("<fixcrlf> error: srcdir is not a directory: '%s'", new Object[] { this.srcDir });
/*     */     }
/*     */     
/* 333 */     if (this.destDir != null) {
/* 334 */       if (!this.destDir.exists()) {
/* 335 */         throw new BuildException("<fixcrlf> error: destdir does not exist: '%s'", new Object[] { this.destDir });
/*     */       }
/*     */       
/* 338 */       if (!this.destDir.isDirectory()) {
/* 339 */         throw new BuildException("<fixcrlf> error: destdir is not a directory: '%s'", new Object[] { this.destDir });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processFile(String file) throws BuildException {
/* 347 */     File srcFile = new File(this.srcDir, file);
/* 348 */     long lastModified = srcFile.lastModified();
/* 349 */     File destD = (this.destDir == null) ? this.srcDir : this.destDir;
/*     */     
/* 351 */     if (this.fcv == null) {
/* 352 */       FilterChain fc = new FilterChain();
/* 353 */       fc.add((ChainableReader)this.filter);
/* 354 */       this.fcv = new Vector<>(1);
/* 355 */       this.fcv.add(fc);
/*     */     } 
/* 357 */     File tmpFile = FILE_UTILS.createTempFile(getProject(), "fixcrlf", "", null, true, true);
/*     */     try {
/* 359 */       FILE_UTILS.copyFile(srcFile, tmpFile, null, this.fcv, true, false, this.encoding, 
/* 360 */           (this.outputEncoding == null) ? this.encoding : this.outputEncoding, 
/* 361 */           getProject());
/*     */       
/* 363 */       File destFile = new File(destD, file);
/*     */       
/* 365 */       boolean destIsWrong = true;
/* 366 */       if (destFile.exists()) {
/*     */         
/* 368 */         log("destFile " + destFile + " exists", 4);
/* 369 */         destIsWrong = !FILE_UTILS.contentEquals(destFile, tmpFile);
/* 370 */         log(destFile + (destIsWrong ? " is being written" : 
/* 371 */             " is not written, as the contents are identical"), 4);
/*     */       } 
/*     */       
/* 374 */       if (destIsWrong) {
/* 375 */         FILE_UTILS.rename(tmpFile, destFile);
/* 376 */         if (this.preserveLastModified) {
/* 377 */           log("preserved lastModified for " + destFile, 4);
/*     */           
/* 379 */           FILE_UTILS.setFileLastModified(destFile, lastModified);
/*     */         } 
/*     */       } 
/* 382 */     } catch (IOException e) {
/* 383 */       throw new BuildException("error running fixcrlf on file " + srcFile, e);
/*     */     } finally {
/* 385 */       if (tmpFile != null && tmpFile.exists()) {
/* 386 */         FILE_UTILS.tryHardToDelete(tmpFile);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected class OneLiner
/*     */     implements Enumeration<Object>
/*     */   {
/*     */     private static final int UNDEF = -1;
/*     */     
/*     */     private static final int NOTJAVA = 0;
/*     */     
/*     */     private static final int LOOKING = 1;
/*     */     private static final int INBUFLEN = 8192;
/*     */     private static final int LINEBUFLEN = 200;
/*     */     private static final char CTRLZ = '\032';
/* 404 */     private int state = FixCRLF.this.filter.getJavafiles() ? 1 : 0;
/*     */     
/* 406 */     private StringBuffer eolStr = new StringBuffer(200);
/* 407 */     private StringBuffer eofStr = new StringBuffer();
/*     */     
/*     */     private BufferedReader reader;
/* 410 */     private StringBuffer line = new StringBuffer();
/*     */ 
/*     */     
/*     */     private boolean reachedEof = false;
/*     */ 
/*     */     
/*     */     private File srcFile;
/*     */ 
/*     */ 
/*     */     
/*     */     public OneLiner(File srcFile) throws BuildException {
/* 421 */       this.srcFile = srcFile;
/*     */       try {
/* 423 */         this
/*     */ 
/*     */           
/* 426 */           .reader = new BufferedReader((FixCRLF.this.encoding == null) ? new FileReader(srcFile) : new InputStreamReader(Files.newInputStream(srcFile.toPath(), new java.nio.file.OpenOption[0]), FixCRLF.this.encoding), 8192);
/*     */         
/* 428 */         nextLine();
/* 429 */       } catch (IOException e) {
/* 430 */         throw new BuildException(srcFile + ": " + e.getMessage(), e, FixCRLF.this
/* 431 */             .getLocation());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void nextLine() throws BuildException {
/* 441 */       int ch = -1;
/* 442 */       int eolcount = 0;
/*     */       
/* 444 */       this.eolStr = new StringBuffer();
/* 445 */       this.line = new StringBuffer();
/*     */       
/*     */       try {
/* 448 */         ch = this.reader.read();
/* 449 */         while (ch != -1 && ch != 13 && ch != 10) {
/* 450 */           this.line.append((char)ch);
/* 451 */           ch = this.reader.read();
/*     */         } 
/*     */         
/* 454 */         if (ch == -1 && this.line.length() == 0) {
/*     */           
/* 456 */           this.reachedEof = true;
/*     */           
/*     */           return;
/*     */         } 
/* 460 */         switch ((char)ch) {
/*     */ 
/*     */           
/*     */           case '\r':
/* 464 */             eolcount++;
/* 465 */             this.eolStr.append('\r');
/* 466 */             this.reader.mark(2);
/* 467 */             ch = this.reader.read();
/* 468 */             switch (ch) {
/*     */               case 13:
/* 470 */                 ch = this.reader.read();
/* 471 */                 if ((char)ch == '\n') {
/* 472 */                   eolcount += 2;
/* 473 */                   this.eolStr.append("\r\n"); break;
/*     */                 } 
/* 475 */                 this.reader.reset();
/*     */                 break;
/*     */               
/*     */               case 10:
/* 479 */                 eolcount++;
/* 480 */                 this.eolStr.append('\n');
/*     */                 break;
/*     */               
/*     */               case -1:
/*     */                 break;
/*     */             } 
/*     */             
/* 487 */             this.reader.reset();
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case '\n':
/* 493 */             eolcount++;
/* 494 */             this.eolStr.append('\n');
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 502 */         if (eolcount == 0) {
/* 503 */           int i = this.line.length();
/* 504 */           while (--i >= 0 && this.line.charAt(i) == '\032');
/*     */ 
/*     */           
/* 507 */           if (i < this.line.length() - 1)
/*     */           {
/*     */             
/* 510 */             this.eofStr.append(this.line.toString().substring(i + 1));
/* 511 */             if (i < 0) {
/* 512 */               this.line.setLength(0);
/* 513 */               this.reachedEof = true;
/*     */             } else {
/* 515 */               this.line.setLength(i + 1);
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         } 
/* 521 */       } catch (IOException e) {
/* 522 */         throw new BuildException(this.srcFile + ": " + e.getMessage(), e, FixCRLF.this
/* 523 */             .getLocation());
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getEofStr() {
/* 532 */       return this.eofStr.substring(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getState() {
/* 540 */       return this.state;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setState(int state) {
/* 548 */       this.state = state;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasMoreElements() {
/* 556 */       return !this.reachedEof;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object nextElement() throws NoSuchElementException {
/* 567 */       if (!hasMoreElements()) {
/* 568 */         throw new NoSuchElementException("OneLiner");
/*     */       }
/*     */       
/* 571 */       BufferLine tmpLine = new BufferLine(this.line.toString(), this.eolStr.substring(0));
/* 572 */       nextLine();
/* 573 */       return tmpLine;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 581 */       if (this.reader != null)
/* 582 */         this.reader.close(); 
/*     */     }
/*     */     
/*     */     class BufferLine
/*     */     {
/* 587 */       private int next = 0;
/* 588 */       private int column = 0;
/* 589 */       private int lookahead = -1;
/*     */       
/*     */       private String line;
/*     */       private String eolStr;
/*     */       
/*     */       public BufferLine(String line, String eolStr) throws BuildException {
/* 595 */         this.next = 0;
/* 596 */         this.column = 0;
/* 597 */         this.line = line;
/* 598 */         this.eolStr = eolStr;
/*     */       }
/*     */       
/*     */       public int getNext() {
/* 602 */         return this.next;
/*     */       }
/*     */       
/*     */       public void setNext(int next) {
/* 606 */         this.next = next;
/*     */       }
/*     */       
/*     */       public int getLookahead() {
/* 610 */         return this.lookahead;
/*     */       }
/*     */       
/*     */       public void setLookahead(int lookahead) {
/* 614 */         this.lookahead = lookahead;
/*     */       }
/*     */       
/*     */       public char getChar(int i) {
/* 618 */         return this.line.charAt(i);
/*     */       }
/*     */       
/*     */       public char getNextChar() {
/* 622 */         return getChar(this.next);
/*     */       }
/*     */       
/*     */       public char getNextCharInc() {
/* 626 */         return getChar(this.next++);
/*     */       }
/*     */       
/*     */       public int getColumn() {
/* 630 */         return this.column;
/*     */       }
/*     */       
/*     */       public void setColumn(int col) {
/* 634 */         this.column = col;
/*     */       }
/*     */       
/*     */       public int incColumn() {
/* 638 */         return this.column++;
/*     */       }
/*     */       
/*     */       public int length() {
/* 642 */         return this.line.length();
/*     */       }
/*     */       
/*     */       public int getEolLength() {
/* 646 */         return this.eolStr.length();
/*     */       }
/*     */       
/*     */       public String getLineString() {
/* 650 */         return this.line;
/*     */       }
/*     */       
/*     */       public String getEol() {
/* 654 */         return this.eolStr;
/*     */       }
/*     */       
/*     */       public String substring(int begin) {
/* 658 */         return this.line.substring(begin);
/*     */       }
/*     */       
/*     */       public String substring(int begin, int end) {
/* 662 */         return this.line.substring(begin, end);
/*     */       }
/*     */       
/*     */       public void setState(int state) {
/* 666 */         FixCRLF.OneLiner.this.setState(state);
/*     */       }
/*     */       
/*     */       public int getState() {
/* 670 */         return FixCRLF.OneLiner.this.getState();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AddAsisRemove
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 682 */       return new String[] { "add", "asis", "remove" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CrLf
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 696 */       return new String[] { "asis", "cr", "lf", "crlf", "mac", "unix", "dos" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/FixCRLF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
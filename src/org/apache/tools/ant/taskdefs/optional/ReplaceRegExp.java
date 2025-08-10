/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.Substitution;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.regexp.Regexp;
/*     */ import org.apache.tools.ant.util.regexp.RegexpUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReplaceRegExp
/*     */   extends Task
/*     */ {
/*     */   private File file;
/*     */   private String flags;
/*     */   private boolean byline;
/*     */   private Union resources;
/*     */   private RegularExpression regex;
/*     */   private Substitution subs;
/* 127 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean preserveLastModified = false;
/*     */ 
/*     */   
/* 134 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplaceRegExp() {
/* 139 */     this.file = null;
/* 140 */     this.flags = "";
/* 141 */     this.byline = false;
/*     */     
/* 143 */     this.regex = null;
/* 144 */     this.subs = null;
/*     */   }
/*     */ 
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
/*     */   public void setMatch(String match) {
/* 164 */     if (this.regex != null) {
/* 165 */       throw new BuildException("Only one regular expression is allowed");
/*     */     }
/*     */     
/* 168 */     this.regex = new RegularExpression();
/* 169 */     this.regex.setPattern(match);
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
/*     */   public void setReplace(String replace) {
/* 181 */     if (this.subs != null) {
/* 182 */       throw new BuildException("Only one substitution expression is allowed");
/*     */     }
/*     */ 
/*     */     
/* 186 */     this.subs = new Substitution();
/* 187 */     this.subs.setExpression(replace);
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
/*     */   public void setFlags(String flags) {
/* 207 */     this.flags = flags;
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
/*     */   @Deprecated
/*     */   public void setByLine(String byline) {
/* 223 */     this.byline = Boolean.parseBoolean(byline);
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
/*     */   public void setByLine(boolean byline) {
/* 236 */     this.byline = byline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 247 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 256 */     addConfigured((ResourceCollection)set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection rc) {
/* 266 */     if (!rc.isFilesystemOnly()) {
/* 267 */       throw new BuildException("only filesystem resources are supported");
/*     */     }
/* 269 */     if (this.resources == null) {
/* 270 */       this.resources = new Union();
/*     */     }
/* 272 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegularExpression createRegexp() {
/* 282 */     if (this.regex != null) {
/* 283 */       throw new BuildException("Only one regular expression is allowed.");
/*     */     }
/*     */     
/* 286 */     this.regex = new RegularExpression();
/* 287 */     return this.regex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Substitution createSubstitution() {
/* 298 */     if (this.subs != null) {
/* 299 */       throw new BuildException("Only one substitution expression is allowed");
/*     */     }
/*     */ 
/*     */     
/* 303 */     this.subs = new Substitution();
/* 304 */     return this.subs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveLastModified(boolean b) {
/* 315 */     this.preserveLastModified = b;
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
/*     */   protected String doReplace(RegularExpression r, Substitution s, String input, int options) {
/* 332 */     String res = input;
/* 333 */     Regexp regexp = r.getRegexp(getProject());
/*     */     
/* 335 */     if (regexp.matches(input, options)) {
/* 336 */       log("Found match; substituting", 4);
/* 337 */       res = regexp.substitute(input, s.getExpression(getProject()), options);
/*     */     } 
/*     */ 
/*     */     
/* 341 */     return res;
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
/*     */   protected void doReplace(File f, int options) throws IOException {
/* 353 */     File temp = FILE_UTILS.createTempFile(getProject(), "replace", ".txt", null, true, true);
/*     */     try {
/* 355 */       boolean changes = false;
/*     */       
/* 357 */       Charset charset = (this.encoding == null) ? Charset.defaultCharset() : Charset.forName(this.encoding);
/* 358 */       InputStream is = Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0]); 
/* 359 */       try { OutputStream os = Files.newOutputStream(temp.toPath(), new java.nio.file.OpenOption[0]); 
/* 360 */         try { Reader r = null;
/* 361 */           Writer w = null;
/*     */           try {
/* 363 */             r = new InputStreamReader(is, charset);
/* 364 */             w = new OutputStreamWriter(os, charset);
/* 365 */             log("Replacing pattern '" + this.regex.getPattern(getProject()) + "' with '" + this.subs
/* 366 */                 .getExpression(getProject()) + "' in '" + f
/* 367 */                 .getPath() + "'" + (this.byline ? " by line" : "") + (
/* 368 */                 this.flags.isEmpty() ? "" : (" with flags: '" + this.flags + "'")) + ".", 3);
/*     */ 
/*     */             
/* 371 */             if (this.byline) {
/* 372 */               int c; r = new BufferedReader(r);
/* 373 */               w = new BufferedWriter(w);
/*     */               
/* 375 */               StringBuilder linebuf = new StringBuilder();
/*     */               
/* 377 */               boolean hasCR = false;
/*     */               
/*     */               do {
/* 380 */                 c = r.read();
/*     */                 
/* 382 */                 if (c == 13) {
/* 383 */                   if (hasCR) {
/*     */                     
/* 385 */                     changes |= replaceAndWrite(linebuf.toString(), w, options);
/*     */                     
/* 387 */                     w.write(13);
/*     */                     
/* 389 */                     linebuf = new StringBuilder();
/*     */                   }
/*     */                   else {
/*     */                     
/* 393 */                     hasCR = true;
/*     */                   } 
/* 395 */                 } else if (c == 10) {
/*     */                   
/* 397 */                   changes |= replaceAndWrite(linebuf.toString(), w, options);
/*     */                   
/* 399 */                   if (hasCR) {
/* 400 */                     w.write(13);
/* 401 */                     hasCR = false;
/*     */                   } 
/* 403 */                   w.write(10);
/*     */                   
/* 405 */                   linebuf = new StringBuilder();
/*     */                 } else {
/* 407 */                   if (hasCR || c < 0) {
/*     */                     
/* 409 */                     changes |= replaceAndWrite(linebuf.toString(), w, options);
/*     */                     
/* 411 */                     if (hasCR) {
/* 412 */                       w.write(13);
/* 413 */                       hasCR = false;
/*     */                     } 
/*     */                     
/* 416 */                     linebuf = new StringBuilder();
/*     */                   } 
/*     */                   
/* 419 */                   if (c >= 0) {
/* 420 */                     linebuf.append((char)c);
/*     */                   }
/*     */                 } 
/* 423 */               } while (c >= 0);
/*     */             } else {
/*     */               
/* 426 */               changes = multilineReplace(r, w, options);
/*     */             } 
/*     */           } finally {
/* 429 */             FileUtils.close(r);
/* 430 */             FileUtils.close(w);
/*     */           } 
/* 432 */           if (os != null) os.close();  } catch (Throwable throwable) { if (os != null) try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/* 433 */           try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (changes) {
/* 434 */         log("File has changed; saving the updated file", 3);
/*     */         try {
/* 436 */           long origLastModified = f.lastModified();
/* 437 */           FILE_UTILS.rename(temp, f);
/* 438 */           if (this.preserveLastModified) {
/* 439 */             FILE_UTILS.setFileLastModified(f, origLastModified);
/*     */           }
/* 441 */           temp = null;
/* 442 */         } catch (IOException e) {
/* 443 */           throw new BuildException("Couldn't rename temporary file " + temp, e, 
/* 444 */               getLocation());
/*     */         } 
/*     */       } else {
/* 447 */         log("No change made", 4);
/*     */       } 
/*     */     } finally {
/* 450 */       if (temp != null) {
/* 451 */         temp.delete();
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
/*     */   public void execute() throws BuildException {
/* 463 */     if (this.regex == null) {
/* 464 */       throw new BuildException("No expression to match.");
/*     */     }
/* 466 */     if (this.subs == null) {
/* 467 */       throw new BuildException("Nothing to replace expression with.");
/*     */     }
/*     */     
/* 470 */     if (this.file != null && this.resources != null) {
/* 471 */       throw new BuildException("You cannot supply the 'file' attribute and resource collections at the same time.");
/*     */     }
/*     */ 
/*     */     
/* 475 */     int options = RegexpUtil.asOptions(this.flags);
/*     */     
/* 477 */     if (this.file != null && this.file.exists()) {
/*     */       try {
/* 479 */         doReplace(this.file, options);
/* 480 */       } catch (IOException e) {
/* 481 */         log("An error occurred processing file: '" + this.file
/* 482 */             .getAbsolutePath() + "': " + e.toString(), 0);
/*     */       }
/*     */     
/* 485 */     } else if (this.file != null) {
/* 486 */       log("The following file is missing: '" + this.file
/* 487 */           .getAbsolutePath() + "'", 0);
/*     */     } 
/*     */     
/* 490 */     if (this.resources != null) {
/* 491 */       for (Resource r : this.resources) {
/* 492 */         File f = ((FileProvider)r.as(FileProvider.class)).getFile();
/*     */         
/* 494 */         if (f.exists()) {
/*     */           try {
/* 496 */             doReplace(f, options);
/* 497 */           } catch (Exception e) {
/* 498 */             log("An error occurred processing file: '" + f
/* 499 */                 .getAbsolutePath() + "': " + e.toString(), 0);
/*     */           } 
/*     */           continue;
/*     */         } 
/* 503 */         log("The following file is missing: '" + f
/* 504 */             .getAbsolutePath() + "'", 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean multilineReplace(Reader r, Writer w, int options) throws IOException {
/* 512 */     return replaceAndWrite(FileUtils.safeReadFully(r), w, options);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean replaceAndWrite(String s, Writer w, int options) throws IOException {
/* 517 */     String res = doReplace(this.regex, this.subs, s, options);
/* 518 */     w.write(res);
/* 519 */     return !res.equals(s);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ReplaceRegExp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
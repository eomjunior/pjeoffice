/*     */ package org.apache.tools.ant.taskdefs.cvslib;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.AbstractCvsTask;
/*     */ import org.apache.tools.ant.util.DOMElementWriter;
/*     */ import org.apache.tools.ant.util.DOMUtils;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CvsTagDiff
/*     */   extends AbstractCvsTask
/*     */ {
/*  78 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*  81 */   private static final DOMElementWriter DOM_WRITER = new DOMElementWriter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String FILE_STRING = "File ";
/*     */ 
/*     */ 
/*     */   
/*  90 */   static final int FILE_STRING_LENGTH = "File ".length();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String TO_STRING = " to ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String FILE_IS_NEW = " is new;";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String REVISION = "revision ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String FILE_HAS_CHANGED = " changed from revision ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String FILE_WAS_REMOVED = " is removed";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String mypackage;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String mystartTag;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String myendTag;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String mystartDate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String myendDate;
/*     */ 
/*     */ 
/*     */   
/*     */   private File mydestfile;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ignoreRemoved = false;
/*     */ 
/*     */ 
/*     */   
/* 152 */   private List<String> packageNames = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   private String[] packageNamePrefixes = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   private int[] packageNamePrefixLengths = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackage(String p) {
/* 170 */     this.mypackage = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTag(String s) {
/* 179 */     this.mystartTag = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartDate(String s) {
/* 188 */     this.mystartDate = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndTag(String s) {
/* 197 */     this.myendTag = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndDate(String s) {
/* 206 */     this.myendDate = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestFile(File f) {
/* 215 */     this.mydestfile = f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreRemoved(boolean b) {
/* 226 */     this.ignoreRemoved = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 237 */     validate();
/*     */ 
/*     */     
/* 240 */     addCommandArgument("rdiff");
/* 241 */     addCommandArgument("-s");
/* 242 */     if (this.mystartTag != null) {
/* 243 */       addCommandArgument("-r");
/* 244 */       addCommandArgument(this.mystartTag);
/*     */     } else {
/* 246 */       addCommandArgument("-D");
/* 247 */       addCommandArgument(this.mystartDate);
/*     */     } 
/* 249 */     if (this.myendTag != null) {
/* 250 */       addCommandArgument("-r");
/* 251 */       addCommandArgument(this.myendTag);
/*     */     } else {
/* 253 */       addCommandArgument("-D");
/* 254 */       addCommandArgument(this.myendDate);
/*     */     } 
/*     */ 
/*     */     
/* 258 */     setCommand("");
/* 259 */     File tmpFile = null;
/*     */     try {
/* 261 */       handlePackageNames();
/*     */       
/* 263 */       tmpFile = FILE_UTILS.createTempFile(getProject(), "cvstagdiff", ".log", null, true, true);
/*     */       
/* 265 */       setOutput(tmpFile);
/*     */ 
/*     */       
/* 268 */       super.execute();
/*     */ 
/*     */       
/* 271 */       CvsTagEntry[] entries = parseRDiff(tmpFile);
/*     */ 
/*     */       
/* 274 */       writeTagDiff(entries);
/*     */     } finally {
/*     */       
/* 277 */       this.packageNamePrefixes = null;
/* 278 */       this.packageNamePrefixLengths = null;
/* 279 */       this.packageNames.clear();
/* 280 */       if (tmpFile != null) {
/* 281 */         tmpFile.delete();
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
/*     */ 
/*     */   
/*     */   private CvsTagEntry[] parseRDiff(File tmpFile) throws BuildException {
/*     */     
/* 297 */     try { BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       try { List<CvsTagEntry> entries = new ArrayList<>();
/*     */         
/* 316 */         String line = reader.readLine();
/*     */         
/* 318 */         while (null != line) {
/* 319 */           line = removePackageName(line, this.packageNamePrefixes, this.packageNamePrefixLengths);
/*     */           
/* 321 */           if (line != null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 327 */             boolean bool = (doFileIsNew(entries, line) || doFileHasChanged(entries, line) || doFileWasRemoved(entries, line)) ? true : false;
/*     */           }
/* 329 */           line = reader.readLine();
/*     */         } 
/*     */         
/* 332 */         CvsTagEntry[] arrayOfCvsTagEntry = entries.<CvsTagEntry>toArray(new CvsTagEntry[0]);
/* 333 */         reader.close(); return arrayOfCvsTagEntry; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 334 */     { throw new BuildException("Error in parsing", e); }
/*     */   
/*     */   }
/*     */   
/*     */   private boolean doFileIsNew(List<CvsTagEntry> entries, String line) {
/* 339 */     int index = line.indexOf(" is new;");
/* 340 */     if (index == -1) {
/* 341 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 345 */     String filename = line.substring(0, index);
/* 346 */     String rev = null;
/* 347 */     int indexrev = line.indexOf("revision ", index);
/* 348 */     if (indexrev != -1) {
/* 349 */       rev = line.substring(indexrev + "revision ".length());
/*     */     }
/* 351 */     CvsTagEntry entry = new CvsTagEntry(filename, rev);
/* 352 */     entries.add(entry);
/* 353 */     log(entry.toString(), 3);
/* 354 */     return true;
/*     */   }
/*     */   
/*     */   private boolean doFileHasChanged(List<CvsTagEntry> entries, String line) {
/* 358 */     int index = line.indexOf(" changed from revision ");
/* 359 */     if (index == -1) {
/* 360 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 364 */     String filename = line.substring(0, index);
/* 365 */     int revSeparator = line.indexOf(" to ", index);
/*     */     
/* 367 */     String prevRevision = line.substring(index + " changed from revision ".length(), revSeparator);
/*     */     
/* 369 */     String revision = line.substring(revSeparator + " to ".length());
/* 370 */     CvsTagEntry entry = new CvsTagEntry(filename, revision, prevRevision);
/*     */ 
/*     */     
/* 373 */     entries.add(entry);
/* 374 */     log(entry.toString(), 3);
/* 375 */     return true;
/*     */   }
/*     */   
/*     */   private boolean doFileWasRemoved(List<CvsTagEntry> entries, String line) {
/* 379 */     if (this.ignoreRemoved) {
/* 380 */       return false;
/*     */     }
/* 382 */     int index = line.indexOf(" is removed");
/* 383 */     if (index == -1) {
/* 384 */       return false;
/*     */     }
/*     */     
/* 387 */     String filename = line.substring(0, index);
/* 388 */     String rev = null;
/* 389 */     int indexrev = line.indexOf("revision ", index);
/* 390 */     if (indexrev != -1) {
/* 391 */       rev = line.substring(indexrev + "revision ".length());
/*     */     }
/* 393 */     CvsTagEntry entry = new CvsTagEntry(filename, null, rev);
/* 394 */     entries.add(entry);
/* 395 */     log(entry.toString(), 3);
/* 396 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeTagDiff(CvsTagEntry[] entries) throws BuildException {
/*     */     
/* 407 */     try { PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.mydestfile.toPath(), new java.nio.file.OpenOption[0]), StandardCharsets.UTF_8)); 
/* 408 */       try { writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/* 409 */         Document doc = DOMUtils.newDocument();
/* 410 */         Element root = doc.createElement("tagdiff");
/* 411 */         if (this.mystartTag != null) {
/* 412 */           root.setAttribute("startTag", this.mystartTag);
/*     */         } else {
/* 414 */           root.setAttribute("startDate", this.mystartDate);
/*     */         } 
/* 416 */         if (this.myendTag != null) {
/* 417 */           root.setAttribute("endTag", this.myendTag);
/*     */         } else {
/* 419 */           root.setAttribute("endDate", this.myendDate);
/*     */         } 
/*     */         
/* 422 */         root.setAttribute("cvsroot", getCvsRoot());
/* 423 */         root.setAttribute("package", 
/* 424 */             String.join(",", (Iterable)this.packageNames));
/* 425 */         DOM_WRITER.openElement(root, writer, 0, "\t");
/* 426 */         writer.println();
/* 427 */         for (CvsTagEntry entry : entries) {
/* 428 */           writeTagEntry(doc, writer, entry);
/*     */         }
/* 430 */         DOM_WRITER.closeElement(root, writer, 0, "\t", true);
/* 431 */         writer.flush();
/* 432 */         if (writer.checkError()) {
/* 433 */           throw new IOException("Encountered an error writing tagdiff");
/*     */         }
/* 435 */         writer.close(); } catch (Throwable throwable) { try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (UnsupportedEncodingException uee)
/* 436 */     { log(uee.toString(), 0); }
/* 437 */     catch (IOException ioe)
/* 438 */     { throw new BuildException(ioe.toString(), ioe); }
/*     */   
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
/*     */   private void writeTagEntry(Document doc, PrintWriter writer, CvsTagEntry entry) throws IOException {
/* 452 */     Element ent = doc.createElement("entry");
/* 453 */     Element f = DOMUtils.createChildElement(ent, "file");
/* 454 */     DOMUtils.appendCDATAElement(f, "name", entry.getFile());
/* 455 */     if (entry.getRevision() != null) {
/* 456 */       DOMUtils.appendTextElement(f, "revision", entry.getRevision());
/*     */     }
/* 458 */     if (entry.getPreviousRevision() != null) {
/* 459 */       DOMUtils.appendTextElement(f, "prevrevision", entry
/* 460 */           .getPreviousRevision());
/*     */     }
/* 462 */     DOM_WRITER.write(ent, writer, 1, "\t");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 471 */     if (null == this.mypackage && getModules().isEmpty()) {
/* 472 */       throw new BuildException("Package/module must be set.");
/*     */     }
/*     */     
/* 475 */     if (null == this.mydestfile) {
/* 476 */       throw new BuildException("Destfile must be set.");
/*     */     }
/*     */     
/* 479 */     if (null == this.mystartTag && null == this.mystartDate) {
/* 480 */       throw new BuildException("Start tag or start date must be set.");
/*     */     }
/*     */     
/* 483 */     if (null != this.mystartTag && null != this.mystartDate) {
/* 484 */       throw new BuildException("Only one of start tag and start date must be set.");
/*     */     }
/*     */ 
/*     */     
/* 488 */     if (null == this.myendTag && null == this.myendDate) {
/* 489 */       throw new BuildException("End tag or end date must be set.");
/*     */     }
/*     */     
/* 492 */     if (null != this.myendTag && null != this.myendDate) {
/* 493 */       throw new BuildException("Only one of end tag and end date must be set.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handlePackageNames() {
/* 503 */     if (this.mypackage != null) {
/*     */       
/* 505 */       StringTokenizer myTokenizer = new StringTokenizer(this.mypackage);
/* 506 */       while (myTokenizer.hasMoreTokens()) {
/* 507 */         String pack = myTokenizer.nextToken();
/* 508 */         this.packageNames.add(pack);
/* 509 */         addCommandArgument(pack);
/*     */       } 
/*     */     } 
/* 512 */     for (AbstractCvsTask.Module m : getModules()) {
/* 513 */       this.packageNames.add(m.getName());
/*     */     }
/*     */     
/* 516 */     this.packageNamePrefixes = new String[this.packageNames.size()];
/* 517 */     this.packageNamePrefixLengths = new int[this.packageNames.size()];
/* 518 */     for (int i = 0; i < this.packageNamePrefixes.length; i++) {
/* 519 */       this.packageNamePrefixes[i] = "File " + (String)this.packageNames.get(i) + "/";
/* 520 */       this.packageNamePrefixLengths[i] = this.packageNamePrefixes[i].length();
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
/*     */   private static String removePackageName(String line, String[] packagePrefixes, int[] prefixLengths) {
/* 532 */     if (line.length() < FILE_STRING_LENGTH) {
/* 533 */       return null;
/*     */     }
/* 535 */     for (int i = 0; i < packagePrefixes.length; i++) {
/* 536 */       if (line.startsWith(packagePrefixes[i])) {
/* 537 */         return line.substring(prefixLengths[i]);
/*     */       }
/*     */     } 
/* 540 */     return line.substring(FILE_STRING_LENGTH);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/CvsTagDiff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
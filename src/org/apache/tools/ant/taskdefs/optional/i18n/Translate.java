/*     */ package org.apache.tools.ant.taskdefs.optional.i18n;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Translate
/*     */   extends MatchingTask
/*     */ {
/*     */   private static final int BUNDLE_SPECIFIED_LANGUAGE_COUNTRY_VARIANT = 0;
/*     */   private static final int BUNDLE_SPECIFIED_LANGUAGE_COUNTRY = 1;
/*     */   private static final int BUNDLE_SPECIFIED_LANGUAGE = 2;
/*     */   private static final int BUNDLE_NOMATCH = 3;
/*     */   private static final int BUNDLE_DEFAULT_LANGUAGE_COUNTRY_VARIANT = 4;
/*     */   private static final int BUNDLE_DEFAULT_LANGUAGE_COUNTRY = 5;
/*     */   private static final int BUNDLE_DEFAULT_LANGUAGE = 6;
/*     */   private static final int BUNDLE_MAX_ALTERNATIVES = 7;
/*     */   private String bundle;
/*     */   private String bundleLanguage;
/*     */   private String bundleCountry;
/*     */   private String bundleVariant;
/*     */   private File toDir;
/*     */   private String srcEncoding;
/*     */   private String destEncoding;
/*     */   private String bundleEncoding;
/*     */   private String startToken;
/*     */   private String endToken;
/*     */   private boolean forceOverwrite;
/* 142 */   private List<FileSet> filesets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   private Map<String, String> resourceMap = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   private long[] bundleLastModified = new long[7];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long srcLastModified;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long destLastModified;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean loaded = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundle(String bundle) {
/* 179 */     this.bundle = bundle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundleLanguage(String bundleLanguage) {
/* 187 */     this.bundleLanguage = bundleLanguage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundleCountry(String bundleCountry) {
/* 195 */     this.bundleCountry = bundleCountry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundleVariant(String bundleVariant) {
/* 203 */     this.bundleVariant = bundleVariant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToDir(File toDir) {
/* 211 */     this.toDir = toDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartToken(String startToken) {
/* 219 */     this.startToken = startToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndToken(String endToken) {
/* 227 */     this.endToken = endToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcEncoding(String srcEncoding) {
/* 236 */     this.srcEncoding = srcEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestEncoding(String destEncoding) {
/* 245 */     this.destEncoding = destEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBundleEncoding(String bundleEncoding) {
/* 254 */     this.bundleEncoding = bundleEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForceOverwrite(boolean forceOverwrite) {
/* 265 */     this.forceOverwrite = forceOverwrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 273 */     this.filesets.add(set);
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
/*     */   public void execute() throws BuildException {
/* 287 */     if (this.bundle == null) {
/* 288 */       throw new BuildException("The bundle attribute must be set.", 
/* 289 */           getLocation());
/*     */     }
/*     */     
/* 292 */     if (this.startToken == null) {
/* 293 */       throw new BuildException("The starttoken attribute must be set.", 
/* 294 */           getLocation());
/*     */     }
/*     */     
/* 297 */     if (this.endToken == null) {
/* 298 */       throw new BuildException("The endtoken attribute must be set.", 
/* 299 */           getLocation());
/*     */     }
/*     */     
/* 302 */     if (this.bundleLanguage == null) {
/* 303 */       Locale l = Locale.getDefault();
/* 304 */       this.bundleLanguage = l.getLanguage();
/*     */     } 
/*     */     
/* 307 */     if (this.bundleCountry == null) {
/* 308 */       this.bundleCountry = Locale.getDefault().getCountry();
/*     */     }
/*     */     
/* 311 */     if (this.bundleVariant == null) {
/* 312 */       Locale l = new Locale(this.bundleLanguage, this.bundleCountry);
/* 313 */       this.bundleVariant = l.getVariant();
/*     */     } 
/*     */     
/* 316 */     if (this.toDir == null) {
/* 317 */       throw new BuildException("The todir attribute must be set.", 
/* 318 */           getLocation());
/*     */     }
/*     */     
/* 321 */     if (!this.toDir.exists()) {
/* 322 */       this.toDir.mkdirs();
/* 323 */     } else if (this.toDir.isFile()) {
/* 324 */       throw new BuildException("%s is not a directory", new Object[] { this.toDir });
/*     */     } 
/*     */     
/* 327 */     if (this.srcEncoding == null) {
/* 328 */       this.srcEncoding = System.getProperty("file.encoding");
/*     */     }
/*     */     
/* 331 */     if (this.destEncoding == null) {
/* 332 */       this.destEncoding = this.srcEncoding;
/*     */     }
/*     */     
/* 335 */     if (this.bundleEncoding == null) {
/* 336 */       this.bundleEncoding = this.srcEncoding;
/*     */     }
/*     */     
/* 339 */     loadResourceMaps();
/*     */     
/* 341 */     translate();
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
/*     */ 
/*     */   
/*     */   private void loadResourceMaps() throws BuildException {
/* 364 */     Locale locale = new Locale(this.bundleLanguage, this.bundleCountry, this.bundleVariant);
/*     */ 
/*     */ 
/*     */     
/* 368 */     String language = locale.getLanguage().isEmpty() ? "" : ("_" + locale.getLanguage());
/* 369 */     String country = locale.getCountry().isEmpty() ? "" : ("_" + locale.getCountry());
/* 370 */     String variant = locale.getVariant().isEmpty() ? "" : ("_" + locale.getVariant());
/*     */     
/* 372 */     processBundle(this.bundle + language + country + variant, 0, false);
/* 373 */     processBundle(this.bundle + language + country, 1, false);
/* 374 */     processBundle(this.bundle + language, 2, false);
/* 375 */     processBundle(this.bundle, 3, false);
/*     */ 
/*     */ 
/*     */     
/* 379 */     locale = Locale.getDefault();
/*     */     
/* 381 */     language = locale.getLanguage().isEmpty() ? "" : ("_" + locale.getLanguage());
/* 382 */     country = locale.getCountry().isEmpty() ? "" : ("_" + locale.getCountry());
/* 383 */     variant = locale.getVariant().isEmpty() ? "" : ("_" + locale.getVariant());
/* 384 */     this.bundleEncoding = System.getProperty("file.encoding");
/*     */     
/* 386 */     processBundle(this.bundle + language + country + variant, 4, false);
/* 387 */     processBundle(this.bundle + language + country, 5, false);
/* 388 */     processBundle(this.bundle + language, 6, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processBundle(String bundleFile, int i, boolean checkLoaded) throws BuildException {
/* 396 */     File propsFile = getProject().resolveFile(bundleFile + ".properties");
/* 397 */     InputStream ins = null;
/*     */     try {
/* 399 */       ins = Files.newInputStream(propsFile.toPath(), new java.nio.file.OpenOption[0]);
/* 400 */       this.loaded = true;
/* 401 */       this.bundleLastModified[i] = propsFile.lastModified();
/* 402 */       log("Using " + propsFile, 4);
/* 403 */       loadResourceMap(ins);
/* 404 */     } catch (IOException ioe) {
/* 405 */       log(propsFile + " not found.", 4);
/*     */ 
/*     */ 
/*     */       
/* 409 */       if (!this.loaded && checkLoaded) {
/* 410 */         throw new BuildException(ioe.getMessage(), getLocation());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadResourceMap(InputStream ins) throws BuildException {
/*     */     
/* 420 */     try { BufferedReader in = new BufferedReader(new InputStreamReader(ins, this.bundleEncoding));
/*     */       
/*     */       try { String line;
/* 423 */         while ((line = in.readLine()) != null) {
/*     */           
/* 425 */           if (line.trim().length() > 1 && '#' != line.charAt(0) && '!' != line.charAt(0)) {
/*     */             
/* 427 */             int sepIndex = line.indexOf('=');
/* 428 */             if (-1 == sepIndex) {
/* 429 */               sepIndex = line.indexOf(':');
/*     */             }
/* 431 */             if (-1 == sepIndex) {
/* 432 */               for (int k = 0; k < line.length(); k++) {
/* 433 */                 if (Character.isSpaceChar(line.charAt(k))) {
/* 434 */                   sepIndex = k;
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             }
/* 440 */             if (-1 != sepIndex) {
/* 441 */               String key = line.substring(0, sepIndex).trim();
/* 442 */               String value = line.substring(sepIndex + 1).trim();
/*     */               
/* 444 */               while (value.endsWith("\\")) {
/* 445 */                 value = value.substring(0, value.length() - 1);
/* 446 */                 line = in.readLine();
/* 447 */                 if (line != null) {
/* 448 */                   value = value + line.trim();
/*     */                 }
/*     */               } 
/*     */ 
/*     */               
/* 453 */               if (!key.isEmpty())
/*     */               {
/* 455 */                 this.resourceMap.putIfAbsent(key, value);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 460 */         in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 461 */     { throw new BuildException(ioe.getMessage(), getLocation()); }
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void translate() throws BuildException {
/* 478 */     int filesProcessed = 0;
/* 479 */     for (FileSet fs : this.filesets) {
/* 480 */       DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 481 */       for (String srcFile : ds.getIncludedFiles()) {
/*     */         try {
/* 483 */           File dest = FILE_UTILS.resolveFile(this.toDir, srcFile);
/*     */           
/*     */           try {
/* 486 */             File destDir = new File(dest.getParent());
/* 487 */             if (!destDir.exists()) {
/* 488 */               destDir.mkdirs();
/*     */             }
/* 490 */           } catch (Exception e) {
/* 491 */             log("Exception occurred while trying to check/create  parent directory.  " + e
/* 492 */                 .getMessage(), 4);
/*     */           } 
/*     */           
/* 495 */           this.destLastModified = dest.lastModified();
/* 496 */           File src = FILE_UTILS.resolveFile(ds.getBasedir(), srcFile);
/* 497 */           this.srcLastModified = src.lastModified();
/*     */           
/* 499 */           boolean needsWork = (this.forceOverwrite || this.destLastModified < this.srcLastModified);
/*     */           
/* 501 */           if (!needsWork) {
/* 502 */             for (int icounter = 0; icounter < 7; icounter++) {
/* 503 */               needsWork = (this.destLastModified < this.bundleLastModified[icounter]);
/* 504 */               if (needsWork) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           }
/* 509 */           if (needsWork) {
/* 510 */             log("Processing " + srcFile, 4);
/* 511 */             translateOneFile(src, dest);
/* 512 */             filesProcessed++;
/*     */           } else {
/* 514 */             log("Skipping " + srcFile + " as destination file is up to date", 3);
/*     */           }
/*     */         
/* 517 */         } catch (IOException ioe) {
/* 518 */           throw new BuildException(ioe.getMessage(), getLocation());
/*     */         } 
/*     */       } 
/*     */     } 
/* 522 */     log("Translation performed on " + filesProcessed + " file(s).", 4);
/*     */   }
/*     */ 
/*     */   
/*     */   private void translateOneFile(File src, File dest) throws IOException {
/* 527 */     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(dest.toPath(), new java.nio.file.OpenOption[0]), this.destEncoding));
/*     */     try {
/* 529 */       BufferedReader in = new BufferedReader(new InputStreamReader(Files.newInputStream(src.toPath(), new java.nio.file.OpenOption[0]), this.srcEncoding)); 
/* 530 */       try { LineTokenizer lineTokenizer = new LineTokenizer();
/* 531 */         lineTokenizer.setIncludeDelims(true);
/* 532 */         String line = lineTokenizer.getToken(in);
/* 533 */         while (line != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 539 */           int startIndex = line.indexOf(this.startToken);
/* 540 */           while (startIndex >= 0 && startIndex + this.startToken
/* 541 */             .length() <= line.length()) {
/*     */ 
/*     */ 
/*     */             
/* 545 */             String replace = null;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 550 */             int endIndex = line.indexOf(this.endToken, startIndex + this.startToken
/* 551 */                 .length());
/* 552 */             if (endIndex < 0) {
/* 553 */               startIndex++;
/*     */             } else {
/*     */               
/* 556 */               String token = line.substring(startIndex + this.startToken
/* 557 */                   .length(), endIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 562 */               boolean validToken = true;
/* 563 */               for (int k = 0; k < token.length() && validToken; k++) {
/* 564 */                 char c = token.charAt(k);
/* 565 */                 if (c == ':' || c == '=' || 
/* 566 */                   Character.isSpaceChar(c)) {
/* 567 */                   validToken = false;
/*     */                 }
/*     */               } 
/* 570 */               if (!validToken) {
/* 571 */                 startIndex++;
/*     */               } else {
/*     */                 
/* 574 */                 if (this.resourceMap.containsKey(token)) {
/* 575 */                   replace = this.resourceMap.get(token);
/*     */                 } else {
/* 577 */                   log("Replacement string missing for: " + token, 3);
/*     */                   
/* 579 */                   replace = this.startToken + token + this.endToken;
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 585 */                 line = line.substring(0, startIndex) + replace + line.substring(endIndex + this.endToken.length());
/*     */ 
/*     */                 
/* 588 */                 startIndex += replace.length();
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/* 593 */             startIndex = line.indexOf(this.startToken, startIndex);
/*     */           } 
/* 595 */           out.write(line);
/* 596 */           line = lineTokenizer.getToken(in);
/*     */         } 
/* 598 */         in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  out.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         out.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/i18n/Translate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
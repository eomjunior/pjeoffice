/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.Restrict;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.types.resources.selectors.Type;
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
/*     */ public class Checksum
/*     */   extends MatchingTask
/*     */   implements Condition
/*     */ {
/*     */   private static final int NIBBLE = 4;
/*     */   private static final int WORD = 16;
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int BYTE_MASK = 255;
/*     */   
/*     */   private static class FileUnion
/*     */     extends Restrict
/*     */   {
/*     */     private Union u;
/*     */     
/*     */     FileUnion() {
/*  69 */       this.u = new Union();
/*  70 */       super.add((ResourceCollection)this.u);
/*  71 */       add((ResourceSelector)Type.FILE);
/*     */     }
/*     */     
/*     */     public void add(ResourceCollection rc) {
/*  75 */       this.u.add(rc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private File file = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File todir;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private String algorithm = "MD5";
/*     */ 
/*     */ 
/*     */   
/*  98 */   private String provider = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String fileext;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String property;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private Map<File, byte[]> allDigests = (Map)new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private Map<File, String> relativeFilePaths = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String totalproperty;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean forceOverwrite;
/*     */ 
/*     */ 
/*     */   
/*     */   private String verifyProperty;
/*     */ 
/*     */ 
/*     */   
/* 137 */   private FileUnion resources = null;
/*     */ 
/*     */ 
/*     */   
/* 141 */   private Hashtable<File, Object> includeFileMap = new Hashtable<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageDigest messageDigest;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCondition;
/*     */ 
/*     */ 
/*     */   
/* 153 */   private int readBufferSize = 8192;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   private MessageFormat format = FormatElement.getDefault().getFormat();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 165 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTodir(File todir) {
/* 175 */     this.todir = todir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlgorithm(String algorithm) {
/* 184 */     this.algorithm = algorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(String provider) {
/* 193 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileext(String fileext) {
/* 202 */     this.fileext = fileext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/* 210 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTotalproperty(String totalproperty) {
/* 221 */     this.totalproperty = totalproperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerifyproperty(String verifyProperty) {
/* 230 */     this.verifyProperty = verifyProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForceOverwrite(boolean forceOverwrite) {
/* 240 */     this.forceOverwrite = forceOverwrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadBufferSize(int size) {
/* 248 */     this.readBufferSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormat(FormatElement e) {
/* 258 */     this.format = e.getFormat();
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
/*     */   public void setPattern(String pattern) {
/* 270 */     this.format = new MessageFormat(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 278 */     add((ResourceCollection)set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 286 */     if (rc == null) {
/*     */       return;
/*     */     }
/* 289 */     this.resources = (this.resources == null) ? new FileUnion() : this.resources;
/* 290 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 299 */     this.isCondition = false;
/* 300 */     boolean value = validateAndExecute();
/* 301 */     if (this.verifyProperty != null) {
/* 302 */       getProject().setNewProperty(this.verifyProperty, 
/* 303 */           Boolean.toString(value));
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
/*     */   public boolean eval() throws BuildException {
/* 316 */     this.isCondition = true;
/* 317 */     return validateAndExecute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean validateAndExecute() throws BuildException {
/* 324 */     String savedFileExt = this.fileext;
/*     */     
/* 326 */     if (this.file == null && (this.resources == null || this.resources.size() == 0)) {
/* 327 */       throw new BuildException("Specify at least one source - a file or a resource collection.");
/*     */     }
/*     */     
/* 330 */     if (this.resources != null && !this.resources.isFilesystemOnly()) {
/* 331 */       throw new BuildException("Can only calculate checksums for file-based resources.");
/*     */     }
/* 333 */     if (this.file != null && this.file.exists() && this.file.isDirectory()) {
/* 334 */       throw new BuildException("Checksum cannot be generated for directories");
/*     */     }
/* 336 */     if (this.file != null && this.totalproperty != null) {
/* 337 */       throw new BuildException("File and Totalproperty cannot co-exist.");
/*     */     }
/* 339 */     if (this.property != null && this.fileext != null) {
/* 340 */       throw new BuildException("Property and FileExt cannot co-exist.");
/*     */     }
/* 342 */     if (this.property != null) {
/* 343 */       if (this.forceOverwrite) {
/* 344 */         throw new BuildException("ForceOverwrite cannot be used when Property is specified");
/*     */       }
/*     */       
/* 347 */       int ct = 0;
/* 348 */       if (this.resources != null) {
/* 349 */         ct += this.resources.size();
/*     */       }
/* 351 */       if (this.file != null) {
/* 352 */         ct++;
/*     */       }
/* 354 */       if (ct > 1) {
/* 355 */         throw new BuildException("Multiple files cannot be used when Property is specified");
/*     */       }
/*     */     } 
/*     */     
/* 359 */     if (this.verifyProperty != null) {
/* 360 */       this.isCondition = true;
/*     */     }
/* 362 */     if (this.verifyProperty != null && this.forceOverwrite) {
/* 363 */       throw new BuildException("VerifyProperty and ForceOverwrite cannot co-exist.");
/*     */     }
/* 365 */     if (this.isCondition && this.forceOverwrite) {
/* 366 */       throw new BuildException("ForceOverwrite cannot be used when conditions are being used.");
/*     */     }
/*     */     
/* 369 */     this.messageDigest = null;
/* 370 */     if (this.provider != null) {
/*     */       try {
/* 372 */         this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
/* 373 */       } catch (NoSuchAlgorithmException|java.security.NoSuchProviderException noalgo) {
/* 374 */         throw new BuildException(noalgo, getLocation());
/*     */       } 
/*     */     } else {
/*     */       try {
/* 378 */         this.messageDigest = MessageDigest.getInstance(this.algorithm);
/* 379 */       } catch (NoSuchAlgorithmException noalgo) {
/* 380 */         throw new BuildException(noalgo, getLocation());
/*     */       } 
/*     */     } 
/* 383 */     if (this.messageDigest == null) {
/* 384 */       throw new BuildException("Unable to create Message Digest", getLocation());
/*     */     }
/* 386 */     if (this.fileext == null) {
/* 387 */       this.fileext = "." + this.algorithm;
/* 388 */     } else if (this.fileext.trim().isEmpty()) {
/* 389 */       throw new BuildException("File extension when specified must not be an empty string");
/*     */     } 
/*     */     try {
/* 392 */       if (this.resources != null) {
/* 393 */         for (Resource r : this.resources) {
/* 394 */           File src = ((FileProvider)r.as(FileProvider.class)).getFile();
/* 395 */           if (this.totalproperty != null || this.todir != null)
/*     */           {
/*     */ 
/*     */             
/* 399 */             this.relativeFilePaths.put(src, r.getName().replace(File.separatorChar, '/'));
/*     */           }
/* 401 */           addToIncludeFileMap(src);
/*     */         } 
/*     */       }
/* 404 */       if (this.file != null) {
/* 405 */         if (this.totalproperty != null || this.todir != null) {
/* 406 */           this.relativeFilePaths.put(this.file, this.file
/* 407 */               .getName().replace(File.separatorChar, '/'));
/*     */         }
/* 409 */         addToIncludeFileMap(this.file);
/*     */       } 
/* 411 */       return generateChecksums();
/*     */     } finally {
/* 413 */       this.fileext = savedFileExt;
/* 414 */       this.includeFileMap.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToIncludeFileMap(File file) throws BuildException {
/* 423 */     if (file.exists()) {
/* 424 */       if (this.property == null) {
/* 425 */         File checksumFile = getChecksumFile(file);
/* 426 */         if (this.forceOverwrite || this.isCondition || file
/* 427 */           .lastModified() > checksumFile.lastModified()) {
/* 428 */           this.includeFileMap.put(file, checksumFile);
/*     */         } else {
/* 430 */           log(file + " omitted as " + checksumFile + " is up to date.", 3);
/*     */           
/* 432 */           if (this.totalproperty != null) {
/*     */             
/* 434 */             String checksum = readChecksum(checksumFile);
/* 435 */             byte[] digest = decodeHex(checksum.toCharArray());
/* 436 */             this.allDigests.put(file, digest);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 440 */         this.includeFileMap.put(file, this.property);
/*     */       } 
/*     */     } else {
/*     */       
/* 444 */       String message = "Could not find file " + file.getAbsolutePath() + " to generate checksum for.";
/*     */       
/* 446 */       log(message);
/* 447 */       throw new BuildException(message, getLocation());
/*     */     } 
/*     */   }
/*     */   
/*     */   private File getChecksumFile(File file) {
/*     */     File directory;
/* 453 */     if (this.todir != null) {
/*     */       
/* 455 */       String path = getRelativeFilePath(file);
/* 456 */       directory = (new File(this.todir, path)).getParentFile();
/*     */       
/* 458 */       directory.mkdirs();
/*     */     }
/*     */     else {
/*     */       
/* 462 */       directory = file.getParentFile();
/*     */     } 
/* 464 */     return new File(directory, file.getName() + this.fileext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean generateChecksums() throws BuildException {
/* 471 */     boolean checksumMatches = true;
/* 472 */     InputStream fis = null;
/* 473 */     OutputStream fos = null;
/* 474 */     byte[] buf = new byte[this.readBufferSize];
/*     */     try {
/* 476 */       for (Map.Entry<File, Object> e : this.includeFileMap.entrySet()) {
/* 477 */         this.messageDigest.reset();
/* 478 */         File src = e.getKey();
/* 479 */         if (!this.isCondition) {
/* 480 */           log("Calculating " + this.algorithm + " checksum for " + src, 3);
/*     */         }
/* 482 */         fis = Files.newInputStream(src.toPath(), new java.nio.file.OpenOption[0]);
/* 483 */         DigestInputStream dis = new DigestInputStream(fis, this.messageDigest);
/*     */         
/* 485 */         while (dis.read(buf, 0, this.readBufferSize) != -1);
/*     */ 
/*     */         
/* 488 */         dis.close();
/* 489 */         fis.close();
/* 490 */         fis = null;
/* 491 */         byte[] fileDigest = this.messageDigest.digest();
/* 492 */         if (this.totalproperty != null) {
/* 493 */           this.allDigests.put(src, fileDigest);
/*     */         }
/* 495 */         String checksum = createDigestString(fileDigest);
/*     */         
/* 497 */         Object destination = e.getValue();
/* 498 */         if (destination instanceof String) {
/* 499 */           String prop = (String)destination;
/* 500 */           if (this.isCondition) {
/*     */             
/* 502 */             checksumMatches = (checksumMatches && checksum.equals(this.property)); continue;
/*     */           } 
/* 504 */           getProject().setNewProperty(prop, checksum); continue;
/*     */         } 
/* 506 */         if (destination instanceof File) {
/* 507 */           if (this.isCondition) {
/* 508 */             File existingFile = (File)destination;
/* 509 */             if (existingFile.exists()) {
/*     */               
/*     */               try {
/* 512 */                 String suppliedChecksum = readChecksum(existingFile);
/*     */                 
/* 514 */                 checksumMatches = (checksumMatches && checksum.equals(suppliedChecksum));
/* 515 */               } catch (BuildException be) {
/*     */                 
/* 517 */                 checksumMatches = false;
/*     */               }  continue;
/*     */             } 
/* 520 */             checksumMatches = false;
/*     */             continue;
/*     */           } 
/* 523 */           File dest = (File)destination;
/* 524 */           fos = Files.newOutputStream(dest.toPath(), new java.nio.file.OpenOption[0]);
/* 525 */           fos.write(this.format.format(new Object[] { checksum, src
/*     */                   
/* 527 */                   .getName(), 
/*     */                   
/* 529 */                   FileUtils.getRelativePath(dest
/* 530 */                     .getParentFile(), src), 
/*     */ 
/*     */                   
/* 533 */                   FileUtils.getRelativePath(getProject()
/* 534 */                     .getBaseDir(), src), src
/*     */                   
/* 536 */                   .getAbsolutePath()
/* 537 */                 }).getBytes());
/* 538 */           fos.write(System.lineSeparator().getBytes());
/* 539 */           fos.close();
/* 540 */           fos = null;
/*     */         } 
/*     */       } 
/*     */       
/* 544 */       if (this.totalproperty != null) {
/*     */ 
/*     */         
/* 547 */         File[] keyArray = (File[])this.allDigests.keySet().toArray((Object[])new File[0]);
/*     */ 
/*     */         
/* 550 */         Arrays.sort(keyArray, Comparator.nullsFirst(
/* 551 */               Comparator.comparing(this::getRelativeFilePath)));
/*     */ 
/*     */         
/* 554 */         this.messageDigest.reset();
/* 555 */         for (File src : keyArray) {
/*     */           
/* 557 */           byte[] digest = this.allDigests.get(src);
/* 558 */           this.messageDigest.update(digest);
/*     */ 
/*     */           
/* 561 */           String fileName = getRelativeFilePath(src);
/* 562 */           this.messageDigest.update(fileName.getBytes());
/*     */         } 
/* 564 */         String totalChecksum = createDigestString(this.messageDigest.digest());
/* 565 */         getProject().setNewProperty(this.totalproperty, totalChecksum);
/*     */       } 
/* 567 */     } catch (Exception e) {
/* 568 */       throw new BuildException(e, getLocation());
/*     */     } finally {
/* 570 */       FileUtils.close(fis);
/* 571 */       FileUtils.close(fos);
/*     */     } 
/* 573 */     return checksumMatches;
/*     */   }
/*     */   
/*     */   private String createDigestString(byte[] fileDigest) {
/* 577 */     StringBuilder checksumSb = new StringBuilder();
/* 578 */     for (byte digestByte : fileDigest) {
/* 579 */       checksumSb.append(String.format("%02x", new Object[] { Integer.valueOf(0xFF & digestByte) }));
/*     */     } 
/* 581 */     return checksumSb.toString();
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
/*     */   public static byte[] decodeHex(char[] data) throws BuildException {
/* 597 */     int l = data.length;
/*     */     
/* 599 */     if ((l & 0x1) != 0) {
/* 600 */       throw new BuildException("odd number of characters.");
/*     */     }
/*     */     
/* 603 */     byte[] out = new byte[l >> 1];
/*     */ 
/*     */     
/* 606 */     for (int i = 0, j = 0; j < l; i++) {
/* 607 */       int f = Character.digit(data[j++], 16) << 4;
/* 608 */       f |= Character.digit(data[j++], 16);
/* 609 */       out[i] = (byte)(f & 0xFF);
/*     */     } 
/*     */     
/* 612 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String readChecksum(File f) {
/*     */     
/* 621 */     try { BufferedReader diskChecksumReader = new BufferedReader(new FileReader(f));
/*     */       
/* 623 */       try { Object[] result = this.format.parse(diskChecksumReader.readLine());
/* 624 */         if (result == null || result.length == 0 || result[0] == null) {
/* 625 */           throw new BuildException("failed to find a checksum");
/*     */         }
/* 627 */         String str = (String)result[0];
/* 628 */         diskChecksumReader.close(); return str; } catch (Throwable throwable) { try { diskChecksumReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException|java.text.ParseException e)
/* 629 */     { throw new BuildException("Couldn't read checksum file " + f, e); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getRelativeFilePath(File f) {
/* 637 */     String path = this.relativeFilePaths.get(f);
/* 638 */     if (path == null)
/*     */     {
/* 640 */       throw new BuildException("Internal error: relativeFilePaths could not match file %s\nplease file a bug report on this", new Object[] { f });
/*     */     }
/*     */ 
/*     */     
/* 644 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FormatElement
/*     */     extends EnumeratedAttribute
/*     */   {
/* 653 */     private static HashMap<String, MessageFormat> formatMap = new HashMap<>();
/*     */     private static final String CHECKSUM = "CHECKSUM";
/*     */     private static final String MD5SUM = "MD5SUM";
/*     */     private static final String SVF = "SVF";
/*     */     
/*     */     static {
/* 659 */       formatMap.put("CHECKSUM", new MessageFormat("{0}"));
/* 660 */       formatMap.put("MD5SUM", new MessageFormat("{0} *{1}"));
/* 661 */       formatMap.put("SVF", new MessageFormat("MD5 ({1}) = {0}"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static FormatElement getDefault() {
/* 669 */       FormatElement e = new FormatElement();
/* 670 */       e.setValue("CHECKSUM");
/* 671 */       return e;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MessageFormat getFormat() {
/* 679 */       return formatMap.get(getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 688 */       return new String[] { "CHECKSUM", "MD5SUM", "SVF" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Checksum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.condition.IsSigned;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
/*     */ import org.apache.tools.ant.util.ResourceUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignJar
/*     */   extends AbstractJarSignerTask
/*     */ {
/*  52 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_TODIR_AND_SIGNEDJAR = "'destdir' and 'signedjar' cannot both be set";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_TOO_MANY_MAPPERS = "Too many mappers";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_SIGNEDJAR_AND_PATHS = "You cannot specify the signed JAR when using paths or filesets";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_BAD_MAP = "Cannot map source file to anything sensible: ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_MAPPER_WITHOUT_DEST = "The destDir attribute is required if a mapper is set";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_NO_ALIAS = "alias attribute must be set";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ERROR_NO_STOREPASS = "storepass attribute must be set";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String sigfile;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File signedjar;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean internalsf;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean sectionsonly;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean preserveLastModified;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean lazy;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File destDir;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileNameMapper mapper;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String tsaurl;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String tsaproxyhost;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String tsaproxyport;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String tsacert;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean force = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String sigAlg;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String digestAlg;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String tsaDigestAlg;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSigfile(String sigfile) {
/* 175 */     this.sigfile = sigfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignedjar(File signedjar) {
/* 184 */     this.signedjar = signedjar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInternalsf(boolean internalsf) {
/* 194 */     this.internalsf = internalsf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSectionsonly(boolean sectionsonly) {
/* 203 */     this.sectionsonly = sectionsonly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLazy(boolean lazy) {
/* 213 */     this.lazy = lazy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestDir(File destDir) {
/* 223 */     this.destDir = destDir;
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
/*     */   public void add(FileNameMapper newMapper) {
/* 235 */     if (this.mapper != null) {
/* 236 */       throw new BuildException("Too many mappers");
/*     */     }
/* 238 */     this.mapper = newMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileNameMapper getMapper() {
/* 247 */     return this.mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTsaurl() {
/* 256 */     return this.tsaurl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTsaurl(String tsaurl) {
/* 265 */     this.tsaurl = tsaurl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTsaproxyhost() {
/* 274 */     return this.tsaproxyhost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTsaproxyhost(String tsaproxyhost) {
/* 283 */     this.tsaproxyhost = tsaproxyhost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTsaproxyport() {
/* 292 */     return this.tsaproxyport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTsaproxyport(String tsaproxyport) {
/* 301 */     this.tsaproxyport = tsaproxyport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTsacert() {
/* 310 */     return this.tsacert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTsacert(String tsacert) {
/* 318 */     this.tsacert = tsacert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForce(boolean b) {
/* 327 */     this.force = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForce() {
/* 337 */     return this.force;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSigAlg(String sigAlg) {
/* 346 */     this.sigAlg = sigAlg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSigAlg() {
/* 355 */     return this.sigAlg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDigestAlg(String digestAlg) {
/* 364 */     this.digestAlg = digestAlg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDigestAlg() {
/* 373 */     return this.digestAlg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTSADigestAlg(String digestAlg) {
/* 383 */     this.tsaDigestAlg = digestAlg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTSADigestAlg() {
/* 393 */     return this.tsaDigestAlg;
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
/* 404 */     boolean hasJar = (this.jar != null);
/* 405 */     boolean hasSignedJar = (this.signedjar != null);
/* 406 */     boolean hasDestDir = (this.destDir != null);
/* 407 */     boolean hasMapper = (this.mapper != null);
/*     */     
/* 409 */     if (!hasJar && !hasResources()) {
/* 410 */       throw new BuildException("jar must be set through jar attribute or nested filesets");
/*     */     }
/* 412 */     if (null == this.alias) {
/* 413 */       throw new BuildException("alias attribute must be set");
/*     */     }
/*     */     
/* 416 */     if (null == this.storepass) {
/* 417 */       throw new BuildException("storepass attribute must be set");
/*     */     }
/*     */     
/* 420 */     if (hasDestDir && hasSignedJar) {
/* 421 */       throw new BuildException("'destdir' and 'signedjar' cannot both be set");
/*     */     }
/*     */ 
/*     */     
/* 425 */     if (hasResources() && hasSignedJar) {
/* 426 */       throw new BuildException("You cannot specify the signed JAR when using paths or filesets");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 431 */     if (!hasDestDir && hasMapper) {
/* 432 */       throw new BuildException("The destDir attribute is required if a mapper is set");
/*     */     }
/*     */     
/* 435 */     beginExecution();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 440 */       if (hasJar && hasSignedJar) {
/*     */         
/* 442 */         signOneJar(this.jar, this.signedjar);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 450 */       Path sources = createUnifiedSourcePath();
/*     */       
/* 452 */       FileNameMapper destMapper = hasMapper ? this.mapper : (FileNameMapper)new IdentityMapper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 458 */       for (Resource r : sources) {
/*     */         
/* 460 */         FileResource fr = ResourceUtils.asFileResource((FileProvider)r.as(FileProvider.class));
/*     */ 
/*     */ 
/*     */         
/* 464 */         File toDir = hasDestDir ? this.destDir : fr.getBaseDir();
/*     */ 
/*     */         
/* 467 */         String[] destFilenames = destMapper.mapFileName(fr.getName());
/* 468 */         if (destFilenames == null || destFilenames.length != 1)
/*     */         {
/* 470 */           throw new BuildException("Cannot map source file to anything sensible: " + fr.getFile());
/*     */         }
/* 472 */         File destFile = new File(toDir, destFilenames[0]);
/* 473 */         signOneJar(fr.getFile(), destFile);
/*     */       } 
/*     */     } finally {
/* 476 */       endExecution();
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
/*     */   private void signOneJar(File jarSource, File jarTarget) throws BuildException {
/* 494 */     File targetFile = jarTarget;
/* 495 */     if (targetFile == null) {
/* 496 */       targetFile = jarSource;
/*     */     }
/* 498 */     if (isUpToDate(jarSource, targetFile)) {
/*     */       return;
/*     */     }
/*     */     
/* 502 */     long lastModified = jarSource.lastModified();
/* 503 */     ExecTask cmd = createJarSigner();
/*     */     
/* 505 */     setCommonOptions(cmd);
/*     */     
/* 507 */     bindToKeystore(cmd);
/* 508 */     if (null != this.sigfile) {
/* 509 */       addValue(cmd, "-sigfile");
/* 510 */       String value = this.sigfile;
/* 511 */       addValue(cmd, value);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 517 */       if (!FILE_UTILS.areSame(jarSource, targetFile)) {
/* 518 */         addValue(cmd, "-signedjar");
/* 519 */         addValue(cmd, targetFile.getPath());
/*     */       } 
/* 521 */     } catch (IOException ioex) {
/* 522 */       throw new BuildException(ioex);
/*     */     } 
/*     */     
/* 525 */     if (this.internalsf) {
/* 526 */       addValue(cmd, "-internalsf");
/*     */     }
/*     */     
/* 529 */     if (this.sectionsonly) {
/* 530 */       addValue(cmd, "-sectionsonly");
/*     */     }
/*     */     
/* 533 */     if (this.sigAlg != null) {
/* 534 */       addValue(cmd, "-sigalg");
/* 535 */       addValue(cmd, this.sigAlg);
/*     */     } 
/*     */     
/* 538 */     if (this.digestAlg != null) {
/* 539 */       addValue(cmd, "-digestalg");
/* 540 */       addValue(cmd, this.digestAlg);
/*     */     } 
/*     */ 
/*     */     
/* 544 */     addTimestampAuthorityCommands(cmd);
/*     */ 
/*     */     
/* 547 */     addValue(cmd, jarSource.getPath());
/*     */ 
/*     */     
/* 550 */     addValue(cmd, this.alias);
/*     */     
/* 552 */     log("Signing JAR: " + jarSource
/* 553 */         .getAbsolutePath() + " to " + targetFile
/*     */         
/* 555 */         .getAbsolutePath() + " as " + this.alias);
/*     */ 
/*     */     
/* 558 */     cmd.execute();
/*     */ 
/*     */     
/* 561 */     if (this.preserveLastModified) {
/* 562 */       FILE_UTILS.setFileLastModified(targetFile, lastModified);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addTimestampAuthorityCommands(ExecTask cmd) {
/* 573 */     if (this.tsaurl != null) {
/* 574 */       addValue(cmd, "-tsa");
/* 575 */       addValue(cmd, this.tsaurl);
/*     */     } 
/*     */     
/* 578 */     if (this.tsacert != null) {
/* 579 */       addValue(cmd, "-tsacert");
/* 580 */       addValue(cmd, this.tsacert);
/*     */     } 
/*     */     
/* 583 */     if (this.tsaproxyhost != null) {
/* 584 */       if (this.tsaurl == null || this.tsaurl.startsWith("https")) {
/* 585 */         addProxyFor(cmd, "https");
/*     */       }
/* 587 */       if (this.tsaurl == null || !this.tsaurl.startsWith("https")) {
/* 588 */         addProxyFor(cmd, "http");
/*     */       }
/*     */     } 
/*     */     
/* 592 */     if (this.tsaDigestAlg != null) {
/* 593 */       addValue(cmd, "-tsadigestalg");
/* 594 */       addValue(cmd, this.tsaDigestAlg);
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
/*     */   protected boolean isUpToDate(File jarFile, File signedjarFile) {
/* 612 */     if (isForce() || null == jarFile || !jarFile.exists())
/*     */     {
/*     */       
/* 615 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 619 */     File destFile = signedjarFile;
/* 620 */     if (destFile == null)
/*     */     {
/* 622 */       destFile = jarFile;
/*     */     }
/*     */ 
/*     */     
/* 626 */     if (jarFile.equals(destFile)) {
/* 627 */       if (this.lazy)
/*     */       {
/* 629 */         return isSigned(jarFile);
/*     */       }
/*     */       
/* 632 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 636 */     return FILE_UTILS.isUpToDate(jarFile, destFile);
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
/*     */   protected boolean isSigned(File file) {
/*     */     try {
/* 649 */       return IsSigned.isSigned(file, (this.sigfile == null) ? this.alias : this.sigfile);
/* 650 */     } catch (IOException e) {
/*     */       
/* 652 */       log(e.toString(), 3);
/* 653 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveLastModified(boolean preserveLastModified) {
/* 664 */     this.preserveLastModified = preserveLastModified;
/*     */   }
/*     */   
/*     */   private void addProxyFor(ExecTask cmd, String scheme) {
/* 668 */     addValue(cmd, "-J-D" + scheme + ".proxyHost=" + this.tsaproxyhost);
/*     */     
/* 670 */     if (this.tsaproxyport != null)
/* 671 */       addValue(cmd, "-J-D" + scheme + ".proxyPort=" + this.tsaproxyport); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/SignJar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
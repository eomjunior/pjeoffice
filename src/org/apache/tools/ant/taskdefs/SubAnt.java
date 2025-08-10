/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.DirSet;
/*     */ import org.apache.tools.ant.types.FileList;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.PropertySet;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubAnt
/*     */   extends Task
/*     */ {
/*     */   private Path buildpath;
/*  65 */   private Ant ant = null;
/*  66 */   private String subTarget = null;
/*  67 */   private String antfile = getDefaultBuildFile();
/*  68 */   private File genericantfile = null;
/*     */   private boolean verbose = false;
/*     */   private boolean inheritAll = false;
/*     */   private boolean inheritRefs = false;
/*     */   private boolean failOnError = true;
/*  73 */   private String output = null;
/*     */   
/*  75 */   private List<Property> properties = new Vector<>();
/*  76 */   private List<Ant.Reference> references = new Vector<>();
/*  77 */   private List<PropertySet> propertySets = new Vector<>();
/*     */ 
/*     */   
/*  80 */   private List<Ant.TargetElement> targets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultBuildFile() {
/*  93 */     return "build.xml";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleOutput(String output) {
/* 104 */     if (this.ant != null) {
/* 105 */       this.ant.handleOutput(output);
/*     */     } else {
/* 107 */       super.handleOutput(output);
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
/*     */   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
/* 129 */     if (this.ant != null) {
/* 130 */       return this.ant.handleInput(buffer, offset, length);
/*     */     }
/* 132 */     return super.handleInput(buffer, offset, length);
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
/*     */   public void handleFlush(String output) {
/* 145 */     if (this.ant != null) {
/* 146 */       this.ant.handleFlush(output);
/*     */     } else {
/* 148 */       super.handleFlush(output);
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
/*     */   public void handleErrorOutput(String output) {
/* 161 */     if (this.ant != null) {
/* 162 */       this.ant.handleErrorOutput(output);
/*     */     } else {
/* 164 */       super.handleErrorOutput(output);
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
/*     */   public void handleErrorFlush(String output) {
/* 177 */     if (this.ant != null) {
/* 178 */       this.ant.handleErrorFlush(output);
/*     */     } else {
/* 180 */       super.handleErrorFlush(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 189 */     if (this.buildpath == null) {
/* 190 */       throw new BuildException("No buildpath specified");
/*     */     }
/*     */     
/* 193 */     String[] filenames = this.buildpath.list();
/* 194 */     int count = filenames.length;
/* 195 */     if (count < 1) {
/* 196 */       log("No sub-builds to iterate on", 1);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 205 */     BuildException buildException = null;
/* 206 */     for (String filename : filenames) {
/* 207 */       File file = null;
/* 208 */       String subdirPath = null;
/* 209 */       Throwable thrownException = null;
/*     */       try {
/* 211 */         File directory = null;
/* 212 */         file = new File(filename);
/* 213 */         if (file.isDirectory()) {
/* 214 */           if (this.verbose) {
/* 215 */             subdirPath = file.getPath();
/* 216 */             log("Entering directory: " + subdirPath + "\n", 2);
/*     */           } 
/* 218 */           if (this.genericantfile != null) {
/* 219 */             directory = file;
/* 220 */             file = this.genericantfile;
/*     */           } else {
/* 222 */             file = new File(file, this.antfile);
/*     */           } 
/*     */         } 
/* 225 */         execute(file, directory);
/* 226 */         if (this.verbose && subdirPath != null) {
/* 227 */           log("Leaving directory: " + subdirPath + "\n", 2);
/*     */         }
/* 229 */       } catch (RuntimeException ex) {
/* 230 */         if (!getProject().isKeepGoingMode()) {
/* 231 */           if (this.verbose && subdirPath != null) {
/* 232 */             log("Leaving directory: " + subdirPath + "\n", 2);
/*     */           }
/* 234 */           throw ex;
/*     */         } 
/* 236 */         thrownException = ex;
/* 237 */       } catch (Throwable ex) {
/* 238 */         if (!getProject().isKeepGoingMode()) {
/* 239 */           if (this.verbose && subdirPath != null) {
/* 240 */             log("Leaving directory: " + subdirPath + "\n", 2);
/*     */           }
/* 242 */           throw new BuildException(ex);
/*     */         } 
/* 244 */         thrownException = ex;
/*     */       } 
/* 246 */       if (thrownException != null) {
/* 247 */         if (thrownException instanceof BuildException) {
/* 248 */           log("File '" + file + "' failed with message '" + thrownException
/*     */               
/* 250 */               .getMessage() + "'.", 0);
/*     */           
/* 252 */           if (buildException == null) {
/* 253 */             buildException = (BuildException)thrownException;
/*     */           }
/*     */         } else {
/* 256 */           log("Target '" + file + "' failed with message '" + thrownException
/*     */               
/* 258 */               .getMessage() + "'.", 0);
/* 259 */           log(StringUtils.getStackTrace(thrownException), 0);
/* 260 */           if (buildException == null) {
/* 261 */             buildException = new BuildException(thrownException);
/*     */           }
/*     */         } 
/*     */         
/* 265 */         if (this.verbose && subdirPath != null) {
/* 266 */           log("Leaving directory: " + subdirPath + "\n", 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     if (buildException != null) {
/* 272 */       throw buildException;
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
/*     */   private void execute(File file, File directory) throws BuildException {
/* 288 */     if (!file.exists() || file.isDirectory() || !file.canRead()) {
/* 289 */       String msg = "Invalid file: " + file;
/* 290 */       if (this.failOnError) {
/* 291 */         throw new BuildException(msg);
/*     */       }
/* 293 */       log(msg, 1);
/*     */       
/*     */       return;
/*     */     } 
/* 297 */     this.ant = createAntTask(directory);
/* 298 */     String antfilename = file.getAbsolutePath();
/* 299 */     this.ant.setAntfile(antfilename);
/* 300 */     Objects.requireNonNull(this.ant); this.targets.forEach(this.ant::addConfiguredTarget);
/*     */     
/*     */     try {
/* 303 */       if (this.verbose) {
/* 304 */         log("Executing: " + antfilename, 2);
/*     */       }
/* 306 */       this.ant.execute();
/* 307 */     } catch (BuildException e) {
/* 308 */       if (this.failOnError || isHardError((Throwable)e)) {
/* 309 */         throw e;
/*     */       }
/* 311 */       log("Failure for target '" + this.subTarget + "' of: " + antfilename + "\n" + e
/*     */           
/* 313 */           .getMessage(), 1);
/* 314 */     } catch (Throwable e) {
/* 315 */       if (this.failOnError || isHardError(e)) {
/* 316 */         throw new BuildException(e);
/*     */       }
/* 318 */       log("Failure for target '" + this.subTarget + "' of: " + antfilename + "\n" + e
/*     */           
/* 320 */           .toString(), 1);
/*     */     } finally {
/*     */       
/* 323 */       this.ant = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isHardError(Throwable t) {
/* 329 */     if (t instanceof BuildException) {
/* 330 */       return isHardError(t.getCause());
/*     */     }
/* 332 */     return (t instanceof OutOfMemoryError || t instanceof ThreadDeath);
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
/*     */   public void setAntfile(String antfile) {
/* 344 */     this.antfile = antfile;
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
/*     */   public void setGenericAntfile(File afile) {
/* 358 */     this.genericantfile = afile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailonerror(boolean failOnError) {
/* 367 */     this.failOnError = failOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String target) {
/* 378 */     this.subTarget = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredTarget(Ant.TargetElement t) {
/* 387 */     if (t.getName().isEmpty()) {
/* 388 */       throw new BuildException("target name must not be empty");
/*     */     }
/* 390 */     this.targets.add(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean on) {
/* 399 */     this.verbose = on;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(String s) {
/* 409 */     this.output = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInheritall(boolean b) {
/* 419 */     this.inheritAll = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInheritrefs(boolean b) {
/* 429 */     this.inheritRefs = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(Property p) {
/* 439 */     this.properties.add(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReference(Ant.Reference r) {
/* 449 */     this.references.add(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyset(PropertySet ps) {
/* 458 */     this.propertySets.add(ps);
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
/*     */   public void addDirset(DirSet set) {
/* 472 */     add((ResourceCollection)set);
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
/*     */   public void addFileset(FileSet set) {
/* 486 */     add((ResourceCollection)set);
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
/*     */   public void addFilelist(FileList list) {
/* 499 */     add((ResourceCollection)list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 509 */     getBuildpath().add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildpath(Path s) {
/* 518 */     getBuildpath().append(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createBuildpath() {
/* 527 */     return getBuildpath().createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path.PathElement createBuildpathElement() {
/* 537 */     return getBuildpath().createPathElement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path getBuildpath() {
/* 546 */     if (this.buildpath == null) {
/* 547 */       this.buildpath = new Path(getProject());
/*     */     }
/* 549 */     return this.buildpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildpathRef(Reference r) {
/* 558 */     createBuildpath().setRefid(r);
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
/*     */   private Ant createAntTask(File directory) {
/* 570 */     Ant antTask = new Ant(this);
/* 571 */     antTask.init();
/* 572 */     if (this.subTarget != null && !this.subTarget.isEmpty()) {
/* 573 */       antTask.setTarget(this.subTarget);
/*     */     }
/*     */ 
/*     */     
/* 577 */     if (this.output != null) {
/* 578 */       antTask.setOutput(this.output);
/*     */     }
/*     */     
/* 581 */     if (directory != null) {
/* 582 */       antTask.setDir(directory);
/*     */     } else {
/* 584 */       antTask.setUseNativeBasedir(true);
/*     */     } 
/*     */     
/* 587 */     antTask.setInheritAll(this.inheritAll);
/*     */     
/* 589 */     this.properties.forEach(p -> copyProperty(antTask.createProperty(), p));
/*     */     
/* 591 */     Objects.requireNonNull(antTask); this.propertySets.forEach(antTask::addPropertyset);
/*     */     
/* 593 */     antTask.setInheritRefs(this.inheritRefs);
/*     */     
/* 595 */     Objects.requireNonNull(antTask); this.references.forEach(antTask::addReference);
/*     */     
/* 597 */     return antTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void copyProperty(Property to, Property from) {
/* 607 */     to.setName(from.getName());
/*     */     
/* 609 */     if (from.getValue() != null) {
/* 610 */       to.setValue(from.getValue());
/*     */     }
/* 612 */     if (from.getFile() != null) {
/* 613 */       to.setFile(from.getFile());
/*     */     }
/* 615 */     if (from.getResource() != null) {
/* 616 */       to.setResource(from.getResource());
/*     */     }
/* 618 */     if (from.getPrefix() != null) {
/* 619 */       to.setPrefix(from.getPrefix());
/*     */     }
/* 621 */     if (from.getRefid() != null) {
/* 622 */       to.setRefid(from.getRefid());
/*     */     }
/* 624 */     if (from.getEnvironment() != null) {
/* 625 */       to.setEnvironment(from.getEnvironment());
/*     */     }
/* 627 */     if (from.getClasspath() != null)
/* 628 */       to.setClasspath(from.getClasspath()); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/SubAnt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
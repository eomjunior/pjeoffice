/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.FileList;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.TimeComparison;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.types.resources.Restrict;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.types.resources.comparators.Date;
/*     */ import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
/*     */ import org.apache.tools.ant.types.resources.comparators.Reverse;
/*     */ import org.apache.tools.ant.types.resources.selectors.Date;
/*     */ import org.apache.tools.ant.types.resources.selectors.Exists;
/*     */ import org.apache.tools.ant.types.resources.selectors.Not;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.util.StreamUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DependSet
/*     */   extends MatchingTask
/*     */ {
/*  87 */   private static final ResourceSelector NOT_EXISTS = (ResourceSelector)new Not((ResourceSelector)new Exists());
/*  88 */   private static final ResourceComparator DATE = (ResourceComparator)new Date();
/*     */   
/*  90 */   private static final ResourceComparator REVERSE_DATE = (ResourceComparator)new Reverse(DATE);
/*     */   
/*     */   private static final class NonExistent extends Restrict {
/*     */     private NonExistent(ResourceCollection rc) {
/*  94 */       add(rc);
/*  95 */       add(DependSet.NOT_EXISTS);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HideMissingBasedir
/*     */     implements ResourceCollection {
/*     */     private FileSet fs;
/*     */     
/*     */     private HideMissingBasedir(FileSet fs) {
/* 104 */       this.fs = fs;
/*     */     }
/*     */     
/*     */     public Iterator<Resource> iterator() {
/* 108 */       return basedirExists() ? this.fs.iterator() : Resources.EMPTY_ITERATOR;
/*     */     }
/*     */     
/*     */     public int size() {
/* 112 */       return basedirExists() ? this.fs.size() : 0;
/*     */     }
/*     */     
/*     */     public boolean isFilesystemOnly() {
/* 116 */       return true;
/*     */     }
/*     */     private boolean basedirExists() {
/* 119 */       File basedir = this.fs.getDir();
/*     */       
/* 121 */       return (basedir == null || basedir.exists());
/*     */     }
/*     */   }
/*     */   
/* 125 */   private Union sources = null;
/* 126 */   private Path targets = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean verbose;
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Union createSources() {
/* 135 */     this.sources = (this.sources == null) ? new Union() : this.sources;
/* 136 */     return this.sources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSrcfileset(FileSet fs) {
/* 144 */     createSources().add((ResourceCollection)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSrcfilelist(FileList fl) {
/* 152 */     createSources().add((ResourceCollection)fl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Path createTargets() {
/* 160 */     this.targets = (this.targets == null) ? new Path(getProject()) : this.targets;
/* 161 */     return this.targets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTargetfileset(FileSet fs) {
/* 169 */     createTargets().add(new HideMissingBasedir(fs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTargetfilelist(FileList fl) {
/* 177 */     createTargets().add((ResourceCollection)fl);
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
/*     */   public void setVerbose(boolean b) {
/* 191 */     this.verbose = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 200 */     if (this.sources == null) {
/* 201 */       throw new BuildException("At least one set of source resources must be specified");
/*     */     }
/*     */     
/* 204 */     if (this.targets == null) {
/* 205 */       throw new BuildException("At least one set of target files must be specified");
/*     */     }
/*     */ 
/*     */     
/* 209 */     if (!this.sources.isEmpty() && !this.targets.isEmpty() && !uptodate((ResourceCollection)this.sources, (ResourceCollection)this.targets)) {
/* 210 */       log("Deleting all target files.", 3);
/* 211 */       if (this.verbose) {
/* 212 */         for (String t : this.targets.list()) {
/* 213 */           log("Deleting " + t);
/*     */         }
/*     */       }
/* 216 */       Delete delete = new Delete();
/* 217 */       delete.bindToOwner(this);
/* 218 */       delete.add((ResourceCollection)this.targets);
/* 219 */       delete.perform();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean uptodate(ResourceCollection src, ResourceCollection target) {
/* 224 */     Date datesel = new Date();
/*     */     
/* 226 */     datesel.setMillis(System.currentTimeMillis());
/* 227 */     datesel.setWhen(TimeComparison.AFTER);
/*     */ 
/*     */     
/* 230 */     datesel.setGranularity(0L);
/* 231 */     logFuture((ResourceCollection)this.targets, (ResourceSelector)datesel);
/*     */     
/* 233 */     NonExistent missingTargets = new NonExistent((ResourceCollection)this.targets);
/* 234 */     int neTargets = missingTargets.size();
/* 235 */     if (neTargets > 0) {
/* 236 */       log(neTargets + " nonexistent targets", 3);
/* 237 */       logMissing((ResourceCollection)missingTargets, "target");
/* 238 */       return false;
/*     */     } 
/* 240 */     Resource oldestTarget = getOldest((ResourceCollection)this.targets);
/* 241 */     logWithModificationTime(oldestTarget, "oldest target file");
/*     */     
/* 243 */     logFuture((ResourceCollection)this.sources, (ResourceSelector)datesel);
/*     */     
/* 245 */     NonExistent missingSources = new NonExistent((ResourceCollection)this.sources);
/* 246 */     int neSources = missingSources.size();
/* 247 */     if (neSources > 0) {
/* 248 */       log(neSources + " nonexistent sources", 3);
/* 249 */       logMissing((ResourceCollection)missingSources, "source");
/* 250 */       return false;
/*     */     } 
/* 252 */     Resource newestSource = getNewest((ResourceCollection)this.sources);
/* 253 */     logWithModificationTime(newestSource, "newest source");
/* 254 */     return (oldestTarget.getLastModified() >= newestSource.getLastModified());
/*     */   }
/*     */   
/*     */   private void logFuture(ResourceCollection rc, ResourceSelector rsel) {
/* 258 */     Restrict r = new Restrict();
/* 259 */     r.add(rsel);
/* 260 */     r.add(rc);
/* 261 */     for (Resource res : r) {
/* 262 */       log("Warning: " + res + " modified in the future.", 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private Resource getXest(ResourceCollection rc, ResourceComparator c) {
/* 267 */     return StreamUtils.iteratorAsStream(rc.iterator()).max((Comparator<? super Resource>)c).orElse(null);
/*     */   }
/*     */   
/*     */   private Resource getOldest(ResourceCollection rc) {
/* 271 */     return getXest(rc, REVERSE_DATE);
/*     */   }
/*     */   
/*     */   private Resource getNewest(ResourceCollection rc) {
/* 275 */     return getXest(rc, DATE);
/*     */   }
/*     */   
/*     */   private void logWithModificationTime(Resource r, String what) {
/* 279 */     log(r.toLongString() + " is " + what + ", modified at " + new Date(r
/* 280 */           .getLastModified()), 
/* 281 */         this.verbose ? 2 : 3);
/*     */   }
/*     */   
/*     */   private void logMissing(ResourceCollection missing, String what) {
/* 285 */     if (this.verbose)
/* 286 */       for (Resource r : missing)
/* 287 */         log("Expected " + what + " " + r.toLongString() + " is missing.");  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/DependSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
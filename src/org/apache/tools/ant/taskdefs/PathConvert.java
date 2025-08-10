/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathConvert
/*     */   extends Task
/*     */ {
/*  53 */   private static boolean onWindows = Os.isFamily("dos");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Resources path = null;
/*     */ 
/*     */ 
/*     */   
/*  63 */   private Reference refid = null;
/*     */ 
/*     */ 
/*     */   
/*  67 */   private String targetOS = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean targetWindows = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean setonempty = true;
/*     */ 
/*     */ 
/*     */   
/*  79 */   private String property = null;
/*     */ 
/*     */ 
/*     */   
/*  83 */   private List<MapEntry> prefixMap = new Vector<>();
/*     */ 
/*     */ 
/*     */   
/*  87 */   private String pathSep = null;
/*     */ 
/*     */ 
/*     */   
/*  91 */   private String dirSep = null;
/*     */ 
/*     */   
/*  94 */   private Mapper mapper = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean preserveDuplicates;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class MapEntry
/*     */   {
/* 108 */     private String from = null;
/* 109 */     private String to = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFrom(String from) {
/* 119 */       this.from = from;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTo(String to) {
/* 127 */       this.to = to;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String apply(String elem) {
/* 137 */       if (this.from == null || this.to == null) {
/* 138 */         throw new BuildException("Both 'from' and 'to' must be set in a map entry");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       String cmpElem = PathConvert.onWindows ? elem.toLowerCase().replace('\\', '/') : elem;
/*     */       
/* 146 */       String cmpFrom = PathConvert.onWindows ? this.from.toLowerCase().replace('\\', '/') : this.from;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       return cmpElem.startsWith(cmpFrom) ? (
/* 152 */         this.to + elem.substring(this.from.length())) : elem;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TargetOs
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 166 */       return new String[] { "windows", "unix", "netware", "os/2", "tandem" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath() {
/* 175 */     if (isReference()) {
/* 176 */       throw noChildrenAllowed();
/*     */     }
/* 178 */     Path result = new Path(getProject());
/* 179 */     add((ResourceCollection)result);
/* 180 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 189 */     if (isReference()) {
/* 190 */       throw noChildrenAllowed();
/*     */     }
/* 192 */     getPath().add(rc);
/*     */   }
/*     */   
/*     */   private synchronized Resources getPath() {
/* 196 */     if (this.path == null) {
/* 197 */       this.path = new Resources(getProject());
/* 198 */       this.path.setCache(true);
/*     */     } 
/* 200 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapEntry createMap() {
/* 208 */     MapEntry entry = new MapEntry();
/* 209 */     this.prefixMap.add(entry);
/* 210 */     return entry;
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
/*     */   @Deprecated
/*     */   public void setTargetos(String target) {
/* 224 */     TargetOs to = new TargetOs();
/* 225 */     to.setValue(target);
/* 226 */     setTargetos(to);
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
/*     */   public void setTargetos(TargetOs target) {
/* 238 */     this.targetOS = target.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     this.targetWindows = (!"unix".equals(this.targetOS) && !"tandem".equals(this.targetOS));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSetonempty(boolean setonempty) {
/* 258 */     this.setonempty = setonempty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String p) {
/* 266 */     this.property = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 274 */     if (this.path != null) {
/* 275 */       throw noChildrenAllowed();
/*     */     }
/* 277 */     this.refid = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathSep(String sep) {
/* 286 */     this.pathSep = sep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirSep(String sep) {
/* 295 */     this.dirSep = sep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveDuplicates(boolean preserveDuplicates) {
/* 304 */     this.preserveDuplicates = preserveDuplicates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPreserveDuplicates() {
/* 313 */     return this.preserveDuplicates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/* 321 */     return (this.refid != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 330 */     Resources savedPath = this.path;
/* 331 */     String savedPathSep = this.pathSep;
/* 332 */     String savedDirSep = this.dirSep;
/*     */ 
/*     */     
/*     */     try {
/* 336 */       if (isReference()) {
/* 337 */         Object o = this.refid.getReferencedObject(getProject());
/* 338 */         if (!(o instanceof ResourceCollection)) {
/* 339 */           throw new BuildException("refid '%s' does not refer to a resource collection.", new Object[] { this.refid
/*     */                 
/* 341 */                 .getRefId() });
/*     */         }
/* 343 */         getPath().add((ResourceCollection)o);
/*     */       } 
/* 345 */       validateSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 355 */       String fromDirSep = onWindows ? "\\" : "/";
/*     */       
/* 357 */       StringBuilder rslt = new StringBuilder();
/*     */       
/* 359 */       ResourceCollection resources = isPreserveDuplicates() ? (ResourceCollection)this.path : (ResourceCollection)new Union((ResourceCollection)this.path);
/* 360 */       List<String> ret = new ArrayList<>();
/* 361 */       FileNameMapper mapperImpl = (this.mapper == null) ? (FileNameMapper)new IdentityMapper() : this.mapper.getImplementation();
/* 362 */       for (Resource r : resources) {
/* 363 */         String[] mapped = mapperImpl.mapFileName(String.valueOf(r));
/* 364 */         for (int m = 0; mapped != null && m < mapped.length; m++) {
/* 365 */           ret.add(mapped[m]);
/*     */         }
/*     */       } 
/* 368 */       boolean first = true;
/* 369 */       for (String string : ret) {
/* 370 */         String elem = mapElement(string);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 375 */         if (!first) {
/* 376 */           rslt.append(this.pathSep);
/*     */         }
/* 378 */         first = false;
/*     */         
/* 380 */         StringTokenizer stDirectory = new StringTokenizer(elem, fromDirSep, true);
/*     */         
/* 382 */         while (stDirectory.hasMoreTokens()) {
/* 383 */           String token = stDirectory.nextToken();
/* 384 */           rslt.append(fromDirSep.equals(token) ? this.dirSep : token);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 389 */       if (this.setonempty || rslt.length() > 0) {
/* 390 */         String value = rslt.toString();
/* 391 */         if (this.property == null) {
/* 392 */           log(value);
/*     */         } else {
/* 394 */           log("Set property " + this.property + " = " + value, 3);
/* 395 */           getProject().setNewProperty(this.property, value);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 399 */       this.path = savedPath;
/* 400 */       this.dirSep = savedDirSep;
/* 401 */       this.pathSep = savedPathSep;
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
/*     */   private String mapElement(String elem) {
/* 417 */     for (MapEntry entry : this.prefixMap) {
/* 418 */       String newElem = entry.apply(elem);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 423 */       if (newElem != elem) {
/* 424 */         return newElem;
/*     */       }
/*     */     } 
/* 427 */     return elem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMapper(Mapper mapper) {
/* 436 */     if (this.mapper != null) {
/* 437 */       throw new BuildException("Cannot define more than one mapper");
/*     */     }
/*     */     
/* 440 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 449 */     Mapper m = new Mapper(getProject());
/* 450 */     m.add(fileNameMapper);
/* 451 */     addMapper(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateSetup() throws BuildException {
/* 461 */     if (this.path == null) {
/* 462 */       throw new BuildException("You must specify a path to convert");
/*     */     }
/*     */ 
/*     */     
/* 466 */     String dsep = File.separator;
/* 467 */     String psep = File.pathSeparator;
/*     */     
/* 469 */     if (this.targetOS != null) {
/* 470 */       psep = this.targetWindows ? ";" : ":";
/* 471 */       dsep = this.targetWindows ? "\\" : "/";
/*     */     } 
/* 473 */     if (this.pathSep != null)
/*     */     {
/* 475 */       psep = this.pathSep;
/*     */     }
/* 477 */     if (this.dirSep != null)
/*     */     {
/* 479 */       dsep = this.dirSep;
/*     */     }
/* 481 */     this.pathSep = psep;
/* 482 */     this.dirSep = dsep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BuildException noChildrenAllowed() {
/* 491 */     return new BuildException("You must not specify nested elements when using the refid attribute.");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/PathConvert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
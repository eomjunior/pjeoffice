/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.optional.javah.JavahAdapter;
/*     */ import org.apache.tools.ant.taskdefs.optional.javah.JavahAdapterFactory;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.facade.FacadeTaskHelper;
/*     */ import org.apache.tools.ant.util.facade.ImplementationSpecificArgument;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Javah
/*     */   extends Task
/*     */ {
/*  73 */   private List<ClassArgument> classes = new Vector<>(2);
/*     */   private String cls;
/*     */   private File destDir;
/*  76 */   private Path classpath = null;
/*  77 */   private File outputFile = null;
/*     */   private boolean verbose = false;
/*     */   private boolean force = false;
/*     */   private boolean old = false;
/*     */   private boolean stubs = false;
/*     */   private Path bootclasspath;
/*  83 */   private FacadeTaskHelper facade = null;
/*  84 */   private Vector<FileSet> files = new Vector<>();
/*  85 */   private JavahAdapter nestedAdapter = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Javah() {
/*  91 */     this.facade = new FacadeTaskHelper(JavahAdapterFactory.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClass(String cls) {
/*  99 */     this.cls = cls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassArgument createClass() {
/* 107 */     ClassArgument ga = new ClassArgument();
/* 108 */     this.classes.add(ga);
/* 109 */     return ga;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ClassArgument
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 124 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 132 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileSet(FileSet fs) {
/* 141 */     this.files.add(fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getClasses() {
/* 150 */     Stream<String> stream = Stream.concat(this.files
/* 151 */         .stream()
/* 152 */         .map(fs -> fs.getDirectoryScanner(getProject()).getIncludedFiles())
/* 153 */         .flatMap(Stream::of)
/* 154 */         .map(s -> s.replace('\\', '.').replace('/', '.').replaceFirst("\\.class$", "")), this.classes
/*     */         
/* 156 */         .stream().map(ClassArgument::getName));
/*     */     
/* 158 */     if (this.cls != null) {
/* 159 */       stream = Stream.concat(Stream.<String>of(this.cls.split(",")).map(String::trim), stream);
/*     */     }
/*     */ 
/*     */     
/* 163 */     return stream.<String>toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File destDir) {
/* 172 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestdir() {
/* 181 */     return this.destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path src) {
/* 189 */     if (this.classpath == null) {
/* 190 */       this.classpath = src;
/*     */     } else {
/* 192 */       this.classpath.append(src);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 201 */     if (this.classpath == null) {
/* 202 */       this.classpath = new Path(getProject());
/*     */     }
/* 204 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 213 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 222 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBootclasspath(Path src) {
/* 230 */     if (this.bootclasspath == null) {
/* 231 */       this.bootclasspath = src;
/*     */     } else {
/* 233 */       this.bootclasspath.append(src);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createBootclasspath() {
/* 242 */     if (this.bootclasspath == null) {
/* 243 */       this.bootclasspath = new Path(getProject());
/*     */     }
/* 245 */     return this.bootclasspath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBootClasspathRef(Reference r) {
/* 254 */     createBootclasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getBootclasspath() {
/* 263 */     return this.bootclasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputFile(File outputFile) {
/* 272 */     this.outputFile = outputFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getOutputfile() {
/* 281 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForce(boolean force) {
/* 289 */     this.force = force;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getForce() {
/* 298 */     return this.force;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOld(boolean old) {
/* 309 */     this.old = old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOld() {
/* 318 */     return this.old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStubs(boolean stubs) {
/* 326 */     this.stubs = stubs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getStubs() {
/* 335 */     return this.stubs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 344 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getVerbose() {
/* 353 */     return this.verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementation(String impl) {
/* 362 */     if ("default".equals(impl)) {
/* 363 */       this.facade.setImplementation(JavahAdapterFactory.getDefault());
/*     */     } else {
/* 365 */       this.facade.setImplementation(impl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImplementationSpecificArgument createArg() {
/* 376 */     ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
/* 377 */     this.facade.addImplementationArgument(arg);
/* 378 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCurrentArgs() {
/* 388 */     return this.facade.getArgs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createImplementationClasspath() {
/* 399 */     return this.facade.getImplementationClasspath(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(JavahAdapter adapter) {
/* 409 */     if (this.nestedAdapter != null) {
/* 410 */       throw new BuildException("Can't have more than one javah adapter");
/*     */     }
/* 412 */     this.nestedAdapter = adapter;
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
/* 423 */     Set<Settings> settings = EnumSet.noneOf(Settings.class);
/*     */     
/* 425 */     if (this.cls != null) {
/* 426 */       settings.add(Settings.cls);
/*     */     }
/* 428 */     if (!this.classes.isEmpty()) {
/* 429 */       settings.add(Settings.classes);
/*     */     }
/* 431 */     if (!this.files.isEmpty()) {
/* 432 */       settings.add(Settings.files);
/*     */     }
/* 434 */     if (settings.size() > 1) {
/* 435 */       throw new BuildException("Exactly one of " + Settings.values() + " attributes is required", 
/* 436 */           getLocation());
/*     */     }
/*     */     
/* 439 */     if (this.destDir != null) {
/* 440 */       if (!this.destDir.isDirectory()) {
/* 441 */         throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", 
/* 442 */             getLocation());
/*     */       }
/* 444 */       if (this.outputFile != null) {
/* 445 */         throw new BuildException("destdir and outputFile are mutually exclusive", 
/* 446 */             getLocation());
/*     */       }
/*     */     } 
/*     */     
/* 450 */     if (this.classpath == null) {
/* 451 */       this.classpath = (new Path(getProject())).concatSystemClasspath("last");
/*     */     } else {
/* 453 */       this.classpath = this.classpath.concatSystemClasspath("ignore");
/*     */     } 
/*     */ 
/*     */     
/* 457 */     JavahAdapter ad = (this.nestedAdapter != null) ? this.nestedAdapter : JavahAdapterFactory.getAdapter(this.facade.getImplementation(), (ProjectComponent)this, 
/* 458 */         createImplementationClasspath());
/* 459 */     if (!ad.compile(this)) {
/* 460 */       throw new BuildException("compilation failed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logAndAddFiles(Commandline cmd) {
/* 470 */     logAndAddFilesToCompile(cmd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logAndAddFilesToCompile(Commandline cmd) {
/* 479 */     log("Compilation " + cmd.describeArguments(), 3);
/*     */ 
/*     */     
/* 482 */     String[] c = getClasses();
/* 483 */     StringBuilder message = new StringBuilder("Class");
/* 484 */     if (c.length > 1) {
/* 485 */       message.append("es");
/*     */     }
/* 487 */     message.append(String.format(" to be compiled:%n", new Object[0]));
/* 488 */     for (String element : c) {
/* 489 */       cmd.createArgument().setValue(element);
/* 490 */       message.append(String.format("    %s%n", new Object[] { element }));
/*     */     } 
/* 492 */     log(message.toString(), 3);
/*     */   }
/*     */   
/*     */   private enum Settings {
/* 496 */     cls, files, classes;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/Javah.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
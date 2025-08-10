/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Paths;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.tools.ant.input.DefaultInputHandler;
/*      */ import org.apache.tools.ant.input.InputHandler;
/*      */ import org.apache.tools.ant.launch.AntMain;
/*      */ import org.apache.tools.ant.listener.SilentLogger;
/*      */ import org.apache.tools.ant.property.GetProperty;
/*      */ import org.apache.tools.ant.property.ResolvePropertyMap;
/*      */ import org.apache.tools.ant.util.ClasspathUtils;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.ProxySetup;
/*      */ import org.apache.tools.ant.util.StreamUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Main
/*      */   implements AntMain
/*      */ {
/*   70 */   private static final Set<String> LAUNCH_COMMANDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new String[] { "-lib", "-cp", "-noclasspath", "--noclasspath", "-nouserlib", "-main" })));
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String DEFAULT_BUILD_FILENAME = "build.xml";
/*      */ 
/*      */   
/*   77 */   private int msgOutputLevel = 2;
/*      */ 
/*      */   
/*      */   private File buildFile;
/*      */ 
/*      */   
/*   83 */   private PrintStream out = System.out;
/*      */ 
/*      */   
/*   86 */   private PrintStream err = System.err;
/*      */ 
/*      */   
/*   89 */   private final Vector<String> targets = new Vector<>();
/*      */ 
/*      */   
/*   92 */   private final Properties definedProps = new Properties();
/*      */ 
/*      */   
/*   95 */   private final Vector<String> listeners = new Vector<>(1);
/*      */ 
/*      */   
/*   98 */   private final Vector<String> propertyFiles = new Vector<>(1);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowInput = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean keepGoingMode = false;
/*      */ 
/*      */ 
/*      */   
/*  111 */   private String loggerClassname = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  117 */   private String inputHandlerClassname = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean emacsMode = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean silent = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean readyToRun = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean projectHelp = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isLogFileUsed = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  150 */   private Integer threadPriority = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean proxy = false;
/*      */ 
/*      */   
/*  157 */   private final Map<Class<?>, List<String>> extraArguments = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final GetProperty NOPROPERTIES = aName -> null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printMessage(Throwable t) {
/*  169 */     String message = t.getMessage();
/*  170 */     if (message != null) {
/*  171 */       System.err.println(message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void start(String[] args, Properties additionalUserProperties, ClassLoader coreLoader) {
/*  189 */     Main m = new Main();
/*  190 */     m.startAnt(args, additionalUserProperties, coreLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void startAnt(String[] args, Properties additionalUserProperties, ClassLoader coreLoader) {
/*      */     try {
/*  206 */       processArgs(args);
/*  207 */     } catch (Throwable exc) {
/*  208 */       handleLogfile();
/*  209 */       printMessage(exc);
/*  210 */       exit(1);
/*      */       
/*      */       return;
/*      */     } 
/*  214 */     if (additionalUserProperties != null) {
/*  215 */       additionalUserProperties.stringPropertyNames()
/*  216 */         .forEach(key -> this.definedProps.put(key, additionalUserProperties.getProperty(key)));
/*      */     }
/*      */ 
/*      */     
/*  220 */     int exitCode = 1;
/*      */     try {
/*      */       try {
/*  223 */         runBuild(coreLoader);
/*  224 */         exitCode = 0;
/*  225 */       } catch (ExitStatusException ese) {
/*  226 */         exitCode = ese.getStatus();
/*  227 */         if (exitCode != 0) {
/*  228 */           throw ese;
/*      */         }
/*      */       } 
/*  231 */     } catch (BuildException be) {
/*  232 */       if (this.err != System.err) {
/*  233 */         printMessage(be);
/*      */       }
/*  235 */     } catch (Throwable exc) {
/*  236 */       exc.printStackTrace();
/*  237 */       printMessage(exc);
/*      */     } finally {
/*  239 */       handleLogfile();
/*      */     } 
/*  241 */     exit(exitCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void exit(int exitCode) {
/*  251 */     System.exit(exitCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleLogfile() {
/*  260 */     if (this.isLogFileUsed) {
/*  261 */       FileUtils.close(this.out);
/*  262 */       FileUtils.close(this.err);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/*  274 */     start(args, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected Main(String[] args) throws BuildException {
/*  297 */     processArgs(args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processArgs(String[] args) {
/*  310 */     String searchForThis = null;
/*  311 */     boolean searchForFile = false;
/*  312 */     PrintStream logTo = null;
/*      */ 
/*      */ 
/*      */     
/*  316 */     boolean justPrintUsage = false;
/*  317 */     boolean justPrintVersion = false;
/*  318 */     boolean justPrintDiagnostics = false;
/*      */     
/*  320 */     ArgumentProcessorRegistry processorRegistry = ArgumentProcessorRegistry.getInstance();
/*      */     
/*  322 */     for (int i = 0; i < args.length; i++) {
/*  323 */       String arg = args[i];
/*      */       
/*  325 */       if (arg.equals("-help") || arg.equals("-h"))
/*  326 */       { justPrintUsage = true; }
/*  327 */       else if (arg.equals("-version"))
/*  328 */       { justPrintVersion = true; }
/*  329 */       else if (arg.equals("-diagnostics"))
/*  330 */       { justPrintDiagnostics = true; }
/*  331 */       else if (arg.equals("-quiet") || arg.equals("-q"))
/*  332 */       { this.msgOutputLevel = 1; }
/*  333 */       else if (arg.equals("-verbose") || arg.equals("-v"))
/*  334 */       { this.msgOutputLevel = 3; }
/*  335 */       else if (arg.equals("-debug") || arg.equals("-d"))
/*  336 */       { this.msgOutputLevel = 4; }
/*  337 */       else if (arg.equals("-silent") || arg.equals("-S"))
/*  338 */       { this.silent = true; }
/*  339 */       else if (arg.equals("-noinput"))
/*  340 */       { this.allowInput = false; }
/*  341 */       else if (arg.equals("-logfile") || arg.equals("-l"))
/*      */       { try {
/*  343 */           File logFile = new File(args[i + 1]);
/*  344 */           i++;
/*      */ 
/*      */ 
/*      */           
/*  348 */           logTo = new PrintStream(Files.newOutputStream(logFile.toPath(), new java.nio.file.OpenOption[0]));
/*  349 */           this.isLogFileUsed = true;
/*  350 */         } catch (IOException ioe) {
/*  351 */           String msg = "Cannot write on the specified log file. Make sure the path exists and you have write permissions.";
/*      */ 
/*      */           
/*  354 */           throw new BuildException("Cannot write on the specified log file. Make sure the path exists and you have write permissions.");
/*  355 */         } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  356 */           String msg = "You must specify a log file when using the -log argument";
/*      */           
/*  358 */           throw new BuildException("You must specify a log file when using the -log argument");
/*      */         }  }
/*  360 */       else if (arg.equals("-buildfile") || arg.equals("-file") || arg
/*  361 */         .equals("-f"))
/*  362 */       { i = handleArgBuildFile(args, i); }
/*  363 */       else if (arg.equals("-listener"))
/*  364 */       { i = handleArgListener(args, i); }
/*  365 */       else if (arg.startsWith("-D"))
/*  366 */       { i = handleArgDefine(args, i); }
/*  367 */       else if (arg.equals("-logger"))
/*  368 */       { i = handleArgLogger(args, i); }
/*  369 */       else if (arg.equals("-inputhandler"))
/*  370 */       { i = handleArgInputHandler(args, i); }
/*  371 */       else if (arg.equals("-emacs") || arg.equals("-e"))
/*  372 */       { this.emacsMode = true; }
/*  373 */       else if (arg.equals("-projecthelp") || arg.equals("-p"))
/*      */       
/*  375 */       { this.projectHelp = true; }
/*  376 */       else if (arg.equals("-find") || arg.equals("-s"))
/*  377 */       { searchForFile = true;
/*      */         
/*  379 */         if (i < args.length - 1) {
/*  380 */           searchForThis = args[++i];
/*      */         } }
/*  382 */       else if (arg.startsWith("-propertyfile"))
/*  383 */       { i = handleArgPropertyFile(args, i); }
/*  384 */       else if (arg.equals("-k") || arg.equals("-keep-going"))
/*  385 */       { this.keepGoingMode = true; }
/*  386 */       else if (arg.equals("-nice"))
/*  387 */       { i = handleArgNice(args, i); }
/*  388 */       else { if (LAUNCH_COMMANDS.contains(arg)) {
/*      */ 
/*      */ 
/*      */           
/*  392 */           String msg = "Ant's Main method is being handed an option " + arg + " that is only for the launcher class.\nThis can be caused by a version mismatch between the ant script/.bat file and Ant itself.";
/*      */ 
/*      */ 
/*      */           
/*  396 */           throw new BuildException(msg);
/*  397 */         }  if (arg.equals("-autoproxy")) {
/*  398 */           this.proxy = true;
/*  399 */         } else if (arg.startsWith("-")) {
/*  400 */           boolean processed = false;
/*  401 */           for (ArgumentProcessor processor : processorRegistry.getProcessors()) {
/*  402 */             int newI = processor.readArguments(args, i);
/*  403 */             if (newI != -1) {
/*  404 */               List<String> extraArgs = this.extraArguments.computeIfAbsent(processor.getClass(), k -> new ArrayList());
/*  405 */               extraArgs.addAll(Arrays.<String>asList(args).subList(newI, args.length));
/*  406 */               processed = true;
/*      */               break;
/*      */             } 
/*      */           } 
/*  410 */           if (!processed) {
/*      */             
/*  412 */             String msg = "Unknown argument: " + arg;
/*  413 */             System.err.println(msg);
/*  414 */             printUsage();
/*  415 */             throw new BuildException("");
/*      */           } 
/*      */         } else {
/*      */           
/*  419 */           this.targets.addElement(arg);
/*      */         }  }
/*      */     
/*      */     } 
/*  423 */     if (this.msgOutputLevel >= 3 || justPrintVersion) {
/*  424 */       printVersion(this.msgOutputLevel);
/*      */     }
/*      */     
/*  427 */     if (justPrintUsage || justPrintVersion || justPrintDiagnostics) {
/*  428 */       if (justPrintUsage) {
/*  429 */         printUsage();
/*      */       }
/*  431 */       if (justPrintDiagnostics) {
/*  432 */         Diagnostics.doReport(System.out, this.msgOutputLevel);
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  438 */     if (this.buildFile == null)
/*      */     {
/*  440 */       if (searchForFile) {
/*  441 */         if (searchForThis != null) {
/*  442 */           this.buildFile = findBuildFile(System.getProperty("user.dir"), searchForThis);
/*      */         } else {
/*      */           
/*  445 */           Iterator<ProjectHelper> it = ProjectHelperRepository.getInstance().getHelpers();
/*      */           do {
/*  447 */             ProjectHelper helper = it.next();
/*  448 */             searchForThis = helper.getDefaultBuildFile();
/*  449 */             if (this.msgOutputLevel >= 3) {
/*  450 */               System.out.println("Searching the default build file: " + searchForThis);
/*      */             }
/*  452 */             this.buildFile = findBuildFile(System.getProperty("user.dir"), searchForThis);
/*  453 */           } while (this.buildFile == null && it.hasNext());
/*      */         } 
/*  455 */         if (this.buildFile == null) {
/*  456 */           throw new BuildException("Could not locate a build file!");
/*      */         }
/*      */       } else {
/*      */         
/*  460 */         Iterator<ProjectHelper> it = ProjectHelperRepository.getInstance().getHelpers();
/*      */         do {
/*  462 */           ProjectHelper helper = it.next();
/*  463 */           this.buildFile = new File(helper.getDefaultBuildFile());
/*  464 */           if (this.msgOutputLevel < 3)
/*  465 */             continue;  System.out.println("Trying the default build file: " + this.buildFile);
/*      */         }
/*  467 */         while (!this.buildFile.exists() && it.hasNext());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  472 */     if (!this.buildFile.exists()) {
/*  473 */       System.out.println("Buildfile: " + this.buildFile + " does not exist!");
/*  474 */       throw new BuildException("Build failed");
/*      */     } 
/*      */     
/*  477 */     if (this.buildFile.isDirectory()) {
/*  478 */       File whatYouMeant = new File(this.buildFile, "build.xml");
/*  479 */       if (whatYouMeant.isFile()) {
/*  480 */         this.buildFile = whatYouMeant;
/*      */       } else {
/*  482 */         System.out.println("What? Buildfile: " + this.buildFile + " is a dir!");
/*  483 */         throw new BuildException("Build failed");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  488 */     this
/*  489 */       .buildFile = FileUtils.getFileUtils().normalize(this.buildFile.getAbsolutePath());
/*      */ 
/*      */     
/*  492 */     loadPropertyFiles();
/*      */     
/*  494 */     if (this.msgOutputLevel >= 2) {
/*  495 */       System.out.println("Buildfile: " + this.buildFile);
/*      */     }
/*      */     
/*  498 */     if (logTo != null) {
/*  499 */       this.out = logTo;
/*  500 */       this.err = logTo;
/*  501 */       System.setOut(this.out);
/*  502 */       System.setErr(this.err);
/*      */     } 
/*  504 */     this.readyToRun = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleArgBuildFile(String[] args, int pos) {
/*      */     try {
/*  514 */       this
/*  515 */         .buildFile = new File(args[++pos].replace('/', File.separatorChar));
/*  516 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  517 */       throw new BuildException("You must specify a buildfile when using the -buildfile argument");
/*      */     } 
/*      */     
/*  520 */     return pos;
/*      */   }
/*      */ 
/*      */   
/*      */   private int handleArgListener(String[] args, int pos) {
/*      */     try {
/*  526 */       this.listeners.addElement(args[pos + 1]);
/*  527 */       pos++;
/*  528 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  529 */       String msg = "You must specify a classname when using the -listener argument";
/*      */       
/*  531 */       throw new BuildException("You must specify a classname when using the -listener argument");
/*      */     } 
/*  533 */     return pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleArgDefine(String[] args, int argPos) {
/*  548 */     String value, arg = args[argPos];
/*  549 */     String name = arg.substring(2);
/*      */     
/*  551 */     int posEq = name.indexOf('=');
/*  552 */     if (posEq > 0) {
/*  553 */       value = name.substring(posEq + 1);
/*  554 */       name = name.substring(0, posEq);
/*  555 */     } else if (argPos < args.length - 1) {
/*  556 */       value = args[++argPos];
/*      */     } else {
/*  558 */       throw new BuildException("Missing value for property " + name);
/*      */     } 
/*      */     
/*  561 */     this.definedProps.put(name, value);
/*  562 */     return argPos;
/*      */   }
/*      */ 
/*      */   
/*      */   private int handleArgLogger(String[] args, int pos) {
/*  567 */     if (this.loggerClassname != null) {
/*  568 */       throw new BuildException("Only one logger class may be specified.");
/*      */     }
/*      */     
/*      */     try {
/*  572 */       this.loggerClassname = args[++pos];
/*  573 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  574 */       throw new BuildException("You must specify a classname when using the -logger argument");
/*      */     } 
/*      */     
/*  577 */     return pos;
/*      */   }
/*      */ 
/*      */   
/*      */   private int handleArgInputHandler(String[] args, int pos) {
/*  582 */     if (this.inputHandlerClassname != null) {
/*  583 */       throw new BuildException("Only one input handler class may be specified.");
/*      */     }
/*      */     
/*      */     try {
/*  587 */       this.inputHandlerClassname = args[++pos];
/*  588 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  589 */       throw new BuildException("You must specify a classname when using the -inputhandler argument");
/*      */     } 
/*      */ 
/*      */     
/*  593 */     return pos;
/*      */   }
/*      */ 
/*      */   
/*      */   private int handleArgPropertyFile(String[] args, int pos) {
/*      */     try {
/*  599 */       this.propertyFiles.addElement(args[++pos]);
/*  600 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  601 */       String msg = "You must specify a property filename when using the -propertyfile argument";
/*      */       
/*  603 */       throw new BuildException("You must specify a property filename when using the -propertyfile argument");
/*      */     } 
/*  605 */     return pos;
/*      */   }
/*      */ 
/*      */   
/*      */   private int handleArgNice(String[] args, int pos) {
/*      */     try {
/*  611 */       this.threadPriority = Integer.decode(args[++pos]);
/*  612 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/*  613 */       throw new BuildException("You must supply a niceness value (1-10) after the -nice option");
/*      */     
/*      */     }
/*  616 */     catch (NumberFormatException e) {
/*  617 */       throw new BuildException("Unrecognized niceness value: " + args[pos]);
/*      */     } 
/*      */ 
/*      */     
/*  621 */     if (this.threadPriority.intValue() < 1 || this.threadPriority
/*  622 */       .intValue() > 10) {
/*  623 */       throw new BuildException("Niceness value is out of the range 1-10");
/*      */     }
/*      */     
/*  626 */     return pos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadPropertyFiles() {
/*  635 */     for (Iterator<String> iterator = this.propertyFiles.iterator(); iterator.hasNext(); ) { String filename = iterator.next();
/*  636 */       Properties props = new Properties(); 
/*  637 */       try { InputStream fis = Files.newInputStream(Paths.get(filename, new String[0]), new java.nio.file.OpenOption[0]); 
/*  638 */         try { props.load(fis);
/*  639 */           if (fis != null) fis.close();  } catch (Throwable throwable) { if (fis != null) try { fis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  640 */       { System.out.println("Could not load property file " + filename + ": " + e
/*  641 */             .getMessage()); }
/*      */ 
/*      */ 
/*      */       
/*  645 */       props.stringPropertyNames().stream()
/*  646 */         .filter(name -> (this.definedProps.getProperty(name) == null))
/*  647 */         .forEach(name -> this.definedProps.put(name, props.getProperty(name))); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   private File getParentFile(File file) {
/*  662 */     File parent = file.getParentFile();
/*      */     
/*  664 */     if (parent != null && this.msgOutputLevel >= 3) {
/*  665 */       System.out.println("Searching in " + parent.getAbsolutePath());
/*      */     }
/*      */     
/*  668 */     return parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private File findBuildFile(String start, String suffix) {
/*  687 */     if (this.msgOutputLevel >= 2) {
/*  688 */       System.out.println("Searching for " + suffix + " ...");
/*      */     }
/*      */     
/*  691 */     File parent = new File((new File(start)).getAbsolutePath());
/*  692 */     File file = new File(parent, suffix);
/*      */ 
/*      */     
/*  695 */     while (!file.exists()) {
/*      */       
/*  697 */       parent = getParentFile(parent);
/*      */ 
/*      */ 
/*      */       
/*  701 */       if (parent == null) {
/*  702 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  706 */       file = new File(parent, suffix);
/*      */     } 
/*      */     
/*  709 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void runBuild(ClassLoader coreLoader) throws BuildException {
/*  725 */     if (!this.readyToRun) {
/*      */       return;
/*      */     }
/*      */     
/*  729 */     ArgumentProcessorRegistry processorRegistry = ArgumentProcessorRegistry.getInstance();
/*      */     
/*  731 */     for (ArgumentProcessor processor : processorRegistry.getProcessors()) {
/*  732 */       List<String> extraArgs = this.extraArguments.get(processor.getClass());
/*  733 */       if (extraArgs != null && 
/*  734 */         processor.handleArg(extraArgs)) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  740 */     Project project = new Project();
/*  741 */     project.setCoreLoader(coreLoader);
/*      */     
/*  743 */     Throwable error = null;
/*      */     
/*      */     try {
/*  746 */       addBuildListeners(project);
/*  747 */       addInputHandler(project);
/*      */       
/*  749 */       PrintStream savedErr = System.err;
/*  750 */       PrintStream savedOut = System.out;
/*  751 */       InputStream savedIn = System.in;
/*      */       try {
/*  753 */         if (this.allowInput) {
/*  754 */           project.setDefaultInputStream(System.in);
/*      */         }
/*  756 */         System.setIn(new DemuxInputStream(project));
/*  757 */         System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
/*  758 */         System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
/*      */ 
/*      */         
/*  761 */         if (!this.projectHelp) {
/*  762 */           project.fireBuildStarted();
/*      */         }
/*      */ 
/*      */         
/*  766 */         if (this.threadPriority != null) {
/*      */           try {
/*  768 */             project.log("Setting Ant's thread priority to " + this.threadPriority, 3);
/*      */             
/*  770 */             Thread.currentThread().setPriority(this.threadPriority.intValue());
/*  771 */           } catch (SecurityException swallowed) {
/*      */             
/*  773 */             project.log("A security manager refused to set the -nice value");
/*      */           } 
/*      */         }
/*      */         
/*  777 */         setProperties(project);
/*      */         
/*  779 */         project.setKeepGoingMode(this.keepGoingMode);
/*  780 */         if (this.proxy) {
/*      */           
/*  782 */           ProxySetup proxySetup = new ProxySetup(project);
/*  783 */           proxySetup.enableProxies();
/*      */         } 
/*      */         
/*  786 */         for (ArgumentProcessor processor : processorRegistry.getProcessors()) {
/*  787 */           List<String> extraArgs = this.extraArguments.get(processor.getClass());
/*  788 */           if (extraArgs != null) {
/*  789 */             processor.prepareConfigure(project, extraArgs);
/*      */           }
/*      */         } 
/*      */         
/*  793 */         ProjectHelper.configureProject(project, this.buildFile);
/*      */         
/*  795 */         for (ArgumentProcessor processor : processorRegistry.getProcessors()) {
/*  796 */           List<String> extraArgs = this.extraArguments.get(processor.getClass());
/*  797 */           if (extraArgs != null && 
/*  798 */             processor.handleArg(project, extraArgs)) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  804 */         if (this.projectHelp) {
/*  805 */           printDescription(project);
/*  806 */           printTargets(project, (this.msgOutputLevel > 2), (this.msgOutputLevel > 3));
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/*  812 */         if (this.targets.isEmpty() && 
/*  813 */           project.getDefaultTarget() != null) {
/*  814 */           this.targets.addElement(project.getDefaultTarget());
/*      */         }
/*      */ 
/*      */         
/*  818 */         project.executeTargets(this.targets);
/*      */       } finally {
/*  820 */         System.setOut(savedOut);
/*  821 */         System.setErr(savedErr);
/*  822 */         System.setIn(savedIn);
/*      */       } 
/*  824 */     } catch (RuntimeException|Error exc) {
/*  825 */       error = exc;
/*  826 */       throw exc;
/*      */     } finally {
/*  828 */       if (!this.projectHelp) {
/*      */         try {
/*  830 */           project.fireBuildFinished(error);
/*  831 */         } catch (Throwable t) {
/*      */ 
/*      */           
/*  834 */           System.err.println("Caught an exception while logging the end of the build.  Exception was:");
/*      */           
/*  836 */           t.printStackTrace();
/*  837 */           if (error != null) {
/*  838 */             System.err.println("There has been an error prior to that:");
/*      */             
/*  840 */             error.printStackTrace();
/*      */           } 
/*  842 */           throw new BuildException(t);
/*      */         } 
/*  844 */       } else if (error != null) {
/*  845 */         project.log(error.toString(), 0);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setProperties(Project project) {
/*  852 */     project.init();
/*      */ 
/*      */     
/*  855 */     PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(project);
/*      */     
/*  857 */     Map<Object, Object> raw = new HashMap<>(this.definedProps);
/*      */     
/*  859 */     Map<Object, Object> map1 = raw;
/*      */ 
/*      */     
/*  862 */     ResolvePropertyMap resolver = new ResolvePropertyMap(project, NOPROPERTIES, propertyHelper.getExpanders());
/*  863 */     resolver.resolveAllProperties(map1, null, false);
/*      */ 
/*      */     
/*  866 */     map1.forEach((arg, value) -> project.setUserProperty(arg, String.valueOf(value)));
/*      */     
/*  868 */     project.setUserProperty("ant.file", this.buildFile
/*  869 */         .getAbsolutePath());
/*  870 */     project.setUserProperty("ant.file.type", "file");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  880 */     project.setUserProperty("ant.project.invoked-targets", 
/*  881 */         String.join(",", (Iterable)this.targets));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addBuildListeners(Project project) {
/*  894 */     project.addBuildListener(createLogger());
/*      */     
/*  896 */     int count = this.listeners.size();
/*  897 */     for (int i = 0; i < count; i++) {
/*  898 */       String className = this.listeners.elementAt(i);
/*      */       
/*  900 */       BuildListener listener = (BuildListener)ClasspathUtils.newInstance(className, Main.class
/*  901 */           .getClassLoader(), BuildListener.class);
/*  902 */       project.setProjectReference(listener);
/*      */       
/*  904 */       project.addBuildListener(listener);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addInputHandler(Project project) throws BuildException {
/*  917 */     InputHandler handler = null;
/*  918 */     if (this.inputHandlerClassname == null) {
/*  919 */       DefaultInputHandler defaultInputHandler = new DefaultInputHandler();
/*      */     } else {
/*  921 */       handler = (InputHandler)ClasspathUtils.newInstance(this.inputHandlerClassname, Main.class
/*  922 */           .getClassLoader(), InputHandler.class);
/*      */       
/*  924 */       project.setProjectReference(handler);
/*      */     } 
/*  926 */     project.setInputHandler(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BuildLogger createLogger() {
/*  936 */     BuildLogger logger = null;
/*  937 */     if (this.silent) {
/*  938 */       SilentLogger silentLogger = new SilentLogger();
/*  939 */       this.msgOutputLevel = 1;
/*  940 */       this.emacsMode = true;
/*  941 */     } else if (this.loggerClassname != null) {
/*      */       try {
/*  943 */         logger = (BuildLogger)ClasspathUtils.newInstance(this.loggerClassname, Main.class
/*  944 */             .getClassLoader(), BuildLogger.class);
/*      */       }
/*  946 */       catch (BuildException e) {
/*  947 */         System.err.println("The specified logger class " + this.loggerClassname + " could not be used because " + e
/*      */             
/*  949 */             .getMessage());
/*  950 */         throw e;
/*      */       } 
/*      */     } else {
/*  953 */       logger = new DefaultLogger();
/*      */     } 
/*      */     
/*  956 */     logger.setMessageOutputLevel(this.msgOutputLevel);
/*  957 */     logger.setOutputPrintStream(this.out);
/*  958 */     logger.setErrorPrintStream(this.err);
/*  959 */     logger.setEmacsMode(this.emacsMode);
/*      */     
/*  961 */     return logger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printUsage() {
/*  968 */     System.out.println("ant [options] [target [target2 [target3] ...]]");
/*  969 */     System.out.println("Options: ");
/*  970 */     System.out.println("  -help, -h              print this message and exit");
/*  971 */     System.out.println("  -projecthelp, -p       print project help information and exit");
/*  972 */     System.out.println("  -version               print the version information and exit");
/*  973 */     System.out.println("  -diagnostics           print information that might be helpful to");
/*  974 */     System.out.println("                         diagnose or report problems and exit");
/*  975 */     System.out.println("  -quiet, -q             be extra quiet");
/*  976 */     System.out.println("  -silent, -S            print nothing but task outputs and build failures");
/*  977 */     System.out.println("  -verbose, -v           be extra verbose");
/*  978 */     System.out.println("  -debug, -d             print debugging information");
/*  979 */     System.out.println("  -emacs, -e             produce logging information without adornments");
/*  980 */     System.out.println("  -lib <path>            specifies a path to search for jars and classes");
/*  981 */     System.out.println("  -logfile <file>        use given file for log");
/*  982 */     System.out.println("    -l     <file>                ''");
/*  983 */     System.out.println("  -logger <classname>    the class which is to perform logging");
/*  984 */     System.out.println("  -listener <classname>  add an instance of class as a project listener");
/*  985 */     System.out.println("  -noinput               do not allow interactive input");
/*  986 */     System.out.println("  -buildfile <file>      use given buildfile");
/*  987 */     System.out.println("    -file    <file>              ''");
/*  988 */     System.out.println("    -f       <file>              ''");
/*  989 */     System.out.println("  -D<property>=<value>   use value for given property");
/*  990 */     System.out.println("  -keep-going, -k        execute all targets that do not depend");
/*  991 */     System.out.println("                         on failed target(s)");
/*  992 */     System.out.println("  -propertyfile <name>   load all properties from file with -D");
/*  993 */     System.out.println("                         properties taking precedence");
/*  994 */     System.out.println("  -inputhandler <class>  the class which will handle input requests");
/*  995 */     System.out.println("  -find <file>           (s)earch for buildfile towards the root of");
/*  996 */     System.out.println("    -s  <file>           the filesystem and use it");
/*  997 */     System.out.println("  -nice  number          A niceness value for the main thread:");
/*  998 */     System.out.println("                         1 (lowest) to 10 (highest); 5 is the default");
/*  999 */     System.out.println("  -nouserlib             Run ant without using the jar files from");
/* 1000 */     System.out.println("                         ${user.home}/.ant/lib");
/* 1001 */     System.out.println("  -noclasspath           Run ant without using CLASSPATH");
/* 1002 */     System.out.println("  -autoproxy             Java1.5+: use the OS proxy settings");
/* 1003 */     System.out.println("  -main <class>          override Ant's normal entry point");
/* 1004 */     for (ArgumentProcessor processor : ArgumentProcessorRegistry.getInstance().getProcessors()) {
/* 1005 */       processor.printUsage(System.out);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printVersion(int logLevel) throws BuildException {
/* 1015 */     System.out.println(getAntVersion());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1021 */   private static String antVersion = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1026 */   private static String shortAntVersion = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized String getAntVersion() throws BuildException {
/* 1039 */     if (antVersion == null) {
/*      */       try {
/* 1041 */         Properties props = new Properties();
/*      */         
/* 1043 */         InputStream in = Main.class.getResourceAsStream("/org/apache/tools/ant/version.txt");
/* 1044 */         props.load(in);
/* 1045 */         in.close();
/* 1046 */         shortAntVersion = props.getProperty("VERSION");
/*      */ 
/*      */ 
/*      */         
/* 1050 */         antVersion = "Apache Ant(TM) version " + shortAntVersion + " compiled on " + props.getProperty("DATE");
/* 1051 */       } catch (IOException ioe) {
/* 1052 */         throw new BuildException("Could not load the version information:" + ioe
/* 1053 */             .getMessage());
/* 1054 */       } catch (NullPointerException npe) {
/* 1055 */         throw new BuildException("Could not load the version information.");
/*      */       } 
/*      */     }
/* 1058 */     return antVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortAntVersion() throws BuildException {
/* 1073 */     if (shortAntVersion == null) {
/* 1074 */       getAntVersion();
/*      */     }
/* 1076 */     return shortAntVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printDescription(Project project) {
/* 1087 */     if (project.getDescription() != null) {
/* 1088 */       project.log(project.getDescription());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, Target> removeDuplicateTargets(Map<String, Target> targets) {
/* 1101 */     Map<Location, Target> locationMap = new HashMap<>();
/* 1102 */     targets.forEach((name, target) -> {
/*      */           Target otherTarget = (Target)locationMap.get(target.getLocation());
/*      */ 
/*      */           
/*      */           if (otherTarget == null || otherTarget.getName().length() > name.length()) {
/*      */             locationMap.put(target.getLocation(), target);
/*      */           }
/*      */         });
/*      */ 
/*      */     
/* 1112 */     return (Map<String, Target>)locationMap.values().stream()
/* 1113 */       .collect(Collectors.toMap(Target::getName, target -> target, (a, b) -> b));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printTargets(Project project, boolean printSubTargets, boolean printDependencies) {
/* 1128 */     int maxLength = 0;
/* 1129 */     Map<String, Target> ptargets = removeDuplicateTargets(project.getTargets());
/*      */ 
/*      */     
/* 1132 */     Vector<String> topNames = new Vector<>();
/* 1133 */     Vector<String> topDescriptions = new Vector<>();
/* 1134 */     Vector<Enumeration<String>> topDependencies = new Vector<>();
/* 1135 */     Vector<String> subNames = new Vector<>();
/* 1136 */     Vector<Enumeration<String>> subDependencies = new Vector<>();
/*      */     
/* 1138 */     for (Target currentTarget : ptargets.values()) {
/* 1139 */       String targetName = currentTarget.getName();
/* 1140 */       if (targetName.isEmpty()) {
/*      */         continue;
/*      */       }
/* 1143 */       String targetDescription = currentTarget.getDescription();
/*      */       
/* 1145 */       if (targetDescription == null) {
/* 1146 */         int i = findTargetPosition(subNames, targetName);
/* 1147 */         subNames.insertElementAt(targetName, i);
/* 1148 */         if (printDependencies)
/* 1149 */           subDependencies.insertElementAt(currentTarget.getDependencies(), i); 
/*      */         continue;
/*      */       } 
/* 1152 */       int pos = findTargetPosition(topNames, targetName);
/* 1153 */       topNames.insertElementAt(targetName, pos);
/* 1154 */       topDescriptions.insertElementAt(targetDescription, pos);
/* 1155 */       if (targetName.length() > maxLength) {
/* 1156 */         maxLength = targetName.length();
/*      */       }
/* 1158 */       if (printDependencies) {
/* 1159 */         topDependencies.insertElementAt(currentTarget.getDependencies(), pos);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1164 */     printTargets(project, topNames, topDescriptions, topDependencies, "Main targets:", maxLength);
/*      */ 
/*      */ 
/*      */     
/* 1168 */     if (topNames.isEmpty()) {
/* 1169 */       printSubTargets = true;
/*      */     }
/* 1171 */     if (printSubTargets) {
/* 1172 */       printTargets(project, subNames, null, subDependencies, "Other targets:", 0);
/*      */     }
/*      */     
/* 1175 */     String defaultTarget = project.getDefaultTarget();
/* 1176 */     if (defaultTarget != null && !defaultTarget.isEmpty())
/*      */     {
/* 1178 */       project.log("Default target: " + defaultTarget);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int findTargetPosition(Vector<String> names, String name) {
/* 1193 */     int size = names.size();
/* 1194 */     int res = size;
/* 1195 */     for (int i = 0; i < size && res == size; i++) {
/* 1196 */       if (name.compareTo(names.elementAt(i)) < 0) {
/* 1197 */         res = i;
/*      */       }
/*      */     } 
/* 1200 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printTargets(Project project, Vector<String> names, Vector<String> descriptions, Vector<Enumeration<String>> dependencies, String heading, int maxlen) {
/* 1228 */     String eol = System.lineSeparator();
/*      */     
/* 1230 */     StringBuilder spaces = new StringBuilder("    ");
/* 1231 */     while (spaces.length() <= maxlen) {
/* 1232 */       spaces.append(spaces);
/*      */     }
/* 1234 */     StringBuilder msg = new StringBuilder();
/* 1235 */     msg.append(heading).append(eol).append(eol);
/* 1236 */     int size = names.size();
/* 1237 */     for (int i = 0; i < size; i++) {
/* 1238 */       msg.append(" ");
/* 1239 */       msg.append(names.elementAt(i));
/* 1240 */       if (descriptions != null) {
/* 1241 */         msg.append(spaces
/* 1242 */             .substring(0, maxlen - ((String)names.elementAt(i)).length() + 2));
/* 1243 */         msg.append(descriptions.elementAt(i));
/*      */       } 
/* 1245 */       msg.append(eol);
/* 1246 */       if (!dependencies.isEmpty() && ((Enumeration)dependencies.elementAt(i)).hasMoreElements()) {
/* 1247 */         msg.append(StreamUtils.enumerationAsStream(dependencies.elementAt(i))
/* 1248 */             .collect(Collectors.joining(", ", "   depends on: ", eol)));
/*      */       }
/*      */     } 
/* 1251 */     project.log(msg.toString(), 1);
/*      */   }
/*      */   
/*      */   public Main() {}
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
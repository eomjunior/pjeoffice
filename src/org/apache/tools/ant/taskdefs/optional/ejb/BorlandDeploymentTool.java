/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.ExecTask;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BorlandDeploymentTool
/*     */   extends GenericDeploymentTool
/*     */   implements ExecuteStreamHandler
/*     */ {
/*     */   public static final String PUBLICID_BORLAND_EJB = "-//Inprise Corporation//DTD Enterprise JavaBeans 1.1//EN";
/*     */   protected static final String DEFAULT_BAS45_EJB11_DTD_LOCATION = "/com/inprise/j2ee/xml/dtds/ejb-jar.dtd";
/*     */   protected static final String DEFAULT_BAS_DTD_LOCATION = "/com/inprise/j2ee/xml/dtds/ejb-inprise.dtd";
/*     */   protected static final String BAS_DD = "ejb-inprise.xml";
/*     */   protected static final String BES_DD = "ejb-borland.xml";
/*     */   protected static final String JAVA2IIOP = "java2iiop";
/*     */   protected static final String VERIFY = "com.inprise.ejb.util.Verify";
/* 100 */   private String jarSuffix = "-ejb.jar";
/*     */ 
/*     */   
/*     */   private String borlandDTD;
/*     */ 
/*     */   
/*     */   private boolean java2iiopdebug = false;
/*     */ 
/*     */   
/* 109 */   private String java2iioparams = null;
/*     */ 
/*     */   
/*     */   private boolean generateclient = false;
/*     */ 
/*     */   
/*     */   static final int BES = 5;
/*     */ 
/*     */   
/*     */   static final int BAS = 4;
/*     */ 
/*     */   
/* 121 */   private int version = 4;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean verify = true;
/*     */ 
/*     */   
/* 128 */   private String verifyArgs = "";
/*     */   
/* 130 */   private Map<String, File> genfiles = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 137 */     this.java2iiopdebug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerify(boolean verify) {
/* 145 */     this.verify = verify;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String inString) {
/* 153 */     this.jarSuffix = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerifyArgs(String args) {
/* 161 */     this.verifyArgs = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBASdtd(String inString) {
/* 170 */     this.borlandDTD = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGenerateclient(boolean b) {
/* 179 */     this.generateclient = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(int version) {
/* 187 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJava2iiopParams(String params) {
/* 196 */     this.java2iioparams = params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DescriptorHandler getBorlandDescriptorHandler(final File srcDir) {
/* 205 */     DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir)
/*     */       {
/*     */         protected void processElement() {
/* 208 */           if ("type-storage".equals(this.currentElement))
/*     */           {
/*     */             
/* 211 */             this.ejbFiles.put(this.currentText, new File(srcDir, this.currentText
/* 212 */                   .substring("META-INF/".length())));
/*     */           }
/*     */         }
/*     */       };
/* 216 */     handler.registerDTD("-//Inprise Corporation//DTD Enterprise JavaBeans 1.1//EN", 
/* 217 */         (this.borlandDTD == null) ? "/com/inprise/j2ee/xml/dtds/ejb-inprise.dtd" : this.borlandDTD);
/*     */     
/* 219 */     for (EjbJar.DTDLocation dtdLocation : (getConfig()).dtdLocations) {
/* 220 */       handler.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
/*     */     }
/* 222 */     return handler;
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
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
/* 235 */     if (this.version != 5 && this.version != 4) {
/* 236 */       throw new BuildException("version " + this.version + " is not supported");
/*     */     }
/*     */     
/* 239 */     String dd = (this.version == 5) ? "ejb-borland.xml" : "ejb-inprise.xml";
/*     */     
/* 241 */     log("vendor file : " + ddPrefix + dd, 4);
/*     */     
/* 243 */     File borlandDD = new File((getConfig()).descriptorDir, ddPrefix + dd);
/* 244 */     if (borlandDD.exists()) {
/* 245 */       log("Borland specific file found " + borlandDD, 3);
/* 246 */       ejbFiles.put("META-INF/" + dd, borlandDD);
/*     */     } else {
/* 248 */       log("Unable to locate borland deployment descriptor. It was expected to be in " + borlandDD
/*     */           
/* 250 */           .getPath(), 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File getVendorOutputJarFile(String baseName) {
/* 260 */     return new File(getDestDir(), baseName + this.jarSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyBorlandJar(File sourceJar) {
/* 268 */     if (this.version == 4) {
/* 269 */       verifyBorlandJarV4(sourceJar);
/*     */       return;
/*     */     } 
/* 272 */     if (this.version == 5) {
/* 273 */       verifyBorlandJarV5(sourceJar);
/*     */       return;
/*     */     } 
/* 276 */     log("verify jar skipped because the version is invalid [" + this.version + "]", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyBorlandJarV5(File sourceJar) {
/* 285 */     log("verify BES " + sourceJar, 2);
/*     */     try {
/* 287 */       ExecTask execTask = new ExecTask(getTask());
/* 288 */       execTask.setDir(new File("."));
/* 289 */       execTask.setExecutable("iastool");
/*     */       
/* 291 */       if (getCombinedClasspath() != null) {
/* 292 */         execTask.createArg().setValue("-VBJclasspath");
/* 293 */         execTask.createArg().setValue(getCombinedClasspath().toString());
/*     */       } 
/*     */       
/* 296 */       if (this.java2iiopdebug) {
/* 297 */         execTask.createArg().setValue("-debug");
/*     */       }
/* 299 */       execTask.createArg().setValue("-verify");
/* 300 */       execTask.createArg().setValue("-src");
/*     */       
/* 302 */       execTask.createArg().setValue(sourceJar.getPath());
/* 303 */       log("Calling iastool", 3);
/* 304 */       execTask.execute();
/* 305 */     } catch (Exception e) {
/*     */       
/* 307 */       throw new BuildException("Exception while calling generateclient Details: ", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyBorlandJarV4(File sourceJar) {
/* 316 */     Java javaTask = null;
/* 317 */     log("verify BAS " + sourceJar, 2);
/*     */     try {
/* 319 */       String args = this.verifyArgs;
/* 320 */       args = args + " " + sourceJar.getPath();
/*     */       
/* 322 */       javaTask = new Java(getTask());
/* 323 */       javaTask.setTaskName("verify");
/* 324 */       javaTask.setClassname("com.inprise.ejb.util.Verify");
/* 325 */       Commandline.Argument arguments = javaTask.createArg();
/* 326 */       arguments.setLine(args);
/* 327 */       Path classpath = getCombinedClasspath();
/* 328 */       if (classpath != null) {
/* 329 */         javaTask.setClasspath(classpath);
/* 330 */         javaTask.setFork(true);
/*     */       } 
/*     */       
/* 333 */       log("Calling com.inprise.ejb.util.Verify for " + sourceJar.toString(), 3);
/*     */       
/* 335 */       javaTask.execute();
/* 336 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 339 */       String msg = "Exception while calling com.inprise.ejb.util.Verify Details: " + e.toString();
/* 340 */       throw new BuildException(msg, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateClient(File sourceJar) {
/* 350 */     getTask().getProject().addTaskDefinition("internal_bas_generateclient", BorlandGenerateClient.class);
/*     */ 
/*     */ 
/*     */     
/* 354 */     log("generate client for " + sourceJar, 2);
/*     */     try {
/* 356 */       Project project = getTask().getProject();
/*     */       
/* 358 */       BorlandGenerateClient gentask = (BorlandGenerateClient)project.createTask("internal_bas_generateclient");
/* 359 */       gentask.setEjbjar(sourceJar);
/* 360 */       gentask.setDebug(this.java2iiopdebug);
/* 361 */       Path classpath = getCombinedClasspath();
/* 362 */       if (classpath != null) {
/* 363 */         gentask.setClasspath(classpath);
/*     */       }
/* 365 */       gentask.setVersion(this.version);
/* 366 */       gentask.setTaskName("generate client");
/* 367 */       gentask.execute();
/* 368 */     } catch (Exception e) {
/*     */       
/* 370 */       throw new BuildException("Exception while calling com.inprise.ejb.util.Verify", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildBorlandStubs(Collection<String> ithomes) {
/* 380 */     Execute execTask = new Execute(this);
/* 381 */     Project project = getTask().getProject();
/* 382 */     execTask.setAntRun(project);
/* 383 */     execTask.setWorkingDirectory(project.getBaseDir());
/*     */     
/* 385 */     Commandline commandline = new Commandline();
/* 386 */     commandline.setExecutable("java2iiop");
/*     */     
/* 388 */     if (this.java2iiopdebug) {
/* 389 */       commandline.createArgument().setValue("-VBJdebug");
/*     */     }
/*     */     
/* 392 */     commandline.createArgument().setValue("-VBJclasspath");
/* 393 */     commandline.createArgument().setPath(getCombinedClasspath());
/*     */     
/* 395 */     commandline.createArgument().setValue("-list_files");
/*     */     
/* 397 */     commandline.createArgument().setValue("-no_tie");
/*     */     
/* 399 */     if (this.java2iioparams != null) {
/* 400 */       log("additional  " + this.java2iioparams + " to java2iiop ", 0);
/* 401 */       commandline.createArgument().setLine(this.java2iioparams);
/*     */     } 
/*     */ 
/*     */     
/* 405 */     commandline.createArgument().setValue("-root_dir");
/* 406 */     commandline.createArgument().setValue((getConfig()).srcDir.getAbsolutePath());
/*     */     
/* 408 */     commandline.createArgument().setValue("-compile");
/*     */     
/* 410 */     ithomes.stream().map(Object::toString)
/* 411 */       .forEach(v -> commandline.createArgument().setValue(v));
/*     */     
/*     */     try {
/* 414 */       log("Calling java2iiop", 3);
/* 415 */       log(commandline.describeCommand(), 4);
/* 416 */       execTask.setCommandline(commandline.getCommandline());
/* 417 */       int result = execTask.execute();
/* 418 */       if (Execute.isFailure(result)) {
/* 419 */         throw new BuildException("Failed executing java2iiop (ret code is " + result + ")", 
/*     */             
/* 421 */             getTask().getLocation());
/*     */       }
/* 423 */     } catch (IOException e) {
/* 424 */       log("java2iiop exception :" + e.getMessage(), 0);
/* 425 */       throw new BuildException(e, getTask().getLocation());
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
/*     */   protected void writeJar(String baseName, File jarFile, Hashtable<String, File> files, String publicId) throws BuildException {
/* 443 */     List<String> homes = new ArrayList<>();
/*     */     
/* 445 */     for (String clazz : files.keySet()) {
/* 446 */       if (clazz.endsWith("Home.class")) {
/*     */         
/* 448 */         String home = toClass(clazz);
/* 449 */         homes.add(home);
/* 450 */         log(" Home " + home, 3);
/*     */       } 
/*     */     } 
/*     */     
/* 454 */     buildBorlandStubs(homes);
/*     */ 
/*     */     
/* 457 */     files.putAll(this.genfiles);
/*     */     
/* 459 */     super.writeJar(baseName, jarFile, files, publicId);
/*     */     
/* 461 */     if (this.verify) {
/* 462 */       verifyBorlandJar(jarFile);
/*     */     }
/*     */     
/* 465 */     if (this.generateclient) {
/* 466 */       generateClient(jarFile);
/*     */     }
/* 468 */     this.genfiles.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String toClass(String filename) {
/* 477 */     return filename.substring(0, filename.lastIndexOf(".class"))
/* 478 */       .replace('\\', '.').replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String toClassFile(String filename) {
/* 487 */     return filename.replaceFirst("\\.java$", ".class");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessInputStream(OutputStream param1) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessOutputStream(InputStream is) throws IOException {
/*     */     
/* 514 */     try { BufferedReader reader = new BufferedReader(new InputStreamReader(is));
/*     */       
/*     */       try { String javafile;
/* 517 */         while ((javafile = reader.readLine()) != null) {
/* 518 */           if (javafile.endsWith(".java")) {
/* 519 */             String classfile = toClassFile(javafile);
/* 520 */             String key = classfile.substring(
/* 521 */                 (getConfig()).srcDir.getAbsolutePath().length() + 1);
/* 522 */             this.genfiles.put(key, new File(classfile));
/*     */           } 
/*     */         } 
/* 525 */         reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 526 */     { throw new BuildException("Exception while parsing java2iiop output.", e); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessErrorStream(InputStream is) throws IOException {
/* 537 */     BufferedReader reader = new BufferedReader(new InputStreamReader(is));
/* 538 */     String s = reader.readLine();
/* 539 */     if (s != null)
/* 540 */       log("[java2iiop] " + s, 0); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/BorlandDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.tools.ant.launch;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Launcher
/*     */ {
/*     */   public static final String ANTHOME_PROPERTY = "ant.home";
/*     */   public static final String ANTLIBDIR_PROPERTY = "ant.library.dir";
/*     */   public static final String ANT_PRIVATEDIR = ".ant";
/*     */   public static final String ANT_PRIVATELIB = "lib";
/*  65 */   public static final String USER_LIBDIR = ".ant" + File.separatorChar + "lib";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MAIN_CLASS = "org.apache.tools.ant.Main";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String USER_HOMEDIR = "user.home";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String JAVA_CLASS_PATH = "java.class.path";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int EXIT_CODE_ERROR = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     int exitCode;
/*  98 */     boolean launchDiag = false;
/*     */     try {
/* 100 */       Launcher launcher = new Launcher();
/* 101 */       exitCode = launcher.run(args);
/* 102 */       launchDiag = launcher.launchDiag;
/* 103 */     } catch (LaunchException e) {
/* 104 */       exitCode = 2;
/* 105 */       System.err.println(e.getMessage());
/* 106 */     } catch (Throwable t) {
/* 107 */       exitCode = 2;
/* 108 */       t.printStackTrace(System.err);
/*     */     } 
/* 110 */     if (exitCode != 0) {
/* 111 */       if (launchDiag) {
/* 112 */         System.out.println("Exit code: " + exitCode);
/*     */       }
/* 114 */       System.exit(exitCode);
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
/*     */   public boolean launchDiag = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPath(String path, boolean getJars, List<URL> libPathURLs) throws MalformedURLException {
/* 138 */     StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator);
/* 139 */     while (tokenizer.hasMoreElements()) {
/* 140 */       String elementName = tokenizer.nextToken();
/* 141 */       File element = new File(elementName);
/* 142 */       if (elementName.contains("%") && !element.exists()) {
/*     */         continue;
/*     */       }
/* 145 */       if (getJars && element.isDirectory())
/*     */       {
/* 147 */         for (URL dirURL : Locator.getLocationURLs(element)) {
/* 148 */           if (this.launchDiag) {
/* 149 */             System.out.println("adding library JAR: " + dirURL);
/*     */           }
/* 151 */           libPathURLs.add(dirURL);
/*     */         } 
/*     */       }
/*     */       
/* 155 */       URL url = new URL(element.toURI().toASCIIString());
/* 156 */       if (this.launchDiag) {
/* 157 */         System.out.println("adding library URL: " + url);
/*     */       }
/* 159 */       libPathURLs.add(url);
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
/*     */   private int run(String[] args) throws LaunchException, MalformedURLException {
/* 175 */     String newArgs[], antHomeProperty = System.getProperty("ant.home");
/* 176 */     File antHome = null;
/*     */     
/* 178 */     File sourceJar = Locator.getClassSource(getClass());
/* 179 */     File jarDir = sourceJar.getParentFile();
/* 180 */     String mainClassname = "org.apache.tools.ant.Main";
/*     */     
/* 182 */     if (antHomeProperty != null) {
/* 183 */       antHome = new File(antHomeProperty);
/*     */     }
/*     */     
/* 186 */     if (antHome == null || !antHome.exists()) {
/* 187 */       antHome = jarDir.getParentFile();
/* 188 */       setProperty("ant.home", antHome.getAbsolutePath());
/*     */     } 
/*     */     
/* 191 */     if (!antHome.exists()) {
/* 192 */       throw new LaunchException("Ant home is set incorrectly or ant could not be located (estimated value=" + antHome
/*     */           
/* 194 */           .getAbsolutePath() + ")");
/*     */     }
/*     */     
/* 197 */     List<String> libPaths = new ArrayList<>();
/* 198 */     String cpString = null;
/* 199 */     List<String> argList = new ArrayList<>();
/*     */     
/* 201 */     boolean noUserLib = false;
/* 202 */     boolean noClassPath = false;
/*     */     
/* 204 */     for (int i = 0; i < args.length; i++) {
/* 205 */       if ("-lib".equals(args[i])) {
/* 206 */         if (i == args.length - 1) {
/* 207 */           throw new LaunchException("The -lib argument must be followed by a library location");
/*     */         }
/*     */         
/* 210 */         libPaths.add(args[++i]);
/* 211 */       } else if ("-cp".equals(args[i])) {
/* 212 */         if (i == args.length - 1) {
/* 213 */           throw new LaunchException("The -cp argument must be followed by a classpath expression");
/*     */         }
/*     */         
/* 216 */         if (cpString != null) {
/* 217 */           throw new LaunchException("The -cp argument must not be repeated");
/*     */         }
/*     */         
/* 220 */         cpString = args[++i];
/* 221 */       } else if ("--nouserlib".equals(args[i]) || "-nouserlib".equals(args[i])) {
/* 222 */         noUserLib = true;
/* 223 */       } else if ("--launchdiag".equals(args[i])) {
/* 224 */         this.launchDiag = true;
/* 225 */       } else if ("--noclasspath".equals(args[i]) || "-noclasspath".equals(args[i])) {
/* 226 */         noClassPath = true;
/* 227 */       } else if ("-main".equals(args[i])) {
/* 228 */         if (i == args.length - 1) {
/* 229 */           throw new LaunchException("The -main argument must be followed by a library location");
/*     */         }
/*     */         
/* 232 */         mainClassname = args[++i];
/*     */       } else {
/* 234 */         argList.add(args[i]);
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     logPath("Launcher JAR", sourceJar);
/* 239 */     logPath("Launcher JAR directory", sourceJar.getParentFile());
/* 240 */     logPath("java.home", new File(System.getProperty("java.home")));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     if (argList.size() == args.length) {
/* 246 */       newArgs = args;
/*     */     } else {
/* 248 */       newArgs = argList.<String>toArray(new String[0]);
/*     */     } 
/*     */     
/* 251 */     URL[] libURLs = getLibPathURLs(
/* 252 */         noClassPath ? null : cpString, libPaths);
/* 253 */     URL[] systemURLs = getSystemURLs(jarDir);
/* 254 */     URL[] userURLs = noUserLib ? new URL[0] : getUserURLs();
/*     */     
/* 256 */     File toolsJAR = Locator.getToolsJar();
/* 257 */     logPath("tools.jar", toolsJAR);
/* 258 */     URL[] jars = getJarArray(libURLs, userURLs, systemURLs, toolsJAR);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     StringBuilder baseClassPath = new StringBuilder(System.getProperty("java.class.path"));
/* 264 */     if (baseClassPath.charAt(baseClassPath.length() - 1) == File.pathSeparatorChar)
/*     */     {
/* 266 */       baseClassPath.setLength(baseClassPath.length() - 1);
/*     */     }
/*     */     
/* 269 */     for (URL jar : jars) {
/* 270 */       baseClassPath.append(File.pathSeparatorChar);
/* 271 */       baseClassPath.append(Locator.fromURI(jar.toString()));
/*     */     } 
/*     */     
/* 274 */     setProperty("java.class.path", baseClassPath.toString());
/*     */     
/* 276 */     URLClassLoader loader = new URLClassLoader(jars, Launcher.class.getClassLoader());
/* 277 */     Thread.currentThread().setContextClassLoader(loader);
/* 278 */     Class<? extends AntMain> mainClass = null;
/* 279 */     int exitCode = 0;
/* 280 */     Throwable thrown = null;
/*     */     try {
/* 282 */       mainClass = loader.loadClass(mainClassname).asSubclass(AntMain.class);
/* 283 */       AntMain main = mainClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 284 */       main.startAnt(newArgs, null, null);
/* 285 */     } catch (InstantiationException ex) {
/* 286 */       System.err.println("Incompatible version of " + mainClassname + " detected");
/*     */       
/* 288 */       File mainJar = Locator.getClassSource(mainClass);
/* 289 */       System.err.println("Location of this class " + mainJar);
/*     */       
/* 291 */       thrown = ex;
/* 292 */     } catch (ClassNotFoundException cnfe) {
/* 293 */       System.err.println("Failed to locate" + mainClassname);
/*     */       
/* 295 */       thrown = cnfe;
/* 296 */     } catch (Throwable t) {
/* 297 */       t.printStackTrace(System.err);
/* 298 */       thrown = t;
/*     */     } 
/* 300 */     if (thrown != null) {
/* 301 */       System.err.println("ant.home: " + antHome.getAbsolutePath());
/* 302 */       System.err.println("Classpath: " + baseClassPath.toString());
/* 303 */       System.err.println("Launcher JAR: " + sourceJar.getAbsolutePath());
/* 304 */       System.err.println("Launcher Directory: " + jarDir.getAbsolutePath());
/* 305 */       exitCode = 2;
/*     */     } 
/* 307 */     return exitCode;
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
/*     */   private URL[] getLibPathURLs(String cpString, List<String> libPaths) throws MalformedURLException {
/* 320 */     List<URL> libPathURLs = new ArrayList<>();
/*     */     
/* 322 */     if (cpString != null) {
/* 323 */       addPath(cpString, false, libPathURLs);
/*     */     }
/*     */     
/* 326 */     for (String libPath : libPaths) {
/* 327 */       addPath(libPath, true, libPathURLs);
/*     */     }
/*     */     
/* 330 */     return libPathURLs.<URL>toArray(new URL[0]);
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
/*     */   private URL[] getSystemURLs(File antLauncherDir) throws MalformedURLException {
/* 342 */     File antLibDir = null;
/* 343 */     String antLibDirProperty = System.getProperty("ant.library.dir");
/* 344 */     if (antLibDirProperty != null) {
/* 345 */       antLibDir = new File(antLibDirProperty);
/*     */     }
/* 347 */     if (antLibDir == null || !antLibDir.exists()) {
/* 348 */       antLibDir = antLauncherDir;
/* 349 */       setProperty("ant.library.dir", antLibDir.getAbsolutePath());
/*     */     } 
/* 351 */     return Locator.getLocationURLs(antLibDir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URL[] getUserURLs() throws MalformedURLException {
/* 361 */     File userLibDir = new File(System.getProperty("user.home"), USER_LIBDIR);
/*     */     
/* 363 */     return Locator.getLocationURLs(userLibDir);
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
/*     */   private URL[] getJarArray(URL[] libJars, URL[] userJars, URL[] systemJars, File toolsJar) throws MalformedURLException {
/* 378 */     int numJars = libJars.length + userJars.length + systemJars.length;
/* 379 */     if (toolsJar != null) {
/* 380 */       numJars++;
/*     */     }
/* 382 */     URL[] jars = new URL[numJars];
/* 383 */     System.arraycopy(libJars, 0, jars, 0, libJars.length);
/* 384 */     System.arraycopy(userJars, 0, jars, libJars.length, userJars.length);
/* 385 */     System.arraycopy(systemJars, 0, jars, userJars.length + libJars.length, systemJars.length);
/*     */ 
/*     */     
/* 388 */     if (toolsJar != null) {
/* 389 */       jars[jars.length - 1] = new URL(toolsJar.toURI().toASCIIString());
/*     */     }
/* 391 */     return jars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProperty(String name, String value) {
/* 400 */     if (this.launchDiag) {
/* 401 */       System.out.println("Setting \"" + name + "\" to \"" + value + "\"");
/*     */     }
/* 403 */     System.setProperty(name, value);
/*     */   }
/*     */   
/*     */   private void logPath(String name, File path) {
/* 407 */     if (this.launchDiag)
/* 408 */       System.out.println(name + "= \"" + path + "\""); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/launch/Launcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
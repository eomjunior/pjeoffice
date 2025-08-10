/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JavaEnvUtils
/*     */ {
/*  41 */   private static final boolean IS_DOS = Os.isFamily("dos");
/*     */   
/*  43 */   private static final boolean IS_NETWARE = Os.isName("netware");
/*     */   
/*  45 */   private static final boolean IS_AIX = Os.isName("aix");
/*     */ 
/*     */   
/*  48 */   private static final String JAVA_HOME = System.getProperty("java.home");
/*     */ 
/*     */   
/*  51 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String javaVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int javaVersionNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 189 */       javaVersion = "1.8";
/* 190 */       javaVersionNumber = 18;
/* 191 */       Class.forName("java.lang.module.ModuleDescriptor");
/*     */       
/* 193 */       String v = System.getProperty("java.specification.version");
/* 194 */       DeweyDecimal pv = new DeweyDecimal(v);
/* 195 */       javaVersionNumber = pv.get(0) * 10;
/* 196 */       if (pv.getSize() > 1) {
/* 197 */         javaVersionNumber += pv.get(1);
/*     */       }
/* 199 */       javaVersion = pv.toString();
/* 200 */     } catch (Throwable throwable) {}
/*     */   }
/*     */   public static final String JAVA_1_0 = "1.0"; public static final int VERSION_1_0 = 10; public static final String JAVA_1_1 = "1.1"; public static final int VERSION_1_1 = 11; public static final String JAVA_1_2 = "1.2"; public static final int VERSION_1_2 = 12; public static final String JAVA_1_3 = "1.3"; public static final int VERSION_1_3 = 13; public static final String JAVA_1_4 = "1.4"; public static final int VERSION_1_4 = 14; public static final String JAVA_1_5 = "1.5"; public static final int VERSION_1_5 = 15; public static final String JAVA_1_6 = "1.6"; public static final int VERSION_1_6 = 16;
/*     */   public static final String JAVA_1_7 = "1.7";
/* 204 */   private static final DeweyDecimal parsedJavaVersion = new DeweyDecimal(javaVersion); public static final int VERSION_1_7 = 17; public static final String JAVA_1_8 = "1.8"; public static final int VERSION_1_8 = 18; @Deprecated
/*     */   public static final String JAVA_1_9 = "1.9"; @Deprecated
/*     */   public static final int VERSION_1_9 = 19; public static final String JAVA_9 = "9"; public static final int VERSION_9 = 90; static { try {
/* 207 */       Class.forName("kaffe.util.NotImplemented");
/* 208 */       kaffeDetected = true;
/* 209 */     } catch (Throwable throwable) {} }
/*     */   
/*     */   public static final String JAVA_10 = "10"; public static final int VERSION_10 = 100; public static final String JAVA_11 = "11"; public static final int VERSION_11 = 110; public static final String JAVA_12 = "12"; public static final int VERSION_12 = 120; private static boolean kaffeDetected = false; private static boolean classpathDetected = false;
/*     */   static {
/*     */     try {
/* 214 */       Class.forName("gnu.classpath.Configuration");
/* 215 */       classpathDetected = true;
/* 216 */     } catch (Throwable throwable) {}
/*     */   }
/*     */   private static boolean gijDetected = false;
/*     */   static {
/*     */     try {
/* 221 */       Class.forName("gnu.gcj.Core");
/* 222 */       gijDetected = true;
/* 223 */     } catch (Throwable throwable) {}
/*     */   }
/*     */   private static boolean harmonyDetected = false; private static Vector<String> jrePackages;
/*     */   static {
/*     */     try {
/* 228 */       Class.forName("org.apache.harmony.luni.util.Base64");
/* 229 */       harmonyDetected = true;
/* 230 */     } catch (Throwable throwable) {}
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
/*     */   public static String getJavaVersion() {
/* 244 */     return javaVersion;
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
/*     */   @Deprecated
/*     */   public static int getJavaVersionNumber() {
/* 259 */     return javaVersionNumber;
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
/*     */   public static DeweyDecimal getParsedJavaVersion() {
/* 271 */     return parsedJavaVersion;
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
/*     */   public static boolean isJavaVersion(String version) {
/* 285 */     return (javaVersion.equals(version) || (javaVersion
/* 286 */       .equals("9") && "1.9".equals(version)));
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
/*     */   public static boolean isAtLeastJavaVersion(String version) {
/* 301 */     return (parsedJavaVersion.compareTo(new DeweyDecimal(version)) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isKaffe() {
/* 311 */     return kaffeDetected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isClasspathBased() {
/* 320 */     return classpathDetected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGij() {
/* 330 */     return gijDetected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isApacheHarmony() {
/* 339 */     return harmonyDetected;
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
/*     */   public static String getJreExecutable(String command) {
/* 361 */     if (IS_NETWARE)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 366 */       return command;
/*     */     }
/*     */     
/* 369 */     File jExecutable = null;
/*     */     
/* 371 */     if (IS_AIX)
/*     */     {
/*     */       
/* 374 */       jExecutable = findInDir(JAVA_HOME + "/sh", command);
/*     */     }
/*     */     
/* 377 */     if (jExecutable == null) {
/* 378 */       jExecutable = findInDir(JAVA_HOME + "/bin", command);
/*     */     }
/*     */     
/* 381 */     if (jExecutable != null) {
/* 382 */       return jExecutable.getAbsolutePath();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 387 */     return addExtension(command);
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
/*     */   public static String getJdkExecutable(String command) {
/* 402 */     if (IS_NETWARE)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 407 */       return command;
/*     */     }
/*     */     
/* 410 */     File jExecutable = null;
/*     */     
/* 412 */     if (IS_AIX)
/*     */     {
/*     */       
/* 415 */       jExecutable = findInDir(JAVA_HOME + "/../sh", command);
/*     */     }
/*     */     
/* 418 */     if (jExecutable == null) {
/* 419 */       jExecutable = findInDir(JAVA_HOME + "/../bin", command);
/*     */     }
/*     */     
/* 422 */     if (jExecutable != null) {
/* 423 */       return jExecutable.getAbsolutePath();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 430 */     return getJreExecutable(command);
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
/*     */   private static String addExtension(String command) {
/* 442 */     return command + (IS_DOS ? ".exe" : "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File findInDir(String dirName, String commandName) {
/* 451 */     File dir = FILE_UTILS.normalize(dirName);
/* 452 */     File executable = null;
/* 453 */     if (dir.exists()) {
/* 454 */       executable = new File(dir, addExtension(commandName));
/* 455 */       if (!executable.exists()) {
/* 456 */         executable = null;
/*     */       }
/*     */     } 
/* 459 */     return executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void buildJrePackages() {
/* 470 */     jrePackages = new Vector<>();
/* 471 */     jrePackages.addElement("sun");
/* 472 */     jrePackages.addElement("java");
/* 473 */     jrePackages.addElement("javax");
/* 474 */     jrePackages.addElement("com.sun.java");
/* 475 */     jrePackages.addElement("com.sun.image");
/* 476 */     jrePackages.addElement("org.omg");
/* 477 */     jrePackages.addElement("com.sun.corba");
/* 478 */     jrePackages.addElement("com.sun.jndi");
/* 479 */     jrePackages.addElement("com.sun.media");
/* 480 */     jrePackages.addElement("com.sun.naming");
/* 481 */     jrePackages.addElement("com.sun.org.omg");
/* 482 */     jrePackages.addElement("com.sun.rmi");
/* 483 */     jrePackages.addElement("sunw.io");
/* 484 */     jrePackages.addElement("sunw.util");
/* 485 */     jrePackages.addElement("org.ietf.jgss");
/* 486 */     jrePackages.addElement("org.w3c.dom");
/* 487 */     jrePackages.addElement("org.xml.sax");
/* 488 */     jrePackages.addElement("com.sun.org.apache");
/* 489 */     jrePackages.addElement("jdk");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector<String> getJrePackageTestCases() {
/* 499 */     Vector<String> tests = new Vector<>();
/* 500 */     tests.addElement("java.lang.Object");
/* 501 */     tests.addElement("sun.reflect.SerializationConstructorAccessorImpl");
/* 502 */     tests.addElement("sun.net.www.http.HttpClient");
/* 503 */     tests.addElement("sun.audio.AudioPlayer");
/* 504 */     tests.addElement("javax.accessibility.Accessible");
/* 505 */     tests.addElement("sun.misc.BASE64Encoder");
/* 506 */     tests.addElement("com.sun.image.codec.jpeg.JPEGCodec");
/* 507 */     tests.addElement("org.omg.CORBA.Any");
/* 508 */     tests.addElement("com.sun.corba.se.internal.corba.AnyImpl");
/* 509 */     tests.addElement("com.sun.jndi.ldap.LdapURL");
/* 510 */     tests.addElement("com.sun.media.sound.Printer");
/* 511 */     tests.addElement("com.sun.naming.internal.VersionHelper");
/* 512 */     tests.addElement("com.sun.org.omg.CORBA.Initializer");
/* 513 */     tests.addElement("sunw.io.Serializable");
/* 514 */     tests.addElement("sunw.util.EventListener");
/* 515 */     tests.addElement("sun.audio.AudioPlayer");
/* 516 */     tests.addElement("org.ietf.jgss.Oid");
/* 517 */     tests.addElement("org.w3c.dom.Attr");
/* 518 */     tests.addElement("org.xml.sax.XMLReader");
/* 519 */     tests.addElement("com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl");
/* 520 */     tests.addElement("jdk.net.Sockets");
/* 521 */     return tests;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector<String> getJrePackages() {
/* 529 */     if (jrePackages == null) {
/* 530 */       buildJrePackages();
/*     */     }
/* 532 */     return jrePackages;
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
/*     */   public static File createVmsJavaOptionFile(String[] cmds) throws IOException {
/* 545 */     File script = FILE_UTILS.createTempFile(null, "ANT", ".JAVA_OPTS", null, false, true);
/* 546 */     BufferedWriter out = new BufferedWriter(new FileWriter(script)); 
/* 547 */     try { for (String cmd : cmds) {
/* 548 */         out.write(cmd);
/* 549 */         out.newLine();
/*     */       } 
/* 551 */       out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 552 */      return script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaHome() {
/* 560 */     return JAVA_HOME;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/JavaEnvUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
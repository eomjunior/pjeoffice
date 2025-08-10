/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Calendar;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import org.apache.tools.ant.launch.Launcher;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.JAXPUtils;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.apache.tools.ant.util.java15.ProxyDiagnostics;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Diagnostics
/*     */ {
/*     */   private static final int BIG_DRIFT_LIMIT = 10000;
/*     */   private static final int TEST_FILE_SIZE = 32;
/*     */   private static final int KILOBYTE = 1024;
/*     */   private static final int SECONDS_PER_MILLISECOND = 1000;
/*     */   private static final int SECONDS_PER_MINUTE = 60;
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */   protected static final String ERROR_PROPERTY_ACCESS_BLOCKED = "Access to this property blocked by a security manager";
/*     */   
/*     */   @Deprecated
/*     */   public static boolean isOptionalAvailable() {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void validateVersion() throws BuildException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File[] listLibraries() {
/* 109 */     String home = System.getProperty("ant.home");
/* 110 */     if (home == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     return listJarFiles(new File(home, "lib"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File[] listJarFiles(File libDir) {
/* 123 */     return libDir.listFiles((dir, name) -> name.endsWith(".jar"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 131 */     doReport(System.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getImplementationVersion(Class<?> clazz) {
/* 141 */     return clazz.getPackage().getImplementationVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URL getClassLocation(Class<?> clazz) {
/* 150 */     if (clazz.getProtectionDomain().getCodeSource() == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     return clazz.getProtectionDomain().getCodeSource().getLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getXMLParserName() {
/* 161 */     SAXParser saxParser = getSAXParser();
/* 162 */     if (saxParser == null) {
/* 163 */       return "Could not create an XML Parser";
/*     */     }
/*     */     
/* 166 */     return saxParser.getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getXSLTProcessorName() {
/* 174 */     Transformer transformer = getXSLTProcessor();
/* 175 */     if (transformer == null) {
/* 176 */       return "Could not create an XSLT Processor";
/*     */     }
/*     */     
/* 179 */     return transformer.getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SAXParser getSAXParser() {
/* 187 */     SAXParserFactory saxParserFactory = null;
/*     */     try {
/* 189 */       saxParserFactory = SAXParserFactory.newInstance();
/* 190 */     } catch (Exception e) {
/*     */       
/* 192 */       ignoreThrowable(e);
/* 193 */       return null;
/*     */     } 
/* 195 */     SAXParser saxParser = null;
/*     */     try {
/* 197 */       saxParser = saxParserFactory.newSAXParser();
/* 198 */     } catch (Exception e) {
/*     */       
/* 200 */       ignoreThrowable(e);
/*     */     } 
/* 202 */     return saxParser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Transformer getXSLTProcessor() {
/* 210 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 211 */     if (transformerFactory != null) {
/*     */       try {
/* 213 */         return transformerFactory.newTransformer();
/* 214 */       } catch (Exception e) {
/*     */         
/* 216 */         ignoreThrowable(e);
/*     */       } 
/*     */     }
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getXMLParserLocation() {
/* 227 */     SAXParser saxParser = getSAXParser();
/* 228 */     if (saxParser == null) {
/* 229 */       return null;
/*     */     }
/* 231 */     URL location = getClassLocation(saxParser.getClass());
/* 232 */     return (location != null) ? location.toString() : null;
/*     */   }
/*     */   
/*     */   private static String getNamespaceParserName() {
/*     */     try {
/* 237 */       XMLReader reader = JAXPUtils.getNamespaceXMLReader();
/* 238 */       return reader.getClass().getName();
/* 239 */     } catch (BuildException e) {
/*     */       
/* 241 */       ignoreThrowable(e);
/* 242 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getNamespaceParserLocation() {
/*     */     try {
/* 248 */       XMLReader reader = JAXPUtils.getNamespaceXMLReader();
/* 249 */       URL location = getClassLocation(reader.getClass());
/* 250 */       return (location != null) ? location.toString() : null;
/* 251 */     } catch (BuildException e) {
/*     */       
/* 253 */       ignoreThrowable(e);
/* 254 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getXSLTProcessorLocation() {
/* 263 */     Transformer transformer = getXSLTProcessor();
/* 264 */     if (transformer == null) {
/* 265 */       return null;
/*     */     }
/* 267 */     URL location = getClassLocation(transformer.getClass());
/* 268 */     return (location != null) ? location.toString() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void ignoreThrowable(Throwable thrown) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void doReport(PrintStream out) {
/* 285 */     doReport(out, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void doReport(PrintStream out, int logLevel) {
/* 295 */     out.println("------- Ant diagnostics report -------");
/* 296 */     out.println(Main.getAntVersion());
/* 297 */     header(out, "Implementation Version");
/*     */     
/* 299 */     out.println("core tasks     : " + getImplementationVersion(Main.class) + " in " + 
/* 300 */         getClassLocation(Main.class));
/*     */     
/* 302 */     header(out, "ANT PROPERTIES");
/* 303 */     doReportAntProperties(out);
/*     */     
/* 305 */     header(out, "ANT_HOME/lib jar listing");
/* 306 */     doReportAntHomeLibraries(out);
/*     */     
/* 308 */     header(out, "USER_HOME/.ant/lib jar listing");
/* 309 */     doReportUserHomeLibraries(out);
/*     */     
/* 311 */     header(out, "Tasks availability");
/* 312 */     doReportTasksAvailability(out);
/*     */     
/* 314 */     header(out, "org.apache.env.Which diagnostics");
/* 315 */     doReportWhich(out);
/*     */     
/* 317 */     header(out, "XML Parser information");
/* 318 */     doReportParserInfo(out);
/*     */     
/* 320 */     header(out, "XSLT Processor information");
/* 321 */     doReportXSLTProcessorInfo(out);
/*     */     
/* 323 */     header(out, "System properties");
/* 324 */     doReportSystemProperties(out);
/*     */     
/* 326 */     header(out, "Temp dir");
/* 327 */     doReportTempDir(out);
/*     */     
/* 329 */     header(out, "Locale information");
/* 330 */     doReportLocale(out);
/*     */     
/* 332 */     header(out, "Proxy information");
/* 333 */     doReportProxy(out);
/*     */     
/* 335 */     out.println();
/*     */   }
/*     */   
/*     */   private static void header(PrintStream out, String section) {
/* 339 */     out.println();
/* 340 */     out.println("-------------------------------------------");
/* 341 */     out.print(" ");
/* 342 */     out.println(section);
/* 343 */     out.println("-------------------------------------------");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportSystemProperties(PrintStream out) {
/* 351 */     Properties sysprops = null;
/*     */     try {
/* 353 */       sysprops = System.getProperties();
/* 354 */     } catch (SecurityException e) {
/* 355 */       ignoreThrowable(e);
/* 356 */       out.println("Access to System.getProperties() blocked by a security manager");
/*     */       
/*     */       return;
/*     */     } 
/* 360 */     Objects.requireNonNull(out); sysprops.stringPropertyNames().stream().map(key -> key + " : " + getProperty(key)).forEach(out::println);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getProperty(String key) {
/*     */     String value;
/*     */     try {
/* 373 */       value = System.getProperty(key);
/* 374 */     } catch (SecurityException e) {
/* 375 */       value = "Access to this property blocked by a security manager";
/*     */     } 
/* 377 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportAntProperties(PrintStream out) {
/* 385 */     Project p = new Project();
/* 386 */     p.initProperties();
/* 387 */     out.println("ant.version: " + p.getProperty("ant.version"));
/* 388 */     out.println("ant.java.version: " + p
/* 389 */         .getProperty("ant.java.version"));
/* 390 */     out.println("Is this the Apache Harmony VM? " + (
/* 391 */         JavaEnvUtils.isApacheHarmony() ? "yes" : "no"));
/* 392 */     out.println("Is this the Kaffe VM? " + (
/* 393 */         JavaEnvUtils.isKaffe() ? "yes" : "no"));
/* 394 */     out.println("Is this gij/gcj? " + (
/* 395 */         JavaEnvUtils.isGij() ? "yes" : "no"));
/* 396 */     out.println("ant.core.lib: " + p.getProperty("ant.core.lib"));
/* 397 */     out.println("ant.home: " + p.getProperty("ant.home"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportAntHomeLibraries(PrintStream out) {
/* 405 */     out.println("ant.home: " + System.getProperty("ant.home"));
/* 406 */     printLibraries(listLibraries(), out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportUserHomeLibraries(PrintStream out) {
/* 415 */     String home = System.getProperty("user.home");
/* 416 */     out.println("user.home: " + home);
/* 417 */     File libDir = new File(home, Launcher.USER_LIBDIR);
/* 418 */     printLibraries(listJarFiles(libDir), out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void printLibraries(File[] libs, PrintStream out) {
/* 427 */     if (libs == null) {
/* 428 */       out.println("No such directory.");
/*     */       return;
/*     */     } 
/* 431 */     for (File lib : libs) {
/* 432 */       out.println(lib.getName() + " (" + lib.length() + " bytes)");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportWhich(PrintStream out) {
/* 443 */     Throwable error = null;
/*     */     try {
/* 445 */       Class<?> which = Class.forName("org.apache.env.Which");
/* 446 */       Method method = which.getMethod("main", new Class[] { String[].class });
/*     */       
/* 448 */       method.invoke(null, new Object[] { new String[0] });
/* 449 */     } catch (ClassNotFoundException e) {
/* 450 */       out.println("Not available.");
/* 451 */       out.println("Download it at https://xml.apache.org/commons/");
/* 452 */     } catch (InvocationTargetException e) {
/* 453 */       error = (e.getTargetException() == null) ? e : e.getTargetException();
/* 454 */     } catch (Throwable e) {
/* 455 */       error = e;
/*     */     } 
/*     */     
/* 458 */     if (error != null) {
/* 459 */       out.println("Error while running org.apache.env.Which");
/* 460 */       error.printStackTrace(out);
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
/*     */   private static void doReportTasksAvailability(PrintStream out) {
/* 473 */     InputStream is = Main.class.getResourceAsStream("/org/apache/tools/ant/taskdefs/defaults.properties");
/*     */     
/* 475 */     if (is == null) {
/* 476 */       out.println("None available");
/*     */     } else {
/* 478 */       Properties props = new Properties();
/*     */       try {
/* 480 */         props.load(is);
/* 481 */         for (String key : props.stringPropertyNames()) {
/* 482 */           String classname = props.getProperty(key);
/*     */           try {
/* 484 */             Class.forName(classname);
/* 485 */             props.remove(key);
/* 486 */           } catch (ClassNotFoundException e) {
/* 487 */             out.println(key + " : Not Available (the implementation class is not present)");
/*     */           }
/* 489 */           catch (NoClassDefFoundError e) {
/* 490 */             String pkg = e.getMessage().replace('/', '.');
/* 491 */             out.println(key + " : Missing dependency " + pkg);
/* 492 */           } catch (LinkageError e) {
/* 493 */             out.println(key + " : Initialization error");
/*     */           } 
/*     */         } 
/* 496 */         if (props.size() == 0) {
/* 497 */           out.println("All defined tasks are available");
/*     */         } else {
/* 499 */           out.println("A task being missing/unavailable should only matter if you are trying to use it");
/*     */         }
/*     */       
/* 502 */       } catch (IOException e) {
/* 503 */         out.println(e.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportParserInfo(PrintStream out) {
/* 513 */     String parserName = getXMLParserName();
/* 514 */     String parserLocation = getXMLParserLocation();
/* 515 */     printParserInfo(out, "XML Parser", parserName, parserLocation);
/* 516 */     printParserInfo(out, "Namespace-aware parser", getNamespaceParserName(), 
/* 517 */         getNamespaceParserLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportXSLTProcessorInfo(PrintStream out) {
/* 525 */     String processorName = getXSLTProcessorName();
/* 526 */     String processorLocation = getXSLTProcessorLocation();
/* 527 */     printParserInfo(out, "XSLT Processor", processorName, processorLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void printParserInfo(PrintStream out, String parserType, String parserName, String parserLocation) {
/* 532 */     if (parserName == null) {
/* 533 */       parserName = "unknown";
/*     */     }
/* 535 */     if (parserLocation == null) {
/* 536 */       parserLocation = "unknown";
/*     */     }
/* 538 */     out.println(parserType + " : " + parserName);
/* 539 */     out.println(parserType + " Location: " + parserLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportTempDir(PrintStream out) {
/* 549 */     String tempdir = System.getProperty("java.io.tmpdir");
/* 550 */     if (tempdir == null) {
/* 551 */       out.println("Warning: java.io.tmpdir is undefined");
/*     */       return;
/*     */     } 
/* 554 */     out.println("Temp dir is " + tempdir);
/* 555 */     File tempDirectory = new File(tempdir);
/* 556 */     if (!tempDirectory.exists()) {
/* 557 */       out.println("Warning, java.io.tmpdir directory does not exist: " + tempdir);
/*     */       
/*     */       return;
/*     */     } 
/* 561 */     long now = System.currentTimeMillis();
/* 562 */     File tempFile = null;
/* 563 */     OutputStream fileout = null;
/* 564 */     InputStream filein = null;
/*     */     try {
/* 566 */       tempFile = File.createTempFile("diag", "txt", tempDirectory);
/*     */       
/* 568 */       fileout = Files.newOutputStream(tempFile.toPath(), new java.nio.file.OpenOption[0]);
/* 569 */       byte[] buffer = new byte[1024];
/* 570 */       for (int i = 0; i < 32; i++) {
/* 571 */         fileout.write(buffer);
/*     */       }
/* 573 */       fileout.close();
/* 574 */       fileout = null;
/*     */ 
/*     */       
/* 577 */       Thread.sleep(1000L);
/* 578 */       filein = Files.newInputStream(tempFile.toPath(), new java.nio.file.OpenOption[0]);
/* 579 */       int total = 0;
/* 580 */       int read = 0;
/* 581 */       while ((read = filein.read(buffer, 0, 1024)) > 0) {
/* 582 */         total += read;
/*     */       }
/* 584 */       filein.close();
/* 585 */       filein = null;
/*     */       
/* 587 */       long filetime = tempFile.lastModified();
/* 588 */       long drift = filetime - now;
/* 589 */       tempFile.delete();
/*     */       
/* 591 */       out.print("Temp dir is writeable");
/* 592 */       if (total != 32768) {
/* 593 */         out.println(", but seems to be full.  Wrote 32768but could only read " + total + " bytes.");
/*     */       }
/*     */       else {
/*     */         
/* 597 */         out.println();
/*     */       } 
/*     */       
/* 600 */       out.println("Temp dir alignment with system clock is " + drift + " ms");
/* 601 */       if (Math.abs(drift) > 10000L) {
/* 602 */         out.println("Warning: big clock drift -maybe a network filesystem");
/*     */       }
/* 604 */     } catch (IOException e) {
/* 605 */       ignoreThrowable(e);
/* 606 */       out.println("Failed to create a temporary file in the temp dir " + tempdir);
/* 607 */       out.println("File  " + tempFile + " could not be created/written to");
/* 608 */     } catch (InterruptedException e) {
/* 609 */       ignoreThrowable(e);
/* 610 */       out.println("Failed to check whether tempdir is writable");
/*     */     } finally {
/* 612 */       FileUtils.close(fileout);
/* 613 */       FileUtils.close(filein);
/* 614 */       if (tempFile != null && tempFile.exists()) {
/* 615 */         tempFile.delete();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportLocale(PrintStream out) {
/* 626 */     Calendar cal = Calendar.getInstance();
/* 627 */     TimeZone tz = cal.getTimeZone();
/* 628 */     out.println("Timezone " + tz
/* 629 */         .getDisplayName() + " offset=" + tz
/*     */         
/* 631 */         .getOffset(cal.get(0), cal.get(1), cal
/* 632 */           .get(2), cal.get(5), cal
/* 633 */           .get(7), ((cal.get(11) * 60 + cal
/* 634 */           .get(12)) * 60 + cal
/* 635 */           .get(13)) * 1000 + cal
/* 636 */           .get(14)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void printProperty(PrintStream out, String key) {
/* 646 */     String value = getProperty(key);
/* 647 */     if (value != null) {
/* 648 */       out.print(key);
/* 649 */       out.print(" = ");
/* 650 */       out.print('"');
/* 651 */       out.print(value);
/* 652 */       out.println('"');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doReportProxy(PrintStream out) {
/* 663 */     printProperty(out, "http.proxyHost");
/* 664 */     printProperty(out, "http.proxyPort");
/* 665 */     printProperty(out, "http.proxyUser");
/* 666 */     printProperty(out, "http.proxyPassword");
/* 667 */     printProperty(out, "http.nonProxyHosts");
/* 668 */     printProperty(out, "https.proxyHost");
/* 669 */     printProperty(out, "https.proxyPort");
/* 670 */     printProperty(out, "https.nonProxyHosts");
/* 671 */     printProperty(out, "ftp.proxyHost");
/* 672 */     printProperty(out, "ftp.proxyPort");
/* 673 */     printProperty(out, "ftp.nonProxyHosts");
/* 674 */     printProperty(out, "socksProxyHost");
/* 675 */     printProperty(out, "socksProxyPort");
/* 676 */     printProperty(out, "java.net.socks.username");
/* 677 */     printProperty(out, "java.net.socks.password");
/*     */     
/* 679 */     printProperty(out, "java.net.useSystemProxies");
/* 680 */     ProxyDiagnostics proxyDiag = new ProxyDiagnostics();
/* 681 */     out.println("Java1.5+ proxy settings:");
/* 682 */     out.println(proxyDiag.toString());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Diagnostics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
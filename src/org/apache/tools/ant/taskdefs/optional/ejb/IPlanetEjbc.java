/*      */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Paths;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.stream.Stream;
/*      */ import javax.xml.parsers.SAXParser;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ import org.xml.sax.AttributeList;
/*      */ import org.xml.sax.HandlerBase;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
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
/*      */ public class IPlanetEjbc
/*      */ {
/*      */   private static final int MIN_NUM_ARGS = 2;
/*      */   private static final int MAX_NUM_ARGS = 8;
/*      */   private static final int NUM_CLASSES_WITH_IIOP = 15;
/*      */   private static final int NUM_CLASSES_WITHOUT_IIOP = 9;
/*      */   private static final String ENTITY_BEAN = "entity";
/*      */   private static final String STATELESS_SESSION = "stateless";
/*      */   private static final String STATEFUL_SESSION = "stateful";
/*      */   private File stdDescriptor;
/*      */   private File iasDescriptor;
/*      */   private File destDirectory;
/*      */   private String classpath;
/*      */   private String[] classpathElements;
/*      */   private boolean retainSource = false;
/*      */   private boolean debugOutput = false;
/*      */   private File iasHomeDir;
/*      */   private SAXParser parser;
/*  107 */   private EjbcHandler handler = new EjbcHandler();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  116 */   private Hashtable<String, File> ejbFiles = new Hashtable<>();
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
/*      */   private String displayName;
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
/*      */   public IPlanetEjbc(File stdDescriptor, File iasDescriptor, File destDirectory, String classpath, SAXParser parser) {
/*  142 */     this.stdDescriptor = stdDescriptor;
/*  143 */     this.iasDescriptor = iasDescriptor;
/*  144 */     this.destDirectory = destDirectory;
/*  145 */     this.classpath = classpath;
/*  146 */     this.parser = parser;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  152 */     if (classpath != null) {
/*  153 */       StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
/*      */       
/*  155 */       int count = st.countTokens();
/*  156 */       this.classpathElements = (String[])Collections.list(st).toArray((Object[])new String[count]);
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
/*      */   public void setRetainSource(boolean retainSource) {
/*  169 */     this.retainSource = retainSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDebugOutput(boolean debugOutput) {
/*  179 */     this.debugOutput = debugOutput;
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
/*      */   public void registerDTD(String publicID, String location) {
/*  192 */     this.handler.registerDTD(publicID, location);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIasHomeDir(File iasHomeDir) {
/*  203 */     this.iasHomeDir = iasHomeDir;
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
/*      */   public Hashtable<String, File> getEjbFiles() {
/*  216 */     return this.ejbFiles;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDisplayName() {
/*  225 */     return this.displayName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getCmpDescriptors() {
/*  234 */     return (String[])Stream.<EjbInfo>of(this.handler.getEjbs()).map(EjbInfo::getCmpDescriptors)
/*  235 */       .flatMap(Collection::stream).toArray(x$0 -> new String[x$0]);
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
/*      */   public static void main(String[] args) {
/*  248 */     File destDirectory = null;
/*  249 */     String classpath = null;
/*  250 */     SAXParser parser = null;
/*  251 */     boolean debug = false;
/*  252 */     boolean retainSource = false;
/*      */ 
/*      */     
/*  255 */     if (args.length < 2 || args.length > 8) {
/*  256 */       usage();
/*      */       
/*      */       return;
/*      */     } 
/*  260 */     File stdDescriptor = new File(args[args.length - 2]);
/*  261 */     File iasDescriptor = new File(args[args.length - 1]);
/*      */     
/*  263 */     for (int i = 0; i < args.length - 2; i++) {
/*  264 */       if ("-classpath".equals(args[i])) {
/*  265 */         classpath = args[++i];
/*  266 */       } else if ("-d".equals(args[i])) {
/*  267 */         destDirectory = new File(args[++i]);
/*  268 */       } else if ("-debug".equals(args[i])) {
/*  269 */         debug = true;
/*  270 */       } else if ("-keepsource".equals(args[i])) {
/*  271 */         retainSource = true;
/*      */       } else {
/*  273 */         usage();
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/*  279 */     if (classpath == null) {
/*  280 */       Properties props = System.getProperties();
/*  281 */       classpath = props.getProperty("java.class.path");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  288 */     if (destDirectory == null) {
/*  289 */       Properties props = System.getProperties();
/*  290 */       destDirectory = new File(props.getProperty("user.dir"));
/*      */     } 
/*      */ 
/*      */     
/*  294 */     SAXParserFactory parserFactory = SAXParserFactory.newInstance();
/*  295 */     parserFactory.setValidating(true);
/*      */     try {
/*  297 */       parser = parserFactory.newSAXParser();
/*  298 */     } catch (Exception e) {
/*      */       
/*  300 */       System.out.println("An exception was generated while trying to ");
/*  301 */       System.out.println("create a new SAXParser.");
/*  302 */       e.printStackTrace();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  307 */     IPlanetEjbc ejbc = new IPlanetEjbc(stdDescriptor, iasDescriptor, destDirectory, classpath, parser);
/*      */     
/*  309 */     ejbc.setDebugOutput(debug);
/*  310 */     ejbc.setRetainSource(retainSource);
/*      */ 
/*      */     
/*      */     try {
/*  314 */       ejbc.execute();
/*  315 */     } catch (IOException e) {
/*  316 */       System.out.println("An IOException has occurred while reading the XML descriptors (" + e
/*  317 */           .getMessage() + ").");
/*  318 */     } catch (SAXException e) {
/*  319 */       System.out.println("A SAXException has occurred while reading the XML descriptors (" + e
/*  320 */           .getMessage() + ").");
/*  321 */     } catch (EjbcException e) {
/*  322 */       System.out.println("An error has occurred while executing the ejbc utility (" + e
/*  323 */           .getMessage() + ").");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void usage() {
/*  331 */     System.out.println("java org.apache.tools.ant.taskdefs.optional.ejb.IPlanetEjbc \\");
/*  332 */     System.out.println("  [OPTIONS] [EJB 1.1 descriptor] [iAS EJB descriptor]");
/*  333 */     System.out.println();
/*  334 */     System.out.println("Where OPTIONS are:");
/*  335 */     System.out.println("  -debug -- for additional debugging output");
/*  336 */     System.out.println("  -keepsource -- to retain Java source files generated");
/*  337 */     System.out.println("  -classpath [classpath] -- classpath used for compilation");
/*  338 */     System.out.println("  -d [destination directory] -- directory for compiled classes");
/*  339 */     System.out.println();
/*  340 */     System.out.println("If a classpath is not specified, the system classpath");
/*  341 */     System.out.println("will be used.  If a destination directory is not specified,");
/*  342 */     System.out.println("the current working directory will be used (classes will");
/*  343 */     System.out.println("still be placed in subfolders which correspond to their");
/*  344 */     System.out.println("package name).");
/*  345 */     System.out.println();
/*  346 */     System.out.println("The EJB home interface, remote interface, and implementation");
/*  347 */     System.out.println("class must be found in the destination directory.  In");
/*  348 */     System.out.println("addition, the destination will look for the stubs and skeletons");
/*  349 */     System.out.println("in the destination directory to ensure they are up to date.");
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
/*      */   public void execute() throws EjbcException, IOException, SAXException {
/*  366 */     checkConfiguration();
/*      */     
/*  368 */     EjbInfo[] ejbs = getEjbs();
/*      */     
/*  370 */     for (EjbInfo ejb : ejbs) {
/*  371 */       log("EJBInfo...");
/*  372 */       log(ejb.toString());
/*      */     } 
/*      */     
/*  375 */     for (EjbInfo ejb : ejbs) {
/*  376 */       ejb.checkConfiguration(this.destDirectory);
/*      */       
/*  378 */       if (ejb.mustBeRecompiled(this.destDirectory)) {
/*  379 */         log(ejb.getName() + " must be recompiled using ejbc.");
/*  380 */         callEjbc(buildArgumentList(ejb));
/*      */       } else {
/*  382 */         log(ejb.getName() + " is up to date.");
/*      */       } 
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
/*      */   private void callEjbc(String[] arguments) {
/*  397 */     if (this.iasHomeDir == null) {
/*  398 */       command = "";
/*      */     } else {
/*  400 */       command = this.iasHomeDir.toString() + File.separator + "bin" + File.separator;
/*      */     } 
/*      */     
/*  403 */     String command = command + "ejbc ";
/*      */ 
/*      */     
/*  406 */     String args = String.join(" ", (CharSequence[])arguments);
/*      */     
/*  408 */     log(command + args);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  416 */       Process p = Runtime.getRuntime().exec(command + args);
/*  417 */       RedirectOutput output = new RedirectOutput(p.getInputStream());
/*  418 */       RedirectOutput error = new RedirectOutput(p.getErrorStream());
/*  419 */       output.start();
/*  420 */       error.start();
/*  421 */       p.waitFor();
/*  422 */       p.destroy();
/*  423 */     } catch (IOException e) {
/*  424 */       log("An IOException has occurred while trying to execute ejbc.");
/*  425 */       log(StringUtils.getStackTrace(e));
/*  426 */     } catch (InterruptedException interruptedException) {}
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
/*      */   protected void checkConfiguration() throws EjbcException {
/*  438 */     StringBuilder msg = new StringBuilder();
/*      */     
/*  440 */     if (this.stdDescriptor == null) {
/*  441 */       msg.append("A standard XML descriptor file must be specified.  ");
/*      */     }
/*  443 */     if (this.iasDescriptor == null) {
/*  444 */       msg.append("An iAS-specific XML descriptor file must be specified.  ");
/*      */     }
/*  446 */     if (this.classpath == null) {
/*  447 */       msg.append("A classpath must be specified.    ");
/*      */     }
/*  449 */     if (this.parser == null) {
/*  450 */       msg.append("An XML parser must be specified.    ");
/*      */     }
/*      */     
/*  453 */     if (this.destDirectory == null) {
/*  454 */       msg.append("A destination directory must be specified.  ");
/*  455 */     } else if (!this.destDirectory.exists()) {
/*  456 */       msg.append("The destination directory specified does not exist.  ");
/*  457 */     } else if (!this.destDirectory.isDirectory()) {
/*  458 */       msg.append("The destination specified is not a directory.  ");
/*      */     } 
/*      */     
/*  461 */     if (msg.length() > 0) {
/*  462 */       throw new EjbcException(msg.toString());
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
/*      */ 
/*      */ 
/*      */   
/*      */   private EjbInfo[] getEjbs() throws IOException, SAXException {
/*  483 */     this.parser.parse(this.stdDescriptor, this.handler);
/*  484 */     this.parser.parse(this.iasDescriptor, this.handler);
/*  485 */     return this.handler.getEjbs();
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
/*      */   private String[] buildArgumentList(EjbInfo ejb) {
/*  498 */     List<String> arguments = new ArrayList<>();
/*      */ 
/*      */ 
/*      */     
/*  502 */     if (this.debugOutput) {
/*  503 */       arguments.add("-debug");
/*      */     }
/*      */ 
/*      */     
/*  507 */     if (ejb.getBeantype().equals("stateless")) {
/*  508 */       arguments.add("-sl");
/*  509 */     } else if (ejb.getBeantype().equals("stateful")) {
/*  510 */       arguments.add("-sf");
/*      */     } 
/*      */     
/*  513 */     if (ejb.getIiop()) {
/*  514 */       arguments.add("-iiop");
/*      */     }
/*      */     
/*  517 */     if (ejb.getCmp()) {
/*  518 */       arguments.add("-cmp");
/*      */     }
/*      */     
/*  521 */     if (this.retainSource) {
/*  522 */       arguments.add("-gs");
/*      */     }
/*      */     
/*  525 */     if (ejb.getHasession()) {
/*  526 */       arguments.add("-fo");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  531 */     arguments.add("-classpath");
/*  532 */     arguments.add(this.classpath);
/*      */     
/*  534 */     arguments.add("-d");
/*  535 */     arguments.add(this.destDirectory.toString());
/*      */     
/*  537 */     arguments.add(ejb.getHome().getQualifiedClassName());
/*  538 */     arguments.add(ejb.getRemote().getQualifiedClassName());
/*  539 */     arguments.add(ejb.getImplementation().getQualifiedClassName());
/*      */ 
/*      */     
/*  542 */     return arguments.<String>toArray(new String[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void log(String msg) {
/*  552 */     if (this.debugOutput) {
/*  553 */       System.out.println(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class EjbcException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public EjbcException(String msg) {
/*  575 */       super(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class EjbcHandler
/*      */     extends HandlerBase
/*      */   {
/*      */     private static final String PUBLICID_EJB11 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String PUBLICID_IPLANET_EJB_60 = "-//Sun Microsystems, Inc.//DTD iAS Enterprise JavaBeans 1.0//EN";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String DEFAULT_IAS60_EJB11_DTD_LOCATION = "ejb-jar_1_1.dtd";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String DEFAULT_IAS60_DTD_LOCATION = "IASEjb_jar_1_0.dtd";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  608 */     private Map<String, String> resourceDtds = new HashMap<>();
/*  609 */     private Map<String, String> fileDtds = new HashMap<>();
/*      */     
/*  611 */     private Map<String, IPlanetEjbc.EjbInfo> ejbs = new HashMap<>();
/*      */     
/*      */     private IPlanetEjbc.EjbInfo currentEjb;
/*      */     private boolean iasDescriptor = false;
/*  615 */     private String currentLoc = "";
/*      */ 
/*      */     
/*      */     private String currentText;
/*      */ 
/*      */     
/*      */     private String ejbType;
/*      */ 
/*      */     
/*      */     public EjbcHandler() {
/*  625 */       registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "ejb-jar_1_1.dtd");
/*  626 */       registerDTD("-//Sun Microsystems, Inc.//DTD iAS Enterprise JavaBeans 1.0//EN", "IASEjb_jar_1_0.dtd");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public IPlanetEjbc.EjbInfo[] getEjbs() {
/*  637 */       return (IPlanetEjbc.EjbInfo[])this.ejbs.values().toArray((Object[])new IPlanetEjbc.EjbInfo[0]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getDisplayName() {
/*  648 */       return IPlanetEjbc.this.displayName;
/*      */     }
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
/*      */     public void registerDTD(String publicID, String location) {
/*  664 */       IPlanetEjbc.this.log("Registering: " + location);
/*  665 */       if (publicID == null || location == null) {
/*      */         return;
/*      */       }
/*      */       
/*  669 */       if (ClassLoader.getSystemResource(location) != null) {
/*  670 */         IPlanetEjbc.this.log("Found resource: " + location);
/*  671 */         this.resourceDtds.put(publicID, location);
/*      */       } else {
/*  673 */         File dtdFile = new File(location);
/*  674 */         if (dtdFile.exists() && dtdFile.isFile()) {
/*  675 */           IPlanetEjbc.this.log("Found file: " + location);
/*  676 */           this.fileDtds.put(publicID, location);
/*      */         } 
/*      */       } 
/*      */     }
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
/*      */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
/*  694 */       InputStream inputStream = null;
/*      */ 
/*      */       
/*      */       try {
/*  698 */         String location = this.resourceDtds.get(publicId);
/*  699 */         if (location != null) {
/*      */           
/*  701 */           inputStream = ClassLoader.getSystemResource(location).openStream();
/*      */         } else {
/*  703 */           location = this.fileDtds.get(publicId);
/*  704 */           if (location != null)
/*      */           {
/*  706 */             inputStream = Files.newInputStream(Paths.get(location, new String[0]), new java.nio.file.OpenOption[0]);
/*      */           }
/*      */         } 
/*  709 */       } catch (IOException iOException) {}
/*      */       
/*  711 */       if (inputStream == null) {
/*  712 */         return super.resolveEntity(publicId, systemId);
/*      */       }
/*  714 */       return new InputSource(inputStream);
/*      */     }
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
/*      */     public void startElement(String name, AttributeList atts) throws SAXException {
/*  733 */       this.currentLoc += "\\" + name;
/*      */ 
/*      */       
/*  736 */       this.currentText = "";
/*      */       
/*  738 */       if ("\\ejb-jar".equals(this.currentLoc)) {
/*  739 */         this.iasDescriptor = false;
/*  740 */       } else if ("\\ias-ejb-jar".equals(this.currentLoc)) {
/*  741 */         this.iasDescriptor = true;
/*      */       } 
/*      */       
/*  744 */       if ("session".equals(name) || "entity".equals(name)) {
/*  745 */         this.ejbType = name;
/*      */       }
/*      */     }
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
/*      */     public void characters(char[] ch, int start, int len) throws SAXException {
/*  761 */       this.currentText += (new String(ch)).substring(start, start + len);
/*      */     }
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
/*      */     public void endElement(String name) throws SAXException {
/*  779 */       if (this.iasDescriptor) {
/*  780 */         iasCharacters(this.currentText);
/*      */       } else {
/*  782 */         stdCharacters(this.currentText);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  790 */       int nameLength = name.length() + 1;
/*  791 */       int locLength = this.currentLoc.length();
/*      */       
/*  793 */       this.currentLoc = this.currentLoc.substring(0, locLength - nameLength);
/*      */     }
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
/*      */     private void stdCharacters(String value) {
/*  806 */       if ("\\ejb-jar\\display-name".equals(this.currentLoc)) {
/*  807 */         IPlanetEjbc.this.displayName = value;
/*      */         
/*      */         return;
/*      */       } 
/*  811 */       String base = "\\ejb-jar\\enterprise-beans\\" + this.ejbType;
/*      */       
/*  813 */       if ((base + "\\ejb-name").equals(this.currentLoc)) {
/*  814 */         this.currentEjb = this.ejbs.computeIfAbsent(value, x$0 -> new IPlanetEjbc.EjbInfo(x$0));
/*  815 */       } else if ((base + "\\home").equals(this.currentLoc)) {
/*  816 */         this.currentEjb.setHome(value);
/*  817 */       } else if ((base + "\\remote").equals(this.currentLoc)) {
/*  818 */         this.currentEjb.setRemote(value);
/*  819 */       } else if ((base + "\\ejb-class").equals(this.currentLoc)) {
/*  820 */         this.currentEjb.setImplementation(value);
/*  821 */       } else if ((base + "\\prim-key-class").equals(this.currentLoc)) {
/*  822 */         this.currentEjb.setPrimaryKey(value);
/*  823 */       } else if ((base + "\\session-type").equals(this.currentLoc)) {
/*  824 */         this.currentEjb.setBeantype(value);
/*  825 */       } else if ((base + "\\persistence-type").equals(this.currentLoc)) {
/*  826 */         this.currentEjb.setCmp(value);
/*      */       } 
/*      */     }
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
/*      */     private void iasCharacters(String value) {
/*  841 */       String base = "\\ias-ejb-jar\\enterprise-beans\\" + this.ejbType;
/*      */       
/*  843 */       if ((base + "\\ejb-name").equals(this.currentLoc)) {
/*  844 */         this.currentEjb = this.ejbs.computeIfAbsent(value, x$0 -> new IPlanetEjbc.EjbInfo(x$0));
/*  845 */       } else if ((base + "\\iiop").equals(this.currentLoc)) {
/*  846 */         this.currentEjb.setIiop(value);
/*  847 */       } else if ((base + "\\failover-required").equals(this.currentLoc)) {
/*  848 */         this.currentEjb.setHasession(value);
/*  849 */       } else if ((base + "\\persistence-manager\\properties-file-location")
/*  850 */         .equals(this.currentLoc)) {
/*  851 */         this.currentEjb.addCmpDescriptor(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class EjbInfo
/*      */   {
/*      */     private String name;
/*      */     
/*      */     private IPlanetEjbc.Classname home;
/*      */     
/*      */     private IPlanetEjbc.Classname remote;
/*      */     
/*      */     private IPlanetEjbc.Classname implementation;
/*      */     private IPlanetEjbc.Classname primaryKey;
/*  867 */     private String beantype = "entity";
/*      */     private boolean cmp = false;
/*      */     private boolean iiop = false;
/*      */     private boolean hasession = false;
/*  871 */     private List<String> cmpDescriptors = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public EjbInfo(String name) {
/*  879 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  890 */       if (this.name == null) {
/*  891 */         if (this.implementation == null) {
/*  892 */           return "[unnamed]";
/*      */         }
/*  894 */         return this.implementation.getClassName();
/*      */       } 
/*  896 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setHome(String home) {
/*  908 */       setHome(new IPlanetEjbc.Classname(home));
/*      */     }
/*      */     
/*      */     public void setHome(IPlanetEjbc.Classname home) {
/*  912 */       this.home = home;
/*      */     }
/*      */     
/*      */     public IPlanetEjbc.Classname getHome() {
/*  916 */       return this.home;
/*      */     }
/*      */     
/*      */     public void setRemote(String remote) {
/*  920 */       setRemote(new IPlanetEjbc.Classname(remote));
/*      */     }
/*      */     
/*      */     public void setRemote(IPlanetEjbc.Classname remote) {
/*  924 */       this.remote = remote;
/*      */     }
/*      */     
/*      */     public IPlanetEjbc.Classname getRemote() {
/*  928 */       return this.remote;
/*      */     }
/*      */     
/*      */     public void setImplementation(String implementation) {
/*  932 */       setImplementation(new IPlanetEjbc.Classname(implementation));
/*      */     }
/*      */     
/*      */     public void setImplementation(IPlanetEjbc.Classname implementation) {
/*  936 */       this.implementation = implementation;
/*      */     }
/*      */     
/*      */     public IPlanetEjbc.Classname getImplementation() {
/*  940 */       return this.implementation;
/*      */     }
/*      */     
/*      */     public void setPrimaryKey(String primaryKey) {
/*  944 */       setPrimaryKey(new IPlanetEjbc.Classname(primaryKey));
/*      */     }
/*      */     
/*      */     public void setPrimaryKey(IPlanetEjbc.Classname primaryKey) {
/*  948 */       this.primaryKey = primaryKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public IPlanetEjbc.Classname getPrimaryKey() {
/*  953 */       return this.primaryKey;
/*      */     }
/*      */     
/*      */     public void setBeantype(String beantype) {
/*  957 */       this.beantype = beantype.toLowerCase();
/*      */     }
/*      */     
/*      */     public String getBeantype() {
/*  961 */       return this.beantype;
/*      */     }
/*      */     
/*      */     public void setCmp(boolean cmp) {
/*  965 */       this.cmp = cmp;
/*      */     }
/*      */     
/*      */     public void setCmp(String cmp) {
/*  969 */       setCmp("Container".equals(cmp));
/*      */     }
/*      */     
/*      */     public boolean getCmp() {
/*  973 */       return this.cmp;
/*      */     }
/*      */     
/*      */     public void setIiop(boolean iiop) {
/*  977 */       this.iiop = iiop;
/*      */     }
/*      */     
/*      */     public void setIiop(String iiop) {
/*  981 */       setIiop(Boolean.parseBoolean(iiop));
/*      */     }
/*      */     
/*      */     public boolean getIiop() {
/*  985 */       return this.iiop;
/*      */     }
/*      */     
/*      */     public void setHasession(boolean hasession) {
/*  989 */       this.hasession = hasession;
/*      */     }
/*      */     
/*      */     public void setHasession(String hasession) {
/*  993 */       setHasession(Boolean.parseBoolean(hasession));
/*      */     }
/*      */     
/*      */     public boolean getHasession() {
/*  997 */       return this.hasession;
/*      */     }
/*      */     
/*      */     public void addCmpDescriptor(String descriptor) {
/* 1001 */       this.cmpDescriptors.add(descriptor);
/*      */     }
/*      */     
/*      */     public List<String> getCmpDescriptors() {
/* 1005 */       return this.cmpDescriptors;
/*      */     }
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
/*      */     private void checkConfiguration(File buildDir) throws IPlanetEjbc.EjbcException {
/* 1020 */       if (this.home == null) {
/* 1021 */         throw new IPlanetEjbc.EjbcException("A home interface was not found for the " + this.name + " EJB.");
/*      */       }
/*      */       
/* 1024 */       if (this.remote == null) {
/* 1025 */         throw new IPlanetEjbc.EjbcException("A remote interface was not found for the " + this.name + " EJB.");
/*      */       }
/*      */ 
/*      */       
/* 1029 */       if (this.implementation == null) {
/* 1030 */         throw new IPlanetEjbc.EjbcException("An EJB implementation class was not found for the " + this.name + " EJB.");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1035 */       if (!this.beantype.equals("entity") && 
/* 1036 */         !this.beantype.equals("stateless") && 
/* 1037 */         !this.beantype.equals("stateful")) {
/* 1038 */         throw new IPlanetEjbc.EjbcException("The beantype found (" + this.beantype + ") isn't valid in the " + this.name + " EJB.");
/*      */       }
/*      */ 
/*      */       
/* 1042 */       if (this.cmp && !this.beantype.equals("entity")) {
/* 1043 */         System.out.println("CMP stubs and skeletons may not be generated for a Session Bean -- the \"cmp\" attribute will be ignoredfor the " + this.name + " EJB.");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1048 */       if (this.hasession && !this.beantype.equals("stateful")) {
/* 1049 */         System.out.println("Highly available stubs and skeletons may only be generated for a Stateful Session Bean-- the \"hasession\" attribute will be ignored for the " + this.name + " EJB.");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1056 */       if (!this.remote.getClassFile(buildDir).exists()) {
/* 1057 */         throw new IPlanetEjbc.EjbcException("The remote interface " + this.remote
/* 1058 */             .getQualifiedClassName() + " could not be found.");
/*      */       }
/* 1060 */       if (!this.home.getClassFile(buildDir).exists()) {
/* 1061 */         throw new IPlanetEjbc.EjbcException("The home interface " + this.home
/* 1062 */             .getQualifiedClassName() + " could not be found.");
/*      */       }
/* 1064 */       if (!this.implementation.getClassFile(buildDir).exists()) {
/* 1065 */         throw new IPlanetEjbc.EjbcException("The EJB implementation class " + this.implementation
/* 1066 */             .getQualifiedClassName() + " could not be found.");
/*      */       }
/*      */     }
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
/*      */     public boolean mustBeRecompiled(File destDir) {
/* 1085 */       long sourceModified = sourceClassesModified(destDir);
/*      */       
/* 1087 */       long destModified = destClassesModified(destDir);
/*      */       
/* 1089 */       return (destModified < sourceModified);
/*      */     }
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
/*      */     private long sourceClassesModified(File buildDir) {
/* 1110 */       File pkFile, remoteFile = this.remote.getClassFile(buildDir);
/* 1111 */       long modified = remoteFile.lastModified();
/* 1112 */       if (modified == -1L) {
/* 1113 */         System.out.println("The class " + this.remote.getQualifiedClassName() + " couldn't be found on the classpath");
/*      */         
/* 1115 */         return -1L;
/*      */       } 
/* 1117 */       long latestModified = modified;
/*      */ 
/*      */       
/* 1120 */       File homeFile = this.home.getClassFile(buildDir);
/* 1121 */       modified = homeFile.lastModified();
/* 1122 */       if (modified == -1L) {
/* 1123 */         System.out.println("The class " + this.home.getQualifiedClassName() + " couldn't be found on the classpath");
/*      */         
/* 1125 */         return -1L;
/*      */       } 
/* 1127 */       latestModified = Math.max(latestModified, modified);
/*      */ 
/*      */       
/* 1130 */       if (this.primaryKey != null) {
/* 1131 */         pkFile = this.primaryKey.getClassFile(buildDir);
/* 1132 */         modified = pkFile.lastModified();
/* 1133 */         if (modified == -1L) {
/* 1134 */           System.out.println("The class " + this.primaryKey
/* 1135 */               .getQualifiedClassName() + "couldn't be found on the classpath");
/*      */           
/* 1137 */           return -1L;
/*      */         } 
/* 1139 */         latestModified = Math.max(latestModified, modified);
/*      */       } else {
/* 1141 */         pkFile = null;
/*      */       } 
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
/* 1153 */       File implFile = this.implementation.getClassFile(buildDir);
/* 1154 */       modified = implFile.lastModified();
/* 1155 */       if (modified == -1L) {
/* 1156 */         System.out.println("The class " + this.implementation
/* 1157 */             .getQualifiedClassName() + " couldn't be found on the classpath");
/*      */         
/* 1159 */         return -1L;
/*      */       } 
/*      */       
/* 1162 */       String pathToFile = this.remote.getQualifiedClassName();
/* 1163 */       pathToFile = pathToFile.replace('.', File.separatorChar) + ".class";
/* 1164 */       IPlanetEjbc.this.ejbFiles.put(pathToFile, remoteFile);
/*      */       
/* 1166 */       pathToFile = this.home.getQualifiedClassName();
/* 1167 */       pathToFile = pathToFile.replace('.', File.separatorChar) + ".class";
/* 1168 */       IPlanetEjbc.this.ejbFiles.put(pathToFile, homeFile);
/*      */       
/* 1170 */       pathToFile = this.implementation.getQualifiedClassName();
/* 1171 */       pathToFile = pathToFile.replace('.', File.separatorChar) + ".class";
/* 1172 */       IPlanetEjbc.this.ejbFiles.put(pathToFile, implFile);
/*      */       
/* 1174 */       if (pkFile != null) {
/* 1175 */         pathToFile = this.primaryKey.getQualifiedClassName();
/* 1176 */         pathToFile = pathToFile.replace('.', File.separatorChar) + ".class";
/* 1177 */         IPlanetEjbc.this.ejbFiles.put(pathToFile, pkFile);
/*      */       } 
/*      */       
/* 1180 */       return latestModified;
/*      */     }
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
/*      */     private long destClassesModified(File destDir) {
/* 1196 */       String[] classnames = classesToGenerate();
/* 1197 */       long destClassesModified = Instant.now().toEpochMilli();
/* 1198 */       boolean allClassesFound = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1204 */       for (String classname : classnames) {
/*      */         
/* 1206 */         String pathToClass = classname.replace('.', File.separatorChar) + ".class";
/* 1207 */         File classFile = new File(destDir, pathToClass);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1213 */         IPlanetEjbc.this.ejbFiles.put(pathToClass, classFile);
/*      */         
/* 1215 */         allClassesFound = (allClassesFound && classFile.exists());
/*      */         
/* 1217 */         if (allClassesFound) {
/* 1218 */           long fileMod = classFile.lastModified();
/*      */ 
/*      */           
/* 1221 */           destClassesModified = Math.min(destClassesModified, fileMod);
/*      */         } 
/*      */       } 
/*      */       
/* 1225 */       return allClassesFound ? destClassesModified : -1L;
/*      */     }
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
/*      */     private String[] classesToGenerate() {
/* 1241 */       String[] classnames = this.iiop ? new String[15] : new String[9];
/*      */       
/* 1243 */       String remotePkg = this.remote.getPackageName() + ".";
/* 1244 */       String remoteClass = this.remote.getClassName();
/* 1245 */       String homePkg = this.home.getPackageName() + ".";
/* 1246 */       String homeClass = this.home.getClassName();
/* 1247 */       String implPkg = this.implementation.getPackageName() + ".";
/* 1248 */       String implFullClass = this.implementation.getQualifiedWithUnderscores();
/* 1249 */       int index = 0;
/*      */       
/* 1251 */       classnames[index++] = implPkg + "ejb_fac_" + implFullClass;
/* 1252 */       classnames[index++] = implPkg + "ejb_home_" + implFullClass;
/* 1253 */       classnames[index++] = implPkg + "ejb_skel_" + implFullClass;
/* 1254 */       classnames[index++] = remotePkg + "ejb_kcp_skel_" + remoteClass;
/* 1255 */       classnames[index++] = homePkg + "ejb_kcp_skel_" + homeClass;
/* 1256 */       classnames[index++] = remotePkg + "ejb_kcp_stub_" + remoteClass;
/* 1257 */       classnames[index++] = homePkg + "ejb_kcp_stub_" + homeClass;
/* 1258 */       classnames[index++] = remotePkg + "ejb_stub_" + remoteClass;
/* 1259 */       classnames[index++] = homePkg + "ejb_stub_" + homeClass;
/*      */       
/* 1261 */       if (!this.iiop) {
/* 1262 */         return classnames;
/*      */       }
/*      */       
/* 1265 */       classnames[index++] = "org.omg.stub." + remotePkg + "_" + remoteClass + "_Stub";
/*      */       
/* 1267 */       classnames[index++] = "org.omg.stub." + homePkg + "_" + homeClass + "_Stub";
/*      */       
/* 1269 */       classnames[index++] = "org.omg.stub." + remotePkg + "_ejb_RmiCorbaBridge_" + remoteClass + "_Tie";
/*      */ 
/*      */       
/* 1272 */       classnames[index++] = "org.omg.stub." + homePkg + "_ejb_RmiCorbaBridge_" + homeClass + "_Tie";
/*      */ 
/*      */ 
/*      */       
/* 1276 */       classnames[index++] = remotePkg + "ejb_RmiCorbaBridge_" + remoteClass;
/*      */       
/* 1278 */       classnames[index++] = homePkg + "ejb_RmiCorbaBridge_" + homeClass;
/*      */       
/* 1280 */       return classnames;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1291 */       StringBuilder s = new StringBuilder("EJB name: " + this.name + "\n\r              home:      " + this.home + "\n\r              remote:    " + this.remote + "\n\r              impl:      " + this.implementation + "\n\r              primaryKey: " + this.primaryKey + "\n\r              beantype:  " + this.beantype + "\n\r              cmp:       " + this.cmp + "\n\r              iiop:      " + this.iiop + "\n\r              hasession: " + this.hasession);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1301 */       for (String cmpDescriptor : this.cmpDescriptors) {
/* 1302 */         s.append("\n\r              CMP Descriptor: ").append(cmpDescriptor);
/*      */       }
/*      */       
/* 1305 */       return s.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Classname
/*      */   {
/*      */     private String qualifiedName;
/*      */ 
/*      */ 
/*      */     
/*      */     private String packageName;
/*      */ 
/*      */ 
/*      */     
/*      */     private String className;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Classname(String qualifiedName) {
/* 1329 */       if (qualifiedName == null) {
/*      */         return;
/*      */       }
/*      */       
/* 1333 */       this.qualifiedName = qualifiedName;
/*      */       
/* 1335 */       int index = qualifiedName.lastIndexOf('.');
/* 1336 */       if (index == -1) {
/* 1337 */         this.className = qualifiedName;
/* 1338 */         this.packageName = "";
/*      */       } else {
/* 1340 */         this.packageName = qualifiedName.substring(0, index);
/* 1341 */         this.className = qualifiedName.substring(index + 1);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getQualifiedClassName() {
/* 1351 */       return this.qualifiedName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getPackageName() {
/* 1360 */       return this.packageName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getClassName() {
/* 1369 */       return this.className;
/*      */     }
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
/*      */     public String getQualifiedWithUnderscores() {
/* 1382 */       return this.qualifiedName.replace('.', '_');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public File getClassFile(File directory) {
/* 1394 */       String pathToFile = this.qualifiedName.replace('.', File.separatorChar) + ".class";
/*      */       
/* 1396 */       return new File(directory, pathToFile);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1407 */       return getQualifiedClassName();
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
/*      */   private static class RedirectOutput
/*      */     extends Thread
/*      */   {
/*      */     private InputStream stream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public RedirectOutput(InputStream stream) {
/* 1430 */       this.stream = stream;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/*      */       
/* 1439 */       try { BufferedReader reader = new BufferedReader(new InputStreamReader(this.stream));
/*      */         
/*      */         try { String text;
/* 1442 */           while ((text = reader.readLine()) != null) {
/* 1443 */             System.out.println(text);
/*      */           }
/* 1445 */           reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 1446 */       { e.printStackTrace(); }
/*      */     
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
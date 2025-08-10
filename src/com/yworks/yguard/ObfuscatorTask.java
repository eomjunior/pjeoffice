/*      */ package com.yworks.yguard;
/*      */ 
/*      */ import com.yworks.common.ShrinkBag;
/*      */ import com.yworks.common.ant.AttributesSection;
/*      */ import com.yworks.common.ant.EntryPointsSection;
/*      */ import com.yworks.common.ant.Exclude;
/*      */ import com.yworks.common.ant.InOutPair;
/*      */ import com.yworks.common.ant.TypePatternSet;
/*      */ import com.yworks.common.ant.YGuardBaseTask;
/*      */ import com.yworks.common.ant.ZipScannerTool;
/*      */ import com.yworks.util.CollectionFilter;
/*      */ import com.yworks.util.Filter;
/*      */ import com.yworks.util.Version;
/*      */ import com.yworks.yguard.ant.AttributesSection;
/*      */ import com.yworks.yguard.ant.ClassSection;
/*      */ import com.yworks.yguard.ant.ExposeSection;
/*      */ import com.yworks.yguard.ant.FieldSection;
/*      */ import com.yworks.yguard.ant.MapParser;
/*      */ import com.yworks.yguard.ant.Mappable;
/*      */ import com.yworks.yguard.ant.MethodSection;
/*      */ import com.yworks.yguard.ant.PackageSection;
/*      */ import com.yworks.yguard.obf.Cl;
/*      */ import com.yworks.yguard.obf.ClassTree;
/*      */ import com.yworks.yguard.obf.Filter;
/*      */ import com.yworks.yguard.obf.GuardDB;
/*      */ import com.yworks.yguard.obf.KeywordNameMaker;
/*      */ import com.yworks.yguard.obf.LineNumberTableMapper;
/*      */ import com.yworks.yguard.obf.NameMaker;
/*      */ import com.yworks.yguard.obf.NameMakerFactory;
/*      */ import com.yworks.yguard.obf.NoSuchMappingException;
/*      */ import com.yworks.yguard.obf.ResourceHandler;
/*      */ import com.yworks.yguard.obf.YGuardRule;
/*      */ import com.yworks.yguard.obf.classfile.LineNumberInfo;
/*      */ import com.yworks.yguard.obf.classfile.LineNumberTableAttrInfo;
/*      */ import com.yworks.yguard.obf.classfile.Logger;
/*      */ import com.yworks.yshrink.YShrinkInvoker;
/*      */ import com.yworks.yshrink.YShrinkModel;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.parsers.SAXParser;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.PatternSet;
/*      */ import org.apache.tools.ant.types.ZipFileSet;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.XMLReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObfuscatorTask
/*      */   extends YGuardBaseTask
/*      */ {
/*      */   private String mainClass;
/*      */   private boolean conserveManifest = false;
/*   98 */   private File logFile = new File("yguardlog.xml");
/*   99 */   protected ExposeSection expose = null;
/*  100 */   protected List adjustSections = new ArrayList();
/*  101 */   protected MapSection map = null;
/*  102 */   protected PatchSection patch = null;
/*      */ 
/*      */   
/*      */   private boolean doShrink = false;
/*      */   
/*  107 */   protected EntryPointsSection entryPoints = null;
/*  108 */   private File shrinkLog = null;
/*      */   
/*      */   private boolean useExposeAsEntryPoints = true;
/*      */   
/*      */   private static final String LOG_TITLE_PRE_VERSION = "  yGuard Bytecode Obfuscator, v";
/*      */   
/*      */   private static final String LOG_TITLE_POST_VERSION = ", a Product of yWorks GmbH - http://www.yworks.com";
/*      */   
/*      */   private static final String LOG_CREATED = "  Logfile created on ";
/*      */   
/*      */   private static final String LOG_INPUT_FILE = "  Jar file to be obfuscated:           ";
/*      */   
/*      */   private static final String LOG_OUTPUT_FILE = "  Target Jar file for obfuscated code: ";
/*      */   
/*      */   private static final String NO_SHRINKING_SUPPORT = "No shrinking support found.";
/*      */   
/*      */   private static final String DEPRECATED = "The obfuscate task is deprecated. Please use the new com.yworks.yguard.YGuardTask instead.";
/*      */   
/*      */   private boolean replaceClassNameStrings = true;
/*      */   
/*      */   private File[] tempJars;
/*      */   
/*      */   private boolean needYShrinkModel;
/*      */   
/*      */   private YShrinkModel yShrinkModel;
/*      */   
/*      */   private String annotationClass;
/*      */ 
/*      */   
/*      */   public ObfuscatorTask() {}
/*      */   
/*      */   public ObfuscatorTask(boolean mode) {
/*  140 */     super(mode);
/*      */   }
/*      */   
/*      */   private static String toNativePattern(String pattern) {
/*  144 */     if (pattern.endsWith(".class")) {
/*  145 */       return pattern;
/*      */     }
/*  147 */     if (pattern.endsWith("**"))
/*  148 */       return pattern.replace('.', '/') + "/*.class"; 
/*  149 */     if (pattern.endsWith("*"))
/*  150 */       return pattern.replace('.', '/') + ".class"; 
/*  151 */     if (pattern.endsWith(".")) {
/*  152 */       return pattern.replace('.', '/') + "**/*.class";
/*      */     }
/*  154 */     return pattern.replace('.', '/') + ".class";
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
/*      */   public static String[] toNativePattern(String[] patterns) {
/*  166 */     if (patterns == null) {
/*  167 */       return new String[0];
/*      */     }
/*  169 */     String[] res = new String[patterns.length];
/*  170 */     for (int i = 0; i < patterns.length; i++) {
/*  171 */       res[i] = toNativePattern(patterns[i]);
/*      */     }
/*  173 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String toNativeClass(String className) {
/*  184 */     return className.replace('.', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String[] toNativeMethod(String javaMethod) {
/*  194 */     StringTokenizer tokenizer = new StringTokenizer(javaMethod, "(,[]) ", true);
/*  195 */     String tmp = tokenizer.nextToken();
/*  196 */     while (tmp.trim().length() == 0) {
/*  197 */       tmp = tokenizer.nextToken();
/*      */     }
/*  199 */     String returnType = tmp;
/*  200 */     tmp = tokenizer.nextToken();
/*  201 */     int retarraydim = 0;
/*  202 */     while (tmp.equals("[")) {
/*  203 */       tmp = tokenizer.nextToken();
/*  204 */       if (!tmp.equals("]")) throw new IllegalArgumentException("']' expected but found " + tmp); 
/*  205 */       retarraydim++;
/*  206 */       tmp = tokenizer.nextToken();
/*      */     } 
/*  208 */     if (tmp.trim().length() != 0) {
/*  209 */       throw new IllegalArgumentException("space expected but found " + tmp);
/*      */     }
/*  211 */     tmp = tokenizer.nextToken();
/*  212 */     while (tmp.trim().length() == 0) {
/*  213 */       tmp = tokenizer.nextToken();
/*      */     }
/*  215 */     String name = tmp;
/*  216 */     StringBuffer nativeMethod = new StringBuffer(30);
/*  217 */     nativeMethod.append('(');
/*  218 */     tmp = tokenizer.nextToken();
/*  219 */     while (tmp.trim().length() == 0) {
/*  220 */       tmp = tokenizer.nextToken();
/*      */     }
/*  222 */     if (!tmp.equals("(")) throw new IllegalArgumentException("'(' expected but found " + tmp); 
/*  223 */     tmp = tokenizer.nextToken();
/*  224 */     while (!tmp.equals(")")) {
/*  225 */       while (tmp.trim().length() == 0) {
/*  226 */         tmp = tokenizer.nextToken();
/*      */       }
/*  228 */       String type = tmp;
/*  229 */       tmp = tokenizer.nextToken();
/*  230 */       while (tmp.trim().length() == 0) {
/*  231 */         tmp = tokenizer.nextToken();
/*      */       }
/*  233 */       int arraydim = 0;
/*  234 */       while (tmp.equals("[")) {
/*  235 */         tmp = tokenizer.nextToken();
/*  236 */         if (!tmp.equals("]")) throw new IllegalArgumentException("']' expected but found " + tmp); 
/*  237 */         arraydim++;
/*  238 */         tmp = tokenizer.nextToken();
/*      */       } 
/*  240 */       while (tmp.trim().length() == 0) {
/*  241 */         tmp = tokenizer.nextToken();
/*      */       }
/*      */       
/*  244 */       nativeMethod.append(toNativeType(type, arraydim));
/*  245 */       if (tmp.equals(",")) {
/*  246 */         tmp = tokenizer.nextToken();
/*  247 */         while (tmp.trim().length() == 0) {
/*  248 */           tmp = tokenizer.nextToken();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  253 */     nativeMethod.append(')');
/*  254 */     nativeMethod.append(toNativeType(returnType, retarraydim));
/*  255 */     String[] result = { name, nativeMethod.toString() };
/*  256 */     return result;
/*      */   }
/*      */   
/*      */   private static final String toNativeType(String type, int arraydim) {
/*  260 */     StringBuffer nat = new StringBuffer(30);
/*  261 */     for (int i = 0; i < arraydim; i++) {
/*  262 */       nat.append('[');
/*      */     }
/*  264 */     if ("byte".equals(type)) {
/*  265 */       nat.append('B');
/*  266 */     } else if ("char".equals(type)) {
/*  267 */       nat.append('C');
/*  268 */     } else if ("double".equals(type)) {
/*  269 */       nat.append('D');
/*  270 */     } else if ("float".equals(type)) {
/*  271 */       nat.append('F');
/*  272 */     } else if ("int".equals(type)) {
/*  273 */       nat.append('I');
/*  274 */     } else if ("long".equals(type)) {
/*  275 */       nat.append('J');
/*  276 */     } else if ("short".equals(type)) {
/*  277 */       nat.append('S');
/*  278 */     } else if ("boolean".equals(type)) {
/*  279 */       nat.append('Z');
/*  280 */     } else if ("void".equals(type)) {
/*  281 */       nat.append('V');
/*      */     } else {
/*  283 */       nat.append('L');
/*  284 */       nat.append(type.replace('.', '/'));
/*  285 */       nat.append(';');
/*      */     } 
/*  287 */     return nat.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNeedYShrinkModel(boolean b) {
/*  296 */     this.needYShrinkModel = b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final class PatchSection
/*      */   {
/*  303 */     private List patches = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addConfiguredClass(ClassSection cs) {
/*  311 */       this.patches.add(cs);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Collection createEntries(Collection srcJars) throws IOException {
/*  322 */       Collection entries = new ArrayList(20);
/*  323 */       for (Iterator<File> it = srcJars.iterator(); it.hasNext(); ) {
/*      */         
/*  325 */         File file = it.next();
/*  326 */         ZipFileSet zipFile = new ZipFileSet();
/*  327 */         zipFile.setProject(ObfuscatorTask.this.getProject());
/*  328 */         zipFile.setSrc(file);
/*  329 */         for (Iterator<ClassSection> it2 = this.patches.iterator(); it2.hasNext(); ) {
/*  330 */           ClassSection cs = it2.next();
/*  331 */           if (cs.getName() == null) {
/*  332 */             cs.addEntries(entries, zipFile); continue;
/*      */           } 
/*  334 */           cs.addEntries(entries, cs.getName());
/*      */         } 
/*      */       } 
/*      */       
/*  338 */       return entries;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Modifiers
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/*  366 */       return new String[] { "public", "protected", "friendly", "private", "none" };
/*      */     }
/*      */     
/*      */     private int myGetIndex() {
/*  370 */       String[] values = getValues();
/*  371 */       for (int i = 0; i < values.length; i++) {
/*  372 */         if (getValue().equals(values[i])) {
/*  373 */           return i;
/*      */         }
/*      */       } 
/*  376 */       return -1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getModifierValue() {
/*  385 */       switch (myGetIndex())
/*      */       { default:
/*  387 */           return 0;
/*      */         case 0:
/*  389 */           return 1;
/*      */         case 1:
/*  391 */           return 5;
/*      */         case 2:
/*  393 */           return 4101;
/*      */         case 3:
/*  395 */           return 4103;
/*      */         case 4:
/*  397 */           break; }  return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final class MapSection
/*      */   {
/*      */     private File logFile;
/*      */ 
/*      */     
/*  408 */     private List mappables = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addConfiguredPackage(PackageSection ps) {
/*  416 */       this.mappables.add(ps);
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
/*      */     public void addConfiguredClass(ClassSection ps) {
/*  431 */       this.mappables.add(ps);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addConfiguredField(FieldSection ps) {
/*  440 */       this.mappables.add(ps);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addConfiguredMethod(MethodSection ps) {
/*  449 */       this.mappables.add(ps);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setLogFile(File logFile) {
/*  458 */       this.logFile = logFile;
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
/*      */     Collection createEntries(Project antproject, PrintWriter log) throws BuildException {
/*      */       Collection res;
/*  471 */       if (this.logFile != null) {
/*      */         try {
/*  473 */           Reader reader; SAXParserFactory f = SAXParserFactory.newInstance();
/*  474 */           f.setValidating(false);
/*  475 */           SAXParser parser = f.newSAXParser();
/*  476 */           XMLReader r = parser.getXMLReader();
/*  477 */           MapParser mp = new MapParser(ObfuscatorTask.this);
/*  478 */           r.setContentHandler((ContentHandler)mp);
/*      */           
/*  480 */           if (this.logFile.getName().endsWith(".gz")) {
/*  481 */             reader = new InputStreamReader(new GZIPInputStream(new FileInputStream(this.logFile)));
/*      */           } else {
/*  483 */             reader = new FileReader(this.logFile);
/*      */           } 
/*  485 */           InputSource source = new InputSource(reader);
/*  486 */           antproject.log("Parsing logfile's " + this.logFile.getName() + " map elements...", 2);
/*  487 */           r.parse(source);
/*  488 */           reader.close();
/*  489 */           r = null;
/*  490 */           f = null;
/*  491 */           parser = null;
/*  492 */           res = mp.getEntries();
/*  493 */         } catch (ParserConfigurationException pxe) {
/*  494 */           throw new BuildException("Could configure xml parser!", pxe);
/*  495 */         } catch (SAXException pxe) {
/*  496 */           throw new BuildException("Error parsing xml logfile!" + pxe, pxe);
/*  497 */         } catch (IOException ioe) {
/*  498 */           throw new BuildException("Could not parse map from logfile!", ioe);
/*      */         } 
/*      */       } else {
/*  501 */         res = new ArrayList(this.mappables.size());
/*      */       } 
/*  503 */       for (Iterator<Mappable> it = this.mappables.iterator(); it.hasNext(); ) {
/*  504 */         Mappable m = it.next();
/*  505 */         m.addMapEntries(res);
/*      */       } 
/*  507 */       return res;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class AdjustSection
/*      */     extends ZipFileSet
/*      */   {
/*      */     private boolean replaceName = false;
/*      */     
/*      */     private boolean replaceContent = false;
/*  518 */     private String replaceContentSeparator = "/";
/*      */ 
/*      */     
/*      */     private boolean replacePath = true;
/*      */ 
/*      */     
/*      */     private Set entries;
/*      */ 
/*      */     
/*      */     public AdjustSection() {
/*  528 */       setProject(ObfuscatorTask.this.getProject());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(String name) {
/*  539 */       return this.entries.contains(name);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setReplaceContent(boolean rc) {
/*  548 */       this.replaceContent = rc;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getReplaceContent() {
/*  558 */       return this.replaceContent;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setReplaceContentSeparator(String separator) {
/*  567 */       this.replaceContentSeparator = separator;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getReplaceContentSeparator() {
/*  576 */       return this.replaceContentSeparator;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setReplacePath(boolean rp) {
/*  585 */       this.replacePath = rp;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getReplacePath() {
/*  595 */       return this.replacePath;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getReplaceName() {
/*  605 */       return this.replaceName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setReplaceName(boolean rn) {
/*  614 */       this.replaceName = rn;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void createEntries(Collection srcJars) throws IOException {
/*  625 */       this.entries = new HashSet();
/*  626 */       for (Iterator<File> iter = srcJars.iterator(); iter.hasNext(); ) {
/*      */         
/*  628 */         File file = iter.next();
/*  629 */         setSrc(file);
/*      */         
/*  631 */         DirectoryScanner scanner = getDirectoryScanner(getProject());
/*  632 */         String[] includedFiles = ZipScannerTool.getMatches(this, scanner);
/*      */         
/*  634 */         for (int i = 0; i < includedFiles.length; i++)
/*      */         {
/*  636 */           this.entries.add(includedFiles[i]);
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ExposeSection createExpose() {
/*  656 */     if (this.expose != null) {
/*  657 */       throw new IllegalArgumentException("Only one expose element allowed!");
/*      */     }
/*  659 */     this.expose = newExposeSection(this);
/*  660 */     return this.expose;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ExposeSection newExposeSection(ObfuscatorTask ot) {
/*  670 */     return new ExposeSection(ot);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExcludes(EntryPointsSection entryPoints) {
/*  679 */     if (null == this.expose) {
/*  680 */       createExpose();
/*      */     }
/*      */   }
/*      */   
/*      */   public Exclude createKeep() {
/*  685 */     return (Exclude)createExpose();
/*      */   }
/*      */   
/*      */   public void addAttributesSections(List<AttributesSection> attributesSections) {
/*  689 */     if (null != this.expose) {
/*  690 */       List<AttributesSection> attributes = this.expose.getAttributes();
/*  691 */       for (AttributesSection attributesSection : attributesSections) {
/*  692 */         AttributesSection asYGuard = new AttributesSection(this);
/*  693 */         PatternSet patternSet = attributesSection.getPatternSet(TypePatternSet.Type.NAME);
/*  694 */         if (patternSet != null) {
/*  695 */           asYGuard.addConfiguredPatternSet(patternSet);
/*      */         }
/*  697 */         asYGuard.setName(attributesSection.getAttributesStr());
/*  698 */         attributes.add(asYGuard);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AdjustSection createAdjust() {
/*  709 */     AdjustSection adjust = newAdjustSection();
/*  710 */     adjust.setProject(getProject());
/*  711 */     this.adjustSections.add(adjust);
/*  712 */     return adjust;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AdjustSection newAdjustSection() {
/*  722 */     return new AdjustSection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredExpose(ExposeSection ex) {
/*  731 */     if (this.expose != null) {
/*  732 */       throw new IllegalArgumentException("Only one expose element allowed!");
/*      */     }
/*  734 */     this.expose = ex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntryPointsSection createEntryPoints() {
/*  743 */     return newEntryPointsSection(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected EntryPointsSection newEntryPointsSection(YGuardBaseTask bt) {
/*  753 */     return new EntryPointsSection(bt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredEntryPoints(EntryPointsSection eps) {
/*  762 */     if (this.entryPoints != null) {
/*  763 */       throw new IllegalArgumentException("Only one entrypoints element allowed!");
/*      */     }
/*  765 */     this.entryPoints = eps;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapSection createMap() {
/*  774 */     if (this.map != null) {
/*  775 */       throw new IllegalArgumentException("Only one map element allowed!");
/*      */     }
/*  777 */     this.map = newMapSection();
/*  778 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MapSection newMapSection() {
/*  788 */     return new MapSection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredMap(MapSection map) {
/*  797 */     if (this.map != null) {
/*  798 */       throw new IllegalArgumentException("Only one map element allowed!");
/*      */     }
/*  800 */     this.map = map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PatchSection createPatch() {
/*  809 */     if (this.patch != null) {
/*  810 */       throw new IllegalArgumentException("Only one patch element allowed!");
/*      */     }
/*  812 */     this.patch = newPatchSection();
/*  813 */     return this.patch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PatchSection newPatchSection() {
/*  823 */     return new PatchSection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredPatch(PatchSection patch) {
/*  832 */     if (this.patch != null) {
/*  833 */       throw new IllegalArgumentException("Only one patch element allowed!");
/*      */     }
/*  835 */     this.patch = patch;
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
/*      */   public void setLogFile(File file) {
/*  852 */     this.logFile = file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConserveManifest(boolean c) {
/*  861 */     this.conserveManifest = c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMainClass(String mainClass) {
/*  870 */     this.mainClass = mainClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  878 */     getProject().log((Task)this, "yGuard Obfuscator v" + Version.getVersion() + " - http://www.yworks.com/products/yguard", 2);
/*      */ 
/*      */     
/*  881 */     if (!this.mode) {
/*  882 */       getProject().log((Task)this, "The obfuscate task is deprecated. Please use the new com.yworks.yguard.YGuardTask instead.", 1);
/*      */     }
/*      */     
/*  885 */     TaskLogger taskLogger = new TaskLogger();
/*      */     
/*  887 */     if (this.mode) {
/*  888 */       this.doShrink = false;
/*      */     }
/*      */     
/*  891 */     if (this.doShrink) doShrink();
/*      */     
/*  893 */     ResourceCpResolver resolver = null;
/*  894 */     if (this.resourceClassPath != null) {
/*  895 */       resolver = new ResourceCpResolver(this.resourceClassPath, (Task)this);
/*  896 */       Cl.setClassResolver(resolver);
/*      */     } 
/*      */     
/*  899 */     YGuardNameFactory nameFactory = null;
/*      */     
/*  901 */     if (this.properties.containsKey("naming-scheme") || this.properties
/*  902 */       .containsKey("language-conformity") || this.properties
/*  903 */       .containsKey("overload-enabled")) {
/*  904 */       String ns = (String)this.properties.get("naming-scheme");
/*  905 */       String lc = (String)this.properties.get("language-conformity");
/*      */       
/*  907 */       int ilc = 0;
/*  908 */       int ins = 4;
/*      */       
/*  910 */       if ("compatible".equalsIgnoreCase(lc)) {
/*  911 */         ilc = 1;
/*  912 */       } else if ("illegal".equalsIgnoreCase(lc)) {
/*  913 */         ilc = 2;
/*      */       } 
/*  915 */       if ("mix".equalsIgnoreCase(ns)) {
/*  916 */         ins = 12;
/*      */       }
/*  918 */       if ("best".equalsIgnoreCase(ns)) {
/*  919 */         ins = 8;
/*      */       }
/*  921 */       nameFactory = new YGuardNameFactory(ilc | ins);
/*      */       
/*  923 */       nameFactory.setPackagePrefix((String)this.properties.get("obfuscation-prefix"));
/*      */     } else {
/*  925 */       nameFactory = new YGuardNameFactory(4);
/*  926 */       nameFactory.setPackagePrefix((String)this.properties.get("obfuscation-prefix"));
/*      */     } 
/*      */     
/*  929 */     if (this.properties.containsKey("overload-enabled")) {
/*  930 */       String overload = (String)this.properties.get("overload-enabled");
/*  931 */       boolean overloadEnabled = true;
/*  932 */       if ("false".equalsIgnoreCase(overload) || "no".equalsIgnoreCase("overload")) {
/*  933 */         overloadEnabled = false;
/*      */       }
/*  935 */       nameFactory.setOverloadEnabled(overloadEnabled);
/*      */     } 
/*      */     
/*  938 */     boolean pedantic = false;
/*  939 */     if (this.properties.containsKey("error-checking")) {
/*  940 */       String ed = (String)this.properties.get("error-checking");
/*  941 */       if ("pedantic".equalsIgnoreCase(ed)) {
/*  942 */         pedantic = true;
/*      */       }
/*      */     } 
/*  945 */     getProject().log((Task)this, "Using NameMakerFactory: " + NameMakerFactory.getInstance(), 3);
/*      */     
/*  947 */     if (this.pairs == null) {
/*  948 */       throw new BuildException("No in out pairs specified!");
/*      */     }
/*  950 */     Collection<File> inFilesList = new ArrayList(this.pairs.size());
/*  951 */     File[] inFiles = new File[this.pairs.size()];
/*  952 */     File[] outFiles = new File[this.pairs.size()];
/*  953 */     for (int i = 0; i < this.pairs.size(); i++) {
/*      */       
/*  955 */       InOutPair pair = this.pairs.get(i);
/*  956 */       if (pair.getIn() == null || !pair.getIn().canRead()) {
/*  957 */         throw new BuildException("Cannot open inoutpair.in " + pair.getIn());
/*      */       }
/*  959 */       inFiles[i] = pair.getIn();
/*  960 */       inFilesList.add(pair.getIn());
/*  961 */       if (pair.getOut() == null) {
/*  962 */         throw new BuildException("Must specify inoutpair.out!");
/*      */       }
/*  964 */       outFiles[i] = pair.getOut();
/*      */     } 
/*  966 */     PrintWriter log = null;
/*  967 */     if (this.logFile != null) {
/*      */       
/*      */       try {
/*  970 */         if (this.logFile.getName().endsWith(".gz")) {
/*  971 */           log = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(this.logFile)))));
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  981 */           log = new PrintWriter(new BufferedWriter(new FileWriter(this.logFile)));
/*      */         } 
/*  983 */         taskLogger.setWriter(log);
/*  984 */       } catch (IOException ioe) {
/*  985 */         getProject().log((Task)this, "Could not create logfile: " + ioe, 0);
/*  986 */         log = new PrintWriter(System.out);
/*      */       } 
/*      */     } else {
/*  989 */       log = new PrintWriter(System.out);
/*      */     } 
/*  991 */     writeLogHeader(log, inFiles, outFiles);
/*      */     try {
/*  993 */       Collection<YGuardRule> rules = null;
/*  994 */       if (this.expose != null) {
/*  995 */         rules = this.expose.createEntries(inFilesList);
/*      */       } else {
/*  997 */         rules = new ArrayList(20);
/*      */       } 
/*      */       
/* 1000 */       if (this.mainClass != null) {
/*      */         
/* 1002 */         String cn = toNativeClass(this.mainClass);
/* 1003 */         rules.add(new YGuardRule(1, cn));
/* 1004 */         rules.add(new YGuardRule(3, cn + "/main", "([Ljava/lang/String;)V"));
/*      */       } 
/* 1006 */       if (this.map != null) {
/* 1007 */         Collection<? extends YGuardRule> mapEntries = this.map.createEntries(getProject(), log);
/* 1008 */         rules.addAll(mapEntries);
/*      */       } 
/*      */       
/* 1011 */       for (Iterator<AdjustSection> iter = this.adjustSections.iterator(); iter.hasNext(); ) {
/*      */         
/* 1013 */         AdjustSection as = iter.next();
/* 1014 */         as.createEntries(inFilesList);
/*      */       } 
/*      */       
/* 1017 */       if (this.properties.containsKey("expose-attributes")) {
/* 1018 */         StringTokenizer st = new StringTokenizer((String)this.properties.get("expose-attributes"), ",", false);
/* 1019 */         while (st.hasMoreTokens()) {
/* 1020 */           String attribute = st.nextToken().trim();
/* 1021 */           rules.add(new YGuardRule(0, attribute));
/* 1022 */           getProject().log((Task)this, "Exposing attribute '" + attribute + "'", 3);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 1028 */         LogListener listener = new LogListener(getProject());
/* 1029 */         Filter filter = null;
/* 1030 */         if (this.patch != null) {
/* 1031 */           getProject().log((Task)this, "Patching...", 2);
/* 1032 */           Collection patchfiles = this.patch.createEntries(inFilesList);
/*      */           
/* 1034 */           Set<String> names = new HashSet();
/* 1035 */           for (Iterator<YGuardRule> iterator = patchfiles.iterator(); iterator.hasNext(); ) {
/* 1036 */             YGuardRule entry = iterator.next();
/* 1037 */             if (entry.type == 1) {
/* 1038 */               names.add(entry.name + ".class");
/*      */             }
/*      */           } 
/* 1041 */           filter = new ClassFileFilter((Filter)new CollectionFilter(names));
/*      */         } 
/* 1043 */         GuardDB db = newGuardDB(inFiles);
/*      */         
/* 1045 */         if (this.properties.containsKey("digests")) {
/* 1046 */           String digests = (String)this.properties.get("digests");
/* 1047 */           if (digests.trim().equalsIgnoreCase("none")) {
/* 1048 */             db.setDigests(new String[0]);
/*      */           } else {
/* 1050 */             db.setDigests(digests.split("\\s*,\\s*"));
/*      */           } 
/*      */         } 
/*      */         
/* 1054 */         if (this.annotationClass != null) db.setAnnotationClass(toNativeClass(this.annotationClass));
/*      */         
/* 1056 */         db.setResourceHandler(newResourceAdjuster(db));
/* 1057 */         db.setPedantic(pedantic);
/* 1058 */         db.setReplaceClassNameStrings(this.replaceClassNameStrings);
/* 1059 */         db.addListener(listener);
/* 1060 */         db.retain(rules, log);
/* 1061 */         db.remapTo(outFiles, filter, log, this.conserveManifest);
/*      */         
/* 1063 */         for (Iterator<YGuardRule> it = rules.iterator(); it.hasNext();) {
/* 1064 */           ((YGuardRule)it.next()).logProperties(log);
/*      */         }
/*      */         
/* 1067 */         db.close();
/* 1068 */         Cl.setClassResolver(null);
/*      */         
/* 1070 */         if (this.doShrink) {
/* 1071 */           for (int j = 0; j < this.tempJars.length; j++) {
/* 1072 */             if (null != this.tempJars[j]) {
/* 1073 */               this.tempJars[j].delete();
/*      */             }
/*      */           } 
/*      */         }
/*      */         
/* 1078 */         if (!Logger.getInstance().isAllResolved()) {
/* 1079 */           Logger.getInstance().warning("Not all dependencies could be resolved. Please see the logfile for details.");
/*      */         }
/*      */       }
/* 1082 */       catch (NoSuchMappingException nsm) {
/* 1083 */         throw new BuildException("yGuard was unable to determine the mapped name for " + nsm.getKey() + ".\n Probably broken code. Try recompiling from source!", nsm);
/* 1084 */       } catch (ClassNotFoundException cnfe) {
/* 1085 */         throw new BuildException("yGuard was unable to resolve a class (" + cnfe + ").\n Probably a missing external dependency.", cnfe);
/* 1086 */       } catch (IOException ioe) {
/* 1087 */         if (ioe.getMessage() != null) {
/* 1088 */           getProject().log((Task)this, ioe.getMessage(), 0);
/*      */         }
/* 1090 */         throw new BuildException("yGuard encountered an IO problem!", ioe);
/* 1091 */       } catch (ParseException pe) {
/* 1092 */         throw new BuildException("yGuard encountered problems during parsing!", pe);
/* 1093 */       } catch (RuntimeException rte) {
/* 1094 */         if (rte.getMessage() != null) {
/* 1095 */           getProject().log((Task)this, rte.getMessage(), 0);
/*      */         }
/* 1097 */         rte.printStackTrace();
/* 1098 */         throw new BuildException("yGuard encountered an unknown problem!", rte);
/*      */       } finally {
/* 1100 */         writeLogFooter(log);
/* 1101 */         log.flush();
/* 1102 */         log.close();
/*      */       } 
/* 1104 */     } catch (IOException ioe) {
/* 1105 */       throw new BuildException("yGuard encountered an IO problem!", ioe);
/*      */     } finally {
/*      */       try {
/* 1108 */         if (resolver != null) {
/* 1109 */           resolver.close();
/*      */         }
/* 1111 */       } catch (Exception exception) {}
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
/*      */   protected GuardDB newGuardDB(File[] inFile) throws IOException {
/* 1124 */     return new GuardDB(inFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResourceAdjuster newResourceAdjuster(GuardDB db) {
/* 1134 */     return new ResourceAdjuster(db);
/*      */   }
/*      */   
/*      */   private void doShrink() {
/* 1138 */     YShrinkInvoker yShrinkInvoker = null;
/*      */     
/*      */     try {
/* 1141 */       yShrinkInvoker = (YShrinkInvoker)Class.forName("com.yworks.yshrink.YShrinkInvokerImpl").newInstance();
/* 1142 */     } catch (InstantiationException e) {
/* 1143 */       throw new BuildException("No shrinking support found.", e);
/* 1144 */     } catch (IllegalAccessException e) {
/* 1145 */       throw new BuildException("No shrinking support found.", e);
/* 1146 */     } catch (ClassNotFoundException e) {
/* 1147 */       throw new BuildException("No shrinking support found.", e);
/*      */     } 
/*      */     
/* 1150 */     if (null == yShrinkInvoker)
/*      */       return; 
/* 1152 */     yShrinkInvoker.setContext((Task)this);
/*      */     
/* 1154 */     this.tempJars = new File[this.pairs.size()];
/* 1155 */     File[] outJars = new File[this.pairs.size()];
/*      */     int i;
/* 1157 */     for (i = 0; i < this.tempJars.length; i++) {
/*      */       try {
/* 1159 */         this.tempJars[i] = File.createTempFile("tempJar_", "_shrinked.jar", new File(((InOutPair)this.pairs.get(i)).getOut().getParent()));
/* 1160 */       } catch (IOException e) {
/* 1161 */         getProject().log("Could not create tempfile for shrinking " + this.tempJars[i] + ".", 0);
/* 1162 */         this.tempJars[i] = null;
/*      */       } 
/*      */       
/* 1165 */       if (null != this.tempJars[i]) {
/* 1166 */         System.out.println("temp-jar: " + this.tempJars[i]);
/* 1167 */         ShrinkBag pair = this.pairs.get(i);
/* 1168 */         outJars[i] = pair.getOut();
/* 1169 */         pair.setOut(this.tempJars[i]);
/* 1170 */         yShrinkInvoker.addPair(pair);
/*      */       } 
/*      */     } 
/*      */     
/* 1174 */     yShrinkInvoker.setResourceClassPath(this.resourceClassPath);
/*      */     
/* 1176 */     if (this.shrinkLog != null) {
/* 1177 */       yShrinkInvoker.setLogFile(this.shrinkLog);
/*      */     }
/*      */     
/* 1180 */     if (null != this.entryPoints) {
/* 1181 */       yShrinkInvoker.setEntyPoints(this.entryPoints);
/*      */     }
/*      */     
/* 1184 */     if (null != this.expose && this.useExposeAsEntryPoints) {
/* 1185 */       for (ClassSection cs : this.expose.getClasses()) {
/* 1186 */         yShrinkInvoker.addClassSection(cs);
/*      */       }
/* 1188 */       for (MethodSection ms : this.expose.getMethods()) {
/* 1189 */         yShrinkInvoker.addMethodSection(ms);
/*      */       }
/* 1191 */       for (FieldSection fs : this.expose.getFields()) {
/* 1192 */         yShrinkInvoker.addFieldSection(fs);
/*      */       }
/*      */     } 
/*      */     
/* 1196 */     yShrinkInvoker.execute();
/*      */     
/* 1198 */     for (i = 0; i < this.tempJars.length; i++) {
/* 1199 */       if (null != this.tempJars[i]) {
/* 1200 */         InOutPair pair = this.pairs.get(i);
/* 1201 */         pair.setIn(this.tempJars[i]);
/* 1202 */         pair.setOut(outJars[i]);
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
/*      */   public void addInheritanceEntries(Collection entries) throws IOException {
/* 1215 */     if (!this.needYShrinkModel || this.expose == null)
/*      */       return; 
/* 1217 */     this.yShrinkModel = null;
/*      */     
/*      */     try {
/* 1220 */       this.yShrinkModel = (YShrinkModel)Class.forName("com.yworks.yshrink.YShrinkModelImpl").newInstance();
/* 1221 */     } catch (InstantiationException e) {
/* 1222 */       throw new BuildException("No shrinking support found.", e);
/* 1223 */     } catch (IllegalAccessException e) {
/* 1224 */       throw new BuildException("No shrinking support found.", e);
/* 1225 */     } catch (ClassNotFoundException e) {
/* 1226 */       throw new BuildException("No shrinking support found.", e);
/*      */     } 
/*      */     
/* 1229 */     if (null == this.yShrinkModel)
/*      */       return; 
/* 1231 */     if (this.resourceClassPath != null) {
/* 1232 */       this.yShrinkModel.setResourceClassPath(this.resourceClassPath, (Task)this);
/*      */     }
/*      */     
/* 1235 */     this.yShrinkModel.createSimpleModel(this.pairs);
/*      */     
/* 1237 */     for (String className : this.yShrinkModel.getAllClassNames()) {
/*      */       
/* 1239 */       Set<String> allAncestorClasses = this.yShrinkModel.getAllAncestorClasses(className);
/* 1240 */       Set<String> allInterfaces = this.yShrinkModel.getAllImplementedInterfaces(className);
/*      */       
/* 1242 */       for (ClassSection cs : this.expose.getClasses()) {
/*      */         
/* 1244 */         if (null != cs.getExtends()) {
/* 1245 */           String extendsName = cs.getExtends();
/* 1246 */           if (extendsName.equals(className)) {
/*      */             
/* 1248 */             cs.addEntries(entries, className);
/*      */           }
/* 1250 */           else if (allAncestorClasses.contains(extendsName)) {
/* 1251 */             cs.addEntries(entries, className);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1257 */         if (null != cs.getImplements()) {
/* 1258 */           String interfaceName = cs.getImplements();
/* 1259 */           if (interfaceName.equals(className)) {
/*      */             
/* 1261 */             cs.addEntries(entries, className); continue;
/*      */           } 
/* 1263 */           if (allInterfaces.contains(interfaceName)) {
/* 1264 */             cs.addEntries(entries, className);
/*      */           }
/*      */         } 
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
/*      */   public void setShrink(boolean doShrink) {
/* 1279 */     if (!this.mode) {
/* 1280 */       this.doShrink = doShrink;
/*      */     } else {
/* 1282 */       throw new BuildException("The shrink attribute is not supported when the obfuscate task is nested inside a yguard task.\n Use a separate nested shrink task instead.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShrinkLog(File shrinkLog) {
/* 1293 */     this.shrinkLog = shrinkLog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseExposeAsEntryPoints(boolean useExposeAsEntryPoints) {
/* 1302 */     this.useExposeAsEntryPoints = useExposeAsEntryPoints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class ResourceAdjuster
/*      */     implements ResourceHandler
/*      */   {
/*      */     protected final GuardDB db;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final Map map;
/*      */ 
/*      */ 
/*      */     
/* 1321 */     StringReplacer contentReplacer = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected ResourceAdjuster(final GuardDB db) {
/* 1330 */       this.db = db;
/* 1331 */       this.map = new HashMap<Object, Object>()
/*      */         {
/*      */           public Object get(Object key) {
/* 1334 */             return db.translateJavaClass(key.toString());
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean filterName(String inName, StringBuffer outName) {
/* 1341 */       boolean rp = true;
/* 1342 */       boolean rn = false;
/*      */       
/* 1344 */       for (Iterator<ObfuscatorTask.AdjustSection> iter = ObfuscatorTask.this.adjustSections.iterator(); iter.hasNext(); ) {
/*      */         
/* 1346 */         ObfuscatorTask.AdjustSection as = iter.next();
/* 1347 */         if (as.contains(inName)) {
/*      */           
/* 1349 */           if (as.getReplaceName()) rn = true; 
/* 1350 */           if (!as.getReplacePath()) rp = false;
/*      */         
/*      */         } 
/*      */       } 
/* 1354 */       if (rn) {
/*      */         
/* 1356 */         outName.setLength(0);
/* 1357 */         String servicesPrefix = "META-INF/services/";
/* 1358 */         if (inName.startsWith("META-INF/services/")) {
/*      */           
/* 1360 */           String cn = inName.substring("META-INF/services/".length());
/* 1361 */           outName.append("META-INF/services/");
/*      */ 
/*      */ 
/*      */           
/* 1365 */           outName.append(this.db.translateJavaFile(cn).replace('/', '.'));
/*      */         } else {
/* 1367 */           int index = 0;
/* 1368 */           if (inName.endsWith(".properties")) {
/* 1369 */             index = inName.indexOf('_');
/*      */           }
/* 1371 */           if (index <= 0) {
/* 1372 */             index = inName.indexOf('.');
/*      */           }
/* 1374 */           String prefix = inName.substring(0, index);
/* 1375 */           prefix = this.db.translateJavaFile(prefix);
/* 1376 */           outName.append(prefix);
/* 1377 */           outName.append(inName.substring(index));
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1382 */         outName.append(inName);
/*      */       } 
/*      */       
/* 1385 */       if (!rp) {
/*      */         
/* 1387 */         String outPath = inName.substring(0, inName.lastIndexOf('/') + 1);
/* 1388 */         String outFile = outName.toString();
/* 1389 */         outFile = outFile.substring(outFile.lastIndexOf('/') + 1);
/* 1390 */         outName.setLength(0);
/* 1391 */         outName.append(outPath);
/* 1392 */         outName.append(outFile);
/*      */       } 
/*      */       
/* 1395 */       return (rn || !rp);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean filterContent(InputStream in, OutputStream out, String resourceName) throws IOException {
/* 1401 */       for (Iterator<ObfuscatorTask.AdjustSection> iter = ObfuscatorTask.this.adjustSections.iterator(); iter.hasNext(); ) {
/*      */         
/* 1403 */         ObfuscatorTask.AdjustSection as = iter.next();
/* 1404 */         if (filterContentImpl(in, out, resourceName, as))
/*      */         {
/* 1406 */           return true;
/*      */         }
/*      */       } 
/* 1409 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean filterContentImpl(InputStream in, OutputStream out, String resourceName, ObfuscatorTask.AdjustSection as) throws IOException {
/* 1420 */       if (as.contains(resourceName) && as.getReplaceContent()) {
/*      */         
/* 1422 */         Writer writer = new OutputStreamWriter(out);
/* 1423 */         getContentReplacer().replace(new InputStreamReader(in), writer, this.db, as.replaceContentSeparator);
/* 1424 */         writer.flush();
/* 1425 */         return true;
/*      */       } 
/* 1427 */       return false;
/*      */     }
/*      */     
/*      */     public String filterString(String in, String resourceName) throws IOException {
/* 1431 */       StringBuffer result = new StringBuffer(in.length());
/* 1432 */       getContentReplacer().replace(in, result, this.map);
/* 1433 */       return result.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected StringReplacer getContentReplacer() {
/* 1443 */       if (this.contentReplacer == null)
/*      */       {
/* 1445 */         this.contentReplacer = new StringReplacer("(?:\\w|[$])+((?:\\.|\\/)(?:\\w|[$])+)+");
/*      */       }
/* 1447 */       return this.contentReplacer;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ClassFileFilter
/*      */     implements Filter
/*      */   {
/*      */     private Filter parent;
/*      */ 
/*      */ 
/*      */     
/*      */     ClassFileFilter(Filter parent) {
/* 1462 */       this.parent = parent;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean accepts(Object o) {
/* 1467 */       String s = (String)o;
/* 1468 */       if (s.endsWith(".class") && s.indexOf('$') != -1)
/*      */       {
/* 1470 */         s = s.substring(0, s.indexOf('$')) + ".class";
/*      */       }
/* 1472 */       return this.parent.accepts(s);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class TaskLogger
/*      */     extends Logger
/*      */   {
/*      */     private PrintWriter writer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setWriter(PrintWriter writer) {
/* 1493 */       this.writer = writer;
/*      */     }
/*      */ 
/*      */     
/*      */     public void warning(String message) {
/* 1498 */       ObfuscatorTask.this.getProject().log((Task)ObfuscatorTask.this, "WARNING: " + message, 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public void warningToLogfile(String message) {
/* 1503 */       if (null != this.writer) {
/* 1504 */         this.writer.println("<!-- WARNING: " + message + " -->");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void log(String message) {
/* 1510 */       ObfuscatorTask.this.getProject().log((Task)ObfuscatorTask.this, message, 2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void error(String message) {
/* 1515 */       ObfuscatorTask.this.getProject().log((Task)ObfuscatorTask.this, "ERROR: " + message, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeLogHeader(PrintWriter log, File[] inFile, File[] outFile) {
/* 1522 */     log.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/* 1523 */     log.println("<yguard version=\"1.5\">");
/* 1524 */     log.println("<!--");
/* 1525 */     log.println("  yGuard Bytecode Obfuscator, v" + Version.getVersion() + ", a Product of yWorks GmbH - http://www.yworks.com");
/* 1526 */     log.println();
/* 1527 */     log.println("  Logfile created on " + (new Date()).toString());
/* 1528 */     log.println();
/* 1529 */     for (int i = 0; i < inFile.length; i++) {
/* 1530 */       log.println("  Jar file to be obfuscated:           " + inFile[i].getName());
/* 1531 */       log.println("  Target Jar file for obfuscated code: " + outFile[i].getName());
/* 1532 */       log.println();
/*      */     } 
/* 1534 */     log.println("-->");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class LogListener
/*      */     implements ObfuscationListener
/*      */   {
/*      */     private Project p;
/*      */ 
/*      */ 
/*      */     
/*      */     LogListener(Project p) {
/* 1547 */       this.p = p;
/*      */     }
/*      */ 
/*      */     
/*      */     public void obfuscatingClass(String className) {
/* 1552 */       this.p.log("Obfuscating class " + className, 3);
/*      */     }
/*      */ 
/*      */     
/*      */     public void obfuscatingJar(String inJar, String outJar) {
/* 1557 */       this.p.log("Obfuscating Jar " + inJar + " to " + outJar);
/*      */     }
/*      */ 
/*      */     
/*      */     public void parsingClass(String className) {
/* 1562 */       this.p.log("Parsing class " + className, 3);
/*      */     }
/*      */ 
/*      */     
/*      */     public void parsingJar(String jar) {
/* 1567 */       this.p.log("Parsing jar " + jar);
/*      */     }
/*      */   }
/*      */   
/*      */   private void writeLogFooter(PrintWriter log) {
/* 1572 */     log.println("</yguard>");
/*      */   }
/*      */   
/*      */   private static final class YGuardNameFactory
/*      */     extends NameMakerFactory.DefaultNameMakerFactory {
/*      */     private static String legalFirstChars;
/*      */     private static String legalChars;
/*      */     private static String crazylegalFirstChars;
/*      */     private static String crazylegalChars;
/*      */     private static String asciiFirstChars;
/*      */     private static String asciiChars;
/*      */     private static String asciiLowerChars;
/* 1584 */     private static AtomicBoolean scrambled = new AtomicBoolean(false); private String packagePrefix; int mode;
/*      */     static final int LEGAL = 0;
/*      */     static final int COMPATIBLE = 1;
/*      */     
/*      */     static {
/* 1589 */       StringBuffer legalC = new StringBuffer(500);
/* 1590 */       StringBuffer illegalC = new StringBuffer(500);
/* 1591 */       StringBuffer crazyLegalC = new StringBuffer(500);
/* 1592 */       StringBuffer asciiC = new StringBuffer(500);
/* 1593 */       StringBuffer asciiLC = new StringBuffer(100);
/* 1594 */       StringBuffer asciiUC = new StringBuffer(100);
/*      */       
/* 1596 */       BitSet[] bs = null;
/*      */       
/*      */       try {
/* 1599 */         ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(ObfuscatorTask.class.getResourceAsStream("jdks.bits")));
/* 1600 */         bs = (BitSet[])ois.readObject();
/* 1601 */         ois.close();
/* 1602 */       } catch (IOException ioe) {
/* 1603 */         throw new InternalError("Could not load valid character bitset!" + ioe.getMessage());
/* 1604 */       } catch (ClassNotFoundException cnfe) {
/* 1605 */         throw new InternalError("Could not load valid character bitset!" + cnfe.getMessage());
/*      */       } 
/*      */       char i;
/* 1608 */       for (i = Character.MIN_VALUE; i < Character.MAX_VALUE; i = (char)(i + 1)) {
/* 1609 */         if (!bs[0].get(i)) {
/* 1610 */           illegalC.append(i);
/*      */         } else {
/* 1612 */           legalC.append(i);
/* 1613 */           if (i > 'ÿ') {
/* 1614 */             crazyLegalC.append(i);
/* 1615 */           } else if (i < '') {
/* 1616 */             asciiC.append(i);
/* 1617 */             if (Character.isLowerCase(i)) {
/* 1618 */               asciiLC.append(i);
/*      */             } else {
/* 1620 */               asciiUC.append(i);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1626 */       legalFirstChars = legalC.toString();
/* 1627 */       crazylegalFirstChars = crazyLegalC.toString();
/* 1628 */       asciiLowerChars = asciiLC.toString();
/* 1629 */       asciiFirstChars = asciiC.toString();
/*      */       
/* 1631 */       legalC.setLength(0);
/* 1632 */       illegalC.setLength(0);
/* 1633 */       crazyLegalC.setLength(0);
/* 1634 */       for (i = Character.MIN_VALUE; i < Character.MAX_VALUE; i = (char)(i + 1)) {
/*      */         
/* 1636 */         if (!bs[1].get(i)) {
/* 1637 */           illegalC.append(i);
/*      */         } else {
/* 1639 */           legalC.append(i);
/* 1640 */           if (i > 'ÿ') {
/* 1641 */             crazyLegalC.append(i);
/* 1642 */           } else if (i < '') {
/* 1643 */             asciiC.append(i);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1647 */       legalChars = legalC.toString();
/* 1648 */       crazylegalChars = crazyLegalC.toString();
/* 1649 */       asciiChars = asciiC.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final int ILLEGAL = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final int SMALL = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final int MIX = 12;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final int BEST = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean overloadEnabled = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     YGuardNameFactory() {
/* 1690 */       this(4);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     YGuardNameFactory(int mode) {
/* 1699 */       setInstance((NameMakerFactory)this);
/* 1700 */       this.mode = mode;
/*      */     }
/*      */     
/*      */     private static void scramble() {
/* 1704 */       if (scrambled.compareAndSet(false, true)) {
/* 1705 */         asciiChars = scrambleChars(asciiChars);
/* 1706 */         asciiFirstChars = scrambleChars(asciiFirstChars);
/* 1707 */         legalChars = scrambleChars(legalChars);
/* 1708 */         legalFirstChars = scrambleChars(legalFirstChars);
/* 1709 */         crazylegalChars = scrambleChars(crazylegalChars);
/* 1710 */         crazylegalFirstChars = scrambleChars(crazylegalFirstChars);
/*      */       } 
/*      */     }
/*      */     
/*      */     private static String scrambleChars(String string) {
/* 1715 */       char[] chars = string.toCharArray();
/* 1716 */       Random r = new Random();
/*      */       
/* 1718 */       for (int c = 0; c < chars.length; c++) {
/* 1719 */         int randomPosition = r.nextInt(chars.length);
/* 1720 */         char original = chars[c];
/* 1721 */         char random = chars[randomPosition];
/* 1722 */         chars[c] = random;
/* 1723 */         chars[randomPosition] = original;
/*      */       } 
/* 1725 */       StringBuilder sb = new StringBuilder();
/* 1726 */       for (char c1 : chars) {
/* 1727 */         sb.append(c1);
/*      */       }
/* 1729 */       return sb.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isOverloadEnabled() {
/* 1738 */       return this.overloadEnabled;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setOverloadEnabled(boolean overloadEnabled) {
/* 1747 */       this.overloadEnabled = overloadEnabled;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setPackagePrefix(String prefix)
/*      */     {
/* 1756 */       this.packagePrefix = prefix;
/* 1757 */       if (this.packagePrefix != null)
/* 1758 */         this.packagePrefix = this.packagePrefix.replace('.', '/') + '/';  } protected NameMaker createFieldNameMaker(String[] reservedNames, String fqClassName) { ObfuscatorTask.LongNameMaker longNameMaker1, longNameMaker2; ObfuscatorTask.MixNameMaker mixNameMaker1;
/*      */       ObfuscatorTask.LongNameMaker longNameMaker4;
/*      */       ObfuscatorTask.KeywordNameMaker keywordNameMaker1;
/*      */       ObfuscatorTask.CompoundNameMaker compoundNameMaker1;
/*      */       ObfuscatorTask.LongNameMaker longNameMaker3;
/*      */       ObfuscatorTask.MixNameMaker mixNameMaker2;
/* 1764 */       switch (this.mode)
/*      */       
/*      */       { default:
/* 1767 */           longNameMaker1 = new ObfuscatorTask.LongNameMaker(reservedNames, asciiLowerChars, asciiLowerChars, 1);
/*      */           
/* 1769 */           longNameMaker1.setOverloadEnabled(this.overloadEnabled);
/* 1770 */           return longNameMaker1;
/*      */         case 4:
/* 1772 */           longNameMaker2 = new ObfuscatorTask.LongNameMaker(reservedNames, legalFirstChars, legalChars, 1);
/*      */           
/* 1774 */           longNameMaker2.setOverloadEnabled(this.overloadEnabled);
/* 1775 */           return longNameMaker2;
/*      */         case 12:
/*      */         case 13:
/* 1778 */           nm1 = new ObfuscatorTask.LongNameMaker(reservedNames, false, 6);
/* 1779 */           nm2 = new ObfuscatorTask.KeywordNameMaker(reservedNames);
/* 1780 */           mixNameMaker1 = new ObfuscatorTask.MixNameMaker(null, reservedNames, nm1, 3);
/*      */           
/* 1782 */           mnm = mixNameMaker1;
/* 1783 */           mnm.add(nm2, 1);
/* 1784 */           mnm.setOverloadEnabled(this.overloadEnabled);
/* 1785 */           return mnm;
/*      */         case 8:
/*      */         case 9:
/* 1788 */           longNameMaker4 = new ObfuscatorTask.LongNameMaker(reservedNames, false, 256);
/*      */           
/* 1790 */           keywordNameMaker1 = new ObfuscatorTask.KeywordNameMaker(reservedNames);
/* 1791 */           compoundNameMaker1 = new ObfuscatorTask.CompoundNameMaker(longNameMaker4, keywordNameMaker1);
/*      */ 
/*      */           
/* 1794 */           longNameMaker4.setOverloadEnabled(this.overloadEnabled);
/* 1795 */           keywordNameMaker1.setOverloadEnabled(this.overloadEnabled);
/* 1796 */           return compoundNameMaker1;
/*      */         case 6:
/* 1798 */           longNameMaker3 = new ObfuscatorTask.LongNameMaker(reservedNames, crazylegalFirstChars, crazylegalChars, 1);
/*      */           
/* 1800 */           longNameMaker3.setOverloadEnabled(this.overloadEnabled);
/* 1801 */           return longNameMaker3;
/*      */         case 14:
/* 1803 */           nm1 = new ObfuscatorTask.LongNameMaker(reservedNames, false, 6);
/* 1804 */           nm2 = new ObfuscatorTask.KeywordNameMaker(reservedNames, ObfuscatorTask.KeywordNameMaker.KEYWORDS, ObfuscatorTask.KeywordNameMaker.SPACER);
/*      */ 
/*      */           
/* 1807 */           mixNameMaker2 = new ObfuscatorTask.MixNameMaker(null, reservedNames, nm1, 2);
/*      */           
/* 1809 */           mixNameMaker2.add(nm2, 1);
/* 1810 */           mixNameMaker2.setOverloadEnabled(this.overloadEnabled);
/* 1811 */           return mixNameMaker2;
/*      */         case 10:
/* 1813 */           break; }  ObfuscatorTask.AbstractNameMaker nm1 = new ObfuscatorTask.KeywordNameMaker(reservedNames, ObfuscatorTask.KeywordNameMaker.KEYWORDS, ObfuscatorTask.KeywordNameMaker.SPACER);
/*      */ 
/*      */       
/* 1816 */       ObfuscatorTask.AbstractNameMaker nm2 = new ObfuscatorTask.LongNameMaker(reservedNames, false, 256);
/* 1817 */       ObfuscatorTask.MixNameMaker mnm = new ObfuscatorTask.MixNameMaker(null, reservedNames, nm1, 1);
/*      */       
/* 1819 */       mnm.add(nm2, 1);
/* 1820 */       mnm.setOverloadEnabled(this.overloadEnabled);
/* 1821 */       return mnm; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected NameMaker createMethodNameMaker(String[] reservedNames, String fqClassName) {
/* 1827 */       return createFieldNameMaker(reservedNames, fqClassName);
/*      */     }
/*      */     protected NameMaker createPackageNameMaker(String[] reservedNames, String packageName) {
/*      */       ObfuscatorTask.AbstractNameMaker nm1, nm3, nm4, nm2;
/*      */       ObfuscatorTask.MixNameMaker mnm;
/* 1832 */       boolean topLevel = (packageName.length() < 1);
/* 1833 */       switch (this.mode)
/*      */       
/*      */       { 
/*      */         
/*      */         default:
/* 1838 */           if (topLevel && this.packagePrefix != null) {
/* 1839 */             return new ObfuscatorTask.PrefixNameMaker(this.packagePrefix, reservedNames, new ObfuscatorTask.LongNameMaker(null, asciiLowerChars, asciiLowerChars, 1));
/*      */           }
/* 1841 */           return new ObfuscatorTask.LongNameMaker(reservedNames, asciiLowerChars, asciiLowerChars, 1);
/*      */ 
/*      */         
/*      */         case 4:
/*      */         case 6:
/* 1846 */           if (topLevel && this.packagePrefix != null) {
/* 1847 */             return new ObfuscatorTask.PrefixNameMaker(this.packagePrefix, reservedNames, new ObfuscatorTask.LongNameMaker(null, asciiFirstChars, asciiChars, 1));
/*      */           }
/* 1849 */           return new ObfuscatorTask.LongNameMaker(reservedNames, asciiFirstChars, asciiChars, 1);
/*      */ 
/*      */         
/*      */         case 12:
/*      */         case 14:
/* 1854 */           nm1 = new ObfuscatorTask.LongNameMaker(reservedNames, asciiFirstChars, asciiChars, 1);
/*      */           
/* 1856 */           nm3 = new ObfuscatorTask.LongNameMaker(reservedNames, true, 256);
/* 1857 */           nm4 = new ObfuscatorTask.LongNameMaker(reservedNames, true, 4);
/* 1858 */           nm2 = new ObfuscatorTask.KeywordNameMaker(reservedNames);
/* 1859 */           mnm = new ObfuscatorTask.MixNameMaker(topLevel ? this.packagePrefix : null, reservedNames, nm1, 8);
/* 1860 */           mnm.add(nm4, 4);
/* 1861 */           mnm.add(nm2, 4);
/* 1862 */           mnm.add(nm3, 1);
/* 1863 */           return mnm;
/*      */         case 8:
/*      */         case 10:
/* 1866 */           break; }  if (topLevel && this.packagePrefix != null) {
/* 1867 */         return new ObfuscatorTask.PrefixNameMaker(this.packagePrefix, reservedNames, new ObfuscatorTask.LongNameMaker(null, true, 256));
/*      */       }
/* 1869 */       return new ObfuscatorTask.LongNameMaker(reservedNames, true, 256);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected NameMaker createClassNameMaker(String[] reservedNames, String fqClassName) {
/* 1876 */       return createPackageNameMaker(reservedNames, fqClassName);
/*      */     }
/*      */ 
/*      */     
/*      */     protected NameMaker createInnerClassNameMaker(String[] reservedNames, String fqInnerClassName) {
/* 1881 */       switch (this.mode)
/*      */       
/*      */       { 
/*      */         
/*      */         default:
/* 1886 */           return new ObfuscatorTask.PrefixNameMaker("_", reservedNames, new ObfuscatorTask.LongNameMaker(null, asciiLowerChars, asciiLowerChars, 1));
/*      */         case 4:
/* 1888 */           return new ObfuscatorTask.PrefixNameMaker("_", reservedNames, new ObfuscatorTask.LongNameMaker(null, asciiFirstChars, asciiChars, 1));
/*      */         case 12:
/* 1890 */           return new ObfuscatorTask.PrefixNameMaker("_", reservedNames, new ObfuscatorTask.LongNameMaker(null, true, 1));
/*      */         case 8:
/* 1892 */           return new ObfuscatorTask.PrefixNameMaker("_", reservedNames, new ObfuscatorTask.LongNameMaker(null, true, 4));
/*      */         case 6:
/* 1894 */           return new ObfuscatorTask.LongNameMaker(reservedNames, asciiFirstChars, asciiChars, 1);
/*      */         case 14:
/* 1896 */           return new ObfuscatorTask.LongNameMaker(reservedNames, true, 1);
/*      */         case 10:
/* 1898 */           break; }  return new ObfuscatorTask.LongNameMaker(reservedNames, true, 10);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1903 */       switch (this.mode)
/*      */       { default:
/* 1905 */           return "yGuardNameFactory [naming-scheme: default; language-conformity: default]";
/*      */         case 5:
/* 1907 */           return "yGuardNameFactory [naming-scheme: small; language-conformity: compatible]";
/*      */         case 13:
/* 1909 */           return "yGuardNameFactory [naming-scheme: mix; language-conformity: compatible]";
/*      */         case 9:
/* 1911 */           return "yGuardNameFactory [naming-scheme: best; language-conformity: compatible]";
/*      */         case 4:
/* 1913 */           return "yGuardNameFactory [naming-scheme: small; language-conformity: legal]";
/*      */         case 12:
/* 1915 */           return "yGuardNameFactory [naming-scheme: mix; language-conformity: legal]";
/*      */         case 8:
/* 1917 */           return "yGuardNameFactory [naming-scheme: best; language-conformity: legal]";
/*      */         case 6:
/* 1919 */           return "yGuardNameFactory [naming-scheme: small; language-conformity: illegal]";
/*      */         case 14:
/* 1921 */           return "yGuardNameFactory [naming-scheme: mix; language-conformity: illegal]";
/*      */         case 10:
/* 1923 */           break; }  return "yGuardNameFactory [naming-scheme: best; language-conformity: illegal]";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class CompoundNameMaker
/*      */     implements NameMaker
/*      */   {
/*      */     private NameMaker nm1;
/*      */ 
/*      */     
/*      */     private NameMaker nm2;
/*      */ 
/*      */ 
/*      */     
/*      */     CompoundNameMaker(NameMaker nm1, NameMaker nm2) {
/* 1941 */       this.nm1 = nm1;
/* 1942 */       this.nm2 = nm2;
/*      */     }
/*      */ 
/*      */     
/*      */     public String nextName(String descriptor) {
/* 1947 */       return this.nm1.nextName(descriptor) + this.nm2.nextName(descriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MixNameMaker
/*      */     extends AbstractNameMaker
/*      */   {
/* 1959 */     List nameMakers = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String prefix;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MixNameMaker(String prefix, String[] reservedNames, ObfuscatorTask.AbstractNameMaker delegate, int count) {
/* 1974 */       super(reservedNames, "O0", 1);
/* 1975 */       add(delegate, count);
/* 1976 */       this.prefix = prefix;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void add(ObfuscatorTask.AbstractNameMaker delegate, int count) {
/* 1986 */       count = (count < 1) ? 1 : count;
/* 1987 */       for (int i = 0; i < count; i++) {
/* 1988 */         this.nameMakers.add(delegate);
/*      */       }
/* 1990 */       Collections.shuffle(this.nameMakers);
/*      */     }
/*      */ 
/*      */     
/*      */     String generateName(int i) {
/* 1995 */       if (this.prefix != null) {
/* 1996 */         return this.prefix + ((ObfuscatorTask.AbstractNameMaker)this.nameMakers.get(i % this.nameMakers.size())).generateName(i);
/*      */       }
/* 1998 */       return ((ObfuscatorTask.AbstractNameMaker)this.nameMakers.get(i % this.nameMakers.size())).generateName(i);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class LongNameMaker
/*      */     extends AbstractNameMaker
/*      */   {
/*      */     String chars;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String firstChars;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LongNameMaker(String[] reservedNames) {
/* 2022 */       this(reservedNames, false, 256);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LongNameMaker(String[] reservedNames, boolean ascii, int length) {
/* 2033 */       this(reservedNames, ascii ? "Oo" : "OoÒÓÔÕÖØôõöø", 
/* 2034 */           ascii ? "Oo0" : "0OoÒÓÔÕÖØôõöø", length);
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
/*      */     LongNameMaker(String[] reservedNames, String firstChars, String chars, int minLength) {
/* 2046 */       super(reservedNames, null, minLength);
/* 2047 */       this.chars = chars;
/* 2048 */       if (chars == null || chars.length() < 1) {
/* 2049 */         throw new IllegalArgumentException("must specify at least one character!");
/*      */       }
/* 2051 */       this.firstChars = firstChars;
/* 2052 */       if (firstChars != null && firstChars.length() < 1) this.firstChars = null;
/*      */     
/*      */     }
/*      */     
/*      */     String generateName(int i) {
/* 2057 */       StringBuffer sb = new StringBuffer(20);
/* 2058 */       int tmp = i;
/* 2059 */       if (this.firstChars != null) {
/* 2060 */         sb.append(this.firstChars.charAt(tmp % this.firstChars.length()));
/* 2061 */         if (this.firstChars.length() > 1) {
/* 2062 */           tmp /= this.firstChars.length();
/*      */         } else {
/* 2064 */           tmp--;
/*      */         } 
/*      */       } 
/* 2067 */       while (tmp > 0) {
/* 2068 */         sb.append(this.chars.charAt(tmp % this.chars.length()));
/* 2069 */         if (this.chars.length() > 1) {
/* 2070 */           tmp /= this.chars.length(); continue;
/*      */         } 
/* 2072 */         tmp--;
/*      */       } 
/*      */       
/* 2075 */       if (this.chars.length() > 1) {
/* 2076 */         while (sb.length() < this.minLength) {
/* 2077 */           sb.append(this.chars.charAt(0));
/*      */         }
/*      */       }
/* 2080 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class KeywordNameMaker
/*      */     extends AbstractNameMaker
/*      */   {
/* 2091 */     static final String[] KEYWORDS = new String[] { "this", "super", "new", "Object", "String", "class", "return", "void", "null", "int", "if", "float", "for", "do", "while", "public", "private", "interface" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2097 */     static final String[] SPACER = new String[] { ".", "$", " ", "_" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2104 */     static final String[] NOSPACER = new String[] { "" };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String chars;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String[] keyWords;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String[] spacer;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     KeywordNameMaker(String[] reservedNames) {
/* 2125 */       this(reservedNames, KEYWORDS, NOSPACER);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     KeywordNameMaker(String[] reservedNames, String[] keyWords, String[] spacer) {
/* 2136 */       super(reservedNames, "Oo0", 0);
/* 2137 */       this.keyWords = keyWords;
/* 2138 */       this.spacer = spacer;
/*      */     }
/*      */ 
/*      */     
/*      */     String generateName(int i) {
/* 2143 */       StringBuffer sb = new StringBuffer(30);
/* 2144 */       int tmp = i;
/* 2145 */       int sc = 0;
/* 2146 */       while (tmp > 0) {
/* 2147 */         sb.append(this.keyWords[tmp % this.keyWords.length]);
/* 2148 */         tmp /= this.keyWords.length;
/* 2149 */         if (tmp > 0) {
/* 2150 */           sb.append(this.spacer[sc % this.spacer.length]);
/* 2151 */           sc++;
/*      */         } 
/*      */       } 
/* 2154 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class PrefixNameMaker
/*      */     extends AbstractNameMaker
/*      */   {
/*      */     private String prefix;
/*      */ 
/*      */ 
/*      */     
/*      */     private ObfuscatorTask.AbstractNameMaker delegate;
/*      */ 
/*      */ 
/*      */     
/*      */     PrefixNameMaker(String prefix, String[] reservedNames, ObfuscatorTask.AbstractNameMaker delegate) {
/* 2173 */       super(reservedNames, "O0", 1);
/* 2174 */       this.prefix = prefix;
/* 2175 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     String generateName(int i) {
/* 2180 */       return this.prefix + this.delegate.generateName(i);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractNameMaker
/*      */     implements NameMaker
/*      */   {
/*      */     Set reservedNames;
/*      */ 
/*      */ 
/*      */     
/* 2195 */     Map countMap = new HashMap<>();
/*      */ 
/*      */     
/*      */     String fillChars;
/*      */ 
/*      */     
/*      */     int minLength;
/*      */ 
/*      */     
/*      */     private static final String DUMMY = "(com.dummy.Dummy)";
/*      */ 
/*      */     
/*      */     protected boolean overloadEnabled = true;
/*      */ 
/*      */     
/* 2210 */     private int counter = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isOverloadEnabled() {
/* 2218 */       return this.overloadEnabled;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setOverloadEnabled(boolean overloadEnabled) {
/* 2227 */       this.overloadEnabled = overloadEnabled;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractNameMaker(String[] reservedNames) {
/* 2237 */       this(reservedNames, "0o", 256);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractNameMaker(String[] reservedNames, String fillChars, int minLength) {
/* 2248 */       if (reservedNames != null && reservedNames.length > 0) {
/* 2249 */         this.reservedNames = new HashSet(Arrays.asList((Object[])reservedNames));
/*      */       } else {
/* 2251 */         this.reservedNames = Collections.EMPTY_SET;
/*      */       } 
/* 2253 */       this.minLength = minLength;
/* 2254 */       this.fillChars = (fillChars != null) ? fillChars : "0O";
/*      */     }
/*      */ 
/*      */     
/*      */     public String nextName(String descriptor) {
/*      */       int j;
/* 2260 */       if (descriptor == null) {
/* 2261 */         descriptor = "(com.dummy.Dummy)";
/*      */       }
/*      */ 
/*      */       
/* 2265 */       if (this.overloadEnabled) {
/* 2266 */         descriptor = descriptor.substring(0, descriptor.lastIndexOf(')'));
/* 2267 */         Integer i = (Integer)this.countMap.get(descriptor);
/* 2268 */         if (i == null) {
/* 2269 */           i = new Integer(1);
/*      */         }
/* 2271 */         j = i.intValue();
/*      */       } else {
/* 2273 */         j = this.counter;
/*      */       } 
/* 2275 */       String result = null;
/* 2276 */       StringBuffer sb = new StringBuffer((this.minLength > 10) ? (this.minLength + 20) : 20);
/*      */       while (true) {
/* 2278 */         sb.setLength(0);
/* 2279 */         String name = generateName(j);
/* 2280 */         sb.append(name);
/* 2281 */         if (sb.length() < this.minLength) {
/* 2282 */           while (sb.length() < this.minLength) {
/* 2283 */             sb.append(this.fillChars);
/*      */           }
/* 2285 */           sb.setLength(this.minLength);
/*      */         } 
/* 2287 */         result = sb.toString();
/* 2288 */         j++;
/* 2289 */         if (!this.reservedNames.contains(result)) {
/* 2290 */           if (this.overloadEnabled) {
/* 2291 */             this.countMap.put(descriptor, new Integer(j));
/*      */           } else {
/* 2293 */             this.counter = j;
/*      */           } 
/*      */           
/* 2296 */           return result;
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
/*      */     abstract String generateName(int param1Int);
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
/*      */   static final class ResourceCpResolver
/*      */     implements Cl.ClassResolver
/*      */   {
/*      */     Path resource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     URLClassLoader urlClassLoader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ResourceCpResolver(Path resources, Task target) {
/* 2343 */       this.resource = resources;
/* 2344 */       String[] list = resources.list();
/* 2345 */       List<URL> listUrls = new ArrayList();
/* 2346 */       for (int i = 0; i < list.length; i++) {
/*      */         try {
/* 2348 */           URL url = (new File(list[i])).toURL();
/* 2349 */           listUrls.add(url);
/* 2350 */         } catch (MalformedURLException mfue) {
/* 2351 */           target.getProject().log(target, "Could not resolve resource: " + mfue, 1);
/*      */         } 
/*      */       } 
/* 2354 */       URL[] urls = new URL[listUrls.size()];
/* 2355 */       listUrls.toArray(urls);
/* 2356 */       this.urlClassLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());
/*      */     }
/*      */     
/*      */     public Class resolve(String className) throws ClassNotFoundException {
/*      */       try {
/* 2361 */         return Class.forName(className, false, this.urlClassLoader);
/* 2362 */       } catch (NoClassDefFoundError ncdfe) {
/* 2363 */         String message = ncdfe.getMessage();
/* 2364 */         if (message == null || message.equals(className)) {
/* 2365 */           message = className;
/*      */         } else {
/* 2367 */           message = message + "[" + className + "]";
/*      */         } 
/* 2369 */         throw new ClassNotFoundException(message, ncdfe);
/* 2370 */       } catch (LinkageError le) {
/* 2371 */         throw new ClassNotFoundException(className, le);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void close() throws Exception {
/* 2377 */       this.urlClassLoader.close();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReplaceClassNameStrings(boolean replaceClassNameStrings) {
/* 2388 */     this.replaceClassNameStrings = replaceClassNameStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScramble(boolean scramble) {
/* 2397 */     if (scramble) {
/* 2398 */       YGuardNameFactory.scramble();
/* 2399 */       KeywordNameMaker.scramble();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static final class MyLineNumberTableMapper
/*      */     implements LineNumberTableMapper
/*      */   {
/*      */     private long salt;
/*      */     private ObfuscatorTask.LineNumberScrambler last;
/*      */     private long lastSeed;
/* 2410 */     private Set classNames = new HashSet();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MyLineNumberTableMapper(long salt) {
/* 2418 */       this.salt = salt;
/* 2419 */       this.last = new ObfuscatorTask.LineNumberScrambler(3584, this.lastSeed);
/*      */     }
/*      */     public boolean mapLineNumberTable(String className, String methodName, String methodSignature, LineNumberTableAttrInfo lineNumberTable) {
/*      */       ObfuscatorTask.LineNumberScrambler scrambler;
/* 2423 */       String javaClassName = className.replace('/', '.').replace('$', '.');
/* 2424 */       this.classNames.add(className.replace('/', '.'));
/* 2425 */       long seed = this.salt ^ javaClassName.hashCode();
/*      */       
/* 2427 */       if (seed == this.lastSeed) {
/* 2428 */         scrambler = this.last;
/*      */       } else {
/* 2430 */         scrambler = this.last = new ObfuscatorTask.LineNumberScrambler(3584, seed);
/* 2431 */         this.lastSeed = seed;
/*      */       } 
/* 2433 */       for (int i = 0; i < (lineNumberTable.getLineNumberTable()).length; i++) {
/* 2434 */         LineNumberInfo lineNumberInfo = lineNumberTable.getLineNumberTable()[i];
/* 2435 */         lineNumberInfo.setLineNumber(scrambler.scramble(lineNumberInfo.getLineNumber()));
/*      */       } 
/* 2437 */       return true;
/*      */     }
/*      */     
/*      */     public void logProperties(PrintWriter pw) {
/* 2441 */       if (!this.classNames.isEmpty()) {
/* 2442 */         for (Iterator<E> it = this.classNames.iterator(); it.hasNext();) {
/* 2443 */           pw.println("<property owner=\"" + ClassTree.toUtf8XmlString(it.next().toString()) + "\" name=\"scrambling-salt\" value=\"" + Long.toString(this.salt) + "\"/>");
/*      */         }
/* 2445 */         this.classNames.clear();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final class LineNumberSqueezer
/*      */     implements LineNumberTableMapper
/*      */   {
/* 2454 */     private List squeezedNumbers = new ArrayList();
/*      */     public boolean mapLineNumberTable(String className, String methodName, String methodSignature, LineNumberTableAttrInfo lineNumberTable) {
/* 2456 */       LineNumberInfo[] table = lineNumberTable.getLineNumberTable();
/* 2457 */       if (table.length > 0) {
/* 2458 */         LineNumberInfo lineNumberInfo = new LineNumberInfo(table[0].getStartPC(), table[0].getLineNumber());
/* 2459 */         lineNumberTable.setLineNumberTable(new LineNumberInfo[] { lineNumberInfo });
/* 2460 */         this.squeezedNumbers.add(new Object[] { className, methodName, methodSignature, lineNumberInfo });
/* 2461 */         return true;
/*      */       } 
/* 2463 */       return false;
/*      */     }
/*      */     
/*      */     public void logProperties(PrintWriter pw) {
/* 2467 */       if (!this.squeezedNumbers.isEmpty()) {
/* 2468 */         for (Iterator<Object[]> it = this.squeezedNumbers.iterator(); it.hasNext(); ) {
/* 2469 */           Object[] ar = it.next();
/* 2470 */           String className = ar[0].toString();
/* 2471 */           String methodName = ar[1].toString();
/* 2472 */           String methodSignature = ar[2].toString();
/* 2473 */           int line = ((LineNumberInfo)ar[3]).getLineNumber();
/* 2474 */           pw.println("<property owner=\"" + ClassTree.toUtf8XmlString(Conversion.toJavaClass(className)) + "#" + ClassTree.toUtf8XmlString(Conversion.toJavaMethod(methodName, methodSignature)) + "\" name=\"squeezed-linenumber\" value=\"" + line + "\"/>");
/*      */         } 
/* 2476 */         this.squeezedNumbers.clear();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class LineNumberScrambler
/*      */   {
/*      */     private int[] scrambled;
/*      */ 
/*      */ 
/*      */     
/*      */     private int[] unscrambled;
/*      */ 
/*      */ 
/*      */     
/*      */     public LineNumberScrambler(int size, long seed) {
/* 2495 */       this.scrambled = new int[size];
/* 2496 */       this.unscrambled = new int[size];
/* 2497 */       for (int i = 0; i < size; i++) {
/* 2498 */         this.scrambled[i] = i;
/* 2499 */         this.unscrambled[i] = i;
/*      */       } 
/* 2501 */       Random r = new Random(seed);
/* 2502 */       for (int j = 0; j < 10; j++) {
/* 2503 */         for (int k = 0; k < size; k++) {
/* 2504 */           int otherIndex = r.nextInt(size);
/* 2505 */           if (otherIndex != k) {
/* 2506 */             int pos1 = this.scrambled[k];
/* 2507 */             int pos2 = this.scrambled[otherIndex];
/*      */             
/* 2509 */             int p1 = this.unscrambled[pos1];
/* 2510 */             int p2 = this.unscrambled[pos2];
/* 2511 */             this.unscrambled[pos1] = p2;
/* 2512 */             this.unscrambled[pos2] = p1;
/*      */             
/* 2514 */             this.scrambled[k] = pos2;
/* 2515 */             this.scrambled[otherIndex] = pos1;
/*      */           } 
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
/*      */     
/*      */     public int scramble(int i) {
/* 2535 */       if (i >= this.scrambled.length) {
/* 2536 */         return this.scrambled[i % this.scrambled.length] + i / this.scrambled.length * this.scrambled.length;
/*      */       }
/* 2538 */       return this.scrambled[i];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int unscramble(int i) {
/* 2549 */       if (i >= this.scrambled.length) {
/* 2550 */         return this.unscrambled[i % this.scrambled.length] + i / this.scrambled.length * this.scrambled.length;
/*      */       }
/* 2552 */       return this.unscrambled[i];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/* 2563 */     new LineNumberScrambler(2000, 234432L);
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
/*      */   public String getAnnotationClass() {
/* 2603 */     return this.annotationClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAnnotationClass(String annotationClass) {
/* 2612 */     this.annotationClass = annotationClass;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ObfuscatorTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
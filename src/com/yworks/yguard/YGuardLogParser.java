/*      */ package com.yworks.yguard;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.geom.Ellipse2D;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.tree.DefaultMutableTreeNode;
/*      */ import javax.swing.tree.DefaultTreeModel;
/*      */ import javax.swing.tree.TreeNode;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.parsers.SAXParser;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.XMLReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class YGuardLogParser
/*      */ {
/*      */   private DefaultTreeModel tree;
/*   44 */   private final MyContentHandler contentHandler = new MyContentHandler();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AbstractMappedStruct
/*      */     implements Mapped
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String mappedName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Icon icon;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public AbstractMappedStruct(String namePart, String mappedName, Icon icon) {
/*   85 */       this.name = namePart;
/*   86 */       this.mappedName = mappedName;
/*   87 */       this.icon = icon;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getMappedName() {
/*   92 */       return this.mappedName;
/*      */     }
/*      */     
/*      */     public Icon getIcon() {
/*   96 */       return this.icon;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/*  101 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setMappedName(String n) {
/*  111 */       this.mappedName = n;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String n) {
/*  121 */       this.name = n;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  125 */       return getName() + " -> " + getMappedName();
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
/*      */   static final class PackageStruct
/*      */     extends AbstractMappedStruct
/*      */   {
/*      */     PackageStruct(String name, String map) {
/*  140 */       super(name, map, YGuardLogParser.Icons.PACKAGE_ICON);
/*      */     }
/*      */     public String toString() {
/*  143 */       return getName() + " -> " + getMappedName();
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
/*      */   static final class ClassStruct
/*      */     extends AbstractMappedStruct
/*      */   {
/*      */     ClassStruct(String name, String map) {
/*  158 */       super(name, map, YGuardLogParser.Icons.CLASS_ICON);
/*      */     }
/*      */     public String toString() {
/*  161 */       return getName() + " -> " + getMappedName();
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
/*      */   static final class MethodStruct
/*      */     extends AbstractMappedStruct
/*      */   {
/*      */     MethodStruct(String name, String map) {
/*  176 */       super(name, map, YGuardLogParser.Icons.METHOD_ICON);
/*      */     }
/*      */     public String toString() {
/*  179 */       return getName() + " -> " + getMappedName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class FieldStruct
/*      */     extends AbstractMappedStruct
/*      */   {
/*      */     FieldStruct(String name, String map) {
/*  191 */       super(name, map, YGuardLogParser.Icons.FIELD_ICON);
/*      */     }
/*      */     public String toString() {
/*  194 */       return getName() + " -> " + getMappedName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public YGuardLogParser() {
/*  202 */     DefaultMutableTreeNode root = new DefaultMutableTreeNode(null, true);
/*  203 */     this.tree = new DefaultTreeModel(root, true);
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
/*      */   protected DefaultMutableTreeNode findChild(TreeNode node, String name, Class ofType) {
/*  215 */     return findChild(node, name, ofType, false);
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
/*      */   protected DefaultMutableTreeNode findChild(TreeNode node, String name, Class ofType, boolean useMap) {
/*  228 */     for (Enumeration<? extends TreeNode> enumeration = node.children(); enumeration.hasMoreElements(); ) {
/*  229 */       DefaultMutableTreeNode child = (DefaultMutableTreeNode)enumeration.nextElement();
/*  230 */       Mapped m = (Mapped)child.getUserObject();
/*  231 */       if (ofType == null || ofType.isAssignableFrom(m.getClass())) {
/*  232 */         if (useMap) {
/*  233 */           if (m.getMappedName().equals(name))
/*  234 */             return child; 
/*      */           continue;
/*      */         } 
/*  237 */         if (m.getName().equals(name)) {
/*  238 */           return child;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  243 */     return null;
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
/*      */   protected DefaultMutableTreeNode getPackageNode(String packageName) {
/*  266 */     return getPackageNode(packageName, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultMutableTreeNode getPackageNode(String packageName, boolean useMap) {
/*  277 */     DefaultMutableTreeNode node = getRoot();
/*  278 */     if (packageName != null) {
/*  279 */       StringTokenizer st = new StringTokenizer(packageName, ".", false);
/*  280 */       while (st.hasMoreTokens()) {
/*  281 */         String token = st.nextToken();
/*  282 */         DefaultMutableTreeNode child = findChild(node, token, PackageStruct.class, useMap);
/*  283 */         if (child == null) {
/*  284 */           PackageStruct ps = new PackageStruct(token, token);
/*  285 */           child = new DefaultMutableTreeNode(ps, true);
/*  286 */           node.insert(child, calcChildIndex(node, child));
/*      */         } 
/*  288 */         node = child;
/*      */       } 
/*      */     } 
/*  291 */     return node;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClassStruct getClass(String fqn) {
/*  301 */     return (ClassStruct)getClassNode(fqn).getUserObject();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PackageStruct getPackage(String fqn) {
/*  311 */     return (PackageStruct)getPackageNode(fqn).getUserObject();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MethodStruct getMethod(String fqn, String signature) {
/*  322 */     return (MethodStruct)getMethodNode(fqn, signature).getUserObject();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected FieldStruct getField(String fqn, String signature) {
/*  333 */     return (FieldStruct)getFieldNode(fqn, signature).getUserObject();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultMutableTreeNode getClassNode(String fqn) {
/*  343 */     return getClassNode(fqn, false);
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
/*      */   protected DefaultMutableTreeNode getClassNode(String fqn, boolean useMap) {
/*      */     String packageName, className;
/*  356 */     if (fqn.indexOf('.') < 0) {
/*  357 */       packageName = null;
/*  358 */       className = fqn;
/*      */     } else {
/*  360 */       packageName = fqn.substring(0, fqn.lastIndexOf('.'));
/*  361 */       className = fqn.substring(fqn.lastIndexOf('.') + 1);
/*      */     } 
/*  363 */     DefaultMutableTreeNode pn = getPackageNode(packageName);
/*  364 */     if (className.indexOf('$') > 0) {
/*  365 */       for (StringTokenizer st = new StringTokenizer(className, "$", false); st.hasMoreTokens(); ) {
/*  366 */         String token = st.nextToken();
/*  367 */         DefaultMutableTreeNode defaultMutableTreeNode = findChild(pn, token, ClassStruct.class, useMap);
/*  368 */         if (defaultMutableTreeNode == null) {
/*  369 */           defaultMutableTreeNode = new DefaultMutableTreeNode(new ClassStruct(token, token), true);
/*  370 */           pn.insert(defaultMutableTreeNode, calcChildIndex(pn, defaultMutableTreeNode));
/*      */         } 
/*  372 */         pn = defaultMutableTreeNode;
/*      */       } 
/*  374 */       return pn;
/*      */     } 
/*  376 */     DefaultMutableTreeNode child = findChild(pn, className, ClassStruct.class, useMap);
/*  377 */     if (child == null) {
/*  378 */       child = new DefaultMutableTreeNode(new ClassStruct(className, className), true);
/*  379 */       pn.insert(child, calcChildIndex(pn, child));
/*      */     } 
/*  381 */     return child;
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
/*      */   protected DefaultMutableTreeNode getMethodNode(String cname, String fqn) {
/*  393 */     return getMethodNode(cname, fqn, false);
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
/*      */   protected DefaultMutableTreeNode getMethodNode(String cname, String fqn, boolean useMap) {
/*  406 */     DefaultMutableTreeNode cn = getClassNode(cname);
/*  407 */     DefaultMutableTreeNode child = findChild(cn, fqn, MethodStruct.class, useMap);
/*  408 */     if (child == null) {
/*  409 */       MethodStruct ms = new MethodStruct(fqn, fqn);
/*  410 */       child = new DefaultMutableTreeNode(ms, false);
/*  411 */       cn.insert(child, calcChildIndex(cn, child));
/*      */     } 
/*  413 */     return child;
/*      */   }
/*      */   
/*      */   private int calcChildIndex(DefaultMutableTreeNode cn, DefaultMutableTreeNode child) {
/*  417 */     int left = 0;
/*  418 */     int right = cn.getChildCount() - 1;
/*  419 */     Object userObject = child.getUserObject();
/*  420 */     while (right >= left) {
/*  421 */       int test = (left + right) / 2;
/*  422 */       Object testObject = ((DefaultMutableTreeNode)cn.getChildAt(test)).getUserObject();
/*  423 */       int cmp = compare(userObject, testObject);
/*  424 */       if (cmp == 0) {
/*  425 */         return test;
/*      */       }
/*  427 */       if (cmp < 0) {
/*  428 */         right = test - 1; continue;
/*      */       } 
/*  430 */       left = test + 1;
/*      */     } 
/*      */ 
/*      */     
/*  434 */     return left;
/*      */   }
/*      */   
/*      */   private int compare(Object o1, Object o2) {
/*  438 */     Mapped m1 = (Mapped)o1;
/*  439 */     Mapped m2 = (Mapped)o2;
/*  440 */     if (m1.getClass() != m2.getClass()) {
/*  441 */       if (m1.getClass() == PackageStruct.class)
/*  442 */         return -1; 
/*  443 */       if (m2.getClass() == PackageStruct.class) {
/*  444 */         return 1;
/*      */       }
/*  446 */       if (m1.getClass() == ClassStruct.class)
/*  447 */         return -1; 
/*  448 */       if (m2.getClass() == ClassStruct.class) {
/*  449 */         return 1;
/*      */       }
/*  451 */       if (m1.getClass() == MethodStruct.class)
/*  452 */         return -1; 
/*  453 */       if (m2.getClass() == MethodStruct.class) {
/*  454 */         return 1;
/*      */       }
/*      */     } 
/*  457 */     return m1.getName().compareTo(m2.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DefaultMutableTreeNode getFieldNode(String cname, String fqn) {
/*  468 */     return getFieldNode(cname, fqn, false);
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
/*      */   protected DefaultMutableTreeNode getFieldNode(String cname, String fqn, boolean useMap) {
/*  480 */     DefaultMutableTreeNode cn = getClassNode(cname);
/*  481 */     DefaultMutableTreeNode child = findChild(cn, fqn, FieldStruct.class, useMap);
/*  482 */     if (child == null) {
/*  483 */       FieldStruct ms = new FieldStruct(fqn, fqn);
/*  484 */       child = new DefaultMutableTreeNode(ms, false);
/*  485 */       cn.insert(child, calcChildIndex(cn, child));
/*      */     } 
/*  487 */     return child;
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
/*      */   void parse(File file) throws ParserConfigurationException, SAXException, IOException {
/*  499 */     if (file.getName().toLowerCase().endsWith(".gz")) {
/*  500 */       parse(new InputSource(new GZIPInputStream(new FileInputStream(file))));
/*      */     } else {
/*  502 */       URL url = file.toURI().toURL();
/*  503 */       if (url != null) {
/*  504 */         parse(url);
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
/*      */   public void parse(URL url) throws ParserConfigurationException, SAXException, IOException {
/*  518 */     parse(new InputSource(url.openStream()));
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
/*      */   public void parse(InputSource is) throws ParserConfigurationException, SAXException, IOException {
/*  530 */     SAXParserFactory f = SAXParserFactory.newInstance();
/*  531 */     f.setValidating(false);
/*  532 */     SAXParser parser = f.newSAXParser();
/*  533 */     XMLReader r = parser.getXMLReader();
/*  534 */     r.setContentHandler(this.contentHandler);
/*  535 */     r.parse(is);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String translate(String fqn) {
/*  545 */     DefaultMutableTreeNode node = getRoot();
/*      */     
/*  547 */     StringBuffer ocnSb = new StringBuffer();
/*      */     
/*  549 */     StringBuffer sb = new StringBuffer();
/*  550 */     boolean buildPrefix = true;
/*  551 */     for (StringTokenizer st = new StringTokenizer(fqn, "$.", true); st.hasMoreTokens(); ) {
/*  552 */       String token = st.nextToken();
/*  553 */       sb.append(token);
/*      */       
/*  555 */       if ("$".equals(token) || ".".equals(token)) {
/*      */         continue;
/*      */       }
/*      */       
/*  559 */       boolean hasNext = st.hasMoreTokens();
/*  560 */       Class<ClassStruct> type = hasNext ? null : ClassStruct.class;
/*  561 */       DefaultMutableTreeNode child = findChild(node, sb.toString(), type, true);
/*  562 */       if (child == null) {
/*  563 */         if (buildPrefix && hasNext) {
/*      */           
/*  565 */           st.nextToken();
/*      */           
/*  567 */           sb.append('/');
/*      */           continue;
/*      */         } 
/*  570 */         if (hasNext) {
/*  571 */           ocnSb.append(sb.toString().replace('/', '.'));
/*  572 */           append(ocnSb, st);
/*  573 */         } else if (buildPrefix) {
/*  574 */           ocnSb.append(fqn);
/*  575 */         } else if (node.getUserObject().getClass() == ClassStruct.class) {
/*  576 */           ocnSb.append(translateMethodName(node, sb.toString()));
/*      */         } else {
/*  578 */           ocnSb.append(sb.toString().replace('/', '.'));
/*      */         } 
/*  580 */         node = null;
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/*  585 */       buildPrefix = false;
/*  586 */       sb.setLength(0);
/*  587 */       node = child;
/*      */       
/*  589 */       ocnSb.append(getOriginalName(child));
/*  590 */       if (st.hasMoreTokens()) {
/*  591 */         ocnSb.append(st.nextToken());
/*      */       }
/*      */     } 
/*      */     
/*  595 */     return ocnSb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MyStackTraceElement translate(MyStackTraceElement ste) {
/*      */     try {
/*  606 */       DefaultMutableTreeNode classNode = getRoot();
/*  607 */       int dollarPos = ste.getClassName().indexOf('$');
/*  608 */       if (dollarPos < 0) {
/*  609 */         dollarPos = ste.getClassName().length();
/*      */       }
/*  611 */       int lastDot = ste.getClassName().substring(0, dollarPos).lastIndexOf('.');
/*      */       
/*  613 */       String packageName = ste.getClassName().substring(0, lastDot + 1);
/*  614 */       String classAndInnerClassName = ste.getClassName().substring(lastDot + 1);
/*      */       
/*  616 */       StringBuffer ocnSb = new StringBuffer();
/*      */       
/*  618 */       StringBuffer sb = new StringBuffer();
/*  619 */       boolean buildPrefix = true; StringTokenizer st;
/*  620 */       for (st = new StringTokenizer(packageName, ".", true); st.hasMoreTokens(); ) {
/*  621 */         String token = st.nextToken();
/*  622 */         sb.append(token);
/*      */         
/*  624 */         DefaultMutableTreeNode child = findChild(classNode, sb.toString(), PackageStruct.class, true);
/*  625 */         if (child == null) {
/*  626 */           if (buildPrefix && st.hasMoreTokens()) {
/*      */             
/*  628 */             st.nextToken();
/*      */             
/*  630 */             sb.append('/');
/*      */             continue;
/*      */           } 
/*  633 */           classNode = null;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*  638 */         buildPrefix = false;
/*  639 */         sb.setLength(0);
/*  640 */         classNode = child;
/*      */         
/*  642 */         ocnSb.append(getOriginalName(classNode));
/*  643 */         if (st.hasMoreTokens()) {
/*  644 */           ocnSb.append(st.nextToken());
/*      */         }
/*      */       } 
/*  647 */       if (buildPrefix) {
/*  648 */         classNode = null;
/*      */       }
/*      */       
/*  651 */       sb.setLength(0);
/*  652 */       for (st = new StringTokenizer(classAndInnerClassName, "$.", true); st.hasMoreTokens(); ) {
/*  653 */         String token = st.nextToken();
/*  654 */         sb.append(token);
/*  655 */         if (!"$".equals(token) && !".".equals(token)) {
/*  656 */           token = sb.toString();
/*  657 */           sb.setLength(0);
/*      */           
/*  659 */           DefaultMutableTreeNode child = findChild(classNode, token, ClassStruct.class, true);
/*  660 */           if (child == null) {
/*  661 */             ocnSb.append(token);
/*  662 */             append(ocnSb, st);
/*  663 */             classNode = null;
/*      */             break;
/*      */           } 
/*  666 */           classNode = child;
/*  667 */           ocnSb.append(getOriginalName(classNode));
/*  668 */           if (st.hasMoreTokens()) {
/*  669 */             ocnSb.append(st.nextToken());
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  674 */       String newMethodName = translateMethodName(classNode, ste.getMethodName());
/*      */       
/*  676 */       int lineNumber = 0;
/*  677 */       String originalClassName = ocnSb.toString();
/*      */       
/*      */       try {
/*  680 */         lineNumber = ste.getLineNumber();
/*  681 */         if (lineNumber > 0) {
/*  682 */           Map property = (Map)this.contentHandler.ownerProperties.get(originalClassName);
/*  683 */           long salt = -1L;
/*  684 */           if (property != null) {
/*  685 */             String saltString = (String)property.get("scrambling-salt");
/*  686 */             if (saltString != null) {
/*      */               try {
/*  688 */                 salt = Long.parseLong(saltString);
/*  689 */                 long seed = salt ^ originalClassName.replace('$', '.').hashCode();
/*      */                 
/*  691 */                 ObfuscatorTask.LineNumberScrambler scrambler = new ObfuscatorTask.LineNumberScrambler(3584, seed);
/*  692 */                 lineNumber = scrambler.unscramble(lineNumber);
/*  693 */               } catch (NumberFormatException numberFormatException) {}
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  699 */       } catch (Exception ex) {
/*  700 */         ex.printStackTrace();
/*      */       } 
/*      */       
/*  703 */       String fileName = (classNode == null) ? "" : buildFilename(originalClassName);
/*  704 */       return new MyStackTraceElement(originalClassName, newMethodName, fileName, lineNumber);
/*  705 */     } catch (Exception e) {
/*  706 */       return ste;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String translateMethodName(DefaultMutableTreeNode node, String mappedName) {
/*  711 */     StringBuffer originalName = new StringBuffer();
/*  712 */     if (node != null) {
/*  713 */       String del = "";
/*  714 */       for (Enumeration<TreeNode> en = node.children(); en.hasMoreElements(); ) {
/*  715 */         DefaultMutableTreeNode child = (DefaultMutableTreeNode)en.nextElement();
/*  716 */         Mapped mapped = (Mapped)child.getUserObject();
/*  717 */         if (mapped.getClass() == MethodStruct.class && 
/*  718 */           mapped.getMappedName().equals(mappedName)) {
/*  719 */           String name = mapped.getName();
/*      */           
/*  721 */           int braceIndex = name.indexOf('(');
/*  722 */           if (0 < braceIndex && braceIndex + 1 == name.indexOf(')')) {
/*  723 */             name = name.substring(0, braceIndex);
/*      */           }
/*      */           
/*  726 */           int spaceIndex = name.lastIndexOf(' ', (braceIndex < 0) ? name.length() : braceIndex);
/*  727 */           if (0 < spaceIndex) {
/*  728 */             name = name.substring(spaceIndex + 1);
/*      */           }
/*  730 */           originalName.append(del).append(name);
/*  731 */           del = "|";
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  736 */     return (originalName.length() < 1) ? mappedName : originalName.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) throws Exception {
/*  746 */     if (args.length < 1) {
/*  747 */       System.out.println("Usage java -jar yguard.jar logfile.xml[.gz] [-pipe] [name]");
/*  748 */       System.out.println(" where 'logfile.xml' is the logfile that has been generated ");
/*  749 */       System.out.println(" during the obfuscation process");
/*  750 */       System.out.println(" and which may be gzipped (with .gz extension)");
/*  751 */       System.out.println(" and where 'name' is an optional string, which will be translated");
/*  752 */       System.out.println(" according to the logfile automatically.");
/*  753 */       System.out.println(" If no 'name' is given, a tiny GUI will popup that will help in translating");
/*  754 */       System.out.println(" stacktraces, fully qualified classnames etc.");
/*  755 */       System.out.println(" If '-pipe' is specified as the last argument after the logfile the tool");
/*  756 */       System.out.println(" will translate the input from standard in and output the translation to");
/*  757 */       System.out.println(" standard out until the input is closed.");
/*  758 */       System.exit(-1);
/*      */     } 
/*  760 */     final File file = new File(args[0]);
/*  761 */     if (!file.isFile() || !file.canRead()) {
/*  762 */       System.err.println("Could not open file " + args[0]);
/*  763 */       System.exit(-1);
/*      */     } 
/*      */     
/*  766 */     if (args.length < 2) {
/*  767 */       EventQueue.invokeLater(new Runnable()
/*      */           {
/*      */             public void run() {
/*  770 */               (new LogParserView()).show(file);
/*      */             }
/*      */           });
/*      */     } else {
/*  774 */       YGuardLogParser parser = new YGuardLogParser();
/*  775 */       parser.parse(file);
/*      */       
/*  777 */       if (args[1].equals("-pipe")) {
/*  778 */         InputStreamReader er = new InputStreamReader(System.in);
/*  779 */         BufferedReader br = new BufferedReader(er);
/*      */         String s;
/*  781 */         while ((s = br.readLine()) != null) {
/*  782 */           System.out.println(parser.translate(new String[] { s })[0]);
/*      */         } 
/*      */       } else {
/*  785 */         String[] strings = new String[args.length - 1];
/*  786 */         System.arraycopy(args, 1, strings, 0, args.length - 1);
/*  787 */         String[] s = parser.translate(strings);
/*  788 */         for (int i = 0; i < s.length; i++) {
/*  789 */           System.out.println(s[i]);
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
/*      */ 
/*      */   
/*      */   String[] translate(String[] args) {
/*  811 */     String[] resultArr = new String[args.length];
/*  812 */     Pattern jrockitPattern = Pattern.compile("(.*\\s+)?([^;()\\s]+)\\.([^;()\\s]+)\\(([^)]*)\\)(.+)\\(([^:)]+)(?::(\\d*))?\\)(.*)");
/*  813 */     Pattern stePattern = Pattern.compile("(.*\\s+)?([^(\\s]+)\\.([^(\\s]+)\\(([^:)]*)(?::(\\d*))?\\)(.*)");
/*  814 */     Pattern fqnPattern = Pattern.compile("([^:;()\\s]+\\.)+([^:;()\\s]+)");
/*      */     
/*  816 */     for (int i = 0; i < args.length; i++) {
/*  817 */       args[i] = CharConverter.convert(args[i]);
/*  818 */       Matcher m2 = jrockitPattern.matcher(args[i]);
/*  819 */       if (m2.matches()) {
/*  820 */         MyStackTraceElement ste; String[] moduleAndType = split(m2.group(2));
/*      */ 
/*      */         
/*  823 */         if (m2.group(7) == null) {
/*  824 */           ste = new MyStackTraceElement(moduleAndType[1], m2.group(3), "", 0);
/*      */         } else {
/*  826 */           ste = new MyStackTraceElement(moduleAndType[1], m2.group(3), m2.group(6), Integer.parseInt(m2.group(7)));
/*      */         } 
/*  828 */         String params = m2.group(4);
/*      */         try {
/*  830 */           params = Conversion.toJavaArguments(params);
/*  831 */         } catch (RuntimeException runtimeException) {}
/*      */ 
/*      */         
/*  834 */         resultArr[i] = (
/*  835 */           (m2.group(1) != null) ? m2.group(1) : "") + moduleAndType[0] + 
/*      */           
/*  837 */           format(ste, (m2.group(7) == null) ? m2.group(6) : null) + " [" + params + "]" + m2
/*  838 */           .group(8);
/*      */       } else {
/*  840 */         Matcher m = stePattern.matcher(args[i]);
/*  841 */         if (m.matches()) {
/*  842 */           MyStackTraceElement ste; String[] moduleAndType = split(m.group(2));
/*      */ 
/*      */           
/*  845 */           if (m.group(5) == null) {
/*  846 */             ste = new MyStackTraceElement(moduleAndType[1], m.group(3), "", 0);
/*      */           } else {
/*  848 */             ste = new MyStackTraceElement(moduleAndType[1], m.group(3), m.group(4), Integer.parseInt(m.group(5)));
/*      */           } 
/*  850 */           resultArr[i] = (
/*  851 */             (m.group(1) != null) ? m.group(1) : "") + moduleAndType[0] + 
/*      */             
/*  853 */             format(translate(ste), (m.group(5) == null) ? m.group(4) : null) + m
/*  854 */             .group(6);
/*      */         } else {
/*  856 */           StringBuffer replacement = new StringBuffer();
/*  857 */           Matcher fqnMatcher = fqnPattern.matcher(args[i]);
/*  858 */           while (fqnMatcher.find()) {
/*  859 */             String result, moduleAndType[] = split(fqnMatcher.group());
/*      */             
/*      */             try {
/*  862 */               result = translate(moduleAndType[1]);
/*  863 */             } catch (Exception ex) {
/*  864 */               result = moduleAndType[1];
/*      */             } 
/*  866 */             fqnMatcher.appendReplacement(replacement, moduleAndType[0] + escapeReplacement(result));
/*      */           } 
/*  868 */           fqnMatcher.appendTail(replacement);
/*  869 */           resultArr[i] = replacement.toString();
/*      */         } 
/*      */       } 
/*      */     } 
/*  873 */     return resultArr;
/*      */   }
/*      */   
/*      */   private static String[] split(String moduleAndType) {
/*  877 */     if (moduleAndType == null) {
/*  878 */       return new String[] { "", "" };
/*      */     }
/*  880 */     int idx1 = moduleAndType.indexOf('$');
/*  881 */     int idx2 = (idx1 > -1) ? moduleAndType.lastIndexOf('/', idx1) : moduleAndType.lastIndexOf('/');
/*  882 */     if (idx2 > -1) {
/*  883 */       return new String[] { moduleAndType
/*  884 */           .substring(0, idx2 + 1), moduleAndType
/*  885 */           .substring(idx2 + 1) };
/*      */     }
/*      */     
/*  888 */     return new String[] { "", moduleAndType };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String format(MyStackTraceElement ste, String s) {
/*  894 */     String fn = ste.getFileName();
/*  895 */     if ((fn == null || fn.length() == 0) && s != null) {
/*  896 */       return ste.getClassName() + '.' + ste
/*  897 */         .getMethodName() + '(' + s + ')';
/*      */     }
/*  899 */     return ste.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   DefaultTreeModel getTreeModel() {
/*  909 */     return this.tree;
/*      */   }
/*      */   
/*      */   private DefaultMutableTreeNode getRoot() {
/*  913 */     return (DefaultMutableTreeNode)this.tree.getRoot();
/*      */   }
/*      */   
/*      */   private static String getOriginalName(DefaultMutableTreeNode node) {
/*  917 */     return ((Mapped)node.getUserObject()).getName();
/*      */   }
/*      */   
/*      */   private static String escapeReplacement(String replacementString) {
/*  921 */     if (replacementString.indexOf('\\') == -1 && replacementString.indexOf('$') == -1) {
/*  922 */       return replacementString;
/*      */     }
/*  924 */     StringBuffer result = new StringBuffer();
/*  925 */     for (int i = 0; i < replacementString.length(); i++) {
/*  926 */       char c = replacementString.charAt(i);
/*  927 */       if (c == '\\') {
/*  928 */         result.append('\\').append('\\');
/*  929 */       } else if (c == '$') {
/*  930 */         result.append('\\').append('$');
/*      */       } else {
/*  932 */         result.append(c);
/*      */       } 
/*      */     } 
/*  935 */     return result.toString();
/*      */   }
/*      */   
/*      */   private static StringBuffer append(StringBuffer sb, StringTokenizer st) {
/*  939 */     while (st.hasMoreTokens()) {
/*  940 */       sb.append(st.nextToken());
/*      */     }
/*  942 */     return sb;
/*      */   }
/*      */   
/*      */   private static String buildFilename(String qualifiedName) {
/*  946 */     String fileName = "";
/*  947 */     int idxDot = qualifiedName.lastIndexOf('.');
/*  948 */     if (idxDot > 0) {
/*  949 */       fileName = qualifiedName.substring(idxDot + 1);
/*      */     } else {
/*  951 */       fileName = qualifiedName;
/*      */     } 
/*  953 */     int idxDollar = fileName.indexOf('$');
/*  954 */     if (idxDollar > 0) {
/*  955 */       fileName = fileName.substring(0, idxDollar);
/*      */     }
/*  957 */     return fileName + ".java";
/*      */   }
/*      */ 
/*      */   
/*      */   private class MyContentHandler
/*      */     implements ContentHandler
/*      */   {
/*      */     private boolean inMapSection;
/*      */     private boolean inLogSection;
/*      */     private boolean inExposeSection;
/*  967 */     final Map ownerProperties = new HashMap<>();
/*      */ 
/*      */     
/*      */     public void characters(char[] ch, int start, int length) throws SAXException {}
/*      */ 
/*      */     
/*      */     public void endDocument() throws SAXException {}
/*      */     
/*      */     public void endElement(String uri, String localName, String qName) throws SAXException {
/*  976 */       if ("expose".equals(qName)) {
/*  977 */         this.inExposeSection = false;
/*      */       }
/*  979 */       if ("map".equals(qName)) {
/*  980 */         this.inMapSection = false;
/*      */       }
/*  982 */       if ("yguard".equals(qName)) {
/*  983 */         this.inLogSection = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void endPrefixMapping(String prefix) throws SAXException {}
/*      */ 
/*      */     
/*      */     public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*      */ 
/*      */     
/*      */     public void processingInstruction(String target, String data) throws SAXException {}
/*      */ 
/*      */     
/*      */     public void setDocumentLocator(Locator locator) {}
/*      */ 
/*      */     
/*      */     public void skippedEntity(String name) throws SAXException {}
/*      */     
/*      */     public void startDocument() throws SAXException {
/* 1003 */       this.ownerProperties.clear();
/*      */     }
/*      */     
/*      */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 1007 */       if ("expose".equals(qName)) {
/* 1008 */         this.inExposeSection = true;
/*      */       }
/* 1010 */       if ("map".equals(qName)) {
/* 1011 */         this.inMapSection = true;
/*      */       }
/* 1013 */       if ("yguard".equals(qName)) {
/* 1014 */         this.inLogSection = true;
/* 1015 */         String version = attributes.getValue("version");
/* 1016 */         if ("1.5".compareTo(version) < 0) {
/* 1017 */           throw new IllegalStateException("Version should not be greater than 1.5 but was " + version);
/*      */         }
/*      */       } 
/* 1020 */       if (this.inLogSection && !this.inMapSection && 
/* 1021 */         "property".equals(qName)) {
/* 1022 */         String key = attributes.getValue("name");
/* 1023 */         String value = attributes.getValue("value");
/* 1024 */         String owner = attributes.getValue("owner");
/* 1025 */         Map<Object, Object> map = (Map)this.ownerProperties.get(owner);
/* 1026 */         if (map == null) {
/* 1027 */           map = new HashMap<>();
/* 1028 */           this.ownerProperties.put(owner, map);
/*      */         } 
/* 1030 */         map.put(key, value);
/*      */       } 
/*      */       
/* 1033 */       if (this.inExposeSection) {
/* 1034 */         if ("method".equals(qName)) {
/* 1035 */           String className = attributes.getValue("class");
/* 1036 */           String name = attributes.getValue("name");
/* 1037 */           YGuardLogParser.MethodStruct methodStruct = YGuardLogParser.this.getMethod(className, name);
/*      */         } 
/* 1039 */         if ("field".equals(qName)) {
/* 1040 */           String className = attributes.getValue("class");
/* 1041 */           String name = attributes.getValue("name");
/* 1042 */           YGuardLogParser.FieldStruct fieldStruct = YGuardLogParser.this.getField(className, name);
/*      */         } 
/* 1044 */         if ("package".equals(qName)) {
/* 1045 */           String name = attributes.getValue("name");
/* 1046 */           YGuardLogParser.PackageStruct packageStruct = YGuardLogParser.this.getPackage(name);
/*      */         } 
/* 1048 */         if ("class".equals(qName)) {
/* 1049 */           String name = attributes.getValue("name");
/* 1050 */           YGuardLogParser.ClassStruct classStruct = YGuardLogParser.this.getClass(name);
/*      */         } 
/*      */       } 
/* 1053 */       if (this.inMapSection) {
/* 1054 */         if ("method".equals(qName)) {
/* 1055 */           String className = attributes.getValue("class");
/* 1056 */           String name = attributes.getValue("name");
/* 1057 */           String map = attributes.getValue("map");
/* 1058 */           YGuardLogParser.MethodStruct fs = YGuardLogParser.this.getMethod(className, name);
/* 1059 */           fs.setMappedName(map);
/*      */         } 
/* 1061 */         if ("field".equals(qName)) {
/* 1062 */           String className = attributes.getValue("class");
/* 1063 */           String name = attributes.getValue("name");
/* 1064 */           String map = attributes.getValue("map");
/* 1065 */           YGuardLogParser.FieldStruct fs = YGuardLogParser.this.getField(className, name);
/* 1066 */           fs.setMappedName(map);
/*      */         } 
/* 1068 */         if ("package".equals(qName)) {
/* 1069 */           String name = attributes.getValue("name");
/* 1070 */           String map = attributes.getValue("map");
/* 1071 */           YGuardLogParser.PackageStruct ps = YGuardLogParser.this.getPackage(name);
/* 1072 */           ps.setMappedName(map);
/*      */         } 
/* 1074 */         if ("class".equals(qName)) {
/* 1075 */           String name = attributes.getValue("name");
/* 1076 */           String map = attributes.getValue("map");
/* 1077 */           YGuardLogParser.ClassStruct cs = YGuardLogParser.this.getClass(name);
/* 1078 */           cs.setMappedName(map);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void startPrefixMapping(String prefix, String uri) throws SAXException {}
/*      */     
/*      */     private MyContentHandler() {}
/*      */   }
/*      */   
/*      */   public static final class CharConverter
/*      */   {
/* 1091 */     private static final Pattern unicodeEscape = Pattern.compile("&#(\\d{1,5});");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static String convert(String s) {
/* 1100 */       StringBuilder r = new StringBuilder();
/*      */       
/* 1102 */       Matcher matcher = unicodeEscape.matcher(s);
/*      */       
/* 1104 */       int lastMatchEnd = 0;
/*      */       
/* 1106 */       while (matcher.find()) {
/* 1107 */         String match = matcher.group(1);
/* 1108 */         r.append(s.substring(lastMatchEnd, matcher.start()));
/* 1109 */         r.append((char)Integer.parseInt(match));
/* 1110 */         lastMatchEnd = matcher.end();
/*      */       } 
/* 1112 */       r.append(s.substring(lastMatchEnd, s.length()));
/*      */       
/* 1114 */       return r.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class MyStackTraceElement
/*      */   {
/*      */     private String className;
/*      */ 
/*      */     
/*      */     private String methodName;
/*      */ 
/*      */     
/*      */     private String fileName;
/*      */ 
/*      */     
/*      */     private int lineNumber;
/*      */ 
/*      */ 
/*      */     
/*      */     public MyStackTraceElement(String className, String methodName, String fileName, int lineNumber) {
/* 1136 */       this.className = className;
/* 1137 */       this.methodName = methodName;
/* 1138 */       this.fileName = fileName;
/* 1139 */       this.lineNumber = lineNumber;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getClassName() {
/* 1148 */       return this.className;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setMethodName(String methodName) {
/* 1157 */       this.methodName = methodName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getFileName() {
/* 1166 */       return this.fileName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFileName(String fileName) {
/* 1175 */       this.fileName = fileName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getLineNumber() {
/* 1184 */       return this.lineNumber;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setLineNumber(int lineNumber) {
/* 1193 */       this.lineNumber = lineNumber;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMethodName() {
/* 1202 */       return this.methodName;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1206 */       return getClassName() + "." + getMethodName() + "(" + ((this.fileName != null && this.lineNumber >= 0) ? (this.fileName + ":" + this.lineNumber) : "unknown source") + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Icons
/*      */     implements Icon
/*      */   {
/* 1217 */     public static final Icon CLASS_ICON = new Icons(Color.blue, "C");
/*      */ 
/*      */ 
/*      */     
/* 1221 */     public static final Icon METHOD_ICON = new Icons(Color.red, "M");
/*      */ 
/*      */ 
/*      */     
/* 1225 */     public static final Icon PACKAGE_ICON = new Icons(Color.yellow, "P");
/*      */ 
/*      */ 
/*      */     
/* 1229 */     public static final Icon FIELD_ICON = new Icons(Color.green, "F");
/*      */     
/* 1231 */     private static final Ellipse2D circle = new Ellipse2D.Double(1.0D, 1.0D, 14.0D, 14.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Color color;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected String label;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Icons(Color color, String label) {
/* 1249 */       this.color = color;
/* 1250 */       this.label = label;
/*      */     }
/*      */     
/*      */     public void paintIcon(Component c, Graphics g, int x, int y) {
/* 1254 */       g.translate(x, y);
/* 1255 */       g.setColor(this.color);
/* 1256 */       Graphics2D g2d = (Graphics2D)g;
/* 1257 */       Object a = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
/* 1258 */       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 1259 */       g2d.fill(circle);
/* 1260 */       g2d.setColor(this.color.darker());
/* 1261 */       g2d.draw(circle);
/* 1262 */       float width = (float)g2d.getFontMetrics().getStringBounds(this.label, g2d).getWidth();
/* 1263 */       g2d.setColor(Color.black);
/* 1264 */       g2d.drawString(this.label, 9.0F - width * 0.5F, 14.0F);
/* 1265 */       g2d.setColor(Color.white);
/* 1266 */       g2d.drawString(this.label, 8.0F - width * 0.5F, 13.0F);
/* 1267 */       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, a);
/* 1268 */       g.translate(-x, -y);
/*      */     }
/*      */     
/*      */     public int getIconWidth() {
/* 1272 */       return 16;
/*      */     }
/*      */     
/*      */     public int getIconHeight() {
/* 1276 */       return 16;
/*      */     }
/*      */   }
/*      */   
/*      */   static interface Mapped {
/*      */     String getName();
/*      */     
/*      */     String getMappedName();
/*      */     
/*      */     Icon getIcon();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/YGuardLogParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
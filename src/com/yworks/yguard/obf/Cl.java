/*      */ package com.yworks.yguard.obf;
/*      */ 
/*      */ import com.yworks.yguard.Conversion;
/*      */ import com.yworks.yguard.obf.classfile.ClassFile;
/*      */ import com.yworks.yguard.obf.classfile.FieldInfo;
/*      */ import com.yworks.yguard.obf.classfile.Logger;
/*      */ import com.yworks.yguard.obf.classfile.MethodInfo;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Cl
/*      */   extends PkCl
/*      */   implements NameListUp, NameListDown
/*      */ {
/*      */   private boolean sourceFileMappingSet;
/*      */   
/*      */   public Set getAttributesToKeep() {
/*   31 */     return this.attributesToKeep;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface ClassResolver
/*      */     extends AutoCloseable
/*      */   {
/*      */     Class resolve(String param1String) throws ClassNotFoundException;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class DefaultClassResolver
/*      */     implements ClassResolver
/*      */   {
/*      */     private DefaultClassResolver() {}
/*      */ 
/*      */     
/*      */     public Class resolve(String className) throws ClassNotFoundException {
/*   51 */       return Class.forName(className, false, getClass().getClassLoader());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws Exception {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean pedantic = false;
/*      */ 
/*      */   
/*   65 */   private static ClassResolver resolver = new DefaultClassResolver();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassResolver getClassResolver() {
/*   74 */     return resolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setPedantic(boolean val) {
/*   83 */     pedantic = val;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setClassResolver(ClassResolver res) {
/*   92 */     if (res != null) {
/*   93 */       resolver = res;
/*      */     } else {
/*   95 */       resolver = new DefaultClassResolver();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  100 */   private Hashtable mds = new Hashtable<>();
/*  101 */   private Hashtable fds = new Hashtable<>();
/*      */   private boolean isResolved = false;
/*      */   private boolean isScanned = false;
/*      */   private String superClass;
/*      */   private String[] superInterfaces;
/*      */   private boolean isInnerClass;
/*      */   private ObfuscationConfig obfuscationConfig;
/*      */   private String sourceFileMapping;
/*      */   private int classFileAccess;
/*      */   private LineNumberTableMapper lineNumberTableMapper;
/*  111 */   private Vector nameListUps = new Vector();
/*  112 */   private Vector nameListDowns = new Vector();
/*      */ 
/*      */ 
/*      */   
/*  116 */   public static int nameSpace = 0;
/*      */   private static NameMaker methodNameMaker;
/*      */   private static NameMaker fieldNameMaker;
/*  119 */   private Map innerClassModifiers = new HashMap<>();
/*  120 */   private Set attributesToKeep = new HashSet();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cl(TreeItem parent, boolean isInnerClass, String name, String superClass, String[] superInterfaces, int modifiers, ObfuscationConfig obfuscationConfig) {
/*  143 */     super(parent, name);
/*  144 */     this.superClass = superClass;
/*  145 */     this.superInterfaces = superInterfaces;
/*  146 */     this.isInnerClass = isInnerClass;
/*  147 */     this.obfuscationConfig = obfuscationConfig;
/*  148 */     this.access = modifiers;
/*  149 */     if (parent == null || name.equals(""))
/*      */     {
/*  151 */       System.err.println("Internal error: class must have parent and name");
/*      */     }
/*  153 */     if (parent instanceof Cl)
/*      */     {
/*  155 */       this.sep = "$";
/*      */     }
/*      */ 
/*      */     
/*  159 */     if (isInnerClass && Character.isDigit(name.charAt(0)))
/*      */     {
/*  161 */       setOutName(getInName());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setClassFileAccess(int classFileAccess) {
/*  171 */     this.classFileAccess = classFileAccess;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LineNumberTableMapper getLineNumberTableMapper() {
/*  180 */     return this.lineNumberTableMapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLineNumberTableMapper(LineNumberTableMapper lineNumberTableMapper) {
/*  189 */     this.lineNumberTableMapper = lineNumberTableMapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSourceFileMapping() {
/*  198 */     return this.sourceFileMapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourceFileMapping(String sourceFileMapping) {
/*  207 */     this.sourceFileMappingSet = true;
/*  208 */     this.sourceFileMapping = sourceFileMapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSourceFileMappingSet() {
/*  217 */     return this.sourceFileMappingSet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSuperClass() {
/*  226 */     return this.superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getInterfaces() {
/*  235 */     return this.superInterfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInnerClassModifiers(Map map) {
/*  244 */     this.innerClassModifiers.putAll(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInnerClassModifier(String fqn) {
/*  254 */     Integer i = (Integer)this.innerClassModifiers.get(fqn);
/*  255 */     if (i == null) {
/*  256 */       return 2;
/*      */     }
/*  258 */     return i.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInnerClass() {
/*  267 */     return this.isInnerClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Md getMethod(String name, String descriptor) {
/*  276 */     return (Md)this.mds.get(name + descriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fd getField(String name) {
/*  284 */     return (Fd)this.fds.get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration getMethodEnum() {
/*  291 */     return this.mds.elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration getFieldEnum() {
/*  298 */     return this.fds.elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWildcardMatch(String pattern) {
/*  307 */     return isMatch(pattern, getFullInName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNRWildcardMatch(String pattern) {
/*  317 */     return isNRMatch(pattern, getFullInName());
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
/*      */   public boolean hasAsSuper(String queryName) throws ClassNotFoundException {
/*  330 */     if (this.superClass == null) return false;
/*      */ 
/*      */     
/*      */     try {
/*  334 */       if (this.superClass.equals(queryName))
/*      */       {
/*  336 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  340 */       Cl superClassItem = this.classTree.getCl(this.superClass);
/*  341 */       if (superClassItem != null)
/*      */       {
/*  343 */         return superClassItem.hasAsSuper(queryName);
/*      */       }
/*      */ 
/*      */       
/*  347 */       Class extSuper = resolver.resolve(ClassFile.translate(this.superClass));
/*  348 */       while (extSuper != null) {
/*      */         
/*  350 */         if (extSuper.getName().equals(ClassFile.translate(queryName)))
/*      */         {
/*  352 */           return true;
/*      */         }
/*  354 */         extSuper = extSuper.getSuperclass();
/*      */       } 
/*  356 */       return false;
/*      */ 
/*      */     
/*      */     }
/*  360 */     catch (ClassNotFoundException cnfe) {
/*      */       
/*  362 */       if (pedantic) {
/*  363 */         throw cnfe;
/*      */       }
/*  365 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cl addClass(Object[] classInfo) {
/*  373 */     return addClass(true, classInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cl addClass(Cl cl) {
/*  384 */     this.cls.put(cl.getInName(), cl);
/*  385 */     return cl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Cl addPlaceholderClass(String name) {
/*  391 */     return addPlaceholderClass(true, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Md addMethod(MethodInfo methodInfo) {
/*  402 */     boolean isSynthetic = methodInfo.isSynthetic();
/*  403 */     String name = methodInfo.getName();
/*  404 */     String descriptor = methodInfo.getDescriptor();
/*  405 */     int access = methodInfo.getAccessFlags();
/*      */     
/*  407 */     if (name.charAt(0) == '<')
/*      */     {
/*  409 */       return null;
/*      */     }
/*  411 */     Md md = getMethod(name, descriptor);
/*  412 */     if (md == null) {
/*      */       
/*  414 */       md = new Md(this, isSynthetic, name, descriptor, access, methodInfo.getObfuscationConfig());
/*  415 */       this.mds.put(name + descriptor, md);
/*      */     } 
/*      */     
/*  418 */     int PublicStatic = 9;
/*  419 */     if ((this.classFileAccess & 0x4000) == 16384 && (access & 0x9) == 9) {
/*      */       
/*  421 */       String desc = "(Ljava/lang/String;)L" + getFullInName() + ';';
/*  422 */       if ("valueOf".equals(name) && desc.equals(descriptor)) {
/*  423 */         md.setOutName(name);
/*  424 */       } else if ("values".equals(name) && descriptor.equals("()[L" + getFullInName() + ';')) {
/*  425 */         md.setOutName(name);
/*      */       } 
/*      */     } 
/*  428 */     return md;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Fd addField(FieldInfo fieldInfo) {
/*  439 */     boolean isSynthetic = fieldInfo.isSynthetic();
/*  440 */     String name = fieldInfo.getName();
/*  441 */     String descriptor = fieldInfo.getDescriptor();
/*  442 */     int access = fieldInfo.getAccessFlags();
/*  443 */     Fd fd = getField(name);
/*  444 */     if (fd == null) {
/*      */       
/*  446 */       fd = new Fd(this, isSynthetic, name, descriptor, access, fieldInfo.getObfuscationConfig());
/*  447 */       this.fds.put(name, fd);
/*      */     } 
/*  449 */     return fd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetResolve() {
/*  457 */     this.isScanned = false;
/*  458 */     this.isResolved = false;
/*  459 */     this.nameListDowns.removeAllElements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setupNameListDowns() {
/*  468 */     if (this.superClass == null) {
/*      */       return;
/*      */     }
/*  471 */     Cl superClassItem = this.classTree.getCl(this.superClass);
/*  472 */     if (superClassItem != null)
/*      */     {
/*  474 */       superClassItem.nameListDowns.addElement(this);
/*      */     }
/*  476 */     for (int i = 0; i < this.superInterfaces.length; i++) {
/*      */       
/*  478 */       Cl interfaceItem = this.classTree.getCl(this.superInterfaces[i]);
/*  479 */       if (interfaceItem != null)
/*      */       {
/*  481 */         interfaceItem.nameListDowns.addElement(this);
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
/*      */   public void resolveOptimally() throws ClassNotFoundException {
/*  496 */     if (!this.isResolved) {
/*      */ 
/*      */       
/*  499 */       Vector<String> methods = new Vector();
/*  500 */       Vector<String> fields = new Vector();
/*  501 */       scanNameSpaceExcept((Cl)null, methods, fields);
/*  502 */       String[] methodNames = new String[methods.size()];
/*  503 */       for (int i = 0; i < methodNames.length; i++)
/*      */       {
/*  505 */         methodNames[i] = methods.elementAt(i);
/*      */       }
/*  507 */       String[] fieldNames = new String[fields.size()];
/*  508 */       for (int j = 0; j < fieldNames.length; j++)
/*      */       {
/*  510 */         fieldNames[j] = fields.elementAt(j);
/*      */       }
/*      */       
/*  513 */       NameMakerFactory nmf = NameMakerFactory.getInstance();
/*      */ 
/*      */       
/*  516 */       methodNameMaker = nmf.getMethodNameMaker(methodNames, getFullInName());
/*  517 */       fieldNameMaker = nmf.getFieldNameMaker(fieldNames, getFullInName());
/*      */ 
/*      */       
/*  520 */       resolveNameSpaceExcept((Cl)null);
/*      */ 
/*      */       
/*  523 */       nameSpace++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanNameSpaceExcept(Cl ignoreCl, Vector methods, Vector fields) throws ClassNotFoundException {
/*  534 */     if (this.superClass == null) {
/*      */       return;
/*      */     }
/*  537 */     if (!this.isScanned) {
/*      */ 
/*      */       
/*  540 */       Cl superCl = this.classTree.getCl(this.superClass);
/*  541 */       if (superCl != null) {
/*      */         
/*  543 */         if (superCl != ignoreCl)
/*      */         {
/*  545 */           superCl.scanNameSpaceExcept(this, methods, fields);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  550 */         scanExtSupers(this.superClass, methods, fields);
/*      */       } 
/*  552 */       for (int i = 0; i < this.superInterfaces.length; i++) {
/*      */         
/*  554 */         Cl interfaceItem = this.classTree.getCl(this.superInterfaces[i]);
/*  555 */         if (interfaceItem != null) {
/*      */           
/*  557 */           if (interfaceItem != ignoreCl) {
/*  558 */             interfaceItem.scanNameSpaceExcept(this, methods, fields);
/*      */           }
/*      */         } else {
/*  561 */           scanExtSupers(this.superInterfaces[i], methods, fields);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  566 */       if (!this.isScanned) {
/*      */         
/*  568 */         scanThis(methods, fields);
/*      */ 
/*      */         
/*  571 */         this.isScanned = true;
/*      */       } 
/*      */ 
/*      */       
/*  575 */       for (Enumeration<Cl> clEnum = this.nameListDowns.elements(); clEnum.hasMoreElements(); ) {
/*      */         
/*  577 */         Cl cl = clEnum.nextElement();
/*  578 */         if (cl != ignoreCl)
/*      */         {
/*  580 */           cl.scanNameSpaceExcept(this, methods, fields);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanExtSupers(String name, Vector methods, Vector fields) throws ClassNotFoundException {
/*      */     try {
/*  592 */       Class extClass = resolver.resolve(ClassFile.translate(name));
/*  593 */       scanExtSupers(extClass, methods, fields);
/*  594 */     } catch (ClassNotFoundException cnfe) {
/*      */       
/*  596 */       if (pedantic) {
/*  597 */         throw cnfe;
/*      */       }
/*  599 */       Logger.getInstance().warningToLogfile("Unresolved external dependency: " + Conversion.toJavaClass(name) + " not found!");
/*  600 */       Logger.getInstance().setUnresolved();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanExtSupers(Class extClass, Vector<String> methods, Vector<String> fields) throws ClassNotFoundException {
/*  610 */     Method[] allPubMethods = extClass.getMethods();
/*  611 */     if (allPubMethods != null) {
/*  612 */       for (int i = 0; i < allPubMethods.length; i++) {
/*  613 */         String methodName = allPubMethods[i].getName();
/*  614 */         if (methods.indexOf(methodName) == -1) {
/*  615 */           methods.addElement(methodName);
/*      */         }
/*      */       } 
/*      */     }
/*  619 */     Field[] allPubFields = extClass.getFields();
/*  620 */     if (allPubFields != null) {
/*  621 */       for (int i = 0; i < allPubFields.length; i++) {
/*  622 */         String fieldName = allPubFields[i].getName();
/*  623 */         if (fields.indexOf(fieldName) == -1) {
/*  624 */           fields.addElement(fieldName);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  630 */     while (extClass != null) {
/*  631 */       Method[] allClassMethods = extClass.getDeclaredMethods();
/*  632 */       if (allClassMethods != null) {
/*  633 */         for (int i = 0; i < allClassMethods.length; i++) {
/*  634 */           if (!Modifier.isPublic(allClassMethods[i].getModifiers())) {
/*  635 */             String methodName = allClassMethods[i].getName();
/*  636 */             if (methods.indexOf(methodName) == -1) {
/*  637 */               methods.addElement(methodName);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*  642 */       Field[] allClassFields = extClass.getDeclaredFields();
/*  643 */       if (allClassFields != null) {
/*  644 */         for (int i = 0; i < allClassFields.length; i++) {
/*  645 */           if (!Modifier.isPublic(allClassFields[i].getModifiers())) {
/*  646 */             String fieldName = allClassFields[i].getName();
/*  647 */             if (fields.indexOf(fieldName) == -1) {
/*  648 */               fields.addElement(fieldName);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*  653 */       extClass = extClass.getSuperclass();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void scanThis(Vector<String> methods, Vector<String> fields) {
/*  660 */     for (Enumeration<Md> mdEnum = this.mds.elements(); mdEnum.hasMoreElements(); ) {
/*      */       
/*  662 */       Md md = mdEnum.nextElement();
/*  663 */       if (md.isFixed()) {
/*      */         
/*  665 */         String name = md.getOutName();
/*  666 */         if (methods.indexOf(name) == -1)
/*      */         {
/*  668 */           methods.addElement(name);
/*      */         }
/*      */       } 
/*      */     } 
/*  672 */     for (Enumeration<Fd> fdEnum = this.fds.elements(); fdEnum.hasMoreElements(); ) {
/*      */       
/*  674 */       Fd fd = fdEnum.nextElement();
/*  675 */       if (fd.isFixed()) {
/*      */         
/*  677 */         String name = fd.getOutName();
/*  678 */         if (fields.indexOf(name) == -1)
/*      */         {
/*  680 */           fields.addElement(name);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resolveNameSpaceExcept(Cl ignoreCl) throws ClassNotFoundException {
/*  690 */     if (this.superClass == null) {
/*      */       return;
/*      */     }
/*  693 */     if (!this.isResolved) {
/*      */ 
/*      */       
/*  696 */       Cl superCl = this.classTree.getCl(this.superClass);
/*  697 */       if (superCl != null && superCl != ignoreCl)
/*      */       {
/*  699 */         superCl.resolveNameSpaceExcept(this);
/*      */       }
/*  701 */       for (int i = 0; i < this.superInterfaces.length; i++) {
/*      */         
/*  703 */         Cl interfaceItem = this.classTree.getCl(this.superInterfaces[i]);
/*  704 */         if (interfaceItem != null && interfaceItem != ignoreCl)
/*      */         {
/*  706 */           interfaceItem.resolveNameSpaceExcept(this);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  711 */       if (!this.isResolved) {
/*      */ 
/*      */ 
/*      */         
/*  715 */         resolveThis();
/*      */ 
/*      */         
/*  718 */         this.isResolved = true;
/*      */       } 
/*      */ 
/*      */       
/*  722 */       for (Enumeration<Cl> clEnum = this.nameListDowns.elements(); clEnum.hasMoreElements(); ) {
/*      */         
/*  724 */         Cl cl = clEnum.nextElement();
/*  725 */         if (cl != ignoreCl)
/*      */         {
/*  727 */           cl.resolveNameSpaceExcept(this);
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
/*      */   private void resolveThis() throws ClassNotFoundException {
/*  739 */     if (this.superClass == null)
/*      */       return; 
/*  741 */     Cl superClassItem = this.classTree.getCl(this.superClass);
/*  742 */     this.nameListUps.addElement((superClassItem != null) ? 
/*  743 */         superClassItem : 
/*  744 */         getExtNameListUp(this.superClass));
/*  745 */     for (int i = 0; i < this.superInterfaces.length; i++) {
/*      */       
/*  747 */       Cl interfaceItem = this.classTree.getCl(this.superInterfaces[i]);
/*  748 */       this.nameListUps.addElement((interfaceItem != null) ? 
/*  749 */           interfaceItem : 
/*  750 */           getExtNameListUp(this.superInterfaces[i]));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  756 */     for (Enumeration<Md> mdEnum = this.mds.elements(); mdEnum.hasMoreElements(); ) {
/*      */       
/*  758 */       Md md = mdEnum.nextElement();
/*  759 */       if (!md.isFixed()) {
/*      */         
/*  761 */         if (!Modifier.isPrivate(md.getModifiers())) {
/*      */           
/*  763 */           for (Enumeration<NameListDown> enumeration = this.nameListDowns.elements(); enumeration.hasMoreElements(); ) {
/*  764 */             String theOutName = ((NameListDown)enumeration.nextElement()).getMethodObfNameDown(this, md.getInName(), md
/*  765 */                 .getDescriptor());
/*  766 */             if (theOutName != null) {
/*  767 */               md.setOutName(theOutName);
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*  772 */           for (Enumeration<NameListUp> nlEnum = this.nameListUps.elements(); nlEnum.hasMoreElements(); ) {
/*  773 */             String theOutName = ((NameListUp)nlEnum.nextElement()).getMethodOutNameUp(md.getInName(), md
/*  774 */                 .getDescriptor());
/*  775 */             if (theOutName != null) {
/*  776 */               md.setOutName(theOutName);
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  782 */         md.setOutName(methodNameMaker.nextName(md.getDescriptor())); continue;
/*      */       } 
/*  784 */       if (Modifier.isNative(md.access))
/*      */       {
/*  786 */         if (!md.getParent().getFullOutName().equals(md.getParent().getFullInName())) {
/*  787 */           Logger.getInstance().warning("Method " + md
/*  788 */               .getOutName() + " is native but " + md.getParent().getFullInName() + " is not kept/exposed.");
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  794 */     for (Enumeration<Fd> fdEnum = this.fds.elements(); fdEnum.hasMoreElements(); ) {
/*      */       
/*  796 */       Fd fd = fdEnum.nextElement();
/*  797 */       if (!fd.isFixed()) {
/*      */ 
/*      */         
/*  800 */         if (!Modifier.isPrivate(fd.getModifiers())) {
/*      */           
/*  802 */           for (Enumeration<NameListDown> enumeration = this.nameListDowns.elements(); enumeration.hasMoreElements(); ) {
/*      */             
/*  804 */             String theOutName = ((NameListDown)enumeration.nextElement()).getFieldObfNameDown(this, fd.getInName());
/*  805 */             if (theOutName != null)
/*      */             {
/*  807 */               fd.setOutName(theOutName);
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*  812 */           for (Enumeration<NameListUp> nlEnum = this.nameListUps.elements(); nlEnum.hasMoreElements(); ) {
/*      */             
/*  814 */             String superOutName = ((NameListUp)nlEnum.nextElement()).getFieldOutNameUp(fd.getInName());
/*  815 */             if (superOutName != null)
/*      */             {
/*  817 */               fd.setOutName(superOutName);
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  823 */         fd.setOutName(fieldNameMaker.nextName(null));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMethodOutNameUp(String name, String descriptor) throws ClassNotFoundException {
/*  832 */     Md md = getMethod(name, descriptor);
/*  833 */     if (md != null && !Modifier.isPrivate(md.access))
/*      */     {
/*  835 */       return md.getOutName();
/*      */     }
/*      */ 
/*      */     
/*  839 */     for (Enumeration<NameListUp> enumeration = this.nameListUps.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  841 */       String superOutName = ((NameListUp)enumeration.nextElement()).getMethodOutNameUp(name, descriptor);
/*  842 */       if (superOutName != null)
/*      */       {
/*  844 */         return superOutName;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  849 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMethodObfNameUp(String name, String descriptor) throws ClassNotFoundException {
/*  856 */     Md md = getMethod(name, descriptor);
/*  857 */     if (md != null && !Modifier.isPrivate(md.access))
/*      */     {
/*  859 */       return md.getObfName();
/*      */     }
/*      */ 
/*      */     
/*  863 */     for (Enumeration<NameListUp> enumeration = this.nameListUps.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  865 */       String superObfName = ((NameListUp)enumeration.nextElement()).getMethodObfNameUp(name, descriptor);
/*  866 */       if (superObfName != null)
/*      */       {
/*  868 */         return superObfName;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  873 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFieldOutNameUp(String name) throws ClassNotFoundException {
/*  880 */     for (Enumeration<NameListUp> enumeration = this.nameListUps.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  882 */       String superOutName = ((NameListUp)enumeration.nextElement()).getFieldOutNameUp(name);
/*  883 */       if (superOutName != null)
/*      */       {
/*  885 */         return superOutName;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  890 */     Fd fd = getField(name);
/*  891 */     if (fd != null && !Modifier.isPrivate(fd.access))
/*      */     {
/*  893 */       return fd.getOutName();
/*      */     }
/*      */ 
/*      */     
/*  897 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFieldObfNameUp(String name) throws ClassNotFoundException {
/*  905 */     for (Enumeration<NameListUp> enumeration = this.nameListUps.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  907 */       String superObfName = ((NameListUp)enumeration.nextElement()).getFieldObfNameUp(name);
/*  908 */       if (superObfName != null)
/*      */       {
/*  910 */         return superObfName;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  915 */     Fd fd = getField(name);
/*  916 */     if (fd != null && !Modifier.isPrivate(fd.access))
/*      */     {
/*  918 */       return fd.getObfName();
/*      */     }
/*      */ 
/*      */     
/*  922 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMethodObfNameDown(Cl caller, String name, String descriptor) throws ClassNotFoundException {
/*  930 */     Md md = getMethod(name, descriptor);
/*  931 */     if (md != null && md.isFixed())
/*      */     {
/*  933 */       return md.getOutName();
/*      */     }
/*      */ 
/*      */     
/*  937 */     String theObfName = null;
/*  938 */     if (this.superClass != null) {
/*      */       
/*  940 */       Cl superClassItem = this.classTree.getCl(this.superClass);
/*  941 */       if (superClassItem != caller) {
/*      */         
/*  943 */         NameListUp nl = (superClassItem != null) ? superClassItem : getExtNameListUp(this.superClass);
/*  944 */         theObfName = nl.getMethodObfNameUp(name, descriptor);
/*  945 */         if (theObfName != null)
/*      */         {
/*  947 */           return theObfName;
/*      */         }
/*      */       } 
/*  950 */       for (int i = 0; i < this.superInterfaces.length; i++) {
/*      */         
/*  952 */         Cl interfaceItem = this.classTree.getCl(this.superInterfaces[i]);
/*  953 */         if (interfaceItem != caller) {
/*      */           
/*  955 */           NameListUp nl = (interfaceItem != null) ? interfaceItem : getExtNameListUp(this.superInterfaces[i]);
/*  956 */           theObfName = nl.getMethodObfNameUp(name, descriptor);
/*  957 */           if (theObfName != null)
/*      */           {
/*  959 */             return theObfName;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  966 */     for (Enumeration<NameListDown> enumeration = this.nameListDowns.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  968 */       theObfName = ((NameListDown)enumeration.nextElement()).getMethodObfNameDown(this, name, descriptor);
/*  969 */       if (theObfName != null)
/*      */       {
/*  971 */         return theObfName;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  976 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFieldObfNameDown(Cl caller, String name) throws ClassNotFoundException {
/*  983 */     Fd fd = getField(name);
/*  984 */     if (fd != null && fd.isFixed())
/*      */     {
/*  986 */       return fd.getOutName();
/*      */     }
/*      */ 
/*      */     
/*  990 */     String theObfName = null;
/*  991 */     if (this.superClass != null) {
/*      */       
/*  993 */       Cl superClassItem = this.classTree.getCl(this.superClass);
/*  994 */       if (superClassItem != caller) {
/*      */         
/*  996 */         NameListUp nl = (superClassItem != null) ? superClassItem : getExtNameListUp(this.superClass);
/*  997 */         theObfName = nl.getFieldObfNameUp(name);
/*  998 */         if (theObfName != null)
/*      */         {
/* 1000 */           return theObfName;
/*      */         }
/*      */       } 
/* 1003 */       for (int i = 0; i < this.superInterfaces.length; i++) {
/*      */         
/* 1005 */         Cl interfaceItem = this.classTree.getCl(this.superInterfaces[i]);
/* 1006 */         if (interfaceItem != caller) {
/*      */           
/* 1008 */           NameListUp nl = (interfaceItem != null) ? interfaceItem : getExtNameListUp(this.superInterfaces[i]);
/* 1009 */           theObfName = nl.getFieldObfNameUp(name);
/* 1010 */           if (theObfName != null)
/*      */           {
/* 1012 */             return theObfName;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1019 */     for (Enumeration<NameListDown> enumeration = this.nameListDowns.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/* 1021 */       theObfName = ((NameListDown)enumeration.nextElement()).getFieldObfNameDown(this, name);
/* 1022 */       if (theObfName != null)
/*      */       {
/* 1024 */         return theObfName;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1029 */     return null;
/*      */   }
/*      */ 
/*      */   
/* 1033 */   private static Hashtable extNameListUpCache = new Hashtable<>();
/*      */   
/*      */   private NameListUp getExtNameListUp(String name) throws ClassNotFoundException {
/* 1036 */     NameListUp nl = (NameListUp)extNameListUpCache.get(name);
/* 1037 */     if (nl == null) {
/*      */       
/* 1039 */       nl = new ExtNameListUp(name);
/* 1040 */       extNameListUpCache.put(name, nl);
/*      */     } 
/* 1042 */     return nl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class ExtNameListUp
/*      */     implements NameListUp
/*      */   {
/*      */     private Class extClass;
/*      */     
/* 1052 */     private Method[] methods = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ExtNameListUp(String name) throws ClassNotFoundException {
/*      */       try {
/* 1064 */         this.extClass = Cl.resolver.resolve(ClassFile.translate(name));
/*      */       }
/* 1066 */       catch (ClassNotFoundException cnfe) {
/*      */         
/* 1068 */         if (Cl.pedantic) {
/* 1069 */           throw cnfe;
/*      */         }
/* 1071 */         Logger.getInstance().warningToLogfile("Unresolved external dependency: " + Conversion.toJavaClass(name) + " not found!");
/* 1072 */         Logger.getInstance().setUnresolved();
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
/*      */     public ExtNameListUp(Class extClass) {
/* 1084 */       this.extClass = extClass;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMethodObfNameUp(String name, String descriptor) {
/* 1090 */       return getMethodOutNameUp(name, descriptor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMethodOutNameUp(String name, String descriptor) {
/* 1097 */       if (this.extClass == null) return name;
/*      */ 
/*      */       
/* 1100 */       if (this.methods == null) {
/*      */         
/* 1102 */         this.methods = getAllDeclaredMethods(this.extClass);
/* 1103 */         Vector<Method> pruned = new Vector(); int j;
/* 1104 */         for (j = 0; j < this.methods.length; j++) {
/*      */           
/* 1106 */           int modifiers = this.methods[j].getModifiers();
/* 1107 */           if (!Modifier.isPrivate(modifiers))
/*      */           {
/* 1109 */             pruned.addElement(this.methods[j]);
/*      */           }
/*      */         } 
/* 1112 */         this.methods = new Method[pruned.size()];
/* 1113 */         for (j = 0; j < this.methods.length; j++)
/*      */         {
/* 1115 */           this.methods[j] = pruned.elementAt(j);
/*      */         }
/*      */       } 
/*      */       
/*      */       int i;
/*      */       
/* 1121 */       label38: for (i = 0; i < this.methods.length; i++) {
/*      */         
/* 1123 */         if (name.equals(this.methods[i].getName())) {
/*      */           
/* 1125 */           String[] paramAndReturnNames = ClassFile.parseDescriptor(descriptor);
/* 1126 */           Class[] paramTypes = this.methods[i].getParameterTypes();
/* 1127 */           Class<?> returnType = this.methods[i].getReturnType();
/* 1128 */           if (paramAndReturnNames.length == paramTypes.length + 1) {
/*      */             
/* 1130 */             for (int j = 0; j < paramAndReturnNames.length - 1; j++) {
/*      */               
/* 1132 */               if (!paramAndReturnNames[j].equals(paramTypes[j].getName())) {
/*      */                 continue label38;
/*      */               }
/*      */             } 
/*      */             
/* 1137 */             String returnName = returnType.getName();
/* 1138 */             if (paramAndReturnNames[paramAndReturnNames.length - 1].equals(returnName))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1144 */               return name;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1150 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getFieldObfNameUp(String name) {
/* 1156 */       return getFieldOutNameUp(name);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getFieldOutNameUp(String name) {
/* 1162 */       if (this.extClass == null) return name;
/*      */       
/* 1164 */       Field field = getAllDeclaredField(this.extClass, name);
/* 1165 */       if (field != null) {
/*      */ 
/*      */         
/* 1168 */         int modifiers = field.getModifiers();
/* 1169 */         if (!Modifier.isPrivate(modifiers))
/*      */         {
/* 1171 */           return name;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1176 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private Method[] getAllDeclaredMethods(Class theClass) {
/* 1182 */       Vector<Method[]> ma = new Vector();
/* 1183 */       int length = 0;
/*      */ 
/*      */       
/* 1186 */       Method[] allPubMethods = theClass.getMethods();
/* 1187 */       ma.addElement(allPubMethods);
/* 1188 */       length += allPubMethods.length;
/*      */ 
/*      */ 
/*      */       
/* 1192 */       while (theClass != null) {
/*      */         
/* 1194 */         Method[] methods = theClass.getDeclaredMethods();
/* 1195 */         ma.addElement(methods);
/* 1196 */         length += methods.length;
/* 1197 */         theClass = theClass.getSuperclass();
/*      */       } 
/*      */ 
/*      */       
/* 1201 */       Method[] allMethods = new Method[length];
/* 1202 */       int pos = 0;
/* 1203 */       for (Enumeration<Method> enumeration = ma.elements(); enumeration.hasMoreElements(); ) {
/*      */         
/* 1205 */         Method[] methods = (Method[])enumeration.nextElement();
/* 1206 */         System.arraycopy(methods, 0, allMethods, pos, methods.length);
/* 1207 */         pos += methods.length;
/*      */       } 
/* 1209 */       return allMethods;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private Field getAllDeclaredField(Class theClass, String name) {
/* 1215 */       Class origClass = theClass;
/*      */ 
/*      */       
/* 1218 */       while (theClass != null) {
/*      */         
/* 1220 */         Field field = null;
/*      */         
/*      */         try {
/* 1223 */           field = theClass.getDeclaredField(name);
/*      */         }
/* 1225 */         catch (Exception e) {
/*      */           
/* 1227 */           field = null;
/*      */         } 
/* 1229 */         if (field != null)
/*      */         {
/* 1231 */           return field;
/*      */         }
/* 1233 */         theClass = theClass.getSuperclass();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1240 */         return origClass.getField(name);
/*      */       }
/* 1242 */       catch (SecurityException nsfe) {
/*      */         
/* 1244 */         return null;
/*      */       }
/* 1246 */       catch (NoSuchFieldException nsfe) {
/*      */         
/* 1248 */         return null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObfuscationConfig getObfuscationConfig() {
/* 1259 */     return this.obfuscationConfig;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/Cl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
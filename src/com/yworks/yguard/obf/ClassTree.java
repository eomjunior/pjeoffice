/*      */ package com.yworks.yguard.obf;
/*      */ 
/*      */ import com.yworks.yguard.Conversion;
/*      */ import com.yworks.yguard.ParseException;
/*      */ import com.yworks.yguard.obf.classfile.ClassFile;
/*      */ import com.yworks.yguard.obf.classfile.ClassItemInfo;
/*      */ import com.yworks.yguard.obf.classfile.FieldInfo;
/*      */ import com.yworks.yguard.obf.classfile.LineNumberTableAttrInfo;
/*      */ import com.yworks.yguard.obf.classfile.Logger;
/*      */ import com.yworks.yguard.obf.classfile.MethodInfo;
/*      */ import com.yworks.yguard.obf.classfile.NameMapper;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassTree
/*      */   implements NameMapper
/*      */ {
/*      */   public static final char PACKAGE_LEVEL = '/';
/*      */   public static final char CLASS_LEVEL = '$';
/*      */   public static final char METHOD_FIELD_LEVEL = '/';
/*   53 */   private Vector retainAttrs = new Vector();
/*   54 */   private Pk root = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Enumeration getNameEnum(String name) {
/*   66 */     Vector<Cons> vec = new Vector();
/*   67 */     String nameOrig = name;
/*   68 */     while (!name.equals("")) {
/*      */       
/*   70 */       int posP = name.indexOf('/');
/*   71 */       int posC = name.indexOf('$');
/*   72 */       Cons cons = null;
/*   73 */       if (posP == -1 && posC == 0) {
/*      */ 
/*      */ 
/*      */         
/*   77 */         int innerClassIndex = name.indexOf('$', 1);
/*   78 */         int endIndex = (innerClassIndex > 0) ? innerClassIndex : name.length();
/*   79 */         cons = new Cons(new Character('$'), name.substring(0, endIndex));
/*   80 */         name = name.substring(endIndex);
/*      */       } 
/*   82 */       if (posP == -1 && posC == -1) {
/*      */         
/*   84 */         cons = new Cons(new Character('$'), name);
/*   85 */         name = "";
/*      */       } 
/*   87 */       if (posP == -1 && posC > 0) {
/*      */         
/*   89 */         cons = new Cons(new Character('$'), name.substring(0, posC));
/*      */ 
/*      */         
/*   92 */         for (; posC + 1 < name.length() && name.charAt(posC + 1) == '$'; posC++);
/*   93 */         name = name.substring(posC + 1, name.length());
/*      */       } 
/*   95 */       if (posP != -1 && posC == -1) {
/*      */         
/*   97 */         cons = new Cons(new Character('/'), name.substring(0, posP));
/*   98 */         name = name.substring(posP + 1, name.length());
/*      */       } 
/*  100 */       if (posP != -1 && posC != -1)
/*      */       {
/*  102 */         if (posP < posC) {
/*      */           
/*  104 */           cons = new Cons(new Character('/'), name.substring(0, posP));
/*  105 */           name = name.substring(posP + 1, name.length());
/*      */         }
/*      */         else {
/*      */           
/*  109 */           throw new IllegalArgumentException("Invalid fully qualified name (a): " + nameOrig);
/*      */         } 
/*      */       }
/*      */       
/*  113 */       if (((String)cons.cdr).equals(""))
/*      */       {
/*  115 */         throw new IllegalArgumentException("Invalid fully qualified name (b): " + nameOrig);
/*      */       }
/*      */       
/*  118 */       vec.addElement(cons);
/*      */     } 
/*  120 */     return vec.elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassTree() {
/*  131 */     this.root = Pk.createRoot(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Pk getRoot() {
/*  139 */     return this.root;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TreeItem findTreeItem(String[] nameParts) {
/*  148 */     TreeItem tmp = this.root;
/*  149 */     for (int i = 0; tmp != null && i < nameParts.length; i++) {
/*  150 */       String name = nameParts[i];
/*  151 */       tmp = findSubItem(tmp, name);
/*      */     } 
/*  153 */     return tmp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cl findClassForName(String name) {
/*  163 */     int dindex = name.indexOf('$');
/*  164 */     String innerClass = null;
/*  165 */     if (dindex > 0) {
/*  166 */       innerClass = name.substring(dindex + 1);
/*  167 */       name = name.substring(0, dindex);
/*      */     } 
/*  169 */     int pindex = name.lastIndexOf('.');
/*  170 */     String packageName = null;
/*  171 */     if (pindex > 0) {
/*  172 */       packageName = name.substring(0, pindex);
/*  173 */       name = name.substring(pindex + 1);
/*      */     } 
/*  175 */     Pk pk = this.root;
/*  176 */     if (packageName != null)
/*  177 */       for (StringTokenizer st = new StringTokenizer(packageName, ".", false); st.hasMoreTokens(); ) {
/*  178 */         String token = st.nextToken();
/*  179 */         pk = findPackage(pk, token);
/*  180 */         if (pk == null) return null;
/*      */       
/*      */       }  
/*  183 */     Cl cl = findClass(pk, name);
/*  184 */     if (cl != null && innerClass != null)
/*  185 */       for (StringTokenizer st = new StringTokenizer(innerClass, "$", false); st.hasMoreTokens(); ) {
/*  186 */         String token = st.nextToken();
/*  187 */         cl = findClass(cl, token);
/*  188 */         if (cl == null) return null;
/*      */       
/*      */       }  
/*  191 */     return cl;
/*      */   }
/*      */   
/*      */   private Pk findPackage(TreeItem parent, String pName) {
/*  195 */     if (parent instanceof Pk) {
/*  196 */       for (Enumeration<Pk> enumeration = ((Pk)parent).getPackageEnum(); enumeration.hasMoreElements(); ) {
/*  197 */         Pk subPk = enumeration.nextElement();
/*  198 */         if (subPk.getInName().equals(pName)) {
/*  199 */           return subPk;
/*      */         }
/*      */       } 
/*      */     }
/*  203 */     return null;
/*      */   }
/*      */   
/*      */   private Cl findClass(PkCl parent, String pName) {
/*  207 */     for (Enumeration<Cl> enumeration = parent.getClassEnum(); enumeration.hasMoreElements(); ) {
/*  208 */       Cl cl = enumeration.nextElement();
/*  209 */       if (cl.getInName().equals(pName)) {
/*  210 */         return cl;
/*      */       }
/*      */     } 
/*  213 */     return null;
/*      */   }
/*      */   
/*      */   private TreeItem findSubItem(TreeItem parent, String childName) {
/*  217 */     if (parent instanceof Pk) {
/*  218 */       for (Enumeration<Pk> enumeration1 = ((Pk)parent).getPackageEnum(); enumeration1.hasMoreElements(); ) {
/*  219 */         Pk subPk = enumeration1.nextElement();
/*  220 */         if (subPk.getInName().equals(childName)) {
/*  221 */           return subPk;
/*      */         }
/*      */       } 
/*  224 */       for (Enumeration<Cl> enumeration = ((Pk)parent).getClassEnum(); enumeration.hasMoreElements(); ) {
/*  225 */         Cl cl = enumeration.nextElement();
/*  226 */         if (cl.getInName().equals(childName)) {
/*  227 */           return cl;
/*      */         }
/*      */       } 
/*      */     } 
/*  231 */     if (parent instanceof Cl) {
/*  232 */       for (Enumeration<Cl> enumeration = ((Cl)parent).getClassEnum(); enumeration.hasMoreElements(); ) {
/*  233 */         Cl cl = enumeration.nextElement();
/*  234 */         if (cl.getInName().equals(childName)) {
/*  235 */           return cl;
/*      */         }
/*      */       } 
/*  238 */       return null;
/*      */     } 
/*  240 */     return null;
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
/*      */   public String getOutName(String inName) {
/*      */     try {
/*  253 */       TreeItem ti = this.root;
/*  254 */       StringBuffer sb = new StringBuffer();
/*  255 */       for (Enumeration<Cons> nameEnum = getNameEnum(inName); nameEnum.hasMoreElements(); )
/*      */       {
/*  257 */         Cons nameSegment = nameEnum.nextElement();
/*  258 */         char tag = ((Character)nameSegment.car).charValue();
/*  259 */         String name = (String)nameSegment.cdr;
/*  260 */         switch (tag) {
/*      */           
/*      */           case '/':
/*  263 */             if (ti != null) {
/*      */               
/*  265 */               ti = ((Pk)ti).getPackage(name);
/*  266 */               if (ti != null)
/*      */               {
/*  268 */                 sb.append(ti.getOutName());
/*      */               }
/*      */               else
/*      */               {
/*  272 */                 sb.append(name);
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  277 */               sb.append(name);
/*      */             } 
/*  279 */             sb.append('/');
/*      */             continue;
/*      */           
/*      */           case '$':
/*  283 */             sb.append(name);
/*  284 */             return sb.toString();
/*      */         } 
/*      */         
/*  287 */         throw new RuntimeException("Internal error: illegal package/class name tag");
/*      */       }
/*      */     
/*      */     }
/*  291 */     catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */     
/*  295 */     return inName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addClassFile(ClassFile cf) {
/*  306 */     TreeItem ti = this.root;
/*  307 */     char parentTag = '/';
/*  308 */     for (Enumeration<Cons> nameEnum = getNameEnum(cf.getName()); nameEnum.hasMoreElements(); ) {
/*      */       Object[] classInfo; Cl cl;
/*  310 */       Cons nameSegment = nameEnum.nextElement();
/*  311 */       char tag = ((Character)nameSegment.car).charValue();
/*  312 */       String name = (String)nameSegment.cdr;
/*  313 */       switch (tag) {
/*      */         
/*      */         case '/':
/*  316 */           ti = ((Pk)ti).addPackage(name);
/*      */           break;
/*      */ 
/*      */         
/*      */         case '$':
/*  321 */           if (nameEnum.hasMoreElements()) {
/*      */             
/*  323 */             ti = ((PkCl)ti).addPlaceholderClass(name);
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  328 */           classInfo = new Object[] { name, cf.getSuper(), cf.getInterfaces(), Integer.valueOf(cf.getModifiers()), ClassItemInfo.getObfuscationConfig(cf.getName(), cf.getAttributes()) };
/*      */           
/*  330 */           cl = ((PkCl)ti).addClass(classInfo);
/*  331 */           cl.setInnerClassModifiers(cf.getInnerClassModifiers());
/*  332 */           cl.setClassFileAccess(cf.getClassFileAccess());
/*  333 */           ti = cl;
/*      */           break;
/*      */ 
/*      */         
/*      */         default:
/*  338 */           throw new ParseException("Internal error: illegal package/class name tag");
/*      */       } 
/*  340 */       parentTag = tag;
/*      */     } 
/*      */ 
/*      */     
/*  344 */     if (ti instanceof Cl) {
/*      */       
/*  346 */       Cl cl = (Cl)ti;
/*  347 */       cl.access = cf.getModifiers();
/*      */ 
/*      */       
/*  350 */       for (Enumeration<MethodInfo> enumeration1 = cf.getMethodEnum(); enumeration1.hasMoreElements();)
/*      */       {
/*  352 */         cl.addMethod(enumeration1.nextElement());
/*      */       }
/*      */ 
/*      */       
/*  356 */       for (Enumeration<FieldInfo> enumeration = cf.getFieldEnum(); enumeration.hasMoreElements();)
/*      */       {
/*  358 */         cl.addField(enumeration.nextElement());
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  363 */       throw new ParseException("Inconsistent class file.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainAttribute(String name) {
/*  374 */     this.retainAttrs.addElement(name);
/*      */   }
/*      */   
/*      */   private boolean modifierMatch(int level, int mods) {
/*  378 */     if (level == 0) return false; 
/*  379 */     if (Modifier.isPublic(mods)) {
/*  380 */       return ((level & 0x1) == 1);
/*      */     }
/*  382 */     if (Modifier.isProtected(mods)) {
/*  383 */       return ((level & 0x4) == 4);
/*      */     }
/*  385 */     if (Modifier.isPrivate(mods)) {
/*  386 */       return ((level & 0x2) == 2);
/*      */     }
/*      */     
/*  389 */     return ((level & 0x1000) == 4096);
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
/*      */   public void retainClass(String name, int classLevel, int methodLevel, int fieldLevel, boolean retainHierarchy) {
/*  405 */     for (Enumeration<Cl> clEnum = getClEnum(name, classLevel); clEnum.hasMoreElements(); ) {
/*      */       
/*  407 */       Cl classItem = clEnum.nextElement();
/*  408 */       if (retainHierarchy && classLevel != 0) retainHierarchy(classItem);
/*      */       
/*  410 */       if (methodLevel != 0) {
/*      */         
/*  412 */         for (Enumeration<Md> enumeration = classItem.getMethodEnum(); enumeration.hasMoreElements(); ) {
/*      */           
/*  414 */           Md md = enumeration.nextElement();
/*  415 */           if (modifierMatch(methodLevel, md.getModifiers())) {
/*      */             
/*  417 */             md.setOutName(md.getInName());
/*  418 */             md.setFromScript();
/*      */           } 
/*      */         } 
/*      */         
/*  422 */         if ((methodLevel & 0x1005) != 0 || (fieldLevel & 0x1005) != 0) {
/*      */           
/*  424 */           int mask = 2;
/*  425 */           int ml = methodLevel & (mask ^ 0xFFFFFFFF);
/*  426 */           int fl = fieldLevel & (mask ^ 0xFFFFFFFF);
/*  427 */           int cl = classLevel & (mask ^ 0xFFFFFFFF);
/*  428 */           String[] interfaces = classItem.getInterfaces();
/*  429 */           if (interfaces != null) {
/*  430 */             for (int i = 0; i < interfaces.length; i++) {
/*  431 */               String interfaceClass = interfaces[i];
/*  432 */               retainClass(interfaceClass, cl, ml, fl, false);
/*      */             } 
/*      */           }
/*  435 */           String superClass = classItem.getSuperClass();
/*  436 */           if (superClass != null) {
/*      */             
/*  438 */             if (!superClass.startsWith(classItem.getParent().getFullInName())) {
/*  439 */               mask |= 0x1000;
/*  440 */               ml = methodLevel & (mask ^ 0xFFFFFFFF);
/*  441 */               fl = fieldLevel & (mask ^ 0xFFFFFFFF);
/*  442 */               cl = classLevel & (mask ^ 0xFFFFFFFF);
/*      */             } 
/*  444 */             retainClass(superClass, cl, ml, fl, false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  450 */       if (fieldLevel != 0)
/*      */       {
/*  452 */         for (Enumeration<Fd> enumeration = classItem.getFieldEnum(); enumeration.hasMoreElements(); ) {
/*      */           
/*  454 */           Fd fd = enumeration.nextElement();
/*  455 */           if (modifierMatch(fieldLevel, fd.getModifiers())) {
/*      */             
/*  457 */             fd.setOutName(fd.getInName());
/*  458 */             fd.setFromScript();
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
/*      */   public void retainMethod(String name, String descriptor) {
/*  473 */     Enumeration<Md> enumeration = getMdEnum(name, descriptor);
/*  474 */     while (enumeration.hasMoreElements()) {
/*  475 */       Md md = enumeration.nextElement();
/*  476 */       md.setOutName(md.getInName());
/*  477 */       md.setFromScript();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainField(String name) {
/*  488 */     for (Enumeration<Fd> enumeration = getFdEnum(name); enumeration.hasMoreElements(); ) {
/*  489 */       Fd fd = enumeration.nextElement();
/*  490 */       fd.setOutName(fd.getInName());
/*  491 */       fd.setFromScript();
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
/*      */   public void retainPackageMap(String name, String obfName) {
/*  503 */     retainItemMap(getPk(name), obfName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainClassMap(String name, String obfName) {
/*  514 */     retainItemMap(getCl(name), obfName);
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
/*      */   public void retainMethodMap(String name, String descriptor, String obfName) {
/*  526 */     retainItemMap(getMd(name, descriptor), obfName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainFieldMap(String name, String obfName) {
/*  537 */     retainItemMap(getFd(name), obfName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void retainItemMap(TreeItem item, String obfName) {
/*  543 */     if (!item.isFixed()) {
/*      */       
/*  545 */       item.setOutName(obfName);
/*  546 */       item.setFromScriptMap();
/*      */     }
/*  548 */     else if (!item.getOutName().equals(obfName)) {
/*  549 */       item.setOutName(obfName);
/*  550 */       item.setFromScriptMap();
/*      */       
/*  552 */       Logger.getInstance().warning("'" + item.getFullInName() + "' will be remapped to '" + obfName + "' according to mapping rule!");
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
/*      */   public void generateNames() {
/*  567 */     walkTree(new TreeAction() {
/*  568 */           public void packageAction(Pk pk) { pk.generateNames(); } public void classAction(Cl cl) {
/*  569 */             cl.generateNames();
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resolveClasses() throws ClassNotFoundException {
/*  580 */     walkTree(new TreeAction() {
/*  581 */           public void classAction(Cl cl) { cl.resetResolve(); }
/*      */         });
/*  583 */     walkTree(new TreeAction() {
/*  584 */           public void classAction(Cl cl) { cl.setupNameListDowns(); }
/*      */         });
/*  586 */     Cl.nameSpace = 0;
/*  587 */     final ClassNotFoundException[] ex = new ClassNotFoundException[1];
/*      */     try {
/*  589 */       walkTree(new TreeAction() {
/*      */             public void classAction(Cl cl) {
/*      */               try {
/*  592 */                 cl.resolveOptimally();
/*  593 */               } catch (ClassNotFoundException cnfe) {
/*  594 */                 ex[0] = cnfe;
/*  595 */                 throw new RuntimeException();
/*      */               } 
/*      */             }
/*      */           });
/*  599 */     } catch (RuntimeException rte) {
/*  600 */       if (ex[0] != null) {
/*  601 */         throw ex[0];
/*      */       }
/*  603 */       throw rte;
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
/*      */   public String[] getAttrsToKeep() {
/*  615 */     String[] attrs = new String[this.retainAttrs.size()];
/*  616 */     for (int i = 0; i < attrs.length; i++)
/*      */     {
/*  618 */       attrs[i] = this.retainAttrs.elementAt(i);
/*      */     }
/*  620 */     return attrs;
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
/*      */   public Enumeration getClEnum(String fullName) {
/*  632 */     return getClEnum(fullName, 4103);
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
/*      */   public Enumeration getClEnum(String fullName, final int classMode) {
/*  645 */     final Vector<Cl> vec = new Vector();
/*      */ 
/*      */ 
/*      */     
/*  649 */     if (fullName.indexOf('*') != -1) {
/*      */       
/*  651 */       if (fullName.indexOf('!') == 0) {
/*  652 */         final String fName = fullName.substring(1);
/*  653 */         walkTree(new TreeAction() {
/*      */               public void classAction(Cl cl) {
/*  655 */                 if (cl.isWildcardMatch(fName) && ClassTree.this.modifierMatch(classMode, cl.getModifiers())) {
/*  656 */                   vec.addElement(cl);
/*      */                 
/*      */                 }
/*      */               }
/*      */             });
/*      */       }
/*      */       else {
/*      */         
/*  664 */         final String fName = fullName;
/*  665 */         walkTree(new TreeAction() {
/*      */               public void classAction(Cl cl) {
/*  667 */                 if (cl.isNRWildcardMatch(fName) && ClassTree.this.modifierMatch(classMode, cl.getModifiers())) {
/*  668 */                   vec.addElement(cl);
/*      */                 }
/*      */               }
/*      */             });
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  677 */       Cl cl = getCl(fullName);
/*  678 */       if (cl != null) {
/*      */         
/*  680 */         int mods = cl.getModifiers();
/*  681 */         if (cl.isInnerClass()) {
/*  682 */           Cl cl1 = (Cl)cl.getParent();
/*      */         }
/*  684 */         boolean match = modifierMatch(classMode, cl.getModifiers());
/*  685 */         if (match || classMode == 0) {
/*  686 */           vec.addElement(cl);
/*      */         }
/*      */       } 
/*      */     } 
/*  690 */     return vec.elements();
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
/*      */   public Enumeration getMdEnum(String fullName, String descriptor) {
/*  703 */     final Vector<Md> vec = new Vector();
/*  704 */     final String fDesc = descriptor;
/*  705 */     if (fullName.indexOf('*') != -1 || descriptor
/*  706 */       .indexOf('*') != -1) {
/*      */       
/*  708 */       if (fullName.indexOf('!') == 0) {
/*  709 */         final String fName = fullName.substring(1);
/*      */         
/*  711 */         walkTree(new TreeAction() {
/*      */               public void methodAction(Md md) {
/*  713 */                 if (md.isWildcardMatch(fName, fDesc)) {
/*  714 */                   vec.addElement(md);
/*      */                 }
/*      */               }
/*      */             });
/*      */       }
/*      */       else {
/*      */         
/*  721 */         final String fName = fullName;
/*      */         
/*  723 */         walkTree(new TreeAction() {
/*      */               public void methodAction(Md md) {
/*  725 */                 if (md.isNRWildcardMatch(fName, fDesc)) {
/*  726 */                   vec.addElement(md);
/*      */                 }
/*      */               }
/*      */             });
/*      */       } 
/*      */     } else {
/*  732 */       Md md = getMd(fullName, descriptor);
/*  733 */       if (md != null) {
/*  734 */         vec.addElement(md);
/*      */       }
/*      */     } 
/*  737 */     return vec.elements();
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
/*      */   public Enumeration getFdEnum(String fullName) {
/*  749 */     final Vector<Fd> vec = new Vector();
/*  750 */     if (fullName.indexOf('*') != -1) {
/*      */       
/*  752 */       if (fullName.indexOf('!') == 0) {
/*      */         
/*  754 */         final String fName = fullName.substring(1);
/*  755 */         walkTree(new TreeAction() {
/*      */               public void fieldAction(Fd fd) {
/*  757 */                 if (fd.isWildcardMatch(fName)) {
/*  758 */                   vec.addElement(fd);
/*      */                 
/*      */                 }
/*      */               }
/*      */             });
/*      */       }
/*      */       else {
/*      */         
/*  766 */         final String fName = fullName;
/*  767 */         walkTree(new TreeAction() {
/*      */               public void fieldAction(Fd fd) {
/*  769 */                 if (fd.isNRWildcardMatch(fName)) {
/*  770 */                   vec.addElement(fd);
/*      */                 }
/*      */               }
/*      */             });
/*      */       } 
/*      */     } else {
/*  776 */       Fd fd = getFd(fullName);
/*  777 */       if (fd != null) {
/*  778 */         vec.addElement(fd);
/*      */       }
/*      */     } 
/*  781 */     return vec.elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cl getCl(String fullName) {
/*  792 */     TreeItem ti = this.root;
/*  793 */     for (Enumeration<Cons> nameEnum = getNameEnum(fullName); nameEnum.hasMoreElements(); ) {
/*      */       
/*  795 */       Cons nameSegment = nameEnum.nextElement();
/*  796 */       char tag = ((Character)nameSegment.car).charValue();
/*  797 */       String name = (String)nameSegment.cdr;
/*  798 */       switch (tag) {
/*      */         
/*      */         case '/':
/*  801 */           ti = ((Pk)ti).getPackage(name);
/*      */           break;
/*      */         
/*      */         case '$':
/*  805 */           ti = ((PkCl)ti).getClass(name);
/*      */           break;
/*      */         
/*      */         default:
/*  809 */           throw new ParseException("Internal error: illegal package/class name tag");
/*      */       } 
/*      */ 
/*      */       
/*  813 */       if (ti == null)
/*      */       {
/*  815 */         return null;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  820 */     if (!(ti instanceof Cl))
/*      */     {
/*  822 */       throw new ParseException("Inconsistent class or interface name.");
/*      */     }
/*  824 */     return (Cl)ti;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Pk getPk(String fullName) {
/*  835 */     TreeItem ti = this.root;
/*  836 */     for (Enumeration<Cons> nameEnum = getNameEnum(fullName); nameEnum.hasMoreElements(); ) {
/*      */       
/*  838 */       Cons nameSegment = nameEnum.nextElement();
/*  839 */       String name = (String)nameSegment.cdr;
/*  840 */       ti = ((Pk)ti).getPackage(name);
/*      */ 
/*      */       
/*  843 */       if (ti == null)
/*      */       {
/*  845 */         return null;
/*      */       }
/*      */       
/*  848 */       if (!(ti instanceof Pk))
/*      */       {
/*  850 */         throw new ParseException("Inconsistent package.");
/*      */       }
/*      */     } 
/*  853 */     return (Pk)ti;
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
/*      */   public Md getMd(String fullName, String descriptor) {
/*  866 */     int pos = fullName.lastIndexOf('/');
/*  867 */     Cl cl = getCl(fullName.substring(0, pos));
/*  868 */     return cl.getMethod(fullName.substring(pos + 1), descriptor);
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
/*      */   public Fd getFd(String fullName) {
/*  880 */     int pos = fullName.lastIndexOf('/');
/*  881 */     Cl cl = getCl(fullName.substring(0, pos));
/*  882 */     return cl.getField(fullName.substring(pos + 1));
/*      */   }
/*      */   
/*      */   public String[] getAttrsToKeep(String className) {
/*  886 */     Cl cl = getCl(className);
/*  887 */     if (cl != null) {
/*  888 */       Set<?> attrs = cl.getAttributesToKeep();
/*  889 */       if (attrs != null && attrs.size() > 0) {
/*  890 */         String[] other = getAttrsToKeep();
/*  891 */         Set<String> tmp = new HashSet(attrs);
/*  892 */         for (int i = 0; i < other.length; i++) {
/*  893 */           tmp.add(other[i]);
/*      */         }
/*  895 */         return tmp.<String>toArray(new String[tmp.size()]);
/*      */       } 
/*  897 */       return getAttrsToKeep();
/*      */     } 
/*      */     
/*  900 */     return getAttrsToKeep();
/*      */   }
/*      */ 
/*      */   
/*      */   public String mapLocalVariable(String thisClassName, String methodName, String descriptor, String string) {
/*  905 */     return string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mapClass(String className) {
/*  914 */     if (className.length() > 0 && className.charAt(0) == '[') {
/*  915 */       StringBuffer newName = new StringBuffer();
/*  916 */       int i = 0;
/*  917 */       while (i < className.length()) {
/*  918 */         int pos; char ch = className.charAt(i++);
/*  919 */         switch (ch) {
/*      */           case ';':
/*      */           case '[':
/*  922 */             newName.append(ch);
/*      */             continue;
/*      */           
/*      */           case 'L':
/*  926 */             newName.append(ch);
/*  927 */             pos = className.indexOf(';', i);
/*  928 */             if (pos < 0) {
/*  929 */               throw new ParseException("Invalid class name encountered: " + className);
/*      */             }
/*  931 */             newName.append(mapClass(className.substring(i, pos)));
/*  932 */             i = pos;
/*      */             continue;
/*      */         } 
/*      */         
/*  936 */         return className;
/*      */       } 
/*      */       
/*  939 */       return newName.toString();
/*      */     } 
/*  941 */     Cl cl = getCl(className);
/*  942 */     if (cl == null) {
/*      */       try {
/*  944 */         Class aClass = Cl.getClassResolver().resolve(Conversion.toJavaClass(className));
/*      */         
/*  946 */         return className;
/*  947 */       } catch (ClassNotFoundException e) {
/*  948 */         if (this.pedantic) {
/*  949 */           throw new NoSuchMappingException("Class " + Conversion.toJavaClass(className));
/*      */         }
/*  951 */         Logger.getInstance().warningToLogfile("Unresolved external dependency: " + Conversion.toJavaClass(className) + " not found!");
/*      */         
/*  953 */         Logger.getInstance().setUnresolved();
/*  954 */         return className;
/*      */       } 
/*      */     }
/*      */     
/*  958 */     return cl.getFullOutName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mapMethod(String className, String methodName, String descriptor) {
/*  967 */     if (className.startsWith("[") && className.endsWith(";")) {
/*  968 */       int count = 0;
/*  969 */       while (className.charAt(count) == '[') {
/*  970 */         count++;
/*      */       }
/*  972 */       if (className.charAt(count) == 'L') {
/*  973 */         className = className.substring(count + 1, className.length() - 1);
/*      */       }
/*      */     } 
/*  976 */     Cl cl = getCl(className);
/*  977 */     if (cl != null && cl.getMethod(methodName, descriptor) != null)
/*      */     {
/*  979 */       return cl.getMethod(methodName, descriptor).getOutName();
/*      */     }
/*      */ 
/*      */     
/*  983 */     if (cl == null) {
/*      */       
/*      */       try {
/*  986 */         Class clazz = Cl.getClassResolver().resolve(Conversion.toJavaClass(className));
/*  987 */       } catch (ClassNotFoundException e) {
/*  988 */         if (this.pedantic) {
/*  989 */           throw new NoSuchMappingException("Class " + Conversion.toJavaClass(className));
/*      */         }
/*  991 */         Logger.getInstance().warningToLogfile("No mapping found: " + Conversion.toJavaClass(className));
/*      */       } 
/*      */ 
/*      */       
/*  995 */       return methodName;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1002 */       String result = cl.getMethodOutNameUp(methodName, descriptor);
/* 1003 */       if (result != null) {
/* 1004 */         return result;
/*      */       }
/* 1006 */     } catch (Exception ex) {
/*      */       
/* 1008 */       System.out.println(ex);
/*      */     } 
/*      */     
/* 1011 */     if (!methodName.equals("<init>") && 
/* 1012 */       !methodName.equals("<clinit>")) {
/* 1013 */       if (this.pedantic) {
/* 1014 */         throw new NoSuchMappingException("Method " + Conversion.toJavaClass(className) + "." + methodName);
/*      */       }
/* 1016 */       Logger.getInstance().error("Method " + Conversion.toJavaClass(className) + "." + methodName + " could not be mapped !\n Probably broken code! Try rebuilding from source!");
/*      */       
/* 1018 */       return methodName;
/*      */     } 
/*      */     
/* 1021 */     return methodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mapAnnotationField(String className, String methodName) {
/* 1030 */     Cl cl = getCl(className);
/* 1031 */     if (cl != null) {
/*      */       
/* 1033 */       for (Enumeration<Md> enumeration = cl.getMethodEnum(); enumeration.hasMoreElements(); ) {
/* 1034 */         Md md = enumeration.nextElement();
/* 1035 */         if (md.getInName().equals(methodName)) {
/* 1036 */           return md.getOutName();
/*      */         }
/*      */       } 
/*      */       
/* 1040 */       return methodName;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1045 */     return methodName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mapField(String className, String fieldName) {
/* 1054 */     Cl cl = getCl(className);
/* 1055 */     if (cl != null && cl.getField(fieldName) != null) {
/*      */ 
/*      */       
/* 1058 */       if (fieldName.startsWith("class$") && isReplaceClassNameStrings()) {
/* 1059 */         String realClassName = fieldName.substring(6);
/* 1060 */         List<String> nameParts = new ArrayList(20);
/* 1061 */         for (StringTokenizer st = new StringTokenizer(realClassName, "$", false); st.hasMoreTokens();) {
/* 1062 */           nameParts.add(st.nextToken());
/*      */         }
/* 1064 */         String[] names = new String[nameParts.size()];
/* 1065 */         nameParts.toArray(names);
/* 1066 */         TreeItem ti = findTreeItem(names);
/* 1067 */         if (ti instanceof Cl) {
/* 1068 */           Fd fd = cl.getField(fieldName);
/* 1069 */           String newClassName = mapClass(ti.getFullInName());
/* 1070 */           String outName = "class$" + newClassName.replace('/', '$');
/* 1071 */           fd.setOutName(outName);
/* 1072 */           return outName;
/*      */         } 
/*      */       } 
/*      */       
/* 1076 */       return cl.getField(fieldName).getOutName();
/*      */     } 
/*      */ 
/*      */     
/* 1080 */     if (cl == null)
/*      */     {
/*      */ 
/*      */       
/* 1084 */       return fieldName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1093 */       String result = cl.getFieldOutNameUp(fieldName);
/* 1094 */       if (result != null) {
/* 1095 */         return result;
/*      */       }
/* 1097 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */     
/* 1101 */     if (!fieldName.equals("this")) {
/* 1102 */       if (this.pedantic) {
/* 1103 */         throw new NoSuchMappingException("Field " + className + "." + fieldName);
/*      */       }
/* 1105 */       Logger.getInstance().error("Field " + className + "." + fieldName + " could not be mapped !\n Probably broken code! Try rebuilding from source!");
/*      */     } 
/*      */ 
/*      */     
/* 1109 */     return fieldName;
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
/*      */   public String mapSignature(String signature) {
/* 1128 */     StringBuffer classString = new StringBuffer();
/*      */     
/* 1130 */     StringBuffer newSignature = new StringBuffer();
/* 1131 */     int i = 0;
/* 1132 */     while (i < signature.length()) {
/*      */       int pos, bracketPos; String classNamePart;
/* 1134 */       char ch = signature.charAt(i++);
/* 1135 */       switch (ch) {
/*      */         
/*      */         case '(':
/*      */         case ')':
/*      */         case '*':
/*      */         case '+':
/*      */         case '-':
/*      */         case ':':
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'D':
/*      */         case 'F':
/*      */         case 'I':
/*      */         case 'J':
/*      */         case 'S':
/*      */         case 'V':
/*      */         case 'Z':
/*      */         case '[':
/* 1153 */           newSignature.append(ch);
/*      */           continue;
/*      */         case ';':
/* 1156 */           newSignature.append(ch);
/* 1157 */           classString.setLength(0);
/*      */           continue;
/*      */         
/*      */         case 'T':
/* 1161 */           newSignature.append(ch);
/* 1162 */           pos = signature.indexOf(';', i);
/* 1163 */           if (pos < 0)
/*      */           {
/* 1165 */             throw new ParseException("Invalid signature string encountered.");
/*      */           }
/* 1167 */           newSignature.append(signature.substring(i, pos));
/* 1168 */           i = pos;
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case '<':
/* 1174 */           newSignature.append(ch);
/*      */           while (true) {
/* 1176 */             int first = i;
/* 1177 */             while (signature.charAt(i) != ':') {
/* 1178 */               i++;
/*      */             }
/* 1180 */             String templateName = signature.substring(first, i);
/* 1181 */             newSignature.append(templateName);
/*      */             
/* 1183 */             while (signature.charAt(i) == ':') {
/* 1184 */               newSignature.append(':');
/*      */               
/* 1186 */               int firstPos = ++i;
/* 1187 */               int bracketCount = 0;
/* 1188 */               while (bracketCount != 0 || signature.charAt(i) != ';') {
/* 1189 */                 if (signature.charAt(i) == '<') {
/* 1190 */                   bracketCount++;
/* 1191 */                 } else if (signature.charAt(i) == '>') {
/* 1192 */                   bracketCount--;
/*      */                 } 
/* 1194 */                 i++;
/*      */               } 
/* 1196 */               i++;
/* 1197 */               newSignature.append(mapSignature(signature.substring(firstPos, i)));
/*      */             } 
/* 1199 */             if (signature.charAt(i) == '>') {
/* 1200 */               newSignature.append('>');
/* 1201 */               i++;
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case '^':
/* 1210 */           newSignature.append(ch);
/* 1211 */           if (signature.charAt(i) == 'T') {
/*      */             
/* 1213 */             while (signature.charAt(i) != ';') {
/* 1214 */               newSignature.append(signature.charAt(i));
/* 1215 */               i++;
/*      */             }  continue;
/*      */           } 
/* 1218 */           if (signature.charAt(i) == 'L') {
/*      */             
/* 1220 */             int first = i;
/* 1221 */             int bracketCount = 0;
/* 1222 */             while (signature.charAt(i) != ';' || bracketCount != 0) {
/* 1223 */               char c = signature.charAt(i);
/* 1224 */               if (c == '<') {
/* 1225 */                 bracketCount++;
/* 1226 */               } else if (c == '>') {
/* 1227 */                 bracketCount--;
/*      */               } 
/* 1229 */               i++;
/*      */             } 
/* 1231 */             i++;
/* 1232 */             String classSig = signature.substring(first, i);
/* 1233 */             newSignature.append(mapSignature(classSig)); continue;
/*      */           } 
/* 1235 */           throw new IllegalStateException("Could not map signature " + signature);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case '.':
/*      */         case 'L':
/* 1242 */           newSignature.append(ch);
/* 1243 */           pos = signature.indexOf(';', i);
/* 1244 */           bracketPos = signature.indexOf('<', i);
/* 1245 */           if (bracketPos >= i && bracketPos < pos) {
/*      */             
/* 1247 */             int bracketCount = 0;
/* 1248 */             int closingBracket = signature.length();
/* 1249 */             for (int walker = bracketPos + 1; walker < signature.length(); walker++) {
/* 1250 */               char c = signature.charAt(walker);
/* 1251 */               if (c == '<') {
/* 1252 */                 bracketCount++;
/* 1253 */               } else if (c == '>') {
/* 1254 */                 if (bracketCount == 0) {
/* 1255 */                   closingBracket = walker;
/*      */                   break;
/*      */                 } 
/* 1258 */                 bracketCount--;
/*      */               } 
/*      */             } 
/*      */ 
/*      */             
/* 1263 */             pos = closingBracket + 1;
/*      */             
/* 1265 */             String templateArg = signature.substring(bracketPos + 1, closingBracket);
/* 1266 */             String str1 = signature.substring(i, bracketPos);
/* 1267 */             if (ch == '.') {
/* 1268 */               appendInnerClass(classString, newSignature, str1);
/*      */             } else {
/* 1270 */               classString.append(str1);
/* 1271 */               newSignature.append(mapClass(classString.toString()));
/*      */             } 
/* 1273 */             newSignature.append('<');
/* 1274 */             newSignature.append(mapSignature(templateArg));
/* 1275 */             newSignature.append('>');
/* 1276 */             i = pos;
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 1281 */           if (pos < 0)
/*      */           {
/* 1283 */             throw new ParseException("Invalid signature string encountered: " + signature);
/*      */           }
/*      */           
/* 1286 */           classNamePart = signature.substring(i, pos);
/* 1287 */           if (ch == '.') {
/* 1288 */             appendInnerClass(classString, newSignature, classNamePart);
/*      */           } else {
/* 1290 */             classString.append(classNamePart);
/* 1291 */             newSignature.append(mapClass(classString.toString()));
/*      */           } 
/*      */           
/* 1294 */           i = pos;
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/* 1299 */       throw new ParseException("Invalid signature string encountered: " + signature + " parsing char " + ch);
/*      */     } 
/*      */     
/* 1302 */     return newSignature.toString();
/*      */   }
/*      */   
/*      */   private void appendInnerClass(StringBuffer classString, StringBuffer newSignature, String classNamePart) {
/* 1306 */     classString.append('$');
/* 1307 */     classString.append(classNamePart);
/* 1308 */     String className = classString.toString();
/*      */     
/* 1310 */     String result = getClassNamePart(classNamePart, className);
/* 1311 */     newSignature.append(result);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String getClassNamePart(String classNamePart, String className) {
/* 1317 */     int j = className.indexOf(classNamePart);
/*      */     
/* 1319 */     if (classNamePart.indexOf('.') != -1 && j > 0) {
/*      */ 
/*      */ 
/*      */       
/* 1323 */       String outerClassName = className.substring(0, j - 1);
/* 1324 */       String retval = "";
/* 1325 */       String currentClassName = outerClassName;
/*      */       
/* 1327 */       StringBuilder innerClassName = new StringBuilder();
/* 1328 */       for (int i = 0; i < classNamePart.length(); i++) {
/* 1329 */         char c = classNamePart.charAt(i);
/* 1330 */         if (c == '.') {
/* 1331 */           currentClassName = currentClassName + '$' + innerClassName;
/* 1332 */           retval = appendOutName(retval, currentClassName);
/* 1333 */           innerClassName = new StringBuilder();
/*      */         } else {
/* 1335 */           innerClassName.append(c);
/*      */         } 
/*      */       } 
/* 1338 */       currentClassName = currentClassName + '$' + innerClassName;
/* 1339 */       retval = appendOutName(retval, currentClassName);
/* 1340 */       return retval;
/*      */     } 
/*      */ 
/*      */     
/* 1344 */     Cl cl = getCl(className);
/* 1345 */     if (cl == null) {
/*      */       try {
/* 1347 */         Class aClass = Cl.getClassResolver().resolve(Conversion.toJavaClass(className));
/*      */         
/* 1349 */         return classNamePart;
/* 1350 */       } catch (ClassNotFoundException e) {
/* 1351 */         if (this.pedantic) {
/* 1352 */           throw new NoSuchMappingException("Class " + Conversion.toJavaClass(className));
/*      */         }
/* 1354 */         Logger.getInstance().warningToLogfile("Unresolved external dependency: " + Conversion.toJavaClass(className) + " not found!");
/*      */         
/* 1356 */         Logger.getInstance().setUnresolved();
/* 1357 */         return classNamePart;
/*      */       } 
/*      */     }
/*      */     
/* 1361 */     return cl.getOutName();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String appendOutName(String retval, String currentClassName) {
/* 1367 */     Cl cl = getCl(currentClassName);
/* 1368 */     if (null != cl) {
/* 1369 */       if (retval.length() > 0) {
/* 1370 */         retval = retval + '.';
/*      */       }
/* 1372 */       retval = retval + cl.getOutName();
/*      */     } else {
/*      */       try {
/* 1375 */         Class aClass = Cl.getClassResolver().resolve(Conversion.toJavaClass(currentClassName));
/*      */         
/* 1377 */         retval = retval + "." + currentClassName;
/* 1378 */       } catch (ClassNotFoundException e) {
/* 1379 */         if (this.pedantic) {
/* 1380 */           throw new NoSuchMappingException("Class " + Conversion.toJavaClass(currentClassName));
/*      */         }
/* 1382 */         Logger.getInstance().warningToLogfile("Unresolved external dependency: " + Conversion.toJavaClass(currentClassName) + " not found!");
/*      */         
/* 1384 */         Logger.getInstance().setUnresolved();
/* 1385 */         retval = retval + "." + currentClassName;
/*      */       } 
/*      */     } 
/*      */     
/* 1389 */     return retval;
/*      */   }
/*      */   
/*      */   public String mapSourceFile(String className, String sourceFileName) {
/* 1393 */     Cl cl = getCl(className);
/* 1394 */     if (cl.isSourceFileMappingSet()) {
/* 1395 */       return cl.getSourceFileMapping();
/*      */     }
/* 1397 */     return sourceFileName;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mapLineNumberTable(String className, String methodName, String methodSignature, LineNumberTableAttrInfo info) {
/* 1402 */     Cl cl = getCl(className);
/* 1403 */     if (cl.getLineNumberTableMapper() != null) {
/* 1404 */       return cl.getLineNumberTableMapper().mapLineNumberTable(className, methodName, methodSignature, info);
/*      */     }
/* 1406 */     return true;
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
/*      */   public String mapDescriptor(String descriptor) {
/* 1418 */     StringBuffer newDesc = new StringBuffer();
/* 1419 */     int i = 0;
/* 1420 */     while (i < descriptor.length()) {
/*      */       int pos;
/* 1422 */       char ch = descriptor.charAt(i++);
/* 1423 */       switch (ch) {
/*      */         
/*      */         case '(':
/*      */         case ')':
/*      */         case ';':
/*      */         case 'B':
/*      */         case 'C':
/*      */         case 'D':
/*      */         case 'F':
/*      */         case 'I':
/*      */         case 'J':
/*      */         case 'S':
/*      */         case 'V':
/*      */         case 'Z':
/*      */         case '[':
/* 1438 */           newDesc.append(ch);
/*      */           continue;
/*      */         
/*      */         case 'L':
/* 1442 */           newDesc.append(ch);
/* 1443 */           pos = descriptor.indexOf(';', i);
/* 1444 */           if (pos < 0)
/*      */           {
/* 1446 */             throw new ParseException("Invalid descriptor string encountered.");
/*      */           }
/* 1448 */           newDesc.append(mapClass(descriptor.substring(i, pos)));
/* 1449 */           i = pos;
/*      */           continue;
/*      */       } 
/*      */       
/* 1453 */       throw new ParseException("Invalid descriptor string encountered.");
/*      */     } 
/*      */     
/* 1456 */     return newDesc.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mapPackage(String packageName) {
/* 1464 */     Pk pk = getPk(packageName);
/* 1465 */     return (pk == null) ? packageName : pk.getFullOutName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dump(final PrintWriter log) {
/* 1475 */     log.println("<expose>");
/* 1476 */     walkTree(new TreeAction() {
/*      */           public void classAction(Cl cl) {
/* 1478 */             String name = cl.getFullInName();
/* 1479 */             if (cl.isFromScript() && !"module-info".equals(name)) {
/* 1480 */               String cla = ClassTree.toUtf8XmlString(Conversion.toJavaClass(name));
/* 1481 */               log.println("  <class name=\"" + cla + "\"/>");
/*      */             } 
/*      */           }
/*      */           public void methodAction(Md md) {
/* 1485 */             if (md.isFromScript()) {
/* 1486 */               String cla = ClassTree.toUtf8XmlString(Conversion.toJavaClass(md.getParent().getFullInName()));
/* 1487 */               String method = ClassTree.toUtf8XmlString(Conversion.toJavaMethod(md.getInName(), md.getDescriptor()));
/* 1488 */               log.println("  <method class=\"" + cla + "\" name=\"" + method + "\"/>");
/*      */             } 
/*      */           }
/*      */           public void fieldAction(Fd fd) {
/* 1492 */             if (fd.isFromScript()) {
/* 1493 */               String cla = ClassTree.toUtf8XmlString(Conversion.toJavaClass(fd.getParent().getFullInName()));
/* 1494 */               log.println("  <field class=\"" + cla + "\" name=\"" + ClassTree.toUtf8XmlString(fd.getInName()) + "\"/>");
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void packageAction(Pk pk) {}
/*      */         });
/* 1501 */     log.println("</expose>");
/* 1502 */     log.println("<map>");
/* 1503 */     walkTree(new TreeAction() {
/*      */           public void classAction(Cl cl) {
/* 1505 */             String name = cl.getFullInName();
/* 1506 */             if ((cl.isFromScriptMap() || !cl.isFromScript()) && !"module-info".equals(name)) {
/* 1507 */               String cla = ClassTree.toUtf8XmlString(Conversion.toJavaClass(name));
/* 1508 */               log.println("  <class name=\"" + ClassTree.toUtf8XmlString(cla) + "\" map=\"" + ClassTree.toUtf8XmlString(cl.getOutName()) + "\"/>");
/*      */             } 
/*      */           }
/*      */           public void methodAction(Md md) {
/* 1512 */             if (md.isFromScriptMap() || !md.isFromScript()) {
/* 1513 */               String cla = ClassTree.toUtf8XmlString(Conversion.toJavaClass(md.getParent().getFullInName()));
/* 1514 */               String method = ClassTree.toUtf8XmlString(Conversion.toJavaMethod(md.getInName(), md.getDescriptor()));
/* 1515 */               log.println("  <method class=\"" + cla + "\" name=\"" + method + "\" map=\"" + ClassTree.toUtf8XmlString(md.getOutName()) + "\"/>");
/*      */             } 
/*      */           }
/*      */           public void fieldAction(Fd fd) {
/* 1519 */             if (fd.isFromScriptMap() || !fd.isFromScript()) {
/* 1520 */               String cla = ClassTree.toUtf8XmlString(Conversion.toJavaClass(fd.getParent().getFullInName()));
/* 1521 */               log.println("  <field class=\"" + cla + "\" name=\"" + ClassTree.toUtf8XmlString(fd.getInName()) + "\" map=\"" + ClassTree.toUtf8XmlString(fd.getOutName()) + "\"/>");
/*      */             } 
/*      */           }
/*      */           public void packageAction(Pk pk) {
/* 1525 */             if ((pk.isFromScriptMap() || !pk.isFromScript()) && pk.getFullInName().length() > 0) {
/* 1526 */               String pa = ClassTree.toUtf8XmlString(Conversion.toJavaClass(pk.getFullInName()));
/* 1527 */               log.println("  <package name=\"" + pa + "\" map=\"" + ClassTree.toUtf8XmlString(pk.getOutName()) + "\"/>");
/*      */             } 
/*      */           }
/*      */         });
/* 1531 */     log.println("</map>");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String toUtf8XmlString(String s) {
/* 1541 */     boolean bad = false;
/* 1542 */     for (int i = 0; i < s.length(); i++) {
/* 1543 */       char c = s.charAt(i);
/* 1544 */       if (c >= '' || c == '"' || c == '<') {
/* 1545 */         bad = true;
/*      */         break;
/*      */       } 
/*      */     } 
/* 1549 */     if (bad) {
/* 1550 */       StringBuffer buf = new StringBuffer(s.length());
/* 1551 */       for (int j = 0; j < s.length(); j++) {
/* 1552 */         buf.append(toUtf8XmlChar(s.charAt(j)));
/*      */       }
/* 1554 */       return buf.toString();
/*      */     } 
/* 1556 */     return s;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final String toUtf8XmlChar(char c) {
/* 1561 */     if (c < '') {
/* 1562 */       if (c == '"')
/* 1563 */         return "&#x22;"; 
/* 1564 */       if (c == '<') {
/* 1565 */         return "&#x3c;";
/*      */       }
/* 1567 */       return new String(new char[] { c });
/*      */     } 
/* 1569 */     if (c < 'ࠀ') {
/*      */       
/* 1571 */       StringBuffer stringBuffer = new StringBuffer(8);
/* 1572 */       stringBuffer.append("&#x");
/* 1573 */       stringBuffer.append(hex[c >> 8 & 0xFF]);
/* 1574 */       stringBuffer.append(hex[c & 0xFF]);
/* 1575 */       stringBuffer.append(';');
/* 1576 */       return stringBuffer.toString();
/*      */     } 
/*      */ 
/*      */     
/* 1580 */     StringBuffer buf = new StringBuffer(10);
/* 1581 */     buf.append("&#x");
/* 1582 */     buf.append(hex[c >> 16 & 0xFF]);
/* 1583 */     buf.append(hex[c >> 8 & 0xFF]);
/* 1584 */     buf.append(hex[c & 0xFF]);
/* 1585 */     buf.append(';');
/* 1586 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1592 */   private static final String[] hex = new String[256]; private static final String hexChars = "0123456789abcdef"; static {
/* 1593 */     for (int i = 0; i < 256; i++) {
/* 1594 */       hex[i] = toHex(i);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean replaceClassNameStrings;
/*      */ 
/*      */   
/*      */   private boolean pedantic;
/*      */ 
/*      */   
/*      */   private static String toHex(int i) {
/* 1607 */     StringBuffer buf = new StringBuffer(2);
/* 1608 */     buf.append("0123456789abcdef".charAt(i / 16 & 0xF));
/* 1609 */     buf.append("0123456789abcdef".charAt(i & 0xF));
/* 1610 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void retainHierarchy(TreeItem ti) {
/* 1617 */     if (!ti.isFixed()) {
/*      */       
/* 1619 */       ti.setOutName(ti.getInName());
/* 1620 */       ti.setFromScript();
/*      */     } 
/* 1622 */     if (ti.parent != null)
/*      */     {
/* 1624 */       retainHierarchy(ti.parent);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void walkTree(TreeAction ta) {
/* 1635 */     walkTree(ta, this.root);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void walkTree(TreeAction ta, TreeItem ti) {
/* 1642 */     if (ti instanceof Pk) {
/*      */       
/* 1644 */       Enumeration<TreeItem> packageEnum = ((Pk)ti).getPackageEnum();
/* 1645 */       ta.packageAction((Pk)ti);
/* 1646 */       while (packageEnum.hasMoreElements())
/*      */       {
/* 1648 */         walkTree(ta, packageEnum.nextElement());
/*      */       }
/*      */     } 
/* 1651 */     if (ti instanceof PkCl) {
/*      */       
/* 1653 */       Enumeration<TreeItem> classEnum = ((PkCl)ti).getClassEnum();
/* 1654 */       while (classEnum.hasMoreElements())
/*      */       {
/* 1656 */         walkTree(ta, classEnum.nextElement());
/*      */       }
/*      */     } 
/* 1659 */     if (ti instanceof Cl) {
/*      */       
/* 1661 */       Enumeration<Fd> fieldEnum = ((Cl)ti).getFieldEnum();
/* 1662 */       Enumeration<Md> methodEnum = ((Cl)ti).getMethodEnum();
/* 1663 */       ta.classAction((Cl)ti);
/* 1664 */       while (fieldEnum.hasMoreElements())
/*      */       {
/* 1666 */         ta.fieldAction(fieldEnum.nextElement());
/*      */       }
/* 1668 */       while (methodEnum.hasMoreElements())
/*      */       {
/* 1670 */         ta.methodAction(methodEnum.nextElement());
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
/*      */   public boolean isReplaceClassNameStrings() {
/* 1682 */     return this.replaceClassNameStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReplaceClassNameStrings(boolean replaceClassNameStrings) {
/* 1692 */     this.replaceClassNameStrings = replaceClassNameStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPedantic() {
/* 1702 */     return this.pedantic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPedantic(boolean pedantic) {
/* 1712 */     this.pedantic = pedantic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainSourceFileAttributeMap(String name, String obfName) {
/* 1722 */     for (Enumeration<Cl> clEnum = getClEnum(name); clEnum.hasMoreElements(); ) {
/*      */       
/* 1724 */       Cl classItem = clEnum.nextElement();
/* 1725 */       classItem.setSourceFileMapping(obfName);
/* 1726 */       classItem.getAttributesToKeep().add("SourceFile");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainLineNumberTable(String name, LineNumberTableMapper lineNumberTableMapper) {
/* 1737 */     for (Enumeration<Cl> clEnum = getClEnum(name); clEnum.hasMoreElements(); ) {
/*      */       
/* 1739 */       Cl classItem = clEnum.nextElement();
/* 1740 */       classItem.setLineNumberTableMapper(lineNumberTableMapper);
/* 1741 */       classItem.getAttributesToKeep().add("LineNumberTable");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainAttributeForClass(String className, String attributeDescriptor) {
/* 1752 */     for (Enumeration<Cl> clEnum = getClEnum(className); clEnum.hasMoreElements(); ) {
/*      */       
/* 1754 */       Cl classItem = clEnum.nextElement();
/* 1755 */       Set<String> set = classItem.getAttributesToKeep();
/* 1756 */       set.add(attributeDescriptor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retainPackage(String packageName) {
/* 1766 */     retainHierarchy(getPk(packageName));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/ClassTree.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
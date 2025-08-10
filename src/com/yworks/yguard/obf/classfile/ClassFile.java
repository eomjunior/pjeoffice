/*      */ package com.yworks.yguard.obf.classfile;
/*      */ 
/*      */ import com.yworks.yguard.Conversion;
/*      */ import com.yworks.yguard.ParseException;
/*      */ import com.yworks.yguard.obf.Cl;
/*      */ import com.yworks.yguard.obf.ClassTree;
/*      */ import com.yworks.yguard.obf.Tools;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassFile
/*      */   implements ClassConstants
/*      */ {
/*      */   public static final String SEP_REGULAR = "/";
/*      */   public static final String SEP_INNER = "$";
/*      */   public static final String LOG_DANGER_HEADER1 = "Methods are called which may break in obfuscated version at runtime.";
/*      */   public static final String LOG_DANGER_HEADER2 = "Please review your source code to ensure that the dangerous methods are not intended";
/*      */   public static final String LOG_DANGER_HEADER3 = "to act on classes which have been obfuscated.";
/*   60 */   private static final String[] SEMI_DANGEROUS_CLASS_SIMPLENAME_DESCRIPTOR_ARRAY = new String[] { "forName(Ljava/lang/String;)Ljava/lang/Class;", "forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;" };
/*      */ 
/*      */ 
/*      */   
/*   64 */   private static final String[] DANGEROUS_CLASS_SIMPLENAME_DESCRIPTOR_ARRAY = new String[] { "forName(Ljava/lang/String;)Ljava/lang/Class;", "forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;", "getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;", "getField(Ljava/lang/String;)Ljava/lang/reflect/Field;", "getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", "getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;" };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String LOG_DANGER_CLASS_PRE = "    Your class ";
/*      */ 
/*      */   
/*      */   private static final String LOG_DANGER_CLASS_MID = " calls the java.lang.Class method ";
/*      */ 
/*      */   
/*   74 */   private static final String[] DANGEROUS_CLASSLOADER_SIMPLENAME_DESCRIPTOR_ARRAY = new String[] { "defineClass(Ljava/lang/String;[BII)Ljava/lang/Class;", "findLoadedClass(Ljava/lang/String;)Ljava/lang/Class;", "findSystemClass(Ljava/lang/String;)Ljava/lang/Class;", "loadClass(Ljava/lang/String;)Ljava/lang/Class;", "loadClass(Ljava/lang/String;Z)Ljava/lang/Class;" };
/*      */   
/*      */   private static final String LOG_DANGER_CLASSLOADER_PRE = "    Your class ";
/*      */   
/*      */   private static final String LOG_DANGER_CLASSLOADER_MID = " calls the java.lang.ClassLoader method ";
/*      */   
/*      */   private static final int BM_TYPE_OM = 3;
/*      */   
/*      */   private static final int BM_TYPE_SCF = 2;
/*      */   
/*      */   private static final int BM_TYPE_LMF = 1;
/*      */   
/*      */   private static final int BM_TYPE_UNKNOWN = 0;
/*      */   
/*      */   private int u4magic;
/*      */   
/*      */   private int u2minorVersion;
/*      */   
/*      */   private int u2majorVersion;
/*      */   
/*      */   private ConstantPool constantPool;
/*      */   
/*      */   private int u2accessFlags;
/*      */   
/*      */   private int u2thisClass;
/*      */   
/*      */   private int u2superClass;
/*      */   
/*      */   private int u2interfacesCount;
/*      */   
/*      */   private int[] u2interfaces;
/*      */   
/*      */   private int u2fieldsCount;
/*      */   
/*      */   private FieldInfo[] fields;
/*      */   
/*      */   private int u2methodsCount;
/*      */   
/*      */   private MethodInfo[] methods;
/*      */   
/*      */   private int u2attributesCount;
/*      */   
/*      */   private AttrInfo[] attributes;
/*      */   
/*      */   private boolean isUnkAttrGone = false;
/*      */   
/*      */   private static boolean writeIdString = false;
/*  121 */   private static CpInfo cpIdString = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void defineIdString(String id) {
/*  133 */     if (id != null) {
/*  134 */       writeIdString = true;
/*  135 */       cpIdString = new Utf8CpInfo(id);
/*      */     } else {
/*  137 */       writeIdString = false;
/*  138 */       cpIdString = null;
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
/*      */   public static ClassFile create(DataInput din) throws IOException {
/*  152 */     if (din == null) throw new NullPointerException("No input stream was provided."); 
/*  153 */     ClassFile cf = new ClassFile();
/*  154 */     cf.read(din);
/*  155 */     return cf;
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
/*      */   public static String[] parseDescriptor(String descriptor) {
/*  167 */     return parseDescriptor(descriptor, false);
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
/*      */   public static String[] parseDescriptor(String descriptor, boolean isDisplay) {
/*  181 */     String[] names = null;
/*  182 */     if (descriptor.charAt(0) != '(') {
/*      */       
/*  184 */       names = new String[1];
/*  185 */       names[0] = descriptor;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  190 */       Vector<String> namesVec = new Vector();
/*  191 */       descriptor = descriptor.substring(1);
/*  192 */       String type = "";
/*  193 */       while (descriptor.length() > 0) {
/*      */         int pos;
/*  195 */         switch (descriptor.charAt(0)) {
/*      */           
/*      */           case '[':
/*  198 */             type = type + "[";
/*  199 */             descriptor = descriptor.substring(1);
/*      */             continue;
/*      */           
/*      */           case 'B':
/*      */           case 'C':
/*      */           case 'D':
/*      */           case 'F':
/*      */           case 'I':
/*      */           case 'J':
/*      */           case 'S':
/*      */           case 'V':
/*      */           case 'Z':
/*  211 */             namesVec.addElement(type + descriptor.substring(0, 1));
/*  212 */             descriptor = descriptor.substring(1);
/*  213 */             type = "";
/*      */             continue;
/*      */           
/*      */           case ')':
/*  217 */             descriptor = descriptor.substring(1);
/*      */             continue;
/*      */ 
/*      */           
/*      */           case 'L':
/*  222 */             pos = descriptor.indexOf(';') + 1;
/*  223 */             namesVec.addElement(type + descriptor.substring(0, pos));
/*  224 */             descriptor = descriptor.substring(pos);
/*  225 */             type = "";
/*      */             continue;
/*      */         } 
/*      */ 
/*      */         
/*  230 */         throw new IllegalArgumentException("Illegal field or method descriptor: " + descriptor);
/*      */       } 
/*      */       
/*  233 */       names = new String[namesVec.size()];
/*  234 */       for (int j = 0; j < names.length; j++)
/*      */       {
/*  236 */         names[j] = namesVec.elementAt(j);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  241 */     String[] translatedNames = new String[names.length];
/*  242 */     for (int i = 0; i < names.length; i++)
/*      */     {
/*  244 */       translatedNames[i] = translateType(names[i], isDisplay);
/*      */     }
/*  246 */     return translatedNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String translateType(String inName, boolean isDisplay) {
/*      */     int pos;
/*  258 */     String outName = null;
/*  259 */     switch (inName.charAt(0)) {
/*      */ 
/*      */       
/*      */       case '[':
/*  263 */         if (!isDisplay) {
/*      */ 
/*      */           
/*  266 */           outName = translate(inName);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  271 */           outName = translateType(inName.substring(1), true) + "[]";
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  321 */         return outName;case 'B': outName = byte.class.getName(); return outName;case 'C': outName = char.class.getName(); return outName;case 'D': outName = double.class.getName(); return outName;case 'F': outName = float.class.getName(); return outName;case 'I': outName = int.class.getName(); return outName;case 'J': outName = long.class.getName(); return outName;case 'S': outName = short.class.getName(); return outName;case 'Z': outName = boolean.class.getName(); return outName;case 'V': outName = void.class.getName(); return outName;case 'L': pos = inName.indexOf(';'); outName = translate(inName.substring(1, inName.indexOf(';'))); return outName;
/*      */     } 
/*      */     throw new IllegalArgumentException("Illegal field or method name: " + inName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String translate(String name) {
/*  332 */     return name.replace('/', '.');
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
/*      */   private void read(DataInput din) throws IOException {
/*  344 */     this.u4magic = din.readInt();
/*  345 */     this.u2minorVersion = din.readUnsignedShort();
/*  346 */     this.u2majorVersion = din.readUnsignedShort();
/*      */ 
/*      */     
/*  349 */     if (this.u4magic != -889275714)
/*      */     {
/*  351 */       throw new IOException("Invalid magic number in class file.");
/*      */     }
/*  353 */     if (this.u2majorVersion > 61)
/*      */     {
/*  355 */       throw new IOException("Incompatible version number for class file format: " + this.u2majorVersion + "." + this.u2minorVersion);
/*      */     }
/*      */ 
/*      */     
/*  359 */     int u2constantPoolCount = din.readUnsignedShort();
/*  360 */     CpInfo[] cpInfo = new CpInfo[u2constantPoolCount];
/*      */     
/*      */     int i;
/*  363 */     for (i = 1; i < u2constantPoolCount; i++) {
/*      */       
/*  365 */       cpInfo[i] = CpInfo.create(din);
/*  366 */       if (cpInfo[i] instanceof LongCpInfo || cpInfo[i] instanceof DoubleCpInfo) {
/*      */ 
/*      */         
/*  369 */         i++;
/*      */       
/*      */       }
/*  372 */       else if (cpInfo[i] instanceof DynamicCpInfo) {
/*  373 */         throw new IOException("Unsupported tag type in constant pool: dynamic");
/*      */       } 
/*      */     } 
/*  376 */     this.constantPool = new ConstantPool(this, cpInfo);
/*      */     
/*  378 */     this.u2accessFlags = din.readUnsignedShort();
/*  379 */     this.u2thisClass = din.readUnsignedShort();
/*  380 */     this.u2superClass = din.readUnsignedShort();
/*  381 */     this.u2interfacesCount = din.readUnsignedShort();
/*  382 */     this.u2interfaces = new int[this.u2interfacesCount];
/*  383 */     for (i = 0; i < this.u2interfacesCount; i++)
/*      */     {
/*  385 */       this.u2interfaces[i] = din.readUnsignedShort();
/*      */     }
/*  387 */     this.u2fieldsCount = din.readUnsignedShort();
/*  388 */     this.fields = new FieldInfo[this.u2fieldsCount];
/*  389 */     for (i = 0; i < this.u2fieldsCount; i++)
/*      */     {
/*  391 */       this.fields[i] = FieldInfo.create(din, this);
/*      */     }
/*  393 */     this.u2methodsCount = din.readUnsignedShort();
/*  394 */     this.methods = new MethodInfo[this.u2methodsCount];
/*  395 */     for (i = 0; i < this.u2methodsCount; i++)
/*      */     {
/*  397 */       this.methods[i] = MethodInfo.create(din, this);
/*      */     }
/*  399 */     this.u2attributesCount = din.readUnsignedShort();
/*  400 */     this.attributes = new AttrInfo[this.u2attributesCount];
/*  401 */     for (i = 0; i < this.u2attributesCount; i++)
/*      */     {
/*  403 */       this.attributes[i] = AttrInfo.create(din, this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getClassFileAccess() {
/*  413 */     return this.u2accessFlags;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModifiers() {
/*  422 */     int mods = 0;
/*  423 */     if ((this.u2accessFlags & 0x1) == 1) mods |= 0x1; 
/*  424 */     if ((this.u2accessFlags & 0x10) == 16) mods |= 0x10; 
/*  425 */     if ((this.u2accessFlags & 0x200) == 512) mods |= 0x200; 
/*  426 */     if ((this.u2accessFlags & 0x400) == 1024) mods |= 0x400; 
/*  427 */     return mods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  437 */     return toName(this.u2thisClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSuper() {
/*  448 */     return (this.u2superClass == 0) ? null : toName(this.u2superClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getInterfaces() {
/*  458 */     String[] interfaces = new String[this.u2interfacesCount];
/*  459 */     for (int i = 0; i < this.u2interfacesCount; i++)
/*      */     {
/*  461 */       interfaces[i] = toName(this.u2interfaces[i]);
/*      */     }
/*  463 */     return interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String toName(int u2index) {
/*  469 */     CpInfo classEntry = getCpEntry(u2index);
/*  470 */     if (classEntry instanceof ClassCpInfo) {
/*      */       
/*  472 */       CpInfo nameEntry = getCpEntry(((ClassCpInfo)classEntry).getNameIndex());
/*  473 */       if (nameEntry instanceof Utf8CpInfo)
/*      */       {
/*  475 */         return ((Utf8CpInfo)nameEntry).getString();
/*      */       }
/*      */ 
/*      */       
/*  479 */       throw new ParseException("Inconsistent Constant Pool in class file.");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  484 */     throw new ParseException("Inconsistent Constant Pool in class file.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration getMethodEnum() {
/*  495 */     Vector<MethodInfo> vec = new Vector();
/*  496 */     for (int i = 0; i < this.methods.length; i++)
/*      */     {
/*  498 */       vec.addElement(this.methods[i]);
/*      */     }
/*  500 */     return vec.elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration getFieldEnum() {
/*  510 */     Vector<FieldInfo> vec = new Vector();
/*  511 */     for (int i = 0; i < this.fields.length; i++)
/*      */     {
/*  513 */       vec.addElement(this.fields[i]);
/*      */     }
/*  515 */     return vec.elements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CpInfo getCpEntry(int cpIndex) {
/*  526 */     return this.constantPool.getCpEntry(cpIndex);
/*      */   }
/*      */   
/*      */   private String getUtf8(int cpIndex) {
/*  530 */     return ((Utf8CpInfo)getCpEntry(cpIndex)).getString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstantPool getConstantPool() {
/*  539 */     return this.constantPool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] logDangerousMethods(boolean replaceClassNameStrings) {
/*  550 */     Vector<String> warningVec = new Vector();
/*      */ 
/*      */ 
/*      */     
/*  554 */     for (Enumeration enumeration = this.constantPool.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  556 */       Object o = enumeration.nextElement();
/*  557 */       if (o instanceof MethodrefCpInfo) {
/*      */ 
/*      */         
/*  560 */         MethodrefCpInfo entry = (MethodrefCpInfo)o;
/*  561 */         ClassCpInfo classEntry = (ClassCpInfo)getCpEntry(entry.getClassIndex());
/*  562 */         String className = ((Utf8CpInfo)getCpEntry(classEntry.getNameIndex())).getString();
/*  563 */         NameAndTypeCpInfo ntEntry = (NameAndTypeCpInfo)getCpEntry(entry.getNameAndTypeIndex());
/*  564 */         String name = ((Utf8CpInfo)getCpEntry(ntEntry.getNameIndex())).getString();
/*  565 */         String descriptor = ((Utf8CpInfo)getCpEntry(ntEntry.getDescriptorIndex())).getString();
/*      */ 
/*      */         
/*  568 */         if (className.equals("java/lang/Class") && 
/*  569 */           Tools.isInArray(name + descriptor, DANGEROUS_CLASS_SIMPLENAME_DESCRIPTOR_ARRAY)) {
/*      */           
/*  571 */           if (replaceClassNameStrings && 
/*  572 */             !Tools.isInArray(name + descriptor, SEMI_DANGEROUS_CLASS_SIMPLENAME_DESCRIPTOR_ARRAY)) {
/*  573 */             String jMethod = Conversion.toJavaMethod(name, descriptor);
/*  574 */             warningVec.addElement("    Your class " + Conversion.toJavaClass(getName()) + " calls the java.lang.Class method " + jMethod);
/*      */           } 
/*      */           continue;
/*      */         } 
/*  578 */         if (Tools.isInArray(name + descriptor, DANGEROUS_CLASSLOADER_SIMPLENAME_DESCRIPTOR_ARRAY)) {
/*      */           
/*  580 */           String jMethod = Conversion.toJavaMethod(name, descriptor);
/*  581 */           warningVec.addElement("    Your class " + Conversion.toJavaClass(getName()) + " calls the java.lang.ClassLoader method " + jMethod); continue;
/*  582 */         }  if ("class$(Ljava/lang/String;)Ljava/lang/Class;".equals(name + descriptor) && 
/*  583 */           !replaceClassNameStrings) {
/*  584 */           warningVec.addElement("    Your class " + Conversion.toJavaClass(getName()) + " seems to be using the '.class' construct!");
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  591 */     String[] warnings = new String[warningVec.size()];
/*  592 */     for (int i = 0; i < warnings.length; i++)
/*      */     {
/*  594 */       warnings[i] = warningVec.elementAt(i);
/*      */     }
/*  596 */     return warnings;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasHeader = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resetDangerHeader() {
/*  606 */     hasHeader = false;
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
/*      */   public void logDangerousMethods(PrintWriter log, boolean replaceClassNameStrings) {
/*  618 */     String[] warnings = logDangerousMethods(replaceClassNameStrings);
/*  619 */     if (warnings != null && warnings.length > 0) {
/*      */       
/*  621 */       if (!hasHeader) {
/*      */         
/*  623 */         log.println("<!-- WARNING");
/*  624 */         log.println("Methods are called which may break in obfuscated version at runtime.");
/*  625 */         log.println("Please review your source code to ensure that the dangerous methods are not intended");
/*  626 */         log.println("to act on classes which have been obfuscated.");
/*      */         
/*  628 */         Logger logger = Logger.getInstance();
/*  629 */         logger.warning("Methods are called which may break in obfuscated version at runtime.\nPlease review your source code to ensure that the dangerous methods are not intended\nto act on classes which have been obfuscated.\nSee the logfile for a list of these classes and methods.");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  634 */         log.println("-->");
/*  635 */         hasHeader = true;
/*      */       } 
/*  637 */       if (warnings.length > 0) {
/*  638 */         log.println("<!--");
/*  639 */         for (int i = 0; i < warnings.length; i++)
/*      */         {
/*  641 */           log.println(" " + warnings[i]);
/*      */         }
/*  643 */         log.println("-->");
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
/*      */   public void markUtf8Refs(ConstantPool pool) {
/*      */     try {
/*      */       int i;
/*  658 */       for (i = 0; i < this.fields.length; i++)
/*      */       {
/*  660 */         this.fields[i].markUtf8Refs(pool);
/*      */       }
/*  662 */       for (i = 0; i < this.methods.length; i++)
/*      */       {
/*  664 */         this.methods[i].markUtf8Refs(pool);
/*      */       }
/*  666 */       for (i = 0; i < this.attributes.length; i++)
/*      */       {
/*  668 */         this.attributes[i].markUtf8Refs(pool);
/*      */       }
/*      */ 
/*      */       
/*  672 */       for (Enumeration enumeration = pool.elements(); enumeration.hasMoreElements(); )
/*      */       {
/*  674 */         Object o = enumeration.nextElement();
/*  675 */         if (o instanceof NameAndTypeCpInfo || o instanceof AbstractTypeCpInfo || o instanceof MethodTypeCpInfo || o instanceof StringCpInfo)
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  680 */           ((CpInfo)o).markUtf8Refs(pool);
/*      */         }
/*      */       }
/*      */     
/*  684 */     } catch (ArrayIndexOutOfBoundsException e) {
/*      */       
/*  686 */       throw new ParseException("Inconsistent reference to constant pool.");
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
/*      */   public void markNTRefs(ConstantPool pool) {
/*      */     try {
/*  700 */       for (Enumeration enumeration = pool.elements(); enumeration.hasMoreElements(); )
/*      */       {
/*  702 */         Object o = enumeration.nextElement();
/*  703 */         if (o instanceof RefCpInfo || o instanceof AbstractDynamicCpInfo)
/*      */         {
/*      */           
/*  706 */           ((CpInfo)o).markNTRefs(pool);
/*      */         }
/*      */       }
/*      */     
/*  710 */     } catch (ArrayIndexOutOfBoundsException e) {
/*      */       
/*  712 */       throw new ParseException("Inconsistent reference to constant pool.");
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
/*      */   public void trimAttrsExcept(String[] extraAttrs) {
/*  725 */     String[] keepAttrs = REQUIRED_ATTRS;
/*  726 */     if (extraAttrs != null && extraAttrs.length > 0) {
/*      */       
/*  728 */       String[] tmp = new String[keepAttrs.length + extraAttrs.length];
/*  729 */       System.arraycopy(keepAttrs, 0, tmp, 0, keepAttrs.length);
/*  730 */       System.arraycopy(extraAttrs, 0, tmp, keepAttrs.length, extraAttrs.length);
/*  731 */       keepAttrs = tmp;
/*      */     } 
/*      */     
/*      */     int i;
/*  735 */     for (i = 0; i < this.fields.length; i++)
/*      */     {
/*  737 */       this.fields[i].trimAttrsExcept(keepAttrs);
/*      */     }
/*  739 */     for (i = 0; i < this.methods.length; i++)
/*      */     {
/*  741 */       this.methods[i].trimAttrsExcept(keepAttrs);
/*      */     }
/*      */     
/*  744 */     this.attributes = AttrInfo.filter(this.attributes, keepAttrs);
/*  745 */     this.u2attributesCount = this.attributes.length;
/*      */ 
/*      */     
/*  748 */     this.isUnkAttrGone = true;
/*      */ 
/*      */     
/*  751 */     this.constantPool.updateRefCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map getInnerClassModifiers() {
/*  760 */     Map<Object, Object> map = new HashMap<>();
/*  761 */     for (int i = 0; i < this.u2attributesCount; i++) {
/*      */       
/*  763 */       AttrInfo attrInfo = this.attributes[i];
/*  764 */       if (attrInfo instanceof InnerClassesAttrInfo) {
/*      */         
/*  766 */         InnerClassesInfo[] info = ((InnerClassesAttrInfo)attrInfo).getInfo();
/*  767 */         for (int j = 0; j < info.length; j++) {
/*      */           
/*  769 */           InnerClassesInfo ici = info[j];
/*  770 */           int index = info[j].getInnerNameIndex();
/*  771 */           if (index != 0) {
/*      */ 
/*      */             
/*  774 */             CpInfo cpInfo = getCpEntry(info[j].getInnerNameIndex());
/*  775 */             if (cpInfo instanceof Utf8CpInfo) {
/*      */               
/*  777 */               Utf8CpInfo utf = (Utf8CpInfo)cpInfo;
/*  778 */               String origClass = utf.getString();
/*  779 */               map.put(origClass, new Integer(ici.getModifiers()));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  784 */     }  return map;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean containsDotClassMethodReference() {
/*  790 */     for (Enumeration enumeration = this.constantPool.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  792 */       Object o = enumeration.nextElement();
/*  793 */       if (o instanceof MethodrefCpInfo) {
/*      */ 
/*      */         
/*  796 */         MethodrefCpInfo entry = (MethodrefCpInfo)o;
/*  797 */         ClassCpInfo classEntry = (ClassCpInfo)getCpEntry(entry.getClassIndex());
/*  798 */         String className = ((Utf8CpInfo)getCpEntry(classEntry.getNameIndex())).getString();
/*  799 */         NameAndTypeCpInfo ntEntry = (NameAndTypeCpInfo)getCpEntry(entry.getNameAndTypeIndex());
/*  800 */         String name = ((Utf8CpInfo)getCpEntry(ntEntry.getNameIndex())).getString();
/*      */         
/*  802 */         if (name.equals("class$")) {
/*  803 */           String descriptor = ((Utf8CpInfo)getCpEntry(ntEntry.getDescriptorIndex())).getString();
/*  804 */           if (descriptor.equals("(Ljava/lang/String;)Ljava/lang/Class;")) {
/*  805 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  810 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean containsClassMethodReference(String cName, String des) {
/*  816 */     for (Enumeration enumeration = this.constantPool.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/*  818 */       Object o = enumeration.nextElement();
/*  819 */       if (o instanceof MethodrefCpInfo) {
/*      */ 
/*      */         
/*  822 */         MethodrefCpInfo entry = (MethodrefCpInfo)o;
/*  823 */         ClassCpInfo classEntry = (ClassCpInfo)getCpEntry(entry.getClassIndex());
/*  824 */         String className = ((Utf8CpInfo)getCpEntry(classEntry.getNameIndex())).getString();
/*  825 */         NameAndTypeCpInfo ntEntry = (NameAndTypeCpInfo)getCpEntry(entry.getNameAndTypeIndex());
/*  826 */         String name = ((Utf8CpInfo)getCpEntry(ntEntry.getNameIndex())).getString();
/*  827 */         String descriptor = ((Utf8CpInfo)getCpEntry(ntEntry.getDescriptorIndex())).getString();
/*      */ 
/*      */         
/*  830 */         if (className.equals(cName) && (name + descriptor).equals(des)) {
/*  831 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  835 */     return false;
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
/*      */   public void remap(NameMapper nm, boolean replaceClassNameStrings, PrintWriter log) {
/*  849 */     String thisClassName = ((Utf8CpInfo)getCpEntry(((ClassCpInfo)getCpEntry(this.u2thisClass)).getNameIndex())).getString();
/*      */ 
/*      */     
/*  852 */     trimAttrsExcept(nm.getAttrsToKeep(thisClassName));
/*      */     
/*      */     int i;
/*  855 */     for (i = 0; i < this.u2attributesCount; i++) {
/*      */       
/*  857 */       AttrInfo attrInfo = this.attributes[i];
/*  858 */       if (attrInfo instanceof RuntimeVisibleAnnotationsAttrInfo) {
/*  859 */         remapAnnotations((RuntimeVisibleAnnotationsAttrInfo)attrInfo, nm);
/*  860 */       } else if (attrInfo instanceof RuntimeVisibleTypeAnnotationsAttrInfo) {
/*  861 */         remapTypeAnnotations((RuntimeVisibleTypeAnnotationsAttrInfo)attrInfo, nm);
/*  862 */       } else if (attrInfo instanceof InnerClassesAttrInfo) {
/*      */         
/*  864 */         InnerClassesInfo[] info = ((InnerClassesAttrInfo)attrInfo).getInfo();
/*  865 */         for (int k = 0; k < info.length; k++) {
/*      */ 
/*      */           
/*  868 */           CpInfo cpInfo = getCpEntry(info[k].getInnerNameIndex());
/*  869 */           if (cpInfo instanceof Utf8CpInfo) {
/*      */ 
/*      */             
/*  872 */             Utf8CpInfo utf = (Utf8CpInfo)cpInfo;
/*  873 */             String origClass = utf.getString();
/*      */ 
/*      */             
/*  876 */             if (!origClass.equals("")) {
/*      */ 
/*      */               
/*  879 */               ClassCpInfo innerClassInfo = (ClassCpInfo)getCpEntry(info[k].getInnerClassIndex());
/*  880 */               String innerClassName = ((Utf8CpInfo)getCpEntry(innerClassInfo.getNameIndex())).getString();
/*      */ 
/*      */               
/*  883 */               String remapClass = nm.mapClass(innerClassName);
/*  884 */               remapClass = remapClass.substring(remapClass.lastIndexOf('$') + 1);
/*  885 */               int remapIndex = this.constantPool.remapUtf8To(remapClass, info[k].getInnerNameIndex());
/*  886 */               info[k].setInnerNameIndex(remapIndex);
/*      */             } 
/*      */           } 
/*      */         } 
/*  890 */       } else if (attrInfo instanceof EnclosingMethodAttrInfo) {
/*  891 */         EnclosingMethodAttrInfo eam = (EnclosingMethodAttrInfo)attrInfo;
/*      */ 
/*      */         
/*  894 */         CpInfo cpi = getCpEntry(eam.getClassIndex());
/*  895 */         if (cpi instanceof ClassCpInfo) {
/*  896 */           ClassCpInfo ccpi = (ClassCpInfo)cpi;
/*  897 */           cpi = getCpEntry(ccpi.getNameIndex());
/*  898 */           if (cpi instanceof Utf8CpInfo) {
/*  899 */             Utf8CpInfo utf = (Utf8CpInfo)cpi;
/*  900 */             String origClass = utf.getString();
/*      */ 
/*      */             
/*  903 */             String remapClass = nm.mapClass(origClass);
/*      */ 
/*      */             
/*  906 */             if (eam.getNameAndTypeIndex() > 0) {
/*  907 */               cpi = getCpEntry(eam.getNameAndTypeIndex());
/*  908 */               if (cpi instanceof NameAndTypeCpInfo) {
/*  909 */                 NameAndTypeCpInfo nameTypeInfo = (NameAndTypeCpInfo)cpi;
/*  910 */                 Utf8CpInfo refUtf = (Utf8CpInfo)getCpEntry(nameTypeInfo.getNameIndex());
/*  911 */                 Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(nameTypeInfo.getDescriptorIndex());
/*  912 */                 String origMethodName = refUtf.getString();
/*  913 */                 String origDescriptor = descUtf.getString();
/*  914 */                 String remapRef = nm.mapMethod(origClass, origMethodName, origDescriptor);
/*  915 */                 String remapDesc = nm.mapDescriptor(descUtf.getString());
/*  916 */                 eam.setNameAndTypeIndex(remapNT(refUtf, remapRef, descUtf, remapDesc, nameTypeInfo, eam.getNameAndTypeIndex()));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*  921 */       } else if (attrInfo instanceof SignatureAttrInfo) {
/*  922 */         remapSignature(nm, (SignatureAttrInfo)attrInfo);
/*  923 */       } else if (attrInfo instanceof SourceFileAttrInfo) {
/*  924 */         SourceFileAttrInfo source = (SourceFileAttrInfo)attrInfo;
/*  925 */         CpInfo cpInfo = getCpEntry(source.getSourceFileIndex());
/*  926 */         if (cpInfo instanceof Utf8CpInfo) {
/*  927 */           Utf8CpInfo utf = (Utf8CpInfo)cpInfo;
/*  928 */           String origName = utf.getString();
/*  929 */           if (origName != null && origName.length() > 0) {
/*  930 */             String newName = nm.mapSourceFile(thisClassName, origName);
/*  931 */             if (!origName.equals(newName)) {
/*  932 */               if (newName == null || newName.length() < 1) {
/*  933 */                 AttrInfo[] newAttributes = new AttrInfo[this.attributes.length - 1];
/*  934 */                 System.arraycopy(this.attributes, 0, newAttributes, 0, i);
/*  935 */                 if (newAttributes.length > i) {
/*  936 */                   System.arraycopy(this.attributes, i + 1, newAttributes, i, newAttributes.length - i);
/*      */                 }
/*  938 */                 this.attributes = newAttributes;
/*  939 */                 this.u2attributesCount--;
/*  940 */                 i--;
/*  941 */                 this.constantPool.decRefCount(source.getAttrNameIndex());
/*  942 */                 utf.decRefCount();
/*      */               } else {
/*  944 */                 int remapIndex = this.constantPool.remapUtf8To(newName, source.getSourceFileIndex());
/*  945 */                 source.setSourceFileIndex(remapIndex);
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  967 */       else if (attrInfo instanceof RecordAttrInfo) {
/*  968 */         RecordAttrInfo record = (RecordAttrInfo)attrInfo;
/*  969 */         RecordComponent[] components = record.getComponents();
/*  970 */         for (int k = 0, n = components.length; k < n; k++) {
/*  971 */           int nameIndex = components[k].getNameIndex();
/*  972 */           Utf8CpInfo nameUtf = (Utf8CpInfo)getCpEntry(nameIndex);
/*  973 */           String remapName = nm.mapField(thisClassName, nameUtf.getString());
/*  974 */           int remapNameIndex = this.constantPool.remapUtf8To(remapName, nameIndex);
/*  975 */           components[k].setNameIndex(remapNameIndex);
/*      */           
/*  977 */           int descIndex = components[k].getDescriptorIndex();
/*  978 */           Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(descIndex);
/*  979 */           String remapDesc = nm.mapDescriptor(descUtf.getString());
/*  980 */           int remapDescIndex = this.constantPool.remapUtf8To(remapDesc, descIndex);
/*  981 */           components[k].setDescriptorIndex(remapDescIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  993 */           AttrInfo[] attributes = components[k].getAttributes();
/*  994 */           for (int m = 0; m < attributes.length; m++) {
/*  995 */             if (attributes[m] instanceof RuntimeVisibleAnnotationsAttrInfo) {
/*  996 */               remapAnnotations((RuntimeVisibleAnnotationsAttrInfo)attributes[m], nm);
/*  997 */             } else if (attributes[m] instanceof RuntimeVisibleTypeAnnotationsAttrInfo) {
/*  998 */               remapTypeAnnotations((RuntimeVisibleTypeAnnotationsAttrInfo)attributes[m], nm);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1007 */     for (i = 0; i < this.u2methodsCount; i++) {
/*      */       
/* 1009 */       for (int k = 0; k < (this.methods[i]).u2attributesCount; k++) {
/*      */         
/* 1011 */         String methodName = this.methods[i].getName();
/* 1012 */         String descriptor = this.methods[i].getDescriptor();
/* 1013 */         AttrInfo attrInfo = (this.methods[i]).attributes[k];
/*      */         
/* 1015 */         if (attrInfo instanceof AnnotationDefaultAttrInfo) {
/* 1016 */           remapAnnotationDefault((AnnotationDefaultAttrInfo)attrInfo, nm);
/* 1017 */         } else if (attrInfo instanceof RuntimeVisibleAnnotationsAttrInfo) {
/* 1018 */           remapAnnotations((RuntimeVisibleAnnotationsAttrInfo)attrInfo, nm);
/* 1019 */         } else if (attrInfo instanceof RuntimeVisibleParameterAnnotationsAttrInfo) {
/* 1020 */           remapParameterAnnotations((RuntimeVisibleParameterAnnotationsAttrInfo)attrInfo, nm);
/* 1021 */         } else if (attrInfo instanceof RuntimeVisibleTypeAnnotationsAttrInfo) {
/* 1022 */           remapTypeAnnotations((RuntimeVisibleTypeAnnotationsAttrInfo)attrInfo, nm);
/* 1023 */         } else if (attrInfo instanceof SignatureAttrInfo) {
/* 1024 */           remapSignature(nm, (SignatureAttrInfo)attrInfo);
/* 1025 */         } else if (attrInfo instanceof CodeAttrInfo) {
/* 1026 */           CodeAttrInfo codeAttrInfo = (CodeAttrInfo)attrInfo;
/* 1027 */           for (int m = 0; m < codeAttrInfo.u2attributesCount; m++) {
/* 1028 */             AttrInfo innerAttrInfo = codeAttrInfo.attributes[m];
/* 1029 */             if (innerAttrInfo instanceof LocalVariableTableAttrInfo) {
/* 1030 */               LocalVariableTableAttrInfo lvtAttrInfo = (LocalVariableTableAttrInfo)innerAttrInfo;
/* 1031 */               LocalVariableInfo[] lvts = lvtAttrInfo.getLocalVariableTable();
/* 1032 */               for (int n = 0; n < lvts.length; n++) {
/*      */                 
/* 1034 */                 Utf8CpInfo nameUtf = (Utf8CpInfo)getCpEntry(lvts[n].getNameIndex());
/* 1035 */                 String remapName = nm.mapLocalVariable(thisClassName, methodName, descriptor, nameUtf.getString());
/* 1036 */                 if (remapName == null || remapName.length() < 1) {
/* 1037 */                   this.constantPool.decRefCount(lvts[n].getNameIndex());
/* 1038 */                   this.constantPool.decRefCount(lvts[n].getDescriptorIndex());
/* 1039 */                   LocalVariableInfo[] newArray = new LocalVariableInfo[lvts.length - 1];
/* 1040 */                   System.arraycopy(lvts, 0, newArray, 0, n);
/* 1041 */                   if (newArray.length > n) {
/* 1042 */                     System.arraycopy(lvts, n + 1, newArray, n, newArray.length - n);
/*      */                   }
/* 1044 */                   lvts = newArray;
/* 1045 */                   lvtAttrInfo.setLocalVariableTable(lvts);
/* 1046 */                   n--;
/*      */                 } else {
/* 1048 */                   lvts[n].setNameIndex(this.constantPool.remapUtf8To(remapName, lvts[n].getNameIndex()));
/*      */ 
/*      */                   
/* 1051 */                   Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(lvts[n].getDescriptorIndex());
/* 1052 */                   String remapDesc = nm.mapDescriptor(descUtf.getString());
/* 1053 */                   lvts[n].setDescriptorIndex(this.constantPool.remapUtf8To(remapDesc, lvts[n].getDescriptorIndex()));
/*      */                 } 
/*      */               } 
/* 1056 */             } else if (innerAttrInfo instanceof LocalVariableTypeTableAttrInfo) {
/* 1057 */               LocalVariableTypeTableAttrInfo lvttAttrInfo = (LocalVariableTypeTableAttrInfo)innerAttrInfo;
/* 1058 */               LocalVariableTypeInfo[] lvts = lvttAttrInfo.getLocalVariableTypeTable();
/* 1059 */               for (int n = 0; n < lvts.length; n++) {
/*      */                 
/* 1061 */                 Utf8CpInfo nameUtf = (Utf8CpInfo)getCpEntry(lvts[n].getNameIndex());
/* 1062 */                 String remapName = nm.mapLocalVariable(thisClassName, methodName, descriptor, nameUtf.getString());
/* 1063 */                 if (remapName == null || remapName.length() < 1) {
/* 1064 */                   this.constantPool.decRefCount(lvts[n].getNameIndex());
/* 1065 */                   this.constantPool.decRefCount(lvts[n].getSignatureIndex());
/* 1066 */                   LocalVariableTypeInfo[] newArray = new LocalVariableTypeInfo[lvts.length - 1];
/* 1067 */                   System.arraycopy(lvts, 0, newArray, 0, n);
/* 1068 */                   if (newArray.length > n) {
/* 1069 */                     System.arraycopy(lvts, n + 1, newArray, n, newArray.length - n);
/*      */                   }
/* 1071 */                   lvts = newArray;
/* 1072 */                   lvttAttrInfo.setLocalVariableTypeTable(lvts);
/* 1073 */                   n--;
/*      */                 } else {
/* 1075 */                   lvts[n].setNameIndex(this.constantPool.remapUtf8To(remapName, lvts[n].getNameIndex()));
/*      */ 
/*      */                   
/* 1078 */                   Utf8CpInfo signatureUtf = (Utf8CpInfo)getCpEntry(lvts[n].getSignatureIndex());
/* 1079 */                   String remapSig = nm.mapSignature(signatureUtf.getString());
/* 1080 */                   lvts[n].setSignatureIndex(this.constantPool.remapUtf8To(remapSig, lvts[n].getSignatureIndex()));
/*      */                 } 
/*      */               } 
/* 1083 */             } else if (innerAttrInfo instanceof LineNumberTableAttrInfo) {
/* 1084 */               LineNumberTableAttrInfo ltai = (LineNumberTableAttrInfo)innerAttrInfo;
/* 1085 */               if (!nm.mapLineNumberTable(thisClassName, methodName, descriptor, ltai)) {
/* 1086 */                 AttrInfo[] newAtt = new AttrInfo[codeAttrInfo.u2attributesCount - 1];
/* 1087 */                 System.arraycopy(codeAttrInfo.attributes, 0, newAtt, 0, m);
/* 1088 */                 if (newAtt.length > m) {
/* 1089 */                   System.arraycopy(codeAttrInfo.attributes, m + 1, newAtt, m, newAtt.length - m);
/*      */                 }
/* 1091 */                 codeAttrInfo.attributes = newAtt;
/* 1092 */                 codeAttrInfo.u2attributesCount--;
/* 1093 */                 m--;
/*      */               } 
/* 1095 */             } else if (innerAttrInfo instanceof RuntimeVisibleTypeAnnotationsAttrInfo) {
/* 1096 */               remapTypeAnnotations((RuntimeVisibleTypeAnnotationsAttrInfo)innerAttrInfo, nm);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1104 */     for (i = 0; i < this.u2fieldsCount; i++) {
/*      */ 
/*      */       
/* 1107 */       FieldInfo field = this.fields[i];
/* 1108 */       Utf8CpInfo nameUtf = (Utf8CpInfo)getCpEntry(field.getNameIndex());
/* 1109 */       if (!field.isSynthetic() || nameUtf.getString().startsWith("class$")) {
/*      */         
/* 1111 */         String remapName = nm.mapField(thisClassName, nameUtf.getString());
/* 1112 */         field.setNameIndex(this.constantPool.remapUtf8To(remapName, field.getNameIndex()));
/*      */       } 
/*      */       
/* 1115 */       for (int k = 0; k < field.u2attributesCount; k++) {
/* 1116 */         AttrInfo attrInfo = field.attributes[k];
/* 1117 */         if (attrInfo instanceof RuntimeVisibleAnnotationsAttrInfo) {
/* 1118 */           remapAnnotations((RuntimeVisibleAnnotationsAttrInfo)attrInfo, nm);
/* 1119 */         } else if (attrInfo instanceof RuntimeVisibleTypeAnnotationsAttrInfo) {
/* 1120 */           remapTypeAnnotations((RuntimeVisibleTypeAnnotationsAttrInfo)attrInfo, nm);
/* 1121 */         } else if (attrInfo instanceof SignatureAttrInfo) {
/* 1122 */           remapSignature(nm, (SignatureAttrInfo)attrInfo);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1127 */       Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(field.getDescriptorIndex());
/* 1128 */       String remapDesc = nm.mapDescriptor(descUtf.getString());
/* 1129 */       field.setDescriptorIndex(this.constantPool.remapUtf8To(remapDesc, field.getDescriptorIndex()));
/*      */     } 
/* 1131 */     for (i = 0; i < this.u2methodsCount; i++) {
/*      */ 
/*      */       
/* 1134 */       MethodInfo method = this.methods[i];
/* 1135 */       Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(method.getDescriptorIndex());
/* 1136 */       if (!method.isSynthetic()) {
/*      */         
/* 1138 */         Utf8CpInfo nameUtf = (Utf8CpInfo)getCpEntry(method.getNameIndex());
/* 1139 */         String remapName = nm.mapMethod(thisClassName, nameUtf.getString(), descUtf.getString());
/* 1140 */         method.setNameIndex(this.constantPool.remapUtf8To(remapName, method.getNameIndex()));
/*      */       } 
/*      */ 
/*      */       
/* 1144 */       String remapDesc = nm.mapDescriptor(descUtf.getString());
/* 1145 */       method.setDescriptorIndex(this.constantPool.remapUtf8To(remapDesc, method.getDescriptorIndex()));
/*      */     } 
/*      */ 
/*      */     
/* 1149 */     if (replaceClassNameStrings && nm instanceof ClassTree)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1155 */       replaceConstantPoolStrings((ClassTree)nm);
/*      */     }
/*      */     
/* 1158 */     LinkedHashSet<Integer> ombIndicies = new LinkedHashSet();
/* 1159 */     int currentCpLength = this.constantPool.length(); int j;
/* 1160 */     for (j = 0; j < currentCpLength; j++) {
/* 1161 */       CpInfo cpInfo = getCpEntry(j);
/* 1162 */       if (cpInfo != null)
/*      */       {
/* 1164 */         if (cpInfo instanceof InvokeDynamicCpInfo) {
/* 1165 */           NameAndTypeCpInfo nameTypeInfo; int idx; String sig; Utf8CpInfo refUtf; NameAndTypeCpInfo ntInfo; Utf8CpInfo descUtf, utf8CpInfo1; String descriptor; Utf8CpInfo utf8CpInfo2; String className, remapDesc; MethodTypeCpInfo methodTypeInfo; Utf8CpInfo samMethodDescriptor; String remapName, str1; InvokeDynamicCpInfo id = (InvokeDynamicCpInfo)cpInfo;
/*      */           
/* 1167 */           BootstrapMethod bm = getBootstrapMethod(id);
/* 1168 */           switch (getType(bm)) {
/*      */             case 1:
/* 1170 */               nameTypeInfo = (NameAndTypeCpInfo)getCpEntry(id.getNameAndTypeIndex());
/* 1171 */               refUtf = (Utf8CpInfo)getCpEntry(nameTypeInfo.getNameIndex());
/* 1172 */               descUtf = (Utf8CpInfo)getCpEntry(nameTypeInfo.getDescriptorIndex());
/*      */               
/* 1174 */               descriptor = descUtf.getString();
/* 1175 */               className = descriptor.substring(descriptor.indexOf(")L") + 2, descriptor.length() - 1);
/*      */ 
/*      */               
/* 1178 */               methodTypeInfo = (MethodTypeCpInfo)getCpEntry(bm.getBootstrapArguments()[0]);
/* 1179 */               samMethodDescriptor = (Utf8CpInfo)getCpEntry(methodTypeInfo.getU2descriptorIndex());
/*      */               
/* 1181 */               remapName = nm.mapMethod(className, refUtf.getString(), samMethodDescriptor.getString());
/* 1182 */               str1 = nm.mapDescriptor(descUtf.getString());
/*      */               
/* 1184 */               id.setNameAndTypeIndex(remapNT(refUtf, remapName, descUtf, str1, nameTypeInfo, id.getNameAndTypeIndex()));
/*      */               break;
/*      */             case 2:
/* 1187 */               idx = id.getNameAndTypeIndex();
/*      */               
/* 1189 */               ntInfo = (NameAndTypeCpInfo)getCpEntry(idx);
/* 1190 */               utf8CpInfo1 = (Utf8CpInfo)getCpEntry(ntInfo.getNameIndex());
/* 1191 */               utf8CpInfo2 = (Utf8CpInfo)getCpEntry(ntInfo.getDescriptorIndex());
/*      */               
/* 1193 */               remapDesc = nm.mapDescriptor(utf8CpInfo2.getString());
/*      */               
/* 1195 */               id.setNameAndTypeIndex(remapNT(utf8CpInfo1, utf8CpInfo1.getString(), utf8CpInfo2, remapDesc, ntInfo, idx));
/*      */               break;
/*      */             case 3:
/* 1198 */               ombIndicies.add(Integer.valueOf(id.getBootstrapMethodAttrIndex()));
/*      */               
/* 1200 */               idx = id.getNameAndTypeIndex();
/*      */               
/* 1202 */               ntInfo = (NameAndTypeCpInfo)getCpEntry(idx);
/* 1203 */               utf8CpInfo1 = (Utf8CpInfo)getCpEntry(ntInfo.getNameIndex());
/* 1204 */               utf8CpInfo2 = (Utf8CpInfo)getCpEntry(ntInfo.getDescriptorIndex());
/*      */               
/* 1206 */               remapDesc = nm.mapDescriptor(utf8CpInfo2.getString());
/*      */               
/* 1208 */               id.setNameAndTypeIndex(remapNT(utf8CpInfo1, utf8CpInfo1.getString(), utf8CpInfo2, remapDesc, ntInfo, idx));
/*      */               break;
/*      */             default:
/* 1211 */               sig = getBootstrapMethodSignature(bm);
/* 1212 */               throw new IllegalArgumentException("Unrecognized bootstrap method: " + sig);
/*      */           } 
/*      */         
/*      */         } 
/*      */       }
/*      */     } 
/* 1218 */     if (!ombIndicies.isEmpty()) {
/* 1219 */       BootstrapMethodsAttrInfo attr = getBootstrapMethodAttribute();
/* 1220 */       for (Iterator<Integer> it = ombIndicies.iterator(); it.hasNext(); ) {
/* 1221 */         BootstrapMethod bm = attr.getBootstrapMethods()[((Integer)it.next()).intValue()];
/*      */         
/* 1223 */         StringCpInfo namesInfo = (StringCpInfo)getCpEntry(bm.getBootstrapArguments()[1]);
/* 1224 */         Utf8CpInfo names = (Utf8CpInfo)getCpEntry(namesInfo.getStringIndex());
/* 1225 */         StringBuilder sb = new StringBuilder();
/* 1226 */         String delim = ";";
/* 1227 */         for (StringTokenizer st = new StringTokenizer(names.getString(), ";", true); st.hasMoreTokens(); ) {
/* 1228 */           String origName = st.nextToken();
/* 1229 */           if (";".equals(origName)) {
/* 1230 */             sb.append(";"); continue;
/*      */           } 
/* 1232 */           sb.append(nm.mapField(thisClassName, origName));
/*      */         } 
/*      */         
/* 1235 */         String remapNames = sb.toString();
/* 1236 */         int remapNamesIndex = this.constantPool.addUtf8Entry(remapNames);
/* 1237 */         StringCpInfo remapNamesInfo = new StringCpInfo();
/* 1238 */         remapNamesInfo.setStringIndex(remapNamesIndex);
/* 1239 */         int remapNamesInfoIndex = this.constantPool.addEntry(remapNamesInfo);
/* 1240 */         bm.getBootstrapArguments()[1] = remapNamesInfoIndex;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1245 */     currentCpLength = this.constantPool.length();
/* 1246 */     for (j = 0; j < currentCpLength; j++) {
/*      */       
/* 1248 */       CpInfo cpInfo = getCpEntry(j);
/* 1249 */       if (cpInfo != null)
/*      */       {
/*      */         
/* 1252 */         if (cpInfo instanceof MethodTypeCpInfo) {
/* 1253 */           MethodTypeCpInfo mt = (MethodTypeCpInfo)cpInfo;
/* 1254 */           Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(mt.getU2descriptorIndex());
/* 1255 */           String remapDesc = nm.mapDescriptor(descUtf.getString());
/* 1256 */           mt.setU2descriptorIndex(this.constantPool.remapUtf8To(remapDesc, mt.getU2descriptorIndex()));
/*      */         }
/* 1258 */         else if (cpInfo instanceof RefCpInfo) {
/*      */           String remapRef;
/*      */           
/* 1261 */           ClassCpInfo classInfo = (ClassCpInfo)getCpEntry(((RefCpInfo)cpInfo).getClassIndex());
/* 1262 */           Utf8CpInfo classUtf = (Utf8CpInfo)getCpEntry(classInfo.getNameIndex());
/* 1263 */           String className = classUtf.getString();
/*      */ 
/*      */           
/* 1266 */           int ntIndex = ((RefCpInfo)cpInfo).getNameAndTypeIndex();
/*      */           
/* 1268 */           NameAndTypeCpInfo nameTypeInfo = (NameAndTypeCpInfo)getCpEntry(ntIndex);
/* 1269 */           Utf8CpInfo refUtf = (Utf8CpInfo)getCpEntry(nameTypeInfo.getNameIndex());
/* 1270 */           Utf8CpInfo descUtf = (Utf8CpInfo)getCpEntry(nameTypeInfo.getDescriptorIndex());
/*      */ 
/*      */ 
/*      */           
/* 1274 */           if (cpInfo instanceof FieldrefCpInfo) {
/*      */             
/* 1276 */             remapRef = nm.mapField(className, refUtf.getString());
/*      */ 
/*      */ 
/*      */             
/* 1280 */             if (refUtf.getString().startsWith("class$"))
/*      */             {
/* 1282 */               if (!replaceClassNameStrings) {
/* 1283 */                 String internalClassName = refUtf.getString().substring(6);
/* 1284 */                 String realClassName = internalClassName.replace('$', '.');
/* 1285 */                 internalClassName = internalClassName.replace('$', '/');
/* 1286 */                 String map = nm.mapClass(internalClassName);
/* 1287 */                 if (map != null && !internalClassName.equals(map))
/*      */                 {
/*      */                   
/* 1290 */                   String warning = realClassName + " shouldn't be obfuscated: it is most likely referenced as " + realClassName + ".class from " + Conversion.toJavaClass(thisClassName);
/* 1291 */                   Logger.getInstance().warning(warning);
/* 1292 */                   log.println("<!-- WARNING: " + warning + " -->");
/*      */                 }
/*      */               
/*      */               } 
/*      */             }
/*      */           } else {
/*      */             
/* 1299 */             remapRef = nm.mapMethod(className, refUtf.getString(), descUtf.getString());
/*      */           } 
/* 1301 */           String remapDesc = nm.mapDescriptor(descUtf.getString());
/* 1302 */           ((RefCpInfo)cpInfo).setNameAndTypeIndex(remapNT(refUtf, remapRef, descUtf, remapDesc, nameTypeInfo, ((RefCpInfo)cpInfo).getNameAndTypeIndex()));
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1308 */     for (j = 0; j < this.constantPool.length(); j++) {
/*      */       
/* 1310 */       CpInfo cpInfo = getCpEntry(j);
/* 1311 */       if (cpInfo != null)
/*      */       {
/*      */         
/* 1314 */         if (cpInfo instanceof ClassCpInfo) {
/*      */           
/* 1316 */           ClassCpInfo classInfo = (ClassCpInfo)cpInfo;
/* 1317 */           Utf8CpInfo utf = (Utf8CpInfo)getCpEntry(classInfo.getNameIndex());
/* 1318 */           String remapClass = nm.mapClass(utf.getString());
/* 1319 */           int remapIndex = this.constantPool.remapUtf8To(remapClass, classInfo.getNameIndex());
/* 1320 */           classInfo.setNameIndex(remapIndex);
/* 1321 */         } else if (cpInfo instanceof PackageCpInfo) {
/* 1322 */           PackageCpInfo info = (PackageCpInfo)cpInfo;
/* 1323 */           int pnIdx = info.getNameIndex();
/* 1324 */           CpInfo pnInfo = getCpEntry(pnIdx);
/* 1325 */           if (pnInfo instanceof Utf8CpInfo) {
/* 1326 */             String oldName = ((Utf8CpInfo)pnInfo).getString();
/* 1327 */             String newName = nm.mapPackage(oldName);
/* 1328 */             info.setNameIndex(this.constantPool.remapUtf8To(newName, pnIdx));
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private BootstrapMethodsAttrInfo getBootstrapMethodAttribute() {
/* 1336 */     for (int i = 0; i < this.attributes.length; i++) {
/* 1337 */       AttrInfo attribute = this.attributes[i];
/* 1338 */       if (attribute instanceof BootstrapMethodsAttrInfo) {
/* 1339 */         return (BootstrapMethodsAttrInfo)attribute;
/*      */       }
/*      */     } 
/* 1342 */     throw new RuntimeException("No BootstrapMethod attribute in class file");
/*      */   }
/*      */   
/*      */   private int getType(BootstrapMethod method) {
/* 1346 */     String sig = getBootstrapMethodSignature(method);
/* 1347 */     if ("java/lang/invoke/StringConcatFactory#makeConcat(...)".equals(sig))
/* 1348 */       return 2; 
/* 1349 */     if ("java/lang/invoke/StringConcatFactory#makeConcatWithConstants(...)".equals(sig))
/* 1350 */       return 2; 
/* 1351 */     if ("java/lang/invoke/LambdaMetafactory#metafactory(...)".equals(sig))
/* 1352 */       return 1; 
/* 1353 */     if ("java/lang/invoke/LambdaMetafactory#altMetafactory(...)".equals(sig))
/* 1354 */       return 1; 
/* 1355 */     if ("java/lang/runtime/ObjectMethods#bootstrap(...)".equals(sig)) {
/* 1356 */       return 3;
/*      */     }
/* 1358 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private BootstrapMethod getBootstrapMethod(InvokeDynamicCpInfo info) {
/* 1363 */     BootstrapMethodsAttrInfo bmInfo = getBootstrapMethodAttribute();
/* 1364 */     return bmInfo.getBootstrapMethods()[info.getBootstrapMethodAttrIndex()];
/*      */   }
/*      */   
/*      */   private String getBootstrapMethodSignature(BootstrapMethod method) {
/* 1368 */     int mhIdx = method.getBootstrapMethodRef();
/* 1369 */     MethodHandleCpInfo mhInfo = (MethodHandleCpInfo)getCpEntry(mhIdx);
/* 1370 */     RefCpInfo mrInfo = (RefCpInfo)getCpEntry(mhInfo.getReferenceIndex());
/* 1371 */     ClassCpInfo cpInfo = (ClassCpInfo)getCpEntry(mrInfo.getClassIndex());
/* 1372 */     String className = getUtf8(cpInfo.getNameIndex());
/* 1373 */     NameAndTypeCpInfo ntInfo = (NameAndTypeCpInfo)getCpEntry(mrInfo.getNameAndTypeIndex());
/* 1374 */     String memberName = getUtf8(ntInfo.getNameIndex());
/* 1375 */     return className + '#' + memberName + getReferenceKindSuffix(mhInfo.getReferenceKind());
/*      */   }
/*      */   
/*      */   private static String getReferenceKindSuffix(int referenceKind) {
/* 1379 */     switch (referenceKind) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/* 1384 */         return "";
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/* 1391 */         return "(...)";
/*      */     } 
/*      */     
/* 1394 */     throw new IllegalArgumentException("Invalid reference kind: " + referenceKind);
/*      */   }
/*      */ 
/*      */   
/*      */   private void remapAnnotationDefault(AnnotationDefaultAttrInfo annotationDefault, NameMapper nm) {
/* 1399 */     remapElementValue(annotationDefault.elementValue, nm);
/*      */   }
/*      */   
/*      */   private void remapAnnotations(RuntimeVisibleAnnotationsAttrInfo annotation, NameMapper nm) {
/* 1403 */     remapAnnotationInfoImpl(annotation.getAnnotations(), nm);
/*      */   }
/*      */   
/*      */   private void remapParameterAnnotations(RuntimeVisibleParameterAnnotationsAttrInfo annotation, NameMapper nm) {
/* 1407 */     ParameterAnnotationInfo[] annotations = annotation.getParameterAnnotations();
/* 1408 */     if (annotations != null) {
/* 1409 */       for (int i = 0; i < annotations.length; i++) {
/* 1410 */         ParameterAnnotationInfo info = annotations[i];
/* 1411 */         remapAnnotationInfoImpl(info.getAnnotations(), nm);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void remapTypeAnnotations(RuntimeVisibleTypeAnnotationsAttrInfo annotation, NameMapper nm) {
/* 1417 */     remapAnnotationInfoImpl((AnnotationInfo[])annotation.getAnnotations(), nm);
/*      */   }
/*      */   
/*      */   private void remapAnnotationInfoImpl(AnnotationInfo[] a, NameMapper nm) {
/* 1421 */     if (a != null) {
/* 1422 */       for (int j = 0; j < a.length; j++) {
/* 1423 */         remapAnnotation(a[j], nm);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void remapAnnotation(AnnotationInfo annotation, NameMapper nm) {
/* 1429 */     CpInfo info = getCpEntry(annotation.u2typeIndex);
/* 1430 */     if (info instanceof Utf8CpInfo) {
/* 1431 */       Utf8CpInfo utf = (Utf8CpInfo)info;
/* 1432 */       String s = utf.getString();
/* 1433 */       if (s.length() > 2 && s.charAt(0) == 'L' && s.charAt(s.length() - 1) == ';') {
/* 1434 */         String fqn = s.substring(1, s.length() - 1);
/* 1435 */         String newFqn = nm.mapClass(fqn);
/* 1436 */         if (!fqn.equals(newFqn)) {
/* 1437 */           annotation.u2typeIndex = this.constantPool.remapUtf8To('L' + newFqn + ';', annotation.u2typeIndex);
/*      */         }
/* 1439 */         ElementValuePairInfo[] evp = annotation.getElementValuePairs();
/* 1440 */         if (evp != null)
/* 1441 */           for (int i = 0; i < evp.length; i++) {
/* 1442 */             ElementValuePairInfo elementValuePair = evp[i];
/* 1443 */             utf = (Utf8CpInfo)getCpEntry(elementValuePair.u2ElementNameIndex);
/* 1444 */             String remapName = nm.mapAnnotationField(fqn, utf.getString());
/* 1445 */             if (!remapName.equals(utf.getString())) {
/* 1446 */               elementValuePair.u2ElementNameIndex = this.constantPool.remapUtf8To(remapName, elementValuePair.u2ElementNameIndex);
/*      */             }
/* 1448 */             ElementValueInfo elementValue = elementValuePair.elementValue;
/* 1449 */             remapElementValue(elementValue, nm);
/*      */           }  
/*      */       } 
/*      */     }  } private void remapElementValue(ElementValueInfo elementValue, NameMapper nm) {
/*      */     Utf8CpInfo utf;
/*      */     int j;
/*      */     String name;
/*      */     String remapName;
/* 1457 */     switch (elementValue.u1Tag) {
/*      */       case 66:
/*      */       case 67:
/*      */       case 68:
/*      */       case 70:
/*      */       case 73:
/*      */       case 74:
/*      */       case 83:
/*      */       case 90:
/*      */       case 115:
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 101:
/* 1473 */         utf = (Utf8CpInfo)getCpEntry(elementValue.u2typeNameIndex);
/* 1474 */         name = utf.getString();
/* 1475 */         remapName = nm.mapDescriptor(name);
/* 1476 */         elementValue.u2typeNameIndex = this.constantPool.remapUtf8To(remapName, elementValue.u2typeNameIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 99:
/* 1482 */         utf = (Utf8CpInfo)getCpEntry(elementValue.u2cpIndex);
/* 1483 */         name = utf.getString();
/* 1484 */         remapName = nm.mapDescriptor(name);
/* 1485 */         elementValue.u2cpIndex = this.constantPool.remapUtf8To(remapName, elementValue.u2cpIndex);
/*      */ 
/*      */       
/*      */       case 64:
/* 1489 */         remapAnnotation(elementValue.nestedAnnotation, nm);
/*      */       
/*      */       case 91:
/* 1492 */         for (j = 0; j < elementValue.arrayValues.length; j++) {
/*      */           
/* 1494 */           ElementValueInfo evi = elementValue.arrayValues[j];
/* 1495 */           remapElementValue(evi, nm);
/*      */         } 
/*      */     } 
/*      */     
/* 1499 */     throw new RuntimeException("Unknown type tag in annotation!");
/*      */   }
/*      */ 
/*      */   
/*      */   private void remapSignature(NameMapper nm, SignatureAttrInfo signature) {
/* 1504 */     CpInfo cpInfo = getCpEntry(signature.getSignatureIndex());
/* 1505 */     if (cpInfo instanceof Utf8CpInfo) {
/* 1506 */       Utf8CpInfo utf = (Utf8CpInfo)cpInfo;
/* 1507 */       String sig = utf.getString();
/* 1508 */       String remapSignature = nm.mapSignature(sig);
/* 1509 */       if (!sig.equals(remapSignature)) {
/* 1510 */         int remapIndex = this.constantPool.remapUtf8To(remapSignature, signature.getSignatureIndex());
/* 1511 */         signature.setSignatureIndex(remapIndex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int remapNT(Utf8CpInfo refUtf, String remapRef, Utf8CpInfo descUtf, String remapDesc, NameAndTypeCpInfo nameTypeInfo, int nameAndTypeIndex) {
/* 1520 */     if (!remapRef.equals(refUtf.getString()) || !remapDesc.equals(descUtf.getString())) {
/*      */       NameAndTypeCpInfo newNameTypeInfo;
/*      */ 
/*      */       
/* 1524 */       if (nameTypeInfo.getRefCount() == 1) {
/*      */         
/* 1526 */         newNameTypeInfo = nameTypeInfo;
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1531 */         newNameTypeInfo = (NameAndTypeCpInfo)nameTypeInfo.clone();
/*      */ 
/*      */         
/* 1534 */         getCpEntry(newNameTypeInfo.getNameIndex()).incRefCount();
/* 1535 */         getCpEntry(newNameTypeInfo.getDescriptorIndex()).incRefCount();
/*      */ 
/*      */ 
/*      */         
/* 1539 */         nameAndTypeIndex = this.constantPool.addEntry(newNameTypeInfo);
/*      */ 
/*      */         
/* 1542 */         newNameTypeInfo.incRefCount();
/* 1543 */         nameTypeInfo.decRefCount();
/*      */       } 
/*      */ 
/*      */       
/* 1547 */       newNameTypeInfo.setNameIndex(this.constantPool.remapUtf8To(remapRef, newNameTypeInfo.getNameIndex()));
/* 1548 */       newNameTypeInfo.setDescriptorIndex(this.constantPool.remapUtf8To(remapDesc, newNameTypeInfo.getDescriptorIndex()));
/*      */     } 
/* 1550 */     return nameAndTypeIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void replaceConstantPoolStrings(ClassTree ct) {
/* 1558 */     for (Enumeration<CpInfo> enumeration = this.constantPool.elements(); enumeration.hasMoreElements(); ) {
/* 1559 */       CpInfo cpi = enumeration.nextElement();
/* 1560 */       if (cpi instanceof Utf8CpInfo) {
/* 1561 */         Utf8CpInfo ui = (Utf8CpInfo)cpi;
/* 1562 */         String s = ui.getString();
/* 1563 */         boolean jikes = false;
/* 1564 */         if (s.length() > 5 && s.startsWith("[L") && s.endsWith(";")) {
/* 1565 */           s = s.substring(2, s.length() - 1);
/* 1566 */           jikes = true;
/*      */         } 
/* 1568 */         if (s.length() > 2 && Character.isJavaIdentifierPart(s.charAt(s.length() - 1)) && s
/* 1569 */           .indexOf(' ') < 0 && s.indexOf('.') > 0) {
/* 1570 */           Cl cl = ct.findClassForName(s);
/* 1571 */           if (cl != null && 
/* 1572 */             !cl.getFullInName().equals(cl.getFullOutName())) {
/* 1573 */             if (jikes) {
/* 1574 */               ui.setString("[L" + cl.getFullOutName().replace('/', '.') + ";"); continue;
/*      */             } 
/* 1576 */             ui.setString(cl.getFullOutName().replace('/', '.'));
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
/*      */ 
/*      */   
/*      */   public void write(DataOutput dout) throws IOException {
/* 1593 */     if (dout == null) throw new NullPointerException("No output stream was provided."); 
/* 1594 */     dout.writeInt(this.u4magic);
/* 1595 */     dout.writeShort(this.u2minorVersion);
/* 1596 */     dout.writeShort(this.u2majorVersion);
/* 1597 */     dout.writeShort(this.constantPool.length() + (writeIdString ? 1 : 0));
/* 1598 */     for (Enumeration<CpInfo> enumeration = this.constantPool.elements(); enumeration.hasMoreElements(); ) {
/*      */       
/* 1600 */       CpInfo cpInfo = enumeration.nextElement();
/* 1601 */       if (cpInfo != null)
/*      */       {
/* 1603 */         cpInfo.write(dout);
/*      */       }
/*      */     } 
/* 1606 */     if (writeIdString) {
/* 1607 */       cpIdString.write(dout);
/*      */     }
/* 1609 */     dout.writeShort(this.u2accessFlags);
/* 1610 */     dout.writeShort(this.u2thisClass);
/* 1611 */     dout.writeShort(this.u2superClass);
/* 1612 */     dout.writeShort(this.u2interfacesCount); int i;
/* 1613 */     for (i = 0; i < this.u2interfacesCount; i++)
/*      */     {
/* 1615 */       dout.writeShort(this.u2interfaces[i]);
/*      */     }
/* 1617 */     dout.writeShort(this.u2fieldsCount);
/* 1618 */     for (i = 0; i < this.u2fieldsCount; i++)
/*      */     {
/* 1620 */       this.fields[i].write(dout);
/*      */     }
/* 1622 */     dout.writeShort(this.u2methodsCount);
/* 1623 */     for (i = 0; i < this.u2methodsCount; i++)
/*      */     {
/* 1625 */       this.methods[i].write(dout);
/*      */     }
/* 1627 */     dout.writeShort(this.u2attributesCount);
/* 1628 */     for (i = 0; i < this.u2attributesCount; i++)
/*      */     {
/* 1630 */       this.attributes[i].write(dout);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dump(PrintWriter pw) {
/* 1641 */     pw.println("_____________________________________________________________________");
/* 1642 */     pw.println("CLASS: " + getName());
/* 1643 */     pw.println("Magic: " + Integer.toHexString(this.u4magic));
/* 1644 */     pw.println("Minor version: " + Integer.toHexString(this.u2minorVersion));
/* 1645 */     pw.println("Major version: " + Integer.toHexString(this.u2majorVersion));
/* 1646 */     pw.println();
/* 1647 */     pw.println("CP length: " + Integer.toHexString(this.constantPool.length())); int i;
/* 1648 */     for (i = 0; i < this.constantPool.length(); i++) {
/*      */       
/* 1650 */       CpInfo cpInfo = this.constantPool.getCpEntry(i);
/* 1651 */       if (cpInfo != null)
/*      */       {
/* 1653 */         cpInfo.dump(pw, this, i);
/*      */       }
/*      */     } 
/* 1656 */     pw.println("Access: " + Integer.toHexString(this.u2accessFlags));
/* 1657 */     pw.println("This class: " + getName());
/* 1658 */     pw.println("Superclass: " + getSuper());
/* 1659 */     pw.println("Interfaces count: " + Integer.toHexString(this.u2interfacesCount));
/* 1660 */     for (i = 0; i < this.u2interfacesCount; i++) {
/*      */       
/* 1662 */       CpInfo info = getCpEntry(this.u2interfaces[i]);
/* 1663 */       if (info == null) {
/*      */         
/* 1665 */         pw.println("  Interface " + Integer.toHexString(i) + ": (null)");
/*      */       }
/*      */       else {
/*      */         
/* 1669 */         pw.println("  Interface " + Integer.toHexString(i) + ": " + ((Utf8CpInfo)getCpEntry(((ClassCpInfo)info).getNameIndex())).getString());
/*      */       } 
/*      */     } 
/* 1672 */     pw.println("Fields count: " + Integer.toHexString(this.u2fieldsCount));
/* 1673 */     for (i = 0; i < this.u2fieldsCount; i++) {
/*      */       
/* 1675 */       ClassItemInfo info = this.fields[i];
/* 1676 */       if (info == null) {
/*      */         
/* 1678 */         pw.println("  Field " + Integer.toHexString(i) + ": (null)");
/*      */       }
/*      */       else {
/*      */         
/* 1682 */         pw.println("  Field " + Integer.toHexString(i) + ": " + ((Utf8CpInfo)getCpEntry(info.getNameIndex())).getString() + " " + ((Utf8CpInfo)getCpEntry(info.getDescriptorIndex())).getString());
/*      */       } 
/* 1684 */       pw.println("    Attrs count: " + Integer.toHexString(info.u2attributesCount));
/* 1685 */       for (int j = 0; j < info.u2attributesCount; j++)
/*      */       {
/* 1687 */         pw.println(info.attributes[j]);
/*      */       }
/*      */     } 
/* 1690 */     pw.println("Methods count: " + Integer.toHexString(this.u2methodsCount));
/* 1691 */     for (i = 0; i < this.u2methodsCount; i++) {
/*      */       
/* 1693 */       ClassItemInfo info = this.methods[i];
/* 1694 */       if (info == null) {
/*      */         
/* 1696 */         pw.println("  Method " + Integer.toHexString(i) + ": (null)");
/*      */       }
/*      */       else {
/*      */         
/* 1700 */         pw.println("  Method " + Integer.toHexString(i) + ": " + ((Utf8CpInfo)getCpEntry(info.getNameIndex())).getString() + " " + ((Utf8CpInfo)getCpEntry(info.getDescriptorIndex())).getString() + " " + Integer.toHexString(info.getAccessFlags()));
/*      */       } 
/* 1702 */       pw.println("    Attrs count: " + Integer.toHexString(info.u2attributesCount));
/* 1703 */       for (int j = 0; j < info.u2attributesCount; j++) {
/*      */         
/* 1705 */         if (info.attributes[j] instanceof CodeAttrInfo) {
/* 1706 */           pw.println(info.attributes[j]);
/* 1707 */           CodeAttrInfo cai = (CodeAttrInfo)info.attributes[j];
/* 1708 */           for (int k = 0; k < cai.u2attributesCount; k++) {
/* 1709 */             pw.println(cai.attributes[k]);
/*      */           }
/*      */         } else {
/* 1712 */           pw.println(info.attributes[j]);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1716 */     pw.println("Attrs count: " + Integer.toHexString(this.u2attributesCount));
/* 1717 */     for (i = 0; i < this.u2attributesCount; i++)
/*      */     {
/* 1719 */       pw.println(this.attributes[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AttrInfo[] getAttributes() {
/* 1729 */     return this.attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String findModuleName() {
/* 1739 */     for (int i = 0; i < this.attributes.length; i++) {
/* 1740 */       if (this.attributes[i] instanceof ModuleAttrInfo) {
/* 1741 */         int mIdx = ((ModuleAttrInfo)this.attributes[i]).getModuleNameIndex();
/* 1742 */         CpInfo mInfo = this.constantPool.getCpEntry(mIdx);
/* 1743 */         if (mInfo instanceof ModuleCpInfo) {
/* 1744 */           int nIdx = ((ModuleCpInfo)mInfo).getNameIndex();
/* 1745 */           CpInfo nInfo = this.constantPool.getCpEntry(nIdx);
/* 1746 */           if (nInfo instanceof Utf8CpInfo) {
/* 1747 */             return ((Utf8CpInfo)nInfo).getString();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1752 */     return "";
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ClassFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
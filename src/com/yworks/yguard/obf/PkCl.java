/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import com.yworks.yguard.Conversion;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PkCl
/*     */   extends TreeItem
/*     */ {
/*  28 */   protected Hashtable cls = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PkCl(TreeItem parent, String name) {
/*  44 */     super(parent, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cl getClass(String name) {
/*  53 */     return (Cl)this.cls.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getClassEnum() {
/*  60 */     return this.cls.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getAllClassEnum() {
/*  69 */     Vector allClasses = new Vector();
/*  70 */     addAllClasses(allClasses);
/*  71 */     return allClasses.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClasses(Vector<Cl> allClasses) {
/*  81 */     for (Enumeration<Cl> enumeration = this.cls.elements(); enumeration.hasMoreElements(); ) {
/*     */       
/*  83 */       Cl cl = enumeration.nextElement();
/*  84 */       allClasses.addElement(cl);
/*  85 */       cl.addAllClasses(allClasses);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClassCount() {
/*  94 */     return this.cls.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Cl addClass(Object[] paramArrayOfObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cl addClass(boolean isInnerClass, Object[] classInfo) {
/* 114 */     String name = (String)classInfo[0];
/* 115 */     String superName = (String)classInfo[1];
/* 116 */     String[] interfaceNames = (String[])classInfo[2];
/* 117 */     int modifiers = ((Integer)classInfo[3]).intValue();
/* 118 */     ObfuscationConfig obfuscationConfig = (ObfuscationConfig)classInfo[4];
/* 119 */     Cl cl = getClass(name);
/*     */ 
/*     */     
/* 122 */     PlaceholderCl plClassItem = null;
/* 123 */     if (cl instanceof PlaceholderCl) {
/*     */       
/* 125 */       plClassItem = (PlaceholderCl)cl;
/* 126 */       this.cls.remove(name);
/* 127 */       cl = null;
/*     */     } 
/*     */ 
/*     */     
/* 131 */     if (cl == null) {
/*     */       
/* 133 */       cl = new Cl(this, isInnerClass, name, superName, interfaceNames, modifiers, obfuscationConfig);
/* 134 */       this.cls.put(name, cl);
/*     */     } 
/*     */ 
/*     */     
/* 138 */     if (plClassItem != null)
/*     */     {
/* 140 */       for (Enumeration<Cl> enumeration = plClassItem.getClassEnum(); enumeration.hasMoreElements(); ) {
/*     */         
/* 142 */         Cl innerCl = enumeration.nextElement();
/* 143 */         innerCl.setParent(cl);
/* 144 */         cl.addClass(innerCl);
/*     */       } 
/*     */     }
/* 147 */     return cl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Cl addPlaceholderClass(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cl addPlaceholderClass(boolean isInnerClass, String name) {
/* 167 */     Cl cl = getClass(name);
/* 168 */     if (cl == null) {
/*     */       
/* 170 */       cl = new PlaceholderCl(this, isInnerClass, name);
/* 171 */       this.cls.put(name, cl);
/*     */     } 
/* 173 */     return cl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateNames() {
/* 181 */     generateNames(this.cls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateNames(Hashtable hash) {
/* 191 */     Vector<String> vec = new Vector();
/* 192 */     for (Enumeration<TreeItem> enumeration = hash.elements(); enumeration.hasMoreElements(); ) {
/*     */       
/* 194 */       TreeItem ti = enumeration.nextElement();
/* 195 */       if (ti.isFixed())
/*     */       {
/* 197 */         vec.addElement(ti.getOutName());
/*     */       }
/*     */     } 
/* 200 */     String[] noObfNames = new String[vec.size()];
/* 201 */     for (int i = 0; i < noObfNames.length; i++)
/*     */     {
/* 203 */       noObfNames[i] = vec.elementAt(i);
/*     */     }
/* 205 */     NameMakerFactory nmf = NameMakerFactory.getInstance(); Enumeration<TreeItem> enumeration1;
/* 206 */     label42: for (enumeration1 = hash.elements(); enumeration1.hasMoreElements(); ) {
/*     */       
/* 208 */       TreeItem ti = enumeration1.nextElement();
/* 209 */       if (!ti.isFixed()) {
/*     */         
/* 211 */         if (ti instanceof Cl && ((Cl)ti).isInnerClass()) {
/* 212 */           NameMaker innerClassNameMaker = nmf.getInnerClassNameMaker(noObfNames, getFullInName());
/* 213 */           ti.setOutName(innerClassNameMaker.nextName(null)); continue;
/* 214 */         }  if (ti instanceof Pk) {
/* 215 */           NameMaker packageNameMaker = nmf.getPackageNameMaker(noObfNames, getFullInName());
/* 216 */           ti.setOutName(packageNameMaker.nextName(null));
/*     */           continue;
/*     */         } 
/* 219 */         if ("package-info".equals(ti.getInName())) {
/* 220 */           ti.setOutName("package-info"); continue;
/* 221 */         }  if ("module-info".equals(ti.getInName())) {
/* 222 */           ti.setOutName("module-info"); continue;
/*     */         } 
/* 224 */         NameMaker classNameMaker = nmf.getClassNameMaker(noObfNames, getFullInName());
/* 225 */         boolean newNameFound = true;
/* 226 */         Cl.ClassResolver resolver = Cl.getClassResolver();
/*     */         while (true) {
/* 228 */           ti.setOutName(classNameMaker.nextName(null));
/* 229 */           String newName = ti.getFullOutName();
/*     */           try {
/* 231 */             resolver.resolve(Conversion.toJavaClass(newName));
/* 232 */             newNameFound = false;
/* 233 */           } catch (ClassNotFoundException cnfe) {
/* 234 */             newNameFound = true;
/*     */           } 
/* 236 */           if (newNameFound)
/*     */             continue label42; 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/PkCl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
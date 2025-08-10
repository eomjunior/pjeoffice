/*     */ package com.yworks.yguard.ant;
/*     */ 
/*     */ import com.yworks.common.ant.Exclude;
/*     */ import com.yworks.common.ant.YGuardBaseTask;
/*     */ import com.yworks.yguard.ObfuscatorTask;
/*     */ import com.yworks.yguard.obf.YGuardRule;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.ZipFileSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExposeSection
/*     */   extends Exclude
/*     */ {
/*  23 */   protected List classes = new ArrayList(5);
/*  24 */   protected List packages = new ArrayList(5);
/*  25 */   protected List patterns = new ArrayList(5);
/*  26 */   protected List methods = new ArrayList(5);
/*  27 */   protected List fields = new ArrayList(5);
/*  28 */   protected List attributes = new ArrayList(5);
/*  29 */   protected List lineNumberTables = new ArrayList(5);
/*  30 */   protected List sourceFiles = new ArrayList(5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExposeSection(ObfuscatorTask task) {
/*  38 */     super((YGuardBaseTask)task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPatternSet(PatternSet ps) {
/*  47 */     this.patterns.add(ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodSection createMethod() {
/*  56 */     MethodSection ms = newMethodSection();
/*  57 */     this.methods.add(ms);
/*  58 */     return ms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MethodSection newMethodSection() {
/*  68 */     return new MethodSection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldSection createField() {
/*  77 */     FieldSection fs = newFieldSection();
/*  78 */     this.fields.add(fs);
/*  79 */     return fs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FieldSection newFieldSection() {
/*  89 */     return new FieldSection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassSection createClass() {
/*  98 */     ClassSection cs = newClassSection(this.task);
/*  99 */     this.classes.add(cs);
/* 100 */     return cs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassSection newClassSection(YGuardBaseTask bt) {
/* 110 */     return new ClassSection(bt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageSection createPackage() {
/* 119 */     PackageSection ps = newPackageSection();
/* 120 */     this.packages.add(ps);
/* 121 */     return ps;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PackageSection newPackageSection() {
/* 131 */     return new PackageSection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributesSection createAttribute() {
/* 140 */     AttributesSection as = newAttributesSection(this.task);
/* 141 */     this.attributes.add(as);
/* 142 */     return as;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AttributesSection newAttributesSection(YGuardBaseTask obfuscatorTask) {
/* 152 */     return new AttributesSection(obfuscatorTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineNumberTableSection createLineNumberTable() {
/* 161 */     LineNumberTableSection lns = newLineNumberTableSection(this.task);
/* 162 */     this.lineNumberTables.add(lns);
/* 163 */     return lns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LineNumberTableSection newLineNumberTableSection(YGuardBaseTask obfuscatorTask) {
/* 173 */     return new LineNumberTableSection(obfuscatorTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceFileSection createSourceFile() {
/* 182 */     SourceFileSection sfs = newSourceFileSection(this.task);
/* 183 */     this.sourceFiles.add(sfs);
/* 184 */     return sfs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SourceFileSection newSourceFileSection(YGuardBaseTask obfuscatorTask) {
/* 194 */     return new SourceFileSection(obfuscatorTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection createEntries(Collection srcJars) throws IOException {
/* 205 */     Collection<YGuardRule> entries = new ArrayList(20);
/* 206 */     if (this.source) {
/* 207 */       entries.add(new YGuardRule(0, "SourceFile"));
/*     */     }
/* 209 */     if (this.vtable) {
/* 210 */       entries.add(new YGuardRule(0, "LocalVariableTable"));
/*     */     }
/* 212 */     if (this.ltable) {
/* 213 */       entries.add(new YGuardRule(0, "LineNumberTable"));
/*     */     }
/* 215 */     if (this.lttable) {
/* 216 */       entries.add(new YGuardRule(0, "LocalVariableTypeTable"));
/*     */     }
/* 218 */     if (this.rvAnn) {
/* 219 */       entries.add(new YGuardRule(0, "RuntimeVisibleAnnotations"));
/*     */     }
/* 221 */     if (this.rvTypeAnn) {
/* 222 */       entries.add(new YGuardRule(0, "RuntimeVisibleTypeAnnotations"));
/*     */     }
/* 224 */     if (this.riAnn) {
/* 225 */       entries.add(new YGuardRule(0, "RuntimeInvisibleAnnotations"));
/*     */     }
/* 227 */     if (this.riTypeAnn) {
/* 228 */       entries.add(new YGuardRule(0, "RuntimeInvisibleTypeAnnotations"));
/*     */     }
/* 230 */     if (this.rvPann) {
/* 231 */       entries.add(new YGuardRule(0, "RuntimeVisibleParameterAnnotations"));
/*     */     }
/* 233 */     if (this.riPann) {
/* 234 */       entries.add(new YGuardRule(0, "RuntimeInvisibleParameterAnnotations"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     for (Iterator<File> iterator2 = srcJars.iterator(); iterator2.hasNext(); ) {
/* 241 */       File file = iterator2.next();
/* 242 */       ZipFileSet zipFile = new ZipFileSet();
/* 243 */       zipFile.setProject(this.task.getProject());
/* 244 */       zipFile.setSrc(file);
/* 245 */       for (Iterator<ClassSection> iterator8 = this.classes.iterator(); iterator8.hasNext(); ) {
/* 246 */         ClassSection cs = iterator8.next();
/* 247 */         if (cs.getName() == null && cs.getExtends() == null && cs.getImplements() == null) {
/* 248 */           cs.addEntries(entries, zipFile);
/*     */         }
/*     */       } 
/* 251 */       for (Iterator<MethodSection> iterator7 = this.methods.iterator(); iterator7.hasNext(); ) {
/* 252 */         MethodSection ms = iterator7.next();
/* 253 */         if (ms.getClassName() == null) {
/* 254 */           ms.addEntries(entries, zipFile);
/*     */         }
/*     */       } 
/* 257 */       for (Iterator<FieldSection> iterator6 = this.fields.iterator(); iterator6.hasNext(); ) {
/* 258 */         FieldSection fs = iterator6.next();
/* 259 */         if (fs.getClassName() == null) {
/* 260 */           fs.addEntries(entries, zipFile);
/*     */         }
/*     */       } 
/* 263 */       for (Iterator<AttributesSection> iterator5 = this.attributes.iterator(); iterator5.hasNext(); ) {
/* 264 */         AttributesSection as = iterator5.next();
/* 265 */         if (as.getAttributes() != null) {
/* 266 */           as.addEntries(entries, zipFile);
/*     */         }
/*     */       } 
/*     */       
/* 270 */       for (Iterator<LineNumberTableSection> iterator4 = this.lineNumberTables.iterator(); iterator4.hasNext(); ) {
/* 271 */         LineNumberTableSection lt = iterator4.next();
/* 272 */         lt.addEntries(entries, zipFile);
/*     */       } 
/*     */       
/* 275 */       for (Iterator<SourceFileSection> iterator3 = this.sourceFiles.iterator(); iterator3.hasNext(); ) {
/* 276 */         SourceFileSection sfs = iterator3.next();
/* 277 */         sfs.addEntries(entries, zipFile);
/*     */       } 
/* 279 */       for (Iterator<PackageSection> it2 = this.packages.iterator(); it2.hasNext(); ) {
/* 280 */         PackageSection ps = it2.next();
/* 281 */         ps.addEntries(entries, zipFile);
/*     */       } 
/*     */     } 
/* 284 */     for (Iterator<ClassSection> iterator1 = this.classes.iterator(); iterator1.hasNext(); ) {
/* 285 */       ClassSection cs = iterator1.next();
/* 286 */       if (cs.getName() != null) {
/* 287 */         cs.addEntries(entries, cs.getName());
/*     */       }
/*     */     } 
/* 290 */     for (Iterator<MethodSection> iterator = this.methods.iterator(); iterator.hasNext(); ) {
/* 291 */       MethodSection ms = iterator.next();
/* 292 */       if (ms.getClassName() != null) {
/* 293 */         ms.addEntries(entries, ms.getClassName());
/*     */       }
/*     */     } 
/* 296 */     for (Iterator<FieldSection> it = this.fields.iterator(); it.hasNext(); ) {
/* 297 */       FieldSection fs = it.next();
/* 298 */       if (fs.getClassName() != null) {
/* 299 */         fs.addEntries(entries, fs.getClassName());
/*     */       }
/*     */     } 
/*     */     
/* 303 */     if (this.task instanceof ObfuscatorTask) {
/* 304 */       ((ObfuscatorTask)this.task).addInheritanceEntries(entries);
/*     */     }
/*     */ 
/*     */     
/* 308 */     return entries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getClasses() {
/* 317 */     return this.classes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getPackages() {
/* 326 */     return this.packages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getPatterns() {
/* 335 */     return this.patterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getMethods() {
/* 344 */     return this.methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getFields() {
/* 353 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getAttributes() {
/* 362 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getLineNumberTables() {
/* 371 */     return this.lineNumberTables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getSourceFiles() {
/* 380 */     return this.sourceFiles;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/ExposeSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
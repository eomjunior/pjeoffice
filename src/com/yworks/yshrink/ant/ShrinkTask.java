/*     */ package com.yworks.yshrink.ant;
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.common.ant.AttributesSection;
/*     */ import com.yworks.common.ant.EntryPointJar;
/*     */ import com.yworks.common.ant.EntryPointsSection;
/*     */ import com.yworks.common.ant.Exclude;
/*     */ import com.yworks.common.ant.YGuardBaseTask;
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.logging.XmlLogger;
/*     */ import com.yworks.util.Version;
/*     */ import com.yworks.yshrink.YShrink;
/*     */ import com.yworks.yshrink.ant.filters.AttributeFilter;
/*     */ import com.yworks.yshrink.ant.filters.ClassFilter;
/*     */ import com.yworks.yshrink.ant.filters.EntryPointFilter;
/*     */ import com.yworks.yshrink.ant.filters.EntryPointFilters;
/*     */ import com.yworks.yshrink.ant.filters.EntryPointJarFilter;
/*     */ import com.yworks.yshrink.ant.filters.FieldFilter;
/*     */ import com.yworks.yshrink.ant.filters.MethodFilter;
/*     */ import com.yworks.yshrink.util.MultiReleaseException;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ 
/*     */ public class ShrinkTask extends YGuardBaseTask {
/*  35 */   private File logFile = new File("yshrinklog.xml");
/*     */   
/*     */   private boolean createStubs = false;
/*     */   
/*  39 */   private String digests = "SHA-1,MD5";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntryPointsSection entryPointsSection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShrinkTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShrinkTask(boolean mode) {
/*  56 */     super(mode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  61 */     getProject().log((Task)this, "yGuard Shrinker v" + Version.getVersion() + " - http://www.yworks.com/products/yguard", 2);
/*     */     
/*  63 */     super.execute();
/*     */     
/*  65 */     XmlLogger xmlLogger = new XmlLogger(getLogWriter());
/*  66 */     Logger antLogger = new AntLogger(getProject(), (Task)this);
/*     */     
/*  68 */     EntryPointFilters epfs = new EntryPointFilters();
/*     */     
/*  70 */     List<AttributesSection> attributesSections = (this.entryPointsSection != null) ? this.entryPointsSection.getAttributesSections() : this.attributesSections;
/*     */     
/*  72 */     if (this.entryPointsSection != null) {
/*     */       
/*  74 */       epfs.setExclude((Exclude)this.entryPointsSection);
/*     */       
/*  76 */       List<MethodSection> methodSections = this.entryPointsSection.getMethodSections();
/*  77 */       List<FieldSection> fieldSections = this.entryPointsSection.getFieldSections();
/*  78 */       List<ClassSection> classSections = this.entryPointsSection.getClassSections();
/*     */       
/*  80 */       if (methodSections.size() > 0) {
/*  81 */         MethodFilter mf = new MethodFilter(getProject());
/*  82 */         for (MethodSection ms : methodSections) {
/*  83 */           mf.addMethodSection(ms);
/*     */         }
/*  85 */         epfs.addEntryPointFilter((EntryPointFilter)mf);
/*     */       } 
/*     */       
/*  88 */       if (fieldSections.size() > 0) {
/*  89 */         FieldFilter ff = new FieldFilter(getProject());
/*  90 */         for (FieldSection fs : fieldSections) {
/*  91 */           ff.addFieldSection(fs);
/*     */         }
/*  93 */         epfs.addEntryPointFilter((EntryPointFilter)ff);
/*     */       } 
/*     */       
/*  96 */       if (classSections.size() > 0) {
/*  97 */         ClassFilter cf = new ClassFilter(getProject());
/*  98 */         for (ClassSection cs : classSections) {
/*  99 */           cf.addClassSection(cs);
/*     */         }
/* 101 */         epfs.addEntryPointFilter((EntryPointFilter)cf);
/*     */       } 
/*     */       
/* 104 */       AttributeFilter attributeFilter = new AttributeFilter(getProject());
/* 105 */       if (this.entryPointsSection.isRiAnn()) {
/* 106 */         addAttributesSection(attributeFilter, "RuntimeInvisibleAnnotations");
/*     */       }
/* 108 */       if (this.entryPointsSection.isRiPann()) {
/* 109 */         addAttributesSection(attributeFilter, "RuntimeInvisibleParameterAnnotations");
/*     */       }
/* 111 */       if (this.entryPointsSection.isRvAnn()) {
/* 112 */         addAttributesSection(attributeFilter, "RuntimeVisibleAnnotations");
/*     */       }
/* 114 */       if (this.entryPointsSection.isRvPann()) {
/* 115 */         addAttributesSection(attributeFilter, "RuntimeVisibleParameterAnnotations");
/*     */       }
/* 117 */       if (this.entryPointsSection.isRvTypeAnn()) {
/* 118 */         addAttributesSection(attributeFilter, "RuntimeVisibleTypeAnnotations");
/*     */       }
/* 120 */       if (this.entryPointsSection.isRiTypeAnn()) {
/* 121 */         addAttributesSection(attributeFilter, "RuntimeInvisibleTypeAnnotations");
/*     */       }
/* 123 */       if (this.entryPointsSection.isSource()) {
/* 124 */         addAttributesSection(attributeFilter, "SourceFile");
/*     */       }
/* 126 */       if (this.entryPointsSection.isLtable()) {
/* 127 */         addAttributesSection(attributeFilter, "LineNumberTable");
/*     */       }
/* 129 */       if (this.entryPointsSection.isLttable()) {
/* 130 */         addAttributesSection(attributeFilter, "LocalVariableTypeTable");
/*     */       }
/* 132 */       if (this.entryPointsSection.isVtable()) {
/* 133 */         addAttributesSection(attributeFilter, "LocalVariableTable");
/*     */       }
/* 135 */       if (this.entryPointsSection.isDebugExtension()) {
/* 136 */         addAttributesSection(attributeFilter, "SourceDebug");
/*     */       }
/* 138 */       epfs.addEntryPointFilter((EntryPointFilter)attributeFilter);
/*     */ 
/*     */       
/* 141 */       ClassFilter classFilter = new ClassFilter(getProject());
/* 142 */       ClassSection classSection = new ClassSection();
/* 143 */       PatternSet patternSet = new PatternSet();
/* 144 */       patternSet.setIncludes("**/package-info");
/* 145 */       classSection.addPatternSet(patternSet, TypePatternSet.Type.NAME);
/* 146 */       classFilter.addClassSection(classSection);
/*     */       
/* 148 */       epfs.addEntryPointFilter((EntryPointFilter)classFilter);
/*     */     } 
/*     */     
/* 151 */     if (null != attributesSections && attributesSections.size() > 0) {
/* 152 */       AttributeFilter af = new AttributeFilter(getProject());
/* 153 */       for (AttributesSection as : attributesSections) {
/* 154 */         af.addAttributesSection(as);
/*     */       }
/* 156 */       epfs.addEntryPointFilter((EntryPointFilter)af);
/*     */     } 
/*     */     
/* 159 */     if (this.pairs == null) {
/* 160 */       throw new BuildException("no files to shrink");
/*     */     }
/* 162 */     boolean containsInOutPair = false;
/* 163 */     boolean containsEntryPointJar = false;
/* 164 */     for (ShrinkBag shrinkBag : this.pairs) {
/* 165 */       if (shrinkBag.isEntryPointJar()) {
/*     */         
/* 167 */         EntryPointJarFilter epjf = new EntryPointJarFilter((EntryPointJar)shrinkBag);
/* 168 */         epfs.addEntryPointFilter((EntryPointFilter)epjf);
/*     */         
/* 170 */         containsEntryPointJar = true; continue;
/*     */       } 
/* 172 */       containsInOutPair = true;
/*     */     } 
/*     */ 
/*     */     
/* 176 */     if (!containsInOutPair) {
/* 177 */       throw new BuildException("no files to shrink");
/*     */     }
/*     */     
/* 180 */     if (!containsEntryPointJar && null == this.entryPointsSection) {
/* 181 */       Logger.log("no entrypoints given - using class access public and protected on all inoutpairs.");
/* 182 */       this.entryPointsSection = new EntryPointsSection(this);
/* 183 */       ClassFilter cf = new ClassFilter(getProject());
/* 184 */       ClassSection cs = new ClassSection();
/* 185 */       cs.setAccess("protected");
/* 186 */       cf.addClassSection(cs);
/* 187 */       epfs.addEntryPointFilter((EntryPointFilter)cf);
/* 188 */       epfs.setExclude((Exclude)this.entryPointsSection);
/*     */     } 
/*     */ 
/*     */     
/* 192 */     ResourceCpResolver resolver = null;
/*     */     
/* 194 */     if (this.resourceClassPath != null) {
/* 195 */       resolver = new ResourceCpResolver(this.resourceClassPath, (Task)this);
/*     */     }
/*     */     
/* 198 */     if (this.properties.containsKey("digests")) {
/* 199 */       setDigests((String)this.properties.get("digests"));
/*     */     }
/*     */     
/* 202 */     YShrink yShrink = new YShrink(this.createStubs, this.digests);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 208 */       yShrink.doShrinkPairs(this.pairs, (EntryPointFilter)epfs, resolver);
/* 209 */     } catch (MultiReleaseException mre) {
/* 210 */       throw mre;
/* 211 */     } catch (RuntimeException rte) {
/* 212 */       if (rte.getMessage() != null) {
/* 213 */         Logger.err(rte.getMessage(), rte);
/*     */       }
/* 215 */       throw new BuildException("yShrink encountered an unknown problem!", rte);
/* 216 */     } catch (Throwable e) {
/* 217 */       if (e.getMessage() != null) {
/* 218 */         Logger.err(e.getMessage(), e);
/*     */       } else {
/* 220 */         Logger.err(e.getClass().getName(), e);
/*     */       } 
/* 222 */       throw new BuildException("yShrink encountered an unknown severe problem!", e);
/*     */     } finally {
/*     */       
/*     */       try {
/* 226 */         resolver.close();
/* 227 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 230 */       xmlLogger.close();
/* 231 */       antLogger.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private PrintWriter getLogWriter() {
/* 236 */     PrintWriter log = null;
/* 237 */     if (this.logFile != null) {
/*     */       try {
/* 239 */         if (this.logFile.getName().endsWith(".gz")) {
/* 240 */           log = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(this.logFile)))));
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 250 */           log = new PrintWriter(new BufferedWriter(new FileWriter(this.logFile)));
/*     */         } 
/* 252 */       } catch (IOException ioe) {
/* 253 */         getProject().log((Task)this, "Could not create logfile: " + ioe, 0);
/* 254 */         log = new PrintWriter(System.out);
/*     */       } 
/*     */     } else {
/* 257 */       log = new PrintWriter(System.out);
/*     */     } 
/* 259 */     return log;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCreateStubs() {
/* 268 */     return this.createStubs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateStubs(boolean createStubs) {
/* 277 */     this.createStubs = createStubs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDigests() {
/* 286 */     return this.digests;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDigests(String digests) {
/* 295 */     this.digests = digests;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogFile(File file) {
/* 304 */     this.logFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntryPointsSection createEntryPoints() {
/* 313 */     if (this.entryPointsSection != null) {
/* 314 */       throw new IllegalArgumentException("Only one entrypoints or expose element allowed!");
/*     */     }
/* 316 */     this.entryPointsSection = newEntryPointsSection(this);
/* 317 */     return this.entryPointsSection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntryPointsSection newEntryPointsSection(YGuardBaseTask bt) {
/* 327 */     return new EntryPointsSection(bt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntryPointsExternally(EntryPointsSection eps) {
/* 336 */     this.entryPointsSection = eps;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntryPointsSection createExpose() {
/* 345 */     return createEntryPoints();
/*     */   }
/*     */   
/*     */   public Exclude createKeep() {
/* 349 */     return (Exclude)createExpose();
/*     */   }
/*     */   
/*     */   public void addAttributesSections(List<AttributesSection> attributesSections) {
/* 353 */     if (null != this.entryPointsSection) {
/* 354 */       for (AttributesSection attributesSection : attributesSections) {
/* 355 */         this.entryPointsSection.addConfiguredAttribute(attributesSection);
/*     */       }
/*     */     }
/* 358 */     else if (null != this.attributesSections) {
/* 359 */       this.attributesSections.addAll(attributesSections);
/*     */     } else {
/* 361 */       this.attributesSections = attributesSections;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredEntrypointjar(EntryPointJar entrypointjar) {
/* 372 */     if (this.pairs == null) this.pairs = new ArrayList(); 
/* 373 */     this.pairs.add(entrypointjar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addAttributesSection(AttributeFilter attributeFilter, String name) {
/* 380 */     AttributesSection as = new AttributesSection();
/* 381 */     as.setName(name);
/* 382 */     attributeFilter.addAttributesSection(as);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/ShrinkTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
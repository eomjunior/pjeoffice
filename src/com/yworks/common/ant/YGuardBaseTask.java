/*     */ package com.yworks.common.ant;
/*     */ 
/*     */ import com.yworks.common.ResourcePolicy;
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.yguard.ant.Property;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public abstract class YGuardBaseTask
/*     */   extends Task
/*     */ {
/*     */   protected static final boolean MODE_STANDALONE = false;
/*     */   protected static final boolean MODE_NESTED = true;
/*     */   protected final boolean mode;
/*     */   protected List<ShrinkBag> pairs;
/*     */   protected Path resourceClassPath;
/*     */   protected List<AttributesSection> attributesSections;
/*  56 */   protected Map properties = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YGuardBaseTask() {
/*  62 */     this.mode = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YGuardBaseTask(boolean mode) {
/*  71 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributesSection createAttribute() {
/*  80 */     if (this.attributesSections == null) this.attributesSections = new ArrayList<>(); 
/*  81 */     AttributesSection as = newAttributesSection();
/*  82 */     this.attributesSections.add(as);
/*  83 */     return as;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AttributesSection newAttributesSection() {
/*  93 */     return new AttributesSection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShrinkBag createInOutPair() {
/* 102 */     if (this.pairs == null) this.pairs = new ArrayList<>(); 
/* 103 */     ShrinkBag pair = newInOutPair();
/* 104 */     this.pairs.add(pair);
/* 105 */     return pair;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InOutPair newInOutPair() {
/* 115 */     return new InOutPair();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredInOutPairs(InOutPairSection section) {
/* 124 */     if (this.pairs == null) this.pairs = new ArrayList<>(); 
/* 125 */     this.pairs.addAll(section.createShrinkBags(getProject()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredInOutPair(ShrinkBag pair) {
/* 134 */     if (this.pairs == null) this.pairs = new ArrayList<>(); 
/* 135 */     this.pairs.add(pair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createExternalClasses() {
/* 144 */     if (this.resourceClassPath != null) {
/* 145 */       throw new IllegalArgumentException("Only one externalclasses element allowed!");
/*     */     }
/* 147 */     this.resourceClassPath = new Path(getProject());
/* 148 */     return this.resourceClassPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceClassPath(Path path) {
/* 157 */     this.resourceClassPath = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Exclude createKeep();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void addAttributesSections(List<AttributesSection> paramList);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredProperty(Property p) {
/* 180 */     this.properties.put(p.getName(), p.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class InOutPairSection
/*     */   {
/*     */     private FileSet set;
/*     */     
/*     */     private Mapper mapper;
/* 189 */     private ResourcePolicy resources = ResourcePolicy.COPY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setResources(String resourcesStr) {
/*     */       try {
/* 199 */         this.resources = ResourcePolicy.valueOf(resourcesStr.trim().toUpperCase());
/* 200 */       } catch (IllegalArgumentException e) {
/* 201 */         throw new BuildException("Invalid resource policy: " + resourcesStr);
/*     */       } 
/*     */     }
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
/*     */     public void addConfiguredFileSet(FileSet set) {
/* 217 */       this.set = set;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(Mapper mapper) {
/* 226 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<ShrinkBag> createShrinkBags(Project project) {
/* 236 */       if (this.mapper == null) {
/* 237 */         Mapper.MapperType type = new Mapper.MapperType();
/* 238 */         type.setValue("glob");
/* 239 */         this.mapper = new Mapper(project);
/* 240 */         this.mapper.setType(type);
/* 241 */         this.mapper.setFrom("*.jar");
/* 242 */         this.mapper.setTo("*_obf.jar");
/*     */       } 
/* 244 */       ArrayList<ShrinkBag> result = new ArrayList<>();
/* 245 */       DirectoryScanner directoryScanner = this.set.getDirectoryScanner(project);
/* 246 */       String[] files = directoryScanner.getIncludedFiles();
/* 247 */       for (int i = 0; i < files.length; i++) {
/* 248 */         String inFile = files[i];
/* 249 */         String[] outFile = this.mapper.getImplementation().mapFileName(inFile);
/* 250 */         if (outFile == null || outFile.length < 1 || outFile[0].equals(inFile)) {
/* 251 */           throw new BuildException("Cannot obfuscate " + inFile + " using that mapping");
/*     */         }
/* 253 */         InOutPair pair = new InOutPair();
/* 254 */         pair.resources = this.resources;
/* 255 */         pair.setIn(FileUtils.newFileUtils().resolveFile(directoryScanner.getBasedir(), inFile));
/* 256 */         pair.setOut(FileUtils.newFileUtils().resolveFile(directoryScanner.getBasedir(), outFile[0]));
/* 257 */         result.add(pair);
/*     */       } 
/* 259 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/YGuardBaseTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
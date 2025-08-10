/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.util.CompositeMapper;
/*     */ import org.apache.tools.ant.util.ContainerMapper;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
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
/*     */ public class Mapper
/*     */   extends DataType
/*     */ {
/*  36 */   protected MapperType type = null;
/*  37 */   protected String classname = null;
/*  38 */   protected Path classpath = null;
/*  39 */   protected String from = null;
/*  40 */   protected String to = null;
/*     */ 
/*     */ 
/*     */   
/*  44 */   private ContainerMapper container = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper(Project p) {
/*  51 */     setProject(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(MapperType type) {
/*  59 */     if (isReference()) {
/*  60 */       throw tooManyAttributes();
/*     */     }
/*  62 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(FileNameMapper fileNameMapper) {
/*  71 */     add(fileNameMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/*  79 */     if (isReference()) {
/*  80 */       throw noChildrenAllowed();
/*     */     }
/*  82 */     if (this.container == null) {
/*  83 */       if (this.type == null && this.classname == null) {
/*  84 */         this.container = (ContainerMapper)new CompositeMapper();
/*     */       } else {
/*  86 */         FileNameMapper m = getImplementation();
/*  87 */         if (m instanceof ContainerMapper) {
/*  88 */           this.container = (ContainerMapper)m;
/*     */         } else {
/*  90 */           throw new BuildException(m + " mapper implementation does not support nested mappers!");
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*  95 */     this.container.add(fileNameMapper);
/*  96 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredMapper(Mapper mapper) {
/* 104 */     add(mapper.getImplementation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/* 112 */     if (isReference()) {
/* 113 */       throw tooManyAttributes();
/*     */     }
/* 115 */     this.classname = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 123 */     if (isReference()) {
/* 124 */       throw tooManyAttributes();
/*     */     }
/* 126 */     if (this.classpath == null) {
/* 127 */       this.classpath = classpath;
/*     */     } else {
/* 129 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 138 */     if (isReference()) {
/* 139 */       throw noChildrenAllowed();
/*     */     }
/* 141 */     if (this.classpath == null) {
/* 142 */       this.classpath = new Path(getProject());
/*     */     }
/* 144 */     setChecked(false);
/* 145 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference ref) {
/* 154 */     if (isReference()) {
/* 155 */       throw tooManyAttributes();
/*     */     }
/* 157 */     createClasspath().setRefid(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(String from) {
/* 165 */     if (isReference()) {
/* 166 */       throw tooManyAttributes();
/*     */     }
/* 168 */     this.from = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTo(String to) {
/* 176 */     if (isReference()) {
/* 177 */       throw tooManyAttributes();
/*     */     }
/* 179 */     this.to = to;
/*     */   }
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
/*     */   public void setRefid(Reference r) throws BuildException {
/* 192 */     if (this.type != null || this.from != null || this.to != null) {
/* 193 */       throw tooManyAttributes();
/*     */     }
/* 195 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileNameMapper getImplementation() throws BuildException {
/* 204 */     if (isReference()) {
/* 205 */       dieOnCircularReference();
/* 206 */       Reference r = getRefid();
/* 207 */       Object o = r.getReferencedObject(getProject());
/* 208 */       if (o instanceof FileNameMapper) {
/* 209 */         return (FileNameMapper)o;
/*     */       }
/* 211 */       if (o instanceof Mapper) {
/* 212 */         return ((Mapper)o).getImplementation();
/*     */       }
/* 214 */       String od = (o == null) ? "null" : o.getClass().getName();
/* 215 */       throw new BuildException(od + " at reference '" + r
/* 216 */           .getRefId() + "' is not a valid mapper reference.");
/*     */     } 
/*     */     
/* 219 */     if (this.type == null && this.classname == null && this.container == null) {
/* 220 */       throw new BuildException("nested mapper or one of the attributes type or classname is required");
/*     */     }
/*     */ 
/*     */     
/* 224 */     if (this.container != null) {
/* 225 */       return (FileNameMapper)this.container;
/*     */     }
/*     */     
/* 228 */     if (this.type != null && this.classname != null) {
/* 229 */       throw new BuildException("must not specify both type and classname attribute");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 234 */       FileNameMapper m = getImplementationClass().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 235 */       Project p = getProject();
/* 236 */       if (p != null) {
/* 237 */         p.setProjectReference(m);
/*     */       }
/* 239 */       m.setFrom(this.from);
/* 240 */       m.setTo(this.to);
/*     */       
/* 242 */       return m;
/* 243 */     } catch (BuildException be) {
/* 244 */       throw be;
/* 245 */     } catch (Throwable t) {
/* 246 */       throw new BuildException(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<? extends FileNameMapper> getImplementationClass() throws ClassNotFoundException {
/* 257 */     String cName = this.classname;
/* 258 */     if (this.type != null) {
/* 259 */       cName = this.type.getImplementation();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     ClassLoader loader = (this.classpath == null) ? getClass().getClassLoader() : (ClassLoader)getProject().createClassLoader(this.classpath);
/*     */     
/* 267 */     return Class.forName(cName, true, loader).asSubclass(FileNameMapper.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected Mapper getRef() {
/* 279 */     return getCheckedRef(Mapper.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class MapperType
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     private Properties implementations;
/*     */ 
/*     */     
/*     */     public MapperType() {
/* 290 */       this.implementations = new Properties();
/* 291 */       this.implementations.put("identity", "org.apache.tools.ant.util.IdentityMapper");
/*     */       
/* 293 */       this.implementations.put("flatten", "org.apache.tools.ant.util.FlatFileNameMapper");
/*     */       
/* 295 */       this.implementations.put("glob", "org.apache.tools.ant.util.GlobPatternMapper");
/*     */       
/* 297 */       this.implementations.put("merge", "org.apache.tools.ant.util.MergingMapper");
/*     */       
/* 299 */       this.implementations.put("regexp", "org.apache.tools.ant.util.RegexpPatternMapper");
/*     */       
/* 301 */       this.implementations.put("package", "org.apache.tools.ant.util.PackageNameMapper");
/*     */       
/* 303 */       this.implementations.put("unpackage", "org.apache.tools.ant.util.UnPackageNameMapper");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 312 */       return new String[] { "identity", "flatten", "glob", "merge", "regexp", "package", "unpackage" };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getImplementation() {
/* 320 */       return this.implementations.getProperty(getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Mapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
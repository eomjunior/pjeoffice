/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Stack;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
/*     */ import org.apache.tools.ant.util.MergingMapper;
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
/*     */ public class MappedResourceCollection
/*     */   extends DataType
/*     */   implements ResourceCollection, Cloneable
/*     */ {
/*  47 */   private ResourceCollection nested = null;
/*  48 */   private Mapper mapper = null;
/*     */   private boolean enableMultipleMappings = false;
/*     */   private boolean cache = false;
/*  51 */   private Collection<Resource> cachedColl = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection c) throws BuildException {
/*  59 */     if (isReference()) {
/*  60 */       throw noChildrenAllowed();
/*     */     }
/*  62 */     if (this.nested != null) {
/*  63 */       throw new BuildException("Only one resource collection can be nested into mappedresources", 
/*     */           
/*  65 */           getLocation());
/*     */     }
/*  67 */     setChecked(false);
/*  68 */     this.cachedColl = null;
/*  69 */     this.nested = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/*  78 */     if (isReference()) {
/*  79 */       throw noChildrenAllowed();
/*     */     }
/*  81 */     if (this.mapper != null) {
/*  82 */       throw new BuildException("Cannot define more than one mapper", 
/*  83 */           getLocation());
/*     */     }
/*  85 */     setChecked(false);
/*  86 */     this.mapper = new Mapper(getProject());
/*  87 */     this.cachedColl = null;
/*  88 */     return this.mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/*  97 */     createMapper().add(fileNameMapper);
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
/*     */ 
/*     */   
/*     */   public void setEnableMultipleMappings(boolean enableMultipleMappings) {
/* 112 */     this.enableMultipleMappings = enableMultipleMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCache(boolean cache) {
/* 121 */     this.cache = cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 129 */     if (isReference()) {
/* 130 */       return getRef().isFilesystemOnly();
/*     */     }
/* 132 */     checkInitialized();
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 141 */     if (isReference()) {
/* 142 */       return getRef().size();
/*     */     }
/* 144 */     checkInitialized();
/* 145 */     return cacheCollection().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/* 153 */     if (isReference()) {
/* 154 */       return getRef().iterator();
/*     */     }
/* 156 */     checkInitialized();
/* 157 */     return cacheCollection().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 166 */     if (this.nested != null || this.mapper != null) {
/* 167 */       throw tooManyAttributes();
/*     */     }
/* 169 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 180 */       MappedResourceCollection c = (MappedResourceCollection)super.clone();
/* 181 */       c.nested = this.nested;
/* 182 */       c.mapper = this.mapper;
/* 183 */       c.cachedColl = null;
/* 184 */       return c;
/* 185 */     } catch (CloneNotSupportedException e) {
/* 186 */       throw new BuildException(e);
/*     */     } 
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
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 200 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 203 */     if (isReference()) {
/* 204 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 206 */       checkInitialized();
/* 207 */       if (this.mapper != null) {
/* 208 */         pushAndInvokeCircularReferenceCheck((DataType)this.mapper, stk, p);
/*     */       }
/* 210 */       if (this.nested instanceof DataType) {
/* 211 */         pushAndInvokeCircularReferenceCheck((DataType)this.nested, stk, p);
/*     */       }
/* 213 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkInitialized() {
/* 218 */     if (this.nested == null) {
/* 219 */       throw new BuildException("A nested resource collection element is required", 
/*     */           
/* 221 */           getLocation());
/*     */     }
/* 223 */     dieOnCircularReference();
/*     */   }
/*     */   
/*     */   private synchronized Collection<Resource> cacheCollection() {
/* 227 */     if (this.cachedColl == null || !this.cache) {
/* 228 */       this.cachedColl = getCollection();
/*     */     }
/* 230 */     return this.cachedColl;
/*     */   }
/*     */   
/*     */   private Collection<Resource> getCollection() {
/*     */     Stream<MappedResource> stream;
/* 235 */     FileNameMapper m = (this.mapper == null) ? (FileNameMapper)new IdentityMapper() : this.mapper.getImplementation();
/*     */ 
/*     */     
/* 238 */     if (this.enableMultipleMappings) {
/*     */       
/* 240 */       stream = this.nested.stream().flatMap(r -> Stream.<String>of(m.mapFileName(r.getName())).filter(Objects::nonNull).map(MergingMapper::new).map(()));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 245 */       stream = this.nested.stream().map(r -> new MappedResource(r, m));
/*     */     } 
/* 247 */     return stream.collect((Collector)Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 256 */     if (isReference()) {
/* 257 */       return getRef().toString();
/*     */     }
/* 259 */     return isEmpty() ? "" : stream().map(Object::toString)
/* 260 */       .collect(Collectors.joining(File.pathSeparator));
/*     */   }
/*     */   
/*     */   private MappedResourceCollection getRef() {
/* 264 */     return (MappedResourceCollection)getCheckedRef(MappedResourceCollection.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/MappedResourceCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
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
/*     */ public abstract class BaseResourceCollectionContainer
/*     */   extends DataType
/*     */   implements AppendableResourceCollection, Cloneable
/*     */ {
/*  41 */   private List<ResourceCollection> rc = new ArrayList<>();
/*  42 */   private Collection<Resource> coll = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cache = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseResourceCollectionContainer(Project project) {
/*  57 */     setProject(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCache(boolean b) {
/*  65 */     this.cache = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isCache() {
/*  73 */     return this.cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() throws BuildException {
/*  81 */     if (isReference()) {
/*  82 */       throw noChildrenAllowed();
/*     */     }
/*  84 */     this.rc.clear();
/*  85 */     FailFast.invalidate(this);
/*  86 */     this.coll = null;
/*  87 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection c) throws BuildException {
/*  97 */     if (isReference()) {
/*  98 */       throw noChildrenAllowed();
/*     */     }
/* 100 */     if (c == null) {
/*     */       return;
/*     */     }
/* 103 */     if (Project.getProject(c) == null) {
/* 104 */       Project p = getProject();
/* 105 */       if (p != null) {
/* 106 */         p.setProjectReference(c);
/*     */       }
/*     */     } 
/* 109 */     this.rc.add(c);
/* 110 */     FailFast.invalidate(this);
/* 111 */     this.coll = null;
/* 112 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addAll(Collection<? extends ResourceCollection> c) throws BuildException {
/* 121 */     if (isReference()) {
/* 122 */       throw noChildrenAllowed();
/*     */     }
/*     */     try {
/* 125 */       c.forEach(this::add);
/* 126 */     } catch (ClassCastException e) {
/* 127 */       throw new BuildException(e);
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
/*     */   public final synchronized Iterator<Resource> iterator() {
/* 139 */     if (isReference()) {
/* 140 */       return getRef().iterator();
/*     */     }
/* 142 */     dieOnCircularReference();
/* 143 */     return new FailFast(this, cacheCollection().iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 152 */     if (isReference()) {
/* 153 */       return getRef().size();
/*     */     }
/* 155 */     dieOnCircularReference();
/* 156 */     return cacheCollection().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isFilesystemOnly() {
/* 165 */     if (isReference()) {
/* 166 */       return getRef().isFilesystemOnly();
/*     */     }
/* 168 */     dieOnCircularReference();
/*     */     
/* 170 */     if (this.rc.stream().allMatch(ResourceCollection::isFilesystemOnly)) {
/* 171 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 175 */     return cacheCollection().stream()
/* 176 */       .allMatch(r -> r.asOptional(FileProvider.class).isPresent());
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
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 188 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 191 */     if (isReference()) {
/* 192 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 194 */       for (ResourceCollection resourceCollection : this.rc) {
/* 195 */         if (resourceCollection instanceof DataType) {
/* 196 */           pushAndInvokeCircularReferenceCheck((DataType)resourceCollection, stk, p);
/*     */         }
/*     */       } 
/* 199 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized List<ResourceCollection> getResourceCollections() {
/* 208 */     dieOnCircularReference();
/* 209 */     return Collections.unmodifiableList(this.rc);
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
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 227 */       BaseResourceCollectionContainer c = (BaseResourceCollectionContainer)super.clone();
/* 228 */       c.rc = new ArrayList<>(this.rc);
/* 229 */       c.coll = null;
/* 230 */       return c;
/* 231 */     } catch (CloneNotSupportedException e) {
/* 232 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 242 */     if (isReference()) {
/* 243 */       return getRef().toString();
/*     */     }
/* 245 */     if (cacheCollection().isEmpty()) {
/* 246 */       return "";
/*     */     }
/* 248 */     return this.coll.stream().map(Object::toString)
/* 249 */       .collect(Collectors.joining(File.pathSeparator));
/*     */   }
/*     */   
/*     */   private BaseResourceCollectionContainer getRef() {
/* 253 */     return (BaseResourceCollectionContainer)getCheckedRef(BaseResourceCollectionContainer.class);
/*     */   }
/*     */   
/*     */   private synchronized Collection<Resource> cacheCollection() {
/* 257 */     if (this.coll == null || !isCache()) {
/* 258 */       this.coll = getCollection();
/*     */     }
/* 260 */     return this.coll;
/*     */   }
/*     */   
/*     */   public BaseResourceCollectionContainer() {}
/*     */   
/*     */   protected abstract Collection<Resource> getCollection();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/BaseResourceCollectionContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
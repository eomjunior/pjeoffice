/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractResourceCollectionWrapper
/*     */   extends DataType
/*     */   implements ResourceCollection, Cloneable
/*     */ {
/*     */   private static final String ONE_NESTED_MESSAGE = " expects exactly one nested resource collection.";
/*     */   private ResourceCollection rc;
/*     */   private boolean cache = true;
/*     */   
/*     */   public synchronized void setCache(boolean b) {
/*  49 */     this.cache = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isCache() {
/*  57 */     return this.cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection c) throws BuildException {
/*  66 */     if (isReference()) {
/*  67 */       throw noChildrenAllowed();
/*     */     }
/*  69 */     if (c == null) {
/*     */       return;
/*     */     }
/*  72 */     if (this.rc != null) {
/*  73 */       throw oneNested();
/*     */     }
/*  75 */     this.rc = c;
/*  76 */     if (Project.getProject(this.rc) == null) {
/*  77 */       Project p = getProject();
/*  78 */       if (p != null) {
/*  79 */         p.setProjectReference(this.rc);
/*     */       }
/*     */     } 
/*  82 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized Iterator<Resource> iterator() {
/*  91 */     if (isReference()) {
/*  92 */       return getRef().iterator();
/*     */     }
/*  94 */     dieOnCircularReference();
/*  95 */     return new FailFast(this, createIterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Iterator<Resource> createIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 114 */     if (isReference()) {
/* 115 */       return getRef().size();
/*     */     }
/* 117 */     dieOnCircularReference();
/* 118 */     return getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int getSize();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isFilesystemOnly() {
/* 134 */     if (isReference()) {
/* 135 */       return getRef().isFilesystemOnly();
/*     */     }
/* 137 */     dieOnCircularReference();
/*     */     
/* 139 */     if (this.rc == null || this.rc.isFilesystemOnly()) {
/* 140 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 144 */     for (Resource r : this) {
/* 145 */       if (r.as(FileProvider.class) == null) {
/* 146 */         return false;
/*     */       }
/*     */     } 
/* 149 */     return true;
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
/* 161 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 164 */     if (isReference()) {
/* 165 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 167 */       if (this.rc instanceof DataType) {
/* 168 */         pushAndInvokeCircularReferenceCheck((DataType)this.rc, stk, p);
/*     */       }
/* 170 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final synchronized ResourceCollection getResourceCollection() {
/* 180 */     dieOnCircularReference();
/* 181 */     if (this.rc == null) {
/* 182 */       throw oneNested();
/*     */     }
/* 184 */     return this.rc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 193 */     if (isReference()) {
/* 194 */       return getRef().toString();
/*     */     }
/* 196 */     if (isEmpty()) {
/* 197 */       return "";
/*     */     }
/* 199 */     return stream().map(Object::toString)
/* 200 */       .collect(Collectors.joining(File.pathSeparator));
/*     */   }
/*     */   
/*     */   private AbstractResourceCollectionWrapper getRef() {
/* 204 */     return (AbstractResourceCollectionWrapper)getCheckedRef(AbstractResourceCollectionWrapper.class);
/*     */   }
/*     */   
/*     */   private BuildException oneNested() {
/* 208 */     return new BuildException(super.toString() + " expects exactly one nested resource collection.");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/AbstractResourceCollectionWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
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
/*     */ public class Resources
/*     */   extends DataType
/*     */   implements AppendableResourceCollection
/*     */ {
/*  45 */   public static final ResourceCollection NONE = new ResourceCollection()
/*     */     {
/*     */       public boolean isFilesystemOnly() {
/*  48 */         return true;
/*     */       }
/*     */       
/*     */       public Iterator<Resource> iterator() {
/*  52 */         return Resources.EMPTY_ITERATOR;
/*     */       }
/*     */       
/*     */       public int size() {
/*  56 */         return 0;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  61 */   public static final Iterator<Resource> EMPTY_ITERATOR = new Iterator<Resource>()
/*     */     {
/*     */       public Resource next() {
/*  64 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/*     */       public boolean hasNext() {
/*  68 */         return false;
/*     */       }
/*     */       
/*     */       public void remove() {
/*  72 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   
/*     */   private List<ResourceCollection> rc;
/*     */   private Collection<Resource> coll;
/*     */   
/*     */   private class MyCollection extends AbstractCollection<Resource> {
/*     */     private Collection<Resource> cached;
/*     */     
/*     */     public int size() {
/*  83 */       return getCache().size();
/*     */     }
/*     */     
/*     */     public Iterator<Resource> iterator() {
/*  87 */       return getCache().iterator();
/*     */     }
/*     */     private synchronized Collection<Resource> getCache() {
/*  90 */       Collection<Resource> coll = this.cached;
/*  91 */       if (coll == null) {
/*  92 */         coll = new ArrayList<>();
/*  93 */         Objects.requireNonNull(coll); (new MyIterator()).forEachRemaining(coll::add);
/*  94 */         if (Resources.this.cache) {
/*  95 */           this.cached = coll;
/*     */         }
/*     */       } 
/*  98 */       return coll;
/*     */     }
/*     */     
/* 101 */     private class MyIterator implements Iterator<Resource> { private Iterator<ResourceCollection> rci = Resources.this.getNested().iterator();
/* 102 */       private Iterator<Resource> ri = null;
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/* 106 */         boolean result = (this.ri != null && this.ri.hasNext());
/* 107 */         while (!result && this.rci.hasNext()) {
/* 108 */           this.ri = ((ResourceCollection)this.rci.next()).iterator();
/* 109 */           result = this.ri.hasNext();
/*     */         } 
/* 111 */         return result;
/*     */       }
/*     */       
/*     */       public Resource next() {
/* 115 */         if (!hasNext()) {
/* 116 */           throw new NoSuchElementException();
/*     */         }
/* 118 */         return this.ri.next();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 122 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private MyIterator() {} }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cache = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resources(Project project) {
/* 143 */     setProject(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCache(boolean b) {
/* 152 */     this.cache = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection c) {
/* 161 */     if (isReference()) {
/* 162 */       throw noChildrenAllowed();
/*     */     }
/* 164 */     if (c == null) {
/*     */       return;
/*     */     }
/* 167 */     if (this.rc == null) {
/* 168 */       this.rc = Collections.synchronizedList(new ArrayList<>());
/*     */     }
/* 170 */     this.rc.add(c);
/* 171 */     invalidateExistingIterators();
/* 172 */     this.coll = null;
/* 173 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Iterator<Resource> iterator() {
/* 182 */     if (isReference()) {
/* 183 */       return getRef().iterator();
/*     */     }
/* 185 */     validate();
/* 186 */     return new FailFast(this, this.coll.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 195 */     if (isReference()) {
/* 196 */       return getRef().size();
/*     */     }
/* 198 */     validate();
/* 199 */     return this.coll.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 208 */     if (isReference()) {
/* 209 */       return getRef().isFilesystemOnly();
/*     */     }
/* 211 */     validate();
/* 212 */     return getNested().stream()
/* 213 */       .allMatch(ResourceCollection::isFilesystemOnly);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 222 */     if (isReference()) {
/* 223 */       return getRef().toString();
/*     */     }
/* 225 */     validate();
/* 226 */     if (this.coll == null || this.coll.isEmpty()) {
/* 227 */       return "";
/*     */     }
/* 229 */     return this.coll.stream().map(Object::toString)
/* 230 */       .collect(Collectors.joining(File.pathSeparator));
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
/*     */   protected void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 243 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 246 */     if (isReference()) {
/* 247 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 249 */       for (ResourceCollection resourceCollection : getNested()) {
/* 250 */         if (resourceCollection instanceof DataType) {
/* 251 */           pushAndInvokeCircularReferenceCheck((DataType)resourceCollection, stk, p);
/*     */         }
/*     */       } 
/* 254 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void invalidateExistingIterators() {
/* 262 */     FailFast.invalidate(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceCollection getRef() {
/* 270 */     return (ResourceCollection)getCheckedRef(ResourceCollection.class);
/*     */   }
/*     */   
/*     */   private synchronized void validate() {
/* 274 */     dieOnCircularReference();
/* 275 */     this.coll = (this.coll == null) ? new MyCollection() : this.coll;
/*     */   }
/*     */   
/*     */   private synchronized List<ResourceCollection> getNested() {
/* 279 */     return (this.rc == null) ? Collections.<ResourceCollection>emptyList() : this.rc;
/*     */   }
/*     */   
/*     */   public Resources() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Resources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
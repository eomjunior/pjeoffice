/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelectorContainer;
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
/*     */ public class Restrict
/*     */   extends ResourceSelectorContainer
/*     */   implements ResourceCollection
/*     */ {
/*  38 */   private LazyResourceCollectionWrapper w = new LazyResourceCollectionWrapper()
/*     */     {
/*     */ 
/*     */       
/*     */       protected boolean filterResource(Resource r)
/*     */       {
/*  44 */         return Restrict.this.getResourceSelectors().stream().anyMatch(rsel -> !rsel.isSelected(r));
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection c) {
/*  53 */     if (isReference()) {
/*  54 */       throw noChildrenAllowed();
/*     */     }
/*  56 */     if (c == null) {
/*     */       return;
/*     */     }
/*  59 */     this.w.add(c);
/*  60 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCache(boolean b) {
/*  68 */     this.w.setCache(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isCache() {
/*  76 */     return this.w.isCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceSelector s) {
/*  85 */     if (s == null) {
/*     */       return;
/*     */     }
/*  88 */     super.add(s);
/*  89 */     FailFast.invalidate(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized Iterator<Resource> iterator() {
/*  98 */     if (isReference()) {
/*  99 */       return getRef().iterator();
/*     */     }
/* 101 */     dieOnCircularReference();
/* 102 */     return this.w.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 111 */     if (isReference()) {
/* 112 */       return getRef().size();
/*     */     }
/* 114 */     dieOnCircularReference();
/* 115 */     return this.w.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isFilesystemOnly() {
/* 124 */     if (isReference()) {
/* 125 */       return getRef().isFilesystemOnly();
/*     */     }
/* 127 */     dieOnCircularReference();
/* 128 */     return this.w.isFilesystemOnly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 137 */     if (isReference()) {
/* 138 */       return getRef().toString();
/*     */     }
/* 140 */     dieOnCircularReference();
/* 141 */     return this.w.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
/* 146 */     if (isChecked()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 151 */     super.dieOnCircularReference(stk, p);
/*     */     
/* 153 */     if (!isReference()) {
/* 154 */       pushAndInvokeCircularReferenceCheck(this.w, stk, p);
/* 155 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Restrict getRef() {
/* 160 */     return (Restrict)getCheckedRef(Restrict.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Restrict.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
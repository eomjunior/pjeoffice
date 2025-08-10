/*     */ package org.apache.tools.ant.types.resources.comparators;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Resource;
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
/*     */ public class DelegatedResourceComparator
/*     */   extends ResourceComparator
/*     */ {
/*  37 */   private List<ResourceComparator> resourceComparators = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceComparator c) {
/*  44 */     if (isReference()) {
/*  45 */       throw noChildrenAllowed();
/*     */     }
/*  47 */     if (c == null) {
/*     */       return;
/*     */     }
/*  50 */     this.resourceComparators = (this.resourceComparators == null) ? new Vector<>() : this.resourceComparators;
/*  51 */     this.resourceComparators.add(c);
/*  52 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean equals(Object o) {
/*  63 */     if (o == this) {
/*  64 */       return true;
/*     */     }
/*  66 */     if (isReference()) {
/*  67 */       return getRef().equals(o);
/*     */     }
/*  69 */     if (o instanceof DelegatedResourceComparator) {
/*  70 */       List<ResourceComparator> ov = ((DelegatedResourceComparator)o).resourceComparators;
/*  71 */       return (this.resourceComparators == null) ? ((ov == null)) : this.resourceComparators.equals(ov);
/*     */     } 
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int hashCode() {
/*  82 */     if (isReference()) {
/*  83 */       return getRef().hashCode();
/*     */     }
/*  85 */     return (this.resourceComparators == null) ? 0 : this.resourceComparators.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized int resourceCompare(Resource foo, Resource bar) {
/*  91 */     return composite((List)this.resourceComparators).compare(foo, bar);
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
/* 104 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 107 */     if (isReference()) {
/* 108 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 110 */       if (this.resourceComparators != null && !this.resourceComparators.isEmpty()) {
/* 111 */         for (ResourceComparator resourceComparator : this.resourceComparators) {
/* 112 */           if (resourceComparator instanceof org.apache.tools.ant.types.DataType) {
/* 113 */             pushAndInvokeCircularReferenceCheck(resourceComparator, stk, p);
/*     */           }
/*     */         } 
/*     */       }
/* 117 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private DelegatedResourceComparator getRef() {
/* 122 */     return (DelegatedResourceComparator)getCheckedRef(DelegatedResourceComparator.class);
/*     */   }
/*     */   
/*     */   private static Comparator<Resource> composite(List<? extends Comparator<Resource>> foo) {
/* 126 */     Comparator<Resource> result = null;
/* 127 */     if (foo != null) {
/* 128 */       for (Comparator<Resource> comparator : foo) {
/* 129 */         if (result == null) {
/* 130 */           result = comparator;
/*     */           continue;
/*     */         } 
/* 133 */         result = result.thenComparing(comparator);
/*     */       } 
/*     */     }
/* 136 */     return (result == null) ? Comparator.<Resource>naturalOrder() : result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/comparators/DelegatedResourceComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
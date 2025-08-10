/*     */ package org.apache.tools.ant.types.resources.selectors;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
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
/*     */ public class ResourceSelectorContainer
/*     */   extends DataType
/*     */ {
/*  36 */   private final List<ResourceSelector> resourceSelectors = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceSelectorContainer() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceSelectorContainer(ResourceSelector... resourceSelectors) {
/*  49 */     for (ResourceSelector rsel : resourceSelectors) {
/*  50 */       add(rsel);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceSelector s) {
/*  59 */     if (isReference()) {
/*  60 */       throw noChildrenAllowed();
/*     */     }
/*  62 */     if (s == null) {
/*     */       return;
/*     */     }
/*  65 */     this.resourceSelectors.add(s);
/*  66 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSelectors() {
/*  74 */     if (isReference()) {
/*  75 */       return getRef().hasSelectors();
/*     */     }
/*  77 */     dieOnCircularReference();
/*  78 */     return !this.resourceSelectors.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int selectorCount() {
/*  86 */     if (isReference()) {
/*  87 */       return getRef().selectorCount();
/*     */     }
/*  89 */     dieOnCircularReference();
/*  90 */     return this.resourceSelectors.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<ResourceSelector> getSelectors() {
/*  98 */     if (isReference()) {
/*  99 */       return getRef().getSelectors();
/*     */     }
/* 101 */     return getResourceSelectors().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ResourceSelector> getResourceSelectors() {
/* 109 */     if (isReference()) {
/* 110 */       return getRef().getResourceSelectors();
/*     */     }
/* 112 */     dieOnCircularReference();
/* 113 */     return Collections.unmodifiableList(this.resourceSelectors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 124 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 127 */     if (isReference()) {
/* 128 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 130 */       for (ResourceSelector resourceSelector : this.resourceSelectors) {
/* 131 */         if (resourceSelector instanceof DataType) {
/* 132 */           pushAndInvokeCircularReferenceCheck((DataType)resourceSelector, stk, p);
/*     */         }
/*     */       } 
/* 135 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ResourceSelectorContainer getRef() {
/* 140 */     return (ResourceSelectorContainer)getCheckedRef(ResourceSelectorContainer.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/ResourceSelectorContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
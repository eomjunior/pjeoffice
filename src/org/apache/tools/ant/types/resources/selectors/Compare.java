/*     */ package org.apache.tools.ant.types.resources.selectors;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Comparison;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Quantifier;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;
/*     */ import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
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
/*     */ public class Compare
/*     */   extends DataType
/*     */   implements ResourceSelector
/*     */ {
/*  40 */   private DelegatedResourceComparator comp = new DelegatedResourceComparator();
/*  41 */   private Quantifier against = Quantifier.ALL;
/*     */   
/*  43 */   private Comparison when = Comparison.EQUAL;
/*     */ 
/*     */ 
/*     */   
/*     */   private Union control;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceComparator c) {
/*  53 */     if (isReference()) {
/*  54 */       throw noChildrenAllowed();
/*     */     }
/*  56 */     this.comp.add(c);
/*  57 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setAgainst(Quantifier against) {
/*  65 */     if (isReference()) {
/*  66 */       throw tooManyAttributes();
/*     */     }
/*  68 */     this.against = against;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setWhen(Comparison when) {
/*  76 */     if (isReference()) {
/*  77 */       throw tooManyAttributes();
/*     */     }
/*  79 */     this.when = when;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ResourceCollection createControl() {
/*  88 */     if (isReference()) {
/*  89 */       throw noChildrenAllowed();
/*     */     }
/*  91 */     if (this.control != null) {
/*  92 */       throw oneControl();
/*     */     }
/*  94 */     this.control = new Union();
/*  95 */     setChecked(false);
/*  96 */     return (ResourceCollection)this.control;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isSelected(Resource r) {
/* 102 */     if (isReference()) {
/* 103 */       return getRef().isSelected(r);
/*     */     }
/* 105 */     if (this.control == null) {
/* 106 */       throw oneControl();
/*     */     }
/* 108 */     dieOnCircularReference();
/* 109 */     int t = 0, f = 0;
/* 110 */     for (Resource res : this.control) {
/* 111 */       if (this.when.evaluate(this.comp.compare(r, res))) {
/* 112 */         t++; continue;
/*     */       } 
/* 114 */       f++;
/*     */     } 
/*     */     
/* 117 */     return this.against.evaluate(t, f);
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
/* 129 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 132 */     if (isReference()) {
/* 133 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 135 */       if (this.control != null) {
/* 136 */         DataType.pushAndInvokeCircularReferenceCheck((DataType)this.control, stk, p);
/*     */       }
/* 138 */       DataType.pushAndInvokeCircularReferenceCheck((DataType)this.comp, stk, p);
/* 139 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ResourceSelector getRef() {
/* 144 */     return (ResourceSelector)getCheckedRef(ResourceSelector.class);
/*     */   }
/*     */   
/*     */   private BuildException oneControl() {
/* 148 */     return new BuildException("%s the <control> element should be specified exactly once.", new Object[] {
/* 149 */           toString()
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Compare.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Objects;
/*    */ import java.util.Stack;
/*    */ import java.util.stream.Collectors;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.types.DataType;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;
/*    */ import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sort
/*    */   extends BaseResourceCollectionWrapper
/*    */ {
/* 41 */   private DelegatedResourceComparator comp = new DelegatedResourceComparator();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected synchronized Collection<Resource> getCollection() {
/* 49 */     Objects.requireNonNull(Resource.class); return (Collection<Resource>)getResourceCollection().stream().map(Resource.class::cast)
/* 50 */       .sorted((Comparator)this.comp).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void add(ResourceComparator c) {
/* 59 */     if (isReference()) {
/* 60 */       throw noChildrenAllowed();
/*    */     }
/* 62 */     this.comp.add(c);
/* 63 */     FailFast.invalidate(this);
/* 64 */     setChecked(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 77 */     if (isChecked()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 82 */     super.dieOnCircularReference(stk, p);
/*    */     
/* 84 */     if (!isReference()) {
/* 85 */       DataType.pushAndInvokeCircularReferenceCheck((DataType)this.comp, stk, p);
/* 86 */       setChecked(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Sort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
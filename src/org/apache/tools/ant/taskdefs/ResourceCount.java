/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.types.Comparison;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ public class ResourceCount
/*     */   extends Task
/*     */   implements Condition
/*     */ {
/*     */   private static final String ONE_NESTED_MESSAGE = "ResourceCount can count resources from exactly one nested ResourceCollection.";
/*     */   private static final String COUNT_REQUIRED = "Use of the ResourceCount condition requires that the count attribute be set.";
/*     */   private ResourceCollection rc;
/*  41 */   private Comparison when = Comparison.EQUAL;
/*     */ 
/*     */   
/*     */   private Integer count;
/*     */ 
/*     */   
/*     */   private String property;
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection r) {
/*  51 */     if (this.rc != null) {
/*  52 */       throw new BuildException("ResourceCount can count resources from exactly one nested ResourceCollection.");
/*     */     }
/*  54 */     this.rc = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/*  62 */     Object o = r.getReferencedObject();
/*  63 */     if (!(o instanceof ResourceCollection)) {
/*  64 */       throw new BuildException("%s doesn't denote a ResourceCollection", new Object[] { r
/*  65 */             .getRefId() });
/*     */     }
/*  67 */     add((ResourceCollection)o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/*  75 */     if (this.rc == null) {
/*  76 */       throw new BuildException("ResourceCount can count resources from exactly one nested ResourceCollection.");
/*     */     }
/*  78 */     if (this.property == null) {
/*  79 */       log("resource count = " + this.rc.size());
/*     */     } else {
/*  81 */       getProject().setNewProperty(this.property, Integer.toString(this.rc.size()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() {
/*  92 */     if (this.rc == null) {
/*  93 */       throw new BuildException("ResourceCount can count resources from exactly one nested ResourceCollection.");
/*     */     }
/*  95 */     if (this.count == null) {
/*  96 */       throw new BuildException("Use of the ResourceCount condition requires that the count attribute be set.");
/*     */     }
/*  98 */     return this.when.evaluate(Integer.valueOf(this.rc.size()).compareTo(this.count));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCount(int c) {
/* 106 */     this.count = Integer.valueOf(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWhen(Comparison c) {
/* 115 */     this.when = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String p) {
/* 123 */     this.property = p;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ResourceCount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
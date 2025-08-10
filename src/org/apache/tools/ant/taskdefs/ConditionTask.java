/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.taskdefs.condition.ConditionBase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConditionTask
/*     */   extends ConditionBase
/*     */ {
/*  42 */   private String property = null;
/*  43 */   private Object value = "true";
/*  44 */   private Object alternative = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConditionTask() {
/*  50 */     super("condition");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String p) {
/*  59 */     this.property = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/*  69 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String v) {
/*  79 */     setValue(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElse(Object alt) {
/*  89 */     this.alternative = alt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElse(String e) {
/*  99 */     setElse(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 109 */     if (countConditions() > 1)
/* 110 */       throw new BuildException("You must not nest more than one condition into <%s>", new Object[] {
/*     */             
/* 112 */             getTaskName()
/*     */           }); 
/* 114 */     if (countConditions() < 1)
/* 115 */       throw new BuildException("You must nest a condition into <%s>", new Object[] {
/* 116 */             getTaskName()
/*     */           }); 
/* 118 */     if (this.property == null) {
/* 119 */       throw new BuildException("The property attribute is required.");
/*     */     }
/* 121 */     Condition c = getConditions().nextElement();
/* 122 */     if (c.eval()) {
/* 123 */       log("Condition true; setting " + this.property + " to " + this.value, 4);
/* 124 */       PropertyHelper.getPropertyHelper(getProject()).setNewProperty(this.property, this.value);
/* 125 */     } else if (this.alternative != null) {
/* 126 */       log("Condition false; setting " + this.property + " to " + this.alternative, 4);
/* 127 */       PropertyHelper.getPropertyHelper(getProject()).setNewProperty(this.property, this.alternative);
/*     */     } else {
/* 129 */       log("Condition false; not setting " + this.property, 4);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ConditionTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
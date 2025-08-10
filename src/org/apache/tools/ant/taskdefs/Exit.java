/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ExitStatusException;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public class Exit
/*     */   extends Task
/*     */ {
/*     */   private String message;
/*     */   private Object ifCondition;
/*     */   private Object unlessCondition;
/*     */   private NestedCondition nestedCondition;
/*     */   private Integer status;
/*     */   
/*     */   private static class NestedCondition
/*     */     extends ConditionBase
/*     */     implements Condition
/*     */   {
/*     */     private NestedCondition() {}
/*     */     
/*     */     public boolean eval() {
/*  55 */       if (countConditions() != 1) {
/*  56 */         throw new BuildException("A single nested condition is required.");
/*     */       }
/*     */       
/*  59 */       return ((Condition)getConditions().nextElement()).eval();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String value) {
/*  74 */     this.message = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIf(Object c) {
/*  84 */     this.ifCondition = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIf(String c) {
/*  93 */     setIf(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnless(Object c) {
/* 103 */     this.unlessCondition = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnless(String c) {
/* 112 */     setUnless(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatus(int i) {
/* 120 */     this.status = Integer.valueOf(i);
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
/*     */   
/*     */   public void execute() throws BuildException {
/* 138 */     boolean fail = nestedConditionPresent() ? testNestedCondition() : ((testIfCondition() && testUnlessCondition()));
/* 139 */     if (fail) {
/* 140 */       String text = null;
/* 141 */       if (this.message != null && !this.message.trim().isEmpty()) {
/* 142 */         text = this.message.trim();
/*     */       } else {
/* 144 */         if (!isNullOrEmpty(this.ifCondition) && testIfCondition()) {
/* 145 */           text = "if=" + this.ifCondition;
/*     */         }
/* 147 */         if (!isNullOrEmpty(this.unlessCondition) && testUnlessCondition()) {
/* 148 */           if (text == null) {
/* 149 */             text = "";
/*     */           } else {
/* 151 */             text = text + " and ";
/*     */           } 
/* 153 */           text = text + "unless=" + this.unlessCondition;
/*     */         } 
/* 155 */         if (nestedConditionPresent()) {
/* 156 */           text = "condition satisfied";
/* 157 */         } else if (text == null) {
/* 158 */           text = "No message";
/*     */         } 
/*     */       } 
/* 161 */       log("failing due to " + text, 4);
/* 162 */       throw (this.status == null) ? new BuildException(text) : 
/* 163 */         new ExitStatusException(text, this.status.intValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isNullOrEmpty(Object value) {
/* 168 */     return (value == null || "".equals(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String msg) {
/* 176 */     if (this.message == null) {
/* 177 */       this.message = "";
/*     */     }
/* 179 */     this.message += getProject().replaceProperties(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConditionBase createCondition() {
/* 188 */     if (this.nestedCondition != null) {
/* 189 */       throw new BuildException("Only one nested condition is allowed.");
/*     */     }
/* 191 */     this.nestedCondition = new NestedCondition();
/* 192 */     return this.nestedCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean testIfCondition() {
/* 200 */     return PropertyHelper.getPropertyHelper(getProject())
/* 201 */       .testIfCondition(this.ifCondition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean testUnlessCondition() {
/* 210 */     return PropertyHelper.getPropertyHelper(getProject())
/* 211 */       .testUnlessCondition(this.unlessCondition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean testNestedCondition() {
/* 219 */     boolean result = nestedConditionPresent();
/*     */     
/* 221 */     if ((result && this.ifCondition != null) || this.unlessCondition != null) {
/* 222 */       throw new BuildException("Nested conditions not permitted in conjunction with if/unless attributes");
/*     */     }
/*     */ 
/*     */     
/* 226 */     return (result && this.nestedCondition.eval());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean nestedConditionPresent() {
/* 234 */     return (this.nestedCondition != null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Exit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
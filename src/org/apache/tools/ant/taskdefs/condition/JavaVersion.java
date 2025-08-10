/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.DeweyDecimal;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
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
/*     */ public class JavaVersion
/*     */   implements Condition
/*     */ {
/*  30 */   private String atMost = null;
/*  31 */   private String atLeast = null;
/*  32 */   private String exactly = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/*  40 */     validate();
/*  41 */     DeweyDecimal actual = JavaEnvUtils.getParsedJavaVersion();
/*  42 */     if (null != this.atLeast) {
/*  43 */       return actual.isGreaterThanOrEqual(new DeweyDecimal(this.atLeast));
/*     */     }
/*  45 */     if (null != this.exactly) {
/*  46 */       return actual.isEqual(new DeweyDecimal(this.exactly));
/*     */     }
/*  48 */     if (this.atMost != null) {
/*  49 */       return actual.isLessThanOrEqual(new DeweyDecimal(this.atMost));
/*     */     }
/*     */     
/*  52 */     return false;
/*     */   }
/*     */   
/*     */   private void validate() throws BuildException {
/*  56 */     if (this.atLeast != null && this.exactly != null && this.atMost != null) {
/*  57 */       throw new BuildException("Only one of atleast or atmost or exactly may be set.");
/*     */     }
/*  59 */     if (null == this.atLeast && null == this.exactly && this.atMost == null) {
/*  60 */       throw new BuildException("One of atleast or atmost or exactly must be set.");
/*     */     }
/*  62 */     if (this.atLeast != null) {
/*     */       
/*     */       try {
/*  65 */         new DeweyDecimal(this.atLeast);
/*  66 */       } catch (NumberFormatException e) {
/*  67 */         throw new BuildException("The 'atleast' attribute is not a Dewey Decimal eg 1.1.0 : " + this.atLeast);
/*     */       }
/*     */     
/*     */     }
/*  71 */     else if (this.atMost != null) {
/*     */       try {
/*  73 */         new DeweyDecimal(this.atMost);
/*  74 */       } catch (NumberFormatException e) {
/*  75 */         throw new BuildException("The 'atmost' attribute is not a Dewey Decimal eg 1.1.0 : " + this.atMost);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  82 */         new DeweyDecimal(this.exactly);
/*  83 */       } catch (NumberFormatException e) {
/*  84 */         throw new BuildException("The 'exactly' attribute is not a Dewey Decimal eg 1.1.0 : " + this.exactly);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAtLeast() {
/*  96 */     return this.atLeast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAtLeast(String atLeast) {
/* 106 */     this.atLeast = atLeast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAtMost() {
/* 115 */     return this.atMost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAtMost(String atMost) {
/* 126 */     this.atMost = atMost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExactly() {
/* 134 */     return this.exactly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExactly(String exactly) {
/* 144 */     this.exactly = exactly;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/JavaVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
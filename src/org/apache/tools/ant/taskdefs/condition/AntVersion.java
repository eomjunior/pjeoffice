/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.DeweyDecimal;
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
/*     */ public class AntVersion
/*     */   extends Task
/*     */   implements Condition
/*     */ {
/*  32 */   private String atLeast = null;
/*  33 */   private String exactly = null;
/*  34 */   private String propertyname = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  42 */     if (this.propertyname == null) {
/*  43 */       throw new BuildException("'property' must be set.");
/*     */     }
/*  45 */     if (this.atLeast != null || this.exactly != null) {
/*     */       
/*  47 */       if (eval()) {
/*  48 */         getProject().setNewProperty(this.propertyname, getVersion().toString());
/*     */       }
/*     */     } else {
/*     */       
/*  52 */       getProject().setNewProperty(this.propertyname, getVersion().toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/*  63 */     validate();
/*  64 */     DeweyDecimal actual = getVersion();
/*  65 */     if (null != this.atLeast) {
/*  66 */       return actual.isGreaterThanOrEqual(new DeweyDecimal(this.atLeast));
/*     */     }
/*  68 */     if (null != this.exactly) {
/*  69 */       return actual.isEqual(new DeweyDecimal(this.exactly));
/*     */     }
/*     */     
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   private void validate() throws BuildException {
/*  76 */     if (this.atLeast != null && this.exactly != null) {
/*  77 */       throw new BuildException("Only one of atleast or exactly may be set.");
/*     */     }
/*  79 */     if (null == this.atLeast && null == this.exactly) {
/*  80 */       throw new BuildException("One of atleast or exactly must be set.");
/*     */     }
/*  82 */     if (this.atLeast != null) {
/*     */       
/*     */       try {
/*  85 */         new DeweyDecimal(this.atLeast);
/*  86 */       } catch (NumberFormatException e) {
/*  87 */         throw new BuildException("The 'atleast' attribute is not a Dewey Decimal eg 1.1.0 : %s", new Object[] { this.atLeast });
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  94 */         new DeweyDecimal(this.exactly);
/*  95 */       } catch (NumberFormatException e) {
/*  96 */         throw new BuildException("The 'exactly' attribute is not a Dewey Decimal eg 1.1.0 : %s", new Object[] { this.exactly });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DeweyDecimal getVersion() {
/* 104 */     Project p = new Project();
/* 105 */     p.init();
/* 106 */     StringBuilder sb = new StringBuilder();
/* 107 */     boolean foundFirstDigit = false;
/* 108 */     for (char versionChar : p.getProperty("ant.version").toCharArray()) {
/* 109 */       if (Character.isDigit(versionChar)) {
/* 110 */         sb.append(versionChar);
/* 111 */         foundFirstDigit = true;
/*     */       } 
/* 113 */       if (versionChar == '.' && foundFirstDigit) {
/* 114 */         sb.append(versionChar);
/*     */       }
/* 116 */       if (Character.isLetter(versionChar) && foundFirstDigit) {
/*     */         break;
/*     */       }
/*     */     } 
/* 120 */     return new DeweyDecimal(sb.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAtLeast() {
/* 128 */     return this.atLeast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAtLeast(String atLeast) {
/* 138 */     this.atLeast = atLeast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExactly() {
/* 146 */     return this.exactly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExactly(String exactly) {
/* 156 */     this.exactly = exactly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProperty() {
/* 164 */     return this.propertyname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String propertyname) {
/* 172 */     this.propertyname = propertyname;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/AntVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
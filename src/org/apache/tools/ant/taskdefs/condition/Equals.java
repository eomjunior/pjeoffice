/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class Equals
/*     */   implements Condition
/*     */ {
/*     */   private static final int REQUIRED = 3;
/*     */   private Object arg1;
/*     */   private Object arg2;
/*     */   private boolean trim = false;
/*     */   private boolean caseSensitive = true;
/*     */   private int args;
/*     */   private boolean forcestring = false;
/*     */   
/*     */   public void setArg1(Object arg1) {
/*  43 */     if (arg1 instanceof String) {
/*  44 */       setArg1((String)arg1);
/*     */     } else {
/*  46 */       setArg1Internal(arg1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArg1(String arg1) {
/*  56 */     setArg1Internal(arg1);
/*     */   }
/*     */   
/*     */   private void setArg1Internal(Object arg1) {
/*  60 */     this.arg1 = arg1;
/*  61 */     this.args |= 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArg2(Object arg2) {
/*  70 */     if (arg2 instanceof String) {
/*  71 */       setArg2((String)arg2);
/*     */     } else {
/*  73 */       setArg2Internal(arg2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArg2(String arg2) {
/*  83 */     setArg2Internal(arg2);
/*     */   }
/*     */   
/*     */   private void setArg2Internal(Object arg2) {
/*  87 */     this.arg2 = arg2;
/*  88 */     this.args |= 0x2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrim(boolean b) {
/*  97 */     this.trim = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCasesensitive(boolean b) {
/* 107 */     this.caseSensitive = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForcestring(boolean forcestring) {
/* 117 */     this.forcestring = forcestring;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/* 125 */     if ((this.args & 0x3) != 3) {
/* 126 */       throw new BuildException("both arg1 and arg2 are required in equals");
/*     */     }
/* 128 */     if (this.arg1 == this.arg2 || (this.arg1 != null && this.arg1.equals(this.arg2))) {
/* 129 */       return true;
/*     */     }
/* 131 */     if (this.forcestring) {
/* 132 */       this.arg1 = (this.arg1 == null || this.arg1 instanceof String) ? this.arg1 : this.arg1.toString();
/* 133 */       this.arg2 = (this.arg2 == null || this.arg2 instanceof String) ? this.arg2 : this.arg2.toString();
/*     */     } 
/* 135 */     if (this.arg1 instanceof String && this.trim) {
/* 136 */       this.arg1 = ((String)this.arg1).trim();
/*     */     }
/* 138 */     if (this.arg2 instanceof String && this.trim) {
/* 139 */       this.arg2 = ((String)this.arg2).trim();
/*     */     }
/* 141 */     if (this.arg1 instanceof String && this.arg2 instanceof String) {
/* 142 */       String s1 = (String)this.arg1;
/* 143 */       String s2 = (String)this.arg2;
/* 144 */       return this.caseSensitive ? s1.equals(s2) : s1.equalsIgnoreCase(s2);
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Equals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
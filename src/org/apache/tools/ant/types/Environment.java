/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Vector;
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
/*     */ public class Environment
/*     */ {
/*     */   protected Vector<Variable> variables;
/*     */   
/*     */   public static class Variable
/*     */   {
/*     */     private String key;
/*     */     private String value;
/*     */     
/*     */     public void setKey(String key) {
/*  64 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/*  72 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getKey() {
/*  80 */       return this.key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/*  88 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPath(Path path) {
/*  98 */       this.value = path.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFile(File file) {
/* 106 */       this.value = file.getAbsolutePath();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getContent() throws BuildException {
/* 117 */       validate();
/* 118 */       return this.key.trim() + "=" + this.value.trim();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void validate() {
/* 126 */       if (this.key == null || this.value == null) {
/* 127 */         throw new BuildException("key and value must be specified for environment variables.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Environment() {
/* 137 */     this.variables = new Vector<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVariable(Variable var) {
/* 147 */     this.variables.addElement(var);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getVariables() throws BuildException {
/* 156 */     if (this.variables.isEmpty()) {
/* 157 */       return null;
/*     */     }
/* 159 */     return (String[])this.variables.stream().map(Variable::getContent).toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<Variable> getVariablesVector() {
/* 169 */     return this.variables;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Environment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Parameter;
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
/*     */ public class TypeSelector
/*     */   extends BaseExtendSelector
/*     */ {
/*     */   public static final String TYPE_KEY = "type";
/*  36 */   private String type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  42 */     return "{typeselector type: " + this.type + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(FileType fileTypes) {
/*  50 */     this.type = fileTypes.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/*  61 */     super.setParameters(parameters);
/*  62 */     if (parameters != null) {
/*  63 */       for (Parameter parameter : parameters) {
/*  64 */         String paramname = parameter.getName();
/*  65 */         if ("type".equalsIgnoreCase(paramname)) {
/*  66 */           FileType t = new FileType();
/*  67 */           t.setValue(parameter.getValue());
/*  68 */           setType(t);
/*     */         } else {
/*  70 */           setError("Invalid parameter " + paramname);
/*     */         } 
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
/*     */   public void verifySettings() {
/*  83 */     if (this.type == null) {
/*  84 */       setError("The type attribute is required");
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
/*     */ 
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 101 */     validate();
/*     */     
/* 103 */     if (file.isDirectory()) {
/* 104 */       return this.type.equals("dir");
/*     */     }
/* 106 */     return this.type.equals("file");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FileType
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final String FILE = "file";
/*     */ 
/*     */     
/*     */     public static final String DIR = "dir";
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 123 */       return new String[] { "file", "dir" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/TypeSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
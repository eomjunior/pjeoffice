/*     */ package org.apache.tools.ant.types.resources.selectors;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Resource;
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
/*     */ public class Type
/*     */   implements ResourceSelector
/*     */ {
/*     */   private static final String FILE_ATTR = "file";
/*     */   private static final String DIR_ATTR = "dir";
/*     */   private static final String ANY_ATTR = "any";
/*  35 */   public static final Type FILE = new Type(new FileDir("file"));
/*     */ 
/*     */   
/*  38 */   public static final Type DIR = new Type(new FileDir("dir"));
/*     */ 
/*     */   
/*  41 */   public static final Type ANY = new Type(new FileDir("any"));
/*     */ 
/*     */   
/*     */   public static class FileDir
/*     */     extends EnumeratedAttribute
/*     */   {
/*  47 */     private static final String[] VALUES = new String[] { "file", "dir", "any" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FileDir() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FileDir(String value) {
/*  60 */       setValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/*  69 */       return VALUES;
/*     */     }
/*     */   }
/*     */   
/*  73 */   private FileDir type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type(FileDir fd) {
/*  86 */     setType(fd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(FileDir fd) {
/*  94 */     this.type = fd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(Resource r) {
/* 103 */     if (this.type == null) {
/* 104 */       throw new BuildException("The type attribute is required.");
/*     */     }
/* 106 */     int i = this.type.getIndex();
/* 107 */     return (i == 2 || (r.isDirectory() ? (i == 1) : (i == 0)));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
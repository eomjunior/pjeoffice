/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
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
/*     */ public class PresentSelector
/*     */   extends BaseSelector
/*     */ {
/*  39 */   private File targetdir = null;
/*  40 */   private Mapper mapperElement = null;
/*  41 */   private FileNameMapper map = null;
/*     */ 
/*     */   
/*     */   private boolean destmustexist = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  49 */     StringBuilder buf = new StringBuilder("{presentselector targetdir: ");
/*  50 */     if (this.targetdir == null) {
/*  51 */       buf.append("NOT YET SET");
/*     */     } else {
/*  53 */       buf.append(this.targetdir.getName());
/*     */     } 
/*  55 */     buf.append(" present: ");
/*  56 */     if (this.destmustexist) {
/*  57 */       buf.append("both");
/*     */     } else {
/*  59 */       buf.append("srconly");
/*     */     } 
/*  61 */     if (this.map != null) {
/*  62 */       buf.append(this.map.toString());
/*  63 */     } else if (this.mapperElement != null) {
/*  64 */       buf.append(this.mapperElement.toString());
/*     */     } 
/*  66 */     buf.append("}");
/*  67 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetdir(File targetdir) {
/*  77 */     this.targetdir = targetdir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/*  86 */     if (this.map != null || this.mapperElement != null) {
/*  87 */       throw new BuildException("Cannot define more than one mapper");
/*     */     }
/*  89 */     this.mapperElement = new Mapper(getProject());
/*  90 */     return this.mapperElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(FileNameMapper fileNameMapper) {
/* 100 */     if (this.map != null || this.mapperElement != null) {
/* 101 */       throw new BuildException("Cannot define more than one mapper");
/*     */     }
/* 103 */     this.map = fileNameMapper;
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
/*     */   public void setPresent(FilePresence fp) {
/* 119 */     if (fp.getIndex() == 0) {
/* 120 */       this.destmustexist = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/* 130 */     if (this.targetdir == null) {
/* 131 */       setError("The targetdir attribute is required.");
/*     */     }
/* 133 */     if (this.map == null) {
/* 134 */       if (this.mapperElement == null) {
/* 135 */         this.map = (FileNameMapper)new IdentityMapper();
/*     */       } else {
/* 137 */         this.map = this.mapperElement.getImplementation();
/* 138 */         if (this.map == null) {
/* 139 */           setError("Could not set <mapper> element.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 158 */     validate();
/*     */ 
/*     */     
/* 161 */     String[] destfiles = this.map.mapFileName(filename);
/*     */ 
/*     */     
/* 164 */     if (destfiles == null) {
/* 165 */       return false;
/*     */     }
/*     */     
/* 168 */     if (destfiles.length != 1 || destfiles[0] == null) {
/* 169 */       throw new BuildException("Invalid destination file results for " + this.targetdir + " with filename " + filename);
/*     */     }
/*     */     
/* 172 */     String destname = destfiles[0];
/* 173 */     File destfile = FileUtils.getFileUtils().resolveFile(this.targetdir, destname);
/* 174 */     return (destfile.exists() == this.destmustexist);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FilePresence
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 187 */       return new String[] { "srconly", "both" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/PresentSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
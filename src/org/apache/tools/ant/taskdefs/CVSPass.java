/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class CVSPass
/*     */   extends Task
/*     */ {
/*  43 */   private String cvsRoot = null;
/*     */   
/*  45 */   private File passFile = null;
/*     */   
/*  47 */   private String password = null;
/*     */ 
/*     */   
/*  50 */   private final char[] shifts = new char[] { Character.MIN_VALUE, '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', 'r', 'x', '5', 'O', '`', 'm', 'H', 'l', 'F', '@', 'L', 'C', 't', 'J', 'D', 'W', 'o', '4', 'K', 'w', '1', '"', 'R', 'Q', '_', 'A', 'p', 'V', 'v', 'n', 'z', 'i', ')', '9', 'S', '+', '.', 'f', '(', 'Y', '&', 'g', '-', '2', '*', '{', '[', '#', '}', '7', '6', 'B', '|', '~', ';', '/', '\\', 'G', 's', 'N', 'X', 'k', 'j', '8', '$', 'y', 'u', 'h', 'e', 'd', 'E', 'I', 'c', '?', '^', ']', '\'', '%', '=', '0', ':', 'q', ' ', 'Z', ',', 'b', '<', '3', '!', 'a', '>', 'M', 'T', 'P', 'U', 'ß', 'á', 'Ø', '»', '¦', 'å', '½', 'Þ', '¼', '', 'ù', '', 'È', '¸', '', 'ø', '¾', 'Ç', 'ª', 'µ', 'Ì', '', 'è', 'Ú', '·', 'ÿ', 'ê', 'Ü', '÷', 'Õ', 'Ë', 'â', 'Á', '®', '¬', 'ä', 'ü', 'Ù', 'É', '', 'æ', 'Å', 'Ó', '', 'î', '¡', '³', ' ', 'Ô', 'Ï', 'Ý', 'þ', '­', 'Ê', '', 'à', '', '', 'Ä', 'Í', '', '', '', '', 'ö', 'À', '', 'ô', 'ï', '¹', '¨', '×', '', '', '¥', '´', '', '', 'º', 'Ö', '°', 'ã', 'ç', 'Û', '©', '¯', '', 'Î', 'Æ', '', '¤', '', 'Ò', '', '±', '', '', '¶', '', '', 'Ð', '¢', '', '§', 'Ñ', '', 'ñ', '', 'û', 'í', 'ì', '«', 'Ã', 'ó', 'é', 'ý', 'ð', 'Â', 'ú', '¿', '', '', '', 'õ', 'ë', '£', 'ò', '²', '' };
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
/*     */   public CVSPass() {
/*  73 */     this
/*  74 */       .passFile = new File(System.getProperty("cygwin.user.home", 
/*  75 */           System.getProperty("user.home")) + File.separatorChar + ".cvspass");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void execute() throws BuildException {
/*  86 */     if (this.cvsRoot == null) {
/*  87 */       throw new BuildException("cvsroot is required");
/*     */     }
/*  89 */     if (this.password == null) {
/*  90 */       throw new BuildException("password is required");
/*     */     }
/*     */     
/*  93 */     log("cvsRoot: " + this.cvsRoot, 4);
/*  94 */     log("password: " + this.password, 4);
/*  95 */     log("passFile: " + this.passFile, 4);
/*     */     
/*  97 */     BufferedReader reader = null;
/*  98 */     BufferedWriter writer = null;
/*     */     try {
/* 100 */       StringBuilder buf = new StringBuilder();
/*     */       
/* 102 */       if (this.passFile.exists()) {
/* 103 */         reader = new BufferedReader(new FileReader(this.passFile));
/*     */         
/* 105 */         String line = null;
/*     */         
/* 107 */         while ((line = reader.readLine()) != null) {
/* 108 */           if (!line.startsWith(this.cvsRoot)) {
/* 109 */             buf.append(line).append(System.lineSeparator());
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 115 */       String pwdfile = buf.toString() + this.cvsRoot + " A" + mangle(this.password);
/*     */       
/* 117 */       log("Writing -> " + pwdfile, 4);
/*     */       
/* 119 */       writer = new BufferedWriter(new FileWriter(this.passFile));
/*     */       
/* 121 */       writer.write(pwdfile);
/* 122 */       writer.newLine();
/* 123 */     } catch (IOException e) {
/* 124 */       throw new BuildException(e);
/*     */     } finally {
/* 126 */       FileUtils.close(reader);
/* 127 */       FileUtils.close(writer);
/*     */     } 
/*     */   }
/*     */   
/*     */   private final String mangle(String password) {
/* 132 */     StringBuilder buf = new StringBuilder();
/* 133 */     for (char ch : password.toCharArray()) {
/* 134 */       buf.append(this.shifts[ch]);
/*     */     }
/* 136 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCvsroot(String cvsRoot) {
/* 145 */     this.cvsRoot = cvsRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassfile(File passFile) {
/* 154 */     this.passFile = passFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 163 */     this.password = password;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/CVSPass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
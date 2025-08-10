/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.util.StreamUtils;
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
/*     */ public class ManifestTask
/*     */   extends Task
/*     */ {
/*     */   public static final String VALID_ATTRIBUTE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
/*  56 */   private Manifest nestedManifest = new Manifest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File manifestFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Mode mode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mergeClassPaths = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean flattenClassPaths = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Mode
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/*  94 */       return new String[] { "update", "replace" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManifestTask() {
/* 102 */     this.mode = new Mode();
/* 103 */     this.mode.setValue("replace");
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
/*     */   public void addConfiguredSection(Manifest.Section section) throws ManifestException {
/* 116 */     Objects.requireNonNull(section); StreamUtils.enumerationAsStream(section.getAttributeKeys()).map(section::getAttribute).forEach(this::checkAttribute);
/* 117 */     this.nestedManifest.addConfiguredSection(section);
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
/*     */   public void addConfiguredAttribute(Manifest.Attribute attribute) throws ManifestException {
/* 129 */     checkAttribute(attribute);
/* 130 */     this.nestedManifest.addConfiguredAttribute(attribute);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAttribute(Manifest.Attribute attribute) throws BuildException {
/* 151 */     String name = attribute.getName();
/* 152 */     char ch = name.charAt(0);
/*     */     
/* 154 */     if (ch == '-' || ch == '_') {
/* 155 */       throw new BuildException("Manifest attribute names must not start with '%c'.", new Object[] {
/* 156 */             Character.valueOf(ch)
/*     */           });
/*     */     }
/* 159 */     for (int i = 0; i < name.length(); i++) {
/* 160 */       ch = name.charAt(i);
/* 161 */       if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_".indexOf(ch) < 0) {
/* 162 */         throw new BuildException("Manifest attribute names must not contain '%c'", new Object[] {
/* 163 */               Character.valueOf(ch)
/*     */             });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File f) {
/* 174 */     this.manifestFile = f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 182 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(Mode m) {
/* 190 */     this.mode = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergeClassPathAttributes(boolean b) {
/* 200 */     this.mergeClassPaths = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFlattenAttributes(boolean b) {
/* 211 */     this.flattenClassPaths = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 221 */     if (this.manifestFile == null) {
/* 222 */       throw new BuildException("the file attribute is required");
/*     */     }
/*     */     
/* 225 */     Manifest toWrite = Manifest.getDefaultManifest();
/* 226 */     Manifest current = null;
/* 227 */     BuildException error = null;
/*     */     
/* 229 */     if (this.manifestFile.exists()) {
/* 230 */       Charset charset = Charset.forName((this.encoding == null) ? "UTF-8" : this.encoding);
/*     */       
/* 232 */       try { InputStreamReader isr = new InputStreamReader(Files.newInputStream(this.manifestFile.toPath(), new java.nio.file.OpenOption[0]), charset); 
/* 233 */         try { current = new Manifest(isr);
/* 234 */           isr.close(); } catch (Throwable throwable) { try { isr.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (ManifestException m)
/*     */       
/* 236 */       { error = new BuildException("Existing manifest " + this.manifestFile + " is invalid", m, getLocation()); }
/* 237 */       catch (IOException e)
/*     */       
/* 239 */       { error = new BuildException("Failed to read " + this.manifestFile, e, getLocation()); }
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 244 */     StreamUtils.enumerationAsStream(this.nestedManifest.getWarnings())
/* 245 */       .forEach(e -> log("Manifest warning: " + e, 1));
/*     */     try {
/* 247 */       if ("update".equals(this.mode.getValue()) && this.manifestFile.exists()) {
/* 248 */         if (current != null) {
/* 249 */           toWrite.merge(current, false, this.mergeClassPaths);
/* 250 */         } else if (error != null) {
/* 251 */           throw error;
/*     */         } 
/*     */       }
/*     */       
/* 255 */       toWrite.merge(this.nestedManifest, false, this.mergeClassPaths);
/* 256 */     } catch (ManifestException m) {
/* 257 */       throw new BuildException("Manifest is invalid", m, getLocation());
/*     */     } 
/*     */     
/* 260 */     if (toWrite.equals(current)) {
/* 261 */       log("Manifest has not changed, do not recreate", 3);
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 267 */     try { PrintWriter w = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.manifestFile.toPath(), new java.nio.file.OpenOption[0]), Manifest.JAR_CHARSET)); 
/* 268 */       try { toWrite.write(w, this.flattenClassPaths);
/* 269 */         if (w.checkError()) {
/* 270 */           throw new IOException("Encountered an error writing manifest");
/*     */         }
/* 272 */         w.close(); } catch (Throwable throwable) { try { w.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 273 */     { throw new BuildException("Failed to write " + this.manifestFile, e, 
/* 274 */           getLocation()); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ManifestTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
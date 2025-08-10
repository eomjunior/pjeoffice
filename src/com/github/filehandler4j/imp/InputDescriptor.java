/*     */ package com.github.filehandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public abstract class InputDescriptor
/*     */   implements IInputDescriptor
/*     */ {
/*     */   protected List<IInputFile> inputs;
/*     */   protected Path outputPath;
/*     */   protected String namePrefix;
/*     */   protected String nameSuffix;
/*     */   protected String extension;
/*     */   
/*     */   public Iterable<IInputFile> getInputFiles() {
/*  56 */     return this.inputs;
/*     */   }
/*     */ 
/*     */   
/*     */   public final File resolveOutput(String fileName) {
/*  61 */     return this.outputPath.resolve(this.namePrefix + fileName + this.nameSuffix + this.extension).toFile();
/*     */   }
/*     */   
/*     */   public static abstract class Builder {
/*  65 */     protected List<IInputFile> inputs = new ArrayList<>(2);
/*  66 */     protected String nameSuffix = Strings.empty();
/*  67 */     protected String namePrefix = Strings.empty();
/*     */     
/*     */     protected String extension;
/*  70 */     protected Path output = Paths.get(System.getProperty("java.io.tmpdir"), new String[0]);
/*     */     
/*     */     public Builder(String extension) {
/*  73 */       this.extension = Strings.trim(extension);
/*     */     }
/*     */     
/*     */     public Builder add(File input) {
/*  77 */       return add(new FileWrapper(input));
/*     */     }
/*     */     
/*     */     public Builder nameSuffix(String suffix) {
/*  81 */       Args.requireNonNull(suffix, "suffix is empty");
/*  82 */       this.nameSuffix = Strings.trim(suffix);
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public Builder namePrefix(String prefix) {
/*  87 */       Args.requireNonNull(prefix, "prefix is empty");
/*  88 */       this.namePrefix = Strings.trim(prefix);
/*  89 */       return this;
/*     */     }
/*     */     
/*     */     public Builder output(Path folder) {
/*  93 */       Args.requireNonNull(folder, "folder is null");
/*  94 */       this.output = folder;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(IInputFile file) {
/*  99 */       Args.requireTrue(file.exists(), "input does not exists");
/* 100 */       this.inputs.add(file);
/* 101 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends IInputDescriptor> T build() throws IOException {
/* 106 */       InputDescriptor desc = createDescriptor();
/* 107 */       desc.inputs = Collections.unmodifiableList(this.inputs);
/* 108 */       desc.namePrefix = this.namePrefix;
/* 109 */       desc.nameSuffix = this.nameSuffix;
/* 110 */       desc.outputPath = this.output;
/* 111 */       desc.extension = this.extension;
/* 112 */       File outputFile = this.output.toFile();
/* 113 */       if (!outputFile.exists() && 
/* 114 */         !outputFile.mkdirs()) {
/* 115 */         throw new IOException("Unabled to create output dir " + outputFile.getCanonicalPath());
/*     */       }
/*     */       
/* 118 */       return (T)desc;
/*     */     }
/*     */     
/*     */     protected abstract InputDescriptor createDescriptor();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/InputDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
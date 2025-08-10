/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.text.ParseException;
/*     */ import java.util.jar.Manifest;
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
/*     */ class LibraryDisplayer
/*     */ {
/*     */   void displayLibrary(File file) throws BuildException {
/*  42 */     Manifest manifest = ExtensionUtil.getManifest(file);
/*  43 */     displayLibrary(file, manifest);
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
/*     */   void displayLibrary(File file, Manifest manifest) throws BuildException {
/*  57 */     Extension[] available = Extension.getAvailable(manifest);
/*  58 */     Extension[] required = Extension.getRequired(manifest);
/*  59 */     Extension[] options = Extension.getOptions(manifest);
/*  60 */     Specification[] specifications = getSpecifications(manifest);
/*     */     
/*  62 */     if (0 == available.length && 0 == required.length && 0 == options.length && 0 == specifications.length) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  67 */     String message = "File: " + file;
/*  68 */     int size = message.length();
/*  69 */     printLine(size);
/*  70 */     System.out.println(message);
/*  71 */     printLine(size);
/*  72 */     if (0 != available.length) {
/*  73 */       System.out.println("Extensions Supported By Library:");
/*  74 */       for (Extension extension : available) {
/*  75 */         System.out.println(extension);
/*     */       }
/*     */     } 
/*     */     
/*  79 */     if (0 != required.length) {
/*  80 */       System.out.println("Extensions Required By Library:");
/*  81 */       for (Extension extension : required) {
/*  82 */         System.out.println(extension);
/*     */       }
/*     */     } 
/*     */     
/*  86 */     if (0 != options.length) {
/*  87 */       System.out.println("Extensions that will be used by Library if present:");
/*  88 */       for (Extension option : options) {
/*  89 */         System.out.println(option);
/*     */       }
/*     */     } 
/*     */     
/*  93 */     if (0 != specifications.length) {
/*  94 */       System.out.println("Specifications Supported By Library:");
/*  95 */       for (Specification specification : specifications) {
/*  96 */         displaySpecification(specification);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printLine(int size) {
/* 107 */     for (int i = 0; i < size; i++) {
/* 108 */       System.out.print("-");
/*     */     }
/* 110 */     System.out.println();
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
/*     */   private Specification[] getSpecifications(Manifest manifest) throws BuildException {
/*     */     try {
/* 123 */       return Specification.getSpecifications(manifest);
/* 124 */     } catch (ParseException pe) {
/* 125 */       throw new BuildException(pe.getMessage(), pe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void displaySpecification(Specification specification) {
/* 135 */     String[] sections = specification.getSections();
/* 136 */     if (null != sections) {
/* 137 */       System.out.print("Sections:  ");
/* 138 */       System.out
/* 139 */         .println(String.join(" ", (CharSequence[])sections));
/*     */     } 
/* 141 */     System.out.println(specification.toString());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/LibraryDisplayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
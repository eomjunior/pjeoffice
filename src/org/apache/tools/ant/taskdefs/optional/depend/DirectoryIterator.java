/*     */ package org.apache.tools.ant.taskdefs.optional.depend;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ public class DirectoryIterator
/*     */   implements ClassFileIterator
/*     */ {
/*     */   private Deque<Iterator<File>> enumStack;
/*     */   private Iterator<File> currentIterator;
/*     */   
/*     */   public DirectoryIterator(File rootDirectory, boolean changeInto) throws IOException {
/*  70 */     this.enumStack = new ArrayDeque<>();
/*  71 */     this.currentIterator = getDirectoryEntries(rootDirectory).iterator();
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
/*     */   private List<File> getDirectoryEntries(File directory) {
/*  83 */     File[] filesInDir = directory.listFiles();
/*  84 */     if (filesInDir == null) {
/*  85 */       return Collections.emptyList();
/*     */     }
/*  87 */     return Arrays.asList(filesInDir);
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
/*     */   public ClassFile getNextClassFile() {
/* 106 */     ClassFile nextElement = null;
/*     */     
/*     */     try {
/* 109 */       while (nextElement == null) {
/* 110 */         if (this.currentIterator.hasNext()) {
/* 111 */           File element = this.currentIterator.next();
/*     */           
/* 113 */           if (element.isDirectory()) {
/*     */ 
/*     */ 
/*     */             
/* 117 */             this.enumStack.push(this.currentIterator);
/*     */             
/* 119 */             List<File> files = getDirectoryEntries(element);
/*     */             
/* 121 */             this.currentIterator = files.iterator();
/*     */             
/*     */             continue;
/*     */           } 
/* 125 */           InputStream inFileStream = Files.newInputStream(element.toPath(), new java.nio.file.OpenOption[0]); 
/* 126 */           try { if (element.getName().endsWith(".class")) {
/*     */ 
/*     */ 
/*     */               
/* 130 */               ClassFile javaClass = new ClassFile();
/*     */               
/* 132 */               javaClass.read(inFileStream);
/*     */               
/* 134 */               nextElement = javaClass;
/*     */             } 
/* 136 */             if (inFileStream != null) inFileStream.close();  } catch (Throwable throwable) { if (inFileStream != null)
/*     */               try { inFileStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */            continue;
/* 139 */         }  if (this.enumStack.isEmpty()) {
/*     */           break;
/*     */         }
/* 142 */         this.currentIterator = this.enumStack.pop();
/*     */       }
/*     */     
/* 145 */     } catch (IOException e) {
/* 146 */       nextElement = null;
/*     */     } 
/*     */     
/* 149 */     return nextElement;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/DirectoryIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.tools.ant.taskdefs.optional.depend;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipInputStream;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JarFileIterator
/*    */   implements ClassFileIterator
/*    */ {
/*    */   private ZipInputStream jarStream;
/*    */   
/*    */   public JarFileIterator(InputStream stream) throws IOException {
/* 44 */     this.jarStream = new ZipInputStream(stream);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassFile getNextClassFile() {
/* 55 */     ClassFile nextElement = null;
/*    */     
/*    */     try {
/* 58 */       ZipEntry jarEntry = this.jarStream.getNextEntry();
/*    */       
/* 60 */       while (nextElement == null && jarEntry != null) {
/* 61 */         String entryName = jarEntry.getName();
/*    */         
/* 63 */         if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
/*    */ 
/*    */           
/* 66 */           ClassFile javaClass = new ClassFile();
/*    */           
/* 68 */           javaClass.read(this.jarStream);
/*    */           
/* 70 */           nextElement = javaClass; continue;
/*    */         } 
/* 72 */         jarEntry = this.jarStream.getNextEntry();
/*    */       }
/*    */     
/* 75 */     } catch (IOException e) {
/* 76 */       String message = e.getMessage();
/* 77 */       String text = e.getClass().getName();
/*    */       
/* 79 */       if (message != null) {
/* 80 */         text = text + ": " + message;
/*    */       }
/*    */       
/* 83 */       throw new BuildException("Problem reading JAR file: " + text);
/*    */     } 
/* 85 */     return nextElement;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/JarFileIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
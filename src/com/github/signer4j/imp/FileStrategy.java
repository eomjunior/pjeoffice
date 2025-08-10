/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverLookupStrategy;
/*    */ import com.github.signer4j.IDriverVisitor;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Paths;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ class FileStrategy
/*    */   extends AbstractStrategy
/*    */   implements IDriverLookupStrategy
/*    */ {
/* 46 */   private static final Logger LOGGER = LoggerFactory.getLogger(FileStrategy.class);
/*    */   
/*    */   private final File file;
/*    */   
/*    */   public FileStrategy(File file) {
/* 51 */     this.file = (File)Args.requireNonNull(file, "file can't be null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void lookup(IDriverVisitor visitor) {
/* 56 */     try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
/*    */       String line;
/* 58 */       while ((line = reader.readLine()) != null) {
/* 59 */         line = Strings.trim(line);
/* 60 */         if (line.isEmpty())
/*    */           continue; 
/* 62 */         createAndVisit(Paths.get(line, new String[0]), visitor);
/*    */       } 
/* 64 */     } catch (IOException e) {
/* 65 */       LOGGER.debug("Exceção durante a leitura de lib's em " + this.file.getAbsolutePath(), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/FileStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
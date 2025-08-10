/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IFilePath;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
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
/*    */ public class FilePath
/*    */   implements IFilePath
/*    */ {
/*    */   private final Path path;
/*    */   
/*    */   public FilePath(Path path) {
/* 42 */     this.path = (Path)Args.requireNonNull(path, "path is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getPath() {
/* 47 */     return this.path.toFile().getAbsolutePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public final Path toPath() {
/* 52 */     return this.path;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 57 */     return getPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     int prime = 31;
/* 63 */     int result = 1;
/* 64 */     result = 31 * result + ((this.path == null) ? 0 : this.path.hashCode());
/* 65 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 70 */     if (this == obj)
/* 71 */       return true; 
/* 72 */     if (obj == null)
/* 73 */       return false; 
/* 74 */     if (getClass() != obj.getClass())
/* 75 */       return false; 
/* 76 */     FilePath other = (FilePath)obj;
/* 77 */     if (this.path == null) {
/* 78 */       if (other.path != null)
/* 79 */         return false; 
/*    */     } else {
/*    */       try {
/* 82 */         if (!Files.isSameFile(this.path, other.path))
/* 83 */           return false; 
/* 84 */       } catch (IOException e) {
/* 85 */         return false;
/*    */       } 
/*    */     } 
/* 88 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/FilePath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
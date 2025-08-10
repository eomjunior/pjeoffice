/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverSetup;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Streams;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
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
/*    */ public class DriverSetup
/*    */   implements IDriverSetup
/*    */ {
/*    */   private final String md5;
/*    */   private final Path library;
/*    */   
/*    */   public static Optional<DriverSetup> create(Path library) {
/*    */     try {
/* 42 */       return Optional.of(new DriverSetup(library.toRealPath(new java.nio.file.LinkOption[0])));
/* 43 */     } catch (IOException e) {
/* 44 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private DriverSetup(Path library) throws IOException {
/* 53 */     this.library = (Path)Args.requireNonNull(library, "Unabled to create driversupport with null library");
/* 54 */     this.md5 = Streams.md5(library.toFile());
/*    */   }
/*    */ 
/*    */   
/*    */   public final Path getLibrary() {
/* 59 */     return this.library;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getMd5() {
/* 64 */     return this.md5;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 69 */     int prime = 31;
/* 70 */     int result = 1;
/* 71 */     result = 31 * result + ((this.md5 == null) ? 0 : this.md5.hashCode());
/* 72 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object obj) {
/* 77 */     if (this == obj)
/* 78 */       return true; 
/* 79 */     if (obj == null)
/* 80 */       return false; 
/* 81 */     if (getClass() != obj.getClass())
/* 82 */       return false; 
/* 83 */     DriverSetup other = (DriverSetup)obj;
/* 84 */     if (this.md5 == null) {
/* 85 */       if (other.md5 != null)
/* 86 */         return false; 
/* 87 */     } else if (!this.md5.equals(other.md5)) {
/* 88 */       return false;
/* 89 */     }  return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/DriverSetup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
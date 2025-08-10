/*    */ package com.github.signer4j;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.List;
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
/*    */ public interface IFilePath
/*    */ {
/*    */   String getPath();
/*    */   
/*    */   Path toPath();
/*    */   
/*    */   static Path[] toPaths(List<IFilePath> list) {
/* 40 */     return (Path[])list.stream().map(IFilePath::getPath).map(x$0 -> Paths.get(x$0, new String[0])).toArray(x$0 -> new Path[x$0]);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IFilePath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
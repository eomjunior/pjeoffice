/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.apache.tools.ant.types.FileSet;
/*    */ import org.apache.tools.ant.types.Resource;
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
/*    */ public class BCFileSet
/*    */   extends FileSet
/*    */ {
/*    */   public BCFileSet() {}
/*    */   
/*    */   public BCFileSet(FileSet fs) {
/* 42 */     super(fs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<Resource> iterator() {
/* 52 */     if (isReference()) {
/* 53 */       return getRef().iterator();
/*    */     }
/* 55 */     FileResourceIterator result = new FileResourceIterator(getProject(), getDir());
/* 56 */     result.addFiles(getDirectoryScanner().getIncludedFiles());
/* 57 */     result.addFiles(getDirectoryScanner().getIncludedDirectories());
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 68 */     if (isReference()) {
/* 69 */       return getRef().size();
/*    */     }
/* 71 */     return getDirectoryScanner().getIncludedFilesCount() + 
/* 72 */       getDirectoryScanner().getIncludedDirsCount();
/*    */   }
/*    */   
/*    */   private FileSet getRef() {
/* 76 */     return (FileSet)getCheckedRef(FileSet.class);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/BCFileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
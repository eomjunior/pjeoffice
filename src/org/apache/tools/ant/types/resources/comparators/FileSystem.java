/*    */ package org.apache.tools.ant.types.resources.comparators;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Comparator;
/*    */ import java.util.Objects;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.types.resources.FileProvider;
/*    */ import org.apache.tools.ant.util.FileUtils;
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
/*    */ public class FileSystem
/*    */   extends ResourceComparator
/*    */ {
/* 34 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
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
/*    */   protected int resourceCompare(Resource foo, Resource bar) {
/* 46 */     return compare(file(foo), file(bar));
/*    */   }
/*    */   
/*    */   private File file(Resource r) {
/* 50 */     return ((FileProvider)r.asOptional(FileProvider.class)
/* 51 */       .orElseThrow(() -> new ClassCastException(r.getClass() + " doesn't provide files")))
/*    */       
/* 53 */       .getFile();
/*    */   }
/*    */   
/*    */   private int compare(File f1, File f2) {
/* 57 */     if (Objects.equals(f1, f2)) {
/* 58 */       return 0;
/*    */     }
/* 60 */     if (FILE_UTILS.isLeadingPath(f1, f2)) {
/* 61 */       return -1;
/*    */     }
/* 63 */     if (FILE_UTILS.isLeadingPath(f2, f1)) {
/* 64 */       return 1;
/*    */     }
/*    */ 
/*    */     
/* 68 */     Objects.requireNonNull(FILE_UTILS); return Comparator.<File, Comparable>comparing(File::getAbsolutePath.andThen(FILE_UTILS::normalize))
/* 69 */       .compare(f1, f2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/comparators/FileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
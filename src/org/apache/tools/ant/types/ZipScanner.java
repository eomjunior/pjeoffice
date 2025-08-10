/*    */ package org.apache.tools.ant.types;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.zip.ZipException;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.resources.FileProvider;
/*    */ import org.apache.tools.ant.types.resources.ZipResource;
/*    */ import org.apache.tools.ant.util.StreamUtils;
/*    */ import org.apache.tools.zip.ZipEntry;
/*    */ import org.apache.tools.zip.ZipFile;
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
/*    */ public class ZipScanner
/*    */   extends ArchiveScanner
/*    */ {
/*    */   protected void fillMapsFromArchive(Resource src, String encoding, Map<String, Resource> fileEntries, Map<String, Resource> matchFileEntries, Map<String, Resource> dirEntries, Map<String, Resource> matchDirEntries) {
/* 60 */     File srcFile = (File)src.<FileProvider>asOptional(FileProvider.class).map(FileProvider::getFile).orElseThrow(() -> new BuildException("Only file provider resources are supported"));
/*    */ 
/*    */     
/* 63 */     try { ZipFile zf = new ZipFile(srcFile, encoding); 
/* 64 */       try { StreamUtils.enumerationAsStream(zf.getEntries()).forEach(entry -> {
/*    */               ZipResource zipResource = new ZipResource(srcFile, encoding, entry);
/*    */               String name = entry.getName();
/*    */               if (entry.isDirectory()) {
/*    */                 name = trimSeparator(name);
/*    */                 dirEntries.put(name, zipResource);
/*    */                 if (match(name)) {
/*    */                   matchDirEntries.put(name, zipResource);
/*    */                 }
/*    */               } else {
/*    */                 fileEntries.put(name, zipResource);
/*    */                 if (match(name)) {
/*    */                   matchFileEntries.put(name, zipResource);
/*    */                 }
/*    */               } 
/*    */             });
/* 80 */         zf.close(); } catch (Throwable throwable) { try { zf.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (ZipException ex)
/* 81 */     { throw new BuildException("Problem reading " + srcFile, ex); }
/* 82 */     catch (IOException ex)
/* 83 */     { throw new BuildException("Problem opening " + srcFile, ex); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/ZipScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
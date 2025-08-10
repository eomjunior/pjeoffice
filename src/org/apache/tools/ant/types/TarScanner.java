/*    */ package org.apache.tools.ant.types;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.resources.TarResource;
/*    */ import org.apache.tools.tar.TarEntry;
/*    */ import org.apache.tools.tar.TarInputStream;
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
/*    */ public class TarScanner
/*    */   extends ArchiveScanner
/*    */ {
/*    */   protected void fillMapsFromArchive(Resource src, String encoding, Map<String, Resource> fileEntries, Map<String, Resource> matchFileEntries, Map<String, Resource> dirEntries, Map<String, Resource> matchDirEntries) {
/*    */     
/* 55 */     try { TarInputStream ti = new TarInputStream(src.getInputStream(), encoding); 
/*    */       try { try {
/* 57 */           TarEntry entry = null;
/* 58 */           while ((entry = ti.getNextEntry()) != null) {
/* 59 */             TarResource tarResource = new TarResource(src, entry);
/* 60 */             String name = entry.getName();
/* 61 */             if (entry.isDirectory()) {
/* 62 */               name = trimSeparator(name);
/* 63 */               dirEntries.put(name, tarResource);
/* 64 */               if (match(name))
/* 65 */                 matchDirEntries.put(name, tarResource); 
/*    */               continue;
/*    */             } 
/* 68 */             fileEntries.put(name, tarResource);
/* 69 */             if (match(name)) {
/* 70 */               matchFileEntries.put(name, tarResource);
/*    */             }
/*    */           }
/*    */         
/* 74 */         } catch (IOException ex) {
/* 75 */           throw new BuildException("problem reading " + this.srcFile, ex);
/*    */         } 
/* 77 */         ti.close(); } catch (Throwable throwable) { try { ti.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ex)
/* 78 */     { throw new BuildException("problem opening " + this.srcFile, ex); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/TarScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
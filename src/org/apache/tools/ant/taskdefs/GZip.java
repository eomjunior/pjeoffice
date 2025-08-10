/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.util.zip.GZIPOutputStream;
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
/*    */ 
/*    */ public class GZip
/*    */   extends Pack
/*    */ {
/*    */   protected void pack() {
/*    */     
/* 43 */     try { GZIPOutputStream zOut = new GZIPOutputStream(Files.newOutputStream(this.zipFile.toPath(), new java.nio.file.OpenOption[0])); 
/* 44 */       try { zipResource(getSrcResource(), zOut);
/* 45 */         zOut.close(); } catch (Throwable throwable) { try { zOut.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 46 */     { String msg = "Problem creating gzip " + ioe.getMessage();
/* 47 */       throw new BuildException(msg, ioe, getLocation()); }
/*    */   
/*    */   }
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
/*    */   protected boolean supportsNonFileResources() {
/* 64 */     return getClass().equals(GZip.class);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/GZip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
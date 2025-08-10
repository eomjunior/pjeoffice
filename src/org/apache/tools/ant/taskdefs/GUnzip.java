/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.util.zip.GZIPInputStream;
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
/*    */ 
/*    */ 
/*    */ public class GUnzip
/*    */   extends Unpack
/*    */ {
/*    */   private static final int BUFFER_SIZE = 8192;
/*    */   private static final String DEFAULT_EXTENSION = ".gz";
/*    */   
/*    */   protected String getDefaultExtension() {
/* 48 */     return ".gz";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void extract() {
/* 56 */     if (this.srcResource.getLastModified() > this.dest.lastModified()) {
/* 57 */       log("Expanding " + this.srcResource.getName() + " to " + this.dest
/* 58 */           .getAbsolutePath()); 
/* 59 */       try { OutputStream out = Files.newOutputStream(this.dest.toPath(), new java.nio.file.OpenOption[0]);
/*    */         
/* 61 */         try { GZIPInputStream zIn = new GZIPInputStream(this.srcResource.getInputStream()); 
/* 62 */           try { byte[] buffer = new byte[8192];
/* 63 */             int count = 0;
/*    */             while (true)
/* 65 */             { out.write(buffer, 0, count);
/* 66 */               count = zIn.read(buffer, 0, buffer.length);
/* 67 */               if (count == -1)
/* 68 */               { zIn.close(); if (out != null) out.close();  break; }  }  } catch (Throwable throwable) { try { zIn.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (out != null) try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 69 */       { String msg = "Problem expanding gzip " + ioe.getMessage();
/* 70 */         throw new BuildException(msg, ioe, getLocation()); }
/*    */     
/*    */     } 
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
/* 88 */     return getClass().equals(GUnzip.class);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/GUnzip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
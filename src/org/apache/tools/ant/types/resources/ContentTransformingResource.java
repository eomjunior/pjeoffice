/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ContentTransformingResource
/*     */   extends ResourceDecorator
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   
/*     */   protected ContentTransformingResource() {}
/*     */   
/*     */   protected ContentTransformingResource(ResourceCollection other) {
/*  49 */     super(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/*  59 */     if (isExists()) {
/*  60 */       try { InputStream in = getInputStream(); 
/*  61 */         try { byte[] buf = new byte[8192];
/*  62 */           int size = 0;
/*     */           int readNow;
/*  64 */           while ((readNow = in.read(buf, 0, buf.length)) > 0) {
/*  65 */             size += readNow;
/*     */           }
/*  67 */           long l = size;
/*  68 */           if (in != null) in.close();  return l; } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/*  69 */       { throw new BuildException("caught exception while reading " + 
/*  70 */             getName(), ex); }
/*     */     
/*     */     }
/*  73 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  86 */     InputStream in = getResource().getInputStream();
/*  87 */     if (in != null) {
/*  88 */       in = wrapStream(in);
/*     */     }
/*  90 */     return in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 103 */     OutputStream out = getResource().getOutputStream();
/* 104 */     if (out != null) {
/* 105 */       out = wrapStream(out);
/*     */     }
/* 107 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T as(Class<T> clazz) {
/* 115 */     if (Appendable.class.isAssignableFrom(clazz)) {
/* 116 */       if (isAppendSupported()) {
/* 117 */         Appendable a = (Appendable)getResource().as(Appendable.class);
/* 118 */         if (a != null) {
/* 119 */           return clazz.cast(() -> {
/*     */                 OutputStream out = a.getAppendOutputStream();
/*     */                 return (out == null) ? null : wrapStream(out);
/*     */               });
/*     */         }
/*     */       } 
/* 125 */       return null;
/*     */     } 
/* 127 */     return FileProvider.class.isAssignableFrom(clazz) ? 
/* 128 */       null : (T)getResource().as(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAppendSupported() {
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract InputStream wrapStream(InputStream paramInputStream) throws IOException;
/*     */   
/*     */   protected abstract OutputStream wrapStream(OutputStream paramOutputStream) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/ContentTransformingResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
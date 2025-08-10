/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.file.Files;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.bzip2.CBZip2InputStream;
/*     */ import org.apache.tools.tar.TarEntry;
/*     */ import org.apache.tools.tar.TarInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Untar
/*     */   extends Expand
/*     */ {
/*  59 */   private UntarCompressionMethod compression = new UntarCompressionMethod();
/*     */   
/*     */   public Untar() {
/*  62 */     super((String)null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompression(UntarCompressionMethod method) {
/*  79 */     this.compression = method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScanForUnicodeExtraFields(boolean b) {
/*  89 */     throw new BuildException("The " + 
/*  90 */         getTaskName() + " task doesn't support the encoding attribute", 
/*     */         
/*  92 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
/* 101 */     if (!srcF.exists())
/* 102 */       throw new BuildException("Unable to untar " + srcF + " as the file does not exist", 
/*     */ 
/*     */           
/* 105 */           getLocation()); 
/*     */     
/* 107 */     try { InputStream fis = Files.newInputStream(srcF.toPath(), new java.nio.file.OpenOption[0]); 
/* 108 */       try { expandStream(srcF.getPath(), fis, dir);
/* 109 */         if (fis != null) fis.close();  } catch (Throwable throwable) { if (fis != null) try { fis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 110 */     { throw new BuildException("Error while expanding " + srcF.getPath() + "\n" + ioe
/* 111 */           .toString(), ioe, 
/* 112 */           getLocation()); }
/*     */   
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
/*     */   protected void expandResource(Resource srcR, File dir) {
/* 125 */     if (!srcR.isExists()) {
/* 126 */       throw new BuildException("Unable to untar " + srcR
/* 127 */           .getName() + " as the it does not exist", 
/*     */           
/* 129 */           getLocation());
/*     */     }
/*     */     
/* 132 */     try { InputStream i = srcR.getInputStream(); 
/* 133 */       try { expandStream(srcR.getName(), i, dir);
/* 134 */         if (i != null) i.close();  } catch (Throwable throwable) { if (i != null) try { i.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 135 */     { throw new BuildException("Error while expanding " + srcR.getName(), ioe, 
/* 136 */           getLocation()); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void expandStream(String name, InputStream stream, File dir) throws IOException {
/* 147 */     TarInputStream tis = new TarInputStream(this.compression.decompress(name, new BufferedInputStream(stream)), getEncoding()); try {
/* 148 */       log("Expanding: " + name + " into " + dir, 2);
/* 149 */       boolean empty = true;
/* 150 */       FileNameMapper mapper = getMapper();
/*     */       TarEntry te;
/* 152 */       while ((te = tis.getNextEntry()) != null) {
/* 153 */         empty = false;
/* 154 */         extractFile(FileUtils.getFileUtils(), (File)null, dir, (InputStream)tis, te
/* 155 */             .getName(), te.getModTime(), te
/* 156 */             .isDirectory(), mapper);
/*     */       } 
/* 158 */       if (empty && getFailOnEmptyArchive()) {
/* 159 */         throw new BuildException("archive '%s' is empty", new Object[] { name });
/*     */       }
/* 161 */       log("expand complete", 3);
/* 162 */       tis.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         tis.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class UntarCompressionMethod
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     private static final String NONE = "none";
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String GZIP = "gzip";
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String BZIP2 = "bzip2";
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String XZ = "xz";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public UntarCompressionMethod() {
/* 197 */       setValue("none");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 207 */       return new String[] { "none", "gzip", "bzip2", "xz" };
/*     */     }
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
/*     */     public InputStream decompress(String name, InputStream istream) throws IOException, BuildException {
/* 223 */       String v = getValue();
/* 224 */       if ("gzip".equals(v)) {
/* 225 */         return new GZIPInputStream(istream);
/*     */       }
/* 227 */       if ("xz".equals(v)) {
/* 228 */         return newXZInputStream(istream);
/*     */       }
/* 230 */       if ("bzip2".equals(v)) {
/* 231 */         char[] magic = { 'B', 'Z' };
/* 232 */         for (char c : magic) {
/* 233 */           if (istream.read() != c) {
/* 234 */             throw new BuildException("Invalid bz2 file." + name);
/*     */           }
/*     */         } 
/* 237 */         return (InputStream)new CBZip2InputStream(istream);
/*     */       } 
/* 239 */       return istream;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static InputStream newXZInputStream(InputStream istream) throws BuildException {
/*     */       try {
/* 247 */         Class<? extends InputStream> clazz = Class.forName("org.tukaani.xz.XZInputStream").asSubclass(InputStream.class);
/*     */         
/* 249 */         Constructor<? extends InputStream> c = clazz.getConstructor(new Class[] { InputStream.class });
/* 250 */         return c.newInstance(new Object[] { istream });
/* 251 */       } catch (ClassNotFoundException ex) {
/* 252 */         throw new BuildException("xz decompression requires the XZ for Java library", ex);
/*     */       }
/* 254 */       catch (NoSuchMethodException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 259 */         throw new BuildException("failed to create XZInputStream", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Untar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
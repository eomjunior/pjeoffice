/*      */ package org.zeroturnaround.zip;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import java.util.zip.ZipInputStream;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ import org.zeroturnaround.zip.commons.FileUtils;
/*      */ import org.zeroturnaround.zip.commons.FilenameUtils;
/*      */ import org.zeroturnaround.zip.commons.IOUtils;
/*      */ import org.zeroturnaround.zip.transform.ZipEntryTransformer;
/*      */ import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ZipUtil
/*      */ {
/*      */   private static final String PATH_SEPARATOR = "/";
/*      */   public static final int DEFAULT_COMPRESSION_LEVEL = -1;
/*   70 */   private static final Logger log = LoggerFactory.getLogger("org/zeroturnaround/zip/ZipUtil".replace('/', '.'));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsEntry(File zip, String name) {
/*   87 */     ZipFile zf = null;
/*      */     try {
/*   89 */       zf = new ZipFile(zip);
/*   90 */       return (zf.getEntry(name) != null);
/*      */     }
/*   92 */     catch (IOException e) {
/*   93 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*   96 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static int getCompressionLevelOfEntry(File zip, String name) {
/*  114 */     return getCompressionMethodOfEntry(zip, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getCompressionMethodOfEntry(File zip, String name) {
/*  128 */     ZipFile zf = null;
/*      */     try {
/*  130 */       zf = new ZipFile(zip);
/*  131 */       ZipEntry zipEntry = zf.getEntry(name);
/*  132 */       if (zipEntry == null) {
/*  133 */         return -1;
/*      */       }
/*  135 */       return zipEntry.getMethod();
/*      */     }
/*  137 */     catch (IOException e) {
/*  138 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  141 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsAnyEntry(File zip, String[] names) {
/*  156 */     ZipFile zf = null;
/*      */     try {
/*  158 */       zf = new ZipFile(zip); int i;
/*  159 */       for (i = 0; i < names.length; i++) {
/*  160 */         if (zf.getEntry(names[i]) != null) {
/*  161 */           return true;
/*      */         }
/*      */       } 
/*  164 */       i = 0; return i;
/*      */     }
/*  166 */     catch (IOException e) {
/*  167 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  170 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] unpackEntry(File zip, String name) {
/*  184 */     ZipFile zf = null;
/*      */     try {
/*  186 */       zf = new ZipFile(zip);
/*  187 */       return doUnpackEntry(zf, name);
/*      */     }
/*  189 */     catch (IOException e) {
/*  190 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  193 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] unpackEntry(File zip, String name, Charset charset) {
/*  211 */     ZipFile zf = null;
/*      */     try {
/*  213 */       if (charset != null) {
/*  214 */         zf = new ZipFile(zip, charset);
/*      */       } else {
/*      */         
/*  217 */         zf = new ZipFile(zip);
/*      */       } 
/*  219 */       return doUnpackEntry(zf, name);
/*      */     }
/*  221 */     catch (IOException e) {
/*  222 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  225 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] unpackEntry(ZipFile zf, String name) {
/*      */     try {
/*  240 */       return doUnpackEntry(zf, name);
/*      */     }
/*  242 */     catch (IOException e) {
/*  243 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] doUnpackEntry(ZipFile zf, String name) throws IOException {
/*  257 */     ZipEntry ze = zf.getEntry(name);
/*  258 */     if (ze == null) {
/*  259 */       return null;
/*      */     }
/*      */     
/*  262 */     InputStream is = zf.getInputStream(ze);
/*      */     try {
/*  264 */       return IOUtils.toByteArray(is);
/*      */     } finally {
/*      */       
/*  267 */       IOUtils.closeQuietly(is);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] unpackEntry(InputStream is, String name) {
/*  281 */     ByteArrayUnpacker action = new ByteArrayUnpacker();
/*  282 */     if (!handle(is, name, action))
/*  283 */       return null; 
/*  284 */     return action.getBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ByteArrayUnpacker
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private byte[] bytes;
/*      */ 
/*      */     
/*      */     private ByteArrayUnpacker() {}
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/*  297 */       this.bytes = IOUtils.toByteArray(in);
/*      */     }
/*      */     
/*      */     public byte[] getBytes() {
/*  301 */       return this.bytes;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean unpackEntry(File zip, String name, File file) {
/*  319 */     return unpackEntry(zip, name, file, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean unpackEntry(File zip, String name, File file, Charset charset) {
/*  338 */     ZipFile zf = null;
/*      */     try {
/*  340 */       if (charset != null) {
/*  341 */         zf = new ZipFile(zip, charset);
/*      */       } else {
/*      */         
/*  344 */         zf = new ZipFile(zip);
/*      */       } 
/*  346 */       return doUnpackEntry(zf, name, file);
/*      */     }
/*  348 */     catch (IOException e) {
/*  349 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  352 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean unpackEntry(ZipFile zf, String name, File file) {
/*      */     try {
/*  370 */       return doUnpackEntry(zf, name, file);
/*      */     }
/*  372 */     catch (IOException e) {
/*  373 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doUnpackEntry(ZipFile zf, String name, File file) throws IOException {
/*  390 */     if (log.isTraceEnabled()) {
/*  391 */       log.trace("Extracting '" + zf.getName() + "' entry '" + name + "' into '" + file + "'.");
/*      */     }
/*      */     
/*  394 */     ZipEntry ze = zf.getEntry(name);
/*  395 */     if (ze == null) {
/*  396 */       return false;
/*      */     }
/*      */     
/*  399 */     if (ze.isDirectory() || zf.getInputStream(ze) == null) {
/*  400 */       if (file.isDirectory()) {
/*  401 */         return true;
/*      */       }
/*  403 */       if (file.exists()) {
/*  404 */         FileUtils.forceDelete(file);
/*      */       }
/*  406 */       return file.mkdirs();
/*      */     } 
/*      */     
/*  409 */     InputStream in = new BufferedInputStream(zf.getInputStream(ze));
/*      */     try {
/*  411 */       FileUtils.copy(in, file);
/*      */     } finally {
/*      */       
/*  414 */       IOUtils.closeQuietly(in);
/*      */     } 
/*  416 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean unpackEntry(InputStream is, String name, File file) throws IOException {
/*  433 */     return handle(is, name, new FileUnpacker(file));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FileUnpacker
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private final File file;
/*      */ 
/*      */ 
/*      */     
/*      */     public FileUnpacker(File file) {
/*  446 */       this.file = file;
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/*  450 */       FileUtils.copy(in, this.file);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(File zip, ZipEntryCallback action) {
/*  472 */     iterate(zip, action, (Charset)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(File zip, ZipEntryCallback action, Charset charset) {
/*  493 */     ZipFile zf = null;
/*      */     try {
/*  495 */       if (charset == null) {
/*  496 */         zf = new ZipFile(zip);
/*      */       } else {
/*      */         
/*  499 */         zf = new ZipFile(zip, charset);
/*      */       } 
/*      */       
/*  502 */       Enumeration<? extends ZipEntry> en = zf.entries();
/*  503 */       while (en.hasMoreElements()) {
/*  504 */         ZipEntry e = en.nextElement();
/*      */         
/*  506 */         InputStream is = zf.getInputStream(e);
/*      */         try {
/*  508 */           action.process(is, e);
/*      */         }
/*  510 */         catch (IOException ze) {
/*  511 */           throw new ZipException("Failed to process zip entry '" + e.getName() + "' with action " + action, ze);
/*      */         }
/*  513 */         catch (ZipBreakException ex) {
/*      */           
/*      */           break;
/*      */         } finally {
/*  517 */           IOUtils.closeQuietly(is);
/*      */         }
/*      */       
/*      */       } 
/*  521 */     } catch (IOException e) {
/*  522 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  525 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(File zip, String[] entryNames, ZipEntryCallback action) {
/*  545 */     iterate(zip, entryNames, action, (Charset)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(File zip, String[] entryNames, ZipEntryCallback action, Charset charset) {
/*  566 */     ZipFile zf = null;
/*      */     try {
/*  568 */       if (charset == null) {
/*  569 */         zf = new ZipFile(zip);
/*      */       } else {
/*      */         
/*  572 */         zf = new ZipFile(zip, charset);
/*      */       } 
/*      */       
/*  575 */       for (int i = 0; i < entryNames.length; i++) {
/*  576 */         ZipEntry e = zf.getEntry(entryNames[i]);
/*  577 */         if (e != null)
/*      */         {
/*      */           
/*  580 */           InputStream is = zf.getInputStream(e);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  595 */     catch (IOException e) {
/*  596 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  599 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(File zip, ZipInfoCallback action) {
/*  618 */     ZipFile zf = null;
/*      */     try {
/*  620 */       zf = new ZipFile(zip);
/*      */       
/*  622 */       Enumeration<? extends ZipEntry> en = zf.entries();
/*  623 */       while (en.hasMoreElements()) {
/*  624 */         ZipEntry e = en.nextElement();
/*      */         try {
/*  626 */           action.process(e);
/*      */         }
/*  628 */         catch (IOException ze) {
/*  629 */           throw new ZipException("Failed to process zip entry '" + e.getName() + " with action " + action, ze);
/*      */         }
/*  631 */         catch (ZipBreakException ex) {
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  636 */     } catch (IOException e) {
/*  637 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  640 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(File zip, String[] entryNames, ZipInfoCallback action) {
/*  660 */     ZipFile zf = null;
/*      */     try {
/*  662 */       zf = new ZipFile(zip);
/*      */       
/*  664 */       for (int i = 0; i < entryNames.length; i++) {
/*  665 */         ZipEntry e = zf.getEntry(entryNames[i]);
/*  666 */         if (e != null) {
/*      */           
/*      */           try {
/*      */             
/*  670 */             action.process(e);
/*      */           }
/*  672 */           catch (IOException ze) {
/*  673 */             throw new ZipException("Failed to process zip entry '" + e.getName() + " with action " + action, ze);
/*      */           }
/*  675 */           catch (ZipBreakException ex) {
/*      */             break;
/*      */           } 
/*      */         }
/*      */       } 
/*  680 */     } catch (IOException e) {
/*  681 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  684 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(InputStream is, ZipEntryCallback action, Charset charset) {
/*      */     try {
/*  706 */       ZipInputStream in = null;
/*      */       try {
/*  708 */         in = newCloseShieldZipInputStream(is, charset);
/*      */         ZipEntry entry;
/*  710 */         while ((entry = in.getNextEntry()) != null) {
/*      */           try {
/*  712 */             action.process(in, entry);
/*      */           }
/*  714 */           catch (IOException ze) {
/*  715 */             throw new ZipException("Failed to process zip entry '" + entry.getName() + " with action " + action, ze);
/*      */           }
/*  717 */           catch (ZipBreakException ex) {
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } finally {
/*  723 */         if (in != null) {
/*  724 */           in.close();
/*      */         }
/*      */       }
/*      */     
/*  728 */     } catch (IOException e) {
/*  729 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(InputStream is, ZipEntryCallback action) {
/*  746 */     iterate(is, action, (Charset)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(InputStream is, String[] entryNames, ZipEntryCallback action, Charset charset) {
/*  767 */     Set<String> namesSet = new HashSet<String>();
/*  768 */     for (int i = 0; i < entryNames.length; i++) {
/*  769 */       namesSet.add(entryNames[i]);
/*      */     }
/*      */     try {
/*  772 */       ZipInputStream in = null;
/*      */       try {
/*  774 */         in = newCloseShieldZipInputStream(is, charset);
/*      */         ZipEntry entry;
/*  776 */         while ((entry = in.getNextEntry()) != null) {
/*  777 */           if (!namesSet.contains(entry.getName())) {
/*      */             continue;
/*      */           }
/*      */           
/*      */           try {
/*  782 */             action.process(in, entry);
/*      */           }
/*  784 */           catch (IOException ze) {
/*  785 */             throw new ZipException("Failed to process zip entry '" + entry.getName() + " with action " + action, ze);
/*      */           }
/*  787 */           catch (ZipBreakException ex) {
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } finally {
/*  793 */         if (in != null) {
/*  794 */           in.close();
/*      */         }
/*      */       }
/*      */     
/*  798 */     } catch (IOException e) {
/*  799 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void iterate(InputStream is, String[] entryNames, ZipEntryCallback action) {
/*  818 */     iterate(is, entryNames, action, (Charset)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ZipInputStream newCloseShieldZipInputStream(InputStream is, Charset charset) {
/*  826 */     InputStream in = new BufferedInputStream(new CloseShieldInputStream(is));
/*  827 */     if (charset == null) {
/*  828 */       return new ZipInputStream(in);
/*      */     }
/*  830 */     return ZipFileUtil.createZipInputStream(in, charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean handle(File zip, String name, ZipEntryCallback action) {
/*  848 */     ZipFile zf = null;
/*      */     try {
/*  850 */       zf = new ZipFile(zip);
/*      */       
/*  852 */       ZipEntry ze = zf.getEntry(name);
/*  853 */       if (ze == null) {
/*  854 */         return false;
/*      */       }
/*      */       
/*  857 */       InputStream in = new BufferedInputStream(zf.getInputStream(ze));
/*      */       try {
/*  859 */         action.process(in, ze);
/*      */       } finally {
/*      */         
/*  862 */         IOUtils.closeQuietly(in);
/*      */       } 
/*  864 */       return true;
/*      */     }
/*  866 */     catch (IOException e) {
/*  867 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/*  870 */       closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean handle(InputStream is, String name, ZipEntryCallback action) {
/*  890 */     SingleZipEntryCallback helper = new SingleZipEntryCallback(name, action);
/*  891 */     iterate(is, helper);
/*  892 */     return helper.found();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class SingleZipEntryCallback
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private final String name;
/*      */ 
/*      */     
/*      */     private final ZipEntryCallback action;
/*      */     
/*      */     private boolean found;
/*      */ 
/*      */     
/*      */     public SingleZipEntryCallback(String name, ZipEntryCallback action) {
/*  909 */       this.name = name;
/*  910 */       this.action = action;
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/*  914 */       if (this.name.equals(zipEntry.getName())) {
/*  915 */         this.found = true;
/*  916 */         this.action.process(in, zipEntry);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean found() {
/*  921 */       return this.found;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(File zip, File outputDir) {
/*  939 */     unpack(zip, outputDir, IdentityNameMapper.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(File zip, File outputDir, Charset charset) {
/*  958 */     unpack(zip, outputDir, IdentityNameMapper.INSTANCE, charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(File zip, File outputDir, NameMapper mapper, Charset charset) {
/*  976 */     log.debug("Extracting '{}' into '{}'.", zip, outputDir);
/*  977 */     iterate(zip, new Unpacker(outputDir, mapper), charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(File zip, File outputDir, NameMapper mapper) {
/*  995 */     log.debug("Extracting '{}' into '{}'.", zip, outputDir);
/*  996 */     iterate(zip, new Unpacker(outputDir, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unwrap(File zip, File outputDir) {
/* 1012 */     unwrap(zip, outputDir, IdentityNameMapper.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unwrap(File zip, File outputDir, NameMapper mapper) {
/* 1030 */     log.debug("Unwrapping '{}' into '{}'.", zip, outputDir);
/* 1031 */     iterate(zip, new Unwrapper(outputDir, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(InputStream is, File outputDir) {
/* 1045 */     unpack(is, outputDir, IdentityNameMapper.INSTANCE, (Charset)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(InputStream is, File outputDir, Charset charset) {
/* 1061 */     unpack(is, outputDir, IdentityNameMapper.INSTANCE, charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(InputStream is, File outputDir, NameMapper mapper) {
/* 1077 */     unpack(is, outputDir, mapper, (Charset)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unpack(InputStream is, File outputDir, NameMapper mapper, Charset charset) {
/* 1095 */     log.debug("Extracting {} into '{}'.", is, outputDir);
/* 1096 */     iterate(is, new Unpacker(outputDir, mapper), charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unwrap(InputStream is, File outputDir) {
/* 1112 */     unwrap(is, outputDir, IdentityNameMapper.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unwrap(InputStream is, File outputDir, NameMapper mapper) {
/* 1130 */     log.debug("Unwrapping {} into '{}'.", is, outputDir);
/* 1131 */     iterate(is, new Unwrapper(outputDir, mapper));
/*      */   }
/*      */   
/*      */   private static File makeDestinationFile(File outputDir, String name) throws IOException {
/* 1135 */     return checkDestinationFileForTraversal(outputDir, name, new File(outputDir, name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static File checkDestinationFileForTraversal(File outputDir, String name, File destFile) throws IOException {
/* 1143 */     if (name.indexOf("..") != -1 && !destFile.getCanonicalPath().startsWith(outputDir.getCanonicalPath())) {
/* 1144 */       throw new MaliciousZipException(outputDir, name);
/*      */     }
/* 1146 */     return destFile;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Unpacker
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private final File outputDir;
/*      */     
/*      */     private final NameMapper mapper;
/*      */ 
/*      */     
/*      */     public Unpacker(File outputDir, NameMapper mapper) {
/* 1160 */       this.outputDir = outputDir;
/* 1161 */       this.mapper = mapper;
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 1165 */       String name = this.mapper.map(zipEntry.getName());
/* 1166 */       if (name != null) {
/* 1167 */         File file = ZipUtil.makeDestinationFile(this.outputDir, name);
/*      */         
/* 1169 */         if (zipEntry.isDirectory()) {
/* 1170 */           FileUtils.forceMkdir(file);
/*      */         } else {
/*      */           
/* 1173 */           FileUtils.forceMkdir(file.getParentFile());
/*      */           
/* 1175 */           if (ZipUtil.log.isDebugEnabled() && file.exists()) {
/* 1176 */             ZipUtil.log.debug("Overwriting file '{}'.", zipEntry.getName());
/*      */           }
/*      */           
/* 1179 */           FileUtils.copy(in, file);
/*      */         } 
/*      */         
/*      */         try {
/* 1183 */           ZTFilePermissions permissions = ZipEntryUtil.getZTFilePermissions(zipEntry);
/* 1184 */           if (permissions != null) {
/* 1185 */             ZTFilePermissionsUtil.getDefaultStategy().setPermissions(file, permissions);
/*      */           }
/*      */         }
/* 1188 */         catch (ZipException zipException) {}
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BackslashUnpacker
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private final File outputDir;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final NameMapper mapper;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BackslashUnpacker(File outputDir, NameMapper mapper) {
/* 1213 */       this.outputDir = outputDir;
/* 1214 */       this.mapper = mapper;
/*      */     }
/*      */     
/*      */     public BackslashUnpacker(File outputDir) {
/* 1218 */       this(outputDir, IdentityNameMapper.INSTANCE);
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 1222 */       String name = this.mapper.map(zipEntry.getName());
/* 1223 */       if (name != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1234 */         if (name.indexOf('\\') != -1) {
/* 1235 */           File parentDirectory = this.outputDir;
/* 1236 */           String[] dirs = name.split("\\\\");
/*      */ 
/*      */           
/* 1239 */           for (int i = 0; i < dirs.length - 1; i++) {
/* 1240 */             File file = new File(parentDirectory, dirs[i]);
/* 1241 */             if (!file.exists()) {
/* 1242 */               FileUtils.forceMkdir(file);
/*      */             }
/* 1244 */             parentDirectory = file;
/*      */           } 
/* 1246 */           File destFile = ZipUtil.checkDestinationFileForTraversal(this.outputDir, name, new File(parentDirectory, dirs[dirs.length - 1]));
/*      */ 
/*      */           
/* 1249 */           FileUtils.copy(in, destFile);
/*      */         }
/*      */         else {
/*      */           
/* 1253 */           File destFile = ZipUtil.makeDestinationFile(this.outputDir, name);
/*      */           
/* 1255 */           FileUtils.copy(in, destFile);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Unwrapper
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private final File outputDir;
/*      */     
/*      */     private final NameMapper mapper;
/*      */     
/*      */     private String rootDir;
/*      */ 
/*      */     
/*      */     public Unwrapper(File outputDir, NameMapper mapper) {
/* 1274 */       this.outputDir = outputDir;
/* 1275 */       this.mapper = mapper;
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 1279 */       String root = getRootName(zipEntry.getName());
/* 1280 */       if (this.rootDir == null) {
/* 1281 */         this.rootDir = root;
/*      */       }
/* 1283 */       else if (!this.rootDir.equals(root)) {
/* 1284 */         throw new ZipException("Unwrapping with multiple roots is not supported, roots: " + this.rootDir + ", " + root);
/*      */       } 
/*      */       
/* 1287 */       String name = this.mapper.map(getUnrootedName(root, zipEntry.getName()));
/* 1288 */       if (name != null) {
/* 1289 */         File file = ZipUtil.makeDestinationFile(this.outputDir, name);
/*      */         
/* 1291 */         if (zipEntry.isDirectory()) {
/* 1292 */           FileUtils.forceMkdir(file);
/*      */         } else {
/*      */           
/* 1295 */           FileUtils.forceMkdir(file.getParentFile());
/*      */           
/* 1297 */           if (ZipUtil.log.isDebugEnabled() && file.exists()) {
/* 1298 */             ZipUtil.log.debug("Overwriting file '{}'.", zipEntry.getName());
/*      */           }
/*      */           
/* 1301 */           FileUtils.copy(in, file);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private String getUnrootedName(String root, String name) {
/* 1307 */       return name.substring(root.length());
/*      */     }
/*      */     
/*      */     private String getRootName(String name) {
/* 1311 */       String newName = name.substring(FilenameUtils.getPrefixLength(name));
/* 1312 */       int idx = newName.indexOf("/");
/* 1313 */       if (idx < 0) {
/* 1314 */         throw new ZipException("Entry " + newName + " from the root of the zip is not supported");
/*      */       }
/* 1316 */       return newName.substring(0, newName.indexOf("/"));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void explode(File zip) {
/*      */     try {
/* 1334 */       File tempFile = FileUtils.getTempFileFor(zip);
/*      */ 
/*      */       
/* 1337 */       FileUtils.moveFile(zip, tempFile);
/*      */ 
/*      */       
/* 1340 */       unpack(tempFile, zip);
/*      */ 
/*      */       
/* 1343 */       if (!tempFile.delete()) {
/* 1344 */         throw new IOException("Unable to delete file: " + tempFile);
/*      */       }
/*      */     }
/* 1347 */     catch (IOException e) {
/* 1348 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] packEntry(File file) {
/* 1361 */     log.trace("Compressing '{}' into a ZIP file with single entry.", file);
/*      */     
/* 1363 */     ByteArrayOutputStream result = new ByteArrayOutputStream();
/*      */     try {
/* 1365 */       ZipOutputStream out = new ZipOutputStream(result);
/* 1366 */       ZipEntry entry = ZipEntryUtil.fromFile(file.getName(), file);
/* 1367 */       InputStream in = new BufferedInputStream(new FileInputStream(file));
/*      */       try {
/* 1369 */         ZipEntryUtil.addEntry(entry, in, out);
/*      */       } finally {
/*      */         
/* 1372 */         IOUtils.closeQuietly(in);
/*      */       } 
/* 1374 */       out.close();
/*      */     }
/* 1376 */     catch (IOException e) {
/* 1377 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/* 1379 */     return result.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File rootDir, File zip) {
/* 1396 */     pack(rootDir, zip, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File rootDir, File zip, int compressionLevel) {
/* 1413 */     pack(rootDir, zip, IdentityNameMapper.INSTANCE, compressionLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, File targetZipFile, boolean preserveRoot) {
/* 1430 */     if (preserveRoot) {
/* 1431 */       final String parentName = sourceDir.getName();
/* 1432 */       pack(sourceDir, targetZipFile, new NameMapper() {
/*      */             public String map(String name) {
/* 1434 */               return parentName + "/" + name;
/*      */             }
/*      */           });
/*      */     } else {
/*      */       
/* 1439 */       pack(sourceDir, targetZipFile);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntry(File fileToPack, File destZipFile) {
/* 1454 */     packEntry(fileToPack, destZipFile, IdentityNameMapper.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntry(File fileToPack, File destZipFile, final String fileName) {
/* 1470 */     packEntry(fileToPack, destZipFile, new NameMapper() {
/*      */           public String map(String name) {
/* 1472 */             return fileName;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntry(File fileToPack, File destZipFile, NameMapper mapper) {
/* 1490 */     packEntries(new File[] { fileToPack }, destZipFile, mapper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntries(File[] filesToPack, File destZipFile) {
/* 1504 */     packEntries(filesToPack, destZipFile, IdentityNameMapper.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntries(File[] filesToPack, File destZipFile, NameMapper mapper) {
/* 1520 */     packEntries(filesToPack, destZipFile, mapper, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntries(File[] filesToPack, File destZipFile, int compressionLevel) {
/* 1537 */     packEntries(filesToPack, destZipFile, IdentityNameMapper.INSTANCE, compressionLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void packEntries(File[] filesToPack, File destZipFile, NameMapper mapper, int compressionLevel) {
/* 1556 */     log.debug("Compressing '{}' into '{}'.", filesToPack, destZipFile);
/*      */     
/* 1558 */     ZipOutputStream out = null;
/* 1559 */     FileOutputStream fos = null;
/*      */     try {
/* 1561 */       fos = new FileOutputStream(destZipFile);
/* 1562 */       out = new ZipOutputStream(new BufferedOutputStream(fos));
/* 1563 */       out.setLevel(compressionLevel);
/*      */       
/* 1565 */       for (int i = 0; i < filesToPack.length; i++) {
/* 1566 */         File fileToPack = filesToPack[i];
/*      */         
/* 1568 */         ZipEntry zipEntry = ZipEntryUtil.fromFile(mapper.map(fileToPack.getName()), fileToPack);
/* 1569 */         out.putNextEntry(zipEntry);
/* 1570 */         FileUtils.copy(fileToPack, out);
/* 1571 */         out.closeEntry();
/*      */       }
/*      */     
/* 1574 */     } catch (IOException e) {
/* 1575 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 1578 */       IOUtils.closeQuietly(out);
/* 1579 */       IOUtils.closeQuietly(fos);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, File targetZip, NameMapper mapper) {
/* 1596 */     pack(sourceDir, targetZip, mapper, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, File targetZip, NameMapper mapper, int compressionLevel) {
/* 1614 */     log.debug("Compressing '{}' into '{}'.", sourceDir, targetZip);
/* 1615 */     if (!sourceDir.exists()) {
/* 1616 */       throw new ZipException("Given file '" + sourceDir + "' doesn't exist!");
/*      */     }
/* 1618 */     ZipOutputStream out = null;
/*      */     try {
/* 1620 */       out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetZip)));
/* 1621 */       out.setLevel(compressionLevel);
/* 1622 */       pack(sourceDir, out, mapper, "", true);
/*      */     }
/* 1624 */     catch (IOException e) {
/* 1625 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 1628 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, OutputStream os) {
/* 1645 */     pack(sourceDir, os, IdentityNameMapper.INSTANCE, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, OutputStream os, int compressionLevel) {
/* 1663 */     pack(sourceDir, os, IdentityNameMapper.INSTANCE, compressionLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, OutputStream os, NameMapper mapper) {
/* 1681 */     pack(sourceDir, os, mapper, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(File sourceDir, OutputStream os, NameMapper mapper, int compressionLevel) {
/* 1701 */     log.debug("Compressing '{}' into a stream.", sourceDir);
/* 1702 */     if (!sourceDir.exists()) {
/* 1703 */       throw new ZipException("Given file '" + sourceDir + "' doesn't exist!");
/*      */     }
/* 1705 */     ZipOutputStream out = null;
/* 1706 */     IOException error = null;
/*      */     try {
/* 1708 */       out = new ZipOutputStream(new BufferedOutputStream(os));
/* 1709 */       out.setLevel(compressionLevel);
/* 1710 */       pack(sourceDir, out, mapper, "", true);
/*      */     }
/* 1712 */     catch (IOException e) {
/* 1713 */       error = e;
/*      */     } finally {
/*      */       
/* 1716 */       if (out != null && error == null) {
/*      */         try {
/* 1718 */           out.finish();
/* 1719 */           out.flush();
/*      */         }
/* 1721 */         catch (IOException e) {
/* 1722 */           error = e;
/*      */         } 
/*      */       }
/*      */     } 
/* 1726 */     if (error != null) {
/* 1727 */       throw ZipExceptionUtil.rethrow(error);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void pack(File dir, ZipOutputStream out, NameMapper mapper, String pathPrefix, boolean mustHaveChildren) throws IOException {
/* 1746 */     String[] filenames = dir.list();
/* 1747 */     if (filenames == null) {
/* 1748 */       if (!dir.exists()) {
/* 1749 */         throw new ZipException("Given file '" + dir + "' doesn't exist!");
/*      */       }
/* 1751 */       throw new IOException("Given file is not a directory '" + dir + "'");
/*      */     } 
/*      */     
/* 1754 */     if (mustHaveChildren && filenames.length == 0) {
/* 1755 */       throw new ZipException("Given directory '" + dir + "' doesn't contain any files!");
/*      */     }
/*      */     
/* 1758 */     for (int i = 0; i < filenames.length; i++) {
/* 1759 */       String filename = filenames[i];
/* 1760 */       File file = new File(dir, filename);
/* 1761 */       boolean isDir = file.isDirectory();
/* 1762 */       String path = pathPrefix + file.getName();
/* 1763 */       if (isDir) {
/* 1764 */         path = path + "/";
/*      */       }
/*      */ 
/*      */       
/* 1768 */       String name = mapper.map(path);
/* 1769 */       if (name != null) {
/* 1770 */         ZipEntry zipEntry = ZipEntryUtil.fromFile(name, file);
/*      */         
/* 1772 */         out.putNextEntry(zipEntry);
/*      */ 
/*      */         
/* 1775 */         if (!isDir) {
/* 1776 */           FileUtils.copy(file, out);
/*      */         }
/*      */         
/* 1779 */         out.closeEntry();
/*      */       } 
/*      */ 
/*      */       
/* 1783 */       if (isDir) {
/* 1784 */         pack(file, out, mapper, path, false);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void repack(File srcZip, File dstZip, int compressionLevel) {
/* 1802 */     log.debug("Repacking '{}' into '{}'.", srcZip, dstZip);
/*      */     
/* 1804 */     RepackZipEntryCallback callback = new RepackZipEntryCallback(dstZip, compressionLevel);
/*      */     
/*      */     try {
/* 1807 */       iterate(srcZip, callback);
/*      */     } finally {
/*      */       
/* 1810 */       callback.closeStream();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void repack(InputStream is, File dstZip, int compressionLevel) {
/* 1827 */     log.debug("Repacking from input stream into '{}'.", dstZip);
/*      */     
/* 1829 */     RepackZipEntryCallback callback = new RepackZipEntryCallback(dstZip, compressionLevel);
/*      */     
/*      */     try {
/* 1832 */       iterate(is, callback);
/*      */     } finally {
/*      */       
/* 1835 */       callback.closeStream();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void repack(File zip, int compressionLevel) {
/*      */     try {
/* 1850 */       File tmpZip = FileUtils.getTempFileFor(zip);
/*      */       
/* 1852 */       repack(zip, tmpZip, compressionLevel);
/*      */ 
/*      */       
/* 1855 */       if (!zip.delete()) {
/* 1856 */         throw new IOException("Unable to delete the file: " + zip);
/*      */       }
/*      */ 
/*      */       
/* 1860 */       FileUtils.moveFile(tmpZip, zip);
/*      */     }
/* 1862 */     catch (IOException e) {
/* 1863 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void createEmpty(File file) {
/* 1874 */     FileOutputStream fos = null;
/*      */     try {
/* 1876 */       fos = new FileOutputStream(file);
/* 1877 */       fos.write(new byte[] { 80, 75, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
/*      */     }
/* 1879 */     catch (IOException e) {
/* 1880 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 1883 */       IOUtils.closeQuietly(fos);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class RepackZipEntryCallback
/*      */     implements ZipEntryCallback
/*      */   {
/*      */     private ZipOutputStream out;
/*      */ 
/*      */ 
/*      */     
/*      */     private RepackZipEntryCallback(File dstZip, int compressionLevel) {
/*      */       try {
/* 1898 */         this.out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dstZip)));
/* 1899 */         this.out.setLevel(compressionLevel);
/*      */       }
/* 1901 */       catch (IOException e) {
/* 1902 */         ZipExceptionUtil.rethrow(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 1907 */       ZipEntryUtil.copyEntry(zipEntry, in, this.out);
/*      */     }
/*      */     
/*      */     private void closeStream() {
/* 1911 */       IOUtils.closeQuietly(this.out);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unexplode(File dir) {
/* 1928 */     unexplode(dir, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unexplode(File dir, int compressionLevel) {
/*      */     try {
/* 1948 */       File zip = FileUtils.getTempFileFor(dir);
/*      */ 
/*      */       
/* 1951 */       pack(dir, zip, compressionLevel);
/*      */ 
/*      */       
/* 1954 */       FileUtils.deleteDirectory(dir);
/*      */ 
/*      */       
/* 1957 */       FileUtils.moveFile(zip, dir);
/*      */     }
/* 1959 */     catch (IOException e) {
/* 1960 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(ZipEntrySource[] entries, OutputStream os) {
/* 1975 */     if (log.isDebugEnabled()) {
/* 1976 */       log.debug("Creating stream from {}.", Arrays.asList(entries));
/*      */     }
/* 1978 */     pack(entries, os, false);
/*      */   }
/*      */   
/*      */   private static void pack(ZipEntrySource[] entries, OutputStream os, boolean closeStream) {
/*      */     try {
/* 1983 */       ZipOutputStream out = new ZipOutputStream(os);
/* 1984 */       for (int i = 0; i < entries.length; i++) {
/* 1985 */         addEntry(entries[i], out);
/*      */       }
/* 1987 */       out.flush();
/* 1988 */       out.finish();
/* 1989 */       if (closeStream) {
/* 1990 */         out.close();
/*      */       }
/*      */     }
/* 1993 */     catch (IOException e) {
/* 1994 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void pack(ZipEntrySource[] entries, File zip) {
/* 2007 */     if (log.isDebugEnabled()) {
/* 2008 */       log.debug("Creating '{}' from {}.", zip, Arrays.asList(entries));
/*      */     }
/*      */     
/* 2011 */     OutputStream out = null;
/*      */     try {
/* 2013 */       out = new BufferedOutputStream(new FileOutputStream(zip));
/* 2014 */       pack(entries, out, true);
/*      */     }
/* 2016 */     catch (IOException e) {
/* 2017 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 2020 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(File zip, String path, File file, File destZip) {
/* 2037 */     addEntry(zip, new FileSource(path, file), destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(final File zip, final String path, final File file) {
/* 2051 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2053 */             ZipUtil.addEntry(zip, path, file, tmpFile);
/* 2054 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(File zip, String path, byte[] bytes, File destZip) {
/* 2072 */     addEntry(zip, new ByteSource(path, bytes), destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(File zip, String path, byte[] bytes, File destZip, int compressionMethod) {
/* 2090 */     addEntry(zip, new ByteSource(path, bytes, compressionMethod), destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(final File zip, final String path, final byte[] bytes) {
/* 2104 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2106 */             ZipUtil.addEntry(zip, path, bytes, tmpFile);
/* 2107 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(final File zip, final String path, final byte[] bytes, final int compressionMethod) {
/* 2125 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2127 */             ZipUtil.addEntry(zip, path, bytes, tmpFile, compressionMethod);
/* 2128 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(File zip, ZipEntrySource entry, File destZip) {
/* 2144 */     addEntries(zip, new ZipEntrySource[] { entry }, destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntry(final File zip, final ZipEntrySource entry) {
/* 2156 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2158 */             ZipUtil.addEntry(zip, entry, tmpFile);
/* 2159 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntries(File zip, ZipEntrySource[] entries, File destZip) {
/* 2175 */     if (log.isDebugEnabled()) {
/* 2176 */       log.debug("Copying '" + zip + "' to '" + destZip + "' and adding " + Arrays.<ZipEntrySource>asList(entries) + ".");
/*      */     }
/*      */     
/* 2179 */     OutputStream destOut = null;
/*      */     try {
/* 2181 */       destOut = new BufferedOutputStream(new FileOutputStream(destZip));
/* 2182 */       addEntries(zip, entries, destOut);
/*      */     }
/* 2184 */     catch (IOException e) {
/* 2185 */       ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 2188 */       IOUtils.closeQuietly(destOut);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntries(File zip, ZipEntrySource[] entries, OutputStream destOut) {
/* 2203 */     if (log.isDebugEnabled()) {
/* 2204 */       log.debug("Copying '" + zip + "' to a stream and adding " + Arrays.<ZipEntrySource>asList(entries) + ".");
/*      */     }
/*      */     
/* 2207 */     ZipOutputStream out = null;
/*      */     try {
/* 2209 */       out = new ZipOutputStream(destOut);
/* 2210 */       copyEntries(zip, out);
/* 2211 */       for (int i = 0; i < entries.length; i++) {
/* 2212 */         addEntry(entries[i], out);
/*      */       }
/* 2214 */       out.finish();
/*      */     }
/* 2216 */     catch (IOException e) {
/* 2217 */       ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntries(InputStream is, ZipEntrySource[] entries, OutputStream destOut) {
/* 2234 */     if (log.isDebugEnabled()) {
/* 2235 */       log.debug("Copying input stream to an output stream and adding " + Arrays.<ZipEntrySource>asList(entries) + ".");
/*      */     }
/*      */     
/* 2238 */     ZipOutputStream out = null;
/*      */     try {
/* 2240 */       out = new ZipOutputStream(destOut);
/* 2241 */       copyEntries(is, out);
/* 2242 */       for (int i = 0; i < entries.length; i++) {
/* 2243 */         addEntry(entries[i], out);
/*      */       }
/* 2245 */       out.finish();
/*      */     }
/* 2247 */     catch (IOException e) {
/* 2248 */       ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addEntries(final File zip, final ZipEntrySource[] entries) {
/* 2261 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2263 */             ZipUtil.addEntries(zip, entries, tmpFile);
/* 2264 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeEntry(File zip, String path, File destZip) {
/* 2281 */     removeEntries(zip, new String[] { path }, destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeEntry(final File zip, final String path) {
/* 2294 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2296 */             ZipUtil.removeEntry(zip, path, tmpFile);
/* 2297 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeEntries(File zip, String[] paths, File destZip) {
/* 2314 */     if (log.isDebugEnabled()) {
/* 2315 */       log.debug("Copying '" + zip + "' to '" + destZip + "' and removing paths " + Arrays.<String>asList(paths) + ".");
/*      */     }
/*      */     
/* 2318 */     ZipOutputStream out = null;
/*      */     try {
/* 2320 */       out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
/* 2321 */       copyEntries(zip, out, new HashSet<String>(Arrays.asList(paths)));
/*      */     }
/* 2323 */     catch (IOException e) {
/* 2324 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 2327 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeEntries(File zip, String[] paths, OutputStream destOut) {
/* 2343 */     if (log.isDebugEnabled()) {
/* 2344 */       log.debug("Copying '" + zip + "' to an output stream and removing paths " + Arrays.<String>asList(paths) + ".");
/*      */     }
/*      */     
/* 2347 */     ZipOutputStream out = null;
/*      */     try {
/* 2349 */       out = new ZipOutputStream(destOut);
/* 2350 */       copyEntries(zip, out, new HashSet<String>(Arrays.asList(paths)));
/*      */     } finally {
/*      */       
/* 2353 */       IOUtils.closeQuietly(out);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void removeEntries(final File zip, final String[] paths) {
/* 2367 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2369 */             ZipUtil.removeEntries(zip, paths, tmpFile);
/* 2370 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void copyEntries(File zip, final ZipOutputStream out) {
/* 2385 */     final Set<String> names = new HashSet<String>();
/* 2386 */     iterate(zip, new ZipEntryCallback() {
/*      */           public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 2388 */             String entryName = zipEntry.getName();
/* 2389 */             if (names.add(entryName)) {
/* 2390 */               ZipEntryUtil.copyEntry(zipEntry, in, out);
/*      */             }
/* 2392 */             else if (ZipUtil.log.isDebugEnabled()) {
/* 2393 */               ZipUtil.log.debug("Duplicate entry: {}", entryName);
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void copyEntries(InputStream is, final ZipOutputStream out) {
/* 2409 */     final Set<String> names = new HashSet<String>();
/* 2410 */     iterate(is, new ZipEntryCallback() {
/*      */           public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 2412 */             String entryName = zipEntry.getName();
/* 2413 */             if (names.add(entryName)) {
/* 2414 */               ZipEntryUtil.copyEntry(zipEntry, in, out);
/*      */             }
/* 2416 */             else if (ZipUtil.log.isDebugEnabled()) {
/* 2417 */               ZipUtil.log.debug("Duplicate entry: {}", entryName);
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void copyEntries(File zip, final ZipOutputStream out, final Set<String> ignoredEntries) {
/* 2434 */     final Set<String> names = new HashSet<String>();
/* 2435 */     final Set<String> dirNames = filterDirEntries(zip, ignoredEntries);
/* 2436 */     iterate(zip, new ZipEntryCallback() {
/*      */           public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 2438 */             String entryName = zipEntry.getName();
/* 2439 */             if (ignoredEntries.contains(entryName)) {
/*      */               return;
/*      */             }
/*      */             
/* 2443 */             for (String dirName : dirNames) {
/* 2444 */               if (entryName.startsWith(dirName)) {
/*      */                 return;
/*      */               }
/*      */             } 
/*      */             
/* 2449 */             if (names.add(entryName)) {
/* 2450 */               ZipEntryUtil.copyEntry(zipEntry, in, out);
/*      */             }
/* 2452 */             else if (ZipUtil.log.isDebugEnabled()) {
/* 2453 */               ZipUtil.log.debug("Duplicate entry: {}", entryName);
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Set<String> filterDirEntries(File zip, Collection<String> names) {
/* 2469 */     Set<String> dirs = new HashSet<String>();
/* 2470 */     if (zip == null) {
/* 2471 */       return dirs;
/*      */     }
/* 2473 */     ZipFile zf = null;
/*      */     try {
/* 2475 */       zf = new ZipFile(zip);
/* 2476 */       for (String entryName : names) {
/* 2477 */         ZipEntry entry = zf.getEntry(entryName);
/* 2478 */         if (entry != null) {
/* 2479 */           if (entry.isDirectory()) {
/* 2480 */             dirs.add(entry.getName()); continue;
/*      */           } 
/* 2482 */           if (zf.getInputStream(entry) == null)
/*      */           {
/* 2484 */             dirs.add(entry.getName() + "/");
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 2490 */     } catch (IOException e) {
/* 2491 */       ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 2494 */       closeQuietly(zf);
/*      */     } 
/* 2496 */     return dirs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(File zip, String path, File file, File destZip) {
/* 2513 */     return replaceEntry(zip, new FileSource(path, file), destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(final File zip, final String path, final File file) {
/* 2528 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2530 */             return ZipUtil.replaceEntry(zip, new FileSource(path, file), tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(File zip, String path, byte[] bytes, File destZip) {
/* 2549 */     return replaceEntry(zip, new ByteSource(path, bytes), destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(final File zip, final String path, final byte[] bytes) {
/* 2564 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2566 */             return ZipUtil.replaceEntry(zip, new ByteSource(path, bytes), tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(final File zip, final String path, final byte[] bytes, final int compressionMethod) {
/* 2586 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2588 */             return ZipUtil.replaceEntry(zip, new ByteSource(path, bytes, compressionMethod), tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(File zip, ZipEntrySource entry, File destZip) {
/* 2605 */     return replaceEntries(zip, new ZipEntrySource[] { entry }, destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntry(final File zip, final ZipEntrySource entry) {
/* 2618 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2620 */             return ZipUtil.replaceEntry(zip, entry, tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntries(File zip, ZipEntrySource[] entries, File destZip) {
/* 2637 */     if (log.isDebugEnabled()) {
/* 2638 */       log.debug("Copying '" + zip + "' to '" + destZip + "' and replacing entries " + Arrays.<ZipEntrySource>asList(entries) + ".");
/*      */     }
/*      */     
/* 2641 */     final Map<String, ZipEntrySource> entryByPath = entriesByPath(entries);
/* 2642 */     int entryCount = entryByPath.size();
/*      */     try {
/* 2644 */       final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
/*      */       try {
/* 2646 */         final Set<String> names = new HashSet<String>();
/* 2647 */         iterate(zip, new ZipEntryCallback() {
/*      */               public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 2649 */                 if (names.add(zipEntry.getName())) {
/* 2650 */                   ZipEntrySource entry = (ZipEntrySource)entryByPath.remove(zipEntry.getName());
/* 2651 */                   if (entry != null) {
/* 2652 */                     ZipUtil.addEntry(entry, out);
/*      */                   } else {
/*      */                     
/* 2655 */                     ZipEntryUtil.copyEntry(zipEntry, in, out);
/*      */                   }
/*      */                 
/* 2658 */                 } else if (ZipUtil.log.isDebugEnabled()) {
/* 2659 */                   ZipUtil.log.debug("Duplicate entry: {}", zipEntry.getName());
/*      */                 } 
/*      */               }
/*      */             });
/*      */       } finally {
/*      */         
/* 2665 */         IOUtils.closeQuietly(out);
/*      */       }
/*      */     
/* 2668 */     } catch (IOException e) {
/* 2669 */       ZipExceptionUtil.rethrow(e);
/*      */     } 
/* 2671 */     return (entryByPath.size() < entryCount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean replaceEntries(final File zip, final ZipEntrySource[] entries) {
/* 2684 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2686 */             return ZipUtil.replaceEntries(zip, entries, tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addOrReplaceEntries(File zip, ZipEntrySource[] entries, File destZip) {
/* 2702 */     if (log.isDebugEnabled()) {
/* 2703 */       log.debug("Copying '" + zip + "' to '" + destZip + "' and adding/replacing entries " + Arrays.<ZipEntrySource>asList(entries) + ".");
/*      */     }
/*      */ 
/*      */     
/* 2707 */     final Map<String, ZipEntrySource> entryByPath = entriesByPath(entries);
/*      */     try {
/* 2709 */       final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
/*      */       
/*      */       try {
/* 2712 */         final Set<String> names = new HashSet<String>();
/* 2713 */         iterate(zip, new ZipEntryCallback() {
/*      */               public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 2715 */                 if (names.add(zipEntry.getName())) {
/* 2716 */                   ZipEntrySource entry = (ZipEntrySource)entryByPath.remove(zipEntry.getName());
/* 2717 */                   if (entry != null) {
/* 2718 */                     ZipUtil.addEntry(entry, out);
/*      */                   } else {
/*      */                     
/* 2721 */                     ZipEntryUtil.copyEntry(zipEntry, in, out);
/*      */                   }
/*      */                 
/* 2724 */                 } else if (ZipUtil.log.isDebugEnabled()) {
/* 2725 */                   ZipUtil.log.debug("Duplicate entry: {}", zipEntry.getName());
/*      */                 } 
/*      */               }
/*      */             });
/*      */ 
/*      */         
/* 2731 */         for (ZipEntrySource zipEntrySource : entryByPath.values()) {
/* 2732 */           addEntry(zipEntrySource, out);
/*      */         }
/*      */       } finally {
/*      */         
/* 2736 */         IOUtils.closeQuietly(out);
/*      */       }
/*      */     
/* 2739 */     } catch (IOException e) {
/* 2740 */       ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addOrReplaceEntries(final File zip, final ZipEntrySource[] entries) {
/* 2753 */     operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2755 */             ZipUtil.addOrReplaceEntries(zip, entries, tmpFile);
/* 2756 */             return true;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Map<String, ZipEntrySource> entriesByPath(ZipEntrySource... entries) {
/* 2765 */     Map<String, ZipEntrySource> result = new HashMap<String, ZipEntrySource>();
/* 2766 */     for (int i = 0; i < entries.length; i++) {
/* 2767 */       ZipEntrySource source = entries[i];
/* 2768 */       result.put(source.getPath(), source);
/*      */     } 
/* 2770 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntry(File zip, String path, ZipEntryTransformer transformer, File destZip) {
/* 2788 */     if (zip.equals(destZip)) throw new IllegalArgumentException("Input (" + zip.getAbsolutePath() + ") is the same as the destination!Please use the transformEntry method without destination for in-place transformation.");
/*      */     
/* 2790 */     return transformEntry(zip, new ZipEntryTransformerEntry(path, transformer), destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntry(final File zip, final String path, final ZipEntryTransformer transformer) {
/* 2805 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2807 */             return ZipUtil.transformEntry(zip, path, transformer, tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntry(File zip, ZipEntryTransformerEntry entry, File destZip) {
/* 2824 */     return transformEntries(zip, new ZipEntryTransformerEntry[] { entry }, destZip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntry(final File zip, final ZipEntryTransformerEntry entry) {
/* 2837 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2839 */             return ZipUtil.transformEntry(zip, entry, tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntries(File zip, ZipEntryTransformerEntry[] entries, File destZip) {
/* 2856 */     if (log.isDebugEnabled()) {
/* 2857 */       log.debug("Copying '" + zip + "' to '" + destZip + "' and transforming entries " + Arrays.<ZipEntryTransformerEntry>asList(entries) + ".");
/*      */     }
/*      */     try {
/* 2860 */       ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
/*      */       try {
/* 2862 */         TransformerZipEntryCallback action = new TransformerZipEntryCallback(Arrays.asList(entries), out);
/* 2863 */         iterate(zip, action);
/* 2864 */         return action.found();
/*      */       } finally {
/*      */         
/* 2867 */         IOUtils.closeQuietly(out);
/*      */       }
/*      */     
/* 2870 */     } catch (IOException e) {
/* 2871 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntries(final File zip, final ZipEntryTransformerEntry[] entries) {
/* 2885 */     return operateInPlace(zip, new InPlaceAction() {
/*      */           public boolean act(File tmpFile) {
/* 2887 */             return ZipUtil.transformEntries(zip, entries, tmpFile);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntry(InputStream is, String path, ZipEntryTransformer transformer, OutputStream os) {
/* 2906 */     return transformEntry(is, new ZipEntryTransformerEntry(path, transformer), os);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntry(InputStream is, ZipEntryTransformerEntry entry, OutputStream os) {
/* 2921 */     return transformEntries(is, new ZipEntryTransformerEntry[] { entry }, os);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean transformEntries(InputStream is, ZipEntryTransformerEntry[] entries, OutputStream os) {
/* 2936 */     if (log.isDebugEnabled()) {
/* 2937 */       log.debug("Copying '" + is + "' to '" + os + "' and transforming entries " + Arrays.<ZipEntryTransformerEntry>asList(entries) + ".");
/*      */     }
/*      */     try {
/* 2940 */       ZipOutputStream out = new ZipOutputStream(os);
/* 2941 */       TransformerZipEntryCallback action = new TransformerZipEntryCallback(Arrays.asList(entries), out);
/* 2942 */       iterate(is, action);
/*      */ 
/*      */       
/* 2945 */       out.finish();
/* 2946 */       return action.found();
/*      */     }
/* 2948 */     catch (IOException e) {
/* 2949 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static class TransformerZipEntryCallback
/*      */     implements ZipEntryCallback {
/*      */     private final Map<String, ZipEntryTransformer> entryByPath;
/*      */     private final int entryCount;
/*      */     private final ZipOutputStream out;
/* 2958 */     private final Set<String> names = new HashSet<String>();
/*      */     
/*      */     public TransformerZipEntryCallback(List<ZipEntryTransformerEntry> entries, ZipOutputStream out) {
/* 2961 */       this.entryByPath = ZipUtil.transformersByPath(entries);
/* 2962 */       this.entryCount = this.entryByPath.size();
/* 2963 */       this.out = out;
/*      */     }
/*      */     
/*      */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 2967 */       if (this.names.add(zipEntry.getName())) {
/* 2968 */         ZipEntryTransformer entry = this.entryByPath.remove(zipEntry.getName());
/* 2969 */         if (entry != null) {
/* 2970 */           entry.transform(in, zipEntry, this.out);
/*      */         } else {
/*      */           
/* 2973 */           ZipEntryUtil.copyEntry(zipEntry, in, this.out);
/*      */         }
/*      */       
/* 2976 */       } else if (ZipUtil.log.isDebugEnabled()) {
/* 2977 */         ZipUtil.log.debug("Duplicate entry: {}", zipEntry.getName());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean found() {
/* 2985 */       return (this.entryByPath.size() < this.entryCount);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Map<String, ZipEntryTransformer> transformersByPath(List<ZipEntryTransformerEntry> entries) {
/* 2994 */     Map<String, ZipEntryTransformer> result = new HashMap<String, ZipEntryTransformer>();
/* 2995 */     for (ZipEntryTransformerEntry entry : entries) {
/* 2996 */       result.put(entry.getPath(), entry.getTransformer());
/*      */     }
/* 2998 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addEntry(ZipEntrySource entry, ZipOutputStream out) throws IOException {
/* 3010 */     out.putNextEntry(entry.getEntry());
/* 3011 */     InputStream in = entry.getInputStream();
/* 3012 */     if (in != null) {
/*      */       try {
/* 3014 */         IOUtils.copy(in, out);
/*      */       } finally {
/*      */         
/* 3017 */         IOUtils.closeQuietly(in);
/*      */       } 
/*      */     }
/* 3020 */     out.closeEntry();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean archiveEquals(File f1, File f2) {
/*      */     try {
/* 3061 */       if (FileUtils.contentEquals(f1, f2)) {
/* 3062 */         return true;
/*      */       }
/*      */       
/* 3065 */       log.debug("Comparing archives '{}' and '{}'...", f1, f2);
/*      */       
/* 3067 */       long start = System.currentTimeMillis();
/* 3068 */       boolean result = archiveEqualsInternal(f1, f2);
/* 3069 */       long time = System.currentTimeMillis() - start;
/* 3070 */       if (time > 0L) {
/* 3071 */         log.debug("Archives compared in " + time + " ms.");
/*      */       }
/* 3073 */       return result;
/*      */     }
/* 3075 */     catch (Exception e) {
/* 3076 */       log.debug("Could not compare '" + f1 + "' and '" + f2 + "':", e);
/* 3077 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean archiveEqualsInternal(File f1, File f2) throws IOException {
/* 3082 */     ZipFile zf1 = null;
/* 3083 */     ZipFile zf2 = null;
/*      */     try {
/* 3085 */       zf1 = new ZipFile(f1);
/* 3086 */       zf2 = new ZipFile(f2);
/*      */ 
/*      */       
/* 3089 */       if (zf1.size() != zf2.size()) {
/* 3090 */         log.debug("Number of entries changed (" + zf1.size() + " vs " + zf2.size() + ").");
/* 3091 */         return false;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3104 */       Enumeration<? extends ZipEntry> en = zf1.entries();
/* 3105 */       while (en.hasMoreElements()) {
/* 3106 */         ZipEntry e1 = en.nextElement();
/* 3107 */         String path = e1.getName();
/* 3108 */         ZipEntry e2 = zf2.getEntry(path);
/*      */ 
/*      */         
/* 3111 */         if (!metaDataEquals(path, e1, e2)) {
/* 3112 */           return false;
/*      */         }
/*      */ 
/*      */         
/* 3116 */         InputStream is1 = null;
/* 3117 */         InputStream is2 = null;
/*      */         try {
/* 3119 */           is1 = zf1.getInputStream(e1);
/* 3120 */           is2 = zf2.getInputStream(e2);
/*      */           
/* 3122 */           if (!IOUtils.contentEquals(is1, is2)) {
/* 3123 */             log.debug("Entry '{}' content changed.", path);
/* 3124 */             return false;
/*      */           } 
/*      */         } finally {
/*      */           
/* 3128 */           IOUtils.closeQuietly(is1);
/* 3129 */           IOUtils.closeQuietly(is2);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*      */       
/* 3134 */       closeQuietly(zf1);
/* 3135 */       closeQuietly(zf2);
/*      */     } 
/*      */     
/* 3138 */     log.debug("Archives are the same.");
/*      */     
/* 3140 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean metaDataEquals(String path, ZipEntry e1, ZipEntry e2) throws IOException {
/* 3164 */     if (e2 == null) {
/* 3165 */       log.debug("Entry '{}' removed.", path);
/* 3166 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3170 */     if (e1.isDirectory()) {
/* 3171 */       if (e2.isDirectory()) {
/* 3172 */         return true;
/*      */       }
/*      */       
/* 3175 */       log.debug("Entry '{}' not a directory any more.", path);
/* 3176 */       return false;
/*      */     } 
/*      */     
/* 3179 */     if (e2.isDirectory()) {
/* 3180 */       log.debug("Entry '{}' now a directory.", path);
/* 3181 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3185 */     long size1 = e1.getSize();
/* 3186 */     long size2 = e2.getSize();
/* 3187 */     if (size1 != -1L && size2 != -1L && size1 != size2) {
/* 3188 */       log.debug("Entry '" + path + "' size changed (" + size1 + " vs " + size2 + ").");
/* 3189 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3193 */     long crc1 = e1.getCrc();
/* 3194 */     long crc2 = e2.getCrc();
/* 3195 */     if (crc1 != -1L && crc2 != -1L && crc1 != crc2) {
/* 3196 */       log.debug("Entry '" + path + "' CRC changed (" + crc1 + " vs " + crc2 + ").");
/* 3197 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3201 */     if (log.isTraceEnabled()) {
/* 3202 */       long time1 = e1.getTime();
/* 3203 */       long time2 = e2.getTime();
/* 3204 */       if (time1 != -1L && time2 != -1L && time1 != time2) {
/* 3205 */         log.trace("Entry '" + path + "' time changed (" + new Date(time1) + " vs " + new Date(time2) + ").");
/*      */       }
/*      */     } 
/*      */     
/* 3209 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean entryEquals(File f1, File f2, String path) {
/* 3225 */     return entryEquals(f1, f2, path, path);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean entryEquals(File f1, File f2, String path1, String path2) {
/* 3242 */     ZipFile zf1 = null;
/* 3243 */     ZipFile zf2 = null;
/*      */     
/*      */     try {
/* 3246 */       zf1 = new ZipFile(f1);
/* 3247 */       zf2 = new ZipFile(f2);
/*      */       
/* 3249 */       return doEntryEquals(zf1, zf2, path1, path2);
/*      */     }
/* 3251 */     catch (IOException e) {
/* 3252 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 3255 */       closeQuietly(zf1);
/* 3256 */       closeQuietly(zf2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean entryEquals(ZipFile zf1, ZipFile zf2, String path1, String path2) {
/*      */     try {
/* 3275 */       return doEntryEquals(zf1, zf2, path1, path2);
/*      */     }
/* 3277 */     catch (IOException e) {
/* 3278 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doEntryEquals(ZipFile zf1, ZipFile zf2, String path1, String path2) throws IOException {
/* 3296 */     InputStream is1 = null;
/* 3297 */     InputStream is2 = null;
/*      */     try {
/* 3299 */       ZipEntry e1 = zf1.getEntry(path1);
/* 3300 */       ZipEntry e2 = zf2.getEntry(path2);
/*      */       
/* 3302 */       if (e1 == null && e2 == null) {
/* 3303 */         return true;
/*      */       }
/*      */       
/* 3306 */       if (e1 == null || e2 == null) {
/* 3307 */         return false;
/*      */       }
/*      */       
/* 3310 */       is1 = zf1.getInputStream(e1);
/* 3311 */       is2 = zf2.getInputStream(e2);
/* 3312 */       if (is1 == null && is2 == null) {
/* 3313 */         return true;
/*      */       }
/* 3315 */       if (is1 == null || is2 == null) {
/* 3316 */         return false;
/*      */       }
/*      */       
/* 3319 */       return IOUtils.contentEquals(is1, is2);
/*      */     } finally {
/*      */       
/* 3322 */       IOUtils.closeQuietly(is1);
/* 3323 */       IOUtils.closeQuietly(is2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(ZipFile zf) {
/*      */     try {
/* 3335 */       if (zf != null) {
/* 3336 */         zf.close();
/*      */       }
/*      */     }
/* 3339 */     catch (IOException iOException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class InPlaceAction
/*      */   {
/*      */     private InPlaceAction() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract boolean act(File param1File);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean operateInPlace(File src, InPlaceAction action) {
/* 3368 */     File tmp = null;
/*      */     try {
/* 3370 */       tmp = File.createTempFile("zt-zip-tmp", ".zip");
/* 3371 */       boolean result = action.act(tmp);
/* 3372 */       if (result) {
/* 3373 */         FileUtils.forceDelete(src);
/* 3374 */         FileUtils.moveFile(tmp, src);
/*      */       } 
/* 3376 */       return result;
/*      */     }
/* 3378 */     catch (IOException e) {
/* 3379 */       throw ZipExceptionUtil.rethrow(e);
/*      */     } finally {
/*      */       
/* 3382 */       FileUtils.deleteQuietly(tmp);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
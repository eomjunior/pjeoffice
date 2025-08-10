/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public final class RandomAccessSourceFactory
/*     */ {
/*     */   private boolean forceRead = false;
/*     */   private boolean usePlainRandomAccess = false;
/*     */   private boolean exclusivelyLockFile = false;
/*     */   
/*     */   public RandomAccessSourceFactory setForceRead(boolean forceRead) {
/*  89 */     this.forceRead = forceRead;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessSourceFactory setUsePlainRandomAccess(boolean usePlainRandomAccess) {
/*  99 */     this.usePlainRandomAccess = usePlainRandomAccess;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public RandomAccessSourceFactory setExclusivelyLockFile(boolean exclusivelyLockFile) {
/* 104 */     this.exclusivelyLockFile = exclusivelyLockFile;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessSource createSource(byte[] data) {
/* 114 */     return new ArrayRandomAccessSource(data);
/*     */   }
/*     */   
/*     */   public RandomAccessSource createSource(RandomAccessFile raf) throws IOException {
/* 118 */     return new RAFRandomAccessSource(raf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessSource createSource(URL url) throws IOException {
/* 128 */     InputStream is = url.openStream();
/*     */     try {
/* 130 */       return createSource(is);
/*     */     } finally {
/*     */       
/* 133 */       try { is.close(); } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessSource createSource(InputStream is) throws IOException {
/*     */     try {
/* 145 */       return createSource(StreamUtil.inputStreamToArray(is));
/*     */     } finally {
/*     */       
/* 148 */       try { is.close(); } catch (IOException iOException) {}
/*     */     } 
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
/*     */   public RandomAccessSource createBestSource(String filename) throws IOException {
/* 161 */     File file = new File(filename);
/* 162 */     if (!file.canRead()) {
/* 163 */       if (filename.startsWith("file:/") || filename
/* 164 */         .startsWith("http://") || filename
/* 165 */         .startsWith("https://") || filename
/* 166 */         .startsWith("jar:") || filename
/* 167 */         .startsWith("wsjar:") || filename
/* 168 */         .startsWith("wsjar:") || filename
/* 169 */         .startsWith("vfszip:")) {
/* 170 */         return createSource(new URL(filename));
/*     */       }
/* 172 */       return createByReadingToMemory(filename);
/*     */     } 
/*     */ 
/*     */     
/* 176 */     if (this.forceRead) {
/* 177 */       return createByReadingToMemory(new FileInputStream(filename));
/*     */     }
/*     */     
/* 180 */     String openMode = this.exclusivelyLockFile ? "rw" : "r";
/*     */     
/* 182 */     RandomAccessFile raf = new RandomAccessFile(file, openMode);
/*     */     
/* 184 */     if (this.exclusivelyLockFile) {
/* 185 */       raf.getChannel().lock();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 190 */       return createBestSource(raf);
/* 191 */     } catch (IOException e) {
/*     */       try {
/* 193 */         raf.close();
/* 194 */       } catch (IOException iOException) {}
/* 195 */       throw e;
/* 196 */     } catch (RuntimeException e) {
/*     */       try {
/* 198 */         raf.close();
/* 199 */       } catch (IOException iOException) {}
/* 200 */       throw e;
/*     */     } 
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
/*     */   public RandomAccessSource createBestSource(RandomAccessFile raf) throws IOException {
/* 214 */     if (this.usePlainRandomAccess) {
/* 215 */       return new RAFRandomAccessSource(raf);
/*     */     }
/*     */     
/* 218 */     if (raf.length() <= 0L) {
/* 219 */       return new RAFRandomAccessSource(raf);
/*     */     }
/*     */     
/*     */     try {
/* 223 */       return createBestSource(raf.getChannel());
/* 224 */     } catch (MapFailedException e) {
/* 225 */       return new RAFRandomAccessSource(raf);
/*     */     } 
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
/*     */   public RandomAccessSource createBestSource(FileChannel channel) throws IOException {
/* 238 */     if (channel.size() <= 67108864L) {
/* 239 */       return new GetBufferedRandomAccessSource(new FileChannelRandomAccessSource(channel));
/*     */     }
/* 241 */     return new GetBufferedRandomAccessSource(new PagedChannelRandomAccessSource(channel));
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomAccessSource createRanged(RandomAccessSource source, long[] ranges) throws IOException {
/* 246 */     RandomAccessSource[] sources = new RandomAccessSource[ranges.length / 2];
/* 247 */     for (int i = 0; i < ranges.length; i += 2) {
/* 248 */       sources[i / 2] = new WindowRandomAccessSource(source, ranges[i], ranges[i + 1]);
/*     */     }
/* 250 */     return new GroupedRandomAccessSource(sources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RandomAccessSource createByReadingToMemory(String filename) throws IOException {
/* 260 */     InputStream is = StreamUtil.getResourceStream(filename);
/* 261 */     if (is == null)
/* 262 */       throw new IOException(MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", new Object[] { filename })); 
/* 263 */     return createByReadingToMemory(is);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RandomAccessSource createByReadingToMemory(InputStream is) throws IOException {
/*     */     try {
/* 274 */       return new ArrayRandomAccessSource(StreamUtil.inputStreamToArray(is));
/*     */     } finally {
/*     */       
/* 277 */       try { is.close(); } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/RandomAccessSourceFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
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
/*     */ public class MappedRandomAccessFile
/*     */ {
/*     */   private static final int BUFSIZE = 1073741824;
/*  66 */   private FileChannel channel = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private MappedByteBuffer[] mappedBuffers;
/*     */ 
/*     */   
/*     */   private long size;
/*     */ 
/*     */   
/*     */   private long pos;
/*     */ 
/*     */ 
/*     */   
/*     */   public MappedRandomAccessFile(String filename, String mode) throws FileNotFoundException, IOException {
/*  81 */     if (mode.equals("rw")) {
/*  82 */       init((new RandomAccessFile(filename, mode))
/*  83 */           .getChannel(), FileChannel.MapMode.READ_WRITE);
/*     */     } else {
/*     */       
/*  86 */       init((new FileInputStream(filename))
/*  87 */           .getChannel(), FileChannel.MapMode.READ_ONLY);
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
/*     */   private void init(FileChannel channel, FileChannel.MapMode mapMode) throws IOException {
/* 101 */     this.channel = channel;
/*     */ 
/*     */     
/* 104 */     this.size = channel.size();
/* 105 */     this.pos = 0L;
/* 106 */     int requiredBuffers = (int)(this.size / 1073741824L) + ((this.size % 1073741824L == 0L) ? 0 : 1);
/*     */ 
/*     */     
/* 109 */     this.mappedBuffers = new MappedByteBuffer[requiredBuffers];
/*     */     try {
/* 111 */       int index = 0; long offset;
/* 112 */       for (offset = 0L; offset < this.size; offset += 1073741824L) {
/* 113 */         long size2 = Math.min(this.size - offset, 1073741824L);
/* 114 */         this.mappedBuffers[index] = channel.map(mapMode, offset, size2);
/* 115 */         this.mappedBuffers[index].load();
/* 116 */         index++;
/*     */       } 
/* 118 */       if (index != requiredBuffers) {
/* 119 */         throw new Error("Should never happen - " + index + " != " + requiredBuffers);
/*     */       }
/* 121 */     } catch (IOException e) {
/* 122 */       close();
/* 123 */       throw e;
/* 124 */     } catch (RuntimeException e) {
/* 125 */       close();
/* 126 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileChannel getChannel() {
/* 135 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() {
/*     */     try {
/* 144 */       int mapN = (int)(this.pos / 1073741824L);
/* 145 */       int offN = (int)(this.pos % 1073741824L);
/*     */       
/* 147 */       if (mapN >= this.mappedBuffers.length) {
/* 148 */         return -1;
/*     */       }
/* 150 */       if (offN >= this.mappedBuffers[mapN].limit()) {
/* 151 */         return -1;
/*     */       }
/* 153 */       byte b = this.mappedBuffers[mapN].get(offN);
/* 154 */       this.pos++;
/* 155 */       int n = b & 0xFF;
/*     */       
/* 157 */       return n;
/* 158 */     } catch (BufferUnderflowException e) {
/* 159 */       return -1;
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
/*     */   public int read(byte[] bytes, int off, int len) {
/* 171 */     int mapN = (int)(this.pos / 1073741824L);
/* 172 */     int offN = (int)(this.pos % 1073741824L);
/* 173 */     int totalRead = 0;
/*     */     
/* 175 */     while (totalRead < len && 
/* 176 */       mapN < this.mappedBuffers.length) {
/*     */       
/* 178 */       MappedByteBuffer currentBuffer = this.mappedBuffers[mapN];
/* 179 */       if (offN > currentBuffer.limit())
/*     */         break; 
/* 181 */       currentBuffer.position(offN);
/* 182 */       int bytesFromThisBuffer = Math.min(len - totalRead, currentBuffer.remaining());
/* 183 */       currentBuffer.get(bytes, off, bytesFromThisBuffer);
/* 184 */       off += bytesFromThisBuffer;
/* 185 */       this.pos += bytesFromThisBuffer;
/* 186 */       totalRead += bytesFromThisBuffer;
/*     */       
/* 188 */       mapN++;
/* 189 */       offN = 0;
/*     */     } 
/*     */     
/* 192 */     return (totalRead == 0) ? -1 : totalRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFilePointer() {
/* 200 */     return this.pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void seek(long pos) {
/* 208 */     this.pos = pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/* 216 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 224 */     for (int i = 0; i < this.mappedBuffers.length; i++) {
/* 225 */       if (this.mappedBuffers[i] != null) {
/* 226 */         clean(this.mappedBuffers[i]);
/* 227 */         this.mappedBuffers[i] = null;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (this.channel != null)
/* 232 */       this.channel.close(); 
/* 233 */     this.channel = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 242 */     close();
/* 243 */     super.finalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean clean(final ByteBuffer buffer) {
/* 252 */     if (buffer == null || !buffer.isDirect()) {
/* 253 */       return false;
/*     */     }
/* 255 */     Boolean b = AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>() {
/*     */           public Boolean run() {
/* 257 */             Boolean success = Boolean.FALSE;
/*     */             try {
/* 259 */               Method getCleanerMethod = buffer.getClass().getMethod("cleaner", (Class[])null);
/* 260 */               getCleanerMethod.setAccessible(true);
/* 261 */               Object cleaner = getCleanerMethod.invoke(buffer, (Object[])null);
/* 262 */               Method clean = cleaner.getClass().getMethod("clean", (Class[])null);
/* 263 */               clean.invoke(cleaner, (Object[])null);
/* 264 */               success = Boolean.TRUE;
/* 265 */             } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */             
/* 269 */             return success;
/*     */           }
/*     */         });
/*     */     
/* 273 */     return b.booleanValue();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/MappedRandomAccessFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
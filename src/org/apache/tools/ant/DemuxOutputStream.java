/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.WeakHashMap;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class DemuxOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int MAX_SIZE = 1024;
/*     */   private static final int INITIAL_SIZE = 132;
/*     */   private static final int CR = 13;
/*     */   private static final int LF = 10;
/*     */   
/*     */   private static class BufferInfo
/*     */   {
/*     */     private ByteArrayOutputStream buffer;
/*     */     
/*     */     private BufferInfo() {}
/*     */     
/*     */     private boolean crSeen = false;
/*     */   }
/*  67 */   private WeakHashMap<Thread, BufferInfo> buffers = new WeakHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Project project;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isErrorStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DemuxOutputStream(Project project, boolean isErrorStream) {
/*  90 */     this.project = project;
/*  91 */     this.isErrorStream = isErrorStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferInfo getBufferInfo() {
/* 100 */     Thread current = Thread.currentThread();
/* 101 */     return this.buffers.computeIfAbsent(current, x -> {
/*     */           BufferInfo bufferInfo = new BufferInfo();
/*     */           bufferInfo.buffer = new ByteArrayOutputStream(132);
/*     */           bufferInfo.crSeen = false;
/*     */           return bufferInfo;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetBufferInfo() {
/* 113 */     Thread current = Thread.currentThread();
/* 114 */     BufferInfo bufferInfo = this.buffers.get(current);
/* 115 */     FileUtils.close(bufferInfo.buffer);
/* 116 */     bufferInfo.buffer = new ByteArrayOutputStream();
/* 117 */     bufferInfo.crSeen = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeBuffer() {
/* 124 */     Thread current = Thread.currentThread();
/* 125 */     this.buffers.remove(current);
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
/*     */   public void write(int cc) throws IOException {
/* 137 */     byte c = (byte)cc;
/*     */     
/* 139 */     BufferInfo bufferInfo = getBufferInfo();
/*     */     
/* 141 */     if (c == 10) {
/*     */       
/* 143 */       bufferInfo.buffer.write(cc);
/* 144 */       processBuffer(bufferInfo.buffer);
/*     */     } else {
/* 146 */       if (bufferInfo.crSeen)
/*     */       {
/* 148 */         processBuffer(bufferInfo.buffer);
/*     */       }
/*     */       
/* 151 */       bufferInfo.buffer.write(cc);
/*     */     } 
/* 153 */     bufferInfo.crSeen = (c == 13);
/* 154 */     if (!bufferInfo.crSeen && bufferInfo.buffer.size() > 1024) {
/* 155 */       processBuffer(bufferInfo.buffer);
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
/*     */   protected void processBuffer(ByteArrayOutputStream buffer) {
/* 168 */     String output = buffer.toString();
/* 169 */     this.project.demuxOutput(output, this.isErrorStream);
/* 170 */     resetBufferInfo();
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
/*     */   protected void processFlush(ByteArrayOutputStream buffer) {
/* 182 */     String output = buffer.toString();
/* 183 */     this.project.demuxFlush(output, this.isErrorStream);
/* 184 */     resetBufferInfo();
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
/*     */   public void close() throws IOException {
/* 196 */     flush();
/* 197 */     removeBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 208 */     BufferInfo bufferInfo = getBufferInfo();
/* 209 */     if (bufferInfo.buffer.size() > 0) {
/* 210 */       processFlush(bufferInfo.buffer);
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
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 226 */     int offset = off;
/* 227 */     int blockStartOffset = offset;
/* 228 */     int remaining = len;
/* 229 */     BufferInfo bufferInfo = getBufferInfo();
/* 230 */     while (remaining > 0) {
/* 231 */       while (remaining > 0 && b[offset] != 10 && b[offset] != 13) {
/* 232 */         offset++;
/* 233 */         remaining--;
/*     */       } 
/*     */       
/* 236 */       int blockLength = offset - blockStartOffset;
/* 237 */       if (blockLength > 0) {
/* 238 */         bufferInfo.buffer.write(b, blockStartOffset, blockLength);
/*     */       }
/* 240 */       while (remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
/* 241 */         write(b[offset]);
/* 242 */         offset++;
/* 243 */         remaining--;
/*     */       } 
/* 245 */       blockStartOffset = offset;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/DemuxOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
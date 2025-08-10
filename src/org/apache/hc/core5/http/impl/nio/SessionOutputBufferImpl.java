/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*     */ class SessionOutputBufferImpl
/*     */   extends ExpandableBuffer
/*     */   implements SessionOutputBuffer
/*     */ {
/*  47 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CharsetEncoder charEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int lineBufferSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharBuffer charbuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int bufferSize, int lineBufferSize, CharsetEncoder charEncoder) {
/*  69 */     super(bufferSize);
/*  70 */     this.lineBufferSize = Args.positive(lineBufferSize, "Line buffer size");
/*  71 */     this.charEncoder = charEncoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int bufferSize, int lineBufferSize, Charset charset) {
/*  81 */     this(bufferSize, lineBufferSize, (charset != null) ? charset.newEncoder() : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int bufferSize, int lineBufferSize) {
/*  90 */     this(bufferSize, lineBufferSize, (CharsetEncoder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int bufferSize) {
/*  97 */     this(bufferSize, 256);
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 102 */     return super.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/* 107 */     return super.hasData();
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 112 */     return super.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public int flush(WritableByteChannel channel) throws IOException {
/* 117 */     Args.notNull(channel, "Channel");
/* 118 */     setOutputMode();
/* 119 */     return channel.write(buffer());
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ByteBuffer src) {
/* 124 */     if (src == null) {
/*     */       return;
/*     */     }
/* 127 */     setInputMode();
/* 128 */     ensureAdjustedCapacity(buffer().position() + src.remaining());
/* 129 */     buffer().put(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ReadableByteChannel src) throws IOException {
/* 134 */     if (src == null) {
/*     */       return;
/*     */     }
/* 137 */     setInputMode();
/* 138 */     src.read(buffer());
/*     */   }
/*     */   
/*     */   private void write(byte[] b) {
/* 142 */     if (b == null) {
/*     */       return;
/*     */     }
/* 145 */     setInputMode();
/* 146 */     int off = 0;
/* 147 */     int len = b.length;
/* 148 */     int requiredCapacity = buffer().position() + len;
/* 149 */     ensureAdjustedCapacity(requiredCapacity);
/* 150 */     buffer().put(b, 0, len);
/*     */   }
/*     */   
/*     */   private void writeCRLF() {
/* 154 */     write(CRLF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLine(CharArrayBuffer lineBuffer) throws CharacterCodingException {
/* 159 */     if (lineBuffer == null) {
/*     */       return;
/*     */     }
/* 162 */     setInputMode();
/*     */     
/* 164 */     if (lineBuffer.length() > 0) {
/* 165 */       if (this.charEncoder == null) {
/* 166 */         int requiredCapacity = buffer().position() + lineBuffer.length();
/* 167 */         ensureCapacity(requiredCapacity);
/* 168 */         if (buffer().hasArray()) {
/* 169 */           byte[] b = buffer().array();
/* 170 */           int len = lineBuffer.length();
/* 171 */           int off = buffer().position();
/* 172 */           int arrayOffset = buffer().arrayOffset();
/* 173 */           for (int i = 0; i < len; i++) {
/* 174 */             b[arrayOffset + off + i] = (byte)lineBuffer.charAt(i);
/*     */           }
/* 176 */           buffer().position(off + len);
/*     */         } else {
/* 178 */           for (int i = 0; i < lineBuffer.length(); i++) {
/* 179 */             buffer().put((byte)lineBuffer.charAt(i));
/*     */           }
/*     */         } 
/*     */       } else {
/* 183 */         if (this.charbuffer == null) {
/* 184 */           this.charbuffer = CharBuffer.allocate(this.lineBufferSize);
/*     */         }
/* 186 */         this.charEncoder.reset();
/*     */         
/* 188 */         int remaining = lineBuffer.length();
/* 189 */         int offset = 0;
/* 190 */         while (remaining > 0) {
/* 191 */           int l = this.charbuffer.remaining();
/* 192 */           boolean eol = false;
/* 193 */           if (remaining <= l) {
/* 194 */             l = remaining;
/*     */             
/* 196 */             eol = true;
/*     */           } 
/* 198 */           this.charbuffer.put(lineBuffer.array(), offset, l);
/* 199 */           this.charbuffer.flip();
/*     */           
/* 201 */           boolean bool1 = true;
/* 202 */           while (bool1) {
/* 203 */             CoderResult result = this.charEncoder.encode(this.charbuffer, buffer(), eol);
/* 204 */             if (result.isError()) {
/* 205 */               result.throwException();
/*     */             }
/* 207 */             if (result.isOverflow()) {
/* 208 */               expand();
/*     */             }
/* 210 */             bool1 = !result.isUnderflow();
/*     */           } 
/* 212 */           this.charbuffer.compact();
/* 213 */           offset += l;
/* 214 */           remaining -= l;
/*     */         } 
/*     */         
/* 217 */         boolean retry = true;
/* 218 */         while (retry) {
/* 219 */           CoderResult result = this.charEncoder.flush(buffer());
/* 220 */           if (result.isError()) {
/* 221 */             result.throwException();
/*     */           }
/* 223 */           if (result.isOverflow()) {
/* 224 */             expand();
/*     */           }
/* 226 */           retry = !result.isUnderflow();
/*     */         } 
/*     */       } 
/*     */     }
/* 230 */     writeCRLF();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/SessionOutputBufferImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
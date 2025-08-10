/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.slf4j.Logger;
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
/*     */ class LoggingIOSession
/*     */   implements IOSession
/*     */ {
/*     */   private final Logger log;
/*     */   private final Logger wireLog;
/*     */   private final IOSession session;
/*     */   
/*     */   public LoggingIOSession(IOSession session, Logger log, Logger wireLog) {
/*  54 */     this.session = session;
/*  55 */     this.log = log;
/*  56 */     this.wireLog = wireLog;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  61 */     return this.session.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public Lock getLock() {
/*  66 */     return this.session.getLock();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCommands() {
/*  71 */     return this.session.hasCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public Command poll() {
/*  76 */     return this.session.poll();
/*     */   }
/*     */ 
/*     */   
/*     */   public void enqueue(Command command, Command.Priority priority) {
/*  81 */     this.session.enqueue(command, priority);
/*  82 */     if (this.log.isDebugEnabled()) {
/*  83 */       this.log.debug("{} Enqueued {} with priority {}", new Object[] { this.session, command.getClass().getSimpleName(), priority });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteChannel channel() {
/*  89 */     return this.session.channel();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/*  94 */     return this.session.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/*  99 */     return this.session.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventMask() {
/* 104 */     return this.session.getEventMask();
/*     */   }
/*     */   
/*     */   private static String formatOps(int ops) {
/* 108 */     StringBuilder buffer = new StringBuilder(6);
/* 109 */     buffer.append('[');
/* 110 */     if ((ops & 0x1) > 0) {
/* 111 */       buffer.append('r');
/*     */     }
/* 113 */     if ((ops & 0x4) > 0) {
/* 114 */       buffer.append('w');
/*     */     }
/* 116 */     if ((ops & 0x10) > 0) {
/* 117 */       buffer.append('a');
/*     */     }
/* 119 */     if ((ops & 0x8) > 0) {
/* 120 */       buffer.append('c');
/*     */     }
/* 122 */     buffer.append(']');
/* 123 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEventMask(int ops) {
/* 128 */     this.session.setEventMask(ops);
/* 129 */     if (this.log.isDebugEnabled()) {
/* 130 */       this.log.debug("{} Event mask set {}", this.session, formatOps(ops));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEvent(int op) {
/* 136 */     this.session.setEvent(op);
/* 137 */     if (this.log.isDebugEnabled()) {
/* 138 */       this.log.debug("{} Event set {}", this.session, formatOps(op));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearEvent(int op) {
/* 144 */     this.session.clearEvent(op);
/* 145 */     if (this.log.isDebugEnabled()) {
/* 146 */       this.log.debug("{} Event cleared {}", this.session, formatOps(op));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 152 */     return this.session.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 157 */     if (this.log.isDebugEnabled()) {
/* 158 */       this.log.debug("{} Close", this.session);
/*     */     }
/* 160 */     this.session.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOSession.Status getStatus() {
/* 165 */     return this.session.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 170 */     if (this.log.isDebugEnabled()) {
/* 171 */       this.log.debug("{} Close {}", this.session, closeMode);
/*     */     }
/* 173 */     this.session.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 178 */     return this.session.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 183 */     if (this.log.isDebugEnabled()) {
/* 184 */       this.log.debug("{} Set timeout {}", this.session, timeout);
/*     */     }
/* 186 */     this.session.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastReadTime() {
/* 191 */     return this.session.getLastReadTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastWriteTime() {
/* 196 */     return this.session.getLastWriteTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateReadTime() {
/* 201 */     this.session.updateReadTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWriteTime() {
/* 206 */     this.session.updateWriteTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastEventTime() {
/* 211 */     return this.session.getLastEventTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOEventHandler getHandler() {
/* 216 */     return this.session.getHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgrade(final IOEventHandler handler) {
/* 221 */     Args.notNull(handler, "Protocol handler");
/* 222 */     if (this.log.isDebugEnabled()) {
/* 223 */       this.log.debug("{} protocol upgrade {}", this.session, handler.getClass());
/*     */     }
/* 225 */     this.session.upgrade(new IOEventHandler()
/*     */         {
/*     */           public void connected(IOSession protocolSession) throws IOException
/*     */           {
/* 229 */             handler.connected(protocolSession);
/*     */           }
/*     */ 
/*     */           
/*     */           public void inputReady(IOSession protocolSession, ByteBuffer src) throws IOException {
/* 234 */             if (src != null && LoggingIOSession.this.wireLog.isDebugEnabled()) {
/* 235 */               ByteBuffer b = src.duplicate();
/* 236 */               LoggingIOSession.this.logData(b, "<< ");
/*     */             } 
/* 238 */             handler.inputReady(protocolSession, src);
/*     */           }
/*     */ 
/*     */           
/*     */           public void outputReady(IOSession protocolSession) throws IOException {
/* 243 */             handler.outputReady(protocolSession);
/*     */           }
/*     */ 
/*     */           
/*     */           public void timeout(IOSession protocolSession, Timeout timeout) throws IOException {
/* 248 */             handler.timeout(protocolSession, timeout);
/*     */           }
/*     */ 
/*     */           
/*     */           public void exception(IOSession protocolSession, Exception cause) {
/* 253 */             handler.exception(protocolSession, cause);
/*     */           }
/*     */ 
/*     */           
/*     */           public void disconnected(IOSession protocolSession) {
/* 258 */             handler.disconnected(protocolSession);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void logData(ByteBuffer data, String prefix) throws IOException {
/* 266 */     byte[] line = new byte[16];
/* 267 */     StringBuilder buf = new StringBuilder();
/* 268 */     while (data.hasRemaining()) {
/* 269 */       buf.setLength(0);
/* 270 */       buf.append(this.session).append(" ").append(prefix);
/* 271 */       int chunk = Math.min(data.remaining(), line.length);
/* 272 */       data.get(line, 0, chunk);
/*     */       int i;
/* 274 */       for (i = 0; i < chunk; i++) {
/* 275 */         char ch = (char)line[i];
/* 276 */         if (ch > ' ' && ch <= '') {
/* 277 */           buf.append(ch);
/* 278 */         } else if (Character.isWhitespace(ch)) {
/* 279 */           buf.append(' ');
/*     */         } else {
/* 281 */           buf.append('.');
/*     */         } 
/*     */       } 
/* 284 */       for (i = chunk; i < 17; i++) {
/* 285 */         buf.append(' ');
/*     */       }
/*     */       
/* 288 */       for (i = 0; i < chunk; i++) {
/* 289 */         buf.append(' ');
/* 290 */         int b = line[i] & 0xFF;
/* 291 */         String s = Integer.toHexString(b);
/* 292 */         if (s.length() == 1) {
/* 293 */           buf.append("0");
/*     */         }
/* 295 */         buf.append(s);
/*     */       } 
/* 297 */       this.wireLog.debug(buf.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/* 303 */     int bytesRead = this.session.read(dst);
/* 304 */     if (this.log.isDebugEnabled()) {
/* 305 */       this.log.debug("{} {} bytes read", this.session, Integer.valueOf(bytesRead));
/*     */     }
/* 307 */     if (bytesRead > 0 && this.wireLog.isDebugEnabled()) {
/* 308 */       ByteBuffer b = dst.duplicate();
/* 309 */       int p = b.position();
/* 310 */       b.limit(p);
/* 311 */       b.position(p - bytesRead);
/* 312 */       logData(b, "<< ");
/*     */     } 
/* 314 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/* 319 */     int byteWritten = this.session.write(src);
/* 320 */     if (this.log.isDebugEnabled()) {
/* 321 */       this.log.debug("{} {} bytes written", this.session, Integer.valueOf(byteWritten));
/*     */     }
/* 323 */     if (byteWritten > 0 && this.wireLog.isDebugEnabled()) {
/* 324 */       ByteBuffer b = src.duplicate();
/* 325 */       int p = b.position();
/* 326 */       b.limit(p);
/* 327 */       b.position(p - byteWritten);
/* 328 */       logData(b, ">> ");
/*     */     } 
/* 330 */     return byteWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 335 */     return this.session.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/LoggingIOSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
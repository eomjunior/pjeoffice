/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ class IOSessionImpl
/*     */   implements IOSession
/*     */ {
/*  52 */   private static final AtomicLong COUNT = new AtomicLong(0L);
/*     */   
/*     */   private final SelectionKey key;
/*     */   
/*     */   private final SocketChannel channel;
/*     */   
/*     */   private final Deque<Command> commandQueue;
/*     */   private final Lock lock;
/*     */   private final String id;
/*     */   private final AtomicReference<IOEventHandler> handlerRef;
/*     */   private final AtomicReference<IOSession.Status> status;
/*     */   private volatile Timeout socketTimeout;
/*     */   private volatile long lastReadTime;
/*     */   private volatile long lastWriteTime;
/*     */   private volatile long lastEventTime;
/*     */   
/*     */   public IOSessionImpl(String type, SelectionKey key, SocketChannel socketChannel) {
/*  69 */     this.key = (SelectionKey)Args.notNull(key, "Selection key");
/*  70 */     this.channel = (SocketChannel)Args.notNull(socketChannel, "Socket channel");
/*  71 */     this.commandQueue = new ConcurrentLinkedDeque<>();
/*  72 */     this.lock = new ReentrantLock();
/*  73 */     this.socketTimeout = Timeout.DISABLED;
/*  74 */     this.id = String.format(type + "-%010d", new Object[] { Long.valueOf(COUNT.getAndIncrement()) });
/*  75 */     this.handlerRef = new AtomicReference<>();
/*  76 */     this.status = new AtomicReference<>(IOSession.Status.ACTIVE);
/*  77 */     long currentTimeMillis = System.currentTimeMillis();
/*  78 */     this.lastReadTime = currentTimeMillis;
/*  79 */     this.lastWriteTime = currentTimeMillis;
/*  80 */     this.lastEventTime = currentTimeMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  85 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOEventHandler getHandler() {
/*  90 */     return this.handlerRef.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgrade(IOEventHandler handler) {
/*  95 */     this.handlerRef.set(handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public Lock getLock() {
/* 100 */     return this.lock;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enqueue(Command command, Command.Priority priority) {
/* 105 */     if (priority == Command.Priority.IMMEDIATE) {
/* 106 */       this.commandQueue.addFirst(command);
/*     */     } else {
/* 108 */       this.commandQueue.add(command);
/*     */     } 
/* 110 */     setEvent(4);
/*     */     
/* 112 */     if (isStatusClosed()) {
/* 113 */       command.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCommands() {
/* 119 */     return !this.commandQueue.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Command poll() {
/* 124 */     return this.commandQueue.poll();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteChannel channel() {
/* 129 */     return this.channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 134 */     return this.channel.socket().getLocalSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 139 */     return this.channel.socket().getRemoteSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventMask() {
/* 144 */     return this.key.interestOps();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEventMask(int newValue) {
/* 149 */     this.lock.lock();
/*     */     try {
/* 151 */       if (isStatusClosed()) {
/*     */         return;
/*     */       }
/* 154 */       this.key.interestOps(newValue);
/*     */     } finally {
/* 156 */       this.lock.unlock();
/*     */     } 
/* 158 */     this.key.selector().wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEvent(int op) {
/* 163 */     this.lock.lock();
/*     */     try {
/* 165 */       if (isStatusClosed()) {
/*     */         return;
/*     */       }
/* 168 */       this.key.interestOps(this.key.interestOps() | op);
/*     */     } finally {
/* 170 */       this.lock.unlock();
/*     */     } 
/* 172 */     this.key.selector().wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearEvent(int op) {
/* 177 */     this.lock.lock();
/*     */     try {
/* 179 */       if (isStatusClosed()) {
/*     */         return;
/*     */       }
/* 182 */       this.key.interestOps(this.key.interestOps() & (op ^ 0xFFFFFFFF));
/*     */     } finally {
/* 184 */       this.lock.unlock();
/*     */     } 
/* 186 */     this.key.selector().wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 191 */     return this.socketTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 196 */     this.socketTimeout = Timeout.defaultsToDisabled(timeout);
/* 197 */     this.lastEventTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/* 202 */     return this.channel.read(dst);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/* 207 */     return this.channel.write(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateReadTime() {
/* 212 */     this.lastReadTime = System.currentTimeMillis();
/* 213 */     this.lastEventTime = this.lastReadTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWriteTime() {
/* 218 */     this.lastWriteTime = System.currentTimeMillis();
/* 219 */     this.lastEventTime = this.lastWriteTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastReadTime() {
/* 224 */     return this.lastReadTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastWriteTime() {
/* 229 */     return this.lastWriteTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastEventTime() {
/* 234 */     return this.lastEventTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOSession.Status getStatus() {
/* 239 */     return this.status.get();
/*     */   }
/*     */   
/*     */   private boolean isStatusClosed() {
/* 243 */     return (this.status.get() == IOSession.Status.CLOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 248 */     return (this.status.get() == IOSession.Status.ACTIVE && this.channel.isOpen());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 253 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 258 */     if (this.status.compareAndSet(IOSession.Status.ACTIVE, IOSession.Status.CLOSED)) {
/* 259 */       if (closeMode == CloseMode.IMMEDIATE) {
/*     */         try {
/* 261 */           this.channel.socket().setSoLinger(true, 0);
/* 262 */         } catch (SocketException socketException) {}
/*     */       }
/*     */ 
/*     */       
/* 266 */       this.key.cancel();
/* 267 */       this.key.attach(null);
/* 268 */       Closer.closeQuietly(this.key.channel());
/* 269 */       if (this.key.selector().isOpen()) {
/* 270 */         this.key.selector().wakeup();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void formatOps(StringBuilder buffer, int ops) {
/* 276 */     if ((ops & 0x1) > 0) {
/* 277 */       buffer.append('r');
/*     */     }
/* 279 */     if ((ops & 0x4) > 0) {
/* 280 */       buffer.append('w');
/*     */     }
/* 282 */     if ((ops & 0x10) > 0) {
/* 283 */       buffer.append('a');
/*     */     }
/* 285 */     if ((ops & 0x8) > 0) {
/* 286 */       buffer.append('c');
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 292 */     StringBuilder buffer = new StringBuilder();
/* 293 */     buffer.append(this.id).append("[");
/* 294 */     buffer.append(this.status);
/* 295 */     buffer.append("][");
/* 296 */     if (this.key.isValid()) {
/* 297 */       formatOps(buffer, this.key.interestOps());
/* 298 */       buffer.append(":");
/* 299 */       formatOps(buffer, this.key.readyOps());
/*     */     } 
/* 301 */     buffer.append("]");
/* 302 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOSessionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
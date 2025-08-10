/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.UnresolvedAddressException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.hc.core5.http.nio.command.CommandSupport;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.SocketTimeoutExceptionFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SocksProxyProtocolHandler
/*     */   implements IOEventHandler
/*     */ {
/*     */   private static final int MAX_COMMAND_CONNECT_LENGTH = 22;
/*     */   private static final byte CLIENT_VERSION = 5;
/*     */   private static final byte NO_AUTHENTICATION_REQUIRED = 0;
/*     */   private static final byte USERNAME_PASSWORD = 2;
/*     */   private static final byte USERNAME_PASSWORD_VERSION = 1;
/*     */   private static final byte SUCCESS = 0;
/*     */   private static final byte COMMAND_CONNECT = 1;
/*     */   private static final byte ATYP_DOMAINNAME = 3;
/*     */   private final ProtocolIOSession ioSession;
/*     */   private final Object attachment;
/*     */   private final InetSocketAddress targetAddress;
/*     */   private final String username;
/*     */   private final String password;
/*     */   private final IOEventHandlerFactory eventHandlerFactory;
/*     */   
/*     */   private enum State
/*     */   {
/*  72 */     SEND_AUTH, RECEIVE_AUTH_METHOD, SEND_USERNAME_PASSWORD, RECEIVE_AUTH, SEND_CONNECT, RECEIVE_RESPONSE_CODE, RECEIVE_ADDRESS_TYPE, RECEIVE_ADDRESS, COMPLETE;
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
/*  83 */   private ByteBuffer buffer = ByteBuffer.allocate(32);
/*  84 */   private State state = State.SEND_AUTH;
/*     */   
/*     */   SocksProxyProtocolHandler(ProtocolIOSession ioSession, Object attachment, InetSocketAddress targetAddress, String username, String password, IOEventHandlerFactory eventHandlerFactory) {
/*  87 */     this.ioSession = ioSession;
/*  88 */     this.attachment = attachment;
/*  89 */     this.targetAddress = targetAddress;
/*  90 */     this.username = username;
/*  91 */     this.password = password;
/*  92 */     this.eventHandlerFactory = eventHandlerFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) throws IOException {
/*  97 */     this.buffer.put((byte)5);
/*  98 */     this.buffer.put((byte)1);
/*  99 */     this.buffer.put((byte)0);
/* 100 */     this.buffer.flip();
/* 101 */     session.setEventMask(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) throws IOException {
/* 106 */     switch (this.state) {
/*     */       case SEND_AUTH:
/* 108 */         if (writeAndPrepareRead(session, 2)) {
/* 109 */           session.setEventMask(1);
/* 110 */           this.state = State.RECEIVE_AUTH_METHOD;
/*     */         } 
/*     */         break;
/*     */       case SEND_USERNAME_PASSWORD:
/* 114 */         if (writeAndPrepareRead(session, 2)) {
/* 115 */           session.setEventMask(1);
/* 116 */           this.state = State.RECEIVE_AUTH;
/*     */         } 
/*     */         break;
/*     */       case SEND_CONNECT:
/* 120 */         if (writeAndPrepareRead(session, 2)) {
/* 121 */           session.setEventMask(1);
/* 122 */           this.state = State.RECEIVE_RESPONSE_CODE;
/*     */         } 
/*     */         break;
/*     */       case RECEIVE_AUTH_METHOD:
/*     */       case RECEIVE_AUTH:
/*     */       case RECEIVE_ADDRESS:
/*     */       case RECEIVE_ADDRESS_TYPE:
/*     */       case RECEIVE_RESPONSE_CODE:
/* 130 */         session.setEventMask(1);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inputReady(IOSession session, ByteBuffer src) throws IOException {
/* 139 */     if (src != null) {
/*     */       try {
/* 141 */         this.buffer.put(src);
/* 142 */       } catch (BufferOverflowException ex) {
/* 143 */         throw new IOException("Unexpected input data");
/*     */       } 
/*     */     }
/* 146 */     switch (this.state) {
/*     */       case RECEIVE_AUTH_METHOD:
/* 148 */         if (fillBuffer(session)) {
/* 149 */           this.buffer.flip();
/* 150 */           byte serverVersion = this.buffer.get();
/* 151 */           byte serverMethod = this.buffer.get();
/* 152 */           if (serverVersion != 5) {
/* 153 */             throw new IOException("SOCKS server returned unsupported version: " + serverVersion);
/*     */           }
/* 155 */           if (serverMethod == 2) {
/* 156 */             this.buffer.clear();
/* 157 */             setBufferLimit(this.username.length() + this.password.length() + 3);
/* 158 */             this.buffer.put((byte)1);
/* 159 */             this.buffer.put((byte)this.username.length());
/* 160 */             this.buffer.put(this.username.getBytes(StandardCharsets.ISO_8859_1));
/* 161 */             this.buffer.put((byte)this.password.length());
/* 162 */             this.buffer.put(this.password.getBytes(StandardCharsets.ISO_8859_1));
/* 163 */             session.setEventMask(4);
/* 164 */             this.state = State.SEND_USERNAME_PASSWORD; break;
/* 165 */           }  if (serverMethod == 0) {
/* 166 */             prepareConnectCommand();
/* 167 */             session.setEventMask(4);
/* 168 */             this.state = State.SEND_CONNECT; break;
/*     */           } 
/* 170 */           throw new IOException("SOCKS server return unsupported authentication method: " + serverMethod);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case RECEIVE_AUTH:
/* 175 */         if (fillBuffer(session)) {
/* 176 */           this.buffer.flip();
/* 177 */           this.buffer.get();
/* 178 */           byte status = this.buffer.get();
/* 179 */           if (status != 0) {
/* 180 */             throw new IOException("Authentication failed for external SOCKS proxy");
/*     */           }
/* 182 */           prepareConnectCommand();
/* 183 */           session.setEventMask(4);
/* 184 */           this.state = State.SEND_CONNECT;
/*     */         } 
/*     */         break;
/*     */       case RECEIVE_RESPONSE_CODE:
/* 188 */         if (fillBuffer(session)) {
/* 189 */           this.buffer.flip();
/* 190 */           byte serverVersion = this.buffer.get();
/* 191 */           byte responseCode = this.buffer.get();
/* 192 */           if (serverVersion != 5) {
/* 193 */             throw new IOException("SOCKS server returned unsupported version: " + serverVersion);
/*     */           }
/* 195 */           if (responseCode != 0) {
/* 196 */             throw new IOException("SOCKS server was unable to establish connection returned error code: " + responseCode);
/*     */           }
/* 198 */           this.buffer.compact();
/* 199 */           this.buffer.limit(3);
/* 200 */           this.state = State.RECEIVE_ADDRESS_TYPE;
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */       
/*     */       case RECEIVE_ADDRESS_TYPE:
/* 206 */         if (fillBuffer(session)) {
/* 207 */           int addressSize; this.buffer.flip();
/* 208 */           this.buffer.get();
/* 209 */           byte aType = this.buffer.get();
/*     */           
/* 211 */           if (aType == 1) {
/* 212 */             addressSize = 4;
/* 213 */           } else if (aType == 4) {
/* 214 */             addressSize = 16;
/* 215 */           } else if (aType == 3) {
/*     */             
/* 217 */             addressSize = this.buffer.get() & 0xFF;
/*     */           } else {
/* 219 */             throw new IOException("SOCKS server returned unsupported address type: " + aType);
/*     */           } 
/* 221 */           int remainingResponseSize = addressSize + 2;
/* 222 */           this.buffer.compact();
/*     */           
/* 224 */           this.buffer.limit(remainingResponseSize);
/* 225 */           this.state = State.RECEIVE_ADDRESS;
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */       
/*     */       case RECEIVE_ADDRESS:
/* 231 */         if (fillBuffer(session)) {
/* 232 */           this.buffer.clear();
/* 233 */           this.state = State.COMPLETE;
/* 234 */           IOEventHandler newHandler = this.eventHandlerFactory.createHandler(this.ioSession, this.attachment);
/* 235 */           this.ioSession.upgrade(newHandler);
/* 236 */           newHandler.connected(this.ioSession);
/*     */         } 
/*     */         break;
/*     */       case SEND_AUTH:
/*     */       case SEND_USERNAME_PASSWORD:
/*     */       case SEND_CONNECT:
/* 242 */         session.setEventMask(4);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareConnectCommand() throws IOException {
/* 250 */     InetAddress address = this.targetAddress.getAddress();
/* 251 */     int port = this.targetAddress.getPort();
/* 252 */     if (address == null || port == 0) {
/* 253 */       throw new UnresolvedAddressException();
/*     */     }
/*     */     
/* 256 */     this.buffer.clear();
/* 257 */     setBufferLimit(22);
/* 258 */     this.buffer.put((byte)5);
/* 259 */     this.buffer.put((byte)1);
/* 260 */     this.buffer.put((byte)0);
/* 261 */     if (address instanceof java.net.Inet4Address) {
/* 262 */       this.buffer.put((byte)1);
/* 263 */     } else if (address instanceof java.net.Inet6Address) {
/* 264 */       this.buffer.put((byte)4);
/*     */     } else {
/* 266 */       throw new IOException("Unsupported remote address class: " + address.getClass().getName());
/*     */     } 
/* 268 */     this.buffer.put(address.getAddress());
/* 269 */     this.buffer.putShort((short)port);
/* 270 */     this.buffer.flip();
/*     */   }
/*     */   
/*     */   private void setBufferLimit(int newLimit) {
/* 274 */     if (this.buffer.capacity() < newLimit) {
/* 275 */       ByteBuffer newBuffer = ByteBuffer.allocate(newLimit);
/* 276 */       this.buffer.flip();
/* 277 */       newBuffer.put(this.buffer);
/* 278 */       this.buffer = newBuffer;
/*     */     } else {
/* 280 */       this.buffer.limit(newLimit);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean writeAndPrepareRead(ByteChannel channel, int readSize) throws IOException {
/* 285 */     if (writeBuffer(channel)) {
/* 286 */       this.buffer.clear();
/* 287 */       setBufferLimit(readSize);
/* 288 */       return true;
/*     */     } 
/* 290 */     return false;
/*     */   }
/*     */   
/*     */   private boolean writeBuffer(ByteChannel channel) throws IOException {
/* 294 */     if (this.buffer.hasRemaining()) {
/* 295 */       channel.write(this.buffer);
/*     */     }
/* 297 */     return !this.buffer.hasRemaining();
/*     */   }
/*     */   
/*     */   private boolean fillBuffer(ByteChannel channel) throws IOException {
/* 301 */     if (this.buffer.hasRemaining()) {
/* 302 */       channel.read(this.buffer);
/*     */     }
/* 304 */     return !this.buffer.hasRemaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public void timeout(IOSession session, Timeout timeout) throws IOException {
/* 309 */     exception(session, SocketTimeoutExceptionFactory.create(timeout));
/*     */   }
/*     */ 
/*     */   
/*     */   public void exception(IOSession session, Exception cause) {
/* 314 */     session.close(CloseMode.IMMEDIATE);
/* 315 */     CommandSupport.failCommands(session, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnected(IOSession session) {
/* 320 */     CommandSupport.cancelCommands(session);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/SocksProxyProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
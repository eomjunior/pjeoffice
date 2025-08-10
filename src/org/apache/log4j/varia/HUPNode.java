/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HUPNode
/*     */   implements Runnable
/*     */ {
/*     */   Socket socket;
/*     */   DataInputStream dis;
/*     */   DataOutputStream dos;
/*     */   ExternallyRolledFileAppender er;
/*     */   
/*     */   public HUPNode(Socket socket, ExternallyRolledFileAppender er) {
/* 146 */     this.socket = socket;
/* 147 */     this.er = er;
/*     */     try {
/* 149 */       this.dis = new DataInputStream(socket.getInputStream());
/* 150 */       this.dos = new DataOutputStream(socket.getOutputStream());
/* 151 */     } catch (InterruptedIOException e) {
/* 152 */       Thread.currentThread().interrupt();
/* 153 */       e.printStackTrace();
/* 154 */     } catch (IOException e) {
/* 155 */       e.printStackTrace();
/* 156 */     } catch (RuntimeException e) {
/* 157 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/* 163 */       String line = this.dis.readUTF();
/* 164 */       LogLog.debug("Got external roll over signal.");
/* 165 */       if ("RollOver".equals(line)) {
/* 166 */         synchronized (this.er) {
/* 167 */           this.er.rollOver();
/*     */         } 
/* 169 */         this.dos.writeUTF("OK");
/*     */       } else {
/* 171 */         this.dos.writeUTF("Expecting [RollOver] string.");
/*     */       } 
/* 173 */       this.dos.close();
/* 174 */     } catch (InterruptedIOException e) {
/* 175 */       Thread.currentThread().interrupt();
/* 176 */       LogLog.error("Unexpected exception. Exiting HUPNode.", e);
/* 177 */     } catch (IOException e) {
/* 178 */       LogLog.error("Unexpected exception. Exiting HUPNode.", e);
/* 179 */     } catch (RuntimeException e) {
/* 180 */       LogLog.error("Unexpected exception. Exiting HUPNode.", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/HUPNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
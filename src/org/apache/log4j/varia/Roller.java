/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import org.apache.log4j.BasicConfigurator;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Roller
/*     */ {
/*  43 */   static Logger cat = Logger.getLogger(Roller.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String host;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int port;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] argv) {
/*  59 */     BasicConfigurator.configure();
/*     */     
/*  61 */     if (argv.length == 2) {
/*  62 */       init(argv[0], argv[1]);
/*     */     } else {
/*  64 */       usage("Wrong number of arguments.");
/*     */     } 
/*  66 */     roll();
/*     */   }
/*     */   
/*     */   static void usage(String msg) {
/*  70 */     System.err.println(msg);
/*  71 */     System.err.println("Usage: java " + Roller.class.getName() + "host_name port_number");
/*  72 */     System.exit(1);
/*     */   }
/*     */   
/*     */   static void init(String hostArg, String portArg) {
/*  76 */     host = hostArg;
/*     */     try {
/*  78 */       port = Integer.parseInt(portArg);
/*  79 */     } catch (NumberFormatException e) {
/*  80 */       usage("Second argument " + portArg + " is not a valid integer.");
/*     */     } 
/*     */   }
/*     */   
/*     */   static void roll() {
/*     */     try {
/*  86 */       Socket socket = new Socket(host, port);
/*  87 */       DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
/*  88 */       DataInputStream dis = new DataInputStream(socket.getInputStream());
/*  89 */       dos.writeUTF("RollOver");
/*  90 */       String rc = dis.readUTF();
/*  91 */       if ("OK".equals(rc)) {
/*  92 */         cat.info("Roll over signal acknowledged by remote appender.");
/*     */       } else {
/*  94 */         cat.warn("Unexpected return code " + rc + " from remote entity.");
/*  95 */         System.exit(2);
/*     */       } 
/*  97 */     } catch (IOException e) {
/*  98 */       cat.error("Could not send roll signal on host " + host + " port " + port + " .", e);
/*  99 */       System.exit(2);
/*     */     } 
/* 101 */     System.exit(0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/Roller.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
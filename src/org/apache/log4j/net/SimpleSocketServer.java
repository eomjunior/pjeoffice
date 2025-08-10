/*    */ package org.apache.log4j.net;
/*    */ 
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.PropertyConfigurator;
/*    */ import org.apache.log4j.xml.DOMConfigurator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleSocketServer
/*    */ {
/* 45 */   static Logger cat = Logger.getLogger(SimpleSocketServer.class);
/*    */   
/*    */   static int port;
/*    */   
/*    */   public static void main(String[] argv) {
/* 50 */     if (argv.length == 2) {
/* 51 */       init(argv[0], argv[1]);
/*    */     } else {
/* 53 */       usage("Wrong number of arguments.");
/*    */     } 
/*    */     
/*    */     try {
/* 57 */       cat.info("Listening on port " + port);
/* 58 */       ServerSocket serverSocket = new ServerSocket(port);
/*    */       while (true) {
/* 60 */         cat.info("Waiting to accept a new client.");
/* 61 */         Socket socket = serverSocket.accept();
/* 62 */         cat.info("Connected to client at " + socket.getInetAddress());
/* 63 */         cat.info("Starting new socket node.");
/* 64 */         (new Thread(new SocketNode(socket, LogManager.getLoggerRepository()), "SimpleSocketServer-" + port))
/* 65 */           .start();
/*    */       } 
/* 67 */     } catch (Exception e) {
/* 68 */       e.printStackTrace();
/*    */       return;
/*    */     } 
/*    */   }
/*    */   static void usage(String msg) {
/* 73 */     System.err.println(msg);
/* 74 */     System.err.println("Usage: java " + SimpleSocketServer.class.getName() + " port configFile");
/* 75 */     System.exit(1);
/*    */   }
/*    */   
/*    */   static void init(String portStr, String configFile) {
/*    */     try {
/* 80 */       port = Integer.parseInt(portStr);
/* 81 */     } catch (NumberFormatException e) {
/* 82 */       e.printStackTrace();
/* 83 */       usage("Could not interpret port number [" + portStr + "].");
/*    */     } 
/*    */     
/* 86 */     if (configFile.endsWith(".xml")) {
/* 87 */       DOMConfigurator.configure(configFile);
/*    */     } else {
/* 89 */       PropertyConfigurator.configure(configFile);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SimpleSocketServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
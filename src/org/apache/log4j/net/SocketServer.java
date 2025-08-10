/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Hashtable;
/*     */ import org.apache.log4j.Hierarchy;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.LogManager;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.PropertyConfigurator;
/*     */ import org.apache.log4j.spi.LoggerRepository;
/*     */ import org.apache.log4j.spi.RootLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketServer
/*     */ {
/*  89 */   static String GENERIC = "generic";
/*  90 */   static String CONFIG_FILE_EXT = ".lcf";
/*     */   
/*  92 */   static Logger cat = Logger.getLogger(SocketServer.class);
/*     */   
/*     */   static SocketServer server;
/*     */   
/*     */   static int port;
/*     */   Hashtable hierarchyMap;
/*     */   LoggerRepository genericHierarchy;
/*     */   File dir;
/*     */   
/*     */   public static void main(String[] argv) {
/* 102 */     if (argv.length == 3) {
/* 103 */       init(argv[0], argv[1], argv[2]);
/*     */     } else {
/* 105 */       usage("Wrong number of arguments.");
/*     */     } 
/*     */     try {
/* 108 */       cat.info("Listening on port " + port);
/* 109 */       ServerSocket serverSocket = new ServerSocket(port);
/*     */       while (true) {
/* 111 */         cat.info("Waiting to accept a new client.");
/* 112 */         Socket socket = serverSocket.accept();
/* 113 */         InetAddress inetAddress = socket.getInetAddress();
/* 114 */         cat.info("Connected to client at " + inetAddress);
/*     */         
/* 116 */         LoggerRepository h = (LoggerRepository)server.hierarchyMap.get(inetAddress);
/* 117 */         if (h == null) {
/* 118 */           h = server.configureHierarchy(inetAddress);
/*     */         }
/*     */         
/* 121 */         cat.info("Starting new socket node.");
/* 122 */         (new Thread(new SocketNode(socket, h))).start();
/*     */       } 
/* 124 */     } catch (Exception e) {
/* 125 */       e.printStackTrace();
/*     */       return;
/*     */     } 
/*     */   }
/*     */   static void usage(String msg) {
/* 130 */     System.err.println(msg);
/* 131 */     System.err.println("Usage: java " + SocketServer.class.getName() + " port configFile directory");
/* 132 */     System.exit(1);
/*     */   }
/*     */   
/*     */   static void init(String portStr, String configFile, String dirStr) {
/*     */     try {
/* 137 */       port = Integer.parseInt(portStr);
/* 138 */     } catch (NumberFormatException e) {
/* 139 */       e.printStackTrace();
/* 140 */       usage("Could not interpret port number [" + portStr + "].");
/*     */     } 
/*     */     
/* 143 */     PropertyConfigurator.configure(configFile);
/*     */     
/* 145 */     File dir = new File(dirStr);
/* 146 */     if (!dir.isDirectory()) {
/* 147 */       usage("[" + dirStr + "] is not a directory.");
/*     */     }
/* 149 */     server = new SocketServer(dir);
/*     */   }
/*     */   
/*     */   public SocketServer(File directory) {
/* 153 */     this.dir = directory;
/* 154 */     this.hierarchyMap = new Hashtable<Object, Object>(11);
/*     */   }
/*     */ 
/*     */   
/*     */   LoggerRepository configureHierarchy(InetAddress inetAddress) {
/*     */     String key;
/* 160 */     cat.info("Locating configuration file for " + inetAddress);
/*     */ 
/*     */     
/* 163 */     String s = inetAddress.toString();
/* 164 */     int i = s.indexOf("/");
/* 165 */     if (i == -1) {
/* 166 */       cat.warn("Could not parse the inetAddress [" + inetAddress + "]. Using default hierarchy.");
/* 167 */       return genericHierarchy();
/*     */     } 
/*     */     
/* 170 */     if (i == 0) {
/* 171 */       key = s.substring(i);
/*     */     } else {
/* 173 */       key = s.substring(0, i);
/*     */     } 
/*     */     
/* 176 */     File configFile = new File(this.dir, key + CONFIG_FILE_EXT);
/* 177 */     if (configFile.exists()) {
/* 178 */       Hierarchy h = new Hierarchy((Logger)new RootLogger(Level.DEBUG));
/* 179 */       this.hierarchyMap.put(inetAddress, h);
/*     */       
/* 181 */       (new PropertyConfigurator()).doConfigure(configFile.getAbsolutePath(), (LoggerRepository)h);
/*     */       
/* 183 */       return (LoggerRepository)h;
/*     */     } 
/* 185 */     cat.warn("Could not find config file [" + configFile + "].");
/* 186 */     return genericHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   LoggerRepository genericHierarchy() {
/* 192 */     if (this.genericHierarchy == null) {
/* 193 */       File f = new File(this.dir, GENERIC + CONFIG_FILE_EXT);
/* 194 */       if (f.exists()) {
/* 195 */         this.genericHierarchy = (LoggerRepository)new Hierarchy((Logger)new RootLogger(Level.DEBUG));
/* 196 */         (new PropertyConfigurator()).doConfigure(f.getAbsolutePath(), this.genericHierarchy);
/*     */       } else {
/* 198 */         cat.warn("Could not find config file [" + f + "]. Will use the default hierarchy.");
/* 199 */         this.genericHierarchy = LogManager.getLoggerRepository();
/*     */       } 
/*     */     } 
/* 202 */     return this.genericHierarchy;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SocketServer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
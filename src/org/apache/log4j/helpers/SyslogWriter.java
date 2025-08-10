/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.SocketException;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SyslogWriter
/*     */   extends Writer
/*     */ {
/*  38 */   final int SYSLOG_PORT = 514;
/*     */ 
/*     */ 
/*     */   
/*     */   static String syslogHost;
/*     */ 
/*     */ 
/*     */   
/*     */   private InetAddress address;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int port;
/*     */ 
/*     */ 
/*     */   
/*     */   private DatagramSocket ds;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SyslogWriter(String syslogHost) {
/*  60 */     SyslogWriter.syslogHost = syslogHost;
/*  61 */     if (syslogHost == null) {
/*  62 */       throw new NullPointerException("syslogHost");
/*     */     }
/*     */     
/*  65 */     String host = syslogHost;
/*  66 */     int urlPort = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (host.indexOf("[") != -1 || host.indexOf(':') == host.lastIndexOf(':')) {
/*     */       try {
/*  74 */         URL url = new URL("http://" + host);
/*  75 */         if (url.getHost() != null) {
/*  76 */           host = url.getHost();
/*     */           
/*  78 */           if (host.startsWith("[") && host.charAt(host.length() - 1) == ']') {
/*  79 */             host = host.substring(1, host.length() - 1);
/*     */           }
/*  81 */           urlPort = url.getPort();
/*     */         } 
/*  83 */       } catch (MalformedURLException e) {
/*  84 */         LogLog.error("Malformed URL: will attempt to interpret as InetAddress.", e);
/*     */       } 
/*     */     }
/*     */     
/*  88 */     if (urlPort == -1) {
/*  89 */       urlPort = 514;
/*     */     }
/*  91 */     this.port = urlPort;
/*     */     
/*     */     try {
/*  94 */       this.address = InetAddress.getByName(host);
/*  95 */     } catch (UnknownHostException e) {
/*  96 */       LogLog.error("Could not find " + host + ". All logging will FAIL.", e);
/*     */     } 
/*     */     
/*     */     try {
/* 100 */       this.ds = new DatagramSocket();
/* 101 */     } catch (SocketException e) {
/* 102 */       e.printStackTrace();
/* 103 */       LogLog.error("Could not instantiate DatagramSocket to " + host + ". All logging will FAIL.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(char[] buf, int off, int len) throws IOException {
/* 109 */     write(new String(buf, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String string) throws IOException {
/* 114 */     if (this.ds != null && this.address != null) {
/* 115 */       byte[] bytes = string.getBytes();
/*     */ 
/*     */ 
/*     */       
/* 119 */       int bytesLength = bytes.length;
/* 120 */       if (bytesLength >= 1024) {
/* 121 */         bytesLength = 1024;
/*     */       }
/* 123 */       DatagramPacket packet = new DatagramPacket(bytes, bytesLength, this.address, this.port);
/* 124 */       this.ds.send(packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() {}
/*     */ 
/*     */   
/*     */   public void close() {
/* 133 */     if (this.ds != null)
/* 134 */       this.ds.close(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/SyslogWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
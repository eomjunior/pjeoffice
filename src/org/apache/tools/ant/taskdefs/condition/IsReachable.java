/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IsReachable
/*     */   extends ProjectComponent
/*     */   implements Condition
/*     */ {
/*     */   public static final int DEFAULT_TIMEOUT = 30;
/*     */   private static final int SECOND = 1000;
/*     */   public static final String ERROR_NO_HOSTNAME = "No hostname defined";
/*     */   public static final String ERROR_BAD_TIMEOUT = "Invalid timeout value";
/*     */   private static final String WARN_UNKNOWN_HOST = "Unknown host: ";
/*     */   public static final String ERROR_ON_NETWORK = "network error to ";
/*     */   public static final String ERROR_BOTH_TARGETS = "Both url and host have been specified";
/*     */   public static final String MSG_NO_REACHABLE_TEST = "cannot do a proper reachability test on this Java version";
/*     */   public static final String ERROR_BAD_URL = "Bad URL ";
/*     */   public static final String ERROR_NO_HOST_IN_URL = "No hostname in URL ";
/*     */   @Deprecated
/*     */   public static final String METHOD_NAME = "isReachable";
/*     */   private String host;
/*     */   private String url;
/*  94 */   private int timeout = 30;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHost(String host) {
/* 102 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/* 111 */     this.url = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(int timeout) {
/* 120 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNullOrEmpty(String string) {
/* 131 */     return (string == null || string.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/*     */     InetAddress address;
/*     */     boolean reachable;
/* 144 */     if (isNullOrEmpty(this.host) && isNullOrEmpty(this.url)) {
/* 145 */       throw new BuildException("No hostname defined");
/*     */     }
/* 147 */     if (this.timeout < 0) {
/* 148 */       throw new BuildException("Invalid timeout value");
/*     */     }
/* 150 */     String target = this.host;
/* 151 */     if (!isNullOrEmpty(this.url)) {
/* 152 */       if (!isNullOrEmpty(this.host)) {
/* 153 */         throw new BuildException("Both url and host have been specified");
/*     */       }
/*     */       
/*     */       try {
/* 157 */         URL realURL = new URL(this.url);
/* 158 */         target = realURL.getHost();
/* 159 */         if (isNullOrEmpty(target)) {
/* 160 */           throw new BuildException("No hostname in URL " + this.url);
/*     */         }
/* 162 */       } catch (MalformedURLException e) {
/* 163 */         throw new BuildException("Bad URL " + this.url, e);
/*     */       } 
/*     */     } 
/* 166 */     log("Probing host " + target, 3);
/*     */     
/*     */     try {
/* 169 */       address = InetAddress.getByName(target);
/* 170 */     } catch (UnknownHostException e1) {
/* 171 */       log("Unknown host: " + target);
/* 172 */       return false;
/*     */     } 
/* 174 */     log("Host address = " + address.getHostAddress(), 3);
/*     */ 
/*     */     
/*     */     try {
/* 178 */       reachable = address.isReachable(this.timeout * 1000);
/* 179 */     } catch (IOException ioe) {
/* 180 */       reachable = false;
/* 181 */       log("network error to " + target + ": " + ioe.toString());
/*     */     } 
/*     */     
/* 184 */     log("host is" + (reachable ? "" : " not") + " reachable", 3);
/* 185 */     return reachable;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsReachable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
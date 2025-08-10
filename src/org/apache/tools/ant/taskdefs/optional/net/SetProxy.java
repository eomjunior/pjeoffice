/*     */ package org.apache.tools.ant.taskdefs.optional.net;
/*     */ 
/*     */ import java.net.Authenticator;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetProxy
/*     */   extends Task
/*     */ {
/*     */   private static final int HTTP_PORT = 80;
/*     */   private static final int SOCKS_PORT = 1080;
/*  66 */   protected String proxyHost = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected int proxyPort = 80;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private String socksProxyHost = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private int socksProxyPort = 1080;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private String nonProxyHosts = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private String proxyUser = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private String proxyPassword = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyHost(String hostname) {
/* 108 */     this.proxyHost = hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyPort(int port) {
/* 118 */     this.proxyPort = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSocksProxyHost(String host) {
/* 128 */     this.socksProxyHost = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSocksProxyPort(int port) {
/* 138 */     this.socksProxyPort = port;
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
/*     */   public void setNonProxyHosts(String nonProxyHosts) {
/* 150 */     this.nonProxyHosts = nonProxyHosts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyUser(String proxyUser) {
/* 160 */     this.proxyUser = proxyUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyPassword(String proxyPassword) {
/* 169 */     this.proxyPassword = proxyPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyWebProxySettings() {
/* 179 */     boolean settingsChanged = false;
/* 180 */     boolean enablingProxy = false;
/* 181 */     Properties sysprops = System.getProperties();
/* 182 */     if (this.proxyHost != null) {
/* 183 */       settingsChanged = true;
/* 184 */       if (!this.proxyHost.isEmpty()) {
/* 185 */         traceSettingInfo();
/* 186 */         enablingProxy = true;
/* 187 */         sysprops.put("http.proxyHost", this.proxyHost);
/* 188 */         String portString = Integer.toString(this.proxyPort);
/* 189 */         sysprops.put("http.proxyPort", portString);
/* 190 */         sysprops.put("https.proxyHost", this.proxyHost);
/* 191 */         sysprops.put("https.proxyPort", portString);
/* 192 */         sysprops.put("ftp.proxyHost", this.proxyHost);
/* 193 */         sysprops.put("ftp.proxyPort", portString);
/* 194 */         if (this.nonProxyHosts != null) {
/* 195 */           sysprops.put("http.nonProxyHosts", this.nonProxyHosts);
/* 196 */           sysprops.put("https.nonProxyHosts", this.nonProxyHosts);
/* 197 */           sysprops.put("ftp.nonProxyHosts", this.nonProxyHosts);
/*     */         } 
/* 199 */         if (this.proxyUser != null) {
/* 200 */           sysprops.put("http.proxyUser", this.proxyUser);
/* 201 */           sysprops.put("http.proxyPassword", this.proxyPassword);
/*     */         } 
/*     */       } else {
/* 204 */         log("resetting http proxy", 3);
/* 205 */         sysprops.remove("http.proxyHost");
/* 206 */         sysprops.remove("http.proxyPort");
/* 207 */         sysprops.remove("http.proxyUser");
/* 208 */         sysprops.remove("http.proxyPassword");
/* 209 */         sysprops.remove("https.proxyHost");
/* 210 */         sysprops.remove("https.proxyPort");
/* 211 */         sysprops.remove("ftp.proxyHost");
/* 212 */         sysprops.remove("ftp.proxyPort");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 217 */     if (this.socksProxyHost != null) {
/* 218 */       settingsChanged = true;
/* 219 */       if (!this.socksProxyHost.isEmpty()) {
/* 220 */         enablingProxy = true;
/* 221 */         sysprops.put("socksProxyHost", this.socksProxyHost);
/* 222 */         sysprops.put("socksProxyPort", Integer.toString(this.socksProxyPort));
/* 223 */         if (this.proxyUser != null) {
/*     */           
/* 225 */           sysprops.put("java.net.socks.username", this.proxyUser);
/* 226 */           sysprops.put("java.net.socks.password", this.proxyPassword);
/*     */         } 
/*     */       } else {
/*     */         
/* 230 */         log("resetting socks proxy", 3);
/* 231 */         sysprops.remove("socksProxyHost");
/* 232 */         sysprops.remove("socksProxyPort");
/* 233 */         sysprops.remove("java.net.socks.username");
/* 234 */         sysprops.remove("java.net.socks.password");
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     if (this.proxyUser != null) {
/* 239 */       if (enablingProxy) {
/* 240 */         Authenticator.setDefault(new ProxyAuth(this.proxyUser, this.proxyPassword));
/*     */       }
/* 242 */       else if (settingsChanged) {
/* 243 */         Authenticator.setDefault(new ProxyAuth("", ""));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void traceSettingInfo() {
/* 252 */     log("Setting proxy to " + (
/* 253 */         (this.proxyHost != null) ? this.proxyHost : "''") + ":" + this.proxyPort, 3);
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
/*     */   public void execute() throws BuildException {
/* 265 */     applyWebProxySettings();
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ProxyAuth
/*     */     extends Authenticator
/*     */   {
/*     */     private PasswordAuthentication auth;
/*     */     
/*     */     private ProxyAuth(String user, String pass) {
/* 275 */       this.auth = new PasswordAuthentication(user, pass.toCharArray());
/*     */     }
/*     */ 
/*     */     
/*     */     protected PasswordAuthentication getPasswordAuthentication() {
/* 280 */       return this.auth;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/net/SetProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
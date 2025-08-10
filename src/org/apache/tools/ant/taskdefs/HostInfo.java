/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class HostInfo
/*     */   extends Task
/*     */ {
/*     */   private static final String DEF_REM_ADDR6 = "::";
/*     */   private static final String DEF_REM_ADDR4 = "0.0.0.0";
/*     */   private static final String DEF_LOCAL_ADDR6 = "::1";
/*     */   private static final String DEF_LOCAL_ADDR4 = "127.0.0.1";
/*     */   private static final String DEF_LOCAL_NAME = "localhost";
/*     */   private static final String DEF_DOMAIN = "localdomain";
/*     */   private static final String DOMAIN = "DOMAIN";
/*     */   private static final String NAME = "NAME";
/*     */   private static final String ADDR4 = "ADDR4";
/*     */   private static final String ADDR6 = "ADDR6";
/*  63 */   private String prefix = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private String host;
/*     */ 
/*     */   
/*     */   private InetAddress nameAddr;
/*     */ 
/*     */   
/*     */   private InetAddress best6;
/*     */ 
/*     */   
/*     */   private InetAddress best4;
/*     */ 
/*     */   
/*     */   private List<InetAddress> inetAddrs;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String aPrefix) {
/*  84 */     this.prefix = aPrefix;
/*  85 */     if (!this.prefix.endsWith(".")) {
/*  86 */       this.prefix += ".";
/*     */     }
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
/*     */   public void setHost(String aHost) {
/*  99 */     this.host = aHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 110 */     if (this.host == null || this.host.isEmpty()) {
/* 111 */       executeLocal();
/*     */     } else {
/* 113 */       executeRemote();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeLocal() {
/*     */     try {
/* 119 */       this.inetAddrs = new LinkedList<>();
/* 120 */       Collections.<NetworkInterface>list(NetworkInterface.getNetworkInterfaces())
/* 121 */         .forEach(netInterface -> this.inetAddrs.addAll(Collections.list(netInterface.getInetAddresses())));
/* 122 */       selectAddresses();
/*     */       
/* 124 */       if (this.nameAddr != null && hasHostName(this.nameAddr)) {
/* 125 */         setDomainAndName(this.nameAddr.getCanonicalHostName());
/*     */       } else {
/* 127 */         setProperty("DOMAIN", "localdomain");
/* 128 */         setProperty("NAME", "localhost");
/*     */       } 
/* 130 */       if (this.best4 != null) {
/* 131 */         setProperty("ADDR4", this.best4.getHostAddress());
/*     */       } else {
/* 133 */         setProperty("ADDR4", "127.0.0.1");
/*     */       } 
/* 135 */       if (this.best6 != null) {
/* 136 */         setProperty("ADDR6", this.best6.getHostAddress());
/*     */       } else {
/* 138 */         setProperty("ADDR6", "::1");
/*     */       } 
/* 140 */     } catch (Exception e) {
/* 141 */       log("Error retrieving local host information", e, 1);
/* 142 */       setProperty("DOMAIN", "localdomain");
/* 143 */       setProperty("NAME", "localhost");
/* 144 */       setProperty("ADDR4", "127.0.0.1");
/* 145 */       setProperty("ADDR6", "::1");
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasHostName(InetAddress addr) {
/* 150 */     return !addr.getHostAddress().equals(addr.getCanonicalHostName());
/*     */   }
/*     */   
/*     */   private void selectAddresses() {
/* 154 */     for (InetAddress current : this.inetAddrs) {
/* 155 */       if (!current.isMulticastAddress()) {
/* 156 */         if (current instanceof java.net.Inet4Address) {
/* 157 */           this.best4 = selectBestAddress(this.best4, current); continue;
/* 158 */         }  if (current instanceof java.net.Inet6Address) {
/* 159 */           this.best6 = selectBestAddress(this.best6, current);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     this.nameAddr = selectBestAddress(this.best4, this.best6);
/*     */   }
/*     */ 
/*     */   
/*     */   private InetAddress selectBestAddress(InetAddress bestSoFar, InetAddress current) {
/* 169 */     InetAddress best = bestSoFar;
/* 170 */     if (best == null) {
/*     */       
/* 172 */       best = current;
/* 173 */     } else if (current != null && !current.isLoopbackAddress()) {
/*     */       
/* 175 */       if (current.isLinkLocalAddress()) {
/*     */         
/* 177 */         if (best.isLoopbackAddress()) {
/* 178 */           best = current;
/*     */         }
/* 180 */       } else if (current.isSiteLocalAddress()) {
/*     */ 
/*     */ 
/*     */         
/* 184 */         if (best.isLoopbackAddress() || best
/* 185 */           .isLinkLocalAddress() || (best
/* 186 */           .isSiteLocalAddress() && !hasHostName(best))) {
/* 187 */           best = current;
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 194 */       else if (best.isLoopbackAddress() || best
/* 195 */         .isLinkLocalAddress() || best
/* 196 */         .isSiteLocalAddress() || 
/* 197 */         !hasHostName(best)) {
/* 198 */         best = current;
/*     */       } 
/*     */     } 
/* 201 */     return best;
/*     */   }
/*     */   
/*     */   private void executeRemote() {
/*     */     try {
/* 206 */       this.inetAddrs = Arrays.asList(InetAddress.getAllByName(this.host));
/*     */       
/* 208 */       selectAddresses();
/*     */       
/* 210 */       if (this.nameAddr != null && hasHostName(this.nameAddr)) {
/* 211 */         setDomainAndName(this.nameAddr.getCanonicalHostName());
/*     */       } else {
/* 213 */         setDomainAndName(this.host);
/*     */       } 
/* 215 */       if (this.best4 != null) {
/* 216 */         setProperty("ADDR4", this.best4.getHostAddress());
/*     */       } else {
/* 218 */         setProperty("ADDR4", "0.0.0.0");
/*     */       } 
/* 220 */       if (this.best6 != null) {
/* 221 */         setProperty("ADDR6", this.best6.getHostAddress());
/*     */       } else {
/* 223 */         setProperty("ADDR6", "::");
/*     */       } 
/* 225 */     } catch (Exception e) {
/* 226 */       log("Error retrieving remote host information for host:" + this.host + ".", e, 1);
/*     */       
/* 228 */       setDomainAndName(this.host);
/* 229 */       setProperty("ADDR4", "0.0.0.0");
/* 230 */       setProperty("ADDR6", "::");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setDomainAndName(String fqdn) {
/* 235 */     int idx = fqdn.indexOf('.');
/* 236 */     if (idx > 0) {
/* 237 */       setProperty("NAME", fqdn.substring(0, idx));
/* 238 */       setProperty("DOMAIN", fqdn.substring(idx + 1));
/*     */     } else {
/* 240 */       setProperty("NAME", fqdn);
/* 241 */       setProperty("DOMAIN", "localdomain");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setProperty(String name, String value) {
/* 246 */     getProject().setNewProperty(this.prefix + name, value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/HostInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
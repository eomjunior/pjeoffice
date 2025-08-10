/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class HostAndPort
/*     */   implements Serializable
/*     */ {
/*     */   private static final int NO_PORT = -1;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private final boolean hasBracketlessColons;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private HostAndPort(String host, int port, boolean hasBracketlessColons) {
/*  80 */     this.host = host;
/*  81 */     this.port = port;
/*  82 */     this.hasBracketlessColons = hasBracketlessColons;
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
/*     */   
/*     */   public String getHost() {
/*  95 */     return this.host;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPort() {
/* 100 */     return (this.port >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 111 */     Preconditions.checkState(hasPort());
/* 112 */     return this.port;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPortOrDefault(int defaultPort) {
/* 117 */     return hasPort() ? this.port : defaultPort;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostAndPort fromParts(String host, int port) {
/* 133 */     Preconditions.checkArgument(isValidPort(port), "Port out of range: %s", port);
/* 134 */     HostAndPort parsedHost = fromString(host);
/* 135 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 136 */     return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostAndPort fromHost(String host) {
/* 151 */     HostAndPort parsedHost = fromString(host);
/* 152 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 153 */     return parsedHost;
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static HostAndPort fromString(String hostPortString) {
/*     */     String host;
/* 168 */     Preconditions.checkNotNull(hostPortString);
/*     */     
/* 170 */     String portString = null;
/* 171 */     boolean hasBracketlessColons = false;
/*     */     
/* 173 */     if (hostPortString.startsWith("[")) {
/* 174 */       String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
/* 175 */       host = hostAndPort[0];
/* 176 */       portString = hostAndPort[1];
/*     */     } else {
/* 178 */       int colonPos = hostPortString.indexOf(':');
/* 179 */       if (colonPos >= 0 && hostPortString.indexOf(':', colonPos + 1) == -1) {
/*     */         
/* 181 */         host = hostPortString.substring(0, colonPos);
/* 182 */         portString = hostPortString.substring(colonPos + 1);
/*     */       } else {
/*     */         
/* 185 */         host = hostPortString;
/* 186 */         hasBracketlessColons = (colonPos >= 0);
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     int port = -1;
/* 191 */     if (!Strings.isNullOrEmpty(portString)) {
/*     */ 
/*     */       
/* 194 */       Preconditions.checkArgument((
/* 195 */           !portString.startsWith("+") && CharMatcher.ascii().matchesAllOf(portString)), "Unparseable port number: %s", hostPortString);
/*     */ 
/*     */       
/*     */       try {
/* 199 */         port = Integer.parseInt(portString);
/* 200 */       } catch (NumberFormatException e) {
/* 201 */         throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
/*     */       } 
/* 203 */       Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
/*     */     } 
/*     */     
/* 206 */     return new HostAndPort(host, port, hasBracketlessColons);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] getHostAndPortFromBracketedHost(String hostPortString) {
/* 217 */     Preconditions.checkArgument(
/* 218 */         (hostPortString.charAt(0) == '['), "Bracketed host-port string must start with a bracket: %s", hostPortString);
/*     */ 
/*     */     
/* 221 */     int colonIndex = hostPortString.indexOf(':');
/* 222 */     int closeBracketIndex = hostPortString.lastIndexOf(']');
/* 223 */     Preconditions.checkArgument((colonIndex > -1 && closeBracketIndex > colonIndex), "Invalid bracketed host/port: %s", hostPortString);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     String host = hostPortString.substring(1, closeBracketIndex);
/* 229 */     if (closeBracketIndex + 1 == hostPortString.length()) {
/* 230 */       return new String[] { host, "" };
/*     */     }
/* 232 */     Preconditions.checkArgument(
/* 233 */         (hostPortString.charAt(closeBracketIndex + 1) == ':'), "Only a colon may follow a close bracket: %s", hostPortString);
/*     */ 
/*     */     
/* 236 */     for (int i = closeBracketIndex + 2; i < hostPortString.length(); i++) {
/* 237 */       Preconditions.checkArgument(
/* 238 */           Character.isDigit(hostPortString.charAt(i)), "Port must be numeric: %s", hostPortString);
/*     */     }
/*     */ 
/*     */     
/* 242 */     return new String[] { host, hostPortString.substring(closeBracketIndex + 2) };
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
/*     */ 
/*     */   
/*     */   public HostAndPort withDefaultPort(int defaultPort) {
/* 256 */     Preconditions.checkArgument(isValidPort(defaultPort));
/* 257 */     if (hasPort()) {
/* 258 */       return this;
/*     */     }
/* 260 */     return new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public HostAndPort requireBracketsForIPv6() {
/* 279 */     Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object other) {
/* 285 */     if (this == other) {
/* 286 */       return true;
/*     */     }
/* 288 */     if (other instanceof HostAndPort) {
/* 289 */       HostAndPort that = (HostAndPort)other;
/* 290 */       return (Objects.equal(this.host, that.host) && this.port == that.port);
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 297 */     return Objects.hashCode(new Object[] { this.host, Integer.valueOf(this.port) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 304 */     StringBuilder builder = new StringBuilder(this.host.length() + 8);
/* 305 */     if (this.host.indexOf(':') >= 0) {
/* 306 */       builder.append('[').append(this.host).append(']');
/*     */     } else {
/* 308 */       builder.append(this.host);
/*     */     } 
/* 310 */     if (hasPort()) {
/* 311 */       builder.append(':').append(this.port);
/*     */     }
/* 313 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isValidPort(int port) {
/* 318 */     return (port >= 0 && port <= 65535);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/net/HostAndPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
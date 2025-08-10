/*     */ package org.apache.hc.core5.net;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ import org.apache.hc.core5.util.Tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class Host
/*     */   implements NamedEndpoint, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String name;
/*     */   private final String lcName;
/*     */   private final int port;
/*     */   
/*     */   public Host(String name, int port) {
/*  55 */     this.name = (String)Args.notNull(name, "Host name");
/*  56 */     this.port = Ports.checkWithDefault(port);
/*  57 */     this.lcName = TextUtils.toLowerCase(this.name);
/*     */   } static Host parse(CharSequence s, Tokenizer.Cursor cursor) throws URISyntaxException {
/*     */     String hostName;
/*     */     int port;
/*  61 */     Tokenizer tokenizer = Tokenizer.INSTANCE;
/*     */     
/*  63 */     boolean ipv6Brackets = (!cursor.atEnd() && s.charAt(cursor.getPos()) == '[');
/*  64 */     if (ipv6Brackets) {
/*  65 */       cursor.updatePos(cursor.getPos() + 1);
/*  66 */       hostName = tokenizer.parseContent(s, cursor, URISupport.IPV6_HOST_TERMINATORS);
/*  67 */       if (cursor.atEnd() || s.charAt(cursor.getPos()) != ']') {
/*  68 */         throw URISupport.createException(s, cursor, "Expected an IPv6 closing bracket ']'");
/*     */       }
/*  70 */       cursor.updatePos(cursor.getPos() + 1);
/*  71 */       if (!InetAddressUtils.isIPv6Address(hostName)) {
/*  72 */         throw URISupport.createException(s, cursor, "Expected an IPv6 address");
/*     */       }
/*     */     } else {
/*  75 */       hostName = tokenizer.parseContent(s, cursor, URISupport.PORT_SEPARATORS);
/*     */     } 
/*  77 */     String portText = null;
/*  78 */     if (!cursor.atEnd() && s.charAt(cursor.getPos()) == ':') {
/*  79 */       cursor.updatePos(cursor.getPos() + 1);
/*  80 */       portText = tokenizer.parseContent(s, cursor, URISupport.TERMINATORS);
/*     */     } 
/*     */     
/*  83 */     if (!TextUtils.isBlank(portText)) {
/*  84 */       if (!ipv6Brackets && portText.contains(":")) {
/*  85 */         throw URISupport.createException(s, cursor, "Expected IPv6 address to be enclosed in brackets");
/*     */       }
/*     */       try {
/*  88 */         port = Integer.parseInt(portText);
/*  89 */       } catch (NumberFormatException ex) {
/*  90 */         throw URISupport.createException(s, cursor, "Port is invalid");
/*     */       } 
/*     */     } else {
/*  93 */       port = -1;
/*     */     } 
/*  95 */     return new Host(hostName, port);
/*     */   }
/*     */   
/*     */   static Host parse(CharSequence s) throws URISyntaxException {
/*  99 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/* 100 */     return parse(s, cursor);
/*     */   }
/*     */   
/*     */   static void format(StringBuilder buf, NamedEndpoint endpoint) {
/* 104 */     String hostName = endpoint.getHostName();
/* 105 */     if (InetAddressUtils.isIPv6Address(hostName)) {
/* 106 */       buf.append('[').append(hostName).append(']');
/*     */     } else {
/* 108 */       buf.append(hostName);
/*     */     } 
/* 110 */     if (endpoint.getPort() != -1) {
/* 111 */       buf.append(":");
/* 112 */       buf.append(endpoint.getPort());
/*     */     } 
/*     */   }
/*     */   
/*     */   static void format(StringBuilder buf, Host host) {
/* 117 */     format(buf, host);
/*     */   }
/*     */   
/*     */   static String format(Host host) {
/* 121 */     StringBuilder buf = new StringBuilder();
/* 122 */     format(buf, host);
/* 123 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public static Host create(String s) throws URISyntaxException {
/* 127 */     Args.notEmpty(s, "HTTP Host");
/* 128 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/* 129 */     Host host = parse(s, cursor);
/* 130 */     if (TextUtils.isBlank(host.getHostName())) {
/* 131 */       throw URISupport.createException(s, cursor, "Hostname is invalid");
/*     */     }
/* 133 */     if (!cursor.atEnd()) {
/* 134 */       throw URISupport.createException(s, cursor, "Unexpected content");
/*     */     }
/* 136 */     return host;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHostName() {
/* 141 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 146 */     return this.port;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 151 */     if (this == o) {
/* 152 */       return true;
/*     */     }
/* 154 */     if (o instanceof Host) {
/* 155 */       Host that = (Host)o;
/* 156 */       return (this.lcName.equals(that.lcName) && this.port == that.port);
/*     */     } 
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 163 */     int hash = 17;
/* 164 */     hash = LangUtils.hashCode(hash, this.lcName);
/* 165 */     hash = LangUtils.hashCode(hash, this.port);
/* 166 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     return format(this);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/Host.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
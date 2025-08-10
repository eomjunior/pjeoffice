/*     */ package org.apache.hc.core5.net;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Objects;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class URIAuthority
/*     */   implements NamedEndpoint, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String userInfo;
/*     */   private final Host host;
/*     */   
/*     */   static URIAuthority parse(CharSequence s, Tokenizer.Cursor cursor) throws URISyntaxException {
/*  54 */     Tokenizer tokenizer = Tokenizer.INSTANCE;
/*  55 */     String userInfo = null;
/*  56 */     int initPos = cursor.getPos();
/*  57 */     String token = tokenizer.parseContent(s, cursor, URISupport.HOST_SEPARATORS);
/*  58 */     if (!cursor.atEnd() && s.charAt(cursor.getPos()) == '@') {
/*  59 */       cursor.updatePos(cursor.getPos() + 1);
/*  60 */       if (!TextUtils.isBlank(token)) {
/*  61 */         userInfo = token;
/*     */       }
/*     */     } else {
/*     */       
/*  65 */       cursor.updatePos(initPos);
/*     */     } 
/*  67 */     Host host = Host.parse(s, cursor);
/*  68 */     return new URIAuthority(userInfo, host);
/*     */   }
/*     */   
/*     */   static URIAuthority parse(CharSequence s) throws URISyntaxException {
/*  72 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/*  73 */     return parse(s, cursor);
/*     */   }
/*     */   
/*     */   static void format(StringBuilder buf, URIAuthority uriAuthority) {
/*  77 */     if (uriAuthority.getUserInfo() != null) {
/*  78 */       buf.append(uriAuthority.getUserInfo());
/*  79 */       buf.append("@");
/*     */     } 
/*  81 */     Host.format(buf, uriAuthority);
/*     */   }
/*     */   
/*     */   static String format(URIAuthority uriAuthority) {
/*  85 */     StringBuilder buf = new StringBuilder();
/*  86 */     format(buf, uriAuthority);
/*  87 */     return buf.toString();
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
/*     */   public URIAuthority(String userInfo, String hostname, int port) {
/*  99 */     this.userInfo = userInfo;
/* 100 */     this.host = new Host(hostname, port);
/*     */   }
/*     */   
/*     */   public URIAuthority(String hostname, int port) {
/* 104 */     this(null, hostname, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIAuthority(String userInfo, Host host) {
/* 112 */     Args.notNull(host, "Host");
/* 113 */     this.userInfo = userInfo;
/* 114 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIAuthority(Host host) {
/* 121 */     this((String)null, host);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIAuthority(String userInfo, NamedEndpoint endpoint) {
/* 129 */     Args.notNull(endpoint, "Endpoint");
/* 130 */     this.userInfo = userInfo;
/* 131 */     this.host = new Host(endpoint.getHostName(), endpoint.getPort());
/*     */   }
/*     */   
/*     */   public URIAuthority(NamedEndpoint namedEndpoint) {
/* 135 */     this((String)null, namedEndpoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URIAuthority create(String s) throws URISyntaxException {
/* 142 */     if (TextUtils.isBlank(s)) {
/* 143 */       return null;
/*     */     }
/* 145 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/* 146 */     URIAuthority uriAuthority = parse(s, cursor);
/* 147 */     if (!cursor.atEnd()) {
/* 148 */       throw URISupport.createException(s, cursor, "Unexpected content");
/*     */     }
/* 150 */     return uriAuthority;
/*     */   }
/*     */   
/*     */   public URIAuthority(String hostname) {
/* 154 */     this(null, hostname, -1);
/*     */   }
/*     */   
/*     */   public String getUserInfo() {
/* 158 */     return this.userInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHostName() {
/* 163 */     return this.host.getHostName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 168 */     return this.host.getPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 173 */     return format(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 178 */     if (this == obj) {
/* 179 */       return true;
/*     */     }
/* 181 */     if (obj instanceof URIAuthority) {
/* 182 */       URIAuthority that = (URIAuthority)obj;
/* 183 */       return (Objects.equals(this.userInfo, that.userInfo) && 
/* 184 */         Objects.equals(this.host, that.host));
/*     */     } 
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 191 */     int hash = 17;
/* 192 */     hash = LangUtils.hashCode(hash, this.userInfo);
/* 193 */     hash = LangUtils.hashCode(hash, this.host);
/* 194 */     return hash;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/URIAuthority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
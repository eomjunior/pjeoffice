/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class StatusLine
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2443303766890459269L;
/*     */   private final ProtocolVersion protoVersion;
/*     */   private final int statusCode;
/*     */   private final StatusClass statusClass;
/*     */   private final String reasonPhrase;
/*     */   
/*     */   public StatusLine(HttpResponse response) {
/*  71 */     Args.notNull(response, "Response");
/*  72 */     this.protoVersion = (response.getVersion() != null) ? response.getVersion() : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*  73 */     this.statusCode = response.getCode();
/*  74 */     this.statusClass = StatusClass.from(this.statusCode);
/*  75 */     this.reasonPhrase = response.getReasonPhrase();
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
/*     */   public StatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
/*  88 */     this.statusCode = Args.notNegative(statusCode, "Status code");
/*  89 */     this.statusClass = StatusClass.from(this.statusCode);
/*  90 */     this.protoVersion = (version != null) ? version : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*  91 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */   
/*     */   public int getStatusCode() {
/*  95 */     return this.statusCode;
/*     */   }
/*     */   
/*     */   public StatusClass getStatusClass() {
/*  99 */     return this.statusClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInformational() {
/* 108 */     return (getStatusClass() == StatusClass.INFORMATIONAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuccessful() {
/* 117 */     return (getStatusClass() == StatusClass.SUCCESSFUL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirection() {
/* 126 */     return (getStatusClass() == StatusClass.REDIRECTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClientError() {
/* 135 */     return (getStatusClass() == StatusClass.CLIENT_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isServerError() {
/* 144 */     return (getStatusClass() == StatusClass.SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isError() {
/* 154 */     return (isClientError() || isServerError());
/*     */   }
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 158 */     return this.protoVersion;
/*     */   }
/*     */   
/*     */   public String getReasonPhrase() {
/* 162 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     StringBuilder buf = new StringBuilder();
/* 168 */     buf.append(this.protoVersion).append(" ").append(this.statusCode).append(" ");
/* 169 */     if (this.reasonPhrase != null) {
/* 170 */       buf.append(this.reasonPhrase);
/*     */     }
/* 172 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum StatusClass
/*     */   {
/* 183 */     INFORMATIONAL,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     SUCCESSFUL,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     REDIRECTION,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     CLIENT_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     SERVER_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     OTHER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static StatusClass from(int statusCode) {
/* 219 */       switch (statusCode / 100)
/*     */       { case 1:
/* 221 */           statusClass = INFORMATIONAL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 240 */           return statusClass;case 2: statusClass = SUCCESSFUL; return statusClass;case 3: statusClass = REDIRECTION; return statusClass;case 4: statusClass = CLIENT_ERROR; return statusClass;case 5: statusClass = SERVER_ERROR; return statusClass; }  StatusClass statusClass = OTHER; return statusClass;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/StatusLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
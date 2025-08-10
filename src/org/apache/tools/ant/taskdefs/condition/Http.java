/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Locale;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.taskdefs.Get;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Http
/*     */   extends ProjectComponent
/*     */   implements Condition
/*     */ {
/*     */   private static final int ERROR_BEGINS = 400;
/*     */   private static final String DEFAULT_REQUEST_METHOD = "GET";
/*     */   private static final String HTTP = "http";
/*     */   private static final String HTTPS = "https";
/*  48 */   private String spec = null;
/*  49 */   private String requestMethod = "GET";
/*     */   
/*     */   private boolean followRedirects = true;
/*  52 */   private int errorsBeginAt = 400;
/*  53 */   private int readTimeout = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/*  60 */     this.spec = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorsBeginAt(int errorsBeginAt) {
/*  69 */     this.errorsBeginAt = errorsBeginAt;
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
/*     */   public void setRequestMethod(String method) {
/*  85 */     this
/*  86 */       .requestMethod = (method == null) ? "GET" : method.toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFollowRedirects(boolean f) {
/*  97 */     this.followRedirects = f;
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
/*     */   public void setReadTimeout(int t) {
/* 109 */     if (t >= 0) {
/* 110 */       this.readTimeout = t;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/* 120 */     if (this.spec == null) {
/* 121 */       throw new BuildException("No url specified in http condition");
/*     */     }
/* 123 */     log("Checking for " + this.spec, 3);
/*     */     try {
/* 125 */       URL url = new URL(this.spec);
/*     */       try {
/* 127 */         URLConnection conn = url.openConnection();
/* 128 */         if (conn instanceof HttpURLConnection) {
/* 129 */           int code = request((HttpURLConnection)conn, url);
/* 130 */           log("Result code for " + this.spec + " was " + code, 3);
/*     */           
/* 132 */           return (code > 0 && code < this.errorsBeginAt);
/*     */         } 
/* 134 */       } catch (ProtocolException pe) {
/* 135 */         throw new BuildException("Invalid HTTP protocol: " + this.requestMethod, pe);
/*     */       }
/* 137 */       catch (IOException e) {
/* 138 */         return false;
/*     */       } 
/* 140 */     } catch (MalformedURLException e) {
/* 141 */       throw new BuildException("Badly formed URL: " + this.spec, e);
/*     */     } 
/* 143 */     return true;
/*     */   }
/*     */   
/*     */   private int request(HttpURLConnection http, URL url) throws IOException {
/* 147 */     http.setRequestMethod(this.requestMethod);
/* 148 */     http.setInstanceFollowRedirects(this.followRedirects);
/* 149 */     http.setReadTimeout(this.readTimeout);
/* 150 */     int firstStatusCode = http.getResponseCode();
/* 151 */     if (this.followRedirects && Get.isMoved(firstStatusCode)) {
/* 152 */       String newLocation = http.getHeaderField("Location");
/* 153 */       URL newURL = new URL(newLocation);
/* 154 */       if (redirectionAllowed(url, newURL)) {
/* 155 */         URLConnection newConn = newURL.openConnection();
/* 156 */         if (newConn instanceof HttpURLConnection) {
/* 157 */           log("Following redirect from " + url + " to " + newURL);
/* 158 */           return request((HttpURLConnection)newConn, newURL);
/*     */         } 
/*     */       } 
/*     */     } 
/* 162 */     return firstStatusCode;
/*     */   }
/*     */   
/*     */   private boolean redirectionAllowed(URL from, URL to) {
/* 166 */     if (from.equals(to))
/*     */     {
/* 168 */       return false;
/*     */     }
/* 170 */     if (!from.getProtocol().equals(to.getProtocol()) && (
/* 171 */       !"http".equals(from.getProtocol()) || 
/* 172 */       !"https".equals(to.getProtocol()))) {
/* 173 */       log("Redirection detected from " + from
/* 174 */           .getProtocol() + " to " + to.getProtocol() + ". Protocol switch unsafe, not allowed.");
/*     */       
/* 176 */       return false;
/*     */     } 
/* 178 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Http.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
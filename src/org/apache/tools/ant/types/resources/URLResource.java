/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URLResource
/*     */   extends Resource
/*     */   implements URLProvider
/*     */ {
/*  41 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  43 */   private static final int NULL_URL = Resource.getMagicNumber("null URL".getBytes());
/*     */ 
/*     */   
/*     */   private URL url;
/*     */ 
/*     */   
/*     */   private URLConnection conn;
/*     */ 
/*     */   
/*     */   private URL baseURL;
/*     */   
/*     */   private String relPath;
/*     */ 
/*     */   
/*     */   public URLResource() {}
/*     */ 
/*     */   
/*     */   public URLResource(URL u) {
/*  61 */     setURL(u);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLResource(URLProvider u) {
/*  69 */     setURL(u.getURL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLResource(File f) {
/*  77 */     setFile(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLResource(String u) {
/*  86 */     this(newURL(u));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setURL(URL u) {
/*  94 */     checkAttributesAllowed();
/*  95 */     this.url = u;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setFile(File f) {
/*     */     try {
/* 104 */       setURL(FILE_UTILS.getFileURL(f));
/* 105 */     } catch (MalformedURLException e) {
/* 106 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setBaseURL(URL base) {
/* 117 */     checkAttributesAllowed();
/* 118 */     if (this.url != null) {
/* 119 */       throw new BuildException("can't define URL and baseURL attribute");
/*     */     }
/* 121 */     this.baseURL = base;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setRelativePath(String r) {
/* 131 */     checkAttributesAllowed();
/* 132 */     if (this.url != null) {
/* 133 */       throw new BuildException("can't define URL and relativePath attribute");
/*     */     }
/*     */     
/* 136 */     this.relPath = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized URL getURL() {
/* 145 */     if (isReference()) {
/* 146 */       return getRef().getURL();
/*     */     }
/* 148 */     if (this.url == null && 
/* 149 */       this.baseURL != null) {
/* 150 */       if (this.relPath == null) {
/* 151 */         throw new BuildException("must provide relativePath attribute when using baseURL.");
/*     */       }
/*     */       
/*     */       try {
/* 155 */         this.url = new URL(this.baseURL, this.relPath);
/* 156 */       } catch (MalformedURLException e) {
/* 157 */         throw new BuildException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setRefid(Reference r) {
/* 170 */     if (this.url != null || this.baseURL != null || this.relPath != null) {
/* 171 */       throw tooManyAttributes();
/*     */     }
/* 173 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getName() {
/* 182 */     if (isReference()) {
/* 183 */       return getRef().getName();
/*     */     }
/* 185 */     String name = getURL().getFile();
/* 186 */     return name.isEmpty() ? name : name.substring(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 194 */     return isReference() ? 
/* 195 */       getRef().toString() : String.valueOf(getURL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isExists() {
/* 203 */     if (isReference()) {
/* 204 */       return getRef().isExists();
/*     */     }
/* 206 */     return isExists(false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized boolean isExists(boolean closeConnection) {
/* 228 */     if (getURL() == null) {
/* 229 */       return false;
/*     */     }
/*     */     try {
/* 232 */       connect(3);
/* 233 */       if (this.conn instanceof HttpURLConnection) {
/* 234 */         int sc = ((HttpURLConnection)this.conn).getResponseCode();
/*     */         
/* 236 */         return (sc < 400);
/* 237 */       }  if (this.url.getProtocol().startsWith("ftp")) {
/* 238 */         closeConnection = true;
/* 239 */         InputStream in = null;
/*     */         try {
/* 241 */           in = this.conn.getInputStream();
/*     */         } finally {
/* 243 */           FileUtils.close(in);
/*     */         } 
/*     */       } 
/* 246 */       return true;
/* 247 */     } catch (IOException e) {
/* 248 */       return false;
/*     */     } finally {
/* 250 */       if (closeConnection) {
/* 251 */         close();
/*     */       }
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
/*     */   public synchronized long getLastModified() {
/* 264 */     if (isReference()) {
/* 265 */       return getRef().getLastModified();
/*     */     }
/* 267 */     if (!isExists(false)) {
/* 268 */       return 0L;
/*     */     }
/* 270 */     return withConnection(c -> this.conn.getLastModified(), 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isDirectory() {
/* 278 */     return isReference() ? 
/* 279 */       getRef().isDirectory() : 
/* 280 */       getName().endsWith("/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getSize() {
/* 289 */     if (isReference()) {
/* 290 */       return getRef().getSize();
/*     */     }
/* 292 */     if (!isExists(false)) {
/* 293 */       return 0L;
/*     */     }
/* 295 */     return withConnection(c -> this.conn.getContentLength(), -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean equals(Object another) {
/* 304 */     if (this == another) {
/* 305 */       return true;
/*     */     }
/* 307 */     if (isReference()) {
/* 308 */       return getRef().equals(another);
/*     */     }
/* 310 */     if (another == null || another.getClass() != getClass()) {
/* 311 */       return false;
/*     */     }
/* 313 */     URLResource other = (URLResource)another;
/* 314 */     return (getURL() == null) ? (
/* 315 */       (other.getURL() == null)) : 
/* 316 */       getURL().equals(other.getURL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int hashCode() {
/* 324 */     if (isReference()) {
/* 325 */       return getRef().hashCode();
/*     */     }
/* 327 */     return MAGIC * ((getURL() == null) ? NULL_URL : getURL().hashCode());
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
/*     */   public synchronized InputStream getInputStream() throws IOException {
/* 339 */     if (isReference()) {
/* 340 */       return getRef().getInputStream();
/*     */     }
/* 342 */     connect();
/*     */     try {
/* 344 */       return this.conn.getInputStream();
/*     */     } finally {
/* 346 */       this.conn = null;
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
/*     */   
/*     */   public synchronized OutputStream getOutputStream() throws IOException {
/* 360 */     if (isReference()) {
/* 361 */       return getRef().getOutputStream();
/*     */     }
/* 363 */     connect();
/*     */     try {
/* 365 */       return this.conn.getOutputStream();
/*     */     } finally {
/* 367 */       this.conn = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void connect() throws IOException {
/* 376 */     connect(0);
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
/*     */   protected synchronized void connect(int logLevel) throws IOException {
/* 388 */     URL u = getURL();
/* 389 */     if (u == null) {
/* 390 */       throw new BuildException("URL not set");
/*     */     }
/* 392 */     if (this.conn == null) {
/*     */       try {
/* 394 */         this.conn = u.openConnection();
/* 395 */         this.conn.connect();
/* 396 */       } catch (IOException e) {
/* 397 */         log(e.toString(), logLevel);
/* 398 */         this.conn = null;
/* 399 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected URLResource getRef() {
/* 406 */     return (URLResource)getCheckedRef(URLResource.class);
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
/*     */   private synchronized void close() {
/*     */     try {
/* 419 */       FileUtils.close(this.conn);
/*     */     } finally {
/* 421 */       this.conn = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static URL newURL(String u) {
/*     */     try {
/* 427 */       return new URL(u);
/* 428 */     } catch (MalformedURLException e) {
/* 429 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long withConnection(ConnectionUser u, long defaultValue) {
/*     */     try {
/* 439 */       if (this.conn != null) {
/* 440 */         return u.useConnection(this.conn);
/*     */       }
/*     */       try {
/* 443 */         connect();
/* 444 */         return u.useConnection(this.conn);
/*     */       } finally {
/* 446 */         close();
/*     */       }
/*     */     
/* 449 */     } catch (IOException ex) {
/* 450 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static interface ConnectionUser {
/*     */     long useConnection(URLConnection param1URLConnection);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/URLResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
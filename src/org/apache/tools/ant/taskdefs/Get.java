/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Main;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.email.Header;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.types.resources.URLProvider;
/*     */ import org.apache.tools.ant.types.resources.URLResource;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Get
/*     */   extends Task
/*     */ {
/*     */   private static final int NUMBER_RETRIES = 3;
/*     */   private static final int DOTS_PER_LINE = 50;
/*     */   private static final int BIG_BUFFER_SIZE = 102400;
/*  65 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private static final int REDIRECT_LIMIT = 25;
/*     */   
/*     */   private static final int HTTP_MOVED_TEMP = 307;
/*     */   
/*     */   private static final String HTTP = "http";
/*     */   
/*     */   private static final String HTTPS = "https";
/*     */   
/*     */   private static final String DEFAULT_AGENT_PREFIX = "Apache Ant";
/*     */   private static final String GZIP_CONTENT_ENCODING = "gzip";
/*  77 */   private final Resources sources = new Resources();
/*     */   private File destination;
/*     */   private boolean verbose = false;
/*     */   private boolean quiet = false;
/*     */   private boolean useTimestamp = false;
/*     */   private boolean ignoreErrors = false;
/*  83 */   private String uname = null;
/*  84 */   private String pword = null;
/*  85 */   private long maxTime = 0L;
/*  86 */   private int numberRetries = 3;
/*     */   private boolean skipExisting = false;
/*     */   private boolean httpUseCaches = true;
/*     */   private boolean tryGzipEncoding = false;
/*  90 */   private Mapper mapperElement = null;
/*     */   
/*  92 */   private String userAgent = System.getProperty("ant.http.agent", "Apache Ant/" + 
/*     */       
/*  94 */       Main.getShortAntVersion());
/*     */ 
/*     */   
/*  97 */   private Map<String, String> headers = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 106 */     checkAttributes();
/*     */     
/* 108 */     for (Resource r : this.sources) {
/* 109 */       URLProvider up = (URLProvider)r.as(URLProvider.class);
/* 110 */       URL source = up.getURL();
/*     */       
/* 112 */       File dest = this.destination;
/* 113 */       if (this.destination.isDirectory()) {
/* 114 */         if (this.mapperElement == null) {
/* 115 */           String path = source.getPath();
/* 116 */           if (path.endsWith("/")) {
/* 117 */             path = path.substring(0, path.length() - 1);
/*     */           }
/* 119 */           int slash = path.lastIndexOf('/');
/* 120 */           if (slash > -1) {
/* 121 */             path = path.substring(slash + 1);
/*     */           }
/* 123 */           dest = new File(this.destination, path);
/*     */         } else {
/* 125 */           FileNameMapper mapper = this.mapperElement.getImplementation();
/* 126 */           String[] d = mapper.mapFileName(source.toString());
/* 127 */           if (d == null) {
/* 128 */             log("skipping " + r + " - mapper can't handle it", 1);
/*     */             
/*     */             continue;
/*     */           } 
/* 132 */           if (d.length == 0) {
/* 133 */             log("skipping " + r + " - mapper returns no file name", 1);
/*     */             
/*     */             continue;
/*     */           } 
/* 137 */           if (d.length > 1) {
/* 138 */             log("skipping " + r + " - mapper returns multiple file names", 1);
/*     */             
/*     */             continue;
/*     */           } 
/* 142 */           dest = new File(this.destination, d[0]);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 147 */       int logLevel = 2;
/* 148 */       DownloadProgress progress = null;
/* 149 */       if (this.verbose) {
/* 150 */         progress = new VerboseProgress(System.out);
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 155 */         doGet(source, dest, 2, progress);
/* 156 */       } catch (IOException ioe) {
/* 157 */         log("Error getting " + source + " to " + dest);
/* 158 */         if (!this.ignoreErrors) {
/* 159 */           throw new BuildException(ioe, getLocation());
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean doGet(int logLevel, DownloadProgress progress) throws IOException {
/* 182 */     checkAttributes();
/* 183 */     return doGet(((URLProvider)((Resource)this.sources.iterator().next()).as(URLProvider.class)).getURL(), this.destination, logLevel, progress);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doGet(URL source, File dest, int logLevel, DownloadProgress progress) throws IOException {
/* 209 */     if (dest.exists() && this.skipExisting) {
/* 210 */       log("Destination already exists (skipping): " + dest
/* 211 */           .getAbsolutePath(), logLevel);
/* 212 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 216 */     if (progress == null) {
/* 217 */       progress = new NullProgress();
/*     */     }
/* 219 */     log("Getting: " + source, logLevel);
/* 220 */     log("To: " + dest.getAbsolutePath(), logLevel);
/*     */ 
/*     */     
/* 223 */     long timestamp = 0L;
/*     */     
/* 225 */     boolean hasTimestamp = false;
/* 226 */     if (this.useTimestamp && dest.exists()) {
/* 227 */       timestamp = dest.lastModified();
/* 228 */       if (this.verbose) {
/* 229 */         Date t = new Date(timestamp);
/* 230 */         log("local file date : " + t.toString(), logLevel);
/*     */       } 
/* 232 */       hasTimestamp = true;
/*     */     } 
/*     */     
/* 235 */     GetThread getThread = new GetThread(source, dest, hasTimestamp, timestamp, progress, logLevel, this.userAgent);
/*     */ 
/*     */     
/* 238 */     getThread.setDaemon(true);
/* 239 */     getProject().registerThreadTask(getThread, this);
/* 240 */     getThread.start();
/*     */     try {
/* 242 */       getThread.join(this.maxTime * 1000L);
/* 243 */     } catch (InterruptedException ie) {
/* 244 */       log("interrupted waiting for GET to finish", 3);
/*     */     } 
/*     */ 
/*     */     
/* 248 */     if (getThread.isAlive()) {
/* 249 */       String msg = "The GET operation took longer than " + this.maxTime + " seconds, stopping it.";
/*     */       
/* 251 */       if (this.ignoreErrors) {
/* 252 */         log(msg);
/*     */       }
/* 254 */       getThread.closeStreams();
/* 255 */       if (!this.ignoreErrors) {
/* 256 */         throw new BuildException(msg);
/*     */       }
/* 258 */       return false;
/*     */     } 
/*     */     
/* 261 */     return getThread.wasSuccessful();
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String msg, int msgLevel) {
/* 266 */     if (!this.quiet || msgLevel <= 0) {
/* 267 */       super.log(msg, msgLevel);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAttributes() {
/* 276 */     if (this.userAgent == null || this.userAgent.trim().isEmpty()) {
/* 277 */       throw new BuildException("userAgent may not be null or empty");
/*     */     }
/*     */     
/* 280 */     if (this.sources.size() == 0) {
/* 281 */       throw new BuildException("at least one source is required", 
/* 282 */           getLocation());
/*     */     }
/* 284 */     for (Resource r : this.sources) {
/* 285 */       URLProvider up = (URLProvider)r.as(URLProvider.class);
/* 286 */       if (up == null) {
/* 287 */         throw new BuildException("Only URLProvider resources are supported", 
/* 288 */             getLocation());
/*     */       }
/*     */     } 
/*     */     
/* 292 */     if (this.destination == null) {
/* 293 */       throw new BuildException("dest attribute is required", getLocation());
/*     */     }
/*     */     
/* 296 */     if (this.destination.exists() && this.sources.size() > 1 && 
/* 297 */       !this.destination.isDirectory()) {
/* 298 */       throw new BuildException("The specified destination is not a directory", 
/* 299 */           getLocation());
/*     */     }
/*     */     
/* 302 */     if (this.destination.exists() && !this.destination.canWrite()) {
/* 303 */       throw new BuildException("Can't write to " + this.destination
/* 304 */           .getAbsolutePath(), 
/* 305 */           getLocation());
/*     */     }
/*     */     
/* 308 */     if (this.sources.size() > 1 && !this.destination.exists()) {
/* 309 */       this.destination.mkdirs();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(URL u) {
/* 319 */     add((ResourceCollection)new URLResource(u));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 328 */     this.sources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dest) {
/* 337 */     this.destination = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean v) {
/* 346 */     this.verbose = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuiet(boolean v) {
/* 356 */     this.quiet = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreErrors(boolean v) {
/* 365 */     this.ignoreErrors = v;
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
/*     */   public void setUseTimestamp(boolean v) {
/* 387 */     this.useTimestamp = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsername(String u) {
/* 396 */     this.uname = u;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String p) {
/* 405 */     this.pword = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTime(long maxTime) {
/* 416 */     this.maxTime = maxTime;
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
/*     */   public void setRetries(int r) {
/* 430 */     if (r <= 0) {
/* 431 */       log("Setting retries to " + r + " will make the task not even try to reach the URI at all", 1);
/*     */     }
/*     */ 
/*     */     
/* 435 */     this.numberRetries = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipExisting(boolean s) {
/* 445 */     this.skipExisting = s;
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
/*     */   public void setUserAgent(String userAgent) {
/* 458 */     this.userAgent = userAgent;
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
/*     */   public void setHttpUseCaches(boolean httpUseCache) {
/* 473 */     this.httpUseCaches = httpUseCache;
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
/*     */   public void setTryGzipEncoding(boolean b) {
/* 487 */     this.tryGzipEncoding = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredHeader(Header header) {
/* 496 */     if (header != null) {
/* 497 */       String key = StringUtils.trimToNull(header.getName());
/* 498 */       String value = StringUtils.trimToNull(header.getValue());
/* 499 */       if (key != null && value != null) {
/* 500 */         this.headers.put(key, value);
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
/*     */   public Mapper createMapper() throws BuildException {
/* 512 */     if (this.mapperElement != null) {
/* 513 */       throw new BuildException("Cannot define more than one mapper", 
/* 514 */           getLocation());
/*     */     }
/* 516 */     this.mapperElement = new Mapper(getProject());
/* 517 */     return this.mapperElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 526 */     createMapper().add(fileNameMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class Base64Converter
/*     */     extends org.apache.tools.ant.util.Base64Converter {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMoved(int responseCode) {
/* 542 */     return (responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface DownloadProgress
/*     */   {
/*     */     void beginDownload();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void onTick();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void endDownload();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NullProgress
/*     */     implements DownloadProgress
/*     */   {
/*     */     public void beginDownload() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onTick() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void endDownload() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VerboseProgress
/*     */     implements DownloadProgress
/*     */   {
/* 602 */     private int dots = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     PrintStream out;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public VerboseProgress(PrintStream out) {
/* 612 */       this.out = out;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void beginDownload() {
/* 620 */       this.dots = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onTick() {
/* 629 */       this.out.print(".");
/* 630 */       if (this.dots++ > 50) {
/* 631 */         this.out.flush();
/* 632 */         this.dots = 0;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void endDownload() {
/* 641 */       this.out.println();
/* 642 */       this.out.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   private class GetThread
/*     */     extends Thread
/*     */   {
/*     */     private final URL source;
/*     */     private final File dest;
/*     */     private final boolean hasTimestamp;
/*     */     private final long timestamp;
/*     */     private final Get.DownloadProgress progress;
/*     */     private final int logLevel;
/*     */     private boolean success = false;
/* 656 */     private IOException ioexception = null;
/* 657 */     private BuildException exception = null;
/* 658 */     private InputStream is = null;
/* 659 */     private OutputStream os = null;
/*     */     private URLConnection connection;
/* 661 */     private int redirections = 0;
/* 662 */     private String userAgent = null;
/*     */ 
/*     */     
/*     */     GetThread(URL source, File dest, boolean h, long t, Get.DownloadProgress p, int l, String userAgent) {
/* 666 */       this.source = source;
/* 667 */       this.dest = dest;
/* 668 */       this.hasTimestamp = h;
/* 669 */       this.timestamp = t;
/* 670 */       this.progress = p;
/* 671 */       this.logLevel = l;
/* 672 */       this.userAgent = userAgent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 678 */         this.success = get();
/* 679 */       } catch (IOException ioex) {
/* 680 */         this.ioexception = ioex;
/* 681 */       } catch (BuildException bex) {
/* 682 */         this.exception = bex;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean get() throws IOException, BuildException {
/* 688 */       this.connection = openConnection(this.source);
/*     */       
/* 690 */       if (this.connection == null) {
/* 691 */         return false;
/*     */       }
/*     */       
/* 694 */       boolean downloadSucceeded = downloadFile();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 699 */       if (downloadSucceeded && Get.this.useTimestamp) {
/* 700 */         updateTimeStamp();
/*     */       }
/*     */       
/* 703 */       return downloadSucceeded;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean redirectionAllowed(URL aSource, URL aDest) {
/* 708 */       if (!aSource.getProtocol().equals(aDest.getProtocol()) && (
/* 709 */         !"http".equals(aSource.getProtocol()) || !"https".equals(aDest
/* 710 */           .getProtocol()))) {
/*     */         
/* 712 */         String message = "Redirection detected from " + aSource.getProtocol() + " to " + aDest.getProtocol() + ". Protocol switch unsafe, not allowed.";
/*     */         
/* 714 */         if (Get.this.ignoreErrors) {
/* 715 */           Get.this.log(message, this.logLevel);
/* 716 */           return false;
/*     */         } 
/* 718 */         throw new BuildException(message);
/*     */       } 
/*     */ 
/*     */       
/* 722 */       this.redirections++;
/* 723 */       if (this.redirections > 25) {
/* 724 */         String message = "More than 25 times redirected, giving up";
/*     */         
/* 726 */         if (Get.this.ignoreErrors) {
/* 727 */           Get.this.log("More than 25 times redirected, giving up", this.logLevel);
/* 728 */           return false;
/*     */         } 
/* 730 */         throw new BuildException("More than 25 times redirected, giving up");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 735 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private URLConnection openConnection(URL aSource) throws IOException {
/* 741 */       URLConnection connection = aSource.openConnection();
/*     */ 
/*     */       
/* 744 */       if (this.hasTimestamp) {
/* 745 */         connection.setIfModifiedSince(this.timestamp);
/*     */       }
/*     */       
/* 748 */       connection.addRequestProperty("User-Agent", this.userAgent);
/*     */ 
/*     */       
/* 751 */       if (Get.this.uname != null || Get.this.pword != null) {
/* 752 */         String up = Get.this.uname + ":" + Get.this.pword;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 757 */         Get.Base64Converter encoder = new Get.Base64Converter();
/* 758 */         String encoding = encoder.encode(up.getBytes());
/* 759 */         connection.setRequestProperty("Authorization", "Basic " + encoding);
/*     */       } 
/*     */       
/* 762 */       if (Get.this.tryGzipEncoding) {
/* 763 */         connection.setRequestProperty("Accept-Encoding", "gzip");
/*     */       }
/*     */       
/* 766 */       for (Map.Entry<String, String> header : (Iterable<Map.Entry<String, String>>)Get.this.headers.entrySet()) {
/*     */         
/* 768 */         Get.this.log(String.format("Adding header '%s' ", new Object[] { header.getKey() }));
/* 769 */         connection.setRequestProperty(header.getKey(), header.getValue());
/*     */       } 
/*     */       
/* 772 */       if (connection instanceof HttpURLConnection) {
/* 773 */         ((HttpURLConnection)connection).setInstanceFollowRedirects(false);
/* 774 */         connection.setUseCaches(Get.this.httpUseCaches);
/*     */       } 
/*     */       
/*     */       try {
/* 778 */         connection.connect();
/* 779 */       } catch (NullPointerException e) {
/*     */         
/* 781 */         throw new BuildException("Failed to parse " + this.source.toString(), e);
/*     */       } 
/*     */ 
/*     */       
/* 785 */       if (connection instanceof HttpURLConnection) {
/* 786 */         HttpURLConnection httpConnection = (HttpURLConnection)connection;
/* 787 */         int responseCode = httpConnection.getResponseCode();
/* 788 */         if (Get.isMoved(responseCode)) {
/* 789 */           String newLocation = httpConnection.getHeaderField("Location");
/*     */ 
/*     */           
/* 792 */           String message = aSource + ((responseCode == 301) ? " permanently" : "") + " moved to " + newLocation;
/* 793 */           Get.this.log(message, this.logLevel);
/* 794 */           URL newURL = new URL(aSource, newLocation);
/* 795 */           if (!redirectionAllowed(aSource, newURL)) {
/* 796 */             return null;
/*     */           }
/* 798 */           return openConnection(newURL);
/*     */         } 
/*     */         
/* 801 */         long lastModified = httpConnection.getLastModified();
/* 802 */         if (responseCode == 304 || (lastModified != 0L && this.hasTimestamp && this.timestamp >= lastModified)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 808 */           Get.this.log("Not modified - so not downloaded", this.logLevel);
/* 809 */           return null;
/*     */         } 
/*     */         
/* 812 */         if (responseCode == 401) {
/* 813 */           String message = "HTTP Authorization failure";
/* 814 */           if (Get.this.ignoreErrors) {
/* 815 */             Get.this.log("HTTP Authorization failure", this.logLevel);
/* 816 */             return null;
/*     */           } 
/* 818 */           throw new BuildException("HTTP Authorization failure");
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 827 */       return connection;
/*     */     }
/*     */     
/*     */     private boolean downloadFile() throws IOException {
/* 831 */       for (int i = 0; i < Get.this.numberRetries; i++) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 836 */           this.is = this.connection.getInputStream();
/*     */           break;
/* 838 */         } catch (IOException ex) {
/* 839 */           Get.this.log("Error opening connection " + ex, this.logLevel);
/*     */         } 
/*     */       } 
/* 842 */       if (this.is == null) {
/* 843 */         Get.this.log("Can't get " + this.source + " to " + this.dest, this.logLevel);
/* 844 */         if (Get.this.ignoreErrors) {
/* 845 */           return false;
/*     */         }
/* 847 */         throw new BuildException("Can't get " + this.source + " to " + this.dest, Get.this
/* 848 */             .getLocation());
/*     */       } 
/*     */       
/* 851 */       if (Get.this.tryGzipEncoding && "gzip"
/* 852 */         .equals(this.connection.getContentEncoding())) {
/* 853 */         this.is = new GZIPInputStream(this.is);
/*     */       }
/*     */       
/* 856 */       this.os = Files.newOutputStream(this.dest.toPath(), new java.nio.file.OpenOption[0]);
/* 857 */       this.progress.beginDownload();
/* 858 */       boolean finished = false;
/*     */       try {
/* 860 */         byte[] buffer = new byte[102400];
/*     */         int length;
/* 862 */         while (!isInterrupted() && (length = this.is.read(buffer)) >= 0) {
/* 863 */           this.os.write(buffer, 0, length);
/* 864 */           this.progress.onTick();
/*     */         } 
/* 866 */         finished = !isInterrupted();
/*     */       } finally {
/* 868 */         FileUtils.close(this.os);
/* 869 */         FileUtils.close(this.is);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 874 */         if (!finished) {
/* 875 */           this.dest.delete();
/*     */         }
/*     */       } 
/* 878 */       this.progress.endDownload();
/* 879 */       return true;
/*     */     }
/*     */     
/*     */     private void updateTimeStamp() {
/* 883 */       long remoteTimestamp = this.connection.getLastModified();
/* 884 */       if (Get.this.verbose) {
/* 885 */         Date t = new Date(remoteTimestamp);
/* 886 */         Get.this.log("last modified = " + t.toString() + (
/* 887 */             (remoteTimestamp == 0L) ? " - using current time instead" : ""), this.logLevel);
/*     */       } 
/* 889 */       if (remoteTimestamp != 0L) {
/* 890 */         Get.FILE_UTILS.setFileLastModified(this.dest, remoteTimestamp);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean wasSuccessful() throws IOException, BuildException {
/* 900 */       if (this.ioexception != null) {
/* 901 */         throw this.ioexception;
/*     */       }
/* 903 */       if (this.exception != null) {
/* 904 */         throw this.exception;
/*     */       }
/* 906 */       return this.success;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void closeStreams() {
/* 914 */       interrupt();
/* 915 */       FileUtils.close(this.os);
/* 916 */       FileUtils.close(this.is);
/* 917 */       if (!this.success && this.dest.exists())
/* 918 */         this.dest.delete(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Get.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
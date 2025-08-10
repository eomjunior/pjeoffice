/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.filters.ChainableReader;
/*     */ import org.apache.tools.ant.types.FilterChain;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.RedirectorElement;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VerifyJar
/*     */   extends AbstractJarSignerTask
/*     */ {
/*     */   public static final String ERROR_NO_FILE = "Not found :";
/*     */   public static final String ERROR_NO_VERIFY = "Failed to verify ";
/*     */   private static final String VERIFIED_TEXT = "jar verified.";
/*     */   private boolean certificates = false;
/*  60 */   private BufferingOutputFilter outputCache = new BufferingOutputFilter();
/*     */   
/*  62 */   private String savedStorePass = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCertificates(boolean certificates) {
/*  69 */     this.certificates = certificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  79 */     boolean hasJar = (this.jar != null);
/*     */     
/*  81 */     if (!hasJar && !hasResources()) {
/*  82 */       throw new BuildException("jar must be set through jar attribute or nested filesets");
/*     */     }
/*     */     
/*  85 */     beginExecution();
/*     */ 
/*     */     
/*  88 */     RedirectorElement redirector = getRedirector();
/*  89 */     redirector.setAlwaysLog(true);
/*  90 */     FilterChain outputFilterChain = redirector.createOutputFilterChain();
/*  91 */     outputFilterChain.add(this.outputCache);
/*     */     
/*     */     try {
/*  94 */       Path sources = createUnifiedSourcePath();
/*  95 */       for (Resource r : sources) {
/*  96 */         FileProvider fr = (FileProvider)r.as(FileProvider.class);
/*  97 */         verifyOneJar(fr.getFile());
/*     */       } 
/*     */     } finally {
/* 100 */       endExecution();
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
/*     */   
/*     */   protected void beginExecution() {
/* 121 */     if (this.storepass != null) {
/* 122 */       this.savedStorePass = this.storepass;
/* 123 */       setStorepass((String)null);
/*     */     } 
/* 125 */     super.beginExecution();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void endExecution() {
/* 133 */     if (this.savedStorePass != null) {
/* 134 */       setStorepass(this.savedStorePass);
/* 135 */       this.savedStorePass = null;
/*     */     } 
/* 137 */     super.endExecution();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyOneJar(File jar) {
/* 146 */     if (!jar.exists()) {
/* 147 */       throw new BuildException("Not found :" + jar);
/*     */     }
/* 149 */     ExecTask cmd = createJarSigner();
/*     */     
/* 151 */     setCommonOptions(cmd);
/* 152 */     bindToKeystore(cmd);
/* 153 */     if (this.savedStorePass != null) {
/* 154 */       addValue(cmd, "-storepass");
/* 155 */       addValue(cmd, this.savedStorePass);
/*     */     } 
/*     */ 
/*     */     
/* 159 */     addValue(cmd, "-verify");
/*     */     
/* 161 */     if (this.certificates) {
/* 162 */       addValue(cmd, "-certs");
/*     */     }
/*     */ 
/*     */     
/* 166 */     addValue(cmd, jar.getPath());
/*     */     
/* 168 */     if (this.alias != null) {
/* 169 */       addValue(cmd, this.alias);
/*     */     }
/*     */     
/* 172 */     log("Verifying JAR: " + jar.getAbsolutePath());
/* 173 */     this.outputCache.clear();
/* 174 */     BuildException ex = null;
/*     */     try {
/* 176 */       cmd.execute();
/* 177 */     } catch (BuildException e) {
/* 178 */       ex = e;
/*     */     } 
/* 180 */     String results = this.outputCache.toString();
/*     */     
/* 182 */     if (ex != null) {
/* 183 */       if (results.contains("zip file closed")) {
/* 184 */         log("You are running jarsigner against a JVM with a known bug that manifests as an IllegalStateException.", 1);
/*     */       }
/*     */       else {
/*     */         
/* 188 */         throw ex;
/*     */       } 
/*     */     }
/* 191 */     if (!results.contains("jar verified.")) {
/* 192 */       throw new BuildException("Failed to verify " + jar);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BufferingOutputFilter
/*     */     implements ChainableReader
/*     */   {
/*     */     private VerifyJar.BufferingOutputFilterReader buffer;
/*     */     
/*     */     private BufferingOutputFilter() {}
/*     */     
/*     */     public Reader chain(Reader rdr) {
/* 205 */       this.buffer = new VerifyJar.BufferingOutputFilterReader(rdr);
/* 206 */       return this.buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 211 */       return this.buffer.toString();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 215 */       if (this.buffer != null) {
/* 216 */         this.buffer.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BufferingOutputFilterReader
/*     */     extends Reader
/*     */   {
/*     */     private Reader next;
/*     */     
/* 228 */     private StringBuffer buffer = new StringBuffer();
/*     */     
/*     */     public BufferingOutputFilterReader(Reader next) {
/* 231 */       this.next = next;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(char[] cbuf, int off, int len) throws IOException {
/* 237 */       int result = this.next.read(cbuf, off, len);
/*     */       
/* 239 */       this.buffer.append(cbuf, off, len);
/*     */       
/* 241 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 246 */       this.next.close();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 251 */       return this.buffer.toString();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 255 */       this.buffer = new StringBuffer();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/VerifyJar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*     */ import org.apache.tools.ant.types.FilterChain;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadResource
/*     */   extends Task
/*     */ {
/*     */   private Resource src;
/*     */   private boolean failOnError = true;
/*     */   private boolean quiet = false;
/*  62 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private String property = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private final List<FilterChain> filterChains = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setEncoding(String encoding) {
/*  87 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setProperty(String property) {
/*  97 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFailonerror(boolean fail) {
/* 106 */     this.failOnError = fail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuiet(boolean quiet) {
/* 115 */     this.quiet = quiet;
/* 116 */     if (quiet) {
/* 117 */       this.failOnError = false;
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
/*     */   public final void execute() throws BuildException {
/* 130 */     if (this.src == null) {
/* 131 */       throw new BuildException("source resource not defined");
/*     */     }
/* 133 */     if (this.property == null) {
/* 134 */       throw new BuildException("output property not defined");
/*     */     }
/* 136 */     if (this.quiet && this.failOnError) {
/* 137 */       throw new BuildException("quiet and failonerror cannot both be set to true");
/*     */     }
/* 139 */     if (!this.src.isExists()) {
/* 140 */       String message = this.src + " doesn't exist";
/* 141 */       if (this.failOnError) {
/* 142 */         throw new BuildException(message);
/*     */       }
/* 144 */       log(message, this.quiet ? 1 : 0);
/*     */       
/*     */       return;
/*     */     } 
/* 148 */     log("loading " + this.src + " into property " + this.property, 3);
/*     */ 
/*     */ 
/*     */     
/* 152 */     Charset charset = (this.encoding == null) ? Charset.defaultCharset() : Charset.forName(this.encoding); try {
/*     */       String text;
/* 154 */       long len = this.src.getSize();
/* 155 */       log("resource size = " + ((len != -1L) ? 
/* 156 */           String.valueOf(len) : "unknown"), 4);
/*     */       
/* 158 */       int size = (int)len;
/*     */ 
/*     */ 
/*     */       
/* 162 */       if (size != 0)
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 171 */         ChainReaderHelper.ChainReader chainReader = (new ChainReaderHelper(getProject(), new InputStreamReader(new BufferedInputStream(this.src.getInputStream()), charset), this.filterChains)).with(crh -> { if (this.src.getSize() != -1L) crh.setBufferSize(size);  }).getAssembledReader();
/*     */         
/* 173 */         try { text = chainReader.readFully();
/* 174 */           if (chainReader != null) chainReader.close();  } catch (Throwable throwable) { if (chainReader != null)
/*     */             try { chainReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  }
/* 176 */       else { log("Do not set property " + this.property + " as its length is 0.", 
/* 177 */             this.quiet ? 3 : 2);
/* 178 */         text = null; }
/*     */ 
/*     */       
/* 181 */       if (text != null && !text.isEmpty()) {
/* 182 */         getProject().setNewProperty(this.property, text);
/* 183 */         log("loaded " + text.length() + " characters", 3);
/*     */         
/* 185 */         log(this.property + " := " + text, 4);
/*     */       } 
/* 187 */     } catch (IOException ioe) {
/* 188 */       String message = "Unable to load resource: " + ioe;
/* 189 */       if (this.failOnError) {
/* 190 */         throw new BuildException(message, ioe, getLocation());
/*     */       }
/* 192 */       log(message, this.quiet ? 3 : 0);
/* 193 */     } catch (BuildException be) {
/* 194 */       if (this.failOnError) {
/* 195 */         throw be;
/*     */       }
/* 197 */       log(be.getMessage(), this.quiet ? 3 : 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addFilterChain(FilterChain filter) {
/* 206 */     this.filterChains.add(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/* 214 */     if (a.size() != 1) {
/* 215 */       throw new BuildException("only single argument resource collections are supported");
/*     */     }
/*     */     
/* 218 */     this.src = a.iterator().next();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/LoadResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
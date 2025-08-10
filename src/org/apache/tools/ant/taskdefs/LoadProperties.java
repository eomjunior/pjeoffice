/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*     */ import org.apache.tools.ant.types.FilterChain;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.JavaResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadProperties
/*     */   extends Task
/*     */ {
/*  55 */   private Resource src = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private final List<FilterChain> filterChains = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private String prefix = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prefixValues = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSrcFile(File srcFile) {
/*  79 */     addConfigured((ResourceCollection)new FileResource(srcFile));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResource(String resource) {
/*  88 */     getRequiredJavaResource().setName(resource);
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
/*     */   public final void setEncoding(String encoding) {
/* 103 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 111 */     getRequiredJavaResource().setClasspath(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 119 */     return getRequiredJavaResource().createClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 128 */     getRequiredJavaResource().setClasspathRef(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 136 */     return getRequiredJavaResource().getClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 144 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefixValues(boolean b) {
/* 155 */     this.prefixValues = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void execute() throws BuildException {
/* 166 */     if (this.src == null) {
/* 167 */       throw new BuildException("A source resource is required.");
/*     */     }
/* 169 */     if (!this.src.isExists()) {
/* 170 */       if (this.src instanceof JavaResource) {
/*     */         
/* 172 */         log("Unable to find resource " + this.src, 1);
/*     */         return;
/*     */       } 
/* 175 */       throw new BuildException("Source resource does not exist: " + this.src);
/*     */     } 
/*     */     
/* 178 */     Charset charset = (this.encoding == null) ? Charset.defaultCharset() : Charset.forName(this.encoding);
/*     */ 
/*     */ 
/*     */     
/* 182 */     try { ChainReaderHelper.ChainReader instream = (new ChainReaderHelper(getProject(), new InputStreamReader(new BufferedInputStream(this.src.getInputStream()), charset), this.filterChains)).getAssembledReader();
/*     */       
/* 184 */       try { String text = instream.readFully();
/*     */         
/* 186 */         if (text != null && !text.isEmpty()) {
/* 187 */           if (!text.endsWith("\n")) {
/* 188 */             text = text + "\n";
/*     */           }
/*     */           
/* 191 */           ByteArrayInputStream tis = new ByteArrayInputStream(text.getBytes(StandardCharsets.ISO_8859_1));
/* 192 */           Properties props = new Properties();
/* 193 */           props.load(tis);
/*     */           
/* 195 */           Property propertyTask = new Property();
/* 196 */           propertyTask.bindToOwner(this);
/* 197 */           propertyTask.setPrefix(this.prefix);
/* 198 */           propertyTask.setPrefixValues(this.prefixValues);
/* 199 */           propertyTask.addProperties(props);
/*     */         } 
/* 201 */         if (instream != null) instream.close();  } catch (Throwable throwable) { if (instream != null) try { instream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 202 */     { throw new BuildException("Unable to load file: " + ioe, ioe, getLocation()); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addFilterChain(FilterChain filter) {
/* 211 */     this.filterChains.add(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addConfigured(ResourceCollection a) {
/* 220 */     if (this.src != null) {
/* 221 */       throw new BuildException("only a single source is supported");
/*     */     }
/* 223 */     if (a.size() != 1) {
/* 224 */       throw new BuildException("only single-element resource collections are supported");
/*     */     }
/*     */     
/* 227 */     this.src = a.iterator().next();
/*     */   }
/*     */   
/*     */   private synchronized JavaResource getRequiredJavaResource() {
/* 231 */     if (this.src == null) {
/* 232 */       this.src = (Resource)new JavaResource();
/* 233 */       this.src.setProject(getProject());
/* 234 */     } else if (!(this.src instanceof JavaResource)) {
/* 235 */       throw new BuildException("expected a java resource as source");
/*     */     } 
/* 237 */     return (JavaResource)this.src;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/LoadProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
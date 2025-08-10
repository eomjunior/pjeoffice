/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ public abstract class ResourceDecorator
/*     */   extends Resource
/*     */ {
/*     */   private Resource resource;
/*     */   
/*     */   protected ResourceDecorator() {}
/*     */   
/*     */   protected ResourceDecorator(ResourceCollection other) {
/*  52 */     addConfigured(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addConfigured(ResourceCollection a) {
/*  60 */     checkChildrenAllowed();
/*  61 */     if (this.resource != null) {
/*  62 */       throw new BuildException("you must not specify more than one resource");
/*     */     }
/*     */     
/*  65 */     if (a.size() != 1) {
/*  66 */       throw new BuildException("only single argument resource collections are supported");
/*     */     }
/*     */     
/*  69 */     setChecked(false);
/*  70 */     this.resource = a.iterator().next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  79 */     return getResource().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/*  88 */     return getResource().isExists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/*  99 */     return getResource().getLastModified();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 108 */     return getResource().isDirectory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 118 */     return getResource().getSize();
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
/*     */   public InputStream getInputStream() throws IOException {
/* 131 */     return getResource().getInputStream();
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 144 */     return getResource().getOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 153 */     return (as(FileProvider.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 162 */     if (this.resource != null) {
/* 163 */       throw noChildrenAllowed();
/*     */     }
/* 165 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T as(Class<T> clazz) {
/* 173 */     return (T)getResource().as(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Resource other) {
/* 181 */     if (other == this) {
/* 182 */       return 0;
/*     */     }
/* 184 */     if (other instanceof ResourceDecorator) {
/* 185 */       return getResource().compareTo(((ResourceDecorator)other)
/* 186 */           .getResource());
/*     */     }
/* 188 */     return getResource().compareTo(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 198 */     return getClass().hashCode() << 4 | getResource().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Resource getResource() {
/* 207 */     if (isReference()) {
/* 208 */       return (Resource)getCheckedRef(Resource.class);
/*     */     }
/* 210 */     if (this.resource == null) {
/* 211 */       throw new BuildException("no resource specified");
/*     */     }
/* 213 */     dieOnCircularReference();
/* 214 */     return this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dieOnCircularReference(Stack<Object> stack, Project project) throws BuildException {
/* 223 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 226 */     if (isReference()) {
/* 227 */       super.dieOnCircularReference(stack, project);
/*     */     } else {
/* 229 */       pushAndInvokeCircularReferenceCheck((DataType)this.resource, stack, project);
/* 230 */       setChecked(true);
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
/*     */   public void setName(String name) throws BuildException {
/* 243 */     throw new BuildException("you can't change the name of a " + 
/* 244 */         getDataTypeName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExists(boolean exists) {
/* 253 */     throw new BuildException("you can't change the exists state of a " + 
/* 254 */         getDataTypeName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModified(long lastmodified) throws BuildException {
/* 264 */     throw new BuildException("you can't change the timestamp of a " + 
/* 265 */         getDataTypeName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean directory) throws BuildException {
/* 275 */     throw new BuildException("you can't change the directory state of a " + 
/* 276 */         getDataTypeName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(long size) throws BuildException {
/* 286 */     throw new BuildException("you can't change the size of a " + 
/* 287 */         getDataTypeName());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/ResourceDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
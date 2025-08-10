/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringResource
/*     */   extends Resource
/*     */ {
/*  40 */   private static final int STRING_MAGIC = Resource.getMagicNumber("StringResource".getBytes());
/*     */   
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*  43 */   private String encoding = "UTF-8";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringResource(String value) {
/*  56 */     this((Project)null, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringResource(Project project, String value) {
/*  66 */     setProject(project);
/*  67 */     setValue((project == null) ? value : project.replaceProperties(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setName(String s) {
/*  76 */     if (getName() != null) {
/*  77 */       throw new BuildException(new ImmutableResourceException());
/*     */     }
/*  79 */     super.setName(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setValue(String s) {
/*  87 */     setName(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getName() {
/*  96 */     return super.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getValue() {
/* 104 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 114 */     return (getValue() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 124 */     checkChildrenAllowed();
/* 125 */     setValue(getProject().replaceProperties(text));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setEncoding(String s) {
/* 133 */     checkAttributesAllowed();
/* 134 */     this.encoding = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getEncoding() {
/* 142 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getSize() {
/* 152 */     return isReference() ? getRef().getSize() : 
/* 153 */       getContent().length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int hashCode() {
/* 163 */     if (isReference()) {
/* 164 */       return getRef().hashCode();
/*     */     }
/* 166 */     return super.hashCode() * STRING_MAGIC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     return String.valueOf(getContent());
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
/*     */   public synchronized InputStream getInputStream() throws IOException {
/* 190 */     if (isReference()) {
/* 191 */       return getRef().getInputStream();
/*     */     }
/* 193 */     String content = getContent();
/* 194 */     if (content == null) {
/* 195 */       throw new IllegalStateException("unset string value");
/*     */     }
/* 197 */     return new ByteArrayInputStream((this.encoding == null) ? 
/* 198 */         content.getBytes() : content.getBytes(this.encoding));
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
/* 211 */     if (isReference()) {
/* 212 */       return getRef().getOutputStream();
/*     */     }
/* 214 */     if (getValue() != null) {
/* 215 */       throw new ImmutableResourceException();
/*     */     }
/* 217 */     return new StringResourceFilterOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 226 */     if (this.encoding != "UTF-8") {
/* 227 */       throw tooManyAttributes();
/*     */     }
/* 229 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized String getContent() {
/* 237 */     return getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected StringResource getRef() {
/* 242 */     return (StringResource)getCheckedRef(StringResource.class);
/*     */   }
/*     */   
/*     */   public StringResource() {}
/*     */   
/*     */   private class StringResourceFilterOutputStream extends FilterOutputStream {
/*     */     public StringResourceFilterOutputStream() {
/* 249 */       super(new ByteArrayOutputStream());
/* 250 */       this.baos = (ByteArrayOutputStream)this.out;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 255 */       super.close();
/*     */       
/* 257 */       String result = (StringResource.this.encoding == null) ? this.baos.toString() : this.baos.toString(StringResource.this.encoding);
/*     */       
/* 259 */       setValueFromOutputStream(result);
/*     */     }
/*     */     private final ByteArrayOutputStream baos;
/*     */     private void setValueFromOutputStream(String output) {
/*     */       String value;
/* 264 */       if (StringResource.this.getProject() != null) {
/* 265 */         value = StringResource.this.getProject().replaceProperties(output);
/*     */       } else {
/* 267 */         value = output;
/*     */       } 
/* 269 */       StringResource.this.setValue(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/StringResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
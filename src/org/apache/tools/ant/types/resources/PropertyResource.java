/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.util.PropertyOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyResource
/*     */   extends Resource
/*     */ {
/*  39 */   private static final int PROPERTY_MAGIC = Resource.getMagicNumber("PropertyResource".getBytes());
/*     */   
/*  41 */   private static final InputStream UNSET = new InputStream()
/*     */     {
/*     */       public int read() {
/*  44 */         return -1;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyResource() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyResource(Project p, String n) {
/*  60 */     super(n);
/*  61 */     setProject(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  69 */     if (isReference()) {
/*  70 */       return getRef().getValue();
/*     */     }
/*  72 */     Project p = getProject();
/*  73 */     return (p == null) ? null : p.getProperty(getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObjectValue() {
/*  82 */     if (isReference()) {
/*  83 */       return getRef().getObjectValue();
/*     */     }
/*  85 */     Project p = getProject();
/*  86 */     return (p == null) ? null : PropertyHelper.getProperty(p, getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/*  95 */     if (isReferenceOrProxy()) {
/*  96 */       return getReferencedOrProxied().isExists();
/*     */     }
/*  98 */     return (getObjectValue() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 108 */     if (isReferenceOrProxy()) {
/* 109 */       return getReferencedOrProxied().getSize();
/*     */     }
/* 111 */     Object o = getObjectValue();
/* 112 */     return (o == null) ? 0L : String.valueOf(o).length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 123 */     return (super.equals(o) || (isReferenceOrProxy() && getReferencedOrProxied().equals(o)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 132 */     if (isReferenceOrProxy()) {
/* 133 */       return getReferencedOrProxied().hashCode();
/*     */     }
/* 135 */     return super.hashCode() * PROPERTY_MAGIC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     if (isReferenceOrProxy()) {
/* 144 */       return getReferencedOrProxied().toString();
/*     */     }
/* 146 */     return getValue();
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
/* 159 */     if (isReferenceOrProxy()) {
/* 160 */       return getReferencedOrProxied().getInputStream();
/*     */     }
/* 162 */     Object o = getObjectValue();
/* 163 */     return (o == null) ? UNSET : new ByteArrayInputStream(String.valueOf(o).getBytes());
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
/* 176 */     if (isReferenceOrProxy()) {
/* 177 */       return getReferencedOrProxied().getOutputStream();
/*     */     }
/* 179 */     if (isExists()) {
/* 180 */       throw new ImmutableResourceException();
/*     */     }
/* 182 */     return (OutputStream)new PropertyOutputStream(getProject(), getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReferenceOrProxy() {
/* 191 */     return (isReference() || getObjectValue() instanceof Resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getReferencedOrProxied() {
/* 201 */     if (isReference()) {
/* 202 */       return super.getRef();
/*     */     }
/* 204 */     Object o = getObjectValue();
/* 205 */     if (o instanceof Resource) {
/* 206 */       return (Resource)o;
/*     */     }
/* 208 */     throw new IllegalStateException("This PropertyResource does not reference or proxy another Resource");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyResource getRef() {
/* 214 */     return (PropertyResource)getCheckedRef(PropertyResource.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/PropertyResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
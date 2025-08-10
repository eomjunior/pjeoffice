/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
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
/*     */ public class Resource
/*     */   extends DataType
/*     */   implements Comparable<Resource>, ResourceCollection
/*     */ {
/*     */   public static final long UNKNOWN_SIZE = -1L;
/*     */   public static final long UNKNOWN_DATETIME = 0L;
/*  50 */   protected static final int MAGIC = getMagicNumber("Resource".getBytes());
/*     */   
/*  52 */   private static final int NULL_NAME = getMagicNumber("null name".getBytes());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int getMagicNumber(byte[] seed) {
/*  60 */     return (new BigInteger(seed)).intValue();
/*     */   }
/*     */   
/*  63 */   private String name = null;
/*  64 */   private Boolean exists = null;
/*  65 */   private Long lastmodified = null;
/*  66 */   private Boolean directory = null;
/*  67 */   private Long size = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource(String name) {
/*  84 */     this(name, false, 0L, false);
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
/*     */   public Resource(String name, boolean exists, long lastmodified) {
/*  96 */     this(name, exists, lastmodified, false);
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
/*     */   public Resource(String name, boolean exists, long lastmodified, boolean directory) {
/* 109 */     this(name, exists, lastmodified, directory, -1L);
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
/*     */   public Resource(String name, boolean exists, long lastmodified, boolean directory, long size) {
/* 123 */     this.name = name;
/* 124 */     setName(name);
/* 125 */     setExists(exists);
/* 126 */     setLastModified(lastmodified);
/* 127 */     setDirectory(directory);
/* 128 */     setSize(size);
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
/*     */   public String getName() {
/* 144 */     return isReference() ? getRef().getName() : this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 153 */     checkAttributesAllowed();
/* 154 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 162 */     if (isReference()) {
/* 163 */       return getRef().isExists();
/*     */     }
/*     */     
/* 166 */     return (this.exists == null || this.exists.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExists(boolean exists) {
/* 174 */     checkAttributesAllowed();
/* 175 */     this.exists = exists ? Boolean.TRUE : Boolean.FALSE;
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
/*     */   public long getLastModified() {
/* 189 */     if (isReference()) {
/* 190 */       return getRef().getLastModified();
/*     */     }
/* 192 */     if (!isExists() || this.lastmodified == null) {
/* 193 */       return 0L;
/*     */     }
/* 195 */     long result = this.lastmodified.longValue();
/* 196 */     return (result < 0L) ? 0L : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModified(long lastmodified) {
/* 204 */     checkAttributesAllowed();
/* 205 */     this.lastmodified = Long.valueOf(lastmodified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 213 */     if (isReference()) {
/* 214 */       return getRef().isDirectory();
/*     */     }
/*     */     
/* 217 */     return (this.directory != null && this.directory.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean directory) {
/* 225 */     checkAttributesAllowed();
/* 226 */     this.directory = directory ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(long size) {
/* 235 */     checkAttributesAllowed();
/* 236 */     this.size = Long.valueOf((size > -1L) ? size : -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 246 */     if (isReference()) {
/* 247 */       return getRef().getSize();
/*     */     }
/* 249 */     return isExists() ? (
/* 250 */       (this.size != null) ? this.size.longValue() : -1L) : 
/* 251 */       0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 261 */       return super.clone();
/* 262 */     } catch (CloneNotSupportedException e) {
/* 263 */       throw new UnsupportedOperationException("CloneNotSupportedException for a Resource caught. Derived classes must support cloning.");
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
/*     */   public int compareTo(Resource other) {
/* 277 */     if (isReference()) {
/* 278 */       return getRef().compareTo(other);
/*     */     }
/* 280 */     return toString().compareTo(other.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 291 */     if (this == other) {
/* 292 */       return true;
/*     */     }
/* 294 */     if (isReference()) {
/* 295 */       return getRef().equals(other);
/*     */     }
/* 297 */     return (other != null && other.getClass().equals(getClass()) && 
/* 298 */       compareTo((Resource)other) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 308 */     if (isReference()) {
/* 309 */       return getRef().hashCode();
/*     */     }
/* 311 */     String name = getName();
/* 312 */     return MAGIC * ((name == null) ? NULL_NAME : name.hashCode());
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
/* 325 */     if (isReference()) {
/* 326 */       return getRef().getInputStream();
/*     */     }
/* 328 */     throw new UnsupportedOperationException();
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
/* 341 */     if (isReference()) {
/* 342 */       return getRef().getOutputStream();
/*     */     }
/* 344 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/* 354 */     return isReference() ? getRef().iterator() : 
/* 355 */       Collections.<Resource>singleton(this).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 365 */     return isReference() ? getRef().size() : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 375 */     return ((isReference() && getRef().isFilesystemOnly()) || 
/* 376 */       as(FileProvider.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 386 */     if (isReference()) {
/* 387 */       return getRef().toString();
/*     */     }
/* 389 */     String n = getName();
/* 390 */     return (n == null) ? "(anonymous)" : n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toLongString() {
/* 401 */     return isReference() ? getRef().toLongString() : (
/* 402 */       getDataTypeName() + " \"" + toString() + '"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 411 */     if (this.name != null || this.exists != null || this.lastmodified != null || this.directory != null || this.size != null)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 416 */       throw tooManyAttributes();
/*     */     }
/* 418 */     super.setRefid(r);
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
/*     */   public <T> T as(Class<T> clazz) {
/* 438 */     return clazz.isAssignableFrom(getClass()) ? clazz.cast(this) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Optional<T> asOptional(Class<T> clazz) {
/* 449 */     return Optional.ofNullable(as(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getRef() {
/* 458 */     return getCheckedRef(Resource.class);
/*     */   }
/*     */   
/*     */   public Resource() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Resource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
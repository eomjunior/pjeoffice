/*     */ package org.apache.tools.ant.types.selectors.modifiedselector;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertiesfileCache
/*     */   implements Cache
/*     */ {
/*  63 */   private File cachefile = null;
/*     */ 
/*     */   
/*  66 */   private Properties cache = new Properties();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cacheLoaded = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cacheDirty = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesfileCache() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesfileCache(File cachefile) {
/*  91 */     this.cachefile = cachefile;
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
/*     */   public void setCachefile(File file) {
/* 103 */     this.cachefile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getCachefile() {
/* 112 */     return this.cachefile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 121 */     return (this.cachefile != null);
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
/*     */   public void load() {
/* 133 */     if (this.cachefile != null && this.cachefile.isFile() && this.cachefile.canRead()) {
/*     */       
/* 135 */       try { BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(this.cachefile.toPath(), new java.nio.file.OpenOption[0])); 
/* 136 */         try { this.cache.load(bis);
/* 137 */           bis.close(); } catch (Throwable throwable) { try { bis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 138 */       { e.printStackTrace(); }
/*     */     
/*     */     }
/*     */     
/* 142 */     this.cacheLoaded = true;
/* 143 */     this.cacheDirty = false;
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
/*     */   public void save() {
/* 155 */     if (!this.cacheDirty) {
/*     */       return;
/*     */     }
/* 158 */     if (this.cachefile != null && this.cache.propertyNames().hasMoreElements()) {
/*     */       
/* 160 */       try { BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(this.cachefile.toPath(), new java.nio.file.OpenOption[0])); 
/* 161 */         try { this.cache.store(bos, (String)null);
/* 162 */           bos.flush();
/* 163 */           bos.close(); } catch (Throwable throwable) { try { bos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 164 */       { e.printStackTrace(); }
/*     */     
/*     */     }
/* 167 */     this.cacheDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 173 */     this.cache = new Properties();
/* 174 */     this.cachefile.delete();
/* 175 */     this.cacheLoaded = true;
/* 176 */     this.cacheDirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(Object key) {
/* 186 */     if (!this.cacheLoaded) {
/* 187 */       load();
/*     */     }
/*     */     try {
/* 190 */       return this.cache.getProperty(String.valueOf(key));
/* 191 */     } catch (ClassCastException e) {
/* 192 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Object key, Object value) {
/* 203 */     this.cache.put(String.valueOf(key), String.valueOf(value));
/* 204 */     this.cacheDirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<String> iterator() {
/* 213 */     return this.cache.stringPropertyNames().iterator();
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
/*     */   public String toString() {
/* 226 */     return String.format("<PropertiesfileCache:cachefile=%s;noOfEntries=%d>", new Object[] { this.cachefile, 
/* 227 */           Integer.valueOf(this.cache.size()) });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/PropertiesfileCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
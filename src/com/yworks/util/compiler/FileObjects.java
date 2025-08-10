/*     */ package com.yworks.util.compiler;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.SimpleJavaFileObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FileObjects
/*     */ {
/*     */   static JavaFileObject newInMemoryFileObject(String typeName, String code) {
/*  32 */     return new InMemoryFileObject(typeName, code);
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
/*     */   static JavaFileObject newUrlFileObject(String typeName, URL url) {
/*  45 */     return new UrlFileObject(typeName, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class AbstractSourceObject
/*     */     extends SimpleJavaFileObject
/*     */   {
/*     */     AbstractSourceObject(String typname) {
/*  57 */       super(asUri(typname), JavaFileObject.Kind.SOURCE);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static URI asUri(String typeName) {
/*  67 */       return URI.create("string:///" + typeName.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class InMemoryFileObject
/*     */     extends AbstractSourceObject
/*     */   {
/*     */     private final String code;
/*     */ 
/*     */ 
/*     */     
/*     */     InMemoryFileObject(String typeName, String code) {
/*  81 */       super(typeName);
/*  82 */       this.code = code;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
/*  88 */       return this.code;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class UrlFileObject
/*     */     extends AbstractSourceObject
/*     */   {
/*     */     private final URL url;
/*     */ 
/*     */ 
/*     */     
/*     */     UrlFileObject(String typname, URL url) {
/* 102 */       super(typname);
/* 103 */       this.url = url;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
/* 109 */       StringBuffer sb = new StringBuffer();
/*     */       
/* 111 */       int[] tail = new int[1];
/*     */       
/* 113 */       BufferedReader br = new BufferedReader(Streams.newTail(openReader(ignoreEncodingErrors), tail));
/*     */       try {
/* 115 */         for (String line = br.readLine(); line != null; line = br.readLine()) {
/* 116 */           sb.append(line).append('\n');
/*     */         }
/*     */       } finally {
/* 119 */         br.close();
/*     */       } 
/*     */       
/* 122 */       if (sb.length() > 0 && tail[0] != 10) {
/* 123 */         sb.setLength(sb.length() - 1);
/*     */       }
/*     */       
/* 126 */       return sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
/* 132 */       return new InputStreamReader(this.url.openStream(), "UTF-8");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/compiler/FileObjects.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
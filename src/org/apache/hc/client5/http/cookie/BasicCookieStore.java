/*     */ package org.apache.hc.client5.http.cookie;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class BasicCookieStore
/*     */   implements CookieStore, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7581093305228232025L;
/*  59 */   private final TreeSet<Cookie> cookies = new TreeSet<>(CookieIdentityComparator.INSTANCE);
/*  60 */   private transient ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  64 */     stream.defaultReadObject();
/*     */ 
/*     */     
/*  67 */     this.lock = new ReentrantReadWriteLock();
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
/*     */   public void addCookie(Cookie cookie) {
/*  82 */     if (cookie != null) {
/*  83 */       this.lock.writeLock().lock();
/*     */       
/*     */       try {
/*  86 */         this.cookies.remove(cookie);
/*  87 */         if (!cookie.isExpired(Instant.now())) {
/*  88 */           this.cookies.add(cookie);
/*     */         }
/*     */       } finally {
/*  91 */         this.lock.writeLock().unlock();
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
/*     */   public void addCookies(Cookie[] cookies) {
/* 107 */     if (cookies != null) {
/* 108 */       for (Cookie cookie : cookies) {
/* 109 */         addCookie(cookie);
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
/*     */   public List<Cookie> getCookies() {
/* 122 */     this.lock.readLock().lock();
/*     */     
/*     */     try {
/* 125 */       return new ArrayList<>(this.cookies);
/*     */     } finally {
/* 127 */       this.lock.readLock().unlock();
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
/*     */   public boolean clearExpired(Date date) {
/* 142 */     if (date == null) {
/* 143 */       return false;
/*     */     }
/* 145 */     this.lock.writeLock().lock();
/*     */     try {
/* 147 */       boolean removed = false;
/* 148 */       for (Iterator<Cookie> it = this.cookies.iterator(); it.hasNext();) {
/* 149 */         if (((Cookie)it.next()).isExpired(date)) {
/* 150 */           it.remove();
/* 151 */           removed = true;
/*     */         } 
/*     */       } 
/* 154 */       return removed;
/*     */     } finally {
/* 156 */       this.lock.writeLock().unlock();
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
/*     */   public boolean clearExpired(Instant instant) {
/* 170 */     if (instant == null) {
/* 171 */       return false;
/*     */     }
/* 173 */     this.lock.writeLock().lock();
/*     */     try {
/* 175 */       boolean removed = false;
/* 176 */       for (Iterator<Cookie> it = this.cookies.iterator(); it.hasNext();) {
/* 177 */         if (((Cookie)it.next()).isExpired(instant)) {
/* 178 */           it.remove();
/* 179 */           removed = true;
/*     */         } 
/*     */       } 
/* 182 */       return removed;
/*     */     } finally {
/* 184 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 193 */     this.lock.writeLock().lock();
/*     */     try {
/* 195 */       this.cookies.clear();
/*     */     } finally {
/* 197 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 203 */     this.lock.readLock().lock();
/*     */     try {
/* 205 */       return this.cookies.toString();
/*     */     } finally {
/* 207 */       this.lock.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/BasicCookieStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
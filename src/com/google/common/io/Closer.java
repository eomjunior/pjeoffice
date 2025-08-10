/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.logging.Level;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class Closer
/*     */   implements Closeable
/*     */ {
/*     */   private static final Suppressor SUPPRESSOR;
/*     */   @VisibleForTesting
/*     */   final Suppressor suppressor;
/*     */   
/*     */   static {
/*  99 */     SuppressingSuppressor suppressingSuppressor = SuppressingSuppressor.tryCreate();
/* 100 */     SUPPRESSOR = (suppressingSuppressor == null) ? LoggingSuppressor.INSTANCE : suppressingSuppressor;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Closer create() {
/* 105 */     return new Closer(SUPPRESSOR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private final Deque<Closeable> stack = new ArrayDeque<>(4);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   Closer(Suppressor suppressor) {
/* 116 */     this.suppressor = (Suppressor)Preconditions.checkNotNull(suppressor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private Throwable thrown;
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public <C extends Closeable> C register(@ParametricNullness C closeable) {
/* 129 */     if (closeable != null) {
/* 130 */       this.stack.addFirst((Closeable)closeable);
/*     */     }
/*     */     
/* 133 */     return closeable;
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
/*     */   public RuntimeException rethrow(Throwable e) throws IOException {
/* 150 */     Preconditions.checkNotNull(e);
/* 151 */     this.thrown = e;
/* 152 */     Throwables.propagateIfPossible(e, IOException.class);
/* 153 */     throw new RuntimeException(e);
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
/*     */   public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType) throws IOException, X {
/* 172 */     Preconditions.checkNotNull(e);
/* 173 */     this.thrown = e;
/* 174 */     Throwables.propagateIfPossible(e, IOException.class);
/* 175 */     Throwables.propagateIfPossible(e, declaredType);
/* 176 */     throw new RuntimeException(e);
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
/*     */   public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2) throws IOException, X1, X2 {
/* 196 */     Preconditions.checkNotNull(e);
/* 197 */     this.thrown = e;
/* 198 */     Throwables.propagateIfPossible(e, IOException.class);
/* 199 */     Throwables.propagateIfPossible(e, declaredType1, declaredType2);
/* 200 */     throw new RuntimeException(e);
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
/*     */   public void close() throws IOException {
/* 212 */     Throwable throwable = this.thrown;
/*     */ 
/*     */     
/* 215 */     while (!this.stack.isEmpty()) {
/* 216 */       Closeable closeable = this.stack.removeFirst();
/*     */       try {
/* 218 */         closeable.close();
/* 219 */       } catch (Throwable e) {
/* 220 */         if (throwable == null) {
/* 221 */           throwable = e; continue;
/*     */         } 
/* 223 */         this.suppressor.suppress(closeable, throwable, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 228 */     if (this.thrown == null && throwable != null) {
/* 229 */       Throwables.propagateIfPossible(throwable, IOException.class);
/* 230 */       throw new AssertionError(throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface Suppressor
/*     */   {
/*     */     void suppress(Closeable param1Closeable, Throwable param1Throwable1, Throwable param1Throwable2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class LoggingSuppressor
/*     */     implements Suppressor
/*     */   {
/* 249 */     static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
/*     */ 
/*     */ 
/*     */     
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
/* 254 */       Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class SuppressingSuppressor
/*     */     implements Suppressor
/*     */   {
/*     */     private final Method addSuppressed;
/*     */     
/*     */     @CheckForNull
/*     */     static SuppressingSuppressor tryCreate() {
/*     */       Method addSuppressed;
/*     */       try {
/* 269 */         addSuppressed = Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
/* 270 */       } catch (Throwable e) {
/* 271 */         return null;
/*     */       } 
/* 273 */       return new SuppressingSuppressor(addSuppressed);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private SuppressingSuppressor(Method addSuppressed) {
/* 279 */       this.addSuppressed = addSuppressed;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
/* 285 */       if (thrown == suppressed) {
/*     */         return;
/*     */       }
/*     */       try {
/* 289 */         this.addSuppressed.invoke(thrown, new Object[] { suppressed });
/* 290 */       } catch (Throwable e) {
/*     */         
/* 292 */         Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
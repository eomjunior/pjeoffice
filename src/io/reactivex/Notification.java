/*     */ package io.reactivex;
/*     */ 
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Notification<T>
/*     */ {
/*     */   final Object value;
/*     */   
/*     */   private Notification(Object value) {
/*  31 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnComplete() {
/*  39 */     return (this.value == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnError() {
/*  49 */     return NotificationLite.isError(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnNext() {
/*  59 */     Object o = this.value;
/*  60 */     return (o != null && !NotificationLite.isError(o));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getValue() {
/*  72 */     Object o = this.value;
/*  73 */     if (o != null && !NotificationLite.isError(o)) {
/*  74 */       return (T)this.value;
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getError() {
/*  87 */     Object o = this.value;
/*  88 */     if (NotificationLite.isError(o)) {
/*  89 */       return NotificationLite.getError(o);
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  96 */     if (obj instanceof Notification) {
/*  97 */       Notification<?> n = (Notification)obj;
/*  98 */       return ObjectHelper.equals(this.value, n.value);
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     Object o = this.value;
/* 106 */     return (o != null) ? o.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     Object o = this.value;
/* 112 */     if (o == null) {
/* 113 */       return "OnCompleteNotification";
/*     */     }
/* 115 */     if (NotificationLite.isError(o)) {
/* 116 */       return "OnErrorNotification[" + NotificationLite.getError(o) + "]";
/*     */     }
/* 118 */     return "OnNextNotification[" + this.value + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static <T> Notification<T> createOnNext(@NonNull T value) {
/* 130 */     ObjectHelper.requireNonNull(value, "value is null");
/* 131 */     return new Notification<T>(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static <T> Notification<T> createOnError(@NonNull Throwable error) {
/* 143 */     ObjectHelper.requireNonNull(error, "error is null");
/* 144 */     return new Notification<T>(NotificationLite.error(error));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static <T> Notification<T> createOnComplete() {
/* 156 */     return (Notification)COMPLETE;
/*     */   }
/*     */ 
/*     */   
/* 160 */   static final Notification<Object> COMPLETE = new Notification(null);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Notification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
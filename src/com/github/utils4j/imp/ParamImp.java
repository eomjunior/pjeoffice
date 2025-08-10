/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IParam;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ParamImp
/*     */   implements IParam
/*     */ {
/*  40 */   static final IParam NULL = new ParamImp(null);
/*     */   
/*     */   static ParamImp of(String name, Object value) {
/*  43 */     return new ParamImp(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   private final Optional<Object> value;
/*     */   
/*     */   private ParamImp(Object value) {
/*  51 */     this.name = null;
/*  52 */     this.value = Optional.ofNullable(value);
/*     */   }
/*     */   
/*     */   private ParamImp(String name, Object value) {
/*  56 */     this.name = Args.requireText(name, "name can't be null").trim();
/*  57 */     this.value = Optional.ofNullable(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  62 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> T get() {
/*  67 */     return orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T orElse(T defaultIf) {
/*     */     try {
/*  74 */       return (T)this.value.orElse(defaultIf);
/*  75 */     } catch (Throwable e) {
/*  76 */       return defaultIf;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <X extends Throwable, T> T orElseThrow(Supplier<? extends X> supplier) throws X {
/*     */     try {
/*  84 */       return (T)this.value.orElseThrow(supplier);
/*  85 */     } catch (Throwable e) {
/*  86 */       Throwable t = (Throwable)supplier.get();
/*  87 */       t.addSuppressed(e);
/*  88 */       throw (X)t;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T orElseGet(Supplier<? extends T> other) {
/*     */     try {
/*  96 */       return (T)this.value.orElseGet(other);
/*  97 */     } catch (Throwable e) {
/*  98 */       return other.get();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void ifPresent(Consumer<T> consumer) {
/* 105 */     this.value.ifPresent((Consumer)consumer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPresent() {
/* 110 */     return this.value.isPresent();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/ParamImp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
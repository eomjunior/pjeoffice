/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public abstract class AbstractInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/*  44 */   private static final Object[] NO_ARGS = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public final Object invoke(Object proxy, Method method, @CheckForNull Object[] args) throws Throwable {
/*  65 */     if (args == null) {
/*  66 */       args = NO_ARGS;
/*     */     }
/*  68 */     if (args.length == 0 && method.getName().equals("hashCode")) {
/*  69 */       return Integer.valueOf(hashCode());
/*     */     }
/*  71 */     if (args.length == 1 && method
/*  72 */       .getName().equals("equals") && method
/*  73 */       .getParameterTypes()[0] == Object.class) {
/*  74 */       Object arg = args[0];
/*  75 */       if (arg == null) {
/*  76 */         return Boolean.valueOf(false);
/*     */       }
/*  78 */       if (proxy == arg) {
/*  79 */         return Boolean.valueOf(true);
/*     */       }
/*  81 */       return Boolean.valueOf((isProxyOfSameInterfaces(arg, proxy.getClass()) && 
/*  82 */           equals(Proxy.getInvocationHandler(arg))));
/*     */     } 
/*  84 */     if (args.length == 0 && method.getName().equals("toString")) {
/*  85 */       return toString();
/*     */     }
/*  87 */     return handleInvocation(proxy, method, args);
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
/*     */   @CheckForNull
/*     */   protected abstract Object handleInvocation(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 115 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return super.toString();
/*     */   }
/*     */   
/*     */   private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
/* 138 */     return (proxyClass.isInstance(arg) || (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       Proxy.isProxyClass(arg.getClass()) && 
/* 145 */       Arrays.equals((Object[])arg.getClass().getInterfaces(), (Object[])proxyClass.getInterfaces())));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/AbstractInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
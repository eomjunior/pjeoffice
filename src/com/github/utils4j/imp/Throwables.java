/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.function.IExecutable;
/*     */ import com.github.utils4j.imp.function.IProcedure;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Throwables
/*     */ {
/*  53 */   private static final Logger LOGGER = LoggerFactory.getLogger(Throwables.class);
/*     */ 
/*     */   
/*     */   private static final String UNKNOWN_CAUSE = "Causa desconhecida";
/*     */ 
/*     */   
/*     */   public static boolean run(IExecutable<?> e) {
/*  60 */     return run(e, false);
/*     */   }
/*     */   
/*     */   public static <E extends Exception> boolean run(IProcedure<Boolean, E> procedure) {
/*  64 */     return run(procedure, false);
/*     */   }
/*     */   
/*     */   public static boolean run(IExecutable<?> e, boolean defaultIfFail) {
/*  68 */     return run(e, defaultIfFail, false);
/*     */   }
/*     */   
/*     */   public static void quietly(IExecutable<?> e) {
/*  72 */     quietly(e, true);
/*     */   }
/*     */   
/*     */   public static void quietly(IExecutable<?> e, boolean logQuietly) {
/*  76 */     run(e, false, logQuietly);
/*     */   }
/*     */   
/*     */   public static void run(boolean logQuietly, IExecutable<?> e) {
/*  80 */     run(e, false, logQuietly);
/*     */   }
/*     */   
/*     */   public static void throwIf(boolean condition, String message) throws Exception {
/*  84 */     throwIf(condition, message, (Throwable)null);
/*     */   }
/*     */   
/*     */   public static void throwIf(boolean condition, String message, Throwable cause) throws Exception {
/*  88 */     throwIf(() -> Boolean.valueOf(condition), message, cause);
/*     */   }
/*     */   
/*     */   public static void throwIf(Supplier<Boolean> condition, String message) throws Exception {
/*  92 */     throwIf(condition, message, (Throwable)null);
/*     */   }
/*     */   
/*     */   public static void throwIf(Supplier<Boolean> condition, String message, Throwable cause) throws Exception {
/*  96 */     if (((Boolean)condition.get()).booleanValue()) {
/*  97 */       throw (cause != null) ? new Exception(message, cause) : new Exception(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean run(IExecutable<?> e, boolean defaultIfFail, boolean logQuietly) {
/*     */     try {
/* 103 */       e.execute();
/* 104 */       return true;
/* 105 */     } catch (Exception ex) {
/* 106 */       if (!logQuietly) {
/* 107 */         LOGGER.warn("run fail", ex);
/*     */       }
/* 109 */       return defaultIfFail;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <E extends Exception> boolean run(IProcedure<Boolean, E> procedure, boolean logQuietly) {
/*     */     try {
/* 115 */       return ((Boolean)procedure.call()).booleanValue();
/* 116 */     } catch (Exception ex) {
/* 117 */       if (!logQuietly) {
/* 118 */         LOGGER.warn("run fail", ex);
/*     */       }
/* 120 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Optional<Exception> capture(IExecutable<?> e) {
/* 125 */     return capturee(e);
/*     */   }
/*     */   
/*     */   public static Optional<Exception> capturep(IProcedure<?, Exception> p) {
/* 129 */     return tryCatch(p);
/*     */   }
/*     */   
/*     */   public static Optional<Exception> capturee(IExecutable<?> e) {
/*     */     try {
/* 134 */       e.execute();
/* 135 */       return Optional.empty();
/* 136 */     } catch (Exception ex) {
/* 137 */       return Optional.of(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void capture(IExecutable<?> e, Consumer<Exception> catchBlock) {
/*     */     try {
/* 143 */       e.execute();
/* 144 */     } catch (Exception ex) {
/* 145 */       catchBlock.accept(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Optional<Exception> tryCatch(IProcedure<?, Exception> p) {
/*     */     try {
/* 151 */       p.call();
/* 152 */       return Optional.empty();
/* 153 */     } catch (Exception ex) {
/* 154 */       return Optional.of(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void tryCatch(IProcedure<?, Exception> p, Consumer<Exception> catchBlock) {
/*     */     try {
/* 160 */       p.call();
/* 161 */     } catch (Exception ex) {
/* 162 */       catchBlock.accept(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void runtime(IExecutable<?> e) {
/*     */     try {
/* 168 */       e.execute();
/* 169 */     } catch (RuntimeException rte) {
/* 170 */       throw rte;
/* 171 */     } catch (Exception ex) {
/* 172 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void runtime(IExecutable<?> e, String message) {
/*     */     try {
/* 178 */       e.execute();
/* 179 */     } catch (Exception ex) {
/* 180 */       throw new RuntimeException(message, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T runtime(IProcedure<T, E> procedure) {
/* 185 */     return runtime(procedure, "");
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T runtime(IProcedure<T, E> procedure, String throwMessageIfFail) {
/* 189 */     return runtime(procedure, () -> throwMessageIfFail);
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T runtime(IProcedure<T, E> procedure, Supplier<String> throwMessageIfFail) {
/* 193 */     return runtime(procedure, ex -> new RuntimeException(Strings.needText(throwMessageIfFail.get(), "runtime fail"), ex));
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T runtime(IProcedure<T, E> procedure, Function<Exception, RuntimeException> wrapper) {
/*     */     try {
/* 198 */       return (T)procedure.call();
/* 199 */     } catch (Exception ex) {
/* 200 */       throw (RuntimeException)wrapper.apply(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> Optional<T> call(IProcedure<T, E> procedure) {
/* 205 */     return Optional.ofNullable(call(procedure, (T)null));
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T call(IProcedure<T, E> procedure, T defaultIfFail) {
/* 209 */     return call(procedure, defaultIfFail, false);
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T call(IProcedure<T, E> procedure, T defaultIfFail, Runnable finallyBlock) {
/* 213 */     return call(procedure, defaultIfFail, false, finallyBlock);
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T call(IProcedure<T, E> procedure, T defaultIfFail, boolean logQuietly, Runnable finallyBlock) {
/* 217 */     return call(procedure, () -> defaultIfFail, logQuietly, finallyBlock);
/*     */   }
/*     */   
/*     */   public static <T, E extends Exception> T call(IProcedure<T, E> procedure, Supplier<T> defaultIfFail) {
/* 221 */     return call(procedure, defaultIfFail, false, () -> {
/*     */         
/*     */         });
/*     */   } public static <T, E extends Exception> T call(IProcedure<T, E> procedure, T defaultIfFail, boolean logQuietly) {
/* 225 */     return call(procedure, () -> defaultIfFail, logQuietly, () -> {
/*     */         
/*     */         });
/*     */   } public static <T, E extends Exception> T call(IProcedure<T, E> procedure, Supplier<T> defaultIfFail, boolean logQuietly, Runnable finallyBlock) {
/*     */     try {
/* 230 */       return (T)procedure.call();
/* 231 */     } catch (Exception e) {
/* 232 */       if (!logQuietly) {
/* 233 */         LOGGER.warn("call fail", e);
/*     */       }
/* 235 */       return defaultIfFail.get();
/*     */     } finally {
/* 237 */       finallyBlock.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Throwable rootCause(Throwable throwable) {
/* 242 */     return rootCause(throwable, t -> {
/*     */         
/*     */         }); } private static Throwable rootCause(Throwable throwable, Consumer<Throwable> visitor) {
/*     */     Throwable rootCause;
/* 246 */     if (throwable == null) {
/* 247 */       return null;
/*     */     }
/*     */     
/* 250 */     Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
/*     */     do {
/* 252 */       dejaVu.add(rootCause = throwable);
/* 253 */       visitor.accept(rootCause);
/* 254 */       throwable = throwable.getCause();
/* 255 */     } while (throwable != null && !dejaVu.contains(throwable));
/* 256 */     return rootCause;
/*     */   }
/*     */   
/*     */   public static Stream<Throwable> traceStream(Throwable throwable) {
/* 260 */     List<Throwable> list = new ArrayList<>();
/* 261 */     rootCause(throwable, list::add);
/* 262 */     return list.stream();
/*     */   }
/*     */   
/*     */   public static String rootMessage(Throwable throwable) {
/* 266 */     Throwable rootCause = rootCause(throwable);
/* 267 */     if (rootCause == null)
/* 268 */       return "Causa desconhecida"; 
/* 269 */     String message = rootCause.getClass().getName();
/* 270 */     return message + ": " + Strings.text(rootCause.getMessage(), "Causa desconhecida");
/*     */   }
/*     */   
/*     */   public static String rootTrace(Throwable throwable) {
/* 274 */     Throwable rootCause = rootCause(throwable);
/* 275 */     if (rootCause == null)
/* 276 */       return "Causa desconhecida"; 
/* 277 */     StringWriter w = new StringWriter();
/* 278 */     try (PrintWriter p = new PrintWriter(w)) {
/* 279 */       rootCause.printStackTrace(p);
/* 280 */       return w.toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String stackTrace(Throwable throwable) {
/* 285 */     if (throwable == null)
/* 286 */       return "Causa desconhecida"; 
/* 287 */     StringWriter w = new StringWriter();
/* 288 */     try (PrintWriter p = new PrintWriter(w)) {
/* 289 */       throwable.printStackTrace(p);
/* 290 */       return w.toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean hasCause(Throwable throwable, Class<?> clazz) {
/* 295 */     if (throwable == null || clazz == null)
/* 296 */       return false; 
/* 297 */     Set<Throwable> dejaVu = null;
/*     */     do {
/* 299 */       if (clazz.isInstance(throwable))
/* 300 */         return true; 
/* 301 */       if (dejaVu == null)
/*     */       {
/* 303 */         dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
/*     */       }
/* 305 */       dejaVu.add(throwable);
/* 306 */       throwable = throwable.getCause();
/* 307 */     } while (throwable != null && !dejaVu.contains(throwable));
/* 308 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
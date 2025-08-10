/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Locale;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Pattern;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class Platform
/*     */ {
/*  34 */   private static final Logger logger = Logger.getLogger(Platform.class.getName());
/*  35 */   private static final PatternCompiler patternCompiler = loadPatternCompiler();
/*     */ 
/*     */ 
/*     */   
/*     */   static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
/*  40 */     return matcher.precomputedInternal();
/*     */   }
/*     */   
/*     */   static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
/*  44 */     WeakReference<? extends Enum<?>> ref = Enums.<T>getEnumConstants(enumClass).get(value);
/*  45 */     return (ref == null) ? Optional.<T>absent() : Optional.<T>of(enumClass.cast(ref.get()));
/*     */   }
/*     */   
/*     */   static String formatCompact4Digits(double value) {
/*  49 */     return String.format(Locale.ROOT, "%.4g", new Object[] { Double.valueOf(value) });
/*     */   }
/*     */   
/*     */   static boolean stringIsNullOrEmpty(@CheckForNull String string) {
/*  53 */     return (string == null || string.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String nullToEmpty(@CheckForNull String string) {
/*  63 */     return (string == null) ? "" : string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   static String emptyToNull(@CheckForNull String string) {
/*  74 */     return stringIsNullOrEmpty(string) ? null : string;
/*     */   }
/*     */   
/*     */   static CommonPattern compilePattern(String pattern) {
/*  78 */     Preconditions.checkNotNull(pattern);
/*  79 */     return patternCompiler.compile(pattern);
/*     */   }
/*     */   
/*     */   static boolean patternCompilerIsPcreLike() {
/*  83 */     return patternCompiler.isPcreLike();
/*     */   }
/*     */   
/*     */   private static PatternCompiler loadPatternCompiler() {
/*  87 */     return new JdkPatternCompiler();
/*     */   }
/*     */   
/*     */   private static void logPatternCompilerError(ServiceConfigurationError e) {
/*  91 */     logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
/*     */   }
/*     */   
/*     */   private static final class JdkPatternCompiler
/*     */     implements PatternCompiler {
/*     */     public CommonPattern compile(String pattern) {
/*  97 */       return new JdkPattern(Pattern.compile(pattern));
/*     */     }
/*     */     private JdkPatternCompiler() {}
/*     */     
/*     */     public boolean isPcreLike() {
/* 102 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
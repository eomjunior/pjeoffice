/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import com.github.utils4j.gui.IThrowableTracker;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Containers;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThrowableTracker
/*    */   implements IThrowableTracker
/*    */ {
/*    */   private static final String BEGIN_TAG = "<code-response-fail>";
/*    */   private static final String END_TAG = "</code-response-fail>";
/* 25 */   public static ThrowableTracker DEFAULT = new ThrowableTracker();
/*    */   private final String beginTag;
/*    */   private final String endTag;
/*    */   
/*    */   protected ThrowableTracker() {
/* 30 */     this("<code-response-fail>", "</code-response-fail>");
/*    */   }
/*    */   
/*    */   protected ThrowableTracker(String beginTag, String endTag) {
/* 34 */     this.beginTag = Args.requireText(beginTag, "beginTag is empty");
/* 35 */     this.endTag = Args.requireText(endTag, "beginTag is empty");
/*    */   }
/*    */   
/*    */   public String mark(String message) {
/* 39 */     return this.beginTag + Strings.trim(message) + this.endTag;
/*    */   }
/*    */   
/*    */   private void unmark(Throwable throwable, Consumer<String> consumer) {
/* 43 */     Args.requireNonNull(consumer, "consumer is null");
/*    */     
/* 45 */     if (throwable != null) {
/* 46 */       traverse(throwable, consumer, Collections.newSetFromMap(new IdentityHashMap<>()));
/*    */     }
/*    */   }
/*    */   
/*    */   private void traverse(Throwable throwable, Consumer<String> consumer, Set<Throwable> dejaVu) {
/* 51 */     while (throwable != null && !dejaVu.contains(throwable)) {
/* 52 */       dejaVu.add(throwable);
/* 53 */       unmark(throwable.getMessage()).ifPresent(consumer);
/* 54 */       Arrays.<Throwable>stream(throwable.getSuppressed()).forEach(t -> traverse(t, consumer, dejaVu));
/* 55 */       throwable = throwable.getCause();
/*    */     } 
/*    */   }
/*    */   
/*    */   public Optional<String> unmark(String message) {
/* 60 */     message = Strings.trim(message);
/* 61 */     int s = message.indexOf(this.beginTag);
/* 62 */     if (s < 0)
/* 63 */       return Optional.empty(); 
/* 64 */     s += this.beginTag.length();
/* 65 */     int e = message.indexOf(this.endTag, s);
/* 66 */     if (e < 0)
/* 67 */       e = message.length(); 
/* 68 */     return Strings.optional(message.substring(s, e));
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] track(Throwable cause) {
/* 73 */     if (cause == null)
/* 74 */       return Strings.emptyArray(); 
/* 75 */     Set<String> out = new HashSet<>();
/* 76 */     unmark(cause, out::add);
/* 77 */     return Containers.arrayOf(out);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/ThrowableTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
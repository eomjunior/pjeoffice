/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NDC
/*     */ {
/* 118 */   static Hashtable ht = new Hashtable<Object, Object>();
/*     */   
/* 120 */   static int pushCounter = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int REAP_THRESHOLD = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Stack getCurrentStack() {
/* 140 */     if (ht != null) {
/* 141 */       return (Stack)ht.get(Thread.currentThread());
/*     */     }
/* 143 */     return null;
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
/*     */   public static void clear() {
/* 158 */     Stack stack = getCurrentStack();
/* 159 */     if (stack != null) {
/* 160 */       stack.setSize(0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stack cloneStack() {
/* 179 */     Stack stack = getCurrentStack();
/* 180 */     if (stack == null) {
/* 181 */       return null;
/*     */     }
/* 183 */     return (Stack)stack.clone();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void inherit(Stack stack) {
/* 210 */     if (stack != null) {
/* 211 */       ht.put(Thread.currentThread(), stack);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get() {
/* 219 */     Stack s = getCurrentStack();
/* 220 */     if (s != null && !s.isEmpty()) {
/* 221 */       return ((DiagnosticContext)s.peek()).fullMessage;
/*     */     }
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDepth() {
/* 233 */     Stack stack = getCurrentStack();
/* 234 */     if (stack == null) {
/* 235 */       return 0;
/*     */     }
/* 237 */     return stack.size();
/*     */   }
/*     */   private static void lazyRemove() {
/*     */     Vector<Thread> v;
/* 241 */     if (ht == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     synchronized (ht) {
/*     */       
/* 251 */       if (++pushCounter <= 5) {
/*     */         return;
/*     */       }
/* 254 */       pushCounter = 0;
/*     */ 
/*     */       
/* 257 */       int misses = 0;
/* 258 */       v = new Vector();
/* 259 */       Enumeration<Thread> enumeration = ht.keys();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 264 */       while (enumeration.hasMoreElements() && misses <= 4) {
/* 265 */         Thread t = enumeration.nextElement();
/* 266 */         if (t.isAlive()) {
/* 267 */           misses++; continue;
/*     */         } 
/* 269 */         misses = 0;
/* 270 */         v.addElement(t);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 275 */     int size = v.size();
/* 276 */     for (int i = 0; i < size; i++) {
/* 277 */       Thread t = v.elementAt(i);
/* 278 */       LogLog.debug("Lazy NDC removal for thread [" + t.getName() + "] (" + ht.size() + ").");
/* 279 */       ht.remove(t);
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
/*     */   public static String pop() {
/* 294 */     Stack stack = getCurrentStack();
/* 295 */     if (stack != null && !stack.isEmpty()) {
/* 296 */       return ((DiagnosticContext)stack.pop()).message;
/*     */     }
/* 298 */     return "";
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
/*     */   public static String peek() {
/* 313 */     Stack stack = getCurrentStack();
/* 314 */     if (stack != null && !stack.isEmpty()) {
/* 315 */       return ((DiagnosticContext)stack.peek()).message;
/*     */     }
/* 317 */     return "";
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
/*     */   public static void push(String message) {
/* 330 */     Stack<DiagnosticContext> stack = getCurrentStack();
/*     */     
/* 332 */     if (stack == null) {
/* 333 */       DiagnosticContext dc = new DiagnosticContext(message, null);
/* 334 */       stack = new Stack();
/* 335 */       Thread key = Thread.currentThread();
/* 336 */       ht.put(key, stack);
/* 337 */       stack.push(dc);
/* 338 */     } else if (stack.isEmpty()) {
/* 339 */       DiagnosticContext dc = new DiagnosticContext(message, null);
/* 340 */       stack.push(dc);
/*     */     } else {
/* 342 */       DiagnosticContext parent = stack.peek();
/* 343 */       stack.push(new DiagnosticContext(message, parent));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove() {
/* 366 */     if (ht != null) {
/* 367 */       ht.remove(Thread.currentThread());
/*     */ 
/*     */       
/* 370 */       lazyRemove();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMaxDepth(int maxDepth) {
/* 404 */     Stack stack = getCurrentStack();
/* 405 */     if (stack != null && maxDepth < stack.size()) {
/* 406 */       stack.setSize(maxDepth);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DiagnosticContext
/*     */   {
/*     */     String fullMessage;
/*     */     String message;
/*     */     
/*     */     DiagnosticContext(String message, DiagnosticContext parent) {
/* 416 */       this.message = message;
/* 417 */       if (parent != null) {
/* 418 */         parent.fullMessage += ' ' + message;
/*     */       } else {
/* 420 */         this.fullMessage = message;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/NDC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
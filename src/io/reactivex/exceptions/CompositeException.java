/*     */ package io.reactivex.exceptions;
/*     */ 
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CompositeException
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 3026362227162912146L;
/*     */   private final List<Throwable> exceptions;
/*     */   private final String message;
/*     */   private Throwable cause;
/*     */   
/*     */   public CompositeException(@NonNull Throwable... exceptions) {
/*  53 */     this((exceptions == null) ? 
/*  54 */         Collections.<Throwable>singletonList(new NullPointerException("exceptions was null")) : Arrays.<Throwable>asList(exceptions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeException(@NonNull Iterable<? extends Throwable> errors) {
/*  65 */     Set<Throwable> deDupedExceptions = new LinkedHashSet<Throwable>();
/*  66 */     List<Throwable> localExceptions = new ArrayList<Throwable>();
/*  67 */     if (errors != null) {
/*  68 */       for (Throwable ex : errors) {
/*  69 */         if (ex instanceof CompositeException) {
/*  70 */           deDupedExceptions.addAll(((CompositeException)ex).getExceptions()); continue;
/*     */         } 
/*  72 */         if (ex != null) {
/*  73 */           deDupedExceptions.add(ex); continue;
/*     */         } 
/*  75 */         deDupedExceptions.add(new NullPointerException("Throwable was null!"));
/*     */       } 
/*     */     } else {
/*     */       
/*  79 */       deDupedExceptions.add(new NullPointerException("errors was null"));
/*     */     } 
/*  81 */     if (deDupedExceptions.isEmpty()) {
/*  82 */       throw new IllegalArgumentException("errors is empty");
/*     */     }
/*  84 */     localExceptions.addAll(deDupedExceptions);
/*  85 */     this.exceptions = Collections.unmodifiableList(localExceptions);
/*  86 */     this.message = this.exceptions.size() + " exceptions occurred. ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public List<Throwable> getExceptions() {
/*  96 */     return this.exceptions;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public String getMessage() {
/* 102 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public synchronized Throwable getCause() {
/* 108 */     if (this.cause == null) {
/*     */       
/* 110 */       CompositeExceptionCausalChain localCause = new CompositeExceptionCausalChain();
/* 111 */       Set<Throwable> seenCauses = new HashSet<Throwable>();
/*     */       
/* 113 */       Throwable chain = localCause;
/* 114 */       for (Throwable e : this.exceptions) {
/* 115 */         if (seenCauses.contains(e)) {
/*     */           continue;
/*     */         }
/*     */         
/* 119 */         seenCauses.add(e);
/*     */         
/* 121 */         List<Throwable> listOfCauses = getListOfCauses(e);
/*     */         
/* 123 */         for (Throwable child : listOfCauses) {
/* 124 */           if (seenCauses.contains(child)) {
/*     */             
/* 126 */             e = new RuntimeException("Duplicate found in causal chain so cropping to prevent loop ...");
/*     */             continue;
/*     */           } 
/* 129 */           seenCauses.add(child);
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 134 */           chain.initCause(e);
/* 135 */         } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 140 */         chain = getRootCause(chain);
/*     */       } 
/* 142 */       this.cause = localCause;
/*     */     } 
/* 144 */     return this.cause;
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
/*     */   public void printStackTrace() {
/* 159 */     printStackTrace(System.err);
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream s) {
/* 164 */     printStackTrace(new WrappedPrintStream(s));
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter s) {
/* 169 */     printStackTrace(new WrappedPrintWriter(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printStackTrace(PrintStreamOrWriter s) {
/* 180 */     StringBuilder b = new StringBuilder(128);
/* 181 */     b.append(this).append('\n');
/* 182 */     for (StackTraceElement myStackElement : getStackTrace()) {
/* 183 */       b.append("\tat ").append(myStackElement).append('\n');
/*     */     }
/* 185 */     int i = 1;
/* 186 */     for (Throwable ex : this.exceptions) {
/* 187 */       b.append("  ComposedException ").append(i).append(" :\n");
/* 188 */       appendStackTrace(b, ex, "\t");
/* 189 */       i++;
/*     */     } 
/* 191 */     s.println(b.toString());
/*     */   }
/*     */   
/*     */   private void appendStackTrace(StringBuilder b, Throwable ex, String prefix) {
/* 195 */     b.append(prefix).append(ex).append('\n');
/* 196 */     for (StackTraceElement stackElement : ex.getStackTrace()) {
/* 197 */       b.append("\t\tat ").append(stackElement).append('\n');
/*     */     }
/* 199 */     if (ex.getCause() != null) {
/* 200 */       b.append("\tCaused by: ");
/* 201 */       appendStackTrace(b, ex.getCause(), "");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class PrintStreamOrWriter
/*     */   {
/*     */     abstract void println(Object param1Object);
/*     */   }
/*     */   
/*     */   static final class WrappedPrintStream
/*     */     extends PrintStreamOrWriter
/*     */   {
/*     */     private final PrintStream printStream;
/*     */     
/*     */     WrappedPrintStream(PrintStream printStream) {
/* 217 */       this.printStream = printStream;
/*     */     }
/*     */ 
/*     */     
/*     */     void println(Object o) {
/* 222 */       this.printStream.println(o);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WrappedPrintWriter extends PrintStreamOrWriter {
/*     */     private final PrintWriter printWriter;
/*     */     
/*     */     WrappedPrintWriter(PrintWriter printWriter) {
/* 230 */       this.printWriter = printWriter;
/*     */     }
/*     */ 
/*     */     
/*     */     void println(Object o) {
/* 235 */       this.printWriter.println(o);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class CompositeExceptionCausalChain
/*     */     extends RuntimeException {
/*     */     private static final long serialVersionUID = 3875212506787802066L;
/*     */     static final String MESSAGE = "Chain of Causes for CompositeException In Order Received =>";
/*     */     
/*     */     public String getMessage() {
/* 245 */       return "Chain of Causes for CompositeException In Order Received =>";
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Throwable> getListOfCauses(Throwable ex) {
/* 250 */     List<Throwable> list = new ArrayList<Throwable>();
/* 251 */     Throwable root = ex.getCause();
/* 252 */     if (root == null || root == ex) {
/* 253 */       return list;
/*     */     }
/*     */     while (true) {
/* 256 */       list.add(root);
/* 257 */       Throwable cause = root.getCause();
/* 258 */       if (cause == null || cause == root) {
/* 259 */         return list;
/*     */       }
/* 261 */       root = cause;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 272 */     return this.exceptions.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Throwable getRootCause(Throwable e) {
/* 282 */     Throwable root = e.getCause();
/* 283 */     if (root == null || e == root) {
/* 284 */       return e;
/*     */     }
/*     */     while (true) {
/* 287 */       Throwable cause = root.getCause();
/* 288 */       if (cause == null || cause == root) {
/* 289 */         return root;
/*     */       }
/* 291 */       root = cause;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/exceptions/CompositeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
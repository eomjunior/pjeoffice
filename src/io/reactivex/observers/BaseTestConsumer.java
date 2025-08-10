/*      */ package io.reactivex.observers;
/*      */ 
/*      */ import io.reactivex.Notification;
/*      */ import io.reactivex.disposables.Disposable;
/*      */ import io.reactivex.exceptions.CompositeException;
/*      */ import io.reactivex.functions.Predicate;
/*      */ import io.reactivex.internal.functions.Functions;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.util.ExceptionHelper;
/*      */ import io.reactivex.internal.util.VolatileSizeArrayList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class BaseTestConsumer<T, U extends BaseTestConsumer<T, U>>
/*      */   implements Disposable
/*      */ {
/*   62 */   protected final List<T> values = (List<T>)new VolatileSizeArrayList();
/*   63 */   protected final List<Throwable> errors = (List<Throwable>)new VolatileSizeArrayList();
/*   64 */   protected final CountDownLatch done = new CountDownLatch(1);
/*      */   
/*      */   protected long completions;
/*      */   
/*      */   protected Thread lastThread;
/*      */   protected boolean checkSubscriptionOnce;
/*      */   
/*      */   public final Thread lastThread() {
/*   72 */     return this.lastThread;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int initialFusionMode;
/*      */ 
/*      */ 
/*      */   
/*      */   protected int establishedFusionMode;
/*      */ 
/*      */ 
/*      */   
/*      */   protected CharSequence tag;
/*      */ 
/*      */   
/*      */   protected boolean timeout;
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<T> values() {
/*   94 */     return this.values;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<Throwable> errors() {
/*  116 */     return this.errors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long completions() {
/*  124 */     return this.completions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTerminated() {
/*  132 */     return (this.done.getCount() == 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int valueCount() {
/*  140 */     return this.values.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int errorCount() {
/*  148 */     return this.errors.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final AssertionError fail(String message) {
/*  161 */     StringBuilder b = new StringBuilder(64 + message.length());
/*  162 */     b.append(message);
/*      */     
/*  164 */     b.append(" (")
/*  165 */       .append("latch = ").append(this.done.getCount()).append(", ")
/*  166 */       .append("values = ").append(this.values.size()).append(", ")
/*  167 */       .append("errors = ").append(this.errors.size()).append(", ")
/*  168 */       .append("completions = ").append(this.completions);
/*      */ 
/*      */     
/*  171 */     if (this.timeout) {
/*  172 */       b.append(", timeout!");
/*      */     }
/*      */     
/*  175 */     if (isDisposed()) {
/*  176 */       b.append(", disposed!");
/*      */     }
/*      */     
/*  179 */     CharSequence tag = this.tag;
/*  180 */     if (tag != null) {
/*  181 */       b.append(", tag = ")
/*  182 */         .append(tag);
/*      */     }
/*      */     
/*  185 */     b
/*  186 */       .append(')');
/*      */ 
/*      */     
/*  189 */     AssertionError ae = new AssertionError(b.toString());
/*  190 */     if (!this.errors.isEmpty()) {
/*  191 */       if (this.errors.size() == 1) {
/*  192 */         ae.initCause(this.errors.get(0));
/*      */       } else {
/*  194 */         CompositeException ce = new CompositeException(this.errors);
/*  195 */         ae.initCause((Throwable)ce);
/*      */       } 
/*      */     }
/*  198 */     return ae;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U await() throws InterruptedException {
/*  209 */     if (this.done.getCount() == 0L) {
/*  210 */       return (U)this;
/*      */     }
/*      */     
/*  213 */     this.done.await();
/*  214 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean await(long time, TimeUnit unit) throws InterruptedException {
/*  227 */     boolean d = (this.done.getCount() == 0L || this.done.await(time, unit));
/*  228 */     this.timeout = !d;
/*  229 */     return d;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertComplete() {
/*  240 */     long c = this.completions;
/*  241 */     if (c == 0L) {
/*  242 */       throw fail("Not completed");
/*      */     }
/*  244 */     if (c > 1L) {
/*  245 */       throw fail("Multiple completions: " + c);
/*      */     }
/*  247 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNotComplete() {
/*  256 */     long c = this.completions;
/*  257 */     if (c == 1L) {
/*  258 */       throw fail("Completed!");
/*      */     }
/*  260 */     if (c > 1L) {
/*  261 */       throw fail("Multiple completions: " + c);
/*      */     }
/*  263 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNoErrors() {
/*  272 */     int s = this.errors.size();
/*  273 */     if (s != 0) {
/*  274 */       throw fail("Error(s) present: " + this.errors);
/*      */     }
/*  276 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertError(Throwable error) {
/*  292 */     return assertError(Functions.equalsWith(error));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertError(Class<? extends Throwable> errorClass) {
/*  303 */     return assertError(Functions.isInstanceOf(errorClass));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertError(Predicate<Throwable> errorPredicate) {
/*  316 */     int s = this.errors.size();
/*  317 */     if (s == 0) {
/*  318 */       throw fail("No errors");
/*      */     }
/*      */     
/*  321 */     boolean found = false;
/*      */     
/*  323 */     for (Throwable e : this.errors) {
/*      */       try {
/*  325 */         if (errorPredicate.test(e)) {
/*  326 */           found = true;
/*      */           break;
/*      */         } 
/*  329 */       } catch (Exception ex) {
/*  330 */         throw ExceptionHelper.wrapOrThrow(ex);
/*      */       } 
/*      */     } 
/*      */     
/*  334 */     if (found) {
/*  335 */       if (s != 1) {
/*  336 */         throw fail("Error present but other errors as well");
/*      */       }
/*      */     } else {
/*  339 */       throw fail("Error not present");
/*      */     } 
/*  341 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValue(T value) {
/*  352 */     int s = this.values.size();
/*  353 */     if (s != 1) {
/*  354 */       throw fail("expected: " + valueAndClass(value) + " but was: " + this.values);
/*      */     }
/*  356 */     T v = this.values.get(0);
/*  357 */     if (!ObjectHelper.equals(value, v)) {
/*  358 */       throw fail("expected: " + valueAndClass(value) + " but was: " + valueAndClass(v));
/*      */     }
/*  360 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNever(T value) {
/*  374 */     int s = this.values.size();
/*      */     
/*  376 */     for (int i = 0; i < s; i++) {
/*  377 */       T v = this.values.get(i);
/*  378 */       if (ObjectHelper.equals(v, value)) {
/*  379 */         throw fail("Value at position " + i + " is equal to " + valueAndClass(value) + "; Expected them to be different");
/*      */       }
/*      */     } 
/*  382 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValue(Predicate<T> valuePredicate) {
/*  395 */     assertValueAt(0, valuePredicate);
/*      */     
/*  397 */     if (this.values.size() > 1) {
/*  398 */       throw fail("Value present but other values as well");
/*      */     }
/*      */     
/*  401 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNever(Predicate<? super T> valuePredicate) {
/*  416 */     int s = this.values.size();
/*      */     
/*  418 */     for (int i = 0; i < s; i++) {
/*  419 */       T v = this.values.get(i);
/*      */       try {
/*  421 */         if (valuePredicate.test(v)) {
/*  422 */           throw fail("Value at position " + i + " matches predicate " + valuePredicate.toString() + ", which was not expected.");
/*      */         }
/*  424 */       } catch (Exception ex) {
/*  425 */         throw ExceptionHelper.wrapOrThrow(ex);
/*      */       } 
/*      */     } 
/*  428 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueAt(int index, T value) {
/*  442 */     int s = this.values.size();
/*  443 */     if (s == 0) {
/*  444 */       throw fail("No values");
/*      */     }
/*      */     
/*  447 */     if (index >= s) {
/*  448 */       throw fail("Invalid index: " + index);
/*      */     }
/*      */     
/*  451 */     T v = this.values.get(index);
/*  452 */     if (!ObjectHelper.equals(value, v)) {
/*  453 */       throw fail("expected: " + valueAndClass(value) + " but was: " + valueAndClass(v));
/*      */     }
/*  455 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueAt(int index, Predicate<T> valuePredicate) {
/*  469 */     int s = this.values.size();
/*  470 */     if (s == 0) {
/*  471 */       throw fail("No values");
/*      */     }
/*      */     
/*  474 */     if (index >= this.values.size()) {
/*  475 */       throw fail("Invalid index: " + index);
/*      */     }
/*      */     
/*  478 */     boolean found = false;
/*      */     
/*      */     try {
/*  481 */       if (valuePredicate.test(this.values.get(index))) {
/*  482 */         found = true;
/*      */       }
/*  484 */     } catch (Exception ex) {
/*  485 */       throw ExceptionHelper.wrapOrThrow(ex);
/*      */     } 
/*      */     
/*  488 */     if (!found) {
/*  489 */       throw fail("Value not present");
/*      */     }
/*  491 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String valueAndClass(Object o) {
/*  500 */     if (o != null) {
/*  501 */       return o + " (class: " + o.getClass().getSimpleName() + ")";
/*      */     }
/*  503 */     return "null";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueCount(int count) {
/*  513 */     int s = this.values.size();
/*  514 */     if (s != count) {
/*  515 */       throw fail("Value counts differ; expected: " + count + " but was: " + s);
/*      */     }
/*  517 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNoValues() {
/*  525 */     return assertValueCount(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValues(T... values) {
/*  536 */     int s = this.values.size();
/*  537 */     if (s != values.length) {
/*  538 */       throw fail("Value count differs; expected: " + values.length + " " + Arrays.toString(values) + " but was: " + s + " " + this.values);
/*      */     }
/*      */     
/*  541 */     for (int i = 0; i < s; i++) {
/*  542 */       T v = this.values.get(i);
/*  543 */       T u = values[i];
/*  544 */       if (!ObjectHelper.equals(u, v)) {
/*  545 */         throw fail("Values at position " + i + " differ; expected: " + valueAndClass(u) + " but was: " + valueAndClass(v));
/*      */       }
/*      */     } 
/*  548 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValuesOnly(T... values) {
/*  559 */     return (U)assertSubscribed()
/*  560 */       .assertValues(values)
/*  561 */       .assertNoErrors()
/*  562 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueSet(Collection<? extends T> expected) {
/*  580 */     if (expected.isEmpty()) {
/*  581 */       assertNoValues();
/*  582 */       return (U)this;
/*      */     } 
/*  584 */     for (T v : this.values) {
/*  585 */       if (!expected.contains(v)) {
/*  586 */         throw fail("Value not in the expected collection: " + valueAndClass(v));
/*      */       }
/*      */     } 
/*  589 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueSetOnly(Collection<? extends T> expected) {
/*  600 */     return (U)assertSubscribed()
/*  601 */       .assertValueSet(expected)
/*  602 */       .assertNoErrors()
/*  603 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueSequence(Iterable<? extends T> sequence) {
/*      */     boolean actualNext, expectedNext;
/*  613 */     int i = 0;
/*  614 */     Iterator<T> actualIterator = this.values.iterator();
/*  615 */     Iterator<? extends T> expectedIterator = sequence.iterator();
/*      */ 
/*      */     
/*      */     while (true) {
/*  619 */       expectedNext = expectedIterator.hasNext();
/*  620 */       actualNext = actualIterator.hasNext();
/*      */       
/*  622 */       if (!actualNext || !expectedNext) {
/*      */         break;
/*      */       }
/*      */       
/*  626 */       T u = expectedIterator.next();
/*  627 */       T v = actualIterator.next();
/*      */       
/*  629 */       if (!ObjectHelper.equals(u, v)) {
/*  630 */         throw fail("Values at position " + i + " differ; expected: " + valueAndClass(u) + " but was: " + valueAndClass(v));
/*      */       }
/*  632 */       i++;
/*      */     } 
/*      */     
/*  635 */     if (actualNext) {
/*  636 */       throw fail("More values received than expected (" + i + ")");
/*      */     }
/*  638 */     if (expectedNext) {
/*  639 */       throw fail("Fewer values received than expected (" + i + ")");
/*      */     }
/*  641 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertValueSequenceOnly(Iterable<? extends T> sequence) {
/*  652 */     return (U)assertSubscribed()
/*  653 */       .assertValueSequence(sequence)
/*  654 */       .assertNoErrors()
/*  655 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertTerminated() {
/*  664 */     if (this.done.getCount() != 0L) {
/*  665 */       throw fail("Subscriber still running!");
/*      */     }
/*  667 */     long c = this.completions;
/*  668 */     if (c > 1L) {
/*  669 */       throw fail("Terminated with multiple completions: " + c);
/*      */     }
/*  671 */     int s = this.errors.size();
/*  672 */     if (s > 1) {
/*  673 */       throw fail("Terminated with multiple errors: " + s);
/*      */     }
/*      */     
/*  676 */     if (c != 0L && s != 0) {
/*  677 */       throw fail("Terminated with multiple completions and errors: " + c);
/*      */     }
/*  679 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNotTerminated() {
/*  688 */     if (this.done.getCount() == 0L) {
/*  689 */       throw fail("Subscriber terminated!");
/*      */     }
/*  691 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean awaitTerminalEvent() {
/*      */     try {
/*  701 */       await();
/*  702 */       return true;
/*  703 */     } catch (InterruptedException ex) {
/*  704 */       Thread.currentThread().interrupt();
/*  705 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean awaitTerminalEvent(long duration, TimeUnit unit) {
/*      */     try {
/*  718 */       return await(duration, unit);
/*  719 */     } catch (InterruptedException ex) {
/*  720 */       Thread.currentThread().interrupt();
/*  721 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertErrorMessage(String message) {
/*  732 */     int s = this.errors.size();
/*  733 */     if (s == 0) {
/*  734 */       throw fail("No errors");
/*      */     }
/*  736 */     if (s == 1) {
/*  737 */       Throwable e = this.errors.get(0);
/*  738 */       String errorMessage = e.getMessage();
/*  739 */       if (!ObjectHelper.equals(message, errorMessage)) {
/*  740 */         throw fail("Error message differs; exptected: " + message + " but was: " + errorMessage);
/*      */       }
/*      */     } else {
/*  743 */       throw fail("Multiple errors");
/*      */     } 
/*  745 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<List<Object>> getEvents() {
/*  757 */     List<List<Object>> result = new ArrayList<List<Object>>();
/*      */     
/*  759 */     result.add(values());
/*      */     
/*  761 */     result.add(errors());
/*      */     
/*  763 */     List<Object> completeList = new ArrayList();
/*  764 */     for (long i = 0L; i < this.completions; i++) {
/*  765 */       completeList.add(Notification.createOnComplete());
/*      */     }
/*  767 */     result.add(completeList);
/*      */     
/*  769 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract U assertSubscribed();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract U assertNotSubscribed();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertResult(T... values) {
/*  794 */     return (U)assertSubscribed()
/*  795 */       .assertValues(values)
/*  796 */       .assertNoErrors()
/*  797 */       .assertComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertFailure(Class<? extends Throwable> error, T... values) {
/*  808 */     return (U)assertSubscribed()
/*  809 */       .assertValues(values)
/*  810 */       .assertError(error)
/*  811 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertFailure(Predicate<Throwable> errorPredicate, T... values) {
/*  824 */     return (U)assertSubscribed()
/*  825 */       .assertValues(values)
/*  826 */       .assertError(errorPredicate)
/*  827 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertFailureAndMessage(Class<? extends Throwable> error, String message, T... values) {
/*  841 */     return (U)assertSubscribed()
/*  842 */       .assertValues(values)
/*  843 */       .assertError(error)
/*  844 */       .assertErrorMessage(message)
/*  845 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U awaitDone(long time, TimeUnit unit) {
/*      */     try {
/*  859 */       if (!this.done.await(time, unit)) {
/*  860 */         this.timeout = true;
/*  861 */         dispose();
/*      */       } 
/*  863 */     } catch (InterruptedException ex) {
/*  864 */       dispose();
/*  865 */       throw ExceptionHelper.wrapOrThrow(ex);
/*      */     } 
/*  867 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertEmpty() {
/*  875 */     return (U)assertSubscribed()
/*  876 */       .assertNoValues()
/*  877 */       .assertNoErrors()
/*  878 */       .assertNotComplete();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U withTag(CharSequence tag) {
/*  891 */     this.tag = tag;
/*  892 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum TestWaitStrategy
/*      */     implements Runnable
/*      */   {
/*  903 */     SPIN
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*      */       public void run() {}
/*      */     },
/*  910 */     YIELD
/*      */     {
/*      */       public void run() {
/*  913 */         Thread.yield();
/*      */       }
/*      */     },
/*      */     
/*  917 */     SLEEP_1MS
/*      */     {
/*      */       public void run() {
/*  920 */         null.sleep(1);
/*      */       }
/*      */     },
/*      */     
/*  924 */     SLEEP_10MS
/*      */     {
/*      */       public void run() {
/*  927 */         null.sleep(10);
/*      */       }
/*      */     },
/*      */     
/*  931 */     SLEEP_100MS
/*      */     {
/*      */       public void run() {
/*  934 */         null.sleep(100);
/*      */       }
/*      */     },
/*      */     
/*  938 */     SLEEP_1000MS
/*      */     {
/*      */       public void run() {
/*  941 */         null.sleep(1000);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static void sleep(int millis) {
/*      */       try {
/*  951 */         Thread.sleep(millis);
/*  952 */       } catch (InterruptedException ex) {
/*  953 */         throw new RuntimeException(ex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract void run();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U awaitCount(int atLeast) {
/*  969 */     return awaitCount(atLeast, TestWaitStrategy.SLEEP_10MS, 5000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U awaitCount(int atLeast, Runnable waitStrategy) {
/*  987 */     return awaitCount(atLeast, waitStrategy, 5000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U awaitCount(int atLeast, Runnable waitStrategy, long timeoutMillis) {
/* 1006 */     long start = System.currentTimeMillis();
/*      */     while (true) {
/* 1008 */       if (timeoutMillis > 0L && System.currentTimeMillis() - start >= timeoutMillis) {
/* 1009 */         this.timeout = true;
/*      */         break;
/*      */       } 
/* 1012 */       if (this.done.getCount() == 0L) {
/*      */         break;
/*      */       }
/* 1015 */       if (this.values.size() >= atLeast) {
/*      */         break;
/*      */       }
/*      */       
/* 1019 */       waitStrategy.run();
/*      */     } 
/* 1021 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isTimeout() {
/* 1034 */     return this.timeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U clearTimeout() {
/* 1046 */     this.timeout = false;
/* 1047 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertTimeout() {
/* 1058 */     if (!this.timeout) {
/* 1059 */       throw fail("No timeout?!");
/*      */     }
/* 1061 */     return (U)this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final U assertNoTimeout() {
/* 1072 */     if (this.timeout) {
/* 1073 */       throw fail("Timeout?!");
/*      */     }
/* 1075 */     return (U)this;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/BaseTestConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
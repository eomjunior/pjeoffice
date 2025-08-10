/*    */ package org.apache.hc.core5.util;
/*    */ 
/*    */ import java.util.concurrent.TimeoutException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeoutValueException
/*    */   extends TimeoutException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final Timeout actual;
/*    */   private final Timeout deadline;
/*    */   
/*    */   public static TimeoutValueException fromMilliseconds(long timeoutDeadline, long timeoutActual) {
/* 49 */     return new TimeoutValueException(Timeout.ofMilliseconds(min0(timeoutDeadline)), 
/* 50 */         Timeout.ofMilliseconds(min0(timeoutActual)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static long min0(long value) {
/* 60 */     return (value < 0L) ? 0L : value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TimeoutValueException(Timeout deadline, Timeout actual) {
/* 74 */     super(String.format("Timeout deadline: %s, actual: %s", new Object[] { deadline, actual }));
/* 75 */     this.actual = actual;
/* 76 */     this.deadline = deadline;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Timeout getActual() {
/* 85 */     return this.actual;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Timeout getDeadline() {
/* 94 */     return this.deadline;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/TimeoutValueException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
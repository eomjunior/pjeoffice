/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import org.apache.hc.client5.http.HttpRoute;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.pool.ConnPoolControl;
/*    */ import org.apache.hc.core5.pool.PoolStats;
/*    */ import org.apache.hc.core5.util.Identifiable;
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
/*    */ @Internal
/*    */ public final class ConnPoolSupport
/*    */ {
/*    */   public static String getId(Object object) {
/* 44 */     if (object == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     return (object instanceof Identifiable) ? ((Identifiable)object)
/* 48 */       .getId() : (object
/* 49 */       .getClass().getSimpleName() + "-" + 
/* 50 */       Integer.toHexString(System.identityHashCode(object)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String formatStats(HttpRoute route, Object state, ConnPoolControl<HttpRoute> connPool) {
/* 57 */     StringBuilder buf = new StringBuilder();
/* 58 */     buf.append("[route: ").append(route).append("]");
/* 59 */     if (state != null) {
/* 60 */       buf.append("[state: ").append(state).append("]");
/*    */     }
/* 62 */     PoolStats totals = connPool.getTotalStats();
/* 63 */     PoolStats stats = connPool.getStats(route);
/* 64 */     buf.append("[total available: ").append(totals.getAvailable()).append("; ");
/* 65 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 66 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 67 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 68 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 69 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/ConnPoolSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
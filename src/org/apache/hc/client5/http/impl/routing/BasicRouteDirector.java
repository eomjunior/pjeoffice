/*     */ package org.apache.hc.client5.http.impl.routing;
/*     */ 
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.routing.HttpRouteDirector;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class BasicRouteDirector
/*     */   implements HttpRouteDirector
/*     */ {
/*  49 */   public static final BasicRouteDirector INSTANCE = new BasicRouteDirector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextStep(RouteInfo plan, RouteInfo fact) {
/*  64 */     Args.notNull(plan, "Planned route");
/*     */     
/*  66 */     int step = -1;
/*     */     
/*  68 */     if (fact == null || fact.getHopCount() < 1) {
/*  69 */       step = firstStep(plan);
/*  70 */     } else if (plan.getHopCount() > 1) {
/*  71 */       step = proxiedStep(plan, fact);
/*     */     } else {
/*  73 */       step = directStep(plan, fact);
/*     */     } 
/*     */     
/*  76 */     return step;
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
/*     */   protected int firstStep(RouteInfo plan) {
/*  90 */     return (plan.getHopCount() > 1) ? 2 : 1;
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
/*     */   protected int directStep(RouteInfo plan, RouteInfo fact) {
/* 106 */     if (fact.getHopCount() > 1) {
/* 107 */       return -1;
/*     */     }
/* 109 */     if (!plan.getTargetHost().equals(fact.getTargetHost()))
/*     */     {
/* 111 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     if (plan.isSecure() != fact.isSecure()) {
/* 121 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 125 */     if (plan.getLocalAddress() != null && 
/* 126 */       !plan.getLocalAddress().equals(fact.getLocalAddress()))
/*     */     {
/* 128 */       return -1;
/*     */     }
/*     */     
/* 131 */     return 0;
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
/*     */   protected int proxiedStep(RouteInfo plan, RouteInfo fact) {
/* 146 */     if (fact.getHopCount() <= 1) {
/* 147 */       return -1;
/*     */     }
/* 149 */     if (!plan.getTargetHost().equals(fact.getTargetHost())) {
/* 150 */       return -1;
/*     */     }
/* 152 */     int phc = plan.getHopCount();
/* 153 */     int fhc = fact.getHopCount();
/* 154 */     if (phc < fhc) {
/* 155 */       return -1;
/*     */     }
/*     */     
/* 158 */     for (int i = 0; i < fhc - 1; i++) {
/* 159 */       if (!plan.getHopTarget(i).equals(fact.getHopTarget(i))) {
/* 160 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 164 */     if (phc > fhc)
/*     */     {
/* 166 */       return 4;
/*     */     }
/*     */ 
/*     */     
/* 170 */     if ((fact.isTunnelled() && !plan.isTunnelled()) || (fact
/* 171 */       .isLayered() && !plan.isLayered())) {
/* 172 */       return -1;
/*     */     }
/*     */     
/* 175 */     if (plan.isTunnelled() && !fact.isTunnelled()) {
/* 176 */       return 3;
/*     */     }
/* 178 */     if (plan.isLayered() && !fact.isLayered()) {
/* 179 */       return 5;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (plan.isSecure() != fact.isSecure()) {
/* 186 */       return -1;
/*     */     }
/*     */     
/* 189 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/routing/BasicRouteDirector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
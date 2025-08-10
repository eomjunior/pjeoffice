/*    */ package org.apache.log4j.or;
/*    */ 
/*    */ import org.apache.log4j.Layout;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadGroupRenderer
/*    */   implements ObjectRenderer
/*    */ {
/*    */   public String doRender(Object o) {
/* 52 */     if (o instanceof ThreadGroup) {
/* 53 */       StringBuilder sbuf = new StringBuilder();
/* 54 */       ThreadGroup tg = (ThreadGroup)o;
/* 55 */       sbuf.append("java.lang.ThreadGroup[name=");
/* 56 */       sbuf.append(tg.getName());
/* 57 */       sbuf.append(", maxpri=");
/* 58 */       sbuf.append(tg.getMaxPriority());
/* 59 */       sbuf.append("]");
/* 60 */       Thread[] t = new Thread[tg.activeCount()];
/* 61 */       tg.enumerate(t);
/* 62 */       for (int i = 0; i < t.length; i++) {
/* 63 */         sbuf.append(Layout.LINE_SEP);
/* 64 */         sbuf.append("   Thread=[");
/* 65 */         sbuf.append(t[i].getName());
/* 66 */         sbuf.append(",");
/* 67 */         sbuf.append(t[i].getPriority());
/* 68 */         sbuf.append(",");
/* 69 */         sbuf.append(t[i].isDaemon());
/* 70 */         sbuf.append("]");
/*    */       } 
/* 72 */       return sbuf.toString();
/*    */     } 
/*    */     
/*    */     try {
/* 76 */       return o.toString();
/* 77 */     } catch (Exception ex) {
/* 78 */       return ex.toString();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/or/ThreadGroupRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
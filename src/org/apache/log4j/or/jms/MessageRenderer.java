/*    */ package org.apache.log4j.or.jms;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import org.apache.log4j.helpers.LogLog;
/*    */ import org.apache.log4j.or.ObjectRenderer;
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
/*    */ public class MessageRenderer
/*    */   implements ObjectRenderer
/*    */ {
/*    */   public String doRender(Object o) {
/* 42 */     if (o instanceof Message) {
/* 43 */       StringBuilder sbuf = new StringBuilder();
/* 44 */       Message m = (Message)o;
/*    */       try {
/* 46 */         sbuf.append("DeliveryMode=");
/* 47 */         switch (m.getJMSDeliveryMode()) {
/*    */           case 1:
/* 49 */             sbuf.append("NON_PERSISTENT");
/*    */             break;
/*    */           case 2:
/* 52 */             sbuf.append("PERSISTENT");
/*    */             break;
/*    */           default:
/* 55 */             sbuf.append("UNKNOWN"); break;
/*    */         } 
/* 57 */         sbuf.append(", CorrelationID=");
/* 58 */         sbuf.append(m.getJMSCorrelationID());
/*    */         
/* 60 */         sbuf.append(", Destination=");
/* 61 */         sbuf.append(m.getJMSDestination());
/*    */         
/* 63 */         sbuf.append(", Expiration=");
/* 64 */         sbuf.append(m.getJMSExpiration());
/*    */         
/* 66 */         sbuf.append(", MessageID=");
/* 67 */         sbuf.append(m.getJMSMessageID());
/*    */         
/* 69 */         sbuf.append(", Priority=");
/* 70 */         sbuf.append(m.getJMSPriority());
/*    */         
/* 72 */         sbuf.append(", Redelivered=");
/* 73 */         sbuf.append(m.getJMSRedelivered());
/*    */         
/* 75 */         sbuf.append(", ReplyTo=");
/* 76 */         sbuf.append(m.getJMSReplyTo());
/*    */         
/* 78 */         sbuf.append(", Timestamp=");
/* 79 */         sbuf.append(m.getJMSTimestamp());
/*    */         
/* 81 */         sbuf.append(", Type=");
/* 82 */         sbuf.append(m.getJMSType());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 91 */       catch (JMSException e) {
/* 92 */         LogLog.error("Could not parse Message.", (Throwable)e);
/*    */       } 
/* 94 */       return sbuf.toString();
/*    */     } 
/* 96 */     return o.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/or/jms/MessageRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
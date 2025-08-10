/*    */ package org.apache.log4j.spi;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.log4j.Category;
/*    */ import org.apache.log4j.DefaultThrowableRenderer;
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
/*    */ public class ThrowableInformation
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = -4748765566864322735L;
/*    */   private transient Throwable throwable;
/*    */   private transient Category category;
/*    */   private String[] rep;
/*    */   
/*    */   public ThrowableInformation(Throwable throwable) {
/* 43 */     this.throwable = throwable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ThrowableInformation(Throwable throwable, Category category) {
/* 54 */     this.throwable = throwable;
/* 55 */     this.category = category;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ThrowableInformation(String[] r) {
/* 65 */     if (r != null) {
/* 66 */       this.rep = (String[])r.clone();
/*    */     }
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 71 */     return this.throwable;
/*    */   }
/*    */   
/*    */   public synchronized String[] getThrowableStrRep() {
/* 75 */     if (this.rep == null) {
/* 76 */       ThrowableRenderer renderer = null;
/* 77 */       if (this.category != null) {
/* 78 */         LoggerRepository repo = this.category.getLoggerRepository();
/* 79 */         if (repo instanceof ThrowableRendererSupport) {
/* 80 */           renderer = ((ThrowableRendererSupport)repo).getThrowableRenderer();
/*    */         }
/*    */       } 
/* 83 */       if (renderer == null) {
/* 84 */         this.rep = DefaultThrowableRenderer.render(this.throwable);
/*    */       } else {
/* 86 */         this.rep = renderer.doRender(this.throwable);
/*    */       } 
/*    */     } 
/* 89 */     return (String[])this.rep.clone();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/ThrowableInformation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
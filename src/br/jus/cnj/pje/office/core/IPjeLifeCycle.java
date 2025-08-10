/*    */ package br.jus.cnj.pje.office.core;
/*    */ 
/*    */ import com.github.utils4j.ILifeCycle;
/*    */ import io.reactivex.Observable;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
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
/*    */ public interface IPjeLifeCycle
/*    */   extends ILifeCycle<IOException>
/*    */ {
/*    */   Observable<LifeCycle> lifeCycle();
/*    */   
/*    */   boolean isProcessing();
/*    */   
/*    */   public enum LifeCycle
/*    */   {
/* 40 */     STARTUP,
/* 41 */     SHUTDOWN,
/* 42 */     KILL;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default void showAbout() {}
/*    */ 
/*    */   
/*    */   default void dispatch(List<File> files) {}
/*    */ 
/*    */   
/*    */   default boolean setDevmode(boolean devmode) {
/* 54 */     return devmode;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeLifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
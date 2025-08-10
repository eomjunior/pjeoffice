/*   */ package com.github.utils4j.gui;
/*   */ 
/*   */ import com.github.utils4j.imp.Strings;
/*   */ 
/*   */ public interface IThrowableTracker {
/*   */   public static final IThrowableTracker NOTHING = t -> Strings.emptyArray();
/*   */   
/*   */   static IThrowableTracker orNothing(IThrowableTracker t) {
/* 9 */     return (t == null) ? NOTHING : t;
/*   */   }
/*   */   
/*   */   String[] track(Throwable paramThrowable);
/*   */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/IThrowableTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
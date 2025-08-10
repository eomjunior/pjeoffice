/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IWindowLockDettector;
/*     */ import com.github.signer4j.IWorkstationLockListener;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.sun.jna.Callback;
/*     */ import com.sun.jna.platform.win32.Kernel32;
/*     */ import com.sun.jna.platform.win32.Kernel32Util;
/*     */ import com.sun.jna.platform.win32.User32;
/*     */ import com.sun.jna.platform.win32.WinDef;
/*     */ import com.sun.jna.platform.win32.WinUser;
/*     */ import com.sun.jna.platform.win32.Wtsapi32;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ class ForWindowsLockDettector
/*     */   implements IWindowLockDettector
/*     */ {
/*  50 */   public static final Logger LOGGER = LoggerFactory.getLogger(IWindowLockDettector.class);
/*     */   
/*  52 */   private final List<IWorkstationLockListener> listeners = new ArrayList<>();
/*     */   
/*     */   private Thread thread;
/*     */   
/*     */   private WorkStation workstation;
/*     */ 
/*     */   
/*     */   public IWindowLockDettector notifyTo(IWorkstationLockListener listener) {
/*  60 */     if (listener != null) {
/*  61 */       synchronized (this.listeners) {
/*  62 */         this.listeners.add(listener);
/*     */       } 
/*     */     }
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  70 */     if (this.thread == null) {
/*  71 */       this.thread = new Thread(this.workstation = new WorkStation(), "window.lock.dettector");
/*  72 */       this.thread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  78 */     if (this.thread != null) {
/*  79 */       LOGGER.debug("Interrompendo monitoração login/logoff");
/*     */       try {
/*  81 */         this.workstation.interrupt(this.thread);
/*  82 */         LOGGER.debug("Interrompido com sucesso!");
/*     */       } finally {
/*  84 */         synchronized (this.listeners) {
/*  85 */           this.listeners.clear();
/*     */         } 
/*  87 */         this.thread = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private class WorkStation
/*     */     implements WinUser.WindowProc, Runnable {
/*  94 */     private final String windowClass = "WorkStationClass_" + System.currentTimeMillis();
/*     */     
/*     */     private WinDef.HWND windowHandle;
/*     */     
/*     */     private WorkStation() {
/*  99 */       WinUser.WNDCLASSEX wClass = new WinUser.WNDCLASSEX();
/* 100 */       wClass.hInstance = null;
/* 101 */       wClass.lpfnWndProc = (Callback)this;
/* 102 */       wClass.lpszClassName = this.windowClass;
/* 103 */       User32.INSTANCE.RegisterClassEx(wClass);
/*     */     }
/*     */     
/*     */     private void info(String message) {
/* 107 */       ForWindowsLockDettector.LOGGER.info(message);
/*     */     }
/*     */     
/*     */     private void checkError(String message) {
/* 111 */       int code = Kernel32.INSTANCE.GetLastError();
/* 112 */       if (code != 0) {
/* 113 */         message = message + ". Error code: " + code + ". Description: " + (String)Throwables.call(() -> Kernel32Util.formatMessage(code), "Unknown");
/* 114 */         ForWindowsLockDettector.LOGGER.error(message);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public WinDef.LRESULT callback(WinDef.HWND hwnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
/* 120 */       switch (uMsg) {
/*     */         case 2:
/* 122 */           User32.INSTANCE.PostQuitMessage(0);
/* 123 */           return new WinDef.LRESULT(0L);
/*     */         case 689:
/* 125 */           onSessionChange(wParam, lParam);
/* 126 */           return new WinDef.LRESULT(0L);
/*     */       } 
/* 128 */       WinDef.LRESULT r = User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
/* 129 */       checkError("Falha em DefWindowProc");
/* 130 */       return r;
/*     */     }
/*     */     
/*     */     protected void onSessionChange(WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
/* 134 */       switch (wParam.intValue()) {
/*     */         case 2:
/*     */         case 4:
/*     */         case 6:
/*     */         case 7:
/* 139 */           synchronized (ForWindowsLockDettector.this.listeners) {
/* 140 */             Throwables.quietly(() -> ForWindowsLockDettector.this.listeners.forEach(()));
/*     */           } 
/*     */           break;
/*     */         case 1:
/*     */         case 3:
/*     */         case 5:
/*     */         case 8:
/* 147 */           synchronized (ForWindowsLockDettector.this.listeners) {
/* 148 */             Throwables.quietly(() -> ForWindowsLockDettector.this.listeners.forEach(()));
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void run() {
/* 155 */       this.windowHandle = User32.INSTANCE.CreateWindowEx(8, this.windowClass, "WorkstationLockListening", 0, 0, 0, 0, 0, null, null, null, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       checkError("Não foi possível a criação da janela de monitoração login/logout");
/* 162 */       if (this.windowHandle == null) {
/*     */         return;
/*     */       }
/* 165 */       info("Criada janela de monitoração login/logout. Handle: " + this.windowHandle.getPointer().toString());
/* 166 */       Wtsapi32.INSTANCE.WTSRegisterSessionNotification(this.windowHandle, 0);
/* 167 */       checkError("Não foi possível registrar notificação de sessão no SO");
/* 168 */       WinUser.MSG msg = new WinUser.MSG();
/* 169 */       while (User32.INSTANCE.GetMessage(msg, this.windowHandle, 0, 0) != 0) {
/* 170 */         User32.INSTANCE.TranslateMessage(msg);
/* 171 */         User32.INSTANCE.DispatchMessage(msg);
/*     */       } 
/* 173 */       Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(this.windowHandle);
/* 174 */       User32.INSTANCE.DestroyWindow(this.windowHandle);
/* 175 */       checkError("Não foi possível a destruição da janela de monitoração login/logout");
/* 176 */       info("Thread de monitoração log(in/off) finalizada");
/*     */     }
/*     */     
/*     */     public void interrupt(Thread thread) {
/* 180 */       thread.interrupt();
/* 181 */       if (this.windowHandle != null) {
/* 182 */         User32.INSTANCE.PostMessage(this.windowHandle, 18, null, null);
/*     */       }
/*     */       try {
/* 185 */         thread.join();
/* 186 */       } catch (InterruptedException e) {
/* 187 */         ForWindowsLockDettector.LOGGER.error("Não foi possível aguardar a finalização da thread de monitoração de login/logout.", e);
/* 188 */         Thread.currentThread().interrupt();
/*     */       } finally {
/* 190 */         User32.INSTANCE.UnregisterClass(this.windowClass, null);
/* 191 */         checkError("Falha em UnregisterClass");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ForWindowsLockDettector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
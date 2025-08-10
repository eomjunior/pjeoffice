/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeEchoNotifier;
/*     */ import br.jus.cnj.pje.office.gui.echo.ApiDebuggerNotifier;
/*     */ import br.jus.cnj.pje.office.gui.echo.Browser2SignerPanel;
/*     */ import br.jus.cnj.pje.office.gui.echo.IDebuggerNotifier;
/*     */ import br.jus.cnj.pje.office.gui.echo.Signer2BackendPanel;
/*     */ import com.github.utils4j.IStringDumpable;
/*     */ import com.github.utils4j.echo.IEcho;
/*     */ import com.github.utils4j.echo.imp.EchoNotifierWindow;
/*     */ import java.util.function.Consumer;
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
/*     */ enum PjeEchoNotifier
/*     */   implements IPjeEchoNotifier
/*     */ {
/*  45 */   DEVMODE {
/*  46 */     private final IDebuggerNotifier debugger = (IDebuggerNotifier)new ApiDebuggerNotifier("http://127.0.0.1:8800/pjeOffice/welcome?file=index.html&page=devguide/devguide.html");
/*     */ 
/*     */     
/*     */     public void echoS2B(IStringDumpable request) {
/*  50 */       this.debugger.echoS2B(request.dump());
/*     */     }
/*     */ 
/*     */     
/*     */     public void echoN2S(IStringDumpable request) {
/*  55 */       this.debugger.echoB2S(request.dump());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/*  60 */       return this.debugger.isOpen();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVisible() {
/*  65 */       return this.debugger.isVisible();
/*     */     }
/*     */ 
/*     */     
/*     */     public void open() {
/*  70 */       this.debugger.open();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/*  75 */       this.debugger.close();
/*     */     }
/*     */ 
/*     */     
/*     */     public void show() {
/*  80 */       this.debugger.show();
/*     */     }
/*     */   },
/*     */   
/*  84 */   DEVMODE_MULTIFRAME {
/*     */     class ApiBrowser2SignerNotifier
/*     */       extends EchoNotifierWindow
/*     */       implements Consumer<String> {
/*     */       protected IEcho createPanel() {
/*  89 */         return (IEcho)new Browser2SignerPanel(this.headerFormat);
/*     */       }
/*     */ 
/*     */       
/*     */       public void accept(String message) {
/*  94 */         onNext(message);
/*     */       }
/*     */     }
/*     */     
/*     */     class ApiSigner2BackendNotifier
/*     */       extends EchoNotifierWindow implements Consumer<String> {
/*     */       protected IEcho createPanel() {
/* 101 */         return (IEcho)new Signer2BackendPanel();
/*     */       }
/*     */ 
/*     */       
/*     */       public void accept(String message) {
/* 106 */         onNext(message);
/*     */       }
/*     */     }
/*     */     
/* 110 */     private final null.ApiSigner2BackendNotifier signer2Backend = new null.ApiSigner2BackendNotifier();
/* 111 */     private final null.ApiBrowser2SignerNotifier navi2Signer = new null.ApiBrowser2SignerNotifier();
/*     */ 
/*     */     
/*     */     public void echoS2B(IStringDumpable request) {
/* 115 */       this.signer2Backend.accept(request.dump());
/*     */     }
/*     */ 
/*     */     
/*     */     public void echoN2S(IStringDumpable request) {
/* 120 */       this.navi2Signer.accept(request.dump());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 125 */       return (this.signer2Backend.isOpen() || this.navi2Signer.isOpen());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVisible() {
/* 130 */       return (this.signer2Backend.isVisible() || this.navi2Signer.isVisible());
/*     */     }
/*     */ 
/*     */     
/*     */     public void open() {
/* 135 */       this.signer2Backend.open();
/* 136 */       this.navi2Signer.open();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 141 */       this.signer2Backend.close();
/* 142 */       this.navi2Signer.close();
/*     */     }
/*     */ 
/*     */     
/*     */     public void show() {
/* 147 */       this.signer2Backend.show();
/* 148 */       this.navi2Signer.show();
/*     */     }
/*     */   },
/*     */   
/* 152 */   PRODUCTION
/*     */   {
/*     */     public void echoS2B(IStringDumpable request) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void echoN2S(IStringDumpable request) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVisible() {
/* 168 */       return false;
/*     */     }
/*     */     
/*     */     public void show() {}
/*     */     
/*     */     public void open() {}
/*     */     
/*     */     public void close() {}
/*     */   };
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeEchoNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
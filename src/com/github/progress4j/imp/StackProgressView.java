/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IContainerProgressView;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Pair;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ class StackProgressView
/*     */   extends ProgressFrameView
/*     */ {
/*  52 */   private final Map<String, Pair<IContainerProgressView<?>, Disposable>> tickets = new HashMap<>();
/*     */   
/*     */   protected StackProgressView() {
/*  55 */     super(new StackProgressFrame());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(String message) {
/*  60 */     Throwables.quietly(() -> super.begin(message));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void info(String message, Object... params) {
/*  65 */     Throwables.quietly(() -> super.info(message, params));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void end() {
/*  70 */     Throwables.quietly(() -> super.end());
/*     */   }
/*     */   
/*     */   final void cancel(Thread thread) {
/*  74 */     synchronized (this.tickets) {
/*  75 */       this.tickets.values().stream().map(Pair::getKey).filter(c -> c.isFrom(thread)).forEach(IProgressView::cancel);
/*     */     } 
/*     */   }
/*     */   
/*     */   final void push(IContainerProgressView<?> progress) throws InterruptedException {
/*  80 */     asContainer().add(progress.asContainer());
/*  81 */     Disposable ticketDetail = progress.detailStatus().subscribe(targetDetail -> onDetail(progress));
/*  82 */     synchronized (this.tickets) {
/*  83 */       this.tickets.put(progress.getName(), Pair.of(progress, ticketDetail));
/*     */     } 
/*  85 */     cancelCode(progress::interrupt);
/*     */   }
/*     */   
/*     */   final void remove(IContainerProgressView<?> pv) {
/*  89 */     asContainer().remove(pv.asContainer());
/*  90 */     synchronized (this.tickets) {
/*  91 */       ((Disposable)((Pair)this.tickets.remove(pv.getName())).getValue()).dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   final void setMode(Mode mode) {
/*  96 */     Mode previous = asContainer().getMode();
/*  97 */     asContainer().setMode(mode);
/*  98 */     if (previous != mode) {
/*  99 */       SwingTools.invokeLater(() -> {
/*     */             asContainer().applyDetail(false);
/*     */             if (Mode.HIDDEN == mode) {
/*     */               asContainer().pack();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDispose() {
/* 110 */     synchronized (this.tickets) {
/* 111 */       this.tickets.values().stream().map(Pair::getValue).forEach(Disposable::dispose);
/* 112 */       this.tickets.clear();
/*     */     } 
/* 114 */     super.doDispose();
/*     */   }
/*     */   
/*     */   private void onDetail(IContainerProgressView<?> progress) {
/* 118 */     synchronized (this.tickets) {
/* 119 */       this.tickets.values().stream().map(Pair::getKey).filter(p -> (p != progress)).forEach(other -> other.showSteps(false));
/* 120 */       progress.showSteps(!progress.isStepsVisible());
/* 121 */       ((StackProgressFrame)asContainer()).repack();
/*     */     } 
/*     */   }
/*     */   
/*     */   static class StackProgressFrame
/*     */     extends ProgressFrame
/*     */   {
/* 128 */     private static final Dimension MININUM_SIZE = new Dimension(450, 144);
/*     */     
/*     */     StackProgressFrame() {
/* 131 */       super(new ProgressBox());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void remove(Container container) {
/* 136 */       super.remove(container);
/* 137 */       repack(1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void add(Container container) {
/* 142 */       super.add(container);
/* 143 */       repack();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void setDefaultPosition() {
/* 149 */       int dx = 0, dy = 0;
/*     */       
/* 151 */       Dimension windowSize = getSize();
/*     */       
/* 153 */       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 154 */       GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
/* 155 */       Rectangle gcBounds = gc.getBounds();
/* 156 */       Point centerPoint = ge.getCenterPoint();
/* 157 */       dx = centerPoint.x - windowSize.width / 2;
/* 158 */       dy = centerPoint.y - windowSize.height / 2;
/*     */       
/* 160 */       dy += ProgressPosition.DEFAULT_WINDOW_DELTA_Y_POSITION;
/* 161 */       dx += ProgressPosition.DEFAULT_WINDOW_DELTA_X_POSITION;
/*     */ 
/*     */ 
/*     */       
/* 165 */       if (dy + windowSize.height > gcBounds.y + gcBounds.height) {
/* 166 */         dy = gcBounds.y + gcBounds.height - windowSize.height;
/*     */       }
/*     */       
/* 169 */       if (dy < gcBounds.y) {
/* 170 */         dy = gcBounds.y;
/*     */       }
/*     */       
/* 173 */       if (dx + windowSize.width > gcBounds.x + gcBounds.width) {
/* 174 */         dx = gcBounds.x + gcBounds.width - windowSize.width;
/*     */       }
/*     */       
/* 177 */       if (dx < gcBounds.x) {
/* 178 */         dx = gcBounds.x;
/*     */       }
/*     */       
/* 181 */       setLocation(dx, dy);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onRestore(WindowEvent e) {
/* 186 */       super.onRestore(e);
/* 187 */       applyDetail(false);
/* 188 */       repackSingle();
/*     */     }
/*     */     
/*     */     protected void repackSingle() {
/* 192 */       repack((getHandlerContainer().getComponentCount() == 1));
/*     */     }
/*     */     
/*     */     private void repack() {
/* 196 */       repack(false);
/*     */     }
/*     */     
/*     */     private void repack(boolean force) {
/* 200 */       repack(2, force);
/*     */     }
/*     */     
/*     */     private void repack(int max) {
/* 204 */       repack(max, false);
/*     */     }
/*     */     
/*     */     private void repack(int max, boolean force) {
/* 208 */       SwingTools.invokeLater(() -> {
/*     */             if (!isMaximized() && (getHandlerContainer().getComponentCount() == max || !isDetailed() || force)) {
/*     */               getHandlerContainer().revalidate();
/*     */               pack();
/*     */             } 
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void packDetail() {
/* 220 */       pack();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void yesCancel() {
/* 225 */       cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelCode(Runnable cancelCode) throws InterruptedException {
/* 230 */       AtomicReference<InterruptedException> ex = new AtomicReference<>();
/*     */       try {
/* 232 */         SwingUtilities.invokeAndWait(() -> {
/*     */               try {
/*     */                 super.cancelCode(cancelCode);
/* 235 */               } catch (InterruptedException e) {
/*     */                 ex.set(e);
/*     */               } 
/*     */             });
/* 239 */       } catch (Exception e) {
/* 240 */         super.cancelCode(cancelCode);
/*     */       } 
/* 242 */       if (ex.get() != null) {
/* 243 */         throw (InterruptedException)ex.get();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected Dimension getDefaultMininumSize() {
/* 249 */       return MININUM_SIZE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/StackProgressView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
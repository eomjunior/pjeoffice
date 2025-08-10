/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressHandler;
/*     */ import com.github.progress4j.IStageEvent;
/*     */ import com.github.progress4j.IStepEvent;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SimpleFrame;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowStateListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.EtchedBorder;
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
/*     */ class ProgressFrame
/*     */   extends SimpleFrame
/*     */   implements IProgressHandler<ProgressFrame>
/*     */ {
/*     */   protected static final int MIN_DETAIL_HEIGHT = 312;
/*  67 */   private static final Dimension MININUM_SIZE = new Dimension(450, 154);
/*     */   
/*  69 */   protected int currentHeight = getDefaultMinDetailHeight();
/*     */   
/*     */   protected final ProgressHandler<?> handler;
/*     */   
/*     */   private boolean maximized = false;
/*     */   
/*     */   private boolean detailed = true;
/*     */   
/*     */   private Disposable detailTicket;
/*     */   
/*     */   private JPanel center;
/*     */   
/*     */   ProgressFrame() {
/*  82 */     this(Images.PROGRESS_ICON.asImage());
/*     */   }
/*     */   
/*     */   ProgressFrame(ProgressHandler<?> handler) {
/*  86 */     this(Images.PROGRESS_ICON.asImage(), handler);
/*     */   }
/*     */   
/*     */   ProgressFrame(Image icon) {
/*  90 */     this(icon, new ProgressBox());
/*     */   }
/*     */   
/*     */   ProgressFrame(Image icon, ProgressHandler<?> handler) {
/*  94 */     super("Progresso", icon);
/*  95 */     this.handler = (ProgressHandler)Args.requireNonNull(handler, "panel is null");
/*  96 */     setupLayout();
/*  97 */     setup();
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/* 101 */     JPanel owner = new JPanel();
/* 102 */     owner.setLayout(new BorderLayout(0, 0));
/* 103 */     owner.setBorder(new EtchedBorder(1));
/* 104 */     owner.add(header(), "North");
/* 105 */     owner.add(center(), "Center");
/* 106 */     setContentPane(owner);
/*     */   }
/*     */   
/*     */   final void setMode(Mode mode) {
/* 110 */     this.handler.setMode(mode);
/*     */   }
/*     */   
/*     */   final Mode getMode() {
/* 114 */     return this.handler.getMode();
/*     */   }
/*     */   
/*     */   protected Component center() {
/* 118 */     this.center = new JPanel();
/* 119 */     this.center.setLayout(new BoxLayout(this.center, 1));
/* 120 */     this.center.add(this.handler);
/* 121 */     return this.center;
/*     */   }
/*     */   
/*     */   public final boolean isMaximized() {
/* 125 */     return this.maximized;
/*     */   }
/*     */   
/*     */   protected int getDefaultMinDetailHeight() {
/* 129 */     return 312;
/*     */   }
/*     */   
/*     */   protected Dimension getDefaultMininumSize() {
/* 133 */     return MININUM_SIZE;
/*     */   }
/*     */   
/*     */   protected final JPanel getHandlerContainer() {
/* 137 */     return this.center;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stepToken(IStepEvent e) {
/* 142 */     this.handler.stepToken(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stageToken(IStageEvent e) {
/* 147 */     this.handler.stageToken(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void cancel() {
/* 152 */     this.handler.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCanceled() {
/* 157 */     return this.handler.isCanceled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelCode(Runnable cancelCode) throws InterruptedException {
/* 162 */     this.handler.cancelCode(cancelCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<Boolean> cancelClick() {
/* 167 */     return this.handler.cancelClick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Thread thread) {
/* 172 */     this.handler.bind(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrom(Thread thread) {
/* 177 */     return this.handler.isFrom(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProgressFrame asContainer() {
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   final void exit() {
/* 186 */     SwingTools.invokeLater(() -> close());
/*     */   }
/*     */   
/*     */   protected boolean isExpanded() {
/* 190 */     return ((getBounds()).height > (getDefaultMininumSize()).height);
/*     */   }
/*     */   
/*     */   protected void add(Container container) {
/* 194 */     SwingTools.invokeLater(() -> {
/*     */           this.center.add(container);
/*     */           centerUpdate();
/*     */         });
/*     */   }
/*     */   
/*     */   protected void remove(Container container) {
/* 201 */     SwingTools.invokeLater(() -> {
/*     */           this.center.remove(container);
/*     */           centerUpdate();
/*     */         });
/*     */   }
/*     */   
/*     */   private void centerUpdate() {
/* 208 */     this.center.revalidate();
/* 209 */     this.center.updateUI();
/*     */   }
/*     */   
/*     */   private JLabel header() {
/* 213 */     JLabel headerLabel = new JLabel("Registro de atividades");
/* 214 */     headerLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 3, 0));
/* 215 */     headerLabel.setIcon(Images.LOG.asIcon());
/* 216 */     headerLabel.setHorizontalAlignment(2);
/* 217 */     headerLabel.setFont(new Font("Tahoma", 1, 15));
/* 218 */     return headerLabel;
/*     */   }
/*     */   
/*     */   protected void setDefaultPosition() {
/* 222 */     toCenter();
/*     */   }
/*     */   
/*     */   final void reveal() {
/* 226 */     if (!isVisible()) {
/* 227 */       SwingTools.invokeLater(() -> {
/*     */             setDefaultPosition();
/*     */             showToFront();
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   final void unreveal() {
/* 235 */     if (isVisible()) {
/* 236 */       SwingTools.invokeLater(() -> setVisible(false));
/*     */     }
/*     */   }
/*     */   
/*     */   private void setup() {
/* 241 */     setupListeners();
/* 242 */     setFixedMinimumSize(getDefaultMininumSize());
/* 243 */     setDefaultCloseOperation(0);
/* 244 */     toCenter();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 249 */     ticketDispose();
/* 250 */     this.handler.dispose();
/* 251 */     super.dispose();
/*     */   }
/*     */   
/*     */   private void ticketDispose() {
/* 255 */     if (this.detailTicket != null) {
/* 256 */       this.detailTicket.dispose();
/* 257 */       this.detailTicket = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<Boolean> detailStatus() {
/* 263 */     return this.handler.detailStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void showSteps(boolean visible) {
/* 268 */     this.handler.showSteps(visible);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStepsVisible() {
/* 273 */     return this.handler.isStepsVisible();
/*     */   }
/*     */   
/*     */   private void setupListeners() {
/* 277 */     this.detailTicket = detailStatus().subscribe(this::applyDetail);
/*     */     
/* 279 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent windowEvent) {
/* 281 */             ProgressFrame.this.onEscPressed((ActionEvent)null);
/*     */           }
/*     */         });
/* 284 */     addWindowStateListener(new WindowStateListener() {
/*     */           public void windowStateChanged(WindowEvent e) {
/* 286 */             if ((e.getNewState() & 0x6) == 6) {
/* 287 */               ProgressFrame.this.onMaximized(e);
/* 288 */             } else if ((e.getNewState() & 0x0) == 0) {
/* 289 */               ProgressFrame.this.onRestore(e);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void onRestore(WindowEvent e) {
/* 296 */     this.handler.showSteps(isExpanded());
/* 297 */     this.maximized = false;
/*     */   }
/*     */   
/*     */   protected void onMaximized(WindowEvent e) {
/* 301 */     this.maximized = true;
/* 302 */     this.handler.showSteps(!this.maximized);
/* 303 */     applyDetail(this.maximized);
/*     */   }
/*     */   
/*     */   protected void expandTo(int height) {
/* 307 */     setBounds((getBounds()).x, (getBounds()).y, (getBounds()).width, height);
/*     */   }
/*     */   
/*     */   protected void expandTo(Dimension dimension) {
/* 311 */     setBounds((getBounds()).x, (getBounds()).y, dimension.width, dimension.height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {
/* 316 */     Dialogs.Choice choice = Dialogs.getBoolean("Deseja mesmo cancelar a operação?", "Cancelamento da operação", false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     if (choice == Dialogs.Choice.YES) {
/* 322 */       yesCancel();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void yesCancel() {
/* 327 */     cancel();
/* 328 */     unreveal();
/*     */   }
/*     */   
/*     */   protected final boolean isDetailed() {
/* 332 */     return this.detailed;
/*     */   }
/*     */   
/*     */   protected void packDetail() {
/* 336 */     expandTo(this.currentHeight);
/*     */   }
/*     */   
/*     */   protected void applyDetail(boolean toshow) {
/* 340 */     if (isMaximized()) {
/* 341 */       if (!toshow) {
/* 342 */         setExtendedState(0);
/* 343 */         expandTo(getDefaultMininumSize());
/*     */       } 
/* 345 */     } else if (toshow) {
/* 346 */       packDetail();
/*     */     } else {
/* 348 */       expandTo((getDefaultMininumSize()).height);
/*     */     } 
/* 350 */     showSteps(toshow);
/* 351 */     this.detailed = !toshow;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 355 */     SwingTools.invokeLater(() -> {
/*     */           ProgressFrame p = new ProgressFrame();
/*     */           p.setDefaultCloseOperation(2);
/*     */           p.reveal();
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
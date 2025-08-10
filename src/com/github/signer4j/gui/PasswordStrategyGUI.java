/*     */ package com.github.signer4j.gui;
/*     */ 
/*     */ import com.github.signer4j.IAuthStrategyAware;
/*     */ import com.github.signer4j.IsAuthStrategy;
/*     */ import com.github.signer4j.imp.AuthStrategy;
/*     */ import com.github.signer4j.imp.Repository;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.awt.CheckboxMenuItem;
/*     */ import java.awt.Font;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
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
/*     */ public class PasswordStrategyGUI<T extends Menu>
/*     */   implements IsAuthStrategy
/*     */ {
/*  59 */   private static final List<PasswordStrategyGUI<?>> INSTANCES = new ArrayList<>();
/*     */   
/*     */   private final T menu;
/*     */   
/*     */   private final JLabel label;
/*     */   
/*     */   private CheckboxMenuItem mnuAways;
/*     */   
/*     */   private CheckboxMenuItem mnuOneTime;
/*     */   
/*     */   private CheckboxMenuItem mnuConfirm;
/*     */   
/*     */   private Disposable repositoryTicket;
/*     */   
/*     */   private final IAuthStrategyAware model;
/*     */   
/*     */   private final JDialog dialog;
/*     */   
/*     */   public PasswordStrategyGUI(IAuthStrategyAware model, T menu) {
/*  78 */     this(model, menu, null, null);
/*     */   }
/*     */   
/*     */   public PasswordStrategyGUI(IAuthStrategyAware model, T menu, JDialog dialog) {
/*  82 */     this(model, menu, dialog, null);
/*     */   }
/*     */   
/*     */   public PasswordStrategyGUI(IAuthStrategyAware model, T menu, JDialog dialog, JLabel label) {
/*  86 */     this.model = (IAuthStrategyAware)Args.requireNonNull(model, "model is null");
/*  87 */     this.menu = (T)Args.requireNonNull(menu, "menu is null");
/*  88 */     this.dialog = dialog;
/*  89 */     this.label = label;
/*  90 */     setup();
/*  91 */     INSTANCES.add(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAwayStrategy() {
/*  96 */     return this.mnuAways.getState();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isOneTimeStrategy() {
/* 101 */     return this.mnuOneTime.getState();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isConfirmStrategy() {
/* 106 */     return this.mnuConfirm.getState();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getStrategyLabel() {
/* 111 */     return this.model.getStrategyLabel();
/*     */   }
/*     */   
/*     */   public final T menu() {
/* 115 */     return this.menu;
/*     */   }
/*     */   
/*     */   private void setMenuState(ItemEvent e) {
/* 119 */     setClick2Menu(e);
/* 120 */     setMenu2Model(false);
/* 121 */     INSTANCES.stream().forEach(PasswordStrategyGUI::setModel2Menu);
/*     */   }
/*     */   
/*     */   private void setClick2Menu(ItemEvent e) {
/* 125 */     MenuItem clickMenu = (MenuItem)e.getSource();
/* 126 */     this.mnuAways.setState(clickMenu.getLabel().equals(AuthStrategy.AWAYS.getStrategyLabel()));
/* 127 */     this.mnuOneTime.setState(clickMenu.getLabel().equals(AuthStrategy.ONE_TIME.getStrategyLabel()));
/* 128 */     this.mnuConfirm.setState(clickMenu.getLabel().equals(AuthStrategy.CONFIRM.getStrategyLabel()));
/*     */   }
/*     */   
/*     */   private void setModel2Menu() {
/* 132 */     this.mnuAways.setState(this.model.isAwayStrategy());
/* 133 */     this.mnuOneTime.setState(this.model.isOneTimeStrategy());
/* 134 */     this.mnuConfirm.setState(this.model.isConfirmStrategy());
/* 135 */     setModel2MenuLabel();
/*     */   }
/*     */   
/*     */   private void setModel2MenuLabel() {
/* 139 */     if (this.label != null) {
/* 140 */       this.label.setText("<html><u>" + getStrategyLabel() + "</u></html>");
/*     */     }
/* 142 */     if (this.dialog != null) {
/* 143 */       this.dialog.pack();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setMenu2Model(boolean force) {
/* 148 */     int i = force | ((this.label != null) ? 1 : 0);
/* 149 */     if (this.mnuAways.getState()) {
/* 150 */       this.model.setAuthStrategy(AuthStrategy.AWAYS, i);
/* 151 */     } else if (this.mnuOneTime.getState()) {
/* 152 */       this.model.setAuthStrategy(AuthStrategy.ONE_TIME, i);
/* 153 */     } else if (this.mnuConfirm.getState()) {
/* 154 */       this.model.setAuthStrategy(AuthStrategy.CONFIRM, i);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 159 */     if (this.repositoryTicket != null) {
/* 160 */       this.repositoryTicket.dispose();
/* 161 */       this.repositoryTicket = null;
/* 162 */       INSTANCES.remove(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildRepoTicket() {
/* 167 */     this.repositoryTicket = this.model.newRepository().subscribe(r -> {
/*     */           Runnable dispatchCode;
/*     */           if (!Jvms.isWindows())
/*     */             return; 
/* 171 */           boolean notSupported = (r.isMSCAPI() && isAwayStrategy());
/*     */           if (notSupported) {
/*     */             this.model.setAuthStrategy(AuthStrategy.ONE_TIME, true);
/*     */             dispatchCode = (());
/*     */           } else {
/*     */             setMenu2Model(true);
/*     */             dispatchCode = (());
/*     */           } 
/*     */           SwingTools.invokeLater(dispatchCode);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setup() {
/* 190 */     Font font = new Font("Tahoma", 0, 12);
/* 191 */     this.mnuAways = new CheckboxMenuItem(AuthStrategy.AWAYS.getStrategyLabel());
/* 192 */     this.mnuOneTime = new CheckboxMenuItem(AuthStrategy.ONE_TIME.getStrategyLabel());
/* 193 */     this.mnuConfirm = new CheckboxMenuItem(AuthStrategy.CONFIRM.getStrategyLabel());
/*     */     
/* 195 */     this.mnuAways.setFont(font);
/* 196 */     this.mnuOneTime.setFont(font);
/* 197 */     this.mnuConfirm.setFont(font);
/*     */     
/* 199 */     this.mnuAways.addItemListener(this::setMenuState);
/* 200 */     this.mnuOneTime.addItemListener(this::setMenuState);
/* 201 */     this.mnuConfirm.addItemListener(this::setMenuState);
/*     */     
/* 203 */     setModel2Menu();
/*     */     
/* 205 */     this.menu.add(this.mnuAways);
/* 206 */     this.menu.add(this.mnuOneTime);
/* 207 */     this.menu.add(this.mnuConfirm);
/*     */     
/* 209 */     if (this.label != null) {
/* 210 */       this.label.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mousePressed(MouseEvent e) {
/* 213 */               maybeShowPopup(e);
/*     */             }
/*     */             
/*     */             private void maybeShowPopup(MouseEvent e) {
/* 217 */               if (PasswordStrategyGUI.this.menu instanceof PopupMenu) {
/* 218 */                 ((PopupMenu)PasswordStrategyGUI.this.menu).show(e.getComponent(), e.getX(), e.getY());
/*     */               }
/*     */             }
/*     */           });
/*     */     }
/* 223 */     buildRepoTicket();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/PasswordStrategyGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
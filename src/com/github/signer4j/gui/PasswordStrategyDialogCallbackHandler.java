/*     */ package com.github.signer4j.gui;
/*     */ 
/*     */ import com.github.signer4j.IAuthStrategyAware;
/*     */ import com.github.signer4j.IGadget;
/*     */ import com.github.signer4j.IPasswordCollector;
/*     */ import com.github.signer4j.gui.utils.Images;
/*     */ import com.github.utils4j.gui.imp.SimpleDialog;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.UIManager;
/*     */ import net.miginfocom.swing.MigLayout;
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
/*     */ public class PasswordStrategyDialogCallbackHandler
/*     */   extends PasswordDialogCallbackHandler
/*     */ {
/*  63 */   private static Optional<IAuthStrategyAware> DEFAULT = Optional.empty();
/*     */   
/*     */   public static void register(IAuthStrategyAware aware) {
/*  66 */     DEFAULT = Optional.ofNullable(aware);
/*     */   }
/*     */ 
/*     */   
/*     */   private PasswordStrategyGUI<PopupMenu> strategyGui;
/*  71 */   private Integer choice = Integer.valueOf(2);
/*     */   
/*     */   public PasswordStrategyDialogCallbackHandler(IGadget gadget) {
/*  74 */     super(gadget);
/*     */   }
/*     */   
/*     */   public PasswordStrategyDialogCallbackHandler(IGadget gadget, IPasswordCollector collector) {
/*  78 */     super(gadget, collector);
/*     */   }
/*     */   
/*     */   public PasswordStrategyDialogCallbackHandler(IGadget gadget, String title, IPasswordCollector collector) {
/*  82 */     super(gadget, collector, title);
/*     */   }
/*     */   
/*     */   private class DisposablePasswordDialog
/*     */     extends PasswordDialogCallbackHandler.DefaultPasswordDialog {
/*     */     private DisposablePasswordDialog(JPasswordField passwordField, Supplier<Object> chooser, final JDialog dialog) {
/*  88 */       super(passwordField, chooser, dialog);
/*  89 */       passwordField.addKeyListener(new KeyAdapter()
/*     */           {
/*     */             public void keyPressed(KeyEvent e) {
/*  92 */               if (e.getKeyCode() == 10) {
/*  93 */                 PasswordStrategyDialogCallbackHandler.this.onEnter(dialog);
/*     */               }
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 101 */       super.close();
/* 102 */       if (PasswordStrategyDialogCallbackHandler.this.strategyGui != null) {
/* 103 */         PasswordStrategyDialogCallbackHandler.this.strategyGui.dispose();
/* 104 */         PasswordStrategyDialogCallbackHandler.this.strategyGui = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Integer getChoice() {
/* 110 */     return this.choice;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final PasswordDialogCallbackHandler.DefaultPasswordDialog createDialog() {
/* 115 */     if (!DEFAULT.isPresent())
/* 116 */       return super.createDialog(); 
/* 117 */     JPasswordField passwordField = new JPasswordField();
/* 118 */     SimpleDialog simpleDialog = new SimpleDialog(this.title, true);
/* 119 */     simpleDialog.setLayout(new BorderLayout());
/* 120 */     simpleDialog.add(createQuestionPane(), "West");
/* 121 */     simpleDialog.add(createCenterPane(passwordField, (JDialog)simpleDialog), "Center");
/* 122 */     simpleDialog.pack();
/* 123 */     simpleDialog.setResizable(false);
/* 124 */     simpleDialog.setLocationRelativeTo((Component)null);
/* 125 */     return new DisposablePasswordDialog(passwordField, this::getChoice, (JDialog)simpleDialog);
/*     */   }
/*     */   
/*     */   private JPanel createCenterPane(JPasswordField passwordField, JDialog dialog) {
/* 129 */     JLabel token = new JLabel(String.format("Token: %s - Modelo: %s - %s", new Object[] { this.gadget
/* 130 */             .getLabel(), this.gadget
/* 131 */             .getCategory(), this.gadget
/* 132 */             .getModel() }));
/*     */ 
/*     */     
/* 135 */     JLabel manufacturer = new JLabel(String.format("Fabricante: %s ", new Object[] { this.gadget.getManufacturer() }));
/* 136 */     JLabel serial = new JLabel("Número de série: " + this.gadget.getSerial());
/* 137 */     JLabel pin = new JLabel("Senha/PIN: ");
/* 138 */     JPanel center = new JPanel((LayoutManager)new MigLayout("insets 10 0 0 15"));
/*     */     
/* 140 */     center.add(token, "wrap");
/* 141 */     center.add(manufacturer, "wrap");
/* 142 */     center.add(serial, "wrap");
/* 143 */     center.add(pin, "wrap");
/* 144 */     center.add(passwordField, "growx,wrap");
/* 145 */     center.add(createChoicePane(dialog), "growx,wrap");
/* 146 */     return center;
/*     */   }
/*     */   
/*     */   private void onEnter(JDialog dialog) {
/* 150 */     this.choice = Integer.valueOf(0);
/* 151 */     dialog.setVisible(false);
/*     */   }
/*     */   
/*     */   private void onCancel(JDialog dialog) {
/* 155 */     this.choice = Integer.valueOf(2);
/* 156 */     dialog.setVisible(false);
/*     */   }
/*     */   
/*     */   private JPanel createChoicePane(JDialog dialog) {
/* 160 */     JPanel choicePane = new JPanel(new BorderLayout());
/* 161 */     choicePane.add(createSecurityOptions(dialog), "West");
/* 162 */     choicePane.add(createOkCancelPane(dialog), "East");
/* 163 */     return choicePane;
/*     */   }
/*     */   
/*     */   private JPanel createOkCancelPane(JDialog dialog) {
/* 167 */     JButton cancelButton = new JButton("Cancelar");
/* 168 */     cancelButton.addActionListener(e -> onCancel(dialog));
/*     */     
/* 170 */     JButton okButton = new JButton("OK");
/* 171 */     okButton.setPreferredSize(cancelButton.getPreferredSize());
/* 172 */     okButton.addActionListener(e -> onEnter(dialog));
/*     */     
/* 174 */     JPanel okCancelPane = new JPanel((LayoutManager)new MigLayout("fillx", "[][]push", "[][]"));
/* 175 */     okCancelPane.add(okButton);
/* 176 */     okCancelPane.add(cancelButton);
/* 177 */     return okCancelPane;
/*     */   }
/*     */   
/*     */   private JPanel createSecurityOptions(JDialog dialog) {
/* 181 */     JLabel secOptionsLabel = new JLabel();
/* 182 */     secOptionsLabel.setIcon(Images.SMALL_LOCK.asIcon());
/* 183 */     secOptionsLabel.setForeground(Color.BLUE);
/* 184 */     secOptionsLabel.setFont(new Font("Tahoma", 0, 12));
/* 185 */     secOptionsLabel.setPreferredSize(new Dimension(280, (secOptionsLabel.getPreferredSize()).height));
/* 186 */     secOptionsLabel.setCursor(new Cursor(12));
/* 187 */     secOptionsLabel.add(createPopupMenu(secOptionsLabel, dialog));
/*     */     
/* 189 */     JPanel pane = new JPanel((LayoutManager)new MigLayout("fillx", "push[]", "[]"));
/* 190 */     pane.add(secOptionsLabel);
/* 191 */     return pane;
/*     */   }
/*     */   
/*     */   private PopupMenu createPopupMenu(JLabel secOptionsLabel, JDialog dialog) {
/* 195 */     this.strategyGui = new PasswordStrategyGUI<>(DEFAULT.get(), new PopupMenu(), dialog, secOptionsLabel);
/* 196 */     return this.strategyGui.menu();
/*     */   }
/*     */   
/*     */   private JPanel createQuestionPane() {
/* 200 */     Icon questionIcon = (Icon)UIManager.get("OptionPane.questionIcon");
/* 201 */     JLabel questionLabel = new JLabel(questionIcon);
/* 202 */     questionLabel.setVerticalAlignment(1);
/* 203 */     JPanel questionPane = new JPanel((LayoutManager)new MigLayout("insets 10 10 0 12"));
/* 204 */     questionPane.add(questionLabel);
/* 205 */     return questionPane;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/PasswordStrategyDialogCallbackHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
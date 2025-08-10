/*     */ package com.github.signer4j.gui;
/*     */ 
/*     */ import com.github.signer4j.ICertificateListUI;
/*     */ import com.github.signer4j.IRepository;
/*     */ import com.github.signer4j.gui.utils.Images;
/*     */ import com.github.signer4j.imp.Config;
/*     */ import com.github.signer4j.imp.Repository;
/*     */ import com.github.signer4j.imp.SwitchRepositoryException;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import net.java.balloontip.BalloonTip;
/*     */ import net.java.balloontip.styles.BalloonTipStyle;
/*     */ import net.java.balloontip.styles.EdgedBalloonStyle;
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
/*     */ class WindowsCertificateListDialog
/*     */   extends CertificateListDialog
/*     */   implements ActionListener
/*     */ {
/*     */   private static final long serialVersionUID = 1222285112497878845L;
/*     */   private JPanel radioOptions;
/*     */   private BalloonTip balloonTip;
/*     */   private JLabel repositoryLabel;
/*     */   private JRadioButton mscapiButton;
/*     */   private final boolean repoWaiting;
/*     */   private JRadioButton standardButton;
/*     */   private RepositoryStrategy currentRepository;
/*  53 */   private Optional<Repository> targetRepository = Optional.empty(); private boolean closed; private MouseListener mscapiListener;
/*     */   private MouseListener standardListener;
/*     */   
/*  56 */   protected WindowsCertificateListDialog(String defaultAlias, ICertificateListUI.IConfigSavedCallback savedCallback, Repository repository, boolean repoWaiting) { super(defaultAlias, savedCallback);
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
/*  77 */     this.closed = false;
/*     */     this.currentRepository = RepositoryStrategy.from(repository).update(this);
/*     */     this.repoWaiting = repoWaiting; }
/*     */   private final void switchRepository(Repository repository) { this.targetRepository = Optional.ofNullable(repository); }
/*  81 */   public void close() { if (!this.closed) {
/*  82 */       disposeBalloon();
/*  83 */       saveRepository();
/*  84 */       super.close();
/*  85 */       this.closed = true;
/*     */     }  }
/*     */   private void saveRepository() { Config.saveRepository(this.currentRepository.repo); }
/*     */   protected void beforeChoice() throws SwitchRepositoryException { if (this.targetRepository.isPresent()) {
/*     */       close(); throw new SwitchRepositoryException(this.repoWaiting, (Repository)this.targetRepository.get());
/*  90 */     }  } private void disposeBalloon() { if (this.balloonTip != null) {
/*  91 */       this.balloonTip.closeBalloon();
/*  92 */       this.balloonTip.removeAll();
/*  93 */       this.balloonTip = null;
/*     */     }  }
/*     */ 
/*     */   
/*     */   protected void showWindowsTip() {
/*  98 */     disposeBalloon();
/*  99 */     createBalloon();
/*     */   }
/*     */   
/*     */   private void createBalloon() {
/* 103 */     this.balloonTip = new BalloonTip(this.repositoryLabel, new JLabel(Images.MSCAPITIP.asIcon()), (BalloonTipStyle)new EdgedBalloonStyle(Color.WHITE, Color.DARK_GRAY), true);
/* 104 */     this.balloonTip.getCloseButton().addActionListener(e -> disposeBalloon());
/*     */   }
/*     */ 
/*     */   
/*     */   protected JPanel createSetup() {
/* 109 */     JPanel pnlNorthEast = new JPanel();
/* 110 */     pnlNorthEast.setLayout(new BorderLayout(0, 0));
/* 111 */     pnlNorthEast.add(createConfigInstall(), "North");
/* 112 */     return pnlNorthEast;
/*     */   }
/*     */   
/*     */   private JPanel createConfigInstall() {
/* 116 */     JPanel panel = new JPanel(new BorderLayout(4, 0));
/* 117 */     panel.add(createOptions(), "West");
/* 118 */     panel.add(createRepositoryLabel(), "Center");
/* 119 */     panel.add(createRefresh(BorderFactory.createEmptyBorder(0, 2, 10, 4)), "East");
/* 120 */     panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
/* 121 */     panel.setVisible(hasSavedCallback());
/* 122 */     return panel;
/*     */   }
/*     */   
/*     */   private JLabel createRepositoryLabel() {
/* 126 */     this.repositoryLabel = new JLabel("");
/* 127 */     this.repositoryLabel.setVerticalAlignment(3);
/* 128 */     this.repositoryLabel.setHorizontalAlignment(4);
/* 129 */     this.repositoryLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 10, 2));
/* 130 */     this.repositoryLabel.setCursor(Cursor.getPredefinedCursor(12));
/* 131 */     return this.repositoryLabel;
/*     */   }
/*     */   
/*     */   private JPanel createOptions() {
/* 135 */     this.radioOptions = new JPanel(new BorderLayout());
/* 136 */     this.radioOptions.add(createStandardRadio(), "West");
/* 137 */     this.radioOptions.add(createMscapiRadio(), "Center");
/*     */     
/* 139 */     ButtonGroup group = new ButtonGroup();
/* 140 */     group.add(this.mscapiButton);
/* 141 */     group.add(this.standardButton);
/*     */     
/* 143 */     return this.radioOptions;
/*     */   }
/*     */   
/*     */   private JRadioButton createMscapiRadio() {
/* 147 */     this.mscapiButton = new JRadioButton(RepositoryStrategy.MSCAPI.getText());
/* 148 */     this.mscapiButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 9, 15));
/* 149 */     this.mscapiButton.setFont(new Font("Tahoma", 0, 12));
/* 150 */     this.mscapiButton.setMnemonic(87);
/* 151 */     this.mscapiButton.setActionCommand(RepositoryStrategy.MSCAPI.name());
/* 152 */     this.mscapiButton.setToolTipText("<html>Certificados reconhecidos pelo sistema Windows (opção <b>não</b> recomendada).</html>");
/* 153 */     this.mscapiButton.addActionListener(this);
/* 154 */     return this.mscapiButton;
/*     */   }
/*     */   
/*     */   private JRadioButton createStandardRadio() {
/* 158 */     this.standardButton = new JRadioButton(RepositoryStrategy.NATIVE.getText());
/* 159 */     this.standardButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 9, 0));
/* 160 */     this.standardButton.setFont(new Font("Tahoma", 0, 12));
/* 161 */     this.standardButton.setMnemonic(80);
/* 162 */     this.standardButton.setActionCommand(RepositoryStrategy.NATIVE.name());
/* 163 */     this.standardButton.setToolTipText("Certificados reconhecidos nativamente pelo PJeOffice (opção recomendada).");
/* 164 */     this.standardButton.addActionListener(this);
/* 165 */     return this.standardButton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 170 */     RepositoryStrategy r = RepositoryStrategy.valueOf(e.getActionCommand());
/* 171 */     if (r != this.currentRepository)
/* 172 */       (this.currentRepository = r).update(this, Optional.ofNullable(e)); 
/*     */   }
/*     */   
/*     */   private static class MouseListener
/*     */     extends MouseAdapter
/*     */   {
/*     */     private Runnable runnable;
/*     */     
/*     */     MouseListener(Runnable runnable) {
/* 181 */       this.runnable = runnable;
/*     */     }
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 185 */       this.runnable.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private enum RepositoryStrategy
/*     */     implements Consumer<WindowsCertificateListDialog>, IRepository
/*     */   {
/* 193 */     MSCAPI("Windows", Repository.MSCAPI)
/*     */     {
/*     */       protected void doUpdate(WindowsCertificateListDialog dialog, Optional<ActionEvent> e)
/*     */       {
/* 197 */         dialog.repositoryLabel.setIcon(Images.WINDOWS.asIcon());
/* 198 */         dialog.repositoryLabel.removeMouseListener(dialog.standardListener);
/* 199 */         dialog.repositoryLabel.setToolTipText("Abrir repositório de certificados do Windows.");
/* 200 */         dialog.repositoryLabel.addMouseListener(dialog.mscapiListener = new WindowsCertificateListDialog.MouseListener(() -> accept(dialog)));
/* 201 */         dialog.mscapiButton.setSelected(true);
/* 202 */         e.ifPresent(a -> {
/*     */               dialog.switchRepository(this.repo);
/*     */               dialog.refresh();
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public void accept(WindowsCertificateListDialog dialog) {
/* 210 */         Throwables.quietly(() -> (new ProcessBuilder(new String[] { "cmd", "/c", "certmgr.msc" })).start().waitFor(400L, TimeUnit.MILLISECONDS));
/* 211 */         dialog.showWindowsTip();
/*     */       }
/*     */     },
/*     */     
/* 215 */     NATIVE("PJeOffice", Repository.NATIVE)
/*     */     {
/*     */       protected void doUpdate(WindowsCertificateListDialog dialog, Optional<ActionEvent> e) {
/* 218 */         dialog.repositoryLabel.setIcon(Images.GEAR.asIcon());
/* 219 */         dialog.repositoryLabel.setToolTipText("Configurar um novo certificado A1 / A3.");
/* 220 */         dialog.repositoryLabel.removeMouseListener(dialog.mscapiListener);
/* 221 */         dialog.repositoryLabel.addMouseListener(dialog.standardListener = new WindowsCertificateListDialog.MouseListener(() -> accept(dialog)));
/* 222 */         dialog.standardButton.setSelected(true);
/* 223 */         e.ifPresent(a -> {
/*     */               dialog.switchRepository(this.repo);
/*     */               dialog.refresh();
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public void accept(WindowsCertificateListDialog dialog) {
/* 231 */         dialog.clickConfig();
/* 232 */         if (dialog.isNeedReload())
/* 233 */           dialog.switchRepository(this.repo); 
/*     */       } };
/*     */     protected final Repository repo;
/*     */     private final String text;
/*     */     
/*     */     static RepositoryStrategy from(Repository repository) {
/*     */       try {
/* 240 */         return valueOf(repository.getName());
/* 241 */       } catch (Exception e) {
/* 242 */         return NATIVE;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RepositoryStrategy(String text, Repository repo) {
/* 251 */       this.text = text;
/* 252 */       this.repo = repo;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getName() {
/* 257 */       return this.repo.getName();
/*     */     }
/*     */     
/*     */     final String getText() {
/* 261 */       return this.text;
/*     */     }
/*     */     
/*     */     protected final RepositoryStrategy update(WindowsCertificateListDialog dialog) {
/* 265 */       return update(dialog, Optional.empty());
/*     */     }
/*     */ 
/*     */     
/*     */     protected final RepositoryStrategy update(WindowsCertificateListDialog dialog, Optional<ActionEvent> e) {
/* 270 */       doUpdate(dialog, e);
/* 271 */       return this;
/*     */     }
/*     */     
/*     */     protected abstract void doUpdate(WindowsCertificateListDialog param1WindowsCertificateListDialog, Optional<ActionEvent> param1Optional);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/WindowsCertificateListDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
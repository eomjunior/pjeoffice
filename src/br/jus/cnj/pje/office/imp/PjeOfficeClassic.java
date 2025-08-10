/*     */ package br.jus.cnj.pje.office.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IPjeFrontEnd;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeLifeCycleFactory;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeVersion;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeWebGlobal;
/*     */ import br.jus.cnj.pje.office.core.imp.shell.ShellExtensionLauncher;
/*     */ import br.jus.cnj.pje.office.updater.imp.Updater;
/*     */ import com.github.signer4j.IAuthStrategyAware;
/*     */ import com.github.signer4j.gui.PasswordStrategyGUI;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Media;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
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
/*     */ public abstract class PjeOfficeClassic
/*     */   extends PjeOfficeApp
/*     */ {
/*     */   private IPjeFrontEnd frontEnd;
/*     */   private PasswordStrategyGUI<Menu> strategyGui;
/*     */   
/*     */   protected PjeOfficeClassic(IPjeFrontEnd frontEnd, PjeLifeCycleFactory factory, String model) {
/*  57 */     super(factory, model);
/*  58 */     this.frontEnd = (IPjeFrontEnd)Args.requireNonNull(frontEnd, "frontEnd is null");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void preInstall() {}
/*     */ 
/*     */   
/*     */   private MenuItem buildServersMenu() {
/*  66 */     MenuItem mnuServer = new MenuItem("Sites autorizados");
/*  67 */     mnuServer.addActionListener(e -> this.office.showAuthorizedServers());
/*  68 */     return mnuServer;
/*     */   }
/*     */   
/*     */   private MenuItem buildConfigMenu() {
/*  72 */     MenuItem mnuConfig = new MenuItem("Configuração de certificado");
/*  73 */     mnuConfig.addActionListener(e -> this.office.showCertificates());
/*  74 */     return mnuConfig;
/*     */   }
/*     */   
/*     */   private MenuItem buildExitMenu() {
/*  78 */     MenuItem mnuExit = new MenuItem("Sair");
/*  79 */     mnuExit.addActionListener(e -> this.office.exit());
/*  80 */     return mnuExit;
/*     */   }
/*     */   
/*     */   private MenuItem buildHelpMenu() {
/*  84 */     MenuItem mnuHelp = new MenuItem("Ajuda");
/*  85 */     mnuHelp.addActionListener(e -> this.office.showHelp());
/*  86 */     return mnuHelp;
/*     */   }
/*     */   
/*     */   private MenuItem buildAboutMenu() {
/*  90 */     MenuItem mnuAbout = new MenuItem("Sobre (versão " + PjeVersion.CURRENT.toString() + this.office.model() + ")");
/*  91 */     mnuAbout.addActionListener(e -> this.office.showAbout());
/*  92 */     return mnuAbout;
/*     */   }
/*     */   
/*     */   protected Menu doOptions(PopupMenu popup, Menu mnuOption) {
/*  96 */     if (PjeOfficeFrontEnd.supportsSystray()) {
/*  97 */       switchMode(mnuOption);
/*     */     }
/*  99 */     return mnuOption;
/*     */   }
/*     */   
/*     */   private Menu buildOptionsMenu() {
/* 103 */     Menu mnuOption = new Menu("Opções");
/* 104 */     MenuItem mnuUpdate = new MenuItem("Verificar nova atualização...");
/* 105 */     mnuUpdate.setEnabled(!Updater.INSTANCE.isDisabled());
/* 106 */     mnuUpdate.addActionListener(e -> this.office.update(false));
/* 107 */     mnuOption.add(mnuUpdate);
/* 108 */     return mnuOption;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKill() {
/* 113 */     LOGGER.info("Liberando ticket repository");
/* 114 */     if (this.strategyGui != null) {
/* 115 */       this.strategyGui.dispose();
/* 116 */       this.strategyGui = null;
/*     */     } 
/* 118 */     LOGGER.info("Liberando frontEnd");
/* 119 */     if (this.frontEnd != null) {
/* 120 */       this.frontEnd.uninstall();
/* 121 */       this.frontEnd = null;
/*     */     } 
/* 123 */     super.onKill();
/*     */   }
/*     */   
/*     */   protected final void switchMode(Menu mnuOption) {
/* 127 */     if (PjeOfficeFrontEnd.supportsSystray()) {
/* 128 */       IPjeFrontEnd front = this.frontEnd.fallback();
/* 129 */       MenuItem mnuDesk = new MenuItem(front.getTitle());
/* 130 */       mnuDesk.addActionListener(e -> systray(false));
/* 131 */       mnuOption.add(mnuDesk);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void systray(boolean closing) {
/* 137 */     if (PjeOfficeFrontEnd.supportsSystray()) {
/* 138 */       IPjeFrontEnd front = this.frontEnd.fallback();
/* 139 */       this.office.kill();
/* 140 */       PjeWebGlobal.recycleAll();
/* 141 */       newInstance(front).start();
/* 142 */     } else if (closing) {
/* 143 */       this.office.kill();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start() {
/* 149 */     super.start();
/* 150 */     PopupMenu popup = new PopupMenu();
/* 151 */     popup.add(buildPdfsMenu());
/* 152 */     popup.add(buildVideosMenu());
/* 153 */     popup.addSeparator();
/* 154 */     popup.add(buildConfigMenu());
/* 155 */     popup.add(buildSecurityMenu());
/* 156 */     popup.add(doOptions(popup, buildOptionsMenu()));
/* 157 */     popup.addSeparator();
/* 158 */     popup.add(buildHelpMenu());
/* 159 */     popup.add(buildAboutMenu());
/* 160 */     popup.addSeparator();
/* 161 */     popup.add(buildExitMenu());
/* 162 */     preInstall();
/* 163 */     install(popup);
/* 164 */     SwingTools.invokeLater(() -> this.office.update(true));
/*     */   }
/*     */   
/*     */   private void install(PopupMenu popup) {
/*     */     try {
/* 169 */       this.frontEnd.install(this.office, popup).show(STARTUP);
/* 170 */     } catch (Exception es) {
/* 171 */       LOGGER.error(this.frontEnd.getTitle() + "Não é suportada. Nova tentativa com Desktop", es);
/* 172 */       this.frontEnd.uninstall();
/* 173 */       this.frontEnd = PjeOfficeFrontEnd.DESKTOP;
/*     */       try {
/* 175 */         this.frontEnd.install(this.office, popup).show(STARTUP);
/* 176 */       } catch (Exception ed) {
/* 177 */         this.frontEnd.uninstall();
/* 178 */         String message = "Incapaz de instanciar frontEnd da aplicação.";
/* 179 */         LOGGER.error(message, ed);
/* 180 */         Dialogs.error(message);
/* 181 */         System.exit(1);
/*     */       } 
/*     */     } 
/* 184 */     Toolkit.getDefaultToolkit().beep();
/*     */   }
/*     */   
/*     */   private Menu buildSecurityMenu() {
/* 188 */     this.strategyGui = new PasswordStrategyGUI((IAuthStrategyAware)this.office, new Menu("Segurança"));
/* 189 */     this.strategyGui.menu().addSeparator();
/* 190 */     this.strategyGui.menu().add(buildServersMenu());
/* 191 */     return this.strategyGui.menu();
/*     */   }
/*     */   
/*     */   private Menu buildVideosMenu() {
/* 195 */     Menu mnuOptions = new Menu("Selecionar vídeo(s) para");
/* 196 */     MenuItem mnu1 = new MenuItem("Dividir a cada 90MB...");
/* 197 */     mnu1.addActionListener(e -> this.office.selectTo(mnu1.getLabel(), Media.MP4, ShellExtensionLauncher::mp4Split90));
/* 198 */     mnuOptions.add(mnu1);
/*     */     
/* 200 */     MenuItem mnu2 = new MenuItem("Dividir a cada 'n'MB...");
/* 201 */     mnu2.addActionListener(e -> this.office.selectTo(mnu2.getLabel(), Media.MP4, ShellExtensionLauncher::mp4SplitNSize));
/* 202 */     mnuOptions.add(mnu2);
/*     */     
/* 204 */     MenuItem mnu3 = new MenuItem("Dividir a cada 'n' minutos...");
/* 205 */     mnu3.addActionListener(e -> this.office.selectTo(mnu3.getLabel(), Media.MP4, ShellExtensionLauncher::mp4SplitNTime));
/* 206 */     mnuOptions.add(mnu3);
/*     */     
/* 208 */     MenuItem mnu4 = new MenuItem("Dividir a corte(s) específico(s)...");
/* 209 */     mnu4.addActionListener(e -> this.office.selectTo(mnu4.getLabel(), Media.MP4, ShellExtensionLauncher::mp4Slice));
/* 210 */     mnuOptions.add(mnu4);
/*     */     
/* 212 */     MenuItem mnu5 = new MenuItem("Extrair áudio...");
/* 213 */     mnu5.addActionListener(e -> this.office.selectTo(mnu5.getLabel(), Media.MP4, ShellExtensionLauncher::mp4Audio));
/* 214 */     mnuOptions.add(mnu5);
/*     */     
/* 216 */     MenuItem mnu6 = new MenuItem("Converter para webm...");
/* 217 */     mnu6.addActionListener(e -> this.office.selectTo(mnu6.getLabel(), Media.MP4, ShellExtensionLauncher::mp4Webm));
/* 218 */     mnuOptions.add(mnu6);
/*     */     
/* 220 */     MenuItem mnu7 = new MenuItem("Otimizar (qualidade vs tamanho)...");
/* 221 */     mnu7.addActionListener(e -> this.office.selectTo(mnu7.getLabel(), Media.MP4, ShellExtensionLauncher::mp4Optimize));
/* 222 */     mnuOptions.add(mnu7);
/*     */     
/* 224 */     return mnuOptions;
/*     */   }
/*     */   
/*     */   private Menu buildPdfsMenu() {
/* 228 */     Menu mnuOptions = new Menu("Selecionar pdf(s) para");
/* 229 */     MenuItem mnu1 = new MenuItem("Assinar e salvar na mesma pasta...");
/* 230 */     mnu1.addActionListener(e -> this.office.selectTo(mnu1.getLabel(), Media.PDF, ShellExtensionLauncher::signAtSameFolder));
/* 231 */     mnuOptions.add(mnu1);
/*     */     
/* 233 */     MenuItem mnu2 = new MenuItem("Assinar e salvar em nova pasta...");
/* 234 */     mnu2.addActionListener(e -> this.office.selectTo(mnu2.getLabel(), Media.PDF, ShellExtensionLauncher::signAtNewFolder));
/* 235 */     mnuOptions.add(mnu2);
/*     */     
/* 237 */     MenuItem mnu3 = new MenuItem("Assinar e salvar em pasta específica...");
/* 238 */     mnu3.addActionListener(e -> this.office.selectTo(mnu3.getLabel(), Media.PDF, ShellExtensionLauncher::signAtOtherFolder));
/* 239 */     mnuOptions.add(mnu3);
/*     */     
/* 241 */     MenuItem mnu4 = new MenuItem("Dividir a cada 10MB...");
/* 242 */     mnu4.addActionListener(e -> this.office.selectTo(mnu4.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplit10));
/* 243 */     mnuOptions.add(mnu4);
/*     */     
/* 245 */     MenuItem mnu5 = new MenuItem("Dividir a cada 'n'MB...");
/* 246 */     mnu5.addActionListener(e -> this.office.selectTo(mnu5.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplitN));
/* 247 */     mnuOptions.add(mnu5);
/*     */     
/* 249 */     MenuItem mnu6 = new MenuItem("Dividir a cada página...");
/* 250 */     mnu6.addActionListener(e -> this.office.selectTo(mnu6.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplitCount1));
/* 251 */     mnuOptions.add(mnu6);
/*     */     
/* 253 */     MenuItem mnu7 = new MenuItem("Dividir a cada 'n' páginas...");
/* 254 */     mnu7.addActionListener(e -> this.office.selectTo(mnu7.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplitCountN));
/* 255 */     mnuOptions.add(mnu7);
/*     */     
/* 257 */     MenuItem mnu8 = new MenuItem("Dividir à(s) página(s) específica(s)...");
/* 258 */     mnu8.addActionListener(e -> this.office.selectTo(mnu8.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplitByPages));
/* 259 */     mnuOptions.add(mnu8);
/*     */     
/* 261 */     MenuItem mnu9 = new MenuItem("Remover páginas pares...");
/* 262 */     mnu9.addActionListener(e -> this.office.selectTo(mnu9.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplitByI));
/* 263 */     mnuOptions.add(mnu9);
/*     */     
/* 265 */     MenuItem mnu10 = new MenuItem("Remover páginas ímpares...");
/* 266 */     mnu10.addActionListener(e -> this.office.selectTo(mnu10.getLabel(), Media.PDF, ShellExtensionLauncher::pdfSplitByP));
/* 267 */     mnuOptions.add(mnu10);
/*     */     
/* 269 */     MenuItem mnu11 = new MenuItem("Unir...");
/* 270 */     mnu11.addActionListener(e -> this.office.selectTo(mnu11.getLabel(), Media.PDF, ShellExtensionLauncher::pdfJoin));
/* 271 */     mnuOptions.add(mnu11);
/* 272 */     return mnuOptions;
/*     */   }
/*     */   
/*     */   protected abstract PjeOfficeClassic newInstance(IPjeFrontEnd paramIPjeFrontEnd);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficeClassic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
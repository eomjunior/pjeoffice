/*     */ package com.github.signer4j.gui;
/*     */ 
/*     */ import com.github.signer4j.AllowedExtensions;
/*     */ import com.github.signer4j.ICertificateListUI;
/*     */ import com.github.signer4j.IDriverSetup;
/*     */ import com.github.signer4j.IFilePath;
/*     */ import com.github.signer4j.gui.utils.Images;
/*     */ import com.github.signer4j.imp.Config;
/*     */ import com.github.signer4j.imp.DriverSetup;
/*     */ import com.github.signer4j.imp.FilePath;
/*     */ import com.github.signer4j.imp.SystemSupport;
/*     */ import com.github.utils4j.gui.imp.ButtonRenderer;
/*     */ import com.github.utils4j.gui.imp.DefaultFileChooser;
/*     */ import com.github.utils4j.gui.imp.SimpleDialog;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import net.java.balloontip.BalloonTip;
/*     */ import net.java.balloontip.styles.BalloonTipStyle;
/*     */ import net.java.balloontip.styles.EdgedBalloonStyle;
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
/*     */ 
/*     */ class CertificateInstallerDialog
/*     */   extends SimpleDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int MININUM_WIDTH = 490;
/*     */   private static final int MININUM_HEIGHT = 545;
/*  99 */   private static final Color SELECTED = new Color(234, 248, 229);
/*     */   
/* 101 */   private static String SEARCH_TITLE_FORMAT = "<html><u>Busca automática</u>%s</html>";
/*     */   
/*     */   private JTable table;
/*     */   
/*     */   private JButton a1Button;
/*     */   
/*     */   private JButton a3Button;
/*     */   private JButton saveButton;
/*     */   private JPanel contentPane;
/*     */   private JButton locateButton;
/*     */   private ButtonRenderer buttonRenderer;
/* 112 */   private String searchResultCount = "&nbsp;";
/*     */   
/*     */   private final ICertificateListUI.IConfigSavedCallback savedCallback;
/*     */   
/* 116 */   private List<IFilePath> a1List = new ArrayList<>();
/* 117 */   private List<IFilePath> a3List = new ArrayList<>();
/*     */   
/* 119 */   private Optional<CertType> current = Optional.empty();
/*     */   
/* 121 */   private final AtomicReference<Thread> searchThread = new AtomicReference<>();
/*     */   
/*     */   CertificateInstallerDialog() {
/* 124 */     this(ICertificateListUI.IConfigSavedCallback.NOTHING);
/*     */   }
/*     */   
/*     */   CertificateInstallerDialog(ICertificateListUI.IConfigSavedCallback savedCallback) {
/* 128 */     super("Configuração de certificado", Config.getIcon(), true);
/* 129 */     this.savedCallback = (ICertificateListUI.IConfigSavedCallback)Args.requireNonNull(savedCallback, "onSaved is null");
/* 130 */     Config.loadA3Paths(this.a3List::add);
/* 131 */     Config.loadA1Paths(this.a1List::add);
/* 132 */     setup();
/*     */   }
/*     */   
/*     */   private void setup() {
/* 136 */     setupLayout();
/* 137 */     setupFrame();
/*     */   }
/*     */   
/*     */   private void setupFrame() {
/* 141 */     setDefaultCloseOperation(2);
/* 142 */     setFixedMinimumSize(new Dimension(490, 249));
/* 143 */     toCenter();
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/* 147 */     this.contentPane = new JPanel();
/* 148 */     this.contentPane.setLayout(new GridLayout(1, 0, 0, 5));
/* 149 */     this.contentPane.setBorder(new EtchedBorder(1, null, null));
/* 150 */     this.contentPane.add(createStep1());
/* 151 */     setContentPane(this.contentPane);
/*     */   }
/*     */   
/*     */   private JPanel createStep1() {
/* 155 */     JPanel step1Pane = new JPanel();
/* 156 */     step1Pane.setLayout(new BorderLayout(0, 0));
/* 157 */     step1Pane.setBorder(new EtchedBorder(1, null, null));
/* 158 */     step1Pane.add(createStep1_Title(), "North");
/* 159 */     step1Pane.add(createStep1_Center(), "Center");
/* 160 */     return step1Pane;
/*     */   }
/*     */   
/*     */   private JPanel createStep1_Center() {
/* 164 */     JPanel step1CenterPane = new JPanel();
/* 165 */     step1CenterPane.setLayout(new BorderLayout(0, 0));
/* 166 */     step1CenterPane.add(createStep1_CertificateType(), "North");
/* 167 */     step1CenterPane.add(createStep1_A1A3Pane(), "Center");
/* 168 */     return step1CenterPane;
/*     */   }
/*     */   
/*     */   private JLabel createStep1_Title() {
/* 172 */     JLabel step1Label = new JLabel("Passo 1");
/* 173 */     step1Label.setFont(new Font("Tahoma", 0, 20));
/* 174 */     return step1Label;
/*     */   }
/*     */   
/*     */   private JPanel createStep1_A1A3Pane() {
/* 178 */     JPanel step1_a1a3_Pane = new JPanel();
/* 179 */     step1_a1a3_Pane.setBorder(new EtchedBorder(1, null, null));
/* 180 */     step1_a1a3_Pane.setLayout(new GridLayout(1, 2, 10, 10));
/* 181 */     step1_a1a3_Pane.add(createStep1_A1Button());
/* 182 */     step1_a1a3_Pane.add(createStep1_A3Button());
/* 183 */     return step1_a1a3_Pane;
/*     */   }
/*     */   
/*     */   private JLabel createStep1_CertificateType() {
/* 187 */     JLabel step1TitleLabel = new JLabel("Configurar um certificado do tipo:");
/* 188 */     step1TitleLabel.setFont(new Font("Tahoma", 0, 16));
/* 189 */     return step1TitleLabel;
/*     */   }
/*     */   
/*     */   private JButton createStep1_A3Button() {
/* 193 */     this.a3Button = new JButton("A3");
/* 194 */     this.a3Button.setToolTipText("Certificado A3 (pkcs11)");
/* 195 */     this.a3Button.addActionListener(e -> onClickA3());
/* 196 */     this.a3Button.setIcon(new ImageIcon(Images.ICON_A3.asImage()));
/* 197 */     this.a3Button.setCursor(new Cursor(12));
/* 198 */     return this.a3Button;
/*     */   }
/*     */   
/*     */   private JButton createStep1_A1Button() {
/* 202 */     this.a1Button = new JButton("A1");
/* 203 */     this.a1Button.setToolTipText("Certificado A1 (pkcs12)");
/* 204 */     this.a1Button.setIcon(new ImageIcon(Images.ICON_A1.asImage()));
/* 205 */     this.a1Button.setCursor(new Cursor(12));
/* 206 */     this.a1Button.addActionListener(e -> onClickA1());
/* 207 */     return this.a1Button;
/*     */   }
/*     */   
/*     */   private JPanel createStep2(CertType type) {
/* 211 */     JPanel step2Pane = new JPanel();
/* 212 */     step2Pane.setLayout(new BorderLayout(0, 0));
/* 213 */     step2Pane.add(createStep2_NorthPane(type), "North");
/* 214 */     step2Pane.add(createStep2_CenterPane(type), "Center");
/* 215 */     step2Pane.add(createStep2_OkCancelPane(), "South");
/* 216 */     return step2Pane;
/*     */   }
/*     */   
/*     */   private JPanel createStep2_CenterPane(CertType type) {
/* 220 */     JPanel step2CenterPane = new JPanel();
/* 221 */     step2CenterPane.setLayout(new CardLayout(0, 0));
/* 222 */     step2CenterPane.add(createStep2_TablePane(type));
/* 223 */     return step2CenterPane;
/*     */   }
/*     */   
/*     */   private JScrollPane createStep2_TablePane(CertType type) {
/* 227 */     this.table = new JTable();
/* 228 */     this.table.setModel(type.model);
/* 229 */     this.table.getColumnModel().getColumn(0).setPreferredWidth(330);
/* 230 */     this.table.getColumnModel().getColumn(1).setPreferredWidth(70);
/* 231 */     this.buttonRenderer = new ButtonRenderer(arg -> this.current.ifPresent(()));
/* 232 */     this.table.getColumnModel().getColumn(1).setCellRenderer((TableCellRenderer)this.buttonRenderer);
/* 233 */     this.table.getColumnModel().getColumn(1).setCellEditor((TableCellEditor)this.buttonRenderer);
/* 234 */     this.table.setSelectionMode(0);
/* 235 */     this.table.setFont(new Font("Tahoma", 0, 13));
/* 236 */     this.table.setFillsViewportHeight(true);
/* 237 */     this.table.setFillsViewportHeight(true);
/* 238 */     this.table.setRowHeight(this.table.getRowHeight() + 10);
/*     */     
/* 240 */     DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)this.table.getTableHeader().getDefaultRenderer();
/* 241 */     renderer.setHorizontalAlignment(2);
/* 242 */     JScrollPane scrollPane = new JScrollPane(this.table);
/* 243 */     return scrollPane;
/*     */   }
/*     */   
/*     */   private JPanel createStep2_NorthPane(CertType type) {
/* 247 */     JPanel step2NorthPane = new JPanel();
/* 248 */     step2NorthPane.setLayout(new BorderLayout(0, 0));
/* 249 */     step2NorthPane.add(createStep2_Title(), "North");
/* 250 */     step2NorthPane.add(createStep2_FinderPane(type), "Center");
/* 251 */     return step2NorthPane;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 256 */     interruptSearchThread();
/* 257 */     super.close();
/*     */   }
/*     */   
/*     */   private JPanel createStep2_OkCancelPane() {
/* 261 */     JButton cancelButton = new JButton("Cancelar");
/* 262 */     cancelButton.addActionListener(e -> close());
/* 263 */     this.saveButton = new JButton("OK");
/* 264 */     this.saveButton.setPreferredSize(cancelButton.getPreferredSize());
/* 265 */     this.saveButton.addActionListener(e -> onSave());
/*     */     
/* 267 */     JPanel okCancelPane = new JPanel();
/* 268 */     okCancelPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][]", "[][]"));
/* 269 */     okCancelPane.add(this.saveButton);
/* 270 */     okCancelPane.add(cancelButton);
/* 271 */     return okCancelPane;
/*     */   }
/*     */   
/*     */   private JPanel createStep2_FinderPane(CertType type) {
/* 275 */     JPanel finderPane = new JPanel();
/* 276 */     finderPane.setLayout(new GridLayout(2, 1, 0, 0));
/* 277 */     finderPane.add(createStep2_Headers(type));
/* 278 */     finderPane.add(createStep2_LocateButton());
/* 279 */     return finderPane;
/*     */   }
/*     */   
/*     */   private JPanel createStep2_Headers(CertType type) {
/* 283 */     JPanel titlePanel = new JPanel(new BorderLayout());
/* 284 */     titlePanel.add(createStep2_Title(type), "Center");
/* 285 */     titlePanel.add(createStep2_AutoSearch(type), "East");
/* 286 */     return titlePanel;
/*     */   }
/*     */   
/*     */   private JLabel createStep2_AutoSearch(CertType type) {
/* 290 */     return type.createAutoSearchLabel(this);
/*     */   }
/*     */   
/*     */   private void beginAutoSearch() {
/* 294 */     this.a1Button.setEnabled(false);
/* 295 */     this.a3Button.setEnabled(false);
/* 296 */     this.locateButton.setEnabled(false);
/* 297 */     this.buttonRenderer.setViewEnabled(false);
/* 298 */     this.saveButton.setEnabled(false);
/* 299 */     this.table.setEnabled(false);
/*     */   }
/*     */   
/*     */   private void endAutoSearch(List<IFilePath> found) {
/* 303 */     this.a1Button.setEnabled(true);
/* 304 */     this.a3Button.setEnabled(true);
/* 305 */     this.locateButton.setEnabled(true);
/* 306 */     this.saveButton.setEnabled(true);
/* 307 */     this.buttonRenderer.setViewEnabled(true);
/* 308 */     this.table.setEnabled(true);
/* 309 */     this.searchResultCount = "&nbsp;(" + found.size() + " localizada" + ((found.size() == 1) ? "" : "s") + ") &nbsp;";
/* 310 */     setComponent(CertType.A3, found);
/*     */   }
/*     */   
/*     */   private JLabel createStep2_Title() {
/* 314 */     JLabel step2Label = new JLabel("Passo 2");
/* 315 */     step2Label.setFont(new Font("Tahoma", 0, 20));
/* 316 */     return step2Label;
/*     */   }
/*     */   
/*     */   private JButton createStep2_LocateButton() {
/* 320 */     this.locateButton = new JButton("Localizar...");
/* 321 */     this.locateButton.addActionListener(e -> onLocate());
/* 322 */     return this.locateButton;
/*     */   }
/*     */   
/*     */   private JLabel createStep2_Title(CertType type) {
/* 326 */     JLabel pathTitle = new JLabel(type.labelTitle());
/* 327 */     pathTitle.setFont(new Font("Tahoma", 0, 16));
/* 328 */     return pathTitle;
/*     */   }
/*     */   
/*     */   private void onLocate() {
/* 332 */     this.current.ifPresent(c -> {
/*     */           DefaultFileChooser chooser = new DefaultFileChooser();
/*     */           chooser.setFileSelectionMode(0);
/*     */           chooser.setAcceptAllFileFilterUsed(false);
/*     */           chooser.setMultiSelectionEnabled(true);
/*     */           chooser.setDialogTitle(c.chooseTitle());
/*     */           FileNameExtensionFilter filter = c.fileFilter();
/*     */           chooser.resetChoosableFileFilters();
/*     */           chooser.setFileFilter(filter);
/*     */           chooser.addChoosableFileFilter(filter);
/*     */           if (1 == chooser.showOpenDialog(null)) {
/*     */             return;
/*     */           }
/*     */           File[] files = chooser.getSelectedFiles();
/*     */           if (files == null || files.length == 0) {
/*     */             return;
/*     */           }
/*     */           for (File file : files)
/*     */             c.load(file); 
/*     */         });
/*     */   }
/*     */   
/*     */   private void onSave() {
/* 355 */     this.current.ifPresent(c -> c.save(this.a1List, this.a3List));
/* 356 */     close();
/* 357 */     this.savedCallback.call(
/* 358 */         Collections.unmodifiableList(this.a1List), 
/* 359 */         Collections.unmodifiableList(this.a3List));
/*     */   }
/*     */ 
/*     */   
/*     */   private void onClickA1() {
/* 364 */     if (!CertType.A1.equals(this.current.orElse(CertType.A3))) {
/* 365 */       this.a1Button.setBackground(SELECTED);
/* 366 */       this.a3Button.setBackground((Color)null);
/* 367 */       this.current.ifPresent(c -> this.a3List = new ArrayList<>(c.model.entries));
/* 368 */       setComponent(CertType.A1, this.a1List);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onClickA3() {
/* 373 */     if (!CertType.A3.equals(this.current.orElse(CertType.A1))) {
/* 374 */       this.a3Button.setBackground(SELECTED);
/* 375 */       this.a1Button.setBackground((Color)null);
/* 376 */       this.current.ifPresent(c -> this.a1List = new ArrayList<>(c.model.entries));
/* 377 */       setComponent(CertType.A3, this.a3List);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setComponent(CertType type, List<IFilePath> actual) {
/* 382 */     frameRefit(!this.current.isPresent());
/* 383 */     renderStep2(type);
/* 384 */     type.load(actual);
/* 385 */     this.current = Optional.of(type);
/*     */   }
/*     */   
/*     */   private void frameRefit(boolean center) {
/* 389 */     setFixedMinimumSize(new Dimension(490, 545));
/* 390 */     setBounds(getX(), getY(), 490, 545);
/* 391 */     if (center) toCenter(); 
/*     */   }
/*     */   
/*     */   private void renderStep2(CertType type) {
/* 395 */     JPanel step2Pane = createStep2(type);
/* 396 */     this.current.ifPresent(c -> this.contentPane.remove(this.contentPane.getComponentCount() - 1));
/* 397 */     ((GridLayout)this.contentPane.getLayout()).setRows(2);
/* 398 */     this.contentPane.add(step2Pane);
/* 399 */     this.contentPane.revalidate();
/* 400 */     this.contentPane.updateUI();
/*     */   }
/*     */   
/*     */   public enum Action {
/* 404 */     REMOVER;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 408 */       return "Remover";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class PathModel
/*     */     extends AbstractTableModel
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final List<IFilePath> entries;
/*     */     private final String itemPath;
/*     */     
/*     */     public PathModel(String itemPath) {
/* 421 */       this.itemPath = itemPath;
/* 422 */       this.entries = new LinkedList<>();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowCount() {
/* 427 */       return this.entries.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 432 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getColumnClass(int columnIndex) {
/* 437 */       switch (columnIndex) {
/*     */         case 0:
/* 439 */           return String.class;
/*     */         case 2:
/* 441 */           return CertificateInstallerDialog.Action.class;
/*     */       } 
/* 443 */       return Object.class;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isCellEditable(int row, int column) {
/* 449 */       return (column == 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getColumnName(int columnIndex) {
/* 454 */       switch (columnIndex) {
/*     */         case 0:
/* 456 */           return this.itemPath;
/*     */         case 1:
/* 458 */           return "Ação";
/*     */       } 
/* 460 */       return "?";
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int rowIndex, int columnIndex) {
/* 465 */       IFilePath entry = this.entries.get(rowIndex);
/* 466 */       switch (columnIndex) {
/*     */         case 0:
/* 468 */           return entry.getPath();
/*     */         case 1:
/* 470 */           return CertificateInstallerDialog.Action.REMOVER;
/*     */       } 
/* 472 */       return "?";
/*     */     }
/*     */     
/*     */     public final void remove(int index) {
/* 476 */       if (index < 0 || index >= this.entries.size())
/*     */         return; 
/* 478 */       this.entries.remove(index);
/* 479 */       fireTableRowsDeleted(index, index);
/*     */     }
/*     */     
/*     */     public final void add(IFilePath path) {
/* 483 */       if (this.entries.contains(path))
/*     */         return; 
/* 485 */       int row = this.entries.size();
/* 486 */       this.entries.add(path);
/* 487 */       fireTableRowsInserted(row, row);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 491 */       this.entries.clear();
/* 492 */       fireTableDataChanged();
/*     */     }
/*     */     
/*     */     public void load(List<IFilePath> entry) {
/* 496 */       clear();
/* 497 */       int i = 0;
/* 498 */       while (i < entry.size()) {
/* 499 */         IFilePath item = entry.get(i);
/* 500 */         if (!this.entries.contains(item)) {
/* 501 */           int row = this.entries.size();
/* 502 */           this.entries.add(item);
/* 503 */           fireTableRowsInserted(row, row);
/*     */         } 
/* 505 */         i++;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class A1PathModel extends PathModel {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     A1PathModel() {
/* 514 */       super("Arquivo");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class A3PathModel extends PathModel {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     A3PathModel() {
/* 522 */       super("Biblioteca");
/*     */     }
/*     */   }
/*     */   
/*     */   private enum CertType {
/* 527 */     A1((String)new CertificateInstallerDialog.A1PathModel()) {
/*     */       String labelTitle() {
/* 529 */         return "Localize o certificado A1:";
/*     */       }
/*     */ 
/*     */       
/*     */       protected void load(List<IFilePath> actual) {
/* 534 */         List<IFilePath> m = new ArrayList<>();
/* 535 */         Config.loadA1Paths(m::add);
/* 536 */         m.addAll(actual);
/* 537 */         this.model.load(m);
/*     */       }
/*     */ 
/*     */       
/*     */       void save(List<IFilePath> listA1, List<IFilePath> listA3) {
/* 542 */         listA1.clear();
/* 543 */         listA1.addAll(this.model.entries);
/* 544 */         super.save(listA1, listA3);
/*     */       }
/*     */ 
/*     */       
/*     */       protected String chooseTitle() {
/* 549 */         return "Selecione o(s) certificado(s) A1";
/*     */       }
/*     */ 
/*     */       
/*     */       protected FileNameExtensionFilter fileFilter() {
/* 554 */         return AllowedExtensions.CERTIFICATES;
/*     */       }
/*     */ 
/*     */       
/*     */       protected JLabel createAutoSearchLabel(CertificateInstallerDialog dialog) {
/* 559 */         return new JLabel("");
/*     */       }
/*     */     },
/* 562 */     A3((String)new CertificateInstallerDialog.A3PathModel())
/*     */     {
/*     */       String labelTitle() {
/* 565 */         return "Localize a biblioteca do driver A3:";
/*     */       }
/*     */ 
/*     */       
/*     */       void load(List<IFilePath> actual) {
/* 570 */         List<IFilePath> m = new ArrayList<>();
/* 571 */         Config.loadA3Paths(m::add);
/* 572 */         m.addAll(actual);
/* 573 */         this.model.load(m);
/*     */       }
/*     */ 
/*     */       
/*     */       void save(List<IFilePath> listA1, List<IFilePath> listA3) {
/* 578 */         listA3.clear();
/* 579 */         listA3.addAll(this.model.entries);
/* 580 */         super.save(listA1, listA3);
/*     */       }
/*     */ 
/*     */       
/*     */       protected String chooseTitle() {
/* 585 */         return "Selecione o driver do dispositivo";
/*     */       }
/*     */ 
/*     */       
/*     */       protected FileNameExtensionFilter fileFilter() {
/* 590 */         return AllowedExtensions.LIBRARIES;
/*     */       }
/*     */ 
/*     */       
/*     */       protected JLabel createAutoSearchLabel(final CertificateInstallerDialog dialog) {
/* 595 */         final JLabel autoSearch = new JLabel(String.format(CertificateInstallerDialog.SEARCH_TITLE_FORMAT, new Object[] { CertificateInstallerDialog.access$300(dialog) }));
/* 596 */         autoSearch.setToolTipText("<html>Busca automaticamente pelas principais bibliotecas<br>presentes no seu sistema. Note que o driver do seu<br>dispositivo <b>deve</b> ter sido instalado <b>antes</b>!</html>");
/*     */ 
/*     */ 
/*     */         
/* 600 */         autoSearch.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
/* 601 */         autoSearch.setCursor(Cursor.getPredefinedCursor(12));
/* 602 */         autoSearch.setForeground(Color.BLUE);
/* 603 */         autoSearch.setFont(new Font("Tahoma", 0, 12));
/* 604 */         autoSearch.addMouseListener(new MouseAdapter() {
/*     */               public void mouseClicked(MouseEvent e) {
/* 606 */                 autoSearch.setText(String.format(CertificateInstallerDialog.SEARCH_TITLE_FORMAT, new Object[] { "&nbsp;" }));
/* 607 */                 dialog.autoSearchA3Library(autoSearch);
/*     */               }
/*     */             });
/* 610 */         return autoSearch;
/*     */       }
/*     */     };
/*     */     
/*     */     public final CertificateInstallerDialog.PathModel model;
/*     */     
/*     */     CertType(CertificateInstallerDialog.PathModel model) {
/* 617 */       this.model = model;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void save(List<IFilePath> listA1, List<IFilePath> listA3) {
/* 625 */       Config.saveA1Paths(listA1.<IFilePath>toArray(new IFilePath[listA1.size()]));
/* 626 */       Config.saveA3Paths(listA3.<IFilePath>toArray(new IFilePath[listA3.size()]));
/*     */     }
/*     */     
/*     */     void load(File file) {
/* 630 */       this.model.add((IFilePath)new FilePath(file.toPath()));
/*     */     } protected abstract JLabel createAutoSearchLabel(CertificateInstallerDialog param1CertificateInstallerDialog); protected abstract FileNameExtensionFilter fileFilter();
/*     */     abstract void load(List<IFilePath> param1List);
/*     */     void remove(int row) {
/* 634 */       if (row >= 0) {
/* 635 */         this.model.remove(row);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     abstract String chooseTitle();
/*     */     
/*     */     abstract String labelTitle();
/*     */   }
/*     */   
/*     */   private void interruptSearchThread() {
/* 646 */     Optional.ofNullable(this.searchThread.getAndSet(null)).ifPresent(Thread::interrupt);
/*     */   }
/*     */   
/*     */   protected void autoSearchA3Library(JLabel autoSearch) {
/* 650 */     MouseListener[] mls = autoSearch.getMouseListeners();
/* 651 */     for (MouseListener ml : mls) {
/* 652 */       autoSearch.removeMouseListener(ml);
/*     */     }
/* 654 */     beginAutoSearch();
/*     */     
/* 656 */     List<IFilePath> libs = (List<IFilePath>)SystemSupport.getDefault().getStrategy().queriedPaths().stream().map(x$0 -> Paths.get(x$0, new String[0])).map(FilePath::new).collect(Collectors.toList());
/*     */     
/* 658 */     JProgressBar progressBar = new JProgressBar();
/* 659 */     progressBar.setMaximum(libs.size());
/* 660 */     progressBar.setIndeterminate(false);
/* 661 */     progressBar.setMinimum(0);
/* 662 */     progressBar.setValue(0);
/* 663 */     progressBar.setStringPainted(true);
/* 664 */     progressBar.setString("");
/*     */     
/* 666 */     BalloonTip balloonTip = new BalloonTip(autoSearch, progressBar, (BalloonTipStyle)new EdgedBalloonStyle(Color.WHITE, Color.DARK_GRAY), false);
/* 667 */     balloonTip.setVisible(true);
/*     */     
/* 669 */     this.searchThread.set(Threads.startDaemon(() -> {
/*     */             Set<IDriverSetup> found = new HashSet<>(5);
/*     */             int i = 0;
/*     */             while (i < libs.size() && !Thread.currentThread().isInterrupted()) {
/*     */               int it = i;
/*     */               IFilePath file = libs.get(it);
/*     */               SwingTools.invokeLater(());
/*     */               Threads.sleep(25L);
/*     */               i++;
/*     */             } 
/*     */             SwingTools.invokeLater(());
/*     */           }));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/CertificateInstallerDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
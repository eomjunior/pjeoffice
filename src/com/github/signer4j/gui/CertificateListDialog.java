/*     */ package com.github.signer4j.gui;
/*     */ 
/*     */ import com.github.signer4j.ICertificateListUI;
/*     */ import com.github.signer4j.IFilePath;
/*     */ import com.github.signer4j.gui.utils.Images;
/*     */ import com.github.signer4j.imp.Config;
/*     */ import com.github.signer4j.imp.Repository;
/*     */ import com.github.signer4j.imp.SwitchRepositoryException;
/*     */ import com.github.utils4j.gui.imp.SimpleDialog;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.TableCellRenderer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CertificateListDialog
/*     */   extends SimpleDialog
/*     */   implements ICertificateListUI
/*     */ {
/*     */   private static final long serialVersionUID = -1L;
/*     */   private static ICertificateListUI.IChoice UNDEFINED_CHOICE = () -> Optional.empty();
/*  84 */   private static final Dimension MININUM_SIZE = new Dimension(740, 302);
/*     */   
/*     */   private JTable table;
/*     */   
/*     */   private JButton okButton;
/*     */   
/*     */   private final String defaultAlias;
/*     */   
/*     */   private JCheckBox rememberMeCheckbox;
/*     */   
/*     */   private final ICertificateListUI.IConfigSavedCallback savedCallback;
/*     */   
/*  96 */   private Optional<ICertificateListUI.ICertificateEntry> selectedEntry = Optional.empty();
/*     */   
/*  98 */   private ICertificateListUI.IChoice choice = UNDEFINED_CHOICE;
/*     */   
/*     */   protected CertificateListDialog(String defaultAlias, ICertificateListUI.IConfigSavedCallback savedCallback) {
/* 101 */     super("Seleção de certificado", Config.getIcon(), true);
/* 102 */     this.defaultAlias = (String)Args.requireNonNull(defaultAlias, "defaultAlias is null");
/* 103 */     this.savedCallback = (ICertificateListUI.IConfigSavedCallback)Args.requireNonNull(savedCallback, "onSaved is null");
/* 104 */     setup();
/*     */   }
/*     */   
/*     */   protected final boolean hasSavedCallback() {
/* 108 */     return (this.savedCallback != ICertificateListUI.IConfigSavedCallback.NOTHING);
/*     */   }
/*     */   
/*     */   private void setup() {
/* 112 */     setupLayout();
/* 113 */     setDefaultCloseOperation(2);
/* 114 */     setMinimumSize(MININUM_SIZE);
/* 115 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent windowEvent) {
/* 117 */             CertificateListDialog.this.clickCancel((ActionEvent)null);
/*     */           }
/*     */         });
/* 120 */     toCenter();
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/* 124 */     JPanel contentPane = new JPanel();
/* 125 */     contentPane.setBorder(new EtchedBorder(1, null, null));
/* 126 */     contentPane.setLayout(new BorderLayout(0, 0));
/* 127 */     contentPane.add(createNorth(), "North");
/* 128 */     contentPane.add(createCenter(), "Center");
/* 129 */     contentPane.add(createSouth(), "South");
/* 130 */     setContentPane(contentPane);
/*     */   }
/*     */   
/*     */   private JPanel createNorth() {
/* 134 */     JPanel pnlNorth = new JPanel();
/* 135 */     pnlNorth.setLayout(new BorderLayout(0, 0));
/* 136 */     pnlNorth.add(createCertListLabel());
/* 137 */     pnlNorth.add(createSetup(), "East");
/* 138 */     return pnlNorth;
/*     */   }
/*     */   
/*     */   protected JPanel createSetup() {
/* 142 */     JPanel pnlNorthEast = new JPanel();
/* 143 */     pnlNorthEast.setLayout(new BorderLayout(0, 0));
/* 144 */     pnlNorthEast.add(createHeaderConfig(), "North");
/* 145 */     return pnlNorthEast;
/*     */   }
/*     */   
/*     */   private JPanel createHeaderConfig() {
/* 149 */     JPanel headerPanel = new JPanel();
/* 150 */     headerPanel.setLayout(new BorderLayout());
/* 151 */     headerPanel.add(createConfigInstall(), "Center");
/* 152 */     headerPanel.add(createRefresh(BorderFactory.createEmptyBorder(15, 2, 0, 4)), "East");
/* 153 */     return headerPanel;
/*     */   }
/*     */   
/*     */   private JLabel createCertListLabel() {
/* 157 */     JLabel lblCertificateList = new JLabel("Certificados Disponíveis");
/* 158 */     lblCertificateList.setIcon(Images.CERTIFICATE.asIcon());
/* 159 */     lblCertificateList.setHorizontalAlignment(2);
/* 160 */     lblCertificateList.setFont(new Font("Tahoma", 1, 15));
/* 161 */     return lblCertificateList;
/*     */   }
/*     */   
/*     */   protected final JLabel createRefresh(Border border) {
/* 165 */     JLabel refreshLabel = new JLabel("");
/* 166 */     refreshLabel.setVerticalAlignment(3);
/* 167 */     refreshLabel.setHorizontalAlignment(4);
/* 168 */     refreshLabel.setIcon(Images.REFRESH.asIcon());
/* 169 */     refreshLabel.setBorder(border);
/* 170 */     refreshLabel.setCursor(Cursor.getPredefinedCursor(12));
/* 171 */     refreshLabel.setToolTipText("Recarrega e atualiza a lista de certificados abaixo.");
/* 172 */     refreshLabel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 174 */             CertificateListDialog.this.refresh();
/*     */           }
/*     */         });
/* 177 */     refreshLabel.setVisible(hasSavedCallback());
/* 178 */     return refreshLabel;
/*     */   }
/*     */   
/*     */   private JLabel createConfigInstall() {
/* 182 */     JLabel configLabel = new JLabel("<html><u>Configurar um novo certificado</u>&nbsp;</html>");
/* 183 */     configLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
/* 184 */     configLabel.setVerticalAlignment(3);
/* 185 */     configLabel.setCursor(Cursor.getPredefinedCursor(12));
/* 186 */     configLabel.setForeground(Color.BLUE);
/* 187 */     configLabel.setFont(new Font("Tahoma", 0, 12));
/* 188 */     configLabel.setHorizontalAlignment(2);
/* 189 */     configLabel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 191 */             CertificateListDialog.this.clickConfig();
/*     */           }
/*     */         });
/* 194 */     configLabel.setVisible(hasSavedCallback());
/* 195 */     return configLabel;
/*     */   }
/*     */   
/*     */   private JPanel createCenter() {
/* 199 */     JPanel centerPane = new JPanel();
/* 200 */     centerPane.setLayout(new CardLayout(0, 0));
/* 201 */     this.table = new JTable();
/* 202 */     this.table.setModel(new CertificateModel());
/* 203 */     this.table.getColumnModel().getColumn(0).setPreferredWidth(70);
/* 204 */     this.table.getColumnModel().getColumn(1).setPreferredWidth(167);
/* 205 */     this.table.getColumnModel().getColumn(2).setPreferredWidth(132);
/* 206 */     this.table.getColumnModel().getColumn(3).setPreferredWidth(111);
/* 207 */     this.table.getColumnModel().getColumn(3).setMinWidth(111);
/* 208 */     TableCellRenderer renderer = this.table.getTableHeader().getDefaultRenderer();
/* 209 */     ((DefaultTableCellRenderer)renderer).setHorizontalAlignment(2);
/* 210 */     this.table.setSelectionMode(0);
/* 211 */     this.table.setFont(new Font("Tahoma", 0, 13));
/* 212 */     this.table.setFillsViewportHeight(true);
/* 213 */     this.table.setBorder((Border)null);
/* 214 */     this.table.getSelectionModel().addListSelectionListener(this::onCertificateSelected);
/* 215 */     JScrollPane scrollPane = new JScrollPane(this.table);
/* 216 */     centerPane.add(scrollPane);
/* 217 */     return centerPane;
/*     */   }
/*     */   
/*     */   private void onCertificateSelected(ListSelectionEvent e) {
/* 221 */     int i, selectedRow = this.table.getSelectedRow();
/* 222 */     boolean enabled = false;
/* 223 */     if (selectedRow < 0) {
/* 224 */       this.selectedEntry = Optional.empty();
/* 225 */       this.rememberMeCheckbox.setSelected(false);
/*     */     } else {
/* 227 */       CertificateModel model = (CertificateModel)this.table.getModel();
/* 228 */       ICertificateListUI.ICertificateEntry rowEntry = model.getEntryAt(selectedRow);
/* 229 */       this.selectedEntry = Optional.of(rowEntry);
/* 230 */       i = enabled | (!rowEntry.isExpired() ? 1 : 0);
/* 231 */       this.rememberMeCheckbox.setSelected((i != 0 && this.defaultAlias.equals(rowEntry.getId())));
/* 232 */       rowEntry.setRemembered(this.rememberMeCheckbox.isSelected());
/*     */     } 
/* 234 */     this.okButton.setEnabled(i);
/* 235 */     this.rememberMeCheckbox.setEnabled(i);
/*     */   }
/*     */   
/*     */   private JPanel createSouth() {
/* 239 */     this.rememberMeCheckbox = new JCheckBox("Memorizar este certificado como padrão e não perguntar novamente.");
/* 240 */     this.rememberMeCheckbox.setEnabled(false);
/* 241 */     this.rememberMeCheckbox.setSelected(false);
/* 242 */     JButton cancelButton = new JButton("Cancelar");
/* 243 */     cancelButton.addActionListener(this::clickCancel);
/* 244 */     this.okButton = new JButton("OK");
/* 245 */     this.okButton.setPreferredSize(cancelButton.getPreferredSize());
/* 246 */     this.okButton.setEnabled(false);
/* 247 */     this.okButton.addActionListener(arg -> close());
/* 248 */     JPanel southPane = new JPanel();
/* 249 */     southPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][][]", "[][][]"));
/* 250 */     southPane.add(this.rememberMeCheckbox);
/* 251 */     southPane.add(this.okButton);
/* 252 */     southPane.add(cancelButton);
/* 253 */     return southPane;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CertificateModel
/*     */     extends AbstractTableModel
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 263 */     private final List<ICertificateListUI.ICertificateEntry> entries = new LinkedList<>();
/*     */ 
/*     */ 
/*     */     
/*     */     public int getRowCount() {
/* 268 */       return this.entries.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 273 */       return 4;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCellEditable(int row, int column) {
/* 278 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getColumnName(int columnIndex) {
/* 283 */       switch (columnIndex) {
/*     */         case 0:
/* 285 */           return "Dispositivo";
/*     */         case 1:
/* 287 */           return "Nome";
/*     */         case 2:
/* 289 */           return "Emitido Por";
/*     */         case 3:
/* 291 */           return "Validade";
/*     */       } 
/* 293 */       return "?";
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int rowIndex, int columnIndex) {
/* 298 */       ICertificateListUI.ICertificateEntry entry = this.entries.get(rowIndex);
/* 299 */       switch (columnIndex) {
/*     */         case 0:
/* 301 */           return entry.getDevice();
/*     */         case 1:
/* 303 */           return entry.getName();
/*     */         case 2:
/* 305 */           return entry.getIssuer();
/*     */         case 3:
/* 307 */           return entry.getDate();
/*     */       } 
/* 309 */       return "?";
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getColumnClass(int columnIndex) {
/* 314 */       return String.class;
/*     */     }
/*     */     
/*     */     public ICertificateListUI.ICertificateEntry getEntryAt(int row) {
/* 318 */       return this.entries.get(row);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 322 */       this.entries.clear();
/* 323 */       fireTableDataChanged();
/*     */     }
/*     */     
/*     */     public CertificateModel load(List<ICertificateListUI.ICertificateEntry> entry) {
/* 327 */       clear();
/* 328 */       int i = 0;
/* 329 */       while (i < entry.size()) {
/* 330 */         this.entries.add(entry.get(i));
/* 331 */         fireTableRowsInserted(i, i);
/* 332 */         i++;
/*     */       } 
/* 334 */       return this;
/*     */     }
/*     */     
/*     */     public void preselect(JTable table) {
/* 338 */       AtomicReference<Integer> idx = new AtomicReference<>();
/* 339 */       if (IntStream.range(0, this.entries.size()).filter(i -> !((ICertificateListUI.ICertificateEntry)this.entries.get(i)).isExpired()).peek(idx::set).count() == 1L) {
/* 340 */         table.setRowSelectionInterval(((Integer)idx.get()).intValue(), ((Integer)idx.get()).intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void clickConfig() {
/* 346 */     (new CertificateInstallerDialog(this::saveCallback)).showToFront();
/* 347 */     onAfterConfig();
/*     */   }
/*     */   
/*     */   protected final boolean isNeedReload() {
/* 351 */     return (this.choice == ICertificateListUI.IChoice.NEED_RELOAD);
/*     */   }
/*     */   
/*     */   protected void onAfterConfig() {
/* 355 */     if (this.choice == ICertificateListUI.IChoice.NEED_RELOAD) {
/* 356 */       close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void saveCallback(List<IFilePath> a1list, List<IFilePath> a3List) {
/* 361 */     this.choice = ICertificateListUI.IChoice.NEED_RELOAD;
/* 362 */     this.table.getSelectionModel().clearSelection();
/* 363 */     this.savedCallback.call(a1list, a3List);
/*     */   }
/*     */   
/*     */   protected final void refresh() {
/* 367 */     this.choice = ICertificateListUI.IChoice.NEED_RELOAD;
/* 368 */     close();
/*     */   }
/*     */   
/*     */   private void clickCancel(ActionEvent e) {
/* 372 */     ((CertificateModel)this.table.getModel()).clear();
/* 373 */     this.selectedEntry = Optional.empty();
/* 374 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICertificateListUI.IChoice choose(List<ICertificateListUI.ICertificateEntry> entries) throws SwitchRepositoryException {
/* 379 */     Args.requireNonNull(entries, "entries is null");
/* 380 */     this.rememberMeCheckbox.setSelected(false);
/* 381 */     this.rememberMeCheckbox.setEnabled(false);
/*     */     
/* 383 */     CertificateModel model = (CertificateModel)this.table.getModel();
/* 384 */     model.load(entries);
/* 385 */     model.preselect(this.table);
/*     */     
/* 387 */     showToFront();
/*     */     
/* 389 */     beforeChoice();
/*     */     
/* 391 */     this.selectedEntry.ifPresent(entry -> {
/*     */           if (this.defaultAlias.equals(entry.getId())) {
/*     */             if (!this.rememberMeCheckbox.isSelected()) {
/*     */               Config.save("");
/*     */             }
/*     */           } else if (this.rememberMeCheckbox.isSelected()) {
/*     */             Config.save(entry.getId());
/*     */           } 
/*     */         });
/* 400 */     close();
/* 401 */     return (this.choice == ICertificateListUI.IChoice.NEED_RELOAD) ? this.choice : (() -> this.selectedEntry);
/*     */   }
/*     */   
/*     */   protected void beforeChoice() throws SwitchRepositoryException {}
/*     */   
/*     */   public static ICertificateListUI.IChoice display(List<ICertificateListUI.ICertificateEntry> entries) throws SwitchRepositoryException {
/* 407 */     return display(entries, false);
/*     */   }
/*     */   
/*     */   public static ICertificateListUI.IChoice display(List<ICertificateListUI.ICertificateEntry> entries, boolean repoWaiting) throws SwitchRepositoryException {
/* 411 */     return display(entries, repoWaiting, true);
/*     */   }
/*     */   
/*     */   public static ICertificateListUI.IChoice display(List<ICertificateListUI.ICertificateEntry> entries, boolean repoWaiting, boolean auto) throws SwitchRepositoryException {
/* 415 */     return display(entries, repoWaiting, auto, ICertificateListUI.IConfigSavedCallback.NOTHING);
/*     */   }
/*     */   
/*     */   public static ICertificateListUI.IChoice display(List<ICertificateListUI.ICertificateEntry> entries, boolean repoWaiting, boolean auto, ICertificateListUI.IConfigSavedCallback saveCallback) throws SwitchRepositoryException {
/* 419 */     return display(entries, repoWaiting, auto, saveCallback, Repository.NATIVE);
/*     */   }
/*     */   
/*     */   public static ICertificateListUI.IChoice display(List<ICertificateListUI.ICertificateEntry> entries, boolean repoWaiting, boolean auto, ICertificateListUI.IConfigSavedCallback saveCallback, Repository repository) throws SwitchRepositoryException {
/* 423 */     Args.requireNonNull(entries, "entries is null");
/* 424 */     Args.requireNonNull(saveCallback, "onSaved is null");
/* 425 */     String defaultAlias = Config.defaultAlias().orElse("$not_found$");
/* 426 */     if (auto) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 431 */       Optional<ICertificateListUI.ICertificateEntry> defaultEntry = entries.stream().filter(c -> (c.getId().equals(defaultAlias) && !c.isExpired())).findFirst();
/*     */       
/* 433 */       if (defaultEntry.isPresent()) {
/* 434 */         return () -> defaultEntry;
/*     */       }
/*     */       
/* 437 */       AtomicReference<ICertificateListUI.ICertificateEntry> choosen = new AtomicReference<>();
/* 438 */       if (entries
/*     */         
/* 440 */         .stream()
/* 441 */         .filter(c -> !c.isExpired())
/* 442 */         .peek(choosen::set)
/* 443 */         .count() == 1L) {
/* 444 */         return () -> Optional.of(choosen.get());
/*     */       }
/*     */     } 
/* 447 */     return (Jvms.isWindows() ? new WindowsCertificateListDialog(defaultAlias, saveCallback, repository, repoWaiting) : new CertificateListDialog(defaultAlias, saveCallback))
/*     */ 
/*     */       
/* 450 */       .choose(entries);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/CertificateListDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
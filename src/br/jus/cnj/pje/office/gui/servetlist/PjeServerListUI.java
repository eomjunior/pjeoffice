/*     */ package br.jus.cnj.pje.office.gui.servetlist;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*     */ import br.jus.cnj.pje.office.gui.PjeImages;
/*     */ import com.github.utils4j.gui.imp.ButtonRenderer;
/*     */ import com.github.utils4j.gui.imp.SimpleDialog;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.swing.AbstractCellEditor;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
/*     */ import javax.swing.table.TableCellEditor;
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
/*     */ public class PjeServerListUI
/*     */   extends SimpleDialog
/*     */   implements IPjeServerListUI
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  74 */   private static final Dimension MININUM_SIZE = new Dimension(602, 307);
/*     */   
/*     */   private JTable table;
/*     */   
/*     */   private JPanel contentPane;
/*     */   
/*     */   private List<IPjeServerListUI.IServerEntry> loadedList;
/*     */   
/*  82 */   private List<IPjeServerListUI.IServerEntry> currentList = Collections.emptyList();
/*     */   
/*     */   public PjeServerListUI() {
/*  85 */     super("Sites autorizados", PjeConfig.getIcon(), true);
/*  86 */     setupLayout();
/*  87 */     setFixedMinimumSize(MININUM_SIZE);
/*  88 */     setDefaultCloseOperation(2);
/*  89 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e) {
/*  92 */             PjeServerListUI.this.cancelClick((ActionEvent)null);
/*     */           }
/*     */         });
/*  95 */     toCenter();
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/*  99 */     this.contentPane = new JPanel();
/* 100 */     this.contentPane.setBorder(new EtchedBorder(1, null, null));
/* 101 */     this.contentPane.setLayout(new BorderLayout(0, 0));
/* 102 */     this.contentPane.add(north(), "North");
/* 103 */     this.contentPane.add(center(), "Center");
/* 104 */     this.contentPane.add(south(), "South");
/* 105 */     setContentPane(this.contentPane);
/*     */   }
/*     */   
/*     */   private JPanel south() {
/* 109 */     JButton cancelButton = new JButton("Cancelar");
/* 110 */     cancelButton.addActionListener(this::cancelClick);
/*     */     
/* 112 */     JButton okButton = new JButton("OK");
/* 113 */     okButton.setPreferredSize(cancelButton.getPreferredSize());
/* 114 */     okButton.addActionListener(this::okClick);
/*     */     
/* 116 */     JPanel southPane = new JPanel();
/* 117 */     southPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][]", "[][]"));
/* 118 */     southPane.add(okButton);
/* 119 */     southPane.add(cancelButton);
/* 120 */     return southPane;
/*     */   }
/*     */   
/*     */   private JPanel center() {
/* 124 */     this.table = new JTable();
/* 125 */     this.table.setModel(new ServerModel());
/*     */     
/* 127 */     this.table.setDefaultEditor(IPjeServerListUI.Authorization.class, new AuthorizationEditor());
/* 128 */     this.table.setDefaultRenderer(IPjeServerListUI.Authorization.class, new AuthorizationEditor());
/* 129 */     this.table.getColumnModel().getColumn(0).setPreferredWidth(40);
/* 130 */     this.table.getColumnModel().getColumn(1).setPreferredWidth(190);
/* 131 */     this.table.getColumnModel().getColumn(2).setPreferredWidth(102);
/*     */     
/* 133 */     this.table.setSelectionMode(0);
/* 134 */     this.table.setFont(new Font("Tahoma", 0, 13));
/* 135 */     this.table.setFillsViewportHeight(true);
/* 136 */     this.table.setBorder((Border)null);
/* 137 */     this.table.setRowHeight(this.table.getRowHeight() + 10);
/*     */     
/* 139 */     ButtonRenderer bc = new ButtonRenderer(arg -> clickRemove(arg));
/* 140 */     this.table.getColumnModel().getColumn(3).setCellRenderer((TableCellRenderer)bc);
/* 141 */     this.table.getColumnModel().getColumn(3).setCellEditor((TableCellEditor)bc);
/*     */     
/* 143 */     TableCellRenderer renderer = this.table.getTableHeader().getDefaultRenderer();
/* 144 */     ((DefaultTableCellRenderer)renderer).setHorizontalAlignment(2);
/* 145 */     JScrollPane scrollPane = new JScrollPane(this.table);
/*     */     
/* 147 */     JPanel pnlCenter = new JPanel();
/* 148 */     pnlCenter.setLayout(new CardLayout(0, 0));
/* 149 */     pnlCenter.add(scrollPane);
/* 150 */     return pnlCenter;
/*     */   }
/*     */   
/*     */   private JPanel north() {
/* 154 */     JPanel pnlNorth = new JPanel();
/* 155 */     pnlNorth.setLayout(new BorderLayout(0, 0));
/* 156 */     JLabel lblServerList = new JLabel("Sites Disponíveis");
/* 157 */     lblServerList.setIcon(PjeImages.PJE_SERVER.asIcon());
/* 158 */     lblServerList.setHorizontalAlignment(2);
/* 159 */     lblServerList.setFont(new Font("Tahoma", 1, 15));
/* 160 */     pnlNorth.add(lblServerList, "North");
/* 161 */     return pnlNorth;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {
/* 166 */     cancelClick(e);
/*     */   }
/*     */   
/*     */   private void cancelClick(ActionEvent e) {
/* 170 */     this.currentList = this.loadedList;
/* 171 */     close();
/*     */   }
/*     */   
/*     */   private void okClick(ActionEvent e) {
/* 175 */     ServerModel model = (ServerModel)this.table.getModel();
/* 176 */     if (model.getRowCount() == 0) {
/* 177 */       this.currentList = Collections.emptyList();
/*     */     } else {
/* 179 */       this.currentList = model.getList();
/*     */     } 
/* 181 */     close();
/*     */   }
/*     */   
/*     */   private void clickRemove(ActionEvent arg) {
/* 185 */     ServerModel model = (ServerModel)this.table.getModel();
/* 186 */     int row = this.table.getSelectedRow();
/* 187 */     if (row >= 0) {
/* 188 */       model.remove(row);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IPjeServerListUI.IServerEntry> show(List<IPjeServerListUI.IServerEntry> entries) {
/* 194 */     Args.requireNonNull(entries, "entries is null");
/* 195 */     this.loadedList = clone(entries);
/* 196 */     ServerModel model = (ServerModel)this.table.getModel();
/* 197 */     model.load(entries);
/* 198 */     showToFront();
/* 199 */     return this.currentList;
/*     */   }
/*     */   
/*     */   private static List<IPjeServerListUI.IServerEntry> clone(List<IPjeServerListUI.IServerEntry> entries) {
/* 203 */     return (List<IPjeServerListUI.IServerEntry>)entries.stream().map(IPjeServerListUI.IServerEntry::clone).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   private static final class ServerModel
/*     */     extends AbstractTableModel {
/* 208 */     private static final String[] COLUMN_NAMES = new String[] { "Aplicação", "Endereço", "Autorizado", "Ação" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     private final List<IPjeServerListUI.IServerEntry> entries = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public final List<IPjeServerListUI.IServerEntry> getList() {
/* 224 */       return new ArrayList<>(this.entries);
/*     */     }
/*     */     
/*     */     public final void clear() {
/* 228 */       this.entries.clear();
/* 229 */       fireTableDataChanged();
/*     */     }
/*     */     
/*     */     public final void load(List<IPjeServerListUI.IServerEntry> entry) {
/* 233 */       clear();
/* 234 */       int i = 0;
/* 235 */       while (i < entry.size()) {
/* 236 */         this.entries.add(entry.get(i));
/* 237 */         fireTableRowsInserted(i, i);
/* 238 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     public final void remove(int index) {
/* 243 */       if (index < 0 || index >= this.entries.size())
/*     */         return; 
/* 245 */       this.entries.remove(index);
/* 246 */       fireTableRowsDeleted(index, index);
/*     */     }
/*     */     
/*     */     public final int getRowCount() {
/* 250 */       return this.entries.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getColumnName(int column) {
/* 255 */       return COLUMN_NAMES[column];
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getColumnClass(int columnIndex) {
/* 260 */       switch (columnIndex) {
/*     */         case 0:
/*     */         case 1:
/* 263 */           return String.class;
/*     */         case 2:
/* 265 */           return IPjeServerListUI.Authorization.class;
/*     */         case 3:
/* 267 */           return IPjeServerListUI.Action.class;
/*     */       } 
/* 269 */       return Object.class;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 275 */       return (columnIndex >= 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 280 */       return COLUMN_NAMES.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setValueAt(Object aValue, int rIndex, int cIndex) {
/* 285 */       IPjeServerListUI.IServerEntry entry = this.entries.get(rIndex);
/* 286 */       switch (cIndex) {
/*     */         case 2:
/* 288 */           entry.setAuthorization((IPjeServerListUI.Authorization)aValue);
/*     */           break;
/*     */       } 
/*     */       
/* 292 */       fireTableCellUpdated(rIndex, cIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int rIndex, int cIndex) {
/* 297 */       IPjeServerListUI.IServerEntry entry = this.entries.get(rIndex);
/* 298 */       switch (cIndex) {
/*     */         case 0:
/* 300 */           return entry.getApp();
/*     */         case 1:
/* 302 */           return entry.getServer();
/*     */         case 2:
/* 304 */           return entry.getAuthorization();
/*     */         case 3:
/* 306 */           return entry.getAction();
/*     */       } 
/* 308 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class AuthorizedPanel
/*     */     extends JPanel
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private JRadioButton yesOption;
/*     */     private JRadioButton noOption;
/* 319 */     private ButtonGroup buttonGroup = new ButtonGroup();
/*     */     
/*     */     AuthorizedPanel(ActionListener listener) {
/* 322 */       super(new GridLayout(1, 3));
/* 323 */       setOpaque(true);
/* 324 */       this.yesOption = createRadio(IPjeServerListUI.Authorization.SIM, listener);
/* 325 */       this.noOption = createRadio(IPjeServerListUI.Authorization.NÃO, listener);
/*     */     }
/*     */     
/*     */     private JRadioButton createRadio(IPjeServerListUI.Authorization status, ActionListener listener) {
/* 329 */       JRadioButton radioButton = new JRadioButton(status.name());
/* 330 */       radioButton.addActionListener(listener);
/* 331 */       radioButton.setOpaque(false);
/* 332 */       add(radioButton);
/* 333 */       this.buttonGroup.add(radioButton);
/* 334 */       return radioButton;
/*     */     }
/*     */     
/*     */     public IPjeServerListUI.Authorization getAuthorization() {
/* 338 */       return IPjeServerListUI.Authorization.from(this.yesOption.isSelected());
/*     */     }
/*     */     
/*     */     public void setAuthorization(IPjeServerListUI.Authorization status) {
/* 342 */       this.yesOption.setSelected(IPjeServerListUI.Authorization.SIM.equals(status));
/* 343 */       this.noOption.setSelected(IPjeServerListUI.Authorization.NÃO.equals(status));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AuthorizationEditor
/*     */     extends AbstractCellEditor
/*     */     implements TableCellRenderer, TableCellEditor, ActionListener
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 355 */     private final PjeServerListUI.AuthorizedPanel authorizationPanel = new PjeServerListUI.AuthorizedPanel(this);
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
/* 360 */       this.authorizationPanel.setAuthorization((IPjeServerListUI.Authorization)value);
/* 361 */       this.authorizationPanel.setBackground(isSelected ? table
/* 362 */           .getSelectionBackground() : table
/* 363 */           .getBackground());
/*     */       
/* 365 */       return this.authorizationPanel;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 370 */       stopCellEditing();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getCellEditorValue() {
/* 375 */       return this.authorizationPanel.getAuthorization();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 380 */       this.authorizationPanel.setAuthorization((IPjeServerListUI.Authorization)value);
/* 381 */       this.authorizationPanel.setBackground(isSelected ? table
/* 382 */           .getSelectionBackground() : table
/* 383 */           .getBackground());
/*     */       
/* 385 */       return this.authorizationPanel;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/servetlist/PjeServerListUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
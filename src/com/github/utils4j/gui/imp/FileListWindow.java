/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.Dates;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import com.github.utils4j.imp.Media;
/*     */ import com.github.utils4j.imp.Pair;
/*     */ import com.github.utils4j.imp.Sizes;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
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
/*     */ public class FileListWindow
/*     */   extends SimpleDialog
/*     */   implements IFileListView
/*     */ {
/*     */   private static final int MIN_WIDTH = 600;
/*  70 */   private static final int MIN_HEIGHT = Jvms.isWindows() ? 340 : 360;
/*     */   
/*     */   private JTable table;
/*     */   
/*     */   private File outputFile;
/*     */   private File currentDir;
/*     */   private Media media;
/*     */   private FileTableModel tableModel;
/*  78 */   private final DelayedFileChooser chooser = DelayedFileChooser.DIALOG;
/*     */   private JButton saveAtButton; private int[] selectedRows;
/*     */   
/*  81 */   public FileListWindow(Image icon, List<File> files, Media media) { this(icon, files, media, (File)null); }
/*     */   private void createSouth() { this.saveAtButton = new JButton("Salvar..."); this.saveAtButton.addActionListener(this::onSave); JButton cancelButton = new JButton("Cancelar"); cancelButton.addActionListener(this::onEscPressed); JPanel actionPanel = new JPanel(); actionPanel.setLayout((LayoutManager)new MigLayout("fillx", "push[][]", "[][]")); actionPanel.add(this.saveAtButton); actionPanel.add(cancelButton); JPanel southPanel = new JPanel();
/*     */     southPanel.setLayout(new BorderLayout());
/*     */     southPanel.add(actionPanel, "East");
/*  85 */     add(southPanel, "South"); } public FileListWindow(Image icon, List<File> files, Media media, File currentDir) { super("Ordem dos arquivos", icon, true);
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
/*     */ 
/*     */     
/* 158 */     this.selectedRows = new int[0]; Args.requireNonNull(files, "files is empty"); Args.requireNonNull(media, "media is null"); this.media = media; this.currentDir = currentDir; setLayout(new BorderLayout()); createCenter(files); createEast(); createSouth(); setSize(new Dimension(600, MIN_HEIGHT)); setFixedMinimumSize(new Dimension(600, MIN_HEIGHT)); toCenter(); setAutoRequestFocus(true); setAlwaysOnTop(true);
/*     */     setDefaultCloseOperation(0);
/*     */     addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent windowEvent) { FileListWindow.this.onEscPressed((ActionEvent)null); } }
/* 161 */       ); } private void createCenter(List<File> files) { this.tableModel = new FileTableModel(files);
/* 162 */     this.table = new JTable(this.tableModel);
/* 163 */     this.table.setAutoCreateRowSorter(false);
/* 164 */     this.table.setFillsViewportHeight(true);
/* 165 */     this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());
/* 166 */     this.table.getColumnModel().getColumn(0).setPreferredWidth(250);
/* 167 */     this.table.getColumnModel().getColumn(1).setCellRenderer(new DateTableCellRenderer());
/* 168 */     this.table.getColumnModel().getColumn(2).setCellRenderer(new SizeTableCellRenderer());
/* 169 */     this.table.getColumnModel().getColumn(2).setPreferredWidth(50);
/* 170 */     this.table.setSelectionMode(1);
/* 171 */     this.table.getSelectionModel().addListSelectionListener(e -> {
/*     */           if (!e.getValueIsAdjusting()) {
/*     */             this.selectedRows = this.table.getSelectedRows();
/*     */           }
/*     */         });
/* 176 */     JPanel panel = new JPanel();
/* 177 */     panel.setLayout((LayoutManager)new MigLayout());
/* 178 */     panel.add(new JScrollPane(this.table), "pushx, pushy, growy, growx");
/* 179 */     add(panel, "Center"); } private void createEast() { JButton firstButton = new JButton(Images.FIRST.asIcon()); firstButton.addActionListener(this::onFirst); firstButton.setToolTipText("Mover para a primeira posição."); JButton upButton = new JButton(Images.UP.asIcon()); upButton.setToolTipText("Mover para uma posição acima."); upButton.addActionListener(this::onUp); JButton downButton = new JButton(Images.DOWN.asIcon()); downButton.setToolTipText("Mover para uma posição abaixo."); downButton.addActionListener(this::onDown); JButton lastButton = new JButton(Images.LAST.asIcon()); lastButton.addActionListener(this::onLast); lastButton.setToolTipText("Mover para a última posição."); JButton addButton = new JButton(Images.ADD.asIcon()); addButton.addActionListener(this::onAdd); addButton.setToolTipText("Adicionar um novo arquivo à lista."); JButton remButton = new JButton(Images.REM.asIcon()); remButton.setToolTipText("Remover arquivo(s) selecionado(s) da lista."); remButton.addActionListener(this::onRem); JPanel eastPane = new JPanel(); eastPane.setLayout((LayoutManager)new MigLayout()); eastPane.add(firstButton, "wrap"); eastPane.add(upButton, "wrap"); eastPane.add(downButton, "wrap"); eastPane.add(lastButton, "wrap");
/*     */     eastPane.add(new JPanel(), "wrap");
/*     */     eastPane.add(addButton, "wrap");
/*     */     eastPane.add(remButton, "wrap");
/*     */     add(eastPane, "East"); }
/* 184 */   protected void onEscPressed(ActionEvent e) { setAlwaysOnTop(false);
/* 185 */     Dialogs.Choice choice = Dialogs.getBoolean("Deseja mesmo cancelar a operação?", "Cancelamento da operação", false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     if (choice == Dialogs.Choice.YES) {
/* 191 */       this.outputFile = null;
/* 192 */       close();
/*     */     }  }
/*     */ 
/*     */   
/*     */   private void onSave(ActionEvent e) {
/* 197 */     this.chooser.filesOnly().select("onde será salvo o arquivo final", this.media, this.currentDir)
/* 198 */       .map(Directory::stringPath)
/* 199 */       .map(s -> s.toLowerCase().endsWith(this.media.getExtension(true)) ? s : (s + this.media.getExtension(true)))
/* 200 */       .map(s -> new File(s))
/* 201 */       .ifPresent(f -> {
/*     */           this.outputFile = f;
/*     */           close();
/*     */         });
/*     */   }
/*     */   
/*     */   private void scrollToVisible(int rowIndex, int vColIndex) {
/* 208 */     this.table.scrollRectToVisible(new Rectangle(this.table.getCellRect(rowIndex, 0, true)));
/*     */   }
/*     */   
/*     */   private void onUp(ActionEvent e) {
/* 212 */     if (this.selectedRows.length > 0) {
/* 213 */       Pair<Integer, Integer> p = this.tableModel.sortUp(this.selectedRows);
/* 214 */       this.table.setRowSelectionInterval(((Integer)p.getKey()).intValue(), ((Integer)p.getValue()).intValue());
/* 215 */       scrollToVisible(((Integer)p.getKey()).intValue(), 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onDown(ActionEvent e) {
/* 220 */     if (this.selectedRows.length > 0) {
/* 221 */       Pair<Integer, Integer> p = this.tableModel.sortDown(this.selectedRows);
/* 222 */       this.table.setRowSelectionInterval(((Integer)p.getKey()).intValue(), ((Integer)p.getValue()).intValue());
/* 223 */       scrollToVisible(((Integer)p.getValue()).intValue(), 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onFirst(ActionEvent e) {
/* 228 */     while (this.selectedRows.length > 0 && this.selectedRows[0] != 0) {
/* 229 */       onUp(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onLast(ActionEvent e) {
/* 234 */     while (this.selectedRows.length > 0 && this.selectedRows[this.selectedRows.length - 1] != this.table.getRowCount() - 1) {
/* 235 */       onDown(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onAdd(ActionEvent e) {
/* 240 */     this.chooser.filesOnly().multiSelect("adição de novos arquivos", Media.PDF, this.currentDir).ifPresent(f -> Stream.<File>of(f).forEach(this.tableModel::add));
/* 241 */     this.saveAtButton.setEnabled((this.tableModel.getRowCount() > 0));
/*     */   }
/*     */   
/*     */   private void onRem(ActionEvent e) {
/* 245 */     if (this.selectedRows.length > 0)
/* 246 */       this.tableModel.delete(this.selectedRows); 
/* 247 */     this.saveAtButton.setEnabled((this.tableModel.getRowCount() > 0));
/*     */   }
/*     */   
/*     */   private static class SizeTableCellRenderer extends DefaultTableCellRenderer {
/*     */     public SizeTableCellRenderer() {
/* 252 */       setHorizontalAlignment(0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 258 */       if (value instanceof Long) {
/* 259 */         value = Sizes.defaultFormat(((Long)value).longValue());
/*     */       }
/* 261 */       return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTableCellRenderer extends DefaultTableCellRenderer {
/*     */     public DateTableCellRenderer() {
/* 267 */       setHorizontalAlignment(0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 273 */       if (value instanceof Date) {
/* 274 */         value = Dates.defaultFormat((Date)value);
/*     */       }
/* 276 */       return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<File> getOutputFile() {
/* 282 */     showToFront();
/* 283 */     return Optional.ofNullable(this.outputFile);
/*     */   }
/*     */   
/*     */   private static List<File> createListFiles() {
/* 287 */     return 
/* 288 */       Containers.arrayList((Object[])(new File("D:\\temp\\pdfs")).listFiles(new FilenameFilter()
/*     */           {
/*     */             public boolean accept(File dir, String name) {
/* 291 */               return name.endsWith(".pdf");
/*     */             }
/*     */           }));
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 297 */     SwingTools.invokeLater(() -> {
/*     */           List<File> files = createListFiles();
/*     */           IFileListView window = new FileListWindow(Images.FIRST.asImage(), files, Media.PDF, null);
/*     */           File fileName = window.getOutputFile().orElse(null);
/*     */           System.out.println(fileName);
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/FileListWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
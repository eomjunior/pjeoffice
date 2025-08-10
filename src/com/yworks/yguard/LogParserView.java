/*     */ package com.yworks.yguard;
/*     */ 
/*     */ import com.yworks.util.Version;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.io.File;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import javax.swing.tree.TreeNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LogParserView
/*     */ {
/*     */   void show(File initialPath) {
/*  64 */     final JTree tree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode()));
/*  65 */     tree.setCellRenderer(new TreeCellRenderer() {
/*  66 */           DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer();
/*     */           public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/*  68 */             JComponent c = (JComponent)this.dtcr.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
/*  69 */             DefaultMutableTreeNode dmtr = (DefaultMutableTreeNode)value;
/*  70 */             if (dmtr.getUserObject() != null) {
/*  71 */               this.dtcr.setIcon(((YGuardLogParser.Mapped)dmtr.getUserObject()).getIcon());
/*     */             }
/*  73 */             return c;
/*     */           }
/*     */         });
/*  76 */     tree.setRootVisible(false);
/*  77 */     tree.setShowsRootHandles(true);
/*     */ 
/*     */     
/*  80 */     JPanel textPanel = new JPanel(new BorderLayout());
/*  81 */     final JTextArea textArea = new JTextArea();
/*  82 */     textArea.setMinimumSize(new Dimension(600, 200));
/*  83 */     JScrollPane textScrollPane = new JScrollPane(textArea);
/*  84 */     textScrollPane.getViewport().setPreferredSize(new Dimension(400, 200));
/*  85 */     textPanel.add(textScrollPane, "Center");
/*  86 */     JButton button = new JButton("Deobfuscate!");
/*  87 */     button.setMnemonic('D');
/*  88 */     textPanel.add(button, "South");
/*  89 */     button.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  91 */             LogParserView.deobfuscate(LogParserView.getParser(tree), textArea);
/*     */           }
/*     */         });
/*     */     
/*  95 */     final JPanel top = new JPanel(new BorderLayout());
/*  96 */     top.add(new JScrollPane(tree), "Center");
/*  97 */     JPanel buttonPanel = new JPanel(new FlowLayout(3, 0, 0));
/*  98 */     buttonPanel.add(new JButton(new AbstractAction("Sort by Mapping") {
/*     */             public void actionPerformed(ActionEvent e) {
/* 100 */               DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
/* 101 */               LogParserView.sort(model, new LogParserView.MappedNameComparator());
/*     */             }
/*     */           }));
/* 104 */     buttonPanel.add(new JButton(new AbstractAction("Sort by Names") {
/*     */             public void actionPerformed(ActionEvent e) {
/* 106 */               DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
/* 107 */               LogParserView.sort(model, new LogParserView.NameComparator());
/*     */             }
/*     */           }));
/* 110 */     top.add(buttonPanel, "North");
/*     */ 
/*     */     
/* 113 */     final JFrame frame = new JFrame(newTitle(initialPath.getAbsolutePath()));
/*     */     
/* 115 */     JMenu recent = new JMenu("Open Recent");
/*     */     
/* 117 */     JFileChooser jfc = new JFileChooser();
/* 118 */     jfc.addChoosableFileFilter(new FileFilterImpl(".gz", "Compressed XML (*.gz)"));
/* 119 */     jfc.addChoosableFileFilter(new FileFilterImpl(".xml", "XML (*.xml)"));
/* 120 */     jfc.setAcceptAllFileFilterUsed(true);
/* 121 */     jfc.setFileFilter(jfc.getAcceptAllFileFilter());
/*     */     
/* 123 */     File parent = initialPath.getParentFile();
/* 124 */     if (parent != null) {
/* 125 */       jfc.setCurrentDirectory(parent);
/*     */     }
/*     */     
/* 128 */     UiContext ctx = new UiContext(frame, tree, textArea, recent, jfc);
/*     */     
/*     */     try {
/* 131 */       setParser(tree, newParser(initialPath));
/* 132 */       addRecent(ctx, initialPath);
/* 133 */     } catch (Exception ex) {
/* 134 */       setParser(tree, new YGuardLogParser());
/*     */       
/* 136 */       frame.setTitle("Element Mapping - yGuard " + Version.getVersion());
/*     */       
/* 138 */       final String msg = toErrorMessage(initialPath, ex);
/* 139 */       frame.addComponentListener(new ComponentAdapter() {
/*     */             public void componentShown(ComponentEvent e) {
/* 141 */               frame.removeComponentListener(this);
/* 142 */               EventQueue.invokeLater(new Runnable() {
/*     */                     public void run() {
/* 144 */                       LogParserView.showErrorMessage(msg, tree);
/*     */                     }
/*     */                   });
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 151 */     JMenu file = new JMenu("File");
/* 152 */     file.add(new AbstractOpenAction(ctx, "Open") {
/*     */           public void actionPerformed(ActionEvent e) {
/* 154 */             JFileChooser jfc = this.context.fileChooser;
/* 155 */             if (jfc.showOpenDialog(top) == 0) {
/* 156 */               open(jfc.getSelectedFile());
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           void onOpened(LogParserView.UiContext context, File path) {
/* 162 */             LogParserView.addRecent(context, path);
/* 163 */             super.onOpened(context, path);
/*     */           }
/*     */         });
/* 166 */     file.add(recent);
/* 167 */     file.addSeparator();
/* 168 */     file.add(new AbstractAction("Quit") {
/*     */           public void actionPerformed(ActionEvent e) {
/* 170 */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/* 174 */     JMenu help = new JMenu("?");
/* 175 */     help.add(new AbstractAction("About") {
/*     */           public void actionPerformed(ActionEvent e) {
/* 177 */             JLabel jl = new JLabel("Element Mapping - yGuard " + Version.getVersion());
/* 178 */             JOptionPane.showMessageDialog(top, jl, "About", -1);
/*     */           }
/*     */         });
/* 181 */     JMenuBar jmb = new JMenuBar();
/* 182 */     jmb.add(file);
/* 183 */     jmb.add(help);
/*     */ 
/*     */     
/* 186 */     frame.setDefaultCloseOperation(3);
/* 187 */     frame.setJMenuBar(jmb);
/* 188 */     frame.setContentPane(new JSplitPane(0, top, textPanel));
/* 189 */     frame.pack();
/* 190 */     frame.setLocationRelativeTo((Component)null);
/* 191 */     frame.setVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void addRecent(UiContext context, File path) {
/* 201 */     JMenu menu = context.recentMenu;
/* 202 */     int n = menu.getItemCount();
/* 203 */     if (n > 9) {
/* 204 */       menu.remove(n - 1);
/*     */     }
/*     */     
/* 207 */     RecentAction ra = new RecentAction(context, path);
/* 208 */     JMenuItem jc = menu.add(ra);
/* 209 */     ra.setItem(jc);
/* 210 */     if (n > 0) {
/* 211 */       menu.remove(menu.getItemCount() - 1);
/* 212 */       menu.add(jc, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void showErrorMessage(String text, JComponent parent) {
/* 223 */     JTextArea jta = new JTextArea(text);
/* 224 */     jta.setEditable(false);
/* 225 */     JScrollPane jsp = new JScrollPane(jta);
/* 226 */     jsp.setPreferredSize(new Dimension(400, 600));
/* 227 */     JOptionPane.showMessageDialog(parent, jsp, "Error", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toErrorMessage(File file, Exception ex) {
/* 238 */     StringWriter sw = new StringWriter();
/* 239 */     sw.write("Could not read ");
/* 240 */     sw.write(file.getAbsolutePath());
/* 241 */     sw.write(":\n");
/* 242 */     ex.printStackTrace(new PrintWriter(sw));
/* 243 */     return sw.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static YGuardLogParser getParser(JTree tree) {
/* 253 */     return (YGuardLogParser)tree.getClientProperty("PARSER");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setParser(JTree tree, YGuardLogParser parser) {
/* 263 */     tree.setModel(parser.getTreeModel());
/* 264 */     tree.putClientProperty("PARSER", parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static YGuardLogParser newParser(File file) throws Exception {
/* 275 */     YGuardLogParser parser = new YGuardLogParser();
/* 276 */     parser.parse(file);
/* 277 */     return parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String newTitle(String path) {
/* 287 */     String t = (path == null) ? "" : path.trim();
/* 288 */     if (t.length() > 31) {
/* 289 */       return newTitleSuffix(t);
/*     */     }
/* 291 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String newTitleSuffix(String path) {
/* 302 */     int idx = path.lastIndexOf(File.separatorChar);
/* 303 */     if (idx > -1) {
/* 304 */       idx = path.lastIndexOf(File.separatorChar, idx - 1);
/*     */     }
/* 306 */     if (idx > -1) {
/* 307 */       return "..." + path.substring(idx);
/*     */     }
/* 309 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void deobfuscate(YGuardLogParser parser, JTextArea textArea) {
/* 322 */     String[] lines = textArea.getText().split("\n");
/* 323 */     lines = parser.translate(lines);
/* 324 */     StringBuffer sb = new StringBuffer();
/* 325 */     for (int i = 0; i < lines.length; i++) {
/* 326 */       sb.append(lines[i]).append("\n");
/*     */     }
/* 328 */     textArea.setText(sb.toString());
/* 329 */     textArea.setCaretPosition(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void sort(DefaultTreeModel model, Comparator c) {
/* 339 */     sortRecursively((DefaultMutableTreeNode)model.getRoot(), c);
/* 340 */     model.nodeStructureChanged((DefaultMutableTreeNode)model.getRoot());
/*     */   }
/*     */   
/*     */   private static void sortRecursively(DefaultMutableTreeNode parent, Comparator c) {
/* 344 */     if (parent.getChildCount() > 0) {
/* 345 */       if (parent.getChildCount() > 1) {
/* 346 */         sort(parent, c);
/*     */       }
/* 348 */       for (Enumeration<TreeNode> enu = parent.children(); enu.hasMoreElements(); ) {
/* 349 */         DefaultMutableTreeNode tn = (DefaultMutableTreeNode)enu.nextElement();
/* 350 */         sortRecursively(tn, c);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void sort(DefaultMutableTreeNode parent, Comparator<? super DefaultMutableTreeNode> c) {
/* 356 */     DefaultMutableTreeNode[] children = new DefaultMutableTreeNode[parent.getChildCount()]; int i;
/* 357 */     for (i = 0; i < children.length; i++) {
/* 358 */       children[i] = (DefaultMutableTreeNode)parent.getChildAt(i);
/*     */     }
/* 360 */     parent.removeAllChildren();
/* 361 */     Arrays.sort(children, c);
/* 362 */     for (i = 0; i < children.length; i++) {
/* 363 */       parent.add(children[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class MappedNameComparator
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object o1, Object o2) {
/* 373 */       YGuardLogParser.Mapped m1 = (YGuardLogParser.Mapped)((DefaultMutableTreeNode)o1).getUserObject();
/* 374 */       YGuardLogParser.Mapped m2 = (YGuardLogParser.Mapped)((DefaultMutableTreeNode)o2).getUserObject();
/* 375 */       if (m1.getClass() != m2.getClass()) {
/* 376 */         if (m1.getClass() == YGuardLogParser.PackageStruct.class)
/* 377 */           return -1; 
/* 378 */         if (m2.getClass() == YGuardLogParser.PackageStruct.class) {
/* 379 */           return 1;
/*     */         }
/* 381 */         if (m1.getClass() == YGuardLogParser.ClassStruct.class)
/* 382 */           return -1; 
/* 383 */         if (m2.getClass() == YGuardLogParser.ClassStruct.class) {
/* 384 */           return 1;
/*     */         }
/* 386 */         if (m1.getClass() == YGuardLogParser.MethodStruct.class)
/* 387 */           return -1; 
/* 388 */         if (m2.getClass() == YGuardLogParser.MethodStruct.class) {
/* 389 */           return 1;
/*     */         }
/*     */       } 
/* 392 */       return m1.getMappedName().compareTo(m2.getMappedName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class NameComparator
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object o1, Object o2) {
/* 402 */       YGuardLogParser.Mapped m1 = (YGuardLogParser.Mapped)((DefaultMutableTreeNode)o1).getUserObject();
/* 403 */       YGuardLogParser.Mapped m2 = (YGuardLogParser.Mapped)((DefaultMutableTreeNode)o2).getUserObject();
/* 404 */       if (m1.getClass() != m2.getClass()) {
/* 405 */         if (m1.getClass() == YGuardLogParser.PackageStruct.class)
/* 406 */           return -1; 
/* 407 */         if (m2.getClass() == YGuardLogParser.PackageStruct.class) {
/* 408 */           return 1;
/*     */         }
/* 410 */         if (m1.getClass() == YGuardLogParser.ClassStruct.class)
/* 411 */           return -1; 
/* 412 */         if (m2.getClass() == YGuardLogParser.ClassStruct.class) {
/* 413 */           return 1;
/*     */         }
/* 415 */         if (m1.getClass() == YGuardLogParser.MethodStruct.class)
/* 416 */           return -1; 
/* 417 */         if (m2.getClass() == YGuardLogParser.MethodStruct.class) {
/* 418 */           return 1;
/*     */         }
/*     */       } 
/* 421 */       return m1.getName().compareTo(m2.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FileFilterImpl
/*     */     extends FileFilter
/*     */   {
/*     */     private final String suffix;
/*     */     
/*     */     private final String description;
/*     */ 
/*     */     
/*     */     FileFilterImpl(String suffix, String description) {
/* 436 */       this.suffix = (suffix == null) ? "" : suffix.toLowerCase();
/* 437 */       this.description = description;
/*     */     }
/*     */     
/*     */     public boolean accept(File f) {
/* 441 */       return (f.isDirectory() || f.getName().toLowerCase().endsWith(this.suffix));
/*     */     }
/*     */     
/*     */     public String getDescription() {
/* 445 */       return this.description;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class UiContext
/*     */   {
/*     */     final JFrame frame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final JTree mappingTree;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final JTextArea textArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final JMenu recentMenu;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final JFileChooser fileChooser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     UiContext(JFrame frame, JTree mappingTree, JTextArea textArea, JMenu recentMenu, JFileChooser fileChooser) {
/* 488 */       this.frame = frame;
/* 489 */       this.mappingTree = mappingTree;
/* 490 */       this.textArea = textArea;
/* 491 */       this.recentMenu = recentMenu;
/* 492 */       this.fileChooser = fileChooser;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class AbstractOpenAction
/*     */     extends AbstractAction
/*     */   {
/*     */     final LogParserView.UiContext context;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AbstractOpenAction(LogParserView.UiContext context, String name) {
/* 509 */       super(name);
/* 510 */       this.context = context;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void open(File path) {
/* 519 */       JTree tree = this.context.mappingTree;
/*     */       try {
/* 521 */         LogParserView.setParser(tree, LogParserView.newParser(path));
/* 522 */         onOpened(this.context, path);
/* 523 */       } catch (Exception ex) {
/* 524 */         LogParserView.showErrorMessage(LogParserView.toErrorMessage(path, ex), tree);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void onOpened(LogParserView.UiContext context, File path) {
/* 535 */       context.textArea.setText("");
/* 536 */       context.frame.setTitle(LogParserView.newTitle(path.getAbsolutePath()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RecentAction
/*     */     extends AbstractOpenAction
/*     */   {
/*     */     final File path;
/*     */ 
/*     */ 
/*     */     
/*     */     JMenuItem item;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RecentAction(LogParserView.UiContext context, File path) {
/* 557 */       super(context, LogParserView.newTitleSuffix(path.getAbsolutePath()));
/* 558 */       this.path = path;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 563 */       open(this.path);
/*     */     }
/*     */ 
/*     */     
/*     */     void onOpened(LogParserView.UiContext context, File path) {
/* 568 */       updateRecent(context, path);
/* 569 */       updateFileChooser(context, path);
/* 570 */       super.onOpened(context, path);
/*     */     }
/*     */     
/*     */     private void updateRecent(LogParserView.UiContext context, File path) {
/* 574 */       JMenu menu = context.recentMenu;
/* 575 */       JMenuItem ami = getItem();
/* 576 */       if (ami != null) {
/* 577 */         for (int i = 0, n = menu.getItemCount(); i < n; i++) {
/* 578 */           JMenuItem mi = menu.getItem(i);
/* 579 */           if (mi == ami) {
/* 580 */             menu.remove(i);
/* 581 */             menu.add(ami, 0);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     private void updateFileChooser(LogParserView.UiContext context, File path) {
/* 589 */       File parent = path.getParentFile();
/* 590 */       if (parent != null) {
/* 591 */         context.fileChooser.setCurrentDirectory(parent);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     JMenuItem getItem() {
/* 601 */       return this.item;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setItem(JMenuItem jc) {
/* 610 */       this.item = jc;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/LogParserView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
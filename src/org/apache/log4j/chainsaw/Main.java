/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTable;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.PropertyConfigurator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */   extends JFrame
/*     */ {
/*     */   private static final int DEFAULT_PORT = 4445;
/*     */   public static final String PORT_PROP_NAME = "chainsaw.port";
/*  52 */   private static final Logger LOG = Logger.getLogger(Main.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Main() {
/*  58 */     super("CHAINSAW - Log4J Log Viewer");
/*     */     
/*  60 */     MyTableModel model = new MyTableModel();
/*     */ 
/*     */     
/*  63 */     JMenuBar menuBar = new JMenuBar();
/*  64 */     setJMenuBar(menuBar);
/*  65 */     JMenu menu = new JMenu("File");
/*  66 */     menuBar.add(menu);
/*     */     
/*     */     try {
/*  69 */       LoadXMLAction lxa = new LoadXMLAction(this, model);
/*  70 */       JMenuItem loadMenuItem = new JMenuItem("Load file...");
/*  71 */       menu.add(loadMenuItem);
/*  72 */       loadMenuItem.addActionListener(lxa);
/*  73 */     } catch (NoClassDefFoundError e) {
/*  74 */       LOG.info("Missing classes for XML parser", e);
/*  75 */       JOptionPane.showMessageDialog(this, "XML parser not in classpath - unable to load XML events.", "CHAINSAW", 0);
/*     */     }
/*  77 */     catch (Exception e) {
/*  78 */       LOG.info("Unable to create the action to load XML files", e);
/*  79 */       JOptionPane.showMessageDialog(this, "Unable to create a XML parser - unable to load XML events.", "CHAINSAW", 0);
/*     */     } 
/*     */ 
/*     */     
/*  83 */     JMenuItem exitMenuItem = new JMenuItem("Exit");
/*  84 */     menu.add(exitMenuItem);
/*  85 */     exitMenuItem.addActionListener(ExitAction.INSTANCE);
/*     */ 
/*     */     
/*  88 */     ControlPanel cp = new ControlPanel(model);
/*  89 */     getContentPane().add(cp, "North");
/*     */ 
/*     */     
/*  92 */     JTable table = new JTable(model);
/*  93 */     table.setSelectionMode(0);
/*  94 */     JScrollPane scrollPane = new JScrollPane(table);
/*  95 */     scrollPane.setBorder(BorderFactory.createTitledBorder("Events: "));
/*  96 */     scrollPane.setPreferredSize(new Dimension(900, 300));
/*     */ 
/*     */     
/*  99 */     JPanel details = new DetailPanel(table, model);
/* 100 */     details.setPreferredSize(new Dimension(900, 300));
/*     */ 
/*     */     
/* 103 */     JSplitPane jsp = new JSplitPane(0, scrollPane, details);
/* 104 */     getContentPane().add(jsp, "Center");
/*     */     
/* 106 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent aEvent) {
/* 108 */             ExitAction.INSTANCE.actionPerformed(null);
/*     */           }
/*     */         });
/*     */     
/* 112 */     pack();
/* 113 */     setVisible(true);
/*     */     
/* 115 */     setupReceiver(model);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupReceiver(MyTableModel aModel) {
/* 124 */     int port = 4445;
/* 125 */     String strRep = System.getProperty("chainsaw.port");
/* 126 */     if (strRep != null) {
/*     */       try {
/* 128 */         port = Integer.parseInt(strRep);
/* 129 */       } catch (NumberFormatException nfe) {
/* 130 */         LOG.fatal("Unable to parse chainsaw.port property with value " + strRep + ".");
/* 131 */         JOptionPane.showMessageDialog(this, "Unable to parse port number from '" + strRep + "', quitting.", "CHAINSAW", 0);
/*     */         
/* 133 */         System.exit(1);
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 138 */       LoggingReceiver lr = new LoggingReceiver(aModel, port);
/* 139 */       lr.start();
/* 140 */     } catch (IOException e) {
/* 141 */       LOG.fatal("Unable to connect to socket server, quiting", e);
/* 142 */       JOptionPane.showMessageDialog(this, "Unable to create socket on port " + port + ", quitting.", "CHAINSAW", 0);
/*     */       
/* 144 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initLog4J() {
/* 154 */     Properties props = new Properties();
/* 155 */     props.setProperty("log4j.rootLogger", "DEBUG, A1");
/* 156 */     props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
/* 157 */     props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.TTCCLayout");
/* 158 */     PropertyConfigurator.configure(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] aArgs) {
/* 167 */     initLog4J();
/* 168 */     new Main();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/Main.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
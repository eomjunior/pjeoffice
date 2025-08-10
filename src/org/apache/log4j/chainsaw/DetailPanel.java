/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Date;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DetailPanel
/*     */   extends JPanel
/*     */   implements ListSelectionListener
/*     */ {
/*  39 */   private static final Logger LOG = Logger.getLogger(DetailPanel.class);
/*     */ 
/*     */   
/*  42 */   private static final MessageFormat FORMATTER = new MessageFormat("<b>Time:</b> <code>{0,time,medium}</code>&nbsp;&nbsp;<b>Priority:</b> <code>{1}</code>&nbsp;&nbsp;<b>Thread:</b> <code>{2}</code>&nbsp;&nbsp;<b>NDC:</b> <code>{3}</code><br><b>Logger:</b> <code>{4}</code><br><b>Location:</b> <code>{5}</code><br><b>Message:</b><pre>{6}</pre><b>Throwable:</b><pre>{7}</pre>");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final MyTableModel mModel;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final JEditorPane mDetails;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DetailPanel(JTable aTable, MyTableModel aModel) {
/*  60 */     this.mModel = aModel;
/*  61 */     setLayout(new BorderLayout());
/*  62 */     setBorder(BorderFactory.createTitledBorder("Details: "));
/*     */     
/*  64 */     this.mDetails = new JEditorPane();
/*  65 */     this.mDetails.setEditable(false);
/*  66 */     this.mDetails.setContentType("text/html");
/*  67 */     add(new JScrollPane(this.mDetails), "Center");
/*     */     
/*  69 */     ListSelectionModel rowSM = aTable.getSelectionModel();
/*  70 */     rowSM.addListSelectionListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void valueChanged(ListSelectionEvent aEvent) {
/*  76 */     if (aEvent.getValueIsAdjusting()) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     ListSelectionModel lsm = (ListSelectionModel)aEvent.getSource();
/*  81 */     if (lsm.isSelectionEmpty()) {
/*  82 */       this.mDetails.setText("Nothing selected");
/*     */     } else {
/*  84 */       int selectedRow = lsm.getMinSelectionIndex();
/*  85 */       EventDetails e = this.mModel.getEventDetails(selectedRow);
/*     */ 
/*     */       
/*  88 */       Object[] args = { new Date(e.getTimeStamp()), e.getPriority(), escape(e.getThreadName()), escape(e.getNDC()), escape(e.getCategoryName()), escape(e.getLocationDetails()), escape(e.getMessage()), escape(getThrowableStrRep(e)) };
/*  89 */       this.mDetails.setText(FORMATTER.format(args));
/*  90 */       this.mDetails.setCaretPosition(0);
/*     */     } 
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
/*     */   
/*     */   private static String getThrowableStrRep(EventDetails aEvent) {
/* 105 */     String[] strs = aEvent.getThrowableStrRep();
/* 106 */     if (strs == null) {
/* 107 */       return null;
/*     */     }
/*     */     
/* 110 */     StringBuilder sb = new StringBuilder();
/* 111 */     for (int i = 0; i < strs.length; i++) {
/* 112 */       sb.append(strs[i]).append("\n");
/*     */     }
/*     */     
/* 115 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String escape(String aStr) {
/* 126 */     if (aStr == null) {
/* 127 */       return null;
/*     */     }
/*     */     
/* 130 */     StringBuilder buf = new StringBuilder();
/* 131 */     for (int i = 0; i < aStr.length(); i++) {
/* 132 */       char c = aStr.charAt(i);
/* 133 */       switch (c) {
/*     */         case '<':
/* 135 */           buf.append("&lt;");
/*     */           break;
/*     */         case '>':
/* 138 */           buf.append("&gt;");
/*     */           break;
/*     */         case '"':
/* 141 */           buf.append("&quot;");
/*     */           break;
/*     */         case '&':
/* 144 */           buf.append("&amp;");
/*     */           break;
/*     */         default:
/* 147 */           buf.append(c);
/*     */           break;
/*     */       } 
/*     */     } 
/* 151 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/DetailPanel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MyTableModel
/*     */   extends AbstractTableModel
/*     */ {
/*  40 */   private static final Logger LOG = Logger.getLogger(MyTableModel.class);
/*     */ 
/*     */   
/*  43 */   private static final Comparator MY_COMP = new Comparator()
/*     */     {
/*     */       public int compare(Object aObj1, Object aObj2) {
/*  46 */         if (aObj1 == null && aObj2 == null)
/*  47 */           return 0; 
/*  48 */         if (aObj1 == null)
/*  49 */           return -1; 
/*  50 */         if (aObj2 == null) {
/*  51 */           return 1;
/*     */         }
/*     */ 
/*     */         
/*  55 */         EventDetails le1 = (EventDetails)aObj1;
/*  56 */         EventDetails le2 = (EventDetails)aObj2;
/*     */         
/*  58 */         if (le1.getTimeStamp() < le2.getTimeStamp()) {
/*  59 */           return 1;
/*     */         }
/*     */         
/*  62 */         return -1;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private class Processor
/*     */     implements Runnable
/*     */   {
/*     */     private Processor() {}
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/*     */         try {
/*  76 */           Thread.sleep(1000L);
/*  77 */         } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */         
/*  81 */         synchronized (MyTableModel.this.mLock) {
/*  82 */           if (MyTableModel.this.mPaused) {
/*     */             continue;
/*     */           }
/*     */           
/*  86 */           boolean toHead = true;
/*  87 */           boolean needUpdate = false;
/*  88 */           Iterator<EventDetails> it = MyTableModel.this.mPendingEvents.iterator();
/*  89 */           while (it.hasNext()) {
/*  90 */             EventDetails event = it.next();
/*  91 */             MyTableModel.this.mAllEvents.add(event);
/*  92 */             toHead = (toHead && event == MyTableModel.this.mAllEvents.first());
/*  93 */             needUpdate = (needUpdate || MyTableModel.this.matchFilter(event));
/*     */           } 
/*  95 */           MyTableModel.this.mPendingEvents.clear();
/*     */           
/*  97 */           if (needUpdate) {
/*  98 */             MyTableModel.this.updateFilteredEvents(toHead);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   private static final String[] COL_NAMES = new String[] { "Time", "Priority", "Trace", "Category", "NDC", "Message" };
/*     */ 
/*     */   
/* 110 */   private static final EventDetails[] EMPTY_LIST = new EventDetails[0];
/*     */ 
/*     */   
/* 113 */   private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance(3, 2);
/*     */ 
/*     */ 
/*     */   
/* 117 */   private final Object mLock = new Object();
/*     */   
/* 119 */   private final SortedSet mAllEvents = new TreeSet(MY_COMP);
/*     */   
/* 121 */   private EventDetails[] mFilteredEvents = EMPTY_LIST;
/*     */   
/* 123 */   private final List mPendingEvents = new ArrayList();
/*     */ 
/*     */   
/*     */   private boolean mPaused = false;
/*     */   
/* 128 */   private String mThreadFilter = "";
/*     */   
/* 130 */   private String mMessageFilter = "";
/*     */   
/* 132 */   private String mNDCFilter = "";
/*     */   
/* 134 */   private String mCategoryFilter = "";
/*     */   
/* 136 */   private Priority mPriorityFilter = Priority.DEBUG;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MyTableModel() {
/* 143 */     Thread t = new Thread(new Processor());
/* 144 */     t.setDaemon(true);
/* 145 */     t.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 154 */     synchronized (this.mLock) {
/* 155 */       return this.mFilteredEvents.length;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 162 */     return COL_NAMES.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnName(int aCol) {
/* 168 */     return COL_NAMES[aCol];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getColumnClass(int aCol) {
/* 174 */     return (aCol == 2) ? Boolean.class : Object.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValueAt(int aRow, int aCol) {
/* 179 */     synchronized (this.mLock) {
/* 180 */       EventDetails event = this.mFilteredEvents[aRow];
/*     */       
/* 182 */       if (aCol == 0)
/* 183 */         return DATE_FORMATTER.format(new Date(event.getTimeStamp())); 
/* 184 */       if (aCol == 1)
/* 185 */         return event.getPriority(); 
/* 186 */       if (aCol == 2)
/* 187 */         return (event.getThrowableStrRep() == null) ? Boolean.FALSE : Boolean.TRUE; 
/* 188 */       if (aCol == 3)
/* 189 */         return event.getCategoryName(); 
/* 190 */       if (aCol == 4) {
/* 191 */         return event.getNDC();
/*     */       }
/* 193 */       return event.getMessage();
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
/*     */   public void setPriorityFilter(Priority aPriority) {
/* 208 */     synchronized (this.mLock) {
/* 209 */       this.mPriorityFilter = aPriority;
/* 210 */       updateFilteredEvents(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadFilter(String aStr) {
/* 220 */     synchronized (this.mLock) {
/* 221 */       this.mThreadFilter = aStr.trim();
/* 222 */       updateFilteredEvents(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageFilter(String aStr) {
/* 232 */     synchronized (this.mLock) {
/* 233 */       this.mMessageFilter = aStr.trim();
/* 234 */       updateFilteredEvents(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNDCFilter(String aStr) {
/* 244 */     synchronized (this.mLock) {
/* 245 */       this.mNDCFilter = aStr.trim();
/* 246 */       updateFilteredEvents(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCategoryFilter(String aStr) {
/* 256 */     synchronized (this.mLock) {
/* 257 */       this.mCategoryFilter = aStr.trim();
/* 258 */       updateFilteredEvents(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEvent(EventDetails aEvent) {
/* 268 */     synchronized (this.mLock) {
/* 269 */       this.mPendingEvents.add(aEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 277 */     synchronized (this.mLock) {
/* 278 */       this.mAllEvents.clear();
/* 279 */       this.mFilteredEvents = new EventDetails[0];
/* 280 */       this.mPendingEvents.clear();
/* 281 */       fireTableDataChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void toggle() {
/* 287 */     synchronized (this.mLock) {
/* 288 */       this.mPaused = !this.mPaused;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPaused() {
/* 294 */     synchronized (this.mLock) {
/* 295 */       return this.mPaused;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventDetails getEventDetails(int aRow) {
/* 306 */     synchronized (this.mLock) {
/* 307 */       return this.mFilteredEvents[aRow];
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
/*     */   
/*     */   private void updateFilteredEvents(boolean aInsertedToFront) {
/* 323 */     long start = System.currentTimeMillis();
/* 324 */     List<EventDetails> filtered = new ArrayList();
/* 325 */     int size = this.mAllEvents.size();
/* 326 */     Iterator<EventDetails> it = this.mAllEvents.iterator();
/*     */     
/* 328 */     while (it.hasNext()) {
/* 329 */       EventDetails event = it.next();
/* 330 */       if (matchFilter(event)) {
/* 331 */         filtered.add(event);
/*     */       }
/*     */     } 
/*     */     
/* 335 */     EventDetails lastFirst = (this.mFilteredEvents.length == 0) ? null : this.mFilteredEvents[0];
/* 336 */     this.mFilteredEvents = filtered.<EventDetails>toArray(EMPTY_LIST);
/*     */     
/* 338 */     if (aInsertedToFront && lastFirst != null) {
/* 339 */       int index = filtered.indexOf(lastFirst);
/* 340 */       if (index < 1) {
/* 341 */         LOG.warn("In strange state");
/* 342 */         fireTableDataChanged();
/*     */       } else {
/* 344 */         fireTableRowsInserted(0, index - 1);
/*     */       } 
/*     */     } else {
/* 347 */       fireTableDataChanged();
/*     */     } 
/*     */     
/* 350 */     long end = System.currentTimeMillis();
/* 351 */     LOG.debug("Total time [ms]: " + (end - start) + " in update, size: " + size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchFilter(EventDetails aEvent) {
/* 361 */     if (aEvent.getPriority().isGreaterOrEqual(this.mPriorityFilter) && aEvent
/* 362 */       .getThreadName().indexOf(this.mThreadFilter) >= 0 && aEvent
/* 363 */       .getCategoryName().indexOf(this.mCategoryFilter) >= 0 && (this.mNDCFilter.length() == 0 || (aEvent
/* 364 */       .getNDC() != null && aEvent.getNDC().indexOf(this.mNDCFilter) >= 0))) {
/* 365 */       String rm = aEvent.getMessage();
/* 366 */       if (rm == null)
/*     */       {
/* 368 */         return (this.mMessageFilter.length() == 0);
/*     */       }
/* 370 */       return (rm.indexOf(this.mMessageFilter) >= 0);
/*     */     } 
/*     */ 
/*     */     
/* 374 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/MyTableModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
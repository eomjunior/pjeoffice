/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicPublisher;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public class JMSAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   String securityPrincipalName;
/*     */   String securityCredentials;
/*     */   String initialContextFactoryName;
/*     */   String urlPkgPrefixes;
/*     */   String providerURL;
/*     */   String topicBindingName;
/*     */   String tcfBindingName;
/*     */   String userName;
/*     */   String password;
/*     */   boolean locationInfo;
/*     */   TopicConnection topicConnection;
/*     */   TopicSession topicSession;
/*     */   TopicPublisher topicPublisher;
/*     */   
/*     */   public void setTopicConnectionFactoryBindingName(String tcfBindingName) {
/* 130 */     this.tcfBindingName = tcfBindingName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTopicConnectionFactoryBindingName() {
/* 137 */     return this.tcfBindingName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTopicBindingName(String topicBindingName) {
/* 145 */     this.topicBindingName = topicBindingName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTopicBindingName() {
/* 152 */     return this.topicBindingName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/* 160 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*     */     try {
/*     */       Context jndi;
/* 172 */       LogLog.debug("Getting initial context.");
/* 173 */       if (this.initialContextFactoryName != null) {
/* 174 */         Properties env = new Properties();
/* 175 */         env.put("java.naming.factory.initial", this.initialContextFactoryName);
/* 176 */         if (this.providerURL != null) {
/* 177 */           env.put("java.naming.provider.url", this.providerURL);
/*     */         } else {
/* 179 */           LogLog.warn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
/*     */         } 
/*     */         
/* 182 */         if (this.urlPkgPrefixes != null) {
/* 183 */           env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
/*     */         }
/*     */         
/* 186 */         if (this.securityPrincipalName != null) {
/* 187 */           env.put("java.naming.security.principal", this.securityPrincipalName);
/* 188 */           if (this.securityCredentials != null) {
/* 189 */             env.put("java.naming.security.credentials", this.securityCredentials);
/*     */           } else {
/* 191 */             LogLog.warn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
/*     */           } 
/*     */         } 
/*     */         
/* 195 */         jndi = new InitialContext(env);
/*     */       } else {
/* 197 */         jndi = new InitialContext();
/*     */       } 
/*     */       
/* 200 */       LogLog.debug("Looking up [" + this.tcfBindingName + "]");
/* 201 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(jndi, this.tcfBindingName);
/* 202 */       LogLog.debug("About to create TopicConnection.");
/* 203 */       if (this.userName != null) {
/* 204 */         this.topicConnection = topicConnectionFactory.createTopicConnection(this.userName, this.password);
/*     */       } else {
/* 206 */         this.topicConnection = topicConnectionFactory.createTopicConnection();
/*     */       } 
/*     */       
/* 209 */       LogLog.debug("Creating TopicSession, non-transactional, in AUTO_ACKNOWLEDGE mode.");
/* 210 */       this.topicSession = this.topicConnection.createTopicSession(false, 1);
/*     */       
/* 212 */       LogLog.debug("Looking up topic name [" + this.topicBindingName + "].");
/* 213 */       Topic topic = (Topic)lookup(jndi, this.topicBindingName);
/*     */       
/* 215 */       LogLog.debug("Creating TopicPublisher.");
/* 216 */       this.topicPublisher = this.topicSession.createPublisher(topic);
/*     */       
/* 218 */       LogLog.debug("Starting TopicConnection.");
/* 219 */       this.topicConnection.start();
/*     */       
/* 221 */       jndi.close();
/* 222 */     } catch (JMSException e) {
/* 223 */       this.errorHandler.error("Error while activating options for appender named [" + this.name + "].", (Exception)e, 0);
/*     */     }
/* 225 */     catch (NamingException e) {
/* 226 */       this.errorHandler.error("Error while activating options for appender named [" + this.name + "].", e, 0);
/*     */     }
/* 228 */     catch (RuntimeException e) {
/* 229 */       this.errorHandler.error("Error while activating options for appender named [" + this.name + "].", e, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object lookup(Context ctx, String name) throws NamingException {
/* 235 */     Object result = JNDIUtil.lookupObject(ctx, name);
/* 236 */     if (result == null) {
/* 237 */       String msg = "Could not find name [" + name + "].";
/* 238 */       throw new NamingException(msg);
/*     */     } 
/* 240 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean checkEntryConditions() {
/* 244 */     String fail = null;
/*     */     
/* 246 */     if (this.topicConnection == null) {
/* 247 */       fail = "No TopicConnection";
/* 248 */     } else if (this.topicSession == null) {
/* 249 */       fail = "No TopicSession";
/* 250 */     } else if (this.topicPublisher == null) {
/* 251 */       fail = "No TopicPublisher";
/*     */     } 
/*     */     
/* 254 */     if (fail != null) {
/* 255 */       this.errorHandler.error(fail + " for JMSAppender named [" + this.name + "].");
/* 256 */       return false;
/*     */     } 
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 269 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 272 */     LogLog.debug("Closing appender [" + this.name + "].");
/* 273 */     this.closed = true;
/*     */     
/*     */     try {
/* 276 */       if (this.topicSession != null)
/* 277 */         this.topicSession.close(); 
/* 278 */       if (this.topicConnection != null)
/* 279 */         this.topicConnection.close(); 
/* 280 */     } catch (JMSException e) {
/* 281 */       LogLog.error("Error while closing JMSAppender [" + this.name + "].", (Throwable)e);
/* 282 */     } catch (RuntimeException e) {
/* 283 */       LogLog.error("Error while closing JMSAppender [" + this.name + "].", e);
/*     */     } 
/*     */     
/* 286 */     this.topicPublisher = null;
/* 287 */     this.topicSession = null;
/* 288 */     this.topicConnection = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 296 */     if (!checkEntryConditions()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 301 */       ObjectMessage msg = this.topicSession.createObjectMessage();
/* 302 */       if (this.locationInfo) {
/* 303 */         event.getLocationInformation();
/*     */       }
/* 305 */       msg.setObject((Serializable)event);
/* 306 */       this.topicPublisher.publish((Message)msg);
/* 307 */     } catch (JMSException e) {
/* 308 */       this.errorHandler.error("Could not publish message in JMSAppender [" + this.name + "].", (Exception)e, 0);
/*     */     }
/* 310 */     catch (RuntimeException e) {
/* 311 */       this.errorHandler.error("Could not publish message in JMSAppender [" + this.name + "].", e, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInitialContextFactoryName() {
/* 322 */     return this.initialContextFactoryName;
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
/*     */   public void setInitialContextFactoryName(String initialContextFactoryName) {
/* 336 */     this.initialContextFactoryName = initialContextFactoryName;
/*     */   }
/*     */   
/*     */   public String getProviderURL() {
/* 340 */     return this.providerURL;
/*     */   }
/*     */   
/*     */   public void setProviderURL(String providerURL) {
/* 344 */     this.providerURL = providerURL;
/*     */   }
/*     */   
/*     */   String getURLPkgPrefixes() {
/* 348 */     return this.urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public void setURLPkgPrefixes(String urlPkgPrefixes) {
/* 352 */     this.urlPkgPrefixes = urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public String getSecurityCredentials() {
/* 356 */     return this.securityCredentials;
/*     */   }
/*     */   
/*     */   public void setSecurityCredentials(String securityCredentials) {
/* 360 */     this.securityCredentials = securityCredentials;
/*     */   }
/*     */   
/*     */   public String getSecurityPrincipalName() {
/* 364 */     return this.securityPrincipalName;
/*     */   }
/*     */   
/*     */   public void setSecurityPrincipalName(String securityPrincipalName) {
/* 368 */     this.securityPrincipalName = securityPrincipalName;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 372 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String userName) {
/* 382 */     this.userName = userName;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 386 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 393 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationInfo(boolean locationInfo) {
/* 402 */     this.locationInfo = locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TopicConnection getTopicConnection() {
/* 410 */     return this.topicConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TopicSession getTopicSession() {
/* 418 */     return this.topicSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TopicPublisher getTopicPublisher() {
/* 426 */     return this.topicPublisher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 434 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/JMSAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
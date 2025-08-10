/*      */ package org.apache.hc.core5.net;
/*      */ 
/*      */ import java.net.InetAddress;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import org.apache.hc.core5.http.HttpHost;
/*      */ import org.apache.hc.core5.http.NameValuePair;
/*      */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*      */ import org.apache.hc.core5.http.message.ParserCursor;
/*      */ import org.apache.hc.core5.util.Args;
/*      */ import org.apache.hc.core5.util.TextUtils;
/*      */ import org.apache.hc.core5.util.Tokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class URIBuilder
/*      */ {
/*      */   private String scheme;
/*      */   private String encodedSchemeSpecificPart;
/*      */   private String encodedAuthority;
/*      */   private String userInfo;
/*      */   private String encodedUserInfo;
/*      */   private String host;
/*      */   private int port;
/*      */   private String encodedPath;
/*      */   private boolean pathRootless;
/*      */   private List<String> pathSegments;
/*      */   private String encodedQuery;
/*      */   private List<NameValuePair> queryParams;
/*      */   private String query;
/*      */   private Charset charset;
/*      */   private String fragment;
/*      */   private String encodedFragment;
/*      */   private static final char QUERY_PARAM_SEPARATOR = '&';
/*      */   private static final char PARAM_VALUE_SEPARATOR = '=';
/*      */   private static final char PATH_SEPARATOR = '/';
/*      */   
/*      */   public static URIBuilder localhost() throws UnknownHostException {
/*   64 */     return (new URIBuilder()).setHost(InetAddress.getLocalHost());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static URIBuilder loopbackAddress() {
/*   71 */     return (new URIBuilder()).setHost(InetAddress.getLoopbackAddress());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder() {
/*   96 */     this.port = -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder(String uriString) throws URISyntaxException {
/*  106 */     this(new URI(uriString), StandardCharsets.UTF_8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder(URI uri) {
/*  114 */     this(uri, StandardCharsets.UTF_8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder(String uriString, Charset charset) throws URISyntaxException {
/*  124 */     this(new URI(uriString), charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder(URI uri, Charset charset) {
/*  134 */     digestURI(uri, charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setAuthority(NamedEndpoint authority) {
/*  145 */     setUserInfo(null);
/*  146 */     setHost(authority.getHostName());
/*  147 */     setPort(authority.getPort());
/*  148 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setAuthority(URIAuthority authority) {
/*  159 */     setUserInfo(authority.getUserInfo());
/*  160 */     setHost(authority.getHostName());
/*  161 */     setPort(authority.getPort());
/*  162 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setCharset(Charset charset) {
/*  172 */     this.charset = charset;
/*  173 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIAuthority getAuthority() {
/*  183 */     return new URIAuthority(getUserInfo(), getHost(), getPort());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Charset getCharset() {
/*  192 */     return this.charset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  199 */   private static final BitSet QUERY_PARAM_SEPARATORS = new BitSet(256);
/*  200 */   private static final BitSet QUERY_VALUE_SEPARATORS = new BitSet(256);
/*  201 */   private static final BitSet PATH_SEPARATORS = new BitSet(256);
/*      */   
/*      */   static {
/*  204 */     QUERY_PARAM_SEPARATORS.set(38);
/*  205 */     QUERY_PARAM_SEPARATORS.set(61);
/*  206 */     QUERY_VALUE_SEPARATORS.set(38);
/*  207 */     PATH_SEPARATORS.set(47);
/*      */   }
/*      */   
/*      */   static List<NameValuePair> parseQuery(CharSequence s, Charset charset, boolean plusAsBlank) {
/*  211 */     if (s == null) {
/*  212 */       return null;
/*      */     }
/*  214 */     Tokenizer tokenParser = Tokenizer.INSTANCE;
/*  215 */     ParserCursor cursor = new ParserCursor(0, s.length());
/*  216 */     List<NameValuePair> list = new ArrayList<>();
/*  217 */     while (!cursor.atEnd()) {
/*  218 */       String name = tokenParser.parseToken(s, (Tokenizer.Cursor)cursor, QUERY_PARAM_SEPARATORS);
/*  219 */       String value = null;
/*  220 */       if (!cursor.atEnd()) {
/*  221 */         int delim = s.charAt(cursor.getPos());
/*  222 */         cursor.updatePos(cursor.getPos() + 1);
/*  223 */         if (delim == 61) {
/*  224 */           value = tokenParser.parseToken(s, (Tokenizer.Cursor)cursor, QUERY_VALUE_SEPARATORS);
/*  225 */           if (!cursor.atEnd()) {
/*  226 */             cursor.updatePos(cursor.getPos() + 1);
/*      */           }
/*      */         } 
/*      */       } 
/*  230 */       if (!name.isEmpty()) {
/*  231 */         list.add(new BasicNameValuePair(
/*  232 */               PercentCodec.decode(name, charset, plusAsBlank), 
/*  233 */               PercentCodec.decode(value, charset, plusAsBlank)));
/*      */       }
/*      */     } 
/*  236 */     return list;
/*      */   }
/*      */   
/*      */   static List<String> splitPath(CharSequence s) {
/*  240 */     if (s == null) {
/*  241 */       return null;
/*      */     }
/*  243 */     ParserCursor cursor = new ParserCursor(0, s.length());
/*      */     
/*  245 */     if (cursor.atEnd()) {
/*  246 */       return new ArrayList<>(0);
/*      */     }
/*  248 */     if (PATH_SEPARATORS.get(s.charAt(cursor.getPos()))) {
/*  249 */       cursor.updatePos(cursor.getPos() + 1);
/*      */     }
/*  251 */     List<String> list = new ArrayList<>();
/*  252 */     StringBuilder buf = new StringBuilder();
/*      */     while (true) {
/*  254 */       if (cursor.atEnd()) {
/*  255 */         list.add(buf.toString());
/*      */         break;
/*      */       } 
/*  258 */       char current = s.charAt(cursor.getPos());
/*  259 */       if (PATH_SEPARATORS.get(current)) {
/*  260 */         list.add(buf.toString());
/*  261 */         buf.setLength(0);
/*      */       } else {
/*  263 */         buf.append(current);
/*      */       } 
/*  265 */       cursor.updatePos(cursor.getPos() + 1);
/*      */     } 
/*  267 */     return list;
/*      */   }
/*      */   
/*      */   static List<String> parsePath(CharSequence s, Charset charset) {
/*  271 */     if (s == null) {
/*  272 */       return null;
/*      */     }
/*  274 */     List<String> segments = splitPath(s);
/*  275 */     List<String> list = new ArrayList<>(segments.size());
/*  276 */     for (String segment : segments) {
/*  277 */       list.add(PercentCodec.decode(segment, charset));
/*      */     }
/*  279 */     return list;
/*      */   }
/*      */   
/*      */   static void formatPath(StringBuilder buf, Iterable<String> segments, boolean rootless, Charset charset) {
/*  283 */     int i = 0;
/*  284 */     for (String segment : segments) {
/*  285 */       if (i > 0 || !rootless) {
/*  286 */         buf.append('/');
/*      */       }
/*  288 */       PercentCodec.encode(buf, segment, charset);
/*  289 */       i++;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void formatQuery(StringBuilder buf, Iterable<? extends NameValuePair> params, Charset charset, boolean blankAsPlus) {
/*  295 */     int i = 0;
/*  296 */     for (NameValuePair parameter : params) {
/*  297 */       if (i > 0) {
/*  298 */         buf.append('&');
/*      */       }
/*  300 */       PercentCodec.encode(buf, parameter.getName(), charset, blankAsPlus);
/*  301 */       if (parameter.getValue() != null) {
/*  302 */         buf.append('=');
/*  303 */         PercentCodec.encode(buf, parameter.getValue(), charset, blankAsPlus);
/*      */       } 
/*  305 */       i++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URI build() throws URISyntaxException {
/*  313 */     return new URI(buildString());
/*      */   }
/*      */   
/*      */   private String buildString() {
/*  317 */     StringBuilder sb = new StringBuilder();
/*  318 */     if (this.scheme != null) {
/*  319 */       sb.append(this.scheme).append(':');
/*      */     }
/*  321 */     if (this.encodedSchemeSpecificPart != null) {
/*  322 */       sb.append(this.encodedSchemeSpecificPart);
/*      */     } else {
/*      */       boolean authoritySpecified;
/*  325 */       if (this.encodedAuthority != null) {
/*  326 */         sb.append("//").append(this.encodedAuthority);
/*  327 */         authoritySpecified = true;
/*  328 */       } else if (this.host != null) {
/*  329 */         sb.append("//");
/*  330 */         if (this.encodedUserInfo != null) {
/*  331 */           sb.append(this.encodedUserInfo).append("@");
/*  332 */         } else if (this.userInfo != null) {
/*  333 */           int idx = this.userInfo.indexOf(':');
/*  334 */           if (idx != -1) {
/*  335 */             PercentCodec.encode(sb, this.userInfo.substring(0, idx), this.charset);
/*  336 */             sb.append(':');
/*  337 */             PercentCodec.encode(sb, this.userInfo.substring(idx + 1), this.charset);
/*      */           } else {
/*  339 */             PercentCodec.encode(sb, this.userInfo, this.charset);
/*      */           } 
/*  341 */           sb.append("@");
/*      */         } 
/*  343 */         if (InetAddressUtils.isIPv6Address(this.host)) {
/*  344 */           sb.append("[").append(this.host).append("]");
/*      */         } else {
/*  346 */           sb.append(PercentCodec.encode(this.host, this.charset));
/*      */         } 
/*  348 */         if (this.port >= 0) {
/*  349 */           sb.append(":").append(this.port);
/*      */         }
/*  351 */         authoritySpecified = true;
/*      */       } else {
/*  353 */         authoritySpecified = false;
/*      */       } 
/*  355 */       if (this.encodedPath != null) {
/*  356 */         if (authoritySpecified && !TextUtils.isEmpty(this.encodedPath) && !this.encodedPath.startsWith("/")) {
/*  357 */           sb.append('/');
/*      */         }
/*  359 */         sb.append(this.encodedPath);
/*  360 */       } else if (this.pathSegments != null) {
/*  361 */         formatPath(sb, this.pathSegments, (!authoritySpecified && this.pathRootless), this.charset);
/*      */       } 
/*  363 */       if (this.encodedQuery != null) {
/*  364 */         sb.append("?").append(this.encodedQuery);
/*  365 */       } else if (this.queryParams != null && !this.queryParams.isEmpty()) {
/*  366 */         sb.append("?");
/*  367 */         formatQuery(sb, this.queryParams, this.charset, false);
/*  368 */       } else if (this.query != null) {
/*  369 */         sb.append("?");
/*  370 */         PercentCodec.encode(sb, this.query, this.charset, PercentCodec.URIC, false);
/*      */       } 
/*      */     } 
/*  373 */     if (this.encodedFragment != null) {
/*  374 */       sb.append("#").append(this.encodedFragment);
/*  375 */     } else if (this.fragment != null) {
/*  376 */       sb.append("#");
/*  377 */       PercentCodec.encode(sb, this.fragment, this.charset);
/*      */     } 
/*  379 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private void digestURI(URI uri, Charset charset) {
/*  383 */     this.scheme = uri.getScheme();
/*  384 */     this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
/*  385 */     this.encodedAuthority = uri.getRawAuthority();
/*  386 */     String uriHost = uri.getHost();
/*      */ 
/*      */     
/*  389 */     this
/*  390 */       .host = (uriHost != null && InetAddressUtils.isIPv6URLBracketedAddress(uriHost)) ? uriHost.substring(1, uriHost.length() - 1) : uriHost;
/*      */     
/*  392 */     this.port = uri.getPort();
/*  393 */     this.encodedUserInfo = uri.getRawUserInfo();
/*  394 */     this.userInfo = uri.getUserInfo();
/*  395 */     if (this.encodedAuthority != null && this.host == null) {
/*      */       try {
/*  397 */         URIAuthority uriAuthority = URIAuthority.parse(this.encodedAuthority);
/*  398 */         this.encodedUserInfo = uriAuthority.getUserInfo();
/*  399 */         this.userInfo = PercentCodec.decode(uriAuthority.getUserInfo(), charset);
/*  400 */         this.host = PercentCodec.decode(uriAuthority.getHostName(), charset);
/*  401 */         this.port = uriAuthority.getPort();
/*  402 */       } catch (URISyntaxException uRISyntaxException) {}
/*      */     }
/*      */ 
/*      */     
/*  406 */     this.encodedPath = uri.getRawPath();
/*  407 */     this.pathSegments = parsePath(uri.getRawPath(), charset);
/*  408 */     this.pathRootless = (uri.getRawPath() == null || !uri.getRawPath().startsWith("/"));
/*  409 */     this.encodedQuery = uri.getRawQuery();
/*  410 */     this.queryParams = parseQuery(uri.getRawQuery(), charset, false);
/*  411 */     this.encodedFragment = uri.getRawFragment();
/*  412 */     this.fragment = uri.getFragment();
/*  413 */     this.charset = charset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setScheme(String scheme) {
/*  422 */     this.scheme = !TextUtils.isBlank(scheme) ? scheme : null;
/*  423 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setSchemeSpecificPart(String schemeSpecificPart) {
/*  434 */     this.encodedSchemeSpecificPart = schemeSpecificPart;
/*  435 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setSchemeSpecificPart(String schemeSpecificPart, NameValuePair... nvps) {
/*  447 */     return setSchemeSpecificPart(schemeSpecificPart, (nvps != null) ? Arrays.<NameValuePair>asList(nvps) : null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setSchemeSpecificPart(String schemeSpecificPart, List<NameValuePair> nvps) {
/*  459 */     this.encodedSchemeSpecificPart = null;
/*  460 */     if (!TextUtils.isBlank(schemeSpecificPart)) {
/*  461 */       StringBuilder sb = new StringBuilder(schemeSpecificPart);
/*  462 */       if (nvps != null && !nvps.isEmpty()) {
/*  463 */         sb.append("?");
/*  464 */         formatQuery(sb, nvps, this.charset, false);
/*      */       } 
/*  466 */       this.encodedSchemeSpecificPart = sb.toString();
/*      */     } 
/*  468 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setUserInfo(String userInfo) {
/*  478 */     this.userInfo = !TextUtils.isBlank(userInfo) ? userInfo : null;
/*  479 */     this.encodedSchemeSpecificPart = null;
/*  480 */     this.encodedAuthority = null;
/*  481 */     this.encodedUserInfo = null;
/*  482 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public URIBuilder setUserInfo(String username, String password) {
/*  496 */     return setUserInfo(username + ':' + password);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setHost(InetAddress host) {
/*  505 */     this.host = (host != null) ? host.getHostAddress() : null;
/*  506 */     this.encodedSchemeSpecificPart = null;
/*  507 */     this.encodedAuthority = null;
/*  508 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setHost(String host) {
/*  519 */     this.host = host;
/*  520 */     this.encodedSchemeSpecificPart = null;
/*  521 */     this.encodedAuthority = null;
/*  522 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setHttpHost(HttpHost httpHost) {
/*  532 */     setScheme(httpHost.getSchemeName());
/*  533 */     setHost(httpHost.getHostName());
/*  534 */     setPort(httpHost.getPort());
/*  535 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setPort(int port) {
/*  544 */     this.port = (port < 0) ? -1 : port;
/*  545 */     this.encodedSchemeSpecificPart = null;
/*  546 */     this.encodedAuthority = null;
/*  547 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setPath(String path) {
/*  556 */     setPathSegments((path != null) ? splitPath(path) : null);
/*  557 */     this.pathRootless = (path != null && !path.startsWith("/"));
/*  558 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder appendPath(String path) {
/*  567 */     if (path != null) {
/*  568 */       appendPathSegments(splitPath(path));
/*      */     }
/*  570 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setPathSegments(String... pathSegments) {
/*  579 */     return setPathSegments(Arrays.asList(pathSegments));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder appendPathSegments(String... pathSegments) {
/*  588 */     return appendPathSegments(Arrays.asList(pathSegments));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setPathSegmentsRootless(String... pathSegments) {
/*  600 */     return setPathSegmentsRootless(Arrays.asList(pathSegments));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setPathSegments(List<String> pathSegments) {
/*  609 */     this.pathSegments = (pathSegments != null && !pathSegments.isEmpty()) ? new ArrayList<>(pathSegments) : null;
/*  610 */     this.encodedSchemeSpecificPart = null;
/*  611 */     this.encodedPath = null;
/*  612 */     this.pathRootless = false;
/*  613 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder appendPathSegments(List<String> pathSegments) {
/*  622 */     if (pathSegments != null && !pathSegments.isEmpty()) {
/*  623 */       if (this.pathSegments == null) {
/*  624 */         this.pathSegments = new ArrayList<>();
/*      */       }
/*  626 */       this.pathSegments.addAll(pathSegments);
/*  627 */       this.encodedSchemeSpecificPart = null;
/*  628 */       this.encodedPath = null;
/*      */     } 
/*  630 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setPathSegmentsRootless(List<String> pathSegments) {
/*  642 */     this.pathSegments = (pathSegments != null && !pathSegments.isEmpty()) ? new ArrayList<>(pathSegments) : null;
/*  643 */     this.encodedSchemeSpecificPart = null;
/*  644 */     this.encodedPath = null;
/*  645 */     this.pathRootless = true;
/*  646 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder removeQuery() {
/*  655 */     this.queryParams = null;
/*  656 */     this.query = null;
/*  657 */     this.encodedQuery = null;
/*  658 */     this.encodedSchemeSpecificPart = null;
/*  659 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setParameters(List<NameValuePair> nameValuePairs) {
/*  673 */     if (this.queryParams == null) {
/*  674 */       this.queryParams = new ArrayList<>();
/*      */     } else {
/*  676 */       this.queryParams.clear();
/*      */     } 
/*  678 */     if (nameValuePairs != null) {
/*  679 */       this.queryParams.addAll(nameValuePairs);
/*      */     }
/*  681 */     this.encodedQuery = null;
/*  682 */     this.encodedSchemeSpecificPart = null;
/*  683 */     this.query = null;
/*  684 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder addParameters(List<NameValuePair> nameValuePairs) {
/*  698 */     if (this.queryParams == null) {
/*  699 */       this.queryParams = new ArrayList<>();
/*      */     }
/*  701 */     if (nameValuePairs != null) {
/*  702 */       this.queryParams.addAll(nameValuePairs);
/*      */     }
/*  704 */     this.encodedQuery = null;
/*  705 */     this.encodedSchemeSpecificPart = null;
/*  706 */     this.query = null;
/*  707 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setParameters(NameValuePair... nameValuePairs) {
/*  721 */     if (this.queryParams == null) {
/*  722 */       this.queryParams = new ArrayList<>();
/*      */     } else {
/*  724 */       this.queryParams.clear();
/*      */     } 
/*  726 */     if (nameValuePairs != null) {
/*  727 */       Collections.addAll(this.queryParams, nameValuePairs);
/*      */     }
/*  729 */     this.encodedQuery = null;
/*  730 */     this.encodedSchemeSpecificPart = null;
/*  731 */     this.query = null;
/*  732 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder addParameter(String param, String value) {
/*  746 */     return addParameter((NameValuePair)new BasicNameValuePair(param, value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder addParameter(NameValuePair nameValuePair) {
/*  761 */     if (this.queryParams == null) {
/*  762 */       this.queryParams = new ArrayList<>();
/*      */     }
/*  764 */     if (nameValuePair != null) {
/*  765 */       this.queryParams.add(nameValuePair);
/*      */     }
/*  767 */     this.encodedQuery = null;
/*  768 */     this.encodedSchemeSpecificPart = null;
/*  769 */     this.query = null;
/*  770 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder removeParameter(String param) {
/*  785 */     Args.notNull(param, "param");
/*  786 */     if (this.queryParams != null && !this.queryParams.isEmpty()) {
/*  787 */       this.queryParams.removeIf(nvp -> nvp.getName().equals(param));
/*      */     }
/*  789 */     this.encodedQuery = null;
/*  790 */     this.encodedSchemeSpecificPart = null;
/*  791 */     this.query = null;
/*  792 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setParameter(String param, String value) {
/*  806 */     if (this.queryParams == null) {
/*  807 */       this.queryParams = new ArrayList<>();
/*      */     }
/*  809 */     if (!this.queryParams.isEmpty()) {
/*  810 */       this.queryParams.removeIf(nvp -> nvp.getName().equals(param));
/*      */     }
/*  812 */     this.queryParams.add(new BasicNameValuePair(param, value));
/*  813 */     this.encodedQuery = null;
/*  814 */     this.encodedSchemeSpecificPart = null;
/*  815 */     this.query = null;
/*  816 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder clearParameters() {
/*  825 */     this.queryParams = null;
/*  826 */     this.encodedQuery = null;
/*  827 */     this.encodedSchemeSpecificPart = null;
/*  828 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setCustomQuery(String query) {
/*  842 */     this.query = !TextUtils.isBlank(query) ? query : null;
/*  843 */     this.encodedQuery = null;
/*  844 */     this.encodedSchemeSpecificPart = null;
/*  845 */     this.queryParams = null;
/*  846 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder setFragment(String fragment) {
/*  856 */     this.fragment = !TextUtils.isBlank(fragment) ? fragment : null;
/*  857 */     this.encodedFragment = null;
/*  858 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAbsolute() {
/*  867 */     return (this.scheme != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOpaque() {
/*  876 */     return (this.pathSegments == null && this.encodedPath == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getScheme() {
/*  885 */     return this.scheme;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchemeSpecificPart() {
/*  895 */     return this.encodedSchemeSpecificPart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserInfo() {
/*  904 */     return this.userInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getHost() {
/*  914 */     return this.host;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPort() {
/*  923 */     return this.port;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPathEmpty() {
/*  932 */     return ((this.pathSegments == null || this.pathSegments.isEmpty()) && (this.encodedPath == null || this.encodedPath
/*  933 */       .isEmpty()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getPathSegments() {
/*  942 */     return (this.pathSegments != null) ? new ArrayList<>(this.pathSegments) : new ArrayList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPath() {
/*  951 */     if (this.pathSegments == null) {
/*  952 */       return null;
/*      */     }
/*  954 */     StringBuilder result = new StringBuilder();
/*  955 */     for (String segment : this.pathSegments) {
/*  956 */       result.append('/').append(segment);
/*      */     }
/*  958 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isQueryEmpty() {
/*  967 */     return ((this.queryParams == null || this.queryParams.isEmpty()) && this.encodedQuery == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<NameValuePair> getQueryParams() {
/*  976 */     return (this.queryParams != null) ? new ArrayList<>(this.queryParams) : new ArrayList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NameValuePair getFirstQueryParam(String name) {
/*  987 */     return this.queryParams.stream().filter(e -> name.equals(e.getName())).findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFragment() {
/*  996 */     return this.fragment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URIBuilder normalizeSyntax() {
/* 1011 */     String scheme = this.scheme;
/* 1012 */     if (scheme != null) {
/* 1013 */       this.scheme = TextUtils.toLowerCase(scheme);
/*      */     }
/*      */     
/* 1016 */     if (this.pathRootless) {
/* 1017 */       return this;
/*      */     }
/*      */ 
/*      */     
/* 1021 */     this.encodedSchemeSpecificPart = null;
/* 1022 */     this.encodedAuthority = null;
/* 1023 */     this.encodedUserInfo = null;
/* 1024 */     this.encodedPath = null;
/* 1025 */     this.encodedQuery = null;
/* 1026 */     this.encodedFragment = null;
/*      */     
/* 1028 */     String host = this.host;
/* 1029 */     if (host != null) {
/* 1030 */       this.host = TextUtils.toLowerCase(host);
/*      */     }
/*      */     
/* 1033 */     if (this.pathSegments != null) {
/* 1034 */       List<String> inputSegments = this.pathSegments;
/* 1035 */       if (!inputSegments.isEmpty()) {
/* 1036 */         LinkedList<String> outputSegments = new LinkedList<>();
/* 1037 */         for (String inputSegment : inputSegments) {
/* 1038 */           if (!inputSegment.isEmpty() && !".".equals(inputSegment)) {
/* 1039 */             if ("..".equals(inputSegment)) {
/* 1040 */               if (!outputSegments.isEmpty())
/* 1041 */                 outputSegments.removeLast(); 
/*      */               continue;
/*      */             } 
/* 1044 */             outputSegments.addLast(inputSegment);
/*      */           } 
/*      */         } 
/*      */         
/* 1048 */         if (!inputSegments.isEmpty()) {
/* 1049 */           String lastSegment = inputSegments.get(inputSegments.size() - 1);
/* 1050 */           if (lastSegment.isEmpty()) {
/* 1051 */             outputSegments.addLast("");
/*      */           }
/*      */         } 
/* 1054 */         this.pathSegments = outputSegments;
/*      */       } else {
/* 1056 */         this.pathSegments = Collections.singletonList("");
/*      */       } 
/*      */     } 
/*      */     
/* 1060 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1070 */     return buildString();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/URIBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
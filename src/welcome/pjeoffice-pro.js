/********************************************************************************************************
/* A LIGHTWEIGHT XMLHttpRequest LIBRARY 
/*******************************************************************************************************/

const PjeClient=function(){function t(){}t.ajax=function(t){t=t||{url:""},t.type=t.type&&t.type.toUpperCase()||"GET",t.headers=t.headers||{},t.timeout=parseInt(t.timeout)||0,t.success=t.success||function(){},t.error=t.error||function(){},t.async="undefined"==typeof t.async?!0:t.async;var e=new XMLHttpRequest;t.timeout>0&&(e.timeout=t.timeout,e.ontimeout=function(){t.error("timeout","timeout",e)}),e.open(t.type,t.url,t.async);for(var s in t.headers)t.headers.hasOwnProperty(s)&&e.setRequestHeader(s,t.headers[s]);return e.send(t.data),e.onreadystatechange=function(){if(4==this.readyState&&(this.status>=200&&this.status<300||304==this.status)){var e=this.responseText,s=this.getResponseHeader("Content-Type");s&&s.match(/json/)&&(e=JSON.parse(this.responseText)),t.success(e,this.statusText,this)}else 4==this.readyState&&t.error(this.status,this.statusText,this)},0==t.async&&(4==e.readyState&&(e.status>=200&&e.status<300||304==e.status)?t.success(e.responseText,e):4==e.readyState&&t.error(e.status,e.statusText,e)),e};var e=function(e,s,n,r){return"function"==typeof n&&(r=n,n=void 0),t.ajax({url:s,data:n,type:e,success:r})};return t.get=function(t,s,n){return e("GET",t,s,n)},t.head=function(t,s,n){return e("HEAD",t,s,n)},t.post=function(t,s,n){return e("POST",t,s,n)},t.patch=function(t,s,n){return e("PATCH",t,s,n)},t.put=function(t,s,n){return e("PUT",t,s,n)},t["delete"]=function(t,s,n){return e("DELETE",t,s,n)},t.options=function(t,s,n){return e("OPTIONS",t,s,n)},t}();__=PjeClient;


/********************************************************************************************************
/* UTILITIES FUNCTIONS
/*******************************************************************************************************/

const readJson = function(json) {
  const jsonStringify = JSON.stringify;  
  const arrayToJson = Array.prototype.toJSON;
  delete Array.prototype.toJSON;
  const output = jsonStringify(json);
  Array.prototype.toJSON = arrayToJson;
  return output;
};

const readCookie = function (cookieName) {
  const cookiePrefix = cookieName + "=";
  const decodedCookies = decodeURIComponent(document.cookie);
  const cookieItems = decodedCookies.split(';');
  for(let i = 0; i < cookieItems.length; i++) {
    let cookie = cookieItems[i];
    while (cookie.charAt(0) == ' ') {
      cookie = cookie.substring(1);
    }
    if (cookie.indexOf(cookiePrefix) == 0) {
      return cookie;
    }
  }
  return "";
};

let readSession = function () {
  return document.cookie; // return readCookie('JSESSIONID'); 
};

/*****************************************************************************************************************
* A constante 'PJEOFFICE_DEFAULT_ARGUMENTS' é a configuração 'global' padrão usada pela instância PjeOffice. Alternativamente 
* você pode remover este código daqui e definir esta instância na sua aplicação com todos os atributos e colocá-la 
* no escopo global javascript. 
/*****************************************************************************************************************/

const PJEOFFICE_DEFAULT_ARGUMENTS = {
  //Aplicação que faz uso do PJeOffice
  //Este parâmetro é OBRIGATÓRIO e deveria ser entendido como uma constante por toda aplicação.
  //O valor deste parâmetro DEVE ser exatamente o mesmo definido no campo 'Nome da aplicação' do formulário
  //mantido no endereço: https://pjeoffice.pje.jus.br/  
  "APP_REQUISITANTE"  : "PJe",

  //Código de segurança da aplicação - proteção CSRF fornecido pelo CNJ
  //Este parâmetro é OBRIGATÓRIO, deveria ser entendido como uma constante por toda aplicação e pode ser gerado
  //no endereço eletrônico https://pjeoffice.pje.jus.br/. Para os fins deste guia com foco no desenvolvedor, o 
  //modo desenvolvimento habilitado tornará esta informação irrelevante.
  "CODIGO_SEGURANCA"  : "bypass",

  //O contexto raiz da aplicação. Comporá o parâmetro 'servidor' do payload de requisição ao PJeOffice Pro.
  //Este parâmetro é OBRIGATÓRIO e deveria ser entendido como uma constante por toda aplicação 
  //OBS: Troque /pjeOffice pelo contexto da aplicação do servidor do Pje (comumente /pje). Aqui é informado /pjeOffice
  //porque o próprio PJeOffice Pro emula o backend web para demonstração da API de integração
  //Nota 1: Este parâmetro DEVE iniciar com '/' já que comporá o endereço completo a partir de 'window.location.origin' 
  //Nota 2: Este parâmetro NÃO deve finalizar com o caracter '/' já que os caminhos relativos exigidos por alguma API já 
  //serão iniciado com '/' e concatenado a este parâmetro
  "WEB_CONTEXT"       : "/pjeOffice",

  //Para ambientes de testes, desenvolvimento, treinamento e cia informe MODO_TESTE=true. Informe false para produção
  //Este parâmetro impede o envio de hash's assinados em ambientes de treinamento ou similares (API signHash)
  //Este parâmetro é OBRIGATÓRIO e deveria ser entendido como uma constante por toda aplicação 
  "MODO_TESTE"        : false,

  //O timeout padrão das requisições PUT entre o navegador e o PJeOffice Pro.
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API  
  "REQUEST_TIMEOUT"   : 600000, //milliseconds (10 minutes)

  //Algoritmo padrão utilizado para assinatura de hash's, família ASN1?withRSA
  //Exemplos: ASN1MD5withRSA, ASN1MD2withRSA, ASN1SHA1withRSA, ASN1SHA256withRSA, ASN1SHA384withRSA, ASN1SHA512withRSA  
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API signHash
  "ALGORITMO_ASSINATURA_HASH"    : "ASN1MD5withRSA",
  
  //Algoritmo padrão utilizado para assinatura da mensagem desafio de autenticação
  //Exemplos:  MD2withRSA, MD5withRSA, SHA1withRSA, SHA256withRSA, SHA384withRSA, SHA512withRSA   
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API login / loginSSO
  "ALGORITMO_AUTENTICACAO"       : "MD5withRSA",
  
  //Algoritmo padrão utilizado para assinatura de documentos (não hash's).
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API signRemote / signBase64
  //Exemplos:  MD2withRSA, MD5withRSA, SHA1withRSA, SHA256withRSA, SHA384withRSA, SHA512withRSA
  "ALGORITMO_ASSINATURA"         : "SHA256withRSA",

  //O padrão de assinatura que será utilizado tal que:
  //CADES: Será assinado como um arquivo application/pkcs7-signature (.p7s)
  //XADES: Será assinado como um arquivo application/xml (.xml)
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API signRemote
  "PADRAO_ASSINATURA"            : "CADES",
  
  //Tipo padrão de assinatura p7s
  //  ATTACHED: conteúdo vinculado à assinatura 
  //  DETACHED: conteúdo desvinculado da assinatura
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API signRemote
  "TIPO_ASSINATURA"      : "ATTACHED",
  
  //Página padrão de autenticação que receberá a mensagem desafio assinada 
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API login / loginSSO
  //Este caminho deve ser relativo a WEB_CONTEXT
  //Nota: Este parâmetro DEVE iniciar com '/'
  "PAGINA_LOGIN"         : "/fakeBackend",
        
  //Página padrão que receberá assinaturas de hash's
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API signHash
  //Este caminho deve ser relativo a WEB_CONTEXT
  //Nota: Este parâmetro DEVE iniciar com '/'
  "PAGINA_ASSINATURA"    : "/fakeBackend",
        
  //Página padrão que receberá arquivos assinados em P7S
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API signRemote / signBase64
  //Este caminho deve ser relativo a WEB_CONTEXT
  //Nota: Este parâmetro DEVE iniciar com '/'
  "PAGINA_UPLOAD"        : "/fakeBackend",
  
  //Página padrão que receberá a cadeia de certificados do usuário
  //Este parâmetro é OBRIGATÓRIO e PODE SER SOBRESCRITO dinamicamente em tempo de chamada API getCertChain
  //Este caminho deve ser relativo a WEB_CONTEXT
  //Nota: Este parâmetro DEVE iniciar com '/'
  "PAGINA_CERTCHAIN"     : "/fakeBackend",
  
  //Página padrão que retornará um JSON descrevendo a versão mínima exigida do PJEOFFICE com suas respectivas mensagens de alerta:
  //Este caminho deve ser relativo a WEB_CONTEXT
  //TRF3:  "/seam/resource/rest/pje-legacy/pje-office/versao"
  //Nota: Este parâmetro DEVE iniciar com '/'
  "PAGINA_VERSAO_MINIMA" : "/fakeBackend/minimum"  
};


/********************************************************************************************************
* Instância PjeOffice disponível para toda a aplicação  
/*******************************************************************************************************/

const PjeOffice = (function () {
    
  function PjeOffice() {}

  //Options 1: http using port 8800
  //Options 2: httpS using port 8801  (require accept localhost certificate on browser)
  
  const PJEOFFICE_PROTOCOL         = "http";
  const PJEOFFICE_PORT             = 8800;
  
  //Please do NOT change these constants if you don't know what you really doing
  const PJEOFFICE_BASE_ENDPOINT    = PJEOFFICE_PROTOCOL + "://127.0.0.1:" + PJEOFFICE_PORT;
  const PJEOFFICE_BASE_CONTEXT     = "/pjeOffice/";
  const PJEOFFICE_TASK_ENDPOINT    = PJEOFFICE_BASE_CONTEXT + "requisicao/";
  const PJEOFFICE_LOGOUT_ENDPOINT  = PJEOFFICE_BASE_CONTEXT + "logout/";
  const PJEOFFICE_VERSION_ENDPOINT = PJEOFFICE_BASE_CONTEXT + "versao/"
  const PJEOFFICE_LOGGER_ENDPOINT  = PJEOFFICE_BASE_CONTEXT + "logger/";
  const PJEOFFICE_PREFLIGHT_METHOD = "PUT";
  const PJEOFFICE_TIMEOUT_ALERT    = 'Alcançado tempo máximo de espera por resposta do PJeOffice Pro (timeout)';     

  const noCache = function() {
    const now = new Date().getTime();
    return '&u=' + (Math.floor(Math.random() * now) + now); //random number!
  };

  const fullRequest = function (method, url, body, apiContext, checkSuccess) {
    const argument      = apiContext?.argument;
    const onSuccess     = apiContext?.onSuccess;
    const onFailed      = apiContext?.onFailed;
    const onUnavailable = apiContext?.onUnavailable;
    let complete = false;
    PjeClient.ajax({
      "url": url,
      "type": method,
      "headers": { "Content-Type": 'application/json', "Force-Preflight": 'true'},
      "timeout": argument?.REQUEST_TIMEOUT || PJEOFFICE_DEFAULT_ARGUMENTS.REQUEST_TIMEOUT,
      "async": true,
      "data": body,
      "error": function(status, statusText, httpClient) {
        if ('timeout' === status)
          alert(PJEOFFICE_TIMEOUT_ALERT);    
        if (complete) 
          return;
        complete = true;
        if (onUnavailable)
          onUnavailable(statusText); 
      },
      "success": function(data, statusText, httpClient) {
        if (complete)
          return;
        complete = true;
        const success = checkSuccess(data);
        if (success) {
          if (onSuccess)
            onSuccess(data);
        } else {
          if (onFailed)
            onFailed(statusText);  
        }
      }
    });
  };
  
  const request = function(method, endPoint, body, apiContext, checkSuccess) {
    return fullRequest(
      method, 
      PJEOFFICE_BASE_ENDPOINT + endPoint, 
      body, 
      apiContext, 
      checkSuccess
    );      
  };
  
  const isObject = function(data) {
    return (typeof data === 'object');
  };
  
  const send = function(endPoint, noParams, body, apiContext, checkSuccess) {
    request(
      PJEOFFICE_PREFLIGHT_METHOD, 
      endPoint + (noParams ? "?" : "") + noCache(), 
      body, 
      apiContext, 
      checkSuccess || function(data) {
        return isObject(data) && data.success;
      }
    );
  };
  
  const get = function(url, apiContext, checkSuccess) {
    fullRequest(
      "GET",
      url, 
      null, //GET has no body 
      apiContext, 
      checkSuccess || isObject
    ); 
  };
  
  const createBodyRequest = function(taskId, task) {
    return readJson({
      "sessao": readSession(),
      "aplicacao": PJEOFFICE_DEFAULT_ARGUMENTS.APP_REQUISITANTE,
      "servidor": window.location.origin + PJEOFFICE_DEFAULT_ARGUMENTS.WEB_CONTEXT,
      "codigoSeguranca": PJEOFFICE_DEFAULT_ARGUMENTS.CODIGO_SEGURANCA,
      "tarefaId": taskId,
      "tarefa": readJson(task)  
    });
  };
  
  const runTask = function(taskId, task, apiContext) {
    send(
      PJEOFFICE_TASK_ENDPOINT, 
      true,
      createBodyRequest(taskId, task), 
      apiContext
    );
  };
  
  const match = function(v1, v2) {
    v1 = v1.split('.');
    v2 = v2.split('.');
    const k = Math.min(v1.length, v2.length);
    for (let i = 0; i < k; i++) {
      v1[i] = parseInt(v1[i], 10);
      v2[i] = parseInt(v2[i], 10);
      if (v1[i] > v2[i]) return 1;
      if (v1[i] < v2[i]) return -1;        
    }
    return v1.length == v2.length ? 0: (v1.length < v2.length ? -1 : 1);
  };
  
  PjeOffice.logout = function(apiContext) {
    send(
      PJEOFFICE_LOGOUT_ENDPOINT, 
      true, 
      null, //no body 
      apiContext
    );
  };

  PjeOffice.ping = function(apiContext) {
    send(
      PJEOFFICE_BASE_CONTEXT, 
      true, 
      null, //no body 
      apiContext
    );
  };
  
  PjeOffice.about = function(apiContext) {
    send(
      PJEOFFICE_VERSION_ENDPOINT, 
      true, 
      null, //no body 
      apiContext, 
      isObject
    );
  };
  
  PjeOffice.getLog = function(windowSize, apiContext) {
    send(
      PJEOFFICE_LOGGER_ENDPOINT + '?windowSize=' + windowSize, 
      false, 
      null, //no body 
      apiContext
    );
  };
  
  PjeOffice.login = function(challengePhrase, apiContext) {
    runTask('cnj.autenticador', {
      "enviarPara": apiContext?.argument?.PAGINA_LOGIN || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_LOGIN,
      "mensagem": challengePhrase,
      "algoritmoAssinatura": apiContext?.argument?.ALGORITMO || PJEOFFICE_DEFAULT_ARGUMENTS.ALGORITMO_AUTENTICACAO,
    }, apiContext);
  };

  PjeOffice.loginSSO = function(challengePhrase, token, apiContext) {
    runTask('sso.autenticador', {
      "enviarPara": apiContext?.argument?.PAGINA_LOGIN || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_LOGIN,
      "mensagem": challengePhrase,
      "token": token,
      "algoritmoAssinatura": apiContext?.argument?.ALGORITMO || PJEOFFICE_DEFAULT_ARGUMENTS.ALGORITMO_AUTENTICACAO
    }, apiContext);  
  };
    
  PjeOffice.signHash = function(documents, apiContext) {
    runTask('cnj.assinadorHash', {
      "algoritmoAssinatura": apiContext?.argument?.ALGORITMO || PJEOFFICE_DEFAULT_ARGUMENTS.ALGORITMO_ASSINATURA_HASH,
      "uploadUrl": apiContext?.argument?.PAGINA_ASSINATURA || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_ASSINATURA,
      "modoTeste": apiContext?.argument?.MODO_TESTE || PJEOFFICE_DEFAULT_ARGUMENTS.MODO_TESTE,
      "arquivos": documents
    }, apiContext);
  };

  PjeOffice.signRemote = function(documents, apiContext) {
    runTask('cnj.assinador', {
      "modo": "REMOTO",
      "padraoAssinatura": apiContext?.argument?.PADRAO_ASSINATURA || PJEOFFICE_DEFAULT_ARGUMENTS.PADRAO_ASSINATURA,
      "tipoAssinatura": apiContext?.argument?.TIPO_ASSINATURA || PJEOFFICE_DEFAULT_ARGUMENTS.TIPO_ASSINATURA,
      "enviarPara": apiContext?.argument?.PAGINA_UPLOAD || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_UPLOAD,
      "algoritmoHash": apiContext?.argument?.ALGORITMO || PJEOFFICE_DEFAULT_ARGUMENTS.ALGORITMO_ASSINATURA,
      "arquivos": documents
    }, apiContext);
  };
  
  PjeOffice.signBase64 = function(documents, apiContext) {
    runTask('cnj.assinadorBase64', {
      "algoritmoAssinatura": apiContext?.argument?.ALGORITMO || PJEOFFICE_DEFAULT_ARGUMENTS.ALGORITMO_ASSINATURA,
      "uploadUrl": apiContext?.argument?.PAGINA_UPLOAD || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_UPLOAD,
      "arquivos": documents
    }, apiContext);  
  };
  
  PjeOffice.getCertChain = function(apiContext) {
    runTask('cnj.certchain', {
      "uploadUrl": apiContext?.argument?.PAGINA_CERTCHAIN || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_CERTCHAIN,
    }, apiContext);
  };

  PjeOffice.printTag = function(printerPort, content, apiContext) {
    runTask('util.impressor', {
      "porta": printerPort,
      "conteudo": content
    }, apiContext);
  };
  
  PjeOffice.simpleTest = function(message, apiContext) {
    runTask('util.test', {
      "message": message
    }, apiContext);
  };
  
  const onMinimumServerSuccess = function(apiContext, pjeofficeData, serverData) {  
    if (serverData.parametros_configurados) {
      const userVersion   = pjeofficeData.versao;  
      const explain = "\n\n===== Vers\u00f5es do PJeOffice =====\n\n" +
        "Do seu computador: " + userVersion + "\n" +
        "M\u00ednima obrigat\u00f3ria: " + serverData.versao_pje_office_minima_obrigatoria + "\n" +
        "M\u00ednima recomendada: " + serverData.versao_pje_office_minima_recomendada + "\n" +
        "Mais recente: " + serverData.versao_pje_office;
     
      let compare = match(userVersion, serverData.versao_pje_office_minima_obrigatoria);     
      if (compare == -1) {
        const failMessage = serverData.versao_pje_office_mensagem_bloqueio + explain;
        alert(failMessage);
        const onFailed  = apiContext?.onFailed;  //Chamado quando a versão do PJEOFFICE NÃO foi aceita  
        if (onFailed) {
          onFailed(failMessage);
        }
        return;
      }
      
      compare = match(userVersion, serverData.versao_pje_office_minima_recomendada);
      if (compare == -1) {
        alert(serverData.versao_pje_office_mensagem_aviso + explain);
      } else {
        compare = match(userVersion, serverData.versao_pje_office);
        if (compare == -1) {
          alert(serverData.versao_pje_office_mensagem_recomendacao + explain);
        }
      }  
    }
    
    const onSuccess = apiContext?.onSuccess;     //Chamado quando a versão do PJEOFFICE foi aceita
    if (onSuccess) {
      onSuccess(serverData);
    }
  };
  
  const onAboutSuccess = function(apiContext, pjeofficeData) {
    const argument      = apiContext?.argument;
    const onUnavailable = apiContext?.onUnavailable; //Invocado quando for impossível concluir se a versão é correta ou não.
    const resource      = argument?.PAGINA_VERSAO_MINIMA || PJEOFFICE_DEFAULT_ARGUMENTS.PAGINA_VERSAO_MINIMA;
    const url           = window.location.origin + PJEOFFICE_DEFAULT_ARGUMENTS.WEB_CONTEXT + resource;
    get(url, {
      "argument": {
        "REQUEST_TIMEOUT": argument?.REQUEST_TIMEOUT || PJEOFFICE_DEFAULT_ARGUMENTS.REQUEST_TIMEOUT
      },  
      "onUnavailable"    : onUnavailable, //Deve ser tratado como onUnavailable porque neste caso do backend NÃO retornou dados     
      "onFailed"         : onUnavailable, //Deve ser tratado como onUnavailable TAMBÉM porque neste caso o backend retornou dados mas NÃO é um objeto JSON conhecido    
      "onSuccess"        : function(serverData) { //Algum dado foi retornado do servidor
        onMinimumServerSuccess(apiContext, pjeofficeData, serverData);
      }
    });
  };
  
  PjeOffice.checkMininum = function(apiContext) {
    PjeOffice.about({
      "argument": {
        "REQUEST_TIMEOUT" : 5000
      },
      "onSuccess": function(pjeofficeData) {
        onAboutSuccess(apiContext, pjeofficeData);
      },
      "onUnavailable": function(statusText) {
         alert("Acesso via certificado digital indispon\u00edvel.\n\n"
          + "Os procedimentos abaixo podem ajudar a resolver o problema:\n\n"
          + "- Verifique se o PJeOffice est\u00e1 em execução no seu computador.\n"
          + "- Verifique se a vers\u00e3o do PJeOffice est\u00e1 atualizada.\n"
          + "- Atualize a tela do navegador com CTRL+F5.\n\n"
          + "Abaixo segue informa\u00e7\u00e3o t\u00e9cnica sobre o erro ocorrido:\n"
          + "- PJeOffice não responde por requisições!"
        );
      },
    });  
  };

  return PjeOffice;
})();

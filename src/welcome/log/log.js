
const Logger = (function() {
  function Logger() {}

  const defaultSuccess = (dom) => {
    if(dom && dom.scrollHeight) {
      dom.scrollTop = dom.scrollHeight;
    }
  }; 
  
  let lastLog = '';
  let ticket;
  let afterSuccess = defaultSuccess;

  Logger.setAutoScroll = function() {
    const dom = document.getElementById('autoscroll');
    afterSuccess = dom && dom.checked ? defaultSuccess : (dom) => {};	
    afterSuccess(document.getElementById("log"));
  };
  
  Logger.clear = function() {
    Logger.setup(-1);
  };
    
  Logger.setup = function (maxSize) {
    PjeOffice.getLog(maxSize || 100, {
      "onSuccess": function(data) {
        const dom = document.getElementById("log");
        if (!dom)
          return;
        const datalog = data["log"];
        const updated = !(datalog == lastLog); 
      	dom.innerHTML = datalog;
      	lastLog = datalog; 
      	if (updated)
       		afterSuccess(dom); 
        if (ticket) {
          clearTimeout(ticket);
          timeout = null;
        }
        ticket = setTimeout(() => Logger.setup(), 3000);
      }, 
      "onUnavailable": function(statusText) {
        if (ticket) {
          clearTimeout(ticket);
          timeout = null;
        }
        alert("Assinador encontra-se indisponível!");  
      }
    });          
  };  
  
  Logger.copy = function() {    
	const dom = document.getElementById("log");
    if (dom) {
      dom.select();
      document.execCommand('copy');
      const selection = window.getSelection ? window.getSelection() : document.selection;
      if (selection) {
        if (selection.removeAllRanges) {
          selection.removeAllRanges();
        } else if (selection.empty) {
          selection.empty();
        }
      }
      const label = document.getElementById("labelcopy");
      if (label) {
		labelcopy.innerHTML = "<strong style='color: green'>Texto copiado!</strong>";
		setTimeout(() => {
			labelcopy.innerHTML = "Copiar texto";
		}, 2000);  
	  }
      
    }    
  };

  return Logger;  
})();


const Tester = (function () {
    
  function Tester() {}

  const personKeys = [
    'pf.name', 
    'pf.cpf',
    'pf.email', 
    'pf.birth.date', 
    'pf.nis', 
    'pf.rg', 
    'pf.cei', 
    'pf.issuing.agency.rg', 
    'pf.issuing.agency.rg.uf',
    
    'pj.cnpj',
    'pj.birth.date',
    'pj.nis',
    'pj.rg',
    'pj.cei',
    'pj.responsible.name',
    'pj.responsible.cpf',
    'pj.business.name',
    'pj.issuing.agency.rg',
    'pj.issuing.agency.rg.uf'
  ];
 
  const infraKeys = [
    'issuer.cn', 
    'issuer.ou',
    'issuer.o', 
    'issuer.c',
    'serial'
  ];

  const validationKeys = [ 
    'before.date', 
    'after.date' 
  ];
  
  const electoralKeys = [
    'pf.electoral.document', 
    'pf.electoral.document.section', 
    'pf.electoral.document.zone', 
    'pf.electoral.document.city', 
    'pf.electoral.document.uf'
  ];

  const setUserData = function (certificate) {
    setFields(certificate, 'person', personKeys);
    setFields(certificate, 'infra', infraKeys);
    setFields(certificate, 'electoral', electoralKeys);
    setFields(certificate, 'validation', validationKeys);
  };

  const setFields = function (instance, attribute, keys) {
    const dom = document.getElementById(attribute + ".view");
    if (dom)
      dom.style.display = '';
    const field = instance[attribute];
    if (!field) {
      if (dom)
        dom.style.display = 'none';
      return;
    }
    keys.forEach(key => setDomValue(field, key));
  };

  const setDomValue = function (instance, attribute) {
    const dom = document.getElementById(attribute);
    if (!dom)
      return;   
    dom.style.display = '';
    const view = document.getElementById(attribute + ".view");
    if (view) {
      view.style.display = '';
    }
    const value = instance[attribute];
    if (!value) {
      dom.style.display = "none";
      if (view) {
        view.style.display = "none";
      }
      return;
    }
    dom.innerHTML = value;
  };

  const disableBtn = function (btnt) {
    btnt.disabled = true;
    btnt.style.opacity = "0.2"
  };

  const enableBtn = function (btnt) {
    let ticket = setTimeout(function() {
		btnt.disabled = false;		
		btnt.style.opacity = "1.0";
		clearTimeout(ticket);
	}, 2000);
  };

  Tester.test = function() {
    const btnt = document.getElementById("btnTest");
    const user = document.getElementById('certUser');
    const list = document.getElementById('chainList'); 
    const resu = document.getElementById('result');
    const succ = document.getElementById('success');
    const fail = document.getElementById('fail');
    user.style.display = 'none';
    list.style.display = 'none';
    resu.style.display = 'none';
    disableBtn(btnt);
    
    PjeOffice.simpleTest("aHR0cHM6Ly9naXRodWIuY29tL2wzb25hcmRvLW9saXYzaXJh", { 
      "onSuccess": function(data) {
		enableBtn(btnt);
        const cuser = data.output[0];
        setUserData(cuser);
        const chain = data.output.slice(1);
        setChainData(chain);
        btnt.disabled = false;
        certUser.style.display = '';
        list.style.display = '';
        resu.style.display = '';
        succ.style.display = '';
        fail.style.display = 'none';	    
      },
      "onFailed": function() {
	    enableBtn(btnt);
        resu.style.display = '';
        succ.style.display = 'none';
        fail.style.display = '';
      },
      "onUnavailable": function() {
	    enableBtn(btnt);
        resu.style.display = '';
        succ.style.display = 'none';
        fail.style.display = '';
        setTimeout(() => {
          alert("O assinador encontra-se indisponível!");  
        }, 1000);
      }
    });
  };
  
  const setChainData = function (chain) {
    const chainBody = document.getElementById('chainBody');
    chainBody.innerHTML = ''; //clean old values
    if (chain.length == 0) {
      chainBody.insertAdjacentHTML('beforeend', '<p>AUSENTE</p>');
    } else {
      chain.forEach(function(certificate, index) {
        chainBody.insertAdjacentHTML('beforeend', (index > 0 ? '<p></p>' : '') + computeDom(certificate));
      });
    }
  };

  //KEEP CALM! WE HAVE TO GO BACK HERE!
  const computeDom = function(certificate) {
    return `<div class="row">
    <div class="col-12">
      <div class="card mt-1">
        <div class="card-header">
          <span style="font-size: larger;">` + certificate.infra['subject.cn'] + `</span>
          <div style="float: right; font-size: x-small;">
            <span class="fieldname">Serial:</span>
            <span class="fieldvalue">` + certificate.infra['serial'] + `</span>
          </div>
        </div>
        <div class="row">
          <div class="col-12">
            <p class="mx-3 mt-4 mb-2 ms-4">
              <span style="font-size: larger;">Emitido Para</span>
            </p>
            <ul class="list-group list-group-flush ms-4">
              <li class="list-group-item">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Nome comum (CN):</span>
                    </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['subject.cn'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item backlineon">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Unidade Organizacional (OU):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['subject.ou'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item backlineon">
                  <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Organização (O):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['subject.o'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Nome do país (C):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['subject.c'] + `</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="row">
          <div class="col-12">
            <p class="mx-3 mt-4 mb-2 ms-4">
              <span style="font-size: larger;">Emitido Por</span>
            </p>
            <ul class="list-group list-group-flush ms-4">
              <li class="list-group-item">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Nome comum (CN):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['issuer.cn'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item backlineon">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Unidade Organizacional (OU):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['issuer.ou'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item backlineon">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Organização (O):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['issuer.o'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Nome do país (C):</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.infra['issuer.c'] + `</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="row">
          <div class="col-12">
            <p class="mx-3 mt-4 mb-2 ms-4">
              <span style="font-size: larger;">Data de Validade</span>
            </p>
            <ul class="list-group list-group-flush ms-4">
              <li class="list-group-item">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Emitido em:</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.validation['before.date'] + `</span>
                  </div>
                </div>
              </li>
              <li class="list-group-item backlineon mb-3">
                <div class="row">
                  <div class="col-4">
                    <span class="fieldname">Expira em:</span>
                  </div>
                  <div class="col-8">
                    <span class="fieldvalue">` + certificate.validation['after.date'] + `</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div></div>`;  
  };  
  return Tester;
})();


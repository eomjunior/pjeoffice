
const Dashboard = (function() {
  function Dashboard() {}

  Dashboard.setup = function () {
    PjeOffice.about({
      "onSuccess": function(data) {
        Object.keys(data).forEach(key => {
          const dom = document.getElementById(key);
          if (dom) {
          	dom.innerHTML = data[key];              
          }
        });
      }, 
      "onUnavailable": function(statusText) {
        alert("Assinador encontra-se indisponível!");  
      }
    });          
  };  

  return Dashboard;  
})();

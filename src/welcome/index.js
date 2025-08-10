
const Welcome = (function() {
  
  function Welcome() {}
  
  Welcome.setWindow = function (page) {
    document.getElementById('viewport').src='/pjeOffice/welcome?file=' + page;
  };
  
  Welcome.setDefaults = function () {
    this.setWindow(new URLSearchParams(window.location.search).get('page') || 'dashboard/dashboard.html');
  };
  
  return Welcome;
})();
  

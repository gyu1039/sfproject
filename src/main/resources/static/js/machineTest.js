function doCheckFactory(){
    fetch("http://localhost:8080/checkFactory", {
        method: "GET"
    }).then((response) => console.log(response));
}
function doStartupFactory(){
    fetch("http://localhost:8080/startupFactory", {
        method: "GET"
    }).then((response) => console.log(response));
}
function doPauseFactory(){
    fetch("http://localhost:8080/pauseFactory", {
          method: "GET"
    }).then((response) => console.log(response));
}
function doShutdownFactory(){
    fetch("http://localhost:8080/shutdownFactory", {
         method: "GET"
    }).then((response) => console.log(response));
}
function doCheckMachine(){
    fetch("http://localhost:8080/chkMachine/0", {
          method: "GET"
    }).then((response) => console.log(response));
}
function doAddMachine(){
    fetch("http://localhost:8080/addMachine/0", {
           method: "GET"
    }).then((response) => console.log(response));
}
function doDelMachine(){
    fetch("http://localhost:8080/delMachine/0", {
            method: "GET"
    }).then((response) => console.log(response));
}
function doRunMachine(){
    fetch("http://localhost:8080/runMachine/0", {
            method: "GET"
    }).then((response) => console.log(response));
}
function doStopMachine(){
    fetch("http://localhost:8080/stopMachine/0", {
            method: "GET"
    }).then((response) => console.log(response));
}
function doCheckMcState(){
    fetch("http://localhost:8080/checkMcState/0", {
            method: "GET"
    }).then((response) => console.log(response));
}
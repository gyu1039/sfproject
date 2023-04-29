function doCheckFactory(){
    fetch("http://localhost:8080/checkFactory", {
        method: "GET"
    }).then((response) => console.log(response));
}
function doWakeStub(){
    fetch("http://localhost:8080/wakeStub", {
        method: "GET"
    }).then((response) => console.log(response));
}
function doStopStub(){
    fetch("http://localhost:8080/stopStub", {
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
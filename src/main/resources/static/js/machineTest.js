

function doCheckFactory(){
    fetch("http://localhost:8080/factory/check", {
        method: "GET"
    }).then((response) => response.json())
    .then((response)=>{
        console.log(response);
        if(response){
            alert("Connection Successful");
            window.location.reload();
        }else{
            alert("Connection Denied.\nPleas Check Factory or Contact Administrator");
        }
    });
}
function doStartupFactory(){
    fetch("http://localhost:8080/factory/startup", {
        method: "GET"
    }).then((response) => console.log(response));
}
function doPauseFactory(){
    fetch("http://localhost:8080/factory/pause", {
          method: "GET"
    }).then((response) => console.log(response));
}
function doShutdownFactory(){
    fetch("http://localhost:8080/factory/shutdown", {
         method: "GET"
    }).then((response) => console.log(response));
}
function doCheckMachine0(){
    fetch("http://localhost:8080/machine/chk/0", {
          method: "GET"
    }).then((response) => console.log(response));
}
function doAddMachine0(){
    fetch("http://localhost:8080/machine/add/0", {
           method: "GET"
    }).then((response) => console.log(response));
}
function doDelMachine0(){
    fetch("http://localhost:8080/machine/del/0", {
            method: "GET"
    }).then((response) => console.log(response));
}

function doStopMachine0(){
    fetch("http://localhost:8080/machine/stop/0", {
            method: "GET"
    }).then((response) => console.log(response));
}
function doCheckMcState0(){
    fetch("http://localhost:8080/machine/checkMcState/0", {
            method: "GET"
    }).then((response) => console.log(response));
}

function doRunMachine(mid){
    fetch("http://localhost:8080/machine/run/"+mid, {
            method: "GET"
    }).then((response) => response.json())
    .then((response) => {
        console.log(response);
        if(response){
            document.getElementById('opState').innerText=response;
        }
    });
}

function doCheckMachineAtRegistration(){
    const mid = Number(document.getElementById('mid').value);
    if(mid==NaN){
        alert("기계 번호는 숫자를 입력해주세요.");
    }else{
        fetch("http://localhost:8080/machine/chk/"+mid, {
            method: "GET"
        }).then((response) => response.json())
        .then((response)=>{
            if(response){
                document.getElementById('mcExist').innerText="장치 확인됨 : "+mid;
                document.getElementById('confirmMid').value=mid;
                document.getElementById('confirmExist').value="true";
                document.getElementById('btn_submit').disabled=false;
            }else{
                alert("그런 장치는 존재하지 않습니다.");
                document.getElementById('mcExist').innerText="";
                document.getElementById('confirmMid').value="";
                document.getElementById('confirmExist').value="false";
                document.getElementById('btn_submit').disabled=true;
            }
        });
    }
}


function doCheckFactory(){
    //alert(window.location.protocol+"//"+window.location.host+"/factory/check");
    fetch(window.location.protocol+"//"+window.location.host+"/factory/check", {
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
    fetch(window.location.protocol+"//"+window.location.host+"/factory/startup", {
        method: "GET"
    }).then((response) =>response.json())
    .then((response)=>{
        console.log(response);
         if(response){
            document.getElementById('factoryOperation').innerText="In Operation";
         }
    });
}
function doPauseFactory(){
    fetch(window.location.protocol+"//"+window.location.host+"/factory/pause", {
         method: "GET"
    }).then((response) => response.json())
    .then((response)=>{
         console.log(response);
         if(response){
            document.getElementById('factoryOperation').innerText="Paused";
         }
    });
}
function doShutdownFactory(){
    if(confirm("공장을 정지합니까?\n재가동을 위해서는 공장의 재부팅이 필요합니다."))
    {
        fetch(window.location.protocol+"//"+window.location.host+"/factory/shutdown", {
             method: "GET"
        }).then((response) => response.json())
        .then((response)=>{
            console.log(response);
            if(response){
                document.getElementById('factoryConnection').innerText="Disconnected";
                document.getElementById('factoryOperation').innerText="Stopped";
            }
        });
    }
}
function doCheckMachine0(){
    fetch(window.location.protocol+"//"+window.location.host+"/machine/check/0", {
          method: "GET"
    }).then((response) => console.log(response));
}
function doAddMachine0(){
    fetch(window.location.protocol+"//"+window.location.host+"/machine/add/0", {
           method: "GET"
    }).then((response) => console.log(response));
}
function doDelMachine0(){
    fetch(window.location.protocol+"//"+window.location.host+"/machine/del/0", {
            method: "GET"
    }).then((response) => console.log(response));
}

function doStopMachine0(){
    fetch(window.location.protocol+"//"+window.location.host+"/machine/stop/0", {
            method: "GET"
    }).then((response) => console.log(response));
}
function doCheckMcState0(){
    fetch(window.location.protocol+"//"+window.location.host+"/checkMcState/0", {
            method: "GET"
    }).then((response) => console.log(response));
}

function doRunMachine(mid){
    fetch(window.location.protocol+"//"+window.location.host+"/machine/run/"+mid, {
            method: "GET"
    }).then((response) => response.json())
    .then((response) => {
        console.log(response);
        //이제 SSE로 구현됨
        //if(response){
        //    document.getElementById('opState').innerText=response;
        //}
    });
}
//올바르게 동작하지않음. *수정됨. fetch 제거
function doDelMachine(mid){
    if(confirm("기계를 삭제합니까?\n저장 된 정보가 지워집니다."))
    {
        location.href='/machine/del/'+mid;
    }
}

function doStopMachine(mid){
    fetch(window.location.protocol+"//"+window.location.host+"/machine/stop/"+mid, {
            method: "GET"
    }).then((response) => response.json())
    .then((response) => {
         console.log(response);
    });
}

function doCheckMachineAtRegistration(){
    const machineId = Number(document.getElementById('machineId').value);
    if(machineId==NaN){
        alert("기계 번호는 숫자를 입력해주세요.");
    }else{
        fetch(window.location.protocol+"//"+window.location.host+"/machine/check/"+machineId, {
            method: "GET"
        }).then((response) => response.json())
        .then((response)=>{
            if(response){
                document.getElementById('mcExist').innerText="장치 확인됨 : "+machineId;
                document.getElementById('confirmMid').value=machineId;
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
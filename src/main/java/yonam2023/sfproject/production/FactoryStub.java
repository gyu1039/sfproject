package yonam2023.sfproject.production;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yonam2023.sfproject.production.repository.ProductionRepository;

import java.net.HttpURLConnection;;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

@Component
public class FactoryStub extends Thread {
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    public void run(){
        FactoryStub http = new FactoryStub();
        Random random = new Random();
        int nValue = 100;

        for(int i = 0; i <100; i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("STYPE", "stub");
            jsonObject.put("SVALUE", nValue);

            nValue += random.nextInt(10)-5;
            if(nValue <0) nValue = 0;
            else if (nValue>200) { nValue=200;

            }

            try{
                http.sendPost("http://localhost:8080/production/insert", jsonObject);
                System.out.println("insert occur : "+i);
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println(e.toString());
            }

        }
    }
    private void sendPost(String targetUrl, JSONObject jsonObject) throws Exception {

        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST"); // HTTP POST 메소드 설정
        con.setRequestProperty("Content-Type","application/json;utf-8");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

        // Send post request
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonObject.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println("HTTP 응답 코드 : " + responseCode);
        System.out.println("HTTP body : " + response.toString());

    }
}

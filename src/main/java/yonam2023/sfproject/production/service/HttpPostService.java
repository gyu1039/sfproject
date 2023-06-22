package yonam2023.sfproject.production.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class HttpPostService {
    private final String USER_AGENT = "Mozilla/5.0";

    private Logger logger = LoggerFactory.getLogger(HttpPostService.class);
    public String sendPost(String targetUrl, JSONObject jsonObject) throws Exception {

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
        logger.info("HttpPostService:HTTP 응답 코드:" + responseCode);
        logger.info("HttpPostService:HTTP body:" + response.toString());
        return response.toString();

    }
    public String sendGet(String targetUrl) throws Exception {

        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET"); // HTTP GET 메소드 설정
        con.setRequestProperty("Content-Type","application/json;utf-8");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // Send post request

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        logger.info("HttpPostService:HTTP 응답 코드:" + responseCode);
        logger.info("HttpPostService:HTTP body:" + response.toString());
        return response.toString();
    }
}

package com.example.melon_shake_webapp.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FastApiService {
    public Object PostRequest(Object sendData, Object returnData,String endPoint){

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(sendData);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000"+endPoint))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            Object finalReturnData = returnData;
            TypeReference<?> typeReference = new TypeReference<>() {
                @Override
                public Type getType() {
                    return finalReturnData.getClass().getGenericSuperclass();
                }
            };
            returnData = objectMapper.readValue(response.body(),typeReference);
          return returnData;
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "fail";
        }
    }
}
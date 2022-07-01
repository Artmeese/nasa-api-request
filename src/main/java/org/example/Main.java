package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static CloseableHttpResponse getResponse(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);
        return response;
    }

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpResponse response  = getResponse("https://api.nasa.gov/planetary/apod?api_key=MAmu2CASSxU0jtqjWwXC3GCqW8uupf9uVinzHIX0");
    //    System.out.println(mapper.readValue(response.getEntity().getContent(), new TypeReference<Object>() {}));
        NasaData data1 = mapper.readValue(response.getEntity().getContent(), new TypeReference<NasaData>() {});
        System.out.println(data1);
        CloseableHttpResponse response2 = getResponse(data1.getUrl());
    //    System.out.println(response2);

        String url = data1.getUrl();
        int index = url.lastIndexOf('/') + 1;
        char[] buf = new char[url.length() - index];
        url.getChars(index, url.length(), buf, 0);
        String name = new String(buf);
        System.out.println(name);

        File file = new File("D://Idea//Netology//JavaCore//", name);
        if(file.createNewFile()) {
            System.out.println("File has been created");
        }

        try (FileOutputStream fos = new FileOutputStream("D://Idea//Netology//JavaCore//" + name)) {
            InputStream bytes = (response2.getEntity().getContent());
            while (bytes.available() > 0) {
                fos.write(bytes.readAllBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
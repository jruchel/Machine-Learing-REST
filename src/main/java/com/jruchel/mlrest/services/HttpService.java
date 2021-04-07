package com.jruchel.mlrest.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class HttpService {

    public String get(String address, String endpoint, Map<String, String> pathParams, MultipartFile file) throws IOException {
        URL url = constructUrl(address, endpoint, pathParams);
        if (file != null) {
            return sendRequestWithFile(url.toString(), file);
        }
        URLConnection yc = url.openConnection();
        yc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; "
                + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;

        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();
        return sb.toString();
    }

    public String sendRequestWithFile(String url, MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resouceFile = new FileSystemResource(file.getBytes(), file.getOriginalFilename());
        body.add("data", resouceFile);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, requestEntity, String.class).getBody();
    }

    public String get(String address, String endpoint) throws IOException {
        return get(address, endpoint, new HashMap<>(), null);
    }


    private URL constructUrl(String address, String endpoint, Map<String, String> pathParams) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append(address).append(endpoint);
        if (pathParams.size() > 0) {
            sb.append("?");
            for (String key : pathParams.keySet()) {
                sb.append(key).append("=").append(pathParams.get(key)).append("&");
            }
            return new URL(sb.substring(0, sb.length() - 1));
        }
        return new URL(sb.toString());
    }

    public static class FileSystemResource extends ByteArrayResource {

        private String fileName;

        public FileSystemResource(byte[] byteArray, String filename) {
            super(byteArray);
            this.fileName = filename;
        }

        public String getFilename() {
            return fileName;
        }

        public void setFilename(String fileName) {
            this.fileName = fileName;
        }

    }
}

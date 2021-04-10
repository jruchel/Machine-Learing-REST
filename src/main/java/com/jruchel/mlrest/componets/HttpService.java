package com.jruchel.mlrest.componets;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpService {

    private final RestTemplate requests;

    public String get(String address, String endpoint, Map<String, String> pathParams, MultipartFile file, String modelFile) throws IOException, URISyntaxException {
        URL url = constructUrl(address, endpoint, pathParams);
        if (file != null) {
            return sendRequestWithFile(url.toString(), file, modelFile);
        }
        return requests.getForObject(url.toURI(), String.class);
    }

    private String sendRequestWithFile(String url, MultipartFile file, String modelFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resourceFile = new FileSystemResource(file.getBytes(), file.getOriginalFilename());
        body.add("data", resourceFile);
        body.add("modelfile", modelFile);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        return requests.postForEntity(url, requestEntity, String.class).getBody();
    }

    public String get(String address, String endpoint) throws IOException, URISyntaxException {
        return get(address, endpoint, new HashMap<>(), null, null);
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

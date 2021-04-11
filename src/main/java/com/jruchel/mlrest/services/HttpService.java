package com.jruchel.mlrest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpService {

    private final RestTemplate requests;

    public String get(String address, String endpoint, Map<String, String> headers, Map<String, String> pathParams) {
        String url = constructUrl(address, endpoint, pathParams);
        HttpEntity<String> requestEntity = new HttpEntity<>(toHeaders(headers));
        return requests.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
    }

    public <T> ResponseEntity<T> postMultipartForm(String address, String endpoint, Map<String, Object> formData, Map<String, String> headersMap, Map<String, String> params, Class<T> c) throws IOException {
        String url = constructUrl(address, endpoint, params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.addAll(toHeaders(headersMap));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(toFormBody(formData), headers);

        return requests.postForEntity(url, requestEntity, c);
    }

    private MultiValueMap<String, String> toHeaders(Map<String, String> headers) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();

        for (String key : headers.keySet()) {
            result.add(key, headers.get(key));
        }
        return result;
    }

    private MultiValueMap<String, Object> toFormBody(Map<String, Object> formData) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (String key : formData.keySet()) {
            Object value = formData.get(key);
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                Resource resourceFile = new FileSystemResource(file.getBytes(), file.getOriginalFilename());
                body.add(key, resourceFile);
            }
            else body.add(key, formData.get(key));

        }
        return body;
    }

    public String get(String address, String endpoint) {
        return get(address, endpoint, new HashMap<>(), new HashMap<>());
    }


    private String constructUrl(String address, String endpoint, Map<String, String> pathParams) {
        StringBuilder sb = new StringBuilder();
        sb.append(address).append(endpoint);
        if (pathParams.size() > 0) {
            sb.append("?");
            for (String key : pathParams.keySet()) {
                sb.append(key).append("=").append(pathParams.get(key)).append("&");
            }
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
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

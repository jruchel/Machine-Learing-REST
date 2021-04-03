package com.jruchel.mlrest.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class HttpService {

    public String get(String address, String endpoint, Map<String, String> pathParams) throws IOException {
        URL url = constructUrl(address, endpoint, pathParams);
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

    public String get(String address, String endpoint) throws IOException {
        return get(address, endpoint, new HashMap<>());
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
}

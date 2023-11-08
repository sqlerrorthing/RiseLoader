package me.oneqxz.riseloader.utils.requests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Requests {

    public static Response get(String serverUrl) throws IOException {
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        connection.setRequestProperty("riseloader-ca-qlp-cls", "KQbfEfRyHoukC3nqUu5cMZYk98D03s8cycbi9360M54RNjvpw69pYBVaSFD");

        int responseCode = connection.getResponseCode();

        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        byte[] content = byteArrayOutputStream.toByteArray();

        inputStream.close();
        byteArrayOutputStream.close();
        connection.disconnect();

        return new Response(content, responseCode);
    }



}

package me.oneqxz.riseloader.utils.requests;

import me.oneqxz.riseloader.RiseUI;

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
        setHeaders(connection);

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


    public static void setHeaders(HttpURLConnection connection)
    {
        connection.setRequestProperty("rise-0x22", "0x22/8cycbi9360M54qUu5cMZYRNjvpw69pYBVaSFDKQbfEfRyHoukC3nqUu5");
        connection.setRequestProperty("User-Agent", "0x22/" + RiseUI.version.getVersion());
    }

}

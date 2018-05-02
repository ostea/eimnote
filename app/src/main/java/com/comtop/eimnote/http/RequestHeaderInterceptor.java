package com.comtop.eimnote.http;


import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class RequestHeaderInterceptor implements Interceptor {

    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final String CHANNEL_KEY = "CQKWQzKEK37VKJZ219QZZ4w7E1BOiJto";

    /**
     * gsma-rsp-lpad or gsma-rsp-lpae
     */
    private static final String USER_AGENT = "gsma-rsp-lpad";
    private static final String ADMIN_PROTOCOL = "gsma/rsp/v2.1.0";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Charset", CHARSET.name())
                .header("User-Agent", USER_AGENT)
                .header("X-Admin-Protocol", ADMIN_PROTOCOL)
                .build();
        return chain.proceed(request);
    }

    private String getMd5String(String input) throws NoSuchAlgorithmException {
        input = input.concat(CHANNEL_KEY);
        input = input.replace("\\/", "/");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte data : byteData) {
            sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}

package com.ostapchuk.tt.hashcat.encryptor;

import com.ostapchuk.tt.hashcat.entity.Hash;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static com.ostapchuk.tt.hashcat.util.Constant.AMPERSAND;
import static com.ostapchuk.tt.hashcat.util.Constant.CODE_PARAM;
import static com.ostapchuk.tt.hashcat.util.Constant.EMAIL_PARAM;
import static com.ostapchuk.tt.hashcat.util.Constant.HASH_PARAM;
import static com.ostapchuk.tt.hashcat.util.Constant.HASH_TYPE_PARAM;
import static com.ostapchuk.tt.hashcat.util.Constant.MD5_LINK_CLIENT;
import static com.ostapchuk.tt.hashcat.util.Constant.MD_5;

@Component
@Slf4j
public class Md5Client implements Encryptor {

    @Value("${md5.auth.email}")
    private String authEmail;

    @Value("${md5.auth.code}")
    private String authCode;

    @Override
    public CompletableFuture<HttpResponse<String>> encrypt(final Hash hash) {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder(createUri(hash.getDecrypted())).build();
        log.info("Getting encryption from md5 client for: " + hash.getDecrypted());
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public URI createUri(final String decrypted) {
        return URI.create(MD5_LINK_CLIENT + HASH_PARAM + decrypted + AMPERSAND + HASH_TYPE_PARAM +
                          MD_5 + AMPERSAND + EMAIL_PARAM + authEmail + AMPERSAND + CODE_PARAM + authCode);
    }
}

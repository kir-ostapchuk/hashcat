package com.ostapchuk.tt.hashcat.encryptor;

import com.ostapchuk.tt.hashcat.entity.Hash;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface Encryptor {

    CompletableFuture<HttpResponse<String>> encrypt(final Hash hash);

    URI createUri(final String decrypted);
}

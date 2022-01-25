package com.ostapchuk.tt.hashcat.util;

import com.ostapchuk.tt.hashcat.entity.Hash;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static com.ostapchuk.tt.hashcat.util.Constant.COLON;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

@UtilityClass
public class SenderUtil {

    public static AtomicReference<String> prepareMessage(final List<CompletableFuture<Hash>> futures) {
        final AtomicReference<String> message = new AtomicReference<>(EMPTY);
        futures.forEach(cf -> cf.thenAccept(
                hash -> {
                    final var newMsg = hash.getDecrypted() + COLON + SPACE + hash.getEncrypted() +
                                       System.lineSeparator();
                    message.set(message.get() + newMsg);
                }
        ));
        return message;
    }

    public static String prepareMessageNew(final Set<Hash> hashes) {
        final List<String> preparedHashes = hashes.stream()
                                                  .map(h -> h.getDecrypted() + COLON + SPACE + h.getEncrypted())
                                                  .toList();
        return StringUtils.join(preparedHashes, System.lineSeparator());
    }
}

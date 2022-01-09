package com.ostapchuk.tt.hashcat.util;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final String EMAIL = "someEmail@email.com";
    public static final String HASH_1 = "c4ca4238a0b923820dcc509a6f75849b";
    public static final String HASH_2 = "c81e728d9d4c2f636f067f89cc14862c";
    public static final String HASH_3 = "eccbc87e4b5ce2fe28308fd9f2a7baf3";
    public static final List<String> HASHES = List.of(HASH_1, HASH_2, HASH_3);
    public static final String POST_APPLICATIONS_ENDPOINT = "/api/v1/applications";
}

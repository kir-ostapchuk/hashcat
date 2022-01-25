package com.ostapchuk.tt.hashcat.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    // Literals
    public static final String COLON = ":";

    public static final String AMPERSAND = "&";

    public static final String TRUE = "true";

    // Words & phrases
    public static final String ENCRYPTION_RESULTS = "Encryption results!";

    public static final String DECRYPTED_QUEUE = "decrypted";

    public static final String EMAILS_QUEUE = "emails";

    // URI parameters
    public static final String HASH_PARAM = "hash=";

    public static final String HASH_TYPE_PARAM = "hash_type=";

    public static final String EMAIL_PARAM = "email=";

    public static final String CODE_PARAM = "code=";

    // URI
    public static final String MD5_LINK_CLIENT = "https://md5decrypt.net/en/Api/api.php?";

    // Decoders
    public static final String MD_5 = "md5";

    public static final String ERROR_CODE_MD5_CLIENT = "ERROR CODE : 00";

    // Mail
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";

    public static final String MAIL_SMTP_PORT = "mail.smtp.port";

    public static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    // Mail ru
    public static final String SMTP_MAIL_RU = "smtp.mail.ru";

    public static final String PORT_MAIL_RU = "465";
}

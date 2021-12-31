package com.skillbox.socialnet.util;

public class Constants {

    public static final int MAX_AGE = 6733;

    public static final String EMAIL_EXISTS_MESSAGE = "email exists";
    public static final String NO_SUCH_USER_MESSAGE = "no such user";
    public static final String NO_SUCH_POST_MESSAGE = "no such post";
    public static final String NO_ANY_POST_MESSAGE = "no any post";
    public static final String WRONG_CREDENTIALS_MESSAGE = "wrong credentials";
    public static final String TOKEN_EXPIRED_MESSAGE = "Token expired";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid token";
    public static final String BAD_REQUEST_MESSAGE = "invalid_request";

    public static final String PASSWWORD_RECOVERY_SUBJECT = "Востановление пароля";
    public static final String PASSWWORD_RECOVERY_TEXT =
            "Здравствуйте,\n" +
            "Мы получили запрос на изменение пароля вашей учётной записи ZERON.\n" +
            "Если вы не отправляли его, пожалуйста, игнорируйте это письмо и продолжайте использовать текущий пароль.\n" +
            "Чтобы сбросить пароль, перейдите по следующей ссылке в течение 24 часов: \n%s\n";

    public static final String EMAIL_RECOVERY_SUBJECT = "Смена почты";
    public static final String EMAIL_RECOVERY_TEXT =
            "Здравствуйте,\n" +
            "Мы получили запрос на изменение почты в вашей учётной записи ZERON.\n" +
            "Если вы не отправляли его, пожалуйста, игнорируйте это письмо.\n" +
            "Чтобы изменить вашу почту, перейдите по следующей ссылке в течение 24 часов: \n%s\n";

    public static final String API_ACCOUNT = "/api/v1/account";
    public static final String API_PLATFORM = "/api/v1/platform";
    public static final int RECOMMENDED_FRIENDS_LIMIT = 10;
    public static final int RECOMMENDED_POST_LIMIT = 10;

}

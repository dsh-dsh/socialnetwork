package com.skillbox.socialnet.util;

public class Constants {
    private Constants() {
    }

    public static final String ALLOW_ORIGIN = "http://localhost:8080";

    public static final int MAX_AGE = 6733;
    public static final int RECOMMENDED_FRIENDS_LIMIT = 15;
    public static final String EXPIRATION_PREFIX = "E";
    public static final String STRING = "string";

    public static final String EMAIL_EXISTS_MESSAGE = "email exists";
    public static final String NO_SUCH_USER_MESSAGE = "no such user";
    public static final String NO_SUCH_POST_MESSAGE = "no such post";
    public static final String NO_SUCH_COMMENT_MESSAGE = "no such comment";
    public static final String NO_SUCH_FRIENDSHIP_MESSAGE = "no such friendship relation";
    public static final String NO_ANY_POST_MESSAGE = "no any post";
    public static final String WRONG_CREDENTIALS_MESSAGE = "wrong credentials";
    public static final String BAD_REQUEST_MESSAGE = "invalid_request";

    public static final String PASSWORD_NOT_VALID_MESSAGE = "Length of password has to be 8";
    public static final String WRONG_FIRST_NAME_MESSAGE = "Wrong first name string";
    public static final String WRONG_LAST_NAME_MESSAGE = "Wrong last name string";
    public static final String NOT_VALID_EMAIL_MESSAGE = "not a well-formed email";
    public static final String NOT_VALID_PHONE_NUMBER_MESSAGE = "not a well-formed phone number";
    public static final String NOT_VALID_BIRTHDAY_MESSAGE = "birthday must be a past date";
    public static final String NOT_VALID_SETTING_TYPE_MESSAGE = "Notification setting type must be valid";
    public static final String BLANK_COMMENT_MESSAGE = "Comment text must not be blank";
    public static final String NOT_VALID_TITLE_MESSAGE = "Title must more then 3 length";
    public static final String NOT_VALID_LOCAL_MESSAGE = "Location title must more then 3 length";
    public static final String NOT_VALID_TEXT_MESSAGE = "Text must more then 15 length";

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

    public static final String RECOVERING_CODE_EXPIRED = "Recovering code expired";
    public static final String WRONG_RECOVERING_CODE = "Wrong recovering code";

    public static final String USER_DELETE_SUCCESS = "User deleted successfully";
    public static final String USER_RECOVER_SUCCESS = "User recovered successfully";
}

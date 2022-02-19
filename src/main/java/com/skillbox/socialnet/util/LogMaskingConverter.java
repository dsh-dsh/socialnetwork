package com.skillbox.socialnet.util;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name="LogMaskingConverter", category = "Converter")
@ConverterKeys("maskedMessage")
public class LogMaskingConverter extends LogEventPatternConverter {

    private static final String EMAIL_REGEX = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String EMAIL_REPLACEMENT = "***@******.***";

    private static final String PHONE_REGEX = "=[0-9]{11},";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final String PHONE_REPLACEMENT = "=***********,";

    protected LogMaskingConverter(String name, String style) {
        super(name, style);
    }

    public static LogMaskingConverter newInstance() {
        return new LogMaskingConverter("maskedMessage", Thread.currentThread().getName());
    }

    @Override
    public void format(LogEvent event, StringBuilder outputMessage) {

        String message = event.getMessage().getFormattedMessage();
        String maskedMessage;
        try {
            maskedMessage = mask(message);
        } catch (Exception e) {
            maskedMessage = message;
        }
        outputMessage.append(maskedMessage);

    }

    private String mask(String message) {

        Matcher matcher;
        StringBuffer buffer = new StringBuffer();

        matcher = PHONE_PATTERN.matcher(message);
        maskMatcher(matcher, buffer,PHONE_REPLACEMENT);
        message=buffer.toString();
        buffer.setLength(0);

        matcher = EMAIL_PATTERN.matcher(message);
        maskMatcher(matcher, buffer, EMAIL_REPLACEMENT);

        return buffer.toString();
    }

    private void maskMatcher(Matcher matcher, StringBuffer buffer, String maskStr)
    {
        while (matcher.find()) {
            matcher.appendReplacement(buffer, maskStr);
        }
        matcher.appendTail(buffer);
    }
}

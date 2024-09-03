package com.c2w;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    public static final String ACCOUNT_SID = "AC312f69f816ac9c8be5f8f24fffa41203";
    public static final String AUTH_TOKEN = "4f3865206833cb1942f1f8257ce58e33";
    public static final String TWILIO_PHONE_NUMBER = "+15076232533";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String to, String messageBody) throws TwilioException {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),  // To number
                    new PhoneNumber(TWILIO_PHONE_NUMBER),  // From number
                    messageBody)
                .create();
            System.out.println("SMS sent successfully: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            e.printStackTrace();
            throw new TwilioException("Failed to send SMS", e);
        }
    }
}

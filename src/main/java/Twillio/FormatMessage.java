package Twillio;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;

public class FormatMessage {

    /**
     * Process the string received as a message, and creates a new XML response, with the
     * response as the body of the XML, to be sent back to Twillio server.
     * @param response the body of the message
     * @return String representing the XML format of the message.
     */
    public static String toXml(String response) {

        // Default template to follow to send a message back to the user on WhatsApp
        Body body = new Body
                .Builder(response)
                .build();
        com.twilio.twiml.messaging.Message sms = new com.twilio.twiml.messaging.Message
                .Builder()
                .body(body)
                .build();
        MessagingResponse twiml = new MessagingResponse
                .Builder()
                .message(sms)
                .build();
        return twiml.toXml();
    }
}

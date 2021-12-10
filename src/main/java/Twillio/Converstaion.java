package Twillio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.util.Map;

/**
 * Manages the state of the bot. Keeps track of the next trade the bot wants to execute, and takes permission
 * from the user to exceute such a trade.
 */
public class Converstaion {

    private Map<String, Boolean> toExecuteNext;

    public Converstaion() {

    }

    /**
     * Sends a simple message to the user.
     * @param message the message to send.
     */
    public static void sendMessage(String message) {
        Twilio.init(System.getenv("ACCOUNT_SID"), System.getenv("AUTH_TOKEN"));
        Message m = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:+12063692068"),
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                        message)
                .create();
    }

    /**
     * Waits for approval from the user to execute the trade as specified in the map toExecute.
     * @param toExecute Map representing the tickers of the stock to trade, and if to sell or buy.
     * @throws IllegalStateException if there is already a task to be executed.
     */
    public void waitForApproval(Map<String, Boolean> toExecute) {
        if (toExecuteNext != null) {
            throw new IllegalStateException("A task to be executed is already lined up.");
        }
        toExecuteNext = toExecute;
    }

    /**
     * Checks if there is a task that is waiting for an approval.
     * @return
     */
    public boolean isWaiting() {
        return toExecuteNext != null;
    }

    /**
     * Changes the state of the class to approved, which results in the task provided to be excecuted.
     */
    public void approved() {
        if (toExecuteNext == null) {
            throw new IllegalStateException("The command to execute does not exist");
        }

        sendMessage("Executed all of the following: \n " + toExecuteNext.toString());
        toExecuteNext = null;
    }
}

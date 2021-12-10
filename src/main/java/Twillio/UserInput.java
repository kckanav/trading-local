package Twillio;

import java.util.Locale;

public class UserInput {

    public static String process(String body, Converstaion con) {
        String result = "";
        String[] allCommands = body.split(" ");
        switch (allCommands[0].toLowerCase(Locale.ROOT)) {
            case "approve":
                if (con.isWaiting()) {
                    con.approved();
                    result = "Thank you for approving";
                } else {
                    result = "No current appprovals needed";
                }
                break;
            case "sell":
                // TODO: Sell the stock
                break;
            case "buy":
                // TODO: Buy the stock
                break;
        }
        return result;
    }
}

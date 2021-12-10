//package util;
//
//import FileIO.StateGenerator;
//import Testing.Strategies.StratergyTestingFramework;
//import Trade.RunMeanReversion;
//import net.jacobpeterson.alpaca.AlpacaAPI;
//import net.jacobpeterson.alpaca.model.endpoint.asset.enums.AssetStatus;
//import net.jacobpeterson.alpaca.rest.AlpacaClientException;
//import spark.Spark;
//import Twillio.Converstaion;
//import Twillio.FormatMessage;
//import Twillio.UserInput;
//
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//
//public class Server {
//
//    private static final AlpacaAPI api = LoginAlpaca.getLogin();
//
//    public static void main(String[] args) throws AlpacaClientException {
//
//        CORSFilter corsFilter = new CORSFilter();
//        corsFilter.apply();
//        Converstaion con = new Converstaion();
//        RunMeanReversion r = new RunMeanReversion();
//        StateGenerator g = new StateGenerator(ZonedDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
//        r.setup(api.assets().get(AssetStatus.ACTIVE, "us_equity"), g);
//
//        /**
//         * This endpoint is to communicate with the server using Twillio.
//         */
//        Spark.post("/sms", (req, res) -> {
//            res.type("application/xml");
//            String command = req.queryParams("Body");
//
//            r.setup(api.assets().get(AssetStatus.ACTIVE, "us_equity"), g);
//            StateGenerator.genTextFile(ZonedDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
//
//            String result = UserInput.process(command, con);
//
//            return FormatMessage.toXml(result);
//        });
//
//
//        /**
//         * This endpoint is to provide user with constant updates about the current
//         * strategy being implemented.
//         */
//        Spark.post("/update", (request, response) -> {
//            response.type("application/text");
//            Map<String, Boolean> curr = alert.shouldNotify();
//            if (curr.isEmpty()) {
//                return FormatMessage.toXml("No trades");
//            }
//            Converstaion.sendMessage(curr.toString());
//            con.waitForApproval(curr);
//            return curr.toString();
//        });
//    }
//}

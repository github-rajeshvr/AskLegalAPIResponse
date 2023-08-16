import java.io.IOException;
import java.text.ParseException;

import APIInvoke.APIInvoker;
import Configuration.ExecutionManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Launcher {
    private static final Logger logger = LogManager.getLogger(Launcher.class);
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
    try {
        logger.info("*********AskLegal Auditlogs and Reviews*********");
        logger.info("STARTED- AskLegal API Connector");
        ExecutionManager _executionManager = new ExecutionManager();
        String API_Date_Params = _executionManager.getAPIParametersFromConfig();
        String startDate="";
        String endDate="";
        String securityCode="";
        String libraryId="";
        String viewId="";
        /*

        if(API_Date_Params.contains("&")){
            String[]dates= API_Date_Params.split("&",2 );
            startDate=dates[0];
            endDate=dates[1];
            logger.info("Retrieved date param information from the configuration file");
        }
        else{
            logger.warn("Unable to process date information from Configuration file. Quiting the execution");
        }

         */

        startDate=_executionManager.startDate;
        endDate=_executionManager.endDate;
        securityCode=_executionManager.securityCode;
        libraryId=_executionManager.libraryId;
        viewId=_executionManager.viewId;

        //logger.info("START DATE="+startDate);
        //logger.info("END DATE="+endDate);

        new APIInvoker(securityCode,libraryId,viewId,startDate,endDate).InvokeAPICall();
        logger.info("Audit logs and Reviews info has been retrieved and stored as JSON file");
        logger.info("Setting the last execution date for the reference of next iteration");
        _executionManager.setLastExecutionDate();
        logger.info("COMPLETED- AskLegal API Connector");
    }
    catch (Exception e){
        logger.error(e.getMessage());
    }
    }
}

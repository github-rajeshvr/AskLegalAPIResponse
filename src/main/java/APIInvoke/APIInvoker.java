package APIInvoke;
import AODocsAPIHandlers.APIHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APIInvoker {
    private static final Logger logger = LogManager.getLogger(APIInvoker.class);

    String securityCode="";
    String libraryId="";
    String viewId="";
    String minDate;
    String maxDate;

    public APIInvoker(String _securityCode, String _libraryId, String _viewId, String _minDate, String _maxDate){
        this.securityCode=_securityCode;
        this.libraryId=_libraryId;
        this.viewId=_viewId;
        this.minDate=_minDate;
        this.maxDate=_maxDate;

    }
    public void InvokeAPICall(){
        try {
            logger.info("Begins to invoke API Calls");
            String workingDirectory= "C:/AskLegalJSONResponses";
            Path path = Paths.get(workingDirectory);
            if(!Files.exists(path)) {
                if(new File(workingDirectory).mkdir())
                    logger.info("Folder for JSON response created at "+workingDirectory);
                else
                    logger.info("Couldn't create the folder at "+workingDirectory);
            }
            else
                logger.info("Folder already existing at "+workingDirectory);

            String objectName =  "";
            String fileName = "";

            APIHandler aodocsApiHandler = new APIHandler();

            logger.info("About to start fetching Auditlog");
            objectName =  new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.json'").format(new Date());
            objectName = "DOC_STATE_CHANGED_"+objectName;
            fileName = workingDirectory+"/"+objectName;
            aodocsApiHandler.getAuditLogs(this.securityCode,this.libraryId,fileName,this.minDate,this.maxDate);
            logger.info("Auditlog fetched");

            Thread.sleep(100);
            //}

            logger.info("Preparing to fetch Reviews");
            objectName =  new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.json'").format(new Date());
            objectName = "AL_REVIEWS_"+objectName;
            fileName = workingDirectory+"/"+objectName;
            aodocsApiHandler.getReviewData(this.securityCode,this.libraryId,this.viewId, fileName);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}

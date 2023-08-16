package Configuration;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ExecutionManager {
   public String startDate="";
   public String endDate="";

   public String libraryId="";
   public String securityCode="";
   public String viewId="";

    private static final Logger logger = LogManager.getLogger(ExecutionManager.class);
    public String getAPIParametersFromConfig(){
        boolean IsLastDateAvailable=false;
        ConfigurationManager configManager = new ConfigurationManager();
        configManager.getAPIParametersFromConfig();
        String lastExecutionDate = configManager.lastExecutionDate;

        this.securityCode=configManager.securityCode;
        this.libraryId=configManager.libraryId;
        this.viewId=configManager.viewId;

        String defaultStartDate="";
        if(lastExecutionDate.contains("&")){
            String[] dates = lastExecutionDate.split("&",2 );
            IsLastDateAvailable=false;
            defaultStartDate = dates[0];
        }
        else
            IsLastDateAvailable=true;
     return getDateParamsForAuditLogAPI(lastExecutionDate,defaultStartDate,IsLastDateAvailable);
    }

    private String getDateParamsForAuditLogAPI(String lastExecutionDate,String defaultStartDateMilliSec,boolean isLastExecutionDateAvailable){
        Date today;
        Date previousExecutionDate;
        Date maxDate;
        Date minDate;
        String startDateMilliSec;
        String endDateMilliSec;
        String startAndEndDate_MilliSec="";
        try{
            Calendar defaultCalendar = Calendar.getInstance();
            today = defaultCalendar.getTime();
            //defaultCalendar.set(Calendar.DATE,-1);
            defaultCalendar.set(Calendar.HOUR_OF_DAY, 23);
            defaultCalendar.set(Calendar.MINUTE, 59);
            defaultCalendar.set(Calendar.SECOND, 0);
            defaultCalendar.add(Calendar.DAY_OF_MONTH,-1);
            maxDate=defaultCalendar.getTime();
            endDateMilliSec = maxDate.getTime()+"";
            logger.info("End/Max Date param of Auditlog API=>"+maxDate);
            if(isLastExecutionDateAvailable) {
                logger.info("Generating the Start and End date params with the previous Execution Date=>" + lastExecutionDate);
                defaultCalendar.setTimeInMillis(Long.parseLong(lastExecutionDate));
                previousExecutionDate = defaultCalendar.getTime();
                //setting the start date as 2 days prior to last execution date
                //defaultCalendar.set(Calendar.DATE,-1);
                //defaultCalendar.set(Calendar.HOUR_OF_DAY, 23);
                //defaultCalendar.set(Calendar.MINUTE, 59);
                //defaultCalendar.set(Calendar.SECOND, 0);
                defaultCalendar.add(Calendar.DAY_OF_MONTH,-1);
                minDate=defaultCalendar.getTime();
                logger.info("Start/Min Date param of Auditlog API=>"+minDate);

                startDateMilliSec = defaultCalendar.getTimeInMillis()+"";
/*
                long timeGap = today.getTime()-previousExecutionDate.getTime();

                long difference_In_Days
                        = (timeGap
                        / (1000 * 60 * 60 * 24))
                        % 365;

                //checks the time gap with the last execution date
                if(difference_In_Days>1)

 */
            }
            else {
                logger.info("Retrieving Audit logs from the default start date");
                Calendar _cal = Calendar.getInstance();
                _cal.setTimeInMillis(Long.parseLong(defaultStartDateMilliSec));
                logger.info("Default Start/Min date for audit log API is=>"+_cal.getTime());
                startDateMilliSec = defaultStartDateMilliSec;
            }
            startAndEndDate_MilliSec = startDateMilliSec+"&"+endDateMilliSec;
            this.startDate=startDateMilliSec;
            this.endDate=endDateMilliSec;
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return startAndEndDate_MilliSec;
    }

    public void setLastExecutionDate(){
        try{
            ConfigurationManager _xml = new ConfigurationManager();
            _xml.updateConfigFileWithLastExecutionDate();
            logger.info("Last execution date updated");
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}

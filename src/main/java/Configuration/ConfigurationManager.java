package Configuration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ConfigurationManager {
    private static final Logger logger = LogManager.getLogger(ConfigurationManager.class);
    public String lastExecutionDate="";
    public String securityCode="";
    public String libraryId="";
    public String viewId="";

    public void getAPIParametersFromConfig(){
        String last_execution_date="";
        try{
            logger.info("Reading the configuration XML to retrieve the date information");
            String filePath="D:/Java Projects/AskLegalAPIResponseToJSON/src/main/resources/ExecutionInfo.xml";
            File file  = new File(filePath);
            // Make an  instance of the DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("apiinvokedate");

            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                logger.info("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    last_execution_date =eElement.getElementsByTagName("last_execution_date").item(0).getTextContent();
                    //checks whether last execution date is AVAILABLE or NOT
                    if(last_execution_date==null||last_execution_date.equals("")){
                        logger.info("No entry of last execution date in the XML Config file. Auditlogs will fetch from default start date");
                        String defaultStartDate = eElement.getElementsByTagName("default_start_date").item(0).getTextContent();
                        Calendar defaultCalendar = Calendar.getInstance();
                        defaultCalendar.setTimeInMillis(Long.parseLong(defaultStartDate));
                        logger.info("Start date=>"+defaultCalendar.getTime());
                        this.lastExecutionDate=defaultStartDate+"&"+"DEFAULT";
                    }
                    else
                        this.lastExecutionDate= last_execution_date;

                    this.securityCode =eElement.getElementsByTagName("security_code").item(0).getTextContent();
                    this.libraryId =eElement.getElementsByTagName("library_id").item(0).getTextContent();
                    this.viewId =eElement.getElementsByTagName("view_id").item(0).getTextContent();
                }
            }
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        //return last_execution_date;
    }

    public boolean updateConfigFileWithLastExecutionDate(){
        try {
            String filepath = "D:/Java Projects/AskLegalAPIResponseToJSON/src/main/resources/ExecutionInfo.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node data= doc.getFirstChild();

            Node last_execution_date = doc.getElementsByTagName("last_execution_date").item(0);

            last_execution_date.setTextContent(Calendar.getInstance().getTimeInMillis()+"");

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            logger.info("Last execution date updated");

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return true;
    }

    public void getAPIBasicParameters(){
        try{

        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}

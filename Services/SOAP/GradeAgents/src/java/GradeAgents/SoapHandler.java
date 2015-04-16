package GradeAgents;

import java.io.ByteArrayOutputStream;
import java.util.Set;
 
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
 
public class SoapHandler implements SOAPHandler<SOAPMessageContext> {
 
    public boolean handleMessage(SOAPMessageContext smc) {
 
        Boolean outboundProperty = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
 
        SOAPMessage message = smc.getMessage();
 
        if (outboundProperty.booleanValue()) {
            System.out.println(" SOAP Request ");
        } else {
            System.out.println(" SOAP Respone ");
        }
        System.out.println(getSOAPMessageAsString(message));
 
        return outboundProperty;
 
    }
 
    public Set getHeaders() {
        return null;
    }
    
public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
      try {

         TransformerFactory tff = TransformerFactory.newInstance();
         Transformer tf = tff.newTransformer();

         // Set formatting
        
         tf.setOutputProperty(OutputKeys.INDENT, "yes");
         tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
               "2");
         
         Source sc = soapMessage.getSOAPPart().getContent();

         ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
         StreamResult result = new StreamResult(streamOut);
         tf.transform(sc, result);

         String strMessage = streamOut.toString();
         return strMessage;
      } catch (Exception e) {
         System.out.println("Exception in getSOAPMessageAsString "
               + e.getMessage());
         return null;
      }

   }
    


    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }
 

    public void close(MessageContext context) {
    }
}


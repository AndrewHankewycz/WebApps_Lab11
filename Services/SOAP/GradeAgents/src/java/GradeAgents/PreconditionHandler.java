/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GradeAgents;

import iGradeClients.ToHundredScale;
import iGradeClients.ToLetterScale;
import iGradeClients.ToSevenScale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author andrew
 */
public class PreconditionHandler implements LogicalHandler<LogicalMessageContext> {
    
    public static final String SEVEN_SCALE = "7-scale";
    public static final String HUNDRED_SCALE = "100-scale";
    public static final String LETTER_SCALE = "letter-grade";

    public void close(MessageContext mctx) {
    }

    public boolean handleFault(LogicalMessageContext lmctx) {
        return true;
    }

    public boolean handleMessage(LogicalMessageContext lmctx) {
        String gradeResult = "";
        Boolean outbound
                = (Boolean) lmctx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) { // request?
            LogicalMessage msg = lmctx.getMessage();
            System.out.println("@@@ID Handler filtered");
            try {
                JAXBContext jaxbCtx = JAXBContext.newInstance("iGradeClients");
                Object payload = msg.getPayload(jaxbCtx);
                // Check payload to be sure it's what we want.
                if (payload instanceof JAXBElement) {
                    Object value = ((JAXBElement) payload).getValue();
                    // Three possibilities of interest: GetOne, Edit, or Delete
                    //int id = 0;
                    String gradeInput = "";
                    String fromInput = "";
                    
                    boolean hundredScale, letterScale, sevenScale;
                    hundredScale = letterScale = sevenScale = false;
                    
                    if (value.toString().contains("ToHundredScale")) {
                        gradeInput = ((ToHundredScale) value).getArg0();
                        fromInput = ((ToHundredScale) value).getArg1();
                        
                        switch (gradeInput) {
                            case "7":   case "A":   gradeResult = "93-100"; break;
                            case "6.5": case "A-":  gradeResult = "89-92"; break;
                            case "6":   case "B+":  gradeResult = "85-88"; break;
                            case "5.8": case "B":   gradeResult = "80-84"; break;
                            case "5.5": case "B-":  gradeResult = "75-79"; break;
                            case "5":   case "C+":  gradeResult = "70-74"; break;
                            case "4.8": case "C":   gradeResult = "65-69"; break;
                            case "4.5": case "D":   gradeResult = "60-64"; break;
                            case "4":   case "F":   gradeResult = "0-59"; break;
                            default:
                                sendSOAPFaultException(payload, "SOAPFaultException", jaxbCtx, msg);
                                return true;
                        }
                        
                        hundredScale = true;
                    } else if (value.toString().contains("ToLetterScale")) {
                        gradeInput = ((ToLetterScale) value).getArg0();
                        fromInput = ((ToLetterScale) value).getArg1();
                        
                        if(fromInput.equals(HUNDRED_SCALE)) {
                            int numGrade = Integer.parseInt(gradeInput);
                            
                            if(numGrade >= 93)
                                gradeResult = "A";
                            else if(numGrade <= 92 && numGrade >= 89)
                                gradeResult = "A-";
                            else if(numGrade <= 88 && numGrade >= 85)
                                gradeResult = "B+";
                            else if(numGrade <= 84 && numGrade >= 80)
                                gradeResult = "B";
                            else if(numGrade <= 79 && numGrade >= 75)
                                gradeResult = "B-";
                            else if(numGrade <= 74 && numGrade >= 70)
                                gradeResult = "C+";
                            else if(numGrade <= 69 && numGrade >= 65)
                                gradeResult = "C";
                            else if(numGrade <= 64 && numGrade >= 60)
                                gradeResult = "D";
                            else if(numGrade <= 59)
                                gradeResult = "F";
                        } else if(fromInput.equals(SEVEN_SCALE)) {
                            switch (gradeInput) {
                                case "7":   gradeResult = "A"; break;
                                case "6.5": gradeResult = "A-"; break;
                                case "6":   gradeResult = "B+"; break;
                                case "5.8": gradeResult = "B"; break;
                                case "5.5": gradeResult = "B-"; break;
                                case "5":   gradeResult =  "C+"; break;
                                case "4.8": gradeResult = "C"; break;
                                case "4.5": gradeResult =  "D"; break;
                                case "4":   gradeResult = "F"; break;
                                default:
                                    sendSOAPFaultException(payload, "SOAPFaultException", jaxbCtx, msg);
                                    return true;
                            }
                        }
                        
                        letterScale = true;
                    } else if (value.toString().contains("ToSevenScale")) {
                        gradeInput = ((ToSevenScale) value).getArg0();
                        fromInput = ((ToSevenScale) value).getArg1();
                        
                        
                        if(fromInput.equals(HUNDRED_SCALE)) {
                            int numGrade = Integer.parseInt(gradeInput);

                            if(numGrade >= 93)
                                gradeResult =  "7";
                            else if(numGrade <= 92 && numGrade >= 89)
                                gradeResult =  "6.5";
                            else if(numGrade <= 88 && numGrade >= 85)
                                gradeResult =  "6";
                            else if(numGrade <= 84 && numGrade >= 80)
                                gradeResult =  "5.8";
                            else if(numGrade <= 79 && numGrade >= 75)
                                gradeResult =  "5.5";
                            else if(numGrade <= 74 && numGrade >= 70)
                                gradeResult =  "5";
                            else if(numGrade <= 69 && numGrade >= 65)
                                gradeResult =  "4.8";
                            else if(numGrade <= 64 && numGrade >= 60)
                                gradeResult =  "4.5";
                            else if(numGrade <= 59)
                                gradeResult =  "4";
                            else {
                                sendSOAPFaultException(payload, "SOAPFaultException", jaxbCtx, msg);
                                return true;
                            }
                        } else if(fromInput.equals(LETTER_SCALE)) {
                            switch (gradeInput) {
                                case "A":   gradeResult =  "7"; break;
                                case "A-":  gradeResult =  "6.5"; break;
                                case "B+":  gradeResult =  "6"; break;
                                case "B":   gradeResult =  "5.8"; break;
                                case "B-":  gradeResult =  "5.5"; break;
                                case "C+":  gradeResult =  "5"; break;
                                case "C":   gradeResult =  "4.8"; break;
                                case "D":   gradeResult =  "4.5"; break;
                                case "F":   gradeResult =  "4"; break;
                                default:
                                    sendSOAPFaultException(payload, "SOAPFaultException", jaxbCtx, msg);
                                    return true;
                            }
                        }
                        sevenScale = true;
                    } else {
                        return true; // GetAll or Create
                    }// If id > 0, there is no problem to fix on the client side.
                    if (gradeInput == "") {
                        sendSOAPFaultException(payload, "SOAPFaultException", jaxbCtx, msg);
                        return true;
                    }
                    // If the request is GetOne, Edit, or Delete and the id is zero,
                    // there is a problem that cannot be fixed.
                    if (hundredScale || letterScale || sevenScale) {
                        if (gradeResult == "") // can't fix
                        {
                            sendSOAPFaultException(payload, "SOAPFaultException", jaxbCtx, msg);
                            return true;
                        }

                        // Update payload.
                        ((JAXBElement) payload).setValue(value);
                        // Update message
                        msg.setPayload(payload, jaxbCtx);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            
        }
        
        
        return true;
    }
    
    private void sendSOAPFaultException(Object payload, Object value, JAXBContext jaxbCtx, LogicalMessage msg) {
        value = "SOAPFaultException";

        // Update payload.
        ((JAXBElement) payload).setValue(value);
        // Update message
        msg.setPayload(value, jaxbCtx);
    }
}

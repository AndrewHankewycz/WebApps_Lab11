package iGrade;

import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService
public class iGrade {
    public static final String SEVEN_SCALE = "7-scale";
    public static final String HUNDRED_SCALE = "100-scale";
    public static final String LETTER_SCALE = "letter-grade";
    
    @WebMethod
    public String toHundredScale(String grade, String unit) {
        switch (grade) {
            case "7":   case "A":   return "93-100";
            case "6.5": case "A-":  return "89-92";
            case "6":   case "B+":  return "85-88";
            case "5.8": case "B":   return "80-84";
            case "5.5": case "B-":  return "75-79";
            case "5":   case "C+":  return "70-74";
            case "4.8": case "C":   return "65-69";
            case "4.5": case "D":   return "60-64";
            case "4":   case "F":   return "0-59";
        }
        return "-1";
    }
    
    @WebMethod
    public String toSevenScale(String grade, String unit) {
        if(unit.equals(HUNDRED_SCALE)) {
            int numGrade = Integer.parseInt(grade);
            
            if(numGrade >= 93)
                return "7";
            else if(numGrade <= 92 && numGrade >= 89)
                return "6.5";
            else if(numGrade <= 88 && numGrade >= 85)
                return "6";
            else if(numGrade <= 84 && numGrade >= 80)
                return "5.8";
            else if(numGrade <= 79 && numGrade >= 75)
                return "5.5";
            else if(numGrade <= 74 && numGrade >= 70)
                return "5";
            else if(numGrade <= 69 && numGrade >= 65)
                return "4.8";
            else if(numGrade <= 64 && numGrade >= 60)
                return "4.5";
            else if(numGrade <= 59)
                return "4";
        } else if(unit.equals(LETTER_SCALE)) {
            switch (grade) {
                case "A":   return "7";
                case "A-":  return "6.5";
                case "B+":  return "6";
                case "B":   return "5.8";
                case "B-":  return "5.5";
                case "C+":  return "5";
                case "C":   return "4.8";
                case "D":   return "4.5";
                case "F":   return "4";
            }
        }
        
        return "-1";
    }
    
    @WebMethod
    public String toLetterScale(String grade, String unit) {
        if(unit.equals(HUNDRED_SCALE)) {
            int numGrade = Integer.parseInt(grade);
            
            if(numGrade >= 93)
                return "A";
            else if(numGrade <= 92 && numGrade >= 89)
                return "A-";
            else if(numGrade <= 88 && numGrade >= 85)
                return "B+";
            else if(numGrade <= 84 && numGrade >= 80)
                return "B";
            else if(numGrade <= 79 && numGrade >= 75)
                return "B-";
            else if(numGrade <= 74 && numGrade >= 70)
                return "C+";
            else if(numGrade <= 69 && numGrade >= 65)
                return "C";
            else if(numGrade <= 64 && numGrade >= 60)
                return "D";
            else if(numGrade <= 59)
                return "F";
        } else if(unit.equals(SEVEN_SCALE)) {
            switch (grade) {
                case "7":   return "A";
                case "6.5": return "A-";
                case "6":   return "B+";
                case "5.8": return "B";
                case "5.5": return "B-";
                case "5":   return "C+";
                case "4.8": return "C";
                case "4.5": return "D";
                case "4":   return "F";
            }
        }
        
        return "-1";
    }
}

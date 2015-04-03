
package iGradeClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the iGradeClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ToLetterScale_QNAME = new QName("http://iGrade/", "toLetterScale");
    private final static QName _ToSevenScale_QNAME = new QName("http://iGrade/", "toSevenScale");
    private final static QName _ToHundredScale_QNAME = new QName("http://iGrade/", "toHundredScale");
    private final static QName _ToLetterScaleResponse_QNAME = new QName("http://iGrade/", "toLetterScaleResponse");
    private final static QName _ToSevenScaleResponse_QNAME = new QName("http://iGrade/", "toSevenScaleResponse");
    private final static QName _ToHundredScaleResponse_QNAME = new QName("http://iGrade/", "toHundredScaleResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: iGradeClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ToHundredScale }
     * 
     */
    public ToHundredScale createToHundredScale() {
        return new ToHundredScale();
    }

    /**
     * Create an instance of {@link ToLetterScale }
     * 
     */
    public ToLetterScale createToLetterScale() {
        return new ToLetterScale();
    }

    /**
     * Create an instance of {@link ToSevenScale }
     * 
     */
    public ToSevenScale createToSevenScale() {
        return new ToSevenScale();
    }

    /**
     * Create an instance of {@link ToSevenScaleResponse }
     * 
     */
    public ToSevenScaleResponse createToSevenScaleResponse() {
        return new ToSevenScaleResponse();
    }

    /**
     * Create an instance of {@link ToHundredScaleResponse }
     * 
     */
    public ToHundredScaleResponse createToHundredScaleResponse() {
        return new ToHundredScaleResponse();
    }

    /**
     * Create an instance of {@link ToLetterScaleResponse }
     * 
     */
    public ToLetterScaleResponse createToLetterScaleResponse() {
        return new ToLetterScaleResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToLetterScale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://iGrade/", name = "toLetterScale")
    public JAXBElement<ToLetterScale> createToLetterScale(ToLetterScale value) {
        return new JAXBElement<ToLetterScale>(_ToLetterScale_QNAME, ToLetterScale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToSevenScale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://iGrade/", name = "toSevenScale")
    public JAXBElement<ToSevenScale> createToSevenScale(ToSevenScale value) {
        return new JAXBElement<ToSevenScale>(_ToSevenScale_QNAME, ToSevenScale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToHundredScale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://iGrade/", name = "toHundredScale")
    public JAXBElement<ToHundredScale> createToHundredScale(ToHundredScale value) {
        return new JAXBElement<ToHundredScale>(_ToHundredScale_QNAME, ToHundredScale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToLetterScaleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://iGrade/", name = "toLetterScaleResponse")
    public JAXBElement<ToLetterScaleResponse> createToLetterScaleResponse(ToLetterScaleResponse value) {
        return new JAXBElement<ToLetterScaleResponse>(_ToLetterScaleResponse_QNAME, ToLetterScaleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToSevenScaleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://iGrade/", name = "toSevenScaleResponse")
    public JAXBElement<ToSevenScaleResponse> createToSevenScaleResponse(ToSevenScaleResponse value) {
        return new JAXBElement<ToSevenScaleResponse>(_ToSevenScaleResponse_QNAME, ToSevenScaleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToHundredScaleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://iGrade/", name = "toHundredScaleResponse")
    public JAXBElement<ToHundredScaleResponse> createToHundredScaleResponse(ToHundredScaleResponse value) {
        return new JAXBElement<ToHundredScaleResponse>(_ToHundredScaleResponse_QNAME, ToHundredScaleResponse.class, null, value);
    }

}

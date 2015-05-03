
package UserManagerClients;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the UserManagerClients package. 
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

    private final static QName _UserLogin_QNAME = new QName("http://Controller/", "userLogin");
    private final static QName _RegisterUserResponse_QNAME = new QName("http://Controller/", "registerUserResponse");
    private final static QName _UserLogout_QNAME = new QName("http://Controller/", "userLogout");
    private final static QName _UserLogoutResponse_QNAME = new QName("http://Controller/", "userLogoutResponse");
    private final static QName _UserLoginResponse_QNAME = new QName("http://Controller/", "userLoginResponse");
    private final static QName _RegisterUser_QNAME = new QName("http://Controller/", "registerUser");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: UserManagerClients
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UserLogin }
     * 
     */
    public UserLogin createUserLogin() {
        return new UserLogin();
    }

    /**
     * Create an instance of {@link RegisterUser }
     * 
     */
    public RegisterUser createRegisterUser() {
        return new RegisterUser();
    }

    /**
     * Create an instance of {@link UserLoginResponse }
     * 
     */
    public UserLoginResponse createUserLoginResponse() {
        return new UserLoginResponse();
    }

    /**
     * Create an instance of {@link UserLogoutResponse }
     * 
     */
    public UserLogoutResponse createUserLogoutResponse() {
        return new UserLogoutResponse();
    }

    /**
     * Create an instance of {@link UserLogout }
     * 
     */
    public UserLogout createUserLogout() {
        return new UserLogout();
    }

    /**
     * Create an instance of {@link RegisterUserResponse }
     * 
     */
    public RegisterUserResponse createRegisterUserResponse() {
        return new RegisterUserResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Controller/", name = "userLogin")
    public JAXBElement<UserLogin> createUserLogin(UserLogin value) {
        return new JAXBElement<UserLogin>(_UserLogin_QNAME, UserLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Controller/", name = "registerUserResponse")
    public JAXBElement<RegisterUserResponse> createRegisterUserResponse(RegisterUserResponse value) {
        return new JAXBElement<RegisterUserResponse>(_RegisterUserResponse_QNAME, RegisterUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLogout }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Controller/", name = "userLogout")
    public JAXBElement<UserLogout> createUserLogout(UserLogout value) {
        return new JAXBElement<UserLogout>(_UserLogout_QNAME, UserLogout.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLogoutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Controller/", name = "userLogoutResponse")
    public JAXBElement<UserLogoutResponse> createUserLogoutResponse(UserLogoutResponse value) {
        return new JAXBElement<UserLogoutResponse>(_UserLogoutResponse_QNAME, UserLogoutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Controller/", name = "userLoginResponse")
    public JAXBElement<UserLoginResponse> createUserLoginResponse(UserLoginResponse value) {
        return new JAXBElement<UserLoginResponse>(_UserLoginResponse_QNAME, UserLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Controller/", name = "registerUser")
    public JAXBElement<RegisterUser> createRegisterUser(RegisterUser value) {
        return new JAXBElement<RegisterUser>(_RegisterUser_QNAME, RegisterUser.class, null, value);
    }

}

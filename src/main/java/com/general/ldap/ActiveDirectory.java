package com.general.ldap;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocketFactory;

/**
 * This class provides a simple utility method that may be used to change the
 * password of a user stored in an Active Directory server instance.
 */
public class ActiveDirectory {
    private LDAPConnection connection = null;
    private String adHost;
    private Integer adSSLPort;
    private String bindDN;
    private String bindPassword;

    /**
     * Perform the complete set of processing required to change a user's
     * password in an Active Directory server.
     *
     * @param  adHost        The address of the Active Directory server.
     * @param  adSSLPort     The SSL-based port of the Active Directory server
     *                       (typically 636).
     * @param  bindDN        The DN to use when binding to the Active Directory
     *                       server instance.  It must have sufficient permission
     *                       to change user passwords.
     * @param  bindPassword  The clear-text password to use when binding to the
     *                       Active Directory server instance.
     */
    public ActiveDirectory(String adHost, Integer adSSLPort, String bindDN, String bindPassword) {
        this.adHost = adHost;
        this.adSSLPort = adSSLPort;
        this.bindDN = bindDN;
        this.bindPassword = bindPassword;
    }

    public ActiveDirectory() {
    }

    /**
     * Perform the complete set of processing required to change a user's
     * password in an Active Directory server.
     *
     * @param  userDN        The DN of the user whose password should be changed.
     * @param  newPassword   The clear-text new password to assign to the user.
     *
     * @throws  LDAPException  If a problem is encountered while performing any
     *                         of the required processing.
     * @throws java.io.UnsupportedEncodingException
     */
    public void changePassword(final String userDN, final String newPassword)
           throws LDAPException, UnsupportedEncodingException {
        // Properly encode the password.  It must be enclosed in quotation marks,
        // and it must use a UTF-16LE encoding.
        System.out.println("Going to encode the password.");
        final byte[] quotedPasswordBytes;
        try {
          final String quotedPassword = '"' + newPassword + '"';
          quotedPasswordBytes = quotedPassword.getBytes("UTF-16LE");
        } catch (final UnsupportedEncodingException uee) {
            throw new LDAPException(ResultCode.LOCAL_ERROR, "Unable to encode the quoted password in UTF-16LE:  " + StaticUtils.getExceptionMessage(uee), uee);
        }    

        // Attempt to modify the user password.
        final Modification mod = new Modification(ModificationType.REPLACE, "unicodePwd", quotedPasswordBytes);
        connection.modify(userDN, mod);
    }
    
    /**
     * Perform the complete set of processing required to change a user's
     * password in an Active Directory server.
     *
     * @throws  LDAPException  If a problem is encountered while performing any
     *                         of the required processing.
     */
    public void connect() throws LDAPException {
        // Create an SSL socket factory to use during the course of establishing
        // an SSL-based connection to the server.  For simplicity, we'll cheat and
        // use a trust manager that will trust any certificate that the server
        // presents, but in production environments you should validate the
        // certificate more carefully.
        System.out.println("Going to create the SSL socket factory.");
        final SSLSocketFactory socketFactory;
        final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
        try{
            socketFactory = sslUtil.createSSLSocketFactory();
        } catch (final GeneralSecurityException e) {
            throw new LDAPException(ResultCode.LOCAL_ERROR, "Unable to create an SSL socket factory to use for establishing " + "a secure connection:  " + StaticUtils.getExceptionMessage(e), e);
        }

        // Create a secure connection to the Active Directory server.
        connection = new LDAPConnection(socketFactory, adHost, adSSLPort, bindDN, bindPassword);
    }
    
    public Boolean connect(String adHost, Integer adSSLPort, String bindDN, String bindPassword) {
        try {
            this.adHost = adHost;
            this.adSSLPort = adSSLPort;
            this.bindDN = bindDN;
            this.bindPassword = bindPassword;
            connect();
        } catch (LDAPException ex) {
            Logger.getLogger(ActiveDirectory.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public void disconnect() {
        connection.close();
    }
    
    public List<SearchResultEntry> search(final String baseDN, final String filter) throws LDAPException{
        SearchResult searchResult = connection.search(baseDN, SearchScope.ONE, Filter.create(filter));
        return searchResult.getSearchEntries();
    }    
}
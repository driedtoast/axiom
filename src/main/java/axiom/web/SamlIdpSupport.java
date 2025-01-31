package axiom.web;

import axiom.saml.idp.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

public abstract class SamlIdpSupport extends AxiomSupport {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(SamlIdpSupport.class);

    private IdpConfiguration idpConfig = new IdpConfiguration();
    private String rawSAMLResponse = null;
    private String target;
    private boolean autoLogin = false;


    public IdpConfiguration getIdpConfig() {
        String keystoreFile = getServletContext().getRealPath("/") + getServletContext().getInitParameter("keystoreFile");
        if (keystoreFile != null && !keystoreFile.equals("")) {
            idpConfig.setKeystoreFile(new File(keystoreFile));
        }
        idpConfig.setKeystoreAlias(getServletContext().getInitParameter("keystoreAlias"));
        idpConfig.setKeystorePassword(getServletContext().getInitParameter("keystorePassword").toCharArray());
        idpConfig.setKeystoreAliasPassword(getServletContext().getInitParameter("keystoreAliasPassword").toCharArray());

        return idpConfig;
    }

    public void setIdpConfig(IdpConfiguration idpConfig) {
        this.idpConfig = idpConfig;
    }


    public String getRawSAMLResponse() {
        return rawSAMLResponse;
    }

    public void setRawSAMLResponse(String rawSAMLResponse) {
        this.rawSAMLResponse = rawSAMLResponse;
    }

    //all CAPS getter for buggy Struts
    public String getTARGET() {
        return getTarget();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public Map<SamlVersion, String> getSamlVersions() {
        return buildValueToLabelMap(SamlVersion.values());
    }

    public Map<SamlUserIdLocation, String> getSamlUserIdLocations() {
        return buildValueToLabelMap(SamlUserIdLocation.values());
    }

    public Map<UserType, String> getUserTypes() {
        return buildValueToLabelMap(UserType.values());
    }

    //properties for RelayState that are stored in startUrl
    public String getRelayState() {
        return getIdpConfig().getStartURL();
    }

    public void setRelayState(String relayState) {
        getIdpConfig().setStartURL(relayState);
    }

    private <LK extends LabelKeyed> Map<LK, String> buildValueToLabelMap(LK... labelKeyedValues) {
        final Map<LK, String> valuesToLabels = new LinkedHashMap<LK, String>();
        for (LK lk : labelKeyedValues) {
            valuesToLabels.put(lk, getText(lk.getLabelKey()));
        }
        return Collections.unmodifiableMap(valuesToLabels);
    }
}

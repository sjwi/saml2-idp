package com.sjwi.saml.idp.model;

import javax.servlet.http.HttpServletRequest;

import com.sjwi.saml.idp.exception.InvalidSessionException;

/**
 * Object to maintain session specific attributes for login cycle
 * @author sjwi
 *
 */
public class SamlAttributes {

	private final Object acsUrl;
	private final Object subjectNameId;
	private final Object requestId;
	private final Object relayState;
	private final Object entityId;
	
	public SamlAttributes(HttpServletRequest request) throws InvalidSessionException {
		
		try {
			subjectNameId = request.getSession().getAttribute("NAME_ID");
			acsUrl = request.getSession().getAttribute("ACS");
			relayState = request.getSession().getAttribute("RELAY_STATE");
			entityId = request.getSession().getAttribute("ENTITY_ID");
			requestId = request.getSession().getAttribute("REQ_ID");
		}
		catch (NullPointerException e) {
			throw new InvalidSessionException("One or more of the required session attributes are null");
		}
	}
	
	public String getAcsUrl() {
		return acsUrl == null? null: acsUrl.toString();
	}
	public String getSubjectNameId() {
		return subjectNameId == null? null: subjectNameId.toString();
	}
	public String getRequestId() {
		return requestId == null? null: requestId.toString();
	}
	public String getRelayState() {
		return relayState == null? null: relayState.toString();
	}
	public String getEntityId() {
		return entityId == null? null: entityId.toString();
	}

	@Override
	public String toString() {
		return "SamlAttributes [acsUrl=" + acsUrl + ", subjectNameId=" + subjectNameId + ", requestId=" + requestId
				+ ", relayState=" + relayState + ", entityId=" + entityId + "]";
	}

}

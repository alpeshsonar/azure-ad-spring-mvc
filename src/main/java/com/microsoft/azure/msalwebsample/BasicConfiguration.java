// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.azure.msalwebsample;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Object containing configuration data for the application. Spring will automatically wire the
 * values by grabbing them from application.properties file
 */

@Component
@ConfigurationProperties("aad")
class BasicConfiguration {
    String clientId;
    @Getter(AccessLevel.NONE) String authority;
    String redirectUriSignin;
    String redirectUriGraphUsers;
    String secretKey;

    String getAuthority(){
        if (!authority.endsWith("/")) {
            authority += "/";
        }
        return authority;
    }

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRedirectUriSignin() {
		return redirectUriSignin;
	}

	public void setRedirectUriSignin(String redirectUriSignin) {
		this.redirectUriSignin = redirectUriSignin;
	}

	public String getRedirectUriGraphUsers() {
		return redirectUriGraphUsers;
	}

	public void setRedirectUriGraphUsers(String redirectUriGraphUsers) {
		this.redirectUriGraphUsers = redirectUriGraphUsers;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
    
}
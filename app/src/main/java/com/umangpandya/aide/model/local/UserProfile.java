package com.umangpandya.aide.model.local;

import android.net.Uri;

import com.google.android.gms.common.api.Scope;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by umang on 23/10/16.
 */

public class UserProfile {
    private String id;
    private String email;
    private String displayName;
    private String givenName;
    private String familyName;
    private String photoUrl;
    private String serverAuthCode;
    private String idToken;
    private Set<Scope> grantedScopes = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getServerAuthCode() {
        return serverAuthCode;
    }

    public void setServerAuthCode(String serverAuthCode) {
        this.serverAuthCode = serverAuthCode;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Set<Scope> getGrantedScopes() {
        return grantedScopes;
    }

    public void setGrantedScopes(Set<Scope> grantedScopes) {
        this.grantedScopes = grantedScopes;
    }
}

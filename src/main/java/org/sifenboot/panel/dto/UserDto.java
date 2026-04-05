package org.sifenboot.panel.dto;

public class UserDto {
    private String username;
    private String accessToken;
    private String tokenType = "Bearer";

    public UserDto() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
}



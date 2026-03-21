package com.sifen.api.dto.auth.response;


public class RefreshTokenRequest {

  private String email;
  private String refreshToken;

  public RefreshTokenRequest() {
  }

  public RefreshTokenRequest(String email, String refreshToken) {
    this.email = email;
    this.refreshToken = refreshToken;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}

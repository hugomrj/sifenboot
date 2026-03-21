package com.sifen.api.dto.auth;


public class UserInfo {

  private String id;
  private String email;
  private String fullName;
  private String role;
  private String companyId;

  public UserInfo() {
  }

  public UserInfo(String id, String email, String fullName, String role, String companyId) {
    this.id = id;
    this.email = email;
    this.fullName = fullName;
    this.role = role;
    this.companyId = companyId;
  }

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

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }
}

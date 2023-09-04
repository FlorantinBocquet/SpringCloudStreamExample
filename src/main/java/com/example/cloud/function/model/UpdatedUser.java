package com.example.cloud.function.model;

public record UpdatedUser(String name, String password) {
  @Override
  public String toString() {
    return "UpdatedUser{" + "name=" + name + ", password=" + password + '}';
  }
}

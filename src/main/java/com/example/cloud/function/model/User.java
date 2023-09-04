package com.example.cloud.function.model;

public record User(String firstname, String lastname) {
  @Override
  public String toString() {
    return "User{" + "firstname=" + firstname + ", lastname=" + lastname + '}';
  }
}

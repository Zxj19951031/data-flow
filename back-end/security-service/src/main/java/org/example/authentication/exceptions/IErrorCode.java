package org.example.authentication.exceptions;

public interface IErrorCode {
    int getCode();

    String getMsg();

    String toString();
}
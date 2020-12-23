package org.example.cron.exceptions;

public interface IErrorCode {
    int getCode();

    String getMsg();

    String toString();
}
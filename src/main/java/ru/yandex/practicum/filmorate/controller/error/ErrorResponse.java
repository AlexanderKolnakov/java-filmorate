package ru.yandex.practicum.filmorate.controller.error;

public class ErrorResponse extends Exception{
    public ErrorResponse(String message){
        super(message);
    }
}

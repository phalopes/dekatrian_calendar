package br.com.dekatrian.calendar.dekatrian_calendar;

class InvalidDateDekaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    InvalidDateDekaException(String msg) {
        super(msg);
    }

}

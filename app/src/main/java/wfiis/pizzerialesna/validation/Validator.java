package wfiis.pizzerialesna.validation;


import java.util.regex.Pattern;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.customViews.TopToast;
import wfiis.pizzerialesna.tools.Util;

public class Validator {
    static String POLISH_LETTERS = "a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ";


    public static final int MAX_PHONE_CHARACTERS = 9;
    public static final int NIP_NUMBER_LENGTH = 10;
    public static final int MAX_AGE_CHARACTERS = 4;
    public static final int MAX_PHONE_LENGHT_EDIT = 20;
    public static final int MAX_FIRST_NAME_LENGTH = 11;
    public static final int MAX_LAST_NAME_LENGTH = 28;


    public static boolean phoneIsValid(String number) {
        number = number.trim();
        if (number.length() == 0) return false;
        return android.util.Patterns.PHONE.matcher(number).matches() && number.length() == MAX_PHONE_CHARACTERS;
    }

    public static boolean longPhonNoIsValid(String number) {
        number = number.trim();
        if (number.length() == 0) return false;
        return android.util.Patterns.PHONE.matcher(number).matches() && number.length() >= MAX_PHONE_CHARACTERS && number.length() <= MAX_PHONE_LENGHT_EDIT;
    }

    public static boolean phoneNumberFormatIsValid(String number) {
        number = number.trim();
        String NUMBER_PATTERN = "^[4-8]{1}+[0-9]{8}$";
        return Pattern.compile(NUMBER_PATTERN, Pattern.CASE_INSENSITIVE).matcher(number).matches();
    }

    public static boolean dateIsValid(String date) {
        String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19[0-9][0-9]|20[0-9][0-9])\\d\\d)";
        if (!Pattern.compile(DATE_PATTERN, Pattern.CASE_INSENSITIVE).matcher(date).matches()) {
            return true;
        }
        return false;
    }

    public static boolean zipCodeOrTownIsValid(String text) {
        String PATTERN = "^\\d{2}\\-\\d{3}$|^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\\-\\s]{0,50}$";
        return text.matches(PATTERN);
    }

    public static boolean zipCodeIsValid(String zipCode) {
        String pattern = "\\d{2}-\\d{3}";
        return zipCode.matches(pattern);
    }

    public static boolean isEmailValid(CharSequence email) {
        if (Util.nullOrEmpty((String) email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches();
        }
    }

    public static boolean isPasswordValid(String text) {
        String pattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,15}+$";
        if (Util.nullOrEmpty(text)) {
            return false;
        } else {
            return text.matches(pattern);
        }
    }

    public static boolean firstNameIsValid(String fistName) {
        String pattern = "^[-" + POLISH_LETTERS + "\\s]{1,11}+$";
        return fistName.matches(pattern);
    }

    public static boolean lastNameIsValid(String lastName) {
        String pattern = "^[-" + POLISH_LETTERS + "\\s]{1,28}+$";
        return lastName.matches(pattern);
    }

    public static boolean firstAndLastNameIsValid(String lastName) {
        String pattern = "^[-" + POLISH_LETTERS + "\\s]{1,40}+$";
        return lastName.matches(pattern);
    }

    public static boolean isHouseNumberValid(String number) {
        String pattern = "^[-a-zA-Z0-9\\/\\s]+$";
        return number.matches(pattern);
    }

    public static boolean isFlatNumberValid(String number) {
        if (Util.nullOrEmpty(number)) {
            return true;
        } else {
            String pattern = "^[-a-zA-Z0-9\\/\\s]+$";
            return number.matches(pattern);
        }
    }

    public static boolean nipNumberIsValid(String number) {
        String pattern = "^[0-9]{10}$";
        return number.matches(pattern);
    }

    public static boolean isNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean cityIsValid(String city) {
        String pattern = "^.+$";
        return city.matches(pattern);
    }

    public static boolean streetIsValid(String street) {
        String pattern = "^.+$";
        return street.matches(pattern);
    }
}


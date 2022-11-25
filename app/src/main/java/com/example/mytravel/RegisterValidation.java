package com.example.mytravel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {
    public boolean Matcher(String regex, String toCheck)
    {
        Pattern p = Pattern.compile(regex);
        if(toCheck == null)
        {
            return false;
        }
        Matcher m = p.matcher(toCheck);
        return m.matches();
    }
    public boolean nameValidation(String name)
    {
        String regex = "[A-Z]+[a-z]{1,29}$";
        return Matcher(regex, name);
    }
    public boolean lastnameValidation(String fullname)
    {
        String regex = "[A-Z]+[a-z]{1,15}([-][A-Z]+[a-z]{1,15})*";
        return Matcher(regex, fullname);
    }
    public boolean emailValidation(String email)
    {
        String regex = "([A-Za-z0-9]([.][A-Za-z0-9])*){6,30}@[a-z0-9]{1,7}[.][a-z]{1,3}";
        return Matcher(regex, email);
    }
    public boolean passwordValidation(String password)
    {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!/@#$%]).{8,20}$";
        return Matcher(regex, password);
    }
    public boolean phoneValidation(String phoneNumber)
    {
        String regex = "[0-9]{9,9}";
        return Matcher(regex, phoneNumber);
    }
}

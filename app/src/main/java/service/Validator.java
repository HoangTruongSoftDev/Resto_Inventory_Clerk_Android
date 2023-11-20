package service;

public class Validator {
    public static boolean isValidUserId(String userId, String position ) {
        int digitId = position.equals("Admin") ? 4 : 6;
        if (userId.matches("^\\d{" + digitId + "}$")) {
            return true;
        }
        return false;
    }
}

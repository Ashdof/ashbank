package com.ashbank.objects.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

public class Security {
    /***
     * Generate ID:
     * generate a random UUID value to use for
     * customer ID
     * @return the generated ID
     */
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /***
     * Generate Date:
     * generates a date for the current operation
     * @return the date
     */
    public String generateDate() {
        Calendar calendar = Calendar.getInstance();
        String date;
        int day, month, year;

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        date = year + "-" + month + "-" + day;

        return date;
    }

    /***
     * Hash Password:
     * hash the user's password before persisting it to the
     * storage. This is done for Security purposes.
     * @param dataToHash the data to hash
     * @return the hashed password as a string object.
     */
    public String hashSecurityData(String dataToHash) {
        CustomDialogs customDialogs = new CustomDialogs();
        String ERR_HASH_TITLE = "Data Hash Error";
        StringBuilder hexString = new StringBuilder();
        byte[] hash;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hash = messageDigest.digest(dataToHash.getBytes());

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            customDialogs.showErrInformation(ERR_HASH_TITLE, e.getMessage());
        }

        return hexString.toString();
    }
}

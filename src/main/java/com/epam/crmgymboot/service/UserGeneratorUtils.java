package com.epam.crmgymboot.service;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Locale;
import java.util.function.Predicate;

import static org.passay.IllegalCharacterRule.ERROR_CODE;

public class UserGeneratorUtils {

    public static String usernameGenerator(String firstname, String lastname, Predicate<String> condition) {
        String baseUsername = firstname.toLowerCase(Locale.ROOT) + "." + lastname.toLowerCase(Locale.ROOT);
        String finalUsername = baseUsername;
        int serialNumber = 0;

        while (condition.test(finalUsername)) {
            serialNumber++;
            finalUsername = baseUsername + serialNumber;
        }

        return finalUsername;
    }

    public static String passwordGenerator(int length) {
        PasswordGenerator generator = new PasswordGenerator();

        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        return generator.generatePassword(length, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }
}

package lapr4.usermanagement.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ShodronePasswordPolicyTest {

    private final ShodronePasswordPolicy subject = new ShodronePasswordPolicy();

    @Test
    void ensurePasswordHasAtLeastOneDigitOneCapitalAnd6CharactersLong() {
        assertTrue(subject.isSatisfiedBy("abCfefgh1"));
    }

    @Test
    void ensurePasswordsSmallerThan6CharactersAreNotAllowed() {
        assertFalse(subject.isSatisfiedBy("ab1c"));
    }

    @Test
    void ensurePasswordsWithoutDigitsAreNotAllowed() {
        assertFalse(subject.isSatisfiedBy("abcefghi"));
    }

    @Test
    void ensurePasswordsWithoutCapitalLetterAreNotAllowed() {
        assertFalse(subject.isSatisfiedBy("abcefghi1"));
    }

    @Test
    void testWeakPassword1() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.WEAK, subject.strength("A23456"));
    }

    @Test
    void testWeakPassword2() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.WEAK, subject.strength("A234567"));
    }

    @Test
    void testGoodPassword1() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.GOOD, subject.strength("A2345678"));
    }

    @Test
    void testGoodPassword2() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.GOOD, subject.strength("A23456789"));
    }

    @Test
    void testExcelentPassword1() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.EXCELENT, subject.strength("123456789ABC"));
    }

    @Test
    void testExcelentPassword2() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.EXCELENT, subject.strength("123456789ABCD"));
    }

    @Test
    void testExcelentPassword3() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.EXCELENT, subject.strength("A234$5678"));
    }

    @Test
    void testExcelentPassword4() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.EXCELENT, subject.strength("A234#5678"));
    }

    @Test
    void testExcelentPassword5() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.EXCELENT, subject.strength("A234!5678"));
    }

    @Test
    void testExcelentPassword6() {
        assertEquals(ShodronePasswordPolicy.PasswordStrength.EXCELENT, subject.strength("A234?5678"));
    }

}

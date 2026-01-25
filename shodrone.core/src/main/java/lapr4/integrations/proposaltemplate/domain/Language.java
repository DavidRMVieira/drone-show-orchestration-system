package lapr4.integrations.proposaltemplate.domain;

public enum Language {

    PT,
    EN;

    @Override
    public String toString() {
        return name();
    }

    public static Language fromString(String language) {
        return Language.valueOf(language.trim().toUpperCase());
    }

}

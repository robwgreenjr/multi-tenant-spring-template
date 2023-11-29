package template.global.services;

public interface StringEncoder {
    String encode(String string);

    String decode(String string);

    Boolean verify(String string, String encodedString);
}
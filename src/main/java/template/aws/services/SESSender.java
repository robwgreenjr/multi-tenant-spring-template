package template.aws.services;

public interface SESSender {
    void sendEmail(String[] to, String subject, String html, String text);
}

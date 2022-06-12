package com.folcolf.automailer;

import com.folcolf.automailer.model.Artist;
import com.folcolf.automailer.model.HeaderMessage;
import com.folcolf.automailer.service.CSVService;
import com.folcolf.automailer.service.EmailSenderService;
import com.folcolf.automailer.util.EmailUtil;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@SpringBootApplication
public class AutoMailerApplication implements ApplicationRunner {

    private static List<Artist> artists;
    @Autowired
    private EmailSenderService emailService;
    @Autowired
    private CSVService csvService;
    private String subject;
    private Map<String, String> attachments;


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AutoMailerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }


    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting application");

        // Get args
        if (!extractArgs(args)) return;

        log.info("START... Sending email");

        Dotenv dotenv = Dotenv.load();

        String username = dotenv.get("username");
        String password = dotenv.get("password");

        Properties props = EmailUtil.createMailProperties();

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        for (Artist artist : artists) {
            Map<String, Object> model = new HashMap<>();
            model.put("name", artist.getName());
            model.put("instagram", "ardez.studio");
            model.put("sender", "Ardez");

            HeaderMessage headerMessage = new HeaderMessage(username, List.of(artist.getEmail()), subject, attachments, model);
            try {
                emailService.sendEmail(headerMessage, session);
            } catch (Throwable e) {
                log.error("Error sending email to " + artist.getEmail(), e);
                throw new RuntimeException(e);
            }
        }

        log.info("END... Email sent success");
        log.info("Application stopped");
    }

    private boolean extractArgs(ApplicationArguments args) {
        log.info("START... Get args");
        String[] argsMap = args.getSourceArgs();
        log.info("argsMap: {}", Arrays.stream(argsMap).toList());

        if (argsMap.length == 0) {
            log.error("No args");
            System.out.println("No args");
            System.out.println("See help: -h");
            return false;
        }

        AtomicBoolean help = new AtomicBoolean(false);

        // Get args help
        Arrays.stream(argsMap).forEach(arg -> {
            if (arg.matches("--help") || arg.matches("-h")) {
                log.info("Get help");
                System.out.println("Help: ");
                System.out.println("-h, --help: Show help");
                System.out.println("-s=<subject>, --subject=<subject>: Subject of email");
                System.out.println("-f=<file>, --file=<file>: File with artists");
                System.out.println("-a=<files>, --attach=<files>: Files attachments");
                help.set(true);
            }
        });

        if (help.get()) return false;

        for (String arg : argsMap) {
            if (arg.matches("--file=.+") || arg.matches("-f=.+")) {
                log.info("START... Get CSV file");
                String csvFile = arg.split("=")[1];
                log.debug("csvFile: {}", csvFile);
                csvService.setData(csvFile);
                artists = csvService.getArtists();
            } else if (arg.matches("-s=.+") || arg.matches("--subject=.+")) {
                subject = arg.split("=")[1];
                log.debug("subject: {}", subject);
            } else if (arg.matches("-a=.+") || arg.matches("--attach=.+")) {
                attachments = new HashMap<>();
                String[] files = arg.split("=")[1].split(",");
                log.debug("files: {}", Arrays.stream(files).toList());
                String filename;
                for (String file : files) {
                    filename = file.split("/")[file.split("/").length - 1];
                    attachments.put(filename, file);
                }
            } else {
                log.error("Unknown arg: {}", arg);
                System.out.println("Unknown arg: " + arg);
                return false;
            }
        }

        if (artists == null || artists.isEmpty()) {
            log.error("No artists");
            System.out.println("No artists");
            return false;
        }

        if (subject == null || subject.isEmpty()) {
            subject = "";
        }

        log.info("END... Get CSV file");
        return true;
    }
}
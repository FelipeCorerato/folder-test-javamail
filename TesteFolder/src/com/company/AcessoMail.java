package com.company;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AcessoMail {

    protected Session session;
    protected POP3Store store;
    protected String userName, password;

    public AcessoMail (String userName, String password) throws Exception{

        this.userName = userName;
        this.password = password;


        //create properties field
        Properties properties = new Properties();

        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);

        //create the POP3 store object and connect with the pop server
        store = (POP3Store) emailSession.getStore("pop3s");

        store.connect("pop.gmail.com", userName, password);
    }

    public void enviarEmail(String destinatario) {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contatestetur000@gmail.com")); //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                    .parse(destinatario);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("Enviando email com JavaMail");//Assunto
            message.setText("Enviei este email utilizando JavaMail com minha conta GMail!");
            /**Método para enviar a mensagem criada*/
            Transport.send(message);

            System.out.println("Feito!!!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public Message[] listarEmailsDoInbox() throws Exception {
        //create the folder object and open it
        POP3Folder emailFolder = (POP3Folder) store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        // retrieve the messages from the folder in an array and print it
        return emailFolder.getMessages();
    }

    public Folder[] listarPastas() throws Exception{
        return store.getDefaultFolder().list();
        /*
        javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
        for (javax.mail.Folder folder : folders) {
            if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
            }
        */
    }
}

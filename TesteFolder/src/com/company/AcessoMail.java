package com.company;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AcessoMail {

    protected Session session;
    protected Store store;
    protected String userName, password;

    public AcessoMail (String userName, String password) throws Exception{

        // credenciais usadas no login
        this.userName = userName;
        this.password = password;


        // cria o nosso campo de propriedades
        Properties properties = new Properties();

        // as linhas abaixo preenchem as propriedades com informacoes necessarias para o login
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "imap");
        properties.put("mail.imap.starttls.enable", "true");


        /* codigo caso seja desejado fazer a conexao em POP3
        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        */

        // cria a sessao para fazer a conexao
        Session emailSession = Session.getDefaultInstance(properties);

        // inicializa a store e conecta com o servidor pop3
        store = emailSession.getStore("imaps");
        store.connect("imap.gmail.com", userName, password);
    }

    public void renomearPasta(String nomeOriginal, String nomeFinal) throws Exception {
        Folder pasta = store.getFolder(nomeOriginal);

        if (!pasta.exists())
            throw new FolderNotFoundException();
        else
            pasta.renameTo(store.getFolder(nomeFinal));
    }

    public boolean deletarPasta(String nome) throws Exception {
        Folder pasta = store.getFolder(nome);

        if (!pasta.exists())
            throw new FolderNotFoundException();

        if (pasta.isOpen())
            pasta.close();

        return pasta.delete(true);
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
        Folder emailFolder = store.getFolder("INBOX");
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

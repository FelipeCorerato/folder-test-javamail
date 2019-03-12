package com.company;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AcessoMail {

    protected Session minhaSessao;
    protected Store minhaStore;
    protected String userName, senha;

    public AcessoMail (String userName, String senha) throws Exception{

        // credenciais usadas no login
        this.userName = userName;
        this.senha = senha;


        // cria o nosso campo de propriedades
        Properties propriedades = new Properties();

        // as linhas abaixo preenchem as propriedades com informacoes necessarias para o login
        propriedades.put("mail.imap.host", "imap.gmail.com");
        propriedades.put("mail.imap.port", "imap");
        propriedades.put("mail.imap.starttls.enable", "true");


        /* codigo caso seja desejado fazer a conexao em POP3:

        propriedades.put("mail.pop3.host", "pop.gmail.com");
        propriedades.put("mail.pop3.port", "995");
        propriedades.put("mail.pop3.starttls.enable", "true");

        */

        // cria a sessao para fazer a conexao
        Session sessaoDoEmail = Session.getDefaultInstance(propriedades);

        // inicializa a minhaStore e conecta com o servidor pop3
        minhaStore = sessaoDoEmail.getStore("imaps");
        minhaStore.connect("imap.gmail.com", userName, senha);
    }

    public void renomearPasta(String nomeOriginal, String nomeFinal) throws Exception {
        Folder pasta = minhaStore.getFolder(nomeOriginal);

        if (!pasta.exists())
            throw new FolderNotFoundException();

        if (pasta.isOpen())
            pasta.close();

        pasta.renameTo(minhaStore.getFolder(nomeFinal));
    }

    public void criarPasta(String nome) throws Exception {
        Folder minhaPasta = minhaStore.getFolder(nome);
        if (!minhaPasta.exists()) {
            if (minhaPasta.create(Folder.HOLDS_MESSAGES)) {
                minhaPasta.setSubscribed(true);
                System.out.println("Folder was created successfully");
            }
        }
    }

    public boolean deletarPasta(String nome) throws Exception {
        Folder pasta = minhaStore.getFolder(nome);

        if (!pasta.exists())
            throw new FolderNotFoundException();

        if (pasta.isOpen())
            pasta.close();

        return pasta.delete(true);
    }

    // a partir daqui, os metodos foram feitos apenas para testar o projeto

    public void enviarEmail(String destinatario) throws Exception {
        Message mensagem = new MimeMessage(minhaSessao);
        mensagem.setFrom(new InternetAddress("contatestetur000@gmail.com")); //Remetente

        Address[] toUser = InternetAddress //Destinatário(s)
                .parse(destinatario);

        mensagem.setRecipients(Message.RecipientType.TO, toUser);
        mensagem.setSubject("Enviando email com JavaMail");//Assunto
        mensagem.setText("Enviei este email utilizando JavaMail com minha conta GMail!");

        // metodo para enviar a mensagem criada
        Transport.send(mensagem);

        System.out.println("Feito!!!");
    }

    public Message[] listarEmailsDoInbox() throws Exception {
        // cria e instancia a pasta INBOX
        Folder pastaInbox = minhaStore.getFolder("INBOX");
        try {
            pastaInbox.open(Folder.READ_ONLY);
            return pastaInbox.getMessages();

        } finally {
            pastaInbox.close();
        }
    }

    public Folder[] listarPastas() throws Exception{
        return minhaStore.getDefaultFolder().list();
    }
}

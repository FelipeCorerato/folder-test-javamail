package com.company;

import javax.mail.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            Scanner input = new Scanner(System.in);
            AcessoMail acesso = new AcessoMail("contatestetur000@gmail.com", "Senha123");

            System.out.println("Bem vindo ao EMAIL. O que deseja fazer?");
            System.out.println("1 - Enviar um e-mail");
            System.out.println("2 - Listar os e-mails do seu INBOX");
            System.out.println("3 - Listar as pastas do seu e-mail");

            String opcao = input.nextLine();

            switch (opcao) {
                case "1":
                    System.out.println("Digite o endereco do destinatario:");
                    String destinatario = input.nextLine();
                    acesso.enviarEmail("arturpmrs@gmail.com");
                    break;

                case "2":
                    Message[] mensagens = acesso.listarEmailsDoInbox();
                    acesso.listarEmailsDoInbox();
                    System.out.println("Numero de e-mails: " + mensagens.length);

                    for (int i = 0, n = mensagens.length; i < n; i++) {
                        Message mensagem = mensagens[i];
                        System.out.println("---------------------------------");
                        System.out.println("Email Number " + (i + 1));
                        System.out.println("Subject: " + mensagem.getSubject());
                        System.out.println("From: " + mensagem.getFrom()[0]);
                        System.out.println("Text: " + mensagem.getContent().toString());
                    }
                    break;

                case "3":
                    for(Folder fd:acesso.listarPastas())
                        System.out.println(">> "+fd.getName());
            }
            acesso.listarEmailsDoInbox();
            //acesso.listarPastas();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package detectorerro;

/**
 *
 * @author AleGomes
 */
public class DetectorDeErro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //arg0 - Operação: cod (codificação) ou decod (decodificação)
        //arg1 - caminho do arquivo de entrada para aquela operação
        //arg2 - caminho do arquivo de saida para aquela operação

        /*
         //Para rodar pela linha de comando
         if (args.length > 3 || args.length < 3) {
         System.out.println("Número errado de parâmetros.");
         System.out.println("Uso correto: operação(cod ou decod) caminho_do_arquivo_de_entrada.txt caminho_do_arquivo_de_saida.txt");
         System.out.println("Exemplo1: cod inicio.txt meio.txt");
         System.out.println("Exemplo2: cod meio.txt fim.txt");
         } else {
         if (args[0].equalsIgnoreCase("cod")) {
         Codificador codificador = new Codificador();
         codificador.codificar(args[1], args[2]);
         } else {
         if (args[0].equalsIgnoreCase("decod")) {
         Decodificador decodificador = new Decodificador();
         decodificador.decodificar(args[1], args[2]);
         }
         }
         }
         */
        //Para rodar pelo IDE
        
        String op = "decod";

        if (op.equalsIgnoreCase("cod")) {
            Codificador codificador = new Codificador();
            codificador.codificar("inicio.txt", "meio.txt");
        } else {
            if (op.equalsIgnoreCase("decod")) {
                Decodificador decodificador = new Decodificador();
                decodificador.decodificar("meio.txt", "fim.txt");
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package detectorerro;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author AleGomes
 */
public class Arquivo {
    
    
    static String lerArquivo(String caminho) {
        String conteudo = "";
        try {
            for (String linha : Files.readAllLines(Paths.get(caminho))) {
                conteudo += linha;
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return conteudo;
    }

    static void escreverArquivo(String conteudo, String caminho) {
        try (PrintStream out = new PrintStream(new FileOutputStream(caminho))) {
            out.print(conteudo);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }
    
    /*
    private static String converterTextoParaBinario(String conteudo) {
        String resultado = "";
        byte[] infoBin = null;
        try {
            infoBin = conteudo.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.toString());
        }
        for (byte b : infoBin) {
            String bin = Integer.toBinaryString(b);
            if (bin.length() < 8) {
                bin = "0" + bin;
            }
            resultado += bin;
        }
        return resultado;
    }

    private static String converterBinarioParaTexto(String conteudo) {
        String mensagem = "";
        for (int i = 0; i <= conteudo.length() - 8; i+=8) {
            mensagem += (char) Integer.parseInt(conteudo.substring(i, i + 8), 2);
        }
        return mensagem;
    }
    */
    
    /*
    static String lerArquivoConvertido(String caminho) {
        String conteudo = "";
        try {
            for (String linha : Files.readAllLines(Paths.get(caminho))) {
                conteudo += linha;
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        conteudo = converterTextoParaBinario(conteudo);
        return conteudo;
    }
*/
    
    /*
    static void escreverArquivoConvertido(String conteudo, String caminho) {
        String resultado = converterBinarioParaTexto(conteudo);

        try (PrintStream out = new PrintStream(new FileOutputStream(caminho))) {
            out.print(resultado);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }
*/


}

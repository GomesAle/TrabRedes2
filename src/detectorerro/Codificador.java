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
public class Codificador {

    public void codificar(String caminhoArquivoEntrada, String caminhoArquivoSaida) {
        /*
        1) Ler arquivo
        2) Divide o conteudo em blocos de 8 bytes
        3) Calcula as paridades
        4) Concatena paridades com cada linha da matriz 
        */
        String entrada = Arquivo.lerArquivo(caminhoArquivoEntrada);
        
        String[] blocos = dividirBlocos(entrada);
        String saida = "";
        for (String bloco : blocos) {
            int[][] matrizConteudo = converterParaMatriz(bloco);
            String paridade = gerarParidade(matrizConteudo);
            saida += paridade + bloco;
        }
        Arquivo.escreverArquivo(saida, caminhoArquivoSaida);
    }

    private String[] dividirBlocos(String entrada) {
        /*
        Gera lista blocos de 8 bytes a partir do arquivo de entrada
        */
        
        String[] blocos = new String[entrada.length() / 64 + 1];

        if (entrada.length() <= 64) {
            blocos[0] = entrada;
            return blocos;
        }

        for (int i = 0; i < blocos.length; i++) {
            if (entrada.length() <= 64) {
                blocos[i] = entrada;
            } else {
                blocos[i] = entrada.substring(0, 64);
                entrada = entrada.substring(64);
            }
        }
        return blocos;
    }

    private int[][] converterParaMatriz(String bloco) {
        
        /*
        Gera a matriz de 8 bytes a partir da string do bloco
        */
        
        int[][] conteudo = new int[8][8];
        int cont = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cont >= bloco.length()) {
                    conteudo[i][j] = 0;
                } else {
                    conteudo[i][j] = Character.getNumericValue(bloco.charAt(cont));
                    cont++;    
                }
            }
        }
        return conteudo;
    }

    private String gerarParidade(int[][] matrizConteudo) {
        /*
        Gera uma string com as paridades (das colunas + das linhas) 
        */
        
        int[] paridadeColuna = new int[8];
        int[] paridadeLinha = new int[8];
        for (int i = 0; i < matrizConteudo.length; i++) {
            for (int j = 0; j < matrizConteudo[0].length; j++) {
                paridadeLinha[i] += matrizConteudo[i][j];
                paridadeColuna[j] += matrizConteudo[i][j];
            }
        }

        String paridade = "";

        for (int i = 0; i < paridadeColuna.length; i++) {
            paridade += paridadeColuna[i] % 2;
        }

        for (int i = 0; i < paridadeLinha.length; i++) {
            paridade += paridadeLinha[i] % 2;
        }

        return paridade;
    }

    /*
    private void imprimirMatriz(int[][] matrizConteudo) {
        for (int i = 0; i < matrizConteudo.length; i++) {
            for (int j = 0; j < matrizConteudo[0].length; j++) {
                System.out.printf("%d ",matrizConteudo[i][j]);
            }
            System.out.println("");
        }
    }
    */
}

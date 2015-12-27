/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package detectorerro;

import java.util.Arrays;

/**
 *
 * @author AleGomes
 */
public class Decodificador {
    /*
    1)  Ler arquivo.
    2)  Divide a string de entrada em um vetor de blocos de 10 bytes (8 bytes de conteudo + 2 bytes de paridade).
    3)  Calcula a paridade antiga.
    4)  Calcula  a nova paridade.
    5) Compara se as paridades antigas e novas são iguais.
    6) Corrige se elas forem diferentes (se for possível).
    7) Escreve em arquivo o resultado final.
    */
    public void decodificar(String caminhoArquivoEntrada, String caminhoArquivoSaida) {
        String entrada = Arquivo.lerArquivo(caminhoArquivoEntrada);
        String[] superBlocos = dividirSuperBlocos(entrada); //superblocos tem 10 bytes

        String saida = "";

        int tamanhoConteudo = 0;

        try {
            for (String superBloco : superBlocos) {
                if (superBloco.length() < 3 * 8) {
                    throw new IllegalArgumentException("Último bloco menor que 3 bytes");
                }
                //calcula paridade antiga  (2 bytes antes no inicio do bloco)
                String paridadeAntiga = gerarParidadeAntiga(superBloco);

                //calcula bloco (8bytes) (bloco sem a paridade antiga)
                int[][] matrizConteudo = converterParaMatriz(superBloco);

                tamanhoConteudo = superBloco.length() - paridadeAntiga.length();

                String paridadeNova = gerarParidade(matrizConteudo);

                String matrizFinal = detectarECorrigirErro(paridadeAntiga, paridadeNova, matrizConteudo);

                if (matrizFinal.length() >= tamanhoConteudo) {
                    saida += matrizFinal.substring(0, tamanhoConteudo);
                } else {
                    saida += matrizFinal;
                }
            }
            Arquivo.escreverArquivo(saida, caminhoArquivoSaida);
        } catch (UnsupportedOperationException ex) {
            //System.out.println("Erro incorrigível encontrado.");
            ex.printStackTrace();
            Arquivo.escreverArquivo("Erro incorrigível encontrado.", caminhoArquivoSaida);
        } catch (StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String[] dividirSuperBlocos(String entrada) {
        /*
        Qeubra a entrada em strings (superBlocos) com paridade e conteudo.
        */
        
        String[] blocos = new String[entrada.length() / 80 + 1];

        if (entrada.length() <= 64) {
            blocos[0] = entrada;
            return blocos;
        }

        for (int i = 0; i < blocos.length; i++) {
            if (entrada.length() <= 80) {
                blocos[i] = entrada;
            } else {
                blocos[i] = entrada.substring(0, 80);
                entrada = entrada.substring(80);
            }
        }
        return blocos;
    }

    private String gerarParidadeAntiga(String superBloco) {
        //O tamanho da paridade é de 2 bytes (paridade das linhas e colunas) -> 16 bits
        return superBloco.substring(0, 16);
    }

    private int[][] converterParaMatriz(String superBloco) {
        /*
        Converte o bloco string em matriz de int. 
        Caso esteja incompleta, preenche com zeros
        */
        
        superBloco = superBloco.substring(16);
        int[][] conteudo = new int[8][8];
        int cont = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cont >= superBloco.length()) {
                    conteudo[i][j] = 0;
                } else {
                    conteudo[i][j] = Character.getNumericValue(superBloco.charAt(cont));
                    cont++;
                }
            }
        }
        return conteudo;
    }

    private String gerarParidade(int[][] matrizConteudo) {
        /*
        Calcula a nova paridade da matriz
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

    private String detectarECorrigirErro(String paridadeAntiga, String paridadeNova, int[][] matrizConteudo) throws UnsupportedOperationException {
        /*
        Compara as paridades novas e antigas:
        Se fores iguais, não há erro.
        Se tiver um erro, corrige.
        Se tiver mais de um erro, informa que tem erro incorrigível.
        */
        
        if (paridadeAntiga.equals(paridadeNova)) {
            return gerarStringMatriz(matrizConteudo);
        }

        int contagemErro = 0;
        int erroColuna = -1;
        for (int i = 0; i < paridadeAntiga.length() / 2; i++) {
            if (paridadeAntiga.charAt(i) != paridadeNova.charAt(i)) {
                erroColuna = i;
                contagemErro++;
                if (contagemErro > 1) {
                    throw new UnsupportedOperationException("Erro incorrigível.");
                }
            }
        }

        contagemErro = 0;
        int erroLinha = -1;
        for (int i = 0; i < paridadeAntiga.length() / 2; i++) {
            if (paridadeAntiga.charAt(i + 8) != paridadeNova.charAt(i + 8)) {
                erroLinha = i;
                contagemErro++;
                if (contagemErro > 1) {
                    throw new UnsupportedOperationException("Erro incorrigível.");
                }
            }
        }

        matrizConteudo[erroLinha][erroColuna] += 1;

        matrizConteudo[erroLinha][erroColuna] = matrizConteudo[erroLinha][erroColuna] % 2;

        return gerarStringMatriz(matrizConteudo);
    }

    private String gerarStringMatriz(int[][] matrizConteudo) {
        String saida = "";
        for (int i = 0; i < matrizConteudo.length; i++) {
            for (int j = 0; j < matrizConteudo[0].length; j++) {
                saida += matrizConteudo[i][j];
            }
        }
        return saida;
    }

    private void imprimirMatriz(int[][] matrizConteudo) {
        for (int i = 0; i < matrizConteudo.length; i++) {
            for (int j = 0; j < matrizConteudo[0].length; j++) {
                System.out.printf("%d ", matrizConteudo[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

}

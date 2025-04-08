import java.io.*;
import java.util.*;

public class ColetaLixo {
    static Scanner scanner = new Scanner(System.in);
    static int tipoGrafo;
    static int numVertices;
    static String[] rotulos;
    static int[][] matrizAdj;

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n==== MENU - COLETA DE LIXO COMUM ====");
            System.out.println("1. Ler grafo do arquivo");
            System.out.println("2. Gravar grafo no arquivo");
            System.out.println("3. Inserir vértice");
            System.out.println("4. Inserir aresta");
            System.out.println("5. Remover vértice");
            System.out.println("6. Remover aresta");
            System.out.println("7. Mostrar conteúdo do grafo");
            System.out.println("8. Mostrar matriz de adjacência");
            System.out.println("9. Verificar conexidade");
            System.out.println("10. Encerrar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

             switch (opcao) {
                case 1:
                    lerArquivo();
                    break;
                case 2:
                    gravarArquivo();
                    break;
                case 3:
                    inserirVertice();
                    break;
                case 4:
                    inserirAresta();
                    break;
                case 5:
                    removerVertice();
                    break;
                case 6:
                    removerAresta();
                    break;
                case 7:
                    mostrarGrafo();
                    System.out.println("==============================================================================");
                    mostrarConteudoArquivo();
                    break;
                case 8:
                    mostrarMatriz();
                    break;
                case 9:
                    verificarConexidade();
                    break;
                case 10:
                    System.out.println("ENCERRANDO...");
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }while(opcao != 10);
    }

    static void lerArquivo() {
        try {
            File file = new File("grafo_sp.txt");
            Scanner sc = new Scanner(file);
            tipoGrafo = Integer.parseInt(sc.nextLine());
            numVertices = Integer.parseInt(sc.nextLine());
            rotulos = new String[numVertices];

            for (int i = 0; i < numVertices; i++) {
                String linha = sc.nextLine();
                int pos1 = linha.indexOf('"');
                int pos2 = linha.lastIndexOf('"');
                int id = Integer.parseInt(linha.substring(0, pos1).trim());
                String rotulo = linha.substring(pos1 + 1, pos2);
                rotulos[id] = rotulo;
            }

            matrizAdj = new int[numVertices][numVertices];
            int numArestas = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < numArestas; i++) {
                String[] partes = sc.nextLine().split(" ");
                int origem = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);
                int peso = Integer.parseInt(partes[2]);
                matrizAdj[origem][destino] = peso;
                matrizAdj[destino][origem] = peso;
            }
            sc.close();
            System.out.println("Arquivo lido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    static void gravarArquivo() {
        try {
            FileWriter writer = new FileWriter("grafo_sp.txt");
            writer.write(tipoGrafo + "\n");
            writer.write(numVertices + "\n");
            for (int i = 0; i < numVertices; i++) {
                writer.write(i + " \"" + rotulos[i] + "\"\n");
            }
            int totalArestas = 0;
            StringBuilder arestas = new StringBuilder();
            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (matrizAdj[i][j] > 0) {
                        arestas.append(i).append(" ").append(j).append(" ").append(matrizAdj[i][j]).append("\n");
                        totalArestas++;
                    }
                }
            }
            writer.write(totalArestas + "\n");
            writer.write(arestas.toString());
            writer.close();
            System.out.println("Arquivo salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    static void inserirVertice() {
        scanner.nextLine(); // limpa buffer
        System.out.print("Nome da rua: ");
        String novo = scanner.nextLine();

        rotulos = Arrays.copyOf(rotulos, numVertices + 1);
        rotulos[numVertices] = novo;

        int[][] novaMatriz = new int[numVertices + 1][numVertices + 1];
        if (matrizAdj != null) {
            for (int i = 0; i < numVertices; i++)
                for (int j = 0; j < numVertices; j++)
                    novaMatriz[i][j] = matrizAdj[i][j];
        }
        matrizAdj = novaMatriz;

        System.out.println("Vértice inserido");
        System.out.println("ID do novo vértice: " + numVertices);

        numVertices++;
    }

    static void inserirAresta() {
        try {
            System.out.print("Origem: ");
            int o = scanner.nextInt();
            System.out.print("Destino: ");
            int d = scanner.nextInt();
            System.out.print("Peso em km: ");
            int p = scanner.nextInt();

            if (o < 0 || o >= numVertices || d < 0 || d >= numVertices) {
                System.out.println("Erro vértice de origem ou destino inválido");
                return;
            }

            matrizAdj[o][d] = p;
            matrizAdj[d][o] = p;
            System.out.println("Aresta inserida");
        } catch (Exception e) {
            System.out.println("Erro ao inserir aresta: " + e.getMessage());
            scanner.nextLine(); // Limpa o buffer
        }
    }

    static void removerVertice() {
        try {
            System.out.print("ID do vértice a ser removido: ");
            int id = scanner.nextInt();
    
            if (id < 0 || id >= numVertices) {
                System.out.println("Erro: vértice inválido");
                return;
            }
    
            // Cria nova matriz com uma linha e coluna a menos
            int[][] novaMatriz = new int[numVertices - 1][numVertices - 1];
            String[] novosRotulos = new String[numVertices - 1];
    
            int iNova = 0;
            for (int i = 0; i < numVertices; i++) {
                if (i == id) continue;
                int jNova = 0;
                for (int j = 0; j < numVertices; j++) {
                    if (j == id) continue;
                    novaMatriz[iNova][jNova] = matrizAdj[i][j];
                    jNova++;
                }
                novosRotulos[iNova] = rotulos[i];
                iNova++;
            }
    
            matrizAdj = novaMatriz;
            rotulos = novosRotulos;
            numVertices--;
    
            System.out.println("Vértice removido definitivamente.");
        } catch (Exception e) {
            System.out.println("Erro ao remover vértice: " + e.getMessage());
            scanner.nextLine();
        }
    }


    static void removerAresta() {
        try {
            System.out.print("Origem: ");
            int o = scanner.nextInt();
            System.out.print("Destino: ");
            int d = scanner.nextInt();

            if (o < 0 || o >= numVertices || d < 0 || d >= numVertices) {
                System.out.println("Erro vértice de origem ou destino inválido");
                return;
            }

            matrizAdj[o][d] = 0;
            matrizAdj[d][o] = 0;
            System.out.println("Aresta removida");
        } catch (Exception e) {
            System.out.println("Erro ao remover aresta: " + e.getMessage());
            scanner.nextLine();
        }
    }

    static void mostrarGrafo() {
        for (int i = 0; i < numVertices; i++) {
            System.out.print(i + " - " + rotulos[i] + ": ");
            for (int j = 0; j < numVertices; j++) {
                if (matrizAdj[i][j] > 0)
                    System.out.print("[" + j + " - " + matrizAdj[i][j] + "km] ");
            }
            System.out.println();
        }
    }

    static void mostrarMatriz() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(matrizAdj[i][j] + " ");
            }
            System.out.println();
        }
    }

    static void verificarConexidade() {
        boolean[] visitado = new boolean[numVertices];

        if (numVertices == 0) {
            System.out.println("O grafo esta vazio");
            return;
        }

        dfs(0, visitado);

        boolean conexo = true;
        for (int i = 0; i < numVertices; i++) {
            if (!visitado[i]) {
                conexo = false;
                break;
            }
        }

        System.out.println(conexo ? "O grafo é conexo." : "O grafo é desconexo.");
    }

    static void dfs(int v, boolean[] visitado) {
        visitado[v] = true;
        for (int i = 0; i < numVertices; i++) {
            if (matrizAdj[v][i] > 0 && !visitado[i])
                dfs(i, visitado);
        }
    }
    static void mostrarConteudoArquivo() {
    System.out.println("Tipo de grafo: " + tipoGrafo);
    System.out.println("Número de vértices: " + numVertices);
    for (int i = 0; i < numVertices; i++)
        System.out.println(i + " \"" + rotulos[i] + "\"");
    int totalArestas = 0;
    for (int i = 0; i < numVertices; i++)
        for (int j = i + 1; j < numVertices; j++)
            if (matrizAdj[i][j] > 0) {
                totalArestas++;
                System.out.println(i + " " + j + " " + matrizAdj[i][j]);
            }
    System.out.println("Número total de arestas: " + totalArestas);
    }

}
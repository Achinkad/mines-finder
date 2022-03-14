import javax.swing.*;
import java.util.Random;

public class CampoMinado {
    public static final int VAZIO = 0;
    public static final int TAPADO = 9;
    public static final int DUVIDA = 10;
    public static final int MARCADO = 11;
    public static final int REBENTADO = 12;

    private boolean[][] minas;
    private int [][] estado;

    private int largura;
    private int altura;
    private int numMinas;

    private boolean primeiraJogada = true;
    private boolean jogoTerminado;
    private boolean jogadorDerrotado;

    private long instanteInicioJogo;
    private long duracaoJogo;

    public CampoMinado(int largura, int altura, int numMinas) {
        this.largura = largura;
        this.altura = altura;
        this.numMinas = numMinas;
        minas = new boolean[largura][altura];
        estado = new int[largura][altura];
        jogadorDerrotado = false;
        jogoTerminado = false;

        for (var x = 0; x < largura; ++x) {
            for (var y = 0; y < altura; ++y) {
                estado[x][y] = TAPADO;
            }
        }
    }

    private int contarMinasVizinhas(int x, int y) {
        var numMinasVizinhas = 0;

        for (var i = Math.max(0, x - 1); i < Math.min(largura, x + 2); ++i) {
            for (var j = Math.max(0, y - 1); j < Math.min(altura, y + 2); ++j) {
                if (minas[i][j]) {
                    ++numMinasVizinhas;
                }
            }
        }

        return numMinasVizinhas;
    }

    public void revelarQuadriculaVizinhas(int x, int y) {
        for (var i = Math.max(0, x - 1); i < Math.min(largura, x + 2); ++i) {
            for (var j = Math.max(0, y - 1); j < Math.min(altura, y + 2); ++j) {
                if (!minas[x][y]) {
                    revelarQuadricula(i, j);
                }
            }
        }
    }

    public void revelarQuadricula(int x, int y) {
        if (jogoTerminado || estado[x][y] < TAPADO) {
            return;
        }

        if (primeiraJogada) {
            colocarMinas(x, y);
            primeiraJogada = false;
            instanteInicioJogo = System.currentTimeMillis();
        }

        if (minas[x][y]) {
            estado[x][y] = REBENTADO;
            jogoTerminado = true;
            jogadorDerrotado = true;
            duracaoJogo = System.currentTimeMillis() - instanteInicioJogo;
        }

        if (isVitoria()) {
            jogoTerminado = true;
            duracaoJogo = System.currentTimeMillis() - instanteInicioJogo;
        }

        estado[x][y] = contarMinasVizinhas(x, y);
        if (estado[x][y] == VAZIO) {
            revelarQuadriculaVizinhas(x, y);
        }
    }

    public void marcarComoTendoMina(int x, int y) {
        if (estado[x][y] == TAPADO || estado[x][y] == DUVIDA) {
            estado[x][y] = MARCADO;
        }
    }

    public void marcarComoSuspeita(int x, int y) {
        if (estado[x][y] == TAPADO || estado[x][y] == MARCADO) {
            estado[x][y] = DUVIDA;
        }
    }

    public void desmarcarQuadricula(int x, int y) {
        if (estado[x][y] == MARCADO || estado[x][y] == DUVIDA) {
            estado[x][y] = TAPADO;
        }
    }

    private void colocarMinas(int exceptoX, int exceptoY) {
        var aleatorio = new Random();
        var x = 0;
        var y = 0;
        for (var i = 0; i < numMinas; ++i) {
            do {
                x = aleatorio.nextInt(largura);
                y = aleatorio.nextInt(altura);
            } while (minas[x][y] || (x == exceptoX && y == exceptoY));
            minas[x][y] = true;
        }
    }

    private boolean isVitoria() {
        for (int i = 0; i < largura; ++i) {
            for (var j = 0 ; j < altura; ++j) {
                if (!minas[i][j] && estado[i][j] >= TAPADO) {
                    return false;
                }
            }
        }
        return true;
    }

    public long getDuracaoJogo() {
        if (primeiraJogada) {
            return 0;
        }

        if (!jogoTerminado) {
            return System.currentTimeMillis() - instanteInicioJogo;
        }

        return duracaoJogo;
    }

    public int getEstadoQuadricula(int x, int y) {
        return estado[x][y];
    }

    public boolean hasMina(int x, int y) {
        return minas[x][y];
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public boolean isJogoDerrotado() {
        return jogadorDerrotado;
    }

    public boolean isJogoTerminado() {
        return jogoTerminado;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaDeJogo extends JFrame{
    private JPanel painelJogo;
    private BotaoCampoMinado[][] botoes;
    private CampoMinado campoMinado;
    private TabelaRecordes recordes;

    public JanelaDeJogo(CampoMinado campoMinado, TabelaRecordes tabela) {
        this.campoMinado = campoMinado;
        this.recordes = tabela;

        var largura = campoMinado.getLargura();
        var altura = campoMinado.getAltura();

        this.botoes = new BotaoCampoMinado[largura][altura];

        painelJogo.setLayout(new GridLayout(altura, largura));

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3) {
                    return;
                }

                var botao = (BotaoCampoMinado) e.getSource();

                var x = botao.getLinha();
                var y = botao.getColuna();

                var estadoQuadricula = campoMinado.getEstadoQuadricula(x, y);

                if (estadoQuadricula == CampoMinado.TAPADO) {
                    campoMinado.marcarComoTendoMina(x, y);
                } else if (estadoQuadricula == CampoMinado.MARCADO) {
                    campoMinado.marcarComoSuspeita(x, y);
                } else if (estadoQuadricula == CampoMinado.DUVIDA) {
                    campoMinado.desmarcarQuadricula(x, y);
                }

                atualizarEstadoBotoes();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                var btn = (BotaoCampoMinado) e.getSource();

                var linha = btn.getLinha();
                var coluna = btn.getColuna();

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> botoes[linha][--coluna < 0 ? altura - 1 : coluna].requestFocus();
                    case KeyEvent.VK_DOWN -> botoes[linha][(coluna + 1) % altura].requestFocus();
                    case KeyEvent.VK_LEFT -> botoes[--linha < 0 ? largura - 1 : linha][coluna].requestFocus();
                    case KeyEvent.VK_RIGHT -> botoes[(linha + 1) % largura][coluna].requestFocus();
                    case KeyEvent.VK_M -> {
                        switch (campoMinado.getEstadoQuadricula(linha, coluna)) {
                            case CampoMinado.TAPADO -> campoMinado.marcarComoTendoMina(linha, coluna);
                            case CampoMinado.MARCADO -> campoMinado.marcarComoSuspeita(linha, coluna);
                            case CampoMinado.DUVIDA -> campoMinado.desmarcarQuadricula(linha, coluna);
                        }
                        atualizarEstadoBotoes();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        for (int coluna = 0; coluna < altura; ++coluna) {
            for (int linha = 0; linha < largura; ++linha) {
                botoes[linha][coluna] = new BotaoCampoMinado(linha, coluna);
                botoes[linha][coluna].addActionListener(this::btnCampoMinadoActionPerformed);
                botoes[linha][coluna].addMouseListener(mouseListener);
                botoes[linha][coluna].addKeyListener(keyListener);
                painelJogo.add(botoes[linha][coluna]);
            }
        }

        setContentPane(painelJogo);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();

        setVisible(true);
    }

    public void btnCampoMinadoActionPerformed(ActionEvent e){
        var btn = (BotaoCampoMinado) e.getSource();

        int x = btn.getLinha();
        int y = btn.getColuna();

        campoMinado.revelarQuadricula(x, y);

        atualizarEstadoBotoes();

        if (campoMinado.isJogoTerminado()) {
            if (campoMinado.isJogoDerrotado()) {
                JOptionPane.showMessageDialog(null, "Oh, rebentou uma mina", "Perdeu!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Parabéns. Conseguiu descobrir todas as minas em " + (campoMinado.getDuracaoJogo()/1000) + " segundos", "Vitória", JOptionPane.INFORMATION_MESSAGE);

                boolean novoRecorde = campoMinado.getDuracaoJogo() < recordes.getTempoJogo();

                if (novoRecorde) {
                    String nome = JOptionPane.showInputDialog("Introduza o seu nome");
                    recordes.setRecorde(nome, campoMinado.getDuracaoJogo());
                }

            }
            setVisible(false);
        }
    }

    private void atualizarEstadoBotoes() {
        for (int i = 0; i < campoMinado.getLargura(); i++) {
            for (int j = 0; j < campoMinado.getAltura(); j++) {
                botoes[i][j].setEstado(campoMinado.getEstadoQuadricula(i, j));
            }
        }
    }
}

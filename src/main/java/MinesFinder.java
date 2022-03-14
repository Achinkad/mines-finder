import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinesFinder extends JFrame {
    private JPanel painelPrincipal;
    private JButton jogoFácilButton;
    private JButton jogoMédioButton;
    private JButton jogoDifícilButton;
    private JButton sairButton;
    private JLabel lblJogadorFacil;
    private JLabel lblJogadorMedio;
    private JLabel lblJogadorDificil;
    private JLabel lblPontucaoFacil;
    private JLabel lblPontuacaoMedio;
    private JLabel lblPontuacaoDificil;

    private TabelaRecordes recordesFaceis;
    private TabelaRecordes recordesMedio;
    private TabelaRecordes recordesDifil;

    public MinesFinder(String title) {
        super(title);

        recordesFaceis = new TabelaRecordes();
        recordesMedio = new TabelaRecordes();
        recordesDifil = new TabelaRecordes();

        lerRecordesDoDisco();

        lblJogadorFacil.setText(recordesFaceis.getNome());
        lblPontucaoFacil.setText(Long.toString(recordesFaceis.getTempoJogo()/1000));
        lblJogadorMedio.setText(recordesMedio.getNome());
        lblPontuacaoMedio.setText(Long.toString(recordesMedio.getTempoJogo()/1000));
        lblJogadorDificil.setText(recordesDifil.getNome());
        lblPontuacaoDificil.setText(Long.toString(recordesDifil.getTempoJogo()/1000));

        recordesFaceis.addTabelaRecordesListeners(new TabelaRecordesListener() {
            @Override
            public void recordesAtualizados(TabelaRecordes recordes) {
                recordesFacilAtualizado(recordes);
            }
        });

        recordesMedio.addTabelaRecordesListeners(new TabelaRecordesListener() {
            @Override
            public void recordesAtualizados(TabelaRecordes recordes) {
                recordesMedioAtualizado(recordes);
            }
        });

        recordesDifil.addTabelaRecordesListeners(new TabelaRecordesListener() {
            @Override
            public void recordesAtualizados(TabelaRecordes recordes) {
                recordesDifilAtualizado(recordes);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(painelPrincipal);

        sairButton.addActionListener(this::btnSairActionPerformed);
        jogoFácilButton.addActionListener(this::btnFacilActionPerformed);
        jogoMédioButton.addActionListener(this::btnMedioActionPerformed);
        jogoDifícilButton.addActionListener(this::btnDificilActionPerformed);

        pack();
    }

    private void recordesFacilAtualizado(TabelaRecordes recordes) {
        lblJogadorFacil.setText(recordes.getNome());
        lblPontucaoFacil.setText(Long.toString(recordes.getTempoJogo()/1000));
    }

    private void recordesMedioAtualizado(TabelaRecordes recordes) {
        lblJogadorMedio.setText(recordes.getNome());
        lblPontuacaoMedio.setText(Long.toString(recordes.getTempoJogo()/1000));
    }

    private void recordesDifilAtualizado(TabelaRecordes recordes) {
        lblJogadorDificil.setText(recordes.getNome());
        lblPontuacaoDificil.setText(Long.toString(recordes.getTempoJogo()/1000));
    }

    private void btnFacilActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(9,9,10), recordesFaceis);
        janela.setVisible(true);
    }

    private void btnMedioActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(16,16,40), recordesMedio);
        janela.setVisible(true);
    }

    private void btnDificilActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(16,30,90), recordesDifil);
        janela.setVisible(true);
    }

    private void btnSairActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void guardarRecordesDisco() {
        ObjectOutputStream oos = null;
        try {
            File f = new File(System.getProperty("user.home")+File.separator+"minesfinder.recordes");
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(recordesFaceis);
            oos.writeObject(recordesMedio);
            oos.writeObject(recordesDifil);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lerRecordesDoDisco() {
        ObjectInputStream ois = null;
        File f = new File(System.getProperty("user.home")+File.separator+"minesfinder.recordes");
        if (f.canRead()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(f));
                recordesFaceis = (TabelaRecordes) ois.readObject();
                recordesMedio = (TabelaRecordes) ois.readObject();
                recordesDifil = (TabelaRecordes) ois.readObject();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new MinesFinder("Mines Finder").setVisible(true);
    }
}

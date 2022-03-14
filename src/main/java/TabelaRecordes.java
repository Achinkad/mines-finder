import java.io.Serializable;
import java.util.ArrayList;

public class TabelaRecordes implements Serializable {
    private String nome;
    private long tempoJogo;
    private transient ArrayList<TabelaRecordesListener> listeners;

    public TabelaRecordes() {
        nome = "Anónimo";
        tempoJogo = 999999;
        listeners = new ArrayList<>();
    }

    public void addTabelaRecordesListeners(TabelaRecordesListener list) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(list);
    }

    public void removeTabelaRecordesListener(TabelaRecordesListener list) {
        if (listeners != null) listeners.remove(list);
    }

    private void notifyRecordesAtualizados() {
        if (listeners != null) {
            for (TabelaRecordesListener list : listeners) {
                list.recordesAtualizados(this);
            }
        }
    }

    public void setRecorde(String n, long t) {
        if (t < tempoJogo) {
            tempoJogo = t;
            nome = n;
            notifyRecordesAtualizados();
        }
    }

    public long getTempoJogo() {
        return tempoJogo;
    }

    public String getNome() {
        return nome;
    }
}

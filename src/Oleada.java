import java.util.ArrayList;

public class Oleada {
    private int numeroOleada;
    private ArrayList<Soldado> enemigos;
    private String descripcion;

    public Oleada(int numero, String descripcion, Mascota mascota) {
        this.numeroOleada = numero;
        this.descripcion = descripcion;
        this.enemigos = new ArrayList<>();
        configurarEnemigos(mascota.getNivel());
    }

    private void configurarEnemigos(int nivelMascota) {
        int escala = (numeroOleada - 1) / 5;
        int nivelEfectivo = nivelMascota + escala;

        switch(numeroOleada) {
            case 1:  enemigos.add(new Soldado("Soldado", nivelEfectivo)); break;
            case 2:
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 3:
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 4:  enemigos.add(new Soldado("Juggernaut", nivelEfectivo)); break;
            case 5:
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 6:
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                break;
            case 7:  enemigos.add(new Soldado("Tanque", nivelEfectivo)); break;
            case 8:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 9:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 10:
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 11:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                break;
            case 12:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                break;
            case 13:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 14:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                break;
            case 15:
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 16:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 17:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                break;
            case 18:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                break;
            case 19:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            case 20:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
            default:
                enemigos.add(new Soldado("Tanque", nivelEfectivo));
                enemigos.add(new Soldado("Juggernaut", nivelEfectivo));
                enemigos.add(new Soldado("Soldado", nivelEfectivo));
                break;
        }
    }

    public int getNumeroOleada() { return numeroOleada; }
    public String getDescripcion() { return descripcion; }
    public ArrayList<Soldado> getEnemigos() { return enemigos; }
    public int getCantidadEnemigos() { return enemigos.size(); }

    public void mostrarEnemigos() {
        System.out.println("\n" + descripcion);
        System.out.println("Enemigos en esta oleada: " + enemigos.size());
        for (int i = 0; i < enemigos.size(); i++) {
            System.out.print((i+1) + ". ");
            enemigos.get(i).mostrarInfo();
        }
    }
}
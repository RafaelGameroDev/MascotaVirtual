public class TemporizadorMascota implements Runnable {
    private Mascota mascota;
    private boolean activo = true;

    public TemporizadorMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    @Override
    public void run() {
        int contadorCiclos = 0;
        while (activo && mascota.isVivo()) {
            try {
                // Comprobamos cada 3 segundos para que sea receptivo al cierre,
                // pero aplicamos la reducción de higiene cada 90 segundos (30 ciclos de 3s).
                Thread.sleep(3000);
                if (!activo) break;

                contadorCiclos++;

                // Reducciones continuas pequeñas (cada 3s)
                mascota.reducirPanza(1);
                mascota.reducirDiversion(1);
                mascota.reducirCansancio(1);

                // Cada 90 segundos (90s / 3s = 30 ciclos) hace caca y baja 30% la higiene
                if (contadorCiclos >= 30) {
                    mascota.reducirHigiene(30);
                    contadorCiclos = 0;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void detener() {
        activo = false;
    }
}
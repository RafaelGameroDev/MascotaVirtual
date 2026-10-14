import java.util.ArrayList;
import java.util.Scanner;

public class SistemaCombate {
    private Mascota mascota;
    private Scanner scanner;
    private Oleada oleadaActual;
    private Guardado guardado;

    private int oleadaMaximaDesbloqueada = 1;
    private static final int OLEADAS_TOTALES = 20;

    public SistemaCombate(Mascota mascota) {
        this.mascota = mascota;
        this.scanner = new Scanner(System.in);
        this.guardado = Guardado.getInstancia();
        this.oleadaMaximaDesbloqueada = guardado.cargarProgresoCombate(guardado.getSlotActivo());
    }

    public int getOleadaMaximaDesbloqueada() {
        return oleadaMaximaDesbloqueada;
    }

    public void mostrarOleadasDisponibles() {
        System.out.println("\n=== OLEADAS DISPONIBLES ===");
        System.out.println("Progreso: " + oleadaMaximaDesbloqueada + "/" + OLEADAS_TOTALES + " desbloqueadas\n");

        for (int i = 1; i <= oleadaMaximaDesbloqueada; i++) {
            String estado = (i == oleadaMaximaDesbloqueada && i < OLEADAS_TOTALES) ? " [SIGUIENTE]" : "";
            String completado = (i < oleadaMaximaDesbloqueada) ? " ✓" : "";
            System.out.println(i + ". Oleada " + i + estado + completado);
        }

        if (oleadaMaximaDesbloqueada >= OLEADAS_TOTALES) {
            System.out.println("\n¡TODAS LAS OLEADAS COMPLETADAS! Eres un maestro.");
        }
    }

    public void iniciarOleada(int numeroOleada) {
        if (!mascota.isVivo()) {
            System.out.println("Tu mascota esta muerta. No puede combatir.");
            return;
        }

        if (numeroOleada > oleadaMaximaDesbloqueada) {
            System.out.println("¡Oleada bloqueada! Debes completar la oleada " + oleadaMaximaDesbloqueada + " primero.");
            return;
        }

        if (mascota.getCansancio() < 20) {
            System.out.println("Tu mascota esta muy cansada para pelear. ¡Hazla descansar!");
            return;
        }

        oleadaActual = new Oleada(numeroOleada, "Oleada " + numeroOleada, mascota);
        oleadaActual.mostrarEnemigos();

        System.out.println("\nPresiona ENTER para iniciar el combate en tiempo real...");
        if (scanner.hasNextLine()) scanner.nextLine();

        boolean victoria = ejecutarCombateEnTiempoReal(numeroOleada);

        if (victoria) {
            System.out.println("\n>>> VICTORIA EN LA OLEADA " + numeroOleada + " <<<");

            int expGanada = 40 + (numeroOleada * 15);
            System.out.println("Exp ganada: +" + expGanada);
            ganarExperiencia(expGanada);

            if (numeroOleada == oleadaMaximaDesbloqueada && numeroOleada < OLEADAS_TOTALES) {
                oleadaMaximaDesbloqueada++;
                guardado.guardarProgresoCombate(oleadaMaximaDesbloqueada);
                System.out.println(">>> ¡OLEADA " + oleadaMaximaDesbloqueada + " DESBLOQUEADA! <<<");
            }

            mascota.curar();
            System.out.println("Tu mascota ha recuperado toda su salud.");
        } else {
            System.out.println("\n>>> DERROTA EN LA OLEADA " + numeroOleada + " <<<");
            mascota.penalizarDerrota();
        }
    }

    private boolean ejecutarCombateEnTiempoReal(int numeroOleada) {
        ArrayList<Soldado> enemigos = oleadaActual.getEnemigos();

        System.out.println("\n========== COMBATE EN TIEMPO REAL INICIADO ==========");
        System.out.println("[Mascota] ATK: " + mascota.getAtaque() + " | DEF: " + mascota.getDefensa() + " | VEL: " + mascota.getVelocidad());

        // Enfriamiento base de 2 segundos (2000 ms) modificado por velocidad.
        // LÍMITE: Math.max(333.33, ...) evita que baje de 333.33ms (máximo 3 ataques por segundo).
        double cdMaxMascota = Math.max(333.33, 2000.0 * (10.0 / Math.max(1, mascota.getVelocidad())));
        double cdActualMascota = cdMaxMascota;

        double[] cdMaxEnemigos = new double[enemigos.size()];
        double[] cdActualEnemigos = new double[enemigos.size()];

        for (int i = 0; i < enemigos.size(); i++) {
            Soldado enemigo = enemigos.get(i);
            cdMaxEnemigos[i] = Math.max(333.33, 2000.0 * (10.0 / Math.max(1, enemigo.getVelocidad())));
            cdActualEnemigos[i] = cdMaxEnemigos[i];
        }

        int tickMs = 100;
        int acumuladorCansancio = 0;

        while (mascota.isVivo() && hayEnemigosVivos(enemigos)) {
            try {
                Thread.sleep(tickMs);
            } catch (InterruptedException e) {}

            acumuladorCansancio += tickMs;
            if (acumuladorCansancio >= 5000) {
                mascota.reducirCansancio(1);
                acumuladorCansancio = 0;
                if (mascota.getCansancio() <= 0) {
                    System.out.println("\nTu mascota se ha quedado sin energia y colapsa...");
                    return false;
                }
            }

            // --- 1. GESTIONAR ENFRIAMIENTO DE LA MASCOTA ---
            cdActualMascota -= tickMs;
            if (cdActualMascota <= 0) {
                Soldado objetivo = seleccionarObjetivo(enemigos);
                if (objetivo != null) {
                    System.out.println("\n[TURBO] ¡La mascota ataca a " + objetivo.getNombre() + "!");
                    int danio = mascota.atacar();
                    objetivo.recibirDanio(danio);

                    if (!objetivo.estaVivo()) {
                        System.out.println("  ¡" + objetivo.getNombre() + " DESTRUIDO!");
                    }
                }
                cdActualMascota = cdMaxMascota;
            }

            if (!hayEnemigosVivos(enemigos)) {
                return true;
            }

            // --- 2. GESTIONAR ENFRIAMIENTOS DE LOS ENEMIGOS ---
            for (int i = 0; i < enemigos.size(); i++) {
                Soldado enemigo = enemigos.get(i);
                if (enemigo.estaVivo()) {
                    cdActualEnemigos[i] -= tickMs;
                    if (cdActualEnemigos[i] <= 0) {
                        int danioEnemigo = enemigo.atacar();
                        System.out.println("\n[¡ALERTA!] " + enemigo.getNombre() + " ataca e inflige " + danioEnemigo + " de daño.");
                        mascota.recibirDanio(danioEnemigo);

                        if (!mascota.isVivo()) {
                            System.out.println("\nTu mascota ha caido en combate...");
                            return false;
                        }
                        cdActualEnemigos[i] = cdMaxEnemigos[i];
                    }
                }
            }
        }

        return !hayEnemigosVivos(enemigos);
    }

    private Soldado seleccionarObjetivo(ArrayList<Soldado> enemigos) {
        Soldado menorSalud = null;
        int minSalud = Integer.MAX_VALUE;

        for (Soldado e : enemigos) {
            if (e.estaVivo() && e.getSalud() < minSalud) {
                minSalud = e.getSalud();
                menorSalud = e;
            }
        }
        return menorSalud;
    }

    private boolean hayEnemigosVivos(ArrayList<Soldado> enemigos) {
        for (Soldado e : enemigos) {
            if (e.estaVivo()) return true;
        }
        return false;
    }

    private void ganarExperiencia(int exp) {
        try {
            java.lang.reflect.Field fieldExp = mascota.getClass().getDeclaredField("exp");
            fieldExp.setAccessible(true);
            int expActual = (int) fieldExp.get(mascota);
            fieldExp.set(mascota, expActual + exp);

            java.lang.reflect.Method nivelChequeo = mascota.getClass().getDeclaredMethod("nivelChequeo");
            nivelChequeo.setAccessible(true);
            nivelChequeo.invoke(mascota);
        } catch (Exception e) {
            System.out.println("Error al ganar experiencia");
        }
    }
}
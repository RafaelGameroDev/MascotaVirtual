public class Mascota {
    private int felicidad = 50;
    private int panza = 50;
    private int diversion = 50;
    private int higiene = 100;
    private int exp = 0;
    private int nivel = 1;
    private int cansancio = 100;
    private int salud = 100;

    private int ataque = 10;
    private int defensa = 5;
    private int velocidad = 10;
    private int saludMax = 100;

    private String etapaEvolutiva = "Larvoideo";
    private boolean vivo = true;

    // Control de límite de entrenamiento diario (máximo 3 veces)
    private int entrenamientosRealizadosHoy = 0;

    private Guardado guardado;

    public Mascota() {
        this.guardado = Guardado.getInstancia();
    }

    private void autoGuardar() {
        guardado.guardarMascota(this);
    }

    public int getFelicidad() {
        calcularFelicidad();
        return felicidad;
    }
    public int getPanza() { return panza; }
    public int getDiversion() { return diversion; }
    public int getHigiene() { return higiene; }
    public int getExp() { return exp; }
    public int getNivel() { return nivel; }
    public int getCansancio() { return cansancio; }
    public int getSalud() { return salud; }
    public int getSaludMax() { return saludMax; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
    public String getEtapaEvolutiva() { return etapaEvolutiva; }
    public boolean isVivo() { return vivo; }

    private void calcularFelicidad() {
        int atributosBuenos = 0;
        int totalDesviacion = 0;

        if (panza >= 75) atributosBuenos++;
        else totalDesviacion += (75 - panza);

        if (diversion >= 75) atributosBuenos++;
        else totalDesviacion += (75 - diversion);

        if (higiene >= 75) atributosBuenos++;
        else totalDesviacion += (75 - higiene);

        if (cansancio >= 75) atributosBuenos++;
        else totalDesviacion += (75 - cansancio);

        double felicidadBase = atributosBuenos * 25;
        double penalizacion = totalDesviacion * 0.4;
        felicidad = (int) Math.max(0, Math.min(100, felicidadBase - penalizacion));
    }

    private void verificarMuerte() {
        if (panza <= 0 || salud <= 0 || cansancio <= 0 || higiene <= 0) {
            vivo = false;
            System.out.println("¡OH NO! Tu mascota ha muerto...");
            autoGuardar();
        }
    }

    private boolean accionPermitida(int nivelAccion) {
        if (!vivo) return false;

        int nivelMaximo = 1;
        if (etapaEvolutiva.equals("Aliendescente")) {
            nivelMaximo = 2;
        } else if (etapaEvolutiva.equals("Alphalien")) {
            nivelMaximo = 3;
        }

        if (nivelAccion <= nivelMaximo) {
            return true;
        } else {
            System.out.println("Tu mascota aun no tiene suficiente nivel para esta accion. Evoluciona a " +
                    (nivelAccion == 2 ? "Aliendescente" : "Alphalien") + " para desbloquearla.");
            return false;
        }
    }

    private boolean verificarLimiteEntrenamiento() {
        if (entrenamientosRealizadosHoy >= 3) {
            System.out.println("\n[AVISO] ¡Has alcanzado el límite de 3 entrenamientos permitidos para hoy! Vuelve más tarde.");
            return false;
        }
        return true;
    }

    public void penalizarDerrota() {
        System.out.println("\n¡DERROTA! Tu mascota se viene abajo...");
        panza = Math.max(panza - 10, 0);
        diversion = Math.max(diversion - 10, 0);
        higiene = Math.max(higiene - 10, 0);
        cansancio = Math.max(cansancio - 10, 0);
        salud = Math.max(salud - 10, 0);
        System.out.println("-10 en todas las estadisticas");
        autoGuardar();
        verificarMuerte();
    }

    private void nivelChequeo() {
        int expNecesaria = (int)((nivel + 0.5) * 100);
        if (exp >= expNecesaria) {
            nivel++;
            exp = 0;

            int bonus = 0;
            if (etapaEvolutiva.equals("Larvoideo")) {
                bonus = 1;
            } else if (etapaEvolutiva.equals("Aliendescente")) {
                bonus = 2;
            } else if (etapaEvolutiva.equals("Alphalien")) {
                bonus = 3;
            }

            ataque += bonus;
            defensa += bonus;
            velocidad += bonus;
            saludMax += bonus;
            salud = saludMax;

            System.out.println("¡Subida de nivel! Tu bicho ahora es nivel " + nivel + "!");
            System.out.println("Bonificacion de +" + bonus + " a Ataque, Defensa, Velocidad y Salud Maxima");

            actualizarEvolucion();
            autoGuardar();
        }
    }

    private void actualizarEvolucion() {
        String etapaAnterior = etapaEvolutiva;

        if (nivel >= 10) {
            etapaEvolutiva = "Alphalien";
        } else if (nivel >= 4) {
            etapaEvolutiva = "Aliendescente";
        } else {
            etapaEvolutiva = "Larvoideo";
        }

        if (!etapaEvolutiva.equals(etapaAnterior)) {
            System.out.println("============== ¡INCREIBLE! ==============");
            System.out.println("¡Tu bicho ha evolucionado a: " + etapaEvolutiva + "!");
            System.out.println("Ataque: " + ataque + " | Defensa: " + defensa + " | Velocidad: " + velocidad + " | Salud: " + saludMax);
            System.out.println("=========================================");
            autoGuardar();
        }
    }

    public void comerPoco() {
        if (!accionPermitida(1)) return;
        panza = Math.min(panza + 25, 100);
        System.out.println("Le diste un snack. Panza al " + panza + "%.");
        higiene = Math.max(higiene - 5, 0);
        exp += 5;
        nivelChequeo();
        verificarMuerte();
        autoGuardar();
    }

    public void comerMucho() {
        if (!accionPermitida(2)) return;
        panza = Math.min(panza + 50, 100);
        System.out.println("Le diste un buen plato de comida. Panza al " + panza + "%.");
        higiene = Math.max(higiene - 10, 0);
        exp += 12;
        nivelChequeo();
        verificarMuerte();
        autoGuardar();
    }

    public void comerLleno() {
        if (!accionPermitida(3)) return;
        panza = 100;
        System.out.println("¡Tu mascota esta completamente llena!");
        higiene = Math.max(higiene - 15, 0);
        exp += 25;
        nivelChequeo();
        verificarMuerte();
        autoGuardar();
    }

    // Acciones de juego / entrenamiento limitadas a 3 veces al día
    public void jugarPoco() {
        if (!accionPermitida(1)) return;
        if (!verificarLimiteEntrenamiento()) return;

        diversion = Math.min(diversion + 25, 100);
        cansancio = Math.max(cansancio - 15, 0);
        entrenamientosRealizadosHoy++;
        System.out.println("Jugaste un ratito (Entrenamiento " + entrenamientosRealizadosHoy + "/3). Diversion: " + diversion + "%. Energia: " + cansancio + "%.");
        exp += 8;
        nivelChequeo();
        verificarMuerte();
        autoGuardar();
    }

    public void jugarNormal() {
        if (!accionPermitida(2)) return;
        if (!verificarLimiteEntrenamiento()) return;

        diversion = Math.min(diversion + 50, 100);
        cansancio = Math.max(cansancio - 25, 0);
        entrenamientosRealizadosHoy++;
        System.out.println("¡Buen rato de juego! (Entrenamiento " + entrenamientosRealizadosHoy + "/3). Diversion: " + diversion + "%. Energia: " + cansancio + "%.");
        exp += 18;
        nivelChequeo();
        verificarMuerte();
        autoGuardar();
    }

    public void jugarMucho() {
        if (!accionPermitida(3)) return;
        if (!verificarLimiteEntrenamiento()) return;

        diversion = 100;
        cansancio = Math.max(cansancio - 40, 0);
        entrenamientosRealizadosHoy++;
        System.out.println("¡Sesion intensa de juego! (Entrenamiento " + entrenamientosRealizadosHoy + "/3). Diversion al maximo. Energia: " + cansancio + "%.");
        exp += 30;
        nivelChequeo();
        verificarMuerte();
        autoGuardar();
    }

    public void descansar() {
        if (!vivo) return;
        cansancio = 100;
        System.out.println("¡Vaya sieston! ¡Energia recuperada!");
        exp += 5;
        nivelChequeo();
        autoGuardar();
    }

    public void curar() {
        if (!vivo) return;
        salud = saludMax;
        System.out.println("¡La mascota ha recuperado toda su salud!");
        autoGuardar();
    }

    public void recibirDanio(int danio) {
        int danioReal = Math.max(1, danio - (defensa / 2));
        salud = Math.max(salud - danioReal, 0);
        System.out.println("¡Vaya leche! La mascota recibe " + danioReal + " de dano. Salud: " + salud + "/" + saludMax);
        autoGuardar();
        verificarMuerte();
    }

    public int atacar() {
        int danioBase = ataque + (nivel * 2);
        int variacion = (int)(Math.random() * 10) - 5;
        int danioTotal = Math.max(1, danioBase + variacion);
        System.out.println("¡Al ataquerl! Dano causado: " + danioTotal);
        return danioTotal;
    }

    public void reducirPanza(int cantidad) {
        if (!vivo) return;
        panza = Math.max(panza - cantidad, 0);
        autoGuardar();
        verificarMuerte();
    }

    public void reducirDiversion(int cantidad) {
        if (!vivo) return;
        diversion = Math.max(diversion - cantidad, 0);
        autoGuardar();
    }

    public void reducirHigiene(int cantidad) {
        if (!vivo) return;
        higiene = Math.max(higiene - cantidad, 0);
        System.out.println("\n[AVISO] ¡Tu mascota ha hecho caca! La higiene baja un 30% (Higiene: " + higiene + "%).");
        autoGuardar();
        verificarMuerte();
    }

    public void reducirCansancio(int cantidad) {
        if (!vivo) return;
        cansancio = Math.max(cansancio - cantidad, 0);
        autoGuardar();
        verificarMuerte();
    }

    public void mostrarEstado() {
        System.out.println("\n=== ESTADO DE LA MASCOTA ===");
        System.out.println("Etapa: " + etapaEvolutiva + " | Nivel: " + nivel);
        System.out.println("Salud: " + salud + "/" + saludMax + " | Panza: " + panza + "%");
        System.out.println("Diversion: " + diversion + "% | Energia: " + cansancio + "%");
        System.out.println("Higiene: " + higiene + "% | Felicidad: " + getFelicidad() + "%");
        System.out.println("Exp: " + exp + " | Ataque: " + ataque + " | Defensa: " + defensa + " | Velocidad: " + velocidad);
        System.out.println("Entrenamientos hoy: " + entrenamientosRealizadosHoy + "/3");
        System.out.println("=============================\n");
    }

    public void cargarDesdeBD(int felicidad, int panza, int diversion, int higiene,
                              int exp, int nivel, int cansancio, int salud,
                              int ataque, int defensa, int velocidad, int saludMax,
                              String etapaEvolutiva, boolean vivo) {
        this.felicidad = felicidad;
        this.panza = panza;
        this.diversion = diversion;
        this.higiene = higiene;
        this.exp = exp;
        this.nivel = nivel;
        this.cansancio = cansancio;
        this.salud = salud;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.saludMax = saludMax;
        this.etapaEvolutiva = etapaEvolutiva;
        this.vivo = vivo;
    }
}
-- Tabla para guardar los datos principales de la mascota en cada slot
CREATE TABLE IF NOT EXISTS mascota (
    slot INTEGER PRIMARY KEY CHECK (slot >= 1 AND slot <= 3),
    felicidad INTEGER DEFAULT 50,
    panza INTEGER DEFAULT 50,
    diversion INTEGER DEFAULT 50,
    higiene INTEGER DEFAULT 100,
    exp INTEGER DEFAULT 0,
    nivel INTEGER DEFAULT 1,
    cansancio INTEGER DEFAULT 100,
    salud INTEGER DEFAULT 100,
    ataque INTEGER DEFAULT 10,
    defensa INTEGER DEFAULT 5,
    velocidad INTEGER DEFAULT 10,
    saludMax INTEGER DEFAULT 100,
    etapaEvolutiva TEXT DEFAULT 'Larvoideo',
    vivo INTEGER DEFAULT 1,
    nombreSlot TEXT DEFAULT 'Vacío'
);

-- Tabla independiente para el progreso de las oleadas por slot
CREATE TABLE IF NOT EXISTS progreso_combate (
    slot INTEGER PRIMARY KEY CHECK (slot >= 1 AND slot <= 3),
    oleadaMaximaDesbloqueada INTEGER DEFAULT 1
);

-- Inicialización de los tres slots vacíos por defecto
INSERT OR IGNORE INTO mascota (slot, nombreSlot) VALUES (1, 'Vacío');
INSERT OR IGNORE INTO mascota (slot, nombreSlot) VALUES (2, 'Vacío');
INSERT OR IGNORE INTO mascota (slot, nombreSlot) VALUES (3, 'Vacío');

INSERT OR IGNORE INTO progreso_combate (slot) VALUES (1);
INSERT OR IGNORE INTO progreso_combate (slot) VALUES (2);
INSERT OR IGNORE INTO progreso_combate (slot) VALUES (3);

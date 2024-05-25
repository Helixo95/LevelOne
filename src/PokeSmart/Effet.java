package PokeSmart;

import java.util.Random;

public enum Effet {
    VITESSEMOINS,
    DEFENSEMOINS,
    ATTAQUEMOINS,
    ATTAQUEPLUS,
    HEAL,
    DAMAGE,
    OPENDOOR,
    OVERWALL,
    NEWWORLD,
    VICTORY,
    SWIM,
    DEATH,
    NULL;

    private static final Random RANDOM = new Random();
    public static Effet getRandomEffet() {
        Effet[] effets = Effet.values();
        return effets[RANDOM.nextInt(effets.length)];
    }
}

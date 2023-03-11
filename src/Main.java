import java.util.Random;

public class Main {
    public static int bossHealth = 7000;
    public static int bossInitialDamage = 50;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 260, 250, 300, 500, 250, 270, 300};
    public static int[] heroesDamage = {10, 15, 20, 0, 5, 10, 15, 15};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic",
            "Golem", "Lucky", "Berserk", "Thor"};

    public static boolean skipStunned = false;

    public static int berserkInitialDamage = 15;

    public static int roundNumber = 0;
    public static String message = "";

    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        message = "";
        chooseBossDefence();
        bossHits();
        damageGolem();
        healHeroes();
        heroesHit();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();

        for (int i = 0; i < heroesAttackType.length; i++) {
            int randomIndex = random.nextInt(heroesAttackType.length);
            if (randomIndex != 3) {
                // 0,1,2
                bossDefence = heroesAttackType[randomIndex];
            }

        }

    }

    public static void bossHits() {
        if (skipStunned){
            skipStunned = false;
            return;
        }
        Random rnd = new Random();
        for (int i = 0; i < heroesHealth.length; i++) {
            String hero = heroesAttackType[i];
            if (heroesHealth[i] > 0) {
                if (hero.equals("Berserk") && heroesHealth[6] > 0){
                    damageBerserk();
                    int bossDamageOnBerserk = (int) (bossDamage * 0.9);
                    heroesHealth[6] -= bossDamageOnBerserk;
                    heroesDamage[6] = berserkInitialDamage;
                    continue;
                }
                if (hero.equals("Lucky")) {

                    boolean skipDamage = rnd.nextBoolean();
                    if (skipDamage) {
                        continue;
                    }
                }
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }

    private static void healHeroes() {
        int medicHealth = heroesHealth[3];
        if (medicHealth <= 0) {
            return;
        }
        for (int i = 0; i < heroesAttackType.length; i++) {
            String hero = heroesAttackType[i];
            if (hero.equals("Medic")) {
                continue;
            }
            int health = heroesHealth[i];
            if (health > 0 && health < 100) {
                heroesHealth[i] += 40;
                System.out.println(heroesAttackType[i] + " +40hp");
                break;
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                Random random = new Random();
                String hero = heroesAttackType[i];
                if (hero.equals("Thor")){
                    skipStunned = random.nextBoolean();
                }
                int damage = heroesDamage[i];
                if (heroesAttackType[i] == bossDefence) {

                    int coefficient = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10;
                    damage = damage * coefficient;
                    message = "Critical damage: " + damage;
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        /*if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;*/
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ----------");
        /*String defence;
        if (bossDefence == null) {
            defence = "No defence";
        } else {
            defence = bossDefence;
        }*/
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
        System.out.println(message);
    }

    public static void damageGolem() {
        int golemHeals = heroesHealth[4];
        if (golemHeals <= 0) {
            bossDamage = bossInitialDamage;
            return;
        }
        bossDamage = (int) (bossInitialDamage * 0.8);
        int takenDamage = (heroesHealth.length - 1) * (int) (bossDamage * 0.25);
        golemHeals = heroesHealth[4] - takenDamage;
        System.out.println(golemHeals + "___________________");// check golem is health

        System.out.println("golem taken" + takenDamage);
    }

    public static void damageBerserk(){
        int berserkHealth = heroesHealth[6];
        int initialBerserkDamage = heroesDamage[6];
        if (berserkHealth <= 0){
            heroesDamage[6] = berserkInitialDamage;
            return;
        }
        heroesDamage[6] = initialBerserkDamage + bossDamage / 10;


    }

}

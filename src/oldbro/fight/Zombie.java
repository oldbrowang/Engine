package oldbro.fight;

class Zombie extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/zombie.gif";
        monsterWidth = 100;
        monsterHeight = 120;
        upLeftOffsetX = 50;
        upLeftOffsetY = 50;
        gifCycleDistance = 180;
        gifOriginalDirection = 1;
    }

    public Zombie(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}
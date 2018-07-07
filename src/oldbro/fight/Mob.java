package oldbro.fight;

class Mob extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/mob.gif";
        monsterWidth = 80;
        monsterHeight = 80;
        upLeftOffsetX = 40;
        upLeftOffsetY = 40;
        gifCycleDistance = 30;
        gifOriginalDirection = -1;
    }

    public Mob(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}

package oldbro.fight;

class Tinman extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/tinman.gif";
        monsterWidth = 140;
        monsterHeight = 180;
        upLeftOffsetX = 70;
        upLeftOffsetY = 70;
        gifCycleDistance = 100;
        gifOriginalDirection = -1;
    }

    public Tinman(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}

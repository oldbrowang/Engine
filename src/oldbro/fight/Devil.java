package oldbro.fight;

class Devil extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/devil.gif";
        monsterWidth = 100;
        monsterHeight = 140;
        upLeftOffsetX = 50;
        upLeftOffsetY = 50;
        gifCycleDistance = 20;
        gifOriginalDirection = 1;
    }

    public Devil(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}
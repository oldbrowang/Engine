package oldbro.fight;

class Phantom extends Monster
{
    static String monsterGif;

    static int monsterWidth;

    static int monsterHeight;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int gifCycleDistance;

    static int gifOriginalDirection;

    static {
        monsterGif = "resource/phantom.gif";
        monsterWidth = 180;
        monsterHeight = 240;
        upLeftOffsetX = 90;
        upLeftOffsetY = 100;
        gifCycleDistance = 50;
        gifOriginalDirection = 1;
    }

    public Phantom(int centerX, int centerY, int speed, boolean isFree)
    {
        super(monsterGif, monsterWidth, monsterHeight, upLeftOffsetX, upLeftOffsetY, gifCycleDistance, gifOriginalDirection, centerX, centerY, speed, isFree);
    }
}

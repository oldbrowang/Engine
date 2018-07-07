package oldbro.fight;

class MonsterDeployment extends Deployment
{
    String monsterClassName;

    int startX;

    int startY;

    int speed;

    boolean isFree;

    MonsterDeployment(String monsterClassName, int startX, int startY, int speed, boolean isFree)
    {
        this.monsterClassName = monsterClassName;
        this.startX = startX;
        this.startY = startY;
        this.speed = speed;
        this.isFree = isFree;
    }
}

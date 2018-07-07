package oldbro;

public class ComponentPosition
{
    public double x;

    public double y;

    public ComponentPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public int calculateXDirection(ComponentPosition destination)
    {
        if (destination.x > x) {
            return 1;
        } else if (destination.x < x) {
            return -1;
        } else {
            return 0;
        }
    }

    public double getDistance(ComponentPosition destination)
    {
        return Math.sqrt(Math.pow((destination.x - x), 2) + Math.pow((destination.y - y), 2));
    }

    public double caculateDirctionFactorX(ComponentPosition destination)
    {
        return (double) (destination.x - x) / getDistance(destination);
    }

    public double caculateDirctionFactorY(ComponentPosition destination)
    {
        return (double) (destination.y - y) / getDistance(destination);
    }
}

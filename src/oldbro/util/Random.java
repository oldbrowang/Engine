package oldbro.util;

public class Random
{
    public static int getDirection()
    {
        if (Math.random() > 0.5) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int getRandomInteger(int from, int to)
    {
        return (int) (Math.random() * (to - from + 1)) + from;
    }

    public static double getRandomFloat(double from, double to)
    {
        String fromStr = from + "";
        String toStr = to + "";
        int fromFractionLength = fromStr.length() - fromStr.indexOf(".") - 1;
        int toFractionLength = toStr.length() - toStr.indexOf(".") - 1;
        int scale = Math.max(fromFractionLength, toFractionLength);
        int factor = (int) Math.pow(10, scale);
        return (double) getRandomInteger((int) (from * factor), (int) (to * factor)) / factor;
    }
}

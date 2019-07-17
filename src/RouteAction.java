public class RouteAction
{
    // North == 0, rotation == clockwise
    public int Direction = 0;

    public int MovementVelocity = 0;

    public boolean IsStanding()
    {
        return MovementVelocity == 0;
    }
}
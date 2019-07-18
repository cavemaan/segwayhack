public class MovementAction
{
    public static final MovementAction NoAction = new MovementAction(0, 0);

    // North == 0, rotation == clockwise
    public final double direction;

    public final int movementVelocity;

    public MovementAction(double direction, int movementVelocity)
    {
        this.direction = direction;
        this.movementVelocity = movementVelocity;
    }

    public boolean IsStanding()
    {
        return this.movementVelocity == 0;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == this) return true;

        MovementAction ra = (MovementAction) obj;
        if (ra.movementVelocity == this.movementVelocity && Math.abs(ra.direction - this.direction) < 0.01d) return true;
        
        return false;   
    }
}
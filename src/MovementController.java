import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class MovementController implements IPositionListener
{
    private static final double EPSILON = 0.01d;

    private Point2D.Double targetPosition;
    private Point2D.Double currentPosition;
    private double currentDirection = 0;
    
    public MovementController(Point2D.Double targetPosition)
    {
        this.targetPosition = targetPosition;
    }

    public MovementAction NextAction(MovementAction currentAction)
    {
        if (currentPosition == null || computeDistance(this.currentPosition, this.targetPosition) < 0.00001d)
        {
            // location not reported yet
            return MovementAction.NoAction;
        }

        double targetDirection = computeDirection(this.currentPosition, this.targetPosition);
        return new MovementAction(targetDirection, this.currentDirection < EPSILON ? 1 : 0);
    }

    public void OnNewPosition(Point2D.Double point)
    {
        if (this.currentPosition.equals(point))
        {
            return;
        }

        if (this.currentPosition != null)
        {
            this.currentDirection = computeDirection(this.currentPosition, point);
        }

        this.currentPosition = point;
    }

    private static double computeDistance(Point2D.Double point1, Point2D.Double point2)
    {
        double x1 = point1.x;
        double x2 = point2.x;
        double y1 = point1.y;
        double y2 = point2.y;
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    private static double computeDirection(Point2D.Double point1, Point2D.Double point2)
    {
        double xDiff = point2.x - point1.x;
        double yDiff = point2.y - point1.y;
        return Math.atan2(yDiff, xDiff) * 180.0 / Math.PI;
    }
}
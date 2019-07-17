public interface IRouteFinder
{
    public RouteAction NextAction(Route route, RouteAction currentAction, RoutePosition currentPosition);
}
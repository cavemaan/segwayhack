import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import com.google.gson.*;

public class RouteFinder {
    private static String bingMapsApiKey = "<TODO_GET_FROM_ENV>";

    private static String routeCalcApiFormat = "http://dev.virtualearth.net/REST/v1/Routes/Walking?wayPoint.0={currentPosition}&wayPoint.1={targetPosition}&routeAttributes=routePath&distanceUnit=mi&key={bingApiKey}";

    private RouteFinder() {
    }

    public static List<Point2D.Double> findRoute(Point2D.Double currentLocation, Point2D.Double targetLocation) {
        String url = routeCalcApiFormat.replace("{currentPosition}", currentLocation.x + "," + currentLocation.y)
                .replace("{targetPosition}", targetLocation.x + "," + targetLocation.y)
                .replace("{bingApiKey}", bingMapsApiKey);

        String jsonResp = new NetRequest(url).getResult();
        return parseResponse(jsonResp);
    }

    private static List<Point2D.Double> parseResponse(String jsonResp) {
        if (jsonResp == null)
        {
            return new LinkedList<Point2D.Double>();
        }

        LinkedList<Point2D.Double> result = new LinkedList<Point2D.Double>();
        JsonObject jsonObject = new JsonParser().parse(jsonResp).getAsJsonObject();
        JsonArray resourceSets = jsonObject.get("resourceSets").getAsJsonArray();
        JsonArray resources = resourceSets.get(0).getAsJsonObject().get("resources").getAsJsonArray();
        JsonObject routePath = resources.get(0).getAsJsonObject().get("routePath").getAsJsonObject();
        JsonObject line = routePath.get("line").getAsJsonObject();
        JsonArray coordinates = line.get("coordinates").getAsJsonArray();

        for (int x = 0; x < coordinates.size(); x++)
        {
            JsonArray coordinate = coordinates.get(x).getAsJsonArray();
            Point2D.Double point = new Point2D.Double();
            point.x = coordinate.get(0).getAsDouble();
            point.y = coordinate.get(0).getAsDouble();

            result.add(point);
        }

        return result;
    }
}
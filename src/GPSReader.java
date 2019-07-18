import java.awt.geom.Point2D;
import java.time.Duration;

public class GPSReader implements Runnable {
    private Duration refreshRate;
    private IPositionListener onRead;

    public GPSReader(IPositionListener onRead) {
        this(Duration.ofSeconds(5), onRead);
    }

    public GPSReader(Duration refreshRate, IPositionListener onRead) {
        this.refreshRate = refreshRate;
        this.onRead = onRead;

        // Ask for permission of USB accessory read
    }

    @Override
    public void run() {
        do {

            try
            {
                Thread.sleep(this.refreshRate.toMillis());

                // Read bytes from accessory
                Point2D.Double reading = new Point2D.Double(0, 0);
                
                this.onRead.OnNewPosition(reading);
            }
            catch (InterruptedException e)
            {
                // do nothing
            }
        } while (true);
    }
}
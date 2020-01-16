namespace TrafficModel
{
    public class CarContext
    {
        public CarContext(RoadWay roadWay, long roadWayDistance) //, long forwardDistance)
        {
            RoadWay = roadWay;
            RoadWayDistance = roadWayDistance;
            //ForwardDistance = forwardDistance;
        }
        public RoadWay RoadWay { get; }
        public long RoadWayDistance { get; }
        //public long ForwardDistance { get; }
    }
}

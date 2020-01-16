using System.Collections.Generic;

namespace TrafficModel.Interfaces
{
    public interface IRoadWayLocation
    {
        RoadWay RoadWay { get; }
        IDictionary<long, Car> Cars { get; }
    }
}
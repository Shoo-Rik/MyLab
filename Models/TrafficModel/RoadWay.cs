using System;
using System.Collections.Generic;
using System.Linq;
using TrafficModel.Interfaces;

namespace TrafficModel
{
    public class RoadWay : IValidate
    {
        public RoadWay(long length)
        {
            Id = Guid.NewGuid();
            Distance = length;
            CrossRoadWays = new Dictionary<long, RoadWay>();

            if (!IsValid())
            {
                throw new ArgumentOutOfRangeException();
            }
        }

        public Guid Id { get; }
        public long Distance { get; }
        /// <summary>
        /// There is (temporal) constraint - one (cross) road per one point of the road
        /// </summary>
        public IDictionary<long, RoadWay> CrossRoadWays { get; }

        public bool IsValid()
        {
            return (Distance > 0) 
                   && CrossRoadWays.Keys.All(key => (key >= 0) && (key <= Distance)) 
                   && CrossRoadWays.Values.All(r => (r != null) && (r.Id != Id));
        }
    }
}

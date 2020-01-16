using System;
using System.Collections.Generic;
using System.Linq;
using TrafficModel.Interfaces;

namespace TrafficModel
{
    public class RoadWayLocation : IValidate, IRoadWayLocation
    {
        public RoadWay RoadWay { get; }
        public IDictionary<long, Car> Cars { get; }

        public RoadWayLocation(RoadWay roadWay, IDictionary<long, Car> cars)
        {
            RoadWay = roadWay ?? throw new ArgumentNullException(nameof(roadWay));
            Cars = cars ?? throw new ArgumentNullException(nameof(cars));

            if (!IsValid())
            {
                throw new ArgumentOutOfRangeException();
            }
        }
        public bool IsValid()
        {
            return RoadWay.IsValid() && 
                Cars.All(item => item.Value.IsValid() && item.Key >= 0 && item.Key <= RoadWay.Distance) &&
                Cars.Keys.Count == Cars.Keys.Distinct().Count();
        }

        public void AddCarToBeginning(Car car)
        {
            if (!car.IsValid())
            {
                throw new ArgumentException("Car is not valid");
            }
            if (!Cars.TryGetValue(0, out _))
            {
                throw new InvalidOperationException("Cannot add car to beginning");
            }
            Cars.Add(0, car);
        }
    }
}